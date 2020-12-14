package com.example.presentlist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ShowList extends AppCompatActivity {

    ArrayList<String> listData_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Task List");

        Intent intent = this.getIntent();
        String _listData = intent.getStringExtra("ListData");

        ListView listView = findViewById(R.id.listView);

        listData_list = new ArrayList<>();
        ArrayAdapter adapter = new ArrayAdapter(ShowList.this,android.R.layout.simple_list_item_1, listData_list);

        listView.setAdapter(adapter);

        String[] str = _listData.split("\n");

        if (!(_listData.equals("No data to show") || _listData == null)) {
            for (String data : str){

                ListData listData = new ListData();
                String [] _str = data.split(" ");
                listData.Date = _str[0];
                listData.Name = _str[1];
                listData.Details = _str [2];


                listData_list.add(  "\nDate: " + listData.Date + "\n\n" +
                                    "Task Name: " + listData.Name + "\n" +
                                    "Task Details: " + listData.Details + "\n");
            }
        } else {
            listData_list.add("No data to show");
        }
        adapter.notifyDataSetChanged();

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