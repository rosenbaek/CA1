/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import com.google.gson.JsonObject;
import dtos.CityInfoDTO;
import dtos.CityInfosDTO;
import dtos.HobbyDTO;
import dtos.PersonDTO;
import dtos.PersonsDTO;
import entities.Address;
import entities.CityInfo;
import entities.Hobby;
import entities.Person;
import errorhandling.NotFoundException;
import static facades.DatabaseFacade.getDatabaseFacade;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import utils.EMF_Creator;

/**
 *
 * @author christianrosenbaek
 */
public class PersonFacade {
    private static DatabaseFacade dbFacade;
    private static PersonFacade instance;

    
    public static PersonFacade getPersonFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            instance = new PersonFacade();
            dbFacade = getDatabaseFacade(_emf);
        }
        return instance;
    }
    
    public PersonDTO getPersonByPhoneNumber(String phoneNumber) throws NotFoundException{
        return new PersonDTO(dbFacade.getPersonByPhoneNumber(phoneNumber));
    }

    public PersonsDTO getPersonsByHobby(String hobbyName) throws NotFoundException{
        return new PersonsDTO(dbFacade.getPersonsByHobby(hobbyName));
    }
    
    public PersonsDTO getPersonsByZip(String zip) throws NotFoundException{
        return new PersonsDTO(dbFacade.getPersonsByZip(zip));
    }
    
    public JsonObject countPersonsWithAGivenHobby(String hobby){
        JsonObject json = new JsonObject();
        int amount = dbFacade.countPersonsWithAGivenHobby(hobby);
        json.addProperty("amount", amount);
        return json;
    }
    
    public CityInfosDTO getAllCityInfos(){
        return new CityInfosDTO(dbFacade.getAllCityInfos());
    }
    
    public PersonDTO addPerson(PersonDTO personDTO) throws NotFoundException{
        //convert to Person and address
        Person person = new Person(personDTO);
        Address address = new Address(personDTO.getAddress());
        System.out.println("adddress" +address);

        
        try {
            //check if address exist
            address = dbFacade.getAddress(address);
        } catch (NotFoundException e) {
            //if not exits create address
            
        }

        //Link address and person
        //Husk alle hobbies og phones
        person.setAddress(address);
        //Persist and return person
        return new PersonDTO(dbFacade.addPerson(person));        
    }
    
    public PersonDTO editPerson(PersonDTO personDTO) throws NotFoundException{
        Person person = new Person(personDTO);
        person = dbFacade.editPerson(person);
        PersonDTO newDTO = new PersonDTO(person);
        return newDTO;
    }
}
