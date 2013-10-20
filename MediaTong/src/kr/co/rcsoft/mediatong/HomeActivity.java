package kr.co.rcsoft.mediatong;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.euiweonjeong.base.BaseActivity;
import com.euiweonjeong.base.UISizeConverter;


public class HomeActivity extends BaseActivity implements OnClickListener {

	private HomeActivity self;
	
	String PCVerAddress= "";
	String PRAddress= "";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);  // ��Ʈ�� ���̾ƿ� ���     
        
        
        self = this;
        
        // ���α׷��� ���̾�α� 
        {
        	mProgress = new ProgressDialog(this);
    		mProgress.setMessage("��ø� ��ٷ� �ּ���.");
    		mProgress.setIndeterminate(true);
    		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		mProgress.setCancelable(false);
        }
		
		
        
        AppManagement _AppManager = (AppManagement) getApplication();
        
        // ���̾ƿ� ������¡ 
        {
            FrameLayout layout = (FrameLayout)findViewById(R.id.home_head);
            _AppManager.GetUISizeConverter().ParentLinearConvertFrameLayout(layout);
            
            FrameLayout layout2 = (FrameLayout)findViewById(R.id.home_middle);
            _AppManager.GetUISizeConverter().ParentLinearConvertFrameLayout(layout2);
            
            FrameLayout layout3 = (FrameLayout)findViewById(R.id.home_foot);
            _AppManager.GetUISizeConverter().ParentLinearConvertFrameLayout(layout3);
                 	
        }
        // �̹��� ������¡ & �̺�Ʈ 
        {
            ImageBtnResize(R.id.home_login );
            ImageBtnResize(R.id.home_recruit );
            ImageBtnResize(R.id.home_recruit2 );
            ImageBtnResize(R.id.home_edu );
            ImageBtnResize(R.id.home_scrap );
            ImageBtnResize(R.id.home_event );
            ImageBtnResize(R.id.home_myjob );
            ImageBtnResize(R.id.home_setting );
            ImageBtnResize(R.id.home_pr );
            ImageBtnResize(R.id.home_pc );
            //
        }
        
        // PR�� Ȩ������ �ּ� �������� 
        {
        	GetAddress();
        }
        
        
        // �α��� �Ǿ��ִ��� üũ 
        {
        	if ( _AppManager.m_LoginCheck == true )
        	{
        		ImageView imageview = (ImageView)findViewById(R.id.home_login);
        		imageview.setVisibility(View.GONE);
        	}
        	else
        	{
        		ImageView imageview = (ImageView)findViewById(R.id.home_login);
        		imageview.setVisibility(View.VISIBLE);
        	}
        	
        }
        
    }
    
    
    public void GetAddress()
    {
    	if ( mProgress.isShowing() == true)
	   		return;
		mProgress.show();
		
		final AppManagement _AppManager = (AppManagement) getApplication();
		
		Thread thread = new Thread(new Runnable()
		{
			public void run() 
			{
				Map<String, String> data = new HashMap  <String, String>();
				data.put("return_type", "json");
				
				String strJSON = _AppManager.GetHttpManager().GetHTTPData("http://mediatong.rcsoft.co.kr/api/api_getLinkData.php", data);
				
				try 
				{
					JSONObject json = new JSONObject(strJSON);
					if(json.getString("result").equals("ok"))
					{
						 PCVerAddress = _AppManager.GetHttpManager().DecodeString(json.getString("pc_mode_index"));
						 PRAddress = _AppManager.GetHttpManager().DecodeString(json.getString("pc_mode_tong_index"));
						 
						 _AppManager.m_PCVerAddress = PCVerAddress;
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
					e.printStackTrace();
				}
			}
		}
		);
		
		thread.start();
    }
    
    
    public void ImageBtnResize( int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	ImageView imageview = (ImageView)findViewById(id);
        _AppManager.GetUISizeConverter().ConvertFrameLayoutImage(imageview); 
        imageview.setOnClickListener(this);
    }
    
    @Override
    public void onBackPressed() 
    {
    	// ���� ȭ������ �� ���ư��� ���´�.( �ƿ� ���ᰡ �ǵ����Ѵ� )
    	// �ϴ��� ���ư��� �ְ� �׽�Ʈ������ ����д�. 
    	new AlertDialog.Builder(this)
		 .setTitle("���� Ȯ��")
		 .setMessage("���� ���� �ϰڽ��ϱ�?") //�ٿ���
		 .setPositiveButton("��", new DialogInterface.OnClickListener() 
		 {
		     public void onClick(DialogInterface dialog, int whichButton)
		     {   
				moveTaskToBack(true);
			    //android.os.Process.killProcess(android.os.Process.myPid());
		    	 for ( int i = 0; i < activityList1.size() ; i++ )
		    		 activityList1.get(i).finish();
		    	 
		    	 android.os.Process.killProcess(android.os.Process.myPid());

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

				break;
				
			case 3:
	
				break;
			default:
				break;
			}

		}
    	
	};


	public void onClick(View arg0) 
	{
		// TODO �ڵ� ������ �޼ҵ� ����

        
        switch(arg0.getId())
    	{
    	case R.id.home_login:
	    	{
	    		Intent intent;
	            intent = new Intent().setClass(this, LoginActivity.class);
	            startActivity( intent );
	    		
	    	}
	    	break;
    	case R.id.home_recruit:
    	{
    		Intent intent;
            intent = new Intent().setClass(this, RecruitFindActivity.class);
            startActivity( intent );
    		
    	}
    	break;
    	case R.id.home_recruit2:
    	{
    		Intent intent;
            intent = new Intent().setClass(this, Recruit2Activity.class);
            startActivity( intent );
    		
    	}
    	break;
    	case R.id.home_edu:
    	{
    		Intent intent;
            intent = new Intent().setClass(this, EduListActivity.class);
            startActivity( intent );
    	}
    	break;
    	case R.id.home_scrap:
    	{
    		final AppManagement _AppManager = (AppManagement) getApplication();
    		if ( _AppManager.m_LoginCheck == true )
    		{
   			
    			Intent intent;
                intent = new Intent().setClass(self, ScrapMainActivity.class);
                startActivity( intent );

    		}
    		else
    		{
    			self.ShowAlertDialLog( self ,"�α��� ����" , "�α����� �ؾ� �� �޴��� ����Ҽ� �ֽ��ϴ�." );
    		}
    		
 		
    	}
    	break;
    	case R.id.home_event:
    	{
    		Intent intent;
            intent = new Intent().setClass(this, EventMainActivity.class);
            startActivity( intent );
           
    	}
    	break;
    	case R.id.home_myjob:
    	{
    		
    		
    		AppManagement _AppManager = (AppManagement) getApplication();
    		if ( _AppManager.m_LoginCheck == true )
    		{
    			Intent intent;
                intent = new Intent().setClass(this, MyJobListActivity.class);
                startActivity( intent );
                
    		}
    		else
    		{
    			self.ShowAlertDialLog( self ,"�α��� ����" , "�α����� �ؾ� �� �޴��� ����Ҽ� �ֽ��ϴ�." );
    		}
    	}
    	break;
    	case R.id.home_setting:
    	{
    		
    		Intent intent;
            intent = new Intent().setClass(this, SettingActivity.class);
            startActivity( intent );
    	}
    	break;
    	case R.id.home_pr:
    	{
    		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(PRAddress)));
    	}
    	break;
    	case R.id.home_pc:
    	{
    		// �������� ����
    		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(PCVerAddress)));
    	}
    	break;
    	
    	}
	}

}