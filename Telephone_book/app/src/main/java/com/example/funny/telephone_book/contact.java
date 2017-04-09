package com.example.funny.telephone_book;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class contact extends AppCompatActivity implements View.OnClickListener {

    TextView contFirstNameU, contLastNameU, contTelephoneU;
    ImageView photoU;
    Button buttonU;
    database dtb = new database(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact);
        contFirstNameU = (TextView) findViewById(R.id.contNameU);
        contLastNameU = (TextView) findViewById(R.id.contLastnameU);
        contTelephoneU = (TextView) findViewById(R.id.contTelefU);
        photoU = (ImageView) findViewById(R.id.photoU);
        buttonU = (Button) findViewById(R.id.buttonU);
        buttonU.setOnClickListener(this);
        Intent intent = getIntent();
        String idUpdate = intent.getStringExtra("idUpdate");
        SQLiteDatabase db = dtb.getWritableDatabase();
        Cursor cur = db.query(dtb.NAME_DATATABLE,null,dtb.KEY_ID + " = " + idUpdate,null,null,null,null);
        int name = cur.getColumnIndex(dtb.NAME);
        int lastName = cur.getColumnIndex(dtb.LASTNAME);
        int telePhone = cur.getColumnIndex(dtb.TELEPHONE);
        int photo = cur.getColumnIndex(dtb.LINK_PHOTO);

        cur.moveToFirst();
        contFirstNameU.setText(cur.getString(name));
        contLastNameU.setText(cur.getString(lastName));
        contTelephoneU.setText(cur.getString(telePhone));
        //Разобраться почему не выдает адрес фотографии
        Log.d("photo2", cur.getString(photo));
        Object a = photo;
//        photoU.setImageResource();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.contNameU: {

                break;
            }
            case R.id.contLastnameU: {

                break;
            }
            case R.id.contTelefU: {

                break;
            }
            case R.id.buttonU: {

                break;
            }

        }
    }
}
