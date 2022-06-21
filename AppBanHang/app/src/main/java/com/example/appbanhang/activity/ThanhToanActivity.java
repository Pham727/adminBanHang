package com.example.appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbanhang.R;
import com.example.appbanhang.retrofit.ApiBanHang;
import com.example.appbanhang.retrofit.RetrofitClient;
import com.example.appbanhang.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DecimalFormat;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class ThanhToanActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txtTongTien, txtSodt, txtEmail;
    EditText editDiaChi;
    AppCompatButton btnDatHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    long tongtien=0;
    int totalItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
        initView();
        countItem();
        initControl();

    }

    private void countItem() {
        //Biến lưu tổng giá trị số lượng hàng đã đặt trong giỏ hàng
        totalItem=0;
        //set lại giá trị số lượng cho giỏ hàng
        for(int i=0;i<Utils.mangmuahang.size();i++){
            totalItem = totalItem+ Utils.mangmuahang.get(i).getSoluong();
        }
    }

    private void initControl() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        DecimalFormat dateFormat = new DecimalFormat("###,###,###");
        tongtien= getIntent().getLongExtra("tongtien",0);

        txtTongTien.setText(dateFormat.format(tongtien));
        txtSodt.setText(Utils.user_curent.getMobile());
        txtEmail.setText(Utils.user_curent.getEmail());
        //click nut đặt hàng
        btnDatHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_diachi = editDiaChi.getText().toString().trim();
                if(TextUtils.isEmpty(str_diachi)){
                    Toast.makeText(getApplicationContext(),"bạn chưa nhập địa chỉ",Toast.LENGTH_SHORT).show();
                }else{
                    //post data
                    Log.d("test",new Gson().toJson(Utils.mangmuahang));
                    String str_email= Utils.user_curent.getEmail();
                    String str_sdt= Utils.user_curent.getMobile();
                    int iduser = Utils.user_curent.getId();
                    compositeDisposable.add(apiBanHang.createOder(iduser,str_diachi,totalItem,String.valueOf(tongtien),str_sdt,str_email,new Gson().toJson(Utils.mangmuahang))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            userModel -> {
                                Toast.makeText(getApplicationContext(),"thanh cong",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(intent);
                                Utils.manggiohang.clear();
                                Utils.mangmuahang.clear();
                                finish();
                            },
                            throwable -> {
                                Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                    ));

                }
            }
        });
    }

    private void initView() {
        apiBanHang= RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        toolbar= findViewById(R.id.toobar);
        txtTongTien = findViewById(R.id.txttongtien);
        txtSodt= findViewById(R.id.txtSodt);
        txtEmail= findViewById(R.id.txtEmail);
        editDiaChi = findViewById(R.id.editDiaChi);
        btnDatHang = findViewById(R.id.btndathang);
    }

    @Override
    protected  void  onDestroy(){
        compositeDisposable.clear();
        super.onDestroy();
    }
}