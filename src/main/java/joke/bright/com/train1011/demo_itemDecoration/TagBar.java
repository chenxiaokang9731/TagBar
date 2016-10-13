package joke.bright.com.train1011.demo_itemDecoration;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.github.promeg.pinyinhelper.Pinyin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import joke.bright.com.train1011.R;

/**
 * 城市列表右边字母TagBar
 * Created by chenxiaokang on 2016/10/13.
 */
public class TagBar extends View{

    private static final int DEFAULT_SIZE = 16;

    private List<String> mTagDatas;
    private TextView mTagPressedTv;
    private List<CityBean> mSourceDatas;
    private int mPressedBackground;
    private int mUnPressedBackground;
    private int mWidth;
    private int mHeight;
    private int mTagHeight;   //一个字母Tag所拥有的高度
    private Paint mPaint;
    private int mTextSize;
    private LinearLayoutManager mLayoutManager;

    public TagBar(Context context) {
        this(context, null);
    }

    public TagBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mTagDatas = new ArrayList<>();
        mPressedBackground = Color.BLACK;
        mUnPressedBackground = Color.parseColor("#00000000");

        mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, DEFAULT_SIZE, context.getResources().getDisplayMetrics());

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TagBar, defStyleAttr, 0);
        int count = typedArray.getIndexCount();
        for (int i = 0; i<count; i++){
            int attr = typedArray.getIndex(i);
            if(attr == R.styleable.TagBar_pressedBack){
                mPressedBackground = typedArray.getColor(attr, mPressedBackground);
            }else if (attr == R.styleable.TagBar_tag_textsize){
                mTextSize = typedArray.getDimensionPixelSize(attr, mTextSize);
            }
        }

        typedArray.recycle();

        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(mTextSize);
        mPaint.setAntiAlias(true);

        setOnTagPressedListener(new OnTagPressedListener() {
            @Override
            public void onTagPressed(int tagIndex, String text) {
                  if(mTagPressedTv != null){
                      mTagPressedTv.setText(text);
                      mTagPressedTv.setVisibility(VISIBLE);
                  }

                  if(mLayoutManager != null){
                      int pos = getPosByTag(text);
                      if (pos != -1){
                          mLayoutManager.scrollToPositionWithOffset(pos, 0);
                      }
                  }
            }

            @Override
            public void onTagPressedEnd() {
                if(mTagPressedTv != null){
                    mTagPressedTv.setVisibility(GONE);
                }
            }
        });
    }

    public int getPosByTag(String tag){

        if(mSourceDatas == null && mSourceDatas.isEmpty()){
            return -1;
        }
        if(TextUtils.isEmpty(tag)){
            return  -1;
        }

        for(int i = 0; i<mSourceDatas.size(); i++){
            if(tag.equals(mSourceDatas.get(i).getTag())){
                return i;
            }
        }

        return -1;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        int measureWidth = 0, measureHeight = 0;

        Rect indexRect = new Rect();
        String tag = "";
        for(int i=0; i<mTagDatas.size(); i++){
            tag = mTagDatas.get(i);
            mPaint.getTextBounds(tag, 0, tag.length(), indexRect);
            measureHeight = Math.max(measureHeight, indexRect.height());
            measureWidth = Math.max(measureWidth, indexRect.width());
        }
        measureHeight = measureHeight * mTagDatas.size();

        //测Height
        switch (hMode){
            case MeasureSpec.EXACTLY:
                measureHeight = hSize;
                break;
            case MeasureSpec.AT_MOST:
                measureHeight = Math.min(measureHeight, hSize);
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }

        //测Width
        switch (wMode){
            case MeasureSpec.EXACTLY:
                measureWidth = wSize;
                break;
            case MeasureSpec.AT_MOST:
                measureWidth = Math.min(measureWidth, wSize);
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }

        setMeasuredDimension(measureWidth, measureHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Rect rect = new Rect();
        int t = getPaddingTop();
        String tag = "";
        for(int i = 0; i<mTagDatas.size(); i++){
            tag = mTagDatas.get(i);
            mPaint.getTextBounds(tag, 0, tag.length(), rect);
            Paint.FontMetrics metrics = mPaint.getFontMetrics();
            int baseline = (int) ((mTagHeight - (metrics.bottom+metrics.top))/2);
            canvas.drawText(tag, mWidth/2-rect.width()/2, t+mTagHeight*i+baseline, mPaint);
        }
    }

    public TagBar setSourceDatas(List<CityBean> datas){
        mSourceDatas = datas;
        initSourceDatas();

        return this;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if(mTagDatas == null || mTagDatas.isEmpty()){
            return;
        }

        mWidth = w;
        mHeight = h;
        mTagHeight = (int) ((mHeight - getPaddingTop() - getPaddingBottom()) * 1.0f/ mTagDatas.size());
    }

    /**
     *   初始化数据源
     */
    private void initSourceDatas() {

        if(mSourceDatas == null && mSourceDatas.isEmpty()){
            return;
        }

        for(int i = 0; i<mSourceDatas.size(); i++){
            StringBuffer sb = new StringBuffer();
            String city = mSourceDatas.get(i).getCity();
            for(int j=0; j<city.length(); j++){
                sb.append(Pinyin.toPinyin(city.charAt(j)));
            }
            String cityPyFirstTag = sb.toString().substring(0,1);
            if(cityPyFirstTag.matches("[A-Z]")){
                mSourceDatas.get(i).setTag(cityPyFirstTag);
                if(!mTagDatas.contains(cityPyFirstTag)){
                    mTagDatas.add(cityPyFirstTag);
                }
            }else {
                mSourceDatas.get(i).setTag("#");
                if(!mTagDatas.contains("#")){
                    mTagDatas.add("#");
                }
            }
        }
        sortData();
    }

    /**
     * 数据排序
     */
    private void sortData() {

        //对mTagDatas进行排序
        Collections.sort(mTagDatas, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {

                if(lhs.equals("#")){
                    return 1;
                }else if (rhs.equals("#")){
                    return -1;
                }else {
                    return lhs.compareTo(rhs);
                }
            }
        });

        //对mSourceDatas进行排序
        Collections.sort(mSourceDatas, new Comparator<CityBean>() {
            @Override
            public int compare(CityBean lhs, CityBean rhs) {
                if(lhs.getTag().equals("#")){
                    return 1;
                }else if (rhs.getTag().equals("#")){
                    return -1;
                }else {
                    return lhs.getTag().compareTo(rhs.getTag());
                }
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                setBackgroundColor(mPressedBackground);
            case MotionEvent.ACTION_MOVE:
                float y = event.getY();
                int pressTag = (int) ((y - getPaddingTop())/ mTagHeight);
                if(pressTag < 0){
                    pressTag = 0;
                }else if(pressTag >= mTagDatas.size()){
                    pressTag = mTagDatas.size() - 1;
                }
                if(mListener != null){
                    mListener.onTagPressed(pressTag, mTagDatas.get(pressTag));
                }
                break;
            case MotionEvent.ACTION_UP:
                setBackgroundColor(mUnPressedBackground);
                if(mListener != null){
                    mListener.onTagPressedEnd();
                }
                break;
        }

        return true;
    }

    /**
     * 设置按下TagBar时屏幕正中间显示的TextView,由外部传入
     * @param tv
     * @return
     */
    public TagBar setTagPressedTextView(TextView tv){
        mTagPressedTv = tv;
        return this;
    }

    public TagBar setLayoutManager(LinearLayoutManager mLayoutManager){
        this.mLayoutManager = mLayoutManager;
        return this;
    }

    private OnTagPressedListener mListener;

    public void setOnTagPressedListener(OnTagPressedListener listener){
        mListener = listener;
    }

    /**
     * 设置TagBar触摸监听事件
     */
    public interface OnTagPressedListener{
        void onTagPressed(int tagIndex, String text);
        void onTagPressedEnd();
    }
}
