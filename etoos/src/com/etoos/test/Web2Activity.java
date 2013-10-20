package com.etoos.test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

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
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

public class Web2Activity extends EtoosBaseActivity  implements OnClickListener{

	private WebView mWebView;
	
	private final Handler handler = new Handler();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview2);
		
		
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
		
		BtnEvent( R.id.bottom_1);
		BtnEvent( R.id.bottom_2);
		BtnEvent( R.id.bottom_3);
		BtnEvent( R.id.bottom_4);
		
		IninWebView();
		
		// �ʱ� �������� ���� 
		OpenWebView("http://www.html-kit.com/tools/cookietester/");
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
	
	
	public void BtnEvent( int id )
    {
		ImageView imageview = (ImageView)findViewById(id);
		imageview.setOnClickListener(this);
    }
	
	
	// �ڷΰ��� 
	@Override
	public boolean onKeyDown( int keyCode, KeyEvent event )
	{
		if( (keyCode == KeyEvent.KEYCODE_BACK) )
		{
			if( mWebView != null && mWebView.canGoBack() == true )
			{
				mWebView.goBack();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
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
	
	private class WebViewClientClass extends WebViewClient 
	{      
		// Ư�� �������� �ּҸ� ȣ���Ҷ� ������ �Լ�. 
		@Override        
		public boolean shouldOverrideUrlLoading(WebView view, String url)
		{             
			String strDecodedUrl = url;


			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			InitCookie();
			/*String temp  = cookieManager.getCookie(strDecodedUrl) ;
			

			if ( temp != null)
				Log.d("Cookie" , temp);*/
			try
			{
				strDecodedUrl = URLDecoder.decode(url, "utf-8");
			}
			catch( UnsupportedEncodingException e )
			{
				e.printStackTrace();
			}

			if( strDecodedUrl.startsWith("tel:") )
			{
				try
				{
					Uri uri = Uri.parse(strDecodedUrl);
					Intent intent = new Intent(Intent.ACTION_DIAL, uri);
					startActivity(intent);
					return true;
				}
				catch( ActivityNotFoundException e )
				{
					e.printStackTrace();
				}
				catch( Exception e )
				{
					e.printStackTrace();
				}
			}
			else if (url.endsWith("pricecode://goods_detail_backbutton"))
			{
				
				//self.HideWebview();
				return true;
			}
			
			else if (url.startsWith("pricecodelogin://"))
			{

	    		return true;
			}
			
			// �α��� �� ó�� 
			else if ( url.startsWith("etooslogin://") )
			{
				
				// ��Ű �Ŵ����� ���� ��Ű�� �����´�. 
				CookieManager cookieManager = CookieManager.getInstance();
				cookieManager.setAcceptCookie(true);
				
				CookieSyncManager.getInstance().sync();


			    String cookie = cookieManager.getCookie(url);
			    
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
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
		switch( arg0.getId() )
		{
		case R.id.bottom_1:
			{
				Intent intent;
		          // Create an Intent to launch an Activity for the tab (to be reused)
		          intent = new Intent().setClass(baseself, FastTestMain.class);
		          startActivity( intent ); 
			}
			break;
		case R.id.bottom_2:
			baseself.ShowAlertDialLog(baseself, "OMR ã�� ", "OMR ã��");
			break;
		case R.id.bottom_3:
			baseself.ShowAlertDialLog(baseself, "��������", "��������");
			break;
		case R.id.bottom_4:
			baseself.ShowAlertDialLog(baseself, "������", "������");
			break; 
		}
		
	}
	

	

}
