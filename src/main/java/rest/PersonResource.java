/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;


import errorhandling.PersonNotFoundException;
import utils.EMF_Creator;
import facades.PersonFacade;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

//Todo Remove or change relevant parts before ACTUAL use
@Path("person")
public class PersonResource {
    private final EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
    private final PersonFacade facade =  PersonFacade.getPersonFacade(emf);
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
          
  
    @GET
    @Produces({MediaType.APPLICATION_JSON}) 
    public Response getPersonByPhoneNumber() {
        return Response.ok(gson.toJson("WELCOME!"), MediaType.APPLICATION_JSON).build();
    }
    
    
    @Path("/byPhoneNumber/{phoneNumber}")
    @GET
    @Produces({MediaType.APPLICATION_JSON}) 
    public Response getPersonByPhoneNumber(@PathParam("phoneNumber") String phoneNumber) {
        return Response.ok(gson.toJson(facade.getPersonByPhoneNumber(phoneNumber)), MediaType.APPLICATION_JSON).build();
    }
    
    @Path("/byHobby/{hobby}")
    @GET
    @Produces({MediaType.APPLICATION_JSON}) 
    public Response getPersonsByHobby(@PathParam("hobby") String hobbyName) {
        return Response.ok(gson.toJson(facade.getPersonsByHobby(hobbyName)), MediaType.APPLICATION_JSON).build();
    }
    
    @Path("/byZip/{zip}")
    @GET
    @Produces({MediaType.APPLICATION_JSON}) 
    public Response getPersonsInZip(@PathParam("zip") String zip) {
        return Response.ok(gson.toJson(facade.getPersonsByZip(zip)), MediaType.APPLICATION_JSON).build();
    }
    
    @Path("/hobbyCount/{hobby}")
    @GET
    @Produces({MediaType.APPLICATION_JSON}) 
    public Response getPersonsCountByHobby(@PathParam("hobby") String hobby) {
        return Response.ok(gson.toJson(facade.countPersonsWithAGivenHobby(hobby)), MediaType.APPLICATION_JSON).build();
    }
    
    @Path("/zipList")
    @GET
    @Produces({MediaType.APPLICATION_JSON}) 
    public Response getAllZips() {
        return Response.ok(gson.toJson(facade.getAllCityInfos()), MediaType.APPLICATION_JSON).build();
    }
    
    
    
    
    
    
    
    
   
}
