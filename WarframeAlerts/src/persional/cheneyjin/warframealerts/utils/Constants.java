package persional.cheneyjin.warframealerts.utils;

/**
 * @author CheneyJin E-mail:cheneyjin@outlook.com
 */
public class Constants {

	public static final String WARFRAME_ALERTS_PLATFORM_PS4 = "PS4";

	public static final String WARFRAME_ALERTS_PLATFORM_PC = "PC";

	public static final String WARFRAME_ALERTS_PLATFORM_XBOX = "XBOX";

	public static final String WARFRAME_ALERTS_PLATFORM_ERROR = "ERROR";

	public static final int VIEWTYPE_ALERT = 1;

	public static final int VIEWTYPE_INVASION = 2;

	public static final int VIEWTYPE_OUTBREAK = 3;

	public static final int VIEWTYPE_ERROR = 4;

	public static final String RSS_ALERT = "Alert";

	public static final String RSS_INVASION = "Invasion";

	public static final String RSS_OUTBREAK = "Outbreak";

	public static final String WARFRAME_RSS_PS4_URL = "http://content.ps4.warframe.com/dynamic/rss.php";

	public static final String WARFRAME_RSS_PC_URL = "http://content.warframe.com/dynamic/rss.php";
	// public static final String WARFRAME_RSS_PC_URL =
	// "http://10.16.0.17:8095/rss/rss.php";

	public static final String WARFRAME_RSS_XBOX_URL = "http://content.xb1.warframe.com/dynamic/rss.php";

	public static final String CONTACT_ME = "Please send mail to cheneyjin@outlook.com. ";

	public static final String PLEASE_REFRESH = "Please refresh!";
	// public static final String TEST_RSS =
	// "http://10.16.0.17:8095/rss/rss.php";

	public static String RSS_PLATFORM = "";

	public static String getRssUrl(String alertsType) {
		if (alertsType.equals(Constants.WARFRAME_ALERTS_PLATFORM_PS4))
			return Constants.WARFRAME_RSS_PS4_URL;
		else if (alertsType.equals(Constants.WARFRAME_ALERTS_PLATFORM_PC))
			return Constants.WARFRAME_RSS_PC_URL;
		else if (alertsType.equals(Constants.WARFRAME_ALERTS_PLATFORM_XBOX))
			return Constants.WARFRAME_RSS_XBOX_URL;
		else
			return Constants.WARFRAME_ALERTS_PLATFORM_ERROR;
	}
}
