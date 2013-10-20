package com.hoteljoin;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NetPopup  extends Activity implements OnClickListener 
{
	Thread ConnectThread;
	boolean bFinish = false;
	int APINumber= -1;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		
		
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
				WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		setContentView(R.layout.netpopup);

		
		
		{
			ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
			setResize( 0, root, true);
			
			APINumber =  getIntent().getIntExtra("API", -1);
			ConnectAPI( APINumber );
		}
	}
	

	public void onClick(View arg0) 
	{
	
	}
	
	
	public void ImageResize(int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	
    	View box = (View)findViewById(id);
    	Log.v("Type", box.getClass().getName() );
    	_AppManager.GetUISizeConverter().ConvertFrameLayoutView(box);
    }
    public void ImageResize2(int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	View box = (View)findViewById(id);
    	_AppManager.GetUISizeConverter().ConvertLinearLayoutView(box);
    }
    
    public void ImageResize3(int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	View box = (View)findViewById(id);
    	if ( box != null )
    		_AppManager.GetUISizeConverter().ConvertRelativeLayoutView(box);
    }
    
    public void TextResize(int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	TextView box = (TextView)findViewById(id);
    	_AppManager.GetUISizeConverter().ConvertFrameLayoutTextView(box);
    }
    public void TextResize2(int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	TextView box = (TextView)findViewById(id);
    	_AppManager.GetUISizeConverter().ConvertLinearLayoutTextView(box);
    }
    
    public void TextResize3(int id )
    {
    	AppManagement _AppManager = (AppManagement) getApplication();
    	TextView box = (TextView)findViewById(id);
    	_AppManager.GetUISizeConverter().ConvertRelativeLayoutTextView(box);
    }
	
	void setResize( int index , ViewGroup root , Boolean first )
    {
    	for (int i = 0; i < root.getChildCount(); i++) 
    	{
            View child = root.getChildAt(i);
            if ( first == false)
            {
            	if ( index == 0 )
                {
            		 if (child instanceof TextView)
            		 {
            			 TextResize3(child.getId());
            			 
            		 }
            		 else
            		 {
            			 ImageResize3(child.getId());
            		 }
                	
                }
                else if ( index == 1 )
                {
                	if (child instanceof TextView)
	           		{
                		TextResize2(child.getId());
                		
	           		}
	           		else
	           		{
	           			ImageResize2(child.getId());
	           		}

                }
                else if ( index == 2 )
                {
                	if (child instanceof TextView)
	           		{
                		TextResize(child.getId());
                		
	           		}
                	else if ( child instanceof CalendarView)
                	{
                		ImageResize(child.getId());
                	}
	           		else
	           		{
	           			ImageResize(child.getId());
	           		}
                }
            }
            
            if (child instanceof LinearLayout)
            {
            	setResize( 1 , (ViewGroup)child, false );
            }
                
            else if (child instanceof FrameLayout)
            {
            	if ( child instanceof CalendarView)
            	{
            		
            	}
            	else
            	{
            		setResize( 2 , (ViewGroup)child, false );
            	}
            	
            }
            else if (child instanceof RelativeLayout)
            {
            	setResize( 0 , (ViewGroup)child, false );
            } 
        }
    }
	private void ConnectAPI( int index )
	{
		
		if ( index == -1 )
			return;
		bFinish = false;
		final AppManagement _AppManager = (AppManagement) getApplication();
		_AppManager.ParseString = "";
		String URL = _AppManager.DEF_URL;
		switch ( index )
		{
//		0. �����������ȸ
		case 0:
		{
			URL += "/mweb/search/searchDestinationList.gm";
		}
			break;
//			1. ���������ø����ȸ
		case 1:
			URL += "/mweb/search/searchDestinationCityList.gm";
			break;
//			2. ȣ���ڵ�����ȸ
		case 2:
			URL += "/mweb/search/searchHotelCodeList.gm";
			break;
//			3. ȣ�ڰ��ݸ����ȸ
		case 3:
			URL += "/mweb/search/searchHotelPriceList.gm";
			break;
//			4. ȣ�ڻ�������ȸ
		case 4:
			URL += "/mweb/search/searchHotelDetail.gm";
			break; 
//			5. ���ǰ��ݸ����ȸ
		case 5:
			URL += "/mweb/search/searchHotelDetailRoomList.gm";
			break;
//			6. ���ǿɼǸ����ȸ
		case 6:
			URL += "/mweb/search/searchHotelDetailRoomOptionList.gm";
			break;
//			7. ���ֺ�ȣ�ڸ����ȸ
		case 7:
			URL += "/mweb/search/searchHotelNearbyList.gm";
			break;
//			8. ����������
		case 8:
		{
			if (_AppManager.m_SearchWorld)
			{
				URL += "/mweb/booking/bookingFormInt.gm";
			}
			else
			{
				URL += "/mweb/booking/bookingFormDom.gm";
			}
		}
			break;
			
//			9. �����Է�
		case 9:
		{
			if (_AppManager.m_SearchWorld)
			{
				URL += "/mweb/booking/bookingAddInt.gm";
			}
			else
			{
				URL += "/mweb/booking/bookingAddDom.gm";
			}
		}
			break;
//			10.����ȭ��
		case 10:
			URL += "/mweb/booking/payRequestForm.gm";
			break;
//			11.�߰����ιޱ�����
		case 11:
			URL += "/mweb/booking/extraDiscountInfo.gm";
			break;
//			12.�������׸����ȸ
		case 12:
			URL += "/mweb/board/noticeList.gm";
			break; 
//			13.����������ȸ
		case 13:
			URL += "/mweb/board/noticeDetail.gm";
			break;
//			14.���Ǹ����ȸ
		case 14:
			URL += "/mweb/board/consultList.gm";
			break;
//			15.���ǻ���ȸ
		case 15:
			URL += "/mweb/board/consultDetail.gm";
			break;
//			16.���ǵ��		
		case 16:
			URL += "/mweb/board/consultAdd.gm";
			break;
//			17.�̿��ı�����ȸ
		case 17:
			URL += "/mweb/board/reviewList.gm";
			break; 
//			18.�̿��ı����ȸ	
		case 18:
			URL += "/mweb/board/reviewDetail.gm";
			break;
//			19.�̿��ı��۸����ȸ
		case 19:
			URL += "/mweb/board/reviewDetailReplies.gm";
			break;
//			20.�̿��ı���õ
		case 20:
			URL += "/mweb/board/reviewAddRecommend.gm";
			break;
//			21.�̿��ı��۵��			
		case 21:
			URL += "/mweb/board/reviewDetailAddReply.gm";
			break;
//			22.�̿��ı��ۻ���			
		case 22:
			URL += "/mweb/board/reviewDetailDeleteReply.gm";
			break;
//			23.�������������ȸ
		case 23:
			URL += "/mweb/board/diaryList.gm";
			break;
////////////////////////////////////////
			
//			24.������������ȸ
		case 24:
			URL += "/mweb/board/diaryDetail.gm";
			break;
//			25.����������۸����ȸ
		case 25:
			URL += "/mweb/board/diaryReplyList.gm";
			break;
//			26.�����������
		case 26:
			URL += "/mweb/board/diaryAdd.gm";
			break;
//			27.����������� �����ڵ���
		case 27:
			URL += "/mweb/board/diaryNationCodeList.gm";
			break;
//			28.����������� �����ڵ���
		case 28:
			URL += "/mweb/board/diaryCityCodeList.gm";
			break;
//			29.����������õ
		case 29:
			URL += "/mweb/board/diaryAddRecommend.gm";
			break;
//			30.������������
		case 30:
			URL += "/mweb/board/diaryUpdate.gm";
			break;
//			31.������������
		case 31:
			URL += "/mweb/board/diaryDelete.gm";
			break;
//			32.����������۵��
		case 32:
			URL += "/mweb/board/diaryReplyAdd.gm";
			break;
//			33.����������ۻ���
		case 33:
			URL += "/mweb/board/diaryReplyDelete.gm";
			break;
//			34.�̺�Ʈ�����ȸ
		case 34:
			URL += "/mweb/event/eventList.gm";
			break;
//			35.�����̺�Ʈ�̹�����ȸ
		case 35:
			URL += "/mweb/event/eventMainImage.gm";
			break;
//			36.�̺�Ʈ����ȸ
		case 36:
			URL += "/mweb/event/eventDetail.gm";
			break;
//			37.�̺�Ʈ���ʸ����ȸ
		case 37:
			URL += "/mweb/event/eventBannerList.gm";
			break;
//			38.�α���
		case 38:
			URL += "/mweb/member/login.gm";
			break;
//			39.ȸ������
		case 39:
			URL += "/mweb/member/register.gm";
			break;
//			40.ȸ������ ���̵�üũ
		case 40:
			URL += "/mweb/member/validateId.gm";
			break;
//			41.������Ȳ�����ȸ
		case 41:
			URL += "/mweb/booking/bookingList.gm";
			break;
//			42.������Ȳ����ȸ
		case 42:
			URL += "/mweb/booking/bookingDetail.gm";
			break;
//			43.������ҿ�û
		case 43:
			URL += "/mweb/booking/bookingCancelRequest.gm";
			break;
//			44.���������ޱ�
		case 44:
			URL += "/mweb/member/happyCouponAccept.gm";
			break;
//			45.�������
		case 45:
			URL += "/mweb/member/couponAccept.gm";
			break;
//			46.������������������ȸ
		case 46:
			URL += "/mweb/member/myBenefitsInfo.gm";
			break;
//			47.�����ݳ�����ȸ
		case 47:
			URL += "/mweb/member/myMileageList.gm";
			break;
//			48.����������ȸ
		case 48:
			URL += "/mweb/member/myCouponList.gm";
			break;
//			49.��ǰ�ǳ�����ȸ
		case 49:
			URL += "/mweb/member/myGiftList.gm";
			break;
//			50.��ȣ������ȸ
		case 50:
			URL += "/mweb/member/myPreferCity.gm";
			break;
//			51.��ȣ�������
		case 51:
			URL += "/mweb/member/myPreferCityAdd.gm";
			break;
//			52.��ȣ��������
		case 52:
			URL += "/mweb/member/myPreferCityDel.gm";
			break;
//			53.��ȣȣ����ȸ
		case 53:
			URL += "/mweb/member/myPreferHotel.gm";
			break;
//			54.��ȣȣ�ڵ��
		case 54:
			URL += "/mweb/member/myPreferHotelAdd.gm";
			break;
//			55.��ȣȣ�ڻ���
		case 55:
			URL += "/mweb/member/myPreferHotelDel.gm";
			break;
//			56.�� BOX ��� ��ȸ
		case 56:
			URL += "/mweb/bookmark/bookmarkList.gm";
			break;
//			57.�� BOX ���
		case 57:
			URL += "/mweb/bookmark/bookmarkAdd.gm";
			break;
//			58.�� BOX ����
		case 58:
			URL += "/mweb/bookmark/bookmarkDelete.gm";
			break;
			
		
		}

		final String finalURL = URL;
		ConnectThread = new Thread(new Runnable()
		{

			public void run() 
			{
				String strJSON = _AppManager.GetHttpManager().GetHTTPData(finalURL, _AppManager.ParamData);
				_AppManager.ParseString = strJSON;
				handler.sendEmptyMessage(0);
			}
		});
		ConnectThread.start();
		


	}
	
	@Override
	public void onBackPressed() 
	{
		// �����尡 ��� �������� ���Ḧ �ϰ��� �Ҷ� ����. 
		if ( bFinish == false )
		{
			bFinish = true;
	    	this.setResult(-1);
	    	finish();			
		}

	}
	
	
	final Handler handler = new Handler( )
	{
    	
    	
    	public void handleMessage(Message msg)
		{

			switch(msg.what)
			{
				case 0:
				{
					// ���� �޼����� �޾Ҵ����� ȭ���� ���� ��Ű�� ���� ȭ������ �̵� ��Ų��. 
					callFinish();
				}
				break;
			}
		}
	};
    	
    private void callFinish()
    {
    	// �����尡 ����ִµ� ���� ���Դٴ°� ���� ���߿� back Ű�� �����ٴ� �� 
    	// �˾��� ����Ǿ ������� ���� �ʱ⿡, �޼����� ������ �ʵ��� �ؾ� �Ѵ�. 
    	if ( bFinish == false )
		{
    		bFinish = true;
        	Intent i =  new Intent();
        	i.putExtra("return", APINumber);
        	setResult(10, i);
        	
        	finish();
		}


    	
    }
//	void setGlobalFont(ViewGroup root) {
//        for (int i = 0; i < root.getChildCount(); i++) {
//            View child = root.getChildAt(i);
//            if (child instanceof TextView)
//            {
//                ((TextView)child).setTypeface(mTypeface);
//                ((TextView)child).setTypeface(mTypefaceBold, Typeface.BOLD);
//            }
//           /* else if  (child instanceof EditText) 
//            {
//            	((EditText)child).setTypeface(mTypeface);
//                ((EditText)child).setTypeface(mTypefaceBold, Typeface.BOLD);
//            }*/
//            else if (child instanceof ViewGroup)
//                setGlobalFont((ViewGroup)child);
//            
//            
//            
//        }
//    }
	

}
