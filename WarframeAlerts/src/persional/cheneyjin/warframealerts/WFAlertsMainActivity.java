package persional.cheneyjin.warframealerts;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import persional.cheneyjin.warframealerts.list.PullToRefreshListView;
import persional.cheneyjin.warframealerts.list.PullToRefreshListView.OnRefreshListener;
import persional.cheneyjin.warframealerts.list.PullToRefreshListViewAdapter;
import persional.cheneyjin.warframealerts.list.PullToRefreshListViewAdapter.ViewHolder;
import persional.cheneyjin.warframealerts.http.RssFeed;
import persional.cheneyjin.warframealerts.http.RssFeedSAXParser;
import persional.cheneyjin.warframealerts.utils.Constants;
import persional.cheneyjin.warframealerts.utils.EventsUtils;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
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
 * @author CheneyJin 
 *	E-mail:cheneyjin@outlook.com
 */
public class WFAlertsMainActivity extends ActionBarActivity {
	
	private String 									rssPlatform;
	private EventsUtils 							eventsUtils;
	private RssFeed 								rssFeed;
	private PullToRefreshListView 					eventsListView;
	private PullToRefreshListViewAdapter 			ptrAdapter;
	private List<HashMap<String,Object>> 			rssItemsList;
	private boolean                                 isOptionChanged = false;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventsUtils = new EventsUtils(this);
        setContentView(R.layout.activity_wfalerts_main);
        if(android.os.Build.VERSION.SDK_INT > 9){
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);	
        }
		loadingRss();
		showListView();
    }
    	  
    public void loadingRss(){
        try {
        	rssPlatform = eventsUtils.getPlatformType();
			rssFeed = new RssFeedSAXParser().getFeed(Constants.getRssUrl(rssPlatform));
			rssItemsList = rssFeed.getAllItems();
        } catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        if (rssFeed == null) {
			setTitle("Network error / Rss error !");
		}else{
			setTitle("WarframeAlerts - "  + rssPlatform);
		}
    }

	private void showListView() {    	
    	ptrAdapter = new PullToRefreshListViewAdapter(this);
    	eventsListView = (PullToRefreshListView) this.findViewById(R.id.pull_to_refresh_listview);
    	eventsListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
                rssItemsList = null;
                loadingRss();
				eventsListView.postDelayed(new Runnable() {			
					@Override
					public void run() {
						ptrAdapter.loadData(rssItemsList);
						eventsListView.onRefreshComplete();
					}
				}, 1000);
				if(isOptionChanged != false){
					supportInvalidateOptionsMenu();	
				}
				isOptionChanged = false;
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
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.wfalerts_main, menu);
        int menuItemBySelected = 0;
        if(rssPlatform.equals(Constants.WARFRAME_ALERTS_PLATFORM_PS4)){
        	menuItemBySelected = R.id.ps4_alerts_select;
        }else if(rssPlatform.equals(Constants.WARFRAME_ALERTS_PLATFORM_PC)){
        	menuItemBySelected = R.id.pc_alerts_select;
        }else if(rssPlatform.equals(Constants.WARFRAME_ALERTS_PLATFORM_XBOX)){
        	menuItemBySelected = R.id.xbox_alerts_select;
        } 
        SpannableString beUsedItem = new SpannableString(rssPlatform + " Alerts");
        beUsedItem.setSpan(new ForegroundColorSpan(Color.RED), 0, beUsedItem.length(), 0);
        menu.findItem(menuItemBySelected).setTitle(beUsedItem);
        return true;
    }

    @Override
	public void supportInvalidateOptionsMenu() {
		// TODO Auto-generated method stub
		super.supportInvalidateOptionsMenu();
	}

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.contact_me) {
			Toast.makeText(WFAlertsMainActivity.this, Constants.CONTACT_ME, Toast.LENGTH_LONG).show();
            return true;
        }
        if (id == R.id.ps4_alerts_select) {
        	eventsUtils.savePlatformType(Constants.WARFRAME_ALERTS_PLATFORM_PS4);
        	isOptionChanged = true;
        	return true;        
        }
        if (id == R.id.pc_alerts_select) {
        	eventsUtils.savePlatformType(Constants.WARFRAME_ALERTS_PLATFORM_PC);
        	isOptionChanged = true;
        	return true;         
        }
        if (id == R.id.xbox_alerts_select) {
        	eventsUtils.savePlatformType(Constants.WARFRAME_ALERTS_PLATFORM_XBOX);
        	isOptionChanged = true;
        	return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
            // menuUtils.createTwoDispatcher(event);
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.MAIN");
                    intent.addCategory("android.intent.category.HOME");
                    startActivity(intent);
            }
            return false;
    }
    
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	@Override
	protected void onPostResume() {
		// TODO Auto-generated method stub
		super.onPostResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
}
