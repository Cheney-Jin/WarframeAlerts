package persional.cheneyjin.warframealerts.list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import persional.cheneyjin.warframealerts.R;
import persional.cheneyjin.warframealerts.WFAlertsMainActivity;
import persional.cheneyjin.warframealerts.objs.EventElement;
import persional.cheneyjin.warframealerts.objs.RssItem;
import persional.cheneyjin.warframealerts.utils.Constants;
import persional.cheneyjin.warframealerts.utils.ScreenSize;
import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author CheneyJin E-mail:cheneyjin@outlook.com
 */
public class PullToRefreshListViewAdapter extends android.widget.BaseAdapter {

	private WFAlertsMainActivity mContext;
	private ArrayList<HashMap<String, Object>> eventsList;
	private Map<String, Object> record;
	private LayoutInflater inflater;
	private ScreenSize screenSize;
	private float screenWidth;

	public PullToRefreshListViewAdapter(WFAlertsMainActivity context) {
		this.mContext = context;
		screenSize = new ScreenSize(mContext);
		screenWidth = screenSize.getWidth();
		eventsList = new ArrayList<HashMap<String, Object>>();
	}

	public class ViewHolder {
		// public ImageView eventImg;
		public TextView eventPlace;
		// public TextView eventTask;
		public TextView eventDescription;
		public TextView eventReward;
		public TextView eventRemainingTime;
	}

	public void loadData(ArrayList<HashMap<String, Object>> eList) {
		if (eList == null)
			return;
		eventsList = eList;
		// MANDATORY: Notify that the data has changed
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return eventsList.size();
	}

	@Override
	public Map<String, Object> getItem(int position) {
		return eventsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public int getEventsType(String itemAuthor) {
		if ("Alert".equals(itemAuthor))
			return Constants.VIEWTYPE_ALERT;
		else if ("Invasion".equals(itemAuthor))
			return Constants.VIEWTYPE_INVASION;
		else if ("Outbreak".equals(itemAuthor))
			return Constants.VIEWTYPE_OUTBREAK;
		else
			return Constants.VIEWTYPE_ERROR;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		record = getItem(position);
		inflater = mContext.getLayoutInflater();
		ViewHolder viewHolder = new ViewHolder();
		int viewType = getEventsType(record.get(RssItem.AUTHOR).toString());
		if (viewType == Constants.VIEWTYPE_ALERT) {
			convertView = inflater.inflate(R.layout.alert_item, null);
			convertView.findViewById(R.id.content).getLayoutParams().width = (int) (screenWidth * 0.75);

			viewHolder.eventPlace = (TextView) convertView.findViewById(R.id.event_place);
			viewHolder.eventDescription = (TextView) convertView.findViewById(R.id.event_description);
			viewHolder.eventReward = (TextView) convertView.findViewById(R.id.event_reward);
			viewHolder.eventRemainingTime = (TextView) convertView.findViewById(R.id.event_remaining_time);
			convertView.setTag(viewHolder);

			// ViewHolder holder = (ViewHolder) convertView.getTag();
			viewHolder.eventPlace.setText(record.get(EventElement.EVENT_PLACE).toString());
			viewHolder.eventDescription.setText(record.get(EventElement.EVENT_DESCRIPTION).toString());
			viewHolder.eventReward.setText(record.get(EventElement.EVENT_REWARD).toString());
			viewHolder.eventRemainingTime.setText(record.get(EventElement.EVENT_REMAINING_TIME).toString());

		} else {
			if (viewType == Constants.VIEWTYPE_INVASION) {
				convertView = inflater.inflate(R.layout.invasion_item, null);
			} else if (viewType == Constants.VIEWTYPE_OUTBREAK) {
				convertView = inflater.inflate(R.layout.outbreak_item, null);
			}
			viewHolder.eventPlace = (TextView) convertView.findViewById(R.id.event_place);
			viewHolder.eventReward = (TextView) convertView.findViewById(R.id.event_reward);

			convertView.setTag(viewHolder);

			// ViewHolder holder = (ViewHolder) convertView.getTag();
			viewHolder.eventPlace.setText(record.get(EventElement.EVENT_PLACE).toString());
			viewHolder.eventReward.setText(record.get(EventElement.EVENT_REWARD).toString());
		}
		return convertView;
	}
}
