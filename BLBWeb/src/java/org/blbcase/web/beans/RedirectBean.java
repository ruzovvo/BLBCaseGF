package org.blbcase.web.beans;

import java.io.IOException;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.blbcase.web.utils.JSFHelper;

/**
 *
 * @author Shaykhlislamov Sabir (email: sha-sabir@yandex.ru)
 */
@ManagedBean
public class RedirectBean {

    public static String LOGGED_IN_REDIRECT = "index.xhtml";
    public static String GUEST_REDIRECT = "login.xhtml";
    
    public void guestRedirect() throws IOException {
        JSFHelper helper = new JSFHelper();
        if (helper.getUserId() != null) {
            helper.redirect(LOGGED_IN_REDIRECT);
        }
    }
    public void loggedInRedirect() throws IOException {
        JSFHelper helper = new JSFHelper();
        if (helper.getUserId() == null) {
            helper.redirect(GUEST_REDIRECT);
        }
    }
}
