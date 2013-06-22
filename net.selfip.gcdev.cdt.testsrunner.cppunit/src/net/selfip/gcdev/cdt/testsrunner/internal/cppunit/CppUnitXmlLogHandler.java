/**
 * 
 */
package net.selfip.gcdev.cdt.testsrunner.internal.cppunit;

import java.text.MessageFormat;
import java.util.Stack;

import org.eclipse.cdt.testsrunner.model.ITestModelUpdater;
import org.eclipse.cdt.testsrunner.model.ITestItem;
import org.eclipse.cdt.testsrunner.model.ITestMessage;
import org.eclipse.cdt.testsrunner.model.ITestItem.Status;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Parses the CPPUnit XML log and notifies the Tests Runner Core about how
 * the testing process is going.
 */
public class CppUnitXmlLogHandler extends DefaultHandler {
	
	// CppUnit XML log tags
	private static final String XML_NODE_TEST_RUN = "TestRun"; //$NON-NLS-1$
	private static final String XML_NODE_FAILED_TESTS = "FailedTests"; //$NON-NLS-1$
	private static final String XML_NODE_SUCCESSFUL_TESTS = "SuccessfulTests"; //$NON-NLS-1$
	private static final String XML_NODE_TEST = "Test"; //$NON-NLS-1$
	private static final String XML_NODE_FAILED_TEST = "FailedTest"; //$NON-NLS-1$
	private static final String XML_NODE_LOCATION = "Location"; //$NON-NLS-1$
	private static final String XML_NODE_FILE = "File"; //$NON-NLS-1$
	private static final String XML_NODE_LINE = "Line"; //$NON-NLS-1$
	private static final String XML_NODE_MESSAGE = "Message"; //$NON-NLS-1$
	
	private static final String XML_ATTR_ID = "id"; //$NON-NLS-1$
	private static final String XML_NODE_NAME = "Name"; //$NON-NLS-1$
	private static final String XML_NODE_STATISTICS = "Statistics"; //$NON-NLS-1$
	private static final String XML_NODE_STATS_TESTS = "Tests"; //$NON-NLS-1$
	private static final String XML_NODE_STATS_FAILURES_TOTAL = "FailuresTotal"; //$NON-NLS-1$
	private static final String XML_NODE_STATS_ERRORS = "Errors"; //$NON-NLS-1$
	private static final String XML_NODE_STATS_FAILURES = "Failures"; //$NON-NLS-1$
	private static final String XML_NODE_FAILURE_TYPE = "FailureType"; //$NON-NLS-1$
	
	private static final String XML_VALUE_FAILURE_TYPE_ERROR = "Error"; //$NON-NLS-1$
	private static final String XML_VALUE_FAILURE_TYPE_ASSERTION = "Assertion"; //$NON-NLS-1$
	
    /** The default file name for test message location. */
	private static final String DEFAULT_LOCATION_FILE = null;

	/** The default line number for test message location. */
	private static final int DEFAULT_LOCATION_LINE = -1;
    
	/** The interface to notify the Tests Runner Core */
	private ITestModelUpdater modelUpdater;
	
	/** Stores the text between XML tags. */
	private Stack<StringBuilder> elementDataStack = new Stack<StringBuilder>();
	
	/** File name for current test message location. */
	private String fileName;

	/** Test ID */
	private String testId;
	
	/** Line number for current test message location. */
	private int lineNumber;

	/** Current test case status. */
	private ITestItem.Status testStatus;
	
	
	CppUnitXmlLogHandler(ITestModelUpdater modelUpdater) {
		this.modelUpdater = modelUpdater;
	}
	
	@Override
	public void startElement(String namespaceURI, String localName, String qName, Attributes attrs) throws SAXException {
		
		elementDataStack.push(new StringBuilder());
		if (qName == XML_NODE_TEST_RUN) {
			modelUpdater.enterTestSuite("CppUnit Test Suite");

		} else if (qName == XML_NODE_NAME) {

		} else if (qName == XML_NODE_FAILED_TESTS) {
			
		// Nodes found inside FailedTests
		} else if (qName == XML_NODE_FAILED_TEST) {
			testId = attrs.getValue(XML_ATTR_ID);
			
		} else if (qName == XML_NODE_FAILURE_TYPE) {
			
		} else if (qName == XML_NODE_LOCATION) {
			
		} else if (qName == XML_NODE_FILE) {
			
		} else if (qName == XML_NODE_LINE) {
			
		} else if (qName == XML_NODE_MESSAGE) {

		// Nodes found inside SuccessfulTests	
		} else if (qName == XML_NODE_SUCCESSFUL_TESTS) {
			
		} else if (qName == XML_NODE_TEST) {
			testStatus = Status.Passed;
			testId = attrs.getValue(XML_ATTR_ID);
			
		} else if (qName == XML_NODE_STATISTICS) {
		
		} else if (qName == XML_NODE_STATS_TESTS) {
			
		} else if (qName == XML_NODE_STATS_FAILURES_TOTAL) {
			
		} else if (qName == XML_NODE_STATS_ERRORS) {
			
		} else if (qName == XML_NODE_STATS_FAILURES) {
						
		} else {
			logAndThrowErrorForElement(qName);
		}
	}
	
	/**
	 * Common routing: notifies the Tests Runner core about new test message
	 * and resets the internal state.
	 * 
	 * @param level test message level
	 */
	private void addCurrentMessage(ITestMessage.Level level) {
		modelUpdater.addTestMessage(fileName, lineNumber, level, elementDataStack.peek().toString());
		fileName = DEFAULT_LOCATION_FILE;
		lineNumber = DEFAULT_LOCATION_LINE;
	}

	@Override
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {

		if (qName == XML_NODE_TEST_RUN) {
			modelUpdater.exitTestSuite();
			
		} else if (qName == XML_NODE_FAILED_TESTS) {
			// End of the failed tests
			
		} else if (qName == XML_NODE_FAILURE_TYPE) {
			String failType = elementDataStack.peek().toString().trim();
			if (failType.equals(XML_VALUE_FAILURE_TYPE_ASSERTION)) {
				testStatus = Status.Failed;
			} else if (failType.equals(XML_VALUE_FAILURE_TYPE_ERROR)) {
				testStatus = Status.Aborted;
			}
			modelUpdater.setTestStatus(testStatus);
			
		} else if (qName == XML_NODE_LOCATION) {
			
		} else if (qName == XML_NODE_FILE) {
			fileName = elementDataStack.peek().toString().trim();
			
		} else if (qName == XML_NODE_LINE) {
			String lineNumberStr = elementDataStack.peek().toString().trim();
			lineNumber = lineNumberStr != null ? Integer.parseInt(lineNumberStr.trim()) : DEFAULT_LOCATION_LINE;
			
		} else if (qName == XML_NODE_MESSAGE) {
			if (testStatus == Status.Failed) {
				addCurrentMessage(ITestMessage.Level.Error);
			} else {
				addCurrentMessage(ITestMessage.Level.FatalError);
			}

		} else if (qName == XML_NODE_SUCCESSFUL_TESTS) {
			// End of the successful tests

		} else if (qName == XML_NODE_FAILED_TEST) {
			modelUpdater.exitTestCase();

		} else if (qName == XML_NODE_TEST) {
			modelUpdater.setTestStatus(testStatus);
			modelUpdater.exitTestCase();
			
		} else if (qName == XML_NODE_NAME) {
			String name = elementDataStack.peek().toString().trim();
			modelUpdater.enterTestCase(name);

		} else if (qName == XML_NODE_STATISTICS) {
			
		} else if (qName == XML_NODE_STATS_TESTS) {
			
		} else if (qName == XML_NODE_STATS_FAILURES_TOTAL) {
			
		} else if (qName == XML_NODE_STATS_ERRORS) {
			
		} else if (qName == XML_NODE_STATS_FAILURES) {
			
		} else {
			logAndThrowErrorForElement(qName);
		}
		elementDataStack.pop();
	}
	
	@Override
	public void characters(char[] ch, int start, int length) {
		StringBuilder builder = elementDataStack.peek();
		for (int i = start; i < start + length; i++) {
			builder.append(ch[i]);
		}
	}
	
	/**
	 * Throws the testing exception for the specified XML tag.
	 * 
	 * @param tagName XML tag name
	 * @throws SAXException the exception that will be thrown
	 */
	private void logAndThrowErrorForElement(String tagName) throws SAXException {
		logAndThrowError(
			MessageFormat.format(CppUnitTestsRunnerMessages.CppUnitXmlLogHandler_wrong_tag_name, tagName)
		);
	}
	
	/**
	 * Throws the testing exception with the specified message.
	 * 
	 * @param message the reason
	 * @throws SAXException the exception that will be thrown
	 */
	private void logAndThrowError(String message) throws SAXException {
		SAXException e = new SAXException(message);
		CppUnitTestsRunnerPlugin.log(e);
		throw e;
	}

	
	@Override
	public void warning(SAXParseException ex) throws SAXException {
		CppUnitTestsRunnerPlugin.log(ex);
	}

	@Override
	public void error(SAXParseException ex) throws SAXException {
		CppUnitTestsRunnerPlugin.log(ex);
		throw ex;
	}

	@Override
	public void fatalError(SAXParseException ex) throws SAXException {
		CppUnitTestsRunnerPlugin.log(ex);
		throw ex;
	}
	
}
