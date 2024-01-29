package com.cloudpos.aidl.tester;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.cloudpos.utils.Logger;
import com.cloudpos.utils.TextViewUtil;
import com.wizarpos.wizarviewagentassistant.aidl.ISystemExtApi;


public class MainActivity extends AbstractActivity implements OnClickListener , ServiceConnection{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_test1 = (Button) this.findViewById(R.id.btn_test1);
        Button btn_test2 = (Button) this.findViewById(R.id.btn_test2);
        Button btn_test3 = (Button) this.findViewById(R.id.btn_test3);

        ((Button) this.findViewById(R.id.btn_test4)).setOnClickListener(this);
        ((Button) this.findViewById(R.id.btn_test5)).setOnClickListener(this);
        ((Button) this.findViewById(R.id.btn_test6)).setOnClickListener(this);
        ((Button) this.findViewById(R.id.btn_test7)).setOnClickListener(this);
        ((Button) this.findViewById(R.id.btn_test8)).setOnClickListener(this);

        log_text = (TextView) this.findViewById(R.id.text_result);
        log_text.setMovementMethod(ScrollingMovementMethod.getInstance());

        findViewById(R.id.settings).setOnClickListener(this);
        btn_test1.setOnClickListener(this);
        btn_test2.setOnClickListener(this);
        btn_test3.setOnClickListener(this);


        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == R.id.log_default) {
                    log_text.append("\t" + msg.obj + "\n");
                } else if (msg.what == R.id.log_success) {
                    String str = "\t" + msg.obj + "\n";
                    TextViewUtil.infoBlueTextView(log_text, str);
                } else if (msg.what == R.id.log_failed) {
                    String str = "\t" + msg.obj + "\n";
                    TextViewUtil.infoRedTextView(log_text, str);
                } else if (msg.what == R.id.log_clear) {
                    log_text.setText("");
                }
            }
        };
        bindSystemExtService();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unbindService(this);
    }


    public void bindSystemExtService() {
        try {
            startConnectService(MainActivity.this,
                    "com.wizarpos.wizarviewagentassistant",
                    "com.wizarpos.wizarviewagentassistant.SystemExtApiService", this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    protected boolean startConnectService(Context mContext, String packageName, String className, ServiceConnection connection) {
        boolean isSuccess = startConnectService(mContext, new ComponentName(packageName, className), connection);
        writerInLog("bind service result : " + isSuccess, R.id.log_default);
        return isSuccess;
    }

    protected boolean startConnectService(Context context, ComponentName comp, ServiceConnection connection) {
        Intent intent = new Intent();
        intent.setPackage(comp.getPackageName());
        intent.setComponent(comp);
        boolean isSuccess = context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
        comp = context.startService(intent);
        Logger.debug("bind service (%s, %s)", isSuccess, comp.getPackageName(), comp.getClassName());
        return isSuccess;
    }

    @Override
    public void onClick(View arg0) {
        int index = arg0.getId();
        if (index == R.id.btn_test1) {
//            setUsrProp();//version >= 2.10.50
//            test2();
            setStatusBarLocked(true);
        } else if (index == R.id.btn_test2) {
            setStatusBarLocked(false);
        } else if (index == R.id.btn_test3) {
        } else if (index == R.id.btn_test4) {
        } else if (index == R.id.btn_test5) {
        } else if (index == R.id.btn_test6) {
        } else if (index == R.id.btn_test7) {
        } else if (index == R.id.btn_test8) {
        } else if (index == R.id.settings) {
            log_text.setText("");
        }
    }

    private void setStatusBarLocked(boolean lock){
        try {
            systemExtApi.setStatusBarLocked(lock);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }


    private ISystemExtApi systemExtApi;
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        try {
//            writerInLog("onServiceConnected:" + service.getInterfaceDescriptor(), R.id.log_default);
            systemExtApi = ISystemExtApi.Stub.asInterface(service);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

}
