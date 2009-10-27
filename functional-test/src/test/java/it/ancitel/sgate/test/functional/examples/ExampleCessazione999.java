/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.ancitel.sgate.test.functional.examples;

import it.ancitel.sgate.test.util.Configurator;
import it.ancitel.test.infrastruttura.MainTestCase;
import it.ancitel.test.infrastruttura.exception.TestException;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.log4j.Logger;

/**
 *
 * @author Crea
 */
public class ExampleCessazione999 extends MainTestCase{
    private final static Logger log = Logger.getLogger(ExampleCessazione999.class);

    public ExampleCessazione999(String msg) {
        super(msg);
    }

    public static Test suite() {
    	log.info("Log del TestCase ExampleCessazione999 Example");
        TestSuite suite= new TestSuite(ExampleCessazione999.class);
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
