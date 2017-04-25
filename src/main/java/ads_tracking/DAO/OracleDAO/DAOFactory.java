package ads_tracking.DAO.OracleDAO;

import ads_tracking.DAO.*;

/**
 * Common abstract class which registered get method concrete DAO
 * and contains static method for getting DAO Factory
 */
public abstract class DAOFactory {

    /**
     * Get UserDAO implementation
     *
     * @return UserDAO
     */
    public abstract UserDAO getUserDAO();

    public abstract UrlDAO getUrlDAO();

    public abstract AdDAO getAdsDAO();

    /**
     * Return factory attached for concrete Data Base
     * In future possibly extended for other Data Bases
     *
     * @return DAOFactory
     */
    public static DAOFactory getDAOFactory() {
        return new PostgreFactory();
    }

}
