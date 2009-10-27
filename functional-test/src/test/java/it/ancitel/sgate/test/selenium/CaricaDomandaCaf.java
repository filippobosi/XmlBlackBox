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
public class CaricaDomandaCaf extends SgateSeleneseTestCase implements SeleniumNavigation{
	private final static Logger logger = Logger.getLogger(CreaDomandaEconomica.class);

    @Override
	public Selenium executeNavigation(Properties prop) throws Exception {
    	logger.debug("SELENIUM PROPERTIES : " + prop);

        selenium.open("/sgate-web/security-area/home.htm");
		selenium.click("link=Invia Domande Agevolazione");
		selenium.waitForPageToLoad("120000");
        selenium.type("file", prop.getProperty("absolutePath"));
		selenium.click("cmdUpload");
		selenium.waitForPageToLoad("30000");
        String idLotto = selenium.getText("//div[contains(text(),\"Invio comunicazione generata con id lotto\")]");
		idLotto = idLotto.replaceAll("Invio comunicazione generata con id lotto ", "");
		idLotto = idLotto.trim();
		prop.setProperty("idLottoDomandaCaf", idLotto);


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
