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
public class PresaInCaricoViaWebFile extends SgateSeleneseTestCase implements SeleniumNavigation{
    private final static Logger logger = Logger.getLogger(CessazioneDomandaConMotivazioni.class);

    @Override
	public Selenium executeNavigation(Properties prop) throws Exception {
        super.setUp(prop.getProperty("SGATE_WEB_URL"));

        selenium.open("/sgate-web/security-area/impresa/richiestaComunicazione.htm");
		selenium.type("idLotto", prop.getProperty("idLottoGenerazioneRich"));
        selenium.select("idStato", "label=regexp:\\s");
		selenium.click("cmdRic");
		selenium.waitForPageToLoad("60000");
		selenium.click("link=Download");
        Thread.sleep(3000);
		selenium.click("//input[@name='cmdRic' and @value='Refresh']");
        //Thread.sleep(3000);
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Prendi in carico");
		selenium.waitForPageToLoad("60000");


        logger.info("Stop per 10 secondi");
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
