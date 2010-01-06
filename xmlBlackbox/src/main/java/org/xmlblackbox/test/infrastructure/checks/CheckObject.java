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

package org.xmlblackbox.test.infrastructure.checks;

import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.dbunit.database.IDatabaseConnection;
import org.xmlblackbox.test.infrastructure.util.MemoryData;



public abstract class  CheckObject {

	public IDatabaseConnection conn = null;

    protected final static Logger logger = Logger.getLogger(CheckObject.class);

	public Properties testProp = null;
	public MemoryData memData = null;
	
    @Deprecated
    public Properties getTestProp() {
		return testProp;
	}
    @Deprecated
	public void setTestProp(Properties testProp) {
		this.testProp = testProp;
	}

	public abstract void initVariable() throws Exception;

    public abstract void execute(Map prop, int step) throws Exception;
     

}
