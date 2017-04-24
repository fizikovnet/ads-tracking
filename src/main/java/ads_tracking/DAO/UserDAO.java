package ads_tracking.DAO;


import ads_tracking.Entity.Url;
import ads_tracking.Entity.User;
import ads_tracking.Exception.DAOException;

/**
 * This interface inherit from {@link CommonDAOInterface}, parameterized him {@link User} objects and added specific methods
 */
public interface UserDAO extends CommonDAOInterface<User> {

    /**
     * Returns object User with specified login
     *
     * @param login String with user login
     * @return User
     * @throws ads_tracking.Exception.DAOException
     */
    User getUserByLogin(String login) throws DAOException;

    void refreshUserToken(int userId, String token);

    User getUserByUrl(Url url);

}
