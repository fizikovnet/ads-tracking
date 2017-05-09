package ads_tracking.Controller;

import ads_tracking.DAO.PostgreDAO.DAOFactory;
import ads_tracking.Entity.Ad;
import ads_tracking.Entity.Url;
import ads_tracking.Entity.User;
import ads_tracking.Exception.DAOException;
import ads_tracking.Services.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
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
        List<Url> outputUrlList = new ArrayList<>();
        try {
            urlList = urlService.handleUrls();
            outputUrlList.addAll(urlList);

        } catch (DAOException e) {
            e.printStackTrace();
        }

        return new ModelAndView("url-list", "urlList", outputUrlList);
    }

    @RequestMapping(value = "/user", params = {"id"})
    public ModelAndView showUserStats(HttpSession session, @RequestParam(value = "id") int id) {
        Map<String, Object> model = new HashMap<>();
        User user = null;
        Url url = null;
        List<Ad> ads = new ArrayList<>();
        try {
            user = daoFactory.getUserDAO().getById(id);
            url = daoFactory.getUrlDAO().getUrlByUserId(user.getId());
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
