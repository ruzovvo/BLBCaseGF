package org.blbcase.core.managers;

import java.util.List;
import javax.ejb.Local;
import org.blbcase.core.exceptions.BLBException;
import org.blbcase.core.jpa.entity.Bond;

/**
 *
 * @author rogvold
 */
@Local
public interface BondManagerLocal {

    public Bond createBond(Bond bond) throws BLBException;

    /**
     * if userId is null returns bonds of government
     *
     * @param userId
     * @return
     */
    public List<Bond> getClientBonds(Long userId);
    
    public Bond getBondByCUSIP(String cusip) throws BLBException;
    
    public List<Bond> getFreeBonds();
    
    public Bond getBondById(Long bId);

    public void buyFreeBond(Long clientId, Long freeBondId, Integer quantity) throws BLBException;
    
    public void sellBond(Long clientId, Long bondId, Integer quantity) throws BLBException;
    
}
