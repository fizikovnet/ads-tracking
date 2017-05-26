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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UrlController {

    @Autowired
    private DAOFactory daoFactory;

    @Autowired
    private UrlService urlService;

    @RequestMapping(value = "/url", method = RequestMethod.GET)
    public ModelAndView url(HttpSession session) {
        Map<String, Object> model = new HashMap<>();
        User user = (User) session.getAttribute("user");
        Url url = null;
        List<Ad> ads = new ArrayList<>();
        url = daoFactory.getUrlDAO().getUrlByUserId(user.getId());
        if (url != null) {
            ads = daoFactory.getAdsDAO().getAdsByUrlId(url.getId());
        }
        model.put("adsCount", ads.size());
        model.put("url", url);

        return new ModelAndView("url-info", "model", model);
    }

    @RequestMapping(value = "/validate-url", method = RequestMethod.GET)
    public String validateUrl(@RequestParam(value = "urlId") int urlId, HttpServletRequest request) {
        try {
            Url url = daoFactory.getUrlDAO().getById(urlId);
            HttpSession session = request.getSession();
            session.setAttribute("validateUrl", urlService.validateUrl(url));
        } catch (DAOException e) {
            e.printStackTrace();
        }
        return "redirect:/url";
    }

    @RequestMapping(value = "/redact-url", method = RequestMethod.GET)
    public ModelAndView showRedactUrlForm(@RequestParam(value = "id") int id) {
        Url url = null;
        try {
            url = daoFactory.getUrlDAO().getById(id);
        } catch (DAOException e) {
            e.printStackTrace();
        }

        return new ModelAndView("redactUrl", "url", url);
    }

    @RequestMapping(value = "/redact-url", method = RequestMethod.POST)
    public String redactUrlForm(HttpSession session, @Valid Url url, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "/redact-url?id=" + url.getId();
        }
        User user = null;
        try {
            user = daoFactory.getUserDAO().getUserByUrl(url);
            daoFactory.getUrlDAO().update(url);
            session.removeAttribute("validateUrl");
        } catch (DAOException e) {
            e.printStackTrace();
        }

        return "redirect:/url";
    }

    @RequestMapping(value = "/add-url", method = RequestMethod.GET)
    public ModelAndView showFormForAddUrl() {
        Url url = new Url();

        return new ModelAndView("addUrl", "url", url);
    }

    @RequestMapping(value = "/add-url", method = RequestMethod.POST)
    public String addUrl(HttpSession session, @Valid Url url, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "/add-url";
        }
        try {
            User user = (User) session.getAttribute("user");
            url.setUserId(user.getId());
            daoFactory.getUrlDAO().create(url);
        } catch (DAOException e) {
            e.printStackTrace();
        }

        return "redirect:/url";
    }

    @RequestMapping(value = "/clean-ads", method = RequestMethod.GET)
    public String cleanAdsByUrl(@RequestParam(value = "id") int id) {
        User user = null;
        try {
            Url url = daoFactory.getUrlDAO().getById(id);
            user = daoFactory.getUserDAO().getUserByUrl(url);
            daoFactory.getAdsDAO().deleteAdsByUrl(url);
        } catch (DAOException e) {
            e.printStackTrace();
        }

        return "redirect:/url";
    }

    @RequestMapping(value = "/delete-url", method = RequestMethod.GET)
    public String deleteURL(@RequestParam(value = "id") int id) {
        User user = null;
        try {
            final Url url = daoFactory.getUrlDAO().getById(id);
            user = daoFactory.getUserDAO().getUserByUrl(url);
            if (url != null) {
                daoFactory.getUrlDAO().delete(url);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        daoFactory.getAdsDAO().deleteAdsByUrl(url);
                    }
                }).start();
            }
        } catch (DAOException e) {
            e.printStackTrace();
        }

        return "redirect:/url";
    }

}
