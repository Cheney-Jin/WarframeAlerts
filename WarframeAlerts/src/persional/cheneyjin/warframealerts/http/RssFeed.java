package persional.cheneyjin.warframealerts.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import persional.cheneyjin.warframealerts.objects.EventElement;
import persional.cheneyjin.warframealerts.objects.RssItem;
import persional.cheneyjin.warframealerts.utils.SplitRssItemValue;
/**
 * @author CheneyJin 
 *	E-mail:cheneyjin@outlook.com
 */
public class RssFeed {
	private List<RssItem> 	rssItemsList;

	public RssFeed(){
		rssItemsList = new ArrayList<RssItem>();
	}
	
	public void addItem(RssItem rssItem){
		rssItemsList.add(rssItem);
	}
	
	public RssItem getItem(int location){
		return rssItemsList.get(location);
	}
	
	private boolean isAlertsItem(String author){
		if("Alert".equals(author)){
			return true;
		}else{
			return false;
		}	
	}
	public List<HashMap<String,Object>> getAllItems(){
		List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < rssItemsList.size() ; i++) {
			HashMap<String, Object> item = new HashMap<String, Object>();
			SplitRssItemValue splitRssItemValue = new SplitRssItemValue();
			splitRssItemValue.splitItemTitleInfo(
					isAlertsItem(rssItemsList.get(i).getItem_author()),
					rssItemsList.get(i).getItem_title()
			);
			item.put(RssItem.AUTHOR, rssItemsList.get(i).getItem_author());
			item.put(EventElement.EVENT_PLACE, splitRssItemValue.getEventPlace());
			item.put(EventElement.EVENT_DESCRIPTION, rssItemsList.get(i).getItem_description());
			item.put(EventElement.EVENT_REWARD, splitRssItemValue.getEventRewardGoods());
			item.put(EventElement.EVENT_REMAINING_TIME, splitRssItemValue.getEventRemaningTime());

			//item.put(RssItem.PUBDATE, rssItemsList.get(i).getItem_pubDate());
			//item.put(RssItem.WF_FACTION, rssItemsList.get(i).getItem_wf_faction());
			//item.put(RssItem.WF_EXPIRY, rssItemsList.get(i).getItem_wf_expiry());
			/*
			item.put(RssItem.GUID, rssItemsList.get(i).getItem_guid());
			item.put(RssItem.TITLE, rssItemsList.get(i).getItem_title());
			item.put(RssItem.AUTHOR, rssItemsList.get(i).getItem_author());
			item.put(RssItem.DESCRIPTION, rssItemsList.get(i).getItem_description());
			item.put(RssItem.PUBDATE, rssItemsList.get(i).getItem_pubDate());
			item.put(RssItem.WF_FACTION, rssItemsList.get(i).getItem_wf_faction());
			item.put(RssItem.WF_EXPIRY, rssItemsList.get(i).getItem_wf_expiry());
			*/
			data.add(item);
		}
		return data;
	}
	
	public List<RssItem> getRssItemsList() {
		return rssItemsList;
	}

	public void setRssItemsList(List<RssItem> rssItemsList) {
		this.rssItemsList = rssItemsList;
	}
	
}
