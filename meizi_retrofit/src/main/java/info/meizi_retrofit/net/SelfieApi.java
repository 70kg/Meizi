package info.meizi_retrofit.net;

import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by Mr_Wrong on 16/1/26.
 * 自拍
 */
public interface SelfieApi {
    @Headers("User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.111 Safari/537.36")
    @GET("/{page}")
    Observable<String> getSelfie(@Path("page") String page);
}
