package it.ancitel.sgate.test.selenium;

import com.thoughtworks.selenium.*;

import it.ancitel.sgate.test.util.SgateSeleneseTestCase;
import it.ancitel.test.infrastruttura.interfaces.SeleniumNavigation;

import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class CessazioneDistributoreViaWebOk extends SgateSeleneseTestCase implements SeleniumNavigation {
		
	 private final static Logger logger = Logger.getLogger(CessazioneDistributoreViaWebOk.class);

	@Override
	public Selenium executeNavigation(Properties prop) throws Exception {
		super.setUp(prop.getProperty("SGATE_WEB_URL"));
		
		logger.debug("\n******CessazioneDistributoreViaWebOk******");
			
		String idDomanda=prop.getProperty("idDomanda");
		String numeroPod=prop.getProperty("numeroPod");
		String dataCessazione=prop.getProperty("dataCessazione");
		logger.debug("\n dataCessazione: "+dataCessazione);
		
		String [] dateSeparatori= dataCessazione.split("-");
		if (dateSeparatori.length!=3){
			logger.debug("\n dataCessazione in fomato non corretto ");
			throw new Exception("dataCessazione in fomato non corretto");
		}
		String giornoCessazione=dateSeparatori[2];
		String meseCessazione=dateSeparatori[1];
		String annoCessazione=dateSeparatori[0];
		
		
		logger.debug("\n idDomanda "+idDomanda);
		logger.debug("\n numeroPod "+numeroPod);
		logger.debug("\n giornoCessazione "+giornoCessazione);
		logger.debug("\n meseCessazione "+meseCessazione);
		logger.debug("\n annoCessazione "+annoCessazione);
		
		selenium.open("/sgate-web/security-area/home.htm");
		selenium.click("link=Ricerca");
		selenium.waitForPageToLoad("30000");
		selenium.type("idDomanda", idDomanda);
		selenium.click("//input[@name='ricercaRichieste']");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Visualizza Dati");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Cessa richiesta");
		selenium.waitForPageToLoad("30000");
		selenium.type("giornoCessazione", giornoCessazione);
		selenium.type("meseCessazione", meseCessazione);
		selenium.type("annoCessazione", annoCessazione);
		selenium.type("protocolloSegnalazioneDistributore", numeroPod);
		selenium.click("_segnalaCessazione");
		selenium.waitForPageToLoad("30000");

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
