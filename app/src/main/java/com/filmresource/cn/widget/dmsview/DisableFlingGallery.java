package com.filmresource.cn.widget.dmsview;
import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;
public class DisableFlingGallery extends Gallery {
    
    private GestureDetector gd;
    
    /**
     * @since 2012-12-6
     * @category
     * @param context
     * @param attrs
     * @param defStyle
     */
    public DisableFlingGallery(Context context) {
        super(context);
    }
    
    /**
     * @since 2012-12-6
     * @category
     * @param context
     * @param attrs
     * @param defStyle
     */
    public DisableFlingGallery(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * @since 2012-12-6
     * @category
     * @param context
     * @param attrs
     * @param defStyle
     */
    public DisableFlingGallery(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    /**
     * @since 2012-12-6
     * @throws 无
     * @see Gallery#onFling(MotionEvent, MotionEvent, float, float)
     *
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
            float velocityY) {
        int keyCode;
        //这样能够实现每次滑动只滚动一张图片的效果
        if (isScrollingLeft(e1,e2)) {
            keyCode= KeyEvent.KEYCODE_DPAD_LEFT;
        }else{
            keyCode= KeyEvent.KEYCODE_DPAD_RIGHT;
        }
        onKeyDown(keyCode,null);
        return true;
    }
    
    /**
     * 判断滑动方向
     * @since 2012-12-7
     * @param e1
     * @param e2
     * @return
     */
    private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2) {
        return e2.getX() > e1.getX();
    }
    
    /**
     * 设置手势监听
     * @since 2012-12-17
     * @throws 无
     * @param gestureDetector
     * void
     */
    public void setOnGestureDetector(GestureDetector gestureDetector) {
        gd = gestureDetector;
    }
    
    /**
     * @since 2012-12-14
     * @throws 无
     * @see android.view.View#onTouchEvent(MotionEvent)
     *
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(gd != null) {
            gd.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }
    
}
