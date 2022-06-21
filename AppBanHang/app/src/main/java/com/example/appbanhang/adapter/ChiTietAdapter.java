package com.example.appbanhang.adapter;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
import com.example.appbanhang.model.Item;
import com.google.gson.Gson;

import java.util.List;

public class ChiTietAdapter extends RecyclerView.Adapter<ChiTietAdapter.MyViewHolder> {
    Context context;
    List<Item> itemList;

    public ChiTietAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chitietdonhang,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.txtten.setText(item.getTensp()+ "");
        holder.txtsoluong.setText("số lượng: "+item.getSoluong()+"");
        Glide.with(context).load(item.getHinhanh()).into(holder.imagechitiet);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtten, txtsoluong;
        ImageView imagechitiet;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imagechitiet=itemView.findViewById(R.id.item_imgchitiet);
            txtten =itemView.findViewById(R.id.item_tenspchitiet);
            txtsoluong= itemView.findViewById(R.id.item_soluongchitiet);

        }
    }

}
