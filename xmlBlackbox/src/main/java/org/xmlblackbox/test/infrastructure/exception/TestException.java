package org.xmlblackbox.test.infrastructure.exception;

import org.apache.log4j.Logger;

/**
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version $Id: TestException.java,v 1.2 2006/04/07 14:03:10 crea Exp $
 */

public class TestException extends Exception{

    private Exception containedException = null;
    private String errorMessage = null;
    protected final static Logger log = Logger.getLogger(TestException.class);

    /**
    * Costruttore vuoto
    */
    public TestException() {
        super();
    }

    /**
    * Costruttore con argomenti
    */
    public TestException(Exception ex, String errorMessage) {
      containedException = ex;
      this.errorMessage = errorMessage;
    }


   /**
   * Costruttore con argomenti
   */
   public TestException(String errorMessage) {

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


/*
 * $Log: TestException.java,v $
 * Revision 1.2  2006/04/07 14:03:10  crea
 * Aggiunta l'eccezione che lancia in caso di errore da parte del pccsa-client
 *
 * Revision 1.1  2006/02/09 15:01:20  crea
 * Aggiunti altri test (Nascita004) e classi di utilita'
 *
 * Revision 1.3  2005/03/29 13:44:37  lmanzoni
 * Allineamento CVS Trieste
 *
 * Revision 1.2  2005/03/01 14:51:10  lmanzoni
 * commit merge CR_TOSCANI
 *
 * Revision 1.1.2.1  2005/02/17 11:48:21  amedeo
 * Modifiche in seguito ad aggiunta dei test della CR_LOG
 *
 * Revision 1.1  2005/02/16 08:42:16  amedeo
 * Renaming della classe EjbTestExcepion in TestException
 *
 * Revision 1.2  2005/02/15 08:59:47  amedeo
 * Aggiunta della keyword log di cvs
 *
 */
