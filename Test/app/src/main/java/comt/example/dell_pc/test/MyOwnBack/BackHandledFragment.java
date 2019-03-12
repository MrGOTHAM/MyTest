package comt.example.dell_pc.test.MyOwnBack;

import android.support.v4.app.Fragment;

import com.github.ikidou.fragmentBackHandler.BackHandlerHelper;
import com.github.ikidou.fragmentBackHandler.FragmentBackHandler;

//没有处理back键需求的Fragment不用实现
public abstract class BackHandledFragment extends Fragment implements FragmentBackHandler {
    @Override
    public boolean onBackPressed() {
        return BackHandlerHelper.handleBackPress(this);
    }
}
