package com.etoos.test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.StringTokenizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.etoos.data.EventData;
import com.google.zxing.client.android.integration.IntentIntegrator;
import com.google.zxing.client.android.integration.IntentResult;

import android.net.Uri;
import android.os.Bundle;

import android.webkit.JsResult;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ResultWebActivity extends EtoosBaseActivity {

	private WebView mWebView;
	
	private final Handler handler = new Handler();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
		
		
		// ���α׷��� ���̾�α� 
        {
        	mProgress = new ProgressDialog(this);
    		mProgress.setMessage("��ø� ��ٷ� �ּ���.");
    		mProgress.setIndeterminate(true);
    		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		mProgress.setCancelable(false);
        }
		
		AfterCreate();
		
		mWebView = (WebView)findViewById(R.id.webView);
		
		IninWebView();
		
		// �ʱ� �������� ���� 
		{
			AppManagement _AppManager = (AppManagement) getApplication();
			
			OpenWebView(_AppManager.WEB_URL);
		}

	}

	
	
	/// �ȵ���̵� �޼���  �� -> �ȵ���̵� 
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

                    }
                    else
                    {

                    }
                }
            });
        }
    }
	
	
	public void IninWebView()
	{

		mWebView.getSettings().setJavaScriptEnabled(true);  
		// Bridge �ν��Ͻ� ���
        mWebView.addJavascriptInterface(new AndroidBridge(), "android"); 




        WebViewClientClass viewClient = new WebViewClientClass();
        mWebView.setWebViewClient(viewClient);

        ThisWebCromeClient cromeclient = new ThisWebCromeClient();
		mWebView.setWebChromeClient( cromeclient );

	}
	
	
	
	
	public void OpenWebView( String URL )
	{
		mWebView.loadUrl(URL);
	}
	
	
	
	private class ThisWebCromeClient extends WebChromeClient
	{
		final Context myApp = baseself;
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
		    public void onProgressChanged(WebView view, int newProgress) 
		    {
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
		    public void onReceivedTitle(WebView view, String title)
		    {
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
	}
	

	
	
	// �ڷΰ��� 
	@Override
	public boolean onKeyDown( int keyCode, KeyEvent event )
	{
		/*if( (keyCode == KeyEvent.KEYCODE_BACK) )
		{
			if( mWebView != null && mWebView.canGoBack() == true )
			{
				mWebView.goBack();
				return true;
			}

		}*/
		

		return super.onKeyDown(keyCode, event);
	}
	
	private class WebViewClientClass extends WebViewClient 
	{      
		// Ư�� �������� �ּҸ� ȣ���Ҷ� ������ �Լ�. 
		@Override        
		public boolean shouldOverrideUrlLoading(WebView view, String url)
		{             
			String strDecodedUrl = url;
			
			
			/*{
				CookieManager cookieManager = CookieManager.getInstance();
				cookieManager.setAcceptCookie(true);
				
				String cookie = cookieManager.getCookie("http://xx.xxx.xxx.com");

			    Log.d("Cookie", "cookie ------>"+cookie);
			}*/
			try
			{
				strDecodedUrl = URLDecoder.decode(url, "utf-8");
			}
			catch( UnsupportedEncodingException e )
			{
				e.printStackTrace();
			}

			if (url.startsWith("etoos://login"))
			{
				InitCookie();
				
				// ��Ű �Ŵ����� ���� ��Ű�� �����´�. 
				CookieManager cookieManager = CookieManager.getInstance();
				cookieManager.setAcceptCookie(true);
				
				CookieSyncManager.getInstance().sync();


			    String cookie = cookieManager.getCookie(url);
			    
			    if ( cookie != null)
			    {
				    Log.d("Cookie", "cookie ------>"+cookie);

				    // ���信�� ������ ��Ű�� httpclient �� �Է����ش�. 
				    SaveCookie(url);

					
					try {
						Thread.sleep(500);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					CookieSyncManager.getInstance().sync();
			    }

				
				
				
				{
					AppManagement _AppManager = (AppManagement) getApplication();
					_AppManager.ParamData.clear();
					
					
					{
						
					
						String value = url.replace("etoos://login?", "");
						{
							String  id  = value.split("&")[0].split("=")[1];
							String uid = value.split("&")[1].split("=")[1];
							
							_AppManager.ParamData.put("email", id);
							_AppManager.ParamData.put("uid", uid);
							_AppManager.ParamData.put("ostype", "android");
							_AppManager.ParamData.put("token", "1");
						}

					}


					Intent intent = new Intent(baseself, NetPopup.class);
					intent.putExtra("API", 1);
					startActivityForResult(intent , 1);
				}
				
	    		return true;
			}
			return super.shouldOverrideUrlLoading(view, url);             
		}  
		
		// ������ �ε� ���� 
		@Override
		public void onPageStarted( WebView view, String url, Bitmap favicon )
		{
			super.onPageStarted(view, url, favicon);
			
		}

		// ������ �ε� ����. 
		@Override
		public void onPageFinished( WebView view, String url )
		{
			super.onPageFinished(view, url);
			
		}
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		
		AppManagement _AppManager = (AppManagement) getApplication();
		if ( requestCode == 1 )
		{
			if ( resultCode == 10 )
			{
				_AppManager.m_LoginCheck = false;
				_AppManager.LogInDataParsing();
				
				if ( _AppManager.m_ResultCode.equals("success"))
				{
					_AppManager.m_LoginCheck = true;
					
					// �α��� �Ϸ� �ڿ��� �ڷ� ����. 
					onBackPressed();
				}
				
				else
				{
					
					baseself.ShowAlertDialLog(baseself, _AppManager.m_ResultMsg);
					
				}
				
				
			}
		}
		else
		{
			// QR�ڵ�/���ڵ带 ��ĵ�� ��� ���� �����ɴϴ�.
			IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

			if ( result.getContents() != null)
			{
				OpenWebView(result.getContents());
			}
			else
			{
				baseself.ShowAlertDialLog(baseself, "QR �ڵ� �νĿ� ���� �߽��ϴ�.");
			}
		}

		

	}

	
	

	

}
