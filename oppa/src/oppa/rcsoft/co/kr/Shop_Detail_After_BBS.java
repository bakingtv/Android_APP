package oppa.rcsoft.co.kr;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import oppa.rcsoft.co.kr.Shop_MainFinderList.MoreBTNClick;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;



// Shop ���� �ı� �Խù� Ŭ����

public class Shop_Detail_After_BBS extends BaseActivity  
{
	
	String m_ShopID;
	String m_TelNumber = "01012341234";
	String m_Title;
	String m_Addr;
	String m_Score;
	String m_C1Name;
	String m_C2Name;
	Bitmap m_Bitmap;
	int m_Distance;
	int m_evlCount = 0;
	int m_ZZimeCount = 0;
	

	ViewFlipper flipper;   
	
	Boolean 		m_bFirstPage;
	
	public static Shop_Detail_After_BBS self;
	
	// �Խù�
    private ArrayList<BBS_Content_Object> m_MoreListObject;
	private Shop_Detail_After_More_List_Adapter m_MoreListAdapter;
	private ListView m_MoreListView;
	
	
	
	
	// FirstPage �� �Խ��� �ƴҰ�찡 �Խù� ����
	public void setFirstPage( Boolean bPage )
	{
		m_bFirstPage = bPage;
	}
    @Override  
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_bbs_viewer_content);   
        
        self = this;
        
        mProgress = new ProgressDialog(this);
		mProgress.setMessage("��ø� ��ٷ� �ֽʽÿ�.");
		mProgress.setIndeterminate(true);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);

        m_bFirstPage = true;
        ChangeUI();
    	{
			Button MainBtn1 = (Button)findViewById(R.id.bbs_main_viewer_tab_btn_1);
	        MainBtn1.setOnClickListener(new MoreBTNClick());
	        Button MainBtn2 = (Button)findViewById(R.id.bbs_main_viewer_tab_btn_3);
	        MainBtn2.setOnClickListener(new MoreBTNClick());
	        Button MainBtn3 = (Button)findViewById(R.id.bbs_main_viewer_tab_btn_4);
	        MainBtn3.setOnClickListener(new MoreBTNClick());
	        Button MainBtn4 = (Button)findViewById(R.id.bbs_main_viewer_tab_btn_5);
	        MainBtn4.setOnClickListener(new MoreBTNClick());
	    }
		{

	        ImageButton MainBtn2 = (ImageButton)findViewById(R.id.shop_detail_tab_btn_1);
	        MainBtn2.setOnClickListener(new MoreBTNClick());
	        ImageButton MainBtn3 = (ImageButton)findViewById(R.id.shop_detail_tab_btn_2);
	        MainBtn3.setOnClickListener(new MoreBTNClick());
	        ImageButton MainBtn4 = (ImageButton)findViewById(R.id.shop_detail_tab_btn_4);
	        MainBtn4.setOnClickListener(new MoreBTNClick());
	    }
		
    	RefreshIntroduce();
        RefreshUI();
    }
    

    public void RefreshIntroduce( )
    {
		mProgress.show();
		JSONObject json = null;
		MyInfo myApp = (MyInfo) getApplication();
		String strJSON = myApp.m_CurrShopInfo.IntroMessage;
		try 
		{
			json = new JSONObject(strJSON);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try 
		{
			

			if(json.getString("result").equals("ok"))
			{

				{
					m_ShopID = myApp.DecodeString(json.getString("sh_id"));
					myApp.m_CurrShopInfo.ShopID = m_ShopID;
					m_Title = myApp.DecodeString(json.getString("sh_name"));
					
					
					
					m_TelNumber = myApp.DecodeString(json.getString("sh_tel"));

					m_Addr= myApp.DecodeString(json.getString("sh_addr"));
					m_Score = myApp.DecodeString(json.getString("sh_eval_point"));
					
					
					m_C1Name = myApp.DecodeString(json.getString("c1_name"));
					m_C2Name = myApp.DecodeString(json.getString("c2_name"));
								
					m_evlCount = json.getInt("sh_eval_cnt");
					m_ZZimeCount = json.getInt("hugi_cnt");
					
					String temp = "";
					String temp2 = json.getString("sh_img");
					
					if ( !temp2.equals(temp) )
					{
						URL imgUrl = new URL(myApp.DecodeString(json.getString("sh_img")));
						URLConnection conn = imgUrl.openConnection();
						conn.connect();
						BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(),512 * 1024);
						m_Bitmap = BitmapFactory.decodeStream(bis);
						bis.close();
					}
					
					m_Distance = json.getInt("distance");			
					
					handler.sendEmptyMessage(20);	
				}

			}
			else if(json.getString("result").equals("error"))
			{
				mProgress.dismiss();
				//self.ShowAlertDialLog( self.getParent() ,"����" , (String) msg.obj );
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
    
    
    public  class MoreBTNClick implements OnClickListener
    {

    	MyInfo myApp = (MyInfo) getApplication();
		public void onClick(View v )
        {
        	switch(v.getId())
        	{
        	
    		
	    	case R.id.shop_detail_tab_btn_2:
	    	{
	    		Intent intent;
	    	    intent = new Intent().setClass(self, Shop_Detail_QNA.class);
	    	    startActivity( intent );   
	    	}
	    		break;
	    		
	    	case R.id.shop_detail_tab_btn_1:
	    	{
	    		Intent intent;
	    	    intent = new Intent().setClass(self, Shop_Detail_Introduce.class);
	    	    startActivity( intent );  
	    	}
	    		break;
	    		
	    	case R.id.shop_detail_tab_btn_4:
	    	{
	    		Intent intent;
	    	    intent = new Intent().setClass(self, Shop_Detail_News.class);
	    	    startActivity( intent );  
	    	}
	    		break;
    		
        	case R.id.bbs_main_viewer_tab_btn_1:
        	{
        		Intent intent;
    	        intent = new Intent().setClass(self, Home.class);
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
        		
        	case R.id.bbs_main_viewer_tab_btn_4:
        	{
        		Intent intent;
    	        intent = new Intent().setClass(self, Community_Main.class);
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
    }
    
    // ����Ʈ�� �����Ѵ�. 
    @Override
    public void RefreshUI()
    {
    	
    	geteHugiContent();

    }
    
 
    
    
    // �Խù��� �����´�
    void geteHugiContent()
    {
    	m_MoreListObject.clear();
    	m_MoreListAdapter.clear();
    	mProgress.show();
    	final MyInfo myApp = (MyInfo) getApplication();
    	{
    		 
			Thread thread = new Thread(new Runnable()
			{
				public void run() 
				{
					Map<String, String> data = new HashMap  <String, String>();

					data.put("wr_id", myApp.m_BBSViewerID.wr_id);
					data.put("sh_id",myApp.m_CurrShopInfo.ShopID );
					data.put("return_type", "json");
					data.put("bo_table", "hugi");
					data.put("shop", "shop");

					
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
								
								item.setWrSubject(myApp.DecodeString(json.getString("wr_content")));
								item.setWrName(myApp.DecodeString(json.getString("wr_name")));
								item.setWrContent(myApp.DecodeString(json.getString("wr_content")));
								item.setWrDatetime(myApp.DecodeString(json.getString("wr_datetime")));
								item.setWrId(json.getInt("wr_id"));
								item.setWrHit(json.getInt("wr_hit"));
								item.setCommentCnt(json.getInt("comment_cnt"));
								item.setWr6(json.getInt("wr_6"));
								item.BBStype ="hugi";
								item.mb_grade = myApp.DecodeString(json.getString("mb_grade")); 

								wr_id = item.getWrId();
								m_MoreListObject.add(item);
								
								
							}

							{
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
									
									item.BBStype ="hugi";
									
									// ��ۿ� ����� �޸��� �Ǵ��ϴ� ���� 
									if ( list.getInt("cmt_reply_len") > 0  )
									{
										item.isOverLapReply = true;
									}
									else
									{
										item.isOverLapReply = false;
									}
									m_MoreListObject.add(item);
								}
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
   
    public void ChangeUI()
    {

		ImageView title = (ImageView) findViewById(R.id.bbs_main_list_title);
		title.setImageResource(R.drawable.shop_info_detail_title);
		title.setMaxWidth(150);
		
       	m_MoreListView = (ListView)findViewById(R.id.bbs_main_list_view);
    	m_MoreListObject = new ArrayList<BBS_Content_Object>();
    	m_MoreListAdapter = new Shop_Detail_After_More_List_Adapter(this, R.layout.bbs_main_ui_row, m_MoreListObject);
    		
    	m_MoreListView.setAdapter(m_MoreListAdapter);

    }
	
	// �ٸ� ������� ����ȭ�� ���� �ڵ鷯 ó�� �۾� 
	final Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
			case 0:
				mProgress.dismiss();
				m_MoreListAdapter.notifyDataSetChanged();
				break;
			case 1:
				break;
			case 2:
				break;
			case 4:
				//������ ���� 
				mProgress.dismiss();
				break;
			case 5:
				//�ı� ��� 
				mProgress.dismiss();
				break;
				
			case 20:
			{
				TextView titleText = (TextView)findViewById(R.id.shop_detail_main_title);
				TextView addrText = (TextView)findViewById(R.id.shop_detail_main_addr);
				TextView scoreText = (TextView)findViewById(R.id.shop_detail_main_score);
				TextView itemDist = (TextView)findViewById(R.id.shop_detail_main_distance);
				ImageView mainImage = (ImageView)findViewById(R.id.shop_detail_main_image);
				TextView itemTel = (TextView)findViewById(R.id.shop_detail_main_tel);
				TextView itemZZim = (TextView)findViewById(R.id.shop_detail_main_zzimcounter);
				TextView itemCate = (TextView)findViewById(R.id.shop_detail_main_cate);
				
				
				
				
				mainImage.setImageBitmap(m_Bitmap);
				titleText.setText(m_Title);
				addrText.setText(m_Addr);
				
				
				final MyInfo myApp = (MyInfo) getApplication();
				if (myApp.mLat == -100000.0 )
				{
					itemDist.setText("�Ÿ������Ұ�");
				}
				else
				{
					float dis = ((float)(m_Distance )) /1000.0f;
					itemDist.setText("�Ÿ� : "+dis + "km");
				}
				
				itemTel.setText("��ȭ : " + m_TelNumber );
				itemZZim.setText("("+m_ZZimeCount+")");
				itemCate.setText( m_C2Name );
				
				
				
				
				{
					ImageView star1 = (ImageView)findViewById(R.id.shop_detail_main_score1);
					ImageView star2 = (ImageView)findViewById(R.id.shop_detail_main_score2);
					ImageView star3 = (ImageView)findViewById(R.id.shop_detail_main_score3);
					ImageView star4 = (ImageView)findViewById(R.id.shop_detail_main_score4);
					ImageView star5 = (ImageView)findViewById(R.id.shop_detail_main_score5);
					switch( Integer.parseInt(m_Score) )
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
				scoreText.setText("���� : ");
			}
				RefreshUI();
				break;
			default:
				//	message = "�����Ͽ����ϴ�."
				mProgress.dismiss();
				self.ShowAlertDialLog( self,"����" , (String) msg.obj );
				break;
			}
		}
	};
	
    
    // �Խù� ��� Ŭ���� 
	public class Shop_Detail_After_More_List_Adapter extends ArrayAdapter<BBS_Content_Object>
	{

		private Context mContext;
		private int mResource;
		private ArrayList<BBS_Content_Object> mList;
		private LayoutInflater mInflater;
		
		public Shop_Detail_After_More_List_Adapter(Context context, int layoutResource, 
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
    		final MyInfo myApp = (MyInfo) getApplication();

    		if(convertView == null)
    		{
    			convertView = mInflater.inflate(mResource, null);
    		}

    		if(tweet != null) 
    		{
    			// �Էµ� ������ �������� Visible Gone ��Ų����
    			// �����͸� �Է����ش�
    			// �̹��� ������¡�� ����� �Ѵ�. 
    			
    			


    			// Ÿ���� ���� Ÿ������ üũ�Ѵ�. 
    			if ( tweet.isMainContent )
    			{
    			
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


        			
    				{
        				mainTitle1.setVisibility(View.VISIBLE);
        				mainTitle2.setVisibility(View.GONE);
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
            			
            			ImageView grade = (ImageView)convertView.findViewById(R.id.bbs_title_writer_1_icon);
            			grade.setBackgroundResource(myApp.GetGradeIcon(tweet.mb_grade));
            			
        				// ���� ����
        				{
        					TextView itemContent = (TextView)convertView.findViewById(R.id.bbs_main_content_12);
        					itemContent.setText(tweet.getWrContent());   
        				}
     
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
    				        		myApp.m_BBSReplyContent.bo_table = "hugi";
    								myApp.m_BBSReplyContent.wr_id = tweet.getWrId().toString();
    								myApp.m_BBSReplyContent.isReply = false;
    								myApp.m_BBSReplyContent.isWriteType =true;
    								
    						        Intent intent;
    						        intent = new Intent().setClass(self, Shop_Detail_After_Write.class);
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
        				        		myApp.m_BBSSingo.bo_table = "hugi";
        								myApp.m_BBSSingo.wr_id = tweet.getWrId().toString();
        								myApp.m_BBSSingo.isReply = false;
        								
        								
        								Intent intent;
        						        intent = new Intent().setClass(self, Shop_Detail_Singo.class);
        						        startActivity( intent );  
        								

        								
        				        	}
        				        		break;
        				        	}
        				        }
        					});
    					}
        				
        				
    				}
    				mainContent.setVisibility(View.VISIBLE);
    				mainImage.setVisibility(View.VISIBLE);
    				subContent.setVisibility(View.GONE);
    				
    				{
    					
    				}
    			}
    			else 
    			{
    				
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

    				mainTitle1.setVisibility(View.GONE);
    				mainTitle2.setVisibility(View.GONE);
    				mainImage.setVisibility(View.GONE);
    				mainContent.setVisibility(View.GONE);
			
    				// ��ۿ� ��� ������ 
    				if ( tweet.isOverLapReply)
    				{
    					ImageView replIcon = (ImageView)convertView.findViewById(R.id.bbs_sub_reply_icon);
    					replIcon.setVisibility(View.VISIBLE);
    				}
    				else
    				{
    					ImageView replIcon = (ImageView)convertView.findViewById(R.id.bbs_sub_reply_icon);
    					replIcon.setVisibility(View.GONE);
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
    				
    				
    				// ��ۿ� ��� ���޵��� ����
    				if (tweet.isOverLapReply )
    				{
    					ImageView itemAddIcon = (ImageView)convertView.findViewById(R.id.bbs_sub_reply_add);
    					itemAddIcon.setVisibility(View.GONE);
    				}
    				else
    				{
    					ImageView itemAddIcon = (ImageView)convertView.findViewById(R.id.bbs_sub_reply_add);
    					itemAddIcon.setVisibility(View.VISIBLE);
    					
    					
    					// Ŭ���̺�Ʈ �߰�     					
    					itemAddIcon.setOnClickListener(new OnClickListener()
    					{
    						public void onClick(View v )
    				        {
    				        	switch(v.getId())
    				        	{
    				        	case R.id.bbs_sub_reply_add:
    				        	{
    				        		myApp.m_BBSReplyContent.bo_table = "hugi";
    								myApp.m_BBSReplyContent.wr_id = tweet.getWrId().toString();
    								myApp.m_BBSReplyContent.isReply = true;
    								myApp.m_BBSReplyContent.re_id = tweet.cmt_id.toString();
    								myApp.m_BBSReplyContent.isWriteType =true;
    								
    								
    						        Intent intent;
    						        intent = new Intent().setClass(self, Shop_Detail_After_Write.class);
    						        startActivity( intent );  

    								
    				        	}
    				        		break;
    				        	}
    				        }
    					});
    					
    				}
    				
    				subContent.setVisibility(View.VISIBLE);

    			}
    		}
    		return convertView;
    	}
	}
    
}
