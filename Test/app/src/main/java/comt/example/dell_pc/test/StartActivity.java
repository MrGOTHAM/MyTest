package comt.example.dell_pc.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import comt.example.dell_pc.test.Logina.Login;
import comt.example.dell_pc.test.Utils.PreferenceUtil;


public class StartActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        initiflogin();
}
private void initiflogin(){
    if ( PreferenceUtil.getString("TEL","").isEmpty()
            && PreferenceUtil.getString("PWD","").isEmpty())//自动登录判断，SharePrefences中有数据，则跳转到主页，没数据则跳转到登录页
    {
        Intent intent2 = new Intent(StartActivity.this, Login.class);
        startActivity(intent2);
        finish();

    } else {
        Intent intent = new Intent(StartActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}



}