package com.example.MillenApp.NotesList;

import androidx.lifecycle.ViewModelProvider;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.MillenApp.R;

import java.util.Calendar;

public class dataEntry_Fragment extends Fragment {

    public static final int DURATION = 300;
    private DataEntryViewModel mViewModel;

    public static dataEntry_Fragment newInstance() {
        return new dataEntry_Fragment();
    }

    View recyclerView;
    View addBtn;
    View rootView;
    ArrayAdapter<String> tTypeAdapter;
    SQLiteDatabase db;
    AutoCompleteTextView tType;
    ImageButton exitBtn;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_data_entry, container, false);

        startRecyclerView_Animations();


        exitBtn = rootView.findViewById(R.id.exitBtn);
        EditText tdate = rootView.findViewById(R.id.dateET);
        tType = rootView.findViewById(R.id.noteTypeET);
        EditText tDetails = rootView.findViewById(R.id.noteDetailsET);
        Button save = rootView.findViewById(R.id.saveBtn);
//        ConstraintLayout cLayout = getActivity().findViewById(R.id.cLayout);
        String [] months = {"Jan","Feb","March","April","May","June","July","Aug","Sep","Oct","Nov","Dec"};

        //Handles arrow clicks on both orientations.
        exitBtn.setOnClickListener(v -> {

            fadeInAddBtn();

            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
            getActivity().getSupportFragmentManager().popBackStack();


        });



        //Getting todays date from phone.
        Calendar current = Calendar.getInstance();
        int y = current.get(Calendar.YEAR);
        int m = current.get(Calendar.MONTH);
        int d = current.get(Calendar.DAY_OF_MONTH);

        //Setting tdate to todays tdate.
        tdate.setText(d + "-" + months[m] + "-" + y);

        //Created a DatePicker dialog in order to get tdate from user more conveniently.
        tdate.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus){
                //Doing this to hide the keyboard
                tdate.clearFocus();

                //This opens up the calender Datepicker Dialog.
                DatePickerDialog sDatePickerDialog;
                DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //This sets the edit text box with the selected tdate
                        tdate.setText(dayOfMonth + "-" + months[month] + "-" + year);
                    }
                };

                sDatePickerDialog = new DatePickerDialog(rootView.getContext(),listener,y,m,d);
                sDatePickerDialog.show();
            }
        });

//        //This hides the keyboard when not on focus.
//        rootView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                InputMethodManager imm = (InputMethodManager) rootView.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
//                //Find the currently focused view, so we can grab the correct window token from it.
//                View view = rootView.getCurrentFocus();
//                //If no view currently has focus, create a new one, just so we can grab a window token from it
//                if (view == null) {
//                    view = new View(com.example.MillenApp.NotesList.dataEntry_Activity.this);
//                }
//                else{
//                    view.clearFocus();
//                }
//                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//            }
//        });

        //Creating database
        db = rootView.getContext().openOrCreateDatabase("NotesListDb",rootView.getContext().MODE_PRIVATE,null);
        //Create table if it does not exist already.
        db.execSQL("CREATE TABLE IF NOT EXISTS NotesListDataTable(" +
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "date varchar(32)," +
                "type varchar(255)," +
                "details varchar(255))");

        setAutoCompleteListAdapter();


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Checks to make sure that all EditText fields have data.
                boolean flag1 = false, flag2 = false;
                flag1 = !tType.getText().toString().isEmpty();
                flag2 = !tDetails.getText().toString().isEmpty();

                if (flag1 && flag2){
                    AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());

                    builder.setTitle("Please Confirm");
                    builder.setMessage( "Date: " + tdate.getText().toString().trim() + "\n" +
                            "Note Type: " + tType.getText().toString().trim() + "\n" +
                            "Note Details: " + tDetails.getText().toString().trim() + "\n\n" +
                            "Is this correct?");

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            ContentValues values = new ContentValues();
                            values.put("date",tdate.getText().toString().trim());
                            values.put("type",tType.getText().toString().trim());
                            values.put("details",tDetails.getText().toString().trim());

                            //Insert data into the data base, I used ContentValues class to help me
                            //put the data into the the row, regula SQL code was not accepting strings as input.
                            try {
                                db.insert("NotesListDataTable",null,values);
                                ((showData_Activity)getActivity()).refreshAdapter();

                                Toast.makeText(getActivity().getApplicationContext(), "Save Successful", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getActivity().getApplicationContext(), "Save Failed", Toast.LENGTH_SHORT).show();
                            }

                            tdate.setText(d + "-" + months[m] + "-" + y);
                            tType.setText("");
                            tDetails.setText("");

                            setAutoCompleteListAdapter();

                            recyclerView.requestFocus();

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
                    if (!flag1) tType.setError("Enter Note Type");
                    if (!flag2) tDetails.setError("Enter Note Details");
                }
            }
        });

//        //Handle click on showTaskList TextView
//        showListTV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                exitBtn.performClick();
//            }
//        });



        return rootView;
    }

    private void startRecyclerView_Animations() {
        recyclerView = getActivity().findViewById(R.id.rvNotes);
        addBtn = getActivity().findViewById(R.id.addEntryButton);


        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(recyclerView, "alpha",  1f, .2f);
        fadeOut.setDuration(DURATION);
        AnimatorSet mAnimationSet = new AnimatorSet();
        mAnimationSet.play(fadeOut);
        mAnimationSet.start();

        fadeOut = ObjectAnimator.ofFloat(addBtn,"alpha", 1f, 0f);
        fadeOut.setDuration(DURATION);
        mAnimationSet.play(fadeOut);
        mAnimationSet.start();

        fadeOut = ObjectAnimator.ofInt(addBtn,"visibility", View.VISIBLE,View.INVISIBLE);
        fadeOut.setDuration(DURATION);
        mAnimationSet.play(fadeOut);
        mAnimationSet.start();

        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(rootView, "alpha",  0f, 1f);
        fadeIn.setDuration(DURATION);
        mAnimationSet = new AnimatorSet();
        mAnimationSet.play(fadeIn);
        mAnimationSet.start();
    }

    private void fadeInAddBtn() {
        ObjectAnimator fadeIn2 = ObjectAnimator.ofFloat(addBtn,"alpha", 0f, 1f);
        fadeIn2.setDuration(DURATION);
        AnimatorSet mAnimationSet2 = new AnimatorSet();
        mAnimationSet2.play(fadeIn2);
        mAnimationSet2.start();

        fadeIn2 = ObjectAnimator.ofInt(addBtn,"visibility", View.INVISIBLE,View.VISIBLE);
        fadeIn2.setDuration(DURATION);
        mAnimationSet2.play(fadeIn2);
        mAnimationSet2.start();
    }

    private void setAutoCompleteListAdapter() {
        // Cursor/array will store unique Note Types. I will then use this to show suggestions when the user typing in the results.
        Cursor cr = db.rawQuery("SELECT DISTINCT type FROM NotesListDataTable",null);
        String [] tType_array = new String [cr.getCount()];
        int i = 0;
        if(cr.moveToFirst()) {
            do {
                tType_array[i] = cr.getString(0);
                i++;
            }
            while (cr.moveToNext());
        }
        cr.close();
        tTypeAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, tType_array);
        tType.setAdapter(tTypeAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DataEntryViewModel.class);
        // TODO: Use the ViewModel

    }

    @Override
    public void onStop(){
        super.onStop();

        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(recyclerView, "alpha",  .2f, 1f);
        fadeIn.setDuration(DURATION);
        AnimatorSet mAnimationSet = new AnimatorSet();
        mAnimationSet.play(fadeIn);
        mAnimationSet.start();



        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(rootView, "alpha",  1f, 0f);
        fadeOut.setDuration(DURATION);

        mAnimationSet = new AnimatorSet();
        mAnimationSet.play(fadeOut);
        mAnimationSet.start();

        fadeInAddBtn();


    }
}