package com.example.funny.telephone_book;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class contact extends AppCompatActivity implements View.OnClickListener {

    TextView contFirstNameU, contLastNameU, contTelephoneU;
    ImageView photoU;
    Button buttonU;
    database dtb = new database(this);
    public static String idUpdate;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact);
        contFirstNameU = (TextView) findViewById(R.id.contNameU);
        contFirstNameU.setOnClickListener(this);
        contLastNameU = (TextView) findViewById(R.id.contLastnameU);
        contLastNameU.setOnClickListener(this);
        contTelephoneU = (TextView) findViewById(R.id.contTelefU);
        contTelephoneU.setOnClickListener(this);
        photoU = (ImageView) findViewById(R.id.photoU);
        photoU.setOnClickListener(this);
        buttonU = (Button) findViewById(R.id.buttonU);
        buttonU.setOnClickListener(this);
        Intent intent = getIntent();
        idUpdate = intent.getStringExtra("idUpdate");
        SQLiteDatabase db = dtb.getWritableDatabase();
        Cursor cur = db.query(dtb.NAME_DATATABLE, null, dtb.KEY_ID + " = " + idUpdate, null, null, null, null);
        int name = cur.getColumnIndex(dtb.NAME);
        int lastName = cur.getColumnIndex(dtb.LASTNAME);
        int telePhone = cur.getColumnIndex(dtb.TELEPHONE);
        int photo = cur.getColumnIndex(dtb.LINK_PHOTO);
        // Записываем данные в соответствующие поля
        cur.moveToFirst();
        contFirstNameU.setText(cur.getString(name));
        contLastNameU.setText(cur.getString(lastName));
        contTelephoneU.setText(cur.getString(telePhone));
        photoU.setImageURI(Uri.parse(cur.getString(photo)));
        photoU.setOnLongClickListener(changePhoto);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.contNameU: {
                intent = new Intent("android.intent.action.NAME");
                startActivityForResult(intent, 3);
                break;
            }
            case R.id.contLastnameU: {
                intent = new Intent("android.intent.action.LASTNAME");
                startActivityForResult(intent, 3);
                break;
            }
            case R.id.contTelefU: {
                intent = new Intent("android.intent.action.TELEPHONE");
                startActivityForResult(intent, 3);
                break;
            }
            case R.id.photoU: {
                intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contTelephoneU.getText().toString()));
                startActivity(intent);
                break;
            }
            case R.id.buttonU: {
                update(idUpdate);
                break;
            }

        }
    }

    View.OnLongClickListener changePhoto = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {

            switch (v.getId()) {
                case R.id.photoU: {
                    Snackbar.make(v,"Заглушка показанная Snackbar-ом",Snackbar.LENGTH_LONG).show();
                    break;
                }
            }


            return false;
        }
    };


    public void update(String idUpdate) {
        SQLiteDatabase db = dtb.getWritableDatabase();
        ContentValues contvalue = new ContentValues();
        contvalue.put(dtb.NAME, contFirstNameU.getText().toString());
        contvalue.put(dtb.LASTNAME, contLastNameU.getText().toString());
        contvalue.put(dtb.TELEPHONE, contTelephoneU.getText().toString());
//        contvalue.put(dtb.LINK_PHOTO," ");
        db.update(dtb.NAME_DATATABLE, contvalue, dtb.KEY_ID + " = ?", new String[]{idUpdate});

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            String text1 = data.getStringExtra("text");
            if (requestCode == 3) {
                if (resultCode == 1) {
                    contFirstNameU.setText(text1);
                } else if (resultCode == 2) {
                    contLastNameU.setText(text1);
                } else if (resultCode == 3) {
                    contTelephoneU.setText(text1);

                } else {
                    Toast.makeText(this, "Упс", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Запись не сделана", Toast.LENGTH_SHORT).show();
        }
    }
}