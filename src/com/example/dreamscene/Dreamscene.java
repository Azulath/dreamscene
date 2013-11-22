package com.example.dreamscene;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.graphics.Color;

public class Dreamscene extends Activity implements SensorEventListener
{
    private TextView tv;
    private TextView warning;
    private SensorManager sManager;
    private  SensorCoordinates sensorCoordinates = new SensorCoordinates();
    private long startTime = System.currentTimeMillis();

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //get the TextView from the layout file
        tv = (TextView) findViewById(R.id.tv);
        //get a hook to the sensor service
        sManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    //when this Activity starts
    @Override
    protected void onResume() {
        super.onResume();
		/*register the sensor listener to listen to the gyroscope sensor, use the
		 * callbacks defined in this class, and gather the sensor information as
		 * quick as possible*/
        sManager.registerListener(this, sManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),SensorManager.SENSOR_DELAY_FASTEST);
    }

    //When this Activity isn't visible anymore
    @Override
    protected void onStop() {
        //unregister the sensor listener
        sManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
        //Do nothing
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Formel: Sekunden = 1000ns * 1000us * 1000ms
        long timestamp = ((event.timestamp)/1000000 - startTime) / 1000;
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
                "Orientation X (Roll): "+ Float.toString(xSensor) +"\n"+
                "Orientation Y (Pitch): "+ Float.toString(ySensor) +"\n"+
                "Orientation Z (Yaw): "+ Float.toString(zSensor) +"\n" +
                "Timestamp: " + (timestamp) + "s"
        );

        // Sensor wie ma mog setzn hoit - am Epilepsiehandy is so praktischer
        if (xSensor > 0.8 || ySensor > 0.8 || zSensor > 0.8)
        {
            sensorCoordinates.addCoordinates(xSensor, ySensor, zSensor, timestamp);
        }

    }

    /**public void saveToFile(View view) {
        File root = Environment.getExternalStorageDirectory();
        File dir = new File (root.getAbsolutePath() + "/others");
        File file = new File(dir, "dreamscene.txt");

        try {
            FileOutputStream f = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(f);
            for(String str: sensorXData) {
                pw.println(str);
            }
            pw.flush();
            pw.close();
            f.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


//		String filename = "dreamscene";
//		FileOutputStream outputStream;
////		File file = new File(context.getFilesDir(), filename);
//
//		try {
//		  outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
//		  for(String str: sensorXData) {
//			  outputStream.write(str.getBytes());
//		  }
//
//		  outputStream.close();
//		} catch (Exception e) {
//		  e.printStackTrace();
//		}
    } **/

    public void terminate(View view) {
        moveTaskToBack(true);
    }
}
