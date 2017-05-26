package ads_tracking.DAO.PostgreDAO;

import ads_tracking.DAO.UserDAO;
import ads_tracking.Entity.Role;
import ads_tracking.Entity.Url;
import ads_tracking.Entity.User;
import ads_tracking.Exception.DAOException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * This class working with connection from {@link PostgreFactory} and implements all methods from {@link UserDAO}
 * and {@link ads_tracking.DAO.CommonDAOInterface} for working with User entities
 */
public class PostgreUser implements UserDAO {

    private static Logger logger = Logger.getLogger(PostgreUser.class.getName());
    private static final String SELECT_QUERY = "SELECT * FROM users";
    private static final String SELECT_BY_URL_QUERY = "SELECT DISTINCT * FROM users inner join urls on urls.user_id = users.id where urls.id = CAST(:url_id AS integer)";
    private static final String CREATE_QUERY = "INSERT INTO Users (name, login, password, role) VALUES (:name, :login, :password, 2)";
    private static final String UPDATE_QUERY = "UPDATE Users SET password = :password WHERE id = CAST(:id AS integer)";
    private static final String UPDATE_TOKEN_ID_QUERY = "UPDATE Users SET token_id = :token_id WHERE id = CAST(:id AS integer)";
    private static final String DELETE_QUERY = "DELETE FROM Users WHERE id = :id";
    private static final String SELECT_USER_BY_ID_QUERY = "SELECT * FROM Users WHERE Users.id = :id";
    private static final String SELECT_USER_BY_LOGIN_QUERY = "SELECT * FROM Users WHERE login = :login";

    private NamedParameterJdbcTemplate jdbcTemplate;

    public PostgreUser(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean create(User object) throws DAOException {
        if (this.jdbcTemplate.update(CREATE_QUERY, getMapForInsert(object)) == 1) {
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean update(User object) throws DAOException {
        if (this.jdbcTemplate.update(UPDATE_QUERY, getMapForUpdate(object)) == 1) {
            return true;
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean delete(User object) throws DAOException {
        if (this.jdbcTemplate.update(DELETE_QUERY, Collections.singletonMap("id", object.getId())) == 1) {
            return true;
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> getAll() throws DAOException {
        return jdbcTemplate.query(SELECT_QUERY, new UserMapper());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getById(int id) throws DAOException {
        return jdbcTemplate.query(SELECT_USER_BY_ID_QUERY, Collections.singletonMap("id", id), new UserMapper()).iterator().next();
    }

    @Override
    public User getUserByUrl(Url url) {
        return jdbcTemplate.query(SELECT_BY_URL_QUERY, Collections.singletonMap("url_id", url.getId()), new UserMapper()).iterator().next();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getUserByLogin(String login) throws DAOException {
        List<User> result = jdbcTemplate.query(SELECT_USER_BY_LOGIN_QUERY, Collections.singletonMap("login", login), new UserMapper());
        if (!result.isEmpty() && result.size() == 1) {
            return result.get(0);
        }
        return null;
    }

    @Override
    public void refreshUserToken(int userId, String token) {
        this.jdbcTemplate.update(UPDATE_TOKEN_ID_QUERY, getMapForUpdateToken(userId, token));
    }

    private static final class UserMapper implements RowMapper<User> {

        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User(rs.getInt("id"));
            user.setFullName(rs.getString("name"));
            user.setLogin(rs.getString("login"));
            user.setPassword(rs.getString("password"));
            user.setToken(rs.getString("token_id"));
            user.setRole((rs.getInt("role") != User.ADMIN_ID) ? Role.USER : Role.ADMIN);

            return user;
        }
    }

    private Map<String, String> getMapForInsert(User object) {
        Map<String, String> namedParameters = new HashMap<>();
        namedParameters.put("name", object.getFullName());
        namedParameters.put("login", object.getLogin());
        namedParameters.put("password", object.getPassword());

        return namedParameters;
    }

    private Map<String, String> getMapForUpdate(User object) {
        Map<String, String> namedParameters = new HashMap<>();
        namedParameters.put("password", object.getPassword());
        namedParameters.put("id", String.valueOf(object.getId()));

        return namedParameters;
    }

    private Map<String, String> getMapForUpdateToken(int userId, String token) {
        Map<String, String> namedParameters = new HashMap<>();
        namedParameters.put("token_id", token);
        namedParameters.put("id", String.valueOf(userId));

        return namedParameters;
    }
}
