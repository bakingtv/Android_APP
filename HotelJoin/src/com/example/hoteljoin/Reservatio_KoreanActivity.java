package com.example.hoteljoin;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.euiweonjeong.base.BitmapManager;
import com.example.hoteljoin.HotelSearchListActivity.SearchList_Adapter;
import com.example.hoteljoin.Reservatio_WorldActivity.Main_Adapter;
import com.example.hoteljoin.Reservatio_WorldActivity.ReservationWorldData;
import com.slidingmenu.lib.SlidingMenu;

import android.net.Uri;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Reservatio_KoreanActivity extends HotelJoinBaseActivity implements OnClickListener{
	Reservatio_KoreanActivity  self;
	 SlidingMenu menu ;
	 int MenuSize;
	 


	 public class ReservationWorldData
		{
			ReservationWorldData()
			{
						
			}
			
			Integer  type;	// Layout Type
			
			/// Layout 1
			String 	roomName = "";
			String roomcount = "";
			String roomman = "";
			String roomtype= "";		// Req Option
			String roomreop= "";
			String roomseop="";
			String price="";
			String day="";
			
			/// Layout 2 ( Login )
			
			/// Layout 3 (������ ���� )
			String 	name1="";	// �̸�
			String  tel1="";
			String  tel2="";
			String 	mail="";
			String  story = "";		// ���޻���
			
			String name2 ="";	// ������ ����
			/// Layout 7 ( üũ �ڽ�  )
			Boolean checkbox1 = false;
			 Boolean checkbox2 = false;
			 Boolean checkbox3 = false;
			
		}
		 

		Integer day;
		Integer month;
		Integer year;
		Calendar calendar = Calendar.getInstance();
		ArrayList< ReservationWorldData > m_ListData;
		private Main_Adapter m_Adapter;
					

		private ListView m_ListView;	 
		



		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.rese_world_main);
			
			

			// �����ε� �� ����
			setBehindContentView(R.layout.main_menu_1);
			
			self = this;
			

		    // ���α׷��� ���̾�α� 
	        {
	        	mProgress = new ProgressDialog(this);
	    		mProgress.setMessage("��ø� ��ٷ� �ּ���.");
	    		mProgress.setIndeterminate(true);
	    		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	    		mProgress.setCancelable(false);
	        }
	        
			
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
			
			
			{
	        	
		     	m_ListView = ((ListView)findViewById(R.id.main_list));
		        	
		        	
		        	
		    }
		        
		    m_ListData = new ArrayList< ReservationWorldData >();
		    m_ListData.clear();
		    m_Adapter = new Main_Adapter(this, R.layout.reser_3_row, m_ListData);

		    m_ListView.setAdapter(m_Adapter);
			m_ListView.setDivider(null);
			m_Adapter.notifyDataSetChanged();
			
			
			AfterCreate(7);
			
			
			GetInputData();
			RefreshUI();
			
			

		}
		
		
		
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
		
		
		public void RefreshUI()
		{
			m_ListData.clear();
			{
				AppManagement _AppManager = (AppManagement) getApplication();
				// Layout 1 
				{
					ReservationWorldData item  = new ReservationWorldData();
					
					item.type = 0;
					item.roomName = _AppManager.m_RoomDetailData.roomName;
					item.roomcount = _AppManager.m_NumRoom;
					item.roomman = _AppManager.m_NumPer;
					
					item.roomtype = ""; 
					
					for ( int i = 0 ; i < _AppManager.m_RoomOptionData.size() ; i++ )
					{
						if( _AppManager.m_RoomOptionData.get(i).optionMethodType.equals("Y"))
						{
							item.roomtype += _AppManager.m_RoomOptionData.get(i).optionName;
							item.roomtype += "  ";
						}
					}
					item.roomreop = ""; 
					for ( int i = 0 ; i < _AppManager.m_RoomOptionData.size() ; i++ )
					{
						if( _AppManager.m_RoomOptionData.get(i).optionMethodType.equals("S"))
						{
							item.roomreop  += _AppManager.m_RoomOptionData.get(i).optionName;
							item.roomreop  += "  ";
						}
					}

					item.roomseop = ""; 
					
					for ( int i = 0 ; i < _AppManager.m_RoomOptionData.size() ; i++ )
					{
						if( _AppManager.m_RoomOptionData.get(i).optionMethodType.equals("N"))
						{
							item.roomseop += _AppManager.m_RoomOptionData.get(i).optionName;
							item.roomseop += "  ";
						}
					}
					
					{
						DecimalFormat df = new DecimalFormat("#,##0");

						item.price = "�� " + df.format(Double.parseDouble(_AppManager.m_TotalPrice)) ;
					}
					
					{
						
						DateFormat sdFormat = new SimpleDateFormat("yyyyMMdd");
						 Date tempDate = null;
						try {
							tempDate = sdFormat.parse(_AppManager.m_CheckInDay);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						 
						calendar.setTime(tempDate);
						
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
						
						
						
						item.day = sbDate + "~" + getDate(Integer.parseInt(_AppManager.m_Duration)) ;

						
					}
					m_ListData.add(item);
				}
				
				// Layout 2 
				if ( _AppManager.m_LoginCheck == false )
				{
					ReservationWorldData item  = new ReservationWorldData();
					
					item.type = 1;
					m_ListData.add(item);
				}
				
				// Layout 3
				{
					ReservationWorldData item  = new ReservationWorldData();
					
					item.type = 2;
					m_ListData.add(item);
				}
			}
			
			m_Adapter.notifyDataSetChanged();
		}
		

		@Override
		public void onResume()
		{
			super.onResume();
			AppManagement _AppManager = (AppManagement) getApplication();
			if (_AppManager.m_RefreshUI == true )
			{
				RefreshUI();
				_AppManager.m_RefreshUI  = false;
			}
			
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
		
		
		public void Next()
		{
			AppManagement _AppManager = (AppManagement) getApplication();
			// ������ Ȯ�� 
			for ( int i = 0  ; i < m_ListData.size() ; i++ )
			{
				if (m_ListData.get(i).type  ==  2 )
				{

					if (    m_ListData.get(i).tel2.equals("") ||
							m_ListData.get(i).name1.equals("") ||
							m_ListData.get(i).mail.equals("") )
					{
						self.ShowAlertDialLog( self ,"����" , "������ ������ �Է����ּ���");
						return;
					}
					
					if ( m_ListData.get(i).name2.equals("")   )
					{
						self.ShowAlertDialLog( self ,"����" , "������ ������ �Է����ּ���");
						return;
					}
					
					
					
					else if ( m_ListData.get(i).checkbox2== false )
					{
						self.ShowAlertDialLog( self ,"����" , "����� �������ּ���");
						return;
					}
					
					else if ( m_ListData.get(i).checkbox3== false )
					{
						self.ShowAlertDialLog( self ,"����" , "����� �������ּ���");
						return;
					}
				}


			}
			
			_AppManager.m_RoomGuestData.clear();
			
			// ������ �Է� 
			for ( int i = 0  ; i < m_ListData.size() ; i++ )
			{
				// ������ ���� �Է� 
				if ( m_ListData.get(i).type  ==  2  )
				{
					_AppManager.m_resvName = m_ListData.get(i).name1;
					_AppManager.m_resvEmail = m_ListData.get(i).mail;
					_AppManager.m_resvTel = m_ListData.get(i).tel1;
					_AppManager.m_resvMobile = m_ListData.get(i).tel2;
					_AppManager.m_resvPasswd = "";
					_AppManager.m_resvIyagi =  m_ListData.get(i).story;
					RoomGuestList item = new RoomGuestList();
					
					item.guestFirstName = m_ListData.get(i).name1;
					_AppManager.m_RoomGuestData.add(item);
				}

			}
			
			Intent intent;
	        intent = new Intent().setClass(baseself, ReservationConfirmActivity.class);
	        startActivity( intent ); 
			
			
			
		}
		

		public void GetInputData()
		{

			mProgress.show();
			final  AppManagement _AppManager = (AppManagement) getApplication();
			
			Thread thread = new Thread(new Runnable()
			{

				public void run() 
				{

					Map<String, String> data = new HashMap  <String, String>();

					data.put("hotelCode", _AppManager.m_HotelCode);
					data.put("supplyCode", "700");
					
					data.put("checkinDay", _AppManager.m_CheckInDay);
					data.put("duration", _AppManager.m_Duration);
					data.put("roomCount1", _AppManager.m_NumRoom);
					data.put("numAdults1", _AppManager.m_NumPer);
					
					data.put("numChild", _AppManager.m_NumChild);
					
					data.put("roomCode", _AppManager.m_RoomDetailData.roomCode);
					
					
					for ( Integer i = 0 ; i < _AppManager.m_RoomOptionData.size(); i++ )
					{
						data.put("optionCode"+"["+ i+"]" ,_AppManager.m_RoomOptionData.get(i).optionCode);
						data.put("optionName"+"["+ i+"]" ,_AppManager.m_RoomOptionData.get(i).optionName);
						data.put("optionSendPrice"+"["+ i+"]" ,_AppManager.m_RoomOptionData.get(i).optionSendPrice);
						data.put("optionPrice"+"["+ i+"]" ,_AppManager.m_RoomOptionData.get(i).optionPrice);
						data.put("optionPriceType"+"["+ i+"]" ,_AppManager.m_RoomOptionData.get(i).optionPriceType);
						data.put("optionMethodType"+"["+ i+"]" ,_AppManager.m_RoomOptionData.get(i).optionMethodType);
						
					}
					

					String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData(_AppManager.DEF_URL +  "/mweb/booking/bookingFormDom.gm", data);

					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.getString("errorCode").equals("0"))
						{
							
							
							_AppManager.m_RoomReserData.roomName = 	json.getString("roomName");
							_AppManager.m_RoomReserData.roomCode = 	json.getString("roomCode");
							_AppManager.m_RoomReserData.roomPrice = 	json.getString("roomPrice");
							_AppManager.m_RoomReserData.totalPrice = 	json.getString("totalPrice");
							_AppManager.m_RoomReserData.numRooms = 	json.getString("numRooms");
							_AppManager.m_RoomReserData.lguCstMid = 	json.getString("lguCstMid");
							_AppManager.m_RoomReserData.nonRefundableYn = 	json.getString("nonRefundableYn");
							//_AppManager.m_RoomReserData.priceFormula = 	json.getString("priceFormula");
							//_AppManager.m_RoomReserData.currencyCode = 	json.getString("currencyCode");
							if( _AppManager.m_SearchWorld == true )
							{
								_AppManager.m_RoomReserData.nativeRoomPrice = 	json.getString("nativeRoomPrice");
								_AppManager.m_RoomReserData.nativeCurrencyCode = 	json.getString("nativeCurrencyCode");
								_AppManager.m_RoomReserData.promoYn = 	json.getString("promoYn");
								_AppManager.m_RoomReserData.promoCode = 	json.getString("promoCode");
								_AppManager.m_RoomReserData.promoDesc = 	json.getString("promoDesc");
								_AppManager.m_RoomReserData.breakfastYn = 	json.getString("breakfastYn");
								_AppManager.m_RoomReserData.bookRequestKey = 	json.getString("bookRequestKey");
								_AppManager.m_RoomReserData.nonResvInfoUpdateYn = 	json.getString("nonResvInfoUpdateYn");
							}



							
							{
								_AppManager.m_RoomCancel.clear();
								JSONArray usageList = (JSONArray)json.get("chargeConditions");
								for(int i = 0; i < usageList.length(); i++)
								{
									CancelPolicyList item = new CancelPolicyList();
									JSONObject list = (JSONObject)usageList.get(i);
									
									if ( list.optString("chargeYn").equals("1") )
									{
										item.cancelDay = list.optString("fromDate");
										item.cancelPrice = list.optString("currencyCode");
										item.cancelPrice = list.optString("chargeAmount");
										_AppManager.m_RoomCancel.add(item);
									}
									
								}
							}
							
							
				
							

							handler.sendEmptyMessage(0);
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
		
		
		final Handler handler = new Handler( )
		{
	    	
	    	
	    	public void handleMessage(Message msg)
			{
				mProgress.dismiss();
				switch(msg.what)
				{
				case 0:
				{
					RefreshUI();

				}
					break;
					
				case 1:
					// ����ó�� 
					self.ShowAlertDialLog( self ,"����" , (String) msg.obj );
					break;
		
				case 20:
				{
					

					
			    	
				}
					break;
				default:
					break;
				}

			}
	    	
		};	
		

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId())
			{


			}
			
		}
		
		public class Main_Adapter extends ArrayAdapter<ReservationWorldData>
	    {
	    	private Context mContext;
			private int mResource;
			private ArrayList<ReservationWorldData> mList;
			private LayoutInflater mInflater;
			
	    	public Main_Adapter(Context context, int layoutResource, ArrayList<ReservationWorldData> mTweetList)
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
	    		final ReservationWorldData mBar = mList.get(position);
	    		final  AppManagement _AppManager = (AppManagement) getApplication();

				if(convertView == null)
				{
					convertView = mInflater.inflate(mResource, null);
				}

				if(mBar != null) 
				{


					FrameLayout detailBar1 = (FrameLayout)convertView.findViewById(R.id.main_row_1);
					LinearLayout detailBar2 = (LinearLayout)convertView.findViewById(R.id.main_row_2);
					FrameLayout detailBar3 = (FrameLayout)convertView.findViewById(R.id.main_row_3);

					switch( mBar.type)
					{
					case 0:
					{
						detailBar1.setVisibility(View.VISIBLE);
						detailBar2.setVisibility(View.GONE);
						detailBar3.setVisibility(View.GONE);

						
						String temp  =  "���Ǹ� : " + mBar.roomName + "\n";
						temp  +=  "������ : " + mBar.day + "\n";
						 temp  +=  "���Ǽ� : " + mBar.roomcount + "\n";
						 
							
							if ( _AppManager.m_NumChild.equals("0") )
							{
								temp  +=  "���Ǵ� �ο� : " +  _AppManager.m_NumPer + "[����]" + "\n";
							}
							else
							{
								temp  += "���Ǵ� �ο� : " +  _AppManager.m_NumPer + "/" +_AppManager.m_NumChild + "[����/���]" + "\n";
								
							}
		
						if ( !mBar.roomreop.equals(""))
							temp  +=  "�ʼ��ɼ� : " + mBar.roomreop + "\n";
						if ( !mBar.roomtype.equals(""))
							temp  +=  "����Ÿ�� : " + mBar.roomtype + "\n";
						if ( !mBar.roomseop.equals(""))
							temp  +=  "���ÿɼ� : " + mBar.roomseop + "\n";
						 

						((TextView)convertView.findViewById(R.id.roomname)).setText(temp);
						((TextView)convertView.findViewById(R.id.main_row_1_price)).setText(mBar.price);
					}
						break;
					case 1:
					{
						detailBar1.setVisibility(View.GONE);
						detailBar2.setVisibility(View.VISIBLE);
						detailBar3.setVisibility(View.GONE);

						
						((ImageView)convertView.findViewById(R.id.main_row_2_pic)).setOnClickListener(new View.OnClickListener() 
						{

							public void onClick(View v) 
							{
								
								Intent intent;
					            intent = new Intent().setClass(baseself, LoginActivity.class);
					            startActivity( intent );
								 

							}
						});
						
					}
						break;
					case 2:
					{
						detailBar1.setVisibility(View.GONE);
						detailBar2.setVisibility(View.GONE);
						detailBar3.setVisibility(View.VISIBLE);

						
						if ( _AppManager.m_LoginCheck == true)
						{
							//((EditText)convertView.findViewById(R.id.name1)).setText(mBar.price);
							((EditText)convertView.findViewById(R.id.tel2)).setText(_AppManager.m_Mobile);
							((EditText)convertView.findViewById(R.id.mail)).setText(_AppManager.m_Email);
							mBar.tel2 = _AppManager.m_Mobile;
							mBar.mail = _AppManager.m_Email;
						}
						
						{
							final EditText  text2 = (EditText)convertView.findViewById(R.id.name1); 
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
									if (text2.isFocusable())         
									{             
										//m_SearchText EditText �� ��Ŀ�� �Ǿ����� ��쿡�� ����˴ϴ�.  
										// �˻�� ���� �ɶ� ���� ������ �������� 
										String Text = text2.getText().toString();
										mBar.name1 = Text;
										
									}   
								}
							};
							//ȣ��
							text2.addTextChangedListener(watcher);
						}

						
						{
							final EditText  text2 = (EditText)convertView.findViewById(R.id.tel1); 
							//����
							TextWatcher watcher = new TextWatcher()
							{    
								@Override    
								public void afterTextChanged(Editable s)	{  }     
								@Override    
								public void beforeTextChanged(CharSequence s, int start, int count, int after)  { }   
								@Override    
								public void onTextChanged(CharSequence s, int start, int before, int count)    
								{         
									if (text2.isFocusable())         
									{             
										String Text = text2.getText().toString();
										mBar.tel1 = Text;
									}   
								}
							};
							//ȣ��
							text2.addTextChangedListener(watcher);
						}
						
						{
							final EditText  text2 = (EditText)convertView.findViewById(R.id.tel2); 
							//����
							TextWatcher watcher = new TextWatcher()
							{    
								@Override    
								public void afterTextChanged(Editable s)	{  }     
								@Override    
								public void beforeTextChanged(CharSequence s, int start, int count, int after)  { }   
								@Override    
								public void onTextChanged(CharSequence s, int start, int before, int count)    
								{         
									if (text2.isFocusable())         
									{             
										String Text = text2.getText().toString();
										mBar.tel2 = Text;
									}   
								}
							};
							//ȣ��
							text2.addTextChangedListener(watcher);
						}
						
						{
							final EditText  text2 = (EditText)convertView.findViewById(R.id.mail); 
							//����
							TextWatcher watcher = new TextWatcher()
							{    
								@Override    
								public void afterTextChanged(Editable s)	{  }     
								@Override    
								public void beforeTextChanged(CharSequence s, int start, int count, int after)  { }   
								@Override    
								public void onTextChanged(CharSequence s, int start, int before, int count)    
								{         
									if (text2.isFocusable())         
									{             
										String Text = text2.getText().toString();
										mBar.mail = Text;
									}   
								}
							};
							//ȣ��
							text2.addTextChangedListener(watcher);
						}
						
						{
							final EditText  text2 = (EditText)convertView.findViewById(R.id.write_text); 
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
									if (text2.isFocusable())         
									{             
										//m_SearchText EditText �� ��Ŀ�� �Ǿ����� ��쿡�� ����˴ϴ�.  
										// �˻�� ���� �ɶ� ���� ������ �������� 
										String Text = text2.getText().toString();
										mBar.story = Text;
									}   
								}
							};
							//ȣ��
							text2.addTextChangedListener(watcher);
						}
						
						
						// ������
						{
							final EditText  text2 = (EditText)convertView.findViewById(R.id.name1_6); 
							//����
							TextWatcher watcher = new TextWatcher()
							{    
								@Override    
								public void afterTextChanged(Editable s)	{  }     
								@Override    
								public void beforeTextChanged(CharSequence s, int start, int count, int after)  { }   
								@Override    
								public void onTextChanged(CharSequence s, int start, int before, int count)    
								{         
									if (text2.isFocusable())         
									{             
										String Text = text2.getText().toString();
										mBar.name2 = Text;
									}   
								}
							};
							//ȣ��
							text2.addTextChangedListener(watcher);
							
						}
						
						
						final View convertView2 =  convertView ;
						View.OnClickListener lisetn = new View.OnClickListener()
						{

							public void onClick(View v) 
							{
								
								switch( v.getId())
								{
								case R.id.detail_btn_2:
								{
									{
										/*_AppManager.m_URL = "http://m.hoteljoin.com/mweb/noMemberPolicy.html";
										_AppManager.m_URLTitle = "��� ���� ";
										Intent intent;
							            intent = new Intent().setClass(baseself, WebActivity.class);
							            startActivity( intent );*/
										
										String temp = "";
										for ( int i = 0 ; i < _AppManager.m_RoomCancel.size() ; i++ )
										{
											temp += _AppManager.m_RoomCancel.get(i).cancelDay;
											temp += " ";
											temp +=  _AppManager.m_RoomCancel.get(i).cancelPrice;
											temp += "%�� ������� �߻�";
											temp += "\n";
										}
										self.ShowAlertDialLog( self ,"��� ����" , temp );
										
									}
								}
									break;
								case R.id.detail_btn_1:
								{
									{
										_AppManager.m_URL = "http://m.hoteljoin.com/mweb/noMemberPolicy.html";
										_AppManager.m_URLTitle = "�������";
										Intent intent;
							            intent = new Intent().setClass(baseself, WebActivity.class);
							            startActivity( intent ); 
									}
								}
									break;
								case R.id.detail_btn_3:
								{
									{
										_AppManager.m_URL = "http://m.hoteljoin.com/mweb/noMemberPolicy.html";
										_AppManager.m_URLTitle = "�������� ��޹�ħ";
										Intent intent;
							            intent = new Intent().setClass(baseself, WebActivity.class);
							            startActivity( intent ); 
									}
								}
									break;
								case R.id.checkbox_3:
								{
									mBar.checkbox3 = !mBar.checkbox3;
									if ( mBar.checkbox3 == true )
									{
										
										((ImageView)convertView2.findViewById(R.id.checkbox_3)).setBackgroundResource(R.drawable.checkbox_2);
									}
									else
									{
										
										((ImageView)convertView2.findViewById(R.id.checkbox_3)).setBackgroundResource(R.drawable.checkbox_1);
									}
								}
									break;
								case R.id.checkbox_2:
								{
									mBar.checkbox2 = !mBar.checkbox2;
									if ( mBar.checkbox2 == true )
									{
										
										((ImageView)convertView2.findViewById(R.id.checkbox_2)).setBackgroundResource(R.drawable.checkbox_2);
									}
									else
									{
										
										((ImageView)convertView2.findViewById(R.id.checkbox_2)).setBackgroundResource(R.drawable.checkbox_1);
									}
								}
									break;
								case R.id.checkbox_1:
								{
									mBar.checkbox1 = !mBar.checkbox1;
									if ( mBar.checkbox1 == true )
									{
										((ImageView)convertView2.findViewById(R.id.checkbox_1)).setBackgroundResource(R.drawable.checkbox_2);
										
										 String text2 = ((EditText)convertView2.findViewById(R.id.name1)).getText().toString(); 
										
										((EditText)convertView2.findViewById(R.id.name1_6)).setText(text2);
									}
									else
									{

										((ImageView)convertView2.findViewById(R.id.checkbox_1)).setBackgroundResource(R.drawable.checkbox_1);
									}
									
									
								}
									break;	
								case R.id.main_row_7_pic:
								{
									// ���� ��������. 
									self.Next();
								}
									break;
								}
								 

							}
						};
						
						((ImageView)convertView.findViewById(R.id.checkbox_1)).setOnClickListener( lisetn );
						((ImageView)convertView.findViewById(R.id.checkbox_2)).setOnClickListener( lisetn );
						((ImageView)convertView.findViewById(R.id.checkbox_3)).setOnClickListener( lisetn );
						((ImageView)convertView.findViewById(R.id.detail_btn_2)).setOnClickListener( lisetn );
						((ImageView)convertView.findViewById(R.id.detail_btn_3)).setOnClickListener( lisetn );
						((ImageView)convertView.findViewById(R.id.main_row_7_pic)).setOnClickListener( lisetn );
						
						
					}
						break;

					}

					

					
				}
				return convertView;
			}
	    }

    

}
