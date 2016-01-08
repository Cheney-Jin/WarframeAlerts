/**
 * 
 */
package persional.cheneyjin.warframealerts.utils;

/**
 * @author CheneyJin E-mail:cheneyjin@outlook.com
 */
public class SplitRssItemValue {

	private String eventPlace = "";
	private String eventRewardGoods = "";
	private String eventRemaningTime = "";

	public void splitItemTitleInfo(boolean isAlert, String itemTitle) {
		String[] splitValue = itemTitle.split(" - ");
		int splitValueLength = splitValue.length;
		StringBuffer rewardGoodsChild = new StringBuffer();
		int splitpoint = 0;
		if (isAlert == true) {
			//setEventRemaningTime(splitValue[splitValueLength - 1]);
			setEventPlace(splitValue[splitValueLength - 2]);
			splitpoint += 2;
		} else {
			setEventPlace(splitValue[splitValueLength - 1]);
			splitpoint += 1;
		}
		for (int i = 0; i < splitValueLength - splitpoint; i++) {
			rewardGoodsChild.append(splitValue[i]);
		}
		setEventRewardGoods(rewardGoodsChild.toString());
	}

	public String getEventPlace() {
		return eventPlace;
	}

	public void setEventPlace(String eventPlace) {
		this.eventPlace = eventPlace;
	}

	public String getEventRewardGoods() {
		return eventRewardGoods;
	}

	public void setEventRewardGoods(String eventRewardGoods) {
		this.eventRewardGoods = eventRewardGoods;
	}

	public String getEventRemaningTime() {
		return eventRemaningTime;
	}

	public void setEventRemaningTime(String eventRemaningTime) {
		this.eventRemaningTime = eventRemaningTime;
	}

}
