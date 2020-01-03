package com.ng.ui.testxiru.other;

/**
 * 描述:
 *
 * @author Jzn
 * @date 2020-01-03
 */
/*
 * System: CoreLib
 * @version     1.00
 *
 * Copyright (C) 2010, LZT Corporation.
 *
 */


public abstract class Mesh
{
    protected int WIDTH      = 40;
    protected int HEIGHT     = 40;
    protected int mBmpWidth   = -1;
    protected int mBmpHeight  = -1;
    protected final float[] mVerts;

    public Mesh(int width, int height)
    {
        WIDTH  = width;
        HEIGHT = height;
        mVerts  = new float[(WIDTH + 1) * (HEIGHT + 1) * 2];
    }

    public float[] getVertices()
    {
        return mVerts;
    }

    public int getWidth()
    {
        return WIDTH;
    }

    public int getHeight()
    {
        return HEIGHT;
    }

    public static void setXY(float[] array, int index, float x, float y)
    {
        array[index*2 + 0] = x;
        array[index*2 + 1] = y;
    }

    public void setBitmapSize(int w, int h)
    {
        mBmpWidth  = w;
        mBmpHeight = h;
    }

    public abstract void buildPaths(float endX, float endY);

    public abstract void buildMeshes(int index);

    public void buildMeshes(float w, float h)
    {
        int index = 0;

        for (int y = 0; y <= HEIGHT; ++y)
        {
            float fy = y * h / HEIGHT;
            for (int x = 0; x <= WIDTH; ++x)
            {
                float fx = x * w / WIDTH;

                setXY(mVerts, index, fx, fy);

                index += 1;
            }
        }
    }
}
