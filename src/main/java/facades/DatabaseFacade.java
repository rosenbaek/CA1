/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.CityInfo;
import entities.Person;
import errorhandling.PersonNotFoundException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author christianrosenbaek
 */
public class DatabaseFacade {
    private static DatabaseFacade instance;
    private static EntityManagerFactory emf;
    
    public static DatabaseFacade getDatabaseFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new DatabaseFacade();
        }
        return instance;
    }

    private DatabaseFacade() {
    }
    
    public Person addPerson(Person person) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(person);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return person;
    }
    
    public void deletePerson(int id) throws PersonNotFoundException{
        EntityManager em = emf.createEntityManager();
        Person person;
        try {
            em.getTransaction().begin();
            person = em.find(Person.class, id);
            if (person == null) {
                throw new PersonNotFoundException("Could not delete, provided id does not exist!");
            }
            em.remove(person);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
    
    public Person getPerson(int id) throws PersonNotFoundException {
        EntityManager em = emf.createEntityManager();
        Person person = em.find(Person.class, id);
        if (person == null) {
            throw new PersonNotFoundException("No person with provided id found");
        } else {
            return person;
        }

    }
    
    public List<Person> getAllPersons() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p", Person.class);
        List<Person> persons = query.getResultList();
        return persons;
    }
    
    public Person editPerson(Person person) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(person);
            em.getTransaction().commit();
            return person;
        } finally {
            em.close();
        }
    }

    public Person getPersonByPhoneNumber(String phoneNumber) throws PersonNotFoundException{
        EntityManager em = emf.createEntityManager();
        Person person = null;
        try{
            TypedQuery<Person> query = em.createQuery("SELECT p from Person p INNER JOIN p.phones ph where ph.number = :phoneNumber",Person.class);
            query.setParameter("phoneNumber", phoneNumber);
            person = query.getSingleResult();
        } catch (Exception e) {
            throw new PersonNotFoundException("No person with provided phonenumber found");
        } finally{
            em.close();
        }
        return person;
    }
    
    public List<Person> getPersonsByHobby(String hobbyName) throws PersonNotFoundException{
        EntityManager em = emf.createEntityManager();
        List<Person> persons;
        try{
            TypedQuery<Person> query = em.createQuery("SELECT p from Person p inner join p.hobbies h WHERE h.name = :hobbyName", Person.class);
            query.setParameter("hobbyName",hobbyName);
            persons = query.getResultList(); 
            if (persons.size() == 0) {
                throw new PersonNotFoundException("No persons with provided hobby found");
            }
            return persons;
        } finally{
            em.close();
        }
    }
    
    public List<Person> getPersonsByZip(String zip) throws PersonNotFoundException{
        EntityManager em = emf.createEntityManager();
        List<Person> persons;
        try{
            TypedQuery<Person> query = em.createQuery("SELECT p from Person p inner join p.address a inner join a.cityInfo c where c.zipCode = :zip", Person.class);
            query.setParameter("zip",zip);
            persons = query.getResultList(); 
            if (persons.size() == 0) {
                throw new PersonNotFoundException("No persons with provided zipcode found");
            }
            return persons;
        } finally{
            em.close();
        }
    }
    
    public int countPersonsWithAGivenHobby(String hobby){
        EntityManager em = emf.createEntityManager();
        try{
            Query query = em.createQuery("SELECT COUNT(distinct p) from Person p INNER JOIN p.hobbies h where h.name = :hobbyName");
            query.setParameter("hobbyName", hobby);
            Long result = (Long) query.getSingleResult();
            return result.intValue();
        }finally{
            em.close();
        }
    }
    
    public List<CityInfo> getAllCityInfos(){
        EntityManager em = emf.createEntityManager();
        List<CityInfo> cityInfos;
        try{
            TypedQuery<CityInfo> query = em.createQuery("SELECT ci from CityInfo ci", CityInfo.class);
            cityInfos = query.getResultList(); 
            return cityInfos;
        } finally{
            em.close();
        }
    }

}
