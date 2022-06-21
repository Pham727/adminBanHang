package com.example.appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
import com.example.appbanhang.adapter.LoaiSpAdapter;
import com.example.appbanhang.adapter.SanPhamMoiAdapter;
import com.example.appbanhang.model.LoaiSp;
import com.example.appbanhang.model.LoaiSpModel;
import com.example.appbanhang.model.SanPhamMoi;
import com.example.appbanhang.model.SanPhamMoiModel;
import com.example.appbanhang.model.User;
import com.example.appbanhang.retrofit.ApiBanHang;
import com.example.appbanhang.retrofit.RetrofitClient;
import com.example.appbanhang.utils.Utils;
import com.google.android.material.navigation.NavigationView;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recyclerViewManHinhChinh;
    NavigationView navigationView;
    ListView listViewManHinhChinh;
    DrawerLayout drawerLayout;
    LoaiSpAdapter loaiSpAdapter;
    List<LoaiSp> mangLoaiSp;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    List<SanPhamMoi> mangSpMoi;
    SanPhamMoiAdapter spAdapter;
    NotificationBadge badge;
    FrameLayout frameLayout;
    ImageView imgsearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);

        AnhXa();
        Init();
        ActionBar();
        
        if(isConnected(this)){
            ActionViewFlipper();
            getLoaiSanPham();
            getSpMoi();
            getEventClick();
        }
        else{
            Toast.makeText(getApplicationContext(),"Không có internet, vui lòng kết nối",Toast.LENGTH_LONG).show();
        }
    }

    private void Init() {
        Paper.init(this);
        if(Paper.book().read("user")!= null)
        {
            User user=Paper.book().read("user");
            Utils.user_curent=user;
        }
    }

    private void getEventClick() {
        listViewManHinhChinh.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        Intent trangchu= new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(trangchu);
                        break;
                    case 1:
                        Intent dienthoai= new Intent(getApplicationContext(),DienThoaiActivity.class);
                        dienthoai.putExtra("loai",1);
                        startActivity(dienthoai);
                        break;
                    case 2:
                        Intent laptop = new Intent(getApplicationContext(), DienThoaiActivity.class);
                        laptop.putExtra("loai",2);
                        startActivity(laptop);
                        break;
                    case 4:
                        Intent quanli = new Intent(getApplicationContext(), QuanLiActivity.class);
                        startActivity(quanli);
                        break;
                    case 5:
                        Intent lsdonhang = new Intent(getApplicationContext(), XemDonActivity.class);
                        startActivity(lsdonhang);
                        break;
                    case 6:
                        //Xóa các key
                        Paper.book().delete("user");
                        Paper.book().delete("pass");
                        Intent dangnhap = new Intent(getApplicationContext(), DangNhapActivity.class);
                        startActivity(dangnhap);
                        break;
                }
            }
        });
    }

    private void getSpMoi() {
        compositeDisposable.add(apiBanHang.getSpMoi()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
                sanPhamMoiModel->{
                    if(sanPhamMoiModel.isSuccess()){
                        mangSpMoi=sanPhamMoiModel.getResult();
                        spAdapter = new SanPhamMoiAdapter(getApplicationContext(),mangSpMoi);
                        recyclerViewManHinhChinh.setAdapter(spAdapter);

                    }

                },
                throwable -> {
                    Toast.makeText(getApplicationContext(),"Không kết nối được với server"+ throwable.getMessage(),Toast.LENGTH_LONG).show();
                }
        ));
    }

    private void getLoaiSanPham() {
        compositeDisposable.add(apiBanHang.getLoaiSp()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
                loaiSpModel -> {
                    if(loaiSpModel.isSuccess()){
                        mangLoaiSp= loaiSpModel.getResult();
                        //Khởi tạo apdapter
                        loaiSpAdapter = new LoaiSpAdapter(getApplicationContext(),mangLoaiSp);
                        listViewManHinhChinh.setAdapter(loaiSpAdapter);
                    }
                }
        ));
    }

    private void ActionViewFlipper() {
        List<String> mangquangcao= new ArrayList<>();
        mangquangcao.add("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-Le-hoi-phu-kien-800-300.png");
        mangquangcao.add("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-HC-Tra-Gop-800-300.png");
        mangquangcao.add("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-big-ky-nguyen-800-300.jpg");
        for(int i =0; i<mangquangcao.size();i++){
            ImageView imageView = new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load((mangquangcao.get(i))).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        Animation slide_in = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in_right);
        Animation slide_out =AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_out_right);
        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setInAnimation(slide_out);
    }

    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View view){
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private  void  AnhXa(){
        toolbar = findViewById(R.id.toolbarmanhinhchinh);
        viewFlipper = findViewById(R.id.viewlipper);
        recyclerViewManHinhChinh = findViewById(R.id.recycleview);
        imgsearch = findViewById(R.id.imgsearch);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerViewManHinhChinh.setLayoutManager(layoutManager);
        recyclerViewManHinhChinh.setHasFixedSize(true);

        navigationView=findViewById(R.id.navigationView);
        listViewManHinhChinh=findViewById(R.id.Listviewmanhinhchinh);
        drawerLayout=findViewById(R.id.drawerlayout);
        frameLayout =findViewById(R.id.framgiohang);
        badge=findViewById(R.id.menu_sl);
        //Khởi tạo list loại sản phẩm
        mangLoaiSp = new ArrayList<>();
        //khởi tạo list sản phẩm mới
        mangSpMoi = new ArrayList<>();
        //Khởi tạo mảng giỏ hàng
        if(Utils.manggiohang == null){
            Utils.manggiohang = new ArrayList<>();
        }
        else {
            int total=0;
            for(int i=0;i<Utils.manggiohang.size();i++){
                total = total+ Utils.manggiohang.get(i).getSoluong();
            }
            badge.setText(String.valueOf(total));
        }
        //Click vao gio hang
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent giohang = new Intent(getApplicationContext(),GioHangActivity.class);
                startActivity(giohang);
            }
        });
        //click button search
        imgsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent search= new Intent(getApplicationContext(),SearchActivity.class);
                startActivity(search);
            }
        });

    }
    @Override
    protected  void onResume(){
        super.onResume();
        int total=0;
        for(int i=0;i<Utils.manggiohang.size();i++){
            total = total+ Utils.manggiohang.get(i).getSoluong();
        }
        badge.setText(String.valueOf(total));
    }

    private boolean isConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if((wifi != null && wifi.isConnected()) || (mobile!= null && mobile.isConnected())){
            return  true;
        }
        else {
            return false;
        }
    }
    @Override
    protected  void  onDestroy(){
        compositeDisposable.clear();
        super.onDestroy();
    }


}