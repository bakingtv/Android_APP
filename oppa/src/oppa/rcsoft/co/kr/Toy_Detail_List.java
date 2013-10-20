package oppa.rcsoft.co.kr;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



import oppa.rcsoft.co.kr.MyInfo.MyAccountData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;





import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


// ��� �Խù� �����ֱ�
public class Toy_Detail_List extends BaseActivity implements OnClickListener 
{
    private ArrayList<BBS_Content_Object> m_ObjectArray;
	private Toy_Detail_List_Adapter m_Adapter;
	private ListView m_ListView;
	
	static Toy_Detail_List self;

	int total_count;
	int current_count;
	
	int total_page;

	Integer current_page = 1;
	String currCategory;
	
	Button imgBtnSeeMore;
		
	

    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.toy_detail_list);          // ��Ʈ�� ���̾ƿ� ���  
        self = this;
        
		mProgress = new ProgressDialog(this);
		mProgress.setMessage("��ø� ��ٷ� �ֽʽÿ�.");
		mProgress.setIndeterminate(true);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);
		
		
		m_ListView = (ListView)findViewById(R.id.toy_detail_page_listview);

		m_ObjectArray = new ArrayList<BBS_Content_Object>();
		m_Adapter = new Toy_Detail_List_Adapter(this, R.layout.toy_detail_list_view_row, m_ObjectArray);
		m_ListView.setAdapter(m_Adapter);
		
        
        {
        	Button TabBTN2 = (Button)findViewById(R.id.toy_detail_tab_btn_2);
			TabBTN2.setOnClickListener(this);
			
	        Button CallBtn = (Button)findViewById(R.id.toy_bbs_call_btn);
	        CallBtn.setOnClickListener(this);
        }
        
        {
			Button TabBTN2 = (Button)findViewById(R.id.toy_main_tab_btn_1);
			TabBTN2.setOnClickListener(this);
			Button TabBTN3 = (Button)findViewById(R.id.toy_main_tab_btn_2);
			TabBTN3.setOnClickListener(this);
			Button TabBTN4 = (Button)findViewById(R.id.toy_main_tab_btn_4);
			TabBTN4.setOnClickListener(this);
			Button TabBTN5 = (Button)findViewById(R.id.toy_main_tab_btn_5);
			TabBTN5.setOnClickListener(this);
		}
        
        
        
        {
            MyInfo myApp = (MyInfo) getApplication();
            MyAccountData Mydata = myApp.GetAccountData();
            
            if ( Mydata.Level == 2 )
            {
                Button ZZimBtn = (Button)findViewById(R.id.toy_detail_title_bar_zzim);
                ZZimBtn.setOnClickListener(this);
                Button WriteBtn = (Button)findViewById(R.id.toy_main_write);
            	WriteBtn.setVisibility(View.GONE);
            }
            else if ( Mydata.Level == 3 )
            {
            	Button ZZimBtn = (Button)findViewById(R.id.toy_detail_title_bar_zzim);
            	ZZimBtn.setVisibility(View.GONE);
            	
            	Button WriteBtn = (Button)findViewById(R.id.toy_main_write);
            	WriteBtn.setOnClickListener(this);
            	
            }
            else
            {
            	Button ZZimBtn = (Button)findViewById(R.id.toy_detail_title_bar_zzim);
            	ZZimBtn.setVisibility(View.GONE);
            	
            	Button WriteBtn = (Button)findViewById(R.id.toy_main_write);
            	WriteBtn.setVisibility(View.GONE);
            }
	
        }

        imgBtnSeeMore= (Button)findViewById(R.id.toy_main_list_more_123);
		imgBtnSeeMore.setOnClickListener(this);
		
		RefreshUI();
    }
    
	private void ZzimOneButton()
	{
		mProgress.show();
		final MyInfo myApp = (MyInfo) getApplication();
		Thread thread = new Thread(new Runnable()
		{
			public void run()
			{
				Map<String, String> data = new HashMap  <String, String>();
				data.put("gr_id",myApp.m_CurrGirlInfo.GirlID );
				data.put("return_type", "json");
				
				String strJSON = myApp.GetHTTPGetData("http://oppa.rcsoft.co.kr/api/oppa_favoriteGirl.php", data);

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
						handler.sendEmptyMessage(8);
					}
					else if(json.getString("result").equals("error"))
					{
						handler.sendMessage(handler.obtainMessage(10,myApp.DecodeString(json.getString("result_text")) ));
					}
				}  catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 

			}
		});
		thread.start();
	}
	
    public void onBackPressed() 
    {
		
        Intent intent;
        intent = new Intent().setClass(self, ToyMainList.class);
        startActivity( intent );  
    }

	public void onClick(View v) 
	{
		switch(v.getId())
    	{
    	case R.id.toy_main_list_content_more:
    		MoreRefreshUI();
    		break;
    		
    	case R.id.toy_bbs_call_btn:
    	{
    		MyInfo myApp = (MyInfo) getApplication();
    		
    		Intent intent = new Intent(Intent.ACTION_DIAL);
    		intent.setData(Uri.parse("tel:" + myApp.m_SisterInfo.m_TelNumber));
    		startActivity(intent);
    		break;
    	}
    	
    	case R.id.toy_main_write:
    	{
    		// �۾���
    		Intent intent;
	        intent = new Intent().setClass(self, Toy_Write.class);
	        startActivity( intent ); 
    	}
    		break;
    		
    	case R.id.toy_detail_title_bar_zzim:
    	{
    		ZzimOneButton();
    	}
    		break;
    		
    	case R.id.toy_detail_tab_btn_2:
    	{
    		Intent intent;
	        intent = new Intent().setClass(self, Toy_Detail_Photo.class);
	        startActivity( intent );  
    	}
    		
    		break;
    		
    	case R.id.toy_main_tab_btn_1:
    	{
    		Intent intent;
	        intent = new Intent().setClass(self, Home.class);
	        startActivity( intent );  
    	}
    		break;
    		
    	case R.id.toy_main_tab_btn_2:
    	{
    		Intent intent;
	        intent = new Intent().setClass(self, Shop_MainList.class);
	        startActivity( intent );   
    	}
    		break;
    		
    	case R.id.toy_main_tab_btn_4:
    	{
    		Intent intent;
	        intent = new Intent().setClass(self, Community_Main.class);
	        startActivity( intent );  
    	}
    		break;
    		
    	case R.id.toy_main_tab_btn_5:
    	{
    		Intent intent;
	        intent = new Intent().setClass(self, MyPage_Main.class);
	        startActivity( intent );  
    	}
    		break;

			
    	}
	}
    
    
    
	
	
	
	@Override
	public void RefreshUI()
	{
		
		
		{
			final MyInfo myApp = (MyInfo) getApplication();
			TextView titleText = (TextView)findViewById(R.id.toy_bbs_name);
			TextView addrText = (TextView)findViewById(R.id.toy_bbs_addr);
			TextView scoreText = (TextView)findViewById(R.id.toy_bbs_score);
			//TextView charText = (TextView)findViewById(R.id.toy_detail_main_char);
			ImageView mainImage = (ImageView)findViewById(R.id.toy_bbs_image_main);
			TextView itemTel = (TextView)findViewById(R.id.toy_bbs_tel);
			TextView itemShopName = (TextView)findViewById(R.id.toy_bbs_shop_name);
			mainImage.setImageBitmap(myApp.m_SisterInfo.m_Bitmap);
			titleText.setText("�̸� : "+ myApp.m_SisterInfo.m_Title + "( " + myApp.m_SisterInfo.m_Ki + "cm / "+ myApp.m_SisterInfo.m_Weight + "kg )\n"
					 + "���� : " + myApp.m_SisterInfo.m_hams );
			addrText.setText("�ּ� : " + myApp.m_SisterInfo.m_Addr);
			itemTel.setText("��ȭ : " + myApp.m_SisterInfo.m_TelNumber );
			
			

			if (myApp.mLat == -100000.0 )
			{
				scoreText.setText("��Ƚ�� : " + myApp.m_SisterInfo.m_Score + "ȸ"   );
			}
			else
			{
				float dis = ((float)(myApp.m_SisterInfo.m_Distance )) /1000.0f;
				scoreText.setText("��Ƚ�� : " + myApp.m_SisterInfo.m_Score + "ȸ" ) ;
			}
			
			
			
			//charText.setText("���� : " + myApp.m_SisterInfo.m_hams );
			
			itemShopName.setText( "�Ҽ� : " + myApp.m_SisterInfo.m_ShopName);
		}
		m_ObjectArray.clear();
		m_Adapter.notifyDataSetChanged();
		current_page = 1;
		GetBBSList();
	}
	    
	public void MoreRefreshUI()
	{
	   	current_page++;
	   	GetBBSList();
	}
	
	void GetBBSList()
	{
		mProgress.show();
		final MyInfo myApp = (MyInfo) getApplication();
		{
			final String strPage = current_page.toString();
			Thread thread = new Thread(new Runnable()
			{
				public void run() 
				{
					Map<String, String> data = new HashMap  <String, String>();

					data.put("bo_table","play" );
					data.put("page", strPage);
					data.put("sfl","mb_id");
					data.put("stx",myApp.m_CurrGirlInfo.GirlID);
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
									
									item.setWrSubject(myApp.DecodeString(list.getString("wr_content")));		// ����
									item.setWrName(myApp.DecodeString(list.getString("wr_name")));				// �۾���
									item.setWrDatetime(myApp.DecodeString(list.getString("wr_datetime")));		// �۾� ��¥
									item.setReplyLen(list.getInt("comment_cnt"));			// �̸�
									item.setWrId(list.getInt("wr_id"));
									
									item.setWrHit(list.getInt("wr_hit"));
									
									String fileDir = myApp.DecodeString(list.getString("file_dir"));
									JSONArray bitmaplist = (JSONArray)list.get("file_list");
									ArrayList<Bitmap> bitmapArray = null;
									for(int j = 0; j < bitmaplist.length(); j++)
									{
										JSONObject bitmaplist1 = (JSONObject)bitmaplist.get(j);
										bitmapArray = new ArrayList<Bitmap>();
										

										URL imgUrl = new URL(fileDir+ "/" + myApp.DecodeString(bitmaplist1.getString("files") ));
										URLConnection conn = imgUrl.openConnection();
										conn.connect();
										BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(),512 * 1024);
										Bitmap bm = BitmapFactory.decodeStream(bis);
										bis.close();

										bitmapArray.add(bm);
										
									}
									if ( bitmapArray != null)
										item.setBitmap(bitmapArray);
									
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
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
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
				// ������ ��ư...
				if(current_page < total_page)
				{
					imgBtnSeeMore.setVisibility(View.VISIBLE);
				}
				else 
				{
					imgBtnSeeMore.setVisibility(View.GONE);
				}
				m_Adapter.notifyDataSetChanged();
				break;
			case 1:
				message = "No data";
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
				break;
			case 2:
				// ����ó�� 
				self.ShowAlertDialLog( self ,"����" , (String) msg.obj );
				break;
			case 8 :
				mProgress.dismiss();
				self.ShowAlertDialLog( self, "�� �Ϸ�", "���� �Ϸ� �Ǿ����ϴ�." );
				break;	
			default:
  				mProgress.dismiss();
  				self.ShowAlertDialLog( self ,"����" , (String) msg.obj );
				break;
			}
		}
	};
		
			
	
	public class Toy_Detail_List_Adapter extends ArrayAdapter<BBS_Content_Object>
	{

	  	private Context mContext;
	   	private int mResource;
	   	private ArrayList<BBS_Content_Object> mList;
	   	private LayoutInflater mInflater;
	    	
	   	public Toy_Detail_List_Adapter(Context context, int layoutResource, 
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
				ImageView itemPic = (ImageView)convertView.findViewById(R.id.top_detail_sample_image);
				TextView itemName = (TextView)convertView.findViewById(R.id.toy_detail_list_name);
				TextView itemContents = (TextView)convertView.findViewById(R.id.toy_detail_list_comment);
				TextView itemUpdate = (TextView)convertView.findViewById(R.id.toy_detail_list_date123);
				//�߰����� ������ �߰��� ���� ���� ������ָ� ��. �� ���������� ���� xml������ �������־�� ��.
					
				FrameLayout frameBar = (FrameLayout)convertView.findViewById(R.id.toy_detail_list_view_row123);
					
					//
				if ( tweet.getBitmap() != null)
				{
					itemPic.setImageBitmap(tweet.getBitmap().get(0));
				}
				else
				{
					itemPic.setImageResource(R.drawable.top_detail_sample_image);
				}
					
				itemName.setText(tweet.getWrName()  );
				itemContents.setText(tweet.getWrSubject());
				itemUpdate.setText("���  :  " + tweet.getReplyLen().toString()+"   ��ȸ  :  " + tweet.getWrHit() + "   �ð� :  " +  tweet.getWrDatetime());
				//itemUpdate.setText("��� : " + tweet.getCommentCnt().toString() +"   ��ȸ  :  " + tweet.getWrHit() + "   �ð� :  " +  tweet.getWrDatetime());
					
				// ���� State �Ѱ��ش�. 
				frameBar.setOnClickListener(new View.OnClickListener() 
				{
					
						
					public void onClick(View v) 
					{
						// ���� ��Ƽ��Ƽ�� �̵��ϰ� ������ �Խù��� ��ȣ�� �����Ѵ�. 
						myApp.m_BBSViewerID.wr_id = tweet.getWrId().toString();
						myApp.m_BBSViewerID.bo_table = "play";
						
						
			    		Intent intent;
				        intent = new Intent().setClass(self, Toy_Detail_BBS.class);
				        startActivity( intent ); 
						

						//parent.RefreshShop();
					}
				});
	   		}
	   		return convertView;
	   	}
	 }
     
}
