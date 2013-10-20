package com.humapcontents.mapp;



import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.euiweonjeong.base.BitmapManager;
import com.humapcontents.mapp.AuditionDetailActivity.ReplyData;
import com.humapcontents.mapp.data.HomeWeekLyRank;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class SettingNoticeActivity extends MappBaseActivity implements OnClickListener {


	
	private class NoticeData
	{
		int  idx;
		int type;
		String title;
		String date;
		String text;
	}
	


	
	private SettingNoticeActivity self;
	

	private ListView m_ListView;
	
	

	ArrayList< NoticeData > 	m_NoticeList;
	private Notice_Adapter m_Adapter;
	

	public Integer m_SelectIndex =-1;
	public String m_SelectText ="";

	private boolean mLockListView; 
	
	public ProgressDialog mProgress2;
	private LayoutInflater mInflater;
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_notice_layout);  // ��Ʈ�� ���̾ƿ� ���     

        self = this;

        mLockListView = true; 
        
        // ���α׷��� ���̾�α� 
        {
        	mProgress = new ProgressDialog(this);
    		mProgress.setMessage("��ø� ��ٷ� �ּ���.");
    		mProgress.setIndeterminate(true);
    		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		mProgress.setCancelable(false);
        }
        
        {
        	mProgress2 = new ProgressDialog(this);
        	mProgress2.setMessage("������ ���� ����Ʈ�� �������Դϴ�. ");
        	mProgress2.setIndeterminate(true);
        	mProgress2.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        	mProgress2.setCancelable(false);
        }
		
        
        ImageResize(R.id.main_layout);
        ImageResize(R.id.notice_list_view);
        {
        	
        	m_ListView = ((ListView)findViewById(R.id.notice_list_view));
        	
        	
        	
        }
        


        
    	m_NoticeList = new ArrayList< NoticeData >();
    	
    	
    	m_NoticeList.clear();
    	
    	m_Adapter = new Notice_Adapter(this, R.layout.notice_row, m_NoticeList);
    	
    	// Ǫ�͸� ����մϴ�. setAdapter ������ �ؾ� �մϴ�. 
        mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        
    	m_ListView.setAdapter(m_Adapter);
		//m_ListView.setDivider(null); 
        
        
	
		
    	ImageBtnEvent(R.id.title_icon , this);
        
        
        {
        	NoticeData item = new NoticeData();
			
			item.idx =  -1;
			item.title = "S�ٹ� ���� ����";
			item.date = "2012-04-14 20:56:50.0";
			item.text = "S-LBUM ���� ���� �� ����\n\n�� HUMAP CONTENTS �� ���� ���� ���� (�̸����� ���� �� 30��)\n�� ���� ���� �� �Ѵް�, STAGE �� ����, �ε� Ȥ�� �Ƹ��߾� ��Ƽ��Ʈ���� ���� ���� \n�� �Ѵ� ��, ���� ���� ��õ���� ���� VOCAL ��Ʈ �Ѹ�, ������ ����(��Ÿ,���̽�,�ǾƳ� ��) ��Ʈ �Ѹ�, �� 2���� �̾� S-ALBUM ���ۿ� ��\n�� S-ALBUM �� ���۵� ��������, ���� MAPP STAR���� �Ұ��� ����\n\n\nMAPP STAR ���� ���� �� ����\n\n�� �Ѵް�, 3���� ���ߵ� (������ �ִ� ������ S-LBUM �����)\n�� �� ��, ȭ���� �Ǿ��� �ι� 3���� �����Ͽ�(HUMAP�� ��ü ���ؿ� ����), �����Ͽ� �Ѹ�,  MAPP STAR���� �Ұ���.\n";
			
			item.type = 0;

			m_NoticeList.add(item);
        }
        
        BottomMenuDown(true);
        AfterCreate( 7 );
        
        GetNoticeList();
        
        ImageBtnEvent(R.id.title_icon , this);

    }
    
 
    public void ImageBtnResize( int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	ImageView imageview = (ImageView)findViewById(id);
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
		case R.id.title_bar:
			break;
		case R.id.title_name:
		case R.id.title_desc:
			
		{

		}
			
			break;
		}
		
	}
	
	public void RefreshUI()
	{

		m_Adapter.notifyDataSetChanged();
		
		mLockListView = false;  

	}
	
	

	
	public void GetNoticeList()
	{
		{
			final  AppManagement _AppManager = (AppManagement) getApplication();
			mProgress.show();
			Thread thread = new Thread(new Runnable()
			{

				public void run() 
				{

					Map<String, String> data = new HashMap  <String, String>();

					
					
					String strJSON = _AppManager.GetHttpManager().GetHTTPData(_AppManager.DEF_URL +  "/mapp/Notice", data);

					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.optString("errcode").equals("0"))
						{
							JSONArray usageList = (JSONArray)json.get("notice");
							
							for(int i = 0; i < usageList.length(); i++)
							{
								NoticeData item = new NoticeData();
								JSONObject list = (JSONObject)usageList.get(i);
								
								item.idx =  Integer.parseInt(list.optString("idx"));
								item.title = (list.optString("title"));
								item.date =  (list.optString("date"));
								item.type = 0;

								m_NoticeList.add(item);
							}
							
							
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
	
	public void GetNoticeData()
	{
		{
			final  AppManagement _AppManager = (AppManagement) getApplication();
			mProgress.show();
			
			
			Thread thread = new Thread(new Runnable()
			{

				public void run() 
				{

					if ( m_SelectIndex == -1 )
					{
						handler.sendEmptyMessage(20);
						return;
					}
					Map<String, String> data = new HashMap  <String, String>();

					data.put("idx", m_SelectIndex.toString());
					
					String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData(_AppManager.DEF_URL +  "/mapp/Notice/Detail", data);

					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.optString("errcode").equals("0"))
						{
							
							m_SelectText = (json.optString("text"));
							
							
							
							handler.sendEmptyMessage(20);
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
				RefreshUI();
				
				break;
			case 1:
				// ����ó�� 
				self.ShowAlertDialLog( self ,"����" , (String) msg.obj );
				
				break;
			case 2:
				
				break;
			case 20:
			{
				for ( int i = 0 ; i < m_NoticeList.size() ; i++ )
				{
					m_NoticeList.get(i ).type = 0;
					
					if ( m_SelectIndex != -1 && m_SelectIndex == m_NoticeList.get(i ).idx )
					{
						m_NoticeList.get(i ).type = 1;
						m_NoticeList.get(i ).text = m_SelectText;
					}
					else if ( m_SelectIndex == -1 )
					{
						m_NoticeList.get(i ).type = 1;
					}
				}
				
			
				m_Adapter.notifyDataSetChanged();
			}
				break;
				

			default:
				break;
			}

		}
    	
	};
	public class Notice_Adapter extends ArrayAdapter<NoticeData>
    {
    	private Context mContext;
		private int mResource;
		private ArrayList<NoticeData> mList;
		private LayoutInflater mInflater;
		
    	public Notice_Adapter(Context context, int layoutResource, ArrayList<NoticeData> mTweetList)
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
    		final NoticeData mBar = mList.get(position);
    		final  AppManagement _AppManager = (AppManagement) getApplication();

			if(convertView == null)
			{
				convertView = mInflater.inflate(mResource, null);
			}

			if(mBar != null) 
			{
				LinearLayout frameBar1 = (LinearLayout)convertView.findViewById(R.id.notice_row_main);
				LinearLayout frameBar3 = (LinearLayout)convertView.findViewById(R.id.notice_row1);
				
				frameBar1.setVisibility(View.VISIBLE);
				
				switch( mBar.type )
				{
				case 0:
				{
					
					frameBar3.setVisibility(View.GONE);
					

					
					((TextView)convertView.findViewById(R.id.notice_row_title)).setText(mBar.title);
					((TextView)convertView.findViewById(R.id.notice_row_desc)).setText(mBar.date);
					frameBar1.setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
							m_SelectIndex = mBar.idx;
							self.GetNoticeData();
						}
					});
					

					

				}
				break;
				
				case 1:
				{
					
					frameBar3.setVisibility(View.VISIBLE);
					((TextView)convertView.findViewById(R.id.notice_row_content)).setText(mBar.text);
					
					
					frameBar1.setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
							mBar.type = 0;
							self.RefreshUI();
						}
					});
					

				}
				break;
				
				
				}
			}
			return convertView;
		}
    }
	
	

	
}
