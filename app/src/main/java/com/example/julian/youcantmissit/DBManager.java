/**
 * Initially Created by Julian on 2015-11-22.
 * Lastly modified by Julian on 2015-11-25.
 */
package com.example.julian.youcantmissit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

public class DBManager extends SQLiteOpenHelper {
    private static int version = 1;
    private static String DBname = "YouCantMissItDB";
    private static DBManager instance = null;
    private ArrayList<LocationData> dataList=null;
    private SQLiteDatabase db;
    private Context context;

    private final String tableName = "location";
    private final String id = "id";
    private final String name = "name";
    private final String lat = "lat";
    private final String lng = "lng";
    private final String act = "activeFlag";

    private DBManager(Context context) {
        super(context,DBname,null,version);
        this.context=context;
        this.db = getWritableDatabase();
        String query = "CREATE TABLE IF NOT EXISTS " +
                tableName + " (" +
                id + " INTEGER, " +
                name + " TEXT not null, " +
                lat + " FLOAT, " +
                lng + " FLOAT, " +
                act + " INTEGER);";
        db.execSQL(query);
        getAllLocation();
    }

    public static DBManager getInstance(Context context) {
        if(instance==null) {
            synchronized(DBManager.class) {
                if (instance == null) {
                    instance = new DBManager(context);
                }
            }
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public ArrayList<LocationData> getAllLocation() {
        if(this.dataList==null) {
            ArrayList<LocationData> locationList = new ArrayList<>();
            String query = "SELECT * FROM " + this.tableName + ";";
            Cursor cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                locationList.add(new LocationData(cursor.getInt(0), cursor.getString(1), cursor.getFloat(2), cursor.getFloat(3), cursor.getInt(4)));
            }
            cursor.close();
            this.dataList = locationList;
        }
        return this.dataList;
    }

    public boolean insert(LocationData newLocation) {
        for(LocationData data : this.dataList) {
            if(newLocation.getKey()==data.getKey()) {
                Toast.makeText(this.context,"이미 등록된 장소입니다.",Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        this.dataList.add(newLocation);

        ContentValues value = new ContentValues();

        value.put(id,newLocation.getKey());
        value.put(name,newLocation.getName());
        value.put(lat,newLocation.getLat());
        value.put(lng,newLocation.getLng());
        value.put(act,1);
        db.insert(tableName,null,value);

        return true;
    }

    public boolean delete(LocationData target) {
        int result = db.delete(tableName,"id=?",new String[]{String.valueOf(target.getKey())});
        if(result==0) {
            return false;
        } else return true;
    }

    public boolean delete(int key) {
        int result = db.delete(tableName,"id=?",new String[]{String.valueOf(key)});
        if(result==0) {
            return false;
        } else return true;
    }

    public ArrayList<LocationData> getActiveLocation() {
        if(this.dataList==null) {
            ArrayList<LocationData> locationList = new ArrayList<>();
            String query = "SELECT * FROM " + this.tableName + " WHERE " + act +  " =1;";
            Cursor cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                locationList.add(new LocationData(cursor.getInt(0), cursor.getString(1), cursor.getFloat(2), cursor.getFloat(3), cursor.getInt(4)));
            }
            cursor.close();
            this.dataList = locationList;
        }
        return this.dataList;
    }

    public boolean changeActive(int key, boolean newStatus) {
        int flag=0;
        if(newStatus) {
            flag=1;
        }
        String query = "UPDATE " +
                tableName + " SET " +
                act + "=" + flag + " WHERE " +
                id + "=" + key + ";";
        db.execSQL(query);
        return true;
    }
}
