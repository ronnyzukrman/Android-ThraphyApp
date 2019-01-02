package com.tenserflow.therapist.Webservice;


import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Admin on 12/13/2018.
 */

public interface Service {


    @FormUrlEncoded
    @POST("register")
    Call<JsonObject> register(@Header("auth") String header, @Field("name") String name, @Field("email") String email, @Field("password") String password, @Field("phone_no") String phone_no, @Field("address") String address, @Field("dob") String dob,@Field("country_code") String country_code,@Field("flag") String flag);

    @FormUrlEncoded
    @POST("login")
    Call<JsonObject> login(@Header("auth") String header, @Field("email") String email, @Field("password") String password);


    @FormUrlEncoded
    @POST("forgotPassword")
    Call<JsonObject> forgot(@Header("auth") String header, @Field("email") String email);

    @FormUrlEncoded
    @POST("SocialLogin")
    Call<JsonObject> social_login(@Header("auth") String header, @Field("social_id") String login_type, @Field("login_type") String social_id, @Field("name") String name, @Field("email") String email,@Field("address") String address,@Field("dob") String dob,@Field("password") String password,@Field("phone_no") String phone_no,@Field("user_image") String user_image,@Field("country_code") String country_code,@Field("flag") String flag,@Field("image_type") String  image_type);

    @FormUrlEncoded
    @POST("changePassword")
    Call<JsonObject> change_password(@Header("token") String header, @Field("old_password") String old_password,@Field("new_password") String new_password);


    @Multipart
    @POST("editProfile")
    Call<JsonObject> editProfile(@Header("token") String token, @Part("name") RequestBody name, @Part("email") RequestBody email, @Part("phone_no") RequestBody phone_no, @Part("address") RequestBody address, @Part("dob") RequestBody dob, @Part("user_image") String pic,@Part("country_code") RequestBody country_code,@Part("flag") RequestBody flag,@Part("image_type") RequestBody image_type);

    @Multipart
    @POST("editProfile")
    Call<JsonObject> editProfile1(@Header("token") String token, @Part("name") RequestBody name, @Part("email") RequestBody email, @Part("phone_no") RequestBody phone_no, @Part("address") RequestBody address, @Part("dob") RequestBody dob, @Part MultipartBody.Part image1,@Part("country_code") RequestBody country_code,@Part("flag") RequestBody flag,@Part("image_type") RequestBody image_type);



    @POST("getProfile")
    Call<JsonObject> getProfile(@Header("token") String token);

    @POST("delete_profile_pic")
    Call<JsonObject> delete_profile_img(@Header("token") String token);




}
