package org.xmlblackbox.test.infrastructure.interfaces;

import com.thoughtworks.selenium.*;
import java.util.Properties;

import org.xmlblackbox.test.infrastructure.util.MemoryData;

/**
 *
 * @author Crea
 */
public interface SeleniumNavigation {

    public Selenium executeNavigation(MemoryData memory) throws Exception ;

    public void setSelenium(Selenium selenium);

    public Selenium getSelenium();

}
