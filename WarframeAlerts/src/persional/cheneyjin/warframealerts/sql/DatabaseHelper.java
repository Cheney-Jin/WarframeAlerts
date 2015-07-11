/**
 * 
 */
package persional.cheneyjin.warframealerts.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author CheneyJin 
 *	E-mail:cheneyjin@outlook.com
 */
public class DatabaseHelper extends SQLiteOpenHelper{

	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	private Context 		mContext;
	
	public DatabaseHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
		this.mContext = context;
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db = mContext.openOrCreateDatabase("warframe.db",Context.MODE_PRIVATE, null);
		db.execSQL("DROP TABLE IF EXISTS Plants");
		db.execSQL("CREATE TABLE person " +
					"(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					" plant_name VARCHAR," +
					" plant_area VARCHAR" +
					" area_action_type VARCHAR" +
					" enemy_faction VARCHAR" +
					" area_level VARCHAR)"
				); 
		//Plant plant = new Plant();
		
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
