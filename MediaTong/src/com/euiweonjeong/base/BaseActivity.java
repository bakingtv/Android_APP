package com.euiweonjeong.base;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;

// �⺻ Activity
public class BaseActivity extends Activity
{

	public ProgressDialog mProgress;
	protected String TAG = "BaseActivity";
	
	public static ArrayList<Activity> 	activityList1 = new ArrayList<Activity>();;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		TAG = getClassName(getClass());
		
		Log.e(TAG, "onCreate");
		
		activityList1.add(this);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		Log.e(TAG, "onDestroy");
	}

	@Override
	protected void onNewIntent(Intent intent)
	{
		super.onNewIntent(intent);
		Log.e(TAG, "onNewIntent");
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		Log.e(TAG, "onPause");
	}

	@Override
	protected void onRestart()
	{
		super.onRestart();
		Log.e(TAG, "onRestart");
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		Log.e(TAG, "onResume");
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		Log.e(TAG, "onSaveInstanceState");
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		Log.e(TAG, "onStart");
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		Log.e(TAG, "onStop");
	}

	@Override
	public void onUserInteraction()
	{
		super.onUserInteraction();
		Log.e(TAG, "onUserInteraction");
	}

	@Override
	protected void onUserLeaveHint()
	{
		super.onUserLeaveHint();
		Log.e(TAG, "onUserLeaveHint");
	}
	


	protected String getClassName(Class<?> cls)
	{
		String FQClassName = cls.getName();
		int firstChar = FQClassName.lastIndexOf('.') + 1;
			
		if(firstChar > 0)
		{
			FQClassName = FQClassName.substring(firstChar);
		}
			
		return FQClassName;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		Log.e(TAG, "onKeyDown");
		return super.onKeyDown(keyCode, event);
	}
	
	public void RefreshUI()
	{
		
	}
	
    public void ImageBtnRefresh( int id , int bitmapID )
    {
    	
    	ImageView imageview = (ImageView)findViewById(id);
    	imageview.setBackgroundResource(bitmapID);
    }
	
	
	public void ShowAlertDialLog( Activity activity , String message )
	{
		// �ϴ� ���� ����� �Ǿ����� Ȯ���Ѵ�. 
		// �������� ��쿡 ���� ó���Ѵ�.
		// 1. ���������� ��� �Ǿ��� ���
		// 2. �̹� ����� �Ǿ� �־��� ���
		// 3. ���� �� ������ ����� �ȵ� ��� 
		
		// ���̾�α׸� ���� 
		new AlertDialog.Builder(activity)

	
		.setTitle("����")
		.setMessage(message)
		.setPositiveButton("Ȯ��", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		})
		.show();
	}
	public void ShowAlertDialLog( Activity activity , String titleMessage , String message )
	{
		// �ϴ� ���� ����� �Ǿ����� Ȯ���Ѵ�. 
		// �������� ��쿡 ���� ó���Ѵ�.
		// 1. ���������� ��� �Ǿ��� ���
		// 2. �̹� ����� �Ǿ� �־��� ���
		// 3. ���� �� ������ ����� �ȵ� ��� 
		
		// ���̾�α׸� ���� 
		new AlertDialog.Builder(activity)

	
		.setTitle(titleMessage)
		.setMessage(message)
		.setPositiveButton("Ȯ��", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		})
		.show();
	}
	
	
}
