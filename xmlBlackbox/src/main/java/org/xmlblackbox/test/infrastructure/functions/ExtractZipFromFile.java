package org.xmlblackbox.test.infrastructure.functions;

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
