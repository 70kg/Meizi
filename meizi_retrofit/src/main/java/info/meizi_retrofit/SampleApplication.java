package info.meizi_retrofit;

import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 * Created by wp on 2017/8/20.
 */

public class SampleApplication extends TinkerApplication {
    public SampleApplication() {
        super(ShareConstants.TINKER_ENABLE_ALL, "info.meizi_retrofit.SampleApplicationLike",
                "com.tencent.tinker.loader.TinkerLoader", false);
    }
}
