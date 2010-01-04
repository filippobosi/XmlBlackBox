/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.xmlblackbox.test.functional.examples;

import java.util.Properties;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;
import org.xmlblackbox.test.infrastructure.FlowControl;
import org.xmlblackbox.test.infrastructure.exception.TestException;
import org.xmlblackbox.test.util.Configurator;

/**
 *
 * @author Crea
 */
public class Example999WebApp extends FlowControl{
    private final static Logger log = Logger.getLogger(Example999WebApp.class);

    public Example999WebApp(String msg) {
        super(msg);
    }

    public static Test suite() {
    	log.info("Log del TestCase Example999WebApp");
        TestSuite suite= new TestSuite(Example999WebApp.class);
        return suite;
    }

    /**
     * Overwiring del metodo setUp().
     * Questo metodo viene chiamato da JUnit prima di eseguire il test.
     */
    public void setUp() throws Exception {
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
            log.info("Esegue l'execute");
            Configurator.configureLog4J();
            execute(this.getClass(),Configurator.getProperties());
            //execute(this.getClass(),Configurator.getProperties(), DBConnection.getConnection().getConnection());
            log.info("stop execute");
   		} catch (TestException e) {
   			log.error("Exception ", e.getContainedException());
   			fail("Exception "+e);
   		} catch (Exception e) {
   			log.error("Exception ", e);
   			fail("Exception "+e.getMessage());
   		}
   	}

}
