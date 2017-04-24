package ads_tracking.DAO.OracleDAO;

import ads_tracking.DAO.ItemDAO;
import ads_tracking.Entity.Item;
import ads_tracking.Entity.User;
import ads_tracking.Exception.DAOException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * This class working with connection from {@link OracleFactory} and implements all methods from {@link ItemDAO}
 * and {@link ads_tracking.DAO.CommonDAOInterface} for working with Item entities
 */
public class OracleItem implements ItemDAO {

    private NamedParameterJdbcTemplate jdbcTemplate;

    private static Logger logger = Logger.getLogger(OracleItem.class.getName());
    private static final String SELECT_QUERY = "SELECT * FROM Items JOIN Users ON Items.seller_id = Users.id";
    private static final String SELECT_CRITERIA_QUERY = "SELECT " +
            "Items.id, Items.TITLE, Items.SELLER_ID, Items.DESCRIPTION, Items.START_PRICE, Items.TIME_LEFT, Items.START_BIDDING_DATE, Items.BUY_IT_NOW, Items.BID_INCREMENT, " +
            "Users.ID, Users.FULL_NAME, Users.BILLING_ADDRESS, Users.LOGIN, Users.PASSWORD, " +
            "Max(Bids.BID) " +
            "FROM Items " +
            "JOIN Users ON Items.seller_id = Users.id " +
            "LEFT OUTER JOIN Bids ON Items.id = Bids.item_id";
    private static final String CREATE_QUERY = "INSERT INTO Items (seller_id, title, description, start_price, time_left, start_bidding_date, buy_it_now, bid_increment) VALUES (:seller_id, :title, :description, :start_price, :time_left, TO_DATE(:start_bidding_date, 'DD-MM-YY HH24:MI:SS', 'NLS_DATE_LANGUAGE = RUSSIAN'), :buy_it_now, :bid_increment)";
    private static final String UPDATE_QUERY = "UPDATE Items SET seller_id = :seller_id, title = :title, description = :description, start_price = :start_price, time_left = :time_left, start_bidding_date = TO_DATE(:start_bidding_date, 'DD-MM-YY HH24:MI:SS', 'NLS_DATE_LANGUAGE = RUSSIAN'), buy_it_now = :buy_it_now, bid_increment = :bid_increment WHERE id = :id";
    private static final String DELETE_QUERY = "DELETE FROM Items WHERE id = :id";
    private static final String SELECT_ITEM_BY_ID_QUERY = "SELECT * FROM Items JOIN Users ON Items.seller_id = Users.id WHERE Items.id = :itemId";
    private static final String SELECT_ITEMS_BY_SUBTITLE_QUERY = "SELECT * FROM Items JOIN Users ON Items.seller_id = Users.id WHERE LOWER(Items.title) LIKE LOWER(:title)";
    private static final String SELECT_ITEMS_BY_SELLER_QUERY = "SELECT * FROM Items JOIN Users ON Items.seller_id = Users.id WHERE Users.full_name = :fullName";

    public OracleItem(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean create(Item object) throws DAOException {
//        Locale.setDefault(Locale.ENGLISH);
        if (this.jdbcTemplate.update(CREATE_QUERY, getMapForInsert(object)) == 1) {
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean update(Item object) throws DAOException {
        if (this.jdbcTemplate.update(UPDATE_QUERY, getMapForUpdate(object)) == 1) {
            return true;
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean delete(Item object) throws DAOException {
        if (this.jdbcTemplate.update(DELETE_QUERY, Collections.singletonMap("id", object.getId())) == 1) {
            return true;
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Item> getAll() throws DAOException {
        return jdbcTemplate.query(SELECT_QUERY, new ItemMapper());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Item getById(int id) throws DAOException {
        List<Item> result = jdbcTemplate.query(SELECT_ITEM_BY_ID_QUERY, Collections.singletonMap("itemId", id), new ItemMapper());
        if (!result.isEmpty()) {
            return result.get(0);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Item> getItemsBySubTitle(String subTitle) throws DAOException {
        return jdbcTemplate.query(SELECT_ITEMS_BY_SUBTITLE_QUERY, Collections.singletonMap("title", "%"+subTitle+"%"), new ItemMapper());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Item> getItemsBySellerName(String sellerName) throws DAOException {
        return jdbcTemplate.query(SELECT_ITEMS_BY_SELLER_QUERY, Collections.singletonMap("fullName", sellerName), new ItemMapper());
    }

    @Override
    public boolean sale(Item item, User newSeller) throws DAOException {
        item.setSeller(newSeller);
        item.setStartBiddingDate(LocalDateTime.now());
        update(item);

        return true;
    }


    private static final class ItemMapper implements RowMapper<Item> {

        public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User(rs.getInt("seller_id"));
            user.setFullName(rs.getString("full_name"));
            user.setLogin(rs.getString("login"));
            user.setPassword(rs.getString("password"));
            Item item = new Item(rs.getInt("id"));
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

            return item;
        }
    }


    private Map<String, String> getMapForInsert(Item object) {
        Map<String, String> namedParameters = new HashMap<>();
        namedParameters.put("seller_id", String.valueOf(object.getSeller().getId()));
        namedParameters.put("title", object.getTitle());
        namedParameters.put("description", object.getDescription());
        namedParameters.put("start_price", String.valueOf(object.getStartPrice()));
        namedParameters.put("time_left", String.valueOf(object.getTimeLeft()));
        if (object.getStartBiddingDate() == null) {
            namedParameters.put("start_bidding_date", new SimpleDateFormat("dd-MM-yy HH:mm:ss").format(new Timestamp(System.currentTimeMillis())));
        } else {
            namedParameters.put("start_bidding_date", Timestamp.valueOf(object.getStartBiddingDate()).toString());
        }
        namedParameters.put("buy_it_now", (object.isBuyItNow()) ? "1" : "0");
        namedParameters.put("bid_increment", String.valueOf(object.getBidIncrement()));

        return namedParameters;
    }

    private Map<String, String> getMapForUpdate(Item object) {
        Map<String, String> namedParameters = new HashMap<>();
        namedParameters.put("id", String.valueOf(object.getId()));
        namedParameters.put("seller_id", String.valueOf(object.getSeller().getId()));
        namedParameters.put("title", object.getTitle());
        namedParameters.put("description", object.getDescription());
        namedParameters.put("start_price", String.valueOf(object.getStartPrice()));
        namedParameters.put("time_left", String.valueOf(object.getTimeLeft()));
        if (object.getStartBiddingDate() == null) {
            namedParameters.put("start_bidding_date", null);
        } else {
            namedParameters.put("start_bidding_date", new SimpleDateFormat("dd-MM-yy HH:mm:ss").format(Timestamp.valueOf(object.getStartBiddingDate())));
        }
        namedParameters.put("buy_it_now", (object.isBuyItNow()) ? "1" : "0");
        namedParameters.put("bid_increment", String.valueOf(object.getBidIncrement()));

        return namedParameters;
    }
}
