package org.apfloat.spi;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @version 1.0
 * @author Mikko Tommila
 */

public class FilenameGeneratorTest
    extends TestCase
{
    public FilenameGeneratorTest(String methodName)
    {
        super(methodName);
    }

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite()
    {
        TestSuite suite = new TestSuite();

        suite.addTest(new FilenameGeneratorTest("testGenerateFilename"));

        return suite;
    }

    public static void testGenerateFilename()
    {
        FilenameGenerator filenameGenerator = new FilenameGenerator("path", "5", "suffix");
        assertEquals("filename", "path5suffix", filenameGenerator.generateFilename());
        assertEquals("filename", "path6suffix", filenameGenerator.generateFilename());
    }
}
