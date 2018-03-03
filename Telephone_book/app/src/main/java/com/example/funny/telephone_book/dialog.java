package com.example.funny.telephone_book;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.icu.text.DisplayContext;
import android.icu.text.MessagePattern;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.funny.telephone_book.R.layout.dialog;

public class dialog extends AppCompatActivity {

    TextView textData;
    EditText editData;
    Button sendBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(dialog);
        final Intent intent = getIntent();
        final String a = intent.getAction();
        textData = (TextView) findViewById(R.id.textData);
        editData = (EditText) findViewById(R.id.editData);

        sendBack = (Button) findViewById(R.id.sendBack);
        sendBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent.putExtra("text", editData.getText().toString());
                if (a.equals("android.intent.action.NAME")) {

                    Pattern pattern = Pattern.compile("\\S+");
                    Matcher matcher = pattern.matcher(editData.getText().toString());
                    if (matcher.matches()) {
                        setResult(1, intent);
                        finish();
                    } else {
                        Toast.makeText(getBaseContext(), R.string.writeNotRecord, Toast.LENGTH_SHORT).show();
                    }
                } else if (a.equals("android.intent.action.LASTNAME")) {
                    Pattern pattern = Pattern.compile("\\S+");
                    Matcher matcher = pattern.matcher(editData.getText().toString());
                    if (matcher.matches()) {
                        setResult(2, intent);
                        finish();
                    } else {
                        Toast.makeText(getBaseContext(), R.string.writeNotRecord, Toast.LENGTH_SHORT).show();
                    }
                } else if (a.equals("android.intent.action.TELEPHONE")) {
                    Pattern pattern = Pattern.compile("((\\+7) \\(([0-9]{3})\\) ([0-9]{3})-([0-9]{2})-([0-9]{2}))"); //создаем паттерн для номера
                    Matcher matcher = pattern.matcher(editData.getText().toString()); // ищем совпадение с данными в поле editData
                    if (matcher.matches()) {
                        setResult(3, intent);
                        finish();
                    } else {
                        Toast.makeText(getBaseContext(), R.string.writeNotRecord, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        if (a.equals("android.intent.action.NAME")) {
            textData.setText(R.string.enterName);

        } else if (a.equals("android.intent.action.LASTNAME")) {
            textData.setText(R.string.enterLastname);
        } else if (a.equals("android.intent.action.TELEPHONE")) {
            textData.setText(R.string.enterTelephone);
            editData.setInputType(3); // устанавливаем только цифровой ввод с виртуальной клавиатуры
            editData.setClickable(false); // устанавливаем невозможность перемещать курсор
            editData.setFilters(new InputFilter[]{new InputFilter.LengthFilter(18)});//Фильтр для ограничения колличество символов
            editData.addTextChangedListener(new TextWatcher() {

                private int mAfter, pos, colVo;
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    mAfter = after;
                    pos = start;
                    colVo = count;
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        if (colVo < mAfter) {

                            switch (s.length()) {
                                case 1: {
                                    s.insert(0, "+7 (", 0, 4);
                                    break;
                                }
                                case 7: {
                                    s.insert(7, ") ");
                                    break;
                                }
                                case 12: {
                                    s.insert(12, "-");
                                    break;
                                }
                                case 15: {
                                    s.insert(15, "-");
                                    break;
                                }
                            }


                            if (s.length() == 18)
                                editData.setTextColor(Color.GREEN);
                            else
                                editData.setTextColor(Color.WHITE);
                        }

                        if (colVo > mAfter) {

                            switch (s.length()) {
                                case 15: {
                                    s.delete(14, 15);
                                    break;
                                }
                                case 12: {
                                    s.delete(11, 12);
                                    break;
                                }
                                case 9: {
                                    s.delete(6, 9);
                                    break;
                                }
                                case 4: {
                                    s.delete(0, 4);
                                    break;
                                }
                            }
                        }


                    } catch (Exception e) {
                        Toast.makeText(getBaseContext(),"Не правильно набрали номер?", Toast.LENGTH_LONG).show();
                    }

                }
            });

        }

    }
}
