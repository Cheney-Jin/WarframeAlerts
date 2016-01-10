package persional.cheneyjin.warframealerts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import persional.cheneyjin.warframealerts.handler.RssFeed;
import persional.cheneyjin.warframealerts.http.RssFeedSAXParser;
import persional.cheneyjin.warframealerts.inform.AlertsCheckService;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
/**
 * @author CheneyJin 
 *	E-mail:cheneyjin@outlook.com
 */
public class WFAlertsMainActivity extends Activity {
	
	private LoadRssAsyncTask 						lRssAsyncTask;
	private EventsUtils 							eventsUtils;
	private PullToRefreshListView 					eventsListView;
	private PullToRefreshListViewAdapter 			ptrAdapter;
	private ArrayList<HashMap<String,Object>> 		rssItemsList;
	
	private boolean                                 isOptionChanged = false;
	private boolean 								ptrDisable 		= false;
	private int 									ACService 		= 0;
	
	private void init(){
        eventsUtils = new EventsUtils(this);
        Constants.RSS_PLATFORM = eventsUtils.getPlatformType();
		setTitle("  WarframeAlerts - "  + Constants.RSS_PLATFORM);
		lRssAsyncTask = new LoadRssAsyncTask();
		lRssAsyncTask.execute(1000);
		showListView();
	}
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wfalerts_main);
        /*ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);*/
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
						if(ptrDisable != true){
							lRssAsyncTask = new LoadRssAsyncTask();
							lRssAsyncTask.execute(1150);	
						}
					}
				}, 1200);
			}
		});
    	eventsListView.setAdapter(ptrAdapter);
		// Request the adapter to load the data
		ptrAdapter.loadData(rssItemsList);	
		// click listener
		eventsListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				ViewHolder viewHolder = (ViewHolder) arg1.getTag();
				if (viewHolder.eventReward != null){
					//Toast.makeText(WFAlertsMainActivity.this, viewHolder.title.getText(), Toast.LENGTH_SHORT).show();
				}					
			}
		});
		// Register the context menu for actions
		registerForContextMenu(eventsListView);
	}
		
	class LoadRssAsyncTask extends AsyncTask<Object, Object, Object>{

		private RssFeed	rssFeed;
		
		@Override
		protected Object doInBackground(Object... params) {
			ptrDisable = true;
			if(isOptionChanged != false){
				invalidateOptionsMenu();
				Constants.RSS_PLATFORM = eventsUtils.getPlatformType();
			}
			try {
				rssFeed = new RssFeedSAXParser().getFeed(Constants.getRssUrl(Constants.RSS_PLATFORM));
				//if(rssItemsList != null) rssItemsList.removeAll(rssItemsList);
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
			}else if(rssFeed!= null && isOptionChanged != false){
				setTitle("  WarframeAlerts - "  + Constants.RSS_PLATFORM);
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
	
	private void StartACService(){
		if(ACService == 0){
			ACService = 1;
	        Intent intent = new Intent(this, AlertsCheckService.class);
	        intent.setAction("persional.cheneyjin.warframealerts.inform.AlertsCheckService");
			if(AlertsCheckService.ServiceisRunning != false){
		        stopService(intent);
		        //Log.i("SERVICE RESTART!", "SERVICE RESTART!");
			}
	        intent.putExtra("rssItemsList", rssItemsList);
	        startService(intent);
	    }
	}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.wfalerts_main, menu);
        int menuItemBySelected = 0;
        if(Constants.RSS_PLATFORM.equals(Constants.WARFRAME_ALERTS_PLATFORM_PS4)) menuItemBySelected = R.id.ps4_alerts_select;
        else if(Constants.RSS_PLATFORM.equals(Constants.WARFRAME_ALERTS_PLATFORM_PC)) menuItemBySelected = R.id.pc_alerts_select;
        else if(Constants.RSS_PLATFORM.equals(Constants.WARFRAME_ALERTS_PLATFORM_XBOX)) menuItemBySelected = R.id.xbox_alerts_select; 
        SpannableString beUsedItem = new SpannableString(Constants.RSS_PLATFORM + " Alerts");
        beUsedItem.setSpan(new ForegroundColorSpan(Color.RED), 0, beUsedItem.length(), 0);
        menu.findItem(menuItemBySelected).setTitle(beUsedItem);
        return true;
    }

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.contact_me) {
			Toast.makeText(WFAlertsMainActivity.this, Constants.CONTACT_ME, Toast.LENGTH_LONG).show();
            return true;
        }
        if (id == R.id.ps4_alerts_select) {
        	eventsUtils.savePlatformType(Constants.WARFRAME_ALERTS_PLATFORM_PS4);
        	afterSavePlatformType();
        }
        if (id == R.id.pc_alerts_select) {
        	eventsUtils.savePlatformType(Constants.WARFRAME_ALERTS_PLATFORM_PC);
        	afterSavePlatformType();
        }
        if (id == R.id.xbox_alerts_select) {
        	eventsUtils.savePlatformType(Constants.WARFRAME_ALERTS_PLATFORM_XBOX);
        	afterSavePlatformType();
        }
        return super.onOptionsItemSelected(item);
    }
	
	private boolean afterSavePlatformType(){
		isOptionChanged = true;
    	ACService = 0;
    	Toast.makeText(WFAlertsMainActivity.this, Constants.PLEASE_REFRESH, Toast.LENGTH_LONG).show();
    	return true;
	}
    
/*    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // menuUtils.createTwoDispatcher(event);
    	if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
    		Intent intent = new Intent();
            intent.setAction("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.HOME");
            startActivity(intent);
        }
        return false;
    }*/
    
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
}
