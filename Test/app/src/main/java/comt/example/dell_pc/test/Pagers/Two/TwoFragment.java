package comt.example.dell_pc.test.Pagers.Two;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.vondear.rxfeature.tool.RxBarCode;
import com.vondear.rxfeature.tool.RxQRCode;
import com.vondear.rxtool.RxActivityTool;
import com.vondear.rxtool.RxDataTool;
import com.vondear.rxtool.RxSPTool;
import com.vondear.rxtool.view.RxToast;
import com.vondear.rxui.view.RxTitle;
import com.vondear.rxui.view.ticker.RxTickerUtils;
import com.vondear.rxui.view.ticker.RxTickerView;
import comt.example.dell_pc.test.R;
import static com.vondear.rxtool.RxConstants.SP_MADE_CODE;


public class TwoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View contentView;

    //Rxtools
    private static final char[] NUMBER_LIST = RxTickerUtils.getDefaultNumberList();
    private RxTitle mRxTitle;
    private EditText mEtQrCode;
    private ImageView mIvCreateQrCode;
    private ImageView mIvQrCode;
    private LinearLayout mActivityCodeTool;
    private LinearLayout mLlCode;
    private LinearLayout mLlQrRoot;
    private EditText mEtBarCode;
    private ImageView mIvCreateBarCode;
    private ImageView mIvBarCode;
    private LinearLayout mLlBarCode;
    private LinearLayout mLlBarRoot;
    private LinearLayout mLlScaner;
    private LinearLayout mLlQr;
    private LinearLayout mLlBar;
    private RxTickerView mRxTickerViewMade;
    private RxTickerView mRxTickerViewScan;
    private NestedScrollView nestedScrollView;

    public TwoFragment mContext;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public TwoFragment() {
        // Required empty public constructor
    }


    public static TwoFragment newInstance(String param1, String param2) {
        TwoFragment fragment = new TwoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contentView = inflater.inflate(R.layout.fragment_two, container, false);
        initView();
        initEvent();
        return contentView;
    }
    @Override
    public void onResume() {            //一般是覆盖activity然后dialog置顶
        super.onResume();
//        updateScanCodeCount();      //扫码次数
    }

    protected void initView() {
//        mRxTitle =  contentView.findViewById(com.vondear.rxfeature.R.id.rx_title);        //标题
        mEtQrCode =  contentView.findViewById(com.vondear.rxfeature.R.id.et_qr_code);
        mIvCreateQrCode = contentView.findViewById(com.vondear.rxfeature.R.id.iv_create_qr_code);
        mIvQrCode =  contentView.findViewById(com.vondear.rxfeature.R.id.iv_qr_code);
        mActivityCodeTool =  contentView.findViewById(com.vondear.rxfeature.R.id.activity_code_tool);
        mLlCode =  contentView.findViewById(com.vondear.rxfeature.R.id.ll_code);
        mLlQrRoot =  contentView.findViewById(com.vondear.rxfeature.R.id.ll_qr_root);

        nestedScrollView =  contentView.findViewById(com.vondear.rxfeature.R.id.nestedScrollView);

        mEtBarCode =  contentView.findViewById(com.vondear.rxfeature.R.id.et_bar_code);
        mIvCreateBarCode =  contentView.findViewById(com.vondear.rxfeature.R.id.iv_create_bar_code);
        mIvBarCode = contentView.findViewById(com.vondear.rxfeature.R.id.iv_bar_code);
        mLlBarCode =  contentView.findViewById(com.vondear.rxfeature.R.id.ll_bar_code);
        mLlBarRoot = contentView.findViewById(com.vondear.rxfeature.R.id.ll_bar_root);
        mLlScaner = contentView.findViewById(com.vondear.rxfeature.R.id.ll_scaner);
        mLlQr = contentView.findViewById(com.vondear.rxfeature.R.id.ll_qr);
        mLlBar = contentView.findViewById(com.vondear.rxfeature.R.id.ll_bar);

//        mRxTickerViewScan =  contentView.findViewById(com.vondear.rxfeature.R.id.ticker_scan_count);      //扫码次数
//        mRxTickerViewScan.setCharacterList(NUMBER_LIST);

//        mRxTickerViewMade =contentView.findViewById(com.vondear.rxfeature.R.id.ticker_made_count);        //生成次数
//        mRxTickerViewMade.setCharacterList(NUMBER_LIST);
//        updateMadeCodeCount();                                                                            //更新生成次数
    }

//    private void updateScanCodeCount() {          //扫码次数
//        mRxTickerViewScan.setText(RxDataTool.stringToInt(RxSPTool.getContent(getContext(), SP_SCAN_CODE)) + "", true);
//    }

//  private void updateMadeCodeCount() {          //生成次数
//       mRxTickerViewMade.setText(RxDataTool.stringToInt(RxSPTool.getContent(getContext(), SP_MADE_CODE)) + "", true);
//    }

    private void initEvent() {
            //点击事件，监听并写出逻辑生成二维码
        mIvCreateQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = mEtQrCode.getText().toString();
                if (RxDataTool.isNullString(str)) {
                    RxToast.error("二维码文字内容不能为空！");
                } else {
                    mLlCode.setVisibility(View.VISIBLE);

                    //二维码生成方式一  推荐此方法
                    RxQRCode.builder(str).
                            backColor(0xFFFFFFFF).
                            codeColor(0xFF000000).
                            codeSide(600).
                            into(mIvQrCode);

                    //二维码生成方式二 默认宽和高都为800 背景为白色 二维码为黑色
                    // RxQRCode.createQRCode(str,mIvQrCode);

                    mIvQrCode.setVisibility(View.VISIBLE);

                    RxToast.success("二维码已生成!");

                    RxSPTool.putContent(getContext(), SP_MADE_CODE, RxDataTool.stringToInt(RxSPTool.getContent(getContext(), SP_MADE_CODE)) + 1 + "");

//                    updateMadeCodeCount();

                    nestedScrollView.computeScroll();
                }
            }
        });

                //点击事件，监听并声称条形码
        mIvCreateBarCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str1 = mEtBarCode.getText().toString();
                if (RxDataTool.isNullString(str1)) {
                    RxToast.error("条形码文字内容不能为空！");
                } else {
                    mLlBarCode.setVisibility(View.VISIBLE);         //可见

                    //条形码生成方式一  推荐此方法
                    RxBarCode.builder(str1).
                            backColor(0x00000000).
                            codeColor(0xFF000000).
                            codeWidth(1000).
                            codeHeight(300).
                            into(mIvBarCode);

                    //条形码生成方式二  默认宽为1000 高为300 背景为白色 二维码为黑色
                    //mIvBarCode.setImageBitmap(RxBarCode.createBarCode(str1, 1000, 300));

                    mIvBarCode.setVisibility(View.VISIBLE);         //可见

                    RxToast.success("条形码已生成!");
                    //每生成一个码都加一

//                    RxSPTool.putContent(getContext(), SP_MADE_CODE, RxDataTool.stringToInt(RxSPTool.getContent(getContext(), SP_MADE_CODE)) + 1 + "");

//                    updateMadeCodeCount();                                                        //更新生成次数
                }
            }
        });
                //扫码监听
        mLlScaner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    //跳过当前Activity调用ActivityScanerCode
                RxActivityTool.skipActivity(getContext(), Scan.class);
            }
        });

        mLlQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //将二维码展现出来
                mLlQrRoot.setVisibility(View.VISIBLE);          //view可见
                //将条形码隐藏
                mLlBarRoot.setVisibility(View.GONE);            //view不可见且不占空间
            }
        });
                                                                    //view.VISIBLE   可见
        mLlBar.setOnClickListener(new View.OnClickListener() {      //View.GONE      不可见且不占空间
            @Override                                               //view.INVISIBLE 不可见且占居显示时的空间
            public void onClick(View v) {
                //将条形码展现出来
                mLlBarRoot.setVisibility(View.VISIBLE);         //view可见
                //将二维码隐藏
                mLlQrRoot.setVisibility(View.GONE);             //view不可见且不占空间
            }
        });
    }
}
