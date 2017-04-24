package ads_tracking.DAO.OracleDAO;

import ads_tracking.DAO.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Locale;

/**
 * Factory for Oracle DataBase
 */
@Repository
public class OracleFactory extends DAOFactory {


    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public UserDAO getUserDAO() {
        return new OracleUser(jdbcTemplate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BidDAO getBidDAO() {
        return new OracleBid(jdbcTemplate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemDAO getItemDAO() {
        return new OracleItem(jdbcTemplate);
    }

    public AdsDAO getAdsDAO() {
        return new OracleAds(jdbcTemplate);
    }

    @Override
    public UrlDAO getUrlDAO() {
        return new OracleUrl(jdbcTemplate);
    }
}
