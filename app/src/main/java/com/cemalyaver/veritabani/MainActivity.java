package com.cemalyaver.veritabani;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {



    private Veritabani ogrenciler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ogrenciler = new Veritabani(this);
        final EditText adi = (EditText) findViewById(R.id.adi);
        final EditText soyadi = (EditText) findViewById(R.id.soyadi);
        Button kaydet = (Button) findViewById(R.id.kaydet);
        Button arama = (Button) findViewById(R.id.arama);
        Button sil = (Button) findViewById(R.id.sil);
        Button guncelle = (Button) findViewById(R.id.guncelle);
        Cursor cursor = KayitGetir();
        KayitGoster(cursor);


        kaydet.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v)
            {
                try{
                    KayitEkle(adi.getText().toString(),soyadi.getText().toString());
                    Cursor cursor = KayitGetir();
                    KayitGoster(cursor);
                }
                finally{
                    ogrenciler.close();
                }
            }

        });


       arama.setOnClickListener(new View.OnClickListener() {

           @Override
           public void onClick(View v) {
               SQLiteDatabase db = ogrenciler.getReadableDatabase();
               String searchStr = adi.getText().toString();

               Cursor c = db.rawQuery("SELECT isim, soyad FROM ogrenciisim WHERE isim like '" + searchStr + "'", null);

               if (c != null) {
                   if (c.moveToFirst()) {
                       do {
                           String adim = c.getString(c.getColumnIndex("isim"));
                           String soyadim = c.getString(c.getColumnIndex("soyad"));
                           Toast.makeText(MainActivity.this, "Bulunan Sonuç " + "Adı: " + adim + " ,Soyadı: " + soyadim, Toast.LENGTH_LONG).show();
                       }
                       while (c.moveToNext());
                   }
               }
           }
       });

        sil.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                silme(adi.getText().toString());
                Cursor cursor = KayitGetir();
                KayitGoster(cursor);
            }
        });


        guncelle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                guncelle(adi.getText().toString(), soyadi.getText().toString());
                Cursor cursor = KayitGetir();
                KayitGoster(cursor);
            }
        });
    }

    private void KayitEkle(String isim, String soyad){
        SQLiteDatabase db = ogrenciler.getWritableDatabase();
        ContentValues veriler = new ContentValues();
        veriler.put("isim", isim);
        veriler.put("soyad", soyad);
        db.insertOrThrow("ogrenciisim", null, veriler);
    }
    private  String[] SELECT ={"id","isim","soyad"} ;

    private Cursor KayitGetir(){
        SQLiteDatabase db = ogrenciler.getReadableDatabase();
        Cursor cursor = db.query("ogrenciisim",SELECT, null, null, null, null, null);
        return cursor;
    }

    private void KayitGoster(Cursor cursor){
        StringBuilder builder = new StringBuilder();

        while(cursor.moveToNext()){

            long id = cursor.getLong(cursor.getColumnIndex("id"));
            String ad = cursor.getString((cursor.getColumnIndex("isim")));
            String soyad = cursor.getString((cursor.getColumnIndex("soyad")));
            builder.append(id).append(" Adı: ");
            builder.append(ad).append(" Soyadı: ");
            builder.append(soyad).append("\n");
        }

        TextView text = (TextView) findViewById(R.id.text);
        text.setText(builder);
    }

    private void silme(String id) {
        SQLiteDatabase db= ogrenciler.getReadableDatabase();
        db.delete("ogrenciisim", "id"+"=?", new String[] {id});
    }

    private void guncelle(String adi, String soyadi)
    {
        SQLiteDatabase db= ogrenciler.getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put("isim", adi);
        cv.put("soyad", soyadi);
        db.update("ogrenciisim", cv, "isim"+"=?", new String[] {adi});
        db.close();
    }



}
