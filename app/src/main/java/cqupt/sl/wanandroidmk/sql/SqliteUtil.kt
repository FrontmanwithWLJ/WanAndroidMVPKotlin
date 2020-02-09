package cqupt.sl.wanandroidmk.sql

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SqliteUtil : SQLiteOpenHelper {
    companion object {
        const val DATABASE_NAME = "WanAndroidMVPKotlin.db"
        const val DATABASE_VERSION = 1
        const val DATABASE_HOME_ARTICLE_TABLE_NAME = "home_article"
        const val DATABASE_HOME_BANNER_TABLE_NAME = "home_banner"
    }
    constructor(context: Context?):super(context,DATABASE_NAME,null,DATABASE_VERSION)
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table if not exists $DATABASE_NAME(appLink String,)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

}