/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import dtos.PersonDTO;
import entities.Person;
import errorhandling.PersonNotFoundException;
import static facades.DatabaseFacade.getDatabaseFacade;
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
    
    public PersonDTO getPersonByPhoneNumber(String phoneNumber){
        return new PersonDTO(dbFacade.getPersonByPhoneNumber(phoneNumber));
    }

}
