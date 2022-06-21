package com.example.appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
import com.example.appbanhang.model.GioHang;
import com.example.appbanhang.model.SanPhamMoi;
import com.example.appbanhang.utils.Utils;
import com.nex3z.notificationbadge.NotificationBadge;

import java.text.DecimalFormat;

public class ChiTietActivity extends AppCompatActivity {
    TextView tensp, giasp, mota;
    Button btnthem;
    ImageView imghinhanh;
    Spinner spinner;
    Toolbar toolbar;
    SanPhamMoi sanPhamMoi;
    NotificationBadge badge;

    FrameLayout frameLayoutgiohang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet);
        initView();
        ActionToolBar();
        initData();
        initControl();
    }

    private void initControl() {
        btnthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themgiohang();
            }
        });
    }

    private void themgiohang() {

        if(Utils.manggiohang.size()>0){
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            boolean flag= false;
            for(int i=0;i<Utils.manggiohang.size();i++){
                if(Utils.manggiohang.get(i).getIdsp() == sanPhamMoi.getId()){
                    Utils.manggiohang.get(i).setSoluong(soluong + Utils.manggiohang.get(i).getSoluong());
                    long gia = Long.parseLong(sanPhamMoi.getGiasp())*Utils.manggiohang.get(i).getSoluong();
                    Utils.manggiohang.get(i).setGiasp(gia);
                    flag= true;
                    break;
                }
            }
            if(flag == false){
                long gia = Long.parseLong(sanPhamMoi.getGiasp())*soluong;
                GioHang gioHang = new GioHang();
                gioHang.setGiasp(gia);
                gioHang.setSoluong(soluong);
                gioHang.setTensp(sanPhamMoi.getTensp());
                gioHang.setIdsp(sanPhamMoi.getId());
                gioHang.setHinhsp(sanPhamMoi.getHinhanh());
                Utils.manggiohang.add(gioHang);
            }
        }
        else {
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            long gia = Long.parseLong(sanPhamMoi.getGiasp())*soluong;
            GioHang gioHang = new GioHang();
            gioHang.setGiasp(gia);
            gioHang.setSoluong(soluong);
            gioHang.setTensp(sanPhamMoi.getTensp());
            gioHang.setIdsp(sanPhamMoi.getId());
            gioHang.setHinhsp(sanPhamMoi.getHinhanh());
            Utils.manggiohang.add(gioHang);
        }
        //Biến lưu tổng giá trị số lượng hàng đã đặt trong giỏ hàng
        int total=0;
        //set lại giá trị số lượng cho giỏ hàng
        for(int i=0;i<Utils.manggiohang.size();i++){
           total = total+ Utils.manggiohang.get(i).getSoluong();
        }
        badge.setText(String.valueOf(total));
    }

    private void initData() {
        sanPhamMoi =(SanPhamMoi) getIntent().getSerializableExtra("chitiet");
        tensp.setText(sanPhamMoi.getTensp());
        mota.setText(sanPhamMoi.getMota());
        Glide.with(getApplicationContext()).load(sanPhamMoi.getHinhanh()).into(imghinhanh);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        giasp.setText("Gía: "+decimalFormat.format(Double.parseDouble(sanPhamMoi.getGiasp()))+ "đ");
        Integer[] so= new Integer[]{1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> adapterspin = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,so);
        spinner.setAdapter(adapterspin);
    }

    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView() {
        tensp=findViewById(R.id.txttensp);
        giasp=findViewById(R.id.txtgiasp);
        mota=findViewById(R.id.txtmotachitiet);
        btnthem=findViewById(R.id.btnthemvaogiohang);
        spinner=findViewById(R.id.spinner);
        imghinhanh=findViewById(R.id.imgchitiet);
        toolbar = findViewById(R.id.toobar);
        badge = findViewById(R.id.menu_sl);
        frameLayoutgiohang= findViewById(R.id.framgiohang);
        //Click vao nut gio hang
        frameLayoutgiohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent giohang = new Intent(getApplicationContext(),GioHangActivity.class);
                startActivity(giohang);
            }
        });
        int total =0;
        //set giá trị  cho biến total khi vào màn hình chi tiết
        if(Utils.manggiohang.size()>0){
            for(int i=0;i<Utils.manggiohang.size();i++){
                total = total+ Utils.manggiohang.get(i).getSoluong();
            }
        }
        badge.setText(String.valueOf(total));
    }
    @Override
    protected void onResume(){
        super.onResume();
        if(Utils.manggiohang != null){
            int total=0;
            for(int i=0;i<Utils.manggiohang.size();i++){
                total = total+ Utils.manggiohang.get(i).getSoluong();
            }
            badge.setText(String.valueOf(total));
        }

    }

}