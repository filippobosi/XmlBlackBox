package it.ancitel.sgate.test.selenium;

import com.thoughtworks.selenium.*;
import it.ancitel.sgate.test.util.SgateSeleneseTestCase;
import it.ancitel.test.infrastruttura.interfaces.SeleniumNavigation;
import java.util.Properties;
import org.apache.log4j.Logger;

public class VerificaLinkCessazione extends SgateSeleneseTestCase implements SeleniumNavigation {
    private final static Logger logger = Logger.getLogger(VerificaLinkCessazione.class);

	@Override
	public Selenium executeNavigation(Properties prop) throws Exception {
		super.setUp(prop.getProperty("SGATE_WEB_URL"));
		
		logger.debug("\n******VerificaLinkCessazione******");
		String idDomanda=prop.getProperty("idDomanda");
		logger.debug("\n idDomanda "+idDomanda);
		
		selenium.open("/sgate-web/security-area/home.htm");
		selenium.click("link=Ricerca");
        selenium.type("idDomanda",idDomanda);
		selenium.click("//input[@name='ricercaRichieste']");
        selenium.waitForPageToLoad("30000");
        verifyFalse(selenium.isTextPresent("Visualizza Dati"));
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
