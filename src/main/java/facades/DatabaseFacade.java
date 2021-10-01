/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.Address;
import entities.CityInfo;
import entities.Person;
import errorhandling.NotFoundException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.RollbackException;
import javax.persistence.TypedQuery;
import org.eclipse.persistence.exceptions.DatabaseException;

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
    
    public Address getAddress(Address address) throws NotFoundException {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Address> query = em.createQuery("SELECT a FROM Address a WHERE a.street = :street AND a.cityInfo.zipCode = :zipcode AND a.additionalInfo = :aditionalInfo", Address.class);
            query.setParameter("street", address.getStreet());
            query.setParameter("zipcode", address.getCityInfo().getZipCode());
            query.setParameter("aditionalInfo", address.getAdditionalInfo());
            Address a = query.getSingleResult();
            return a;
        } catch (Exception e) {
            //TODO create addressNotFound Exception
            throw new NotFoundException("Address not found");
        } finally {
            em.close();
        }  
    }
    
    public Person addPerson(Person person)throws PersistenceException, NotFoundException{
        //Error handling with same email
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(person);
            em.getTransaction().commit();
        } catch(RollbackException e){
            System.out.println("FAIIIIIIL =  "+e.getCause().getLocalizedMessage());
            //throw new PersonNotFoundException("Phone already exist");
            for (Throwable t = e.getCause(); t != null; t = t.getCause()) {
                System.out.println("Exception:" + t);
                
                if (t.getCause().getMessage().contains("phone")){
                    String msg = t.getCause().getMessage();
                    int firstIndex = msg.indexOf("'");
                    int secondIndex = msg.indexOf("'", firstIndex+1)+1;
                    String number = msg.substring(firstIndex,secondIndex);
                    throw new NotFoundException("Phone already exist: "+number);
                }
            }
        }finally {
            em.close();
        }
        return person;
    }
    
    public void deletePerson(int id) throws NotFoundException{
        EntityManager em = emf.createEntityManager();
        Person person;
        try {
            em.getTransaction().begin();
            person = em.find(Person.class, id);
            if (person == null) {
                throw new NotFoundException("Could not delete, provided id does not exist!");
            }
            em.remove(person);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
    
    public Person getPerson(int id) throws NotFoundException {
        EntityManager em = emf.createEntityManager();
        Person person = em.find(Person.class, id);
        if (person == null) {
            throw new NotFoundException("No person with provided id found");
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

    public Person getPersonByPhoneNumber(String phoneNumber) throws NotFoundException{
        EntityManager em = emf.createEntityManager();
        Person person = null;
        try{
            TypedQuery<Person> query = em.createQuery("SELECT p from Person p INNER JOIN p.phones ph where ph.number = :phoneNumber",Person.class);
            query.setParameter("phoneNumber", phoneNumber);
            person = query.getSingleResult();
        } catch (Exception e) {
            throw new NotFoundException("No person with provided phonenumber found");
        } finally{
            em.close();
        }
        return person;
    }
    
    public List<Person> getPersonsByHobby(String hobbyName) throws NotFoundException{
        EntityManager em = emf.createEntityManager();
        List<Person> persons;
        try{
            TypedQuery<Person> query = em.createQuery("SELECT p from Person p inner join p.hobbies h WHERE h.name = :hobbyName", Person.class);
            query.setParameter("hobbyName",hobbyName);
            persons = query.getResultList(); 
            if (persons.size() == 0) {
                throw new NotFoundException("No persons with provided hobby found");
            }
            return persons;
        } finally{
            em.close();
        }
    }
    
    public List<Person> getPersonsByZip(String zip) throws NotFoundException{
        EntityManager em = emf.createEntityManager();
        List<Person> persons;
        try{
            TypedQuery<Person> query = em.createQuery("SELECT p from Person p inner join p.address a inner join a.cityInfo c where c.zipCode = :zip", Person.class);
            query.setParameter("zip",zip);
            persons = query.getResultList(); 
            if (persons.size() == 0) {
                throw new NotFoundException("No persons with provided zipcode found");
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
