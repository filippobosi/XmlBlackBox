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
public class ValidazioneViaWeb extends SgateSeleneseTestCase implements SeleniumNavigation{
    private final static Logger logger = Logger.getLogger(CessazioneDomandaConMotivazioni.class);

    @Override
    @Deprecated
	public Selenium executeNavigation(Properties prop) throws Exception {
        super.setUp(prop.getProperty("SGATE_WEB_URL"));

        // -------------------------------------------------
        // SI PREGA DI USARE : ValidazioneViaWebGeneric
        // DEPRECATED - DEPRECATED - DEPRECATED
        // -------------------------------------------------
        
        selenium.open("/sgate-web/security-area/home.htm");
		selenium.click("link=Ricerca");
		selenium.waitForPageToLoad("90000");
		selenium.type("idDomanda", prop.getProperty("idDomanda"));
		selenium.click("//input[@name='ricercaRichieste']");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Validazione");
		selenium.waitForPageToLoad("30000");
		selenium.click("valida");
		selenium.waitForPageToLoad("30000");


        //logger.info("Stop per 10 secondi");
        //Thread.sleep(20000);

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
