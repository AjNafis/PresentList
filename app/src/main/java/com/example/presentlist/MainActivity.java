package com.example.presentlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onStop() {
        ProgressBar progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.INVISIBLE);
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button toDoListRoute = findViewById(R.id.taskRouteList);
        Button groceryListRoute = findViewById(R.id.groceryRouteList);
        ProgressBar progressBar = findViewById(R.id.progressBar);

        //Creating database for To Do List
        SQLiteDatabase TodoListDb = openOrCreateDatabase("TodoListDb",MODE_PRIVATE,null);

        //Remove table if exists, so that we can create it again.
        //TodoListDb.execSQL("DROP TABLE IF EXISTS ToDoListDataTable");

        //Create table if it does not exist already.
        TodoListDb.execSQL("CREATE TABLE IF NOT EXISTS ToDoListDataTable(" +
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "date varchar(32)," +
                "name varchar(255)," +
                "details varchar(255))");


        //Creating database for Grocery List
        SQLiteDatabase GroceryListDb = openOrCreateDatabase("GroceryListDb",MODE_PRIVATE,null);

        //Remove table if exists, so that we can create it again.
        //GroceryListDb.execSQL("DROP TABLE IF EXISTS GroceryListDataTable");

        //Create table if it does not exist already.
        GroceryListDb.execSQL("CREATE TABLE IF NOT EXISTS GroceryListDataTable(" +
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "name varchar(32)," +
                "quantity varchar(32)," +
                "bestbeforedate varchar(255)," +
                "category varchar(255))");

        //Handle button click
        toDoListRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                startActivity(new Intent(MainActivity.this, ToDoListEntryActivity.class));
            }
        });

        //Handle button click
        groceryListRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                startActivity(new Intent(MainActivity.this, GroceryListEntryActivity.class));
            }
        });

    }







}