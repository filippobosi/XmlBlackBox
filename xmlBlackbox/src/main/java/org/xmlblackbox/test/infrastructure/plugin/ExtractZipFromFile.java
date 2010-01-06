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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.log4j.Logger;

public class ExtractZipFromFile extends GenericRunnablePlugin{

	 private final static Logger log = Logger.getLogger(ExtractZipFromFile.class);
	    
	
	
	@Override
	public void execute(Properties prop, MemoryData memory) throws RunPluginAbnormalTermination{

		 
		String zipFile = prop.getProperty("zipFilePath");
		String extractToPath = prop.getProperty("extractToPath");
		log.debug("zipFilePath "+ zipFile );
		log.debug("extractToPath "+ extractToPath );
		extractZipArchive(new File(zipFile),extractToPath);
		
	}

	@Override
	public List<String> getParametersName() {
		ArrayList<String> ret = new ArrayList();
		ret.add("zipFilePath");
		ret.add("extractToPath");
		return ret;
	}
	
	
	public static void extractZipArchive(File zipName,String destinationName) throws RunPluginAbnormalTermination {
		try
	    {
	       
	        byte[] buf = new byte[1024];
	        ZipInputStream zipinputstream = null;
	        ZipEntry zipentry;
	        
	        zipinputstream = new ZipInputStream(new FileInputStream(zipName));

	        zipentry = zipinputstream.getNextEntry();
	        
	        while (zipentry != null) 
	        { 
	            
	            String entryName = zipentry.getName();
	            System.out.println("entryname "+entryName);
	            
	            int n;
	            
	            FileOutputStream fileoutputstream;
	            File newFile = new File(entryName);
	            String directorynewFile = newFile.getParent();
	            
	            if(directorynewFile == null)
	            {
	                if(newFile.isDirectory())
	                    break;
	            }
	            
	            fileoutputstream = new FileOutputStream(destinationName+entryName);             

	            while ((n = zipinputstream.read(buf, 0, 1024)) > -1)
	                fileoutputstream.write(buf, 0, n);

	            fileoutputstream.close(); 
	            zipinputstream.closeEntry();
	            zipentry = zipinputstream.getNextEntry();

	        }//while

	        zipinputstream.close();
	    }
	    catch (IOException e)
	    {
	    	 log.error("IOException", e);
             throw new RunPluginAbnormalTermination("IOException");
	    }
		}

}
