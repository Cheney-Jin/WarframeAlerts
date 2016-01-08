package persional.cheneyjin.warframealerts.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;

public class NowDateTime {
	private String nowDate;
	private String expiryDate;
	private SimpleDateFormat dateFormat;

	enum Months {
		Jan("Jan", "01"), Feb("Feb", "02"), Mar("Mar", "03"), Apr("Apr", "04"), May("May", "05"), 
		Jun("Jun", "06"), Jul("Jul", "07"), Aug("Aug", "08"), Sep("Sep", "09"), Oct("Oct", "10"), 
		Nov("Nov", "11"), Dec("Dec", "12");

		private String month;
		private String index;

		private Months(String month, String index) {
			this.month = month;
			this.index = index;
		}
		
		public String getMonth() {
			return month;
		}

		public void setMonth(String month) {
			this.month = month;
		}

		public String getIndex() {
			return index;
		}

		public void setIndex(String index) {
			this.index = index;
		}
	}

	@SuppressLint("SimpleDateFormat")
	public
	NowDateTime() {
		dateFormat = new SimpleDateFormat("dd MM yyyy HH:mm:ss");
		setNowDate(dateFormat.format(new java.util.Date()));
	}
	@SuppressLint("SimpleDateFormat")
	public
	NowDateTime(String expiryTime){
		dateFormat = new SimpleDateFormat("dd MM yyyy HH:mm:ss");
		setNowDate(dateFormat.format(new java.util.Date()));	
		setExpiryDate(expiryTime);
	}
	public String formatExpiry(String time) {
		String[] cutAct1 = time.split("\\+");
		String[] cutAct2 = cutAct1[0].split(", ");
		String[] expiryTimeElems = cutAct2[1].split(" ");
		for (Months m : Months.values())
			if (m.getMonth().equals(expiryTimeElems[1])) expiryTimeElems[1] = m.index;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < expiryTimeElems.length; i++)
			sb.append(expiryTimeElems[i]).append(" ");
		setExpiryDate(sb.toString());
		return sb.toString();
	}

	public String timeDiff() throws ParseException {
		Date localTime = dateFormat.parse(nowDate); 
		Date expiryTime = dateFormat.parse(expiryDate);
		
		Calendar calTime = Calendar.getInstance();
		calTime.setTime(localTime);
		calTime.add(Calendar.HOUR,-8);
		String serviceTime = dateFormat.format(calTime.getTime());
		long diffMinutes = (expiryTime.getTime() - dateFormat.parse(serviceTime).getTime())/(1000*60);
		if (diffMinutes >= 60 && diffMinutes < 1440) return diffMinutes / 60 + "h";
		else if (diffMinutes >= 1440) return diffMinutes / 1440 + "d";
		else return String.valueOf(diffMinutes) + "m";
	}

	public String getNowDate() {
		return nowDate;
	}

	private void setNowDate(String nowDate) {
		this.nowDate = nowDate;
	}

	public String getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
}
