import java.io.File;
import java.io.IOException;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;


public class XpathSelect {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws XmlException 
	 */
	public static void main(String[] args) throws XmlException, IOException {
		// TODO Auto-generated method stub
		XmlObject xobj = XmlObject.Factory.parse(
				new File("C:/workspace/XmlBlackBoxImola/xmlbeansExample.xml"));

		String xpath = "$this//xsd:getPerson/xsd:arg0/xsd:address[1]";
		String decleareNamespace= "declare namespace xsd='http://webservice.test.xmlblackbox.org/';";
		
		XmlObject[] xmlObject = xobj.selectPath(decleareNamespace+xpath);
        System.out.println("xmlObject.length "+xmlObject.length);
        System.out.println("xmlObject[0] "+xmlObject[0]);
        System.out.println("xmlObject[0].xmlText() "+xmlObject[0].xmlText());

		xpath = "$this//xsd:getPerson/xsd:arg0/xsd:address[2]";
		xmlObject = xobj.selectPath(decleareNamespace+xpath);

        System.out.println("xmlObject.length "+xmlObject.length);
        System.out.println("xmlObject[2] "+xmlObject[0]);
        System.out.println("xmlObject[2].xmlText() "+xmlObject[0].xmlText());

		xpath = "$this//xsd:getPerson/xsd:arg0/xsd:address[3]";
		xmlObject = xobj.selectPath(decleareNamespace+xpath);

        System.out.println("xmlObject.length "+xmlObject.length);
        System.out.println("xmlObject[3] "+xmlObject[0]);
        System.out.println("xmlObject[3].xmlText() "+xmlObject[0].xmlText());

        XmlCursor xmlCursor = xobj.newCursor();
        xmlCursor.toNextToken();
        XmlCursor xmlCursor2 = xmlCursor.execQuery(decleareNamespace+xpath);
        System.out.println("xmlCursor.getObject().toString() "+xmlCursor.getObject().toString());

	}

}
