package com.example.MillenApp.NotesList;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView DateTV;
        public TextView TypeTV;
        public TextView DetailTV;
        public ImageView edit_icon;
        public Context context;
        public LinearLayout noteTypeLLayout;
        public TextView tvSeeMore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            DateTV = itemView.findViewById(R.id.DateTV);
            TypeTV = itemView.findViewById(R.id.TypeTV);
            DetailTV = itemView.findViewById(R.id.DetailsTV);
            edit_icon = itemView.findViewById(R.id.EditBtn);
            tvSeeMore = itemView.findViewById(R.id.SeeMoreTV);
            context = itemView.getContext();
            noteTypeLLayout = itemView.findViewById(R.id.noteTypeLLayout);

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

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        DataObj data = dataObjArrayList.get(position);

        TextView DateTV_ = holder.DateTV;
        DateTV_.setText(data.Date);

        TextView TypeTV_ = holder.TypeTV;
        TypeTV_.setText(data.Type);

        TextView DetailTV_ = holder.DetailTV;
        DetailTV_.setText(data.Details);

        ImageView edit_icon = holder.edit_icon;
        LinearLayout noteTypeLLayout = holder.noteTypeLLayout;

        TextView TVSeeMore = holder.tvSeeMore;
        if (data.Details.length() > 100) TVSeeMore.setVisibility(View.VISIBLE);
        else TVSeeMore.setVisibility(View.GONE);

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
                                "Note Type: " + tempObj.Type + "\n" +
                                        "Note Details: " + tempObj.Details );
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        int ID = Integer.parseInt(dataObjArrayList.get(position).ID);
                                        db.delete("NotesListDataTable", "id" + "=" + ID , null);
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

                            case R.id.edit:
                                Fragment mFragment = new editView_Fragment(data,holder.context);
                                FragmentTransaction transaction = ((FragmentActivity) rootContext).getSupportFragmentManager().beginTransaction();
                                transaction.addToBackStack(null);
                                transaction.replace(R.id.frameForFragment, mFragment).commit();

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
                if (TVSeeMore.getVisibility() == View.VISIBLE && DetailTV_.getMaxLines() == 2 ){
                    ObjectAnimator animation = ObjectAnimator.ofInt(DetailTV_, "maxLines",8);
                    TVSeeMore.setText("See Less");
                    //DetailTV_.setMaxLines(30);
                    animation.setDuration(200).start();

                }
                if (TVSeeMore.getVisibility() == View.INVISIBLE ) {
                    Fragment mFragment = new editView_Fragment(data,holder.context);
                    FragmentTransaction transaction = ((FragmentActivity) rootContext).getSupportFragmentManager().beginTransaction();
                    transaction.addToBackStack(null);
                    transaction.replace(R.id.frameForFragment, mFragment).commit();
                }
            }
        });

        TVSeeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TVSeeMore.getText() == "See Less"){
                    ObjectAnimator animation = ObjectAnimator.ofInt(DetailTV_, "maxLines",2);
                    TVSeeMore.setText("See More");
                    animation.setDuration(200).start();
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return dataObjArrayList.size();
    }



}
