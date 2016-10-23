package com.mayeosurge.questmaster;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbQuestAdapter {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_DESC = "description";
    public static final String KEY_REWARD = "reward";
    public static final String KEY_GOLD = "gold";
    public static final String KEY_LEVELS = "lvls";
    public static final String KEY_ACTIVE = "questActive";
    public static final String KEY_START_TIME = "startTime";
    public static final String KEY_DURATION = "duration";
    public static final String KEY_COMPLETED = "complete";

    private static final String DATABASE_NAME = "QmDatabase";
    private static final String DATABASE_TABLE = "Quests";

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE =
            "CREATE TABLE "+DATABASE_TABLE+" ("+KEY_ROWID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_TITLE+" TEXT, "+KEY_DESC+" TEXT, "+KEY_REWARD+" TEXT, "
            + KEY_GOLD+" INTEGER DEFAULT 0, "+ KEY_LEVELS +" TEXT, "+KEY_ACTIVE+" INTEGER DEFAULT 0, "
            + KEY_START_TIME+" INTEGER, "+KEY_DURATION+" INTEGER, "+KEY_COMPLETED+" INTEGER DEFAULT 0);";

    public DatabaseHelper DBHelper;

    public DbQuestAdapter(Context ctx){ DBHelper = new DatabaseHelper(ctx); }

    private static class DatabaseHelper extends SQLiteOpenHelper{
        DatabaseHelper(Context c){super(c, DATABASE_NAME, null, DATABASE_VERSION);}

        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(DATABASE_CREATE);
            DbQuestAdapter.newQuest(db);
            DbQuestAdapter.newQuest(db);
            DbQuestAdapter.newQuest(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

        }
    }

    //Open database
    public DbQuestAdapter open() throws SQLException {
        //SQLiteDatabase sdb = DBHelper.getWritableDatabase();
        return this;
    }

    //Close database
    public void close(){DBHelper.close();}

    public void newQuest(Quest q){
        System.out.println("DB qID (pre): "+q.id);
        SQLiteDatabase sdb = DBHelper.getWritableDatabase();
        long id = DatabaseUtils.queryNumEntries(sdb, DATABASE_TABLE) + 1;
        q.id = id;
        long ad = sdb.insert(DATABASE_TABLE, null, Conversions.questToArgs(q));
        if(ad > 0)
            System.out.println("New quest added. id: "+id+"_"+ad);
        else
            System.out.println("Error adding new quest from Quest");
        sdb.close();
    }

    public static void newQuest(SQLiteDatabase db){
        long id = DatabaseUtils.queryNumEntries(db, DATABASE_TABLE) + 1;
        Quest newQ = new Quest(id);
        long ad = db.insert(DATABASE_TABLE, null, Conversions.questToArgs(newQ));
        if(ad > 0)
            System.out.println("New quest added. id: "+id+"_"+ad);
    }

    public void startQuest(long id, long startTime){
        SQLiteDatabase db = DBHelper.getWritableDatabase();
        if(checkQuest(db, id)){
            Quest q = getQuest(db, id);
            System.out.println("Notification in: "+q.duration/1000+" seconds.");
            q.questActive = true;
            q.startTime = startTime;
            q.questSucceeded = false;
            db.update(DATABASE_TABLE, Conversions.questToArgs(q), KEY_ROWID+"="+id, null);
        }else
            System.out.println("Error starting Quest. Cannot find with id="+id);
    }

    public boolean finishQuest(long id, long currentTime) {
        System.out.println("Checking for finished quest...");
        SQLiteDatabase db = DBHelper.getWritableDatabase();
        Quest q = getQuest(db, id);
        System.out.println("Start time: "+q.startTime);
        System.out.println("Current time: "+currentTime);
        System.out.println("End time: "+(q.startTime + q.duration));
        if (q.startTime + q.duration < currentTime) {
            q.questActive = false;
            q.questSucceeded = true;
            System.out.println("Finished quest, updating DB");
            int ret = db.update(DATABASE_TABLE, Conversions.questToArgs(q), KEY_ROWID+"="+id, null);
            System.out.println("DB updated with finished quest "+id+": "+(ret > 0));
            return ret > 0;
        }else {
            System.out.println("DB: Quest not over quite yet...");
            return false;
        }
    }

    public Cursor getActiveQuests(){
        SQLiteDatabase db = DBHelper.getReadableDatabase();
        String query = "SELECT * FROM "+DATABASE_TABLE+" WHERE "+KEY_ACTIVE+" = 1;";
        Cursor c = db.rawQuery(query, null);
        return c;
    }

    public Cursor getAllQuests(){
        SQLiteDatabase db = DBHelper.getReadableDatabase();
        String query = "SELECT * FROM "+DATABASE_TABLE+";";
        return db.rawQuery(query, null);
    }

    public Quest getQuest(long qId){
        SQLiteDatabase db = DBHelper.getReadableDatabase();
        Quest ret = null;
        if(qId > 0){
            Cursor c = getQuestData(db, qId);
            if(c.moveToFirst())
                ret = new Quest(c);
            c.close();
        }
        db.close();
        return ret;
    }

    public Quest getQuest(SQLiteDatabase db, long qId){
        Quest ret = null;
        if(qId > 0){
            Cursor c = getQuestData(db, qId);
            if(c.moveToFirst())
                ret = new Quest(c);
            c.close();
        }
        return ret;
    }

    public boolean checkQuest(SQLiteDatabase db, long id){
        String query = "SELECT * FROM "+DATABASE_TABLE+" WHERE "+KEY_ROWID+" = "+id+";";
        Cursor mCursor = db.rawQuery(query, null);
        if(mCursor.getCount() <= 0){
            mCursor.close();
            return false;
        }
        mCursor.close();
        return true;
    }

    public Cursor getQuestData(SQLiteDatabase db, long qID){
        String query = "SELECT * FROM "+DATABASE_TABLE+" WHERE "+KEY_ROWID+" = "+qID+";";
        Cursor mCursor = db.rawQuery(query, null);
        if(mCursor != null)
            mCursor.moveToFirst();
        return mCursor;
    }
}
