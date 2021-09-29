/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

/**
 *
 * @author mikke
 */


import static org.hamcrest.Matchers.is;

import entities.Address;
import entities.CityInfo;
import entities.Hobby;
import entities.Person;
import entities.Phone;
import utils.EMF_Creator;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import java.net.URI;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.hamcrest.MatcherAssert;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
//Uncomment the line below, to temporarily disable this test
//@Disabled

public class PersonResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    
    private static Person p1,p2,p3;
    private static Hobby hobby1,hobby2;
    private static CityInfo ci1,ci2;
    private static Phone phone1,phone2,phone3;
    private static Address a1,a2;
    
    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        //System.in.read();

        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class
    @BeforeEach
    public void setUp() {
       EntityManager em = emf.createEntityManager();
        try {
            hobby1 = new Hobby("testHobby1", "http://XXX.com", "category1", "type1");
            hobby2 = new Hobby("testHobby2", "http://XXX.com", "category2", "type2");

            ci1 = new CityInfo("1000", "by1000");
            ci2 = new CityInfo("2000", "by2000");

            p1 = new Person("test@test.dk", "testFName", "testLname");
            p2 = new Person("test2@test2.dk", "test2FName", "test2Lname");
            p3 = new Person("test3@test3.dk", "test3FName", "test3Lname");

            phone1 = new Phone("11111111", "testDescription");
            phone2 = new Phone("222222", "test2Description");
            phone3 = new Phone("33333", "test3Description");

            a1 = new Address("testvej1", "mere info");
            a2 = new Address("testvej2", "mere info");

            a1.setCityInfo(ci1);
            ci2.addAddress(a2);

            p1.setAddress(a1);
            a2.addPerson(p2);
            p3.setAddress(a1);

            p1.addPhone(phone1);
            p1.addPhone(phone2);
            p2.addPhone(phone3);

            p1.addHobbies(hobby1);
            p1.addHobbies(hobby2);
            p2.addHobbies(hobby1);
            em.getTransaction().begin();
                em.createNamedQuery("Hobby.deleteAllRows").executeUpdate();
                em.createNamedQuery("Phone.deleteAllRows").executeUpdate();
                em.createNamedQuery("Person.deleteAllRows").executeUpdate();
                em.createNamedQuery("Address.deleteAllRows").executeUpdate();
                em.createNamedQuery("CityInfo.deleteAllRows").executeUpdate();
                
                //Persists Hobbies and CityInfo as we dont execute script in test.
                em.persist(hobby1);
                em.persist(hobby2);
                em.persist(ci1);
                em.persist(ci2);
                
                
                em.persist(p1);
                em.persist(p2);
                em.persist(p3);
            em.getTransaction().commit();

        } finally {
            em.close();
        }
    }

    @Test
    public void testServerIsUp() {
        System.out.println("Testing is server UP");
        given().when().get("/person").then().statusCode(200);
    }
    
    @Test
    public void testGetPersonByPhoneNumber() {
        String phonenumber = p1.getPhones().get(0).getNumber();
        given()
                .contentType("application/json")
                .get("person/byPhoneNumber/"+phonenumber)
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("firstName", is(p1.getFirstName()))
                .body("id", is(p1.getId()));
    }
    
    @Test
    public void testGetPersonsByHobby() {
        String hobby = p1.getHobbies().get(0).getName();
        String url = "person/byHobby/"+hobby;
        given()
                .contentType("application/json")
                .get(url).then()
                .assertThat()
                .statusCode(200)
                .body("all", hasSize(2));
    }
    
    @Test
    public void testGetPersonsByHobby2() {
        String hobby = p1.getHobbies().get(0).getName();
        String url = "person/byHobby/"+hobby;
        given()
                .contentType("application/json")
                .get(url).then()
                .assertThat()
                .statusCode(200)
                .body("all", hasSize(4));
    }
 
    
}
