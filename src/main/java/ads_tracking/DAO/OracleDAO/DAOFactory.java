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

    /**
     * Get BidDAO implementation
     *
     * @return BidDAO
     */
    public abstract BidDAO getBidDAO();

    /**
     * Get ItemDAO implementation
     *
     * @return ItemDAO
     */
    public abstract ItemDAO getItemDAO();

    public abstract UrlDAO getUrlDAO();

    public abstract AdsDAO getAdsDAO();

    /**
     * Return factory attached for concrete Data Base
     * In future possibly extended for other Data Bases
     *
     * @return DAOFactory
     */
    public static DAOFactory getDAOFactory() {
        return new OracleFactory();
    }

}
