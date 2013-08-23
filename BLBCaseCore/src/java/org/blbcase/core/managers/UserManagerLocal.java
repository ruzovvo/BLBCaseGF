package org.blbcase.core.managers;

import java.util.List;
import javax.ejb.Local;
import org.blbcase.core.enums.UserTypeEnum;
import org.blbcase.core.exceptions.BLBException;
import org.blbcase.core.jpa.entity.User;

/**
 *
 * @author rogvold
 */
@Local
public interface UserManagerLocal {

    public boolean checkLoginData(String login, String password) throws BLBException;

    public boolean userExists(String login) throws BLBException;

    public User createUser(String login, String password, UserTypeEnum type) throws BLBException;

    public User createUser(User u) throws BLBException;

    public void deleteUser(Long id) throws BLBException;

    public User getUserById(Long id);

    public User getUserByLogin(String l);

    public User createClient(String clientId, Double balance) throws BLBException;

    public boolean clientExists(String clientId);

    public boolean traderExists(String login);

    public User createTrader(String login, String password) throws BLBException;

    public List<User> getAllTraders();

    public List<User> getAllClients();

    public void replenish(Double money, Long clientId) throws BLBException;

    public void withdraw(Double money, Long clientId) throws BLBException;
}
