package fr.univpau.dudesalonso.boaviztapp.serverconfig;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.buffer.BarBuffer;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.renderer.BarChartRenderer;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class RoundedBarChart extends BarChartRenderer {


    public RoundedBarChart(BarDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(chart, animator, viewPortHandler);
    }

    private float mRadius=5f;

    public void setmRadius(float mRadius) {
        this.mRadius = mRadius;
    }

    @Override
    protected void drawDataSet(Canvas c, IBarDataSet dataSet, int index) {


        Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());

        mShadowPaint.setColor(dataSet.getBarShadowColor());

        float phaseX = mAnimator.getPhaseX();
        float phaseY = mAnimator.getPhaseY();



        if(mBarBuffers!=null){
            // initialize the buffer
            BarBuffer buffer = mBarBuffers[index];
            buffer.setPhases(phaseX, phaseY);
            buffer.setDataSet(index);
            buffer.setBarWidth(mChart.getBarData().getBarWidth());
            buffer.setInverted(mChart.isInverted(dataSet.getAxisDependency()));

            buffer.feed(dataSet);

            trans.pointValuesToPixel(buffer.buffer);

            // if multiple colors
            if (dataSet.getColors().size() > 1) {

                for (int j = 0; j < buffer.size(); j += 4) {

                    if (!mViewPortHandler.isInBoundsLeft(buffer.buffer[j + 2]))
                        continue;

                    if (!mViewPortHandler.isInBoundsRight(buffer.buffer[j]))
                        break;

                    if (mChart.isDrawBarShadowEnabled()) {
                        if (mRadius > 0)
                            c.drawRoundRect(new RectF(buffer.buffer[j], mViewPortHandler.contentTop(), buffer.buffer[j + 2], mViewPortHandler.contentBottom()), mRadius, mRadius, mShadowPaint);
                        else
                            c.drawRect(buffer.buffer[j], mViewPortHandler.contentTop(), buffer.buffer[j + 2], mViewPortHandler.contentBottom(), mShadowPaint);
                    }

                    // Set the color for the currently drawn value. If the index
                    // is
                    // out of bounds, reuse colors.
                    mRenderPaint.setColor(dataSet.getColor(j / 4));
                    if (mRadius > 0){

                        Path path = RoundedRect(buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2] , buffer.buffer[j + 3] , 15,15, true, true, false, false);
                        c.drawPath(path,mRenderPaint);
                    }
                    else
                        c.drawRect(buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2], buffer.buffer[j + 3], mRenderPaint);


                }
            } else {

                mRenderPaint.setColor(dataSet.getColor());

                for (int j = 0; j < buffer.size(); j += 4) {

                    if (!mViewPortHandler.isInBoundsLeft(buffer.buffer[j + 2]))
                        continue;

                    if (!mViewPortHandler.isInBoundsRight(buffer.buffer[j]))
                        break;

                    if (mChart.isDrawBarShadowEnabled()) {
                        if (mRadius > 0)
                            c.drawRoundRect(new RectF(buffer.buffer[j], mViewPortHandler.contentTop(),
                                    buffer.buffer[j + 2],
                                    mViewPortHandler.contentBottom()), mRadius, mRadius, mShadowPaint);
                        else
                            c.drawRect(buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2],
                                    buffer.buffer[j + 3], mRenderPaint);
                    }

                    if (mRadius > 0){
                        Path path = RoundedRect(buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2] , buffer.buffer[j + 3] , 15,15, true, true, false, false);
                        c.drawPath(path,mRenderPaint);
                    }
                    else
                        c.drawRect(buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2],
                                buffer.buffer[j + 3], mRenderPaint);
                }
            }
        }

    }
    public static Path RoundedRect(
            float left, float top, float right, float bottom, float rx, float ry,
            boolean tl, boolean tr, boolean br, boolean bl
    ){
        Path path = new Path();
        if (rx < 0) rx = 0;
        if (ry < 0) ry = 0;
        float width = right - left;
        float height = bottom - top;
        if (rx > width / 2) rx = width / 2;
        if (ry > height / 2) ry = height / 2;
        float widthMinusCorners = (width - (2 * rx));
        float heightMinusCorners = (height - (2 * ry));

        path.moveTo(right, top + ry);
        if (tr)
            path.rQuadTo(0, -ry, -rx, -ry);//top-right corner
        else{
            path.rLineTo(0, -ry);
            path.rLineTo(-rx,0);
        }
        path.rLineTo(-widthMinusCorners, 0);
        if (tl)
            path.rQuadTo(-rx, 0, -rx, ry); //top-left corner
        else{
            path.rLineTo(-rx, 0);
            path.rLineTo(0,ry);
        }
        path.rLineTo(0, heightMinusCorners);

        if (bl)
            path.rQuadTo(0, ry, rx, ry);//bottom-left corner
        else{
            path.rLineTo(0, ry);
            path.rLineTo(rx,0);
        }

        path.rLineTo(widthMinusCorners, 0);
        if (br)
            path.rQuadTo(rx, 0, rx, -ry); //bottom-right corner
        else{
            path.rLineTo(rx,0);
            path.rLineTo(0, -ry);
        }

        path.rLineTo(0, -heightMinusCorners);

        path.close();//Given close, last lineto can be removed.

        return path;
    }
}