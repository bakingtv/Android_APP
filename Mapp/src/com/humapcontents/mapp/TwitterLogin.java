package com.humapcontents.mapp;



import com.humapcontents.mapp.data.TwitterConstant;

import android.app.*;
import android.content.*;
import android.os.*;
import android.webkit.*;

public class TwitterLogin extends Activity {

     Intent mIntent;
 
     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.twitter_login);
 
         WebView webView = (WebView) findViewById(R.id.webView1);
 
         // ȭ�� ��ȯ�� WebView���� ȭ�� ��ȯ�ϵ����Ѵ�.
         // �̷������� ������ ǥ�� �������� ���� ������.
         webView.setWebViewClient(new WebViewClient() {
             public void onPageFinished(WebView view, String url) {
                 // page �������� �Ϸ�Ǹ� ȣ���.
                 super.onPageFinished(view, url);
 
                 // �α׾ƿ��� ó�����Ŀ��� �ٷ� Activity�� �����Ų��.
                 if (url != null && url.equals("http://mobile.twitter.com/")) {
                     finish();
                 } else if (url != null && url.startsWith(TwitterConstant.TWITTER_CALLBACK_URL)) {
                     String[] urlParameters = url.split("\\?")[1].split("&");
                     String oauthToken = "";
                     String oauthVerifier = "";
 
                     try {
                         if (urlParameters[0].startsWith("oauth_token")) {
                             oauthToken = urlParameters[0].split("=")[1];
                         } else if (urlParameters[1].startsWith("oauth_token")) {
                             oauthToken = urlParameters[1].split("=")[1];
                         }
 
                         if (urlParameters[0].startsWith("oauth_verifier")) {
                             oauthVerifier = urlParameters[0].split("=")[1];
                         } else if (urlParameters[1].startsWith("oauth_verifier")) {
                             oauthVerifier = urlParameters[1].split("=")[1];
                         }
 
                         mIntent.putExtra("oauth_token", oauthToken);
                         mIntent.putExtra("oauth_verifier", oauthVerifier);
 
                         setResult(RESULT_OK, mIntent);
                         finish();
                     } catch (Exception e) {
                         e.printStackTrace();
                     }
                 }
             }
         });
         mIntent = getIntent();
         String url1 = mIntent.getStringExtra("auth_url");
         webView.loadUrl(url1);
     }
 }