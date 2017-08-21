package info.meizi_retrofit.net;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Mr_Wrong on 16/1/26.
 * 自拍
 */
public interface SelfieApi {
    @Headers("User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.111 Safari/537.36")
    @GET()
    Observable<String> getSelfie(@Url() String url);
}
