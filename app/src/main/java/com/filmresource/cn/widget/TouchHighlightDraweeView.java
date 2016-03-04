package com.filmresource.cn.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.method.Touch;
import android.util.AttributeSet;

import com.facebook.common.internal.Preconditions;
import com.facebook.common.internal.Supplier;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.interfaces.SimpleDraweeControllerBuilder;
import com.facebook.drawee.view.GenericDraweeView;
import com.filmresource.cn.R;

/**
 * Created by ql on 2016/3/4.
 */
public class TouchHighlightDraweeView  extends GenericDraweeView {
    public TouchHighlightDraweeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

////    private Drawable mForegroundDrawable;
////    private Rect mCachedBounds = new Rect();
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//
////        if(mForegroundDrawable!=null){
////            mForegroundDrawable.setBounds(mCachedBounds);
////            mForegroundDrawable.draw(canvas);
////        }
//    }
//
//    @Override
//    protected void drawableStateChanged() {
//        super.drawableStateChanged();
////        if(mForegroundDrawable!=null&&mForegroundDrawable.isStateful())
////        {
////            mForegroundDrawable.setState(getDrawableState());
////        }
////        invalidate();
//    }
//
//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
////        mCachedBounds.set(0,0,w,h);
//    }

    //        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.touch_highlight, defStyle, 0);
//        int n = a.getIndexCount();
//        for (int i = 0;i < n;i++)
//        {
//            int attar = a.getIndex(n);
//            switch (attar) {
//                case R.styleable.touch_highlight_touchbg:
//                    mForegroundDrawable = a.getDrawable(attar);
//                    mForegroundDrawable.setCallback(this);
//                    break;
//            }
//        }
//        a.recycle();

}
