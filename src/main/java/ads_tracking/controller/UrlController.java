package ads_tracking.controller;

import ads_tracking.DAO.OracleDAO.DAOFactory;
import ads_tracking.Entity.Url;
import ads_tracking.Entity.User;
import ads_tracking.Exception.DAOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class UrlController {

    @Autowired
    private DAOFactory daoFactory;

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
        } catch (DAOException e) {
            e.printStackTrace();
        }

        return "redirect:/user?id=" + user.getId();
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

        return "redirect:/user/?id=" + user.getId();
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

        return "redirect:/user/?id=" + user.getId();
    }

}
