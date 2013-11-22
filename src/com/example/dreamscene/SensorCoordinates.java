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

    public void addCoordinates(float x, float y, float z, long t)
    {
        xData.add(x);
        yData.add(y);
        zData.add(z);
        timestamp.add(t);
    }
}
