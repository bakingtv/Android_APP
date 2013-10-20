package oppa.rcsoft.co.kr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;



// Ŀ�´�Ƽ ���� ���� .

public class BBS_Company extends  BaseActivity   implements OnClickListener
{
	String[] items2 = {"Ű����",
			"������", 
			"����Ʈ", 
			"��η�", 
			"��Ÿ����", 
			};
	int CategoryCount = 0;
	
	ArrayList<String >	m_CategoryList2 = new ArrayList<String >();
	
	public static BBS_Company self;
	
	int m_Category;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);

        self = this;
        View viewToLoad = LayoutInflater.from(this).inflate(R.layout.bbs_company, null); 
        this.setContentView(viewToLoad);   
        
        Button CreateBTN = (Button)findViewById(R.id.bbs_company_wr_score);
        CreateBTN.setOnClickListener(this);
        
        
		///////////////////////////////////////////////////////////////////////////////////////////////////
		// Tab
		{
			Button TabBTN2 = (Button)findViewById(R.id.commu_main_tab_btn_1);
			TabBTN2.setOnClickListener(this);
			Button TabBTN3 = (Button)findViewById(R.id.commu_main_tab_btn_2);
			TabBTN3.setOnClickListener(this);
			Button TabBTN4 = (Button)findViewById(R.id.commu_main_tab_btn_3);
			TabBTN4.setOnClickListener(this);
			Button TabBTN5 = (Button)findViewById(R.id.commu_main_tab_btn_5);
			TabBTN5.setOnClickListener(this);
		}
		
        
        mProgress = new ProgressDialog(this);
		mProgress.setMessage("��ø� ��ٷ� �ֽʽÿ�.");
		mProgress.setIndeterminate(true);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);
		
		
		// ī�װ� ����Ʈ �������� 
		GetCateList();

        
    }
    
    void InputSpinnerItem2()
    {
        // ���ǳ� ó�� 
        Spinner spin = (Spinner)findViewById(R.id.bbs_company_shoptype);
        
        spin.setPrompt("������ �����ϼ���.");
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, m_CategoryList2);
        
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);

        spin.setOnItemSelectedListener(new OnItemSelectedListener()
        {           
        	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
        	{                
        		m_Category = position + 1;            
        	}            
        	public void onNothingSelected(AdapterView<?> parent) 
        	{               
        		// TODO Auto-generated method stub          
        	}       
        });
    }
    

    public void onClick(View v )
    {
    	switch(v.getId())
    	{
	    	case R.id.bbs_company_wr_score:
	    	{
	    		SendWrite( );
	    	}
	    		break;
	    	case R.id.commu_main_tab_btn_1:
	    	{
	       		Intent intent;
		        intent = new Intent().setClass(self, Home.class);
		        startActivity( intent );  		
	    	}
	    		break;
	    	case R.id.commu_main_tab_btn_2:
	    	{
	       		Intent intent;
		        intent = new Intent().setClass(self, Shop_MainList.class);
		        startActivity( intent );  		
	    	}
	    		break;
	    	case R.id.commu_main_tab_btn_3:
	    	{
	       		Intent intent;
		        intent = new Intent().setClass(self, ToyMainList.class);
		        startActivity( intent );  		
	    	}
	    		break;
	    	case R.id.commu_main_tab_btn_5:
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
    
	public void GetCateList()
	{
		// ī�װ� ����Ʈ�� 1������ ���ʴ�� �����´�. 
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
							items2 = new String[Qlist.length()];
							for( int i = 0 ; i < Qlist.length(); i++ )
							{
								items2[i] = myApp.DecodeString(Qlist.getJSONObject(i).getString("c2_name"));
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
	
	
    // ���޹��� ��ư Ŭ���� �޼��� ����
    void SendWrite( )
    {
    	mProgress.show();
    	final MyInfo myApp = (MyInfo) getApplication();
    	{
    		 
			Thread thread = new Thread(new Runnable()
			{
				public void run() 
				{
					Map<String, String> data = new HashMap  <String, String>();

					
					EditText content = (EditText)findViewById(R.id.bbs_company_wr_content);
					String strcon = content.getText().toString();
					
					EditText addr = (EditText)findViewById(R.id.bbs_company_addr);
					String straddr = addr.getText().toString();
					EditText tel = (EditText)findViewById(R.id.bbs_company_tel);
					String strtel = tel.getText().toString();
					
			 		Spinner spin = (Spinner)findViewById(R.id.bbs_company_shoptype);
					String loca = (String) spin.getSelectedItem();
					
					if ( straddr.equals(""))
					{
						self.ShowAlertDialLog( self ,"����" , "�ּҰ� ���Ե��� �ʾҽ��ϴ�" );
						return;
					}
					if ( strtel.equals(""))
					{
						self.ShowAlertDialLog( self ,"����" , "��ȭ��ȣ�� ���Ե��� �ʾҽ��ϴ�");
						
						return;
					}
					if ( strcon.equals(""))
					{
						self.ShowAlertDialLog( self ,"����" ,  "���� ������ ���Ե��� �ʾҽ��ϴ�"  );
						return;
					}
					
					
					data.put("wr_content", strcon);
					data.put("return_type", "json");
					data.put("wr_4", straddr);
					data.put("wr_3", strtel );
					data.put("wr_2", loca);
					data.put("bo_table", "ally");
					
					String strJSON = myApp.PostHTTPData("http://oppa.rcsoft.co.kr/api/gnu_saveBoardArticle.php", data);	
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
			
			switch(msg.what)
			{
			case 0:
				// ���� ����
				mProgress.dismiss();
				Toast.makeText(getApplicationContext(), "������ �����Ͽ����ϴ�.", Toast.LENGTH_LONG).show();
				
				{
					EditText content = (EditText)findViewById(R.id.bbs_company_wr_content);
					content.setText("");
					
					EditText addr = (EditText)findViewById(R.id.bbs_company_addr);
					addr.setText("");
					EditText tel = (EditText)findViewById(R.id.bbs_company_tel);
					tel.setText("");
				}
				onBackPressed();
				break;
			case 1:
				// �����Ͱ� ���� 
				mProgress.dismiss();
				message = "No data";
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
				break;
			case 2:
				// ���� �޼��� ���
				mProgress.dismiss();
				// ����ó�� 
				self.ShowAlertDialLog( self ,"����" , (String) msg.obj );
				break;
				
			case 100:
				// ī�װ� ����Ʈ�� ��δ� ��� ������ Ȯ�� �ϰ� �ƴ϶�� �ٽ��ѹ� �� ������
			{
				CategoryCount++;
				
				for( int i = 0 ; i < items2.length; i++ )
				{
					m_CategoryList2.add(items2[i]);
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
	
    
}
