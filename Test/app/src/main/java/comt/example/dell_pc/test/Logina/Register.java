package comt.example.dell_pc.test.Logina;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import comt.example.dell_pc.test.MainActivity;
import comt.example.dell_pc.test.R;

public class Register extends AppCompatActivity{
    private static final String TAG = "Register";
    public static String host = "192.168.43.2";

    private String session = "";                //验证码session
    private TimeCount time;                     //验证码60秒，每秒刷新

    private EditText tel;
    private EditText pwd;
    private EditText name;
    private CheckBox ok;
    private EditText msg;
    private Button getMsg;
    private Button register;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        initEvent();
    }
    private void initView(){
    tel = findViewById(R.id.tel);
    pwd = findViewById(R.id.pwd);
    name = findViewById(R.id.name);
    ok = findViewById(R.id.ok);
    msg = findViewById(R.id.msg);
    getMsg =findViewById(R.id.getMsg);
    register = findViewById(R.id.register);

    }
    private void initEvent(){
        ok.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                register.setEnabled(true);         //setEnabled(true)设置按钮为可点击
            }else {
                register.setEnabled(false);        //setEnabled(false)设置按钮为不可点击
            }
        });

        time = new TimeCount(60000, 1000);
        getMsg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                time.start();           //验证码倒计时功能
                getMsg();
            }
        });

        register.setOnClickListener(view -> register());
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
                        Toast.makeText(Register.this,msg,Toast.LENGTH_LONG).show();
                    }else if (status == 200 ){
                        session = headers.get("Set-Cookie").split(";")[0];
                        Log.i(TAG, "session "+session);
                    }else {
                        Toast.makeText(Register.this,"发生未知错误！",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(VolleyError error) {
                Toast.makeText(Register.this,error.getMessage(),Toast.LENGTH_LONG).show();
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
    private void register(){
        HttpParams params = new HttpParams();
        params.put("options","register");
        params.put("code",msg.getText().toString());
        params.putHeaders("content-type","application/x-www-form-urlencoded");
        params.putHeaders("Cookie",session);
        params.put("Tel",tel.getText().toString());
        params.put("Pwd",pwd.getText().toString().trim());
        params.put("Name",name.getText().toString());
        HttpCallback callback = new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                Log.i(TAG, "onSuccess: "+t);
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    int status = jsonObject.getInt("status");
                    Log.i(TAG, "onSuccess: "+status);

                    if (status == 200){
                        String userId = jsonObject.getString("userId");
                        BaseCache.userBean.setId(Integer.parseInt(userId));
                        Toast.makeText(Register.this,"恭喜您！注册成功！",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Register.this, Login.class);
                        startActivity(intent);
                        finish();
                    }
                    if (status == 400){
                        String msg = jsonObject.getString("msg");
                        Toast.makeText(Register.this,msg,Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(VolleyError error) {
                Toast.makeText(Register.this,error.getMessage(),Toast.LENGTH_LONG).show();
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
    //内部类
    class TimeCount extends CountDownTimer {        //验证码倒计时    内部类

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            getMsg.setBackgroundColor(Color.parseColor("#B6B6D8"));
            getMsg.setClickable(false);
            getMsg.setText("("+millisUntilFinished / 1000 +") 秒后可重新发送");
        }

        @Override
        public void onFinish() {
            getMsg.setText("重新获取验证码");
            getMsg.setClickable(true);
            getMsg.setBackgroundColor(Color.parseColor("#4EB84A"));

        }
    }
}