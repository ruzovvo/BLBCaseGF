package org.blbcase.core.managers;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.blbcase.core.enums.UserTypeEnum;
import org.blbcase.core.exceptions.BLBException;
import org.blbcase.core.jpa.entity.User;

/**
 *
 * @author postman
 */
@Stateless
public class UserManager implements UserManagerLocal {
    
    public static final String ADMIN_PASSWORD = "postmanmipt";
    @PersistenceContext(unitName = "BLBCaseCorePU")
    EntityManager em;

    /**
     * You can customize this method
     *
     * @param l
     * @throws BLBException
     */
    private void assertLogin(String l) throws BLBException {
        if (l == null || l.isEmpty()) {
            throw new BLBException("invalid login");
        }
    }

    /**
     * You can customize this method
     *
     * @param l
     * @throws BLBException
     */
    private void assertPassword(String l) throws BLBException {
        if (l == null || l.isEmpty()) {
            throw new BLBException("invalid password");
        }
    }
    
    @Override
    public boolean checkLoginData(String login, String password) throws BLBException {
        assertLogin(login);
        assertPassword(password);
        System.out.println("check auth data");

        Query q = em.createQuery("select u from  User u where u.login = :login and u.password = :pass").setParameter("login", login).setParameter("pass", password);
        List<User> list = q.getResultList();
        if (list == null || list.isEmpty()) {
            return false;
        }
        return true;
        
    }
    
    @Override
    public boolean userExists(String login) throws BLBException {
        Query q = em.createQuery("select u from User u where u.login = :login").setParameter("login", login);
        List<User> list = q.getResultList();
        if (list == null || list.isEmpty()) {
            return false;
        }
        return true;
    }
    
    @Override
    public User createUser(String login, String password, UserTypeEnum type) throws BLBException {
        assertLogin(login);
        assertPassword(password);
        if (userExists(login)) {
            throw new BLBException("user with login '" + login + "' already exists in the system");
        }
        User u = new User(login, password, type);
        return em.merge(u);
    }
    
    @Override
    public User createUser(User u) throws BLBException {
        if (u == null) {
            return null;
        }
        return createUser(u.getLogin(), u.getPassword(), u.getType());
    }
    
    @Override
    public void deleteUser(Long id) throws BLBException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public User getUserById(Long id) {
        if (id == null) {
            return null;
        }
        return em.find(User.class, id);
    }
    
    @Override
    public User getUserByLogin(String l) {
        Query q = em.createQuery("select u from User u where u.login = :login").setParameter("login", l);
        List<User> list = q.getResultList();
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
    
    private boolean userExists(String login, UserTypeEnum type) {
        User u = getUserByLogin(login);
        if (u == null || !u.getType().equals(type)) {
            return false;
        }
        return true;
    }
    
    @Override
    public User createClient(String clientId, Double balance) throws BLBException {
        if (clientExists(clientId)) {
            throw new BLBException("client with clientId '" + clientId + "' already exists in the system");
        }
        User u = new User(clientId, null, UserTypeEnum.CLIENT);
        u.setBalance(balance);
        return em.merge(u);
    }
    
    @Override
    public boolean clientExists(String clientId) {
        return userExists(clientId, UserTypeEnum.CLIENT);
    }
    
    @Override
    public boolean traderExists(String login) {
        return userExists(login, UserTypeEnum.TRADER);
    }
    
    @Override
    public User createTrader(String login, String password) throws BLBException {
        if (traderExists(login)) {
            throw new BLBException("trader with login '" + login + "' already exists in the system");
        }
        User u = new User(login, password, UserTypeEnum.TRADER);
        return em.merge(u);
    }
    
    private List<User> getUsersByType(UserTypeEnum type) {
        Query q = em.createQuery("select u from User u where u.type = :tp").setParameter("tp", type);
        List<User> list = q.getResultList();
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list;
    }
    
    @Override
    public List<User> getAllTraders() {
        return getUsersByType(UserTypeEnum.TRADER);
    }
    
    @Override
    public List<User> getAllClients() {
        return getUsersByType(UserTypeEnum.CLIENT);
    }
    
    @Override
    public void replenish(Double money, Long clientId) throws BLBException {
        User client = getUserById(clientId);
        if (client == null) {
            throw new BLBException("clientId is incorrect");
        }
        client.setBalance(client.getBalance() + money);
        em.merge(client);
    }
    
    @Override
    public void withdraw(Double money, Long clientId) throws BLBException {
        User client = getUserById(clientId);
        if (client == null) {
            throw new BLBException("clientId is incorrect");
        }
        if (client.getBalance().compareTo(money) < 0) {
            throw new BLBException("not enough money");
        }
        client.setBalance(client.getBalance() - money);
        em.merge(client);
    }
}
