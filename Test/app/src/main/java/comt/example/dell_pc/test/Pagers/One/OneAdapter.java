package comt.example.dell_pc.test.Pagers.One;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.google.gson.Gson;
import comt.example.dell_pc.test.Bean.GoodsBean;
import comt.example.dell_pc.test.R;

public class OneAdapter extends BaseAdapter {

    private Context context;                //传入到OneFragment
    private GoodsBean goodsBean;            //bean文件
    public OneAdapter(Context context,GoodsBean goodsBean) {
        this.context = context;
        this.goodsBean =goodsBean;
    }

    @Override
    public long getItemId(int i) {
        return i;                           //获取指定行对应的id
    }

    @Override
    public Object getItem(int i) {          //获取数据集中数据对应的索引（精确到物品了）
        return goodsBean.getDatas().get(i);
    }

    @Override
    public int getCount() {                 //适配器要显示的数据个数
        if (goodsBean!=null && goodsBean.getDatas()!=null){
            return goodsBean.getDatas().size();
        }
        else {
            Log.i("测试列表", "列表数据有问题");
            Log.i("测试列表", new Gson().toJson(goodsBean));//测试是否有数据，可看到真实数据
            return 0;
        }
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        View goods_item = LayoutInflater.from(context).inflate(R.layout.goods_item,null);
        TextView Id= goods_item.findViewById(R.id.Id);
        TextView name= goods_item.findViewById(R.id.name);
        TextView goodsid= goods_item.findViewById(R.id.goodsid);
        TextView price= goods_item.findViewById(R.id.price);
        TextView size= goods_item.findViewById(R.id.size);

        //setText中，必须所有都为string类型，若是int类型，则前面加上""+
        Id.setText(""+goodsBean.getDatas().get(i).getId());
        name.setText(goodsBean.getDatas().get(i).getName());
        goodsid.setText(""+goodsBean.getDatas().get(i).getGoodsId());
        price.setText(""+goodsBean.getDatas().get(i).getPrice());
        size.setText(""+goodsBean.getDatas().get(i).getSize());
        return goods_item;
    }

    @Override
    public boolean hasStableIds() {						//刷新顺序问题，根据Item来确定id
        return true;
    }
    public void update(GoodsBean goodsBean){				//刷新数据
        this.goodsBean = goodsBean;
        notifyDataSetChanged();								//此方法用于刷新
    }
}
