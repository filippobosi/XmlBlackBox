package org.xmlblackbox.test.util;


/**
 * Classe di utility per gestire le operazioni e i controlli sulle stringhe.
 *
 * @author 
 */
public class TextUtility {

        /**
        * Pulisce una stringa dagli apostrofi e dagli accenti.
    * @param text Il contentuto del campo di testo in esame.
    * @return text la stringa pulita da accenti ed apostrofi.
        */
        public static String cleanString(String text)
        {
            text = text.replace('\'', ' ');
                return text;
        }
}

