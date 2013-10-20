package com.humapcontents.mapp;



import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.euiweonjeong.base.BaseActivity;
import com.humapcontents.mapp.data.HomeAlbum;
import com.humapcontents.mapp.data.HomeData;
import com.humapcontents.mapp.data.HomeSqaure;
import com.humapcontents.mapp.data.HomeWeekLyRank;

public class UploadActivity extends MappBaseActivity implements OnClickListener {

	private UploadActivity self;

	String m_SelectCategory = "vocal";

	String[] m_Data = {"vocal", "drums","guitar","bass","piano", "dance", "midi", "performance" };
	String[] m_Data2 = {"Vocal", "Drums","Guitar","Bass","Piano", "Dance", "Midi", "Performance" };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audition_uploade_layout);  // ��Ʈ�� ���̾ƿ� ���     

        self = this;
        
        
        
        // ���α׷��� ���̾�α� 
        {
        	mProgress = new ProgressDialog(this);
    		mProgress.setMessage("��ø� ��ٷ� �ּ���.");
    		mProgress.setIndeterminate(true);
    		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		mProgress.setCancelable(false);
        }
		
       
        
      

        ImageBtnResize(R.id.title_popup);
        ImageResize(R.id.main_layout);
        ImageResize(R.id.upload_tag_1);
        ImageResize(R.id.upload_tag_2);
        
        ImageResize(R.id.upload_tag_3);
        ImageResize(R.id.upload_tag_4);
        ImageResize(R.id.upload_url);

        

        
        
        
        ImageResize(R.id.upload_url_line);
        ImageBtnResize(R.id.upload_cate);
        
        ImageResize(R.id.upload_cate_line);
        ImageResize(R.id.upload_name);
        ImageResize(R.id.upload_name_line);
        ImageResize(R.id.upload_intro);
 
        ImageBtnResize(R.id.upload_url_dummy);
        
        
       
        BottomMenuDown(true);
        AfterCreate( 1 );

        
        ImageBtnEvent(R.id.title_icon , this);
        
        
    }
    
    
    public void ImageBtnResize( int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	View imageview = (View)findViewById(id);
        _AppManager.GetUISizeConverter().ConvertFrameLayoutView(imageview); 
        imageview.setOnClickListener(this);
    }
    

    

	public void onClick(View arg0) {
		// TODO �ڵ� ������ �޼ҵ� ����
	
		switch(arg0.getId() )
		{
		case R.id.title_icon:
		{
			onBackPressed();
		}
		break;
		case R.id.title_popup:
			UploadData();
			break;
		case R.id.upload_cate:
		{
			AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
	        
	        alt_bld.setTitle("Select a Category");
	        alt_bld.setSingleChoiceItems(m_Data2, -1, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int item) 
	            {
	            	
	            	
	            	m_SelectCategory = m_Data[item];
	            	
	            	((TextView)findViewById(R.id.upload_cate)).setText(m_Data2[item]);
	            	
	            	
	                dialog.cancel();
	                
	            }
	        });
	        alt_bld.show();
		}
			
			break;
		case R.id.upload_url_dummy:
		{
			 ((View)findViewById(R.id.upload_url_dummy)).setVisibility(View.GONE);
			 // �˾� ����
			 
		}
			break;
		}
		
	}
	
	public void RefreshUI()
	{
		
        


	}
	
	
	public void UploadData()
    {
    	{
			final  AppManagement _AppManager = (AppManagement) getApplication();
			mProgress.show();
			Thread thread = new Thread(new Runnable()
			{

				public void run() 
				{
					// ID�� �н����带 �����´� 
					

					
					Map<String, String> data = new HashMap  <String, String>();

					data.put("id", _AppManager.m_LoginID);
					data.put("skey", _AppManager.m_MappToken);
					
					EditText intro = (EditText)findViewById(R.id.upload_intro);
					EditText name = (EditText)findViewById(R.id.upload_name);
					EditText url = (EditText)findViewById(R.id.upload_url);
					
					String URLAddress = url.getText().toString();
					
					
					if ( URLAddress.indexOf("youtu.be/") == -1 )
					{
						// �ּ� Ÿ���� http://www.youtube.com/watch?v=id Ÿ����. 
						
						if ( URLAddress.indexOf("watch?v=") ==-1)
						{
							handler.sendEmptyMessage(6);
						}
						else
						{
							int index = URLAddress.indexOf("watch?v=") +  "watch?v=".length();
							String youtubeID = URLAddress.substring( URLAddress.indexOf("watch?v=") );
							data.put("img", "http://img.youtube.com/vi/"+ youtubeID+"/hqdefault.jpg");
						}
					}
					else
					{
						int index = URLAddress.indexOf("youtu.be/") +  "youtu.be/".length();
						String youtubeID = URLAddress.substring( index );
						data.put("img", "http://img.youtube.com/vi/"+ youtubeID+"/hqdefault.jpg");
					}
					
					
					if ( intro.getText().toString().length() > 1  )
					{
						
					}
					else
					{
						handler.sendEmptyMessage(4);
						return;
					}
					
					if ( name.getText().toString().length() > 1 )
					{
						
						
					}
					else
					{
						handler.sendEmptyMessage(5);
						return;
					}
					
					
					
					data.put("video", URLAddress);
					data.put("title", _AppManager.GetHttpManager().EncodeString(name.getText().toString()));
					data.put("comment", _AppManager.GetHttpManager().EncodeString(intro.getText().toString()));
					data.put("deletable", "N" );
					data.put("category", m_SelectCategory );
					
					
					String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData(_AppManager.DEF_URL +  "/mapp/Audition/Upload", data);

					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.optString("errcode").equals("0"))
						{


						

							handler.sendEmptyMessage(0);
						}
						else 
						{
							// ���� �޼����� �����Ѵ�. 
							handler.sendMessage(handler.obtainMessage(1,_AppManager.GetError(json.optInt("errcode")) ));
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
    }
	
	
	final Handler handler = new Handler( )
	{
    	
    	
    	public void handleMessage(Message msg)
		{
			mProgress.dismiss();
			switch(msg.what)
			{
			case 0:
				onBackPressed();
				break;
			case 1:
				// ����ó�� 
				self.ShowAlertDialLog( self ,"����" , (String) msg.obj );
				
				break;
			case 2:

				break;
				
			case 3:
	
				break;
			case 4:
				self.ShowAlertDialLog( self ,"����" , "���� �Ұ��� �Է����ּ���. " );
				break;
			case 5:
				self.ShowAlertDialLog( self ,"����" , "�̸��� �Է����ּ���. " );
				break;
			case 6:
				self.ShowAlertDialLog( self ,"����" , "��Ȯ�� URL�� �Է����ּ��� " );
				break;
			case 20:
				break;
			default:
				break;
			}

		}
    	
	};
	
	

	
}
