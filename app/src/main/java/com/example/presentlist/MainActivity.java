package com.example.presentlist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView arrow = findViewById(R.id.imageView);
        EditText tdate = findViewById(R.id.et1);
        EditText tName = findViewById(R.id.et2);
        EditText tDetails = findViewById(R.id.et3);
        Button save = findViewById(R.id.saveBtn);

        //Creating database
        SQLiteDatabase db = openOrCreateDatabase("TodoListDb",MODE_PRIVATE,null);
        //Remove table if exists, so that we can create it again.
        db.execSQL("DROP TABLE IF EXISTS listData");
        //Creating table
        db.execSQL("CREATE TABLE listData(" +
                "date varchar(32)," +
                "name varchar(255)," +
                "details varchar(255))");


        //Handles arrow clicks on both orientations.
        Intent goToShowListScreen = new Intent(this, ShowList.class);
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String results = null;

                Cursor cr = db.rawQuery("SELECT * FROM listData",null);
                if(cr.moveToFirst()) {
                    do {
                        for (int i = 0; i < cr.getColumnCount(); i++) {
                            results += cr.getString(i) + " ";
                            results += "\n";
                        }
                    }
                    while (cr.moveToNext());
                }
                cr.close();

                tDetails.setText(results);
                startActivity(goToShowListScreen);
            }
        });

        //Getting todays date from phone.
        Calendar current = Calendar.getInstance();
        int y = current.get(Calendar.YEAR);
        int m = current.get(Calendar.MONTH);
        int d = current.get(Calendar.DAY_OF_MONTH);
        //Setting tdate to todays tdate.
        tdate.setText(y + "-" + m + "-" + d);

        //Created a DatePicker dialog in order to get tdate from user more conveniently.
        tdate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    //Doing this to hide the keyboard
                    tdate.clearFocus();

                    //This opens up the calender Datepicker Dialog.
                    DatePickerDialog sDatePickerDialog;
                    DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            //This sets the edit text box with the selected tdate
                            tdate.setText(dayOfMonth + " / " + month + " / " + year);
                        }
                    };

                    sDatePickerDialog = new DatePickerDialog(MainActivity.this,listener,y,m,d);
                    sDatePickerDialog.show();
                }
            }
        });



        //Handle Save button press.
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(tName.getText().toString().isEmpty()){
                    tName.setError("Enter Task Name");

                    if(tDetails.getText().toString().isEmpty()){
                        tDetails.setError("Enter Task Details");
                    }
                }
                else if (tDetails.getText().toString().isEmpty()){
                    tDetails.setError("Enter Task Details");
                }
                else {

                    ContentValues values = new ContentValues();
                    values.put("date",tdate.getText().toString());
                    values.put("name",tName.getText().toString());
                    values.put("details",tDetails.getText().toString());

                    db.insert("listData",null,values);

                    String results = null;

                    Cursor cr = db.rawQuery("SELECT * FROM listData",null);
                    if(cr.moveToFirst()) {
                        do {
                            for (int i = 0; i < cr.getColumnCount(); i++) {
                                results += cr.getString(i) + " ";
                            }
                            results += "\n";
                        }
                        while (cr.moveToNext());
                    }
                    cr.close();

                    tdate.setText(y + "-" + m + "-" + d);
                    tName.setText("");
                    tDetails.setText("");

                    ((TextView)findViewById(R.id.TV1)).setText(results);
                }
            }
        });


    }



    //Creates options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.et_menu, menu);
        return true;
    }
    //Handles options menu interactions. Send to previous page.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.tasklist:

                Intent goToShowListScreen = new Intent(this, ShowList.class);
                startActivity(goToShowListScreen);
                return true;


            case R.id.taskinputscreen:
                Toast.makeText(this, "You are currently on the task input screen", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }


}