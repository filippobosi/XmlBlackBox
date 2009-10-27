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
public class LoginImpresa extends SgateSeleneseTestCase implements SeleniumNavigation{
    

    @Override
	public Selenium executeNavigation(Properties prop) throws Exception {

        super.setUp(prop.getProperty("SGATE_WEB_URL"));


        selenium.open(prop.getProperty("SGATE_WEB_URL")+"/login.htm");
        selenium.type("userId", prop.getProperty("USERNAME_DISTRIBUTORE"));
        selenium.type("password", prop.getProperty("PASSWORD_DISTRIBUTORE"));

        selenium.click("//input[@value='Accedi']");
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
