package org.xmlblackbox.test.functions.xmlblackbox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;

import it.ancitel.sgate.www.xsd.RichiestaPresaInCaricoLottoDocument;
import it.ancitel.sgate.www.xsd.RispostaPresaInCaricoLottoDocument;
import org.apache.axis2.client.Stub;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.xmlblackbox.test.infrastructure.exception.RunPluginAbnormalTermination;
import org.xmlblackbox.test.infrastructure.plugin.GenericRunnablePlugin;
import org.xmlblackbox.test.infrastructure.interfaces.Repository;
import org.xmlblackbox.test.infrastructure.util.MemoryData;

public class PresaInCaricoSvuotaCodeXBB extends GenericRunnablePlugin {

    private final static Logger log = Logger.getLogger(PresaInCaricoSvuotaCodeXBB.class);
    XmlObject richiestaPIC = null;

	@Override
	public void execute(Properties prop, MemoryData memory) throws RunPluginAbnormalTermination {
        try {
            Connection conn = (Connection)memory.getObjectByName(Repository.RUN_PLUGIN);

            String filePath = "src/test/resources/templateXml/WebServiceTemplate/RichiestaPresaInCaricoWebService.xml";
            RichiestaPresaInCaricoLottoDocument richiestaTmp = RichiestaPresaInCaricoLottoDocument.Factory.parse(new File(filePath));
            richiestaTmp.getRichiestaPresaInCaricoLotto().setNumeroMessaggi(50);
            richiestaTmp.getRichiestaPresaInCaricoLotto().getMittente().setLogin(prop.getProperty("username"));
            richiestaTmp.getRichiestaPresaInCaricoLotto().getMittente().setPassword(prop.getProperty("password"));
            richiestaTmp.getRichiestaPresaInCaricoLotto().getMittente().setCodiceDistributore(prop.getProperty("codice_distributore"));

            //doInsertCheck(webServiceClient, richiestaTmp, null);
            richiestaPIC = (XmlObject) richiestaTmp;
            
            RispostaPresaInCaricoLottoDocument risposta = (RispostaPresaInCaricoLottoDocument) callWebService(richiestaPIC, prop);
            String idUltimoLottoRicevutoCorrettamente = risposta.getRispostaPresaInCaricoLotto().getLottoMessaggi().getIdLotto();
            String idLotto = risposta.getRispostaPresaInCaricoLotto().getLottoMessaggi().getIdLotto();
            log.info("XXXXXXXXXXXXXXXXXXXXXXXXXX idLotto XXXXXXXXXXXXXXXXXXXXXXXXXX " + idLotto);
            while (idLotto != null) {
                richiestaTmp.getRichiestaPresaInCaricoLotto().setIdUltimoLottoRicevutoCorrettamente(idUltimoLottoRicevutoCorrettamente);
                richiestaTmp.getRichiestaPresaInCaricoLotto().setNumeroMessaggi(50);
                richiestaPIC = richiestaTmp;
                risposta = (RispostaPresaInCaricoLottoDocument) callWebService(richiestaPIC, prop);
                idLotto = risposta.getRispostaPresaInCaricoLotto().getLottoMessaggi().getIdLotto();
            }
        } catch (XmlException ex) {
            log.error("XmlException", ex);
            throw new RunPluginAbnormalTermination("XmlException");
        } catch (IOException ex) {
            log.error("IOException", ex);
            throw new RunPluginAbnormalTermination("IOException");
        } catch (Exception ex) {
            log.error("Exception", ex);
            throw new RunPluginAbnormalTermination("Exception");
        }

	}

    public XmlObject callWebService(XmlObject richiestaSingola, Properties prop) throws Exception
	{

		//rinominaInput(webServiceClient);

        Stub binding = null;

        XmlObject[] richiesta=new XmlObject[1];
        XmlObject risposta=null;

		log.info("################ ");
		log.info("OPERAZIONE: PresaInCaricoLotto");
		log.info("################ ");

		richiesta[0] = richiestaSingola;

//		  try{
//              saveRichiesta(richiesta[0]);
//          }catch(Throwable e){
//            log.info ("Eccezione ", e);
//          }

        log.debug("executeWebServiceClient ");
        try {
            Class stubClass=Class.forName("org.xmlblackbox.test.webservice.client.SgateWebServiceStub");

        	Class[] parametri=new Class[1];
        	parametri[0]=String.class;

        	Object[] valori=new Object[1];
        	valori[0]=prop.getProperty("SGATE_WEB_SERVICE_URL");

			binding = (Stub)
				stubClass.getConstructor(parametri).newInstance(valori);

			log.info("---- WS Operation     : PresaInCaricoLotto");
			log.info("---- WS richiesta.length : " +richiesta.length);
			Class[] parametriInvocazione=new Class[richiesta.length];

			for(int ipar=0; ipar < richiesta.length; ipar++){
				log.info("ipar: " +ipar);
				log.info("richiesta[ipar].xmlText(): " +richiesta[ipar].xmlText());

				Class[] interfacce=richiesta[ipar].getClass().getInterfaces();

				for (int i=0; i < interfacce.length; i++){
					if (XmlObject.class.isAssignableFrom(interfacce[i])){
						parametriInvocazione[ipar]=interfacce[i];
						log.info("   Parametro invocazione #"+ipar+" assegnato: "+parametriInvocazione[ipar]);
						break;
					}
				}

			}
			log.info("---- WS Operation  ----------------");

			//Definisce il timeout del socket
			String timeout = (String) prop.getProperty("timeoutInMilliSeconds");
			if (timeout == null || timeout.equals("")) timeout="300000";

			((org.apache.axis2.client.Stub)binding)._getServiceClient().getOptions().setTimeOutInMilliSeconds(Integer.parseInt(timeout));

			((org.apache.axis2.client.Stub)binding)._getServiceClient().getOptions().setProperty(HTTPConstants.CONNECTION_TIMEOUT, new Integer(1*240*1000));

        	Object[] valoriInvocazione=richiesta;

        	log.info("Invoke del Web Service richiesta : "+richiesta[0].xmlText());

			Method metodo=stubClass.getDeclaredMethod("PresaInCaricoLotto", parametriInvocazione);
			log.info("Invoke del Web Service (PresaInCaricoLotto)");
            risposta=(XmlObject)metodo.invoke(binding, valoriInvocazione);
			log.info("Dopo l'Invoke del Web Service (PresaInCaricoLotto)");
			log.info ("WS RISPOSTA = [" +risposta.toString()+ "]");

        } catch (InvocationTargetException e) {
        	try{
        		risposta=(XmlObject)e.getTargetException().getClass().getMethod("getFaultMessage", new Class[0]).invoke(e.getTargetException(), new Object[0]);
        		log.error("Fault", e);
//        		saveRisposta(risposta);
        		return risposta;
        	}catch(NoSuchMethodException nsme){
        		log.error("Exception", nsme);
        	}
        	log.error("Exception", e);
        	throw e;
        }

//		saveRisposta(risposta);
        return risposta;

	}



//    private XmlObject callWebService(XmlObject richiesta, Properties prop) throws TestException{
//
//        XmlObject risposta;
//        log.debug("callWebService ");
//        try {
//            Stub binding = null;
//
//        	Class stubClass=Class.forName("it.ancitel.sgate.test.webservice.client.SgateWebServiceStub");
//
//        	Class[] parametri=new Class[1];
//        	parametri[0]=String.class;
//
//        	Object[] valori=new Object[1];
//        	valori[0]=prop.getProperty("SGATE_WEB_SERVICE_URL");
//
//			binding = (Stub)
//				stubClass.getConstructor(parametri).newInstance(valori);
//
//			log.info("---- WS Operation     : PresaInCaricoLotto");
//			Class[] parametriInvocazione=new Class[1];
//
//
//            Class[] interfacce=richiesta.getClass().getInterfaces();
//
//            for (int i=0; i < interfacce.length; i++){
//                if (XmlObject.class.isAssignableFrom(interfacce[i])){
//                    parametriInvocazione[0]=interfacce[i];
//                    break;
//                }
//            }
//
//			log.info("---- WS Operation  ----------------");
//
//			//Definisce il timeout del socket
//			String timeout = (String) prop.getProperty("timeoutInMilliSeconds");
//			if (timeout == null || timeout.equals("")) timeout="300000";
//
//			((org.apache.axis2.client.Stub)binding)._getServiceClient().getOptions().setTimeOutInMilliSeconds(Integer.parseInt(timeout));
//
//			((org.apache.axis2.client.Stub)binding)._getServiceClient().getOptions().setProperty(HTTPConstants.CONNECTION_TIMEOUT, new Integer(1*240*1000));
//
//        	Object[] valoriInvocazione=new Object[1];
//
//            valoriInvocazione[0] = richiesta;
//        	log.info("Invoke del Web Service richiesta : "+richiesta.xmlText());
//
//			Method metodo=stubClass.getDeclaredMethod("PresaInCaricoLotto", parametriInvocazione);
//			log.info("Invoke del Web Service (PresaInCaricoLotto)");
//            risposta=(XmlObject)metodo.invoke(binding, valoriInvocazione);
//			log.info("Dopo l'Invoke del Web Service (PresaInCaricoLotto)");
//			log.info ("WS RISPOSTA = [" +risposta.toString()+ "]");
//
//        } catch (InvocationTargetException e) {
//        	try{
//        		log.error("Fault", e);
//        		risposta=(XmlObject)e.getTargetException().getClass().getMethod("getFaultMessage", new Class[0]).invoke(e.getTargetException(), new Object[0]);
//        		return risposta;
//        	}catch(NoSuchMethodException nsme){
//        		log.error("Exception", nsme);
//        	}catch(Exception nsme){
//        		log.error("Exception", nsme);
//        	}
//        	log.error("InvocationTargetException", e);
//        	throw new TestException (e, "InvocationTargetException");
//        } catch (Exception e) {
//        	log.error("Exception", e);
//        	throw new TestException (e, "Exception");
//        }
//
//        return risposta;
//
//
//    }

	@Override
	public List<String> getParametersName() {
		ArrayList<String> ret = new ArrayList();
		return ret;
	}
}
