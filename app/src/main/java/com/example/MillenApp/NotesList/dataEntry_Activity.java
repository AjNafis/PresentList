package com.example.MillenApp.NotesList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.MillenApp.R;
import com.example.MillenApp.ToDoList.showData_Activity;

import java.util.ArrayList;
import java.util.Calendar;

public class dataEntry_Activity extends AppCompatActivity {

    @Override
    protected void onStop(){
        arrow.clearColorFilter();
        super.onStop();
    }

    //Arrow controls activity change.
    ImageView arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_data_entry);

        //This creates the back button on the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Enter Note");

        arrow = findViewById(R.id.arrowImageView);
        EditText tdate = findViewById(R.id.dateET);
        AutoCompleteTextView tType = findViewById(R.id.noteTypeET);
        EditText tDetails = findViewById(R.id.noteDetailsET);
        Button save = findViewById(R.id.saveBtn);
        TextView showListTV = findViewById(R.id.showListTV);
        ConstraintLayout cLayout = findViewById(R.id.cLayout);
        String [] months = {"Jan","Feb","March","April","May","June","July","Aug","Sep","Oct","Nov","Dec"};

        //Handles arrow clicks on both orientations.
        arrow.setOnClickListener(v -> {
            arrow.setColorFilter(Color.RED);
            Intent goToShowListScreen = new Intent(com.example.MillenApp.NotesList.dataEntry_Activity.this, com.example.MillenApp.NotesList.showData_Activity.class);
            startActivity(goToShowListScreen);
        });

        //Getting todays date from phone.
        Calendar current = Calendar.getInstance();
        int y = current.get(Calendar.YEAR);
        int m = current.get(Calendar.MONTH);
        int d = current.get(Calendar.DAY_OF_MONTH);

        //Setting tdate to todays tdate.
        tdate.setText(d + "-" + months[m] + "-" + y);

        //Created a DatePicker dialog in order to get tdate from user more conveniently.
        tdate.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus){
                //Doing this to hide the keyboard
                tdate.clearFocus();

                //This opens up the calender Datepicker Dialog.
                DatePickerDialog sDatePickerDialog;
                DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //This sets the edit text box with the selected tdate
                        tdate.setText(dayOfMonth + "-" + months[month] + "-" + year);
                    }
                };

                sDatePickerDialog = new DatePickerDialog(com.example.MillenApp.NotesList.dataEntry_Activity.this,listener,y,m,d);
                sDatePickerDialog.show();
            }
        });

        //This hides the keyboard when not on focus.
        cLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) com.example.MillenApp.NotesList.dataEntry_Activity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                //Find the currently focused view, so we can grab the correct window token from it.
                View view = com.example.MillenApp.NotesList.dataEntry_Activity.this.getCurrentFocus();
                //If no view currently has focus, create a new one, just so we can grab a window token from it
                if (view == null) {
                    view = new View(com.example.MillenApp.NotesList.dataEntry_Activity.this);
                }
                else{
                    view.clearFocus();
                }
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        //Creating database
        SQLiteDatabase db = openOrCreateDatabase("NotesListDb",MODE_PRIVATE,null);
        //Create table if it does not exist already.
        db.execSQL("CREATE TABLE IF NOT EXISTS NotesListDataTable(" +
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "date varchar(32)," +
                "type varchar(255)," +
                "details varchar(255))");

        // Cursor/array will store unique Note Types. I will then use this to show suggestions when the user typing in the results.
        Cursor cr = db.rawQuery("SELECT DISTINCT type FROM NotesListDataTable",null);

        String [] tType_array = new String [cr.getCount()];
        int i = 0;
        if(cr.moveToFirst()) {
            do {
                tType_array[i] = cr.getString(0);
                i++;
            }
            while (cr.moveToNext());
        }
        cr.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tType_array);
        tType.setAdapter(adapter);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Checks to make sure that all EditText fields have data.
                boolean flag1 = false, flag2 = false;
                flag1 = !tType.getText().toString().isEmpty();
                flag2 = !tDetails.getText().toString().isEmpty();

                if (flag1 && flag2){
                    AlertDialog.Builder builder = new AlertDialog.Builder(com.example.MillenApp.NotesList.dataEntry_Activity.this);

                    builder.setTitle("Please Confirm");
                    builder.setMessage( "Date: " + tdate.getText().toString().trim() + "\n" +
                            "Note Type: " + tType.getText().toString().trim() + "\n" +
                            "Note Details: " + tDetails.getText().toString().trim() + "\n\n" +
                            "Is this correct?");

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            ContentValues values = new ContentValues();
                            values.put("date",tdate.getText().toString().trim());
                            values.put("type",tType.getText().toString().trim());
                            values.put("details",tDetails.getText().toString().trim());

                            //Insert data into the data base, I used ContentValues class to help me
                            //put the data into the the row, regula SQL code was not accepting strings as input.
                            try {
                                db.insert("NotesListDataTable",null,values);
                                Toast.makeText(com.example.MillenApp.NotesList.dataEntry_Activity.this, "Save Successful", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(com.example.MillenApp.NotesList.dataEntry_Activity.this, "Save Failed", Toast.LENGTH_SHORT).show();
                            }

                            tdate.setText(d + "-" + months[m] + "-" + y);
                            tType.setText("");
                            tDetails.setText("");

                            dialog.dismiss();
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else {
                    if (!flag1) tType.setError("Enter Note Type");
                    if (!flag2) tDetails.setError("Enter Note Details");
                }
            }
        });

        //Handle click on showTaskList TextView
        showListTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrow.performClick();
            }
        });


    }

    //This handles the back button on the action bar.
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
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

                ImageView arrow = findViewById(R.id.arrowImageView);
                arrow.performClick();
                return true;

            case R.id.taskinputscreen:
                Toast.makeText(this, "You are currently on the note entry screen", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.goToMainMenu:
                finish();

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}