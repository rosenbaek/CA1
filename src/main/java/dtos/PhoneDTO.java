/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import entities.Hobby;
import entities.Phone;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author christianrosenbaek
 */
public class PhoneDTO {
    private String number;
    private int id;
    private String description;

    public PhoneDTO(Phone phone) {
        this.number = phone.getNumber();
        this.id = phone.getId();
        this.description = phone.getDescription();
    }
    
    public static List<PhoneDTO> getDtos(List<Phone> phones) {
        List<PhoneDTO> phonesDTO = new ArrayList();
        phones.forEach(phone -> phonesDTO.add(new PhoneDTO(phone)));
        return phonesDTO;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    
}
