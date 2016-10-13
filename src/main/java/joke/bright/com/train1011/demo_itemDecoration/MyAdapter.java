package joke.bright.com.train1011.demo_itemDecoration;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import joke.bright.com.train1011.R;

/**
 * Created by chenxiaokang on 2016/10/12.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

    private Context mContext;
    private List<CityBean> mDatas;


    public MyAdapter(Context context, List<CityBean> mDatas){
        mContext = context;
        this.mDatas = mDatas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_rv, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
         holder.mCity.setText(mDatas.get(position).getCity());
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mCity;

        public MyViewHolder(View itemView) {
            super(itemView);

            mCity = (TextView) itemView.findViewById(R.id.city);
        }
    }
}
