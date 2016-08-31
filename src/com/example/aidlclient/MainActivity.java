package com.example.aidlclient;

import com.example.aidl.ItestAidl;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity implements OnClickListener {

	private EditText mEtNum1, mEtNum2, mEtRes;
	private Button mBtnAdd;
	protected ItestAidl itestAidl;
	private ServiceConnection conn = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			itestAidl = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			itestAidl = ItestAidl.Stub.asInterface(service);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();
		// 软件一启动就绑定服务
		bindService();
	}

	private void initView() {
		mEtNum1 = (EditText) findViewById(R.id.id_num1);
		mEtNum2 = (EditText) findViewById(R.id.id_num2);
		mEtRes = (EditText) findViewById(R.id.id_result);
		mBtnAdd = (Button) findViewById(R.id.btn_add);
		mBtnAdd.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		try {
			int result = itestAidl.add(
					Integer.parseInt(mEtNum1.getText().toString()),
					Integer.parseInt(mEtNum2.getText().toString()));
			mEtRes.setText(result+"");
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		

	}

	private void bindService() {
		// 获取到服务端
		Intent intent = new Intent();
		intent.setComponent(new ComponentName("com.example.aidl",
				"com.example.aidl.IRemoteService"));

		bindService(intent, conn, Context.BIND_AUTO_CREATE);
	}
	 @Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(conn);
	}

}
