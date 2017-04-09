package com.example.funny.telephone_book;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class dialog extends AppCompatActivity {

    TextView textData;
    EditText editData;
    Button sendBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);
        final Intent intent = getIntent();
        final String a = intent.getAction();
        textData = (TextView) findViewById(R.id.textData);
        editData = (EditText) findViewById(R.id.editData);
        sendBack = (Button) findViewById(R.id.sendBack);
        sendBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("text",editData.getText().toString());
                if (a.equals("android.intent.action.NAME")) {
                    setResult(1,intent);
                }
                else if(a.equals("android.intent.action.LASTNAME"))
                {
                    setResult(2,intent);
                }
                else
                {
                    setResult(3,intent);
                }
                finish();
            }
        });
        if (a.equals("android.intent.action.NAME"))
        {
            textData.setText(R.string.enterName);
        }
        else if(a.equals("android.intent.action.LASTNAME"))
        {
            textData.setText(R.string.enterLastname);
        }


    }
}
