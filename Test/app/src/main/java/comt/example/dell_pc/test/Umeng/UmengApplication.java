package comt.example.dell_pc.test.Umeng;

import android.app.Application;
import android.util.Log;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.vondear.rxtool.RxTool;

import comt.example.dell_pc.test.Utils.PreferenceUtil;
import static anet.channel.util.Utils.context;

public class UmengApplication extends Application {
    private static final String TAG="UmengApplication";
    @Override
    public void onCreate() {
        super.onCreate();
        RxTool.init(this);
        PreferenceUtil.init(this);       //存数据的  初始化

        //**********************Umeng***************************
        UMConfigure.init(this,UMConfigure.DEVICE_TYPE_PHONE, "ed05e060cd937c7561a5591598b2fefb");
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setResourcePackageName("comt.example.dell_pc.test");
        PushAgent.getInstance(context).onAppStart();                    //开始
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String s) {
                Log.e("获取token成功:",s);
            }
            @Override
            public void onFailure(String s, String s1) {
                Log.e("获取token失败:",s+"且s1="+s1);
            }
        });
    }
}
