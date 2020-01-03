package com.ng.ui.testchumo;

import android.animation.TypeEvaluator;
import android.graphics.Point;

/**
 * 描述:
 *
 * @author Jzn
 * @date 2019-12-31
 */
public class PointEvaluator implements TypeEvaluator<Point> {

    private Point mControlPoint;

    public PointEvaluator(Point mControlPoint) {
        this.mControlPoint = mControlPoint;
    }

    public PointEvaluator() {
    }

    @Override
    public Point evaluate(float t, Point startValue, Point endValue) {

        Point resultPoint = new Point();


        float oneMinusT = 1 - t;

        resultPoint.x = (int) (Math.pow(oneMinusT, 2) * startValue.x +
                2 * oneMinusT * t * mControlPoint.x + Math.pow(t, 2) * endValue.x);
        resultPoint.y = (int) (Math.pow(oneMinusT, 2) * startValue.y +
                2 * oneMinusT * t * mControlPoint.y + Math.pow(t, 2) * endValue.y);



        return resultPoint;
    }

}
