package persional.cheneyjin.warframealerts.handler;

import java.util.ArrayList;
import java.util.HashMap;

import persional.cheneyjin.warframealerts.objs.EventElement;
import persional.cheneyjin.warframealerts.objs.RssItem;
import persional.cheneyjin.warframealerts.utils.SplitRssItemValue;
/**
 * @author CheneyJin 
 *	E-mail:cheneyjin@outlook.com
 */
public class RssFeed {
	private ArrayList<HashMap<String, Object>> rssItemsList;

	public RssFeed(){
		rssItemsList = new ArrayList<HashMap<String, Object>>();
	}
	
	public void addItem(RssItem rssItem){
		SplitRssItemValue splitRssItemValue = new SplitRssItemValue();
		splitRssItemValue.splitItemTitleInfo(
				isAlertsItem(rssItem.getItem_author()), rssItem.getItem_title()
		);
		HashMap<String, Object> itemMap = new HashMap<String, Object>();
		itemMap.put(RssItem.AUTHOR, rssItem.getItem_author());
		itemMap.put(EventElement.EVENT_PLACE, splitRssItemValue.getEventPlace());
		itemMap.put(EventElement.EVENT_DESCRIPTION, rssItem.getItem_description());
		itemMap.put(EventElement.EVENT_REWARD, splitRssItemValue.getEventRewardGoods());
		itemMap.put(EventElement.EVENT_REMAINING_TIME, rssItem.getItem_wf_expiry());
		itemMap.put(EventElement.EVENT_EXPIRY, rssItem.getFormat_expiry());

		rssItemsList.add(itemMap);
	}
	
	public HashMap<String, Object> getItem(int location){
		return rssItemsList.get(location);
	}
	
	public ArrayList<HashMap<String, Object>> getAll(){
		return rssItemsList;
	}
	
	private boolean isAlertsItem(String author){
		if("Alert".equals(author)) return true;
		else return false;
	}
}
