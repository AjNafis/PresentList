package com.example.MillenApp.ToDoList;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.MillenApp.R;

import java.util.ArrayList;

public class showData_Activity extends AppCompatActivity {
//


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_todolist);

        ListView listView = findViewById(R.id.listView);

        //This creates the back button on the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Task List");

        //Opening database - this is safe and we do not need to create table since the only way
        //to get to this activity is throught he main activity and the main activity will always
        //check and create a table.
        SQLiteDatabase db = openOrCreateDatabase("TodoListDb",MODE_PRIVATE,null);

        //We will store the entire table data into our custom built object arrayList.
        ArrayList<DataObj> dataObjArrayList= new ArrayList<>();

        Cursor cr = db.rawQuery("SELECT * FROM ToDoListDataTable ORDER BY name",null);
        int counter = 0;
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

        //This list view help to perform string manipulation
        ArrayList<String> listView_list = new ArrayList<>();

        //Setting up adapter to send ArrayList data to listView
        ArrayAdapter adapter = new ArrayAdapter(showData_Activity.this,android.R.layout.simple_list_item_1, listView_list);
        listView.setAdapter(adapter);

        if (dataObjArrayList.size() != 0) {
            for (DataObj data : dataObjArrayList){
                listView_list.add(      "\nDate: " + data.Date + "\n\n" +
                                        "Task Name: " + data.Name + "\n" +
                                        "Task Details: " + data.Details + "\n");
            }
        }
        else {
            listView_list.add("No data to show");
        }
        adapter.notifyDataSetChanged();


        //Creates popupmenu when you click on listViewItem
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PopupMenu popup = new PopupMenu(showData_Activity.this, view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.showlist_onclicklistener, popup.getMenu());
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.delete:

                                int ID = Integer.parseInt(dataObjArrayList.get(position).ID);

                                db.delete("ToDoListDataTable", "id" + "=" + ID , null);
                                startActivity(new Intent(showData_Activity.this, showData_Activity.class));
                                finish();

                        }

                        return false;
                    }
                });
            }
        });

        //Creates popupmenu when you click on listViewItem  -Just testing - So far does nothing.
        listView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popup = new PopupMenu(showData_Activity.this, v);
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

            case R.id.goToMainMenu:
                startActivity(new Intent(this, com.example.MillenApp.MainMenu.mainMenu_Activity.class));

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}