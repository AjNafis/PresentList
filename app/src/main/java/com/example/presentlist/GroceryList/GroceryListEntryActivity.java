package com.example.presentlist.GroceryList;

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

public class GroceryListEntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list_entry);

        //This creates the back button on the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Task Entry");

        //Need to fix naming conflict with layout from activity_list_entry
        EditText tItemName = findViewById(R.id.ET1);
        EditText tBestBeforeDate = findViewById(R.id.ET2);
        EditText tCategory = findViewById(R.id.ET3);

        EditText tQty = findViewById(R.id.ET1_qty);
        ImageView arrow = findViewById(R.id.arrowImageView);
        Button save = findViewById(R.id.saveBtn);
        TextView showTaskListTV = findViewById(R.id.showTaskListTV);
        ConstraintLayout cLayout = findViewById(R.id.cLayout);
        String [] months = {"Jan","Feb","March","April","May","June","July","Aug","Sep","Oct","Nov","Dec"};

        //Creating database
        SQLiteDatabase db = openOrCreateDatabase("GroceryListDb",MODE_PRIVATE,null);

        //Remove table if exists, so that we can create it again.
        //db.execSQL("DROP TABLE IF EXISTS groceryListData");

        //Create table if it does not exist already.
        db.execSQL("CREATE TABLE IF NOT EXISTS GroceryListDataTable(" +
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "name varchar(32)," +
                "quantity varchar(32)," +
                "bestbeforedate varchar(255)," +
                "category varchar(255))");

        //Handles arrow clicks on both orientations.
        arrow.setOnClickListener(v -> {
            Intent goToShowListScreen = new Intent(GroceryListEntryActivity.this, ShowGroceryListActivity.class);
            startActivity(goToShowListScreen);
        });

        //Getting todays date from phone.
        Calendar current = Calendar.getInstance();
        int y = current.get(Calendar.YEAR);
        int m = current.get(Calendar.MONTH);
        int d = current.get(Calendar.DAY_OF_MONTH);


        //Created a DatePicker dialog in order to get tItemName from user more conveniently.
        tBestBeforeDate.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus){
                //Doing this to hide the keyboard
                tBestBeforeDate.clearFocus();

                //This opens up the calender Datepicker Dialog.
                DatePickerDialog sDatePickerDialog;
                DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //This sets the edit text box with the selected tItemName
                        tBestBeforeDate.setText(dayOfMonth + "-" + months[month] + "-" + year);
                        tCategory.requestFocus();
                    }
                };

                sDatePickerDialog = new DatePickerDialog(GroceryListEntryActivity.this,listener,y,m,d);
                sDatePickerDialog.show();
            }
        });

        //This hides the keyboard when not on focus.
        cLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) GroceryListEntryActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                //Find the currently focused view, so we can grab the correct window token from it.
                View view = GroceryListEntryActivity.this.getCurrentFocus();
                //If no view currently has focus, create a new one, just so we can grab a window token from it
                if (view == null) {
                    view = new View(GroceryListEntryActivity.this);
                }
                else{
                    view.clearFocus();
                }
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        //Handle Save button press.
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Checks to make sure that all EditText fields have data.
                boolean flag1 = false, flag2 = false, flag3 = false;
                flag1 = !tItemName.getText().toString().isEmpty();  //  Needs to be tested
                flag2 = !tQty.getText().toString().isEmpty();       //          ^
                flag3 = !tCategory.getText().toString().isEmpty();  //          ^

                if (flag1 && flag2 && flag3){

                    AlertDialog.Builder builder = new AlertDialog.Builder(GroceryListEntryActivity.this);

                    builder.setTitle("Please Confirm");
                    builder.setMessage( "Item Name : " + tItemName.getText().toString().trim() + "\n" +
                                        "Quantity : " + tQty.getText().toString().trim() + "\n" +
                                        "Best Before : " + tBestBeforeDate.getText().toString().trim() + "\n" +
                                        "Category : " + tCategory.getText().toString().trim() + "\n\n" +
                                        "Is this correct?");

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            ContentValues values = new ContentValues();
                            values.put("name",tItemName.getText().toString().trim());
                            values.put("quantity",tQty.getText().toString().trim());
                            values.put("bestbeforedate",tBestBeforeDate.getText().toString().trim());
                            values.put("category",tCategory.getText().toString().trim());

                            //Insert data into the data base, i used ContentValues class to help me
                            //put the data into the the row, regula SQL code was not accepting strings as input.
                            db.insert("GroceryListDataTable",null,values);

                            tItemName.setText("");
                            tQty.setText("");
                            tBestBeforeDate.setText("");
                            tCategory.setText("");

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
                    if (!flag1) tItemName.setError("Enter Item Name");
                    if (!flag2) tQty.setError("Enter Quantity");
                    if (!flag3) tCategory.setError("Enter Category");
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