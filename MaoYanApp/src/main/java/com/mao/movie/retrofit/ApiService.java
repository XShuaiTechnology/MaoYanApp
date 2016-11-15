package com.mao.movie.retrofit;

import com.mao.movie.model.BannerModel;
import com.mao.movie.model.RecommendMovie;

import retrofit2.Call;
import retrofit2.http.GET;
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

    @GET("http://www.jtsimg.com:8080/ts_launcher/recommend/newtuijian.action?method=listJX&pageSize=1188&pageNum=1&pubdate=20161111021653")
    Observable<RecommendMovie> getRecommendMovie();
}
