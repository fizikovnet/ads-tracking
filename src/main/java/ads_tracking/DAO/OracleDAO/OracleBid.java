package ads_tracking.DAO.OracleDAO;

import ads_tracking.DAO.BidDAO;
import ads_tracking.Entity.Bid;
import ads_tracking.Entity.Item;
import ads_tracking.Entity.User;
import ads_tracking.Exception.DAOException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class working with connection from {@link OracleFactory} and implements all methods from {@link BidDAO}
 * and {@link marketplace.DAO.CommonDAOInterface} for working with Bid entities
 */
public class OracleBid implements BidDAO {

    private static Logger logger = Logger.getLogger(OracleBid.class.getName());
    private static final String SELECT_QUERY = "SELECT * FROM Bids JOIN Users ON Bids.bidder_id = Users.id JOIN Items ON Bids.item_id = Items.id";
    private static final String CREATE_QUERY = "INSERT INTO Bids (id, item_id, bidder_id, bid) VALUES (:id, :item_id, :bidder_id, :bid)";
    private static final String UPDATE_QUERY = "UPDATE Bids SET item_id = :item_id, bidder_id = :bidder_id, bid = :bid WHERE id = :id";
    private static final String DELETE_QUERY = "DELETE FROM Bids WHERE id = :id";
    private static final String SELECT_BID_BY_ID_QUERY = "SELECT * FROM Bids JOIN Users ON Bids.bidder_id = Users.id JOIN Items ON Bids.item_id = Items.id WHERE Bids.id = :id";
    private static final String SELECT_BEST_BID_QUERY = "SELECT * FROM Bids JOIN Users ON Bids.bidder_id = Users.id JOIN Items ON Bids.item_id = Items.id WHERE Bids.item_id = :itemId AND Bids.bid = (SELECT MAX(bid) FROM Bids WHERE item_id = :item_id)";
    private static final String SELECT_ALL_BIDS_QUERY = "SELECT * FROM Bids JOIN Users ON Bids.bidder_id = Users.id JOIN Items ON Bids.item_id = Items.id WHERE Bids.item_id = :item_id";

    private NamedParameterJdbcTemplate jdbcTemplate;

    public OracleBid(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean create(Bid object) throws DAOException {
        if (this.jdbcTemplate.update(CREATE_QUERY, getMapForInsert(object)) == 1) {
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean update(Bid object) throws DAOException {
        if (this.jdbcTemplate.update(UPDATE_QUERY, getMapForUpdate(object)) == 1) {
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean delete(Bid object) throws DAOException {
        if (this.jdbcTemplate.update(DELETE_QUERY, Collections.singletonMap("id", object.getId())) == 1) {
            return true;
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Bid> getAll() throws DAOException {
        return jdbcTemplate.query(SELECT_QUERY, new ItemMapper());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Bid getById(int id) throws DAOException {
        return jdbcTemplate.query(SELECT_BID_BY_ID_QUERY, Collections.singletonMap("id", id), new ItemMapper()).iterator().next();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Bid getBestBidByItem(Item item) throws DAOException {
        Map<String, Integer> param = new HashMap<>();
        param.put("itemId", item.getId());
        param.put("item_id", item.getId());

        List<Bid> result = jdbcTemplate.query(SELECT_BEST_BID_QUERY, param, new ItemMapper());
        if (!result.isEmpty()) {
            return result.get(0);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Bid> getAllBidsByItem(Item item) throws DAOException {
        return jdbcTemplate.query(SELECT_ALL_BIDS_QUERY, Collections.singletonMap("item_id", item.getId()), new ItemMapper());
    }

    private static final class ItemMapper implements RowMapper<Bid> {

        public Bid mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User(rs.getInt("bidder_id"));
            user.setFullName(rs.getString("full_name"));
            user.setLogin(rs.getString("login"));
            user.setPassword(rs.getString("password"));
            Item item = new Item(rs.getInt("item_id"));
            item.setSeller(user);
            item.setTitle(rs.getString("title"));
            item.setBidIncrement(rs.getInt("bid_increment"));
            item.setBuyItNow(rs.getBoolean("buy_it_now"));
            item.setDescription(rs.getString("description"));
            if (rs.getTimestamp("start_bidding_date") != null) {
                item.setStartBiddingDate(rs.getTimestamp("start_bidding_date").toLocalDateTime());
            }
            item.setStartPrice(rs.getDouble("start_price"));
            item.setTimeLeft(rs.getInt("time_left"));
            Bid bid = new Bid(rs.getInt("id"));
            bid.setBid(rs.getDouble("bid"));
            bid.setBidder(user);
            bid.setItem(item);

            return bid;
        }
    }

    private Map<String, String> getMapForInsert(Bid object) {
        Map<String, String> namedParameters = new HashMap<>();
        namedParameters.put("id", String.valueOf(object.getId()));
        namedParameters.put("item_id", String.valueOf(object.getItem().getId()));
        namedParameters.put("bidder_id", String.valueOf(object.getBidder().getId()));
        namedParameters.put("bid", String.valueOf(object.getBid()));

        return namedParameters;
    }

    private Map<String, String> getMapForUpdate(Bid object) {
        return getMapForInsert(object);
    }
}
