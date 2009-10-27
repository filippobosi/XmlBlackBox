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
public class VariazioneResidenza extends SgateSeleneseTestCase implements SeleniumNavigation{
    private final static Logger logger = Logger.getLogger(CessazioneDomandaConMotivazioni.class);

    @Override
	public Selenium executeNavigation(Properties prop) throws Exception {
        

        /* 
         * ATTENZIONE VA ESEGUITO DOPO AVER RICERCATO 
         * LA DOMANDA CON LA CLASSE SELENIUM RICERCA DOMANDA 
         */
        
        // PAGINA DI RICERCA
		selenium.click("link=Visualizza Dati");
		selenium.waitForPageToLoad("30000");

        // PAGINA DETTAGLIO
		selenium.click("link=Variazione Residenza");
		selenium.waitForPageToLoad("30000");

        // PAGINA 1
        if (prop.getProperty("protocollo_comunale") != null) {
            selenium.type("idProtocolloComunale", prop.getProperty("protocollo_comunale"));
            selenium.type("giornoProtocolloComunale", prop.getProperty("giornoProtocolloComunale"));
            selenium.type("meseProtocolloComunale", prop.getProperty("meseProtocolloComunale"));
            selenium.type("annoProtocolloComunale", prop.getProperty("annoProtocolloComunale"));
        } else {
            selenium.type("idProtocolloComunale", "");
            selenium.type("giornoProtocolloComunale", "");
            selenium.type("meseProtocolloComunale", "");
            selenium.type("annoProtocolloComunale", "");
        }
		selenium.click("_eventId_avanti");
		selenium.waitForPageToLoad("30000");

        // PAGINA 2
		selenium.click("privacy1");
		selenium.click("_eventId_avanti");
		selenium.waitForPageToLoad("30000");

        // PAGINA 3
		selenium.type("numPod", prop.getProperty("codice_pod"));
		selenium.type("indirizzoDaAgevolare", "VIA A8888");
        if (prop.getProperty("altraProvincia") != null && prop.getProperty("altroComune") != null) {
            selenium.select("codProvDaAgevolare", "label=PRATO");
            selenium.select("codProvDaAgevolare", "label=MACERATA");
            selenium.click("_eventId_loadComuniDaAgev");
            selenium.waitForPageToLoad("30000");
            selenium.select("codComuneDaAgevolare", "label=APIRO");
        }
        if (prop.getProperty("potenzaImpegnata") != null) {
            selenium.type("potenzaImpegnata", prop.getProperty("potenzaImpegnata"));
        } else {
            selenium.type("potenzaImpegnata", "2.0");
        }
		selenium.type("capDaAgevolare", "00811");
        if (prop.getProperty("numCompFamAnag") != null) {
            selenium.type("numCompFamAnag", prop.getProperty("numCompFamAnag"));
        } else {
            selenium.type("numCompFamAnag", "2");
        }
		selenium.type("civDaAgevolare", "4");
		selenium.click("usoDomesticoresidente1");
		selenium.click("_eventId_avanti");
		selenium.waitForPageToLoad("30000");

        // PAGINA 4
        String idVariazione = selenium.getText("//div[contains(text(),\"Domanda salvata con id\")]");
		idVariazione = idVariazione.replaceAll("Domanda salvata con id ", "");
		idVariazione = idVariazione.trim();
        prop.setProperty("idVariazione", idVariazione);
		selenium.click("confermaStampa1");
		selenium.click("_eventId_avanti");
		selenium.waitForPageToLoad("30000");
        
        // PAGINA 5
        selenium.click("_eventId_presentaDomanda");
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
