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

package org.xmlblackbox.test.infrastructure.interfaces;

public interface Repository {

	public static final String FILE_PROPERTIES = "fileProperties";
	public static final String WEB_NAVIGATION = "webNavigation";
	public static final String SET_VARIABLE = "setVariable";
	public static final String WEB_SERVICE = "webService";
	public static final String DB_CHECK = "dbCheck";
	public static final String RUN_PLUGIN = "runPlugin";
	public static final String DB_CONNECTION = "dbConnection";
	public static final String WAIT_TASK = "waitTask";
	public static final String PARAMETERS = "parameters";
	
	public static final String REPO_NOT_SUPPORTED = "trashRepo";
}
