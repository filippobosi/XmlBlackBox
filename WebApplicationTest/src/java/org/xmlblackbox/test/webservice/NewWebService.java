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

    public Person[] getPerson(Person[] person){
        int number =2;

        
        Person[] people = new Person[number];
        people[0] = new Person();
        people[0].setName("Mario");
        people[0].setSurname("Rossi");
        people[0].setAddress("Via romaaaaaaaaaaaaaaaaaaaa");
        System.out.println("Creata la persona Mario Rossi Via romaaaaaaaaaaaaaaaaaaaa");

        people[1] = new Person();
        people[1].setName("Paolo");
        people[1].setSurname("Bianchi");
        people[1].setAddress("Via milanooooooo");

        System.out.println("Creata la persona Paolo Biianchi Via milanoooooo");

        return people;
    }

}
