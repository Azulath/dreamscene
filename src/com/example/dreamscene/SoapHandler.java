package com.example.dreamscene;

import org.apache.http.message.BasicNameValuePair;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class SoapHandler
{
    public final String WSDL_TARGET_NAMESPACE = "http://tempuri.org";
    public String SOAP_ADDRESS; // = "http://rmu.cc/DsWeb/WebService/WebService.asmx";

    public SoapHandler(String SoapAddress)
    {
        this.SOAP_ADDRESS = SoapAddress;
    }

    public  String HelloWorld() throws Exception
    {
        return  CallService("HelloWorld", true).toString();
    }

    public String UploadSensorData(String DeviceIdentification, long TimeStamp, float x, float y, float z) throws Exception
    {
        String result = CallService("UploadSensorData", true,
                new BasicNameValuePair("AuthKey", "0be0c768732bf998b2d66579aa1f5ad10101012163cc398aec31bf419f66a918"),
                new BasicNameValuePair("DeviceIdentification", DeviceIdentification),
                new BasicNameValuePair("TimeStamp", Long.toString(TimeStamp)),
                new BasicNameValuePair("MoveX", Float.toString(x)),
                new BasicNameValuePair("MoveY", Float.toString(y)),
                new BasicNameValuePair("MoveZ", Float.toString(z))
        ).toString();

        return result;
    }

    private Object CallService(String MethodName, boolean Simple, BasicNameValuePair... params) throws Exception
    {
        final String SOAP_ACTION = "http://tempuri.org" + MethodName;

        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, MethodName);

        for (BasicNameValuePair pair : params)
        {
            PropertyInfo pi = new PropertyInfo();
            pi.setName(pair.getName());
            pi.setValue(pair.getValue());
            request.addProperty(pi);
        }

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        httpTransport.call(SOAP_ACTION, envelope);

        if (Simple) return envelope.getResponse();
        else return envelope.bodyIn;
    }
}
