package digitalquantuminc.inscribesecuresms.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import digitalquantuminc.inscribesecuresms.DataType.TypeProfile;

/**
 * Created by Bagus Hanindhito on 28/06/2017.
 * This class is handle SQLite Database for the Profile Database.
 * Basically, the profile DB is also an application DB that contains last state of the apps.
 */

public class profileDBHelper extends SQLiteOpenHelper {
    //region Global Variable
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "isms.db";

    //endregion
    //region Constructor
    public profileDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //endregion
    //region Override Method
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_PROFILE = "CREATE TABLE " + TypeProfile.TABLE + "("
                + TypeProfile.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TypeProfile.KEY_phone + " TEXT, "
                + TypeProfile.KEY_name + " TEXT, "
                + TypeProfile.KEY_date + " INTEGER, "
                + TypeProfile.KEY_rsapub + "TEXT,"
                + TypeProfile.KEY_rsapriv + "TEXT)";

        db.execSQL(CREATE_TABLE_PROFILE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TypeProfile.TABLE);
        onCreate(db);
    }
    //endregion
}
