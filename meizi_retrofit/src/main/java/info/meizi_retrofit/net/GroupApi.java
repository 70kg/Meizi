package info.meizi_retrofit.net;

import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by Mr_Wrong on 15/10/30.
 */
public interface GroupApi {
    @Headers("User-Agent': 'Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36" +
            "(KHTML, like Gecko) Chrome/41.0.2272.118 Safari/537.36")
    @GET("/{type}/page/{page}")
    Observable<String> getGroup(@Path("type") String type,@Path("page") int page);
}
