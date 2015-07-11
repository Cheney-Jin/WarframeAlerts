package persional.cheneyjin.warframealerts.http;

import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import info.monitorenter.cpdetector.io.ParsingDetector;
import info.monitorenter.cpdetector.io.UnicodeDetector;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
/**
 * @author CheneyJin 
 *	E-mail:cheneyjin@outlook.com
 */
public class RssFeedSAXParser {

	public RssFeed getFeed(String urlStr) throws ParserConfigurationException, SAXException, IOException{
		URL url = new URL(urlStr);
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		SAXParser saxParser = saxParserFactory.newSAXParser();
		XMLReader xmlReader = saxParser.getXMLReader();

		RssHandler rssHandler = new RssHandler();
		xmlReader.setContentHandler(rssHandler);
		CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
		detector.add(JChardetFacade.getInstance());
		Charset charset = detector.detectCodepage(url);
		String encodingName = charset.name();

		InputSource inputSource = null;
		InputStream stream = null;
		if ("GBK".equals(encodingName)) {
			stream = url.openStream();
			InputStreamReader streamReader = new InputStreamReader(stream,encodingName);
			inputSource = new InputSource(streamReader);
			xmlReader.parse(inputSource);
			return rssHandler.getRssFeed();
		} else {
			// UTF-8
			inputSource = new InputSource(url.openStream());
			inputSource.setEncoding("UTF-8");
			xmlReader.parse(inputSource);
			return rssHandler.getRssFeed();
		}
	}

	public static String getReomoteURLFileEncode(URL url) {

		CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
		detector.add(new ParsingDetector(false));
		detector.add(JChardetFacade.getInstance());
		detector.add(ASCIIDetector.getInstance());
		detector.add(UnicodeDetector.getInstance());
		java.nio.charset.Charset charset = null;
		try {
			System.out.println(url);
			charset = detector.detectCodepage(url);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (charset != null) {
			return charset.name();
		} else {
			return "utf-8";
		}
	}
}
