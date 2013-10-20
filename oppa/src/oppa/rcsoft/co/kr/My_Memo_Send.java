package oppa.rcsoft.co.kr;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

// ���� ������ 
public class My_Memo_Send extends  BaseActivity  implements OnClickListener
{
    /** Called when the activity is first created. */
	
	public static My_Memo_Send  self;
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
       
        setContentView(R.layout.memo_send_write);          // ��Ʈ�� ���̾ƿ� ���     
        self = this;
        
        Button WriteBtn = (Button)findViewById(R.id.memo_send_write_btn);
    	WriteBtn.setBackgroundResource(R.drawable.shop_detail_qna_write_btn);
    	WriteBtn.setOnClickListener(this);
    	
    	
        mProgress = new ProgressDialog(this);
		mProgress.setMessage("��ø� ��ٷ� �ֽʽÿ�.");
		mProgress.setIndeterminate(true);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);
		
		
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

    
    
    public void RefreshUI()
    {
    	setContentView(R.layout.memo_send_write);        
         
           
        Button WriteBtn = (Button)findViewById(R.id.memo_send_write_btn);
       	WriteBtn.setBackgroundResource(R.drawable.shop_detail_qna_write_btn);
       	WriteBtn.setOnClickListener(this);
       	
       	
        mProgress = new ProgressDialog(this);
   		mProgress.setMessage("��ø� ��ٷ� �ֽʽÿ�.");
   		mProgress.setIndeterminate(true);
   		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
   		mProgress.setCancelable(false);
    }

    public void onClick(View v )
    {
    	switch(v.getId())
    	{
	    		
	    	case R.id.memo_send_write_btn:
	    	{
	    		SendWrite( );
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
  				{
  	  				EditText content = (EditText)findViewById(R.id.memo_send_text123);
  	  				content.setText("");
  	  				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
  					imm.hideSoftInputFromWindow(content.getWindowToken(), 0);
  				}
  				MyInfo myApp = (MyInfo) getApplication();
  				// ���������� �޼����� ���� �Ǿ��� ��� 
  				onBackPressed();
  				message = "������ �� ���½��ϴ�.";
  				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
  				
  				
  				// Ű���带 ����� �ؽ�Ʈ�� �ʱ�ȭ �Ѵ�. 

  				

  			}
  				break;
  			case 1:
  				message = "No data";
  				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
  				break;
  			case 2:
  				self.ShowAlertDialLog( self,"�޸����۽���" , (String) msg.obj );
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


					
					EditText content = (EditText)findViewById(R.id.memo_send_text123);
					String strcon = content.getText().toString();
					

					data.put("me_recv_mb_id", "admin");
					data.put("me_memo", strcon);
					data.put("return_type", "json");

					
					String strJSON = myApp.PostHTTPData("http://oppa.rcsoft.co.kr/api/opps_memoSend.php", data);	
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
    
}
