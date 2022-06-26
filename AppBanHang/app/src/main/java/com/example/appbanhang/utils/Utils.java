package com.example.appbanhang.utils;

import com.example.appbanhang.model.GioHang;
import com.example.appbanhang.model.User;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    //Tạo link kết nối với database tại mysql
    public static  final  String BASE_URL="http://192.168.1.17/banhang/";
    //Tạo một danh sách lưu các sản phẩm tại giỏ hàng
    public static List<GioHang> manggiohang;
    //Lưu danh sách sản phẩm tại sẽ mua
    public static List<GioHang> mangmuahang= new ArrayList<>();
    //Lưu thông tin user
    public  static User user_curent = new User();

}
