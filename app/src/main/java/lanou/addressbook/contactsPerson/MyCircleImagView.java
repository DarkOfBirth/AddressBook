package lanou.addressbook.contactsPerson;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import lanou.addressbook.R;

/**
 * Created by dllo on 16/9/27.
 */
public class MyCircleImagView extends ImageView {
    private boolean isCircle ;
    public MyCircleImagView(Context context) {
        super(context);
    }

    public MyCircleImagView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 获取xml布局里设置的自定义组件的属性值
        // 找到attrs.xml 里增加的东西
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MyCircleImagView);
        isCircle = array.getBoolean(R.styleable.MyCircleImagView_isCircle, false);

    }

    public MyCircleImagView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(isCircle) {
            // 绘制图片
            // 获取imageveiw的src资源图片
            BitmapDrawable drawable = (BitmapDrawable) getDrawable();
            if(drawable != null) {
                // 将src设置的图片转换成bitmap图片
                Bitmap bitmap = drawable.getBitmap();
            Bitmap circleBitmap = getCircleBitmap(bitmap);
                Paint paint = new Paint();
                paint.setAntiAlias(true);
                Rect rect = new Rect(0, 0, circleBitmap.getWidth(), circleBitmap.getHeight());
                canvas.drawBitmap(circleBitmap,rect,rect,paint);
            }
        } else {

        super.onDraw(canvas);
        }


    }

    private Bitmap getCircleBitmap(Bitmap bitmap) {
        // 初始化一个空的跟src图片一样大小的Bitmap
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        // 创建一个空的装载画布
        Canvas canvas = new Canvas(outBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawCircle(bitmap.getWidth()/2,bitmap.getHeight()/2,bitmap.getWidth()/2,paint);
        // 设置画笔的模式, 两者相交去前景
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        canvas.drawBitmap(bitmap,rect,rect,paint);
        return outBitmap;


    }
}
