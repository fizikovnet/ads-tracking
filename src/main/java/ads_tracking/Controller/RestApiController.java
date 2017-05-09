package ads_tracking.Controller;

import ads_tracking.DAO.PostgreDAO.DAOFactory;
import ads_tracking.DAO.UserDAO;
import ads_tracking.Entity.Ad;
import ads_tracking.Entity.Url;
import ads_tracking.Entity.User;
import ads_tracking.Exception.DAOException;
import ads_tracking.Model.Credential;
import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    public ResponseEntity login(HttpServletRequest request, HttpServletResponse response) {
        Credential credential = new Credential();
        UserDAO userDAO = daoFactory.getUserDAO();
        String login = request.getParameter("email");
        String password = request.getParameter("password");
        User user = null;
        try {
            user = userDAO.getUserByLogin(login);
            if (user != null) {
                if (user.getPassword().equals(Hashing.sha256().hashString(password, Charsets.UTF_8).toString())) {
                    credential.setUsername(user.getFullName());
                    credential.setUserId(user.getId());
                } else {
                    credential.setError_msg("Password error");
                    credential.setError(true);
                }
            } else {
                credential.setError_msg("User with specified login is not exist");
                credential.setError(true);
            }
        } catch (DAOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity(credential, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/register", method = RequestMethod.POST)
    public ResponseEntity register(HttpServletRequest request, HttpServletResponse response) {
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
                credential.setUsername(existUser.getFullName());
                credential.setUserId(existUser.getId());
            } else {
                credential.setError_msg("Unspecified error");
                credential.setError(true);
            }
        } catch (DAOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity(credential, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/uri", method = RequestMethod.POST)
    public ResponseEntity uri(HttpServletRequest request, HttpServletResponse response) throws DAOException {
        Credential credential = new Credential();
        UserDAO userDAO = daoFactory.getUserDAO();
        String userId = request.getParameter("user_id");
        User user = userDAO.getById(Integer.valueOf(userId));
        Url url = daoFactory.getUrlDAO().getUrlByUserId(user.getId());
        credential.setUri(url.getUrl());
        credential.setAct_uri(url.isActive() ? "true" : "false");

        return new ResponseEntity(credential, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/update-uri", method = RequestMethod.POST)
    public ResponseEntity updateUri(HttpServletRequest request, HttpServletResponse response) throws DAOException {
        Credential credential = new Credential();
        UserDAO userDAO = daoFactory.getUserDAO();
        String userId = request.getParameter("user_id");
        String active = request.getParameter("active");
        String uri = request.getParameter("url");
        User user = userDAO.getById(Integer.valueOf(userId));
        Url url = daoFactory.getUrlDAO().getUrlByUserId(user.getId());
        if (url == null) {
            url = new Url();
            url.setUrl(uri);
            url.setUserId(user.getId());
            url.setActive((active.equals("true")) ? true : false);
            daoFactory.getUrlDAO().create(url);
        } else {
            url.setActive((active.equals("true")) ? true : false);
            url.setUrl(uri);
            daoFactory.getUrlDAO().update(url);
        }

        return new ResponseEntity(credential, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/ads", method = RequestMethod.POST)
    public ResponseEntity getItems(HttpServletRequest request, HttpServletResponse response) {
        final List<Ad> ads = new ArrayList<>();
        try {
            User user = daoFactory.getUserDAO().getById(Integer.valueOf(request.getParameter("user_id")));
            ads.addAll(daoFactory.getAdsDAO().getAdsByUrlId(daoFactory.getUrlDAO().getUrlByUserId(user.getId()).getId()));
            new Thread(new Runnable() {
                @Override
                public void run() {
                    daoFactory.getAdsDAO().setSendFlagForAds(ads);
                }
            }).start();
        } catch (DAOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity(ads, HttpStatus.OK);
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

}
