package com.utopia.holytube;


import android.app.Activity;
import android.app.ActivityManager;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;
import android.view.Window;

public class Intro extends Activity {    
	private Handler handler = new Handler();
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
       
        
        setContentView(R.layout.intro);          // ��Ʈ�� ���̾ƿ� ���      

        mainSingleton myApp = (mainSingleton) getApplication();
        myApp.activityList1.add(this);
        handler.postDelayed(TimerRunner, 2000);   		// 2�� �ڿ� ȣ��
    }
    
    
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
            // calculate status bar height
            Rect rect = new Rect();
            getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);

            
            mainSingleton myApp = (mainSingleton) getApplication();
            myApp.StatusBarSize = rect.top;
            
            //RefreshUI();
            
            super.onWindowFocusChanged(hasFocus);
    }
    
    // **********************************************************************
    // Ÿ�̸� ó��
    // **********************************************************************    
    private Runnable TimerRunner = new IntroRunnAble(this);
    public class IntroRunnAble implements Runnable 
    { 
    	 
    	Object parentActivity;
    	public IntroRunnAble(Object parameter)
    	{
    		   
    	    // store parameter for later user 
    	   parentActivity = parameter;
    	   
    	   
    	} 
    	 
    	public void run() 
    	{ 
    		
    		{
    			Rect rect = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);

                
                mainSingleton myApp = (mainSingleton) getApplication();
                myApp.CreateUISizeConverter(rect.top);
    		}
           handler.removeCallbacks(TimerRunner);   // ����� ����
              
           Intent intent;
            // Create an Intent to launch an Activity for the tab (to be reused)
           intent = new Intent().setClass((Intro)parentActivity, HolytubeActivity.class);
          
           startActivity( intent );           
               
    	} 
    } 
}