package org.xmlblackbox.test.infrastructure.util;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


/**
 *
 * <p>Description: </p>
 * <p>- .</p>
 * @author
 */


public class Configurator {

  private final static Logger log = Logger.getLogger(Configurator.class);
  
  private final static String pathLog4JProperties ="log4j.properties";

  private final static String pathPropertiesFile ="xmlBlackbox.properties";

  private static Properties cachedProps;
  private static boolean isConfigureLog = false;

  public Configurator() {
  }

  public static Properties getProperties() {

	 log.info("pathPropertiesFile "+pathPropertiesFile );
	 cachedProps=new Properties();
     InputStream is = Configurator.class.getClassLoader().getResourceAsStream(pathPropertiesFile);
     try {
       cachedProps.load(is);
       log.info("Caricate le prop "+cachedProps );
   	 }
     catch (IOException ex) {
       ex.printStackTrace();
     }finally{
    	 try {
			is.close();
		} catch (IOException e) {
			log.error("Exception closing stream about prooperties file ", e);
		}
     }
    
    return cachedProps;
  }

  public static Properties getProperties(String nomeFile) {

      Properties validazioneProps=new Properties();
      InputStream is = Configurator.class.getClassLoader().getResourceAsStream(nomeFile);
      try {
          validazioneProps.load(is);
      }
      catch (IOException ex) {
        ex.printStackTrace();
      }finally{
     	 try {
 			is.close();
 		} catch (IOException e) {
 			log.error("Exception closing stream about prooperties file ", e);
 		}
      }
     
     return validazioneProps;
   }

  public static void addProperties(Properties props, String nomeFile) {

      if(props == null)
         props=new Properties();

      InputStream is = Configurator.class.getClassLoader().getResourceAsStream(nomeFile);
      try {
          props.load(is);
      }
      catch (IOException ex) {
        ex.printStackTrace();
      }finally{
     	 try {
 			is.close();
 		} catch (IOException e) {
 			log.error("Exception closing stream about prooperties file ", e);
 		}
      }
     
   }

  public static void configureLog4J() {

    System.out.println("Log configuration file "+pathLog4JProperties);
    if (!isConfigureLog) { //Check if is not already configured
      System.out.println("Log configuration file "+pathLog4JProperties);
      InputStream is = null;
      try {
          is = Configurator.class.getClassLoader().getResourceAsStream(pathLog4JProperties);
      }catch (Exception ex) {
        ex.printStackTrace();
      }
      System.out.println("Input Stream Loaded");
      Properties prop = new Properties();
      try {
        prop.load(is);
        System.out.println("Properties loaded");
      }
      catch (IOException ex) {
        ex.printStackTrace();
      }

      PropertyConfigurator.configure(prop);
      log.info("Configurazione log4J per i TEST Ok");
      isConfigureLog = true;
    }

  }




}
/*
 * $Id: $
 * $Log:$
 */
