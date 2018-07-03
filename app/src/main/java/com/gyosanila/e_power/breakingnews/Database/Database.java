package com.gyosanila.e_power.breakingnews.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.gyosanila.e_power.breakingnews.Home.Model.News;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DB_NAME = "breaking.news";

    private static final String TABLE_NEWS = "tb_news";
    private static final String TABLE_NEWS_INTEREST = "tb_news_interest";

    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String AUTHOR = "author";
    private static final String SOURCE = "source";
    private static final String PUBLISH = "publish";
    private static final String URLIMAGE = "urlImage";
    private static final String URL = "url";


    public Database(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NEWS + " " +
                        "(" + TITLE + " TEXT, " +
                        DESCRIPTION + " TEXT, " +
                        AUTHOR + " TEXT, " +
                        SOURCE + " TEXT, " +
                        PUBLISH + " TEXT, " +
                        URLIMAGE + " TEXT, " +
                        URL + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_NEWS_INTEREST + " " +
                "(" + TITLE + " TEXT, " +
                DESCRIPTION + " TEXT, " +
                AUTHOR + " TEXT, " +
                SOURCE + " TEXT, " +
                PUBLISH + " TEXT, " +
                URLIMAGE + " TEXT, " +
                URL + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertNews(List<News> mEvent,int interests_news){
        SQLiteDatabase db = getWritableDatabase();

        for(News event : mEvent){
            ContentValues values = new ContentValues();
            values.put(TITLE,event.getTitle());
            values.put(DESCRIPTION ,event.getDescription());
            values.put(AUTHOR ,event.getAuthor());
            values.put(SOURCE,event.getSource());
            values.put(PUBLISH,event.getPublish());
            values.put(URLIMAGE ,event.getUrlImage());
            values.put(URL ,event.getUrl());

            try{
                if(interests_news == 0)
                {
                    db.insert(TABLE_NEWS, null, values);
                } else if (interests_news == 1)
                {
                    db.insert(TABLE_NEWS_INTEREST, null, values);
                }
            Log.e("Database","Insert Sukses");

            }catch (Exception e){
            }
        }

        db.close();
    }

    public List<News> getNews(int interests_news){
        List<News> mNews = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor;
        String TableName=TABLE_NEWS;
        if(interests_news == 1){
            TableName = TABLE_NEWS_INTEREST;
        }
        cursor = db.rawQuery(String.format("SELECT * FROM %s", TableName), null);

        if(cursor.moveToFirst()){
            do{
                mNews.add(new News(
                        cursor.getString(0),cursor.getString(1),
                        cursor.getString(2), cursor.getString(3),
                        cursor.getString(4),cursor.getString(5),
                        cursor.getString(6)
                ));
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return mNews;
    }


    /*
    @interestsEvents
    0 = false / allEvent
    1= true /Interests Event
     */
    public List<News> searchingNews(String query, int interestEvents){
        List<News> mNews = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String TableName = TABLE_NEWS;

        Cursor cursor = db.rawQuery("SELECT * FROM " + TableName + " WHERE " +
                TITLE + " LIKE '%" + query + "%' or " +
                AUTHOR + " LIKE '%"+ query+"%'",null);
        if(cursor.moveToFirst()){
            do{
                mNews.add(new News(
                        cursor.getString(0),cursor.getString(1),
                        cursor.getString(2), cursor.getString(3),
                        cursor.getString(4),cursor.getString(5),
                        cursor.getString(6)
                ));
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return mNews;
    }

    public void clearEvent(int interests_news){
        SQLiteDatabase db = getWritableDatabase();
        String tableName = TABLE_NEWS;
        if(interests_news == 1){
            tableName = TABLE_NEWS_INTEREST;
        }

        db.delete(tableName,null,null);

        db.close();
    }
}
