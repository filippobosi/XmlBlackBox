
package org.xmlblackbox.test.infrastructure.xml;



import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.ITable;
import org.jdom.Element;
import org.apache.log4j.Logger;


import org.xmlblackbox.test.infrastructure.exception.TestException;
import org.xmlblackbox.test.infrastructure.interfaces.Repository;
import org.xmlblackbox.test.infrastructure.util.MemoryData;

import java.sql.Connection;
import org.dbunit.database.DatabaseConnection;
/**
 *
 * @author acrea
 */
public class WaitTask extends Runnable  {
	
	private final static Logger logger = Logger.getLogger(WaitTask.class);
	
	private boolean waitingTerminated=false;
	private String waitingQuery = null;
	private String waitingConnection = null;
	private String waitingTimeout = "0";
	
	public WaitTask(Element el) throws Exception {
		super(el);
        build(el);
	}
	
	public void build(Element waitElement) throws Exception {
    	
		WaitTask waitTask=this;
		logger.debug("waitElement "+waitElement);
		logger.debug("waitElement.getAttributeValue(nome) "+waitElement.getAttributeValue("name"));
		waitTask.setNome(waitElement.getAttributeValue("name"));
		
    	Element waitTerminated = waitElement.getChild("WAIT", uriXsd);
    	logger.info("waitTerminated "+waitTerminated);
    	if (waitTerminated!=null){
    		waitTask.setWaitingTerminated(true);
    		waitTask.setWaitingQuery(waitTerminated.getAttributeValue("query"));
    		
        	if (waitTerminated.getAttributeValue("timeout")!=null){
    			waitTask.setWaitingTimeout(waitTerminated.getAttributeValue("timeout"));
    		}
            waitTask.setWaitingConnection(waitTerminated.getAttributeValue("connection"));

    	}
    	
	}
	
	public String getWaitingQuery() {
		return waitingQuery;
	}

	public void setWaitingQuery(String waitingQuery) {
		this.waitingQuery = waitingQuery;
	}

	public String getWaitingTimeout() {
		return waitingTimeout;
	} 

	public void setWaitingTimeout(String waitingTimeout) {
		this.waitingTimeout = waitingTimeout;
	}
	
	public boolean isWaitingTerminated() {
		return waitingTerminated;
	}

	public void setWaitingTerminated(boolean waitTerminated) {
		this.waitingTerminated = waitTerminated;
	}

	@Override
	public String getRepositoryName() {
		return Repository.WAIT_TASK;
	}
	
	private void waitTask(MemoryData memory) throws TestException{
		if (isWaitingTerminated()){
	    	boolean waitExit = false;
	    	int index = 0;
	    	int timeout = new Integer(getWaitingTimeout());
            IDatabaseConnection conn = new DatabaseConnection((Connection)memory.getObjectByName(getWaitingConnection()));
	    	while(!waitExit && (index<timeout)){
	    		String query = getWaitingQuery();
	    		logger.info("query "+query);
	    		ITable waiTaskItable = null;
	    		try{
	    			waiTaskItable = org.xmlblackbox.test.infrastructure.util.ITableUtil.getITable(conn,
	    				"WAITTASK", query);
	    		}catch(Exception e){
	    			logger.error("Exception ", e);
	    			throw new TestException("Eccezione eseguendo la query "+getWaitingQuery()+" per la WAIT_TERMINATED");
	    			
	    		}
	    		
	    		logger.info("waiTaskItable.getRowCount() "+waiTaskItable.getRowCount());
	    		if (waiTaskItable.getRowCount()==1){
	    			waitExit = true;
	    		}else{
	    			try {
	    	    		logger.info(index+" Sleep one second before checking wheater the query return result");
						Thread.sleep(1000);
						index++;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    		}
	    		logger.info("waitExit "+waitExit);
	    		logger.info("(waitTask.getWaitingTimeout()) "+(getWaitingTimeout()));
	    		logger.info("(index<waitTask.getWaitingTimeout()) "+(index<timeout));
	    		
	    	}
	    	
		}
	}
	
    /**
     * @return the waitingConnection
     */
    public String getWaitingConnection() {
        return waitingConnection;
    }

    /**
     * @param waitingConnection the waitingConnection to set
     */
    public void setWaitingConnection(String waitingConnection) {
        this.waitingConnection = waitingConnection;
    }

}