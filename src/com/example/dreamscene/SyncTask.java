package com.example.dreamscene;

import android.app.ProgressDialog;
import android.os.AsyncTask;

public class SyncTask extends AsyncTask<Object, Void, String>
{
    private ProgressDialog dialog;
    private String id;

    public SyncTask(Dreamscene ds, String id)
    {
        dialog = new ProgressDialog(ds);
        this.id = id;
    }

    public void onPreExecute()
    {
        dialog.setMessage("Uploading...");
        dialog.show();
    }

    public String doInBackground(Object... params)
    {
        SoapHandler sh;
        Coordinates co;
        String result;

        try
        {
            co = (Coordinates) params[0];
            String url = "http://rmu.cc/DsWeb/WebService/WebService.asmx";
            sh = new SoapHandler(url);
        }
        catch (Exception e)
        {
            return e.getMessage();
        }

        try
        {
            result = sh.UploadSensorData(id, co.getTime(),
                    co.getX(), co.getY(), co.getZ());
        }
        catch (Exception e)
        {
            result = e.toString();
        }
        return  result;
    }

    public void onPostExecute(String result)
    {
        if (dialog.isShowing())
        {
            dialog.dismiss();
        }
    }
}
