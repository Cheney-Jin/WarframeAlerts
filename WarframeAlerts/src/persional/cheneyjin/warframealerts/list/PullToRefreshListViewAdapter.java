package persional.cheneyjin.warframealerts.list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import persional.cheneyjin.warframealerts.R;
import persional.cheneyjin.warframealerts.WFAlertsMainActivity;
import persional.cheneyjin.warframealerts.objects.EventElement;
import persional.cheneyjin.warframealerts.objects.RssItem;
import persional.cheneyjin.warframealerts.utils.Constants;
import persional.cheneyjin.warframealerts.utils.ScreenSize;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author CheneyJin 
 *	E-mail:cheneyjin@outlook.com
 */
public class PullToRefreshListViewAdapter extends android.widget.BaseAdapter {

	private List<HashMap<String, Object>> 	items = new ArrayList<HashMap<String, Object>>();
	private WFAlertsMainActivity mContext;
	
	private float screenWidth = 0;
	
	public PullToRefreshListViewAdapter(WFAlertsMainActivity context) {
		this.mContext = context;
		ScreenSize screenSize = new ScreenSize(mContext);
		screenWidth = screenSize.WIDTH;
	}

	public class ViewHolder {
		//public ImageView eventImg;
		public TextView eventPlace;
		//public TextView eventTask;
		public TextView eventDescription;
		public TextView eventReward;
		public TextView eventRemainingTime;
	}

	public void loadData(List<HashMap<String, Object>> itemList) {
		if(itemList == null) return;
		items = itemList;
		// MANDATORY: Notify that the data has changed
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Map<String, Object> getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public int getEventsType(String itemAuthor) {
		// TODO Auto-generated method stub
		String author = itemAuthor;
		if ("Alert".equals(author)) {
			return Constants.VIEWTYPE_ALERT;
		} else if ("Invasion".equals(author)) {
			return Constants.VIEWTYPE_INVASION;
		} else if ("Outbreak".equals(author)) {
			return Constants.VIEWTYPE_OUTBREAK;
		} else {
			return Constants.VIEWTYPE_ERROR;
		}
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		Map<String, Object> record = getItem(position);
		LayoutInflater inflater = mContext.getLayoutInflater();
		ViewHolder viewHolder = new ViewHolder();
		int viewType = getEventsType(record.get(RssItem.AUTHOR).toString());
		if(viewType == Constants.VIEWTYPE_ALERT){
			rowView = inflater.inflate(R.layout.alert_item, null);
			rowView.findViewById(R.id.content).getLayoutParams().width=(int) (screenWidth*0.75);

			viewHolder.eventPlace = (TextView) rowView.findViewById(R.id.event_place);
			viewHolder.eventDescription = (TextView) rowView.findViewById(R.id.event_description);
			viewHolder.eventReward = (TextView) rowView.findViewById(R.id.event_reward);
			viewHolder.eventRemainingTime = (TextView) rowView.findViewById(R.id.event_remaining_time);
			rowView.setTag(viewHolder);

			ViewHolder holder = (ViewHolder) rowView.getTag();
			holder.eventPlace.setText(record.get(EventElement.EVENT_PLACE).toString());
			holder.eventDescription.setText(record.get(EventElement.EVENT_DESCRIPTION).toString());
			holder.eventReward.setText(record.get(EventElement.EVENT_REWARD).toString());
			holder.eventRemainingTime.setText(record.get(EventElement.EVENT_REMAINING_TIME).toString());
			
		}else{
			if(viewType == Constants.VIEWTYPE_INVASION){
				rowView = inflater.inflate(R.layout.invasion_item, null);				
			}else if(viewType == Constants.VIEWTYPE_OUTBREAK){
				rowView = inflater.inflate(R.layout.outbreak_item, null);
			}
			viewHolder.eventPlace = (TextView) rowView.findViewById(R.id.event_place);
			viewHolder.eventReward = (TextView) rowView.findViewById(R.id.event_reward);
			
			rowView.setTag(viewHolder);

			ViewHolder holder = (ViewHolder) rowView.getTag();
			holder.eventPlace.setText(record.get(EventElement.EVENT_PLACE).toString());
			holder.eventReward.setText(record.get(EventElement.EVENT_REWARD).toString());
		}
		
		return rowView;
	}
	/*
	 * 
	 * public class ViewHolder { //public TextView guid; //public TextView
	 * author; //public TextView title; //public TextView description; //public
	 * TextView pubDate; //public TextView wf_faction; //public TextView
	 * wf_expiry; public TextView eventPlace; public TextView eventTask; public
	 * TextView eventReward; public TextView remainingTime; }
	 * 
	 * if(viewType == Constants.VIEWTYPE_ALERT){ rowView =
	 * inflater.inflate(R.layout.list_item, null); //viewHolder.eventImg =
	 * (ImageView) rowView.findViewById(R.id.event_reward_img);
	 * viewHolder.eventPlace =(TextView) rowView.findViewById(R.id.event_place);
	 * viewHolder.eventDescription = (TextView)
	 * rowView.findViewById(R.id.event_description); viewHolder.eventReward =
	 * (TextView) rowView.findViewById(R.id.event_reward);
	 * viewHolder.eventRemainingTime = (TextView)
	 * rowView.findViewById(R.id.event_remaining_time);
	 * rowView.findViewById(R.id.event_task).setVisibility(View.GONE);
	 * rowView.setTag(viewHolder);
	 * 
	 * ViewHolder holder = (ViewHolder) rowView.getTag();
	 * //holder.eventImg.setBackgroundResource(R.id.);
	 * holder.eventPlace.setText(splitAlertValue.getEventPlace());
	 * holder.eventDescription
	 * .setText(record.get(RssItem.DESCRIPTION).toString());
	 * holder.eventReward.setText(splitAlertValue.getEventRewardGoods());
	 * holder.
	 * eventRemainingTime.setText(splitAlertValue.getEventRemaningTime());
	 * 
	 * } else if(viewType == Constants.VIEWTYPE_INVASION){
	 * 
	 * } else if(viewType == Constants.VIEWTYPE_OUTBREAK){
	 * 
	 * } else{
	 * 
	 * }
	 */

}
