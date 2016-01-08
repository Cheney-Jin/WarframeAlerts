package persional.cheneyjin.warframealerts.objs;
/**
 * @author CheneyJin 
 *	E-mail:cheneyjin@outlook.com
 */
public class RssItem {
	
	private String item_guid = "";
	private String item_title = "";
	private String item_author = "";
	private String item_description = "";
	private String item_pubDate = "";
	private String item_wf_faction = "";
	private String item_wf_expiry = "";
	private String format_expiry = "";

	public static final String GUID 	    = "guid";
	public static final String TITLE        = "title";
	public static final String AUTHOR       = "author";
	public static final String DESCRIPTION  = "description";
	public static final String PUBDATE      = "pubDate";
	public static final String WF_FACTION   = "wf_faction";
	public static final String WF_EXPIRY    = "wf_expiry";
	public static final String FORMAT_EXPIRY= "format_expiry";

	public RssItem(){
		
	}
	public String getItem_guid() {
		return item_guid;
	}
	public void setItem_guid(String item_guid) {
		this.item_guid = item_guid;
	}
	public String getItem_title() {
		return item_title;
	}
	public void setItem_title(String item_title) {
		this.item_title = item_title;
	}
	public String getItem_author() {
		return item_author;
	}
	public void setItem_author(String item_author) {
		this.item_author = item_author;
	}
	public String getItem_description() {
		return item_description;
	}
	public void setItem_description(String item_description) {
		this.item_description = item_description;
	}
	public String getItem_pubDate() {
		return item_pubDate;
	}
	public void setItem_pubDate(String item_pubDate) {
		this.item_pubDate = item_pubDate;
	}
	public String getItem_wf_faction() {
		return item_wf_faction;
	}
	public void setItem_wf_faction(String item_wf_faction) {
		this.item_wf_faction = item_wf_faction;
	}
	public String getItem_wf_expiry() {
		return item_wf_expiry;
	}
	public void setItem_wf_expiry(String item_wf_expiry) {
		this.item_wf_expiry = item_wf_expiry;
	}
	public String getFormat_expiry() {
		return format_expiry;
	}
	public void setFormat_expiry(String format_expiry) {
		this.format_expiry = format_expiry;
	}	
}
