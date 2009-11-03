/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.xmlblackbox.test.functional.examples;

import java.util.Properties;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.log4j.Logger;
import org.xmlblackbox.test.infrastructure.MainTestCase;
import org.xmlblackbox.test.infrastructure.exception.TestException;
import org.xmlblackbox.test.infrastructure.util.DBConnection;
import org.xmlblackbox.test.util.Configurator;

/**
 *
 * @author Crea
 */
public class ExampleCessazione999_xmlBlackbox extends MainTestCase{
    private final static Logger log = Logger.getLogger(ExampleCessazione999_xmlBlackbox.class);

    public ExampleCessazione999_xmlBlackbox(String msg) {
        super(msg);
    }

    public static Test suite() {
    	log.info("Log del TestCase ExampleCessazione999_xmlBlackbox Example");
        TestSuite suite= new TestSuite(ExampleCessazione999_xmlBlackbox.class);
        return suite;
    }

    /**
     * Overwiring del metodo setUp().
     * Questo metodo viene chiamato da JUnit prima di eseguire il test.
     */
    public void setUp() throws Exception {
        // richiamo il metodo setUp() definito nella helper class
    	super.setUp();
    }

    /**
     * Overwriting del metodo tearDown.
     * Questo metodo viene chiamato da JUnit quando il test termina.
     */
    public void tearDown() throws Exception {
        //log.info("Esecuzione tearDown");
    }

    public void testExecute() {
       	try {
            
            Properties fileProperties = Configurator.getProperties();

            // Connessione al DB...
			DBConnection.setConfig(DBConnection.params.DRIVER, fileProperties.getProperty("db.driver"));
			DBConnection.setConfig(DBConnection.params.URL, fileProperties.getProperty("db.url"));
			DBConnection.setConfig(DBConnection.params.USERNAME, fileProperties.getProperty("db.user"));
			DBConnection.setConfig(DBConnection.params.PASSWORD, fileProperties.getProperty("db.pw"));
			execute(this.getClass(),Configurator.getProperties());
   		} catch (TestException e) {
   			log.error("Eccezione", e.getContainedException());
   			fail("Eccezione "+e);
   		} catch (Exception e) {
   			log.error("Eccezione", e);
   			fail("Eccezione "+e.getMessage());
   		}
   	}

}
