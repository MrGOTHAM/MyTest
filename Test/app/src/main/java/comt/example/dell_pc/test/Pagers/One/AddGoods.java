package comt.example.dell_pc.test.Pagers.One;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.kymjs.rxvolley.http.VolleyError;
import org.json.JSONException;
import org.json.JSONObject;
import comt.example.dell_pc.test.R;


public class AddGoods extends AppCompatActivity {
    private static final String TAG = "AddGoods";
    public static String host = "192.168.43.2";

    private View contentView;
    private EditText Id;
    private EditText name;
    private EditText goodsid;
    private EditText size;
    private EditText price;
    private Button submit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_goods);
        initBaseView();

    }

    private void initBaseView(){                        //初始化基本布局
        Id = findViewById(R.id.Id);
        name = findViewById(R.id.name);
        size = findViewById(R.id.size);
        goodsid = findViewById(R.id.goodsid);
        price = findViewById(R.id.price);
        submit = findViewById(R.id.submit);
    submit.setOnClickListener(new View.OnClickListener() {  //提交的点击事件
        @Override
        public void onClick(View view) {
            submit();
        }
    });
    }
    private void submit(){
        HttpParams httpParams = new HttpParams();           //实例化http请求
        httpParams.put("options","addGoods");               //与服务器对应options
        httpParams.put("Id",Id.getText().toString());       //从EditText得到用户输入的 字符串
        httpParams.put("Name",name.getText().toString());
        httpParams.put("Size",size.getText().toString());
        httpParams.put("GoodsId",goodsid.getText().toString());
        httpParams.put("Price",price.getText().toString());

        HttpCallback callback = new HttpCallback() {                     //服务器返回
            @Override
            public void onSuccess(String t) {                           //成功
                Log.i(TAG,"onSuccess:"+t);                          //用来测试是否到了success
                try{
                    JSONObject jsonObject = new JSONObject(t);          //服务器得到的json数据
                    int status = jsonObject.getInt("status");    //从json数据中取出status的值
                    if(status == 200){
                        Toast.makeText(AddGoods.this,"新增商品成功！",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else {
                        Toast.makeText(AddGoods.this,"新增商品失败！",Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){                                  //捕捉异常
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(VolleyError error) {                      //失败
               Toast.makeText(AddGoods.this,"新增商品失败！",Toast.LENGTH_LONG).show();
               Log.i(TAG,"onFailure"+error.getMessage());               //用来测试是否到了failure并得到错误信息
            }
        };
        new RxVolley.Builder()											//服务器配置
                .url("http://"+host+":8080"+"/login")
                .httpMethod(RxVolley.Method.POST) //default GET or POST/PUT/DELETE/HEAD/OPTIONS/TRACE/PATCH
                .contentType(RxVolley.ContentType.FORM)//default FORM or JSON
                .params(httpParams)                                     //http实例化成什么，这括号内就写什么
                .shouldCache(false) //default: get true, post false
                .callback(callback)
                .encoding("UTF-8") //default
                .doTask();
    }
}
