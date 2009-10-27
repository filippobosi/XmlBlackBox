package it.ancitel.sgate.test.selenium;

import java.util.Properties;

import com.thoughtworks.selenium.Selenium;

import it.ancitel.sgate.test.util.SgateSeleneseTestCase;
import it.ancitel.test.infrastruttura.interfaces.SeleniumNavigation;

public class SgateHome extends SgateSeleneseTestCase implements SeleniumNavigation{ 

	@Override
	public void testNew() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public Selenium executeNavigation(Properties prop) throws Exception {
		super.setUp(prop.getProperty("SGATE_WEB_URL"));

		selenium.click("link=Home"); 
		selenium.waitForPageToLoad("30000");
		return selenium;
	}
	
	@Override
	public Selenium getSelenium() {
		// TODO Auto-generated method stub
		return null;
	}
}
