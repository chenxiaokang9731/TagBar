package joke.bright.com.train1011.demo_itemDecoration;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import joke.bright.com.train1011.R;

public class MainActivity extends Activity {

    private TagBar tb;
    private RecyclerView rv;
    private List<CityBean> mDatas;
    private TextView mTv;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv = (RecyclerView) findViewById(R.id.rv);
        mTv = (TextView) findViewById(R.id.tv);
        tb = (TagBar) findViewById(R.id.tb);

        initData(getResources().getStringArray(R.array.provinces));

        rv.setLayoutManager(mLayoutManager = new LinearLayoutManager(this));
        rv.setAdapter(new MyAdapter(this, mDatas));
        rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        rv.addItemDecoration(new MyItemDecoration(this, mDatas));

        tb.setSourceDatas(mDatas).setTagPressedTextView(mTv).setLayoutManager(mLayoutManager);
    }

    /**
     * 城市列表数据源
     * @param datas
     */
    private void initData(String[] datas) {
        mDatas = new ArrayList<>();

        for(int i=0; i<datas.length; i++){
            CityBean cb = new CityBean();
            cb.setCity(datas[i]);
            mDatas.add(cb);
        }

//        mDatas.add(new CityBean("A", "安徽"));
//        mDatas.add(new CityBean("B", "北京"));
//        mDatas.add(new CityBean("F", "福建"));
//        mDatas.add(new CityBean("G", "广东"));
//        mDatas.add(new CityBean("G", "甘肃"));
//        mDatas.add(new CityBean("G", "贵州"));
//        mDatas.add(new CityBean("G", "广西"));
//        mDatas.add(new CityBean("H", "河南"));
//        mDatas.add(new CityBean("H", "湖北"));
//        mDatas.add(new CityBean("H", "湖南"));
//        mDatas.add(new CityBean("H", "河北"));
//        mDatas.add(new CityBean("J", "江苏"));
//        mDatas.add(new CityBean("J", "江苏"));
//        mDatas.add(new CityBean("J", "江苏"));
//        mDatas.add(new CityBean("J", "江苏"));
//        mDatas.add(new CityBean("J", "江苏"));
//        mDatas.add(new CityBean("J", "江苏"));
//        mDatas.add(new CityBean("J", "江苏"));
//        mDatas.add(new CityBean("J", "江苏"));
//        mDatas.add(new CityBean("J", "江苏"));
//        mDatas.add(new CityBean("J", "江苏"));
//        mDatas.add(new CityBean("J", "江苏"));
//        mDatas.add(new CityBean("J", "江苏"));
//        mDatas.add(new CityBean("J", "江苏"));
//        mDatas.add(new CityBean("J", "江苏"));
//        mDatas.add(new CityBean("J", "江苏"));
//        mDatas.add(new CityBean("R", "日本"));
//        mDatas.add(new CityBean("R", "日本"));
//        mDatas.add(new CityBean("R", "日本"));
//        mDatas.add(new CityBean("R", "日本"));
    }
}
