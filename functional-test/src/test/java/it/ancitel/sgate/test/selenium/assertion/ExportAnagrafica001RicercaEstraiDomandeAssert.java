package it.ancitel.sgate.test.selenium.assertion;

import com.thoughtworks.selenium.*;
import it.ancitel.sgate.test.util.SgateSeleneseTestCase;
import it.ancitel.test.infrastruttura.interfaces.SeleniumNavigation;
import java.util.Properties;
import org.apache.log4j.Logger;


public class ExportAnagrafica001RicercaEstraiDomandeAssert extends SgateSeleneseTestCase implements SeleniumNavigation{
	private final static Logger logger = Logger.getLogger(ExportAnagrafica001RicercaEstraiDomandeAssert.class);

    @Override
	public Selenium executeNavigation(Properties prop) throws Exception {

        //super.setUp(prop.getProperty("SGATE_WEB_URL"));

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
        assertTrue("Non trovata La domanda ammessa "+prop.getProperty("idDomandaAmmessa"), selenium.isTextPresent(prop.getProperty("idDomandaAmmessa")));
        logger.debug("selenium.isTextPresent(prop.getProperty(\"idDomandaSospesa\"): " + selenium.isTextPresent(prop.getProperty("idDomandaSospesa")));
		assertTrue("Non trovata La domanda sospesa "+prop.getProperty("idDomandaSospesa"), selenium.isTextPresent(prop.getProperty("idDomandaSospesa")));
        logger.debug("selenium.isTextPresent(prop.getProperty(\"idDomandaCessata\"): " + selenium.isTextPresent(prop.getProperty("idDomandaCessata")));
		assertFalse("E' stata trovata La domanda cessata "+prop.getProperty("idDomandaCessata"),selenium.isTextPresent(prop.getProperty("idDomandaCessata")));
        logger.debug("selenium.isTextPresent(prop.getProperty(\"idDomandaAnnullata\"): " + selenium.isTextPresent(prop.getProperty("idDomandaAnnullata")));
        assertFalse("E' stata trovata La domanda annullata "+prop.getProperty("idDomandaAnnullata"),selenium.isTextPresent(prop.getProperty("idDomandaAnnullata")));
        logger.debug("selenium.isTextPresent(prop.getProperty(\"idDomandaNonAmmissibile\"): " + selenium.isTextPresent(prop.getProperty("idDomandaNonAmmissibile")));
        assertFalse("E' stata trovata La domanda non ammissibile "+prop.getProperty("idDomandaNonAmmissibile"),selenium.isTextPresent(prop.getProperty("idDomandaNonAmmissibile")));
        logger.debug("selenium.isTextPresent(prop.getProperty(\"idDomandaInLavorazione\"): " + selenium.isTextPresent(prop.getProperty("idDomandaInLavorazione")));
        assertFalse("E' stata trovata La domanda in lavorazione "+prop.getProperty("idDomandaInLavorazione"),selenium.isTextPresent(prop.getProperty("idDomandaInLavorazione")));
        logger.debug("selenium.isTextPresent(prop.getProperty(\"idDomandaPresaInCarico\"): " + selenium.isTextPresent(prop.getProperty("idDomandaPresaInCarico")));
        assertTrue("Non trovata La domanda presa in carico "+prop.getProperty("idDomandaPresaInCarico"),selenium.isTextPresent(prop.getProperty("idDomandaPresaInCarico")));
        logger.debug("selenium.isTextPresent(prop.getProperty(\"idDomandaValidata\"): " + selenium.isTextPresent(prop.getProperty("idDomandaValidata")));
        assertTrue("Non trovata La domanda validata "+prop.getProperty("idDomandaValidata"),selenium.isTextPresent(prop.getProperty("idDomandaValidata")));
        logger.debug("selenium.isTextPresent(prop.getProperty(\"idDomandaNonValidata\"): " + selenium.isTextPresent(prop.getProperty("idDomandaNonValidata")));
        assertFalse("E' stata trovata La domanda non validata "+prop.getProperty("idDomandaNonValidata"),selenium.isTextPresent(prop.getProperty("idDomandaNonValidata")));
        logger.debug("selenium.isTextPresent(prop.getProperty(\"idDomandaPresentataDaAltroComune\"): " + selenium.isTextPresent(prop.getProperty("idDomandaPresentataDaAltroComune")));
        assertFalse("E' stata trovata La domanda presentata da altro comune "+prop.getProperty("idDomandaPresentataDaAltroComune"),selenium.isTextPresent(prop.getProperty("idDomandaPresentataDaAltroComune")));

		        
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