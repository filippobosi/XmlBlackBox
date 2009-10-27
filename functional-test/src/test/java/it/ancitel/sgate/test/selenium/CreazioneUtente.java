/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.ancitel.sgate.test.selenium;

import com.thoughtworks.selenium.Selenium;
import it.ancitel.sgate.test.util.SgateSeleneseTestCase;
import it.ancitel.test.infrastruttura.interfaces.SeleniumNavigation;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 *
 * @author kewell
 */
public class CreazioneUtente extends SgateSeleneseTestCase implements SeleniumNavigation{
	private final static Logger logger = Logger.getLogger(CreaDomandaEconomica.class);

    @Override
	public Selenium executeNavigation(Properties prop) throws Exception {
    	logger.debug("SELENIUM PROPERTIES : " + prop);

        selenium.open("/sgate-web/security-area/home.htm");
		selenium.click("link=Nuovo");
		selenium.waitForPageToLoad("30000");
		selenium.type("userId", prop.getProperty("username"));
		selenium.type("password", prop.getProperty("password"));
		selenium.type("confermaPassword", prop.getProperty("password"));
		selenium.type("cf", "CNNSVM64A70L219O");
		selenium.type("cognome", "cognome");
		selenium.type("nome", "nome");
		selenium.click("_finish");
		selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Operazione eseguita con successo"));

        return selenium;
	}

    @Override
    public void testNew() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setSelenium(Selenium selenium) {
        super.selenium = selenium;
    }

    @Override
    public Selenium getSelenium() {
        return super.selenium;
    }

}
