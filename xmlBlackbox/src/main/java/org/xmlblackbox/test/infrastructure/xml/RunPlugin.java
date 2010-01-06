/**
 *
 * This file is part of XmlBlackBox.
 *
 * XmlBlackBox is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * XmlBlackBox is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with XmlBlackBox.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.xmlblackbox.test.infrastructure.xml;

import java.util.HashMap;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.xmlblackbox.test.infrastructure.exception.TestException;
import org.xmlblackbox.test.infrastructure.interfaces.Repository;
import org.xmlblackbox.test.infrastructure.plugin.GenericRunnablePlugin;
import org.xmlblackbox.test.infrastructure.util.MemoryData;

public class RunPlugin extends XmlElement {


	private final static Logger log = Logger.getLogger(RunPlugin.class);

        private String templateClass;
        private HashMap<String, String> input;
        private HashMap<String, String> output;

	public HashMap<String, String> getInput() {
		return input;
	}
	public void setInput(HashMap<String, String> input) {
		this.input = input;
	}
	public HashMap<String, String> getOutput() {
		return output;
	}
	public void setOutput(HashMap<String, String> output) {
		this.output = output;
	}
	public String getTemplateClass() {
		return templateClass;
	}
	public void setTemplateClass(String templateClass) {
		this.templateClass = templateClass;
	}

	public RunPlugin(Element el) throws Exception {
		super(el);
		build(el);
	}
	
	public void build(Element setRunPlugin) throws Exception {
		RunPlugin runPlugin = this;
		runPlugin.setTemplateClass(setRunPlugin.getAttributeValue("class"));
		runPlugin.setInput(parseParameters(setRunPlugin.getChild("PARAMETERS", uriXsd)));
	}

	@Override
	public String getRepositoryName() {
		return Repository.RUN_PLUGIN;
	}
	
    public void executePlugin(MemoryData memory) throws TestException, Exception{

    	Properties prop = memory.getOrCreateRepository(getRepositoryName());
    	//Immetto i parameters di input nella memoria..
    	prop.putAll(getInput());
    	
    	// Instanzio la classe..
    	Class functionClass=Class.forName(getTemplateClass());
    	
    	// Inizializzo il costruttore..
    	GenericRunnablePlugin runPluginImpl = null;
    	runPluginImpl = (GenericRunnablePlugin) functionClass.getConstructor().newInstance();
    	
    	// Eseguo la funzione
    	runPluginImpl.checkPrametersName(prop, runPluginImpl.getParametersName());
    	// DBConnection deve essere stato gia avviato...
    	runPluginImpl.execute(prop,memory); 	
    }



}