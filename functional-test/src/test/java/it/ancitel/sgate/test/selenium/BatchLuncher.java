package it.ancitel.sgate.test.selenium;

import java.util.Properties;

import com.thoughtworks.selenium.Selenium;

import it.ancitel.sgate.test.util.SgateSeleneseTestCase;
import it.ancitel.test.infrastruttura.interfaces.SeleniumNavigation;

public class BatchLuncher extends SgateSeleneseTestCase implements SeleniumNavigation{ 

	@Override
	public void testNew() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public Selenium executeNavigation(Properties prop) throws Exception {
		
		super.setUp(prop.getProperty("SGATE_BATCH_WEB_URL"));
		
		selenium.open("/sgate-batch-web/");
        Thread.sleep(20000);
		selenium.select("jobName", "value=" + prop.getProperty("jobAction"));
		selenium.click("//input[@value='Esegui']");
		selenium.waitForPageToLoad("30000");
		if (!selenium.isTextPresent("invocato correttamente")){
			selenium.select("jobName", "value=" + prop.getProperty("jobAction"));
			selenium.click("//input[@name='force']");
			selenium.click("//input[@value='Esegui']");	
		}
		return selenium;
	}
	
	@Override
	public Selenium getSelenium() {
		// TODO Auto-generated method stub
		return null;
	}
}
