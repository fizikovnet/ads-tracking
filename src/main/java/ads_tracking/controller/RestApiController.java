package ads_tracking.controller;

import ads_tracking.DAO.OracleDAO.DAOFactory;
import ads_tracking.DAO.UserDAO;
import ads_tracking.Entity.Ad;
import ads_tracking.Entity.Url;
import ads_tracking.Entity.User;
import ads_tracking.Exception.DAOException;
import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
public class RestApiController {

    @Autowired
    private DAOFactory daoFactory;

    @RequestMapping(value = "/api/login", method = RequestMethod.POST)
    public @ResponseBody Credential login(HttpServletRequest request, HttpServletResponse response) {
        Credential credential = new Credential();
        UserDAO userDAO = daoFactory.getUserDAO();
        String login = request.getParameter("email");
        String password = request.getParameter("password");
        User user = null;
        try {
            user = userDAO.getUserByLogin(login);
            if (user != null) {
                if (user.getPassword().equals(Hashing.sha256().hashString(password, Charsets.UTF_8).toString())) {
                    credential.username = user.getFullName();
                    credential.userId = user.getId();
                } else {
                    credential.error = true;
                    credential.error_msg = "Password error";
                }
            } else {
                credential.error_msg = "User with specified login is not exist";
                credential.error = true;
            }
        } catch (DAOException e) {
            e.printStackTrace();
        }

        return credential;
    }

    @RequestMapping(value = "/api/register", method = RequestMethod.POST)
    public @ResponseBody Credential register(HttpServletRequest request, HttpServletResponse response) {
        Credential credential = new Credential();
        UserDAO userDAO = daoFactory.getUserDAO();
        String name = request.getParameter("name");
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        try {
            User user = new User();
            user.setFullName(name);
            user.setLogin(login);
            user.setPassword(Hashing.sha256().hashString(password, Charsets.UTF_8).toString());
            userDAO.create(user);
            User existUser = daoFactory.getUserDAO().getUserByLogin(user.getLogin());
            if (existUser != null) {
                credential.username = existUser.getFullName();
                credential.userId = existUser.getId();
            } else {
                credential.error_msg = "Unspecified error";
                credential.error = true;
            }
        } catch (DAOException e) {
            e.printStackTrace();
        }

        return credential;
    }

    @RequestMapping(value = "/api/uri", method = RequestMethod.POST)
    public @ResponseBody Credential uri(HttpServletRequest request, HttpServletResponse response) throws DAOException {
        Credential credential = new Credential();
        UserDAO userDAO = daoFactory.getUserDAO();
        String userId = request.getParameter("user_id");
        User user = userDAO.getById(Integer.valueOf(userId));
        Url url = daoFactory.getUrlDAO().getUrlByLogin(user.getId());
        credential.uri = url.getUrl();
        credential.act_uri = url.isActive() ? "true" : "false";

        return credential;
    }

    @RequestMapping(value = "/api/update-uri", method = RequestMethod.POST)
    public @ResponseBody Credential updateUri(HttpServletRequest request, HttpServletResponse response) throws DAOException {
        Credential credential = new Credential();
        UserDAO userDAO = daoFactory.getUserDAO();
        String userId = request.getParameter("user_id");
        String active = request.getParameter("active");
        String uri = request.getParameter("url");
        User user = userDAO.getById(Integer.valueOf(userId));
        Url url = daoFactory.getUrlDAO().getUrlByLogin(user.getId());
        url.setActive((active.equals("true")) ? true : false);
        url.setUrl(uri);
        daoFactory.getUrlDAO().update(url);

        return credential;
    }

    @RequestMapping(value = "/api/ads", method = RequestMethod.POST)
    public @ResponseBody List<Ad> getItems(HttpServletRequest request, HttpServletResponse response) {
        final List<Ad> ads = new ArrayList<>();
        try {
            User user = daoFactory.getUserDAO().getById(Integer.valueOf(request.getParameter("user_id")));
            ads.addAll(daoFactory.getAdsDAO().getAdsByUrlId(daoFactory.getUrlDAO().getUrlByLogin(user.getId()).getId()));
            new Thread(new Runnable() {
                @Override
                public void run() {
                    daoFactory.getAdsDAO().setSendFlagForAds(ads);
                }
            }).start();
        } catch (DAOException e) {
            e.printStackTrace();
        }

        return ads;
    }

    @RequestMapping(value = "/api/refresh-token", method = RequestMethod.POST)
    public void refreshTokenId(HttpServletRequest request, HttpServletResponse response) {
        try {
            User user = daoFactory.getUserDAO().getById(Integer.valueOf(request.getParameter("user_id")));
            if (user != null) {
                daoFactory.getUserDAO().refreshUserToken(user.getId(), request.getParameter("token_id"));
            }
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }

    private static class Credential {
        boolean error = false;
        String username;
        String uri;
        String act_uri;
        int userId;
        String error_msg;
    }

}
