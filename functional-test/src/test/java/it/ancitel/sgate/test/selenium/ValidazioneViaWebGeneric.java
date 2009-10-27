/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.ancitel.sgate.test.selenium;

import com.thoughtworks.selenium.Selenium;

import it.ancitel.sgate.test.util.CalendarDateFormat;
import it.ancitel.sgate.test.util.SgateSeleneseTestCase;
import it.ancitel.test.infrastruttura.interfaces.SeleniumNavigation;

import java.util.Calendar;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 *
 * @author kewell
 */
public class ValidazioneViaWebGeneric extends SgateSeleneseTestCase implements SeleniumNavigation{
    private final static Logger logger = Logger.getLogger(CessazioneDomandaConMotivazioni.class);

    @Override
	public Selenium executeNavigation(Properties prop) throws Exception {
        super.setUp(prop.getProperty("SGATE_WEB_URL"));

          
        selenium.open("/sgate-web/security-area/home.htm");
		selenium.click("link=Ricerca");
		selenium.waitForPageToLoad("90000");
		selenium.type("idDomanda", prop.getProperty("idDomanda"));
		selenium.click("//input[@name='ricercaRichieste']");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Validazione");
		selenium.waitForPageToLoad("30000");
		
		if (prop.getProperty("inizioRetro")!=null){
			Calendar inizioRetro = Calendar.getInstance();
			Calendar fineRetro = Calendar.getInstance();
			inizioRetro.setTime(CalendarDateFormat.simpleDate.parse(prop.getProperty("inizioRetro")));
			fineRetro.setTime(CalendarDateFormat.simpleDate.parse(prop.getProperty("fineRetro")));

			selenium.type("giornoInizioRetroattivitaDistributore",String.valueOf(inizioRetro.get(Calendar.DAY_OF_MONTH)));
			selenium.type("meseInizioRetroattivitaDistributore",String.valueOf(inizioRetro.get(Calendar.MONTH)+1));
			selenium.type("annoInizioRetroattivitaDistributore",String.valueOf(inizioRetro.get(Calendar.YEAR)));
			
			selenium.type("giornoFineRetroattivitaDistributore",String.valueOf(fineRetro.get(Calendar.DAY_OF_MONTH)));
			selenium.type("meseFineRetroattivitaDistributore",String.valueOf(fineRetro.get(Calendar.MONTH)+1));
			selenium.type("annoFineRetroattivitaDistributore",String.valueOf(fineRetro.get(Calendar.YEAR)));
		}
        
		
		selenium.type("esitoRichiestaBean.motivazione","Creata con selenium ide");
		selenium.click(prop.getProperty("commandButton"));
		selenium.waitForPageToLoad("30000");


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
