package com.example.dreamscene;

public class Coordinates
{
    private float x;
    private float y;
    private float z;
    private long time;

    public  Coordinates(float x, float y, float z, long t)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.time = t;
    }

    public float getX()
    {
        return x;
    }

    public float getY()
    {
        return y;
    }

    public float getZ()
    {
        return  z;
    }

    public long getTime()
    {
        return time;
    }

}
