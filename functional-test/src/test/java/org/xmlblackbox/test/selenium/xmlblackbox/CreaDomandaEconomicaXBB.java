package org.xmlblackbox.test.selenium.xmlblackbox;

import com.thoughtworks.selenium.*;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.xmlblackbox.test.infrastructure.interfaces.Repository;
import org.xmlblackbox.test.infrastructure.interfaces.SeleniumNavigation;
import org.xmlblackbox.test.infrastructure.util.MemoryData;
import org.xmlblackbox.test.infrastructure.util.SeleniumEnvironment;


public class CreaDomandaEconomicaXBB extends SeleniumEnvironment implements SeleniumNavigation{
	private final static Logger logger = Logger.getLogger(CreaDomandaEconomicaXBB.class);

    @Override
	public Selenium executeNavigation(MemoryData memory) throws Exception {

        Properties prop = memory.getRepository(Repository.WEB_NAVIGATION);
        logger.debug("SELENIUM PROPERTIES : " + prop);
    	
        //super.setUp(prop.getProperty("SGATE_WEB_URL"));

		selenium.waitForPageToLoad("30000");
		selenium.click("link=Nuova");
		selenium.waitForPageToLoad("30000");
		
		// Pagina 1..
		selenium.select("tipologiaDisagioBean.codTipoDisagio", "label=ECONOMICO");
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
		selenium.click("_target1");
		selenium.waitForPageToLoad("30000");
		
		// Pagina 2..
		selenium.type("clienteDomesticoBean.codiceFiscale", prop.getProperty("codice_fiscale"));
		selenium.type("clienteDomesticoBean.cognome", prop.getProperty("cognome"));
		selenium.type("clienteDomesticoBean.nome", prop.getProperty("nome"));
		selenium.type("clienteDomesticoBean.giornoNascita", prop.getProperty("data_nascita_giorno"));
		selenium.type("clienteDomesticoBean.meseNascita", prop.getProperty("data_nascita_mese"));
		selenium.type("clienteDomesticoBean.annoNascita", prop.getProperty("data_nascita_anno"));
		selenium.type("clienteDomesticoBean.descrNazNascita", "STATI UNITI");
		selenium.select("clienteDomesticoBean.sesso", "label=M");
		selenium.click("privacy1");
		selenium.click("_target2");
		selenium.waitForPageToLoad("30000");
		
		// Pagina 3..
		selenium.type("numPod", prop.getProperty("codice_pod"));
		selenium.type("potenzaImpegnata", prop.getProperty("potenza_impegnata"));
		selenium.type("indirizzoDaAgevolare", "VIA ROMA");
		selenium.type("civDaAgevolare", "3");
		selenium.type("capDaAgevolare", "00100");

		selenium.click("usoDomesticoresidente1");
		selenium.type("numCompFamAnag", "1");
		selenium.click("_target3");
		selenium.waitForPageToLoad("30000");

		// Pagina 4..
		selenium.type("attestazioneIseeBean.numCompNucleoFamigl", "1");
		selenium.type("attestazioneIseeBean.codAttIsee", prop.getProperty("codice_isee"));
		selenium.type("attestazioneIseeBean.valIsee", "4321");
        if (prop.getProperty("giornoRilascioIsee") != null && prop.getProperty("meseRilascioIsee") != null && prop.getProperty("annoRilascioIsee") != null && prop.getProperty("giornoScadenzaIsee") != null && prop.getProperty("meseScadenzaIsee") != null && prop.getProperty("annoScadenzaIsee") != null) {
            selenium.type("attestazioneIseeBean.giornoRilascioAtt", prop.getProperty("giornoRilascioIsee"));
            selenium.type("attestazioneIseeBean.meseRilascioAtt", prop.getProperty("meseRilascioIsee"));
            selenium.type("attestazioneIseeBean.annoRilascioAtt", prop.getProperty("annoRilascioIsee"));
            selenium.type("attestazioneIseeBean.giornoScadenzaAtt", prop.getProperty("giornoScadenzaIsee"));
            selenium.type("attestazioneIseeBean.meseScadenzaAtt", prop.getProperty("meseScadenzaIsee"));
            selenium.type("attestazioneIseeBean.annoScadenzaAtt", prop.getProperty("annoScadenzaIsee"));
        } else {
            selenium.type("attestazioneIseeBean.giornoRilascioAtt", "01");
            selenium.type("attestazioneIseeBean.meseRilascioAtt", "01");
            selenium.type("attestazioneIseeBean.annoRilascioAtt", "2009");
            selenium.type("attestazioneIseeBean.giornoScadenzaAtt", "01");
            selenium.type("attestazioneIseeBean.meseScadenzaAtt", "01");
            selenium.type("attestazioneIseeBean.annoScadenzaAtt", "2010");
        }
		
		selenium.click("_target6");
		selenium.waitForPageToLoad("30000");

		// Pagina 5
		String idDomanda = selenium.getText("//div[contains(text(),\"Domanda salvata con ID domanda:\")]");
		idDomanda = idDomanda.replaceAll("Domanda salvata con ID domanda: ", "");
		idDomanda = idDomanda.trim();
		prop.setProperty("idDomanda", idDomanda);
		
		selenium.click("confermaStampa1");
		selenium.click("_target7");
		selenium.waitForPageToLoad("30000");


		// Pagina 6
		selenium.click("segnalaWarning1");
		selenium.type("noteUtente","Creata con selenium ide");
		selenium.click(prop.getProperty("click_button_finale"));
		selenium.waitForPageToLoad("360000");
		selenium.click("link=Home");
		selenium.waitForPageToLoad("30000");

        //Thread.sleep(10000);
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