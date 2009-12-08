/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.xmlblackbox.test.webservice;

import javax.jws.WebService;

/**
 *
 * @author examar
 */
@WebService()
public class NewWebService {

    public Person[] getPerson(Person person){
        int number =10;

        
        Person[] people = new Person[1];
        people[0] = new Person();
        people[0].setName("Mario");
        people[0].setSurname("Rossi");
        people[0].setAddress("Via romaaaaaaaaaaaaaaaaaaaa");

        return people;
    }

}
