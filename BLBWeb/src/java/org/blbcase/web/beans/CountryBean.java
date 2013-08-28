package org.blbcase.web.beans;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.blbcase.core.jpa.entity.Country;
import org.blbcase.core.jpa.entity.User;
import org.blbcase.core.managers.CountryManagerLocal;
import org.blbcase.core.managers.UserManagerLocal;
import org.blbcase.web.utils.JSFHelper;

/**
 *
 * @author Ruzov Vasilii (email: ruzov.vo@gmail.com)
 */
@ManagedBean
@ViewScoped
public class CountryBean {

    @EJB
    CountryManagerLocal countryMan;
    private Country country;

    @PostConstruct
    private void init() {
        String sId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (sId == null) {
            (new JSFHelper()).redirect("index");
            return;
        }
        Long id = Long.parseLong(sId);
        this.country = countryMan.getCountryById(id);
    }

    public Country getCountry() {
        return country;
    }
}
