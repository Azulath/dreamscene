package com.example.dreamscene;

import android.os.Environment;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Crash with normal List!
 */
public class SensorCoordinates
{
    private LinkedList<Coordinates> coordinateses = new LinkedList<Coordinates>();
    private float[] xTmp;
    private float[] yTmp;
    private float[] zTmp;
    private long[] tTmp;
    private int iterator;
    private int arraySize;

    public SensorCoordinates(int size)
    {
        arraySize = size;
        xTmp = new float[arraySize];
        yTmp = new float[arraySize];
        zTmp = new float[arraySize];
        tTmp = new long[arraySize];
        iterator = 0;
    }

    public void addCoordinates(float x, float y, float z, long t)
    {
        xTmp[iterator] = x;
        yTmp[iterator] = y;
        zTmp[iterator] = z;
        tTmp[iterator] = t;

        if (iterator + 1 == arraySize)
        {
            mergeCoordinates();
        }

        iterator++;
    }

    private void mergeCoordinates()
    {
        float xAvg = 0;
        float yAvg = 0;
        float zAvg = 0;
        long tAvg = 0;
        for (int i = 0; i < arraySize; i++)
        {
            xAvg += xTmp[i];
            yAvg += yTmp[i];
            zAvg += zTmp[i];
            tAvg += tTmp[i];
        }

        xAvg /= arraySize;
        yAvg /= arraySize;
        zAvg /= arraySize;
        tAvg /= arraySize;

        addToList(xAvg, yAvg, zAvg, tAvg);
        iterator = 0;
    }

    private void addToList(float x, float y, float z, long t)
    {
        coordinateses.add(new Coordinates(x, y, z, t));
    }

    public void printToFile()
    {

        File down = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(down, "dreamscene.txt");

        try
        {
            FileOutputStream f = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(f);

            while (!coordinateses.isEmpty())
            {
                pw.println("XData: " + coordinateses.pollFirst().getX());
                pw.println("YData: " + coordinateses.pollFirst().getY());
                pw.println("ZData: " + coordinateses.pollFirst().getZ());
                pw.println("TimeStamp: " + coordinateses.pollFirst().getTime());
                pw.println();
            }

            pw.flush();
            pw.close();
            f.close();
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
