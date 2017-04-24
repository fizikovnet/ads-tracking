package ads_tracking.DAO;


import ads_tracking.Entity.Url;
import ads_tracking.Model.Ads;

import java.util.List;

/**
 * This interface inherit from {@link CommonDAOInterface}, parameterized him {@link Url} objects and added specific methods
 */
public interface AdsDAO extends CommonDAOInterface<Ads.Ad> {

    List<Ads.Ad> getAdsByUrlId(int url_id);

    void setSendFlagForAds(List<Ads.Ad> ads);

    void deleteAdsByUrl(Url url);

}
