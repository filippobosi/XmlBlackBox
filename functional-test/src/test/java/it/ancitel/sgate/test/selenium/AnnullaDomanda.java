package it.ancitel.sgate.test.selenium;

import com.thoughtworks.selenium.*;
import it.ancitel.sgate.test.util.SgateSeleneseTestCase;
import it.ancitel.test.infrastruttura.interfaces.SeleniumNavigation;
import java.util.Properties;
import org.apache.log4j.Logger;


public class AnnullaDomanda extends SgateSeleneseTestCase implements SeleniumNavigation{
	private final static Logger logger = Logger.getLogger(AnnullaDomanda.class);

    @Override
	public Selenium executeNavigation(Properties prop) throws Exception {

        super.setUp(prop.getProperty("SGATE_WEB_URL"));

		selenium.click("link=Ricerca ed Estrai");
		selenium.waitForPageToLoad("30000");
		selenium.type("idDomanda", prop.getProperty("idDomandaAnnullata"));
		selenium.click("//input[@value='Ricerca']");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Visualizza Dati");
		selenium.waitForPageToLoad("30000");
		selenium.click("_annullaDomanda");
		selenium.waitForPageToLoad("30000");
		assertTrue("L'annullamento della domanda non è andato a buon fine, non rilevo il messaggio di operazione completata con successo!",selenium.isTextPresent("Operazione eseguita con successo"));
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