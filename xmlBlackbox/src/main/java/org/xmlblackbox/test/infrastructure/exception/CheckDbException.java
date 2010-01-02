package org.xmlblackbox.test.infrastructure.exception;

import org.apache.log4j.Logger;

public class CheckDbException extends Exception {

    private Exception containedException = null;
    private String errorMessage = null;
    protected final static Logger log = Logger.getLogger(CheckDbException.class);

    /**
    * Costruttore vuoto
    */
    public CheckDbException() {
        super();
    }

    /**
    * Costruttore con argomenti
    */
    public CheckDbException(Exception ex, String errorMessage) {
      containedException = ex;
      this.errorMessage = errorMessage;
    }


   /**
   * Costruttore con argomenti
   */
   public CheckDbException(String errorMessage) {

     this.errorMessage = errorMessage;
   }


    /**
    * Restituisce l'eccezione passata nel costruttore
    *
    * @return containedException
    */
    public Exception getContainedException() {
      return containedException;
    }

    /**
    * Restituisce il messaggio di errore passato nel costruttore
    *
    * @return errorMessage
    */
    public String toString()
    {
        return errorMessage;
    }

    /**
     * @param errorMessage the errorMessage to set
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}

