package ads_tracking.controller;

import ads_tracking.DAO.OracleDAO.DAOFactory;
import ads_tracking.Entity.Url;
import ads_tracking.Entity.User;
import ads_tracking.Exception.DAOException;
import ads_tracking.Model.Ads;
import ads_tracking.Model.Urls;
import ads_tracking.Services.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class IndexController {


    @Autowired
    private DAOFactory daoFactory;
    @Autowired
    private UrlService urlService;

    @RequestMapping(value = "/index")
    public ModelAndView index() {
        List<User> userList = null;
        try {
            userList = daoFactory.getUserDAO().getAll();
        } catch (DAOException e) {
            e.printStackTrace();
        }
        return new ModelAndView("index", "users", userList);
    }

    @RequestMapping(value = "/url-list")
    public ModelAndView urlList(HttpSession session, Model model) {
        List<Url> urlList;
        Urls urls = new Urls();
        try {
            urlList = urlService.handleUrls();
            urls.getUrlList().addAll(urlList);

        } catch (DAOException e) {
            e.printStackTrace();
        }

        return new ModelAndView("url-list", "urlList", urls);
    }

    @RequestMapping(value = "/get-ads", params = {"url-id"})
    public ModelAndView getAds(HttpSession session, @RequestParam(value = "url-id") int urlId) {
        Ads ads = null;
        try {
            ads = urlService.getAds(urlId);
        } catch (DAOException e) {
            e.printStackTrace();
        }

        return new ModelAndView("ads", "listAds", ads.adList);
    }

    @RequestMapping(value = "/user", params = {"id"})
    public ModelAndView showUserStats(HttpSession session, @RequestParam(value = "id") int id) {
        Map<String, Object> model = new HashMap<>();
        User user = null;
        Url url = null;
        List<Ads.Ad> ads = null;
        try {
            user = daoFactory.getUserDAO().getById(id);
            url = daoFactory.getUrlDAO().getUrlByLogin(user.getId());
            if (url != null) {
                ads = daoFactory.getAdsDAO().getAdsByUrlId(url.getId());
            }
            model.put("user", user);
            model.put("adsCount", ads.size());
            model.put("url", url);
        } catch (DAOException e) {
            e.printStackTrace();
        }

        return new ModelAndView("user-info", "model", model);
    }

}
