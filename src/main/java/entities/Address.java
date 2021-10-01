/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import dtos.AddressDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 *
 * @author mikke
 */
@Entity
@NamedQuery(name = "Address.deleteAllRows", query = "DELETE from Address")
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String street;
    private String additionalInfo;
    
    @OneToMany(mappedBy = "address")
    private List<Person> persons;
    
    @ManyToOne
    private CityInfo cityInfo;

    public Address() {
    }

    public Address(String street, String additionalInfo) {
        this.street = street;
        this.additionalInfo = additionalInfo;
        this.persons = new ArrayList<>();
    }
    
    public Address(AddressDTO addressDTO) {
        this.street = addressDTO.getStreet();
        this.additionalInfo = addressDTO.getAdditionalInfo();
        this.cityInfo = new CityInfo(addressDTO.getZipCode(), addressDTO.getCity());
        this.persons = new ArrayList<>();
    }

    
    public List<Person> getPersons() {
        return persons;
    }

    public void addPerson(Person person) {
        if (person != null){
            this.persons.add(person);
            //Makes the relationship bi-directional
            person.setAddress(this);
        }
    }
    
    public void removePerson(Person person) {
        if (person != null){
            this.persons.remove(person);
          
            //Cascade will remove phone from DB.
            person.setAddress(null);
        }
    }
    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public CityInfo getCityInfo() {
        return cityInfo;
    }

    public void setCityInfo(CityInfo cityInfo) {
        this.cityInfo = cityInfo;
        if(!cityInfo.getAddresses().contains(this)){
            //TODO: Make facade that checks if address is in DB and fetches it before just inserting a new one.
            cityInfo.addAddress(this);
        }
    }

    
    
    
    @Override
    public String toString() {
        return "Address{" + "id=" + id + ", street=" + street + ", additionalInfo=" + additionalInfo + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
        hash = 97 * hash + Objects.hashCode(this.street);
        hash = 97 * hash + Objects.hashCode(this.additionalInfo);
        hash = 97 * hash + Objects.hashCode(this.persons);
        hash = 97 * hash + Objects.hashCode(this.cityInfo);
        return hash;
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
        final Address other = (Address) obj;
        if (!Objects.equals(this.street, other.street)) {
            return false;
        }
        if (!Objects.equals(this.additionalInfo, other.additionalInfo)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.persons, other.persons)) {
            return false;
        }
        if (!Objects.equals(this.cityInfo, other.cityInfo)) {
            return false;
        }
        return true;
    }

   

    

    
    
    
    
    

    
    
}
