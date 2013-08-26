package org.blbcase.web.beans;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.blbcase.core.jpa.entity.Bond;
import org.blbcase.core.jpa.entity.User;
import org.blbcase.core.managers.BondManagerLocal;
import org.blbcase.core.managers.UserManagerLocal;
import org.blbcase.web.utils.JSFHelper;

/**
 *
 * @author Ruzov Vasilii (email: ruzov.vo@gmail.com)
 */
@ManagedBean
@ViewScoped
public class BondBean {

    @EJB
    BondManagerLocal bMan;
    private Bond bond;

    @PostConstruct
    private void init() {
        String sId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("bondId");
        if (sId == null) {
            (new JSFHelper()).redirect("client");
            return;
        }
        Long id = Long.parseLong(sId);
        this.bond = bMan.getBondById(id);
    }

    public Bond getBond() {
        return bond;
    }
}
