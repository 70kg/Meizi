package info.meizi_retrofit.net;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by wp on 2017/8/20.
 */

public class Api {
    private static Api api;
    private Retrofit retrofit;

    public Api() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://www.mzitu.com/")
                .addConverterFactory(StringConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public static Api getInsatcne() {
        if (api == null)
            api = new Api();
        return api;
    }

    public GroupApi createGroupApi() {
        return retrofit.create(GroupApi.class);
    }

    public ContentApi createContentApi() {
        return retrofit.create(ContentApi.class);
    }

    public SelfieApi createSelfieApi() {
        return retrofit.create(SelfieApi.class);
    }
}
