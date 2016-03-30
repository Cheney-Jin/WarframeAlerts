package persional.cheneyjin.warframealerts.handler;

import java.text.ParseException;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import persional.cheneyjin.warframealerts.objs.RssItem;
import persional.cheneyjin.warframealerts.utils.NowDateTime;

/**
 * @author CheneyJin E-mail:cheneyjin@outlook.com
 */
public class RssHandler extends DefaultHandler {

	private RssFeed rssFeed;
	private RssItem rssItem;
	private StringBuilder textBuilder;
	private int currentstate = 0;
	private NowDateTime ndf;
	private String formatExpiry;
	private String itemValue;
	private final int ITEM_TITLE = 1;
	private final int ITEM_AUTHOR = 2;
	private final int ITEM_DESCRIPTION = 3;
	private final int ITEM_WFEXPIRY = 7;

	// private final int ITEM_PUBDATE = 4;
	// final int ITEM_GUID = 1;
	// final int ITEM_WFFACTION = 6;

	public RssHandler() {
		textBuilder = new StringBuilder();
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
		textBuilder.append(ch, start, length);
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		textBuilder.delete(0, textBuilder.length());
		if ("item".equals(qName)) {
			rssItem = new RssItem();
			return;
		}
		if ("title".equals(qName)) {
			currentstate = ITEM_TITLE;
			return;
		}
		if ("author".equals(qName)) {
			currentstate = ITEM_AUTHOR;
			return;
		}
		if ("description".equals(qName)) {
			currentstate = ITEM_DESCRIPTION;
			return;
		}
		if ("wf:expiry".equals(qName)) {
			currentstate = ITEM_WFEXPIRY;
			return;
		}
		currentstate = 0;
	}

	// This is others RSS value;
	// if ("channel".equals(qName)) { currentstate = 0; return; }
	// if("pubDate".equals(qName)){ currentstate = ITEM_PUBDATE; return; }

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		itemValue = textBuilder.toString();
		switch (currentstate) {
		case ITEM_TITLE:
			rssItem.setItem_title(itemValue);
			break;
		case ITEM_AUTHOR:
			rssItem.setItem_author(itemValue);
			break;
		case ITEM_DESCRIPTION:
			rssItem.setItem_description(itemValue);
			break;
		case ITEM_WFEXPIRY:
			try {
				formatExpiry = ndf.formatExpiry(itemValue);
				ndf.setExpiryDate(formatExpiry);
				rssItem.setFormat_expiry(formatExpiry);
				rssItem.setItem_wf_expiry(ndf.timeDiff());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
		if ("item".equals(qName)) {
			rssFeed.addItem(rssItem);
			return;
		}
	}
}
