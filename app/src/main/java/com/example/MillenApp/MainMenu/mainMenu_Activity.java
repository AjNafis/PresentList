package com.example.MillenApp.MainMenu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.MillenApp.R;

import java.util.ArrayList;

public class mainMenu_Activity extends AppCompatActivity {

    //We are doing this to keep track of how many rows are present in the database for each individual list.
    ArrayList<String> totalRows = new ArrayList<>();

    //Initializing Database
    SQLiteDatabase TodoListDb;
    SQLiteDatabase GroceryListDb;
    SQLiteDatabase NotesListDb;

    ProgressBar progressBar;
    TextView summaryView;
    String summary;

    Button toDoListRoute;
    Button groceryListRoute;
    Button notesListRoute;



    protected void onResume() {
        super.onResume();
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        setDataSummary();
        buttonAnimation();


    }

    @Override
    protected void onPause() {
        super.onPause();
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        summary = "";

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        progressBar = findViewById(R.id.progressBar);

        HorizontalScrollView hor = findViewById(R.id.horizontalScrollView);
        hor.clearAnimation();
        hor.clearFocus();
        //Func will create or open all tables.
        createOrOpenTables();

        // This handles all available button click.
        buttonClickHandlers();

        //Calling function to get/set DataSummary.
        setDataSummary();

    }


    public void setDataSummary(){

        totalRows.clear();
        //We are doing this to keep track of how many rows are present in the database for each individual list.
        Cursor cr = TodoListDb.rawQuery("SELECT COUNT(ID) FROM ToDoListDataTable",null);
        cr.moveToFirst();
        totalRows.add("You have " + cr.getString(0) + (Integer.parseInt(cr.getString(0)) != 1 ? " tasks" : " task" ) + " left To Do, ");

        cr = GroceryListDb.rawQuery("SELECT COUNT(*) FROM GroceryListDataTable",null);
        cr.moveToFirst();
        totalRows.add("You have " + cr.getString(0) + (Integer.parseInt(cr.getString(0)) != 1 ? " items" : " item") + " left in your Grocery checklist,");

        cr = NotesListDb.rawQuery("SELECT COUNT(*) FROM NotesListDataTable",null);
        cr.moveToFirst();
        totalRows.add("You have created " + cr.getString(0) + (Integer.parseInt(cr.getString(0)) > 1 ? " Notes" : " Note") + " so far. ") ;

        cr.close();
        summaryView = findViewById(R.id.summaryTV);
        summary = "";
        for (String line : totalRows){
            summary += line ;
            summary += "\n";
        }
        summaryView.setText(summary);

    }

    public void createOrOpenTables(){

        //Creating/Opening database for To Do List
        TodoListDb = openOrCreateDatabase("TodoListDb",MODE_PRIVATE,null);
        //Create table if it does not exist already.
        TodoListDb.execSQL("CREATE TABLE IF NOT EXISTS ToDoListDataTable(" +
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "date varchar(32)," +
                "name varchar(255)," +
                "details varchar(255))");

        //Creating/Opening  database for Grocery List
        GroceryListDb = openOrCreateDatabase("GroceryListDb",MODE_PRIVATE,null);
        //Create table if it does not exist already.
        GroceryListDb.execSQL("CREATE TABLE IF NOT EXISTS GroceryListDataTable(" +
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "name varchar(32)," +
                "quantity varchar(32)," +
                "bestbeforedate varchar(255)," +
                "category varchar(255))");

        //Creating/opening database for Grocery List
        NotesListDb = openOrCreateDatabase("NotesListDb",MODE_PRIVATE,null);
        //Create table if it does not exist already.
        NotesListDb.execSQL("CREATE TABLE IF NOT EXISTS NotesListDataTable(" +
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "date varchar(32)," +
                "type varchar(255)," +
                "details varchar(255))");


    }

    public void buttonClickHandlers() {

        toDoListRoute = findViewById(R.id.toDoRouteList);
        toDoListRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                startActivity(new Intent(mainMenu_Activity.this, com.example.MillenApp.ToDoList.dataEntry_Activity.class));
            }
        });

        groceryListRoute = findViewById(R.id.groceryRouteList);
        groceryListRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                startActivity(new Intent(mainMenu_Activity.this, com.example.MillenApp.GroceryList.dataEntry_Activity.class));
            }
        });

        notesListRoute = findViewById(R.id.notesRouteList);
        notesListRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                startActivity(new Intent(mainMenu_Activity.this, com.example.MillenApp.NotesList.dataEntry_Activity.class));
            }
        });

        Button thingsToBuyRoute = findViewById(R.id.thingsToBuyRouteList);
        thingsToBuyRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mainMenu_Activity.this, "Feature unavailable", Toast.LENGTH_SHORT).show();
            }
        });

        Button workOutRoute = findViewById(R.id.workOutRouteList);
        workOutRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mainMenu_Activity.this, "Feature unavailable", Toast.LENGTH_SHORT).show();
            }
        });

        Button movies2watchRoute = findViewById(R.id.movieRouteList);
        movies2watchRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mainMenu_Activity.this, "Feature unavailable", Toast.LENGTH_SHORT).show();
            }
        });

        Button expensesRoute = findViewById(R.id.expensesRouteList);
        expensesRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mainMenu_Activity.this, "Feature unavailable", Toast.LENGTH_SHORT).show();
            }
        });

        Button stockProtfolioRoute = findViewById(R.id.stockRouteList);
        stockProtfolioRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mainMenu_Activity.this, "Feature unavailable", Toast.LENGTH_SHORT).show();
            }
        });


    }

    //I created a small animation to focus the users attention towards the buttons.
    public void buttonAnimation(){

        final Handler handler = new Handler(Looper.getMainLooper());

        int delayMillis = 200;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                buttonAnimationhelper(toDoListRoute,toDoListRoute);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            buttonAnimationhelper(toDoListRoute,notesListRoute);
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    buttonAnimationhelper(notesListRoute,groceryListRoute);
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            buttonAnimationhelper(groceryListRoute,true);
                                        }
                                    }, delayMillis);
                                }
                            }, delayMillis);
                        }
                    }, delayMillis);
            }
        }, 3000);
    }
    public void buttonAnimationhelper (Button Btn, Button nxtBtn ) {

        Btn.clearFocus();
        Btn.setFocusableInTouchMode(false);
        nxtBtn.setFocusableInTouchMode(true);
        nxtBtn.requestFocus();
    }
    public void buttonAnimationhelper (Button Btn, boolean isLast ) {
        if (isLast)
        Btn.clearFocus();
        Btn.setFocusableInTouchMode(false);
    }


}