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

package org.xmlblackbox.test.infrastructure.plugin;

import org.xmlblackbox.test.infrastructure.exception.RunPluginAbnormalTermination;
import org.xmlblackbox.test.infrastructure.util.MemoryData;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class GenerateInteger extends GenericRunnablePlugin {



	@Override
	public List<String> getParametersName() {
		ArrayList ret = new ArrayList<String>();
		ret.add("GenInteger.min");
		ret.add("GenInteger.max");
		return ret;
	}

	@Override
	public void execute(Properties prop, MemoryData memory) throws RunPluginAbnormalTermination{
		
	}

}
