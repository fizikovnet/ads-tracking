package ads_tracking.DAO.PostgreDAO;

import ads_tracking.DAO.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

/**
 * Factory for PostgreSQL DataBase
 */
@Repository
public class PostgreFactory extends DAOFactory {


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
        return new PostgreUser(jdbcTemplate);
    }

    public AdDAO getAdsDAO() {
        return new PostgreAds(jdbcTemplate);
    }

    @Override
    public UrlDAO getUrlDAO() {
        return new PostgreUrl(jdbcTemplate);
    }
}
