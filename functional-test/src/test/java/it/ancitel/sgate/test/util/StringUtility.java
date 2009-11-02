package it.ancitel.sgate.test.util;


import org.apache.log4j.Logger;
import org.xmlblackbox.test.infrastructure.exception.TestException;


/**
 * <p>Title: INA-SAIA </p>
 * <p>Description: Classe di utilita' sulle stringhe
 * <p>Title: INA-SAIA </p></p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Ancitel</p>
 * @author
 */

public class StringUtility {
  private final static Logger log = Logger.getLogger(StringUtility.class);

  private static char alfabeto[] = {
      'B', 'C', 'D', 'F', 'G', 'L', 'M', 'N', 'P', 'Q'};

  public static String convertiNumeroStringa(int num) {

    String str = String.valueOf(num);
    if (str.length()>3) {
        str = str.substring(str.length()-3,str.length());
	}
    StringBuffer ret = new StringBuffer();

    for (int i = 0; i < str.length(); i++) {
      ret.append(alfabetizzazione(str.charAt(i)));
    }

    return ret.toString();
  }

  private static char alfabetizzazione(char c) {
    return alfabeto[Character.getNumericValue(c)];
  }


  public static String normalizza(String str){
    str = str.toLowerCase();
    char primaLettera = Character.toUpperCase(str.charAt(0));
    str = primaLettera + str.substring(1, str.length());
    return str;
  }

  public static void main (String[] args){
    String str = "CASA";
    System.out.println(str  + ": "+ normalizza(str));
  }


  public static int parseInt(String s) throws TestException {
      try {
        return Integer.parseInt(s);
      } catch (NumberFormatException e) {
          log.error("Impossibile trasformare la stringa " + s
              + " in un int. ", e);
          throw new TestException("Impossibile trasformare la stringa " + s
              + " in un int. " + e.getMessage());
      }
  }
  
  /**
   * Controlla se la sequence ha dei caratteri attigui con lo stesso valore
   * @param seq int
   * @return boolean
   */

  public static boolean isSequenceOK(int seq) {

    StringBuffer Sequence = null;
    boolean res = true;

    Sequence = new StringBuffer(new Integer(seq).toString());

    for (int i = 0; i < Sequence.length() - 1; i++) {

      if (Sequence.charAt(i) == Sequence.charAt(i + 1)) {
        res = false;
        break;
      }
    }
    return res;
  }
}



/*
 * $Id: $
 * $Log:$
 *
 */
