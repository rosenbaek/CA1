/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package errorhandling;

/**
 *
 * @author mikke
 */
public class NotFoundException extends Exception{

    public NotFoundException(String message) {
        super(message);
    }
    
}
