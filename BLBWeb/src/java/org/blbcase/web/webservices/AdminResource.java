/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.blbcase.web.webservices;

import com.google.gson.Gson;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import org.blbcase.core.exceptions.BLBException;
import org.blbcase.core.jpa.entity.Bond;
import org.blbcase.core.jpa.entity.Country;
import org.blbcase.core.jpa.entity.Holiday;
import org.blbcase.core.jpa.entity.User;
import org.blbcase.core.json.additionals.BLBExceptionWrapper;
import org.blbcase.core.json.additionals.JsonResponse;
import org.blbcase.core.json.additionals.ResponseConstants;
import org.blbcase.core.json.additionals.SimpleResponseWrapper;
import org.blbcase.core.managers.BondManagerLocal;
import org.blbcase.core.managers.CountryManagerLocal;
import org.blbcase.core.managers.UserManagerLocal;
import org.blbcase.web.utils.SessionUtils;

/**
 * REST Web Service
 *
 * @author postman
 */
@Path("admin")
@Stateless
public class AdminResource {

    @Context
    private UriInfo context;
    @EJB
    UserManagerLocal userMan;
    @EJB
    BondManagerLocal bMan;
    @EJB
    CountryManagerLocal counMan;

    /**
     * Creates a new instance of AdminResource
     */
    @GET
    @Produces("application/json")
    @Path("getAllTraders")
    public String getAllTraders() {
//        try {
        List<User> list = userMan.getAllTraders();
        JsonResponse<List<User>> jr = new JsonResponse<List<User>>(ResponseConstants.OK, null, list);
        return SimpleResponseWrapper.getJsonResponse(jr);
//        } catch (BLBException e) {
//            return BLBExceptionWrapper.wrapException(e);
//        }
    }

    @GET
    @Produces("application/json")
    @Path("getAllClients")
    public String getAllClients() {
//        try {
        List<User> list = userMan.getAllClients();
        JsonResponse<List<User>> jr = new JsonResponse<List<User>>(ResponseConstants.OK, null, list);
        return SimpleResponseWrapper.getJsonResponse(jr);
//        } catch (BLBException e) {
//            return BLBExceptionWrapper.wrapException(e);
//        }
    }

    @GET
    @Produces("application/json")
    @Path("createTrader")
    public String createTrader(@QueryParam("login") String login, @QueryParam("password") String password) {
        try {
            User u = userMan.createTrader(login, password);
            JsonResponse<User> jr = new JsonResponse<User>(ResponseConstants.OK, null, u);
            return SimpleResponseWrapper.getJsonResponse(jr);
        } catch (BLBException e) {
            return BLBExceptionWrapper.wrapException(e);
        }
    }

    @GET
    @Produces("application/json")
    @Path("createClient")
    public String createTrader(@QueryParam("login") String login, @QueryParam("balance") Double balance) {
        try {
            User u = userMan.createClient(login, balance);
            JsonResponse<User> jr = new JsonResponse<User>(ResponseConstants.OK, null, u);
            return SimpleResponseWrapper.getJsonResponse(jr);
        } catch (BLBException e) {
            return BLBExceptionWrapper.wrapException(e);
        }
    }

    @GET
    @Produces("application/json")
    @Path("getClientBonds")
    public String getClientBonds(@QueryParam("clientId") Long id, @Context HttpServletRequest req) {
//        try {
        //checking rights here
        //TODO: uncomment this method after db creation
//            SessionUtils.getCurrentUserIdThrowingException(req);
        List<Bond> list = bMan.getClientBonds(id);
        JsonResponse<List<Bond>> jr = new JsonResponse<List<Bond>>(ResponseConstants.OK, null, list);
        return SimpleResponseWrapper.getJsonResponse(jr);
//        } catch (BLBException e) {
//            return BLBExceptionWrapper.wrapException(e);
//        }
    }

    @GET
    @Produces("application/json")
    @Path("getFreeBonds")
    public String getFreeBonds(@Context HttpServletRequest req) {
        // try {
        //checking rights here
        //SessionUtils.getCurrentUserIdThrowingException(req);
        System.out.println("Trying to find available bonds");
        List<Bond> list = bMan.getFreeBonds();
        System.out.println("Available Bonds: " + list.size());
        JsonResponse<List<Bond>> jr = new JsonResponse<List<Bond>>(ResponseConstants.OK, null, list);
        return SimpleResponseWrapper.getJsonResponse(jr);
        // } catch (BLBException e) {
        //    return BLBExceptionWrapper.wrapException(e);
        //}
    }
    
    @GET
    @Produces("application/json")
    @Path("findBonds")
    public String findBonds(@Context HttpServletRequest req,
        @QueryParam("priceLow") Double priceLow, @QueryParam("priceHigh") Double priceHigh,
        @QueryParam("parLow") Double parLow, @QueryParam("parHigh") Double parHigh,
        @QueryParam("couponLow") Double couponLow, @QueryParam("couponHigh") Double couponHigh,
        @QueryParam("cyLow") Double cyLow, @QueryParam("cyHigh") Double cyHigh,
        @QueryParam("ytmLow") Double ytmLow, @QueryParam("ytmHigh") Double ytmHigh,
        @QueryParam("moodysLow") String moodysLow, @QueryParam("moodysHigh") String moodysHigh,
        @QueryParam("snpLow") String snpLow, @QueryParam("snpHigh") String snpHigh
        )
    {
        // try {
        //checking rights here
        //SessionUtils.getCurrentUserIdThrowingException(req);
        System.out.println("Trying to find available bonds with parameters");
        List<Bond> list = bMan.findBondsWithParameters(priceLow, priceHigh, parLow, parHigh, couponLow, couponHigh, cyLow, cyHigh, ytmLow, ytmHigh, moodysLow, moodysHigh, snpLow, snpHigh);
        System.out.println("Available Bonds: " + list.size());
        JsonResponse<List<Bond>> jr = new JsonResponse<List<Bond>>(ResponseConstants.OK, null, list);
        return SimpleResponseWrapper.getJsonResponse(jr);
        // } catch (BLBException e) {
        //    return BLBExceptionWrapper.wrapException(e);
        //}
    }

    @GET
    @Produces("application/json")
    @Path("buyBonds")
    public String buyBonds(@Context HttpServletRequest req, @QueryParam("clientId") Long clientId, @QueryParam("bondId") Long bondId, @QueryParam("quantity") Integer quantity, @QueryParam("traderId") Long traderId, @QueryParam("jurDelay") Integer jurDelay) {
        try {
            //checking rights here
            SessionUtils.getCurrentUserIdThrowingException(req);
            bMan.buyFreeBond(clientId, bondId, quantity, traderId, jurDelay);
            JsonResponse<String> jr = new JsonResponse<String>(ResponseConstants.OK, null, ResponseConstants.YES);

            return SimpleResponseWrapper.getJsonResponse(jr);
        } catch (BLBException e) {
            return BLBExceptionWrapper.wrapException(e);
        }
    }

    @GET
    @Produces("application/json")
    @Path("sellBonds")
    public String sellBonds(@Context HttpServletRequest req, @QueryParam("clientId") Long clientId, @QueryParam("bondId") Long bondId, @QueryParam("quantity") Integer quantity, @QueryParam("traderId") Long traderId) {
        try {
            //checking rights here
            System.out.println("selling bonds");
            SessionUtils.getCurrentUserIdThrowingException(req);
            System.out.println("logged in");
            bMan.sellBond(clientId, bondId, quantity, traderId);
            System.out.println("bonds sold");
            JsonResponse<String> jr = new JsonResponse<String>(ResponseConstants.OK, null, ResponseConstants.YES);
            return SimpleResponseWrapper.getJsonResponse(jr);
        } catch (BLBException e) {
            return BLBExceptionWrapper.wrapException(e);
        }
    }

    @GET
    @Produces("application/json")
    @Path("getClientInfo")
    public String getClientInfo(@Context HttpServletRequest req, @QueryParam("login") String login) {
        try {
            //checking rights here
            SessionUtils.getCurrentUserIdThrowingException(req);
            User u = userMan.getUserByLogin(login);
            JsonResponse<User> jr = new JsonResponse<User>(ResponseConstants.OK, null, u);
            return SimpleResponseWrapper.getJsonResponse(jr);
        } catch (BLBException e) {
            return BLBExceptionWrapper.wrapException(e);
        }
    }
    
    @GET
    @Produces("application/json")
    @Path("getRatingRangeMoodys")
    public String getRatingRangeMoodys(String data){
        /**
         * TODO finish getting rating from database
         */
        return "";
    }
    
    @GET
    @Produces("application/json")
    @Path("getCountries")
    public String getCountries(String data){
        List<Country> list = counMan.getCountries();
        JsonResponse<List<Country>> jr = new JsonResponse<List<Country>>(ResponseConstants.OK, null, list);
        return SimpleResponseWrapper.getJsonResponse(jr);
    }
    
    @GET
    @Produces("application/json")
    @Path("getCountryInfo")
    public String getCountryInfo(@Context HttpServletRequest req, @QueryParam("countryId") Long countryId){
        Country coun = counMan.getCountryById(countryId);
        JsonResponse<Country> jr = new JsonResponse<Country>(ResponseConstants.OK, null, coun);
        return SimpleResponseWrapper.getJsonResponse(jr);
    }
    
    @GET
    @Produces("application/json")
    @Path("getHolidays")
    public String getHolidays(@Context HttpServletRequest req, @QueryParam("countryId") Long countryId){
        List<Holiday> list = counMan.getHolidaysForCountry(countryId);
        JsonResponse<List<Holiday>> jr = new JsonResponse<List<Holiday>>(ResponseConstants.OK, null, list);
        return SimpleResponseWrapper.getJsonResponse(jr);
    }
    

    @POST
    @Produces("application/json")
    @Path("createBond")
    public String createBond(String data) {
        try {
            System.out.println(data);
            Bond bond = (new Gson()).fromJson(data, Bond.class);
            bond = bMan.createBond(bond);
            JsonResponse<Bond> jr = new JsonResponse<Bond>(ResponseConstants.OK, null, bond);
            return SimpleResponseWrapper.getJsonResponse(jr);
        } catch (BLBException e) {
            return BLBExceptionWrapper.wrapException(e);
        }
    }
    
    

    /**
     * Creates a new instance of AdminResource
     */
    public AdminResource() {
    }

    /**
     * Retrieves representation of an instance of
     * org.blbcase.web.webservices.AdminResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    @GET
    @Produces("application/json")
    @Path("test")
    public String test() {
        //TODO return proper representation object
        return "rtest";
    }

    /**
     * PUT method for updating or creating an instance of AdminResource
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
}
