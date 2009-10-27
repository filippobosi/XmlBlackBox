package org.xmlblackbox.test.infrastructure.replacement;

public interface Replacer {
	
	public static final String MAIN_SOURCE = "MAIN"; 
	
	public abstract String replace(String text) throws Exception;

	public abstract String getReplacementValue(Object source, String sourceName, String value) throws Exception;

	public abstract String getEndDelimiter();

	public abstract void setEndDelimiter(String endDelimiter);

	public abstract String getStartDelimiter();

	public abstract void setStartDelimiter(String startDelimiter);

	public abstract Object getSource();

	public abstract void setSource(Object source);

}