package persional.cheneyjin.warframealerts.inform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import persional.cheneyjin.warframealerts.R;
import persional.cheneyjin.warframealerts.handler.RssFeed;
import persional.cheneyjin.warframealerts.http.RssFeedSAXParser;
import persional.cheneyjin.warframealerts.objs.EventElement;
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
	
	private ArrayList<HashMap<String, Object>> oldRssList;
	
	private int messageNotificationID = 1000;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		ServiceisRunning = true;
		Log.i("SERVICE STARTING!", "SERVICE STARTING!");
		Log.i("RSS TYPE", Constants.RSS_PLATFORM);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		oldRssList = clearOutBreak((ArrayList<HashMap<String, Object>>) intent.getSerializableExtra("rssItemsList"));
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
		return super.onStartCommand(intent, flags, startId);
	}

	class MessageThread extends Thread {
		public boolean isRunning = true;

		public void run() {
			while(true){
				try {
					Thread.sleep(10000);
					ACServiceAsnycTask ascTask = new ACServiceAsnycTask();
					ascTask.execute(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	class ACServiceAsnycTask extends AsyncTask<Object, Object, Object>{
		
		private RssFeed	rssFeed;
		private ArrayList<HashMap<String, Object>> newRssList;
		private StringBuilder updateMessage = null;
		private int[] alertsCount = null;
		
		@Override
		protected void onPreExecute() {
			Log.i("START GET MESSAGE!", "START GET MESSAGE!");
			Log.i("RSS Platform: ", Constants.RSS_PLATFORM +"//"+Constants.getRssUrl(Constants.RSS_PLATFORM));
			super.onPreExecute();
			updateMessage = new StringBuilder();
			alertsCount = new int[2];
		}

		@Override
		protected Object doInBackground(Object... arg0) {
			try {
				rssFeed = new RssFeedSAXParser().getFeed(Constants.getRssUrl(Constants.RSS_PLATFORM));
				newRssList = clearOutBreak(rssFeed.getAll());
				alertsCount = checkUpdate(newRssList);
				oldRssList.clear();
				oldRssList = newRssList;
				if (alertsCount[0] == 0 && alertsCount[1] == 0) return null;
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (alertsCount[0] != 0) updateMessage.append("Update: ").append(String.valueOf(alertsCount[0])).append(" Alert(s) ");
			if (alertsCount[1] != 0) updateMessage.append(String.valueOf(alertsCount[1]).toString()).append(" Invasion(s) ");
			Log.i("MESSAGE IS:", updateMessage.toString());
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
		Iterator<HashMap<String,Object>> newIte = rssList.iterator();
		Iterator<HashMap<String,Object>> oldIte = oldRssList.iterator();
		while(newIte.hasNext()){
			HashMap<String,Object> newItem = newIte.next();
			String newItemAuthor = newItem.get(RssItem.AUTHOR).toString();
			String newItemReward = newItem.get(EventElement.EVENT_REWARD).toString();
			while(oldIte.hasNext()){
				HashMap<String,Object> oldItem = oldIte.next();
				String oldItemAuthor = oldItem.get(RssItem.AUTHOR).toString();
				String oldItemReward = oldItem.get(EventElement.EVENT_REWARD).toString();
				if (newItemAuthor.equals(oldItemAuthor)&& newItemReward.equals(oldItemReward)){
					oldIte.remove();newIte.remove();
				}
				oldIte = oldRssList.iterator();
				newIte = rssList.iterator();
			}
		}
		Iterator<HashMap<String,Object>> surplusIte = rssList.iterator();
		while(surplusIte.hasNext()){
			HashMap<String,Object> surItem = surplusIte.next();
			String surAuthor = surItem.get(RssItem.AUTHOR).toString();
			if(surAuthor.equals(Constants.RSS_ALERT)){
				updateFlags[0]++;
			}else{
				 updateFlags[1]++;
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