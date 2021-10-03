/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import dtos.PersonDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 *
 * @author mikke
 */
@Entity
@NamedQuery(name = "Person.deleteAllRows", query = "DELETE from Person")
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(unique = true)
    private String email;
    private String firstName;
    private String lastName;
    
    @ManyToMany(mappedBy = "persons")
    private List<Hobby> hobbies;
    
    @OneToMany(mappedBy = "person",  cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Phone> phones;
    
    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Address address;

    public Person() {
    }

    public Person(String email, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.hobbies = new ArrayList<>();
        this.phones = new ArrayList<>();
    }
    
    
    
    public Person(PersonDTO personDTO) {
        this.id = personDTO.getId();
        this.email = personDTO.getEmail();
        this.firstName = personDTO.getFirstName();
        this.lastName = personDTO.getLastName();
        this.hobbies = Hobby.getHobbies(personDTO.getHobbies());
        this.phones = Phone.getPhones(personDTO.getPhones());
        if (personDTO.getAddress() != null) {
            this.address = Address.getAddress(personDTO.getAddress());
        }
    }

    public List<Phone> getPhones() {
        return phones;
    }
    
    

    public void addPhone(Phone phone) {
        if (phone != null){
            this.phones.add(phone);
            //Makes the relationship bi-directional
            phone.setPerson(this);
        }
    }
    
    public void removePhone(Phone phone) {
        if (phone != null){
            this.phones.remove(phone);
          
            //Cascade will remove phone from DB.
            phone.setPerson(null);
        }
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
        if(!address.getPersons().contains(this)){
            address.addPerson(this);
        }
        }
    
    
    public List<Hobby> getHobbies() {
        return hobbies;
    }

    public void addHobbies(Hobby hobby) {
        if (hobby != null){
            this.hobbies.add(hobby);
            //Makes the relationship bi-directional
            hobby.getPersons().add(this);
        }
    }
    
    public void removeHobby(Hobby hobby) {
        if (hobby != null){
            this.hobbies.remove(hobby);
            hobby.getPersons().remove(this);
        }
    }

    @Override
    public String toString() {
        return "Person{" + "id=" + id + ", email=" + email + ", firstName=" + firstName + ", lastName=" + lastName + '}';
    }

 




    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Person other = (Person) obj;
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        if (!Objects.equals(this.firstName, other.firstName)) {
            return false;
        }
        if (!Objects.equals(this.lastName, other.lastName)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
       
        return true;
    }

    

    
    
    

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
    
    

  
    
   
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
