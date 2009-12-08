/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.xmlblackbox.test.functional.examples;

import java.sql.Connection;
import java.util.Properties;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlObject;
import org.xmlblackbox.test.infrastructure.FlowControl;
import org.xmlblackbox.test.infrastructure.exception.TestException;
import org.xmlblackbox.test.infrastructure.util.DBConnection;
import org.xmlblackbox.test.util.Configurator;
import org.xmlblackbox.test.webservice.client.NewWebServiceServiceStub;

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
//            org.xmlblackbox.test.webservice.client.NewWebServiceServiceStub stub = new NewWebServiceServiceStub();


            log.info("Esegue l'execute");
            Configurator.configureLog4J();
            execute(this.getClass(),Configurator.getProperties());
            //execute(this.getClass(),Configurator.getProperties(), DBConnection.getConnection().getConnection());
            log.info("fine execute");
   		} catch (TestException e) {
   			log.error("Eccezione", e.getContainedException());
   			fail("Eccezione "+e);
   		} catch (Exception e) {
   			log.error("Eccezione", e);
   			fail("Eccezione "+e.getMessage());
   		}
   	}

}
