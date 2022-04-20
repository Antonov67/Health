package com.example.finalkids;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity2 extends AppCompatActivity {
    public Button buttves, butttemp, buttpressure, buttpulse;
    public EditText dataot, datado;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    public String sql, strpoint;
    public ArrayList<Integer> pointy; // ось у
    public ArrayList<Integer> pointz; // ось у
    public ArrayList<String> pointx; // Ось х строится из значений дат
    private DatePickerDialog.OnDateSetListener mDateSetListener2, mDateSetListener3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mDBHelper = new DatabaseHelper(this);

        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        mDb = mDBHelper.getWritableDatabase();


        buttves = (Button) findViewById(R.id.button10);
        butttemp = (Button) findViewById(R.id.button11);
        buttpressure = (Button) findViewById(R.id.button12);
        buttpulse = (Button) findViewById(R.id.button13);

        dataot = (EditText) findViewById(R.id.editTextNumber21);
        datado = (EditText) findViewById(R.id.editTextNumber22);


        // График веса
        buttves.setOnClickListener(new View.OnClickListener() {
            ArrayList <String> pointx = new ArrayList<>();
            ArrayList <Integer> pointy = new ArrayList<>();
            @Override
            public void onClick(View v) {
                // поля "ОТ" и "ДО" пустые
                if (dataot.getText().length() == 0 && datado.getText().length() == 0) {
                    sql = "SELECT date, weight FROM data ORDER BY date";
                }
                // // поля "ОТ" и "ДО" заполнены
                if (dataot.getText().length() != 0 && datado.getText().length() != 0) {
                    sql = "SELECT date, weight FROM data" +" WHERE date >= " + "'" + dataot.getText().toString() + "'" + " AND " + "date <= " + "'" + datado.getText().toString() + "'" + " ORDER BY date";
                }
                // только поле "ОТ" заполнено
                if (dataot.getText().length() != 0 && datado.getText().length() == 0) {
                    sql = "SELECT date, weight FROM data" +" WHERE date >= " + "'" + dataot.getText().toString() + "'" + " ORDER BY date";
                }
                // только поле "ДО" заполнено
                if (dataot.getText().length() == 0 && datado.getText().length() != 0) {
                    sql = "SELECT date, weight FROM data" +" WHERE date <= " + "'" + datado.getText().toString() + "'" + " ORDER BY date";
                }

                Cursor cursor = mDb.rawQuery(sql, null);
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                      pointx.add(cursor.getString(0));
                      pointy.add(Integer.parseInt(cursor.getString(1)));

                    cursor.moveToNext();
                }
                cursor.close();
                setContentView(new GrafSurfaceView(MainActivity2.this, 1, pointx, pointy));
            }
        });
        // график температуры
        butttemp.setOnClickListener(new View.OnClickListener() {
            ArrayList <String> pointx = new ArrayList<>();
            ArrayList <Integer> pointy = new ArrayList<>();
            @Override
            public void onClick(View v) {
                float f;

                // поля "ОТ" и "ДО" пустые
                if (dataot.getText().length() == 0 && datado.getText().length() == 0) {
                    sql = "SELECT date, temperature FROM data ORDER BY date";
                }
                // // поля "ОТ" и "ДО" заполнены
                if (dataot.getText().length() != 0 && datado.getText().length() != 0) {
                    sql = "SELECT date, temperature FROM data" +" WHERE date >= " + "'" + dataot.getText().toString() + "'" + " AND " + "date <= " + "'" + datado.getText().toString() + "'" + " ORDER BY date";
                }
                // только поле "ОТ" заполнено
                if (dataot.getText().length() != 0 && datado.getText().length() == 0) {
                    sql = "SELECT date, temperature FROM data" +" WHERE date >= " + "'" + dataot.getText().toString() + "'" + " ORDER BY date";
                }
                // только поле "ДО" заполнено
                if (dataot.getText().length() == 0 && datado.getText().length() != 0) {
                    sql = "SELECT date, temperature FROM data" +" WHERE date <= " + "'" + datado.getText().toString() + "'" + " ORDER BY date";
                }

                Cursor cursor = mDb.rawQuery(sql, null);
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    pointx.add(cursor.getString(0));
                    // преобразуем температуру в целое число (умножим сначала ее на 10 чтобы избавиться от запятой)
                    f = 10*Float.parseFloat(cursor.getString(1));
                    pointy.add(Math.round(f));

                    cursor.moveToNext();
                }
                cursor.close();
                setContentView(new GrafSurfaceView(MainActivity2.this, 2, pointx, pointy));
            }
        });

        //график пульса

        buttpulse.setOnClickListener(new View.OnClickListener() {
            ArrayList <String> pointx = new ArrayList<>();
            ArrayList <Integer> pointy = new ArrayList<>();
            @Override
            public void onClick(View v) {

                // поля "ОТ" и "ДО" пустые
                if (dataot.getText().length() == 0 && datado.getText().length() == 0) {
                    sql = "SELECT date, pulse FROM data ORDER BY date";
                }
                // // поля "ОТ" и "ДО" заполнены
                if (dataot.getText().length() != 0 && datado.getText().length() != 0) {
                    sql = "SELECT date, pulse FROM data" +" WHERE date >= " + "'" + dataot.getText().toString() + "'" + " AND " + "date <= " + "'" + datado.getText().toString() + "'" + " ORDER BY date";
                }
                // только поле "ОТ" заполнено
                if (dataot.getText().length() != 0 && datado.getText().length() == 0) {
                    sql = "SELECT date, pulse FROM data" +" WHERE date >= " + "'" + dataot.getText().toString() + "'" + " ORDER BY date";
                }
                // только поле "ДО" заполнено
                if (dataot.getText().length() == 0 && datado.getText().length() != 0) {
                    sql = "SELECT date, pulse FROM data" +" WHERE date <= " + "'" + datado.getText().toString() + "'" + " ORDER BY date";
                }

                Cursor cursor = mDb.rawQuery(sql, null);
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    pointx.add(cursor.getString(0));
                    pointy.add(Integer.parseInt(cursor.getString(1)));

                    cursor.moveToNext();
                }
                cursor.close();
                setContentView(new GrafSurfaceView(MainActivity2.this, 3, pointx, pointy));
            }
        });

        // Графики давления
        buttpressure.setOnClickListener(new View.OnClickListener() {
            ArrayList <String> pointx = new ArrayList<>();
            ArrayList <Integer> pointy = new ArrayList<>();
            ArrayList <Integer> pointz = new ArrayList<>();
            @Override
            public void onClick(View v) {

                // поля "ОТ" и "ДО" пустые
                if (dataot.getText().length() == 0 && datado.getText().length() == 0) {
                    sql = "SELECT date, upressure, dpressure FROM data ORDER BY date";
                }
                // // поля "ОТ" и "ДО" заполнены
                if (dataot.getText().length() != 0 && datado.getText().length() != 0) {
                    sql = "SELECT date, upressure, dpressure FROM data" +" WHERE date >= " + "'" + dataot.getText().toString() + "'" + " AND " + "date <= " + "'" + datado.getText().toString() + "'" + " ORDER BY date";
                }
                // только поле "ОТ" заполнено
                if (dataot.getText().length() != 0 && datado.getText().length() == 0) {
                    sql = "SELECT date, upressure, dpressure FROM data" +" WHERE date >= " + "'" + dataot.getText().toString() + "'" + " ORDER BY date";
                }
                // только поле "ДО" заполнено
                if (dataot.getText().length() == 0 && datado.getText().length() != 0) {
                    sql = "SELECT date, upressure, dpressure FROM data" +" WHERE date <= " + "'" + datado.getText().toString() + "'" + " ORDER BY date";
                }


                Cursor cursor = mDb.rawQuery(sql, null);
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    pointx.add(cursor.getString(0));
                    pointy.add(Integer.parseInt(cursor.getString(1)));
                    pointz.add(Integer.parseInt(cursor.getString(2)));
                    cursor.moveToNext();
                }
                cursor.close();
                setContentView(new GrafSurfaceView(MainActivity2.this, 1, pointx, pointy,pointz, 2));
            }
        });

        DatePickerDialog.OnDateSetListener mDateSetListener;
        dataot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(MainActivity2.this,android.R.style.Theme_Holo_Dialog_MinWidth, mDateSetListener2, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        datado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(MainActivity2.this,android.R.style.Theme_Holo_Dialog_MinWidth, mDateSetListener3, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String strmonth="", strday="";
                month = month + 1;
                if (Integer.toString(month).length() == 1) strmonth = "0" + Integer.toString(month); else strmonth = Integer.toString(month);
                if (Integer.toString(day).length() == 1) strday = "0" + Integer.toString(day); else strday = Integer.toString(day);
                String date = year + "-" + strmonth + "-" + strday;
                dataot.setText(date);

            }
        };

        mDateSetListener3 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String strmonth="", strday="";
                month = month + 1;
                if (Integer.toString(month).length() == 1) strmonth = "0" + Integer.toString(month); else strmonth = Integer.toString(month);
                if (Integer.toString(day).length() == 1) strday = "0" + Integer.toString(day); else strday = Integer.toString(day);
                String date = year + "-" + strmonth + "-" + strday;
                datado.setText(date);

            }
        };

    }
}