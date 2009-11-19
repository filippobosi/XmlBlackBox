import org.xmlblackbox.test.infrastructure.exception.XmlValidationFault;
import org.xmlblackbox.test.infrastructure.util.ValidateXML;


public class ValidateXSD {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        String xml="src/test/java/org/xmlblackbox/test/functional/examples/Example999.xml";
        String xsd="src/test/resources/xmlblackbox_1_0.xsd";

		ValidateXML validateXml = new  ValidateXML(xml, xsd);
		try {
			validateXml.validate();
			System.out.println("OK Validation");
		} catch (XmlValidationFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
