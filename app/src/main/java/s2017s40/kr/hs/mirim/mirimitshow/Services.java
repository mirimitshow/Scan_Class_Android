package s2017s40.kr.hs.mirim.mirimitshow;

import android.util.Log;

import com.google.gson.JsonArray;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

interface Services {

    //@Headers("Content-Type: application/json")
    @POST("/signin?")
    Call<User> signin(
            @Query("email") String email,
            @Query("password") String password);
    @POST("/signup")
    Call<List<User>> signup(
            @Path("name") String name,
            @Path("email") String email,
            @Path("phone") String phone,
            @Path("password") String password);
}