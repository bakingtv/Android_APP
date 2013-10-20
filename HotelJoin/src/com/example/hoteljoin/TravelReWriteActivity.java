package com.example.hoteljoin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.euiweonjeong.base.BitmapManager;
import com.example.hoteljoin.HotelSearchActivity.Destination;
import com.example.hoteljoin.HotelSearchActivity.Search_Adapter;
import com.example.hoteljoin.TravelWriteActivity.NationData;


import com.slidingmenu.lib.SlidingMenu;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;


// ����
public class TravelReWriteActivity extends HotelJoinBaseActivity implements OnClickListener{
	TravelReWriteActivity  self;
	SlidingMenu menu ;
	int MenuSize;
	 
	private final int SELECT_IMAGE = 1;
	private final int SELECT_MOVIE = 2;
	private final int TAKE_GALLERY = 3;
	private final int TAKE_CAMERA = 4;
		
	String filePath= "";
	
	Integer flag = 0;

	String filename ="";
	 String[] m_Video = {"���� ���� ���", "����� ���� �ҷ�����"  };
	Uri fileUri;
	
	Integer m_Tab = -1;
	
	Boolean m_World = false;
	
	public class NationData
	{
		String nationName;
		String nationCode;
	}
	
	NationData[]	m_NationList;
	NationData[]	m_CityList;

	String			m_CurrNation = "KR";
	
	
	Integer			m_NationIndex = 0;
	Integer			m_CityIndex = 0;
	
	String 			m_hotelCode = "";
	
	
	 Boolean UseNetwork = false;			// ���� ��Ʈ��ũ�� ��������� üũ ( ������̶�� ����� ������ ������ �����ؾ��� )
	 Boolean MoreGetData = false;			// �ѹ��� ���;��� ������ �ִ��� Ȯ���Ѵ�. 
	
	
	/// ȣ�� �ڵ��ϼ� ����Ʈ 
	private ListView m_ListView2;
	ArrayList<SearchListData> HotelList;		// 
	private Search_Adapter m_Adapter2;
	
	EditText m_SearchText = null;
	
	boolean m_bPopup2 ;
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.travel_write);
		
		

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
		
		AfterCreate(7);
		

		BottomMenuDown ( true);

		BtnEvent(R.id.write);
		

		
		BtnEvent(R.id.arrow1);
		BtnEvent(R.id.arrow2);
		BtnEvent(R.id.confirm);
		BtnEvent( R.id.page_up_1);
		BtnEvent( R.id.text_1);
		BtnEvent( R.id.text_2);
		BtnEvent( R.id.text_3);
		
		ChangeTab(0);
		//GetCityData();
		
		
		// ȣ�� �ڵ��ϼ� �˻�...
		{
			HotelList = new  ArrayList<SearchListData>();
					
			HotelList.clear();
			m_ListView2 = (ListView)findViewById(R.id.search_list_2);
					
					
	    	m_Adapter2 = new Search_Adapter(this, R.layout.search_row, HotelList);
			    	

			        
	    	m_ListView2.setAdapter(m_Adapter2);
				
		}
			
		// �ؽ�Ʈ ����ɶ� ���� ����Ʈ ����
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
								
						if( !Text.equals("")&& !m_bPopup2 )
						{
							OpenPopup2();
						}
						
						if ( m_bPopup2)
						{
							AutoHotelSearch();
						}
					}   
				}
			};
			//ȣ��
			m_SearchText.addTextChangedListener(watcher);

			ClosePopup2();
		}
		
		
		{
			AppManagement _AppManager = (AppManagement) getApplication();
			ImageView Image = (ImageView)findViewById(R.id.photo_thum);
			Image.setTag( _AppManager.m_rewriteImage);;
			BitmapManager.INSTANCE.loadBitmap_2(_AppManager.m_rewriteImage, Image);
			
			((EditText)findViewById(R.id.write_text)).setText(_AppManager.m_rewriteContent);
			((EditText)findViewById(R.id.write_title)).setText(_AppManager.m_rewriteTitle);
			m_hotelCode = _AppManager.m_rewritehotelcode;
			((TextView)findViewById(R.id.text_3)).setText(_AppManager.m_rewritehotelname);
			
			
			
		}
				
	}
	
	
	public void ChangeTab(int index)
	{
		/*if ( m_Tab != index)
		{
			m_Tab = index;
			((ImageView)findViewById(R.id.radio1)).setBackgroundResource(R.drawable.r_1_bt);
			((ImageView)findViewById(R.id.radio2)).setBackgroundResource(R.drawable.r_1_bt);
			
			switch(m_Tab)
			{
			case 0:
				((ImageView)findViewById(R.id.radio1)).setBackgroundResource(R.drawable.r_2_bt);
				m_World = false;
				break;
			case 1:
				((ImageView)findViewById(R.id.radio2)).setBackgroundResource(R.drawable.r_2_bt);
				m_World = true; 
				break;

			}
			
			if ( m_World )*/
			{
				((ImageView)findViewById(R.id.arrow1)).setVisibility(View.VISIBLE);
				((TextView)findViewById(R.id.text_1)).setText("");
				GetNationData();
			}
			/*else
			{
				((ImageView)findViewById(R.id.arrow1)).setVisibility(View.GONE);
				((TextView)findViewById(R.id.text_1)).setText("�ѱ�");
				m_CurrNation = "KR";
				GetCityData();
				
			}
		}*/

		
	}
	

	@Override
	public void onResume()
	{
		super.onResume();
	}
	
	public void BtnEvent( int id )
    {
		View imageview = (View)findViewById(id);
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
	
	public void OpenPopup2()
	{
		m_bPopup2 = true;
		 ((View)findViewById(R.id.main_layout_2)).setVisibility(View.VISIBLE);
	}
	public void ClosePopup2()
	{
		m_bPopup2 = false;
		 ((View)findViewById(R.id.main_layout_2)).setVisibility(View.GONE);
	}
	
	

	 @Override
	 public void onActivityResult(int requestCode, int resultCode, Intent intent)
	 {    
	  	super.onActivityResult(requestCode, resultCode, intent);
	   	if (resultCode == RESULT_OK) 
	   	{      
	   		if (requestCode == SELECT_IMAGE)       
	   		{            
	   			Uri uri = intent.getData();            
	   			String path = getPath(uri); 
	   			String name = getName(uri); 
	   			String uriId = getUriId(uri);
	   			Log.e("###", "������� : " + path + "\n���ϸ� : " + name + "\nuri : " + uri.toString() + "\nuri id : " + uriId); 
	   			filePath = path;
	   			filename = name;
	   			fileUri = uri;
	   			SetImage();
	   		}       
	   		else if (requestCode == SELECT_MOVIE)        
	   		{           
	   			Uri uri = intent.getData(); 
	   			
	   			/*String path = getPath(uri);          
	   			String name = getName(uri);           
	   			String uriId = getUriId(uri);     */       
	   			Log.e("###", "������� : " + filePath);
	   			//filePath = path;
	   			//filename = name;
	   			fileUri = uri;
	   			Bitmap bm = (Bitmap) intent.getExtras().get( "data" );
	   			SetImage2(bm);
	   			
	   		}   
	   	}
	 }
	 
	// ��������
	private void doSelectImage()
	{   
		
		AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        
        alt_bld.setTitle("���� ����");
        alt_bld.setSingleChoiceItems(m_Video, -1, new DialogInterface.OnClickListener() 
        {
            public void onClick(DialogInterface dialog, int item) 
            {
            	switch( item )
            	{
            	case 0:
            	{
            		{
            			File file1 = new File(Environment.getExternalStorageDirectory() +"/android/data/com.hoteljoin/"); 

            			if( !file1.exists() ) // ���ϴ� ��ο� ������ �ִ��� Ȯ��
            			{
            				if(file1.mkdirs() )
            				{
            		
            				}
            				else
            				{
            			         Toast.makeText(getBaseContext(), 
            			  	           "������ ����� �����ϴ�.", 
            			  	           Toast.LENGTH_LONG).show();
            				}
            			}
            			else
            			{

            			}
            		}

            		// ���� ���� ��θ� �ٲٱ�
            		Intent it = new Intent( ) ;
            		File file = new File( Environment.getExternalStorageDirectory( ), "/android/data/com.hoteljoin"+ "/" + System.currentTimeMillis() + ".jpg"  ) ;
            		
 
            		
            		filePath = file.getAbsolutePath( ) ;
            		 
            		it.putExtra( MediaStore.EXTRA_OUTPUT, filePath ) ;
            		 
            		it.setAction( MediaStore.ACTION_IMAGE_CAPTURE ) ; // ��� �ܸ����� �ȵ� �� �ֱ� ������ �����ؾ� ��
            		startActivityForResult( it, SELECT_MOVIE ) ;
            		
            	}
            		break;
            	case 1:
            	{

            	   	Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            	   	i.setType("image/*"); 
            	   	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);    
            	   	try    
            	   	{        
            	   		startActivityForResult(i, SELECT_IMAGE);
            		} 
            	   	catch (android.content.ActivityNotFoundException e)   
            	   	{        
            	   		e.printStackTrace();   
            	   	}	
            		
            	}
            		break;

            	}

            	dialog.cancel();
            }
        });
        AlertDialog alert = alt_bld.create();
        alert.show();

	} 
	 
	// ���ϸ� ã��
	private String getName(Uri uri)
	    {    
	    	String[] projection = { MediaStore.Images.ImageColumns.DISPLAY_NAME };
	    	Cursor cursor = managedQuery(uri, projection, null, null, null); 
	    	int column_index = cursor .getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DISPLAY_NAME);
	    	cursor.moveToFirst();    
	    	return cursor.getString(column_index);
	    } 
	    // uri ���̵� ã��
	private String getUriId(Uri uri)
	    {    
	    	String[] projection = { MediaStore.Images.ImageColumns._ID }; 
	    	Cursor cursor = managedQuery(uri, projection, null, null, null);
	    	int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID);
	    	cursor.moveToFirst();   
	    	return cursor.getString(column_index);
	    }
	    
	    


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.write:
			doSelectImage();
			break;
		/*case R.id.radio1:
			ChangeTab(0);
			break;
		case R.id.radio2:
			ChangeTab(1);
			break;*/
		case R.id.text_1:
		case R.id.arrow1:
		{
			String []	NationList = new String[m_NationList.length  ];
			
			for ( int i = 0 ; i < m_NationList.length; i++  )
			{
				NationList[i ] = m_NationList[i].nationName;
			}
			AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
	        alt_bld.setTitle("���� ����");
	        alt_bld.setSingleChoiceItems(NationList, -1, new DialogInterface.OnClickListener() 
	        {
	            public void onClick(DialogInterface dialog, int item) 
	            {
	            	m_NationIndex = item ;
	            	((TextView)findViewById(R.id.text_1)).setText(m_NationList[m_NationIndex].nationName);
					m_CurrNation = m_NationList[m_NationIndex].nationCode;
					GetCityData();
	            	dialog.cancel();
	            }
	        });
	        AlertDialog alert = alt_bld.create();
	        alert.show();
		}
			break;
		case R.id.text_2:
		case R.id.arrow2:
		{
			String []	NationList = new String[m_CityList.length  ];
			
			for ( int i = 0 ; i < m_CityList.length; i++  )
			{
				NationList[i ] = m_CityList[i].nationName;
			}
			AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
	        alt_bld.setTitle("���� ����");
	        alt_bld.setSingleChoiceItems(NationList, -1, new DialogInterface.OnClickListener() 
	        {
	            public void onClick(DialogInterface dialog, int item) 
	            {
	            	m_CityIndex = item ;
	            	((TextView)findViewById(R.id.text_2)).setText(m_CityList[m_CityIndex].nationName);
	            	dialog.cancel();
	            }
	        });
	        AlertDialog alert = alt_bld.create();
	        alert.show();
		}
			break;
		case R.id.confirm:
		{
			GetWrite();
		}
			break;
			
		case R.id.page_up_1:
		{
			ClosePopup2();
		}
			break;
			
		 case R.id.text_3:
			OpenPopup2();

		}
		
	}

	
	public void SetImage()
	{
		BitmapFactory.Options options = new BitmapFactory.Options( ) ; 	 // ��Ʈ�� �̹����� �ɼ� ���� ���� ����
        options.inJustDecodeBounds = true;  							 // ��Ʈ���� �ٷ� �ε����� ���� �ɼǸ� �޾ƿ���� ����
        BitmapFactory.decodeFile( filePath, options ) ; 				 // ��Ʈ���� �дµ� ���ٿ� ���� �ɼǸ� �޾ƿ��� ��Ʈ�� �� ������ ����
    	/// �̹����� ���̸� ����
        
        int fscale = options.outHeight ;
        if( options.outWidth > options.outHeight )	 // �̹����� ���̺��� ���̰� Ŭ ���
        {
        	 fscale = options.outWidth ;				 // �̹����� ���̸� �����Ͽ� ����
        }
        Bitmap bmp ;	 // ���� �̹����� ������ ����
        
        
        
       	/// �̹����� ���̰� 800���� ũ��
        
        if( 800 < fscale )
        {
        
       	/// �̹����� ����� 800���� ���� ��ŭ ������¡ �ҰŴ�
        
          	 fscale = fscale / 800 ;
        
          	 /// �� ��Ʈ�� �ɼ� ����
        
       	    BitmapFactory.Options options2 = new BitmapFactory.Options();
        
       	    options2.inSampleSize = fscale ;	 /// ������¡�� ���� ����
        
       	    bmp = BitmapFactory.decodeFile( filePath, options2 ) ;	 /// ������ ��Ʈ���� �о�´�.
        
            }
        
            else
        
            {	 /// ����� ���� �ϸ� �׳� �д´�.
        
            	bmp = BitmapFactory.decodeFile( filePath ) ;
        
            }
        
        /// ���� ��Ʈ���� ����ȯ�ؼ� ���� ����
        
        BitmapDrawable dbmp = new BitmapDrawable( bmp );
		Drawable dr = (Drawable)dbmp ;	 /// �װ� �ٽ� ����ȯ
		ImageView imageview = (ImageView)findViewById(R.id.photo_thum);
		imageview.setBackgroundDrawable( dr ) ; /// �� ��ü�� ��׶���� ����
	}
	
	
	public void SetImage2(Bitmap bm)
	{
		BitmapDrawable bmd = new BitmapDrawable( bm ) ;
		// tempPictuePath�� ���� ���� ��θ� �ٲ� ���� ī�޶�� ������ ����� �� ������ ����̴�.
		File copyFile = new File( filePath ) ;
		OutputStream out = null;
		 
		try {
			out = new FileOutputStream(copyFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		bm.compress(CompressFormat.JPEG, 70, out) ;

		try {
			out.close( ) ;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if( copyFile.exists( ) && copyFile.length( ) > 0 ) /// ���������������� ����Ǿ� ������ 
		{
			SetImage();
		}
		else
		{
		}


		


		
	}

	

	
	final Handler handler = new Handler( )
	{
    	
    	
    	public void handleMessage(Message msg)
		{
			mProgress.dismiss();
			UseNetwork = false;
			switch(msg.what)
			{
			case 0:
			{
				m_CityIndex = 0;
				AppManagement _AppManager = (AppManagement) getApplication();
				if ( _AppManager.m_RefreshUI == true )
				{
					for (int i = 0 ; i < m_CityList.length; i++ )
					{
						 if (m_CityList[i].nationCode.equals(_AppManager.m_rewritecity) )
						 {
							 m_CityIndex =  i;
						 }
					}
				}
				
				_AppManager.m_RefreshUI = false; 
				
				((TextView)findViewById(R.id.text_2)).setText(m_CityList[m_CityIndex].nationName);
				RefreshUI();
				
			}
				break;
			case 1:
				// ����ó�� 
				self.ShowAlertDialLog( self ,"����" , (String) msg.obj );
				
				break;
			case 21:
			{

				onBackPressed();
			}
				break;
			case 23:
			{

			}
			case 11:
			{
				m_NationIndex = 0;
				
				AppManagement _AppManager = (AppManagement) getApplication();
				if ( _AppManager.m_RefreshUI == true )
				{
					for (int i = 0 ; i < m_NationList.length; i++ )
					{
						 if (m_NationList[i].nationCode.equals(_AppManager.m_rewritenation) )
						 {
							 m_NationIndex =  i;
						 }
					}
				}
				
				((TextView)findViewById(R.id.text_1)).setText(m_NationList[m_NationIndex].nationName);
				m_CurrNation = m_NationList[m_NationIndex].nationCode;
				
				
				GetCityData();
			}
				break;
			case 30:
				m_Adapter2.notifyDataSetChanged();
				
				// ���� �����͸� �޾ƾ� �Ұ� ������ ��� �ѹ��� �޴´�. 
				if ( MoreGetData == true)
				{
					MoreGetData =false;
					// �Էµ� ����Ʈ ������.
					AutoHotelSearch();
				}
			default:
				break;
			}

		}
    	
	};	
	
	
	public void GetNationData()
	{
		mProgress.show();

		final  AppManagement _AppManager = (AppManagement) getApplication();
		
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{
				Map<String, String> data = new HashMap  <String, String>();



					
				String strJSON = _AppManager.GetHttpManager().GetHTTPData(_AppManager.DEF_URL +  "/mweb/board/diaryNationCodeList.gm", data);

				try 
				{
					JSONObject json = new JSONObject(strJSON);
					if(json.getString("errorCode").equals("0"))
					{


						JSONArray usageList = (JSONArray)json.get("nationList");


	
						m_NationList = new NationData[ usageList.length()];
						// �˻� ������ ��´�. 
						for(int i = 0; i < usageList.length(); i++)
						{
							NationData item = new NationData();
							JSONObject list = (JSONObject)usageList.get(i);

							item.nationName =  _AppManager.GetHttpManager().DecodeString(list.optString("nationName"));
							item.nationCode =  _AppManager.GetHttpManager().DecodeString(list.optString("nationCode"));
							m_NationList[i] = item;
						}


						handler.sendEmptyMessage(11);
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
	
	public void GetCityData()
	{
		mProgress.show();

		final  AppManagement _AppManager = (AppManagement) getApplication();
		
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{
				Map<String, String> data = new HashMap  <String, String>();


				data.put("nationCode", m_CurrNation);	
				String strJSON = _AppManager.GetHttpManager().GetHTTPData(_AppManager.DEF_URL +  "/mweb/board/diaryCityCodeList.gm", data);

				try 
				{
					JSONObject json = new JSONObject(strJSON);
					if(json.getString("errorCode").equals("0"))
					{


						JSONArray usageList = (JSONArray)json.get("cityList");


	
						m_CityList = new NationData[ usageList.length()];
						// �˻� ������ ��´�. 
						for(int i = 0; i < usageList.length(); i++)
						{
							NationData item = new NationData();
							JSONObject list = (JSONObject)usageList.get(i);

							item.nationName =  _AppManager.GetHttpManager().DecodeString(list.optString("cityName"));
							item.nationCode =  _AppManager.GetHttpManager().DecodeString(list.optString("cityCode"));
							m_CityList[i] = item;
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
	
	
	// �ڵ� �˻� 
		public void AutoHotelSearch()
		{
			if ( UseNetwork == true)
			{
				MoreGetData = true;
				return;
			}
			HotelList.clear();
			UseNetwork = true;
			final  AppManagement _AppManager = (AppManagement) getApplication();
			
			Thread thread = new Thread(new Runnable()
			{

				public void run() 
				{

					EditText searchText = (EditText)findViewById(R.id.search_text_1);
					
					Map<String, String> data = new HashMap  <String, String>();

	
					
					data.put("maxrow", "20");
					
					data.put("nationCode", m_CurrNation);
					data.put("cityCode", m_CityList[m_CityIndex].nationCode);
					
					data.put("hotelName", searchText.getText().toString());
		
					
					String strJSON = _AppManager.GetHttpManager().GetHTTPData(_AppManager.DEF_URL +  "/mweb/search/searchHotelCodeList.gm", data);

					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.getString("errorCode").equals("0"))
						{
							JSONArray usageList = (JSONArray)json.get("hotelList");
							
							// �˻� ������ ��´�. 
							for(int i = 0; i < usageList.length(); i++)
							{
								SearchListData item = new SearchListData();
								JSONObject list = (JSONObject)usageList.get(i);
								
								item.hotelName = _AppManager.GetHttpManager().DecodeString(list.optString("hotelName"));
								item.hotelCode =  _AppManager.GetHttpManager().DecodeString(list.optString("hotelCode"));
								
								HotelList.add(item);
								
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
		
	
	
	public void GetWrite()
	{
		mProgress.show();

		final  AppManagement _AppManager = (AppManagement) getApplication();
		
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{
				Map<String, String> data = new HashMap  <String, String>();


				EditText id = (EditText)findViewById(R.id.write_title);
				EditText pass = (EditText)findViewById(R.id.write_text);
				
					
				MultipartEntity entity = new MultipartEntity();
				try {
					entity.addPart("diaryNum", new StringBody(_AppManager.m_diaryNum));
					entity.addPart("cntntNum", new StringBody( _AppManager.m_cntntNum));
					entity.addPart("memberId", new StringBody( _AppManager.m_LoginID));
					
					{
						String value = _AppManager.m_Name;
						//value = EncodeString(data.get(key));
						//value = new String(_AppManager.m_Name.getBytes(), "utf-8");
						value = _AppManager.GetHttpManager().EncodeString(_AppManager.m_Name);
						entity.addPart("writerName", new StringBody(value));
					}
					{
						String value = pass.getText().toString();
						//value = EncodeString(data.get(key));
						//value = new String(pass.getText().toString().getBytes(), "utf-8");
						value = _AppManager.GetHttpManager().EncodeString(pass.getText().toString());
						entity.addPart("contents", new StringBody(value));
					}
					{
						String value = id.getText().toString();
						//value = EncodeString(data.get(key));
						//value = new String(id.getText().toString().getBytes(), "utf-8");
						value = _AppManager.GetHttpManager().EncodeString(id.getText().toString());
						entity.addPart("subject", new StringBody(value));
					}
					
					
					entity.addPart("cityCode", new StringBody(m_CityList[m_CityIndex].nationCode));
					entity.addPart("nationCode", new StringBody(m_CurrNation));
					
					if(!((TextView)findViewById(R.id.text_3)).getText().equals(""))
					{
						String temp = (String) ((TextView)findViewById(R.id.text_3)).getText();
						entity.addPart("hotelName", new StringBody( temp   ) );
						entity.addPart("hotelCode", new StringBody( m_hotelCode   ) );
						
					}
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				ContentBody brFile1 = null;
				
				if ( !filePath.equals(""))
				{
					File file1 = new File(filePath);
					brFile1 = new FileBody(file1 , "image/jpeg" );
					entity.addPart("image" , brFile1);
				}

				String strJSON = _AppManager.GetHttpManager().PostHTTPFileData(_AppManager.DEF_URL +  "/mweb/board/diaryUpdate.gm", entity);


				try 
				{
					JSONObject json = new JSONObject(strJSON);
					if(json.getString("errorCode").equals("0"))
					{



						handler.sendEmptyMessage(21);
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
	
	
	
	public class Search_Adapter extends ArrayAdapter<SearchListData>
    {
    	private Context mContext;
		private int mResource;
		private ArrayList<SearchListData> mList;
		private LayoutInflater mInflater;
		
    	public Search_Adapter(Context context, int layoutResource, ArrayList<SearchListData> mTweetList)
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
    		final SearchListData mBar = mList.get(position);
    		final  AppManagement _AppManager = (AppManagement) getApplication();

			if(convertView == null)
			{
				convertView = mInflater.inflate(mResource, null);
			}

			if(mBar != null) 
			{

				((TextView)convertView.findViewById(R.id.search_row_text)).setText(mBar.hotelName);
				LinearLayout detailBar1 = (LinearLayout)convertView.findViewById(R.id.search_row_1);
				detailBar1.setOnClickListener(new View.OnClickListener() 
				{

					public void onClick(View v) 
					{
						m_hotelCode = mBar.hotelCode;
						((TextView)findViewById(R.id.text_3)).setText(mBar.hotelName);
						ClosePopup2();

					}
				});
				
				detailBar1.setOnLongClickListener(new View.OnLongClickListener() 
				{

					

					@Override
					public boolean onLongClick(View arg0) 
					{
						return false;
					}
				});
				
			}
			return convertView;
		}
    }
}
