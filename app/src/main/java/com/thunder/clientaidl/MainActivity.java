package com.thunder.clientaidl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.thunder.serveraidl.IServerAidl_Interface;

public class MainActivity extends AppCompatActivity {
    private String TAG = "clientAidl";
    private Button bindSerButton;
    // define aidl interface
    private IServerAidl_Interface mAidlServerService;
    private RemoteServiceConnection serviceConnection;

    // create connection
    class RemoteServiceConnection implements ServiceConnection {
        //在Activity與Service建立關聯時調用
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(TAG, "Server aidl service connected");
            //使用AIDLService1.Stub.asInterface()方法將傳入的IBinder物件傳換成了mAIDL_Service對象
            mAidlServerService = IServerAidl_Interface.Stub.asInterface(service);
            Toast.makeText(MainActivity.this, "Service connected", Toast.LENGTH_LONG)
                    .show();
            //通過該物件調用在IServerAidl_Interface.aidl檔中定義的介面方法,從而實現跨進程通信
            try {
                mAidlServerService.AIDL_getServerData();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private void connectService() {
        serviceConnection = new RemoteServiceConnection();
        Intent i = new Intent("com.thunder.serveraidl.ServerService");
        i.setPackage("com.thunder.serveraidl");
        boolean ret = bindService(i, serviceConnection, Context.BIND_AUTO_CREATE);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindSerButton = (Button) findViewById(R.id.bind_service);
        bindSerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "user click the bind service");
                connectService();
                //通過Intent指定服務端的服務名稱和所在包，與遠端Service進行綁定
                //參數與伺服器端的action要一致,即"packageName.aidl介面檔案名"
                /*
                Intent intent = new Intent()
                        .setComponent(new ComponentName(
                                "com.thunder.serveraidl",
                                "com.thunder.serveraidl.ServerService"));
                //綁定服務,傳入intent和ServiceConnection對象
                startForegroundService(intent);
                bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
*/
                /*
                Intent intent2 = new Intent("com.thunder.serveraidl.IServerAidl_Interface");
                intent2.addCategory(Intent.CATEGORY_DEFAULT);
                intent2.setPackage("com.thunder.serveraidl");
                startService(intent2);
                bindService(intent2, mConnection, Context.BIND_AUTO_CREATE);

                 */
                /*
                // This is the package name of the APK, set in the Android manifest
                String REMOTE_SERVICE_COMPONENT_NAME = "com.thunder.serveraidl";
                // This is the name of the service, according the value of ServiceAttribute.Name
                String REMOTE_SERVICE_PACKAGE_NAME   = "com.thunder.serveraidl.ServerService";

// Provide the package name and the name of the service with a ComponentName object.
                ComponentName cn = new ComponentName(REMOTE_SERVICE_PACKAGE_NAME, REMOTE_SERVICE_COMPONENT_NAME);
                Intent serviceToStart = new Intent();
                serviceToStart.SetComponent(cn);

                 */
            }
        });
    }
}