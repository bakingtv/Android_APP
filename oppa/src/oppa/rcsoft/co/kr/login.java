package oppa.rcsoft.co.kr;

import java.util.HashMap;
import java.util.Map;

import oppa.rcsoft.co.kr.MyInfo.MyAccountData;

import org.json.JSONException;
import org.json.JSONObject;




import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;


// �α��� ȭ�� 
public class login extends BaseActivity implements OnClickListener {
    /** Called when the activity is first created. */
	   /** Called when the activity is first created. */
		private login  self = null;

	    @Override
	    public void onCreate(Bundle savedInstanceState) 
	    {
	        super.onCreate(savedInstanceState);
	        self = this;
	        setContentView(R.layout.home_login_layout);
	        
			mProgress = new ProgressDialog(this);
			mProgress.setMessage("��ø� ��ٷ� �ּ���.");
			mProgress.setIndeterminate(true);
			mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgress.setCancelable(false);
			
	        
	        Button LoginBtn = (Button)findViewById(R.id.home_login_login);
	        LoginBtn.setOnClickListener(this);
	        Button FindBtn = (Button)findViewById(R.id.home_login_account_find_btn);
	        FindBtn.setOnClickListener(this);
	        Button CreateBtn = (Button)findViewById(R.id.home_login_account_create_btn);
	        CreateBtn.setOnClickListener(this);
	        
	        
	        {
	        	MyInfo myApp = (MyInfo) getApplication();
	        	
	        	MyAccountData Mydata = myApp.GetAccountData();
	        	
	        	CheckBox IDSave = (CheckBox)findViewById(R.id.home_login_id_save_check);
				CheckBox AutoLogin = (CheckBox)findViewById(R.id.home_login_auto_save_check);
	        	
	        	if (Mydata.AutoLogin == true )
	        	{
					EditText IDBOX = (EditText)findViewById(R.id.home_login_id_textbox);
					IDBOX.setText(Mydata.ID);
					
					
					EditText Password = (EditText)findViewById(R.id.home_login_password_textbox);
					Password.setText(Mydata.Password);
					IDSave.setChecked(true);
					AutoLogin.setChecked(true);
					
					Login();

	        	}
	        	else if ( Mydata.IDSave == true)
	        	{
					EditText IDBOX = (EditText)findViewById(R.id.home_login_id_textbox);
					IDBOX.setText(Mydata.ID);
					IDSave.setChecked(true);
	        	}
	        	
	        }
	        
	        

	        
	        

	    }
	    public void onResume()
	    {
	    	// �α��� �� ������ ����ε� ����� ������ ��� ������ �̵��Ѵ�. 
	    	 super.onResume();
	    }
	    
	    
	    @Override
		public boolean onKeyDown(int keyCode, KeyEvent event)
		{
	    	if(event.getKeyCode() == KeyEvent.KEYCODE_BACK)
			{
				return true;
			}
			Log.e(TAG, "onKeyDown");
			return super.onKeyDown(keyCode, event);

		}
	    
	    public void onClick(View v )
	    {
	    	switch(v.getId())
	    	{
	    	case R.id.home_login_login:
	    	{
	    		HideKeyBoard();
	    		Login();
				
	    	}
	    		break;
	    	case R.id.home_login_account_create_btn:
	    	{
    		
	           Intent intent;
	           intent = new Intent().setClass(this, login_adult.class);
	           startActivity( intent );   
	           
	    	}
	    		break;
	      	case R.id.home_login_account_find_btn:
	    	{
	    		Intent intent;
		        intent = new Intent().setClass(this, login_finder.class);
		        startActivity( intent );   
		           
	    	}
	    		break;
	    	default:
	    		break;
	    	}
	    }
	    
	    @Override
	    public void onBackPressed() 
	    {
	    	// ���� ȭ������ �� ���ư��� ���´�.( �ƿ� ���ᰡ �ǵ����Ѵ� )
	    	// �ϴ��� ���ư��� �ְ� �׽�Ʈ������ ����д�. 
	    	/*HomeActivity parent = (HomeActivity) getParent();
			parent.onBackPressed();*/
	    	
	    	new AlertDialog.Builder(self)
			 .setTitle("���� Ȯ��")
			 .setMessage("���� ���� �ϰڽ��ϱ�?") //�ٿ���
			 .setPositiveButton("��", new DialogInterface.OnClickListener() 
			 {
			     public void onClick(DialogInterface dialog, int whichButton)
			     {   
						moveTaskToBack(true);
						finish();
					    android.os.Process.killProcess(android.os.Process.myPid());
					    
					    ActivityManager am  = (ActivityManager)self.getSystemService(Activity.ACTIVITY_SERVICE);
			    	    am.restartPackage(self.getPackageName());
			     }
			 })
			 .setNegativeButton("�ƴϿ�", new DialogInterface.OnClickListener() 
			 {
			     public void onClick(DialogInterface dialog, int whichButton) 
			     {
			         //...����
			     }
			 })
			 .show();
	    	
	    }
	    
	    public void Login()
	    {
	    	
			{
				final MyInfo myApp = (MyInfo) getApplication();
				HideKeyBoard();
				mProgress.show();
				Thread thread = new Thread(new Runnable()
				{

					public void run() 
					{
						// ID�� �н����带 �����´� 
						EditText IDBOX = (EditText)findViewById(R.id.home_login_id_textbox);
						String ID = IDBOX.getText().toString();
						
						EditText Password = (EditText)findViewById(R.id.home_login_password_textbox);
						String pass = Password.getText().toString();

						Map<String, String> data = new HashMap  <String, String>();
						data.put("mb_password", pass );
						data.put("mb_id", ID);
						data.put("return_type", "json");
						
						String strJSON = myApp.PostCookieHTTPData("http://oppa.rcsoft.co.kr/api/gnu_login.php", data);
						
						try 
						{
							JSONObject json = new JSONObject(strJSON);
							if(json.getString("result").equals("ok"))
							{
								MyAccountData Mydata = myApp.GetAccountData();
								
								Mydata.ID = ID;
								Mydata.Password = pass;
								Mydata.Name = json.getString("mb_name");
								Mydata.Level = json.getInt("mb_level");
								// �ڵ� �α��ε�....
								CheckBox IDSave = (CheckBox)findViewById(R.id.home_login_id_save_check);
								CheckBox AutoLogin = (CheckBox)findViewById(R.id.home_login_auto_save_check);
								Mydata.IDSave = IDSave.isChecked();
								Mydata.AutoLogin = AutoLogin.isChecked();
								
								
								
					
								// ������ �����ϱ�.
								myApp.SetAccountData(Mydata);
								myApp.SaveInfo();
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
							handler.sendMessage(handler.obtainMessage(1,"�α��ο� �����߽��ϴ�." ));
							e.printStackTrace();
						}
					}
				});
				thread.start();
			}
	    }
	    
	    public void HideKeyBoard()
	    {
	    	
			EditText IDBOX = (EditText)findViewById(R.id.home_login_id_textbox);
			EditText Password = (EditText)findViewById(R.id.home_login_password_textbox);

			
	    	InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
	    	imm.hideSoftInputFromWindow(IDBOX.getWindowToken(),0);   // Ű���� ������ 
	    	imm.hideSoftInputFromWindow(Password.getWindowToken(),0);
	    }
	    
	    public void GetUserInfo()
	    {
			final MyInfo myApp = (MyInfo) getApplication();
			HideKeyBoard();
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
							MyAccountData Mydata = myApp.GetAccountData();

							Mydata.ID = json.getString("mb_id");
							Mydata.NickName = json.getString("mb_nick");
							Mydata.Level = json.getInt("mb_level");

							Mydata.Point = json.getInt("mb_point");
							Mydata.Grade = json.getString("mb_grade");
							Mydata.Status = json.getString("mb_status");
							
							
				
							// ������ �����ϱ�.
							myApp.SetAccountData(Mydata);
							myApp.SaveInfo();
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
	    
	    
	    
	    final Handler handler = new Handler( )
		{
	    			
			public void handleMessage(Message msg)
			{
				mProgress.dismiss();
				switch(msg.what)
				{
				case 0:
					{
					
			           Intent intent;
			           intent = new Intent().setClass(self, Home.class);
			           startActivity( intent );  
					}   
					break;
				case 1:
					// ����ó�� 
					self.ShowAlertDialLog( self ,"�α��� ����" , (String) msg.obj );
					break;
				case 2:
					GetUserInfo();
					break;
				default:

					break;
				}

			}
		};
}