package com.example.MillenApp.NotesList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.MillenApp.R;

import java.util.ArrayList;

public class showData_Activity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_show_data);


        //This creates the back button on the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Notes");

        //Opening database - this is safe and we do not need to create table since the only way
        //to get to this activity is throught he main activity and the main activity will always
        //check and create a table.
        SQLiteDatabase db = openOrCreateDatabase("NotesListDb",MODE_PRIVATE,null);

        //We will store the entire table data into our custom built object arrayList.
        ArrayList<DataObj> dataObjArrayList = new ArrayList<>();

        Cursor cr = db.rawQuery("SELECT * FROM NotesListDataTable ORDER BY type",null);
        if(cr.moveToFirst()) {
            do {
                ArrayList<String> row = new ArrayList<>();

                for (int i = 0; i < cr.getColumnCount(); i++) {
                    row.add(cr.getString(i));
                }
                com.example.MillenApp.NotesList.DataObj dataObj = new com.example.MillenApp.NotesList.DataObj(row);
                dataObjArrayList.add(dataObj);
            }
            while (cr.moveToNext());
        }
        cr.close();

        ListDataAdapter adapter = new ListDataAdapter(dataObjArrayList);

        RecyclerView rvNotes = findViewById(R.id.rvNotes);

        rvNotes.setAdapter(adapter);

        rvNotes.setLayoutManager(new LinearLayoutManager(this));



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
                Toast.makeText(this, "You are currently on the task list screen", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.taskinputscreen:
                finish();
                return true;

            case R.id.goToMainMenu:
                startActivity(new Intent(this, com.example.MillenApp.MainMenu.mainMenu_Activity.class));

            default:
                return super.onOptionsItemSelected(item);

        }
    }

}