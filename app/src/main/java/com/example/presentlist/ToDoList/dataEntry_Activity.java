package com.example.presentlist.ToDoList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.presentlist.R;

import java.util.Calendar;

public class dataEntry_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list_entry);

        //This creates the back button on the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Task Entry");

        ImageView arrow = findViewById(R.id.arrowImageView);
        EditText tdate = findViewById(R.id.ET1);
        EditText tName = findViewById(R.id.ET2);
        EditText tDetails = findViewById(R.id.ET3);
        Button save = findViewById(R.id.saveBtn);
        TextView showTaskListTV = findViewById(R.id.showTaskListTV);
        ConstraintLayout cLayout = findViewById(R.id.cLayout);
        String [] months = {"Jan","Feb","March","April","May","June","July","Aug","Sep","Oct","Nov","Dec"};



        //Handles arrow clicks on both orientations.
        arrow.setOnClickListener(v -> {
            Intent goToShowListScreen = new Intent(dataEntry_Activity.this, showData_Activity.class);
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

                sDatePickerDialog = new DatePickerDialog(dataEntry_Activity.this,listener,y,m,d);
                sDatePickerDialog.show();
            }
        });

        //This hides the keyboard when not on focus.
        cLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) dataEntry_Activity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                //Find the currently focused view, so we can grab the correct window token from it.
                View view = dataEntry_Activity.this.getCurrentFocus();
                //If no view currently has focus, create a new one, just so we can grab a window token from it
                if (view == null) {
                    view = new View(dataEntry_Activity.this);
                }
                else{
                    view.clearFocus();
                }
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        //Creating database
        SQLiteDatabase db = openOrCreateDatabase("TodoListDb",MODE_PRIVATE,null);

        //Remove table if exists, so that we can create it again.
        //db.execSQL("DROP TABLE IF EXISTS listData");

        //Create table if it does not exist already. TABLE NAME IS USE IN MULTIPLE PLACES,
        //THEY WILL NOT ALL CHANGE if YOU JUST CHANGE IT HERE.
        db.execSQL("CREATE TABLE IF NOT EXISTS ToDoListDataTable(" +
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "date varchar(32)," +
                "name varchar(255)," +
                "details varchar(255))");


        //Handle Save button press.
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Checks to make sure that all EditText fields have data.
                boolean flag1 = false, flag2 = false;
                flag1 = !tName.getText().toString().isEmpty();
                flag2 = !tDetails.getText().toString().isEmpty();

                if (flag1 && flag2){
                    AlertDialog.Builder builder = new AlertDialog.Builder(dataEntry_Activity.this);

                    builder.setTitle("Please Confirm");
                    builder.setMessage( "Date: " + tdate.getText().toString().trim() + "\n" +
                            "Task Name: " + tName.getText().toString().trim() + "\n" +
                            "Task Details: " + tDetails.getText().toString().trim() + "\n\n" +
                            "Is this correct?");

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            ContentValues values = new ContentValues();
                            values.put("date",tdate.getText().toString().trim());
                            values.put("name",tName.getText().toString().trim());
                            values.put("details",tDetails.getText().toString().trim());

                            //Insert data into the data base, I used ContentValues class to help me
                            //put the data into the the row, regula SQL code was not accepting strings as input.
                            db.insert("ToDoListDataTable",null,values);

                            tdate.setText(d + "-" + months[m] + "-" + y);
                            tName.setText("");
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
                    if (!flag1) tName.setError("Enter Task Name");
                    if (!flag2) tDetails.setError("Enter Task Details");
                }
            }
        });

        //Handle click on showTaskList TextView
        showTaskListTV.setOnClickListener(new View.OnClickListener() {
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
                Toast.makeText(this, "You are currently on the task input screen", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

}