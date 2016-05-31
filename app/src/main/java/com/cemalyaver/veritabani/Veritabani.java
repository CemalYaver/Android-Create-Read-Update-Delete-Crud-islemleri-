package com.cemalyaver.veritabani;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by cemal on 23.5.2016.
 */
public class Veritabani extends SQLiteOpenHelper {

    private static final String VERITABANI="ogrenciler.db";
    private static final int SURUM=1;
       public Veritabani(Context c)
    {

        super(c, VERITABANI, null, SURUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE ogrenciisim (id INTEGER PRIMARY KEY AUTOINCREMENT,isim TEXT,soyad TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE If EXIST ogrenciisim");
        onCreate(db);

    }
}
