package com.example.hoteljoin;

import java.util.ArrayList;
import java.util.Calendar;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView.ScaleType;

public class MyTravelActivity extends HotelJoinBaseActivity implements OnClickListener{
	MyTravelActivity  self;
	 SlidingMenu menu ;
	 int MenuSize;
	 
	public class TravelListData
	{
		TravelListData()
		{
				
		}

			

		String diaryNum;
		String subject;
		String contents;
		String imageUrl;
		String writerName;
		String regDay;
		String nationCode;
		String cityCode;
		String recommendCount;
		String hitCount;
		String replyCount;
		Boolean opensub = false;
	}
	
	ArrayList< TravelListData > m_ListData;
	private Review_Adapter m_Adapter;
		
	public Boolean m_bFooter = true;
	
	private boolean mLockListView; 
	
	private LayoutInflater mInflater;


	private GridView m_ListView;
	private View m_Footer;

	
	 Integer m_CurrentPage = 1;
	 Integer m_TotalPage = 1;
	 
	 Integer m_TotalCount = 0;
	 


	 
	 Boolean  m_Mylist = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.travel_main3);
		
		

		// 비하인드 뷰 설정
		setBehindContentView(R.layout.main_menu_1);
		
		self = this;
		

	    // 프로그래스 다이얼로그 
        {
        	mProgress = new ProgressDialog(this);
    		mProgress.setMessage("잠시만 기다려 주세요.");
    		mProgress.setIndeterminate(true);
    		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		mProgress.setCancelable(false);
        }
        
		
		// 슬라이딩 뷰
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
		
		AfterCreate(4);
		
		

		BtnEvent(R.id.write);
		BtnEvent(R.id.mylist);
		

		m_ListView = ((GridView)findViewById(R.id.main_list));
        m_ListData = new ArrayList< TravelListData >();
        m_ListData.clear();
    	m_Adapter = new Review_Adapter(this, R.layout.my_travel_row, m_ListData);
        
    	m_ListView.setAdapter(m_Adapter);

		
		 {
	        	/*OnScrollListener listen = new OnScrollListener() 
	        	{
	      		  
	     		   public void onScrollStateChanged(AbsListView view, int scrollState) {}
	     		  
	     		   public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) 
	     		   {

		        		    
		        		    // 현재 가장 처음에 보이는 셀번호와 보여지는 셀번호를 더한값이 
		        		    // 전체의 숫자와 동일해지면 가장 아래로 스크롤 되었다고 가정합니다. 
		        		    int count = totalItemCount - visibleItemCount; 

		        		    if(firstVisibleItem >= count && totalItemCount != 0   && mLockListView == false) 
		        		    { 
		        		    	      // 추가
		        		    	
		        		    	if (m_bFooter == true )
		        		    	{
		        		    		m_CurrentPage ++ ;
		        		    		GetData();
		        		    	}

		        		    		
		        		    } 
	     		   }
	        	};
	        	
	        	m_ListView.setOnScrollListener(listen);*/
	        	
	     }
		 
		 {
			 AppManagement _AppManager = (AppManagement) getApplication();
			 m_Mylist = _AppManager.m_DirectMyTravel;
		 }
		 
		//m_Adapter.notifyDataSetChanged();

	}
	
	


	@Override
	public void onResume()
	{
		super.onResume();
		m_ListData.clear();
		GetData();
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
	
	public void RefreshUI()
	{

		m_ListView.setAdapter(m_Adapter);
		m_Adapter.notifyDataSetChanged();

		
		mLockListView = false;  

	}



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		AppManagement _AppManager = (AppManagement) getApplication();
		switch(v.getId())
		{


		case R.id.write:
		{
			
			if (_AppManager.m_LoginCheck == true)
			{
				Intent intent;
	            intent = new Intent().setClass(baseself, TravelWriteActivity.class);
	            startActivity( intent ); 
			}
			else
			{
				self.ShowAlertDialLog( self ,"에러" , "로그인후 사용할수 있습니다. " );
			}
			
		}
			
			break;
		case R.id.mylist:
			if (_AppManager.m_LoginCheck == true)
			{
				Intent intent;
	            intent = new Intent().setClass(baseself, TravelActivity.class);
	            startActivity( intent ); 
			}
			else
			{
				self.ShowAlertDialLog( self ,"에러" , "로그인후 사용할수 있습니다. " );
			}
			
			break;
			
		}
		
	}

	
	public void GetData()
	{
		mProgress.show();
		mLockListView = true;
		final  AppManagement _AppManager = (AppManagement) getApplication();
		
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{
				Map<String, String> data = new HashMap  <String, String>();


				data.put("page", m_CurrentPage.toString());
				data.put("searchTypeCode", "1");
				
				if ( m_Mylist == true )
					data.put("memberId", _AppManager.m_LoginID);
					
				String strJSON = _AppManager.GetHttpManager().GetHTTPData(_AppManager.DEF_URL +  "/mweb/board/diaryList.gm", data);

				try 
				{
					JSONObject json = new JSONObject(strJSON);
					if(json.getString("errorCode").equals("0"))
					{


						JSONArray usageList = (JSONArray)json.get("tourDiaryList");

						m_CurrentPage = json.getInt("currPage");
						m_TotalPage = json.getInt("totalPage");
	
							
						// 검색 정보를 얻는다. 
						for(int i = 0; i < usageList.length(); i++)
						{
							TravelListData item = new TravelListData();
							JSONObject list = (JSONObject)usageList.get(i);

							item.diaryNum =  _AppManager.GetHttpManager().DecodeString(list.optString("diaryNum"));
							item.contents =  _AppManager.GetHttpManager().DecodeString(list.optString("contents"));
							item.imageUrl = _AppManager.GetHttpManager().DecodeString(list.optString("imageUrl"));
							item.subject =  _AppManager.GetHttpManager().DecodeString(list.optString("subject"));
							item.writerName =  _AppManager.GetHttpManager().DecodeString(list.optString("writerName"));
							item.regDay =  _AppManager.GetHttpManager().DecodeString(list.optString("regDay"));
							item.nationCode =  _AppManager.GetHttpManager().DecodeString(list.optString("nationCode"));
							item.cityCode =  _AppManager.GetHttpManager().DecodeString(list.optString("cityCode"));
							item.recommendCount =  _AppManager.GetHttpManager().DecodeString(list.optString("recommendCount"));
							item.hitCount =  _AppManager.GetHttpManager().DecodeString(list.optString("hitCount"));
							item.replyCount =  _AppManager.GetHttpManager().DecodeString(list.optString("replyCount"));

							
							if ( item.subject.equals("삭제된 글 입니다."))
							{
								
							}
							else
							{
								m_ListData.add(item);
							}
							
						}

						/*if ( m_CurrentPage == m_TotalPage)
							handler.sendEmptyMessage(11);
						else-*/
							handler.sendEmptyMessage(0);

					}
					else 
					{
						// 에러 메세지를 전송한다. 
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
				// 오류처리 
				self.ShowAlertDialLog( self ,"에러" , (String) msg.obj );
				
				break;
			case 20:
			{
				 m_ListData.clear();
				m_Adapter.notifyDataSetChanged();
				
				GetData();
				
			}
				break;
			case 23:
			{
				RefreshUI();
			}
			case 11:
			{
				
				RefreshUI();
				
			}
				break;
			default:
				break;
			}

		}
    	
	};	
	
	public class Review_Adapter extends ArrayAdapter<TravelListData>
    {
    	private Context mContext;
		private int mResource;
		private ArrayList<TravelListData> mList;
		private LayoutInflater mInflater;
		
    	public Review_Adapter(Context context, int layoutResource, ArrayList<TravelListData> mTweetList)
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
    		final TravelListData mBar = mList.get(position);
    		final  AppManagement _AppManager = (AppManagement) getApplication();

			if(convertView == null)
			{
				convertView = mInflater.inflate(mResource, null);
			}

			if(mBar != null) 
			{
				LinearLayout detailBar1 = (LinearLayout)convertView.findViewById(R.id.main_row_1);
				LinearLayout detailBar2 = (LinearLayout)convertView.findViewById(R.id.main_row_2);
				
				if ( mBar.imageUrl == null || mBar.imageUrl.equals("") )
				{
					detailBar1.setVisibility(View.GONE);
					detailBar2.setVisibility(View.VISIBLE);
					
					
					((TextView)convertView.findViewById(R.id.main_row_2_text)).setText(mBar.subject);
					
				}
				else
				{
					detailBar1.setVisibility(View.VISIBLE);
					detailBar2.setVisibility(View.GONE);
					ImageView Image = (ImageView)convertView.findViewById(R.id.main_row_1_pic);
					
					Image.setTag( mBar.imageUrl);;
					Image.setScaleType(ScaleType.FIT_XY);
					BitmapManager.INSTANCE.loadBitmap_3(mBar.imageUrl, Image);
					
				}
				
				detailBar1.setOnClickListener(new View.OnClickListener() 
				{

					public void onClick(View v) 
					{
						_AppManager.m_DirNum = mBar.diaryNum;
						Intent intent;
			            intent = new Intent().setClass(baseself, TravelDetailActivity.class);
			            startActivity( intent ); 

					}
				});
				
				detailBar2.setOnClickListener(new View.OnClickListener() 
				{

					public void onClick(View v) 
					{
						_AppManager.m_DirNum = mBar.diaryNum;
						Intent intent;
			            intent = new Intent().setClass(baseself, TravelDetailActivity.class);
			            startActivity( intent ); 

					}
				});

			}
			return convertView;
		}
    }
	

    

}
