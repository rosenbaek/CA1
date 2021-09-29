/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.Person;
import errorhandling.PersonNotFoundException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
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

    public Person getPersonByPhoneNumber(String phoneNumber) {
        EntityManager em = emf.createEntityManager();
        try{
            Person person;
            TypedQuery<Person> query = em.createQuery("SELECT p from Person p INNER JOIN p.phones ph where ph.number = :phoneNumber",Person.class);
            query.setParameter("phoneNumber", phoneNumber);
            person = query.getSingleResult();
            return person;
        } finally{
            em.close();
        }
    }
    
    public List<Person> getPersonsByHobby(String hobbyName){
        EntityManager em = emf.createEntityManager();
        List<Person> persons;
        try{
            TypedQuery<Person> query = em.createQuery("SELECT p from Person p inner join p.hobbies h WHERE h.name = :hobbyName", Person.class);
            query.setParameter("hobbyName",hobbyName);
            persons = query.getResultList(); 
            return persons;
        } finally{
            em.close();
        }
    }

}
