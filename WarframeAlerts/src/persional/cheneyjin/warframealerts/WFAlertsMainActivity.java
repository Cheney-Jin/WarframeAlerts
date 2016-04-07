package persional.cheneyjin.warframealerts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import persional.cheneyjin.warframealerts.handler.RssFeed;
import persional.cheneyjin.warframealerts.http.RssFeedSAXParser;
import persional.cheneyjin.warframealerts.inform.AlertsCheckService;
import persional.cheneyjin.warframealerts.inform.PollingUtils;
import persional.cheneyjin.warframealerts.list.PullToRefreshListView;
import persional.cheneyjin.warframealerts.list.PullToRefreshListView.OnRefreshListener;
import persional.cheneyjin.warframealerts.list.PullToRefreshListViewAdapter;
import persional.cheneyjin.warframealerts.list.PullToRefreshListViewAdapter.ViewHolder;
import persional.cheneyjin.warframealerts.utils.Constants;
import persional.cheneyjin.warframealerts.utils.EventsUtils;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

/**
 * @author CheneyJin E-mail:cheneyjin@outlook.com
 */
public class WFAlertsMainActivity extends Activity {

	private LoadRssAsyncTask lRssAsyncTask;
	private EventsUtils eventsUtils;
	private PullToRefreshListView eventsListView;
	private PullToRefreshListViewAdapter ptrAdapter;
	public static ArrayList<HashMap<String, Object>> rssItemsList;
	private RssFeedSAXParser rssFAXPer;
	private Intent serviceIntent;
	private boolean isOptionChanged = false;
	private boolean ptrDisable = false;
	private boolean acService = false;

	private void init() {
		eventsUtils = new EventsUtils(this);
		Constants.RSS_PLATFORM = eventsUtils.getPlatformType();
		setTitle("  WarframeAlerts - " + Constants.RSS_PLATFORM);
		rssFAXPer = new RssFeedSAXParser();
		lRssAsyncTask = new LoadRssAsyncTask();
		lRssAsyncTask.execute(1100);
		showListView();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wfalerts_main);
		init();
	}

	private void showListView() {
		ptrAdapter = new PullToRefreshListViewAdapter(this);
		eventsListView = (PullToRefreshListView) this.findViewById(R.id.pull_to_refresh_listview);
		eventsListView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				eventsListView.postDelayed(new Runnable() {
					@Override
					public void run() {
						if (ptrDisable != true) {
							lRssAsyncTask = new LoadRssAsyncTask();
							lRssAsyncTask.execute(1150);
						}
					}
				}, 1200);
			}
		});
		eventsListView.setAdapter(ptrAdapter);
		// Request the adapter to load the data
		// click listener
		eventsListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				ViewHolder viewHolder = (ViewHolder) arg1.getTag();
				if (viewHolder.eventReward != null) {
					// Item on click content.
				}
			}
		});
		// Register the context menu for actions
		registerForContextMenu(eventsListView);
	}

	class LoadRssAsyncTask extends AsyncTask<Object, Object, Object> {

		private RssFeed rssFeed;

		@Override
		protected Object doInBackground(Object... params) {
			ptrDisable = true;
			if (isOptionChanged != false) {
				invalidateOptionsMenu();
				Constants.RSS_PLATFORM = eventsUtils.getPlatformType();
			}
			try {
				rssFeed = rssFAXPer.getFeed(Constants.getRssUrl(Constants.RSS_PLATFORM));
				rssItemsList = rssFeed.getAll();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			publishProgress(0);
			return params;
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			if (rssFeed == null) {
				setTitle("  Network error / Rss error !");
			} else if (rssFeed != null && isOptionChanged != false) {
				setTitle("  WarframeAlerts - " + Constants.RSS_PLATFORM);
				isOptionChanged = false;
			}
		}

		@Override
		protected void onProgressUpdate(Object... values) {
			super.onProgressUpdate(values);
			ptrAdapter.loadData(rssItemsList);
			eventsListView.onRefreshComplete();
			ptrDisable = false;
			// START CHECK_RSS_UPDATE SERVICE
			StartACService();
		}
	}

	void StartACService() {
		if (acService == false) {
			acService = true;
			//serviceIntent = new Intent(this, AlertsCheckService.class);
			//serviceIntent.setAction("persional.cheneyjin.warframealerts.inform.AlertsCheckService");
			PollingUtils.startPollingService(this, 5, AlertsCheckService.class, AlertsCheckService.ACTION);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wfalerts_main, menu);
		int menuItemBySelected = 0;
		if (Constants.RSS_PLATFORM.equals(Constants.WARFRAME_ALERTS_PLATFORM_PS4)) menuItemBySelected = R.id.ps4_alerts_select;
		else if (Constants.RSS_PLATFORM.equals(Constants.WARFRAME_ALERTS_PLATFORM_PC)) menuItemBySelected = R.id.pc_alerts_select;
		else if (Constants.RSS_PLATFORM.equals(Constants.WARFRAME_ALERTS_PLATFORM_XBOX)) menuItemBySelected = R.id.xbox_alerts_select;
		SpannableString beUsedItem = new SpannableString(Constants.RSS_PLATFORM + " Alerts");
		beUsedItem.setSpan(new ForegroundColorSpan(Color.RED), 0, beUsedItem.length(), 0);
		menu.findItem(menuItemBySelected).setTitle(beUsedItem);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.contact_me) Toast.makeText(WFAlertsMainActivity.this, Constants.CONTACT_ME, Toast.LENGTH_LONG).show();
		else {
			if (id == R.id.ps4_alerts_select) eventsUtils.savePlatformType(Constants.WARFRAME_ALERTS_PLATFORM_PS4);
			if (id == R.id.pc_alerts_select) eventsUtils.savePlatformType(Constants.WARFRAME_ALERTS_PLATFORM_PC);
			if (id == R.id.xbox_alerts_select) eventsUtils.savePlatformType(Constants.WARFRAME_ALERTS_PLATFORM_XBOX);
			afterSavePlatformType();
		}
		return super.onOptionsItemSelected(item);
	}

	private void afterSavePlatformType() {
		isOptionChanged = true;
		// ACService should define ServiceStop function in AlertsCheckService.
		// Then the follow boolean elem will be instead of ServiceStop()
		acService = false;

		Toast.makeText(WFAlertsMainActivity.this, Constants.PLEASE_REFRESH, Toast.LENGTH_LONG).show();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addCategory(Intent.CATEGORY_HOME);
			startActivity(intent);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// SERVICE WARNING!
		if (serviceIntent != null)
			stopService(serviceIntent);
	}
}
