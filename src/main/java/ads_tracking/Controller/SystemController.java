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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ads_tracking.Services.UrlService.STATUS;

@Controller
public class SystemController {

    @Autowired
    private DAOFactory daoFactory;
    @Autowired
    private UrlService urlService;

    @RequestMapping(value = "/system")
    public ModelAndView system() {
        Map<String, Object> model = new HashMap<>();
        try {
            int urlCount = daoFactory.getUrlDAO().getAll().size();
            int adsCount = daoFactory.getAdsDAO().getAll().size();
            List<User> userList = daoFactory.getUserDAO().getAll();
            model.put("urlCount", urlCount);
            model.put("adsCount", adsCount);
            model.put("status", STATUS.get());
            model.put("users", userList);
        } catch (DAOException e) {
            e.printStackTrace();
        }

        return new ModelAndView("system", "sys", model);
    }

    @RequestMapping(value = "/system/pause", method = RequestMethod.GET)
    public String pauseSystem(HttpServletRequest request) {
        STATUS.set(false);

        return "redirect:/system";
    }

    @RequestMapping(value = "/system/launch", method = RequestMethod.GET)
    public String launchSystem(HttpServletRequest request) {
        STATUS.set(true);

        return "redirect:/system";
    }

}
