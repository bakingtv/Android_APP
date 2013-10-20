package oppa.rcsoft.co.kr;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



// Ŀ�´�Ƽ �Խ��� ( �ı�, ����, �����Խ��� ) 
public class BBS_Main_List extends  BaseActivity   
{
    /** Called when the activity is first created. */
	
	public static BBS_Main_List self;
	int total_count;
	int total_page = 0;
	Integer current_count = 0;
	Integer current_page = 1;
	
	private ArrayList<BBS_Content_Object> m_ObjectArray;
	private BBS_View_Adapter m_Adapter;
	private ListView m_ListView;
	
	Button Morebtn;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bbs_main_list);
        
        self = this;
     
		mProgress = new ProgressDialog(this);
		mProgress.setMessage("��ø� ��ٷ� �ֽʽÿ�.");
		mProgress.setIndeterminate(true);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);
		
		
		// ����Ʈ��
		m_ListView = (ListView)findViewById(R.id.bbs_main_list_view);
		m_ObjectArray = new ArrayList<BBS_Content_Object>();
		m_Adapter = new BBS_View_Adapter(this, R.layout.bbs_list_ui_row, m_ObjectArray);
		m_ListView.setAdapter(m_Adapter);
		
		
		
		///////////////////////////////////////////////////////////////////////////////////////////////////
		// Tab
		{
			Button TabBTN2 = (Button)findViewById(R.id.bbs_main_list_tab_btn_1);
			TabBTN2.setOnClickListener(new BBSMainBTNClick());
			Button TabBTN3 = (Button)findViewById(R.id.bbs_main_list_tab_btn_2);
			TabBTN3.setOnClickListener(new BBSMainBTNClick());
			Button TabBTN4 = (Button)findViewById(R.id.bbs_main_list_tab_btn_3);
			TabBTN4.setOnClickListener(new BBSMainBTNClick());
			Button TabBTN5 = (Button)findViewById(R.id.bbs_main_list_tab_btn_5);
			TabBTN5.setOnClickListener(new BBSMainBTNClick());
		}

        
        Morebtn = (Button)findViewById(R.id.bbs_main_list_more_123);
        Morebtn.setOnClickListener(new BBSMainBTNClick());
        
        Button writebtn = (Button) findViewById(R.id.bbs_main_write);
        
        writebtn.setOnClickListener(new BBSMainBTNClick());
        
        RefreshUI();
    }
    

    public  class BBSMainBTNClick implements OnClickListener
    {

		public void onClick(View v )
        {
			MyInfo myApp = (MyInfo) getApplication();
        	switch(v.getId())
        	{

        		
        	case R.id.bbs_main_write:
        	{
        		myApp.m_BBSID.bo_table = "free";
           		Intent intent;
    	        intent = new Intent().setClass(self, BBS_Writer.class);
    	        startActivity( intent );	
 
        		
        	}
        		break;
        	case R.id.bbs_main_list_more_123:
        		MoreRefreshUI();
        		break;

	    	case R.id.bbs_main_list_tab_btn_1:
	    	{
	       		Intent intent;
		        intent = new Intent().setClass(self, Home.class);
		        startActivity( intent );  		
	    	}
	    		break;
	    	case R.id.bbs_main_list_tab_btn_2:
	    	{
	       		Intent intent;
		        intent = new Intent().setClass(self, Shop_MainList.class);
		        startActivity( intent );  		
	    	}
	    		break;
	    	case R.id.bbs_main_list_tab_btn_3:
	    	{
	       		Intent intent;
		        intent = new Intent().setClass(self, ToyMainList.class);
		        startActivity( intent );  		
	    	}
	    		break;
	    	case R.id.bbs_main_list_tab_btn_5:
	    	{
	       		Intent intent;
		        intent = new Intent().setClass(self, MyPage_Main.class);
		        startActivity( intent );  		
	    	}
	    		break;
        	}
        }
    }
    
    
    @Override
    public void RefreshUI()
    {
    	m_ObjectArray.clear();
    	m_Adapter.clear();
    	mProgress.show();
    	final MyInfo myApp = (MyInfo) getApplication();
    	
    	// ��� Ÿ��Ʋ�� �Էµ� UI���� �����ش�. 
    	if ( myApp.m_BBSID.bo_table.equals("hugi") )
    	{
    		ImageView title = (ImageView) findViewById(R.id.bbs_main_list_title_3);
    		title.setImageResource(R.drawable.n_4_title7);
    		title.setMaxWidth(210);
    		
    		Button writebtn = (Button) findViewById(R.id.bbs_main_write);
    		writebtn.setVisibility(View.GONE);
    	}
    	else if ( myApp.m_BBSID.bo_table.equals("free") )
    	{
    		ImageView title = (ImageView) findViewById(R.id.bbs_main_list_title_3);
    		title.setImageResource(R.drawable.n_4_title8);
    		title.setMaxWidth(180);
    		
    		Button writebtn = (Button) findViewById(R.id.bbs_main_write);
    		writebtn.setVisibility(View.VISIBLE);
    		
    		
    		
    	}
    	else if ( myApp.m_BBSID.bo_table.equals("notice") )
    	{
    		ImageView title = (ImageView) findViewById(R.id.bbs_main_list_title_3);
    		title.setImageResource(R.drawable.n_4_title2);
    		title.setMaxWidth(150);
    		
    		Button writebtn = (Button) findViewById(R.id.bbs_main_write);
    		writebtn.setVisibility(View.GONE);
    	}
    	current_page  = 1;
    	GetBBSPage();

    }
    
    
    // �ı� �������� ��� 
    // �Խù��� Ŭ���� ���� �ı� �������� �������� ���� �������� ���� �������� �Լ�
    public void getIntroduce()
    {
		mProgress.show();
		final MyInfo myApp = (MyInfo) getApplication();
		Thread thread = new Thread(new Runnable()
		{

			
			public void run()
			{

				Map<String, String> data = new HashMap  <String, String>();
				data.put("sh_id",myApp.m_CurrShopInfo.ShopID );
				data.put("return_type", "json");
				
				data.put("geoy", myApp.mLat.toString());
				data.put("geox", myApp.mLon.toString());
				
				String strJSON = myApp.GetHTTPGetData("http://oppa.rcsoft.co.kr/api/oppa_getShopInfo.php", data);

				JSONObject json = null;
				try {
					json = new JSONObject(strJSON);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try 
				{
					if(json.getString("result").equals("ok"))
					{
						myApp.m_CurrShopInfo.IntroMessage = strJSON;
						handler.sendEmptyMessage(10);
					}
					else if(json.getString("result").equals("error"))
					{
						handler.sendMessage(handler.obtainMessage(6,myApp.DecodeString(json.getString("result_text")) ));
					}
				}  catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 

			}
		});
		thread.start();
    }
    
    
    // �����⸦ Ŭ��������...
    public void MoreRefreshUI()
    {
    	mProgress.show();
    	current_page++;


    	if ( current_page > total_page )
    		return; 

    	GetBBSPage();
    }
    
    
    // �Խù� ����Ʈ �������� 
    void GetBBSPage( )
    {
    	final MyInfo myApp = (MyInfo) getApplication();
		final String strPage = current_page.toString();
		Thread thread = new Thread(new Runnable()
		{
			public void run() 
			{
				Map<String, String> data = new HashMap  <String, String>();

				data.put("bo_table",myApp.m_BBSID.bo_table );
				data.put("page", strPage);
				data.put("return_type", "json");

				
				String strJSON = myApp.GetHTTPGetData("http://oppa.rcsoft.co.kr/api/gnu_getBoardList.php", data);	
				if ( strJSON.equals("error"))
				{
					handler.sendMessage(handler.obtainMessage(2,"������ ������ �����Ƿ� ���߿� �ٽ� �õ��ϼ���." ));
					return;
				}
				try 
				{
					JSONObject json = new JSONObject(strJSON);
					if(json.getString("result").equals("ok"))
					{
						total_count = json.getInt("total_count");
						
						total_page = json.getInt("total_page");
						
						if ( total_count != 0 )
						{
							JSONArray usageList = (JSONArray)json.get("list");

							current_count += usageList.length(); 
							for(int i = 0; i < usageList.length(); i++)
							{
								BBS_Content_Object item = new BBS_Content_Object();
								JSONObject list = (JSONObject)usageList.get(i);
								
								if ( myApp.m_BBSID.bo_table.equals("notice"))
									item.setWrSubject(myApp.DecodeString(list.getString("wr_subject")));		// ����
								else
									item.setWrSubject(myApp.DecodeString(list.getString("wr_content")));		// ����
								
								item.setWrName(myApp.DecodeString(list.getString("wr_name")));				// �۾���
								item.setWrDatetime(myApp.DecodeString(list.getString("wr_datetime")));		// �۾� ��¥
								item.setCommentCnt(list.getInt("comment_cnt"));			// �̸�
								item.setWrId(list.getInt("wr_id"));
								item.mb_grade = myApp.DecodeString(list.getString("mb_grade")); 
								if ( myApp.m_BBSID.bo_table.equals("hugi") )
								{
									item.setWr8( myApp.DecodeString(list.getString("wr_5")) );
									item.setWr6(list.getInt("wr_6"));			// ����
									item.setWr5(myApp.DecodeString(list.getString("sh_name")));
									item.setCaName(myApp.DecodeString(list.getString("c1_name")));

									item.isMainContent = false;
								}
								else
								{
									item.isMainContent = true;
								}
								

								
								
								m_ObjectArray.add(item);

							}
							handler.sendEmptyMessage(0);								
						}
						else
						{
							handler.sendEmptyMessage(3);
						}

					}
					else if(json.getString("result").equals("error"))
					{
						handler.sendMessage(handler.obtainMessage(2,myApp.DecodeString(json.getString("result_text")) ));
					}
				}
				catch (JSONException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
		});
		thread.start();
    }
    
    final Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			String message = null;
			mProgress.dismiss();
			switch(msg.what)
			{
			case 0:
			{
				m_Adapter.notifyDataSetChanged();
				// ������ ��ư�� ��Ȱ��ȭ ��Ų��. 
				if ( total_page > current_page )
				{
					Button btn = (Button)findViewById(R.id.bbs_main_list_more_123);
					btn.setVisibility(View.VISIBLE);
				}
				else
				{
					Button btn = (Button)findViewById(R.id.bbs_main_list_more_123);
					btn.setVisibility(View.GONE);				
				}
			}
				
				break;
			case 1:
				message = "No data";
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
				break;
			case 2:
				// ����ó�� 
				self.ShowAlertDialLog( self.getParent().getParent() ,"����" , (String) msg.obj );
				break;
			case 3:
				message = "�����Ͱ� �����ϴ�";
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
				break;
				
			case 10:
			{
				
		        Intent intent;
		        intent = new Intent().setClass(self, Shop_Detail_After_BBS.class);
		        startActivity( intent );  
			}
				break;
			default:
				//	message = "�����Ͽ����ϴ�.";

				break;
			}

		}
	};
    
    
    public class BBS_View_Adapter extends ArrayAdapter<BBS_Content_Object>
    {

    	private Context mContext;
    	private int mResource;
    	private ArrayList<BBS_Content_Object> mList;
    	private LayoutInflater mInflater;
    	
    	public BBS_View_Adapter(Context context, int layoutResource, 
    			ArrayList<BBS_Content_Object> mTweetList)
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
    		final BBS_Content_Object item = mList.get(position);
    		final MyInfo myApp = (MyInfo) getApplication();

    		if(convertView == null)
    		{
    			convertView = mInflater.inflate(mResource, null);
    		}

    		if(item != null) 
    		{
    			// �Էµ� ������ �������� Visible Gone ��Ų����
    			// �����͸� �Է����ش�
    			// �̹��� ������¡�� ����� �Ѵ�. 
    			  			
    			LinearLayout ParentTitle1  = (LinearLayout)convertView.findViewById(R.id.bbs_list_row_22132232);
    			LinearLayout ParentTitle2  = (LinearLayout)convertView.findViewById(R.id.bbs_list_row_22132);
    			// �Խù� ��� 1
    			LinearLayout mainTitle1  = (LinearLayout)convertView.findViewById(R.id.bbs_list_row_1);
    			// �Խù� ��� 2
    			LinearLayout mainContent1  = (LinearLayout)convertView.findViewById(R.id.bbs_list_row_2);
    			// �Խù� ���� 1
    			LinearLayout mainTitle2 = (LinearLayout)convertView.findViewById(R.id.bbs_list_row_3);


    			
 
    			// �Ϲ� �ؽ�Ʈ ��
    			if ( item.isMainContent) 
    			{

    				ParentTitle1.setVisibility(View.VISIBLE);
    				ParentTitle2.setVisibility(View.GONE);
    				
        			TextView text1 = (TextView)convertView.findViewById(R.id.bbs_list_row_title);
        			text1.setText(item.getWrSubject());
        			
        			TextView text2 = (TextView)convertView.findViewById(R.id.bbs_list_row_reply_count);
        			text2.setText("(" + item.getCommentCnt().toString() + ")");
        			
        			TextView text3 = (TextView)convertView.findViewById(R.id.bbs_title_writer_2);
        			TextView text4 = (TextView)convertView.findViewById(R.id.bbs_title_writer_6);
        			
        			ImageView grade = (ImageView)convertView.findViewById(R.id.bbs_title_writer_2_icon);
        			
        			if ( myApp.m_BBSID.bo_table.equals("notice") )
        			{
        				text3.setText(item.getWrDatetime());
        				grade.setVisibility(View.GONE);
        				text4.setVisibility(View.GONE);
        			}
        			else
        			{
        				text4.setText(item.getWrName());
        				text3.setText(item.getWrDatetime());
        				grade.setVisibility(View.VISIBLE);
        				text4.setVisibility(View.VISIBLE);
        			}
        			grade.setBackgroundResource(myApp.GetGradeIcon(item.mb_grade));
        			
        			// ���� State �Ѱ��ش�. 
        			LinearLayout frameBar = (LinearLayout)convertView.findViewById(R.id.bbs_main_list_row);
    				frameBar.setOnClickListener(new View.OnClickListener() 
    				{
    					public void onClick(View v) 
    					{
    						// ���� �Խ��� ��� �̵��Ѵ�. �Խ��� ���̵�� �� ��ȣ�� Application Ŭ������ �Ѱ��ش�. 
    						MyInfo myApp = (MyInfo) getApplication();
    						myApp.m_BBSViewerID.bo_table = myApp.m_BBSID.bo_table;
    						myApp.m_BBSViewerID.wr_id = item.getWrId().toString();
    						
    						Intent intent;
    		    	        intent = new Intent().setClass(self, BBS_Viewer.class);
    		    	        startActivity( intent );	
    					}
    				});

    			}
    			// ���� �� 
    			else
    			{
    				ParentTitle1.setVisibility(View.GONE);
    				ParentTitle2.setVisibility(View.VISIBLE);
    				

    				
        			TextView text1 = (TextView)convertView.findViewById(R.id.bbs_list_row_title2);
        			text1.setText(item.getWrSubject());
        			
        			TextView text2 = (TextView)convertView.findViewById(R.id.bbs_list_row_reply_count2);
        			text2.setText("(" + item.getCommentCnt().toString() + ")");
        			
        			TextView text3 = (TextView)convertView.findViewById(R.id.bbs_title_writer_4);
        			text3.setText(item.getWrDatetime());
        			
        			TextView text8 = (TextView)convertView.findViewById(R.id.bbs_title_writer_8);
        			text8.setText(item.getWrName());
        			
        			TextView text4 = (TextView)convertView.findViewById(R.id.bbs_title_writer_112);
        			text4.setText(item.getWr5() + "  /  " + item.getCaName() );
        			
        			
        			ImageView grade = (ImageView)convertView.findViewById(R.id.bbs_title_writer_4_icon);
        			grade.setBackgroundResource(myApp.GetGradeIcon(item.mb_grade));

  

        			// ���� State �Ѱ��ش�. 
        			LinearLayout frameBar = (LinearLayout)convertView.findViewById(R.id.bbs_main_list_row);
    				frameBar.setOnClickListener(new View.OnClickListener() 
    				{
    					public void onClick(View v) 
    					{
    						// ���� �Խ��� ��� �̵��Ѵ�. �Խ��� ���̵�� �� ��ȣ�� Application Ŭ������ �Ѱ��ش�. 
    						MyInfo myApp = (MyInfo) getApplication();
    						myApp.m_CurrShopInfo.ShopID = item.getWr8();
    						myApp.m_BBSViewerID.bo_table = "hugi";
    						myApp.m_BBSViewerID.wr_id = item.getWrId().toString();
    						

    				        
    						getIntroduce();
    						

    					}
    				});
    				
    			}
    			


    		}
    		return convertView;
    	}
    }
    
    
}
