package it.ancitel.sgate.test.util;


import java.text.*;
import java.util.*;


/**
 * <p>Title: SAIA </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: INA-SAIA - Imola Informatica S.r.l.</p>
 * @author
 * @version $Id: CodiceFiscale.java,v 1.4 2007/01/16 14:56:51 crea Exp $
 */


public class CodiceFiscale
{

    private static final int PERSON_FISCAL_CODE_LENGTH = 16;
    private static final int MALE = 0;
    private static final int FEMALE = 1;
    private static final int AGE_OFFSET = 40;
    private static final int SURNAME_CODE_LENGTH = 3;
    private static final int NAME_CODE_LENGTH = 3;
    private static final int BIRTHDATE_CODE_LENGTH = 5;
    private static final int BIRTHPLACE_CODE_LENGTH = 4;
    private static final int DUMMY_CHAR = 'x';
    private static final String COMPOUND_SURNAME = " in ";

    /** Matrice di conversione delle vocali accentate in vocali*/
    private static final char[][] vowelsMapping =
        {
            {'a', 'a'},
            {'e', 'e'},
            {'e', 'e'},
            {'i', 'i'},
            {'o', 'o'},
            {'u', 'u'}
        };
    /** Vocali*/
    private static final char[] vowels =
        {
            'a', 'e', 'i', 'o', 'u'
        };
    /** Consonanti*/
    private static final char[] consonants =
        {
            'b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n',
            'p', 'q', 'r', 's', 't', 'v', 'w', 'x', 'y', 'z'
        };

    /** Valori per il calcolo del carattere di controllo*/
     private static final int[] evenMapping =
        {
            1,  // a, 0
            0,  // b, 1
            5,  // c, 2
            7,  // d, 3
            9,  // e, 4
            13, // f, 5
            15, // g, 6
            17, // h, 7
            19, // i, 8
            21, // j, 9
            2,  // k
            4,  // l
            18, // m
            20, // n
            11, // o
            3,  // p
            6,  // q
            8,  // r
            12, // s
            14, // t
            16, // u
            10, // v
            22, // w
            25, // x
            24, // y
            23  // z
        };

    /** Valori per il calcolo del carattere di controllo*/
    private static final int[] oddMapping =
        {
            0,  // a, 0
            1,  // b, 1
            2,  // c, 2
            3,  // d, 3
            4,  // e, 4
            5,  // f, 5
            6,  // g, 6
            7,  // h, 7
            8,  // i, 8
            9,  // j, 9
            10, // k
            11, // l
            12, // m
            13, // n
            14, // o
            15, // p
            16, // q
            17, // r
            18, // s
            19, // t
            20, // u
            21, // v
            22, // w
            23, // x
            24, // y
            25  // z
        };

    /** Codifica Mesi*/
    private static final char[] months =
        {
            'a',    // January
            'b',    // February
            'c',    // March
            'd',    // April
            'e',    // May
            'h',    // June
            'l',    // July
            'm',    // August
            'p',    // September
            'r',    // October
            's',    // November
            't'     // December
        };


   /* private static final char[] similars =
        {
            'l',    // 0
            'm',    // 1
            'n',    // 2
            'p',    // 3
            'q',    // 4
            'r',    // 5
            's',    // 6
            't',    // 7
            'u',    // 8
            'v'     // 9
        };*/

     /** Maschera cifre*/
     private static final boolean[] isDigit =
     {
         false,  // Surname
         false,  // Surname
         false,  // Surname
         false,  // Name
         false,  // Name
         false,  // Name
         true,   // year
         true,   // year
         false,  // month
         true,   // day
         true,   // day
         false,  // birthplace
         true,   // birthplace
         true,   // birthplace
         true,   // birthplace
         false   // control
     };


    /**
     * Costruttore
     */
    public CodiceFiscale(){}


 	/**
 	 * Calcola il codice fiscale
 	 *
 	 * @param nome nome
 	 * @param cognome cognome
 	 * @param sesso sesso
 	 * @param dataNascita data nascita
 	 * @param codLuogoNascita luogo di nascita
 	 * @return Codice fiscale
 	 * @exception java.text.ParseException Errore nel parsing
 	 */
 	public static String getCF(String nome, String cognome, String sesso, String dataNascita, String codLuogoNascita) throws ParseException
   	{
        StringBuffer code = new StringBuffer();
        String nomeSenzaApostrofi = TextUtility.cleanString(nome.toLowerCase());
        String cognomeSenzaApostrofi = TextUtility.cleanString(cognome.toLowerCase());
        char[] name = nomeSenzaApostrofi.toCharArray();
        char[] surname = cognomeSenzaApostrofi.toCharArray();
        int sex;
        if(sesso.equalsIgnoreCase("F"))
        	sex = FEMALE;
        else
        	sex = MALE;
        Date birthdate;
        String formatString="";
		if(dataNascita.length()==8)
        {
			formatString = "yyyyMMdd";
        }
        else if(dataNascita.length()==14)
        {
            formatString = "yyyyMMddHHmmss";
        }
        SimpleDateFormat sdf = (SimpleDateFormat)DateFormat.getInstance();
		sdf.applyPattern(formatString);
		birthdate = sdf.parse(dataNascita);
        String birthplaceCode = codLuogoNascita.toLowerCase();

        code.append(encodeSurname(encodeVowels(surname), sex));   // surname
        code.append(encodeName(encodeVowels(name)));              // name
        code.append(encodeBirthdate(birthdate, sex));             // birthdate and sex
        code.append(birthplaceCode);                              // birthplace
        code.append(controlCode(code.toString().toCharArray()));  // control char


        return code.toString().toUpperCase();
    }

 	/**
 	 * Ottiene la codifica del cognome (le prime tre lettere del codice fiscale)
 	 *
 	 * @param surname Cognome
 	 * @param sex Sesso
 	 * @return Array di caratteri con testo codificato
 	 * @exception java.text.ParseException Errore nel parsing
 	 */
 	private static final char[] encodeSurname(char[] surname, int sex) throws ParseException
    {
        char[] truncatedSurname = surname;

        if(sex == FEMALE)    // female
        {
            String stSurname =  new String(surname);
            int index = stSurname.indexOf(COMPOUND_SURNAME); // cognome composto
            if(index != - 1)
                truncatedSurname = stSurname.substring(0,index).toCharArray();
        }
        // conteggio consonanti
        int consonantCount = countCharType(truncatedSurname, consonants);

        // conteggio vocali
        int vowelCount = countCharType(truncatedSurname, vowels);


        char cons[] = new char[truncatedSurname.length];
        char vows[] = new char[truncatedSurname.length];
        int i, j, k;

        for(i = 0,j = 0,k = 0; i < truncatedSurname.length; i++)
        {
            if(CodiceFiscale.ofType(truncatedSurname[i], consonants))
                cons[j++] = truncatedSurname[i];
            else if(CodiceFiscale.ofType(truncatedSurname[i], vowels))
                vows[k++] = truncatedSurname[i];
        }
        // regole di codifica
        char[] encodedSurname = new char[SURNAME_CODE_LENGTH];
        if(consonantCount >= 3)
        {
            encodedSurname[0] = cons[0];
            encodedSurname[1] = cons[1];
            encodedSurname[2] = cons[2];
        }
        else if((consonantCount == 2)&& (vowelCount >= 1))
        {
            encodedSurname[0] = cons[0];
            encodedSurname[1] = cons[1];
            encodedSurname[2] = vows[0];
        }
        else if((consonantCount == 2)&& (vowelCount == 0))
        {
            encodedSurname[0] = cons[0];
            encodedSurname[1] = cons[1];
            encodedSurname[2] = DUMMY_CHAR;
        }
        else if((consonantCount == 1) && (vowelCount >= 2))
        {
            encodedSurname[0] = cons[0];
            encodedSurname[1] = vows[0];
            encodedSurname[2] = vows[1];
        }
        else if((consonantCount == 1) && (vowelCount == 1))
        {
            encodedSurname[0] = cons[0];
            encodedSurname[1] = vows[0];
            encodedSurname[2] = DUMMY_CHAR;
        }
        else if((consonantCount == 1) && (vowelCount == 0))
        {
            encodedSurname[0] = cons[0];
            encodedSurname[1] = DUMMY_CHAR;
            encodedSurname[2] = DUMMY_CHAR;
        }
        else if((consonantCount == 0) && (vowelCount >= 3))
        {
            encodedSurname[0] = vows[0];
            encodedSurname[1] = vows[1];
            encodedSurname[2] = vows[2];
        }
        else if((consonantCount == 0) && (vowelCount == 2))
        {
            encodedSurname[0] = vows[0];
            encodedSurname[1] = vows[1];
            encodedSurname[2] = DUMMY_CHAR;
        }
        else if((consonantCount == 0) && (vowelCount == 1))
        {
            encodedSurname[0] = vows[0];
            encodedSurname[1] = DUMMY_CHAR;
            encodedSurname[2] = DUMMY_CHAR;
        }
        else if((consonantCount == 0) && (vowelCount == 0))
        {
            encodedSurname[0] = DUMMY_CHAR;
            encodedSurname[1] = DUMMY_CHAR;
            encodedSurname[2] = DUMMY_CHAR;
        }
        else
            throw new ParseException("", 0);


        return encodedSurname;
    }

 	/**
 	 * Ottiene la codifica del nome (le tre lettere del codice fiscale relative al nome)
 	 *
 	 * @param name nome
 	 * @return Array di caratteri con testo codificato
 	 * @exception java.text.ParseException Errore nel parsing
 	 */
 	private static final char[] encodeName(char[] name) throws ParseException
    {

        int consonantCount = countCharType(name, consonants);
        int vowelCount = countCharType(name, vowels);
        char cons[] = new char[name.length];
        char vows[] = new char[name.length];
        int i, j, k;

        for(i = 0,j = 0,k = 0; i < name.length; i++)
        {
            if(ofType(name[i], consonants))
                cons[j++] = name[i];
            else if(ofType(name[i], vowels))
                vows[k++] = name[i];
        }
        // regole di codifica
        char[] encodedName = new char[NAME_CODE_LENGTH];
        if(consonantCount >= 4)
        {
            encodedName[0] = cons[0];
            encodedName[1] = cons[2];
            encodedName[2] = cons[3];
        }
        else if(consonantCount == 3)
        {
            encodedName[0] = cons[0];
            encodedName[1] = cons[1];
            encodedName[2] = cons[2];

        }
        else if((consonantCount == 2)&&((vowelCount >= 1)))
        {
            encodedName[0] = cons[0];
            encodedName[1] = cons[1];
            encodedName[2] = vows[0];
        }
        else if((consonantCount == 2)&&((vowelCount == 0)))
        {
            encodedName[0] = cons[0];
            encodedName[1] = cons[1];
            encodedName[2] = DUMMY_CHAR;
        }
        else if((consonantCount == 1) && (vowelCount >= 2))
        {
            encodedName[0] = cons[0];
            encodedName[1] = vows[0];
            encodedName[2] = vows[1];
        }
        else if((consonantCount == 1) && (vowelCount == 1))
        {
            encodedName[0] = cons[0];
            encodedName[1] = vows[0];
            encodedName[2] = DUMMY_CHAR;
        }
        else if((consonantCount == 1) && (vowelCount == 0))
        {
            encodedName[0] = cons[0];
            encodedName[1] = DUMMY_CHAR;
            encodedName[2] = DUMMY_CHAR;
        }
        else if((consonantCount == 0) && (vowelCount > 2))
        {
            encodedName[0] = vows[0];
            encodedName[1] = vows[1];
            encodedName[2] = vows[2];
        }
        else if((consonantCount == 0) && (vowelCount == 2))
        {
            encodedName[0] = vows[0];
            encodedName[1] = vows[1];
            encodedName[2] = DUMMY_CHAR;
        }
        else if((consonantCount == 0) && (vowelCount == 1))
        {
            encodedName[0] = vows[0];
            encodedName[1] = DUMMY_CHAR;
            encodedName[2] = DUMMY_CHAR;
        }
        else
            throw new ParseException("", 0);


        return encodedName;
    }

 	/**
 	 * Codifica un testo ottenuto sostituendo le vocali accentate con le vocali non accentate
 	 *
 	 * @param text testo in minuscolo da codificare
 	 * @return Array di caratteri con testo codificato
 	 */
 	private static final char[] encodeVowels(char[] text)
    {
        char[] encodedText = new char[text.length];
        boolean encoded;
        int i, j;

        for(i = 0; i < text.length; i++)
        {
            encoded = false;
            for(j = 0; j < vowelsMapping.length; j++)
                if(text[i] == vowelsMapping[j][0])
                {
                    encoded = true;
                    encodedText[i] = vowelsMapping[j][1];
                    break;
                }
            if(encoded == false)
                encodedText[i] = text[i];
        }

        return encodedText;
    }

 	/**
 	 * codifica della data di nascita e del sesso
 	 *
 	 * @param birthdate la data di nascita della persona
 	 * @param sex il sesso della persona
 	 * @return Array di caratteri con testo codificato
 	 * @exception java.text.ParseException Errore nel parsing
 	 */


    private static final char[] encodeBirthdate(Date birthdate, int sex) throws ParseException
    {
        Calendar birthdateCal = new GregorianCalendar();
        birthdateCal.setTime(birthdate);

        StringBuffer code = new StringBuffer();
        //Integer anno = new Integer(birthdateCal.get(Calendar.YEAR)-1900);
        Integer anno = new Integer(birthdateCal.get(Calendar.YEAR));
        /*Integer anno;
        if((birthdateCal.get(Calendar.YEAR)>2000))
           anno = new Integer(birthdateCal.get(Calendar.YEAR)-2000);
        else
           anno = new Integer(birthdateCal.get(Calendar.YEAR)-1900);  */

        // anno (yy)
        String annoStr = anno.toString();
        //log.debug(annoStr);

        //code.append(anno.toString().substring(anno.toString().length()-2,2));
        code.append(annoStr.substring(annoStr.length()-2));

        // mese (M)
        code.append(months[birthdateCal.get(Calendar.MONTH)]);

        // giorno (dd)
        if(sex == FEMALE)
        {
            int day = birthdateCal.get(Calendar.DATE);
            day += AGE_OFFSET;
            code.append(day); // day + 40 (dd)
        }
        else // male
            {
            if (birthdateCal.get(Calendar.DATE)<10) code.append(0);
            code.append(new Integer(birthdateCal.get(Calendar.DATE)).toString()); // giorno (dd)
            }
        return code.toString().toCharArray();
    }

    /**
     * Verifica del carattere di controllo
     *
     * @param code Codice da controllare
     * @return true se carattere valido false se non valido
     * @exception java.text.ParseException Errore nel parsing
     */
    public static final char controlCode(char[] code) throws ParseException
    {
        char control;
        int i, sum, index;

        for(i = 0, sum = 0; i < PERSON_FISCAL_CODE_LENGTH - 1; i++)
        {
            if(Character.isDigit(code[i])) // cifra numerica
                index = code[i] - '0';

            else // carattere alfabetico
                index = code[i] - 'a';

            try
            {
                if(i%2 == 0) // pari
                    sum += evenMapping[index];
                else         // dispari
                    sum += oddMapping[index];
            }
            catch (ArrayIndexOutOfBoundsException a)
            {
                throw new ParseException("",i);
            }

        }

        control = 'a';
        control += sum%evenMapping.length;

        return control;
    }


    /**
     * Conta i caratteri validi che esistono in un insieme
     *
     * @param text caratteri da esaminare
     * @param chars insieme di cartteri validi
     * @return numero caratteri
     */
    private static final int countCharType(char[] text, char[] chars)
    {
        int count = 0;
        int i, j;

        for(i = 0; i < text.length; i++)
            for(j = 0; j < chars.length; j++)
                if(text[i] == chars[j])
                {
                    ++count;
                    break;
                }
        return count;
    }


    /**
     * Verifica se un carattere � valido
     *
     * @param character carattere
     * @param chars Insieme dei caratteri validi
     * @return true se carattere valido
     * false se non valido
     */
    private static final boolean ofType(char character, char[] chars)
    {
        boolean isIn = false;

        for(int j = 0;j < chars.length; j++)
            if(character == chars[j])
            {
                isIn = true;
                break;
            }

        return isIn;
    }
}

/*
 * $Log: CodiceFiscale.java,v $
 * Revision 1.4  2007/01/16 14:56:51  crea
 * *** empty log message ***
 *
 * Revision 1.3.14.1  2007/01/10 11:03:55  crea
 * *** empty log message ***
 *
 * Revision 1.3  2006/05/10 15:31:04  crea
 * Bug fix che fa riferimento alle nuove modifiche di ae-mock che ricrea il CF se c'� una variazione di nome cognome o sesso
 *
 * Revision 1.2  2006/02/22 12:03:38  crea
 * Corretto con il nuovo ambiente di test
 *
 * Revision 1.1  2006/02/20 15:47:59  crea
 * *** empty log message ***
 *
 * Revision 1.1  2006/02/14 11:28:06  crea
 * Modifiche per i test. Agenzia risponde che l'operazione di AttribuzioneCF è andata a buon fine
 *
 * Revision 1.4  2005/03/29 13:44:37  lmanzoni
 * Allineamento CVS Trieste
 *
 * Revision 1.1.6.1  2005/03/09 09:38:38  lmanzoni
 * aggiunti i test http e functional
 *
 * Revision 1.3  2005/03/03 14:06:25  lmanzoni
 * Logging - utilizzo di Category
 *
 * Revision 1.2  2005/03/01 14:51:10  lmanzoni
 * commit merge CR_TOSCANI
 *
 * Revision 1.1.2.2  2005/02/17 11:46:41  amedeo
 * Modifiche in seguito ad aggiunta dei test della CR_LOG
 *
 * Revision 1.1.4.1  2005/02/08 17:11:21  lmanzoni
 * commit dopo il merge con la CR_TRANSACTOR
 *
 * Revision 1.1.2.1  2005/02/03 09:30:35  lmanzoni
 * BackOfficePreventivoCasa
 *
 */
