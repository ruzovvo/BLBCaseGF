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
 * @author rogvold
 */
@Stateless
public class UserManager implements UserManagerLocal {

    @PersistenceContext(unitName = "BLBCaseCorePU")
    EntityManager em;

    /**
     * You can customize this method
     * @param l
     * @throws BLBException 
     */
    private void assertLogin(String l) throws BLBException{
        if (l == null || l.isEmpty()){
            throw new BLBException("invalid login");
        }
    }
    
    /**
     * You can customize this method
     * @param l
     * @throws BLBException 
     */
    private void assertPassword(String l) throws BLBException{
        if (l == null || l.isEmpty()){
            throw new BLBException("invalid password");
        }
    }
    
    
    @Override
    public boolean checkLoginData(String login, String password) throws BLBException {
        assertLogin(login);
        assertPassword(password);
        Query q = em.createQuery("select User u where u.login = :login and u.password = :pass").setParameter("login", login).setParameter("pass", password);
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
        if (userExists(login)){
            throw new BLBException("user with login '" + login +"' already exists in the system");
        }
        User u = new User(login, password, type);
        return em.merge(u);
    }

    @Override
    public void deleteUser(Long id) throws BLBException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public User getUserById(Long id) {
        if (id == null){
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
}
