package oppa.rcsoft.co.kr;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


// �Խù� �� ���� �Ű��ϱ� ..
public class BBS_Singo  extends BaseActivity implements OnClickListener
{
	static public BBS_Singo self;
	public String m_Title;
	public String m_Content;
	
	public boolean m_bReply;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        self = this;
        setContentView(R.layout.bbs_detail_report);
    
        
        mProgress = new ProgressDialog(this);
		mProgress.setMessage("��ø� ��ٷ� �ֽʽÿ�.");
		mProgress.setIndeterminate(true);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);
		m_bReply = false;
		
		
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
		
		
		
		Button itemAddSingo = (Button)findViewById(R.id.sendreportbtn);
		itemAddSingo.setVisibility(View.VISIBLE);
		
		// Ŭ���̺�Ʈ �߰�     					
		itemAddSingo.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v )
	        {
	        	switch(v.getId())
	        	{
	        	case R.id.sendreportbtn:
	        	{
	        		SendReport();
					
	        	}
	        		break;
	        	}
	        }
		});
		
		RefreshUI();
    }
    
    public void RefreshUI()
    {
        setContentView(R.layout.bbs_detail_report);
    
        
        mProgress = new ProgressDialog(this);
		mProgress.setMessage("��ø� ��ٷ� �ֽʽÿ�.");
		mProgress.setIndeterminate(true);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);
		m_bReply = false;
		
		
		Button itemAddSingo = (Button)findViewById(R.id.sendreportbtn);
		itemAddSingo.setVisibility(View.VISIBLE);
		
		// Ŭ���̺�Ʈ �߰�     					
		itemAddSingo.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v )
	        {
	        	switch(v.getId())
	        	{
	        	case R.id.sendreportbtn:
	        	{
	        		SendReport();
					
	        	}
	        		break;
	        	}
	        }
		});
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
  				// ���������� �޼����� ���� �Ǿ��� ��� 
  				// ����� �ϳ��� ������ ���� 
  				onBackPressed();
  				m_bReply = false;
  				message = "�Ű� �Ǿ����ϴ�.";
  				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
  				
  				BBS_Viewer.self.RefreshUI();
  			}
  				break;
  			case 1:
  				message = "No data";
  				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
  				break;
  			case 2:
  				self.ShowAlertDialLog( self.getParent() ,"����" , (String) msg.obj );
  				break;
  			case 3:
  				break;
  			default:

  				break;
  			}

  		}
  	};
  	
    // ���� ����
    void SendReport(  )
    {
    	mProgress.show();
    	final MyInfo myApp = (MyInfo) getApplication();
    	{
    		 
			Thread thread = new Thread(new Runnable()
			{
				public void run() 
				{
					Map<String, String> data = new HashMap  <String, String>();

					data.put("bo_table", myApp.m_BBSSingo.bo_table);
					data.put("wr_id",  myApp.m_BBSSingo.wr_id);
					
					data.put("return_type", "json");
					
					String Reason = "";
					
					CheckBox box1 = (CheckBox)findViewById(R.id.report_check1);
					CheckBox box2 = (CheckBox)findViewById(R.id.report_check2);
					CheckBox box3 = (CheckBox)findViewById(R.id.report_check3);
					CheckBox box4 = (CheckBox)findViewById(R.id.report_check4);
					
					if ( box1.isChecked() )
					{
						Reason = " " + "�ҹ� ȫ�� �Խù�" ;
					}
					

					if ( box2.isChecked() )
					{
						Reason += " " + "���ϳ����� �Խñ� ����" ;
					}
					

					if ( box3.isChecked() )
					{
						Reason += " " + "������ �Խù�" ;
					}
					

					if ( box4.isChecked() )
					{
						EditText text1 = (EditText)findViewById(R.id.report_content);
						Reason += " " + text1.getText();
					}

					data.put("rp_reason", Reason);
					
					String strJSON = myApp.PostHTTPData("http://oppa.rcsoft.co.kr/api/oppa_report.php", data);	
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

	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
}
