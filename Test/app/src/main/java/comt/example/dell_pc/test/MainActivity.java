package comt.example.dell_pc.test;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import comt.example.dell_pc.test.Navigation.MyPagerAdapter;
import comt.example.dell_pc.test.Pagers.Five.FiveFragment;
import comt.example.dell_pc.test.Pagers.Four.FourFragment;
import comt.example.dell_pc.test.Pagers.One.OneFragment;
import comt.example.dell_pc.test.Pagers.Three.ThreeFragment;
import comt.example.dell_pc.test.Pagers.Two.TwoFragment;

//我继承application的类为UmengApplication   所有初始化都务必在UmengApplication中的onCreate中完成

public class MainActivity extends AppCompatActivity {

    //高德地图 Android6.0以上动态权限
    //定位需要的权限
    protected String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
    };
    //跟权限相关
    private static final int PERMISSON_REQUESTCODE = 0;

    private TextView mTextMessage;
    private MyPagerAdapter adapter;
    private ViewPager pager;
    private MenuItem menuItem;
    private BottomNavigationView navigation;



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButtonNavigation();     //底部导航栏
        initBaseView();         //加载MainActivity的布局控件
        pagers();               //加载Fragment页面
        TopBackground();        //加载顶部状态栏颜色
        checkPermissions(needPermissions);     //检查权限

    }
//    ---**************************权限申请处****************************************************

    /**
     * 检查定位权限
     *
     * @param needPermissions
     */
    private void checkPermissions(String[] needPermissions) {
        //获取权限列表
        List<String> needRequestPermissionList = findDeniedPermission(needPermissions);
        //如果没有权限就申请
        if (needRequestPermissionList != null && needRequestPermissionList.size() > 0) {
            //List.toArray将集合转换为数组
            ActivityCompat.requestPermissions(this,
                    needRequestPermissionList.toArray(new String[needRequestPermissionList.size()]),
                    PERMISSON_REQUESTCODE);		//在上面定义为静态并且初始化为0
        }
    }
    /**
     * 获取集中需要申请权限的列表
     *
     * @param needPermissions
     * @return
     */
    private List<String> findDeniedPermission(String[] needPermissions) {
        List<String> needRequestPermissionList = new ArrayList<String>();
        //for (循环变量类型 循环变量名称 : 要被遍历的对象)
        for (String permisson : needPermissions) {
            if (ContextCompat.checkSelfPermission(this, permisson) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, permisson)) {
                needRequestPermissionList.add(permisson);
            }
        }
        return needRequestPermissionList;
    }
    //*****************************************----initBaseView----*********************************************
    //加载布局中控件
    private void initBaseView(){
        mTextMessage =  findViewById(R.id.message);
        navigation = findViewById(R.id.navigation);
        pager = findViewById(R.id.pager);

    }

    //*****************************************----TopBackground----************************************************
    //修改顶部状态栏颜色
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void TopBackground(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
//设置修改状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//设置状态栏的颜色，和你的app主题或者标题栏颜色设置一致就ok了
            window.setStatusBarColor(getColor(R.color.black));
        }
    }


    //**********************************-----ButtomNavigation--Z---**************************************************
    //底部导航栏
    private void ButtonNavigation() {
        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        mTextMessage.setText(R.string.title_home);
                        pager.setCurrentItem(0);
                        return true;
                    case R.id.navigation_dashboard:
                        mTextMessage.setText(R.string.title_dashboard);
                        pager.setCurrentItem(1);
                        return true;
                    case R.id.navigation_notifications:
                        mTextMessage.setText(R.string.title_notifications);
                        pager.setCurrentItem(2);
                        return true;
                    case R.id.navigation_update:
                        mTextMessage.setText(R.string.title_notifications);
                        pager.setCurrentItem(3);
                        return true;
                    case R.id.navigation_update2:
                        mTextMessage.setText(R.string.title_notifications);
                        pager.setCurrentItem(4);
                        return true;
                }
                return false;
            }
        };
        }

    //***************************************----pagers----***************************************************
    //加入fragment
    private void pagers(){
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(OneFragment.newInstance("",""));
        adapter.addFragment(TwoFragment.newInstance("",""));
        adapter.addFragment(ThreeFragment.newInstance("",""));
        adapter.addFragment(FourFragment.newInstance("",""));
        adapter.addFragment(FiveFragment.newInstance("",""));
        ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    navigation.getMenu().getItem(0).setChecked(false);
                }
                menuItem = navigation.getMenu().getItem(position);
                menuItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        pager.addOnPageChangeListener(pageChangeListener);
        pager.setOffscreenPageLimit(5);
        pager.setAdapter(adapter);
    }

    }

