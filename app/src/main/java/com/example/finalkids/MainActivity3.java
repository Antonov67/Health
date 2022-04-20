package com.example.finalkids;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity3 extends AppCompatActivity {
    Button buttadd, buttlist, buttdelete;
    EditText indata, ves, temp, upres, dpres, pulse;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
      mDBHelper = new DatabaseHelper(this);

        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        mDb = mDBHelper.getWritableDatabase();

        buttadd = (Button) findViewById(R.id.button5);
        buttlist = (Button)findViewById(R.id.button6);
        indata = (EditText) findViewById(R.id.editText);
        ves = (EditText) findViewById(R.id.editTextNumber);
        temp = (EditText) findViewById(R.id.editTextNumber1);
        upres = (EditText) findViewById(R.id.editTextNumber2);
        dpres = (EditText) findViewById(R.id.editTextNumber3);
        pulse = (EditText) findViewById(R.id.editTextNumber4);
        buttdelete = (Button) findViewById(R.id.button14);

        // удаление всех данных из таблицы

        buttdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sql = "";
               // ArrayList arr = new ArrayList();
                sql = "DELETE FROM data";
                Cursor cursor = mDb.rawQuery(sql, null);
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                   cursor.moveToNext();
                }
                   cursor.close();
                Toast.makeText(MainActivity3.this, "Все данные удалены", Toast.LENGTH_SHORT).show();
            }
        });


        buttadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = "", age = "", sql = "";
                ContentValues newValues = new ContentValues();
                newValues.put("date", indata.getText().toString());
                newValues.put("weight", ves.getText().toString());
                newValues.put("temperature",temp.getText().toString());
                newValues.put("upressure",upres.getText().toString());
                newValues.put("dpressure",dpres.getText().toString());
                newValues.put("pulse",pulse.getText().toString());
                // если все поля заполнены, то вводим данные в базу, иначе выводим предупреждение
                 if (indata.getText().length() == 0 || ves.getText().length() == 0 || temp.getText().length() == 0 || upres.getText().length() == 0 || dpres.getText().length() == 0 || pulse.getText().length() == 0) {
                     Toast.makeText(MainActivity3.this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                 }
                 else {
                     mDb.insert("data", null, newValues);
                     // обнулим поля ввода
                     indata.setText("");
                     ves.setText("");
                     temp.setText("");
                     upres.setText("");
                     dpres.setText("");
                     pulse.setText("");
                 }
            }
        });

        // ввод даты с помощью календаря

        indata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(MainActivity3.this,android.R.style.Theme_Holo_Dialog_MinWidth, mDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String strmonth="", strday="";
                month = month + 1;
                if (Integer.toString(month).length() == 1) strmonth = "0" + Integer.toString(month); else strmonth = Integer.toString(month);
                if (Integer.toString(day).length() == 1) strday = "0" + Integer.toString(day); else strday = Integer.toString(day);
                String date = year + "-" + strmonth + "-" + strday;
                indata.setText(date);

            }
        };


        buttlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.example.finalkids.MainActivity4");
                startActivity(intent);
            }
        });
    }
}