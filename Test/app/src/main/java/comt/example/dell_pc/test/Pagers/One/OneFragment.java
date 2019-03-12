package comt.example.dell_pc.test.Pagers.One;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.kymjs.rxvolley.http.VolleyError;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import comt.example.dell_pc.test.Bean.GoodsBean;
import comt.example.dell_pc.test.Cache.GoodsCache;
import comt.example.dell_pc.test.R;
import comt.example.dell_pc.test.Utils.PreferenceUtil;


import static comt.example.dell_pc.test.Cache.GoodsCache.goodsBean;


public class OneFragment extends Fragment {
    private static  final String TAG="OneFragment";
    public static String host = "192.168.43.2";

    private View contentView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Gson gson = new Gson();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<Integer> imgs = new ArrayList<>();
    private ListView listview1;
    private Banner banner;
    private Button add;
    private OneAdapter adapter;
    RefreshLayout refreshLayout;
    int index =1;


    public OneFragment() {
        // Required empty public constructor
    }

    public static OneFragment newInstance(String param1, String param2) {
        OneFragment fragment = new OneFragment();
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
        if (contentView == null){
            contentView = inflater.inflate(R.layout.fragment_one, container, false);
            initBaseView();     //初始化界面布局
            initBanner();
            initList();
            initRefresh();

        }
        ViewGroup parent = (ViewGroup) contentView.getParent();
        if ( parent!=null ){                       //调用父类的remove方法，防止再次取出parent
            parent.removeView(contentView);        //移除父类控件。防止bug一直重载
        }
        return contentView;
    }

    private void initBaseView(){                //获取基本控件
        banner = contentView.findViewById(R.id.banner);
        listview1 = contentView.findViewById(R.id.listview1);
        add = contentView.findViewById(R.id.add);
        refreshLayout = contentView.findViewById(R.id.refreshLayout);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取上下文方式传递，仅四大组件是Intent intent = new Intent(OneFragment.this,AddGoods.class);
                Intent intent = new Intent(getContext(),AddGoods.class);
                startActivity(intent);
            }
        });
}
private void initList(){            //初始化list列表
        //PreferenceUtil为   安卓常用数据存储封装
        String strGoods = PreferenceUtil.getString("goodsBean","");
        if (strGoods != ""){
            GoodsCache.goodsBean = gson.fromJson(strGoods,GoodsBean.class);     //OneFragment是空的，调用缓存加载已有数据

        }
        adapter = new OneAdapter(getContext(),goodsBean);   //getContext()方法从oneadapter传入数据
        listview1.setAdapter(adapter);
        HttpParams params = new HttpParams();               //http协议
        params.put("options","getGoods");//options与后台的options对应
        params.put("Id",GoodsCache.initListBean.getId() );      //initListBean模型数据，仅仅为了初始化数据
        params.put("index",1);
        params.put("size",10);
        HttpCallback callback = new HttpCallback() {        //后台收到请求后返回的数据
        @Override
        public void onSuccess(String t) {               //成功
            Log.i("initList","onSuccess:"+t);//打印成功日志
            try {
                JSONObject jsonObject = new JSONObject(t);  //json解析后台数据
                int status = jsonObject.getInt("status");   //从解析的数据中得到status值
                if (status == 200) {
                GoodsCache.goodsBean = gson.fromJson(t, GoodsBean.class);  //将Json文件转化为bean文件
                    //PreferenceUtil.commitString   用来存储数据
                    //内存：app关掉后就直接消失   只是用来快速读取数据
                    //PreferenceUtil.commitString   无论是否开启app，都是存储状态
                PreferenceUtil.commitString("goodsBean",t);
                    if (adapter!=null){
                        adapter.update(GoodsCache.goodsBean);
                    }
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        public void onFailure(VolleyError error) {                          //失败
            Log.i("initList", "onFailure: "+error.getMessage());//失败日志
        }
    };
    new RxVolley.Builder()														//服务器配置
            .url("http://"+host+":8080"+"/login")     //手机与后台同一个局域网，除非服务器放公网
            .httpMethod(RxVolley.Method.POST) //default GET or POST/PUT/DELETE/HEAD/OPTIONS/TRACE/PATCH
            .contentType(RxVolley.ContentType.FORM)//default FORM or JSON
            .params(params)
            .shouldCache(false) //default: get true, post false
            .callback(callback)
            .encoding("UTF-8") //default
            .doTask();
}

//刷新需要在Gradle中添加两个依赖
// 刷新依赖
//    implementation 'com.scwang.smartrefresh:SmartRefreshLayout:1.1.0-alpha-12'
//    implementation 'com.scwang.smartrefresh:SmartRefreshHeader:1.1.0-alpha-12'//没有使用特殊Header，可以不加这行

    //在此Fragment布局中，需要将线性布局改成   com.scwang.smartrefresh.layout.SmartRefreshLayout
    //并且  加入android:id="@+id/refreshLayout"
    //声明RefreshLayout  后  在此class中需要获取对应控件
    //最后写此方法

        private void initRefresh(){             //刷新和加载（加载未写）
            refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {     //加载（未写）
                    index =index+1;
                    HttpParams params = new HttpParams();
                    params.put("options","getGoods");
                    params.put("size",10);
                    params.put("index",index);
                    Log.i("index","is:"+index);
                    HttpCallback callback = new HttpCallback() {
                        @Override
                        public void onSuccess(String t) {
                            refreshLayout.finishLoadMore();
                            Log.i("LoadMore", "onSuccess: "+t);	//打印成功日志
                            try {
                                JSONObject jsonObject = new JSONObject(t);
                                int status = jsonObject.getInt("status");
                                int count = jsonObject.getInt("count");

                                if (index<=(count/10+1)){
                                    Log.i("count/10", "is: "+count/10);
                                if ( status == 200 ){
                                    GoodsBean goodsBean = gson.fromJson(t,GoodsBean.class);  //新列表
                                    if (goodsBean!=null){
                                        List<GoodsBean.DatasBean> datasBean = GoodsCache.goodsBean.getDatas();//老列表
                                        for (int i=0;i<goodsBean.getDatas().size();i++){
                                            datasBean.add(goodsBean.getDatas().get(i));             //新的+老的数据
                                        }
                                        GoodsCache.goodsBean.setDatas(datasBean);                   //把新+老一起放回goodsBean
                                    }
                                    PreferenceUtil.commitString("goodsBean",gson.toJson(GoodsCache.goodsBean));//缓存
                                    if (adapter!=null){                             //如果OneAdapter不空
                                        adapter.update(GoodsCache.goodsBean);       //刷新goods.Bean
                                    }
                                }
                            }else {
                                    Toast.makeText(getContext(),"没有更多了！ ",Toast.LENGTH_LONG).show();
                                }}
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                            refreshLayout.finishLoadMore(1000);
                        }

                        @Override
                        public void onFailure(VolleyError error) {
                            refreshLayout.finishLoadMore(1000);
                            Log.i("LoadMore", "onFailure: "+error.getMessage());//打印失败日志
                        }
                    };
                    new RxVolley.Builder()										//服务器配置
                            .url("http://"+host+":8080"+"/login")
                            .httpMethod(RxVolley.Method.POST) //default GET or POST/PUT/DELETE/HEAD/OPTIONS/TRACE/PATCH
                            .contentType(RxVolley.ContentType.FORM)//default FORM or JSON
                            .params(params)
                            .shouldCache(false) //default: get true, post false
                            .callback(callback)
                            .encoding("UTF-8") //default
                            .doTask();
                }
            });
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                index=1;
                HttpParams params = new HttpParams();
                params.put("options","getGoods");
                params.put("index",index);                  //第一页
                params.put("size",10);                  //显示的商品个数
                HttpCallback callback = new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        refreshLayout.finishRefresh();              //结束刷新
                        Log.i("Refresh","onSuccess"+t);
                        try{
                            JSONObject jsonObject = new JSONObject(t);
                            int status = jsonObject.getInt("status");
                            if (status == 200){
                                goodsBean = gson.fromJson(t, GoodsBean.class);  //将Json文件转化为bean文件
                                PreferenceUtil.commitString("goodsBean",t);
                                if (adapter!=null){
                                    adapter.update(GoodsCache.goodsBean);
                                }
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(VolleyError error) {
                        refreshLayout.finishRefresh();
                        Log.i("Refresh", "onFailure: "+error.getMessage());//打印失败日志
                    }
                };
                new RxVolley.Builder()										//服务器配置
                        .url("http://"+host+":8080"+"/login")
                        .httpMethod(RxVolley.Method.POST) //default GET or POST/PUT/DELETE/HEAD/OPTIONS/TRACE/PATCH
                        .contentType(RxVolley.ContentType.FORM)//default FORM or JSON
                        .params(params)
                        .shouldCache(false) //default: get true, post false
                        .callback(callback)
                        .encoding("UTF-8") //default
                        .doTask();
            }
        });
        }
    //顶部轮播
    private void initBanner(){

        imgs.add(R.mipmap.cover1);
        imgs.add(R.mipmap.cover2);
        imgs.add(R.mipmap.cover3);

        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.setIndicatorGravity(BannerConfig.RIGHT);
        banner.setImages(imgs);
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Toast.makeText(getContext(),"点击了第"+position+"个广告",Toast.LENGTH_LONG).show();
            }
        });
        //banner设置方法全部调用完毕时最后调用
        banner.start();

    }
}
