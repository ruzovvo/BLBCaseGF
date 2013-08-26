package org.blbcase.web.beans;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.blbcase.core.jpa.entity.User;
import org.blbcase.core.managers.UserManagerLocal;
import org.blbcase.web.utils.JSFHelper;

/**
 *
 * @author Ruzov Vasilii (email: ruzov.vo@gmail.com)
 */
@ManagedBean
@ViewScoped
public class TraderBean {

    @EJB
    UserManagerLocal userMan;
    private User trader;

    @PostConstruct
    private void init() {
        Long id = (new JSFHelper()).getUserId();
        this.trader = userMan.getUserById(id);
    }

    public User getTrader() {
        return trader;
    }
}
