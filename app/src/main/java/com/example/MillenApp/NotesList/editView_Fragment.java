package com.example.MillenApp.NotesList;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.MillenApp.R;

public class editView_Fragment extends Fragment {

    private static final int DURATION = 300 ;

    public editView_Fragment() {
        // Required empty public constructor
    }

    private DataObj data;
    private Context context;

    public editView_Fragment(DataObj data, Context context) {
        this.data = data;
        this.context = context;
    }


    SQLiteDatabase db;
    Button uploadBtn;
    View rootView;
    ImageButton exitBtn;
    View recyclerView;
    View addBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_edit_view_, container, false);

        startAnimations();

        TextView date = rootView.findViewById(R.id.dateTV);
        EditText editTitle = rootView.findViewById(R.id.edit_note_title);
        EditText editDetails= rootView.findViewById(R.id.edit_note_details);

        date.setText(data.Date);
        editTitle.setText(data.Type);
        editDetails.setText(data.Details);

        exitBtn = rootView.findViewById(R.id.exitBtn_);
        exitBtn.setOnClickListener(v -> {

            fadeInAddBtn();

            exitFragment();

        });

        db = rootView.getContext().openOrCreateDatabase("NotesListDb",rootView.getContext().MODE_PRIVATE,null);

        uploadBtn = rootView.findViewById(R.id.uploadButton);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag1 = false, flag2 = false;
                flag1 = !editTitle.getText().toString().isEmpty();
                flag2 = !editDetails.getText().toString().isEmpty();

                if (flag1 && flag2){
                    AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());

                    builder.setTitle("Please Confirm");
                    builder.setMessage( "Date: " + date.getText().toString().trim() + "\n" +
                            "Note Type: " + editTitle.getText().toString().trim() + "\n" +
                            "Note Details: " + editDetails.getText().toString().trim() + "\n\n" +
                            "Is this correct?");

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            ContentValues values = new ContentValues();
                            values.put("date",date.getText().toString().trim());
                            values.put("type",editTitle.getText().toString().trim());
                            values.put("details",editDetails.getText().toString().trim());


                            try {
                                db.update("NotesListDataTable",values,"id = " + data.ID,null);

                                Toast.makeText(getActivity().getApplicationContext(), "Update Successful", Toast.LENGTH_SHORT).show();

                                exitFragment();
                                ((showData_Activity)getActivity()).refreshAdapter();

                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getActivity().getApplicationContext(), "Update Failed", Toast.LENGTH_SHORT).show();
                            }

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
                    if (!flag1) editTitle.setError("Enter Note Type");
                    if (!flag2) editDetails.setError("Enter Note Details");
                }
            }
        });
        return rootView;
    }

    private void exitFragment() {
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        getActivity().getSupportFragmentManager().popBackStack();
    }


    private void startAnimations() {
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

    @Override
    public void onStop(){
        super.onStop();

        View recyclerView = getActivity().findViewById(R.id.rvNotes);

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