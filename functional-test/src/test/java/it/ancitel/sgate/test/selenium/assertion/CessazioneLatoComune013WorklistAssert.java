/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.ancitel.sgate.test.selenium.assertion;

import com.thoughtworks.selenium.Selenium;
import it.ancitel.sgate.test.util.SgateSeleneseTestCase;
import it.ancitel.test.infrastruttura.interfaces.SeleniumNavigation;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 *
 * @author kewell
 */
public class CessazioneLatoComune013WorklistAssert extends SgateSeleneseTestCase implements SeleniumNavigation{
	private final static Logger logger = Logger.getLogger(CessazioneLatoComune013WorklistAssert.class);

    @Override
	public Selenium executeNavigation(Properties prop) throws Exception {

        super.setUp(prop.getProperty("SGATE_WEB_URL"));

        logger.debug("VERIFICA PRESENZA SEGNALAZIONE : " + prop.getProperty("idSegnalazione"));
        while (selenium.isElementPresent("//input[@name='scroller' and @value='Successiva']")) {
            assertTrue(!selenium.isTextPresent(prop.getProperty("idSegnalazione")));
            selenium.click("//input[@name='scroller' and @value='Successiva']");
            selenium.waitForPageToLoad("30000");
        }
        assertTrue(!selenium.isTextPresent(prop.getProperty("idSegnalazione")));

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
