package org.blbcase.core.managers;

import java.util.Date;
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
 * @author postman
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
        return new Bond(b.getCUSIP(), b.getPrice(), b.getParValue(), b.getCoupon(), b.getCurrentYield(), b.getYieldToMaturity(), b.getQuantity(), b.getClientId(), b.getBoughtOn(), b.getRatingMoodys(), b.getRatingSnp(), b.getBondId(), b.getIssuer());

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
        System.out.println("Client " + clientId + " buys " + quantity +" bonds №" + b.getBondId());
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
        clientBond.setClientId(clientId);
        clientBond.setQuantity(quantity);
        clientBond.setBoughtOn(new Date());
        em.merge(clientBond);
        b.setQuantity(b.getQuantity() - quantity);
        b = em.merge(b);
        userMan.withdraw(b.getPrice() * quantity, clientId);
        //TODO: merge bonds with equal CUSIPs owned by one client
    }

    @Override
    public void sellBond(Long clientId, Long bondId, Integer quantity) throws BLBException {
        System.out.println("from sellBond");
        Bond b = getBondById(bondId);
        User client = userMan.getUserById(clientId);
        Bond freeBond = cloneBond(b);
        if (b.getQuantity().compareTo(quantity) < 0) {
            throw new BLBException("client hasn't got so many bonds");
        }
        if (b.getQuantity().compareTo(quantity) >= 0) {
            
            b.setQuantity(b.getQuantity() - quantity);
            freeBond.setQuantity(quantity);
            freeBond.setClientId(null);
            freeBond.setBoughtOn(null);
            boolean merged = mergeBonds(freeBond);
            em.merge(b);
            if (!merged)
                em.merge(freeBond);
            if(b.getQuantity() == 0)
            {
                em.remove(b);
            }
            userMan.replenish(b.getPrice() * quantity, clientId);
            //TODO add logging of all transactions
        }
    }
    
    /**
     * left parameter null if you dont want to make a search by this parameter
     * @return 
     */
    @Override
    public List<Bond> findBondsWithParameters(Double priceLow, Double priceHigh,
                Double parLow, Double parHigh, Double couponLow, Double couponHigh,
                Double cyLow, Double cyHigh, Double ytmLow, Double ytmHigh,
                String moodysLow, String moodysHigh, String snpLow, String snpHigh)
    {
        String query = "select b from Bond b where 1=1 ";
        if (priceLow != null)
            query += " AND b.price >= " + priceLow;
        if (priceHigh != null)
            query += " AND b.price <= " + priceHigh;
        if (parLow != null)
            query += " AND b.parValue >= " +parLow;
        if (parHigh != null)
            query += " AND b.parValue <= " +parHigh;
        if (couponLow != null)
            query += " AND b.coupon >= " + couponLow;
        if (couponHigh != null)
            query += " AND b.coupon <= " + couponHigh;
        if (cyLow != null)
            query += " AND b.currentYield >= " + cyLow;
        if (cyHigh != null)
            query += " AND b.currentYield <= " + cyHigh;
        if (ytmLow != null)
            query += " AND b.yieldToMaturity >= " + ytmLow;
        if (ytmHigh != null)
            query += " AND b.yieldToMaturity <= " + ytmHigh;
       
        if (moodysLow != null && moodysHigh != null)
        {
            String q = "SELECT rating FROM ratingmoodys WHERE description = '";
            Query var = em.createNativeQuery(q + moodysLow + "'");
            int moodysRatingMoodysLow = (Integer)var.getSingleResult();
            
            query += " AND b.ratingMoodys >= " + moodysRatingMoodysLow;
            
            var = em.createNativeQuery(q + moodysHigh + "'");
            int moodysRatingMoodysHigh = (Integer)var.getSingleResult();
            
            query += " AND b.ratingMoodys <= " + moodysRatingMoodysHigh;
        }
        if (snpLow != null && snpHigh != null)
        {
            String q = "SELECT rating FROM ratingsnp WHERE description = '";
            Query var = em.createNativeQuery(q + snpLow + "'");
            int snpRatingSnpLow = (Integer)var.getSingleResult();
            
            query += " AND b.ratingSnp >= " + snpRatingSnpLow;
            
            var = em.createNativeQuery(q + snpHigh + "'");
            int snpRatingSnpHigh = (Integer)var.getSingleResult();
            
            query += " AND b.ratingSnp <= " + snpRatingSnpHigh;
        }
        
        System.out.println("---===!!!!Looking for bond with parameters: " + query);
        query += " AND b.clientId is null order by b.id desc";
        List<Bond> list = null;
        list = em.createQuery(query).getResultList();
        System.out.println("---===Found " + list.size() + " bonds by parameters===---");
        return list;
    }
    
    public boolean mergeBonds(Bond bond)
    {
        List<Bond> freeBonds = getFreeBonds();
        for (Bond b : freeBonds)
        {
            if (b.canBeMerged(bond))
            {
                b.setQuantity(b.getQuantity() + bond.getQuantity());
                em.merge(b);
                return true;
            }
        }
        return false;
    }
}
