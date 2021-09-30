/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import dtos.CityInfoDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 *
 * @author mikke
 */
@Entity
@NamedQuery(name = "CityInfo.deleteAllRows", query = "DELETE from CityInfo")
public class CityInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(length = 4)
    private String zipCode;
    @Column(length=35)
    private String city;

    @OneToMany(mappedBy = "cityInfo",  cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Address> addresses;
    
    
    public CityInfo() {
    }

    public CityInfo(String zipCode, String city) {
        this.zipCode = zipCode;
        this.city = city;
        this.addresses = new ArrayList<>();
    }
    
    public CityInfo(CityInfoDTO dto) {
        this.zipCode = dto.getZipCode();
        this.city = dto.getCity();
        this.addresses = new ArrayList<>();
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void addAddress(Address address) {
        if (address != null){
            this.addresses.add(address);
            //Makes the relationship bi-directional
            address.setCityInfo(this);
        }
    }
    
    
    public void removeAddress(Address address) {
        if (address != null){
            this.addresses.remove(address);
          
            //Cascade will remove phone from DB.
            address.setCityInfo(null);
        }
    }
    
    
    
}

