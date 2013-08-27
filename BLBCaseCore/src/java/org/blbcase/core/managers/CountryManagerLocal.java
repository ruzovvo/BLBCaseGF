/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.blbcase.core.managers;

import java.util.List;
import javax.ejb.Local;
import org.blbcase.core.jpa.entity.Country;
import org.blbcase.core.jpa.entity.Holiday;

/**
 *
 * @author postman
 */
@Local
public interface CountryManagerLocal {
    
    public void createCountry();
    public List<Holiday> getHolidaysForCountry(Long id);
    public List<Country> getCountries();
    public Country getCountryById(Long id);
}
