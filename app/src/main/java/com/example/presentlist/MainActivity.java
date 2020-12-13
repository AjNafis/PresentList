package com.example.presentlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView arrow = findViewById(R.id.imageView);
        EditText dateTime = findViewById(R.id.et1);
        EditText tName = findViewById(R.id.et2);
        EditText tDetails = findViewById(R.id.et3);
        Button save = findViewById(R.id.saveBtn);

        //Handles arrow clicks on both orientations.
        Intent goToShowListScreen = new Intent(this, ShowList.class);
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(goToShowListScreen);
            }
        });








    }


    //------------------------CODE FOR SHOWING THE MENU AND DIALOUGE----------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.et_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.tasklist:

                Intent goToShowListScreen = new Intent(this, ShowList.class);
                startActivity(goToShowListScreen);
                return true;


            case R.id.taskinputscreen:
                Toast.makeText(this, "You are currently on the task input screen", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }


}