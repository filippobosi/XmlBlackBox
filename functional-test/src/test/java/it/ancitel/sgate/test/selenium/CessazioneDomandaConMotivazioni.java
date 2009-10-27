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
public class CessazioneDomandaConMotivazioni extends SgateSeleneseTestCase implements SeleniumNavigation{

    private final static Logger logger = Logger.getLogger(CessazioneDomandaConMotivazioni.class);

    @Override
	public Selenium executeNavigation(Properties prop) throws Exception {
        super.setUp(prop.getProperty("SGATE_WEB_URL"));

        selenium.open("/sgate-web/security-area/home.htm");
		selenium.click("link=Ricerca ed Estrai");
		selenium.waitForPageToLoad("30000");
		selenium.type("idDomanda", prop.getProperty("idDomanda"));
		selenium.click("//input[@value='Ricerca']");
		selenium.waitForPageToLoad("90000");
		selenium.click("link=Visualizza Dati");
		selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isElementPresent("//input[@name='_cessaDomanda']"));
        //selenium.waitForPageToLoad("90000");
        selenium.click("_cessaDomanda");
		selenium.waitForPageToLoad("90000");
		selenium.addSelection("motivazioni", "label=Numero civico diverso da quello dichiarato");
		selenium.addSelection("motivazioni", "label=Comune di residenza diverso da quello dichiarato");
		selenium.click("_segnalaCessazione");
		selenium.waitForPageToLoad("90000");
        selenium.isTextPresent("Operazione di segnalazione cessazione eseguita correttamente. Numero di domande cessate: 1");
        

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
