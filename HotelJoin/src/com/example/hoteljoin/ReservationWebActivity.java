package com.example.hoteljoin;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.euiweonjeong.base.BitmapManager;
import com.example.hoteljoin.RoomDetailData;


import com.slidingmenu.lib.SlidingMenu;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

public class ReservationWebActivity extends HotelJoinBaseActivity  {
	ReservationWebActivity  self;
	 SlidingMenu menu ;

	 private WebView mWebView;
	 
	 private final Handler handler = new Handler();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reser_web);
		
		

		// �����ε� �� ����
		setBehindContentView(R.layout.main_menu_1);
		
		self = this;
		

	    // ���α׷��� ���̾�α� 
        {
        	mProgress = new ProgressDialog(this);
    		mProgress.setMessage("��ø� ��ٷ� �ּ���.");
    		mProgress.setIndeterminate(true);
    		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		mProgress.setCancelable(false);
        }
        
		
		// �����̵� ��
		menu = getSlidingMenu();
		menu.setMode(SlidingMenu.RIGHT);
		
		{
			Display defaultDisplay = ((WindowManager)this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
	    	
			int width;
			@SuppressWarnings("deprecation")
			int windowswidth = defaultDisplay.getWidth();
			@SuppressWarnings("deprecation")
			int windowsheight= defaultDisplay.getHeight();
			int originwidth = 480;
			

			width = (int) ( (float) 340 * ( (float)(windowswidth)/ (float)(originwidth) )  );

			
			menu.setBehindOffset(windowswidth - width );
		}
		
		menu.setFadeDegree(0.35f);
		
		AfterCreate(7);
		

		mWebView = (WebView)findViewById(R.id.pay_web);

		RefreshUI();
	}


	@Override
	public void onResume()
	{
		super.onResume();
	}
	
	
	private class AndroidBridge 
	{
        public void setMessage(final String arg) { // must be final
        	handler.post(new Runnable()
        	{
                public void run() 
                {
                	Log.v("Web Message", arg);
                    if (arg.equals("fail"))
                    {
                    	new AlertDialog.Builder(self)
	   					 .setTitle("���� ����")
	   					 .setMessage("������ ���� �Ͽ����ϴ�.\n ������ ó������ �����ϰڽ��ϴ�.  ") 
	   					 .setPositiveButton("Ȯ��", new DialogInterface.OnClickListener() 
	   					 {
	   					     public void onClick(DialogInterface dialog, int whichButton)
	   					     {   
	   					    	RefreshUI();
	   					     }
	   					 })
	   					 .show();
                    }
                    else if (arg.equals("success"))
                    {
                    	new AlertDialog.Builder(self)
	   					 .setTitle("���� ����")
	   					 .setMessage("������ �����Ͽ� ������ �Ϸ�Ǿ����ϴ�  ") 
	   					 .setPositiveButton("Ȯ��", new DialogInterface.OnClickListener() 
	   					 {
	   					     public void onClick(DialogInterface dialog, int whichButton)
	   					     {   
	   					    	 Intent intent;
	   				          // Create an Intent to launch an Activity for the tab (to be reused)
		   				          intent = new Intent().setClass(self, MainActivity.class);
		   				          
		   				          
		   				          startActivity( intent ); 
	   					     }
	   					 })
	   					 .show();
                    }
                    else
                    {
                    	new AlertDialog.Builder(self)
	   					 .setTitle("���������� �޼��� �߰�")
	   					 .setMessage("���������� �޼����� �߰ߵǾ����ϴ�. \n ������ �����Ͽ����ϴ�. \n �ڼ��� ������ Ȯ�� �ϱ� ���� �Ʒ��� ��ȭ��ȣ�� ��ȭ���ֽñ� �ٶ��ϴ�.  ") 
	   					 .setPositiveButton("Ȯ��", new DialogInterface.OnClickListener() 
	   					 {
	   					     public void onClick(DialogInterface dialog, int whichButton)
	   					     {   
	   					    	RefreshUI();
	   					     }
	   					 })
	   					 .show();
                    }
                }
            });
        }
    }



/*	public void BtnEvent( int id )
    {
		View imageview = (View)findViewById(id);
		imageview.setOnClickListener(this);
    }

	
	public void ImageBtnResize( int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	View imageview = (View)findViewById(id);
        _AppManager.GetUISizeConverter().ConvertFrameLayoutView(imageview); 
        imageview.setOnClickListener(this);
    }
	
	public void ImageBtnResize2( int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	View imageview = (View)findViewById(id);
        _AppManager.GetUISizeConverter().ConvertLinearLayoutView(imageview); 
        imageview.setOnClickListener(this);
    }
	
	*/
	
	public void RefreshUI()
	{
		final AppManagement _AppManager = (AppManagement) getApplication();

		// ���信�� �ڹٽ�ũ��Ʈ���డ��        
		/*mWebView.getSettings().setJavaScriptEnabled(true); 
		// ����Ȩ������ ����        
		mWebView.loadUrl(_AppManager.m_PayURL);
		// WebViewClient ����        
		mWebView.setWebViewClient(new WebViewClientClass());  
*/
		
		mWebView.getSettings().setJavaScriptEnabled(true);  //javascript ��� �����ϰ� �Ѵ�
		// Bridge �ν��Ͻ� ���
        mWebView.addJavascriptInterface(new AndroidBridge(), "android"); 



		final Context myApp = this;

		mWebView.setWebChromeClient(new WebChromeClient() {
		    @Override
		    public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result)
		    {
		        new AlertDialog.Builder(myApp)
		           // .setTitle("AlertDialog")
		            .setMessage(message)
		            .setPositiveButton(android.R.string.ok,
		                    new AlertDialog.OnClickListener()
		                    {
		                        public void onClick(DialogInterface dialog, int which)
		                        {
		                            result.confirm();
		                        }
		                    })
		            .setCancelable(false)
		            .create()
		            .show();

		        return true;
		    };
		    
		    
		    /**
		     * �������� �ε��ϴ� ���� ���� ��Ȳ�� �����ݴϴ�.
		     * newProgress  ���� ������ �ε� ���� ��Ȳ, 0�� 100 ������ ������ ǥ��.(0% ~ 100%)
		     */
		    @Override
		    public void onProgressChanged(WebView view, int newProgress) {
		        Log.i("WebView", "Progress: " + String.valueOf(newProgress)); 
		        super.onProgressChanged(view, newProgress);
		        mProgress.setMessage("�ε��� " +  String.valueOf(newProgress) + "%");
		        if ( newProgress > 98 )
		        {
		        	if ( mProgress.isShowing() == true)
		        		mProgress.hide();
		        }
		        else 
		        {
		        	if ( mProgress.isShowing() == false)
		        		mProgress.show();
		        }
		        
		    }

		    
		    /**
		     * ���� ���� ������ �ִٰ� �˸��ϴ�.
		     * title  ������ ���ο� Ÿ��Ʋ�� ����ִ� ���ڿ�  
		     */
		    @Override
		    public void onReceivedTitle(WebView view, String title) {
		        super.onReceivedTitle(view, title);
		    }
		    /*  �Ʒ�ó�� title �±� ������ ���� �����ɴϴ�.
			<title> LG�ڷ��� ���ڰ��� ���� </title>
		     */


		    @Override
		    public Bitmap getDefaultVideoPoster() {
		        return super.getDefaultVideoPoster();
		    }


		    @Override
		    public View getVideoLoadingProgressView() {
		        return super.getVideoLoadingProgressView();
		    }


		    @Override
		    public void getVisitedHistory(ValueCallback<String[]> callback) {
		        super.getVisitedHistory(callback);
		    }


		    @Override
		    public void onCloseWindow(WebView window) {
		         super.onCloseWindow(window);
		    }


		    @Override
		    public void onConsoleMessage(String message, int lineNumber, String sourceID) {
		        super.onConsoleMessage(message, lineNumber, sourceID);
		    }


		    @Override
		    public boolean onCreateWindow(WebView view, boolean dialog,
		            boolean userGesture, Message resultMsg) {
		         return super.onCreateWindow(view, dialog, userGesture, resultMsg);
		    }

		    @Override
		    public void onGeolocationPermissionsHidePrompt() {
		         super.onGeolocationPermissionsHidePrompt();
		    }


		   

		    @Override
		    public void onHideCustomView() {
		        super.onHideCustomView();
		    }


		
		    
		    /**
		     * ���� ���������� ���� Ž���� Ȯ���ϴ� ��ȭ ���ڸ� ���÷����Ѵٰ� Ŭ���̾�Ʈ���� 
		     * �˷��ݴϴ�. �̰��� �ڹ� ��ũ��Ʈ �̺�Ʈ onbeforeunload()�� ����Դϴ�. Ŭ���̾�Ʈ�� 
		     * true�� ��ȯ�ϴ� ���, WebView�� Ŭ���̾�Ʈ�� ��ȭ ���ڸ� ó���ϰ� ������ JsResult 
		     * �޽�带 ȣ���� ���̶�� ����ϴ�. Ŭ���̾�Ʈ�� false�� ��ȯ�ϴ� ���, true�� �⺻���� 
		     * ���� ���������� ���� Ž���ϱ⸦ �����ϱ� ���� �ڹ� ��ũ��Ʈ�� ��ȯ�ϰ� �� ���Դϴ�. 
		     * �⺻ ������ false�� ��ȯ�ϴ� ���Դϴ�. JsResult�� true�� ������ ���� ���� ���������� ���� 
		     * Ž���� ���̰� false�� ������ ���� Ž���� ����� ���Դϴ�.
		     * */
		    @Override
		    public boolean onJsBeforeUnload(WebView view, String url,
		            String message, JsResult result) {
		        return super.onJsBeforeUnload(view, url, message, result);
		    }
		 
		    /**
		     * ����ڿ��� Ȯ�� ��ȭ ���ڸ� ���÷����Ѵٰ� Ŭ���̾�Ʈ���� �˷��ݴϴ�. Ŭ���̾�Ʈ�� 
		     * true�� ��ȯ�ϴ� ���, WebView�� Ŭ���̾�Ʈ�� Ȯ�� ��ȭ ���ڸ� ó���ϰ� ������ 
		     * JsResult �޽�带 ȣ���� �� �ִٰ� ����ϴ�. Ŭ���̾�Ʈ�� false�� ��ȯ�ϴ� ��� false�� 
		     * �⺻���� �ڹ� ��ũ��Ʈ�� ��ȯ�� �� �Դϴ�. �⺻ ������ false�� ��ȯ�ϴ� ���Դϴ�.
		     */
		    @Override
		    public boolean onJsConfirm(WebView view, String url, String message,
		            JsResult result) {
		         return super.onJsConfirm(view, url, message, result);
		    }
		 
		   
		 
		    /**
		     * �ڹ� ��ũ��Ʈ ���� ���� �ð��� �ʰ��ߴٰ� Ŭ���̾�Ʈ���� �˷��ݴϴ�. �׸��� 
		     * Ŭ���̾�Ʈ�� ������ �ߴ����� ���θ� ������ �� �ֽ��ϴ�. Ŭ���̾�Ʈ�� true�� ��ȯ�ϴ�
		     * ���, �ڹ� ��ũ��Ʈ�� �ߴܵ˴ϴ�. Ŭ���̾�Ʈ�� false�� ��ȯ�ϴ� ���, ��� ����˴ϴ�. 
		     * ����� �������� ���� ���¿����� ���� �ð� ī���Ͱ� �缳���ǰ�  ��ũ��Ʈ�� ���� üũ 
		     * ����Ʈ���� �Ϸ���� ���� ��� ��� �ݹ�Ǿ��� �����ϴ�.
		     */
		    @Override
		    public boolean onJsTimeout() {
		        return super.onJsTimeout();
		    }

		    @Override
		    public void onReceivedTouchIconUrl(WebView view, String url,
		            boolean precomposed) {
		        super.onReceivedTouchIconUrl(view, url, precomposed);
		    }


		    @Override
		    public void onRequestFocus(WebView view) {
		        super.onRequestFocus(view);
		    }


		    @Override
		    public void onShowCustomView(View view, CustomViewCallback callback) {
		        super.onShowCustomView(view, callback);
		    }


		});
		mWebView.loadUrl(_AppManager.m_PayURL);
		
		/*Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse((_AppManager.m_PayURL)));

		startActivity(intent);*/

	}
	
	private class WebViewClientClass extends WebViewClient 
	{         
		@Override        
		public boolean shouldOverrideUrlLoading(WebView view, String url)
		{             
			view.loadUrl(url);             
			return true;        
		}     
	}
	


	
	
	


    

}
