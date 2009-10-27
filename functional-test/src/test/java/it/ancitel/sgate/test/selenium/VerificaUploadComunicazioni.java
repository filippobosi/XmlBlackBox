package it.ancitel.sgate.test.selenium;

import com.thoughtworks.selenium.*;

import it.ancitel.sgate.test.util.SgateSeleneseTestCase;
import it.ancitel.test.infrastruttura.interfaces.SeleniumNavigation;

import java.util.Properties;
import java.util.regex.Pattern;


public class VerificaUploadComunicazioni extends SgateSeleneseTestCase implements SeleniumNavigation {
	
	public void setUp() throws Exception {
		setUp("http://change-this-to-the-site-you-are-testing/", "*chrome");
	}
	
	 @Override
	    public void setSelenium(Selenium selenium) {
	        super.selenium = selenium;
	    }
	 

	@Override
	public void testNew() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Selenium executeNavigation(Properties prop) throws Exception {
		
		
		selenium.click("link=Invio segnalazioni");
		selenium.waitForPageToLoad("30000");
		selenium.select("idStato", "label=regexp:\\s");
		selenium.click("cmdRic");
		selenium.waitForPageToLoad("30000");
		selenium.select("idStato", "label=IN ELABORAZIONE");
		selenium.click("cmdRic");
		selenium.waitForPageToLoad("30000");
		String idTesto = selenium.getText("//div[@id='body']/div/table/tbody/tr/td[1]/div");
		String idInvioComunicazione = selenium.getText("//div[@id='body']/div/table/tbody/tr/td[1]/div");
		prop.put("idInvioComunicazione", idInvioComunicazione);
		return selenium;
		  
	}

	@Override
	public Selenium getSelenium() {
		// TODO Auto-generated method stub
		   return super.selenium;
	}
}
