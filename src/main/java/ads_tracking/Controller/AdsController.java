package ads_tracking.Controller;

import ads_tracking.DAO.PostgreDAO.DAOFactory;
import ads_tracking.Entity.Ad;
import ads_tracking.Entity.Url;
import ads_tracking.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AdsController {

    @Autowired
    private DAOFactory daoFactory;

    @RequestMapping(value = "/ads")
    public ModelAndView listAds(HttpSession session) {
        List<Ad> adList = new ArrayList<>();
        User user = (User) session.getAttribute("user");
        Url url = daoFactory.getUrlDAO().getUrlByUserId(user.getId());
        adList = daoFactory.getAdsDAO().getAdsByUrlId(url.getId());

        return new ModelAndView("ads", "listAds", adList);
    }
}
