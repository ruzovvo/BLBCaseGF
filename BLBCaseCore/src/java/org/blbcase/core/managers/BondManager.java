package org.blbcase.core.managers;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.blbcase.core.exceptions.BLBException;
import org.blbcase.core.jpa.entity.Bond;
import org.blbcase.core.jpa.entity.User;

/**
 *
 * @author rogvold
 */
@Stateless
public class BondManager implements BondManagerLocal {

    @PersistenceContext(unitName = "BLBCaseCorePU")
    EntityManager em;
    @EJB
    UserManagerLocal userMan;

    @Override
    public Bond createBond(Bond bond) throws BLBException {
        if (bond == null) {
            return null;
        }
        if (bond.getCUSIP() == null) {
            return em.merge(bond);
        }
        if (getBondByCUSIP(bond.getCUSIP()) == null) {
            return em.merge(bond);
        } else {
            throw new BLBException("bond with CUSIP = " + bond.getCUSIP() + " already exists in the system");
        }
    }

    @Override
    public List<Bond> getClientBonds(Long userId) {
        List<Bond> list = null;
        Query q = null;
        if (userId == null) {
            list = em.createQuery("select b from Bond b where b.clientId is null  order by b.id desc").getResultList();
        } else {
            list = em.createQuery("select b from Bond b where b.clientId = :cId order by b.id desc").setParameter("cId", userId).getResultList();
        }
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list;
    }

    private Bond cloneBond(Bond b) {
        if (b == null) {
            return null;
        }
        return new Bond(b.getCUSIP(), b.getPrice(), b.getParValue(), b.getCoupon(), b.getCurrentYield(), b.getYieldToMaturity(), b.getRating(), b.getQuantity(), b.getValue(), b.getClientId());

    }

    /**
     * WARNING: CUSIP should not be void in this method
     *
     * @param cusip
     * @return
     * @throws BLBException
     */
    @Override
    public Bond getBondByCUSIP(String cusip) {
        if (cusip == null) {
            return null;
        }
        Query q = em.createQuery("select b from Bond b where b.CUSIP = :c").setParameter("c", cusip);
        List<Bond> list = q.getResultList();
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<Bond> getFreeBonds() {
        return getClientBonds(null);
    }

    @Override
    public Bond getBondById(Long bId) {
        if (bId == null) {
            return null;
        }
        return em.find(Bond.class, bId);
    }

    @Override
    public void buyFreeBond(Long clientId, Long freeBondId, Integer quantity) throws BLBException {
        Bond b = getBondById(freeBondId);
        if (b.getClientId() != null) {
            throw new BLBException("access to this bond denied");
        }
        if (b.getQuantity().compareTo(quantity) < 0) {
            throw new BLBException("not enough bonds");
        }
        if (b.getQuantity().compareTo(quantity) == 0) {
            userMan.withdraw(b.getPrice() * quantity, clientId);
            b.setClientId(clientId);
            em.remove(b);
            return;
        }
        Bond clientBond = cloneBond(b);
        clientBond.setQuantity(quantity);
        em.merge(clientBond);
        b.setQuantity(b.getQuantity() - quantity);
        b = em.merge(b);
        userMan.withdraw(b.getPrice() * quantity, clientId);
        //TODO: merge bonds with equal CUSIPs owned by one client
    }

    @Override
    public void sellBond(Long clientId, Long bondId, Integer quantity) throws BLBException {
        Bond b = getBondById(bondId);
        if (b.getQuantity().compareTo(quantity) < 0) {
            throw new BLBException("client has't got so many bonds");
        }
        if (b.getQuantity().compareTo(quantity) > 0) {
            b.setQuantity(b.getQuantity() - quantity);
            em.merge(b);
            userMan.replenish(b.getPrice() * quantity, clientId);
        }
        userMan.replenish(b.getPrice() * quantity, clientId);
        em.remove(b);
    }
}
