package it.ancitel.sgate.test.selenium;

import com.thoughtworks.selenium.*;
import it.ancitel.sgate.test.util.SgateSeleneseTestCase;
import it.ancitel.test.infrastruttura.interfaces.SeleniumNavigation;
import java.util.Calendar;
import java.util.Properties;
import org.apache.log4j.Logger;

public class CreaDomandaEconomicaAltroComune extends SgateSeleneseTestCase implements SeleniumNavigation {

    private final static Logger logger = Logger.getLogger(CreaDomandaEconomicaAltroComune.class);

    @Override
    public Selenium executeNavigation(Properties prop) throws Exception {
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
//		selenium.type("codProvDaAgevolare", prop.getProperty("codice_provincia_comune"));
//		selenium.type("codComuneDaAgevolare", prop.getProperty("codice_comune"));

        selenium.select("codProvDaAgevolare", "value=" + prop.getProperty("codice_provincia_comune"));
        selenium.click("_caricaComuniDaAgev");
        selenium.waitForPageToLoad("30000");
        selenium.select("codComuneDaAgevolare", "value=" + prop.getProperty("codice_comune"));


        selenium.click("usoDomesticoresidente1");
        selenium.type("numCompFamAnag", "1");
        selenium.click("podAttivo1");
        selenium.click("_target3");
        selenium.waitForPageToLoad("30000");

//            <parameter name="codProvDaAgevolare" value="${codice_provincia_comune}" />
//    <parameter name="codComuneDaAgevolare" value="${codice_comune}" />
//    <parameter name="_target3" value="Avanti" />
//    <parameter name="_page" value="2" />
//
//                    <parameter name="clienteDomesticoBean.codProvResidenza" value="${codice_provincia_altro_comune}" />
//    <parameter name="clienteDomesticoBean.indirizzoResidenza" value="Via qualcosa" />
//    <parameter name="clienteDomesticoBean.codIstatComuneResidenza" value="${codice_altro_comune}" /

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
            Calendar now = Calendar.getInstance();

            String giorno = "" + now.get(Calendar.DAY_OF_MONTH);
            String mese = "" + now.get(Calendar.MONTH);
            String anno = "" + now.get(Calendar.YEAR);
            String annoPiuUno = "" + (now.get(Calendar.YEAR) + 1);

            if (giorno.length() == 1) {
                giorno = "0" + giorno;
            }
            if (mese.length() == 1) {
                mese = "0" + mese;
            }
            selenium.type("attestazioneIseeBean.giornoRilascioAtt", giorno);
            selenium.type("attestazioneIseeBean.meseRilascioAtt", mese);
            selenium.type("attestazioneIseeBean.annoRilascioAtt", anno);
            selenium.type("attestazioneIseeBean.giornoScadenzaAtt", giorno);
            selenium.type("attestazioneIseeBean.meseScadenzaAtt", mese);
            selenium.type("attestazioneIseeBean.annoScadenzaAtt", annoPiuUno);
        }



        //logger.info("codice_provincia_altro_comune "+prop.getProperty("codice_provincia_altro_comune"));
        //logger.info("codice_altro_comune "+prop.getProperty("codice_altro_comune"));

        //selenium.select("clienteDomesticoBean.codProvResidenza", "value=" + prop.getProperty("codice_provincia_altro_comune"));
        //selenium.select("clienteDomesticoBean.codIstatComuneResidenza", "value=" + prop.getProperty("codice_altro_comune"));

        selenium.type("clienteDomesticoBean.indirizzoResidenza", "indirizzo test");
        selenium.type("clienteDomesticoBean.numCivResidenza", "44");
        selenium.type("clienteDomesticoBean.capResidenza", "00100");

        //		selenium.type("clienteDomesticoBean.codProvResidenza", prop.getProperty("codice_provincia_altro_comune"));
//		selenium.type("clienteDomesticoBean.codIstatComuneResidenza", prop.getProperty("codice_altro_comune"));

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
        selenium.type("noteUtente", "Creata con selenium ide");
        selenium.click(prop.getProperty("click_button_finale"));
        selenium.waitForPageToLoad("360000");
        selenium.click("link=Home");
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