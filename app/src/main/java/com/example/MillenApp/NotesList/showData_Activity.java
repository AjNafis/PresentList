package com.example.MillenApp.NotesList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.MillenApp.R;

import java.util.ArrayList;

public class showData_Activity extends AppCompatActivity {

    ListDataAdapter adapter;
    SQLiteDatabase db;
    ArrayList<DataObj> dataObjArrayList;
    RecyclerView rvNotes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_notes_data);


        //This creates the back button on the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Notes");

        //Opening database - this is safe and we do not need to create table since the only way
        //to get to this activity is throught he main activity and the main activity will always
        //check and create a table.
        db = openOrCreateDatabase("NotesListDb",MODE_PRIVATE,null);

        refreshAdapter();


    }

    @Override
    protected void onResume(){
        super.onResume();
        View mainView = findViewById(R.id.rvNotes);
        mainView.setAlpha(1);
        getSupportActionBar().setTitle("Notes");

        if (adapter != null){
            refreshAdapter();
        }

    }



    public void refreshAdapter() {
        //We will store the entire table data into our custom built object arrayList.
        dataObjArrayList = new ArrayList<>();

        Cursor cr = db.rawQuery("SELECT * FROM NotesListDataTable ORDER BY type",null);
        if(cr.moveToFirst()) {
            do {
                ArrayList<String> row = new ArrayList<>();

                for (int i = 0; i < cr.getColumnCount(); i++) {
                    row.add(cr.getString(i));
                }
                DataObj dataObj = new DataObj(row);
                dataObjArrayList.add(dataObj);
            }
            while (cr.moveToNext());
        }
        cr.close();

        adapter = new ListDataAdapter(dataObjArrayList,this);

        rvNotes = findViewById(R.id.rvNotes);

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
        inflater.inflate(R.menu.actionbar_options_menu, menu);
        return true;
    }
    //Handles options menu interactions. Send to previous page.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



        switch (item.getItemId()){

            case R.id.taskinputscreen:

            case R.id.addEntryButton:

               // View goToInputScreenBtn = findViewById(R.id.taskinputscreen);
                //goToInputScreenBtn.setVisibility(View.INVISIBLE);

                Fragment mFragment = new dataEntry_Fragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(R.id.frameForFragment, mFragment).commit();

                return true;

            case R.id.goToMainMenu:
                startActivity(new Intent(this, com.example.MillenApp.MainMenu.mainMenu_Activity.class));
                finish();

                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.showList).setVisible(false);
        return true;
    }

}