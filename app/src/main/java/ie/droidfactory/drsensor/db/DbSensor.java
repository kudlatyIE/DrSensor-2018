package ie.droidfactory.drsensor.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kudlaty on 2018-01-20.
 */

public class DbSensor extends SQLiteOpenHelper {

    private static final String TAG = DbHelper.class.getSimpleName();
    private final static String DATABASE_NAME = "dr_sensor.db";
    private static final int DATABASE_VERSION = 1;

    private static final List<String> DATABASE_CREATE_SQL = new ArrayList<>();

    private static final String DROP_TABLE = "DROP TABLE IF EXISTS ";


    public DbSensor(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
