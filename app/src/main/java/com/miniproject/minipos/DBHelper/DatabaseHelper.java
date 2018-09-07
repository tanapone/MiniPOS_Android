package com.miniproject.minipos.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{

    private static String DB_NAME = "MiniPOS";
    public DatabaseHelper(Context context){
        super(context,DB_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createWSConnectorConfig = "CREATE TABLE IF NOT EXISTS WSConnector("+
                "serverID INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "serverIP TEXT ,"+
                "serverPort TEXT)";

        String createPin = "CREATE TABLE IF NOT EXISTS UserPIN("+
                "userID INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "username TEXT ,"+
                "authKey TEXT)";

        db.execSQL(createWSConnectorConfig);
        db.execSQL(createPin);
        System.out.println("Created database");
    }

    public boolean setWSConnectorConfig(String serverIP,String serverPort){
        long result = -1;
        System.out.println("--------------------------");
        System.out.println("Result ");
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("serverID",1);
            values.put("serverIP",serverIP);
            values.put("serverPort",serverPort);
            result = db.insert("WSConnector",null,values);
            db.close();
            System.out.println("--------------------------");
            System.out.println("Result ");
            System.out.println(result);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean updateWSConnectorConfig(String serverIP,String serverPort){
        long result = -1;
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("serverIP",serverIP);
            values.put("serverPort",serverPort);
            result = db.update("WSConnector",values,"serverID = ?",new String[] {String.valueOf(1)});
            db.close();

        }catch (Exception ex){
            ex.printStackTrace();
        }

        if(result == -1){
            return false;
        }else{
            return true;
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop Table if exists WSConnector");
        db.execSQL("Drop Table if exists UserPIN");
    }
}
