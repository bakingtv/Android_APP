package kr.co.rcsoft.mediatong;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.euiweonjeong.base.BaseActivity;

public class NoticeDetailActivity extends BaseActivity implements OnClickListener{


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_detail_layout);  // 인트로 레이아웃 출력      
        AppManagement _AppManager = (AppManagement) getApplication();
        {
        	
        	FrameLayout layout = (FrameLayout)findViewById(R.id.notice_detail_head);
            _AppManager.GetUISizeConverter().ParentLinearConvertFrameLayout(layout);
        }
        
      {
        	
    	  LinearLayout layout = (LinearLayout)findViewById(R.id.notice_detail_list_row_1);
            _AppManager.GetUISizeConverter().ParentLinearConvertLinearLayout(layout);
        }
        
        ImageBtnResize(R.id.notice_detail_back );
        
        ((TextView)findViewById(R.id.notice_detail_list_row_title)).setText(_AppManager.m_NoticeData.bd_title);  
        ((TextView)findViewById(R.id.notice_detail_list_row_content)).setText(_AppManager.m_NoticeData.bd_contents);  
    
    }
    
    public void ImageBtnResize( int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	ImageView imageview = (ImageView)findViewById(id);
        _AppManager.GetUISizeConverter().ConvertFrameLayoutImage(imageview); 
        imageview.setOnClickListener(this);
    }

	public void onClick(View arg0) {
		// TODO 자동 생성된 메소드 스텁
		
		switch( arg0.getId())
		{
		case R.id.notice_detail_back:
		{
			onBackPressed();
		}
		break;
		}

		
	}
}