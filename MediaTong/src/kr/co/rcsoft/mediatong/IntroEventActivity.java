package kr.co.rcsoft.mediatong;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.euiweonjeong.base.BaseActivity;
import com.euiweonjeong.base.ImageViewDoubleTab;
import com.euiweonjeong.base.ImageViewDoubleTabListener;
import com.euiweonjeong.base.UISizeConverter;



public class IntroEventActivity extends BaseActivity implements OnClickListener , ImageViewDoubleTabListener {

	
	
	public Integer 	bd_idx;
	public String  	bd_title;
	public String  	bd_contents;
	public Bitmap 	bd_file;
	
	IntroEventActivity self;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_event);  // ��Ʈ�� ���̾ƿ� ���      
        
        
        {
        	mProgress = new ProgressDialog(this);
    		mProgress.setMessage("��ø� ��ٷ� �ּ���.");
    		mProgress.setIndeterminate(true);
    		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		mProgress.setCancelable(false);
        }
        
        self = this;
        
        AppManagement _AppManager = (AppManagement) getApplication();
        
        
        // ���̾ƿ� ������¡ 
        {
            LinearLayout layout = (LinearLayout)findViewById(R.id.intro_event_image_layout);
            _AppManager.GetUISizeConverter().ConvertLinearLayout(layout);
            
            // �ڵ����� ������ ��������. 
            LinearLayout layout2 = (LinearLayout)findViewById(R.id.intro_event_bottom);
            _AppManager.GetUISizeConverter().ConvertLinearLayout(layout2);       	
        }
        // �̹��� ������¡ & �̺�Ʈ 
        {
        	ImageView imageview = (ImageView)findViewById(R.id.intro_event_close);
            _AppManager.GetUISizeConverter().ConvertLinearLayoutImage(imageview);  
            imageview.setOnClickListener(this);
            
        }
        {
        	CheckBox box = (CheckBox)findViewById(R.id.intro_event_checkbox);
        	box.setOnClickListener(this);
        }
        
        {
        	ImageViewDoubleTab view = (ImageViewDoubleTab)findViewById(R.id.intro_event_image);
        	
        	view.SetDoubleTabListener(this);
        }
        GetEventList();
        
    }
    
    
    public void GetEventList()
    {
    	mProgress.show();
    	final AppManagement _AppManager = (AppManagement) getApplication();

    	
    	{
			
			Thread thread = new Thread(new Runnable()
			{
				public void run() 
				{
					Map<String, String> data = new HashMap  <String, String>();


					data.put("return_type", "json");
					
					
					String strJSON = _AppManager.GetHttpManager().PostCookieHTTPData("http://mediatong.rcsoft.co.kr/api/api_getEventPopupData.php", data);
					
					
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
							
							

							bd_title = _AppManager.GetHttpManager().DecodeString(json.getString("bd_title"));
							bd_contents = _AppManager.GetHttpManager().DecodeString(json.getString("bd_contents"));
							
							
							// �ΰ� �̹��� 
							if ( json.getString("bd_file") ==  null ||json.getString("bd_file").equals("") )
							{
								
							}
							else
							{
								URL imgUrl = new URL( _AppManager.GetHttpManager().DecodeString(json.getString("bd_file") ));
								URLConnection conn = imgUrl.openConnection();
								conn.connect();
								BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(),512 * 1024);
								bd_file = BitmapFactory.decodeStream(bis);
								bis.close();									
							}
							
							handler.sendEmptyMessage(0);
							
						}
						
						else
						{
							handler.sendMessage(handler.obtainMessage(2,"����" ));
						}
					}
					
					catch (JSONException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
						handler.sendMessage(handler.obtainMessage(2,"����" ));
					} catch (MalformedURLException e) {
						// TODO �ڵ� ������ catch ���
						e.printStackTrace();
						handler.sendMessage(handler.obtainMessage(2,"����" ));
					} catch (IOException e) {
						// TODO �ڵ� ������ catch ���
						e.printStackTrace();
						handler.sendMessage(handler.obtainMessage(2,"����" ));
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
			{
				ImageView star1 = (ImageView)findViewById(R.id.intro_event_image);
	   			
	   			if ( bd_file != null )
	   			{
	   				AppManagement _AppManager = (AppManagement) getApplication();
	   		        _AppManager.GetUISizeConverter().ConvertLinearLayoutImage(star1); 
	   		        
	   		        Drawable d =new BitmapDrawable(getResources(),bd_file);
	   		        
	   		        // API 10
	   				star1.setBackgroundDrawable( d );
	   				
	   				// API 16
	   				//star1.setBackground( d );
	   			}
			}

				break;
			case 1:
				message = "No data";
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
				break;
			case 2:
			{
				Intent intent;
	            intent = new Intent().setClass(self, HomeActivity.class);
	            startActivity( intent ); 
			}

				break;

			}
		}
	};
	
    
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


	public void onClick(View v) {
		// TODO �ڵ� ������ �޼ҵ� ����
		
		switch(v.getId())
    	{
    	case R.id.intro_event_close:
    	{
            Intent intent;
            intent = new Intent().setClass(this, HomeActivity.class);
            startActivity( intent ); 
			
    	}
    		break;
    	case R.id.intro_event_checkbox:	
    	{
    		CheckBox box = (CheckBox)findViewById(R.id.intro_event_checkbox);
    		
    		Date today = new Date(); 
            
            SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd"); 
             
            
    		if ( box.isChecked() )
    		{
    			SharedPreferences prefdefault = getSharedPreferences("Event", MODE_PRIVATE); 
    		    SharedPreferences.Editor edit = prefdefault.edit();
    		    edit.putString("CAL", date.format(today));  
    		    edit.commit(); 

    		}
    		else
    		{
    			SharedPreferences prefdefault = getSharedPreferences("Event", MODE_PRIVATE); 
    		    SharedPreferences.Editor edit = prefdefault.edit();
    		    edit.putString("CAL", "0");  
    		    edit.commit(); 

    		}
    	}

    	default:
    		break;
    	}
		
	}
	
	class GestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onDoubleTap(MotionEvent e) 
        {
            // doubletap�� �߻��Ҷ� ����.
            return true;
        }
	}

	public void ImageViewDoubleTabEvent(View v) {
		// TODO �ڵ� ������ �޼ҵ� ����
		
		// ������ �̺�Ʈ 
		
		switch ( v.getId() )
		{
		case R.id.intro_event_image:
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.mediatong.rcsoft.co.kr/company/board_list.php?bd_name=event_result")));
			Log.d("doubleTab", "doubleTab");
			break;
		}
		
	}

}
