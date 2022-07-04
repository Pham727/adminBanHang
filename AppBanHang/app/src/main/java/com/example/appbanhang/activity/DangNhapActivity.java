package com.example.appbanhang.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbanhang.R;
import com.example.appbanhang.retrofit.ApiBanHang;
import com.example.appbanhang.retrofit.RetrofitClient;
import com.example.appbanhang.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangNhapActivity extends AppCompatActivity {
    TextView txtDangky,txtresetpass;
    EditText email,pass;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    AppCompatButton btndangnhap;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    boolean isLogin = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        initView();
        initControl();
    }

    private void initControl() {
        txtDangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),DangKyActivity.class);
                startActivity(intent);
            }
        });
        txtresetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ResetPassActivity.class);
                startActivity(intent);
            }
        });

        btndangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dangnhap();
            }
        });
    }

    private void dangnhap() {
        String str_email = email.getText().toString().trim();
        String str_pass = pass.getText().toString().trim();
        if(TextUtils.isEmpty(str_email)){
            Toast.makeText(getApplicationContext(),"Bạn chưa nhập email",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(str_pass)){
            Toast.makeText(getApplicationContext(),"Bạn chưa nhập password",Toast.LENGTH_SHORT).show();
        }else{
            //Lưu các giá trị đăng nhập
            Paper.book().write("email", str_email);
            Paper.book().write("pass", str_pass);
            if(user != null){
                //user đã đăng nhập firebase
                dangNhap(str_email,str_pass);
            }else{
                //user chưa đăng nhập firebase
                firebaseAuth.signInWithEmailAndPassword(str_email,str_pass)
                        .addOnCompleteListener(DangNhapActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    dangNhap(str_email,str_pass);
                                }
                            }
                        });

            }

        }
    }

    private void initView() {
        Paper.init(this);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        txtDangky = findViewById(R.id.txtdangki);
        txtresetpass =findViewById(R.id.txtresetpass);
        email =findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        btndangnhap = findViewById(R.id.btndangnhap);
        firebaseAuth =FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        if(Paper.book().read("email") != null && Paper.book().read("pass")!= null){
            email.setText(Paper.book().read("email"));
            pass.setText(Paper.book().read("pass"));
            if(Paper.book().read("islogin")!= null){
                boolean flag= Paper.book().read("islogin");
                if(flag){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //dangNhap(Paper.book().read("email"),Paper.book().read("pass"));
                        }
                    },1000);
                }
            }
        }
    }

    private void dangNhap(String email, String pass) {
        compositeDisposable.add(apiBanHang.dangNhap(email,pass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if(userModel.isSuccess()){
                                isLogin = true;
                                Paper.book().write("islogin",isLogin);
                                Utils.user_curent = userModel.getResult().get(0);
                                //lưu thông tin user
                                Paper.book().write("user", userModel.getResult().get(0));
                                //chuyển đến màn hình chính
                                Intent manhinhchinh = new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(manhinhchinh);
                            }else{
                                Toast.makeText(getApplicationContext(),userModel.getMessage(),Toast.LENGTH_SHORT).show();
                            }

                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(Utils.user_curent.getEmail() != null){
            email.setText(Utils.user_curent.getEmail());
        }
    }
    @Override
    protected void onDestroy(){
        compositeDisposable.clear();
        super.onDestroy();
    }
}