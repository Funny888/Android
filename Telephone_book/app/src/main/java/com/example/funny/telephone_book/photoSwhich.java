package com.example.funny.telephone_book;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class photoSwhich extends AppCompatActivity {

    Button mem,cam;
    int id;
    String[] Perm = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photoswhich);

        mem = (Button) findViewById(R.id.Mem);
        cam = (Button) findViewById(R.id.Cam);

        final Intent intent = getIntent();
        intent.putExtra("Swich",id);


      View.OnClickListener sw = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId())
                {

                    case R.id.Mem:
                    {
                        id = 10;
                        break;
                    }
                    case R.id.Cam:
                    {

                      ActivityCompat.requestPermissions(photoSwhich.this,Perm,3);
                        id = 20;
                        break;
                    }
                }
                setResult(id,intent);
                finish();

            }
      };
        mem.setOnClickListener(sw);
        cam.setOnClickListener(sw);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 3)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(getBaseContext(),"Разрешение установлено",Toast.LENGTH_SHORT).show();
                finish();
            }
            else if (grantResults[0] == PackageManager.PERMISSION_DENIED)
            {
                if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA));
                {
                    Toast.makeText(getBaseContext(),"Разрешение отклонено",Toast.LENGTH_SHORT).show();
                }
                Snackbar.make(this.findViewById(R.id.Cam),"Поставьте настройки вручную >",Snackbar.LENGTH_LONG)
                        .setAction("Настройки", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                                startActivityForResult(intent,100);
                            }
                        }

            )
                        .show();

            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100)
        {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
