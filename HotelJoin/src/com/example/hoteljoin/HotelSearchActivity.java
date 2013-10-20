package com.example.hoteljoin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.euiweonjeong.base.BitmapManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;



import com.slidingmenu.lib.SlidingMenu;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class HotelSearchActivity extends HotelJoinBaseActivity implements OnClickListener{

	public class Destination
	{
		public String name;// ���ø�
		public String code;// �з� �ڵ� 
		public Boolean World; 
	}
	HotelSearchActivity self;
	 SlidingMenu menu ;
	 int MenuSize;
	 
	 Integer day;
	 Integer month;
	 Integer year;
	 
	 Integer stayDay = 1;
	 Integer stayCount = 1;
	 Integer statMan = 1;
	 
	 Integer m_CurrentTab =0;

	 double m_lat;
	 double m_lng;
	 
	 Calendar calendar = Calendar.getInstance();
	 
	 Boolean locationcheck = false;;
	 LocationManager locationmanager;
	 private Criteria criteria;
	 
	 Boolean CurrentPosition = true;		// ���� ��ġ����?
	 Boolean UseNetwork = false;			// ���� ��Ʈ��ũ�� ��������� üũ ( ������̶�� ����� ������ ������ �����ؾ��� )
	 Boolean MoreGetData = false;			// �ѹ��� ���;��� ������ �ִ��� Ȯ���Ѵ�. 
	 
	 EditText m_SearchText = null;
	 
	 private ListView m_ListView;			// ����Ʈ��
	 
	 
	 ArrayList<Destination> CityList;		// ���� ����Ʈ
	 private Search_Adapter m_Adapter;
	 
	 
	 Destination m_CurrentCity = new Destination();	// ���� ����
	 Boolean m_bUseGPS = true;					//GPS�� �̿��ߴ��� �ƴϸ� ����Ʈ���� ���ø� ���� �߳�
	 Boolean m_ShowPopup = false;
	 DatePickerDialog.OnDateSetListener dateSetListener = 
				new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year1, int monthOfYear, int dayOfMonth) {
						calendar.set(year1, monthOfYear, dayOfMonth);
						
						year = year1;
						month =monthOfYear + 1;
						day = dayOfMonth;
						
						String CountDay = (String)((TextView)findViewById(R.id.hotel_day_number)).getText();
						((TextView)findViewById(R.id.hotel_day)).setText(year+ "/" + month + "/" + day);
						((TextView)findViewById(R.id.check_out)).setText("üũ�ƿ� : "+ getDate(Integer.parseInt(CountDay)));
					}
				};
				
	public String getDate ( int iDay )
	{
		calendar.set(year, month -1, day);
		StringBuffer sbDate=new StringBuffer ( );
		calendar.add ( Calendar.DAY_OF_MONTH, iDay );
		int nYear = calendar.get ( Calendar.YEAR );
		int nMonth = calendar.get ( Calendar.MONTH ) + 1;
		int nDay = calendar.get ( Calendar.DAY_OF_MONTH );
		sbDate.append ( nYear );
		sbDate.append ("/");
		if ( nMonth < 10 ) sbDate.append ( "0" );
		sbDate.append ( nMonth );
		sbDate.append ("/");
		if ( nDay < 10 ) sbDate.append ( "0" );
		sbDate.append ( nDay );

		return sbDate.toString ( );
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hotel_search_main);
		
		self = this;

		{
        	mProgress = new ProgressDialog(this);
    		mProgress.setMessage("��ø� ��ٷ� �ּ���.");
    		mProgress.setIndeterminate(true);
    		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		mProgress.setCancelable(false);
        }
		
		CityList = new  ArrayList<Destination>();
		
		CityList.clear();
        
		// �����ε� �� ����
		setBehindContentView(R.layout.main_menu_1);
		

		
		// �����̵� ��
		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.RIGHT);
		
		{
			Display defaultDisplay = ((WindowManager)this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
	    	
			int width;
			@SuppressWarnings("deprecation")
			int windowswidth = defaultDisplay.getWidth();
			@SuppressWarnings("deprecation")
			int windowsheight= defaultDisplay.getHeight();
			int originwidth = 480;
			

			width = (int) ( (float) 359 * ( (float)(windowswidth)/ (float)(originwidth) )  );

			
			sm.setBehindOffset(windowswidth - width );
		}
		
		sm.setFadeDegree(0.35f);
		
		BottomMenuDown(true);
		AfterCreate(1);
		
		m_ListView = (ListView)findViewById(R.id.search_list);
		
		
    	m_Adapter = new Search_Adapter(this, R.layout.search_row, CityList);
    	

        
    	m_ListView.setAdapter(m_Adapter);

		
		BtnEvent( R.id.hotel_day);
		BtnEvent( R.id.inn_day_p);
		BtnEvent( R.id.inn_day_m);;
		BtnEvent( R.id.inn_count_p);
		BtnEvent( R.id.inn_count_m);
		BtnEvent( R.id.inn_man_p);
		BtnEvent( R.id.inn_man_m);
		BtnEvent( R.id.search_btn);
		BtnEvent( R.id.hotel_locate);
		BtnEvent( R.id.page_up_1);
		BtnEvent( R.id.search_tab_1_1);
		BtnEvent( R.id.search_tab_2_1);
		BtnEvent( R.id.search_tab_3_1);
		BtnEvent( R.id.gps_now);
		
		
		
		
		
		// ���� ��¥ ����
		{
			calendar.add ( Calendar.DAY_OF_MONTH, 10 );
			year = calendar.get(Calendar.YEAR);
			month = calendar.get(Calendar.MONTH) + 1;
			day = calendar.get(Calendar.DAY_OF_MONTH);
			StringBuffer sbDate=new StringBuffer ( );
			sbDate.append ( year );
			sbDate.append ("/");
			if ( month < 10 ) sbDate.append ( "0" );
			sbDate.append ( month );
			sbDate.append ("/");
			if ( day < 10 ) sbDate.append ( "0" );
			sbDate.append ( day );
			
			String CountDay = (String)((TextView)findViewById(R.id.hotel_day_number)).getText();
			((TextView)findViewById(R.id.hotel_day)).setText(sbDate);
			((TextView)findViewById(R.id.check_out)).setText("üũ�ƿ� : "+ getDate(Integer.parseInt(CountDay)));
		}
		
		
		String context = Context.LOCATION_SERVICE;
        locationmanager=(LocationManager)getSystemService(context);
        
        if(!locationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER)) 
        {
            alertCheckGPS();
        }
        

        
        criteria=new Criteria();
		GetLocation();
		
		
		{
			
			m_SearchText = (EditText)findViewById(R.id.search_text_1); 
			//����
			TextWatcher watcher = new TextWatcher()
			{    
				@Override    
				public void afterTextChanged(Editable s)
				{         
					//�ؽ�Ʈ ���� �� �߻��� �̺�Ʈ�� �ۼ�. 
				}     
				@Override    
				public void beforeTextChanged(CharSequence s, int start, int count, int after)    
				{        
					//�ؽ�Ʈ�� ���̰� ����Ǿ��� ��� �߻��� �̺�Ʈ�� �ۼ�.   
				}   
				@Override    
				public void onTextChanged(CharSequence s, int start, int before, int count)    
				{         
					//�ؽ�Ʈ�� ����ɶ����� �߻��� �̺�Ʈ�� �ۼ�.
					if (m_SearchText.isFocusable())         
					{             
						//m_SearchText EditText �� ��Ŀ�� �Ǿ����� ��쿡�� ����˴ϴ�.  
						// �˻�� ���� �ɶ� ���� ������ �������� 
						String Text = ((EditText)findViewById(R.id.search_text_1)).getText().toString();
						
						if ( m_CurrentTab != 2 )
						{
							if ( Text.equals(""))
							{
								// �ű� ����Ʈ�� ��������,
								CitySearch();
							}
							else
							{
								// �Էµ� ����Ʈ ������.
								AutoCitySearch();
							}
						}
						else
						{
							AppManagement _AppManager = (AppManagement) getApplication();
							CityList.clear();
							
							for ( int  i = 0 ; i < _AppManager.m_HotelSearchData.size() ; i++ )
							{
								Destination item = new Destination();
								
								
								item.name = _AppManager.m_HotelSearchData.get(i).Name;
								item.code = _AppManager.m_HotelSearchData.get(i).Destination;
								item.World =  _AppManager.m_HotelSearchData.get(i).bLocal;
								CityList.add(item);
							}
							m_Adapter.notifyDataSetChanged();
						}

					}   
				}
			};
			//ȣ��
			m_SearchText.addTextChangedListener(watcher);

		}

		RefreshUI();
		
	}
	
	// �ڵ� �˻� 
	public void AutoCitySearch()
	{
		if ( UseNetwork == true)
		{
			MoreGetData = true;
			return;
		}
		CityList.clear();
		UseNetwork = true;
		final  AppManagement _AppManager = (AppManagement) getApplication();
		
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{

				EditText searchText = (EditText)findViewById(R.id.search_text_1);
				Map<String, String> data = new HashMap  <String, String>();

				if ( m_CurrentTab == 0 )
					data.put("searchTypeCode", "S1");
				else
					data.put("searchTypeCode", "S2");
				
				data.put("maxrow", "20");
				
				data.put("searchValue", searchText.getText().toString());
	
				
				String strJSON = _AppManager.GetHttpManager().GetHTTPData(_AppManager.DEF_URL +  "/mweb/search/searchDestinationList.gm", data);

				try 
				{
					JSONObject json = new JSONObject(strJSON);
					if(json.getString("errorCode").equals("0"))
					{
						JSONArray usageList = (JSONArray)json.get("destinationList");
						
						// �˻� ������ ��´�. 
						for(int i = 0; i < usageList.length(); i++)
						{
							Destination item = new Destination();
							JSONObject list = (JSONObject)usageList.get(i);
							
							item.name = _AppManager.GetHttpManager().DecodeString(list.optString("name"));
							item.code =  _AppManager.GetHttpManager().DecodeString(list.optString("code"));
							CityList.add(item);
						}
						handler.sendEmptyMessage(30);
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
	
	public void CitySearch()
	{
		if ( UseNetwork == true)
		{
			MoreGetData = true;
			return;
		}
		CityList.clear();
		UseNetwork = true;
		final  AppManagement _AppManager = (AppManagement) getApplication();
		
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{

				Map<String, String> data = new HashMap  <String, String>();

				if ( m_CurrentTab == 0 )
					data.put("searchTypeCode", "S1");
				else
					data.put("searchTypeCode", "S2");
	
				
				String strJSON = _AppManager.GetHttpManager().GetHTTPData(_AppManager.DEF_URL +  "/mweb/search/searchDestinationCityList.gm", data);

				try 
				{
					JSONObject json = new JSONObject(strJSON);
					if(json.getString("errorCode").equals("0"))
					{
						JSONArray usageList = (JSONArray)json.get("destinationList");
						
						// �˻� ������ ��´�. 
						for(int i = 0; i < usageList.length(); i++)
						{
							Destination item = new Destination();
							JSONObject list = (JSONObject)usageList.get(i);
							
							item.name = _AppManager.GetHttpManager().DecodeString(list.optString("name"));
							item.code =  _AppManager.GetHttpManager().DecodeString(list.optString("code"));
							CityList.add(item);
						}
						handler.sendEmptyMessage(30);
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
	
	
	public class Search_Adapter extends ArrayAdapter<Destination>
    {
    	private Context mContext;
		private int mResource;
		private ArrayList<Destination> mList;
		private LayoutInflater mInflater;
		
    	public Search_Adapter(Context context, int layoutResource, ArrayList<Destination> mTweetList)
		{
			super(context, layoutResource, mTweetList);
			this.mContext = context;
			this.mResource = layoutResource;
			this.mList = mTweetList;
			this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
    	
    	@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
    		final Destination mBar = mList.get(position);
    		final  AppManagement _AppManager = (AppManagement) getApplication();

			if(convertView == null)
			{
				convertView = mInflater.inflate(mResource, null);
			}

			if(mBar != null) 
			{

				((TextView)convertView.findViewById(R.id.search_row_text)).setText(mBar.name);


				
				LinearLayout detailBar1 = (LinearLayout)convertView.findViewById(R.id.search_row_1);
				
				
				detailBar1.setOnClickListener(new View.OnClickListener() 
				{

					public void onClick(View v) 
					{
						m_bUseGPS = false;
						CurrentPosition = false;
						m_CurrentCity = mBar;
						((TextView)findViewById(R.id.hotel_locate)).setText(mBar.name);
						
						ClosePopup();

					}
				});
				
				detailBar1.setOnLongClickListener(new View.OnLongClickListener() 
				{

					

					@Override
					public boolean onLongClick(View arg0) 
					{
						// TODO Auto-generated method stub
						if ( m_CurrentTab == 2 )
						{
							String []	NationList = {"��ü����", "���׸� ����"};
							
							
							AlertDialog.Builder alt_bld = new AlertDialog.Builder(self);
					        alt_bld.setTitle("������� ����");
					        alt_bld.setSingleChoiceItems(NationList, -1, new DialogInterface.OnClickListener() 
					        {
					            public void onClick(DialogInterface dialog, int item) 
					            {
					            	if ( item == 0 )
					            	{
					            		_AppManager.RemoveAllHotelSearchData();
					            		ChangeTab(2);
					            	}
					            	else
					            	{
					            		self.ShowAlertDialLog( self ,"���� �̹���� ������ �������� �ʽ��ϴ�");
					            	}
					            	dialog.cancel();
					            }
					        });
					        AlertDialog alert = alt_bld.create();
					        alert.show();
						}
						return false;
					}
				});
				
			}
			return convertView;
		}
    }
	
	
	final Handler handler = new Handler( )
	{
    	
    	
    	public void handleMessage(Message msg)
		{
    		UseNetwork = false;
			switch(msg.what)
			{
			case 0:

				break;
			case 1:


				break;
			case 2:

				break;	
			case 3:
				break;
			case 20:
				break;
			case 30:
			{
				m_Adapter.notifyDataSetChanged();
				
				// ���� �����͸� �޾ƾ� �Ұ� ������ ��� �ѹ��� �޴´�. 
				if ( MoreGetData == true)
				{
					MoreGetData =false;
					String Text = ((EditText)findViewById(R.id.search_text_1)).getText().toString();
					if ( m_CurrentTab != 2 )
					{
						if ( Text.equals(""))
						{
							// �ű� ����Ʈ�� ��������,
							CitySearch();
						}
						else
						{
							// �Էµ� ����Ʈ ������.
							AutoCitySearch();
						}
					}
					else
					{
						
					}
				}
				
			}
				break;
			default:
				break;
			}

		}
    	
	};
	
	
	public void ChangeTab( int index )
	{
		AppManagement _AppManager = (AppManagement) getApplication();
		// ���� ��ȯ �Ǹ� �˻�� Ŭ���� ���ش�. 
		m_CurrentTab = index;
		 ((ImageView)findViewById(R.id.search_tab_1_1)).setBackgroundResource(R.drawable.tap1_1);
		 ((ImageView)findViewById(R.id.search_tab_2_1)).setBackgroundResource(R.drawable.tap2_1);
		 ((ImageView)findViewById(R.id.search_tab_3_1)).setBackgroundResource(R.drawable.tap3_1);
		switch( index )
		{
		case 0 :
			((ImageView)findViewById(R.id.search_tab_1_1)).setBackgroundResource(R.drawable.tap1_2);
			_AppManager.m_SearchWorld = false;
			break;
		case 1:
			((ImageView)findViewById(R.id.search_tab_2_1)).setBackgroundResource(R.drawable.tap2_2);
			_AppManager.m_SearchWorld = true;
			break;
		case 2:
			((ImageView)findViewById(R.id.search_tab_3_1)).setBackgroundResource(R.drawable.tap3_2);
			CityList.clear();
			m_Adapter.notifyDataSetChanged();
			break; 
		}
		((EditText)findViewById(R.id.search_text_1)).setText("");
		
	}
	
	
    //-- UI ����
	public void RefreshUI()
	{
		((TextView)findViewById(R.id.hotel_day_number)).setText(stayDay.toString());
		((TextView)findViewById(R.id.hotel_room)).setText(stayCount.toString());
		((TextView)findViewById(R.id.hotel_room_man)).setText(statMan.toString());
		
	}
	
	public void RefreshUI1()
	{

		
	}
	public void BtnEvent( int id )
    {
		View imageview = (View)findViewById(id);
		imageview.setOnClickListener(this);
    }

	public void ShowPopup()
	{
		 ((View)findViewById(R.id.main_layout_2)).setVisibility(View.VISIBLE);
		 m_CurrentTab = 0 ;
		 ChangeTab(m_CurrentTab );
	}
	public void ClosePopup()
	{
		 ((View)findViewById(R.id.main_layout_2)).setVisibility(View.GONE);
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
		case R.id.hotel_day:
			showDialog(0);
			break;
		case R.id.inn_day_p:
			stayDay++ ;
			{
				((TextView)findViewById(R.id.hotel_day)).setText(year+ "/" + month + "/" + day);
				((TextView)findViewById(R.id.check_out)).setText("üũ�ƿ� : "+ getDate(stayDay));
			}
			break;
		case R.id.inn_day_m:
			stayDay-- ;
			if (stayDay < 1 )
				stayDay = 1;
			{
				((TextView)findViewById(R.id.hotel_day)).setText(year+ "/" + month + "/" + day);
				((TextView)findViewById(R.id.check_out)).setText("üũ�ƿ� : "+ getDate(stayDay));
			}

			break;
			
		case R.id.inn_count_p:
			stayCount++ ;
			break;
		case R.id.inn_count_m:
			stayCount-- ;
			if (stayCount < 1 )
				stayCount = 1;
			break;
		case R.id.inn_man_p:
			statMan++ ;
			break;
		case R.id.inn_man_m:
			statMan-- ;
			if (statMan < 1 )
				statMan = 1;
			break;
		case R.id.search_btn:
			
			{
				if ( CurrentPosition == false )
				{
					AppManagement _AppManager = (AppManagement) getApplication();
					String str =m_CurrentCity.code; 
					_AppManager.m_DestinationCode ="";
					_AppManager.m_SearchCode = "S1"; 
		
					/*StringTokenizer tokens = new StringTokenizer( str, "|" );
					for( int x = 1; tokens.hasMoreElements(); x++ )
					{ 
					  
						// ���ѷ��� ���°� ���Ƽ� �ϴ� ���� ���������� �ڵ�
						if ( x > 10 )
							break;
						if ( x == 3) 
						{
							tokens.nextToken(); 
						}
						else if (x <= 4 )
							_AppManager.m_DestinationCode +=  tokens.nextToken(); 

						if (x < 3 )
						{
							_AppManager.m_DestinationCode += "|";
						}
					} */
					_AppManager.m_DestinationCode = str;
					Log.v("Destination Code", _AppManager.m_DestinationCode);
					_AppManager.m_CheckInDay = year.toString();
					if ( month < 10 )
						_AppManager.m_CheckInDay += "0";
					_AppManager.m_CheckInDay += month.toString();
					
					if ( day < 10 )
						_AppManager.m_CheckInDay += "0";
					_AppManager.m_CheckInDay += day.toString();
					
					_AppManager.m_NumRoom = self.stayCount.toString();
					_AppManager.m_Duration = stayDay.toString();
					_AppManager.m_NumPer = statMan.toString();
					
					HotelSearchData item = new HotelSearchData();
					item.bLocal = _AppManager.m_SearchWorld;
					item.Destination = m_CurrentCity.code;
					item.Name = m_CurrentCity.name;
					
					_AppManager.AddHotelSearchData(item);
					
					Intent intent;
		            intent = new Intent().setClass(baseself, HotelSearchListActivity.class);
		            startActivity( intent );
				}
				else
				{
					AppManagement _AppManager = (AppManagement) getApplication();
					_AppManager.m_SearchCode = "S3"; 
					_AppManager.m_CheckInDay = year.toString();
					if ( month < 10 )
						_AppManager.m_CheckInDay += "0";
					_AppManager.m_CheckInDay += month.toString();
					
					if ( day < 10 )
						_AppManager.m_CheckInDay += "0";
					_AppManager.m_CheckInDay += day.toString();
					_AppManager.m_NumRoom = self.stayCount.toString();
					_AppManager.m_Duration = stayDay.toString();
					_AppManager.m_NumPer = statMan.toString();
					
					if ( m_lng > 120 && m_lng < 140 && m_lat > 30 && m_lat < 45 )
					{
						 _AppManager.m_SearchWorld = false; 
							Intent intent;
				            intent = new Intent().setClass(baseself, HotelSearchListActivity.class);
				            startActivity( intent );
					}
						
					else
					{
						_AppManager.m_SearchWorld = true; 
						Intent intent;
			            intent = new Intent().setClass(baseself, HotelSearchListActivity.class);
			            startActivity( intent );
					}
					
				}




			}
			break;
		case R.id.hotel_locate:
			ShowPopup();
			break;
		case R.id.page_up_1:
			ClosePopup();
			break;
		case R.id.search_tab_1_1:
		{
			if ( m_CurrentTab != 0 )
				ChangeTab(0);
				
		}
			break;
		case R.id.search_tab_2_1:
		{
			if ( m_CurrentTab != 1 )
				ChangeTab(1);
		}
			break;
		case R.id.search_tab_3_1:
		{
			if ( m_CurrentTab != 2 )
				ChangeTab(2);
		}
			break;
		case R.id.gps_now:
		{
			m_bUseGPS = true;
			GetLocation();
			CurrentPosition = true;
			((TextView)findViewById(R.id.hotel_locate)).setText("���� ��ġ");
		}
			
			break;
					
		}
		
		RefreshUI();
		
	}
	

	@Override    
	protected Dialog onCreateDialog(int id)
	{       
		return new DatePickerDialog(this, dateSetListener, calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));  
	}
	
	
	
	//-- GPS ����
	private void alertCheckGPS() 
	{
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("GPS�� �����ֽ��ϴ�. GPS�� �������� ��� ��ġ Ž���� ���� �ʽ��ϴ�. \n GPS�� �ѽðڽ��ϱ�?")
                .setCancelable(false)
                .setPositiveButton("��",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                moveConfigGPS();
                            }
                    })
                .setNegativeButton("�ƴϿ�",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                    });
        AlertDialog alert = builder.create();
        alert.show();
    }

   // GPS ����ȭ������ �̵�
    private void moveConfigGPS() {
        Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(gpsOptionsIntent);
    }
    
    
	
	public void GetLocation()
    {
    	locationcheck = false;
    	
    	criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
        
        String provider = locationmanager.getBestProvider(criteria, true);

        if ( provider == null )
        {
        	Location location ;
        	ConnectivityManager manager = (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);

            boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
            
            if ( isWifi )
            {
            	criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            	provider = locationmanager.getBestProvider(criteria, true);
            	location = locationmanager.getLastKnownLocation(provider);
            	updateWithNewLocation(location);
            	//locationmanager.requestLocationUpdates(provider, 1000, 10, locationListener);
            	//Toast.makeText(getApplicationContext(),"gps�̿�Ұ�, ��Ʈ��ũ�� ����",Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Location location = locationmanager.getLastKnownLocation(provider);


            ConnectivityManager manager = (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);

            boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();

            // GPS 
            if(!locationmanager.isProviderEnabled(provider)&&locationmanager.getLastKnownLocation(provider)!=null)
            {
            	updateWithNewLocation(location);
            	//locationmanager.requestLocationUpdates(provider, 1000, 10, locationListener);     
            	//Toast.makeText(getApplicationContext(),"gps �̿밡��",Toast.LENGTH_SHORT).show();
               
            } 
            // GPS ���� ���� �����°� �ʾ���, Wifi�� ����Ǿ� ���� ��� 
            else if ( isWifi )
            {
            	criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            	provider = locationmanager.getBestProvider(criteria, true);
            	location = locationmanager.getLastKnownLocation(provider);
            	updateWithNewLocation(location);
            	//locationmanager.requestLocationUpdates(provider, 1000, 10, locationListener);
            	//Toast.makeText(getApplicationContext(),"gps�̿�Ұ�, ��Ʈ��ũ�� ����",Toast.LENGTH_SHORT).show();
            }
            // GPS�� �ȵǰ� Wifi�� �� �ɰ�� ��ġ ���� ���� ������. 
            else
            {
            	//Toast.makeText(getApplicationContext(),"gps�̿�Ұ� Wifi �̿� �Ұ� ",Toast.LENGTH_SHORT).show();
            }
        }
 


    }

	private void updateWithNewLocation(Location location) {

		AppManagement _AppManager = (AppManagement) getApplication();
        String latlng = "";
        if(location!=null && locationcheck == false)

        {
        	
        	locationcheck = true;
        	m_lat = location.getLatitude();
            m_lng = location.getLongitude();
            latlng = "���� : "+m_lat+" \n�浵 : "+m_lng;
            _AppManager.m_MyLat = Double.toString(m_lat);
            _AppManager.m_MyLng = Double.toString(m_lng);
                

        }
        else if(location!=null && locationcheck == true)
        {
        	m_lat = location.getLatitude();
            m_lng = location.getLongitude();
            latlng = "���� : "+m_lat+" \n�浵 : "+m_lng;
            _AppManager.m_MyLat = Double.toString(m_lat);
            _AppManager.m_MyLng = Double.toString(m_lng);
        }

        else
        {
        	m_lat =37.5666091;
        	m_lng = 126.978371;
        	latlng="��ġ�� ã���� �����ϴ�. �⺻ ��ġ�� �����մϴ�. ";
        	_AppManager.m_MyLat = "-1";
            _AppManager.m_MyLng = "-1";
        	self.ShowAlertDialLog( self ,"����" , latlng);
            
        }


	}

}
