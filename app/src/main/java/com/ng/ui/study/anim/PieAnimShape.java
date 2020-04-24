package com.ng.ui.study.anim;

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


    @Override
    public String toString() {
        return "PieAnimShape{" +
                "points=" + points +
                '}';
    }
}
