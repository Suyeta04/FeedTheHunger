package com.example.feedthehunger;

import com.example.feedthehunger.Admin.UserModel;
import com.example.feedthehunger.Admin.VolunteerModel;
import com.example.feedthehunger.User.FoodResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {

    @Multipart
    @POST("food/upload")
    Call<ResponseBody> uploadFood(
            @Part("food_type") RequestBody foodtype,
            @Part("description") RequestBody description,
            @Part("address") RequestBody address,
            @Part("user_id") RequestBody userId,
            @Part("userFcmToken") RequestBody userFcmToken,
            @Part MultipartBody.Part image
    );
    @GET("food/getall")
    Call<FoodResponse> getAllFood();

    @FormUrlEncoded
    @POST("food/updateStatus")
    Call<ResponseBody> updateFoodStatus(
            @Field("id") String id,
            @Field("status") String status
    );

    @DELETE("food/delete/{id}")
    Call<ResponseBody> deleteFood(
            @Path("id") String id
    );

    @GET("users/getAll")
    Call<List<UserModel>> getAllUsers();

    @DELETE("users/delete/{id}")
    Call<ResponseBody> deleteUser(
            @Path("id") String id
    );

    @GET("volunteer/getAll")
    Call<List<VolunteerModel>> getAllVolunteers();

    @GET("food/getAvailablePickups")
    Call<FoodResponse> getAvailablePickups();

    @GET("food/getAssignedDelivery")
    Call<FoodResponse> getAssignedDelivery();

    @GET("food/getCompletedDelivery")
    Call<FoodResponse> getCompletedDelivery();

    @DELETE("volunteer/delete/{id}")
    Call<ResponseBody> deleteVolunteer(
            @Path("id") String id
    );
}