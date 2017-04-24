package ads_tracking.DAO;


import ads_tracking.Entity.Item;
import ads_tracking.Entity.User;
import ads_tracking.Exception.DAOException;

import java.util.List;

/**
 * This interface inherit from {@link CommonDAOInterface}, parameterized him {@link Item} objects and added specific methods
 */
public interface ItemDAO extends CommonDAOInterface<Item> {

    /**
     * Returns list Items which in {@link Item#title} field contains specified string
     *
     * @param subTitle String which contains sub title or whole title
     * @return List of Items
     * @throws DAOException
     */
    List<Item> getItemsBySubTitle(String subTitle) throws DAOException;

    /**
     * Returns list of Items which owned for {@link User} with specified seller name
     *
     * @param sellerName String with user name
     * @return List of Items
     * @throws DAOException
     */
    List<Item> getItemsBySellerName(String sellerName) throws DAOException;

    /**
     * This method setting new User for Item
     * Returns true if success finished successfully
     *
     * @param item {@link Item} which one changed owner
     * @param newSeller {@link User} which becomes owner of Item
     * @return boolean
     * @throws DAOException
     */
    boolean sale(Item item, User newSeller) throws DAOException;

}
