package org.xxmlblackbox.test.functional.suite;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class SuiteAllTest extends TestCase {

	  public SuiteAllTest(String name) {
		    super(name);
		  }

      public static Test suite() {
		  
		  TestSuite suite = new TestSuite("SuiteAllTest");
            
          return suite;
      }

}
