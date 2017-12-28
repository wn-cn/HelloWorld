package com.wangning.helloworld;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView tvResult;
    private Button btnTest1;
    private Button btnTest2;
    private String result;

    private static final String SERVICE_NAMESPACE = "http://sinotrans.com/";
    private static final String SERVICE_URL = "http://www.sd.sinotrans.com/cywebservice/CYWebService.asmx";
    private static final String SERVICE_METHOD_PLAN = "Plan";
    private static final String SERVICE_SOAPACTION_PLAN = "http://sinotrans.com/Plan";
    private static final String SERVICE_METHOD_SHIPPINGORDER = "ShippingOrder";
    private static final String SERVICE_SOAPACTION_SHIPPINGORDER = "http://sinotrans.com/ShippingOrder";

    //定义一个Handler用来更新页面：
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x001:
                    tvResult.setText("结果显示：\n" + result);
                    Toast.makeText(MainActivity.this, "获取计划信息成功", Toast.LENGTH_SHORT).show();
                    break;
                case 0x002:
                    tvResult.setText("结果显示：\n" + result);
                    Toast.makeText(MainActivity.this, "获取下货纸信息成功", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindViews();
    }

    private void bindViews() {
        tvResult = (TextView) findViewById(R.id.tvResult);
        btnTest1 = (Button)findViewById(R.id.btnTest1);
        btnTest2 = (Button)findViewById(R.id.btnTest2);
        btnTest1.setOnClickListener(this);
        btnTest2.setOnClickListener(this);
    }

    protected void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editTextName = (EditText) findViewById(R.id.txt_username);
        EditText editTextPwd = (EditText) findViewById(R.id.txt_userpwd);
        String msgName = editTextName.getText().toString();
        String msgPwd = editTextPwd.getText().toString();
        intent.putExtra("EXTRA_MESSAGE_NAME", msgName);
        intent.putExtra("EXTRA_MESSAGE_PWD", msgPwd);
        startActivity(intent);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnTest1:
                new Thread() {
                    @Override
                    public void run() {
                        getPlan();
                    }
                }.start();
                break;
            case R.id.btnTest2:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getShippingOrder();
                    }
                }).start();
                break;
        }
    }

    public void getPlan() {
        result = "";
        SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE, SERVICE_METHOD_PLAN);
        soapObject.addProperty("hBlno", "KMTCTAO3180366");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        HttpTransportSE httpTransportSE = new HttpTransportSE(SERVICE_URL);
        try {
            httpTransportSE.call(SERVICE_SOAPACTION_PLAN, envelope);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //获得服务返回的数据,并且开始解析
        SoapObject obj1 = (SoapObject)envelope.bodyIn;
        SoapObject obj2 = (SoapObject)obj1.getProperty(0);
        SoapObject obj3 = (SoapObject)obj2.getProperty(1);
        for (int i = 0; i < obj3.getPropertyCount(); i++) {
            SoapObject objDetail = (SoapObject)obj3.getProperty(i);
            result += objDetail.getPropertyAsString(0);
        }

        handler.sendEmptyMessage(0x001);
    }

    public void getShippingOrder() {
        result = "";
        SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE, SERVICE_METHOD_SHIPPINGORDER);
        soapObject.addProperty("hBlno", "KMTCTAO3180366");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        HttpTransportSE httpTransportSE = new HttpTransportSE(SERVICE_URL);
        try {
            httpTransportSE.call(SERVICE_SOAPACTION_SHIPPINGORDER, envelope);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //获得服务返回的数据,并且开始解析
        SoapObject object = (SoapObject)envelope.bodyIn;
        result = object.getProperty(0).toString();
        handler.sendEmptyMessage(0x002);
    }
}
