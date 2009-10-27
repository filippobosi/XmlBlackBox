package it.ancitel.sgate.test.functions;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.log4j.Logger;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;

import it.ancitel.test.infrastruttura.exception.RunFunctionAbnormalTermination;
import it.ancitel.test.infrastruttura.functions.GenericRunnableFunction;

public class SaveEsitoSegnalazioneImpresaToFile extends
GenericRunnableFunction{
	
	
	private final static Logger log = Logger.getLogger(SaveEsitoSegnalazioneImpresaToFile.class);

	@Override
	public void execute(Properties prop, Connection connessione)
			throws RunFunctionAbnormalTermination {

	
	IDatabaseConnection conn = new DatabaseConnection(connessione);
	String query = prop.getProperty("query");
	String saveToPath =prop.getProperty("path");
	
	try{
		 if (query!=null){
	            ITable tableInvioComunicazione = conn.createQueryTable("INVIO_COMUNICAZIONE",	query);
	            if (tableInvioComunicazione.getRowCount()>0){
	            
	            	byte [] esitoBin=(byte[])tableInvioComunicazione.getValue(0, "esito");
	            	saveTo(esitoBin,saveToPath);
	            }
		 }
		
	}catch (DataSetException ex) {
        log.error("DataSetException", ex);
        throw new RunFunctionAbnormalTermination("DataSetException");
    } catch (SQLException ex) {
        log.error("SQLException", ex);
        throw new RunFunctionAbnormalTermination("SQLException");
    } 
	}
	
	
	@Override
	public List<String> getParametersName() {
		ArrayList<String> ret = new ArrayList();
		ret.add("query");
		ret.add("path");
		return ret;
	}
	
	private static void saveTo(byte[] data,String saveTo) throws RunFunctionAbnormalTermination{
		
		FileOutputStream fileoutputstream=null;
		
		try {
			log.debug("\nSalvo in "+saveTo);
			fileoutputstream = new FileOutputStream(saveTo);
			fileoutputstream.write(data);
			fileoutputstream.close();
			log.debug("\n"+data.length+" bytes salvati");
			
			
		} catch (FileNotFoundException e) {
			 log.error("SQLException", e);
             throw new RunFunctionAbnormalTermination("SQLException");
		} catch (IOException e) {
			 log.error("SQLException", e);
             throw new RunFunctionAbnormalTermination("SQLException");
		}
		
		
	
	}

	
	

}
