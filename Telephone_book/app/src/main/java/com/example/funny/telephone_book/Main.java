package com.example.funny.telephone_book;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class Main extends AppCompatActivity implements View.OnClickListener {

    TextView firstName, lastName, telephone, contName, contLastname, contTelef;
    Button setBName, setBLast, setBTelephone, listB, recordB;
    Intent intent;
    database dtb = new database(this);
    ListView list;
    SimpleCursorAdapter siplCursAdapt;
    ImageView photoShow; // фотография показываемая после выбора фото при записи
    public int id;
    public String photoString; // путь куда сохраниться фотография
    public Bitmap saveImage, camPhoto; // слепки
    public FileOutputStream fos; // захват потока данных для записи миникопии
    private int swPhoto; //выбор фото
    public static final int SWHICH_RECORD_INTENT=1,SWICH_RECORD_PHOTO=30,RESULT_INTENT_NAME =1,RESULT_INTENT_FAMILY=2,RESULT_INTENT_PHONE=3,SWHICH_PHOTO_STORAGE=10,SWHICH_PHOTO_CAM=20;

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
        photoShow = (ImageView) findViewById(R.id.Image1);
        photoShow.setImageResource(android.R.drawable.ic_input_add);
        photoShow.setOnClickListener(photoSwich);
        list = (ListView) findViewById(R.id.list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String idUpdate = String.valueOf(parent.getItemIdAtPosition(position));
                intent = new Intent("Contact");
                intent.putExtra("idUpdate", idUpdate);
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

    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {
        SQLiteDatabase db = dtb.getWritableDatabase();
        switch (v.getId()) {
            case R.id.setBFirst: { //Создание интента для запроса имени
                intent = new Intent("android.intent.action.NAME");
                startActivityForResult(intent,SWHICH_RECORD_INTENT);
                break;
            }
            case R.id.setBLast: { //Создание интента для запроса фамилии
                intent = new Intent("android.intent.action.LASTNAME");
                startActivityForResult(intent, SWHICH_RECORD_INTENT);
                break;
            }
            case R.id.setBTelephone: { //Создание интента для запроса номера
                intent = new Intent("android.intent.action.TELEPHONE");
                startActivityForResult(intent, SWHICH_RECORD_INTENT);
                break;
            }
            case R.id.listB: {
                listFunct(db);
                break;
            }
            case R.id.recordB: {

                File in = new File(String.valueOf(getFilesDir()) + "/BookPhoto");//Создает объект с ссылкой на папку
                if (!in.exists()) {//проверяет: если такой папки нет то создает
                    in.mkdirs();
                }

                long calendar = Calendar.getInstance().getTimeInMillis();//получаем время в миллисекундах
                photoString = getFilesDir().getPath() + File.separator + "BookPhoto/" + String.valueOf(calendar) + ".png"; //записываем в переменную весь путь нашей будущей фотографии
                try {

                    fos = new FileOutputStream(photoString); //записываем поток в файл
                    if (swPhoto == 1) {
                        saveImage.compress(Bitmap.CompressFormat.PNG, 75, fos); // преобразуем файл в PNG с качеством в 75% в потоке

                    } else if (swPhoto == 2)
                        try {

                            camPhoto.compress(Bitmap.CompressFormat.PNG, 75, fos);// преобразуем файл в PNG с качеством в 75% в потоке
                        } catch (Exception e) {
                            Toast.makeText(getBaseContext(), "Ошибка записи", Toast.LENGTH_LONG).show();
                        }
                    fos.flush();
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                ContentValues contentValues = new ContentValues(); // Создание экземпляра контейнера для записи в базу
                contentValues.put(dtb.NAME, firstName.getText().toString());
                contentValues.put(dtb.LASTNAME, lastName.getText().toString());
                contentValues.put(dtb.TELEPHONE, telephone.getText().toString());
                contentValues.put(dtb.LINK_PHOTO, photoString);
                if (firstName.getText().equals(getText(R.string.firstName)) || firstName.getText().equals(String.valueOf("")) == true) // Проверка на пустое значение или значение не изменено
                {
                    Toast.makeText(this, "Не введено " + getText(R.string.firstName), Toast.LENGTH_LONG).show();
                } else if (lastName.getText().equals(getText(R.string.lastName)) || lastName.getText().equals(String.valueOf("")) == true) {
                    Toast.makeText(this, "Не введено " + getText(R.string.lastName), Toast.LENGTH_LONG).show();
                } else if (telephone.getText().equals(getText(R.string.telephone)) | telephone.getText().equals(String.valueOf("")) == true) {
                    Toast.makeText(this, "Не введено " + getText(R.string.telephone), Toast.LENGTH_LONG).show();
                } else {
                    db.insert(dtb.NAME_DATATABLE, null, contentValues); // Запись в базу данных
                    Toast.makeText(this, R.string.recordText, Toast.LENGTH_LONG).show();

                    firstName.setText(R.string.firstName);
                    lastName.setText(R.string.lastName);
                    telephone.setText(R.string.telephone);
                    photoShow.setImageResource(android.R.drawable.ic_input_add);
                }
                break;

            }

        }


    }


    View.OnClickListener photoSwich = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent("android.intent.action.photoSwich");
            startActivityForResult(intent, SWICH_RECORD_PHOTO);
        }
    };

    void listFunct(SQLiteDatabase db) {

        Cursor cursor = db.query(dtb.NAME_DATATABLE, null, null, null, null, null, null); // Создание курсора для запроса
        if (cursor.getCount() == 0) {
            Toast.makeText(getBaseContext(), "Контактов нет", Toast.LENGTH_SHORT).show();
        }
        String[] from = new String[]{dtb.NAME, dtb.LASTNAME, dtb.TELEPHONE, dtb.LINK_PHOTO}; // Создание массива из базы данных
        int[] to = new int[]{R.id.contName, R.id.contLastname, R.id.contTelef, R.id.photo}; // Создание массива из ИД View элементов

        siplCursAdapt = new SimpleCursorAdapter(this, R.layout.listlay, cursor, from, to); // Создание адаптера
        list.setAdapter(siplCursAdapt); // Установка адаптера на ListView
        registerForContextMenu(list); // Установка ListView на main activity
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
            if (resultCode == RESULT_INTENT_NAME) {
                firstName.setText(text);
            } else if (resultCode == RESULT_INTENT_FAMILY) {
                lastName.setText(text);
            } else if (resultCode == RESULT_INTENT_PHONE) {
                telephone.setText(text);
            } else if (resultCode ==SWHICH_PHOTO_STORAGE) {

                SwichPhoto(1);
                swPhoto = 1;
            } else if (resultCode == SWHICH_PHOTO_CAM) {
                SwichPhoto(2);
                swPhoto = 2;
            } else if (requestCode == SWHICH_PHOTO_STORAGE) {
                Uri uri2 = null;
                if (intent != null) {
                    uri2 = intent.getData();
                    Log.i("TAG", "Uri: " + uri2.toString());
                    ParcelFileDescriptor parcelFileDescriptor = null;
                    parcelFileDescriptor = getContentResolver().openFileDescriptor(uri2, "r");
                    FileInputStream fileInputStream = new FileInputStream(parcelFileDescriptor.getFileDescriptor());
                    Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);
                    saveImage = Bitmap.createScaledBitmap(bitmap, 90, 110, false);
                    parcelFileDescriptor.close();
                    photoShow.setImageBitmap(saveImage);
                }
            } else if (requestCode == SWHICH_PHOTO_CAM) {
                camPhoto = (Bitmap) intent.getExtras().get("data");
                photoShow.setImageBitmap(camPhoto);
            } else {
                Toast.makeText(this, "Упс", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Запись не сделана", Toast.LENGTH_SHORT).show();
        }
    }

   public void SwichPhoto(int p) {
        if (p == 1) {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, SWHICH_PHOTO_STORAGE);

        } else if (p == 2) {
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, SWHICH_PHOTO_CAM);

        }
    }
}
