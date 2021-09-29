/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


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
          
  
    @Path("{phoneNumber}")
    @GET
    @Produces({MediaType.APPLICATION_JSON}) 
    public Response getPersonById(@PathParam("phoneNumber") String phoneNumber) {
        return Response.ok(gson.toJson(facade.getPersonByPhoneNumber(phoneNumber)), MediaType.APPLICATION_JSON).build();
    }
    
    
   
}
