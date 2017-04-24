package ads_tracking.DAO;


import ads_tracking.Entity.Bid;
import ads_tracking.Entity.Item;
import ads_tracking.Exception.DAOException;

import java.util.List;

/**
 * This interface inherit from {@link CommonDAOInterface}, parameterized him {@link Bid} objects and added specific methods
 */
public interface BidDAO extends CommonDAOInterface<Bid> {

    /**
     * Returns list of Bids which owned for specified {@link Item}
     *
     * @param item {@link Item} on which searching all Bids
     * @return List<Bid>
     * @throws DAOException
     */
    List<Bid> getAllBidsByItem(Item item) throws DAOException;

    /**
     * Returns highest Bid for specified Item
     *
     * @param item {@link Item} on which searching best Bid
     * @return Bid
     * @throws DAOException
     */
    Bid getBestBidByItem(Item item) throws DAOException;

}
