package persional.cheneyjin.warframealerts.inform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import persional.cheneyjin.warframealerts.R;
import persional.cheneyjin.warframealerts.handler.RssFeed;
import persional.cheneyjin.warframealerts.http.RssFeedSAXParser;
import persional.cheneyjin.warframealerts.objs.RssItem;
import persional.cheneyjin.warframealerts.utils.Constants;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

public class AlertsCheckService extends Service {

	public static boolean ServiceisRunning 		= false;
	
	private MessageThread messageThread 		= null;
	private PendingIntent messagePendingIntent 	= null;
	private Notification messageNotification 	= null;
	
	private NotificationManager messageNotificatioManager = null;
	
	private ArrayList<HashMap<String, Object>> oldRssList = new ArrayList<HashMap<String, Object>>();
	
	private int messageNotificationID = 1000;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		ServiceisRunning = true;
		//Log.i("SERVICE STARTING!", "SERVICE STARTING!");
		//Log.i("RSS TYPE", Constants.RSS_PLATFORM);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		ArrayList<HashMap<String, Object>> cob = clearOutBreak((ArrayList<HashMap<String, Object>>) intent.getSerializableExtra("rssItemsList"));
		if(cob != null){
			oldRssList.addAll(cob);
		}
		//Log.i("ONSTARTCOMMAND", "ONSTARTCOMMAND");
		messageNotification = new Notification();
		messageNotification.icon = R.drawable.ic_launcher;
		messageNotification.tickerText = "Warframe Inform";
		messageNotification.defaults = Notification.DEFAULT_SOUND;
		//messageNotification.sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.alert); ;
		messageNotification.when = System.currentTimeMillis();
		messageNotificatioManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		messagePendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
		messageThread = new MessageThread();
		messageThread.isRunning = true;
		messageThread.start();
		startForeground(0, messageNotification);
		return super.onStartCommand(intent, START_REDELIVER_INTENT, startId);
	}

	class MessageThread extends Thread {
		public boolean isRunning = true;

		public void run() {
			while(true){
				try {
					Thread.sleep(350000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				ACServiceAsnycTask ascTask = new ACServiceAsnycTask();
				ascTask.execute(5000);
			}
		}
	}
	
	class ACServiceAsnycTask extends AsyncTask<Object, Object, Object>{
		
		private RssFeed	rssFeed;
		private StringBuilder updateMessage = null;
		
		@Override
		protected void onPreExecute() {
			Log.i("START GET MESSAGE!", "START GET MESSAGE!");
			Log.i("RSS Platform: ", Constants.RSS_PLATFORM +"//"+Constants.getRssUrl(Constants.RSS_PLATFORM));
			super.onPreExecute();
			updateMessage = new StringBuilder();
		}

		@Override
		protected Object doInBackground(Object... arg0) {
			try {
				int[] alertsCount = new int[2];
				rssFeed = new RssFeedSAXParser().getFeed(Constants.getRssUrl(Constants.RSS_PLATFORM));
				ArrayList<HashMap<String, Object>> newRssList = clearOutBreak(rssFeed.getAll());
				alertsCount = checkUpdate(newRssList);
				oldRssList.clear();
				oldRssList.addAll(newRssList);
				if(alertsCount == null) return null;
				if(alertsCount[0] == 0 && alertsCount[1] == 0) return null;
				if (alertsCount[0] != 0) updateMessage.append("Update: ").append(String.valueOf(alertsCount[0])).append(" Alert(s) ");
				if (alertsCount[1] != 0) updateMessage.append(String.valueOf(alertsCount[1]).toString()).append(" Invasion(s) ");
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			publishProgress(0);
			return arg0;
		}
		
		@SuppressWarnings("deprecation")
		@Override
		protected void onProgressUpdate(Object... values) {
			super.onProgressUpdate(values);
			if (updateMessage != null && !"".equals(updateMessage)) {
				Log.i("TWITT MESSAGE!", "TWITT");
				messageNotification.setLatestEventInfo(getApplicationContext(), 
						"New Warframe inform!", updateMessage, messagePendingIntent);
				messageNotificatioManager.notify(messageNotificationID, messageNotification);
				messageNotificationID++;
			}
		}
	}
	
	private ArrayList<HashMap<String, Object>> clearOutBreak(ArrayList<HashMap<String, Object>> olist) {
		if (olist == null || olist.isEmpty()) return null;
		Iterator<HashMap<String,Object>> ite = olist.iterator();
		while(ite.hasNext()){
			HashMap<String,Object> item=ite.next();
			String author = item.get(RssItem.AUTHOR).toString();
			if (author.equals(Constants.RSS_OUTBREAK)) ite.remove();
		}
		return olist;
	}
	
	private int[] checkUpdate(ArrayList<HashMap<String, Object>> rssList) {
		int[] updateFlags = new int[2];
		if (oldRssList == null || oldRssList.isEmpty()) return null;
		HashSet<HashMap<String,Object>> hashSet = new HashSet<HashMap<String, Object>>();
		hashSet.addAll(oldRssList);
		for(int i = 0; i< rssList.size();i++){
			HashMap<String,Object> item = rssList.get(i);
			boolean isSame = hashSet.add(item);
			if(isSame != false){
				String author = item.get(RssItem.AUTHOR).toString();
				if(author.equals(Constants.RSS_ALERT)){
					updateFlags[0]++;
				}else{
					updateFlags[1]++;
				}
			}
		}
		return updateFlags;
	}
	
	@Override
	public void onDestroy() {
		messageThread.isRunning = false;
		stopForeground(true);
		super.onDestroy();
	}
}

/*
Iterator<HashMap<String, Object>> newIte = rssList.iterator();
while(newIte.hasNext()){
	HashMap<String, Object> newItem = newIte.next();
	Iterator<HashMap<String, Object>> oldIte = oldRssList.iterator();
	while(oldIte.hasNext()){
		HashMap<String, Object> oldItem = oldIte.next();
		if(newItem.get(RssItem.AUTHOR).equals(oldItem.get(RssItem.AUTHOR)) 
				&& newItem.get(EventElement.EVENT_REWARD).equals(oldItem.get(EventElement.EVENT_REWARD))){
			newIte.remove();
		}
	}
}
Log.i("rssList", String.valueOf(rssList.size()));
for(int i = 0; i < rssList.size(); i++){
	if(rssList.get(i).get(RssItem.AUTHOR).equals(Constants.RSS_ALERT)){
		updateFlags[0]++;
	}else{
		updateFlags[1]++;
	}
}*/