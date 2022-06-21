package com.example.appbanhang.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbanhang.Interface.IImageClickListener;
import com.example.appbanhang.R;
import com.example.appbanhang.model.EvenBus.TinhTongEven;
import com.example.appbanhang.model.GioHang;
import com.example.appbanhang.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.MyViewHolder> {
    Context context;
    List<GioHang> gioHangList;

    public GioHangAdapter(Context context, List<GioHang> gioHangList) {
        this.context = context;
        this.gioHangList = gioHangList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_giohang,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        GioHang gioHang = gioHangList.get(position);
        holder.item_gohang_tensp.setText(gioHang.getTensp());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.item_giohang_gia.setText(decimalFormat.format(gioHang.getGiasp())+ "đ");
        holder.item_giohang_soluong.setText(gioHang.getSoluong()+"");
        long gia = gioHang.getSoluong()* gioHang.getGiasp();
        holder.item_giohang_giasp2.setText(decimalFormat.format(gia)+ "đ");
        Glide.with(context).load(gioHang.getHinhsp()).into(holder.item_giohang_image);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Utils.mangmuahang.add(gioHang);
                    EventBus.getDefault().postSticky(new TinhTongEven());
                }else{
                    for(int i=0; i<Utils.mangmuahang.size();i++){
                        if(Utils.mangmuahang.get(i).getIdsp()== gioHang.getIdsp()){
                            Utils.mangmuahang.remove(i);
                            EventBus.getDefault().postSticky(new TinhTongEven());
                        }
                    }
                }

            }
        });

        holder.setListener(new IImageClickListener() {
            @Override
            public void onImageClick(View view, int pos, int giatri) {
                Log.d("TAG","onImageClick: "+pos+" ..."+giatri);
                if(giatri==1){
                    if (gioHangList.get(pos).getSoluong() >1){
                        int slmoi= gioHangList.get(pos).getSoluong()-1;
                        gioHangList.get(pos).setSoluong(slmoi);
                        //tính lại số lượng và giá của sản phẩm đó
                        holder.item_giohang_soluong.setText(gioHangList.get(pos).getSoluong()+"");
                        long gia =  gioHangList.get(pos).getSoluong()*  gioHangList.get(pos).getGiasp();
                        holder.item_giohang_giasp2.setText(decimalFormat.format(gia)+ "đ");
                        EventBus.getDefault().postSticky(new TinhTongEven());
                    }
                    else if(gioHangList.get(pos).getSoluong() ==1){
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                        builder.setTitle("Thông báo");
                        builder.setMessage("Bạn có muốn xóa sản phẩm khỏi giỏ hàng không");
                        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Utils.manggiohang.remove(pos);
                                notifyDataSetChanged();
                                EventBus.getDefault().postSticky(new TinhTongEven());
                            }
                        });
                        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();
                    }
                }else if(giatri==2){
                    if (gioHangList.get(pos).getSoluong() <11){
                        int slmoi= gioHangList.get(pos).getSoluong()+1;
                        gioHangList.get(pos).setSoluong(slmoi);
                    }
                    holder.item_giohang_soluong.setText(gioHangList.get(pos).getSoluong()+"");
                    long gia =  gioHangList.get(pos).getSoluong()*  gioHangList.get(pos).getGiasp();
                    holder.item_giohang_giasp2.setText(decimalFormat.format(gia)+ "đ");
                    EventBus.getDefault().postSticky(new TinhTongEven());
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return gioHangList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView item_giohang_image,imgtru,imgcong;
        TextView item_gohang_tensp,item_giohang_gia,item_giohang_soluong,item_giohang_giasp2;
        IImageClickListener listener;
        CheckBox checkBox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_giohang_image = itemView.findViewById(R.id.item_giohang_image);
            item_gohang_tensp = itemView.findViewById(R.id.item_gohang_tensp);
            item_giohang_gia = itemView.findViewById(R.id.item_giohang_gia);
            item_giohang_soluong = itemView.findViewById(R.id.item_giohang_soluong);
            item_giohang_giasp2 = itemView.findViewById(R.id.item_giohang_giasp2);
            checkBox=itemView.findViewById(R.id.item_giohang_check);

            imgcong=itemView.findViewById(R.id.item_giohang_cong);
            imgtru=itemView.findViewById(R.id.item_giohang_tru);
            imgcong.setOnClickListener(this);
            imgtru.setOnClickListener(this);
        }

        public void setListener(IImageClickListener listener) {
            this.listener = listener;
        }

        @Override
        public void onClick(View view) {
            if(view == imgtru){
                listener.onImageClick(view,getAdapterPosition(),1);
            }else if(view == imgcong){
                listener.onImageClick(view,getAdapterPosition(),2);
            }
        }
    }
}
