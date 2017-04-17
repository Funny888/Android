package com.example.funny.telephone_book;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class dialog extends AppCompatActivity {

    TextView textData;
    EditText editData;
    Button sendBack;

    private String mask = "_(___)-__-__";
    private String temp = "";
    String k;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);
        final Intent intent = getIntent();
        final String a = intent.getAction();
        textData = (TextView) findViewById(R.id.textData);
        editData = (EditText) findViewById(R.id.editData);
        editData.setText(mask);
        editData.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if(s != null)
                {



                        temp = String.valueOf(s.charAt(0));
                        k = mask.toString();
                    for (int i = 0; i < mask.length(); i++) {

                        k = k.replaceFirst(String.valueOf(mask.charAt(0)),temp);
                    }
                    textData.setText(k);



                }


}
            @Override
            public void afterTextChanged(Editable s) {



            }
        });
        sendBack = (Button) findViewById(R.id.sendBack);
        sendBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent.putExtra("text",editData.getText().toString());
                if (a.equals("android.intent.action.NAME")) {

                    Pattern pattern = Pattern.compile("\\S+");
                    Matcher matcher = pattern.matcher(editData.getText().toString());
                    setResult(1,intent);
                    if (matcher.matches()){
                        finish();}
                    else {Toast.makeText(getBaseContext(),"Ничего не введено или введено не правильно",Toast.LENGTH_SHORT).show();}
                }
                else if(a.equals("android.intent.action.LASTNAME"))
                {
                    Pattern pattern = Pattern.compile("\\S+");
                    Matcher matcher = pattern.matcher(editData.getText().toString());
                    setResult(2,intent);
                    if (matcher.matches()){
                        finish();}
                    else {Toast.makeText(getBaseContext(),"Ничего не введено или введено не правильно",Toast.LENGTH_SHORT).show();}
                }
                else if (a.equals("android.intent.action.TELEPHONE"))
                {
                    Pattern pattern = Pattern.compile("((8|\\+7)([0-9]{3})([0-9]{7}))");
                    Matcher matcher = pattern.matcher(editData.getText().toString());
                    setResult(3,intent);
                    if (matcher.matches()){
                        finish();}
                    else {Toast.makeText(getBaseContext(),"Ничего не введено или введено не правильно",Toast.LENGTH_SHORT).show();}
                }
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
        else if(a.equals("android.intent.action.TELEPHONE"))
        {
            textData.setText(R.string.enterTelephone);
        }

    }
}
