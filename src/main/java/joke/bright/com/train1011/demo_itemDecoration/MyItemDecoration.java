package joke.bright.com.train1011.demo_itemDecoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import java.util.List;

/**
 * Created by chenxiaokang on 2016/10/12.
 */
public class MyItemDecoration extends RecyclerView.ItemDecoration{

    private List<CityBean> mDatas;
    private Paint mPaint;
    private Rect rect;

    private static int COLOR_TITLE_BG = Color.parseColor("#FFDFDFDF");
    private static int COLOR_TITLE_FONT = Color.parseColor("#FF000000");
    /**
     *  Tag标题高度
     */
    private int myTitleHeight;

    public MyItemDecoration(Context context, List<CityBean> mDatas){
        myTitleHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, context.getResources().getDisplayMetrics());
        this.mDatas = mDatas;
        mPaint = new Paint();
        mPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, context.getResources().getDisplayMetrics()));
        rect = new Rect();
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();
        for(int i=0; i<childCount; i++){
            View view = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
            int position = params.getViewLayoutPosition();
            if(position>-1){
                if(position == 0){
                    drawTitleArea(c, left, right, view, params, position);
                }else {
                    if(null != mDatas.get(position).getTag() &&
                            !mDatas.get(position).getTag().equals(mDatas.get(position-1).getTag())){
                        drawTitleArea(c, left, right, view, params, position);
                    }else {
                    }
                }
            }
        }
    }

    private void drawTitleArea(Canvas c, int left, int right, View child, RecyclerView.LayoutParams params, int pos){
        //画title区域
        mPaint.setColor(COLOR_TITLE_BG);
        c.drawRect(left, child.getTop()-params.topMargin-myTitleHeight, right, child.getTop()-params.topMargin, mPaint);
        //画文字
        mPaint.setColor(COLOR_TITLE_FONT);
        mPaint.getTextBounds(mDatas.get(pos).getTag(), 0, mDatas.get(pos).getTag().length(), rect);
        c.drawText(mDatas.get(pos).getTag(), child.getPaddingLeft(), child.getTop()-params.topMargin-(myTitleHeight/2-rect.height()/2), mPaint);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int position = ((LinearLayoutManager) parent.getLayoutManager()).findFirstVisibleItemPosition();
        String tag = mDatas.get(position).getTag();
        View child = parent.findViewHolderForLayoutPosition(position).itemView;

        boolean flag = false;
        if (null != tag && !tag.equals(mDatas.get(position + 1).getTag())){
            if(child.getHeight()+child.getTop()<myTitleHeight){
                c.save();

                flag = true;

                //可与123行 c.drawRect 比较，只有bottom参数不一样，由于 child.getHeight() + child.getTop() < mTitleHeight，所以绘制区域是在不断的减小，有种折叠起来的感觉
                //c.clipRect(parent.getPaddingLeft(), parent.getPaddingTop(), parent.getRight() - parent.getPaddingRight(), parent.getPaddingTop() + child.getHeight() + child.getTop());

                //类似饿了么点餐时,商品列表的悬停头部切换“动画效果”
                //上滑时，将canvas上移 （y为负数） ,所以后面canvas 画出来的Rect和Text都上移了，有种切换的“动画”感觉
                c.translate(0, child.getHeight()+child.getTop()-myTitleHeight);
            }
        }

        //画title区域
        mPaint.setColor(COLOR_TITLE_BG);
        c.drawRect(parent.getPaddingLeft(), parent.getPaddingTop(),
                   parent.getRight()-parent.getPaddingRight(),
                   parent.getPaddingTop()+myTitleHeight, mPaint);
        //画文字
        mPaint.setColor(COLOR_TITLE_FONT);
        mPaint.getTextBounds(tag, 0, mDatas.get(position).getTag().length(), rect);
        c.drawText(tag, child.getPaddingLeft(),
                   parent.getPaddingTop()+myTitleHeight-(myTitleHeight/2-rect.height()/2), mPaint);

        if(flag)  c.restore();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();  //获取view位置
        if(position > -1){
            if(position == 0){
                outRect.set(0, myTitleHeight, 0, 0);
            }else {
                if(null != mDatas.get(position).getTag() &&
                           !mDatas.get(position).getTag().equals(mDatas.get(position-1).getTag())){
                    outRect.set(0, myTitleHeight, 0, 0);
                }else {
                    outRect.set(0, 0, 0, 1);
                }
            }
        }
    }
}
