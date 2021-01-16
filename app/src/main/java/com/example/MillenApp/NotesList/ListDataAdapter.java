package com.example.MillenApp.NotesList;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.MillenApp.R;

import java.util.ArrayList;


public class ListDataAdapter extends
        RecyclerView.Adapter<ListDataAdapter.ViewHolder> {

    private ArrayList<DataObj> dataObjArrayList;
    private SQLiteDatabase db;
    private Context rootContext;


    public ListDataAdapter (ArrayList<DataObj> DataObjArrayList, Context context) {
        dataObjArrayList = DataObjArrayList;
        rootContext = context;

        db = context.openOrCreateDatabase("NotesListDb",context.MODE_PRIVATE,null);

        db.execSQL("CREATE TABLE IF NOT EXISTS DeletedNotesListDataTable(" +
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "date varchar(32)," +
                "type varchar(255)," +
                "details varchar(255))");

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView DateTV;
        public EditText TitleET;
        public EditText DetailET;
        public ImageView edit_icon;
        public Context context;
        public LinearLayout noteTypeLLayout;
        public TextView tvSeeMore;
        public ImageView saveEditBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            DateTV = itemView.findViewById(R.id.DateTV);
            TitleET = itemView.findViewById(R.id.TypeTV);
            DetailET = itemView.findViewById(R.id.DetailsTV);
            edit_icon = itemView.findViewById(R.id.EditBtn);
            tvSeeMore = itemView.findViewById(R.id.SeeMoreTV);
            context = itemView.getContext();
            noteTypeLLayout = itemView.findViewById(R.id.noteTypeLLayout);
            saveEditBtn = itemView.findViewById(R.id.saveEditBtn);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.notes_list_row, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        DataObj data = dataObjArrayList.get(position);

        TextView DateTV_ = holder.DateTV;
        DateTV_.setText(data.Date);

        EditText TitleET_ = holder.TitleET;
        TitleET_.setText(data.Title);

        EditText DetailET_ = holder.DetailET;
        DetailET_.setText(data.Details);

        ImageView edit_icon = holder.edit_icon;
        ImageView saveEditBtn = holder.saveEditBtn;
        LinearLayout noteTypeLLayout = holder.noteTypeLLayout;

        TextView TVSeeMore = holder.tvSeeMore;
        if (data.Details.length() > 100) TVSeeMore.setVisibility(View.VISIBLE);
        else TVSeeMore.setVisibility(View.GONE);

        TitleET_.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && edit_icon.getVisibility() == View.VISIBLE){

                    AnimatorSet mAnimationSet = new AnimatorSet();
                    ObjectAnimator fadeOut = ObjectAnimator.ofFloat(edit_icon,"alpha", 1f, 0f);
                    fadeOut.setDuration(200);
                    mAnimationSet.play(fadeOut);
                    mAnimationSet.start();
                    edit_icon.setVisibility(View.GONE);

                    fadeOut = ObjectAnimator.ofInt(edit_icon,"visibility", View.VISIBLE,View.GONE);
                    fadeOut.setDuration(200);
                    mAnimationSet.play(fadeOut);
                    mAnimationSet.start();

                    ObjectAnimator fadeIn = ObjectAnimator.ofInt(saveEditBtn,"visibility", View.GONE,View.VISIBLE);
                    fadeIn.setDuration(200);
                    mAnimationSet.play(fadeIn).after(200);
                    mAnimationSet.start();

                    fadeIn = ObjectAnimator.ofFloat(saveEditBtn,"alpha", 0f, 1f);
                    fadeIn.setDuration(200);
                    mAnimationSet.play(fadeIn).after(200);
                    mAnimationSet.start();
                }
                else{

                    if (saveEditBtn.getVisibility() == View.VISIBLE) {
                        edit_icon.setVisibility(View.VISIBLE);
                        edit_icon.setAlpha(1f);
                        saveEditBtn.setVisibility(View.GONE);
                    }
                }
            }
        });

        DetailET_.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && edit_icon.getVisibility() == View.VISIBLE){
                    AnimatorSet mAnimationSet = new AnimatorSet();
                    ObjectAnimator fadeOut = ObjectAnimator.ofFloat(edit_icon,"alpha", 1f, 0f);
                    fadeOut.setDuration(200);
                    mAnimationSet.play(fadeOut);
                    mAnimationSet.start();
                    edit_icon.setVisibility(View.GONE);

                    fadeOut = ObjectAnimator.ofInt(edit_icon,"visibility", View.VISIBLE,View.GONE);
                    fadeOut.setDuration(200);
                    mAnimationSet.play(fadeOut);
                    mAnimationSet.start();

                    ObjectAnimator fadeIn = ObjectAnimator.ofInt(saveEditBtn,"visibility", View.GONE,View.VISIBLE);
                    fadeIn.setDuration(200);
                    mAnimationSet.play(fadeIn).after(200);
                    mAnimationSet.start();

                    fadeIn = ObjectAnimator.ofFloat(saveEditBtn,"alpha", 0f, 1f);
                    fadeIn.setDuration(200);
                    mAnimationSet.play(fadeIn).after(200);
                    mAnimationSet.start();
                }
                else{

                    if (saveEditBtn.getVisibility() == View.VISIBLE) {
                        edit_icon.setVisibility(View.VISIBLE);
                        edit_icon.setAlpha(1f);
                        saveEditBtn.setVisibility(View.GONE);
                    }
                }
            }
        });

        saveEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tempTitle = TitleET_.getText().toString();
                String tempDetail = DetailET_.getText().toString();

                ContentValues values = new ContentValues();
                if (!tempTitle.equals(data.Title)){
                    values.put("type",tempTitle.trim());
                }
                if (!tempDetail.equals(data.Details)){
                    values.put("details",tempTitle.trim());
                }
                db.update("NotesListDataTable",values,"id = " + data.ID,null);
            }
        });

        edit_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edit_icon.setColorFilter(Color.GRAY);

                PopupMenu popup = new PopupMenu(holder.context, edit_icon);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.recyclerview_onclicklistener, popup.getMenu());
                popup.show();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.delete:
                                AlertDialog.Builder builder = new AlertDialog.Builder(holder.context);
                                builder.setTitle("Please Confirm");
                                DataObj tempObj = dataObjArrayList.get(position);
                                builder.setMessage("Are you sure you want to delete the following note?\n\n" +
                                        "Date: " + tempObj.Date + "\n" +
                                "Note Type: " + tempObj.Title + "\n" +
                                        "Note Details: " + tempObj.Details );
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        int ID = Integer.parseInt(tempObj.ID);
                                        db.delete("NotesListDataTable", "id" + "=" + ID , null);

                                        ContentValues values = new ContentValues();
                                        values.put("id",tempObj.ID);
                                        values.put("date",tempObj.Date);
                                        values.put("type", tempObj.Details);
                                        values.put("details",tempObj.ID);

                                        db.insert("DeletedNotesListDataTable",null,values);

                                        dataObjArrayList.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, dataObjArrayList.size());

                                        dialog.dismiss();
                                    }
                                });
                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                                break;

                            case R.id.edit:
                                Fragment mFragment = new editView_Fragment(data,holder.context);
                                FragmentTransaction transaction = ((FragmentActivity) rootContext).getSupportFragmentManager().beginTransaction();
                                transaction.addToBackStack(null);
                                transaction.replace(R.id.frameForFragment, mFragment).commit();
                                break;
                        }
                        return false;
                    }
                });
                popup.setOnDismissListener(new PopupMenu.OnDismissListener() {
                    @Override
                    public void onDismiss(PopupMenu menu) {
                        edit_icon.clearColorFilter();
                    }
                });

            }
        });

        DateTV_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_icon.performClick();
            }
        });

//        int collapsedMaxLines = 2;
//        ObjectAnimator animation = ObjectAnimator.ofInt(TVSeeMore, "maxLines",
//                TVSeeMore.getMaxLines() == collapsedMaxLines? TVSeeMore.getLineCount() : collapsedMaxLines);
//        animation.setDuration(200).start();

        noteTypeLLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TVSeeMore.getVisibility() == View.INVISIBLE ) {
                    Fragment mFragment = new editView_Fragment(data,holder.context);
                    FragmentTransaction transaction = ((FragmentActivity) rootContext).getSupportFragmentManager().beginTransaction();
                    transaction.addToBackStack(null);
                    transaction.replace(R.id.frameForFragment, mFragment).commit();
                }
                else if (TVSeeMore.getVisibility() == View.VISIBLE && DetailET_.getMaxLines() == 2 ){
                    ObjectAnimator animation = ObjectAnimator.ofInt(DetailET_, "maxLines",8);
                    animation.setDuration(100).start();
                    TVSeeMore.setText("See Less");
                }
            }
        });

        TVSeeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if(DetailET_.getLineCount() != 2){
//                    ObjectAnimator animation = ObjectAnimator.ofInt(DetailET_, "maxLines",2);
//                    animation.setDuration(100).start();
//                    TVSeeMore.setText("See More");
                }
//                else{
//                    ObjectAnimator animation = ObjectAnimator.ofInt(DetailET_, "maxLines",8);
//                    animation.setDuration(100).start();
//                    TVSeeMore.setText("See Less");
//                }
//            }
        });



    }


    @Override
    public int getItemCount() {
        return dataObjArrayList.size();
    }



}
