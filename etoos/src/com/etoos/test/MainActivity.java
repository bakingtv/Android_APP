package com.etoos.test;



import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.etoos.data.EventData;
import com.etoos.view.NumberPickerDialog;
import com.euiweonjeong.base.BitmapManager;
import com.google.zxing.client.android.integration.IntentIntegrator;
import com.google.zxing.client.android.integration.IntentResult;

import com.slidingmenu.lib.SlidingMenu;

import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends EtoosBaseActivity implements OnClickListener{

	MainActivity self;
	ViewPager m_VP = null;	//ViewPager
	CustomPagerAdapter m_CPA = null;	//Ŀ���� �����


	// 
	private boolean m_CallBottomBanner = false;		// �ϴ� ��� ȣ�� ��Ÿ�̹��ΰ�? �ƴϸ� ��ܹ�� ȣ�� Ÿ�̹��ΰ�
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		AfterCreate();
		self = this;
		m_CallBottomBanner = false;

		m_CurrIndex =  0;






		m_VP = (ViewPager) findViewById(R.id.market_img);
		m_CPA = new CustomPagerAdapter();


		m_VP.setAdapter(m_CPA);

		//ViewPage ������ ���� ������
		m_VP.setOnPageChangeListener(new OnPageChangeListener()
		{

			@Override
			public void onPageScrollStateChanged(int state) {}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

			@Override
			public void onPageSelected(int position) 
			{
				selectViewPaper( position );

			}

		});


		BtnEvent(R.id.join);
		BtnEvent(R.id.login);
		BtnEvent(R.id.mode_1);
		BtnEvent(R.id.mode_2);
		
		

		{
			AppManagement _AppManager = (AppManagement) getApplication();
			_AppManager.ParamData.clear();


			Intent intent = new Intent(baseself, NetPopup.class);
			intent.putExtra("API", 0);
			startActivityForResult(intent , 1);
		}







	}

	public void BtnEvent( int id )
	{
		View imageview = (View)findViewById(id);
		imageview.setOnClickListener(this);
	}

	@Override
	public void onBackPressed() 
	{

		new AlertDialog.Builder(this)
		.setTitle("���� Ȯ��")
		.setMessage("���� ���� �ϰڽ��ϱ�?") //�ٿ���
		.setPositiveButton("��", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int whichButton)
			{   
				ExitApp();
			}
		})
		.setNegativeButton("�ƴϿ�", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int whichButton) 
			{
				//...����
			}
		})
		.show();

	}

	@Override
	public void onResume()
	{
		super.onResume();

	}




	@Override
	protected void onActivityResult(int requestCode, int resultcode, Intent data)
	{
		super.onActivityResult(requestCode, resultcode, data);
		AppManagement _AppManager = (AppManagement) getApplication();
		if ( requestCode == 1 )
		{
			if ( resultcode == 10 )
			{

				int callnum  = data.getIntExtra("return", -1);
				// ���� Ÿ�Կ� ���� parsing �ϱ� 

				switch ( callnum )

				{
				case 0:
				{

					JSONObject json;
					try 
					{
						json = new JSONObject(_AppManager.ParseString);
						if(json.getString("resultcode").equals("success"))
						{
							_AppManager.m_EventList.clear();
							JSONArray usageList = (JSONArray)json.get("eventlist");

							for(int i = 0; i < usageList.length(); i++)
							{
								EventData item = new EventData();
								JSONObject list = (JSONObject)usageList.get(i);

								item.url = (list.optString("url"));
								item.imgurl =  (list.optString("imgurl"));
								_AppManager.m_EventList.add(item);
							}

						}
						else
						{
							// ����...
						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				break;
				case 38:
					break;
				}

				Log.d("API Call Success", _AppManager.ParseString);
				//_AppManager.PParsingClass.LoginParsing(_AppManager.ParseString);

				// �����޼��� �޾Ƽ� �������� ����ؼ� �׿� ���� ó���� ���ش�. 

			}

			else
			{
				Log.d("API Call failed", "������ ��Ʈ��ũ API ������");
			}

		}
		else
		{
			Log.w("error", "���� �׳� �߸� ���°�, ��Ʈ��ũ �����̰ų� �׳� �ڵ� ���������� �׳� ���� ���Ѿ���");
		}
		initViewPaperDot(m_CPA.getCount());
		selectViewPaper(0);
	}


	///////////////////////////////////////////////////////////////////////////////////////////////////////
	/// ��� �� ������ 

	protected void selectViewPaper(int position)
	{
		int itemIds[] = {
				R.id.imageView_dot_1,
				R.id.imageView_dot_2,
				R.id.imageView_dot_3,
				R.id.imageView_dot_4,
				R.id.imageView_dot_5,
				R.id.imageView_dot_6,
				R.id.imageView_dot_7,
				R.id.imageView_dot_8,
				R.id.imageView_dot_9,
				R.id.imageView_dot_10,
		};
		int nMyId = itemIds[position];
		ImageView ivButton = null;
		for(int nId : itemIds)
		{
			ivButton = (ImageView) findViewById(nId);
			if(nId == nMyId)
			{
				ivButton.setSelected(true);
			}
			else
			{
				ivButton.setSelected(false);
			}
		}
	}

	private void initViewPaperDot( int count)
	{
		if ( count < 2 )
		{
			((View)findViewById(R.id.dot_header)).setVisibility(View.GONE);
			return;
		}
		((View)findViewById(R.id.dot_header)).setVisibility(View.VISIBLE);
		int itemIds[] = {
				R.id.imageView_dot_1,
				R.id.imageView_dot_2,
				R.id.imageView_dot_3,
				R.id.imageView_dot_4,
				R.id.imageView_dot_5,
				R.id.imageView_dot_6,
				R.id.imageView_dot_7,
				R.id.imageView_dot_8,
				R.id.imageView_dot_9,
				R.id.imageView_dot_10,

		};

		ImageView ivButton = null;
		int i = 0;
		for(int nId : itemIds)
		{
			ivButton = (ImageView) findViewById(nId);

			if (i < count)
			{
				ivButton.setVisibility(View.VISIBLE);
			}
			else
			{
				ivButton.setVisibility(View.GONE);
			}
			i++;
		}
	}



	private class CustomPagerAdapter extends PagerAdapter{


		@Override
		public int getCount() {
			AppManagement _AppManager = (AppManagement) getApplication();
			_AppManager.m_EventList.size();
			return _AppManager.m_EventList.size();
		}

		/**
		 * �� ������ ����
		 */
		@Override
		public Object instantiateItem(View collection, int position)
		{



			ImageView img = new ImageView(self); 
			final int pos = position;


			OnClickListener ol  = new OnClickListener()
			{

				@Override
				public void onClick(View v) 
				{

					// �̺�Ʈ �� ��������...
					
					AppManagement _AppManager = (AppManagement) getApplication();
					_AppManager.WEB_URL = _AppManager.m_EventList.get(pos).url;
					Intent intent;
					intent = new Intent().setClass(baseself, WebActivity.class);
					startActivity( intent ); 
					


				}            
			};
			img.setOnClickListener(ol);

			AppManagement _AppManager = (AppManagement) getApplication();
			// �׽�Ʈ ImageURL//
			img.setTag( _AppManager.m_EventList.get(pos).imgurl);
			BitmapManager.INSTANCE.loadBitmap_2( _AppManager.m_EventList.get(pos).imgurl, img);


			((ViewPager) collection).addView(img,0);  

			return img;

		}

		@Override
		public void destroyItem(View collection, int position, Object view) {
			((ViewPager) collection).removeView((View) view);
		}



		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view==((View)object);
		}


		@Override
		public void finishUpdate(View v) {
		}


		@Override
		public void restoreState(Parcelable pc, ClassLoader cl) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View v) {
		}

	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch ( v.getId())
		{
		case R.id.join:
		{
			AppManagement _AppManager = (AppManagement) getApplication();
			_AppManager.WEB_URL = "http://etoos.hubweb.net:8080/SMART_OMR/member/regiYak.jsp";
			Intent intent;
			intent = new Intent().setClass(baseself, WebActivity.class);
			startActivity( intent ); 
		}
		break;
		case R.id.login:
		{
			AppManagement _AppManager = (AppManagement) getApplication();
			_AppManager.WEB_URL = "http://etoos.hubweb.net:8080/SMART_OMR/login/login.jsp";
			Intent intent;
			intent = new Intent().setClass(baseself, WebActivity.class);
			startActivity( intent ); 
		}
		break;
		case R.id.mode_1:
		{
			AppManagement _AppManager = (AppManagement) getApplication();
			_AppManager.WEB_URL = "http://etoos.hubweb.net:8080/SMART_OMR/examinee/examinee.jsp";
			Intent intent;
			intent = new Intent().setClass(baseself, WebActivity.class);
			startActivity( intent ); 

		}
		break;

		case R.id.mode_2:
		{
			AppManagement _AppManager = (AppManagement) getApplication();
			_AppManager.WEB_URL = "http://etoos.hubweb.net:8080/SMART_OMR/examiner/examiner.jsp";
			Intent intent;
			intent = new Intent().setClass(baseself, WebActivity.class);
			startActivity( intent ); 






		}
		break;



		}






	}



}
