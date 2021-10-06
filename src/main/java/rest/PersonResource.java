/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dtos.CityInfoDTO;
import dtos.HobbyDTO;
import dtos.PersonDTO;
import entities.Hobby;


import errorhandling.NotFoundException;
import utils.EMF_Creator;
import facades.PersonFacade;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
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


@OpenAPIDefinition(
        info = @Info(
                title = "Christian og Mikkels API",
                version = "0.1",
                description = "CA1 projekt"
        ),
        tags = {
            @Tag(name = "person", description = "Endpoints for person")

        },
        servers = {
            @Server(
                    description = "For Local host testing",
                    url = "http://localhost:8080/dat3-startcode"
            ),
            @Server(
                    description = "Server API",
                    url = "https://itpiloten.dk"
            )
        }
)


//Todo Remove or change relevant parts before ACTUAL use
@Path("person")
public class PersonResource {
    private final EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
    private final PersonFacade facade =  PersonFacade.getPersonFacade(emf);
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
          
  
    @GET
    @Produces({MediaType.APPLICATION_JSON}) 
    public Response getServerIsUp() {
        return Response.ok(gson.toJson("WELCOME!"), MediaType.APPLICATION_JSON).build();
    }
    
    
    @Path("/byPhoneNumber/{phoneNumber}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(summary = "Get person by phonenumber",
        tags = {"person"},
        responses = {
            @ApiResponse(
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonDTO.class))),
            @ApiResponse(responseCode = "200", description = "The Requested Person"),
            @ApiResponse(responseCode = "400", description = "Entity not found")})
    public Response getPersonByPhoneNumber(@PathParam("phoneNumber") String phoneNumber) throws NotFoundException{
        return Response.ok(gson.toJson(facade.getPersonByPhoneNumber(phoneNumber)), MediaType.APPLICATION_JSON).build();
    }
    
    @Path("/{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(summary = "Get person by id",
            tags = {"person"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonDTO.class))),
                @ApiResponse(responseCode = "200", description = "The Requested Person"),
                @ApiResponse(responseCode = "400", description = "Entity not found")})
    public Response getPersonByPhoneNumber(@PathParam("id") int id) throws NotFoundException {
        return Response.ok(gson.toJson(facade.getPersonById(id)), MediaType.APPLICATION_JSON).build();
    }
    
    
    @Path("/addperson")
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @Operation(summary = "Add person",
            tags = {"person"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonDTO.class))),
                @ApiResponse(responseCode = "200", description = "The Requested Person"),
                @ApiResponse(responseCode = "400", description = "Entity not found")})
    public Response addPerson(String person) throws NotFoundException {
        PersonDTO personDTO = gson.fromJson(person, PersonDTO.class);
        return Response.ok(gson.toJson(facade.addPerson(personDTO)), MediaType.APPLICATION_JSON).build();
    }
    
    @Path("/editperson")
    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @Operation(summary = "Edit person",
            tags = {"person"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonDTO.class))),
                @ApiResponse(responseCode = "200", description = "The Requested Person"),
                @ApiResponse(responseCode = "400", description = "Entity not found")})
    public Response editPerson(String person) throws NotFoundException {
        PersonDTO personDTO = gson.fromJson(person, PersonDTO.class);
        return Response.ok(gson.toJson(facade.editPerson(personDTO)), MediaType.APPLICATION_JSON).build();
    }
    
    
    @Path("/byHobby/{hobby}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(summary = "Get person by hobby",
        tags = {"person"},
        responses = {
            @ApiResponse(
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonDTO.class))),
            @ApiResponse(responseCode = "200", description = "The Requested Person"),
            @ApiResponse(responseCode = "400", description = "Entity not found")})
    public Response getPersonsByHobby(@PathParam("hobby") String hobbyName) throws NotFoundException{
        return Response.ok(gson.toJson(facade.getPersonsByHobby(hobbyName)), MediaType.APPLICATION_JSON).build();
    }
    
    
    @Path("/byZip/{zip}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(summary = "Get person by zipcode",
            tags = {"person"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonDTO.class))),
                @ApiResponse(responseCode = "200", description = "The Requested Person"),
                @ApiResponse(responseCode = "400", description = "Entity not found")})
    public Response getPersonsInZip(@PathParam("zip") String zip) throws NotFoundException{
        return Response.ok(gson.toJson(facade.getPersonsByZip(zip)), MediaType.APPLICATION_JSON).build();
    }
    
    @Path("/hobbyCount/{hobby}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(summary = "Get number of persons by hobby",
            tags = {"person"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json"))})
    public Response getPersonsCountByHobby(@PathParam("hobby") String hobby) {
        return Response.ok(gson.toJson(facade.countPersonsWithAGivenHobby(hobby)), MediaType.APPLICATION_JSON).build();
    }
    
    @Path("/zipList")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(summary = "Get all zipcodes",
            tags = {"person"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = CityInfoDTO.class)))})
    public Response getAllZips() {
        return Response.ok(gson.toJson(facade.getAllCityInfos()), MediaType.APPLICATION_JSON).build();
    }
    
    
    
    @Path("/hobbyList")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(summary = "Get all hobbies",
            tags = {"person"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = HobbyDTO.class)))})
    public Response getAllHobbies() {
        return Response.ok(gson.toJson(facade.getAllHobbies()), MediaType.APPLICATION_JSON).build();
    }
    

    
    
   
}
