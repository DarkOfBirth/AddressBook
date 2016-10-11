package lanou.addressbook.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by dllo on 16/9/23.
 */
public class ContactListHelper extends SQLiteOpenHelper {
    String[] name = {"王大", "陈二", "张三", "李四", "王五"};
    Calendar cal = Calendar.getInstance();

    public ContactListHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);


    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.d("ContactListHelper", "oncreate");
        db.execSQL("create table contact (id integer primary key autoincrement,name text, number text, date text, checked text, visible text)");
        db.execSQL("create table person (id integer primary key autoincrement, name text, number text, imageResource BLOB)");
        db.execSQL("create table message (id integer primary key autoincrement, name text, content text, date text, phonenumber text, type integer)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
