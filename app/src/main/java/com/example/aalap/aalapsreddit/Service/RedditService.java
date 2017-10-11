package com.example.aalap.aalapsreddit.Service;

import com.example.aalap.aalapsreddit.Models.Feed;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Aalap on 2017-09-28.
 */

public interface RedditService {

    @GET("r/{path}/.rss")
    Observable<Response<Feed>> getFeedsO(@Path("path") String text);

    @GET("{path}.rss")
    Observable<Response<Feed>> getCommentFeeds(@Path("path") String text);

    @POST("/api/login/{user}")
    Observable<Response<ResponseBody>> getUser(
            @HeaderMap Map<String, String> headers
            , @Path("user") String userName
            , @Query("user") String user
            , @Query("passwd") String password
            , @Query("api_type") String type);


    @POST("/api/{comment}")
    Observable<Response<ResponseBody>> postComment(
            @HeaderMap Map<String, String> map
            , @Path("comment") String comment
            , @Query("parent") String parent
            , @Query("amp;text") String commentText);

    @POST("{comment}")
    Call<ResponseBody> submitComment(
            @HeaderMap Map<String, String> headers,
            @Path("comment") String comment,
            @Query("parent") String parent,
            @Query("amp;text") String text
    );


    //api/comment?parent=t3_73mwkt&amp;text=aalap"


}
