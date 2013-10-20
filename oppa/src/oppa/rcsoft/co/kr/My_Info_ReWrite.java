package oppa.rcsoft.co.kr;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;


// �������� ���� 
public class My_Info_ReWrite extends  BaseActivity  implements OnClickListener
{
	
	String[] items = {"������� ����",
			"�������(��) 81.01.14",
			"�����ϴ� ������?",
			"ù����� �̸���?",
			"��Ӵ� ������?",
			"���� �����ϴ� �����̸���?"};
	
	static My_Info_ReWrite  self ; 
	
	String ID123;
	String Nick123;
	String pass_q123;
	String point123;
	String grade123;
	String level123;
	String status123;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
       

        
        View viewToLoad = LayoutInflater.from(this).inflate(R.layout.mypage_myinfo, null); 
        this.setContentView(viewToLoad);   

        mProgress = new ProgressDialog(this);
 		mProgress.setMessage("�۾����Դϴ�.");
 		mProgress.setIndeterminate(true);
 		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
 		mProgress.setCancelable(false);
 		
        self  =  this;
        
        InputSpinnerItem();
        
        {
            Button TermsBtn = (Button)findViewById(R.id.my_account_terms);
            TermsBtn.setOnClickListener(this);
            
            Button Overlap2Btn = (Button)findViewById(R.id.mypage_nick_overlap);
            Overlap2Btn.setOnClickListener(this); 
            
            Button CreateBtn = (Button)findViewById(R.id.mypage_myinfo_rewrite);
            CreateBtn.setOnClickListener(this); 
        }
        
		///////////////////////////////////////////////////////////////////////////////////////////////////
		// Tab
		{
    		Button TabBTN2 = (Button)findViewById(R.id.mypage1_main_tab_btn_1);
    		TabBTN2.setOnClickListener(this);
    		Button TabBTN3 = (Button)findViewById(R.id.mypage1_main_tab_btn_2);
    		TabBTN3.setOnClickListener(this);
    		Button TabBTN4 = (Button)findViewById(R.id.mypage1_main_tab_btn_3);
    		TabBTN4.setOnClickListener(this);
    		Button TabBTN5 = (Button)findViewById(R.id.mypage1_main_tab_btn_4);
    		TabBTN5.setOnClickListener(this);
		}
		
        RefreshUI();
    }
    
    
    public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		switch(v.getId())
    	{
    	case R.id.my_account_terms:
    	{
    		
    		new AlertDialog.Builder(this)

    		
    		.setTitle("����Ʈ ��å")
    		.setMessage("ȸ�� ����Ʈ ��å \n\n 1. �������ʹ湮 ( 10�� ) \n 2. ��� ( 5��) \n 3. �����Խ��� ��� ( 5�� ) \n 4. Q&A��� ( 5�� ) \n" +
    				" 5. ���ۿ� ��� ( 3�� ) \n 6. �湮 �ı� ( 5�� )\n 7. ����( 3�� ) \n 8. �߰��湮( 1�� )")
    		.setPositiveButton("Ȯ��", new DialogInterface.OnClickListener()
    		{
    			public void onClick(DialogInterface dialog, int which)
    			{
    				dialog.dismiss();
    			}
    		})
    		.show();
    	}
    		break;

    	case  R.id.mypage_nick_overlap:
    		// �г��� �ߺ�
    		CheckNickOverlap();
    		break;
    	case R.id.mypage_myinfo_rewrite:
    		// ȸ�� ����
    		CreateAccount();
    		break;
    	case R.id.home_login_terms_back:
    	{



    	}
    		break;
    		
    	case R.id.mypage1_main_tab_btn_2:

    	{
    		Intent intent;
	        intent = new Intent().setClass(this, Shop_MainList.class);
	        startActivity( intent );  
    	}
    		break;
    		
    	case R.id.mypage1_main_tab_btn_3:
 
    	{
    		Intent intent;
	        intent = new Intent().setClass(this, ToyMainList.class);
	        startActivity( intent );   
    	}
    		break;
    		
    	case R.id.mypage1_main_tab_btn_4:
    	{
    		Intent intent;
	        intent = new Intent().setClass(this, Community_Main.class);
	        startActivity( intent );  
    	}
    		break;
    		
    	case R.id.mypage1_main_tab_btn_1:
    	{
    		Intent intent;
	        intent = new Intent().setClass(this, Home.class);
	        startActivity( intent );  
    	}
    		break;
    		
    	}
		
	}
    
    
    public void RefreshUI()
    {
    	GetUserInfo();
    }
    
    
   // ��й�ȣ ���� ������ �����´�... 
    
    public void GetQuestionItem()
    {
    	
		{
			final MyInfo myApp = (MyInfo) getApplication();
			
			Thread thread = new Thread(new Runnable()
			{

				public void run() 
				{
					Map<String, String> data = new HashMap  <String, String>();
					data.put("return_type", "json");
					
					String strJSON = myApp.GetHTTPGetData("http://oppa.rcsoft.co.kr/api/gnu_passwordQuestion.php", data);
					
					if( strJSON.equals("error"))
					{
						handler.sendMessage(handler.obtainMessage(1,"���ӿ� ������ �ֽ��ϴ�." ));
						return;
					}
					
					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.getString("result").equals("ok"))
						{
							JSONArray Qlist = json.getJSONArray("list");
							items = new String[Qlist.length()];
							for( int i = 0 ; i < Qlist.length(); i++ )
							{
								items[i] = myApp.DecodeString(Qlist.getJSONObject(i).getString("question"));
							}
							// ����Ʈ�� �����϶�� �޼����� �Ѱ��ش�. 
							handler.sendEmptyMessage(2);
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
						e.printStackTrace();
					}
				}
			});
			thread.start();
		}
		
    }
    public void InputSpinnerItem()
    {
        // ���ǳ� ó�� 
        Spinner spin = (Spinner)findViewById(R.id.my_account_spinner);
        
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
    
    public void CheckNickOverlap()
    {
    	{
			final MyInfo myApp = (MyInfo) getApplication();
			mProgress.show();
			Thread thread = new Thread(new Runnable()
			{

				public void run() 
				{

					
					Map<String, String> data = new HashMap  <String, String>();
					
					TextView ID = (TextView)findViewById(R.id.my_account_id_text);
					String strID = ID.getText().toString();
					
					EditText Nick = (EditText)findViewById(R.id.my_account_nickname);
					String strNick = Nick.getText().toString();
					data.put("return_type", "json");
					data.put("mb_nick", strNick);
					data.put("mb_id", strID);
					
					String strJSON = myApp.GetHTTPGetData("http://oppa.rcsoft.co.kr/api/gnu_registNickCheck.php", data);
					
					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.getString("result").equals("ok"))
						{
							handler.sendEmptyMessage(4);
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
						e.printStackTrace();
					}
				}
			});
			thread.start();
		}
    }
    
    public void CreateAccount()
    {
    	{
			final MyInfo myApp = (MyInfo) getApplication();
			mProgress.show();
			Thread thread = new Thread(new Runnable()
			{

				public void run() 
				{
					
					EditText Nick = (EditText)findViewById(R.id.my_account_nickname);
					String strNick = Nick.getText().toString();
					TextView ID = (TextView)findViewById(R.id.my_account_id_text);
					String strID = ID.getText().toString();
					
					EditText Pass = (EditText)findViewById(R.id.my_account_pass1);
					String strPass = Pass.getText().toString();
					
					EditText Pass4 = (EditText)findViewById(R.id.my_account_newpass);
					String strPass4 = Pass4.getText().toString();
					
					EditText Pass2 = (EditText)findViewById(R.id.my_account_pass2);
					String strPass2 = Pass2.getText().toString();
					
					if ( strPass.equals( myApp.m_AccountData.Password) == false  )
					{
						handler.sendMessage(handler.obtainMessage(1, "���� ��й�ȣ�� ��ġ���� �ʽ��ϴ�. " ));
						return;
					}
					
					
					if ( strPass4.equals( strPass2) == false  )
					{
						handler.sendMessage(handler.obtainMessage(1, "���ο� ��й�ȣ�� ��ġ���� �ʽ��ϴ�. " ));
						return;
					}


					
					Spinner spin = (Spinner)findViewById(R.id.my_account_spinner);
					String strQ = (String) spin.getSelectedItem();
					
					EditText Pass3 = (EditText)findViewById(R.id.my_account_pass3);
					String strPass3 = Pass3.getText().toString();
					
					Map<String, String> data = new HashMap  <String, String>();
					data.put("return_type", "json");
					data.put("w", "u");
					data.put("mb_nick", strNick);
					data.put("mb_id", strID);
					data.put("mb_password", strPass4);
					data.put("mb_password_q", strQ);
					data.put("mb_password_a", strPass3);
					
					String strJSON = myApp.PostHTTPData("http://oppa.rcsoft.co.kr/api/gnu_registMember.php", data);
					
					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.getString("result").equals("ok"))
						{
							handler.sendEmptyMessage(5);
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
						e.printStackTrace();
					}
				}
			});
			thread.start();
		}
    }
    
    void GetUserInfo()
    {
		final MyInfo myApp = (MyInfo) getApplication();
		mProgress.show();
		Thread thread = new Thread(new Runnable()
		{

			public void run() 
			{

				
				Map<String, String> data = new HashMap  <String, String>();
				data.put("return_type", "json");
				
				String strJSON = myApp.GetHTTPGetData("http://oppa.rcsoft.co.kr/api/oppa_getUserInfo.php", data);
				
				try 
				{
					JSONObject json = new JSONObject(strJSON);
					if(json.getString("result").equals("ok"))
					{
						ID123 = myApp.DecodeString(json.getString("mb_id"));
						Nick123  = myApp.DecodeString(json.getString("mb_nick"));
						pass_q123= myApp.DecodeString(json.getString("mb_password_q"));
						point123= myApp.DecodeString(json.getString("mb_point"));
						grade123= myApp.DecodeString(json.getString("mb_grade"));
						level123= myApp.DecodeString(json.getString("mb_level"));
						status123= myApp.DecodeString(json.getString("mb_status"));
						handler.sendEmptyMessage(3);
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
					e.printStackTrace();
				}
			}
		});
		thread.start();
    }
    
    
	final Handler handler = new Handler( )
	{
    			
		public void handleMessage(Message msg)
		{
			mProgress.dismiss();
			switch(msg.what)
			{
			case 0:
				break;
			case 1:
				// ����ó�� 
				self.ShowAlertDialLog( self ,"����" , (String) msg.obj );
				break;
			case 2:
				// ��й�ȣ ���� ����
				InputSpinnerItem();
				break;
			case 3:
			{
				
				final MyInfo myApp = (MyInfo) getApplication();
				
				EditText Nick12 = (EditText)findViewById(R.id.my_account_nickname);
				Nick12.setText(Nick123);
				TextView ID = (TextView)findViewById(R.id.my_account_id_text);
				ID.setText(ID123);		
				
				TextView grade = (TextView)findViewById(R.id.my_mypage_grade);
				grade.setText(grade123);
				
				ImageView grade1 = (ImageView)findViewById(R.id.my_mypage_grade_icon);
    			grade1.setBackgroundResource(myApp.GetGradeIcon(grade123));


			}

				break;
			case 4:
				self.ShowAlertDialLog( self ,"��밡��" , "����Ҽ� �ֽ��ϴ�." );
				break;
			case 5:
				/*self.ShowAlertDialLog( self.getParent() ,"���ԿϷ�" , "������ �Ϸ� �Ǿ����ϴ�." );
	    		MyPageGroup parent = (MyPageGroup) getParent();
				parent.setContentView(BaseActivityGroup.CHILD_ONE);*/
				break;
			default:

				break;
			}

		}
	};
	
}
