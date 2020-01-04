package com.ng.ui.testxiru.other;

/**
 * 描述:
 *
 * @author Jzn
 * @date 2020-01-03
 */

import android.graphics.Path;
import android.graphics.PathMeasure;

import com.ng.nguilib.utils.LogUtils;

public class InhaleMesh extends Mesh {
    public enum InhaleDir {
        UP,
        DOWN,
        LEFT,
        RIGHT,
        UPTARGET
    }

    private Path mFirstPath = new Path();
    private Path mSecondPath = new Path();
    private PathMeasure mFirstPathMeasure = new PathMeasure();
    private PathMeasure mSecondPathMeasure = new PathMeasure();
    private InhaleDir mInhaleDir = InhaleDir.DOWN;

    public InhaleMesh(int width, int height) {
        super(width, height);
    }

    public void setInhaleDir(InhaleDir inhaleDir) {
        mInhaleDir = inhaleDir;
    }

    public InhaleDir getInhaleDir() {
        return mInhaleDir;
    }

    @Override
    public void buildPaths(float endX, float endY, float width, float height) {
        if (mBmpWidth <= 0 || mBmpHeight <= 0) {
            throw new IllegalArgumentException(
                    "Bitmap size must be > 0, do you call setBitmapSize(int, int) method?");
        }

        switch (mInhaleDir) {
            case UP:
                buildPathsUp(endX, endY, width, height);
                break;

            case DOWN:
                buildPathsDown(endX, endY);
                break;

            case RIGHT:
                buildPathsRight(endX, endY);
                break;

            case LEFT:
                buildPathsLeft(endX, endY);
                break;


        }
    }


    @Override
    public void buildMeshes(int index) {
        if (mBmpWidth <= 0 || mBmpHeight <= 0) {
            throw new IllegalArgumentException(
                    "Bitmap size must be > 0, do you call setBitmapSize(int, int) method?");
        }

        switch (mInhaleDir) {
            case UPTARGET:
            case UP:
            case DOWN:
                buildMeshByPathOnVertical(index);
                break;

            case RIGHT:
            case LEFT:
                buildMeshByPathOnHorizontal(index);
                break;
        }
    }

    public Path[] getPaths() {
        return new Path[]{mFirstPath, mSecondPath};
    }

    private void buildPathsDown(float endX, float endY) {
        mFirstPathMeasure.setPath(mFirstPath, false);
        mSecondPathMeasure.setPath(mSecondPath, false);

        float w = mBmpWidth;
        float h = mBmpHeight;

        //bitmap宽高
        LogUtils.INSTANCE.d("w h : " + w + " " + h);
        //屏幕宽高
        LogUtils.INSTANCE.d("endXw endY : " + endX + " " + endY);

        mFirstPath.reset();
        mSecondPath.reset();

        mFirstPath.moveTo(endX / 2 - w / 2, endY / 2 - h / 2);

        mSecondPath.moveTo(endX / 2 + w / 2, endY / 2 - h / 2);

        mFirstPath.lineTo(endX / 2 - w / 2, endY / 2 + h / 2);
        mSecondPath.lineTo(endX / 2 + w / 2, endY / 2 + h / 2);

        mFirstPath.quadTo(endX / 4, endY, endX / 2, endY);
        mSecondPath.quadTo(endX - endX / 4, endY, endX / 2, endY);
    }

    private void buildPathsUp(float endX, float endY, float width, float height) {
        mFirstPathMeasure.setPath(mFirstPath, false);
        mSecondPathMeasure.setPath(mSecondPath, false);

        float w = mBmpWidth;
        float h = mBmpHeight;


        mFirstPath.reset();
        mSecondPath.reset();

        mFirstPath.moveTo(width / 2 - w / 2, height / 2 + h / 2);

        mSecondPath.moveTo(width / 2 + w / 2, height / 2 + h / 2);

        mFirstPath.lineTo(width / 2 - w / 2, height / 2 - h / 2);
        mSecondPath.lineTo(width / 2 + w / 2, height / 2 - h / 2);

        mFirstPath.quadTo(width / 2 - w / 2, endY, endX, endY);
        mSecondPath.quadTo(width / 2 + w / 2, endY, endX, endY);
    }

    private void buildPathsRight(float endX, float endY) {
        mFirstPathMeasure.setPath(mFirstPath, false);
        mSecondPathMeasure.setPath(mSecondPath, false);

        float w = mBmpWidth;
        float h = mBmpHeight;

        mFirstPath.reset();
        mSecondPath.reset();

        mFirstPath.moveTo(0, 0);
        mSecondPath.moveTo(0, h);

        mFirstPath.lineTo(w, 0);
        mSecondPath.lineTo(w, h);

        mFirstPath.quadTo((endX + w) / 2, 0, endX, endY);
        mSecondPath.quadTo((endX + w) / 2, h, endX, endY);
    }

    private void buildPathsLeft(float endX, float endY) {
        mFirstPathMeasure.setPath(mFirstPath, false);
        mSecondPathMeasure.setPath(mSecondPath, false);

        float w = mBmpWidth;
        float h = mBmpHeight;

        mFirstPath.reset();
        mSecondPath.reset();

        mFirstPath.moveTo(w, 0);
        mSecondPath.moveTo(w, h);

        mFirstPath.lineTo(0, 0);
        mSecondPath.lineTo(0, h);

        mFirstPath.quadTo((endX - w) / 2, 0, endX, endY);
        mSecondPath.quadTo((endX - w) / 2, h, endX, endY);
    }

    private void buildMeshByPathOnVertical(int timeIndex) {
        mFirstPathMeasure.setPath(mFirstPath, false);
        mSecondPathMeasure.setPath(mSecondPath, false);

        int index = 0;
        float[] pos1 = {0.0f, 0.0f};
        float[] pos2 = {0.0f, 0.0f};
        float firstLen = mFirstPathMeasure.getLength();
        float secondLen = mSecondPathMeasure.getLength();
        float len1 = firstLen / HEIGHT;
        float len2 = secondLen / HEIGHT;

        float firstPointDist = timeIndex * len1;
        float secondPointDist = timeIndex * len2;
        float height = mBmpHeight;

        mFirstPathMeasure.getPosTan(firstPointDist, pos1, null);
        mFirstPathMeasure.getPosTan(firstPointDist + height, pos2, null);
        float x1 = pos1[0];
        float x2 = pos2[0];
        float y1 = pos1[1];
        float y2 = pos2[1];
        float FIRST_DIST = (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
        float FIRST_H = FIRST_DIST / HEIGHT;

        mSecondPathMeasure.getPosTan(secondPointDist, pos1, null);
        mSecondPathMeasure.getPosTan(secondPointDist + height, pos2, null);
        x1 = pos1[0];
        x2 = pos2[0];
        y1 = pos1[1];
        y2 = pos2[1];

        float SECOND_DIST = (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
        float SECOND_H = SECOND_DIST / HEIGHT;

        if (mInhaleDir == InhaleDir.DOWN) {
            for (int y = 0; y <= HEIGHT; ++y) {
                //得到每一个点的位置
                mFirstPathMeasure.getPosTan(y * FIRST_H + firstPointDist, pos1, null);
                mSecondPathMeasure.getPosTan(y * SECOND_H + secondPointDist, pos2, null);

                float w = pos2[0] - pos1[0];//横轴最左边到最右边的距离
                //左右两边的点的位置
                float fx1 = pos1[0];
                float fx2 = pos2[0];
                float fy1 = pos1[1];
                float fy2 = pos2[1];
                //左右两边点 x 和 y轴方向的差值
                float dy = fy2 - fy1;
                float dx = fx2 - fx1;

                for (int x = 0; x <= WIDTH; ++x) {
                    // y = x * dy / dx
                    float fx = x * w / WIDTH;
                    //tanα = dy/dx = fy/fx
                    float fy = fx * dy / dx;

                    mVerts[index * 2 + 0] = fx + fx1;
                    mVerts[index * 2 + 1] = fy + fy1;

                    index += 1;
                }
            }
        } else if (mInhaleDir == InhaleDir.UP) {
            for (int y = HEIGHT; y >= 0; --y) {
                mFirstPathMeasure.getPosTan(y * FIRST_H + firstPointDist, pos1, null);
                mSecondPathMeasure.getPosTan(y * SECOND_H + secondPointDist, pos2, null);

                float w = pos2[0] - pos1[0];
                float fx1 = pos1[0];
                float fx2 = pos2[0];
                float fy1 = pos1[1];
                float fy2 = pos2[1];
                float dy = fy2 - fy1;
                float dx = fx2 - fx1;

                for (int x = 0; x <= WIDTH; ++x) {
                    // y = x * dy / dx
                    float fx = x * w / WIDTH;
                    float fy = fx * dy / dx;

                    mVerts[index * 2 + 0] = fx + fx1;
                    mVerts[index * 2 + 1] = fy + fy1;

                    index += 1;
                }
            }
        }
    }

    private void buildMeshByPathOnHorizontal(int timeIndex) {
        mFirstPathMeasure.setPath(mFirstPath, false);
        mSecondPathMeasure.setPath(mSecondPath, false);

        int index = 0;
        float[] pos1 = {0.0f, 0.0f};
        float[] pos2 = {0.0f, 0.0f};
        float firstLen = mFirstPathMeasure.getLength();
        float secondLen = mSecondPathMeasure.getLength();

        float len1 = firstLen / WIDTH;
        float len2 = secondLen / WIDTH;

        float firstPointDist = timeIndex * len1;
        float secondPointDist = timeIndex * len2;
        float width = mBmpWidth;

        mFirstPathMeasure.getPosTan(firstPointDist, pos1, null);
        mFirstPathMeasure.getPosTan(firstPointDist + width, pos2, null);
        float x1 = pos1[0];
        float x2 = pos2[0];
        float y1 = pos1[1];
        float y2 = pos2[1];
        float FIRST_DIST = (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
        float FIRST_X = FIRST_DIST / WIDTH;

        mSecondPathMeasure.getPosTan(secondPointDist, pos1, null);
        mSecondPathMeasure.getPosTan(secondPointDist + width, pos2, null);
        x1 = pos1[0];
        x2 = pos2[0];
        y1 = pos1[1];
        y2 = pos2[1];

        float SECOND_DIST = (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
        float SECOND_X = SECOND_DIST / WIDTH;

        if (mInhaleDir == InhaleDir.RIGHT) {
            for (int x = 0; x <= WIDTH; ++x) {
                mFirstPathMeasure.getPosTan(x * FIRST_X + firstPointDist, pos1, null);
                mSecondPathMeasure.getPosTan(x * SECOND_X + secondPointDist, pos2, null);

                float h = pos2[1] - pos1[1];
                float fx1 = pos1[0];
                float fx2 = pos2[0];
                float fy1 = pos1[1];
                float fy2 = pos2[1];
                float dy = fy2 - fy1;
                float dx = fx2 - fx1;

                for (int y = 0; y <= HEIGHT; ++y) {
                    // x = y * dx / dy
                    float fy = y * h / HEIGHT;
                    float fx = fy * dx / dy;

                    index = y * (WIDTH + 1) + x;

                    mVerts[index * 2 + 0] = fx + fx1;
                    mVerts[index * 2 + 1] = fy + fy1;
                }
            }
        } else if (mInhaleDir == InhaleDir.LEFT) {
            for (int x = WIDTH; x >= 0; --x)
            //for (int x = 0; x <= WIDTH; ++x)
            {
                mFirstPathMeasure.getPosTan(x * FIRST_X + firstPointDist, pos1, null);
                mSecondPathMeasure.getPosTan(x * SECOND_X + secondPointDist, pos2, null);

                float h = pos2[1] - pos1[1];
                float fx1 = pos1[0];
                float fx2 = pos2[0];
                float fy1 = pos1[1];
                float fy2 = pos2[1];
                float dy = fy2 - fy1;
                float dx = fx2 - fx1;

                for (int y = 0; y <= HEIGHT; ++y) {
                    // x = y * dx / dy
                    float fy = y * h / HEIGHT;
                    float fx = fy * dx / dy;

                    index = y * (WIDTH + 1) + WIDTH - x;

                    mVerts[index * 2 + 0] = fx + fx1;
                    mVerts[index * 2 + 1] = fy + fy1;
                }
            }
        }
    }
}