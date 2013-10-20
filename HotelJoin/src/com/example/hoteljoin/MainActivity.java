package com.example.hoteljoin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.euiweonjeong.base.BitmapManager;
import com.example.hoteljoin.IntroActivity.IntroRunnAble;
import com.example.hoteljoin.TravelActivity.TravelListData;
import com.slidingmenu.lib.SlidingMenu;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView.ScaleType;
import android.support.v4.view.ViewPager.OnPageChangeListener;

public class MainActivity extends HotelJoinBaseActivity implements OnClickListener{
	MainActivity  self;
	 SlidingMenu menu ;
	 int MenuSize;
	 
	 
	 
		
	 
	 private SharedPreferences mPrefs;
	 
	 
	public class MainEvent
	{
		String eventNum;
		String imageUrl;

	}
	 
	boolean bChange = false;
	boolean bChange2 = false;
	ArrayList<MainEvent>  	m_List = new ArrayList<MainEvent>();
	Gallery m_Gallery ;
	
	ViewPager vp_main = null;	//ViewPager
	CustomPagerAdapter cpa = null;	//Ŀ���� �����
	
	int CurrPos = 0;
	
	int OriPos = 0;
	
	int MoveCount = 0;
	
	private Handler handler2 = new Handler();
	
	Calendar c;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		{
        	mProgress = new ProgressDialog(this);
    		mProgress.setMessage("��ø� ��ٷ� �ּ���.");
    		mProgress.setIndeterminate(true);
    		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		mProgress.setCancelable(false);
        }
        
		// �����ε� �� ����
		setBehindContentView(R.layout.main_menu_1);
		
		self = this;
		

		
		// �����̵� ��
		menu = getSlidingMenu();
		menu.setMode(SlidingMenu.RIGHT);
		
		{
			Display defaultDisplay = ((WindowManager)this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
	    	
			int width;
			@SuppressWarnings("deprecation")
			int windowswidth = defaultDisplay.getWidth();
			@SuppressWarnings("deprecation")
			int windowsheight= defaultDisplay.getHeight();
			int originwidth = 480;
			

			width = (int) ( (float) 340 * ( (float)(windowswidth)/ (float)(originwidth) )  );

			
			menu.setBehindOffset(windowswidth - width );
		}
		
		menu.setFadeDegree(0.35f);
		
		AfterCreate(0);
		
		setTextFont( R.id.main_menu_text);
		
		BtnEvent( R.id.sub_bar);
		ImageResize2( R.id.menu_1);
		ImageResize2( R.id.menu_2);
		BtnEvent( R.id.main_btn_1);
		BtnEvent( R.id.main_btn_2);
		BtnEvent( R.id.main_btn_3);
		BtnEvent( R.id.main_btn_4);
		BtnEvent( R.id.main_btn_5);
		BtnEvent( R.id.main_btn_6);
		BtnEvent( R.id.main_btn_7);
		
		
		
		TextResize2( R.id.main_menu_text);
		ImageBtnResize2( R.id.main_menu_2_btn);
		ImageBtnResize2( R.id.main_menu_3_btn);
		ImageBtnResize2( R.id.main_menu_4_btn);
		ImageResize2( R.id.main_menu_5_btn);
		ImageResize2( R.id.main_menu_6_btn);
		
		ImageResize2( R.id.main_menu_5_switch);
		ImageResize2( R.id.main_menu_6_switch);
		ImageResize2( R.id.main_menu_1);
		
		
		ImageResize2( R.id.login_id_data);
		ImageResize2( R.id.login_pass_data);
		ImageResize2( R.id.menu_2_1);
		ImageBtnResize2( R.id.menu_2_1_1);
		ImageResize2( R.id.menu_2_2);
		ImageBtnResize2( R.id.menu_2_1_2);
		
		
		Switch twiterswitch = (Switch) findViewById(R.id.main_menu_6_switch);
		
		m_List.clear();
		
			

		
		
		
		/*m_Gallery = (Gallery)findViewById(R.id.home_stage_img);
        
        m_Gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
        {
        	 
        	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
        	{
        		// ��Ŀ���� �� View�� ���� ������ ���  �̺�Ʈ ó���� �� �� �ִ�. 
        		

        		
        	}
        	 
        	public void onNothingSelected(AdapterView<?> arg0) 
        	{
        	}
        	  
        }
        );
        
        m_Gallery.setOnItemClickListener(new OnItemClickListener() {
       
            public void onItemClick(AdapterView parent, View v, int position, long id) 
            {
            	AppManagement _AppManager = (AppManagement) getApplication();
            	switch (position)
            	{
            	case 0:
            	{
            		_AppManager.m_EventNum =681;
	                
					Intent intent;
		            intent = new Intent().setClass(baseself, EventDetailMainActivity.class);
		            startActivity( intent );	
            	}
            		break;
            	case 1:
            	{
            		_AppManager.m_EventNum = 696;
	                
					Intent intent;
		            intent = new Intent().setClass(baseself, EventDetailMainActivity.class);
		            startActivity( intent );
            	}
            		break;
            	case 2:
            	{
            		_AppManager.m_EventNum = 707;
	                
					Intent intent;
		            intent = new Intent().setClass(baseself, EventDetailMainActivity.class);
		            startActivity( intent );
            	}
            		break;
            	case 3:
            	{
            		_AppManager.m_EventNum = 711;
	                
					Intent intent;
		            intent = new Intent().setClass(baseself, EventDetailMainActivity.class);
		            startActivity( intent );
            	}
            		break;
            	}

            	
            }

        });*/
        
	        
	        
	    
	        
	    {
	    	mPrefs = getSharedPreferences( "login" ,MODE_PRIVATE);   
	        String auto = mPrefs.getString("auto", "false");
	        
	        if ( auto.equals("true"))
	        {
	        	AppManagement _AppManager = (AppManagement) getApplication();
	        	_AppManager.m_LoginCheck = true;
				_AppManager.m_LoginID = mPrefs.getString("id", "false");
				_AppManager.m_Password = mPrefs.getString("pass", "false");
				
				_AppManager.m_NickName = mPrefs.getString("nickname", "false");;
				_AppManager.m_Name = mPrefs.getString("name", "false");
				_AppManager.m_Email = mPrefs.getString("email", "false");
				_AppManager.m_Mobile = mPrefs.getString("mobile", "false");
				twiterswitch.setChecked(true);
	        }
	        else
	        {
	        	twiterswitch.setChecked(false);
	        }
	 
	    }
	    
	    
	 /*   {
        	Bitmap[] temp = new Bitmap[ 4 ];
        	temp[0] = BitmapFactory.decodeResource(getResources(), R.drawable.event_681);;
        	temp[1] = BitmapFactory.decodeResource(getResources(), R.drawable.event_696);;
        	temp[2] = BitmapFactory.decodeResource(getResources(), R.drawable.event_707);;
        	temp[3] = BitmapFactory.decodeResource(getResources(), R.drawable.event_711);;
        	m_Gallery.setAdapter(new ImageAdapter( this, temp));
    	}*/

		BottomMenuDown ( true);
		
		
		{
			 //handler2.postDelayed(TimerRunner, 7000);;
			vp_main = (ViewPager) findViewById(R.id.home_stage_img);
			cpa = new CustomPagerAdapter();
			
			vp_main.setAdapter(cpa);
			 //ViewPage ������ ���� ������
			vp_main.setOnPageChangeListener(new OnPageChangeListener()
	        {

				@Override
				public void onPageScrollStateChanged(int state) {}

				@Override
				public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

				@Override
				public void onPageSelected(int position) 
				{
					//�������� ����ɶ� ����Ǵ� ������ �����ǿ� ���� üũ(��ư�� Ȱ��ȭ/��Ȱ��ȭ ������)
					//pageCheck(position);		
					
					/* if(position < m_List.size())        //1��° �����ۿ��� ������ ���������� �̵��ϸ�
						 vp_main.setCurrentItem(position+ m_List.size(), false); //�̵� �ִϸ��̼��� ���� �ؾ� �Ѵ�
		                else if(position >=  m_List.size()*2)     //������ �����ۿ��� 1��° ���������� �̵��ϸ�
		                	vp_main.setCurrentItem(position -  m_List.size(), false);*/
				}
	        	
	        });
			
			vp_main.setOnTouchListener(new OnTouchListener()
			{

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					final int action = arg1.getAction() & MotionEventCompat.ACTION_MASK;
					switch( action )
					{
					case MotionEvent.ACTION_MOVE:
					case MotionEvent.ACTION_DOWN:
						handler2.removeCallbacks(TimerRunner);
						break;
					case MotionEvent.ACTION_UP:
						handler2.postDelayed(TimerRunner, 7000);;
						break; 
					}
					
					return false;
				}
				
			});
	           
		}
		GetEventData();
		
	}
	
	@Override
    public void onBackPressed() 
    {
    	
    	// ���� ȭ������ �� ���ư��� ���´�.( �ƿ� ���ᰡ �ǵ����Ѵ� )
    	// �ϴ��� ���ư��� �ְ� �׽�Ʈ������ ����д�. 
    	new AlertDialog.Builder(this)
		 .setTitle("���� �޼���")
		 .setMessage("���� �Ͻðڽ��ϱ�?") //�ٿ���
		 .setPositiveButton("��", new DialogInterface.OnClickListener() 
		 {
		     public void onClick(DialogInterface dialog, int whichButton)
		     {   
				moveTaskToBack(true);
			    android.os.Process.killProcess(android.os.Process.myPid());
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
		this.showContent();
	}
	
	public void BtnEvent( int id )
    {
		ImageView imageview = (ImageView)findViewById(id);
		imageview.setOnClickListener(this);
    }

	
	public void ImageBtnResize( int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	View imageview = (View)findViewById(id);
        _AppManager.GetUISizeConverter().ConvertFrameLayoutView(imageview); 
        imageview.setOnClickListener(this);
    }
	
	public void ImageBtnResize2( int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	View imageview = (View)findViewById(id);
        _AppManager.GetUISizeConverter().ConvertLinearLayoutView(imageview); 
        imageview.setOnClickListener(this);
    }
	
	
	


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.main_btn_1:
		{
			Intent intent;
            intent = new Intent().setClass(baseself, HotelSearchActivity.class);
            startActivity( intent ); 
		}
			break;
		case R.id.main_btn_2:
		{
			Intent intent;
            intent = new Intent().setClass(baseself, MyLocationActivity.class);
            startActivity( intent ); 
		}
			break;
		case R.id.main_btn_5:
		{
			Intent intent;
            intent = new Intent().setClass(baseself, EventMainActivity.class);
            startActivity( intent ); 
		}
			break;
		case R.id.main_btn_6:
		{

			Intent intent;
            intent = new Intent().setClass(baseself, SNSActivity.class);
            startActivity( intent ); 
			
		}
			break;
		case R.id.main_btn_7:
		{
			Intent intent;
            intent = new Intent().setClass(baseself, CompanyActivity.class);
            startActivity( intent ); 
			

			
			//self.ShowAlertDialLog( self ,"ȸ��Ұ�" , "ȸ��Ұ��޴���...");
		}
			break;
		case R.id.main_btn_3:
		{
			//self.ShowAlertDialLog( self ,"����Ȯ��" , "������������ �̵��ϱ� ");
			AppManagement _AppManager = (AppManagement) getApplication();
			if ( _AppManager.m_LoginCheck == false )
			{
				Intent intent;
	            intent = new Intent().setClass(baseself, LoginActivity.class);
	            startActivity( intent ); 
			}
			else
			{
				Intent intent;
	            intent = new Intent().setClass(baseself, ReservationActivity.class);
	            startActivity( intent ); 
			}
			
		}
			break;
		case R.id.main_btn_4:
		{
			AppManagement _AppManager = (AppManagement) getApplication();
			_AppManager.m_UsbHotelcode= false; 
			_AppManager.m_DirectMyTravel = false;
			Intent intent;
          //  intent = new Intent().setClass(baseself, TravelActivity.class);
			intent = new Intent().setClass(baseself, TravelActivity.class);
			
			
            startActivity( intent ); 
		}
			break;

			
			
			
			
			
			
		case  R.id.sub_bar:
		{
			AppManagement _AppManager = (AppManagement) getApplication();
			if ( _AppManager.m_LoginCheck == false )
			{
				/*Intent intent;
	            intent = new Intent().setClass(baseself, LoginActivity.class);
	            startActivity( intent );*/
				
				((View)findViewById(R.id.menu_1)).setVisibility(View.GONE);
				((View)findViewById(R.id.menu_2)).setVisibility(View.VISIBLE);
				this.showMenu();
			}
			else
			{
				this.showMenu();
				((View)findViewById(R.id.menu_1)).setVisibility(View.VISIBLE);
				((View)findViewById(R.id.menu_2)).setVisibility(View.GONE);
			}
			
		}
			break;
		case R.id.main_menu_4_btn:
			{
				AppManagement _AppManager = (AppManagement) getApplication();
				if ( _AppManager.m_LoginCheck == false )
				{
					
				}
				else
				{
					Intent intent;
		            intent = new Intent().setClass(baseself, ZzimActivity.class);
		            startActivity( intent ); 
				}
	
				
			}
			break;
		case R.id.main_menu_3_btn:
		{
			AppManagement _AppManager = (AppManagement) getApplication();
			_AppManager.m_DirectMyTravel = true;
			_AppManager.m_UsbHotelcode= false; 
			Intent intent;
            intent = new Intent().setClass(baseself, TravelActivity.class);
            startActivity( intent );
			break;
		}
		case R.id.menu_2_1_1:
			Login();
			break;
		case R.id.menu_2_1_2:
		{
			Intent intent;
            intent = new Intent().setClass(baseself, JoinActivity.class);
            startActivity( intent );
			
		}
			break;
			

		}
		
	}
	
	
	public void Login()
	{
		final  AppManagement _AppManager = (AppManagement) getApplication();
		mProgress.show();
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{
				// ID�� �н����带 �����´� 
				

				EditText id = (EditText)findViewById(R.id.login_id_data);
				EditText pass = (EditText)findViewById(R.id.login_pass_data);
				Map<String, String> data = new HashMap  <String, String>();

				data.put("memberId", id.getText().toString());
				data.put("password", pass.getText().toString());
				
				if (id.getText().toString().equals(""))
				{
					handler.sendEmptyMessage(2);
					return;
				}
				
				if (pass.getText().toString().equals(""))
				{
					handler.sendEmptyMessage(3);
					return;
				}
				
				
				String strJSON = _AppManager.GetHttpManager().GetHTTPData(_AppManager.DEF_URL +  "/mweb/member/login.gm", data);

				try 
				{
					JSONObject json = new JSONObject(strJSON);
					if(json.getString("errorCode").equals("0"))
					{


						_AppManager.m_LoginCheck = true;
						_AppManager.m_LoginID = id.getText().toString();
						_AppManager.m_Password = pass.getText().toString();
						
						_AppManager.m_NickName = _AppManager.GetHttpManager().DecodeString(json.getString("nickname"));
						_AppManager.m_Name = _AppManager.GetHttpManager().DecodeString(json.getString("name"));
						_AppManager.m_Email = _AppManager.GetHttpManager().DecodeString(json.getString("email"));
						_AppManager.m_Mobile =_AppManager.GetHttpManager().DecodeString(json.getString("mobile"));
						handler.sendEmptyMessage(217);
					}
					else 
					{
						// ���� �޼����� �����Ѵ�. 
						handler.sendMessage(handler.obtainMessage(1,_AppManager.GetHttpManager().DecodeString(json.getString("errorMsg")) ));
						return ;
					}
				} catch (JSONException e) 
				{
					// TODO Auto-generated catch block
					handler.sendMessage(handler.obtainMessage(1,"Error" ));
					e.printStackTrace();
				} 
			}
		});
		thread.start();
	}
	
	
	final Handler handler = new Handler( )
	{
    	
    	
    	public void handleMessage(Message msg)
		{
			mProgress.dismiss();
			switch(msg.what)
			{
			case 217:
				menu.showContent();
				AppManagement _AppManager = (AppManagement) getApplication();
	        	 
				SharedPreferences preferences = getSharedPreferences( "login" ,MODE_PRIVATE);
		        SharedPreferences.Editor editor = preferences.edit();
		        editor.putString("id", _AppManager.m_LoginID ); //Ű��, ���尪
		        editor.putString("name",_AppManager.m_Name );
		        editor.putString("email",_AppManager.m_Email );
		        editor.putString("nickname",_AppManager.m_NickName );
		        editor.putString("mobile",_AppManager.m_Mobile );
		        editor.putString("pass",_AppManager.m_Password );
		        editor.commit();


				break;
			case 1:
				// ����ó�� 
				self.ShowAlertDialLog( self ,"����" , (String) msg.obj );
				break;
			case 2:
				self.ShowAlertDialLog( self ,"����" , "���̵� �Էµ��� �ʾҽ��ϴ�.");
				break;	
			case 3:
				self.ShowAlertDialLog( self ,"����" , "�н����尡 �Էµ��� �ʾҽ��ϴ�.");
				break;
			case 20:
				handler2.postDelayed(TimerRunner, 7000);;
				vp_main.setCurrentItem(0);
				
				//vp_main.setCurrentItem(m_List.size());  
				break;
			default:
				break;
			}

		}
    	
	};
	

	public void GetEventData()
	{
		mProgress.show();

		final  AppManagement _AppManager = (AppManagement) getApplication();
		
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{
				Map<String, String> data = new HashMap  <String, String>();

					
				String strJSON = _AppManager.GetHttpManager().GetHTTPData(_AppManager.DEF_URL +  "/mweb/event/eventMainImage.gm", data);

				try 
				{
					JSONObject json = new JSONObject(strJSON);
					if(json.getString("errorCode").equals("0"))
					{

						JSONArray usageList = (JSONArray)json.get("eventList");
							
						// �˻� ������ ��´�. 
						for(int i = 0; i < usageList.length(); i++)
						{
							MainEvent item = new MainEvent();
							JSONObject list = (JSONObject)usageList.get(i);

							item.imageUrl =  _AppManager.GetHttpManager().DecodeString(list.optString("imageUrl"));
							item.eventNum =  _AppManager.GetHttpManager().DecodeString(list.optString("eventNum"));
	
							m_List.add(item);
							
						}
						handler.sendEmptyMessage(20);
					}
					else 
					{
						// ���� �޼����� �����Ѵ�. 
						handler.sendMessage(handler.obtainMessage(1,_AppManager.GetHttpManager().DecodeString(json.getString("errorMsg")) ));
						return ;
					}
				} catch (JSONException e) 
				{
					// TODO Auto-generated catch block
					handler.sendMessage(handler.obtainMessage(1,"json Data Error \n" + e.getMessage() ));
					e.printStackTrace();
				} 
			}
		});
		thread.start();
	}
	
	
	

	
    private class CustomPagerAdapter extends PagerAdapter{

        
        @Override
        public int getCount() {
                return m_List.size() ;
        }

        /**
         * �� ������ ����
         */
        @Override
        public Object instantiateItem(View collection, int position)
        {

        	

        	ImageView img = new ImageView(self); //this is a variable that stores the context of the activity
            //set properties for the image like width, height, gravity etc...
        	
        	//position %= m_List.size();

        	OriPos = position;
        	
        	
        	Log.v("Currpos",  " " +CurrPos );
        	final int pos = position;
            int resId = 0;
            switch (position) {
            case 0:
                resId = R.drawable.event_681;
                break;
            case 1:
                resId = R.drawable.event_696;
                break;
            case 2:
                resId = R.drawable.event_707;
                break;
            case 3:
                resId = R.drawable.event_711;
                break;
            }
            
            

            
            
            OnClickListener ol  = new OnClickListener()
            {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					AppManagement _AppManager = (AppManagement) getApplication();
					_AppManager.m_EventNum = Integer.parseInt(m_List.get(pos).eventNum);
	                
					Intent intent;
		            intent = new Intent().setClass(baseself, EventDetailMainActivity.class);
		            startActivity( intent );	
                	
					
				}            
            };
            img.setOnClickListener(ol);
            
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
    
    
    
    // **********************************************************************
    // Ÿ�̸� ó��
    // **********************************************************************    
    private Runnable TimerRunner = new IntroRunnAble(this);
    public class IntroRunnAble implements Runnable 
    { 
    	 
    	Object parentActivity;
    	public IntroRunnAble(Object parameter)
    	{
    		   
    	    // store parameter for later user 
    	   parentActivity = parameter;
    	} 
    	 
    	public void run() 
    	{ 
    		
    		 ;
    		 OriPos = vp_main.getCurrentItem() + 1;
    		 if (OriPos  >= cpa.getCount() )
    		 {
    			 OriPos = 0; 
    		 }
    		 vp_main.setCurrentItem(OriPos,  false);
           handler2.removeCallbacks(TimerRunner);   // ����� ����
           handler2.postDelayed(TimerRunner, 7000);;

    	} 
    } 
    

}
