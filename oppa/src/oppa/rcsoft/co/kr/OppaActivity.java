package oppa.rcsoft.co.kr;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


 // ��Ʈ�� 
public class OppaActivity extends Activity {    
	private Handler handler = new Handler();
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
       
        
        setContentView(R.layout.introlayout);          // ��Ʈ�� ���̾ƿ� ���      
        
        handler.postDelayed(TimerRunner, 2000);   		// 2�� �ڿ� ȣ��
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
           handler.removeCallbacks(TimerRunner);   // ����� ����
              
           Intent intent;
            // Create an Intent to launch an Activity for the tab (to be reused)
           intent = new Intent().setClass((OppaActivity)parentActivity, login.class);
          
           startActivity( intent );           
               
    	} 
    } 
}