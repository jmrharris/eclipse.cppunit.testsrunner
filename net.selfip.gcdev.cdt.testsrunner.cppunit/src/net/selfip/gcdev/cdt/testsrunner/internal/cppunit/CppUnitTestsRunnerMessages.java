/**
 * 
 */
package net.selfip.gcdev.cdt.testsrunner.internal.cppunit;

import org.eclipse.osgi.util.NLS;

public class CppUnitTestsRunnerMessages extends NLS {
	private static final String BUNDLE_NAME = "net.sf.cdt.testsrunner.internal.cppunit.CppUnitTestsRunnerMessages"; //$NON-NLS-1$
	public static String CppUnitTestsRunner_error_format;
	public static String CppUnitTestsRunner_io_error_prefix;
	public static String CppUnitTestsRunner_wrong_tests_paths_count;
	public static String CppUnitTestsRunner_xml_error_prefix;
	public static String CppUnitXmlLogHandler_exception_suffix;
	public static String CppUnitXmlLogHandler_wrong_tag_name;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, CppUnitTestsRunnerMessages.class);
		
	}

	private CppUnitTestsRunnerMessages() {
	}
}

