package persional.cheneyjin.warframealerts.handler;

import java.text.ParseException;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import persional.cheneyjin.warframealerts.objs.RssItem;
import persional.cheneyjin.warframealerts.utils.NowDateTime;

/**
 * @author CheneyJin 
 *	E-mail:cheneyjin@outlook.com
 */
public class RssHandler extends DefaultHandler {

	private RssFeed rssFeed;
	private RssItem rssItem;
	private StringBuilder textBuffer;
	private int currentstate = 0;
	private NowDateTime ndf;
	// final int ITEM_GUID = 1;
	final int ITEM_TITLE = 1;
	final int ITEM_AUTHOR = 2;
	final int ITEM_DESCRIPTION = 3;
	final int ITEM_PUBDATE = 4;

	// final int ITEM_WFFACTION = 6;
	final int ITEM_WFEXPIRY = 7;

	public RssHandler() {
		textBuffer = new StringBuilder();
		rssFeed = new RssFeed();
		rssItem = new RssItem();
		ndf = new NowDateTime();
	}

	public RssFeed getRssFeed() {
		return rssFeed;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
	}

	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		textBuffer.append(ch, start, length);
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		textBuffer.delete(0, textBuffer.length());
		if ("channel".equals(qName)) { currentstate = 0; return; }
		if ("item".equals(qName)) { rssItem = new RssItem(); return; }
		if ("title".equals(qName)) { currentstate = ITEM_TITLE; return; }
		if ("author".equals(qName)) { currentstate = ITEM_AUTHOR; return; }
		if ("description".equals(qName)) { currentstate = ITEM_DESCRIPTION; return; }
		//if("pubDate".equals(qName)){ currentstate = ITEM_PUBDATE; return; }
		if("wf:expiry".equals(qName)){ currentstate = ITEM_WFEXPIRY; return; }	
		currentstate = 0;
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		String value = textBuffer.toString();
		switch (currentstate) {
			case ITEM_TITLE: rssItem.setItem_title(value); currentstate = 0; break;
			case ITEM_AUTHOR: rssItem.setItem_author(value); currentstate = 0; break;
			case ITEM_DESCRIPTION: rssItem.setItem_description(value); currentstate = 0; break;
			case ITEM_WFEXPIRY: 
				try {
					String formatExpiry = ndf.formatExpiry(value);
					ndf.setExpiryDate(formatExpiry);
					rssItem.setFormat_expiry(formatExpiry);
					rssItem.setItem_wf_expiry(ndf.timeDiff());
				} catch (ParseException e) {
					e.printStackTrace();
				}finally{
					currentstate = 0; 
				}
				break; 
			default: break;
		}
		if ("item".equals(qName)) { rssFeed.addItem(rssItem); return; }
	}
}
