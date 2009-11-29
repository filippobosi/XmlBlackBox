package org.xmlblackbox.test.infrastructure.util;

import org.xmlblackbox.test.infrastructure.interfaces.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;

public class DBConnection {

	private final static Logger log = Logger.getLogger(DBConnection.class);
	
	public interface params {
		public static final String DRIVER = "db.driver";
		public static final String URL = "db.url";
		public static final String USERNAME = "db.user";
		public static final String PASSWORD = "db.pw";
	}
	
	private static IDatabaseConnection conn = null;
	private static Properties config = new Properties();
	
	public static void setConfig(String paramName,String value) {
		config.put(paramName, value);
	}

	public static IDatabaseConnection getConnection() throws Exception{
		if ((conn==null) || (conn.getConnection().isClosed())){
			conn = new DatabaseConnection(connect());
		}
		log.info("-- Connected to db.... " + conn);
		return conn;
	}
	
//	public static Connection getJdbcConnection() throws Exception{
//		if ((conn==null) || (conn.getConnection().isClosed())){
//			conn = new DatabaseConnection(connect());
//		}
//		log.info("-- Connected to db... " + conn);
//		return conn;
//	}

	
	private static Connection connect() throws Exception{
		log.info("-- Starting db connection...");
		
		log.debug("- Driver : "+config.getProperty(params.DRIVER));
		log.debug("- Url : "+config.getProperty(params.URL));
		log.debug("- User : "+config.getProperty(params.USERNAME));
		log.debug("- Password : "+config.getProperty(params.PASSWORD));

		Connection jdbcConnection = null;
		try {
				Class driverClass = Class.forName(config.getProperty(params.DRIVER));
				jdbcConnection = DriverManager.getConnection(
										config.getProperty(params.URL), 
										config.getProperty(params.USERNAME), 
										config.getProperty(params.PASSWORD));
				jdbcConnection.setAutoCommit(true);
			}catch(SQLException e){
				log.error("[!] Impossibile completare la connessione al database",e);
				throw e;
			} catch (ClassNotFoundException e) {
				log.error("[!] Impossibile completare la connessione al database - driver non trovato",e);
				throw e;
			}
	
		log.info("-- Connection succesful...");
		return jdbcConnection;
		
	}
	
	public static Connection getConnection(String driver, String url, String username, String password) throws Exception{
		log.info("-- Starting db connection...");
		
		log.debug("- Driver : "+driver);
		log.debug("- Url : "+url);
		log.debug("- User : "+username);
		log.debug("- Password : "+password);

		Connection jdbcConnection = null;
		try {
				Class driverClass = Class.forName(driver);
				jdbcConnection = DriverManager.getConnection(
										url, 
										username, 
										password);
				jdbcConnection.setAutoCommit(true);
			}catch(SQLException e){
				log.error("[!] Connection not complete ",e);
				throw e;
			} catch (ClassNotFoundException e) {
				log.error("[!] Connection not complete - driver not found",e);
				throw e;
			}
	
		log.info("-- Connection succesful...");
		return jdbcConnection;
		
	}
	
	
	
}
