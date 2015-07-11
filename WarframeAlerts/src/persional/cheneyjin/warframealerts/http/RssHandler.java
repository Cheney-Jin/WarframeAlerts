package persional.cheneyjin.warframealerts.http;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import persional.cheneyjin.warframealerts.objects.RssItem;

/**
 * @author CheneyJin 
 *	E-mail:cheneyjin@outlook.com
 */
public class RssHandler extends DefaultHandler {

	private RssFeed rssFeed;
	private RssItem rssItem;

	int currentstate = 0;

	// final int ITEM_GUID = 1;
	final int ITEM_TITLE = 1;
	final int ITEM_AUTHOR = 2;
	final int ITEM_DESCRIPTION = 3;
	final int ITEM_PUBDATE = 4;

	// final int ITEM_WFFACTION = 6;
	// final int ITEM_WFEXPIRY = 7;

	RssHandler() {

	}

	public RssFeed getRssFeed() {
		return rssFeed;
	}

	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.startDocument();
		rssFeed = new RssFeed();
		rssItem = new RssItem();
	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.endDocument();
	}

	private StringBuilder textBuffer = new StringBuilder();

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		super.characters(ch, start, length);

		textBuffer.append(ch, start, length);
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		super.startElement(uri, localName, qName, attributes);
		textBuffer.setLength(0);

		if ("channel".equals(qName)) {
			currentstate = 0;
			return;
		}
		if ("item".equals(qName)) {
			rssItem = new RssItem();
			return;
		}
		/*
		 * if("guid".equals(qName)){ currentstate = ITEM_GUID; return; }
		 */
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
		/*
		 * if("pubDate".equals(qName)){ currentstate = ITEM_PUBDATE; return; }
		 * if("wf:faction".equals(qName)){ currentstate = ITEM_WFFACTION;
		 * return; } if("wf:expiry".equals(qName)){ currentstate =
		 * ITEM_WFEXPIRY; return; }
		 */
		currentstate = 0;
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		super.endElement(uri, localName, qName);
		String value = textBuffer.toString();

		switch (currentstate) {
		/*
		 * case ITEM_GUID: rssItem.setItem_guid(text); currentstate = 0; break;
		 */
		case ITEM_TITLE:
			rssItem.setItem_title(value);
			// Log.i("title", text);
			currentstate = 0;
			break;
		case ITEM_AUTHOR:
			rssItem.setItem_author(value);
			currentstate = 0;
			break;
		case ITEM_DESCRIPTION:
			rssItem.setItem_description(value);
			currentstate = 0;
			break;
		/*
		 * case ITEM_PUBDATE: rssItem.setItem_pubDate(text); currentstate = 0;
		 * break; case ITEM_WFFACTION: rssItem.setItem_wf_faction(text);
		 * //Log.w("ITEM_WFFACTION", text); currentstate = 0; break; case
		 * ITEM_WFEXPIRY: rssItem.setItem_wf_expiry(text); currentstate = 0;
		 * break;
		 */
		default:
			break;
		}

		if ("item".equals(qName)) {
			rssFeed.addItem(rssItem);
			return;
		}
	}

}
