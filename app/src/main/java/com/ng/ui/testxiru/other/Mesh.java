package com.ng.ui.testxiru.other;

/**
 * 描述:
 *
 * @author Jzn
 * @date 2020-01-03
 */
public abstract class Mesh
{
    int WIDTH;
    int HEIGHT;
    int mBmpWidth   = -1;
    int mBmpHeight  = -1;
    final float[] mVerts;

    Mesh(int width, int height)
    {
        WIDTH  = width;
        HEIGHT = height;
        mVerts  = new float[(WIDTH + 1) * (HEIGHT + 1) * 2];
    }

    float[] getVertices()
    {
        return mVerts;
    }

    int getWidth()
    {
        return WIDTH;
    }

    int getHeight()
    {
        return HEIGHT;
    }

    private static void setXY(float[] array, int index, float x, float y)
    {
        array[index*2 + 0] = x;
        array[index*2 + 1] = y;
    }

    void setBitmapSize(int w, int h)
    {
        mBmpWidth  = w;
        mBmpHeight = h;
    }

    public abstract void buildPaths(float endX, float endY,float width,float height);

    public abstract void buildMeshes(int index);

    void buildMeshes(float w, float h)
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
