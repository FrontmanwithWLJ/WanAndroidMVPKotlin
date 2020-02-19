package cqupt.sl.wanandroidmk.sql

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import cqupt.sl.wanandroidmk.res.Res

class SqLiteUtil(context: Context?) :
    SQLiteOpenHelper(context, Res.DATABASE_NAME, null, Res.DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        //主页数据
        //db?.execSQL("create table if not exists ${Res.HOME_PATH}(${Res.DATABASE_ARTICLE_PAGE} integer,${Res.HOME_BANNER} varchar,${Res.HOME_TOP_ARTICLE} varchar,${Res.HOME_ARTICLE} varchar,${Res.HOME_HOT_KEY} varchar)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

}