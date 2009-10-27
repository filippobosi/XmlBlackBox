package it.ancitel.sgate.test.selenium;

import com.thoughtworks.selenium.*;
import it.ancitel.sgate.test.util.SgateSeleneseTestCase;
import it.ancitel.test.infrastruttura.interfaces.SeleniumNavigation;
import java.util.Properties;
import org.apache.log4j.Logger;


public class VisualizzazioneEsitiAnagraficaConEsito extends SgateSeleneseTestCase implements SeleniumNavigation{
	private final static Logger logger = Logger.getLogger(VisualizzazioneEsitiAnagraficaConEsito.class);

    @Override
	public Selenium executeNavigation(Properties prop) throws Exception {

        super.setUp(prop.getProperty("SGATE_WEB_URL"));
        

        selenium.click("link=Visualizzazione esiti");
        selenium.waitForPageToLoad("30000");
        selenium.select("abstrPaginatorBean.resultList0.esitoComplessivo", "label=POSITIVO");
        selenium.click("conferma");
        selenium.waitForPageToLoad("30000");
        selenium.click("salva");
        selenium.waitForPageToLoad("30000");

        logger.info("Stop per 10 secondi");
        Thread.sleep(20000);

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