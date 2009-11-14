/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.xmlblackbox.test.selenium.xmlblackbox;

import com.thoughtworks.selenium.Selenium;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.xmlblackbox.test.infrastructure.interfaces.Repository;
import org.xmlblackbox.test.infrastructure.interfaces.SeleniumNavigation;
import org.xmlblackbox.test.infrastructure.util.MemoryData;
import org.xmlblackbox.test.util.SgateSeleneseTestCase;

/**
 *
 * @author kewell
 */
public class CessazioneDomandaConMotivazioniXBB extends SgateSeleneseTestCase implements SeleniumNavigation{

    private final static Logger logger = Logger.getLogger(CessazioneDomandaConMotivazioniXBB.class);

    @Override
	public Selenium executeNavigation(MemoryData memory) throws Exception {

        Properties prop = memory.getRepository(Repository.WEB_NAVIGATION);
        super.setUp(prop.getProperty("EXAMPLE_WEB_URL"));

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
