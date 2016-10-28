package com.mao.movie.retrofit;

import com.mao.movie.model.BannerModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by GaoMatrix on 2016/9/5.
 */
public interface ApiService {
    /**
     * Base url.
     */
    String API_SERVER_URL = "http://7xk9dj.com1.z0.glb.clouddn.com/banner/api/";

    @GET("{itemCount}item.json")
    Call<BannerModel> fetchItemsWithItemCount(@Path("itemCount") int itemCount);

    /*@GET("{page}")
    Call<HttpResponseMeizi<List<Meizi>>> getMeizi(@Path("page") int page);*/

}
