package comt.example.dell_pc.test.Pagers.Four;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.github.ikidou.fragmentBackHandler.BackHandlerHelper;
import com.vondear.rxtool.RxBarTool;
import com.vondear.rxtool.RxConstants;
import com.vondear.rxtool.RxImageTool;
import com.vondear.rxtool.RxKeyboardTool;
import com.vondear.rxui.view.RxTextAutoZoom;

import comt.example.dell_pc.test.R;


public class FourFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View contentView;
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    ProgressBar pbWebBase;
    TextView tvTitle;
    WebView webBase;
    ImageView ivFinish;
    RxTextAutoZoom mRxTextAutoZoom;
    LinearLayout llIncludeTitle;
    private String webPath = "";
    private long mBackPressed;

    public FourFragment() {
        // Required empty public constructor
    }


    public static FourFragment newInstance(String param1, String param2) {
        FourFragment fragment = new FourFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contentView = inflater.inflate(R.layout.fragment_four, container, false);
        RxBarTool.setTransparentStatusBar(getActivity());
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        initView();// 初始化控件 - FindViewById之类的操作
        initData();// 初始化控件的数据及监听事件
    return contentView;
    }
    private void initView(){
        // TODO Auto-generated method stub
        mRxTextAutoZoom = contentView.findViewById(com.vondear.rxui.R.id.afet_tv_title);
        llIncludeTitle = contentView.findViewById(com.vondear.rxui.R.id.ll_include_title);
        tvTitle = contentView.findViewById(com.vondear.rxui.R.id.tv_title);
        pbWebBase = contentView.findViewById(com.vondear.rxui.R.id.pb_web_base);
        webBase = contentView.findViewById(com.vondear.rxui.R.id.web_base);
        ivFinish = contentView.findViewById(com.vondear.rxui.R.id.iv_finish);
        ivFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (webBase.canGoBack()) {
                    webBase.goBack();
                } else {
                    getActivity().finish();
                }
            }
        });

        initAutoFitEditText();
    }
    private void initAutoFitEditText(){

        mRxTextAutoZoom.clearFocus();
        mRxTextAutoZoom.setEnabled(false);
        mRxTextAutoZoom.setFocusableInTouchMode(false);
        mRxTextAutoZoom.setFocusable(false);
        mRxTextAutoZoom.setEnableSizeCache(false);
        //might cause crash on some devices
        mRxTextAutoZoom.setMovementMethod(null);
        // can be added after layout inflation;
        mRxTextAutoZoom.setMaxHeight(RxImageTool.dip2px(55f));
        //don't forget to add min text size programmatically
        mRxTextAutoZoom.setMinTextSize(37f);

        RxTextAutoZoom.setNormalization(getActivity(), llIncludeTitle, mRxTextAutoZoom);

        RxKeyboardTool.hideSoftInput(getActivity());
    }
    private void initData(){
        pbWebBase.setMax(100);//设置加载进度最大值
//        webPath = getIntent().getStringExtra("URL");
        webPath = RxConstants.URL_BAIDU_SEARCH;//加载的URL
        if (webPath.equals("")) {
            webPath = "http://www.baidu.com";
        }
        WebSettings webSettings = webBase.getSettings();
        if (Build.VERSION.SDK_INT >= 19) {
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//加载缓存否则网络
        }

        if (Build.VERSION.SDK_INT >= 19) {
            webSettings.setLoadsImagesAutomatically(true);//图片自动缩放 打开
        } else {
            webSettings.setLoadsImagesAutomatically(false);//图片自动缩放 关闭
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            webBase.setLayerType(View.LAYER_TYPE_SOFTWARE, null);//软件解码
        }
        webBase.setLayerType(View.LAYER_TYPE_HARDWARE, null);//硬件解码

//        webSettings.setAllowContentAccess(true);
//        webSettings.setAllowFileAccessFromFileURLs(true);
//        webSettings.setAppCacheEnabled(true);
   /*     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }*/


        // setMediaPlaybackRequiresUserGesture(boolean require) //是否需要用户手势来播放Media，默认true

        webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
//        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setSupportZoom(true);// 设置可以支持缩放
        webSettings.setBuiltInZoomControls(true);// 设置出现缩放工具 是否使用WebView内置的缩放组件，由浮动在窗口上的缩放控制和手势缩放控制组成，默认false

        webSettings.setDisplayZoomControls(false);//隐藏缩放工具
        webSettings.setUseWideViewPort(true);// 扩大比例的缩放

        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//自适应屏幕
        webSettings.setLoadWithOverviewMode(true);

        webSettings.setDatabaseEnabled(true);//
        webSettings.setSavePassword(true);//保存密码
        webSettings.setDomStorageEnabled(true);//是否开启本地DOM存储  鉴于它的安全特性（任何人都能读取到它，尽管有相应的限制，将敏感数据存储在这里依然不是明智之举），Android 默认是关闭该功能的。
        webBase.setSaveEnabled(true);
        webBase.setKeepScreenOn(true);


        // 设置setWebChromeClient对象
        webBase.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                mRxTextAutoZoom.setText(title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                pbWebBase.setProgress(newProgress);
                super.onProgressChanged(view, newProgress);
            }
        });
        //设置此方法可在WebView中打开链接，反之用浏览器打开
        webBase.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (!webBase.getSettings().getLoadsImagesAutomatically()) {
                    webBase.getSettings().setLoadsImagesAutomatically(true);
                }
                pbWebBase.setVisibility(View.GONE);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                pbWebBase.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url.startsWith("http:") || url.startsWith("https:")) {
                    view.loadUrl(url);
                    return false;
                }


                // Otherwise allow the OS to handle things like tel, mailto, etc.
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
        webBase.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String paramAnonymousString1, String paramAnonymousString2, String paramAnonymousString3, String paramAnonymousString4, long paramAnonymousLong) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(Uri.parse(paramAnonymousString1));
                startActivity(intent);
            }
        });

        webBase.loadUrl(webPath);
        Log.v("帮助类完整连接", webPath);
//        webBase.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,webBase.getHeight()));
    }

    @Override
    public void onSaveInstanceState(Bundle paramBundle) {
        super.onSaveInstanceState(paramBundle);
        paramBundle.putString("url", webBase.getUrl());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        try {
            super.onConfigurationChanged(newConfig);
            if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                Log.v("Himi", "onConfigurationChanged_ORIENTATION_LANDSCAPE");
            } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                Log.v("Himi", "onConfigurationChanged_ORIENTATION_PORTRAIT");
            }
        } catch (Exception ex) {
        }
    }

//    public void onBackPressed() {
//
//        if (webBase.canGoBack()) {
//            webBase.goBack();
//        } else {
//            if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
//                super.onBackPressed();
//                return;
//            } else {
//                Toast.makeText(getContext(), "再次点击返回键退出", Toast.LENGTH_SHORT).show();
//            }
//            mBackPressed = System.currentTimeMillis();
//        }
//
public boolean onBackPressed() {
// 当确认没有子Fragmnt时可以直接return false
    BackHandlerHelper.handleBackPress(this);
    return false;
}




}
