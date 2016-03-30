/**
 * 
 */
package persional.cheneyjin.warframealerts.utils;

import android.app.Activity;
import android.util.DisplayMetrics;
import persional.cheneyjin.warframealerts.WFAlertsMainActivity;

/**
 * @author CheneyJin E-mail:cheneyjin@outlook.com
 */
public class ScreenSize {
	private float width = 0;
	private float height = 0;

	private WFAlertsMainActivity sContext;

	@SuppressWarnings("deprecation")
	public ScreenSize(WFAlertsMainActivity context) {
		this.sContext = context;
		width = sContext.getWindowManager().getDefaultDisplay().getWidth();
		height = sContext.getWindowManager().getDefaultDisplay().getHeight();
	}

	public DisplayMetrics getDisplayMetrics(Activity activity) {
		DisplayMetrics dm = null;
		if (dm == null) {
			// dm = new DisplayMetrics();
			dm = activity.getResources().getDisplayMetrics();
		}
		return dm;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}
}
