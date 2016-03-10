package com.filmresource.cn.widget.dmsview;


import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.filmresource.cn.R;
import com.filmresource.cn.utils.LogUtil;

public class NavigationGallery extends LinearLayout {
    
    private  static int period = 3000;

    private Context mContext;
    private View relativeLayout;
    private LinearLayout llSelectedIcon;
    private DisableFlingGallery mGallery;
    private ImageView[] icons;
    private TextView tvName;
    private SpinnerAdapter mAdapter;
    private boolean isRegister = false;
    private boolean isShowSelecter = true;
    private OnItemSelected onItemSelected;
    
    private static int VIEWTYPE_NORMAL = 0x01;
    private static int VIEWTYPE_HOT_TRIP = 0x02;
    private int viewType = VIEWTYPE_NORMAL;
    private Timer timer;

    public NavigationGallery(Context context) {
        super(context);
    }

    public NavigationGallery(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.GallerySwitch);
        viewType = typedArray.getInteger(R.styleable.GallerySwitch_viewtype, VIEWTYPE_NORMAL);
        typedArray.recycle();
   
        LayoutParams layoutParams = new LayoutParams(
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        
        if(viewType == VIEWTYPE_NORMAL){
        	relativeLayout = LayoutInflater.from(context).inflate(
        			R.layout.navigation_gallery, null);
        } else if(viewType == VIEWTYPE_HOT_TRIP){
        	relativeLayout = LayoutInflater.from(context).inflate(
        			R.layout.navigation_gallery_hot_trip, null);
        }
        mGallery = (DisableFlingGallery) relativeLayout.findViewById(R.id.glFeaturedDes);
        llSelectedIcon = (LinearLayout) relativeLayout.findViewById(R.id.llSelectedIcon);
        mGallery.setOnItemSelectedListener(onItemSelectedListener);
        mGallery.setOnGestureDetector(mGestureDetector);
        mGallery.setOnTouchListener(touchListener);
        this.addView(relativeLayout, layoutParams);
        
    }
    
    public void setHoldTime (int time) {
    	period = time;
    }
    /**
     * @see View#onWindowVisibilityChanged(int)
     *
     */
    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if(visibility == View.VISIBLE)  {
            timer = new Timer();
            timer.schedule(new MyTimerTask(), period, period);
        } else {
            if(timer != null) {
                timer.cancel();
            }
            timer = null;
        }
//       LogUtils.e("visibility = " + visibility);
    }
    
    /**
     * 需要用来渐变的View
     * @author 
     * @since 2012-12-17
     * @param tv
     */
    public void setTextView(TextView tv) {
        tvName = tv;
    }
    
    public void setShowSelecter(boolean isShow) {
        isShowSelecter = isShow;
        initSelectIcons();
    }
    
    public void setOnItemSelected(OnItemSelected itemSelected) {
        onItemSelected = itemSelected;
    }
    
    public void setSelection(int position) {
        mGallery.setSelection(position);
    }
    
    public int getSelectedItemPosition() {
        return mGallery.getSelectedItemPosition();
    }
    
    /**
     * 为gallery设置adapter
     * 
     * @author 
     * @since 2012-12-6
     * @param adapter
     */
    public void setAdapter(LoopGalleryAdapter adapter ) {
        mGallery.setAdapter(adapter);
        mAdapter = adapter;
        mAdapter.registerDataSetObserver(observer);
        isRegister = true;
        initSelectIcons();
    }
    
    /**
     * @author 
     * @since 2012-12-28
     * @throws 无
     * @see LinearLayout#onMeasure(int, int)
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        if(viewType == VIEWTYPE_NORMAL){
//        	int width = getWidth();
//            if(width != 0) {
//                int paddingBottom = getHeight() / 30;
//                llSelectedIcon.setPadding(llSelectedIcon.getPaddingLeft(),
//                        llSelectedIcon.getPaddingTop(),
//                        llSelectedIcon.getPaddingRight(), paddingBottom);
//               LogUtils.e("llSelectedIcon.height = " + llSelectedIcon.getHeight());
//            }	
//        }
    }
    
    /**
     * @author 
     * @since 2012-12-28
     * @throws 无
     * @see View#onAttachedToWindow()
     *
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if(mAdapter != null && !isRegister) {
            mAdapter.registerDataSetObserver(observer);
            isRegister = true;
        }
    }
    
    /**
     * 移除监听器
     * @author 
     * @since 2012-12-6
     * @throws 无
     * @see View#onDetachedFromWindow()
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAdapter != null && isRegister) {
            mAdapter.unregisterDataSetObserver(observer);
            isRegister = false;
        }
    }

    /**
     * 初始化选择器
     * 
     * @author 
     * @since 2012-12-6
     */
    private final void initSelectIcons() {
    	int dmsIcon = (int)this.getResources().getDimension(R.dimen.dms_icon);
        LoopGalleryAdapter mAdapter = (LoopGalleryAdapter) mGallery.getAdapter();
        int count = 0;
        if (mAdapter != null) {
            count = mAdapter.getRealCount();
        }
        if(isShowSelecter) {
            if(count <= 1) {
                llSelectedIcon.setVisibility(View.GONE);
            } else {
                llSelectedIcon.setVisibility(View.VISIBLE);
            }
        } else {
            llSelectedIcon.setVisibility(View.GONE);
        }
        icons = new ImageView[count];
        llSelectedIcon.removeAllViews();
        llSelectedIcon.setWeightSum(count);
        int currentSelected = mGallery.getSelectedItemPosition();
        
        for (int i = 0; i < count; i++) {
            icons[i] = new ImageView(mContext);
//            icons[i].setLayoutParams(new LinearLayout.LayoutParams(20, 20));
            if(viewType == VIEWTYPE_HOT_TRIP){
            	if (i == currentSelected) {
                    icons[i].setBackgroundResource(R.drawable.dms_dot_focused);
                } else {
                    icons[i].setBackgroundResource(R.drawable.dms_dot_normal);
                }
            }else if(viewType == VIEWTYPE_NORMAL){
            	if (i == currentSelected) {
                    icons[i].setBackgroundResource(R.drawable.dms_dot_focused);
                } else {
                    icons[i].setBackgroundResource(R.drawable.dms_dot_normal);
                }
            }
            
            LayoutParams params = new LayoutParams(
            		dmsIcon,
            		dmsIcon);
            params.leftMargin = (int) mContext.getResources().getDimension(
                    R.dimen.navigation_selected_m_left);
            llSelectedIcon.addView(icons[i], params);
        }
//       LogUtils.e("count = " + count + "; currentSelected = " + currentSelected);
        if(count > currentSelected && currentSelected >= 0) {
            update(currentSelected);
        }
    }
    
    /**
     * 更新选择器的位置
     */
    private final void update(int selected) {
        LoopGalleryAdapter mAdapter = (LoopGalleryAdapter) mGallery.getAdapter();
        if(tvName != null) {
            Object obj = mAdapter.getItem(selected);
            if(obj != null) {
                tvName.setText(obj.toString());
            } else {
                tvName.setText("");
            }
        }
        int count = 0;
        if (mAdapter != null) {
            count = mAdapter.getRealCount();
        }

        for (int i = 0; i < count; i++) {
        	 if(viewType == VIEWTYPE_HOT_TRIP){
             	if (i == selected) {
             		icons[i].setBackgroundResource(R.drawable.dms_dot_focused);
                 } else {
                	icons[i].setBackgroundResource(R.drawable.dms_dot_normal);
                 }
             }else if(viewType == VIEWTYPE_NORMAL){
             	if (i == selected) {
             		icons[i].setBackgroundResource(R.drawable.dms_dot_focused);
                 } else {
                	icons[i].setBackgroundResource(R.drawable.dms_dot_normal);
                 }
             }
        }
    }
    
    public void setOnItemClickListener(final OnItemClickListener listener) {
        if(listener != null) {
            mGallery.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> groupView, View view,
                        int position, long id) {
                    LoopGalleryAdapter loopGalleryAdapter = (LoopGalleryAdapter) mGallery.getAdapter();
                    int realPosition = loopGalleryAdapter.getRealPosition(position);
                    listener.onItemClick(groupView, view, realPosition, id);
                }
            });
        } else {
            mGallery.setOnItemClickListener(null);
        }
    }
    
    /**
     * 监听数据变化
     */
    private DataSetObserver observer = new DataSetObserver() {
        public void onChanged() {
            initSelectIcons();
        };
    };

    /**
     * 监听选择变化
     */
    private OnItemSelectedListener onItemSelectedListener = new OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                int position, long id) {
            LoopGalleryAdapter mAdapter = (LoopGalleryAdapter) mGallery.getAdapter();
            int realPosition = mAdapter.getRealPosition(position);
            update(realPosition);
            if(onItemSelected != null) {
                onItemSelected.onItemSelected(realPosition);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }
    };
    
    //都手指按下移动时，调整透明度
    private GestureDetector mGestureDetector = new GestureDetector(mContext, new GestureDetector.OnGestureListener() {
        
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }
        
        @Override
        public void onShowPress(MotionEvent e) {
            
        }
        
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                float distanceY) {
            if(tvName != null) {
                View selectedView = mGallery.getSelectedView();
                if(selectedView != null) {
                    int left = Math.abs(selectedView.getLeft());
                    int ap = (int) (Math.abs((left - 400)) / 400f * 255);
                    tvName.setTextColor(Color.argb(ap, 255, 255, 255));
                    tvName.setShadowLayer(3, 2, 2, Color.argb(ap, 0, 0, 0));
                }
            }
            return false;
        }
        
        @Override
        public void onLongPress(MotionEvent e) {
            // TODO Auto-generated method stub
            
        }
        
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                float velocityY) {
            return false;
        }
        
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }
    });
    
    //当手指释放时，播放渐变动画完成后续透明度的改变
    private OnTouchListener touchListener = new OnTouchListener() {
        
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(MotionEvent.ACTION_DOWN == event.getAction()) {
                if(timer != null) {
                    timer.cancel();
                }
            }
            if(MotionEvent.ACTION_UP == event.getAction()) {
                if(timer != null) {
                    timer = new Timer();
                    timer.schedule(new MyTimerTask(), period, period);
                }
            }
            if(tvName != null && event.getAction() == MotionEvent.ACTION_UP) {
                View selectedView = mGallery.getSelectedView();
                if(selectedView == null) {
                    return false;
                }
                final int left = Math.abs(selectedView.getLeft());
                float ap = Math.abs((left - 400)) / 400f;
                Animation an = new AlphaAnimation(ap, 1);
                int duration = left / 2;
//                duration = duration == 0 ? 1 : duration;
                an.setDuration(duration);

                an.setAnimationListener(new AnimationListener() {
                    
                    @Override
                    public void onAnimationStart(Animation animation) {
                        tvName.setVisibility(View.INVISIBLE);
                    }
                    
                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // TODO Auto-generated method stub
                        
                    }
                    
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        tvName.setVisibility(View.VISIBLE);
                        tvName.setTextColor(Color.argb(255, 255, 255, 255));
                        tvName.setShadowLayer(3, 2, 2, Color.argb(255, 0, 0, 0));
                    }
                });
                if(duration > 0) {
                    tvName.startAnimation(an);
                }
            }
            return false;
        }
    };
    
    private class MyTimerTask extends TimerTask {
        
        @Override
        public void run() {
            post(new Runnable() {
                public void run() {
                	mGallery.onScroll(null, null, 1, 0);
                    mGallery.onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
                }
            });
        }
    };
    
    public static interface OnItemSelected {
        public void onItemSelected(int position);
    }
}
