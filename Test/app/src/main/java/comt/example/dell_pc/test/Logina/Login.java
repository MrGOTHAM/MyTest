package comt.example.dell_pc.test.Logina;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.kymjs.rxvolley.http.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import comt.example.dell_pc.test.MainActivity;
import comt.example.dell_pc.test.R;
import comt.example.dell_pc.test.Utils.PreferenceUtil;

public class Login extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "Login";
    public static String host = "192.168.43.2";

    View contentView;
    EditText tel;
    EditText pwd;
    Button login;
    String TEL;
    String PWD;
    CheckBox checkbox;
    TextView register;
    TextView forget;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initBaseView();
        initEvent();
    }

    private void initBaseView(){
        register = findViewById(R.id.register);
        forget = findViewById(R.id.forget);
        tel = findViewById(R.id.tel);                       //old
        pwd = findViewById(R.id.pwd);                       //old
        login = findViewById(R.id.login);
        checkbox =findViewById(R.id.checkbox);

            String tel1 = PreferenceUtil.getString("TEL","");
            String pwd1 = PreferenceUtil.getString("PWD","");
            Boolean check = PreferenceUtil.getBoolean("Check",false);
            checkbox.setChecked(check);                             //初始化勾选状态
            Log.i(TAG, "doLogin: tel --- "+tel1+",pwd --- "+pwd1);
            if (checkbox.isChecked()){
                tel.setText(tel1);                                 //perference缓存的字符串取出放入tel控件中
                pwd.setText(pwd1);                                 //perference缓存的字符串取出放入pwd控件中
            }
    }

    private void initEvent(){
        register.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this,Register.class);
            startActivity(intent);
        });
        forget.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this,Forget.class);
            startActivity(intent);
        });
       /* login.setOnClickListener(v -> {
            TEL = tel.getText().toString();                 //new
            PWD = pwd.getText().toString().trim();          //new
//            login(TEL,PWD);
        });*/
        login.setOnClickListener(this);//按钮注册点击事件
        //FileUtil.readFromFile(this,TEL,PWD);//从文件中读取用户名和密码
        //FileUtil.readFromSDCard(this, TEL, PWD);//从sd卡中读取用户名和密码
        //FileUtil.readFromFile2(this, TEL, PWD);
//        FileUtil.readFromPre(this, tel, pwd);
    }
    //实现 了android.view.View中的OnClickListener接口，必须实现的方法
    @Override
    public void onClick(View v) {
        //通过获取id来进行多个事件，此处只为按钮添加了判断登录事件
        switch (v.getId()) {
            case R.id.login:
                doLogin();//点击按钮时，做登录判断
                break;
        }
    }

    private void doLogin() {
        //toString()不能少,getText()是一个EditText对象

        String TEL = tel.getText().toString();
        String PWD = pwd.getText().toString();

    HttpParams params = new HttpParams();
    params.put("options","login");
    params.put("Pwd",PWD);
    params.put("Tel",TEL);
        HttpCallback callback = new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                Log.d(TAG, "点击了按钮"+TEL+","+PWD);
                //将输入框输入的用户名和密码保存到本地的文件中
                if(checkbox.isChecked())//如果选中了"记住我"的多选框，就将用户名和密码保存，否则不保存
                {
                    //FileUtil.saveToFile(this,name,pass);
                    //FileUtil.savtToSDCard(this,name,pass);
                    //FileUtil.saveToFile2(this, name, pass);
//            FileUtil.saveToPre(this, TEL, PWD);
                    PreferenceUtil.commitString("TEL",TEL);                 //将TEL存入PreferenceUtil
                    PreferenceUtil.commitString("PWD",PWD);                 //将PWD存入PreferenceUtil
                    PreferenceUtil.commitBoolean("Check",true);
                }
                else {
                    PreferenceUtil.commitBoolean("Check",false);
                }

                Log.i(TAG, "onSuccess:" + t);
                try{
                    JSONObject jsonObject = new JSONObject(t);
                    int status = jsonObject.getInt("status");
                    String msg = jsonObject.getString("msg");
                    if (status == 200){
                        Toast.makeText(Login.this,"welcome to gotham ",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(Login.this,MainActivity.class));
                        finish();
                    }
                    if (status == 400){
                        Toast.makeText(Login.this,msg,Toast.LENGTH_LONG).show();

                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(VolleyError error) {
                Log.i(TAG,"onFailure:"+ error.getMessage());
                Toast.makeText(Login.this,"你怕是没开服务器哟！",Toast.LENGTH_LONG).show();
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
                .doTask();}   }