package com.bct071.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Locale;

/**
 * Created by Prasidha Karki on 5/30/2016.
 */
public class MasterDbAdapter {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_CODE = "code";
    public static final String KEY_TITLE = "titles";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_SOURCE="source";

    private static final String TAG = "MasterDbResult";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "MyDataBase";
    private static final String SQLITE_TABLE = "Poems";
    private static final String SQLITE_TABLE_POEMS="Poem";
    private static final String SQLITE_TABLE_STORIES="Story";
    private static final String SQLITE_TABLE_ESSAYS="Essay";
    private static final String SQLITE_TABLE_BALSANSARS="Balsansar";
    private static final String SQLITE_TABLE_GAJALS="Gajal";
    private static final String SQLITE_TABLE_OTHERS="Other";
    private static final String SQLITE_TABLE_OFFLINE="Offline";
    private static final int DATABASE_VERSION = 3;

    private final Context mCtx;
    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_CODE + "," +
                    KEY_TITLE + "," +
                    KEY_DESCRIPTION +");";
                    //","  +
                    // KEY_CODE +");";

    private static final String DATABASE_CREATE_POEMS =
            "CREATE TABLE if not exists " + SQLITE_TABLE_POEMS + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_CODE + "," +
                    KEY_TITLE + "," +
                    KEY_DESCRIPTION +");";
    //","  +
    // KEY_CODE +");";
    private static final String DATABASE_CREATE_STORIES =
            "CREATE TABLE if not exists " + SQLITE_TABLE_STORIES + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_CODE + "," +
                    KEY_TITLE + "," +
                    KEY_DESCRIPTION +");";
    private static final String DATABASE_CREATE_ESSAYS =
            "CREATE TABLE if not exists " + SQLITE_TABLE_ESSAYS + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_CODE + "," +
                    KEY_TITLE + "," +
                    KEY_DESCRIPTION +");";

    private static final String DATABASE_CREATE_BALSANSARS =
            "CREATE TABLE if not exists " + SQLITE_TABLE_BALSANSARS + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_CODE + "," +
                    KEY_TITLE + "," +
                    KEY_DESCRIPTION +");";

    private static final String DATABASE_CREATE_GAJALS =
            "CREATE TABLE if not exists " + SQLITE_TABLE_GAJALS + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_CODE + "," +
                    KEY_TITLE + "," +
                    KEY_DESCRIPTION +");";

    private static final String DATABASE_CREATE_OTHERS =
            "CREATE TABLE if not exists " + SQLITE_TABLE_OTHERS + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_CODE + "," +
                    KEY_TITLE + "," +
                    KEY_DESCRIPTION +");";

    private static final String DATABASE_CREATE_OFFLINES=
            "CREATE TABLE if not exists "+SQLITE_TABLE_OFFLINE+" ("+
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_CODE + "," +
                    KEY_TITLE + "," +
                    KEY_DESCRIPTION + ","+
                    KEY_SOURCE +", unique ("+ KEY_CODE +","+ KEY_TITLE +"));";



    private static class DatabaseHelper extends SQLiteOpenHelper {

        private static DatabaseHelper sInstance = null;

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        public static DatabaseHelper getInstance(Context c) {
            if (sInstance == null) sInstance = new DatabaseHelper(c);
            return sInstance;
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE_POEMS);
            db.execSQL(DATABASE_CREATE_STORIES);
            db.execSQL(DATABASE_CREATE_ESSAYS);
            db.execSQL(DATABASE_CREATE_GAJALS);
            db.execSQL(DATABASE_CREATE_BALSANSARS);
            db.execSQL(DATABASE_CREATE_OTHERS);

            db.execSQL(DATABASE_CREATE_OFFLINES);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
            //to be deleted soon
        //    db.execSQL(DATABASE_CREATE_POEMS);
        //    db.execSQL(DATABASE_CREATE_STORIES);
        //    db.execSQL(DATABASE_CREATE_ESSAYS);
        //    db.execSQL(DATABASE_CREATE_GAJALS);
        //    db.execSQL(DATABASE_CREATE_BALSANSARS);

          db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE_POEMS);
            db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE_STORIES);
          db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE_ESSAYS);
            db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE_GAJALS);
            db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE_BALSANSARS);
            db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE_OTHERS);

            db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE_OFFLINE);
                        onCreate(db);
        }

    }


    public MasterDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public MasterDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);

        mDb = mDbHelper.getWritableDatabase();
        mDb.setLocale(Locale.US);
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }



    //to add dattas in database of gven name
    public long insertData(String tableName,String code,String title,String description){
        ContentValues initialValues=new ContentValues();
        initialValues.put(KEY_CODE,code);
        initialValues.put(KEY_TITLE,title);
        initialValues.put(KEY_DESCRIPTION,description);
        return mDb.insert(tableName,null,initialValues);
    }

    public long saveData(String title,String description ,String source_text,String url){
        ContentValues initialValuew=new ContentValues();
        initialValuew.put(KEY_TITLE,title);
        initialValuew.put(KEY_DESCRIPTION,description);
        initialValuew.put(KEY_SOURCE,source_text);
        initialValuew.put(KEY_CODE,url);
        return mDb.insert(SQLITE_TABLE_OFFLINE,null,initialValuew);
    }



    //for deleting selected table data
    public boolean deleteAllData(String tableName){
        int doneDelete=0;
        doneDelete=mDb.delete(tableName,null,null);
        Log.i("Delete","Database deleted Successfully");
        return doneDelete>0;

    }


  public boolean deleteOffline(long id){

       return mDb.delete(SQLITE_TABLE_OFFLINE,KEY_ROWID+"="+id,null)>0;
  }




    public Cursor fetchDataByName(String tableName,String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = mDb.query(tableName, new String[] {KEY_ROWID,
                            KEY_CODE, KEY_TITLE, KEY_DESCRIPTION},
                    null, null, null, null, null);

        }
        else {
            mCursor = mDb.query(true, tableName, new String[] {KEY_ROWID,
                            KEY_CODE, KEY_TITLE, KEY_DESCRIPTION},
                    KEY_TITLE + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }





    public Cursor fetchAllData(String tableName) {

        Cursor mCursor = mDb.query(tableName, new String[] {KEY_ROWID,
                        KEY_CODE, KEY_TITLE, KEY_DESCRIPTION},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchAllSavedData(String tableName){
        Cursor mCursor = mDb.query(tableName, new String[] {KEY_ROWID,
                        KEY_CODE, KEY_TITLE, KEY_DESCRIPTION,KEY_SOURCE},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public int getRowCount(String tableName){

        return (int) DatabaseUtils.queryNumEntries(mDb, tableName);
    }
}
