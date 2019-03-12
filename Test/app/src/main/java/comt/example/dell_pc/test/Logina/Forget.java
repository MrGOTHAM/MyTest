package comt.example.dell_pc.test.Logina;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.kymjs.rxvolley.http.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import comt.example.dell_pc.test.Cache.BaseCache;
import comt.example.dell_pc.test.R;

public class Forget extends AppCompatActivity{
    private static final  String TAG = "192.168.43.2";

    private String session = "";

    public static String host = " 192.168.43.2";

    private EditText tel;
    private EditText newpwd;
    private EditText msg;
    private Button getMsg;
    private Button submit;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        initBaseView();
        initEvent();
    }
    private void initBaseView(){
        tel = findViewById(R.id.tel);
        newpwd = findViewById(R.id.newpwd);
        msg = findViewById(R.id.msg);
        getMsg = findViewById(R.id.getMsg);
        submit = findViewById(R.id.submit);

    }
    private void initEvent(){
        getMsg.setOnClickListener(view -> getMsg());
        submit.setOnClickListener(view -> submit());


    }
    //获取短信验证码
    private void getMsg(){
        HttpParams params = new HttpParams();
        params.put("Tel",tel.getText().toString());
        params.put("options","getMsg");
        HttpCallback callback = new HttpCallback() {
            @Override
            public void onSuccess(Map<String, String> headers, byte[] t) {
                String message = new String(t);
                Log.i(TAG, "onSuccess: "+ message);
                try {
                    JSONObject jsonObject = new JSONObject(message);
                    int status = jsonObject.getInt("status");

                    if (status == 400 ){
                        String msg = jsonObject.getString("msg");
                        Toast.makeText(Forget.this,msg,Toast.LENGTH_LONG).show();
                    }else if (status == 200 ){
                        session = headers.get("Set-Cookie").split(";")[0];
                        Log.i(TAG, "session "+session);
                    }else {
                        Toast.makeText(Forget.this,"发生未知错误！",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(VolleyError error) {
                Toast.makeText(Forget.this,error.getMessage(),Toast.LENGTH_LONG).show();
                Log.i(TAG, "onFailure: "+error.getMessage());
            }
        };
        new RxVolley.Builder()
                .url("http://"+host+":8080"+"/util")
                .httpMethod(RxVolley.Method.POST) //default GET or POST/PUT/DELETE/HEAD/OPTIONS/TRACE/PATCH
                .contentType(RxVolley.ContentType.FORM)//default FORM or JSON
                .params(params)
                .shouldCache(false) //default: get true, post false
                .callback(callback)
                .encoding("UTF-8") //default
                .doTask();
    }
    //提交
    private void submit(){
        HttpParams params = new HttpParams();
        params.put("options","forget");
        params.put("code",msg.getText().toString());
        params.putHeaders("content-type","application/x-www-form-urlencoded");
        params.putHeaders("Cookie",session);
        params.put("Tel",tel.getText().toString());
        params.put("Newpwd",newpwd.getText().toString().trim());
        HttpCallback callback = new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                Log.i(TAG, "onSuccess: "+t);
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    int status = jsonObject.getInt("status");
//                    String userId = jsonObject.getString("userId");
                    if (status == 200){
                        Log.i(TAG, "onSuccess: "+status);
                        Toast.makeText(Forget.this,"找回密码成功！",Toast.LENGTH_LONG).show();
//                        BaseCache.userBean.setId(Integer.parseInt(userId));
                        Intent intent = new Intent(Forget.this, Login.class);
                        startActivity(intent);
                        finish();
                    }else if (status ==400){
                        String msg = jsonObject.getString("msg");

                        Toast.makeText(Forget.this,msg,Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(VolleyError error) {
                Toast.makeText(Forget.this,error.getMessage(),Toast.LENGTH_LONG).show();
                Log.i(TAG, "onFailure: "+error.getMessage());
            }
        };
        new RxVolley.Builder()
                .url("http://"+host+":8080"+"/login")
                .httpMethod(RxVolley.Method.POST) //default GET or POST/PUT/DELETE/HEAD/OPTIONS/TRACE/PATCH
                .contentType(RxVolley.ContentType.FORM)//default FORM or JSON
                .params(params)
                .shouldCache(false) //default: get true, post false
                .callback(callback)
                .encoding("UTF-8") //default
                .doTask();
    }
}