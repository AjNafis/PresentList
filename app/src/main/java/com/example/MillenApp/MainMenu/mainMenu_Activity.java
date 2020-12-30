package com.example.MillenApp.MainMenu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.MillenApp.R;

import java.util.ArrayList;

public class mainMenu_Activity extends AppCompatActivity {

    //We are doing this to keep track of how many rows are present in the database for each individual list.
    ArrayList<String> totalRows = new ArrayList<>();

    //Initializing Database
    SQLiteDatabase TodoListDb;
    SQLiteDatabase GroceryListDb;
    SQLiteDatabase NotesListDb;

    TextView summaryView;
    String summary;

    protected void onResume() {
        setDataSummary();
        super.onResume();
    }

    @Override
    protected void onStop() {
        ProgressBar progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.INVISIBLE);

        summary = "";
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        ProgressBar progressBar = findViewById(R.id.progressBar);

        //Creating/Opening database for To Do List
        TodoListDb = openOrCreateDatabase("TodoListDb",MODE_PRIVATE,null);
        //Create table if it does not exist already.
        TodoListDb.execSQL("CREATE TABLE IF NOT EXISTS ToDoListDataTable(" +
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "date varchar(32)," +
                "name varchar(255)," +
                "details varchar(255))");


        //Handle button click
        Button toDoListRoute = findViewById(R.id.toDoRouteList);
        toDoListRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                startActivity(new Intent(mainMenu_Activity.this, com.example.MillenApp.ToDoList.dataEntry_Activity.class));
            }
        });

        //Creating/Opening  database for Grocery List
        GroceryListDb = openOrCreateDatabase("GroceryListDb",MODE_PRIVATE,null);
        //Create table if it does not exist already.
        GroceryListDb.execSQL("CREATE TABLE IF NOT EXISTS GroceryListDataTable(" +
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "name varchar(32)," +
                "quantity varchar(32)," +
                "bestbeforedate varchar(255)," +
                "category varchar(255))");


        //Handle button click
        Button groceryListRoute = findViewById(R.id.groceryRouteList);
        groceryListRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                startActivity(new Intent(mainMenu_Activity.this, com.example.MillenApp.GroceryList.dataEntry_Activity.class));
            }
        });

        //Creating/opening database for Grocery List
        NotesListDb = openOrCreateDatabase("NotesListDb",MODE_PRIVATE,null);
        //Create table if it does not exist already.
        NotesListDb.execSQL("CREATE TABLE IF NOT EXISTS NotesListDataTable(" +
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "date varchar(32)," +
                "type varchar(255)," +
                "details varchar(255))");

        //Handle button click
        Button notesListRoute = findViewById(R.id.notesRouteList);
        notesListRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                startActivity(new Intent(mainMenu_Activity.this, com.example.MillenApp.NotesList.dataEntry_Activity.class));
            }
        });

        //Calling function to get/set DataSummary.
        setDataSummary();

    }


    public void setDataSummary(){

        totalRows.clear();
        //We are doing this to keep track of how many rows are present in the database for each individual list.
        Cursor cr = TodoListDb.rawQuery("SELECT COUNT(ID) FROM ToDoListDataTable",null);
        cr.moveToFirst();
        totalRows.add("You have " + cr.getString(0) + " ToDo items," );

        cr = GroceryListDb.rawQuery("SELECT COUNT(*) FROM GroceryListDataTable",null);
        cr.moveToFirst();
        totalRows.add("You have " + cr.getString(0) + " Grocery items," );

        cr = NotesListDb.rawQuery("SELECT COUNT(*) FROM NotesListDataTable",null);
        cr.moveToFirst();
        totalRows.add("You have " + cr.getString(0) + " Notes." );

        cr.close();
        summaryView = findViewById(R.id.summaryTV);
        summary = "";
        for (String line : totalRows){
            summary += line ;
            summary += "\n";
        }
        summaryView.setText(summary);

    }







}