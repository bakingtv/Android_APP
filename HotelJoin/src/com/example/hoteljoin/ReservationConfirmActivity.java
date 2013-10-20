package com.example.hoteljoin;

import java.text.DateFormat;
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

public class ReservationConfirmActivity extends HotelJoinBaseActivity implements OnClickListener{
	ReservationConfirmActivity  self;
	 SlidingMenu menu ;
	 int MenuSize;
	 


	public Boolean m_bFooter = true;
	
	private boolean mLockListView; 
	
	private LayoutInflater mInflater;
	
	private ListView m_ListView;


	Integer day;
	 Integer month;
	 Integer year;
	 Calendar calendar = Calendar.getInstance();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reservation_pay);
		
		

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
		
		
		
		RefreshUI();
		
		AfterCreate(7);
		BtnEvent(R.id.reser_pay);
	


	}
	

	@Override
	public void onResume()
	{
		super.onResume();
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
	
	
	
	public void GetExecuteData()
	{

		mLockListView = true;
		final  AppManagement _AppManager = (AppManagement) getApplication();
		
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{

				Map<String, String> data = new HashMap  <String, String>();

				data.put("supplyCode", "700");
				data.put("checkIn", _AppManager.m_CheckInDay);
				data.put("duration", _AppManager.m_Duration);
				if (_AppManager.m_LoginCheck == true)
					data.put("memberId", _AppManager.m_LoginID);
				
				data.put("countryCode", _AppManager.m_NationCode);
				data.put("cityCode", _AppManager.m_CityCode);
				data.put("hotelCode", _AppManager.m_HotelCode);
				data.put("hotelName", _AppManager.m_HotelName);
				data.put("roomCode", _AppManager.m_RoomDetailData.roomCode);
				data.put("roomName", _AppManager.m_RoomDetailData.roomName);
				data.put("adultCount", _AppManager.m_NumPer);	
				data.put("childCount", _AppManager.m_NumChild);	
				data.put("roomCount", _AppManager.m_NumRoom);
				data.put("lodgeName", _AppManager.m_lodgeName);
				data.put("resvName", _AppManager.m_resvName);
				data.put("resvEmail", _AppManager.m_resvEmail);
				data.put("resvTel", _AppManager.m_resvTel);
				data.put("resvMobile", _AppManager.m_resvMobile);
				data.put("resvPasswd", "");
				

				
				data.put("breakfastYn", _AppManager.m_RoomDetailData.breakfastYn);
				if ( _AppManager.m_RoomDetailData.promoYn.equals("1"))
					data.put("promoDesc", _AppManager.m_RoomDetailData.promoDesc);
				
				data.put("roomIncInfo", _AppManager.m_RoomDetailData.roomIncInfo);
				//data.put("remarkCode", "");
				//data.put("billSectionCode", "CA");
				data.put("addRemark", "");
				data.put("roomPrice", _AppManager.m_RoomDetailData.roomPrice);
				data.put("liveRoomPrice", _AppManager.m_TotalPrice);
				data.put("liveCurrencyCode", "KRW");
				data.put("currencyCode", "KRW");
				
				data.put("lguCstMid", _AppManager.m_RoomReserData.lguCstMid);
				
				
				for ( Integer i = 0 ; i < _AppManager.m_RoomOptionData.size(); i++ )
				{
					data.put("optionCode"+"["+ i+"]" ,_AppManager.m_RoomOptionData.get(i).optionCode);
					data.put("optionName"+"["+ i+"]" ,_AppManager.m_RoomOptionData.get(i).optionName);
					data.put("optionSendPrice"+"["+ i+"]" ,_AppManager.m_RoomOptionData.get(i).optionSendPrice);
					data.put("optionPrice"+"["+ i+"]" ,_AppManager.m_RoomOptionData.get(i).optionPrice);
					data.put("optionPriceType"+"["+ i+"]" ,_AppManager.m_RoomOptionData.get(i).optionPriceType);
					data.put("optionMethodType"+"["+ i+"]" ,_AppManager.m_RoomOptionData.get(i).optionMethodType);
					
				}
				
				for ( Integer i = 0 ; i < _AppManager.m_RoomCancel.size() ; i++ )
				{
					data.put("cancelDay"+"["+ i+"]" ,_AppManager.m_RoomCancel.get(i).cancelDay);
					data.put("cancelPerCent"+"["+ i+"]" ,_AppManager.m_RoomCancel.get(i).cancelPrice);
					//data.put("cancelPrice"+"["+ i+"]" ,_AppManager.m_RoomCancel.get(i).cancelPrice);
				}
				

				String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData(_AppManager.DEF_URL +  "/mweb/booking/bookingAddDom.gm", data);

				try 
				{
					JSONObject json = new JSONObject(strJSON);
					if(json.getString("errorCode").equals("0"))
					{						
						_AppManager.m_ResvNum = json.getString("resvNum");
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
	
	public void RefreshUI()
	{
		 AppManagement _AppManager = (AppManagement) getApplication();
		 String Temp = "";
		 Temp +="���� �ü��� :" + _AppManager.m_HotelName +"\n";
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

				Temp += "�����Ⱓ :" + sbDate + "~" + getDate(Integer.parseInt(_AppManager.m_Duration)) + "\n";
			}
		 
		 Temp +="�������� :" + _AppManager.m_RoomDetailData.roomName +"\n";
		 Temp +="���Ǽ� :" + _AppManager.m_NumRoom +"\n";
		 if ( _AppManager.m_NumChild.equals("0") )
		 {
			 Temp +="���Ǵ� �ο� : " +  _AppManager.m_NumPer + "[����]" +"\n";
		 }
		 else
		 {
			 Temp +="���Ǵ� �ο� : " +  _AppManager.m_NumPer + "/" +_AppManager.m_NumChild + "[����/���]"+"\n";
		 }
		 String tempString = "";
		for ( int i = 0 ; i < _AppManager.m_RoomOptionData.size() ; i++ )
		{
			if( _AppManager.m_RoomOptionData.get(i).optionMethodType.equals("Y"))
			{
				tempString += _AppManager.m_RoomOptionData.get(i).optionName;
				tempString += "  ";
			}
		}
		
		if ( !tempString.equals("") )
		{
			 Temp +="����Ÿ�� : " + tempString +"\n";
		}
		
		
		tempString = "";
		for ( int i = 0 ; i < _AppManager.m_RoomOptionData.size() ; i++ )
		{
			if( _AppManager.m_RoomOptionData.get(i).optionMethodType.equals("S"))
			{
				tempString += _AppManager.m_RoomOptionData.get(i).optionName;
				tempString += "  ";
			}
		}
		
		if ( !tempString.equals("") )
		{
			 Temp +="�ʼ��ɼ� : " + tempString +"\n";
		}
		
		tempString = "";
		for ( int i = 0 ; i < _AppManager.m_RoomOptionData.size() ; i++ )
		{
			if( _AppManager.m_RoomOptionData.get(i).optionMethodType.equals("N"))
			{
				tempString += _AppManager.m_RoomOptionData.get(i).optionName;
				tempString += "  ";
			}
		}
		
		if ( !tempString.equals("") )
		{
			 Temp +="���ÿɼ� : " + tempString +"\n";
		}
		

		//Temp += ("�߰����� : "  + tempString ) + "\n";
			
		 Temp +="������ :" + _AppManager.m_resvName +"\n";
		 Temp +="����ó :" + _AppManager.m_resvTel +"\n";
		 Temp +="�޴�����ȣ :" + _AppManager.m_resvMobile +"\n";
		 Temp +="E-Mail :" + _AppManager.m_resvEmail +"\n";
		 //Temp +="���޻��� :" + _AppManager.m_resvIyagi +"\n";

		 ((TextView)findViewById(R.id.reser_data_1)).setText(Temp);
		((TextView)findViewById(R.id.reser_money)).setText(_AppManager.m_TotalPrice +"��");
			
			
			
			
			
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
				
				final AppManagement _AppManager = (AppManagement) getApplication();
				if (_AppManager.m_LoginCheck == true)
				{
					double price = Double.parseDouble(_AppManager.m_RoomReserData.totalPrice);
			    	 int price2 = (int)price;
			    	 _AppManager.m_PayURL = _AppManager.DEF_URL+"/mweb/booking/payRequestForm.gm?appOsType=android&cstMid="+
				    			_AppManager.m_RoomReserData.lguCstMid + "&oid=" + _AppManager.m_ResvNum + "&amount=" +  price2 +
				    			"&buyer=" +_AppManager.m_resvName + "&productInfo=" + _AppManager.m_HotelName + "&buyerEmail=" +
				    			_AppManager.m_resvEmail ;
					Intent intent;
		            intent = new Intent().setClass(baseself, ReservationWebActivity.class);
		            startActivity( intent );
					
					
				}
				else
				{
					new AlertDialog.Builder(self)
					 .setTitle("��ȸ�� ����Ȯ��")
					 .setMessage("�����ȣ�� "+ _AppManager.m_ResvNum + " �Դϴ�. ") 
					 .setPositiveButton("Ȯ��", new DialogInterface.OnClickListener() 
					 {
					     public void onClick(DialogInterface dialog, int whichButton)
					     {   
					    	 double price = Double.parseDouble(_AppManager.m_RoomReserData.totalPrice);
					    	 int price2 = (int)price;
					    	 _AppManager.m_PayURL = _AppManager.DEF_URL+"/mweb/booking/payRequestForm.gm?appOsType=android&cstMid="+
						    			_AppManager.m_RoomReserData.lguCstMid + "&oid=" + _AppManager.m_ResvNum + "&amount=" +  price2 +
						    			"&buyer=" +_AppManager.m_resvName + "&productInfo=" + _AppManager.m_HotelName + "&buyerEmail=" +
						    			_AppManager.m_resvEmail ;
					    	Intent intent;
					        intent = new Intent().setClass(baseself, ReservationWebActivity.class);
					        startActivity( intent );
					     }
					 })
					 .show();
				}
				
		    	
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
		case R.id.reser_pay:
			GetExecuteData();
			break;
		}
		
	}


    

}
