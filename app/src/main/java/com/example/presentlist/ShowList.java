package com.example.presentlist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.Notification;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ShowList extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);

        ListView listView = findViewById(R.id.listView);

        //This creates the back button on the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Task List");

        //Opening database - this is safe and we do not need to create table since the only way
        //to get to this activity is throught he main activity and the main activity will always
        //check and create a table.
        SQLiteDatabase db = openOrCreateDatabase("TodoListDb",MODE_PRIVATE,null);

        // We will store table data into this list
        ArrayList<String> listData_list = new ArrayList<>();

        //Moving data from the table to a list.
        String tableRow = "";
        Cursor cr = db.rawQuery("SELECT * FROM listData",null);
        if(cr.moveToFirst()) {
            do {
                for (int i = 0; i < cr.getColumnCount(); i++) {
                    tableRow += cr.getString(i) + " ";
                }
                listData_list.add(tableRow);
                tableRow = "";
            }
            while (cr.moveToNext());
        }
        cr.close();

        //We are creating 2 lists so that we can keep original data intact while we perform string manipulation on this list.
        ArrayList<String> listView_list = new ArrayList<>();

        //Setting up adapter to send ArrayList data to listView
        ArrayAdapter adapter = new ArrayAdapter(ShowList.this,android.R.layout.simple_list_item_1, listView_list);
        listView.setAdapter(adapter);

        String [] _str;
        if (!tableRow.equals(" ")) {
            for (String data : listData_list){

                ListData listData = new ListData();
                _str = data.split(" ");
                listData.ID = _str[0];
                listData.Date = _str[1];
                listData.Name = _str[2];
                listData.Details = _str [3];


                listView_list.add(  "\nDate: " + listData.Date + "\n\n" +
                                    "Task Name: " + listData.Name + "\n" +
                                    "Task Details: " + listData.Details + "\n");
            }
        } else {
            listView_list.add("No data to show");
        }
        adapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PopupMenu popup = new PopupMenu(ShowList.this, view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.showlist_onclicklistener, popup.getMenu());
                popup.show();
            }
        });

        listView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popup = new PopupMenu(ShowList.this, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.et_menu, popup.getMenu());
                popup.show();

                return false;
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
                Toast.makeText(this, "You are currently on the task list screen", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.taskinputscreen:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}