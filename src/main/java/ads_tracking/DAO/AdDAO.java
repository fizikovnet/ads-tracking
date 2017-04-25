package ads_tracking.DAO;

import ads_tracking.Entity.Ad;
import ads_tracking.Entity.Url;

import java.util.List;

public interface AdDAO extends CommonDAOInterface<Ad> {

    List<Ad> getAdsByUrlId(int url_id);

    void setSendFlagForAds(List<Ad> ads);

    void deleteAdsByUrl(Url url);

}
