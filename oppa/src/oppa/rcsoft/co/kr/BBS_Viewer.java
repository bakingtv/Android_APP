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


// Ŀ�´�Ƽ �Խù� ��� ( �ı�, �����Խ��� , �������� )
public class BBS_Viewer extends  BaseActivity implements OnClickListener
{
    /** Called when the activity is first created. */
	
	private ArrayList<BBS_Content_Object> m_ObjectArray;
	private BBS_View_Adapter m_Adapter;
	private ListView m_ListView;
	
	
	static public BBS_Viewer self;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bbs_viewer_content);
        
        
        self = this;
        
		mProgress = new ProgressDialog(this);
		mProgress.setMessage("��ø� ��ٷ� �ֽʽÿ�.");
		mProgress.setIndeterminate(true);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);
		
		m_ListView = (ListView)findViewById(R.id.bbs_main_list_view);

		m_ObjectArray = new ArrayList<BBS_Content_Object>();

		
		
		///////////////////////////////////////////////////////////////////////////////////////////////////
		// Tab
		{
			Button TabBTN2 = (Button)findViewById(R.id.bbs_main_viewer_tab_btn_1);
			TabBTN2.setOnClickListener(this);
			Button TabBTN3 = (Button)findViewById(R.id.bbs_main_viewer_tab_btn_2);
			TabBTN3.setOnClickListener(this);
			Button TabBTN4 = (Button)findViewById(R.id.bbs_main_viewer_tab_btn_3);
			TabBTN4.setOnClickListener(this);
			Button TabBTN5 = (Button)findViewById(R.id.bbs_main_viewer_tab_btn_5);
			TabBTN5.setOnClickListener(this);
		}
		
    	final MyInfo myApp = (MyInfo) getApplication();
    	if ( myApp.m_BBSID.bo_table.equals("hugi") )
    	{
    		m_Adapter = new BBS_View_Adapter(this, R.layout.bbs_main_ui_row, m_ObjectArray);
    	}
    	else if ( myApp.m_BBSID.bo_table.equals("free") )
    	{
    		m_Adapter = new BBS_View_Adapter(this, R.layout.bbs_main_ui_row2, m_ObjectArray);
    	}
    	else if ( myApp.m_BBSID.bo_table.equals("notice") )
    	{
    		m_Adapter = new BBS_View_Adapter(this, R.layout.bbs_main_ui_row, m_ObjectArray);
    	}
		
		
		RefreshUI();
    }
    
    
    public void onClick(View v )
    {
		
    	switch(v.getId())
    	{
	    	case R.id.bbs_main_viewer_tab_btn_1:
	    	{
	       		Intent intent;
		        intent = new Intent().setClass(self, Home.class);
		        startActivity( intent );  		
	    	}
	    		break;
	    	case R.id.bbs_main_viewer_tab_btn_2:
	    	{
	       		Intent intent;
		        intent = new Intent().setClass(self, Shop_MainList.class);
		        startActivity( intent );  		
	    	}
	    		break;
	    	case R.id.bbs_main_viewer_tab_btn_3:
	    	{
	       		Intent intent;
		        intent = new Intent().setClass(self, ToyMainList.class);
		        startActivity( intent );  		
	    	}
	    		break;
	    	case R.id.bbs_main_viewer_tab_btn_5:
	    	{
	       		Intent intent;
		        intent = new Intent().setClass(self, MyPage_Main.class);
		        startActivity( intent );  		
	    	}
	    		break;
    	}
    }
 
    // �Խù��� ������ ��ų� ������ ������ ������ �䱸 �ɶ�...
    @Override
    public void RefreshUI()
    {
    	m_ObjectArray.clear();
    	m_Adapter.clear();
    	mProgress.show();
    	final MyInfo myApp = (MyInfo) getApplication();
    	if ( myApp.m_BBSID.bo_table.equals("hugi") )
    	{
    		// ��� Ÿ��Ʋ�� �Էµ� UI���� �����ش�. 
    		ImageView title = (ImageView) findViewById(R.id.bbs_main_list_title);
    		title.setImageResource(R.drawable.n_4_title7);
    		title.setMaxWidth(210);
    	}
    	else if ( myApp.m_BBSID.bo_table.equals("free") )
    	{
    		ImageView title = (ImageView) findViewById(R.id.bbs_main_list_title);
    		title.setImageResource(R.drawable.n_4_title8);
    		title.setMaxWidth(180);
    	}
    	else if ( myApp.m_BBSID.bo_table.equals("notice") )
    	{
    		ImageView title = (ImageView) findViewById(R.id.bbs_main_list_title);
    		title.setImageResource(R.drawable.n_4_title2);
    		title.setMaxWidth(150);
    	}
    	
		
		m_ListView.setAdapter(m_Adapter);
    	
    	{
 
			Thread thread = new Thread(new Runnable()
			{
				public void run() 
				{
					Map<String, String> data = new HashMap  <String, String>();

					data.put("bo_table",myApp.m_BBSViewerID.bo_table );
					data.put("wr_id", myApp.m_BBSViewerID.wr_id);
					data.put("return_type", "json");

					
					String strJSON = myApp.GetHTTPGetData("http://oppa.rcsoft.co.kr/api/gnu_getBoardView.php", data);	
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
							// ���� ���� ����Ʈ�� �߰��Ѵ�. 
							int wr_id = 0;
							{
								BBS_Content_Object item = new BBS_Content_Object();
								item.isMainContent  =true;
								
								item.setWrSubject(myApp.DecodeString(json.getString("wr_subject")));
								item.setWrName(myApp.DecodeString(json.getString("wr_name")));
								item.setWrContent(myApp.DecodeString(json.getString("wr_content")));
								item.setWrDatetime(myApp.DecodeString(json.getString("wr_datetime")));
								item.setWrId(json.getInt("wr_id"));
								item.setWrHit(json.getInt("wr_hit"));
								item.setCommentCnt(json.getInt("comment_cnt"));
								item.mb_grade = myApp.DecodeString(json.getString("mb_grade")); 
								
								if ( myApp.m_BBSID.bo_table.equals("hugi"))
								{
									item.setWr6(json.getInt("wr_6"));
								}

								wr_id = item.getWrId();
								m_ObjectArray.add(item);
								
								
							}
							
							// ��� ����Ʈ�� �Է��Ѵ�.
							JSONArray usageList = (JSONArray)json.get("comment_list");
							for(int i = 0; i < usageList.length(); i++)
							{
								BBS_Content_Object item = new BBS_Content_Object();
								JSONObject list = (JSONObject)usageList.get(i);
								item.isMainContent  =false;
								
								item.setWrName(myApp.DecodeString(list.getString("cmt_name")));
								item.setWrContent(myApp.DecodeString(list.getString("cmt_content")));
								item.setWrDatetime(myApp.DecodeString(list.getString("cmt_datetime")));
								item.setWrId(wr_id);
								item.cmt_id = list.getInt("cmt_id");
								
								item.mb_grade = myApp.DecodeString(list.getString("cmt_mb_grade")); 
								
								// ��ۿ� ����� �޸��� �Ǵ��ϴ� ���� 
								if ( list.getInt("cmt_reply_len") > 0  )
								{
									item.isOverLapReply = true;
								}
								else
								{
									item.isOverLapReply = false;
								}
								m_ObjectArray.add(item);
							}
							handler.sendEmptyMessage(0);
							
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
			}
				m_Adapter.notifyDataSetChanged();
				break;
			case 1:
				message = "No data";
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
				break;
			case 2:
				break;
			case 3:
				message = "�����Ͱ� �����ϴ�";
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
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
    		final BBS_Content_Object tweet = mList.get(position);

    		if(convertView == null)
    		{
    			convertView = mInflater.inflate(mResource, null);
    		}

    		if(tweet != null) 
    		{
    			// �Էµ� ������ �������� Visible Gone ��Ų����
    			// �����͸� �Է����ش�
    			// �̹��� ������¡�� ����� �Ѵ�. 
    			
    			final MyInfo myApp = (MyInfo) getApplication();
    			
    			// �Խù� ��� 1
    			LinearLayout mainTitle1  = (LinearLayout)convertView.findViewById(R.id.bbs_main_header1);
    			// �Խù� ��� 2
    			LinearLayout mainTitle2  = (LinearLayout)convertView.findViewById(R.id.bbs_main_header2);
    			// �Խù� �̹���
    			LinearLayout mainImage = (LinearLayout)convertView.findViewById(R.id.bbs_main_image_layout);
    			// �Խù� �ؽ�Ʈ
    			LinearLayout mainContent  = (LinearLayout)convertView.findViewById(R.id.bbs_main_content_1);
    			// ��� ���
    			LinearLayout subContent  = (LinearLayout)convertView.findViewById(R.id.bbs_sub_title);

    			// Ÿ���� ���� Ÿ������ üũ�Ѵ�. 
    			if ( tweet.isMainContent )
    			{
    				// ���� �Խ��� Ÿ����?
    				if (  myApp.m_BBSID.bo_table.equals("notice") ||  myApp.m_BBSID.bo_table.equals("free"))
    	
    				{
        				mainTitle1.setVisibility(View.GONE);
        				mainTitle2.setVisibility(View.VISIBLE);
        				mainContent.setVisibility(View.VISIBLE);
        				subContent.setVisibility(View.GONE);
        				
        				// ��� �κ�
        				{
            				// ����
                			TextView itemTitle = (TextView)convertView.findViewById(R.id.bbs_main_content_title);
                			itemTitle.setText(tweet.getWrSubject());
                			
                			
                			// ���� ī��Ʈ 
                			TextView itemReply = (TextView)convertView.findViewById(R.id.bbs_main_content_reply_count);
                			itemReply.setText("("+tweet.getCommentCnt() + ")");
                			
                			// �ۼ� �ð�
                			TextView itemTime = (TextView)convertView.findViewById(R.id.bbs_title_writer_2);
                			
                			if (myApp.m_BBSID.bo_table.equals("notice") )
                			{
                				itemTime.setText( tweet.getWrName()+ " " + tweet.getWrDatetime());    
                			}
                			else
                			{
                				itemTime.setText( tweet.getWrName()+ " "+ tweet.getWrDatetime());    
                			}
                			
                			ImageView grade = (ImageView)convertView.findViewById(R.id.bbs_title_writer_2_icon);
                			grade.setBackgroundResource(myApp.GetGradeIcon(tweet.mb_grade));
                			    					
        				}
        				
        				// ���� ����
        				{
        					TextView itemContent = (TextView)convertView.findViewById(R.id.bbs_main_content_12);
        					itemContent.setText(tweet.getWrContent());   
        				}
        				
        				ImageView itemAddIcon = (ImageView)convertView.findViewById(R.id.bbs_main_reply_add);
    					itemAddIcon.setVisibility(View.GONE);
    					
        				ImageView itemReply = (ImageView)convertView.findViewById(R.id.bbs_main_title_add);
    					itemReply.setVisibility(View.VISIBLE);
            			// Ŭ���̺�Ʈ �߰�     					
    					itemReply.setOnClickListener(new OnClickListener()
    					{
    						public void onClick(View v )
    				        {
    				        	switch(v.getId())
    				        	{
    				        	case R.id.bbs_main_title_add:
    				        	{
    				        		myApp.m_BBSReplyContent.bo_table = myApp.m_BBSID.bo_table;
    								myApp.m_BBSReplyContent.wr_id = tweet.getWrId().toString();
    								myApp.m_BBSReplyContent.isReply = false;

    				           		Intent intent;
    				    	        intent = new Intent().setClass(self, BBS_Reply.class);
    				    	        startActivity( intent );	
    								
    				        	}
    				        		break;
    				        	}
    				        }
    					});
    					
    					{
        					ImageView itemAddSingo = (ImageView)convertView.findViewById(R.id.bbs_title_singo_2);
        					itemAddSingo.setVisibility(View.VISIBLE);
        					
        					// Ŭ���̺�Ʈ �߰�     					
        					itemAddSingo.setOnClickListener(new OnClickListener()
        					{
        						public void onClick(View v )
        				        {
        				        	switch(v.getId())
        				        	{
        				        	case R.id.bbs_title_singo_2:
        				        	{
        				        		myApp.m_BBSSingo.bo_table = myApp.m_BBSID.bo_table;
        								myApp.m_BBSSingo.wr_id = tweet.getWrId().toString();
        								myApp.m_BBSSingo.isReply = false;
        								
        								
        								Intent intent;
        				    	        intent = new Intent().setClass(self, BBS_Singo.class);
        				    	        startActivity( intent );	
        				        	}
        				        		break;
        				        	}
        				        }
        					});
    					}
        				

    				}
    				else if  (  myApp.m_BBSID.bo_table.equals("hugi") )
    				{
        				mainTitle1.setVisibility(View.VISIBLE);
        				mainTitle2.setVisibility(View.GONE);
        				subContent.setVisibility(View.GONE);
        				// �����Է�
        				{
            				ImageView star1 = (ImageView)convertView.findViewById(R.id.bbs_title_score1);
            				ImageView star2 = (ImageView)convertView.findViewById(R.id.bbs_title_score2);
            				ImageView star3 = (ImageView)convertView.findViewById(R.id.bbs_title_score3);
            				ImageView star4 = (ImageView)convertView.findViewById(R.id.bbs_title_score4);
            				ImageView star5 = (ImageView)convertView.findViewById(R.id.bbs_title_score5);
            	            				   				

            				switch( tweet.getWr6() )
            				{
            				case 1:
            					star1.setVisibility(0);
            					star2.setVisibility(4);
            					star3.setVisibility(4);
            					star4.setVisibility(4);
            					star5.setVisibility(4);
            					
            					break;
            				case 2:
            					star1.setVisibility(0);
            					star2.setVisibility(0);
            					star3.setVisibility(4);
            					star4.setVisibility(4);
            					star5.setVisibility(4);
            					break;
            				case 3:
            					star1.setVisibility(0);
            					star2.setVisibility(0);
            					star3.setVisibility(0);
            					star4.setVisibility(4);
            					star5.setVisibility(4);
            					break;
            				case 4:
            					star1.setVisibility(0);
            					star2.setVisibility(0);
            					star3.setVisibility(0);
            					star4.setVisibility(0);
            					star5.setVisibility(4);
            					break;
            				case 5:
            					star1.setVisibility(0);
            					star2.setVisibility(0);
            					star3.setVisibility(0);
            					star4.setVisibility(0);
            					star5.setVisibility(0);
            					break;
            				case 0:
            					star1.setVisibility(4);
            					star2.setVisibility(4);
            					star3.setVisibility(4);
            					star4.setVisibility(4);
            					star5.setVisibility(4);
            					break;
            				}
        				}
        				
        				// �ۼ� �ð�
            			TextView itemTime = (TextView)convertView.findViewById(R.id.bbs_title_writer_1);
            			itemTime.setText( tweet.getWrName());  
            			
            			// �ۼ���
            			TextView itemName = (TextView)convertView.findViewById(R.id.bbs_main_content_date_1);
            			itemName.setText( tweet.getWrDatetime());  
        				// ���� ����
        				{
        					TextView itemContent = (TextView)convertView.findViewById(R.id.bbs_main_content_12);
        					itemContent.setText(tweet.getWrContent());   
        				}
        				
            			ImageView grade = (ImageView)convertView.findViewById(R.id.bbs_title_writer_1_icon);
            			grade.setBackgroundResource(myApp.GetGradeIcon(tweet.mb_grade));
        				
        				
        				mainContent.setVisibility(View.VISIBLE);
        				mainImage.setVisibility(View.VISIBLE);
        				subContent.setVisibility(View.GONE);
        				
     
        				
        				ImageView itemReply = (ImageView)convertView.findViewById(R.id.bbs_main_title_add);
    					itemReply.setVisibility(View.GONE);
        				ImageView itemAddIcon = (ImageView)convertView.findViewById(R.id.bbs_main_reply_add);
    					itemAddIcon.setVisibility(View.VISIBLE);
    					
    					// Ŭ���̺�Ʈ �߰�     					
    					itemAddIcon.setOnClickListener(new OnClickListener()
    					{
    						public void onClick(View v )
    				        {
    				        	switch(v.getId())
    				        	{
    				        	case R.id.bbs_main_reply_add:
    				        	{
    				        		myApp.m_BBSReplyContent.bo_table = myApp.m_BBSID.bo_table;
    								myApp.m_BBSReplyContent.wr_id = tweet.getWrId().toString();
    								myApp.m_BBSReplyContent.isReply = false;
    								
    								
    				           		Intent intent;
    				    	        intent = new Intent().setClass(self, BBS_Reply.class);
    				    	        startActivity( intent );	
    								
    				        	}
    				        		break;
    				        	}
    				        }
    					});
    					
    					{
        					ImageView itemAddSingo = (ImageView)convertView.findViewById(R.id.bbs_title_singo_1);
        					itemAddSingo.setVisibility(View.VISIBLE);
        					
        					// Ŭ���̺�Ʈ �߰�     					
        					itemAddSingo.setOnClickListener(new OnClickListener()
        					{
        						public void onClick(View v )
        				        {
        				        	switch(v.getId())
        				        	{
        				        	case R.id.bbs_title_singo_1:
        				        	{
        				        		myApp.m_BBSSingo.bo_table = myApp.m_BBSID.bo_table;
        								myApp.m_BBSSingo.wr_id = tweet.getWrId().toString();
        								myApp.m_BBSSingo.isReply = false;
        								
        								
        								Intent intent;
        				    	        intent = new Intent().setClass(self, BBS_Singo.class);
        				    	        startActivity( intent );	
        				        	}
        				        		break;
        				        	}
        				        }
        					});
    					}
    					
    				}
    				
    			}
    			else 
    			{
    				mainTitle1.setVisibility(View.GONE);
    				mainTitle2.setVisibility(View.GONE);
    				mainImage.setVisibility(View.GONE);
    				mainContent.setVisibility(View.GONE);
    				subContent.setVisibility(View.VISIBLE);
 
    				ImageView itemAddIcon = (ImageView)convertView.findViewById(R.id.bbs_sub_reply_add);
    				// ��ۿ� ��� ������ 
    				if ( tweet.isOverLapReply)
    				{
    					ImageView replIcon = (ImageView)convertView.findViewById(R.id.bbs_sub_reply_icon);
    					replIcon.setVisibility(View.VISIBLE);
    					itemAddIcon.setVisibility(View.GONE);
    				}
    				else
    				{
    					ImageView replIcon = (ImageView)convertView.findViewById(R.id.bbs_sub_reply_icon);
    					replIcon.setVisibility(View.GONE);
    					
    					itemAddIcon.setVisibility(View.VISIBLE);
    				}
    				
    				
    				// �ۼ� �ð�
        			TextView itemTime = (TextView)convertView.findViewById(R.id.bbs_main_content_date);
        			itemTime.setText(  tweet.getWrDatetime());  
        			
        			// �ۼ���
        			TextView itemName = (TextView)convertView.findViewById(R.id.bbs_main_content_writer);
        			itemName.setText( tweet.getWrName());  
        			
        			ImageView grade = (ImageView)convertView.findViewById(R.id.bbs_main_content_writer_icon);
        			grade.setBackgroundResource(myApp.GetGradeIcon(tweet.mb_grade));
    				
    				// ���� ����
    				{
    					TextView itemContent = (TextView)convertView.findViewById(R.id.bbs_main_content_title123);
    					itemContent.setText(tweet.getWrContent());   
    				}
    				
    				

					

					
    				ImageView itemReply = (ImageView)convertView.findViewById(R.id.bbs_main_title_add);
					itemReply.setVisibility(View.GONE);
					
					
					// Ŭ���̺�Ʈ �߰�     					
					itemAddIcon.setOnClickListener(new OnClickListener()
					{
						public void onClick(View v )
				        {
				        	switch(v.getId())
				        	{
				        	case R.id.bbs_sub_reply_add:
				        	{
				        		myApp.m_BBSReplyContent.bo_table = myApp.m_BBSID.bo_table;
								myApp.m_BBSReplyContent.wr_id = tweet.getWrId().toString();
								myApp.m_BBSReplyContent.isReply = true;
								myApp.m_BBSReplyContent.re_id = tweet.cmt_id.toString();
								
				           		Intent intent;
				    	        intent = new Intent().setClass(self, BBS_Reply.class);
				    	        startActivity( intent );
								
				        	}
				        		break;
				        	}
				        }
					});
					
    			}
    		}
    		return convertView;
    	}
    }
}
