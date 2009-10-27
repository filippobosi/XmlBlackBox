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
public class EsitoSegnalazioneViaWeb extends SgateSeleneseTestCase implements SeleniumNavigation{
    private final static Logger logger = Logger.getLogger(CessazioneDomandaConMotivazioni.class);

    @Override
	public Selenium executeNavigation(Properties prop) throws Exception {
        super.setUp(prop.getProperty("SGATE_WEB_URL"));

        selenium.open("/sgate-web/security-area/home.htm");
		selenium.click("link=Segnalazioni");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=visualizza elenco");
		selenium.waitForPageToLoad("30000");
        selenium.click("//input[@name='scroller' and @value='Ultima']");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent(prop.getProperty("idSegnalazione")));
        selenium.open("/sgate-web/security-area/impresa/cessa.htm?idSegnalazione=" + prop.getProperty("idSegnalazione") + "&idRichiesta=" + prop.getProperty("idRichiesta"));
        selenium.waitForPageToLoad("30000");
		selenium.click("//input[@value='Chiudi']");
		selenium.waitForPageToLoad("30000");


        //logger.info("Stop per 10 secondi");
        //Thread.sleep(10000);

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
