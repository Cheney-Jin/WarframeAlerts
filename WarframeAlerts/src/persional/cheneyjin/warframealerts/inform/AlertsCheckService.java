package persional.cheneyjin.warframealerts.inform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import persional.cheneyjin.warframealerts.R;
import persional.cheneyjin.warframealerts.WFAlertsMainActivity;
import persional.cheneyjin.warframealerts.handler.RssFeed;
import persional.cheneyjin.warframealerts.http.RssFeedSAXParser;
import persional.cheneyjin.warframealerts.objs.EventElement;
import persional.cheneyjin.warframealerts.objs.RssItem;
import persional.cheneyjin.warframealerts.utils.Constants;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AlertsCheckService extends Service {
	public static final String ACTION = "persional.cheneyjin.warframealerts.inform.AlertsCheckService";

	private Notification mNotification;
	private NotificationManager mManager;

	private String MessageInfo;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		initNotifiManager();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		new PollingThread().start();
	}

	// 初始化通知栏配置
	private void initNotifiManager() {
		mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		mNotification = new Notification();
		mNotification.icon = R.drawable.ic_launcher;
		mNotification.tickerText = "New alerts/invasions informations!";
		mNotification.defaults |= Notification.DEFAULT_SOUND;
		mNotification.flags = Notification.FLAG_AUTO_CANCEL;
	}

	// Pop Notification
	@SuppressWarnings("deprecation")
	private void showNotification() {
		mNotification.when = System.currentTimeMillis();
		// Navigator to the new activity when click the notification title
		Intent i = new Intent(this, WFAlertsMainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, Intent.FLAG_ACTIVITY_NEW_TASK);
		mNotification.setLatestEventInfo(this, getResources().getString(R.string.app_name), MessageInfo, pendingIntent);
		mManager.notify(0, mNotification);
	}

	class PollingThread extends Thread {
		@Override
		public void run() {
			RssFeedSAXParser rssFAXPer = new RssFeedSAXParser();
			ArrayList<HashMap<String, Object>> newAlertsList = new ArrayList<HashMap<String, Object>>();
			try {
				RssFeed rssFeed = rssFAXPer.getFeed(Constants.getRssUrl(Constants.RSS_PLATFORM));
				newAlertsList.addAll(removeOutbreak(rssFeed.getAll()));
				equalsAlerts(newAlertsList);
				if (getMessageInfo() != null)
					showNotification();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	private ArrayList<HashMap<String, Object>> removeOutbreak(ArrayList<HashMap<String, Object>> eList) {
		Iterator<HashMap<String, Object>> eListIterator = eList.iterator();
		while (eListIterator.hasNext()) {
			HashMap<String, Object> e = eListIterator.next();
			if (e.get(RssItem.AUTHOR).toString().equals(Constants.RSS_OUTBREAK))
				eListIterator.remove();
		}
		return eList;
	}

	private void equalsAlerts(ArrayList<HashMap<String, Object>> newList) {
		int alertsNum = 0, invasions = 0;
		ArrayList<HashMap<String, Object>> oldList = new ArrayList<HashMap<String, Object>>();
		oldList.addAll(removeOutbreak(WFAlertsMainActivity.rssItemsList));
		boolean same = false;
		for (int j = 0; j < oldList.size(); j++) {
			HashMap<String, Object> newItem = newList.get(j);
			if (newItem.get(RssItem.AUTHOR).toString().equals(Constants.RSS_ALERT)) {
				for (int i = 0; i < oldList.size(); i++) {
					HashMap<String, Object> oldItem = oldList.get(i);
					if (oldItem.get(RssItem.AUTHOR).toString().equals(Constants.RSS_ALERT)) {
						if (newItem.get(EventElement.EVENT_REWARD).toString().equals(oldItem.get(EventElement.EVENT_REWARD))) {
							same = true;
							break;
						}
					}
				}

			}
			if (same != false){
				alertsNum++;
				same = false;				
			}
		}
		for (int j = 0; j < oldList.size(); j++) {
			HashMap<String, Object> newItem = newList.get(j);
			if (newItem.get(RssItem.AUTHOR).toString().equals(Constants.RSS_INVASION)) {
				for (int i = 0; i < oldList.size(); i++) {
					HashMap<String, Object> oldItem = oldList.get(i);
					if (oldItem.get(RssItem.AUTHOR).toString().equals(Constants.RSS_INVASION)) {
						if (newItem.get(EventElement.EVENT_REWARD).toString().equals(oldItem.get(EventElement.EVENT_REWARD))) {
							same = true;
							break;
						}
					}
				}

			}
			if (same != true){
				invasions++;
				same = false;				
			}
		}
		if (alertsNum == 0 && invasions == 0) {
			setMessageInfo(null);
		} else {
			setMessageInfo("News: " + alertsNum + "Alert(s)  " + invasions + "invasion(s)");
		}
	}

	public String getMessageInfo() {
		return MessageInfo;
	}

	public void setMessageInfo(String messageInfo) {
		MessageInfo = messageInfo;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		System.out.println("Service:onDestroy");
	}

}
