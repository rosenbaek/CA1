/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

/**
 *
 * @author mikke
 */


import dtos.AddressDTO;
import dtos.PersonDTO;
import entities.Address;
import entities.CityInfo;
import entities.Hobby;
import entities.Person;
import entities.Phone;
import errorhandling.PersonNotFoundException;
import java.util.List;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


//Uncomment the line below, to temporarily disable this test
//@Disabled
public class DatabaseFacadeTest {

    private static EntityManagerFactory emf;
    private static DatabaseFacade facade;
    private static PersonFacade personFacade;
    private static Person p1,p2,p3;
    private static Hobby hobby1,hobby2;
    private static CityInfo ci1,ci2;
    private static Phone phone1,phone2,phone3;
    private static Address a1,a2;

   

    @BeforeAll
    public static void setUpClass() {
       emf = EMF_Creator.createEntityManagerFactoryForTest();
       facade = DatabaseFacade.getDatabaseFacade(emf);
       personFacade = PersonFacade.getPersonFacade(emf);
    }


    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the code below to use YOUR OWN entity class
    @BeforeEach
    public void setUp(){
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

    

    /**
     * Test of addPerson method, of class DatabaseFacade.
     */
    @Test
    public void testAddPerson() {
        System.out.println("createPerson");
        Person newPerson = new Person("email","firstname","lastname");
        newPerson = facade.addPerson(newPerson);
        assertNotNull(newPerson.getId());
    }

    /**
     * Test of deletePerson method, of class DatabaseFacade.
     */
    @Test
    public void testDeletePerson() throws Exception {
        System.out.println("deletePerson");
        facade.deletePerson(p1.getId());
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
            Person result = em.find(Person.class, p1.getId());
        em.getTransaction().commit();
        em.close();
        assertNull(result);
    }

    /**
     * Test of getPerson method, of class DatabaseFacade.
     */
    @Test
    public void testGetPerson() throws Exception {
        System.out.println("getPersonById");
        Integer id = p1.getId();
        Person expResult = p1;
        Person result = facade.getPerson(id);
        assertEquals(expResult.getId(), result.getId());
    }

    /**
     * Test of getAllPersons method, of class DatabaseFacade.
     */
    @Test
    public void testGetAllPersons() {
        System.out.println("getAllPersons");
        int expResult = 3;
        List<Person> result = facade.getAllPersons();
        assertEquals(expResult, result.size());
    }

    /**
     * Test of editPerson method, of class DatabaseFacade.
     */
    @Test
    public void testEditPerson() throws PersonNotFoundException {
        System.out.println("updatePerson");
        p1.setFirstName("Updated name");
        Person result = facade.editPerson(p1);
        assertEquals("Updated name", result.getFirstName());
    }
    
    @Test
    public void testgetPersonByPhoneNumber() throws PersonNotFoundException {
        System.out.println("getPersonByPhoneNumber");
        String phoneNumber = p1.getPhones().get(0).getNumber();
        int expected = p1.getId();
        Person result = facade.getPersonByPhoneNumber(phoneNumber);
        assertEquals(expected, result.getId());
    }
    


    @Test
    public void testgetPersonsByHobby() throws PersonNotFoundException {
        System.out.println("testgetPersonsByHobby");
        int expectedSize = 2;
        List<Person> result = facade.getPersonsByHobby(hobby1.getName());
        assertEquals(expectedSize, result.size());
    }
 

    @Test
    public void testgetPersonsByZip() throws PersonNotFoundException {
        System.out.println("testgetPersonsByZip");
        int expected = 2;
        List<Person> result = facade.getPersonsByZip(ci1.getZipCode());
        assertEquals(expected, result.size());
    }

    @Test
    public void testcountPersonsWithAGivenHobby() {
        int expected = 2;
        int actual = facade.countPersonsWithAGivenHobby(hobby1.getName());
        assertEquals(expected, actual);
    }
    
    @Test
    public void testgetAllCityInfos() {
        int expected = 2;
        List<CityInfo> cityInfos = facade.getAllCityInfos();
        assertEquals(expected, cityInfos.size());
    }
    
    @Test
    public void testGetAddress() throws PersonNotFoundException {      
        Address a5 = new Address("testvej1", "mere info");
        CityInfo ci6 = new CityInfo("1000", "by1000");
        a5.setCityInfo(ci6);
        AddressDTO aDTO = new AddressDTO(a5);
        Address a6 = new Address(aDTO);
        Address newA = facade.getAddress(a6);
        System.out.println(a6.toString());
        assertEquals(a1.getId(), newA.getId());
    }
    
    @Test
    public void testAddPersonWithAddress() throws PersonNotFoundException {
        Person person = new Person("email1", "firstname1", "lastname1");
        Address a5 = new Address("testvej1", "mere info");
        CityInfo ci6 = new CityInfo("1000", "by1000");
        a5.setCityInfo(ci6);
        person.setAddress(a5);
        PersonDTO personDTO = new PersonDTO(person);
        PersonDTO newDTO = personFacade.addPerson(personDTO);
        assertEquals(a1.getId(), newDTO.getAddress().getId());
    }
   
}
