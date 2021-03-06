package ads_tracking.DAO.PostgreDAO;

import ads_tracking.DAO.UrlDAO;
import ads_tracking.Entity.Url;
import ads_tracking.Exception.DAOException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostgreUrl implements UrlDAO {

    private NamedParameterJdbcTemplate jdbcTemplate;

    public PostgreUrl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String SELECT_QUERY = "SELECT * FROM urls";
    private static final String SELECT_QUERY_BY_ID = "SELECT * FROM urls WHERE id = :id";
    private static final String SELECT_QUERY_BY_USER_ID = "SELECT * FROM urls WHERE user_id = :user_id";
    private static final String UPDATE_QUERY = "UPDATE urls SET url = :url, active = CAST(:active AS boolean) WHERE id = CAST(:id AS integer)";
    private static final String DELETE_QUERY = "DELETE FROM urls WHERE id = :id";
    private static final String SELECT_QUERY_ONLY_ACTIVE_URLS = "SELECT * FROM urls WHERE active = true";
    private static final String CREATE_QUERY = "INSERT INTO urls (url, user_id, active) VALUES (:url, CAST(:user_id AS integer), CAST(:active AS boolean))";

    @Override
    public boolean create(Url object) throws DAOException {
        if (this.jdbcTemplate.update(CREATE_QUERY, getMapForInsert(object)) == 1) {
            return true;
        }

        return false;
    }

    @Override
    public boolean update(Url object) throws DAOException {
        if (this.jdbcTemplate.update(UPDATE_QUERY, getMapForUpdate(object)) == 1) {
            return true;
        }

        return false;
    }

    @Override
    public boolean delete(Url object) throws DAOException {
        if (this.jdbcTemplate.update(DELETE_QUERY, Collections.singletonMap("id", object.getId())) == 1) {
            return true;
        }

        return false;
    }

    @Override
    public Url getById(int id) throws DAOException {
        List<Url> result = jdbcTemplate.query(SELECT_QUERY_BY_ID, Collections.singletonMap("id", id), new UrlMapper());
        if (!result.isEmpty()) {
            return result.get(0);
        }
        return null;
    }

    @Override
    public Url getUrlByUserId(int userId) {
        List<Url> result = jdbcTemplate.query(SELECT_QUERY_BY_USER_ID, Collections.singletonMap("user_id", userId), new UrlMapper());
        if (!result.isEmpty()) {
            return result.get(0);
        }
        return null;
    }

    @Override
    public List<Url> getActiveUrls() {
        return jdbcTemplate.query(SELECT_QUERY_ONLY_ACTIVE_URLS, new UrlMapper());
    }

    @Override
    public List<Url> getAll() throws DAOException {
        return jdbcTemplate.query(SELECT_QUERY, new UrlMapper());
    }

    private Map<String, String> getMapForUpdate(Url object) {
        Map<String, String> namedParameters = new HashMap<>();
        namedParameters.put("url", object.getUrl());
        namedParameters.put("active", object.isActive() ? "TRUE" : "FALSE");
        namedParameters.put("id", String.valueOf(object.getId()));

        return namedParameters;
    }

    private Map<String, String> getMapForInsert(Url object) {
        Map<String, String> namedParameters = new HashMap<>();
        namedParameters.put("url", object.getUrl());
        namedParameters.put("user_id", String.valueOf(object.getUserId()));
        namedParameters.put("active", object.isActive() ? "TRUE" : "FALSE");

        return namedParameters;
    }

    private static final class UrlMapper implements RowMapper<Url> {

        public Url mapRow(ResultSet rs, int rowNum) throws SQLException {
            Url url = new Url();
            url.setId(rs.getInt("id"));
            url.setUrl(rs.getString("url"));
            url.setUserId(rs.getInt("user_id"));
            url.setActive(rs.getBoolean("active"));

            return url;
        }
    }
}
