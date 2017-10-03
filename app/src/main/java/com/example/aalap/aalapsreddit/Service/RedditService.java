package com.example.aalap.aalapsreddit.Service;

import com.example.aalap.aalapsreddit.Models.Feed;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Aalap on 2017-09-28.
 */

public interface RedditService {

    @GET("r/{path}/.rss")
    Observable<Response<Feed>> getFeedsO(@Path("path") String text);

    @GET("{path}/.rss")
    Call<ResponseBody> getFeedComments(@Path("path") String text);

}
