/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.blbcase.web.webservices;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import org.blbcase.core.enums.UserTypeEnum;
import org.blbcase.core.exceptions.BLBException;
import org.blbcase.core.jpa.entity.User;
import org.blbcase.core.json.additionals.BLBExceptionWrapper;
import org.blbcase.core.json.additionals.JsonResponse;
import org.blbcase.core.json.additionals.ResponseConstants;
import org.blbcase.core.json.additionals.SimpleResponseWrapper;
import org.blbcase.core.managers.BondManagerLocal;
import org.blbcase.core.managers.UserManagerLocal;
import org.blbcase.web.utils.SessionUtils;

/**
 * REST Web Service
 *
 * @author rogvold
 */
@Path("trader")
@Stateless
public class TraderResource {

    @Context
    private UriInfo context;
    @EJB
    UserManagerLocal userMan;
    @EJB
    BondManagerLocal bMan;

    /**
     * Creates a new instance of TraderResource
     */
    public TraderResource() {
    }

    @GET
    @Produces("application/json")
    @Path("checkAuthData")
    public String getClientInfo(@QueryParam("login") String login, @QueryParam("password") String password) {
        try {
            //checking rights here
            Boolean b = userMan.checkLoginData(login, password);
            if (!b) {
                throw new BLBException("incorrect pair login/password");
            }
            JsonResponse<String> jr = new JsonResponse<String>(ResponseConstants.OK, null, ResponseConstants.YES);
            return SimpleResponseWrapper.getJsonResponse(jr);
        } catch (BLBException e) {
            return BLBExceptionWrapper.wrapException(e);
        }
    }
    
    @GET
    @Produces("application/json")
    @Path("checkClientExistence")
    public String checkClientExistence(@QueryParam("id") Long id) {
        try {
            //checking rights here
            User u = userMan.getUserById(id);
            if (u == null || !u.getType().equals(UserTypeEnum.CLIENT)){
                throw new BLBException("client with specified id does not exist");
            }
            JsonResponse<String> jr = new JsonResponse<String>(ResponseConstants.OK, null, ResponseConstants.YES);
            return SimpleResponseWrapper.getJsonResponse(jr);
        } catch (BLBException e) {
            return BLBExceptionWrapper.wrapException(e);
        }
    }

    /**
     * Retrieves representation of an instance of
     * org.blbcase.web.webservices.TraderResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of TraderResource
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
}
