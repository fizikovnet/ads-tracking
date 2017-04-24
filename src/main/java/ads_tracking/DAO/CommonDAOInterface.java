package ads_tracking.DAO;

import ads_tracking.Entity.Identified;
import ads_tracking.Exception.DAOException;

import java.util.List;

/**
 * Common interface for all DAO classes
 *
 * @param <T> the type of entity object which working with inheritable class
 */
public interface CommonDAOInterface<T extends Identified> {

    /**
     * Returns true if successfully create a record in the database
     *
     * @param object
     * @return boolean
     * @throws DAOException
     */
    boolean create(T object) throws DAOException;

    /**
     * Returns true if successfully update a record in the database
     *
     * @param object
     * @return boolean
     * @throws DAOException
     */
    boolean update(T object) throws DAOException;

    /**
     * Returns true if successfully delete a record in the database
     *
     * @param object of type T
     * @return boolean
     * @throws DAOException
     */
    boolean delete(T object) throws DAOException;

    /**
     * Returns suitable object from database with specified id (Primary key)
     *
     * @param id
     * @return object of type T
     * @throws DAOException
     */
    T getById(int id) throws DAOException;

    /**
     * Returns list of T objects which stored in database
     *
     * @return List<T>
     * @throws DAOException
     */
    List<T> getAll() throws DAOException;

}
