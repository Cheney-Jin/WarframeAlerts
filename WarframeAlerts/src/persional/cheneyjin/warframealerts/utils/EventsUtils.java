package persional.cheneyjin.warframealerts.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * @author CheneyJin E-mail:cheneyjin@outlook.com
 */
public class EventsUtils {
	private SharedPreferences warframePlatformType;
	private SharedPreferences.Editor editor;

	public EventsUtils(Context context) {
		warframePlatformType = context.getSharedPreferences( "Warframe_Platform_type", Context.MODE_PRIVATE);
	}

	public void savePlatformType(String platformType) {
		try {
			editor = warframePlatformType.edit();
			editor.putString("PlatformType", platformType);
			editor.commit();
			Constants.RSS_PLATFORM = platformType;
		} catch (Exception e) {
			Log.i("SAVE_ERROR", "<!>PlatformType:" + platformType + " <!>");
		}
	}

	public String getPlatformType() {
		try {
			return warframePlatformType.getString("PlatformType", Constants.WARFRAME_ALERTS_PLATFORM_PC);
		} catch (Exception e) {
			// cause to old_app_users
			return Constants.WARFRAME_ALERTS_PLATFORM_PC;
		}
	}

}
