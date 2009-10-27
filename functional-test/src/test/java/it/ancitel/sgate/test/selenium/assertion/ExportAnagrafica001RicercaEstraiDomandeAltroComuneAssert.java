package it.ancitel.sgate.test.selenium.assertion;

import com.thoughtworks.selenium.*;
import it.ancitel.sgate.test.util.SgateSeleneseTestCase;
import it.ancitel.test.infrastruttura.interfaces.SeleniumNavigation;
import java.util.Properties;
import org.apache.log4j.Logger;


public class ExportAnagrafica001RicercaEstraiDomandeAltroComuneAssert extends SgateSeleneseTestCase implements SeleniumNavigation{
	private final static Logger logger = Logger.getLogger(ExportAnagrafica001RicercaEstraiDomandeAltroComuneAssert.class);

    @Override
	public Selenium executeNavigation(Properties prop) throws Exception {

        super.setUp(prop.getProperty("SGATE_WEB_URL"));

        logger.debug("VERIFICA PRESENZA DOMANDA idDomandaAmmessa : " + prop.getProperty("idDomandaAmmessa"));
        logger.debug("VERIFICA PRESENZA DOMANDA idDomandaSospesa : " + prop.getProperty("idDomandaSospesa"));
        logger.debug("VERIFICA PRESENZA DOMANDA idDomandaCessata : " + prop.getProperty("idDomandaCessata"));
        logger.debug("VERIFICA PRESENZA DOMANDA idDomandaPresentataDaAltroComune : " + prop.getProperty("idDomandaPresentataDaAltroComune"));
		logger.debug("VERIFICA PRESENZA DOMANDA idDomandaAnnullata : " + prop.getProperty("idDomandaAnnullata"));
		logger.debug("VERIFICA PRESENZA DOMANDA idDomandaNonAmmissibile : " + prop.getProperty("idDomandaNonAmmissibile"));
		logger.debug("VERIFICA PRESENZA DOMANDA idDomandaInLavorazione : " + prop.getProperty("idDomandaInLavorazione"));
		logger.debug("VERIFICA PRESENZA DOMANDA idDomandaPresaInCarico : " + prop.getProperty("idDomandaPresaInCarico"));
		logger.debug("VERIFICA PRESENZA DOMANDA idDomandaValidata : " + prop.getProperty("idDomandaValidata"));
		logger.debug("VERIFICA PRESENZA DOMANDA idDomandaNonValidata: " + prop.getProperty("idDomandaNonValidata"));


        logger.debug("selenium.isTextPresent(prop.getProperty(\"idDomandaAmmessa\"): " + selenium.isTextPresent(prop.getProperty("idDomandaAmmessa")));
        assertFalse("Non trovata La domanda ammessa ", selenium.isTextPresent(prop.getProperty("idDomandaAmmessa")));
		logger.debug("selenium.isTextPresent(prop.getProperty(\"idDomandaSospesa\"): " + selenium.isTextPresent(prop.getProperty("idDomandaSospesa")));
		assertFalse("Non trovata La domanda sospesa ", selenium.isTextPresent(prop.getProperty("idDomandaSospesa")));
		logger.debug("selenium.isTextPresent(prop.getProperty(\"idDomandaCessata\"): " + selenium.isTextPresent(prop.getProperty("idDomandaCessata")));
		assertFalse("Non trovata La domanda cessata ", selenium.isTextPresent(prop.getProperty("idDomandaCessata")));
        logger.debug("selenium.isTextPresent(prop.getProperty(\"idDomandaPresentataDaAltroComune\"): " + selenium.isTextPresent(prop.getProperty("idDomandaPresentataDaAltroComune")));
        assertTrue("Non trovata La domanda presentata da altro comune ", selenium.isTextPresent(prop.getProperty("idDomandaPresentataDaAltroComune")));
        logger.debug("selenium.isTextPresent(prop.getProperty(\"idDomandaAnnullata\"): " + selenium.isTextPresent(prop.getProperty("idDomandaAnnullata")));
        assertFalse("Non trovata La domanda annullata ", selenium.isTextPresent(prop.getProperty("idDomandaAnnullata")));
        logger.debug("selenium.isTextPresent(prop.getProperty(\"idDomandaNonAmmissibile\"): " + selenium.isTextPresent(prop.getProperty("idDomandaNonAmmissibile")));
        assertFalse("Non trovata La domanda non ammissibile ", selenium.isTextPresent(prop.getProperty("idDomandaNonAmmissibile")));
        logger.debug("selenium.isTextPresent(prop.getProperty(\"idDomandaInLavorazione\"): " + selenium.isTextPresent(prop.getProperty("idDomandaInLavorazione")));
        assertFalse("Non trovata La domanda in lavorazione ", selenium.isTextPresent(prop.getProperty("idDomandaInLavorazione")));
        logger.debug("selenium.isTextPresent(prop.getProperty(\"idDomandaPresaInCarico\"): " + selenium.isTextPresent(prop.getProperty("idDomandaPresaInCarico")));
        assertFalse("Non trovata La domanda presa in carico ", selenium.isTextPresent(prop.getProperty("idDomandaPresaInCarico")));
        logger.debug("selenium.isTextPresent(prop.getProperty(\"idDomandaValidata\"): " + selenium.isTextPresent(prop.getProperty("idDomandaValidata")));
        assertFalse("Non trovata La domanda validata ", selenium.isTextPresent(prop.getProperty("idDomandaValidata")));
        logger.debug("selenium.isTextPresent(prop.getProperty(\"idDomandaNonValidata\"): " + selenium.isTextPresent(prop.getProperty("idDomandaNonValidata")));
        assertFalse("Non trovata La domanda non validata ", selenium.isTextPresent(prop.getProperty("idDomandaNonValidata")));


		        
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