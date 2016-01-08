/**
 * 
 */
package persional.cheneyjin.warframealerts.utils;

import android.app.Activity;
import android.util.DisplayMetrics;
import persional.cheneyjin.warframealerts.WFAlertsMainActivity;

/**
 * @author CheneyJin 
 *	E-mail:cheneyjin@outlook.com
 */
public class ScreenSize {
	
	public float WIDTH = 0;
	public float HEIGHT = 0;
	
	private WFAlertsMainActivity	sContext;
	
	@SuppressWarnings("deprecation")
	public ScreenSize(WFAlertsMainActivity context){
		this.sContext = context;
		WIDTH = sContext.getWindowManager().getDefaultDisplay().getWidth();
		HEIGHT = sContext.getWindowManager().getDefaultDisplay().getHeight();
	}

	private static DisplayMetrics dm = null;

	public static DisplayMetrics getDisplayMetrics(Activity activity){
		if(dm == null){
			dm = new DisplayMetrics();  
			dm = activity.getResources().getDisplayMetrics();
		}
		return dm;
	}
}
