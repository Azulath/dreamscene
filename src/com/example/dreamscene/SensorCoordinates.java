package com.example.dreamscene;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: AlexZ
 * Date: 20.11.13
 * Time: 16:05
 * To change this template use File | Settings | File Templates.
 */
public class SensorCoordinates
{
    private List<Float> xData;
    private List<Float> yData;
    private List<Float> zData;
    private List<Long> timestamp;

    public void addCoordinates(float x, float y, float z, long t)
    {
        xData.add(x);
        yData.add(y);
        zData.add(z);
        timestamp.add(t);
    }
}
