package kr.co.rcsoft.mediatong;


import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.euiweonjeong.base.BaseActivity;


public class LoginActivity extends BaseActivity implements OnClickListener {

	LoginActivity self;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);  // ��Ʈ�� ���̾ƿ� ���  
        
        self = this;
        // ���α׷��� ���̾�α� 
        {
        	mProgress = new ProgressDialog(this);
    		mProgress.setMessage("��ø� ��ٷ� �ּ���.");
    		mProgress.setIndeterminate(true);
    		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		mProgress.setCancelable(false);
        }
        
        // �ؽ�Ʈ�� �����۸�ũ 
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	TextView  link = (TextView)findViewById(R.id.login_link);
        	link.setText(Html.fromHtml("ȸ�����԰� ���̵�/��й�ȣ ã��� �̵���� ������Ʈ (<a href=\"http://www.mediatong.com\" >www.mediatong.com</a> )�� �����Ͽ� Ȯ���� �ֽñ� �ٶ��ϴ�. "));
        	link.setMovementMethod(LinkMovementMethod.getInstance());
        	_AppManager.GetUISizeConverter().ConvertFrameLayoutTextView(link);
        }
        
        // ���̾ƿ� ������¡
        // ����� ������¡ �ϸ� ��. 
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	FrameLayout layout = (FrameLayout)findViewById(R.id.login_head);
            _AppManager.GetUISizeConverter().ParentLinearConvertFrameLayout(layout);
        }
        
        
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	LinearLayout layout =  (LinearLayout)findViewById(R.id.login_btn);
        	_AppManager.GetUISizeConverter().ParentFrameConvertLinearLayout(layout);
            layout.setOnClickListener(this);
        }
        
        
        ImageBtnResize(R.id.login_back );
        ImageBtnResize(R.id.login_recruit );
        
        
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	EditText IDBOX = (EditText)findViewById(R.id.login_id);
        	_AppManager.GetUISizeConverter().ConvertFrameLayoutEditText(IDBOX);
        }
        {
        	AppManagement _AppManager = (AppManagement) getApplication();
        	EditText Password = (EditText)findViewById(R.id.login_pass);
        	_AppManager.GetUISizeConverter().ConvertFrameLayoutEditText(Password);
        }
        
        
        
       
        
    }
    
    
    
    
    public void ImageBtnResize( int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	ImageView imageview = (ImageView)findViewById(id);
        _AppManager.GetUISizeConverter().ConvertFrameLayoutImage(imageview); 
        imageview.setOnClickListener(this);
    }
    
    
    public void Login()
    {
    	{
			final  AppManagement _AppManager = (AppManagement) getApplication();
			mProgress.show();
			Thread thread = new Thread(new Runnable()
			{

				public void run() 
				{
					// ID�� �н����带 �����´� 
					EditText IDBOX = (EditText)findViewById(R.id.login_id);
					String ID = IDBOX.getText().toString();
					
					EditText Password = (EditText)findViewById(R.id.login_pass);
					String pass = Password.getText().toString();

					
					Map<String, String> data = new HashMap  <String, String>();
					data.put("mb_pass", pass );
					data.put("mb_id", ID);
					
					data.put("return_type", "json");
					
					String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData("http://mediatong.rcsoft.co.kr/api/api_memberLogin.php", data);
					
					try 
					{
						JSONObject json = new JSONObject(strJSON);
						if(json.getString("result").equals("ok"))
						{

							handler.sendEmptyMessage(0);
						}
						else 
						{
							// ���� �޼����� �����Ѵ�. 
							handler.sendMessage(handler.obtainMessage(1,_AppManager.GetHttpManager().DecodeString(json.getString("result_text")) ));
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
    
    final Handler handler = new Handler( )
	{
    	
    	
    	public void handleMessage(Message msg)
		{
			mProgress.dismiss();
			switch(msg.what)
			{
			case 0:
				// Ȩ ȭ������ ����. 
				Intent intent;
	            intent = new Intent().setClass(self, HomeActivity.class);
	            startActivity( intent ); 
	            
	            // �α��� üũ�� �Ѵ�. 
	            AppManagement _AppManager = (AppManagement) getApplication();
	            _AppManager.m_LoginCheck = true;
				break;
			case 1:
				// ����ó�� 
				self.ShowAlertDialLog( self ,"����" , (String) msg.obj );
				
				break;
			case 2:

				break;
				
			case 3:
	
				break;
			default:
				break;
			}

		}
    	
	};
	
	

	
    
	public void onClick(View arg0) {
		// TODO �ڵ� ������ �޼ҵ� ����
		switch(arg0.getId())
    	{
		case R.id.login_btn:
		{
			Login();
		}
			break;
		case R.id.login_back:
		{
			onBackPressed();
		}
			break;
	    	case R.id.login_recruit:
	    	{
	    		Intent intent;
	            intent = new Intent().setClass(this, Recruit2Activity.class);
	            startActivity( intent );
	    	}
			break;
			
    	}
	}
    



}