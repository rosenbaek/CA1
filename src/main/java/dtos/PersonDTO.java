/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import entities.Hobby;
import entities.Person;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 *
 * @author christianrosenbaek
 */
public class PersonDTO {
    @Schema(required = false, example = "2")
    private int id;
    @Schema(required = true, example = "test@test.dk")
    private String email;
    @Schema(required = true, example = "Chris")
    private String firstName;
    @Schema(required = true, example = "Larsen")
    private String lastName;
    
    private AddressDTO address;
    private List<PhoneDTO> phones;
    private List<HobbyDTO> hobbies;
    

    public PersonDTO(Person person) {
        this.id = person.getId();
        this.email = person.getEmail();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.address = new AddressDTO(person.getAddress());
        this.phones = PhoneDTO.getDtos(person.getPhones());
        this.hobbies = HobbyDTO.getDtos(person.getHobbies());
    }

    public PersonDTO() {
    }

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<PhoneDTO> getPhones() {
        return phones;
    }

    public void setPhones(List<PhoneDTO> phones) {
        this.phones = phones;
    }

    public List<HobbyDTO> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<HobbyDTO> hobbiesDTO) {
        this.hobbies = hobbiesDTO;
    }
    
    

   
    
    
}
