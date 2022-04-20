package com.example.finalkids;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity4 extends ListActivity {
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    String sql, name;
    ArrayList <String> arr  = new ArrayList<>();
    ArrayAdapter<String> dataAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDBHelper = new DatabaseHelper(this);

        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        mDb = mDBHelper.getWritableDatabase();
        sql = "SELECT * FROM data ORDER BY date";
        Cursor cursor = mDb.rawQuery(sql, null);
        cursor.moveToFirst();
        arr.add("Дата" + " | " + "вес | " + "темпертура | " + "давление | " + "пульс");
        while (!cursor.isAfterLast()) {

            name = cursor.getString(0) + " | " +  cursor.getString(1) + " кг.| " + cursor.getString(2) + "\u00B0C" + " | " + cursor.getString(3) + "/" + cursor.getString(4) + "мм рт.ст.| " +  cursor.getString(5) + " уд/мин";
            arr.add(name);
            cursor.moveToNext();
        }

        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arr);
        setListAdapter(dataAdapter);
        cursor.close();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        if (position > 0) {
            String data = (String) getListAdapter().getItem(position);
            Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
        }

    }
}