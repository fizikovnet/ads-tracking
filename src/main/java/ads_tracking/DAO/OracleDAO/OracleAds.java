package ads_tracking.DAO.OracleDAO;

import ads_tracking.DAO.AdsDAO;
import ads_tracking.Entity.Url;
import ads_tracking.Exception.DAOException;
import ads_tracking.Model.Ads;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class OracleAds implements AdsDAO {

    private NamedParameterJdbcTemplate jdbcTemplate;

    public OracleAds(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String SELECT_QUERY = "SELECT * FROM ads";
    private static final String SELECT_QUERY_BY_ID = "SELECT * FROM ads WHERE id = :id";
    private static final String SELECT_QUERY_BY_URL_ID = "SELECT * FROM ads WHERE url_id = :url_id LIMIT 50";
    private static final String CREATE_QUERY = "INSERT INTO ads (link, description, title, url_id, sid, send) VALUES" +
            " (:link, :description, :title, :url_id, :sid, false)";
    private static final String UPDATE_SEND_FLAG_QUERY = "UPDATE ads SET send = true WHERE id = CAST(:id AS integer)";
    private static final String DELETE_QUERY = "DELETE FROM ads WHERE url_id = CAST(:url_id AS integer)";

    @Override
    public boolean create(Ads.Ad object) throws DAOException {
        if (this.jdbcTemplate.update(CREATE_QUERY, getMapForInsert(object)) == 1) {
            return true;
        }
        return false;
    }

    @Override
    public boolean update(Ads.Ad object) throws DAOException {
        return false;
    }

    @Override
    public boolean delete(Ads.Ad object) throws DAOException {
        return false;
    }

    @Override
    public void deleteAdsByUrl(Url object) {
        this.jdbcTemplate.update(DELETE_QUERY, Collections.singletonMap("url_id", object.getId()));
    }

    @Override
    public Ads.Ad getById(int id) throws DAOException {
        List<Ads.Ad> result = jdbcTemplate.query(SELECT_QUERY_BY_ID, Collections.singletonMap("id", id), new ItemMapper());
        if (!result.isEmpty()) {
            return result.get(0);
        }
        return null;
    }

    @Override
    public List<Ads.Ad> getAdsByUrlId(int url_id) {
        return jdbcTemplate.query(SELECT_QUERY_BY_URL_ID, Collections.singletonMap("url_id", url_id), new ItemMapper());
    }

    @Override
    public void setSendFlagForAds(List<Ads.Ad> ads) {
        //ToDo переделать на batch
        for (Ads.Ad ad : ads) {
            this.jdbcTemplate.update(UPDATE_SEND_FLAG_QUERY, Collections.singletonMap("id", ad.getId()));
        }
    }

    @Override
    public List<Ads.Ad> getAll() throws DAOException {
        return jdbcTemplate.query(SELECT_QUERY, new ItemMapper());
    }

    private static final class ItemMapper implements RowMapper<Ads.Ad> {

        public Ads.Ad mapRow(ResultSet rs, int rowNum) throws SQLException {
            Ads.Ad ad = new Ads.Ad();
            ad.setId(rs.getInt("id"));
            ad.setLink(rs.getString("link"));
            ad.setTitle(rs.getString("title"));
            ad.setDescription(rs.getString("description"));
            ad.setsId(rs.getString("sid"));
            ad.setUrlId(rs.getInt("url_id"));
            ad.setSend(rs.getBoolean("send"));

            return ad;
        }
    }

    private Map<String, Object> getMapForInsert(Ads.Ad object) {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("link", object.getLink());
        namedParameters.put("title", object.getTitle());
        namedParameters.put("description", object.getDescription());
        namedParameters.put("url_id", object.getUrlId());
        namedParameters.put("sid", object.getsId().trim());

        return namedParameters;
    }
}
