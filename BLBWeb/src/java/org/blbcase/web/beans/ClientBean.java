package org.blbcase.web.beans;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.blbcase.core.jpa.entity.User;
import org.blbcase.core.managers.UserManagerLocal;
import org.blbcase.web.utils.JSFHelper;

/**
 *
 * @author Ruzov Vasilii (email: ruzov.vo@gmail.com)
 */
@ManagedBean
@ViewScoped
public class ClientBean {

    @EJB
    UserManagerLocal userMan;
    private User client;

    @PostConstruct
    private void init() {
        String sId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("clientId");
        if (sId == null) {
            (new JSFHelper()).redirect("index");
            return;
        }
        Long id = Long.parseLong(sId);
        this.client = userMan.getUserById(id);
    }

    public User getClient() {
        return client;
    }
}
