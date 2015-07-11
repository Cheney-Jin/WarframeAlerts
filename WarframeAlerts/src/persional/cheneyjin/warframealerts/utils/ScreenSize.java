/**
 * 
 */
package persional.cheneyjin.warframealerts.utils;

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
		/*
		DisplayMetrics DM = new DisplayMetrics();
		sContext.getWindowManager().getDefaultDisplay().getMetrics(DM);

		WIDTH= px2dip(sContext.getApplicationContext(), sContext.getApplicationContext()
	            .getResources().getDisplayMetrics().widthPixels);
		HEIGHT = px2dip(sContext.getApplicationContext(), sContext.getApplicationContext()
	            .getResources().getDisplayMetrics().heightPixels);
	    */
	}
	/*
	private final int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    */
}
