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
import com.example.hoteljoin.TravelReWriteActivity.NationData;


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

public class TravelAddWriteActivity extends HotelJoinBaseActivity implements OnClickListener{
	TravelAddWriteActivity  self;
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

	
	
	Integer			m_NationIndex = 0;
	Integer			m_CityIndex = 0;
	
	
	 Boolean UseNetwork = false;			// ���� ��Ʈ��ũ�� ��������� üũ ( ������̶�� ����� ������ ������ �����ؾ��� )
	 Boolean MoreGetData = false;			// �ѹ��� ���;��� ������ �ִ��� Ȯ���Ѵ�. 
	
	

	
	EditText m_SearchText = null;
	
	boolean m_bPopup2 ;
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.travel_rewrite);
		
		

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

		

		BtnEvent(R.id.confirm);

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



		case R.id.confirm:
		{
			GetWrite();
		}
			break;
			

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


			default:
				break;
			}

		}
    	
	};	
	

		
	
	
	public void GetWrite()
	{
		mProgress.show();

		final  AppManagement _AppManager = (AppManagement) getApplication();
		
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{
				Map<String, String> data = new HashMap  <String, String>();


				
				/*data.put("diaryNum", "-1");	
				//data.put("memberId", _AppManager.m_LoginID );	
				//data.put("writerName", _AppManager.m_Name);
				data.put("memberId", "ds1983" );	
				data.put("writerName", "�ٳ���");
				data.put("subject", "�׽�Ʈ");
				data.put("contents", "�����ٶ󸶹ٻ�");	
				data.put("cityCode", m_CityList[m_CityIndex].nationCode);	
				data.put("nationCode", m_CurrNation);	*/
				EditText id = (EditText)findViewById(R.id.write_title);
				EditText pass = (EditText)findViewById(R.id.write_text);
				
					
				MultipartEntity entity = new MultipartEntity();
				try {
					entity.addPart("diaryNum", new StringBody(_AppManager.m_diaryNum));
					entity.addPart("memberId", new StringBody( _AppManager.m_LoginID));
					
					{
						String value = "";
						//value = EncodeString(data.get(key));
						value = new String(_AppManager.m_Name.getBytes(), "utf-8");
						entity.addPart("writerName", new StringBody(value));
					}
					{
						String value = "";
						//value = EncodeString(data.get(key));
						value = new String(pass.getText().toString().getBytes(), "utf-8");
						entity.addPart("contents", new StringBody(value));
					}
					{
						String value = "";
						//value = EncodeString(data.get(key));
						value = new String(id.getText().toString().getBytes(), "utf-8");
						entity.addPart("subject", new StringBody(value));
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

				String strJSON = _AppManager.GetHttpManager().PostHTTPFileData(_AppManager.DEF_URL +  "/mweb/board/diaryAdd.gm", entity);


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

	

    

}
