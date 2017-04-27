package ads_tracking.DAO;


import ads_tracking.Entity.Url;

import java.util.List;

/**
 * This interface inherit from {@link CommonDAOInterface}, parameterized him {@link Url} objects and added specific methods
 */
public interface UrlDAO extends CommonDAOInterface<Url> {

    Url getUrlByLogin(int userId);

    List<Url> getActiveUrls();

}
