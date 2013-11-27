package com.example.dreamscene;

import android.os.Environment;

import java.io.*;
import java.util.LinkedList;

/**
 * Crash with normal List!
 */
public class SensorCoordinates
{
    private LinkedList<Coordinates> coordinates = new LinkedList<Coordinates>();
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
        coordinates.add(new Coordinates(x, y, z, t));
    }

    public void printToFile()
    {

        File down = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(down, "dreamscene.txt");

        try
        {
            FileOutputStream f = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(f);

            while (!coordinates.isEmpty())
            {
                Coordinates elem = coordinates.pollFirst();
                pw.println("XData: " + elem.getX());
                pw.println("YData: " + elem.getY());
                pw.println("ZData: " + elem.getZ());
                pw.println("TimeStamp: " + elem.getTime());
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

/*    public void upload()
    {
        while (!coordinates.isEmpty())
        {

        }
    }*/

    public LinkedList<Coordinates> getCoordinates()
    {
        return coordinates;
    }
}
