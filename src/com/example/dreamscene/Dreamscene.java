package com.example.dreamscene;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import android.graphics.Color;

import java.io.*;
import java.util.LinkedList;

import static java.lang.Math.abs;

public class Dreamscene extends Activity implements SensorEventListener
{
    private TextView tv;
    private SensorManager sManager;
    private SensorCoordinates sensorCoordinates = new SensorCoordinates(5);
    private long startTime = System.currentTimeMillis();

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //get the TextView from the layout file
        tv = (TextView) findViewById(R.id.tv);
        //get a hook to the sensor service
        sManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    //when this Activity starts
    @Override
    protected void onResume()
    {
        super.onResume();
        /*register the sensor listener to listen to the gyroscope sensor, use the
		 * callbacks defined in this class, and gather the sensor information as
		 * quick as possible*/
        sManager.registerListener(this, sManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                SensorManager.SENSOR_DELAY_FASTEST);
    }

    //When this Activity isn't visible anymore
    @Override
    protected void onStop()
    {
        //un register the sensor listener
        sManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1)
    {
        //Do nothing
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        // Formula: Seconds = 1000ns * 1000us * 1000ms
        long timestamp = ((event.timestamp) / 1000000 - startTime);
        //if sensor is unreliable, return void
        if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE)
        {
            tv.setTextColor(Color.RED);
        }
        float xSensor = event.values[2];
        float ySensor = event.values[1];
        float zSensor = event.values[0];
        //else it will output the Roll, Pitch and Yawn values
        tv.setText(
                "Orientation X (Roll): " + Float.toString(xSensor) + "\n" +
                        "Orientation Y (Pitch): " + Float.toString(ySensor) + "\n" +
                        "Orientation Z (Yaw): " + Float.toString(zSensor) + "\n" +
                        "Timestamp: " + (timestamp/1000) + "s"
        );

        // Sensor wie ma mog setzn hoit - am Epilepsiehandy is so praktischer
        // abs(x) da ja sonst nur positive Werte vorkommen...
        if (abs(xSensor) > 0.1 || abs(ySensor) > 0.1 || abs(zSensor) > 0.1)
        {
            sensorCoordinates.addCoordinates(xSensor, ySensor, zSensor, timestamp);
        }
    }

    public void saveToFile(View view)
    {
        sensorCoordinates.printToFile();
    }

    public void terminate(View view)
    {
        moveTaskToBack(true);
    }

    private String DeviceID()
    {
        return "35" +
                Build.BOARD.length()%10 + Build.BRAND.length()%10 +
                Build.CPU_ABI.length()%10 + Build.DEVICE.length()%10 +
                Build.DISPLAY.length()%10 + Build.HOST.length()%10 +
                Build.ID.length()%10 + Build.MANUFACTURER.length()%10 +
                Build.MODEL.length()%10 + Build.PRODUCT.length()%10 +
                Build.TAGS.length()%10 + Build.TYPE.length()%10 +
                Build.USER.length()%10; // 13 digits
    }

    class SyncTask extends AsyncTask<Object, Void, String>
    {
        private ProgressDialog dialog = new ProgressDialog(Dreamscene.this);

        protected void onPreExecute()
        {
            dialog.setMessage("Uploading...");
            dialog.show();
        }

        protected  String doInBackground(Object... params)
        {
            SoapHandler sh;
            Coordinates coordinates;

            try
            {
                coordinates = (Coordinates) params[0];
                String url = "http://rmu.cc/DsWeb/WebService/WebService.asmx";
                sh = new SoapHandler(url);
            }
            catch (Exception e)
            {
                return  e.getMessage();
            }

            String result;

            try
            {
                String id = DeviceID();
                result = sh.UploadSensorData(id, coordinates.getTime(),
                        coordinates.getX(), coordinates.getY(), coordinates.getZ());
            }
            catch (Exception e)
            {
                result = e.toString();
            }
            return result;
        }

        protected void onPosExecute(String result)
        {
            if (dialog.isShowing()) dialog.dismiss();
        }
    }
}
