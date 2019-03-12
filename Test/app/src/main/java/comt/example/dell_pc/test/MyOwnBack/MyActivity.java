package comt.example.dell_pc.test.MyOwnBack;

import android.support.v4.app.FragmentActivity;

import com.github.ikidou.fragmentBackHandler.BackHandlerHelper;

public class MyActivity extends FragmentActivity {
    //.....
    @Override
    public void onBackPressed() {
        if (!BackHandlerHelper.handleBackPress(this)) {
            super.onBackPressed();
        }
    }
}
