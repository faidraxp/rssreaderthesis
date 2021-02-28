package com.example.rssnewsreader.sqlite;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;




public class DBHelper extends SQLiteOpenHelper{

    public static final String TAG = "DBHelper";

    // columns of the rssfeed table
    public static final String TABLE_RSS_FEED= "rssfeed";
    public static final String COLUMN_FEED_ID = "id";
    public static final String COLUMN_URL= "url";
    public static final String COLUMN_TITLE= "address";
    public static final String COLUMN_LINK = "link";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_IMAGE = "image";


    private static final String SQL_CREATE_NEWS_FEED= "CREATE TABLE " + TABLE_RSS_FEED + "("
            + COLUMN_FEED_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_URL + " TEXT NOT NULL, "
            + COLUMN_TITLE + " TEXT NOT NULL, "
            + COLUMN_LINK + " TEXT NOT NULL, "
            + COLUMN_AUTHOR + " TEXT NOT NULL, "
            + COLUMN_DESCRIPTION + " TEXT NOT NULL, "
            + COLUMN_IMAGE + " TEXT NOT NULL "

            +");";



    // columns of the rssitems table
    public static final String TABLE_RSS_ITEMS = "rss_items";
    public static final String COLUMN_FEED_ITEM_ID = "id";
    public static final String COLUMN_FEED__ID = "rss_feed_id";
    public static final String COLUMN_ITEM_TITLE= "title";
    public static final String COLUMN_PUBDATE= "pubDate";
    public static final String COLUMN_ITEM_LINK = "link";
    public static final String COLUMN_ITEM_AUTHOR = "author";
    public static final String COLUMN_ITEM_DESCRIPTION = "description";
    public static final String COLUMN_ITEM_IMAGE = "enclosure";
    public static final String COLUMN_THUMBNAIL = "thumbnail";
    public static final String COLUMN_GUID = "guid";
    public static final String COLUMN_CONTENT = "content";



    private static final String SQL_CREATE_RSS_ITEMS= "CREATE TABLE " + TABLE_RSS_ITEMS + "("
            + COLUMN_FEED_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_FEED__ID + " INTEGER NOT NULL, "
            + COLUMN_ITEM_TITLE + " TEXT NOT NULL, "
            + COLUMN_PUBDATE + " TEXT NOT NULL, "
            + COLUMN_ITEM_LINK + " TEXT NOT NULL, "
            + COLUMN_ITEM_AUTHOR + " TEXT NOT NULL, "
            + COLUMN_ITEM_DESCRIPTION + " TEXT NOT NULL, "
            + COLUMN_ITEM_IMAGE + " TEXT NOT NULL, "
            + COLUMN_THUMBNAIL + " TEXT NOT NULL, "
            + COLUMN_GUID + " TEXT NOT NULL, "
            + COLUMN_CONTENT + " TEXT NOT NULL "

            +");";


    // columns of the categories table
    public static final String TABLE_CATEGORIES = "categories";
    public static final String COLUMN_CATEGORY = "id";
    public static final String COLUMN_TITLE_CATEGORY= "title";
    public static final String COLUMN_RSS_FEED_ID= "rss_feed_id";



    private static final String SQL_CREATE_CATEGORIES= "CREATE TABLE " + TABLE_CATEGORIES + "("
            + COLUMN_CATEGORY + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_RSS_FEED_ID + " INTEGER NOT NULL, "
            + COLUMN_TITLE_CATEGORY + " TEXT NOT NULL "
            +");";
    // columns of the subcategories table
    public static final String TABLE_SUBCATEGORIES = "subcategories";
    public static final String COLUMN_SUBCATEGORY_ID = "id";
    public static final String COLUMN_SUBCATEGORY_TITLE= "title";
    public static final String COLUMN_CATEGORY_ID= "category_id";

    private static final String SQL_CREATE_SUBCATEGORIES= "CREATE TABLE " + TABLE_SUBCATEGORIES + "("
            + COLUMN_SUBCATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_CATEGORY_ID + " INTEGER NOT NULL, "
            + COLUMN_SUBCATEGORY_TITLE + " TEXT NOT NULL "
            +");";


    // columns of the users table
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USER_ID = "_id";


    // columns of the usersfeed table
    public static final String TABLE_USERS_FEED = "users_feed";
    public static final String COLUMN_USER_FEED_ID = "_id";
    public static final String COLUMN_USERID = "_id";
    public static final String COLUMN_FEEDID = "_id";


    // columns of the users_rss_items table
    public static final String TABLE_USERS_RSS_ITEMS = "users_rss_items";
    public static final String COLUMN_USERS_RSS_ITEMID = "_id";
    public static final String COLUMN_USERID_ = "userid";
    public static final String COLUMN_READ_FEEDID = "feedid";


    private static final String DATABASE_NAME = "rssapp.db";
    private static final int DATABASE_VERSION = 1;







    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(SQL_CREATE_NEWS_FEED);
        database.execSQL(SQL_CREATE_RSS_ITEMS);
        database.execSQL(SQL_CREATE_CATEGORIES);
        database.execSQL(SQL_CREATE_SUBCATEGORIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG,
                "Upgrading the database from version " + oldVersion + " to "+ newVersion);
        // clear all data
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RSS_FEED);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RSS_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBCATEGORIES);

        // recreate the tables
        onCreate(db);
    }

    public DBHelper(Context context, String name, CursorFactory factory,int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }



}
