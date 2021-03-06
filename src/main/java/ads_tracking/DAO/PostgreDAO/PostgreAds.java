package ads_tracking.DAO.PostgreDAO;

import ads_tracking.DAO.AdDAO;
import ads_tracking.Entity.Ad;
import ads_tracking.Entity.Url;
import ads_tracking.Exception.DAOException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class PostgreAds implements AdDAO {

    private NamedParameterJdbcTemplate jdbcTemplate;

    public PostgreAds(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String SELECT_QUERY = "SELECT * FROM ads";
    private static final String SELECT_QUERY_BY_ID = "SELECT * FROM ads WHERE id = :id";
    private static final String SELECT_QUERY_BY_URL_ID = "SELECT * FROM ads WHERE url_id = :url_id ORDER BY id ASC LIMIT 500";
    private static final String SELECT_QUERY_NOT_SEND_BY_URL_ID = "SELECT * FROM ads WHERE url_id = :url_id AND send = false ORDER BY id DESC LIMIT 50";
    private static final String CREATE_QUERY = "INSERT INTO ads (link, description, title, url_id, sid, send, img) VALUES" +
            " (:link, :description, :title, :url_id, :sid, false, :img)";
    private static final String UPDATE_SEND_FLAG_QUERY = "UPDATE ads SET send = true WHERE id = CAST(:id AS integer)";
    private static final String DELETE_QUERY = "DELETE FROM ads WHERE url_id = CAST(:url_id AS integer)";

    @Override
    public boolean create(Ad object) throws DAOException {
        System.out.println("Create AD");
        if (this.jdbcTemplate.update(CREATE_QUERY, getMapForInsert(object)) == 1) {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Created");
            return true;
        }
        return false;
    }

    @Override
    public boolean update(Ad object) throws DAOException {
        return false;
    }

    @Override
    public boolean delete(Ad object) throws DAOException {
        return false;
    }

    @Override
    public void deleteAdsByUrl(Url object) {
        this.jdbcTemplate.update(DELETE_QUERY, Collections.singletonMap("url_id", object.getId()));
    }

    @Override
    public Ad getById(int id) throws DAOException {
        List<Ad> result = jdbcTemplate.query(SELECT_QUERY_BY_ID, Collections.singletonMap("id", id), new AdsMapper());
        if (!result.isEmpty()) {
            return result.get(0);
        }
        return null;
    }

    @Override
    public List<Ad> getAdsByUrlId(int url_id) {
        return jdbcTemplate.query(SELECT_QUERY_BY_URL_ID, Collections.singletonMap("url_id", url_id), new AdsMapper());
    }

    @Override
    public List<Ad> getNotSendAdsByUrlId(int url_id) {
        return jdbcTemplate.query(SELECT_QUERY_NOT_SEND_BY_URL_ID, Collections.singletonMap("url_id", url_id), new AdsMapper());
    }

    @Override
    public void setSendFlagForAds(List<Ad> ads) {
        //ToDo переделать на batch
        for (Ad ad : ads) {
            this.jdbcTemplate.update(UPDATE_SEND_FLAG_QUERY, Collections.singletonMap("id", ad.getId()));
        }
    }

    @Override
    public List<Ad> getAll() throws DAOException {
        return jdbcTemplate.query(SELECT_QUERY, new AdsMapper());
    }

    private static final class AdsMapper implements RowMapper<Ad> {

        public Ad mapRow(ResultSet rs, int rowNum) throws SQLException {
            Ad ad = new Ad();
            ad.setId(rs.getInt("id"));
            ad.setLink(rs.getString("link"));
            ad.setTitle(rs.getString("title"));
            ad.setDescription(rs.getString("description"));
            ad.setsId(rs.getString("sid"));
            ad.setUrlId(rs.getInt("url_id"));
            ad.setSend(rs.getBoolean("send"));
            ad.setImg(rs.getString("img"));

            return ad;
        }
    }

    private Map<String, Object> getMapForInsert(Ad object) {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("link", object.getLink());
        namedParameters.put("title", object.getTitle());
        namedParameters.put("description", object.getDescription());
        namedParameters.put("url_id", object.getUrlId());
        namedParameters.put("sid", object.getsId().trim());
        namedParameters.put("img", object.getImg().trim());

        return namedParameters;
    }
}
