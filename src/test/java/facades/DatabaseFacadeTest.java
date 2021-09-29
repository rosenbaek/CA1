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


import entities.Address;
import entities.CityInfo;
import entities.Hobby;
import entities.Person;
import entities.Phone;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


//Uncomment the line below, to temporarily disable this test
//@Disabled
public class DatabaseFacadeTest {

    private static EntityManagerFactory emf;
    private static DatabaseFacade facade;

   

    @BeforeAll
    public static void setUpClass() {
       emf = EMF_Creator.createEntityManagerFactoryForTest();
       facade = DatabaseFacade.getDatabaseFacade(emf);
    }


    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the code below to use YOUR OWN entity class
    @BeforeEach
    public void setUp(){
        EntityManager em = emf.createEntityManager();
        try {
            Hobby hobby1 = new Hobby("testHobby1", "http://XXX.com", "category1", "type1");
            Hobby hobby2 = new Hobby("testHobby2", "http://XXX.com", "category2", "type2");

            CityInfo ci1 = new CityInfo("1000", "by1000");
            CityInfo ci2 = new CityInfo("2000", "by2000");

            Person p1 = new Person("test@test.dk", "testFName", "testLname");
            Person p2 = new Person("test2@test2.dk", "test2FName", "test2Lname");
            Person p3 = new Person("test3@test3.dk", "test3FName", "test3Lname");

            Phone phone1 = new Phone("11111111", "testDescription");
            Phone phone2 = new Phone("222222", "test2Description");
            Phone phone3 = new Phone("33333", "test3Description");

            Address a1 = new Address("testvej1", "mere info");
            Address a2 = new Address("testvej2", "mere info");

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
    public void testTrue() {
        assertEquals(true,true);
    }

 

   

}
