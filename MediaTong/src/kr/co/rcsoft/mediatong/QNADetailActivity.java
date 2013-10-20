package kr.co.rcsoft.mediatong;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.euiweonjeong.base.BaseActivity;

public class QNADetailActivity extends BaseActivity implements OnClickListener{


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qna_detail_layout);  // ��Ʈ�� ���̾ƿ� ���   
        
        AppManagement _AppManager = (AppManagement) getApplication();
        {
        	
        	FrameLayout layout = (FrameLayout)findViewById(R.id.qna_detail_head);
            _AppManager.GetUISizeConverter().ParentLinearConvertFrameLayout(layout);
        }
        
      {
        	
    	  LinearLayout layout = (LinearLayout)findViewById(R.id.qna_detail_list_row_1);
            _AppManager.GetUISizeConverter().ParentLinearConvertLinearLayout(layout);
        }
        
        ImageBtnResize(R.id.qna_detail_back );
        
        ((TextView)findViewById(R.id.qna_detail_list_row_title)).setText(_AppManager.m_QnaData.bd_title);  
        ((TextView)findViewById(R.id.qna_detail_list_row_content)).setText(_AppManager.m_QnaData.bd_contents);  
        ((TextView)findViewById(R.id.qna_detail_list_row_content_and)).setText(_AppManager.m_QnaData.bd_answer);    
    }
    
    public void ImageBtnResize( int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	ImageView imageview = (ImageView)findViewById(id);
        _AppManager.GetUISizeConverter().ConvertFrameLayoutImage(imageview); 
        imageview.setOnClickListener(this);
    }

	public void onClick(View arg0) {
		// TODO �ڵ� ������ �޼ҵ� ����
		
		switch( arg0.getId() )
		{
		case R.id.qna_detail_back:
		{
			onBackPressed();
		}
			break;
		}
		
	}
}