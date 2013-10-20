package oppa.rcsoft.co.kr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import oppa.rcsoft.co.kr.Shop_MainFinderList.MoreBTNClick;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

// ���� �˻� �ϱ� 
public class Shop_Main_Finder_Activity extends  BaseActivity  implements OnClickListener
{
    /** Called when the activity is first created. */
	
	boolean m_checkLoactionList;
	boolean m_checkShopTypeList;
	static Shop_Main_Finder_Activity self;
	int m_Category;
	int m_Distance;
	int CategoryCount = 0;
	String[] items;
	String[] items2 = {"Ű����",
		"������", 
		"����Ʈ", 
		"��η�", 
		"��Ÿ����", 
		"��ü����",
		};
	
	String[] items3;
	
	ArrayList< Integer> c2_idxList;

	ArrayList<String >	m_CategoryList2 = new ArrayList<String >();
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        View viewToLoad = LayoutInflater.from(this).inflate(R.layout.shop_info_find, null); 
        this.setContentView(viewToLoad);   
        
        self = this;
        
		mProgress = new ProgressDialog(this);
		mProgress.setMessage("��ø� ��ٷ� �ֽʽÿ�.");
		mProgress.setIndeterminate(true);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);
		
		c2_idxList = new ArrayList< Integer>();
		
		c2_idxList.clear();
		
	   /* {
	        // ���ǳ� ó�� 
	        Spinner spin = (Spinner)findViewById(R.id.shop_finder_shoptype);
	        
	        spin.setPrompt("������ �����ϼ���.");
	        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items2);
	        
	        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        spin.setAdapter(aa);

	        spin.setOnItemSelectedListener(new OnItemSelectedListener()
	        {           
	        	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
	        	{                
	        		//m_Category = position + 1;            
	        	}            
	        	public void onNothingSelected(AdapterView<?> parent) 
	        	{               
	        		// TODO Auto-generated method stub          
	        	}       
	        });
	    } */
	    //GetLocationList();
	    
	    SeekBar      m_sb = (SeekBar)findViewById(R.id.shop_find_distance);

	    SeekBar.OnSeekBarChangeListener listenGenerator    = new SeekBar.OnSeekBarChangeListener()
	    {
		   
		   //thumb �� �������� ������� �޼���
		   public void onStopTrackingTouch(SeekBar seekBar) 
		   {

		   }
		   
		   //thumb �� ������� ������� �޼���
		   public void onStartTrackingTouch(SeekBar seekBar) 
		   {

		   }
		   
		   //thumb �� ��ġ�� ����� ������ ���� �޼���
		   public void onProgressChanged(SeekBar seekBar, int progress,  boolean fromUser) 
		   {
			   m_Distance = progress * 10;
			   TextView   m_textProgress = (TextView)findViewById(R.id.shop_finder_distance);
			   float dis = ((float)progress) / 100.0f;
			   if ( m_Distance >= 99000  )
			   {
				   m_Distance = 100000000;
				   m_textProgress.setText("���Ѿ���");
			   }
			   else
			   {
				   m_textProgress.setText(new StringBuilder().append(dis) + "km");
			   }
			  

		   }
	    };
	    m_sb.setOnSeekBarChangeListener(listenGenerator);
	    
	    
	    Button findbtn = (Button)findViewById(R.id.shop_find_findbtn);
	    findbtn.setOnClickListener(this);
	    
		{
			Button MainBtn1 = (Button)findViewById(R.id.shop_main_tab_btn_11);
	        MainBtn1.setOnClickListener(this);
	        Button MainBtn2 = (Button)findViewById(R.id.shop_main_tab_btn_31);
	        MainBtn2.setOnClickListener(this);
	        Button MainBtn3 = (Button)findViewById(R.id.shop_main_tab_btn_41);
	        MainBtn3.setOnClickListener(this);
	        Button MainBtn4 = (Button)findViewById(R.id.shop_main_tab_btn_51);
	        MainBtn4.setOnClickListener(this);
	    }
	    
	    GetLocationList();
    }
    
	
    public void GetLocationList()
    {
    	
		{
			final MyInfo myApp = (MyInfo) getApplication();
			mProgress.show();
			Thread thread = new Thread(new Runnable()
			{

				public void run() 
				{


					Map<String, String> data = new HashMap  <String, String>();
					data.put("return_type", "json");
					
					String strJSON = myApp.GetHTTPGetData("http://oppa.rcsoft.co.kr/api/oppa_getAreaList.php", data);
					
					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.getString("result").equals("ok"))
						{
							JSONArray Qlist = json.getJSONArray("list");
							items = new String[Qlist.length()];
							for( int i = 0 ; i < Qlist.length(); i++ )
							{
								items[i] = myApp.DecodeString(Qlist.getJSONObject(i).getString("a1_name"));
							}
							handler.sendEmptyMessage(0);
						}
						else 
						{
							// ���� �޼����� �����Ѵ�. 
							handler.sendMessage(handler.obtainMessage(1,myApp.DecodeString(json.getString("result_text")) ));
							return ;
						}
					} catch (JSONException e) 
					{
						// TODO Auto-generated catch block
						handler.sendMessage(handler.obtainMessage(1,"�α��ο� �����߽��ϴ�." ));
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
			
			switch(msg.what)
			{
			case 0:
				mProgress.dismiss();
				InputSpinnerItem();
				GetCateList();
				break;
			case 1:
				mProgress.dismiss();
				message = "No data";
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
				break;
			case 2:
				mProgress.dismiss();
				// ����ó�� 
				self.ShowAlertDialLog( self ,"����" , (String) msg.obj );
				break;
			case 3:
				mProgress.dismiss();
				InputSpinnerItem2();
				break;
				
			case 100:
			{
				CategoryCount++;
				
				for( int i = 0 ; i < items3.length; i++ )
				{
					m_CategoryList2.add(items3[i]);
				}
				
				if ( CategoryCount > 5  )
				{
					mProgress.dismiss();
					InputSpinnerItem2();
				}
				else
				{
					GetCateList2( CategoryCount );
				}
			}
			break;
			default:
				//	message = "�����Ͽ����ϴ�.";

				break;
			}

		}
	};
	
	public void InputSpinnerItem()
    {
        // ���ǳ� ó�� 
        Spinner spin = (Spinner)findViewById(R.id.shop_find_location);
        
        spin.setPrompt("������ �����ϼ���.");
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);

        spin.setOnItemSelectedListener(new OnItemSelectedListener()
        {           
        	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
        	{                
        		            
        	}            
        	public void onNothingSelected(AdapterView<?> parent) 
        	{               
        		// TODO Auto-generated method stub          
        	}       
        });
    }
	
	
	public void GetCateList()
    {
    
		m_CategoryList2.clear();
		CategoryCount  = 1;
		GetCateList2( CategoryCount );
    }
	
	
	public void GetCateList2( int kind)
    {
    	
		{
			final int kind1 = kind;
			final MyInfo myApp = (MyInfo) getApplication();
			mProgress.show();
			Thread thread = new Thread(new Runnable()
			{

				public void run() 
				{
					Map<String, String> data = new HashMap  <String, String>();
					data.put("return_type", "json");
					data.put("c1_idx", Integer.toString(kind1) );
					
					String strJSON = myApp.GetHTTPGetData("http://oppa.rcsoft.co.kr/api/oppa_getCate2List.php", data);
					
					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.getString("result").equals("ok"))
						{
							JSONArray Qlist = json.getJSONArray("list");
							items3 = new String[Qlist.length()];
							
							for( int i = 0 ; i < Qlist.length(); i++ )
							{
								items3[i] = myApp.DecodeString(Qlist.getJSONObject(i).getString("c2_name"));
								c2_idxList.add( Qlist.getJSONObject(i).getInt("c2_idx"));
							}
							handler.sendEmptyMessage(100);
						}
						else 
						{
							// ���� �޼����� �����Ѵ�. 
							handler.sendMessage(handler.obtainMessage(1,myApp.DecodeString(json.getString("result_text")) ));
							return ;
						}
					} catch (JSONException e) 
					{
						// TODO Auto-generated catch block
						handler.sendMessage(handler.obtainMessage(1,"�α��ο� �����߽��ϴ�." ));
						e.printStackTrace();
					}
				}
			});
			thread.start();
		}
    }
	
    public void InputSpinnerItem2()
    {
        // ���ǳ� ó�� 
        Spinner spin = (Spinner)findViewById(R.id.shop_finder_detail);
        
        spin.setPrompt("ī�װ��� �����ϼ���.");
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, m_CategoryList2);
        
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);

        spin.setOnItemSelectedListener(new OnItemSelectedListener()
        {           
        	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
        	{                
        		m_Category = c2_idxList.get(position) ;          
        	}            
        	public void onNothingSelected(AdapterView<?> parent) 
        	{               
        		// TODO Auto-generated method stub          
        	}       
        });
    }
    

	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		
    	switch(v.getId())
    	{
    	case R.id.shop_find_findbtn:
    	{

    		Spinner spin = (Spinner)findViewById(R.id.shop_find_location);
			String loca = (String) spin.getSelectedItem();
						
			EditText name = (EditText)findViewById(R.id.shop_find_name);
			String strName = name.getText().toString();
			
    		MyInfo myApp = (MyInfo) getApplication();
    		myApp.m_ShopFindInfo.Location = loca;
    		myApp.m_ShopFindInfo.Distance = Integer.toString(m_Distance);
    		myApp.m_ShopFindInfo.Name = strName;
    		myApp.m_ShopFindInfo.Category = Integer.toString(m_Category);
    		
    		
    		// ���ο� �˻� ���� ����Ʈ��
    		Intent intent;
	        intent = new Intent().setClass(self, Shop_MainFinderList.class);
	        startActivity( intent );  
    	
    	}
    		break;
    		
    	case R.id.shop_main_tab_btn_11:
    	{
    		Intent intent;
	        intent = new Intent().setClass(self, Home.class);
	        startActivity( intent );  
    	}
    		break;
    		
    	case R.id.shop_main_tab_btn_31:
    	{
    		Intent intent;
	        intent = new Intent().setClass(self, ToyMainList.class);
	        startActivity( intent );   
    	}
    		break;
    		
    	case R.id.shop_main_tab_btn_41:
    	{
    		Intent intent;
	        intent = new Intent().setClass(self, Community_Main.class);
	        startActivity( intent );  
    	}
    		break;
    		
    	case R.id.shop_main_tab_btn_51:
    	{
    		Intent intent;
	        intent = new Intent().setClass(self, MyPage_Main.class);
	        startActivity( intent );  
    	}
    		break;
			

    	default:
    		break;
    	}
		
	} 
}
