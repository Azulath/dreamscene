package com.example.dreamscene;

import java.util.LinkedList;
import java.util.List;

/**
 * Crash with normal List!
 */
public class SensorCoordinates
{
    private List<Float> xData = new LinkedList<Float>();
    private List<Float> yData = new LinkedList<Float>();
    private List<Float> zData = new LinkedList<Float>();
    private List<Long> timestamp = new LinkedList<Long>();
    private float[] xTmp;
    private float[] yTmp;
    private float[] zTmp;
    private long[] tTmp;
    private int iterator;

    public SensorCoordinates()
    {
        xTmp = new float[2000];
        yTmp = new float[2000];
        zTmp = new float[2000];
        tTmp = new long[2000];
        iterator = 0;
    }

    public void addCoordinates(float x, float y, float z, long t)
    {
        xTmp[iterator] = x;
        yTmp[iterator] = y;
        zTmp[iterator] = z;
        tTmp[iterator] = t;

        if (iterator > 1900)
        {
            mergeCoordinates();
        }
    }

    private void mergeCoordinates()
    {
        float xAvg = 0;
        float yAvg = 0;
        float zAvg = 0;
        long tAvg = 0;
        for (int i = 0; i <= iterator; i++)
        {
            xAvg += xTmp[i];
            yAvg += yTmp[i];
            zAvg += zTmp[i];
            tAvg += tTmp[i];
        }

        xAvg /= iterator;
        yAvg /= iterator;
        zAvg /= iterator;
        tAvg /= iterator;

        addToList(xAvg, yAvg, zAvg, tAvg);
        iterator = 0;
    }

    private void addToList(float x, float y, float z, long t)
    {
        xData.add(x);
        yData.add(y);
        zData.add(z);
        timestamp.add(t);
    }
}
