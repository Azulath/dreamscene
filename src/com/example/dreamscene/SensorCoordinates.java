package com.example.dreamscene;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;

import java.io.*;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

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
    private String deviceID;
    private Dreamscene ds;

    public SensorCoordinates(Dreamscene ds, int size, String id)
    {
        arraySize = size;
        xTmp = new float[arraySize];
        yTmp = new float[arraySize];
        zTmp = new float[arraySize];
        tTmp = new long[arraySize];
        iterator = 0;
        deviceID = id;
        this.ds = ds;
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
        ds.increaseCount();
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

            for (Coordinates elem : coordinates)
            {
                pw.println("X: " + elem.getX());
                pw.println("Y: " + elem.getY());
                pw.println("Z: " + elem.getZ());
                pw.println("TimeStamp: " + elem.getTime());
                pw.println("\n\r");
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

    public void upload()
    {
        try
        {
            for (Coordinates coord : coordinates)
            {
                SyncTask t = new SyncTask(ds, deviceID);
                String result = t.execute(coord).get(5, TimeUnit.SECONDS);

                if (!result.equals("success"))
                {
                    new AlertDialog.Builder(ds).setMessage("UPLOAD FAILED: " + result).show();
                    return;
                }

            }
        } catch (Exception e)
        {
            displayException(e, "upload data");
        }
    }

    public void clear()
    {
        coordinates = null;
        xTmp = yTmp = zTmp = null;
        tTmp = null;
    }

    private void displayException(Exception e, String whileAction)
    {
        new AlertDialog.Builder(ds).setTitle("EXCEPTION while '" + whileAction + "'").setMessage(
                "Type: " + e.getClass().toString() + "\r\n" + e.getMessage()
        ).show();
    }
}
