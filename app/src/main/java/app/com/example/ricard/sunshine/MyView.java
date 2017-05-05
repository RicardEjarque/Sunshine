package app.com.example.ricard.sunshine;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;

/**
 * Created by Ricard on 05/05/2017.
 */

public class MyView extends View {
    Paint mPaint;
    String mWindSpeed;
    float mDegrees;
    float mBigCircleRadius = 250;
    float mLittleCircleRadius = 200;
    float mCenterCircleRadius = 30;
    float mvarray[] = new float[6];
    RectF rectF;
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private String DETAILFRAGMENT_TAG = "DFTAG";
    AppCompatActivity myActivity;
    detailFragment mDetailFragment;
    public MyView(Context context){
        super(context);
    }

    public MyView(Context context, AttributeSet attrs){
        super(context, attrs);

    }

    public MyView(Context context, AttributeSet attrs, int defaultStyle){
        super(context, attrs, defaultStyle);
    }

    @Override
    protected void onMeasure(int wMeasureSpec,int hMeasureSpec){
        int hSpecMode = MeasureSpec.getMode(hMeasureSpec);
        int hSpecSize = MeasureSpec.getSize(hMeasureSpec);
        int myHeight = hSpecSize;
        Log.e(LOG_TAG,"AAAGHHH");
        if(hSpecMode==MeasureSpec.EXACTLY){
            myHeight = hSpecSize;

        }else if(hSpecMode==MeasureSpec.AT_MOST){
            myHeight = 700;

        }
        else if(hSpecMode==MeasureSpec.UNSPECIFIED){
            myHeight = 700;

        }


        int wSpecMode = MeasureSpec.getMode(wMeasureSpec);
        int wSpecSize = MeasureSpec.getSize(wMeasureSpec);
        int myWidth = wSpecSize;

        if(wSpecMode==MeasureSpec.EXACTLY){
            myWidth = wSpecSize;
        }else if(wSpecMode==MeasureSpec.AT_MOST){
            myWidth = 1000;
        }

        setMeasuredDimension(myWidth,myHeight);



    }


    public void setWindSpeed (String windSpeed){
        mWindSpeed = windSpeed;
        AccessibilityManager accessibilityManager =
                (AccessibilityManager) getContext().getSystemService(
                        Context.ACCESSIBILITY_SERVICE);
        if (accessibilityManager.isEnabled()) {
            sendAccessibilityEvent(
                    AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED);
        }

    }

    @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent ev){
        ev.getText().add(mWindSpeed);
        return true;
    }

    public void setDegrees (float degrees){
        mDegrees = degrees;
    }

    private float[] calculateTriangle (float degrees, float dimension){
        //Paint the indicator
        degrees = (float)(degrees*Math.PI/180);
        float windlinex = (float)(dimension*Math.sin(degrees));
        float windliney = (float)(dimension*Math.cos(degrees));
        float windLineExtremex = (float) (20.0*Math.cos(degrees));
        float windLineExtremey = (float) (20.0*Math.sin(degrees));

        float varray[] = new float[6];

        varray [0] = rectF.centerX()+windLineExtremex;
        varray [1] = rectF.centerY()-windLineExtremey;
        varray [2] = rectF.centerX()-windLineExtremex;
        varray [3] = rectF.centerY()+windLineExtremey;
        varray [4] = rectF.centerX()-windlinex;
        varray [5] = rectF.centerY()-windliney;

        return varray;
    }

    @Override
    protected void onDraw (Canvas canvas){
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);

       //Paint the 2 circles
        rectF = new RectF(60,100,60+mBigCircleRadius*2,100+mBigCircleRadius*2);
        canvas.drawOval(rectF,mPaint);
        mPaint.setStrokeWidth(3);
        RectF rectF2 = new RectF(rectF.centerX()-mLittleCircleRadius,rectF.centerY()-mLittleCircleRadius,rectF.centerX()+mLittleCircleRadius,rectF.centerY()+mLittleCircleRadius);
        canvas.drawOval(rectF2,mPaint);

        //Paint the wind direction names
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(50);
        canvas.drawText("N",rectF.centerX()-10,rectF.centerY()-mBigCircleRadius-15,mPaint);
        canvas.drawText("E",rectF.centerX()+mBigCircleRadius+15,rectF.centerY()+15,mPaint);
        canvas.drawText("S",rectF.centerX()-10,rectF.centerY()+mBigCircleRadius+55,mPaint);
        canvas.drawText("W",rectF.centerX()-mBigCircleRadius-55,rectF.centerY()+15,mPaint);

        //Paint the flechas
        //North flecha
        mvarray = calculateTriangle(0,mBigCircleRadius);
        Path path1 = new Path();
        path1.setFillType(Path.FillType.EVEN_ODD);
        path1.moveTo(mvarray[0],mvarray[1]);
        path1.lineTo(mvarray[2],mvarray[3]);
        path1.lineTo(mvarray[4],mvarray[5]);
        path1.lineTo(mvarray[0],mvarray[1]);
        path1.close();
        canvas.drawPath(path1,mPaint);
        //North-east flecha
        mvarray = calculateTriangle(45,mLittleCircleRadius);
        path1.reset();
        path1.setFillType(Path.FillType.EVEN_ODD);
        path1.moveTo(mvarray[0],mvarray[1]);
        path1.lineTo(mvarray[2],mvarray[3]);
        path1.lineTo(mvarray[4],mvarray[5]);
        path1.lineTo(mvarray[0],mvarray[1]);
        path1.close();
        canvas.drawPath(path1,mPaint);
        //East flecha
        mvarray = calculateTriangle(90,mBigCircleRadius);
        Path path2 = new Path();
        path2.setFillType(Path.FillType.EVEN_ODD);
        path2.moveTo(mvarray[0],mvarray[1]);
        path2.lineTo(mvarray[2],mvarray[3]);
        path2.lineTo(mvarray[4],mvarray[5]);
        path2.lineTo(mvarray[0],mvarray[1]);
        path2.close();
        canvas.drawPath(path2,mPaint);
        //North-east flecha
        mvarray = calculateTriangle(135,mLittleCircleRadius);
        path1.reset();
        path1.setFillType(Path.FillType.EVEN_ODD);
        path1.moveTo(mvarray[0],mvarray[1]);
        path1.lineTo(mvarray[2],mvarray[3]);
        path1.lineTo(mvarray[4],mvarray[5]);
        path1.lineTo(mvarray[0],mvarray[1]);
        path1.close();
        canvas.drawPath(path1,mPaint);
        //South flecha
        mvarray = calculateTriangle(180,mBigCircleRadius);
        Path path3 = new Path();
        path3.setFillType(Path.FillType.EVEN_ODD);
        path3.moveTo(mvarray[0],mvarray[1]);
        path3.lineTo(mvarray[2],mvarray[3]);
        path3.lineTo(mvarray[4],mvarray[5]);
        path3.lineTo(mvarray[0],mvarray[1]);
        path3.close();
        canvas.drawPath(path3,mPaint);
        //North-east flecha
        mvarray = calculateTriangle(225,mLittleCircleRadius);
        path1.reset();
        path1.setFillType(Path.FillType.EVEN_ODD);
        path1.moveTo(mvarray[0],mvarray[1]);
        path1.lineTo(mvarray[2],mvarray[3]);
        path1.lineTo(mvarray[4],mvarray[5]);
        path1.lineTo(mvarray[0],mvarray[1]);
        path1.close();
        canvas.drawPath(path1,mPaint);
        //West flecha
        mvarray = calculateTriangle(270,mBigCircleRadius);
        Path path4 = new Path();
        path4.setFillType(Path.FillType.EVEN_ODD);
        path4.moveTo(mvarray[0],mvarray[1]);
        path4.lineTo(mvarray[2],mvarray[3]);
        path4.lineTo(mvarray[4],mvarray[5]);
        path4.lineTo(mvarray[0],mvarray[1]);
        path4.close();
        canvas.drawPath(path4,mPaint);
        //North-east flecha
        mvarray = calculateTriangle(315,mLittleCircleRadius);
        path1.reset();
        path1.setFillType(Path.FillType.EVEN_ODD);
        path1.moveTo(mvarray[0],mvarray[1]);
        path1.lineTo(mvarray[2],mvarray[3]);
        path1.lineTo(mvarray[4],mvarray[5]);
        path1.lineTo(mvarray[0],mvarray[1]);
        path1.close();
        canvas.drawPath(path1,mPaint);


        mPaint.setStrokeWidth(2);
        mPaint.setColor(android.graphics.Color.RED);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setAntiAlias(true);

        mvarray = calculateTriangle(-mDegrees,mBigCircleRadius);
        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(mvarray[0],mvarray[1]);
        path.lineTo(mvarray[2],mvarray[3]);
        path.lineTo(mvarray[4],mvarray[5]);
        path.lineTo(mvarray[0],mvarray[1]);
        path.close();

        canvas.drawPath(path,mPaint);

        RectF rectF3 = new RectF(rectF.centerX()-mCenterCircleRadius,rectF.centerY()-mCenterCircleRadius,rectF.centerX()+mCenterCircleRadius,rectF.centerY()+mCenterCircleRadius);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.white));
        canvas.drawOval(rectF3,mPaint);
        RectF rectF4 = new RectF(rectF.centerX()-mCenterCircleRadius+15,rectF.centerY()-mCenterCircleRadius+15,rectF.centerX()+mCenterCircleRadius-15,rectF.centerY()-15+mCenterCircleRadius);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.black));
        canvas.drawOval(rectF4,mPaint);
        //canvas.drawVertices(Canvas.VertexMode.TRIANGLE_FAN, 6, varray,0,null,0,null,0,null,0,0,mPaint);
        //canvas.drawLine(rectF.centerX(),rectF.centerY(),rectF.centerX()+windlinex,rectF.centerY()+windliney,mPaint);

        mPaint.setStyle(Paint.Style.FILL);
        //mPaint.setTextSize(50);

        //mPaint.setTextSize(80);
        //mPaint.setTypeface(Typeface.SANS_SERIF);
        //canvas.drawText("Wind:",700,150,mPaint);
        mPaint.setTextSize(50);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.textgrey));
        if(mWindSpeed!=null) {
            canvas.drawText(mWindSpeed, 700, 350, mPaint);
        }










    }


}
