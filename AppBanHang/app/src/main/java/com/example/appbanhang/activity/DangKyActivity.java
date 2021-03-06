package com.example.appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appbanhang.R;
import com.example.appbanhang.retrofit.ApiBanHang;
import com.example.appbanhang.retrofit.RetrofitClient;
import com.example.appbanhang.utils.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangKyActivity extends AppCompatActivity {
  EditText email,username,pass,repass,mobile;
  AppCompatButton button;
  ApiBanHang apiBanHang;
  CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        initView();
        initControl();
    }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        email = findViewById(R.id.email);
        username =findViewById(R.id.username);
        pass = findViewById(R.id.pass);
        repass =findViewById(R.id.repass);
        mobile = findViewById(R.id.mobile);
        button = findViewById(R.id.btndangki);
    }

    private void initControl() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dangKi();
            }
        });
    }

    private void dangKi() {
        String txt_email = email.getText().toString().trim();
        String txt_pass = pass.getText().toString().trim();
        String txt_username = username.getText().toString().trim();
        String txt_repass = repass.getText().toString().trim();
        String txt_mobile = mobile.getText().toString().trim();
        if(TextUtils.isEmpty(txt_email)){
            Toast.makeText(getApplicationContext(),"B???n ch??a nh???p email",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(txt_username)){
            Toast.makeText(getApplicationContext(),"B???n ch??a nh???p Username",Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(txt_pass)){
            Toast.makeText(getApplicationContext(),"B???n ch??a nh???p Password",Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(txt_repass)){
            Toast.makeText(getApplicationContext(),"B???n ch??a nh???p Confirm Password",Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(txt_mobile)){
            Toast.makeText(getApplicationContext(),"B???n ch??a nh???p s??? ??i???n tho???i",Toast.LENGTH_SHORT).show();
        } else{
            if(txt_pass.equals(txt_repass)){
             //Post d??? li???u ????? insert v??o database user
                compositeDisposable.add(apiBanHang.dangki(txt_email,txt_pass,txt_username,txt_mobile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if(userModel.isSuccess()){
                                Utils.user_curent.setEmail(txt_email);

                                //quay v??? m??n h??nh ????ng nh???p
                                Intent dangnhap = new Intent(getApplicationContext(),DangNhapActivity.class);
                                startActivity(dangnhap);

                               //xu???t th??ng b??o th??nh c??ng
                                Toast.makeText(getApplicationContext(),"Th??nh c??ng",Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(getApplicationContext(),userModel.getMessage(),Toast.LENGTH_SHORT).show();
                            }

                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                ));
            }else{
                Toast.makeText(getApplicationContext(),"M???t kh???u kh??ng kh???p",Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    protected  void onDestroy(){
        compositeDisposable.clear();
        super.onDestroy();
    }
}