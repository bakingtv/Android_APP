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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
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

import com.euiweonjeong.base.BaseActivity;
import com.euiweonjeong.base.BitmapManager;

import com.humapcontents.mapp.data.HomeAlbum;
import com.humapcontents.mapp.data.HomeData;
import com.humapcontents.mapp.data.HomeSqaure;
import com.humapcontents.mapp.data.HomeWeekLyRank;
import com.humapcontents.mapp.data.SquareNetworkData;

public class SquareNetworkActivity extends MappBaseActivity implements OnClickListener {

	
	private class AuditionListData
	{
		int  no;
		int  idx;
		Bitmap img;
		String title;
		String nick;
		String imgurl;
		
		ArrayList<SquareNetworkData> data;
	}

	
	private class AuditionListCo
	{
		int type;
		
		int count;
		int  no;
		int  idx;
		String imgurl;
		Bitmap img;
		String title;
		String nick;
		ArrayList<SquareNetworkData> data1;
		
		
		int  no2;
		int  idx2;
		String imgurl2;
		Bitmap img2;
		String title2;
		String nick2;
		ArrayList<SquareNetworkData> data2;
		
		String url;
	}

	protected static final int CAPTURE_MOVIE = 1000;
	
	private SquareNetworkActivity self;
	
	int m_SelectStageIndex = 0;
	
	private ListView m_ListView;
	
	
	ArrayList< AuditionListData >	m_ListData;
	ArrayList< AuditionListCo > m_ListCoData;
	private AuditionRow_Adapter m_Adapter;
	
    Integer m_Offset  = -1; 
    Integer m_SortIndex = 0;
    Boolean m_Refresh = true;
    

	
	Gallery m_Gallery ;
	
	private boolean mLockListView; 
	
	public ProgressDialog mProgress2;
	private LayoutInflater mInflater;
	
	private View m_Footer;
	
	public Boolean m_bFooter = true;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auditionlist_layout);  // ��Ʈ�� ���̾ƿ� ���     

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
		
        ((TextView)findViewById(R.id.title_desc)).setText("HUMAP�� ���޾�ü");
        ((TextView)findViewById(R.id.title_name)).setText("�޸� ���޻�");

        ImageResize(R.id.main_layout);
        ImageResize(R.id.audi_list_view);
        {
        	OnScrollListener listen = new OnScrollListener() 
        	{
      		  
     		   public void onScrollStateChanged(AbsListView view, int scrollState) {}
     		  
     		   // ù��° �������� �� ���̸� �ϴ� �ٸ� ���ش�. 
     		   public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) 
     		   {
	        		    if(totalItemCount > 0 && firstVisibleItem != 0) 
	        		    {
	        		    	self.BottomMenuDown(true);
	        		    }
	        		    else
	        		    {
	        		    	self.BottomMenuDefault();
	        		    }
	        		    
	   
     		   }
        	};
        	m_ListView = ((ListView)findViewById(R.id.audi_list_view));
        	
        	m_ListView.setOnScrollListener(listen);
        	
        }
        
        {
        	
        	
        	FrameLayout imageview = (FrameLayout)findViewById(R.id.title_bar);
            imageview.setOnClickListener(this);
            ((TextView)findViewById(R.id.title_desc)).setOnClickListener(this);
            ((TextView)findViewById(R.id.title_name)).setOnClickListener(this);
        }
        
        {
        	
        	
        	((ImageView)findViewById(R.id.title_icon)).setBackgroundResource(R.drawable.right_back_icon);
        	ImageView imageview = (ImageView)findViewById(R.id.title_icon2);
        	imageview.setVisibility(View.GONE);
        }

        m_ListData = new ArrayList< AuditionListData >();
    	m_ListCoData = new ArrayList< AuditionListCo >();
    	

    	m_ListCoData.clear();
    	m_ListData.clear();
    	m_Adapter = new AuditionRow_Adapter(this, R.layout.audition_row, m_ListCoData);
    	
    	// Ǫ�͸� ����մϴ�. setAdapter ������ �ؾ� �մϴ�. 
        mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        m_Footer  = mInflater.inflate(R.layout.footer, null);
        m_ListView.addFooterView(m_Footer);
        
    	m_ListView.setAdapter(m_Adapter);
		m_ListView.setDivider(null); 
        
        ImageBtnResize(R.id.title_icon2);
        BottomMenuDown(true);
        AfterCreate( 3 );
        
        GetNetworkData();
        
        ImageBtnEvent(R.id.title_icon , this);

    }
    
    public void FooterHide()
    {
    	if (m_bFooter == true)
    	{
    		m_bFooter = false;
    		
    		(m_Footer).setVisibility(View.GONE);
 /*   		 m_ListView.removeFooterView(mInflater.inflate(R.layout.footer, null));
    		m_ListView.setAdapter(m_Adapter);*/
    	}
    }
    public void FooterShow()
    {
    	if (m_bFooter == false)
    	{
    		m_bFooter = true;
    		(m_Footer).setVisibility(View.VISIBLE);
 /*   	    m_ListView.addFooterView(mInflater.inflate(R.layout.footer, null));
    		m_ListView.setAdapter(m_Adapter);*/
    	}
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

		case R.id.title_bar:
			break;
		case R.id.title_icon:
		{
			onBackPressed();
		}
		break;

		}
		
	}
	
	public void RefreshUI()
	{
		if ( m_SortIndex != 2  && m_ListData.size() > 19 )
		{
			FooterShow();
		}
		else
		{
			FooterHide();
		}
		int adcount = 0;
		int listcount = 0;
		
		// �����Ͱ� �ϳ��� ���� �Ұ�쿡 ���� ���� ó�� 
		if ( m_ListData.size() == 1)
		{
			AuditionListCo data = new AuditionListCo();
			data.type = 0; 
			
			data.no = m_ListData.get(0 ).no;
			data.idx = m_ListData.get(0 ).idx;
			data.img = m_ListData.get(0 ).img;
			data.title = m_ListData.get(0 ).title;
			data.nick = m_ListData.get(0 ).nick;
			data.imgurl = m_ListData.get(0 ).imgurl;
			data.data1 = m_ListData.get(0 ).data;
			data.count = 1;
			m_ListCoData.add(data);
		}
		else
		{
			int loopcount = (m_ListData.size()/2);
			if ( (m_ListData.size()%2) != 0 )
				loopcount =  (m_ListData.size()/2) + 1;
			
			for ( int i = 0 ; i < loopcount ; i++  )
			{
				
				
				AuditionListCo data = new AuditionListCo();
				data.type = 0; 
				
				data.no = m_ListData.get(i * 2 ).no;
				data.idx = m_ListData.get(i * 2 ).idx;
				data.img = m_ListData.get(i * 2 ).img;
				data.title = m_ListData.get(i * 2 ).title;
				data.nick = m_ListData.get(i * 2 ).nick;
				data.imgurl = m_ListData.get(i * 2 ).imgurl;
				data.data1 = m_ListData.get(i * 2 ).data;
				

				// ������ ���� �����Ͱ� �Ѱ��� ���� �Ұ�� �˻�
				
				// �ΰ� �� �����Ұ�� 
				if ( (i== ((m_ListData.size()/2)-1) ) && (m_ListData.size()%2) == 0 )
				{
					data.no2 = m_ListData.get(i * 2  +1).no;
					data.idx2 = m_ListData.get(i * 2  +1).idx;
					data.img2 = m_ListData.get(i * 2 +1).img;
					data.title2 = m_ListData.get(i * 2 +1).title;
					data.nick2 = m_ListData.get(i * 2 +1 ).nick;
					data.imgurl2 = m_ListData.get(i * 2 + 1 ).imgurl;
					data.data2 = m_ListData.get(i * 2+ 1 ).data;
					data.count = 2;
				}
				//�ϳ��� �����Ұ�� 
				else if ( (i== (loopcount-1) ) && (m_ListData.size()%2) == 1 )
				{
					data.count = 1;
				}
				else
				{
					data.no2 = m_ListData.get(i * 2  +1).no;
					data.idx2 = m_ListData.get(i * 2  +1).idx;
					data.img2 = m_ListData.get(i * 2 +1).img;
					data.title2 = m_ListData.get(i * 2 +1).title;
					data.nick2 = m_ListData.get(i * 2 +1 ).nick;
					data.imgurl2 = m_ListData.get(i * 2 + 1 ).imgurl;
					data.data2 = m_ListData.get(i * 2+ 1 ).data;
					data.count = 2;
				}
				
				
				

				
				m_ListCoData.add(data);

			}
		}

		
		m_Adapter.notifyDataSetChanged();
		
		mLockListView = false;  

	}
	
	public void GetNetworkData()
	{
		mLockListView = true;
		{
			final  AppManagement _AppManager = (AppManagement) getApplication();
			mProgress.show();
			Thread thread = new Thread(new Runnable()
			{

				public void run() 
				{

					Map<String, String> data = new HashMap  <String, String>();


					
					String strJSON = _AppManager.GetHttpManager().GetHTTPData(_AppManager.DEF_URL +  "/mapp/Square/Network", data);

					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.optString("errcode").equals("0"))
						{
							
							JSONArray usageList = (JSONArray)json.get("network");
							
							for(int i = 0; i < usageList.length(); i++)
							{
								AuditionListData item = new AuditionListData();
								JSONObject list = (JSONObject)usageList.get(i);
								
								item.idx =  Integer.parseInt(list.optString("idx"));
								item.nick = (list.optString("group"));
								
								item.imgurl = _AppManager.DEF_URL +  "/mapp/ImageLoad?page=NetworkGroup&idx=" + item.idx;
								
								item.data = new ArrayList<SquareNetworkData>();
								JSONArray usageList2 = (JSONArray)list.get("info");
								
								for(int j = 0; j < usageList2.length(); j++)
								{
									JSONObject list2 = (JSONObject)usageList2.get(j);
									SquareNetworkData data1 = new SquareNetworkData();
									
									data1.idx =  Integer.parseInt(list2.optString("idx"));
									data1.title =  (list2.optString("title"));
									//data1.text =  (list2.optString("text"));
									data1.text =  list2.optString("text");
									data1.imgn = new ArrayList<Integer>();
									
									JSONArray usageList3 = (JSONArray)list2.get("img");
									
									for(int k = 0; k < usageList3.length(); k++)
									{
										data1.imgn.add( usageList3.getInt(k));
									}
									
									item.data.add(data1);
								}
	
								
								m_ListData.add(item);
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
				
			default:
				break;
			}

		}
    	
	};
	public class AuditionRow_Adapter extends ArrayAdapter<AuditionListCo>
    {
    	private Context mContext;
		private int mResource;
		private ArrayList<AuditionListCo> mList;
		private LayoutInflater mInflater;
		
    	public AuditionRow_Adapter(Context context, int layoutResource, ArrayList<AuditionListCo> mTweetList)
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
    		final AuditionListCo mBar = mList.get(position);
    		final  AppManagement _AppManager = (AppManagement) getApplication();

			if(convertView == null)
			{
				convertView = mInflater.inflate(mResource, null);
			}

			if(mBar != null) 
			{
				LinearLayout frameBar1 = (LinearLayout)convertView.findViewById(R.id.audition_row_1);
				LinearLayout frameBar2 = (LinearLayout)convertView.findViewById(R.id.audition_row_2);
				switch( mBar.type )
				{
				case 0:
				{
					frameBar1.setVisibility(View.VISIBLE);
					frameBar2.setVisibility(View.GONE);
					
					LinearLayout detailBar1 = (LinearLayout)convertView.findViewById(R.id.audition_row_1_1);
					LinearLayout detailBar2 = (LinearLayout)convertView.findViewById(R.id.audition_row_1_2);
					
					((TextView)convertView.findViewById(R.id.audition_row_1_text1)).setText(mBar.title);
					((TextView)convertView.findViewById(R.id.audition_row_1_text2)).setText(mBar.nick);
					((TextView)convertView.findViewById(R.id.audition_row_1_text2)).setTextColor(0xffffffff);
					
					ImageView Image = (ImageView)convertView.findViewById(R.id.audition_row_1_img);
					
					Image.setTag(mBar.imgurl);
					BitmapManager.INSTANCE.loadBitmap(mBar.imgurl, Image, 216, 136);

					
					detailBar1.setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
							_AppManager.m_SquareDetailNetworkData = mBar.data1;
							
							Intent intent;
				               
			                intent = new Intent().setClass(self, SquareNetworkDetailActivity.class);
			                
			                
			                startActivity( intent );
			                

						}
					});
					
		    		
		    		
		    		((ImageView)convertView.findViewById(R.id.audition_row_1_del)).setVisibility(View.GONE);
					if ( mBar.count == 1 )
					{
						detailBar2.setVisibility(View.VISIBLE);
						detailBar2.setBackgroundDrawable(null);
						((TextView)convertView.findViewById(R.id.audition_row_2_text1)).setVisibility(View.GONE);
						((TextView)convertView.findViewById(R.id.audition_row_2_text2)).setVisibility(View.GONE);
						
						((TextView)convertView.findViewById(R.id.audition_row_2_text1)).setText("");
						((TextView)convertView.findViewById(R.id.audition_row_2_text2)).setText("");
						((TextView)convertView.findViewById(R.id.audition_row_2_text2)).setTextColor(0xffffffff);
						((ImageView)convertView.findViewById(R.id.audition_row_2_img)).setBackgroundResource(R.drawable.audition_vacuum);
						((ImageView)convertView.findViewById(R.id.audition_row_2_img)).setVisibility(View.GONE);
						
						((ImageView)convertView.findViewById(R.id.audition_row_2_del)).setVisibility(View.GONE);
						
						detailBar2.setOnClickListener(new View.OnClickListener() 
						{

							public void onClick(View v) 
							{
						


							}
						});
					}
					else
					{
						detailBar2.setVisibility(View.VISIBLE);
						detailBar2.setBackgroundResource(R.drawable.audition_area);
						((TextView)convertView.findViewById(R.id.audition_row_2_text1)).setText(mBar.title2);
						((TextView)convertView.findViewById(R.id.audition_row_2_text2)).setText(mBar.nick2);
						((TextView)convertView.findViewById(R.id.audition_row_2_text2)).setTextColor(0xffffffff);
						
						ImageView Image1 = (ImageView)convertView.findViewById(R.id.audition_row_2_img);
						((ImageView)convertView.findViewById(R.id.audition_row_2_img)).setVisibility(View.VISIBLE);
			    		
						Image1.setTag(mBar.imgurl2);
						BitmapManager.INSTANCE.loadBitmap(mBar.imgurl2, Image1, 216, 136);

			    		((ImageView)convertView.findViewById(R.id.audition_row_2_del)).setVisibility(View.GONE);
			    		
			    		
			    		detailBar2.setOnClickListener(new View.OnClickListener() 
						{

							public void onClick(View v) 
							{
								_AppManager.m_SquareDetailNetworkData = mBar.data2;
								
								Intent intent;
					               
				                intent = new Intent().setClass(self, SquareNetworkDetailActivity.class);
				                
				                
				                startActivity( intent );


							}
						});
					}
					break;
				}
				
				case 1:
				{

					
					break;
				}
				
				
				}
			}
			return convertView;
		}
    }
	

	
}
