
package net.selfip.gcdev.cdt.testsrunner.internal.cppunit;

import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.text.MessageFormat;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import net.selfip.gcdev.cdt.testsrunner.internal.cppunit.CppUnitTestsRunnerMessages;
import net.selfip.gcdev.cdt.testsrunner.internal.cppunit.CppUnitXmlLogHandler;

import org.eclipse.cdt.testsrunner.launcher.ITestsRunnerProvider;
import org.eclipse.cdt.testsrunner.model.ITestModelUpdater;
import org.eclipse.cdt.testsrunner.model.TestingException;
import org.xml.sax.SAXException;

public class CppUnitTestsRunnerProvider implements ITestsRunnerProvider {

	public CppUnitTestsRunnerProvider() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String[] getAdditionalLaunchParameters(String[][] testPaths)
			throws TestingException {
		final String[] cppUnitParameters = { };
		String[] result = cppUnitParameters;
		
		return result;
	}
	
    /**
     * Construct the error message from prefix and detailed description.
     *
     * @param prefix prefix
     * @param description detailed description
     * @return the full message
     */
	private String getErrorText(String prefix, String description) {
		return MessageFormat.format(CppUnitTestsRunnerMessages.CppUnitTestsRunner_error_format, prefix, description);
	}
	
	
	@Override
	public void run(ITestModelUpdater modelUpdater, InputStream inputStream)
			throws TestingException {
		try {
			BufferedInputStream bis = new BufferedInputStream(inputStream);
			
			// Looking for <?xml
			int b = 0;
			boolean found = false;
			while (!found) {
				bis.mark(5);
				b = bis.read();

				if ((char)b != '<')	continue;
				
				while (true) {				
					b = bis.read();
					if ((char)b != '?') break;
	
					b = bis.read();
					if ((char)b != 'x') break;
	
					b = bis.read();
					if ((char)b != 'm') break;
	
					b = bis.read();
					if ((char)b != 'l') break;
					
					// Successfully located <?xml -- now reset back to the mark and let SAX proceed. 
					found = true;
					break;
				}
				bis.reset();
			}
			
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			sp.parse(bis, new CppUnitXmlLogHandler(modelUpdater));

		} catch (IOException e) {
			throw new TestingException(getErrorText(CppUnitTestsRunnerMessages.CppUnitTestsRunner_io_error_prefix, e.getLocalizedMessage()));
			
		} catch (NumberFormatException e) {
			throw new TestingException(getErrorText(CppUnitTestsRunnerMessages.CppUnitTestsRunner_xml_error_prefix, e.getLocalizedMessage()));

		} catch (ParserConfigurationException e) {
			throw new TestingException(getErrorText(CppUnitTestsRunnerMessages.CppUnitTestsRunner_xml_error_prefix, e.getLocalizedMessage()));

		} catch (SAXException e) {
			throw new TestingException(getErrorText(CppUnitTestsRunnerMessages.CppUnitTestsRunner_xml_error_prefix, e.getLocalizedMessage()));
		}
	}		

}
