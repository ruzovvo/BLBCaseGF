/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.blbcase.core.managers;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.blbcase.core.jpa.entity.Country;
import org.blbcase.core.jpa.entity.Holiday;
import org.blbcase.core.jpa.entity.User;

/**
 *
 * @author postman
 */
@Stateless
public class CountryManager implements CountryManagerLocal {

    @PersistenceContext(unitName = "BLBCaseCorePU")
    EntityManager em;
    
    @Override
    public void createCountry() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Holiday> getHolidaysForCountry(Long id) {
        String query = "select h from Holiday h where h.countryId = '" + id + "'";
        Query q = em.createQuery(query);
        List<Holiday> list = q.getResultList();
        /*for(Holiday h in list)
        {
            h.
        }*/
        System.out.println("Holidays for " + id + " amount: " + list.size());
        return list;
    }

    @Override
    public List<Country> getCountries() {
        Query q = em.createQuery("select c from Country c");
        List<Country> list = q.getResultList();
        return list;
    }

    @Override
    public Country getCountryById(Long id) {
        Query q = em.createQuery("select c from Country c where c.id = :id").setParameter("id", id);
        Country country = (Country)q.getSingleResult();
        return country;
    }
    
}
