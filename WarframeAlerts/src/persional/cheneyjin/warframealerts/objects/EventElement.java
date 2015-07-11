/**
 * 
 */
package persional.cheneyjin.warframealerts.objects;

/**
 * @author CheneyJin 
 *	E-mail:cheneyjin@outlook.com
 */
public class EventElement {
	private String eventPlace;
	//private String eventTask;
	private String eventDescription;
	private String eventReward;
	private String eventRemainingTime;
	
	public static final String EVENT_PLACE 			= "EVENT_PLACE";
	//public static final String EVENT_TASK 			= "EVENT_TASK";
	public static final String EVENT_DESCRIPTION 	= "EVENT_DESCRIPTION";
	public static final String EVENT_REWARD 		= "EVENT_REWARD";
	public static final String EVENT_REMAINING_TIME = "EVENT_REMAINING_TIME";
	
	public String getEventPlace() {
		return eventPlace;
	}
	public void setEventPlace(String eventPlace) {
		this.eventPlace = eventPlace;
	}
	/*
	public String getEventTask() {
		return eventTask;
	}
	public void setEventTask(String eventTask) {
		this.eventTask = eventTask;
	}
	*/
	public String getEventDescription() {
		return eventDescription;
	}
	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}
	public String getEventReward() {
		return eventReward;
	}
	public void setEventReward(String eventReward) {
		this.eventReward = eventReward;
	}
	public String getEventRemainingTime() {
		return eventRemainingTime;
	}
	public void setEventRemainingTime(String eventRemainingTime) {
		this.eventRemainingTime = eventRemainingTime;
	}
}
