package oppa.rcsoft.co.kr;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;


// Home ID�� �н����� ã��
public class login_finder extends BaseActivity implements OnClickListener  {

	
	String[] items = {"����� �¾����?",
			"ù����� �̸���?", 
			"�Ƶ��� �̸���?", 
			"�ױ����� ������ ���� ����?", 
			"���� ��ſ��� ����?", 
			"���� â���ߴ� ����?",
			"���� �ΰ� �ִ� ������?", 
			"�������� ������ �� ��Ҵ�?"};
	
    /** Called when the activity is first created. */
	login_finder  self = null;
	
	String ID = "";
	String Pass ="";
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        self = this;
        
        View viewToLoad = LayoutInflater.from(this).inflate(R.layout.home_login_id_find, null); 
        this.setContentView(viewToLoad);   

        
        Button FindBtn = (Button)findViewById(R.id.home_login_id_find_btn);
        FindBtn.setOnClickListener(this);
        Button BackBtn = (Button)findViewById(R.id.home_login_id_find_back);
        BackBtn.setOnClickListener(this);
        
        
        mProgress = new ProgressDialog(this);
		mProgress.setMessage("�۾����Դϴ�.");
		mProgress.setIndeterminate(true);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);
        
  
        GetQuestionItem();

        
    }

    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		Log.e(TAG, "onKeyDown");
		return super.onKeyDown(keyCode, event);
		

	}
    
	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		switch(v.getId())
    	{
    	case R.id.home_login_id_find_btn:
    	{
    		// ���̵�� �н����带 �ؽ�Ʈ �ڽ��� �Է� ���ش�. 
    		FindIDPass();
    	}
    		break;
    	case R.id.home_login_id_find_back:
    	{
    		this.onBackPressed();
    	}
    		break;
    		
    	}
		
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
        Spinner spin = (Spinner)findViewById(R.id.home_id_finder_spinner);
        
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
    
    public void FindIDPass()
    {
		{
			final MyInfo myApp = (MyInfo) getApplication();
			mProgress.show();
			Thread thread = new Thread(new Runnable()
			{

				public void run() 
				{
					Spinner spin = (Spinner)findViewById(R.id.home_id_finder_spinner);
					String strQ = (String) spin.getSelectedItem();
					
					EditText Nick = (EditText)findViewById(R.id.home_find_nick);
					String strNick = Nick.getText().toString();
					EditText Result = (EditText)findViewById(R.id.home_find_result);
					String strResult = Result.getText().toString();
					
					Map<String, String> data = new HashMap  <String, String>();
					
					data.put("return_type", "json");
					/*data.put("mb_nick", strNick);
					
					data.put("mb_password_a", strResult);
					*/
					data.put("mb_nick", strNick);
					data.put("mb_password_q", strQ);
					data.put("mb_password_a", strResult);
					
					String strJSON = myApp.GetHTTPGetData("http://oppa.rcsoft.co.kr/api/gnu_findIdPassword.php", data);
					
					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.getString("result").equals("ok"))
						{
							ID =json.getString("mb_id");
							Pass = json.getString("mb_password");
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
				EditText Nick = (EditText)findViewById(R.id.home_find_id);
				Nick.setText(ID);
				EditText Result = (EditText)findViewById(R.id.home_find_pass);
				Result.setText(Pass);
				break;
			default:
				break;
			}

		}
	};
	
	
}
