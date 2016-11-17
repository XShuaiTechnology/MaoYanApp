package com.mao.movie.retrofit;

import com.mao.movie.model.BannerModel;
import com.mao.movie.model.RecommendMovie;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by GaoMatrix on 2016/9/5.
 */
public interface ApiService {
    /**
     * Base url.
     */
    String API_SERVER_URL = "http://7xk9dj.com1.z0.glb.clouddn.com/banner/api/";
//    String API_SERVER_URL = "http://www.jtsimg.com:8080/";

    @GET("{itemCount}item.json")
    Call<BannerModel> fetchItemsWithItemCount(@Path("itemCount") int itemCount);

    /*@GET("{page}")
    Call<HttpResponseMeizi<List<Meizi>>> getMeizi(@Path("page") int page);*/

    @GET("http://www.jtsimg.com:8080/ts_launcher/recommend/newtuijian.action?method=listJX&pageSize=1188&pageNum=1")
    Observable<RecommendMovie> getRecommendMovie();

    @FormUrlEncoded
    @POST("http://192.168.12.12:80/MaoYanServer/index.php/admin/message/push")
    Call<ResponseBody> pushMovieToDevice(@Field("device_reg_id") String device_reg_id, @Field("message") String message);
}
