package com.example.a15task;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.AdapterView;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.content.SharedPreferences;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText edit1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        edit1 = findViewById(R.id.editText);
        SharedPreferences save = getSharedPreferences("SAVE", 0);
        edit1.setText(save.getString("text", ""));
        Button button = findViewById(R.id.button);
        final SQLiteDatabase db = openOrCreateDatabase("MyDB", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS MyTable (Name VARCHAR, Number Int);");
        Cursor c = db.query("MyTable", null, null, null, null, null, null);
        String tv = "";
        if (c.moveToFirst()) {
            int name = c.getColumnIndex("Name");
            int num = c.getColumnIndex("Number");
            do {
                tv += c.getString(name) + c.getInt(num) + "\n";
            } while (c.moveToNext());
        }
        TextView textView = findViewById(R.id.textView);
        textView.setText(tv);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] text = edit1.getText().toString().split(" ");
                ContentValues cv = new ContentValues();
                try {
                    cv.put("Name", text[0]);
                    cv.put("Number", Integer.parseInt(text[1]));
                } catch (Exception e){
                    AlertDialog.Builder dialog = new
                            AlertDialog.Builder(MainActivity.this);
                    dialog.setMessage("Введите данные в формате: <Текст> <Число>");
                    dialog.setTitle("Ошибка");
                    dialog.setNeutralButton("OK", new
                            DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    dialog.setIcon(R.mipmap.ic_launcher_round);
                    AlertDialog alertDialog = dialog.create();
                    alertDialog.show();
                    return;
                }
                db.insert("MyTable", null, cv);
                Cursor c = db.query("MyTable", null, null, null, null, null, null);
                String tv = "";
                if (c.moveToFirst()) {
                    int name = c.getColumnIndex("Name");
                    int num = c.getColumnIndex("Number");
                    do {
                        tv += c.getString(name) + " " + c.getInt(num) + "\n";
                    } while (c.moveToNext());
                }
                TextView textView = findViewById(R.id.textView);
                textView.setText(tv);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onStop() {
        SharedPreferences save = getSharedPreferences("SAVE", 0);
        SharedPreferences.Editor editor = save.edit(); //создаём редактор shared preferences
        editor.putString("text", edit1.getText().toString()); //сохраняем текст из edit1
        editor.apply(); //применение редактирования shared preferences
        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.info) {
            AlertDialog.Builder dialog = new
                    AlertDialog.Builder(MainActivity.this);
            dialog.setMessage("Программа была выполнена Ивановым Даниилом БПИ182");
            dialog.setTitle("О программе");
            dialog.setNeutralButton("OK", new
                    DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            dialog.setIcon(R.mipmap.ic_launcher_round);
            AlertDialog alertDialog = dialog.create();
            alertDialog.show();
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }
}
