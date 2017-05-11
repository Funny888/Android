package com.example.funny.telephone_book;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main extends AppCompatActivity implements View.OnClickListener {

    TextView firstName, lastName, telephone, contName, contLastname, contTelef;
    Button setBName, setBLast, setBTelephone, listB, recordB,recordPhoto;
    Intent intent;
    database dtb = new database(this);
    ListView list;
    SimpleCursorAdapter siplCursAdapt;
    ImageView imageView;
    public int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        firstName = (TextView) findViewById(R.id.firsName);
        lastName = (TextView) findViewById(R.id.lastName);
        telephone = (TextView) findViewById(R.id.tephone);
        contName = (TextView) findViewById(R.id.contName);
        contLastname = (TextView) findViewById(R.id.contLastname);
        contTelef = (TextView) findViewById(R.id.contTelef);
        setBName = (Button) findViewById(R.id.setBFirst);
        setBName.setOnClickListener(this);
        setBLast = (Button) findViewById(R.id.setBLast);
        setBLast.setOnClickListener(this);
        setBTelephone = (Button) findViewById(R.id.setBTelephone);
        setBTelephone.setOnClickListener(this);
        listB = (Button) findViewById(R.id.listB);
        listB.setOnClickListener(this);
        recordB = (Button) findViewById(R.id.recordB);
        recordB.setOnClickListener(this);
        recordPhoto = (Button)findViewById(R.id.recordPhoto);
        recordPhoto.setOnClickListener(this);
        list = (ListView) findViewById(R.id.list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               String idUpdate = String.valueOf(parent.getItemIdAtPosition(position));
                intent = new Intent("Contact");
                intent.putExtra("idUpdate",idUpdate);
                startActivity(intent);
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Main.this.id = Integer.parseInt(String.valueOf(parent.getItemIdAtPosition(position)));
                return false;
            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, R.string.exit);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1: {
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        SQLiteDatabase db = dtb.getWritableDatabase();
        switch (v.getId()) {
            case R.id.setBFirst: { //Создание интента для запроса имени
                intent = new Intent("android.intent.action.NAME");
                startActivityForResult(intent, 1);
                break;
            }
            case R.id.setBLast: { //Создание интента для запроса фамилии
                intent = new Intent("android.intent.action.LASTNAME");
                startActivityForResult(intent, 1);
                break;
            }
            case R.id.setBTelephone: { //Создание интента для запроса номера
                intent = new Intent("android.intent.action.TELEPHONE");
                startActivityForResult(intent, 1);
                break;
            }
            case R.id.listB: {
                listFunct(db);
                break;
            }
            case R.id.recordB: {
                ContentValues contentValues = new ContentValues(); // Создание экземпляра контейнера для записи в базу
                contentValues.put(dtb.NAME, firstName.getText().toString());
                contentValues.put(dtb.LASTNAME, lastName.getText().toString());
                contentValues.put(dtb.TELEPHONE, telephone.getText().toString());
                contentValues.put(dtb.LINK_PHOTO,android.R.drawable.ic_input_add);
                if (firstName.getText().equals(getText(R.string.firstName)) || firstName.getText().equals(String.valueOf("")) == true) // Проверка на пустое значение или значение не изменено
                {
                    Toast.makeText(this, "Не введено " + getText(R.string.firstName), Toast.LENGTH_LONG).show();
                }
                    else if (lastName.getText().equals(getText(R.string.LastName)) || lastName.getText().equals(String.valueOf("")) == true)
                {
                    Toast.makeText(this, "Не введено " + getText(R.string.LastName), Toast.LENGTH_LONG).show();
                }
                    else if (telephone.getText().equals(getText(R.string.telephone)) | telephone.getText().equals(String.valueOf("")) == true)
                {
                    Toast.makeText(this, "Не введено " + getText(R.string.telephone), Toast.LENGTH_LONG).show();
                }
               else {
                    db.insert(dtb.NAME_DATATABLE, null, contentValues); // Запись в базу данных
                    Toast.makeText(this, R.string.recordText, Toast.LENGTH_LONG).show();
                }
                break;
            }
            case R.id.recordPhoto:
            {
                intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
//                intent.putExtra(Intent.ACTION_OPEN_DOCUMENT,"d");
                startActivityForResult(intent,4);

                break;
            }

        }


    }


    void listFunct(SQLiteDatabase db) {
        Cursor cursor = db.query(dtb.NAME_DATATABLE, null, null, null, null, null, null); // Создание курсора для запроса
        if (cursor.getCount() == 0) {
            Toast.makeText(getBaseContext(), "Контактов нет", Toast.LENGTH_SHORT).show();
        }
//        photo = (ImageView) findViewById(R.id.photo);
//        photo.setImageResource(R.drawable.space);
        String[] from = new String[]{dtb.NAME, dtb.LASTNAME, dtb.TELEPHONE,dtb.LINK_PHOTO}; // Создание массива из базы данных
        int[] to = new int[]{R.id.contName, R.id.contLastname, R.id.contTelef,R.id.photo}; // Создание массива из ИД View элементов
        siplCursAdapt = new SimpleCursorAdapter(this, R.layout.listlay, cursor, from, to); // Создание адаптера
        list.setAdapter(siplCursAdapt); // Установка адаптера на ListView
        registerForContextMenu(list); // Установка ListView на main activity

        //                cursor.moveToFirst();
//                int indexId = cursor.getColumnIndex(dtb.KEY_ID);
//                int name = cursor.getColumnIndex(dtb.NAME);
//                int lastname = cursor.getColumnIndex(dtb.LASTNAME);
//                int telephone = cursor.getColumnIndex(dtb.TELEPHONE);
//                if (cursor.moveToFirst())
//                do {
//                    Log.d("log", "Key = " + cursor.getString(indexId) + " , Name = " + cursor.getString(name) +
//                           " , Lastname = " + cursor.getString(lastname) + " , Telephone = " + cursor.getString(telephone));
//                }
//                while (cursor.moveToNext());
//                else
//                {
//                    Log.d("log", "Записей не найдено");
//                }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, 1, 0, R.string.delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        SQLiteDatabase db = dtb.getWritableDatabase();
        switch (item.getItemId()) {
            case 1: {
                db.delete(dtb.NAME_DATATABLE, dtb.KEY_ID + " = " + id, null);
                Toast.makeText(getBaseContext(), R.string.contDel, Toast.LENGTH_SHORT).show();
                listFunct(db);
            }
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
            try {
                String text = intent.getStringExtra("text");
                if (resultCode == 1) {
                    firstName.setText(text);
                } else if (resultCode == 2) {
                    lastName.setText(text);
                } else if (resultCode == 3) {
                    telephone.setText(text);
//                    regVir();
                }
                else if (requestCode == 4){
                    if (resultCode == RESULT_OK)
                    {


                        }
                }
                else {
                    Toast.makeText(this, "Упс", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(getBaseContext(), "Запись не сделана", Toast.LENGTH_SHORT).show();
            }
    }
    public void regVir()
    {
        Pattern pattern = Pattern.compile("[+0-9]");
        Matcher matcher = pattern.matcher(telephone.getText().toString());
        if (matcher.matches())
        {
            System.out.println("yes");
        }
        else
        {
            System.out.println("no");
        }
    }
}
