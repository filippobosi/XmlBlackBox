package org.xmlblackbox.test.infrastructure.util;

import org.xmlblackbox.test.infrastructure.exception.InvalidVariableAnnotation;
import org.xmlblackbox.test.infrastructure.exception.RepositoryNotFound;
import org.xmlblackbox.test.infrastructure.exception.VariableNotFound;

import java.sql.Connection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;




public class MemoryData {
	
	private final static Logger log = Logger.getLogger(MemoryData.class);
	private Hashtable<String, Properties> memory=new Hashtable();
	private Hashtable<String, Object> memoryObject=new Hashtable();
	
	public String get(String key) throws RepositoryNotFound, InvalidVariableAnnotation, VariableNotFound{
		String repoName = getRepositoryName(key);
		String varName  = getVariableName(key);
		
		Properties repo = getRepository(repoName);
		Object var = repo.getProperty(varName);
		
		if (var == null){
			throw new VariableNotFound(key, repo);
		}
		
		log.info("- get : " + key + " = " + var);
		return (String) var;
	}
	
	public void set(String key,String value) throws InvalidVariableAnnotation{
		checkVariableAnnotation(key);
		String repoName = getRepositoryName(key);
		String varName  = getVariableName(key);
		
		this.set(repoName,varName,value);
	}
	
	public void set(String repoName, String varName,String value) throws InvalidVariableAnnotation{
		Properties repo;
		try {
			repo = getRepository(repoName);
		} catch (RepositoryNotFound e) {
			repo = createRepository(repoName);
		}
		log.info("- set : " + varName + "@" + repoName + " = " + value);
		repo.setProperty(varName, value);
	}
	
	public void overrideRepository(String repoName,Properties props) {
		log.debug("- overrideRepository : " + repoName + " = " + props);
		memory.put(repoName, props);
	}

    public void addToRepository(String repoName,Properties props) {
		log.debug("- overrideRepository : " + repoName + " = " + props);
        Properties crrentProp = memory.get(repoName);
		crrentProp.putAll(props);
	}

	private Properties createRepository(String repoName){
		log.debug("- createRepository : " + repoName);
		Properties newRepo = new Properties();
		memory.put(repoName, newRepo);
		return newRepo;
	}
	
	public Properties Hashtable2Properties(Hashtable input){
		log.debug("- Hash to Properties : " + input );
		Properties prop = new Properties();
		prop.putAll(input);
		return prop;
	}

	private void checkVariableAnnotation(String key) throws InvalidVariableAnnotation{
		if (!key.matches(".+@.+")){
			throw new InvalidVariableAnnotation();
		}
	}
	
	private String[] splitKey(String key) throws InvalidVariableAnnotation{
		checkVariableAnnotation(key);
		String[] keySplitted=key.split("@");
		return keySplitted;
	}
	
	public Properties getRepository(String repoName) throws RepositoryNotFound{
		Properties repo = memory.get(repoName);
		if (repo == null){
			throw new RepositoryNotFound(repoName);
		}
		
		log.debug("- get repository : " + repoName + " = " + repo);
		return repo;
	}

    public Hashtable<String, Object> getAllObject() throws RepositoryNotFound{
		return memoryObject;
	}

	public Properties getOrCreateRepository(String repoName){
		Properties repo;
		try {
			repo = getRepository(repoName);
		} catch (RepositoryNotFound e) {
			repo = createRepository(repoName);
		}
		return repo;
	}
	
	private String getRepositoryName(String key) throws InvalidVariableAnnotation{
		return splitKey(key)[1];
	}
	
	private String getVariableName(String key) throws InvalidVariableAnnotation{
		return splitKey(key)[0];
	}
	
	public Map getMemory2Map(){
		log.debug("- get memory : " + memory);
		return memory;
	}
	
	public Connection getConnectionByName(String nameConnection){
		return (Connection)memoryObject.get(nameConnection);
	}

    public Object getObjectByName(String nameObject){
		return memoryObject.get(nameObject);
	}

	public void setConnection(String nameConnection, Connection conn){
		memoryObject.put(nameConnection, conn);
	}

    public void setObject(String nameObject, Connection object){
		memoryObject.put(nameObject, object);
	}

	public void debugMemory(){
		log.warn("---------------------------------------");
		log.warn("------------- DEBUG MEMORY ------------");
		log.warn("---------------------------------------");
		
		log.warn("***************************************");
		
		Iterator itRepo=memory.keySet().iterator();
		while (itRepo.hasNext()){
			String repo = (String) itRepo.next();
			log.warn("REPOSITORY : " + repo);
			log.warn("---------------------------------------");
			
			Set variables = memory.get(repo).keySet();
			TreeSet<String> variablesOrdered=new TreeSet<String>();
			variablesOrdered.addAll(variables);
			
			Iterator itVar = variablesOrdered.iterator();
			while (itVar.hasNext()){
				String var = (String) itVar.next();
				try {
					log.warn(var + " = " + get(var + "@" + repo));
				} catch (RepositoryNotFound e) {
					log.fatal(e);
				} catch (InvalidVariableAnnotation e) {
					log.fatal(e);
				} catch (VariableNotFound e) {
					log.fatal(e);
				}
			}
			
			log.warn("---------------------------------------");
		}

		log.warn("***************************************");
	}
	
}
