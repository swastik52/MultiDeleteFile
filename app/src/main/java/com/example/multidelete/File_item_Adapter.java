package com.example.multidelete;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class File_item_Adapter extends RecyclerView.Adapter<File_item_Adapter.ItemView>{

    ArrayList<File> file_data;
    Context context;
    MainActivity mainActivity;
    private onRecyclerViewItemClickListener mItemClickListener;

    public File_item_Adapter(ArrayList<File> data,Context context){
        this.file_data=data;
        this.context=context;
        mainActivity= (MainActivity) context;
    }

    @NonNull
    @Override
    public ItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view= inflater.inflate(R.layout.file_item,parent,false);
        return new ItemView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemView holder, final int position) {
        String fileName= String.valueOf(file_data.get(position).getName());
        holder.filename.setText(fileName);

        if (!mainActivity.is_in_Action_mode){
            holder.checked.setVisibility(View.GONE);
        }else{
            holder.checked.setVisibility(View.VISIBLE);
            holder.checked.setChecked(false);
        }
        //On Click
      /*  holder.checked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClickListener(v, position);
                }
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return file_data.size();
    }

    public void updateAdapter(ArrayList<File> list){
        for (File file:list){
            file_data.remove(file);
            deleteFile(file);
        }
        notifyDataSetChanged();
    }
    public boolean deleteFile(File file){
        boolean deleted = false;
        if (file.exists()) {
            deleted = file.delete();
        }
        return deleted;
    }


    //Call the interface
   /* public void setOnItemClickListener(onRecyclerViewItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }*/

    public class ItemView extends RecyclerView.ViewHolder implements View.OnClickListener {
        CheckBox checked;
        TextView filename;
        CardView cardView;
        public ItemView(@NonNull View itemView) {
            super(itemView);
            checked=itemView.findViewById(R.id.checkfordelete);
            filename=itemView.findViewById(R.id.filename);
            cardView=itemView.findViewById(R.id.cardView);

            cardView.setOnLongClickListener(mainActivity);
            checked.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mainActivity.preparedSelection(v,getAdapterPosition());
        }
    }
}
