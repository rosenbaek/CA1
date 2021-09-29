/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;


import entities.Address;
import entities.CityInfo;
import entities.Hobby;
import entities.Person;
import entities.Phone;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import utils.EMF_Creator;

/**
 *
 * @author tha
 */
public class Populator {
    public static void populate(){
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        
        EntityManager em = emf.createEntityManager();
        Hobby hobby1 = em.find(Hobby.class,"Airsoft");
        Hobby hobby2 = em.find(Hobby.class,"Animation");
        
        CityInfo ci1 = em.find(CityInfo.class, "0800");
        CityInfo ci2 = em.find(CityInfo.class, "2765");
        
        
        Person p1 = new Person("test@test.dk","testFName","testLname");
        Person p2 = new Person("test2@test2.dk","test2FName","test2Lname");
        Person p3 = new Person("test3@test3.dk","test3FName","test3Lname");
        
        Phone phone1 = new Phone("11111111","testDescription");
        Phone phone2 = new Phone("222222","test2Description");
        Phone phone3 = new Phone("33333","test3Description");
        
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
           em.persist(p1);
           em.persist(p2);
           em.persist(p3);
        em.getTransaction().commit();
        em.close();
        
        p1.getHobbies().forEach(x -> System.out.println(x));
        hobby1.getPersons().forEach(x -> System.out.println("persons from hobbies "+x));
        
        System.out.println("BREAK");
        
        a1.getPersons().forEach(x -> System.out.println("persons from address A1 "+a1.getId()+"   ---   "+x));
        System.out.println(p2.getAddress());
       
        System.out.println("ZIP-ADDRESS CHECK");
        System.out.println(p1.getAddress().getCityInfo().getZipCode());
        ci1.getAddresses().forEach(x->x.getPersons().forEach(y->System.out.println(y.getFirstName())));
    
    }
    
    public static void main(String[] args) {
        populate();
    }
}
