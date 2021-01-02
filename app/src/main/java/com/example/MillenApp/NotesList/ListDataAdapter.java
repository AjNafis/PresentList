package com.example.MillenApp.NotesList;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.MillenApp.R;

import java.util.ArrayList;


public class ListDataAdapter extends
        RecyclerView.Adapter<ListDataAdapter.ViewHolder> {

    private ArrayList<DataObj> dataObjArrayList;
    private SQLiteDatabase db;

    public ListDataAdapter (ArrayList<DataObj> DataObjArrayList,Context context) {
        dataObjArrayList = DataObjArrayList;
        db = context.openOrCreateDatabase("NotesListDb",context.MODE_PRIVATE,null);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView DateTV;
        public TextView TypeTV;
        public TextView DetailTV;
        public ImageView arrow;
        public Context context;
        public LinearLayout noteTypeLLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            DateTV = itemView.findViewById(R.id.DateTV);
            TypeTV = itemView.findViewById(R.id.TypeTV);
            DetailTV = itemView.findViewById(R.id.DetailsTV);
            arrow = itemView.findViewById(R.id.arrowBtn);
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

        ImageView arrow = holder.arrow;
        LinearLayout noteTypeLLayout = holder.noteTypeLLayout;


        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                arrow.setColorFilter(Color.GRAY);

                PopupMenu popup = new PopupMenu(holder.context, arrow);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.showlist_onclicklistener, popup.getMenu());
                popup.show();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.delete:

                                int ID = Integer.parseInt(dataObjArrayList.get(position).ID);
                                db.delete("NotesListDataTable", "id" + "=" + ID , null);
                                dataObjArrayList.remove(position);
                                notifyDataSetChanged();
                        }

                        return false;
                    }
                });
                popup.setOnDismissListener(new PopupMenu.OnDismissListener() {
                    @Override
                    public void onDismiss(PopupMenu menu) {
                        arrow.clearColorFilter();
                    }
                });

            }
        });

        DateTV_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrow.performClick();
            }
        });

        noteTypeLLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrow.performClick();
            }
        });
    }


    @Override
    public int getItemCount() {
        return dataObjArrayList.size();
    }



}
