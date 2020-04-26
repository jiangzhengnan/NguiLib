package com.ng.ui.study.anim;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:shape
 *
 * @author Jzn
 * @date 2020-04-24
 */
public class PieAnimShape {

    public PieAnimShape() {
    }

    public List<String> getPoints() {
        return points;
    }

    public void setPoints(List<String> points) {
        this.points = points;
    }

    private List<String> points;

    private List<Float> realPoints;

    public List<Float> getRealPoints() {
        if (points == null || points.size() == 0) {
            return null;
        }
        realPoints = new ArrayList<>();
        for (String point : points) {
            realPoints.add(Float.valueOf(point.split(",")[0]));
            realPoints.add(Float.valueOf(point.split(",")[1]));
        }
        return realPoints;
    }

    @Override
    public String toString() {
        return "PieAnimShape{" +
                "points=" + points +
                '}';
    }
}
