package com.hoteljoin;



import java.util.ArrayList;

import com.euiweonjeong.base.BitmapManager;
import com.hoteljoin.data.EventBannerList.eventListData;
import com.hoteljoin.view.MainBottomBanner;
import com.slidingmenu.lib.SlidingMenu;

import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends HotelJoinBaseActivity {

	MainActivity self;
	ViewPager m_VP = null;	//ViewPager
	CustomPagerAdapter m_CPA = null;	//Ŀ���� �����
	
	public ArrayList< eventListData> m_TopBanner = new ArrayList< eventListData>();	// ��ܹ�� 
	public ArrayList< eventListData> m_BottomBanner = new ArrayList< eventListData>();	// �ϴܹ�� 
	
	// 
	private boolean m_CallBottomBanner = false;		// �ϴ� ��� ȣ�� ��Ÿ�̹��ΰ�? �ƴϸ� ��ܹ�� ȣ�� Ÿ�̹��ΰ�
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		AfterCreate();
		self = this;
		m_CallBottomBanner = false;
		
		m_CurrIndex =  0;
		
		((View)findViewById(R.id.title_menu)).setOnClickListener( new OnClickListener()
		{

			@Override
			public void onClick(View v) 
			{
				self.OpenMenu();
			}
		
		});
		

		
		
		m_VP = (ViewPager) findViewById(R.id.market_img);
		m_CPA = new CustomPagerAdapter();
		
		
		m_VP.setAdapter(m_CPA);
		
		 //ViewPage ������ ���� ������
		m_VP.setOnPageChangeListener(new OnPageChangeListener()
        {

			@Override
			public void onPageScrollStateChanged(int state) {}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

			@Override
			public void onPageSelected(int position) 
			{
				selectViewPaper( position );

			}
        	
        });
		
		
		{
			AppManagement _AppManager = (AppManagement) getApplication();
	    	_AppManager.ParamData.clear();
	    	
	    	_AppManager.ParamData.put("typeCode", "1");
	    	
	    	Intent intent = new Intent(baseself, NetPopup.class);
	    	intent.putExtra("API", 37);
			startActivityForResult(intent , 1);
		}
		
		
		
		
	}
	
	@Override
    public void onBackPressed() 
    {

    	new AlertDialog.Builder(this)
		 .setTitle("���� Ȯ��")
		 .setMessage("���� ���� �ϰڽ��ϱ�?") //�ٿ���
		 .setPositiveButton("��", new DialogInterface.OnClickListener() 
		 {
		     public void onClick(DialogInterface dialog, int whichButton)
		     {   
		    	 ExitApp();
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
	
	@Override
	public void onResume()
	{
		super.onResume();
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultcode, Intent data)
	{
		super.onActivityResult(requestCode, resultcode, data);
		AppManagement _AppManager = (AppManagement) getApplication();
		if ( requestCode == 1 )
		{
			if ( resultcode == 10 )
			{
				
				int callnum  = data.getIntExtra("return", -1);
				// ���� Ÿ�Կ� ���� parsing �ϱ� 
				
				switch ( callnum )
				
				{
				case 37:
				{
					
					if ( m_CallBottomBanner )
					{
						_AppManager.PParsingClass.EventBannerList(_AppManager.ParseString);
						m_BottomBanner =(ArrayList<eventListData>) _AppManager.PParsingData.eventBannerList.eventList.clone();
						
						m_CallBottomBanner = false;
						for ( int i = 0 ; i <m_BottomBanner.size() ; i++ )
						{
							MainBottomBanner item = new MainBottomBanner(self,_AppManager);
							item.SetData(m_BottomBanner.get(i).linkTypeCode, m_BottomBanner.get(i).linkCode, m_BottomBanner.get(i).imageUrl);
							
							LinearLayout layout = ((LinearLayout)self.findViewById(R.id.data_view_2));
							
							((LinearLayout)findViewById(R.id.data_view_2)).addView(item);
						}
						
						
					}
					else
					{
						_AppManager.PParsingClass.EventBannerList(_AppManager.ParseString);
						
						m_TopBanner = (ArrayList<eventListData>) _AppManager.PParsingData.eventBannerList.eventList.clone();
						initViewPaperDot( m_TopBanner.size() );	
						if ( m_TopBanner.size() != 0)
						{
							m_VP.setCurrentItem(0,  false);
							selectViewPaper( 0 );
						}
						
						
						m_CallBottomBanner = true;

				    	_AppManager.ParamData.clear();
				    	_AppManager.ParamData.put("typeCode", "2");
				    	
				    	Intent intent = new Intent(baseself, NetPopup.class);
				    	intent.putExtra("API", 37);
						startActivityForResult(intent , 1);
					}
				}
					
					break;
				case 38:
					break;
				}
				
				Log.d("API Call Success", _AppManager.ParseString);
				//_AppManager.PParsingClass.LoginParsing(_AppManager.ParseString);
				
				// �����޼��� �޾Ƽ� �������� ����ؼ� �׿� ���� ó���� ���ش�. 
				
			}
			
			else
			{
				Log.d("API Call failed", "������ ��Ʈ��ũ API ������");
			}
			
		}
		else
		{
			Log.w("error", "���� �׳� �߸� ���°�, ��Ʈ��ũ �����̰ų� �׳� �ڵ� ���������� �׳� ���� ���Ѿ���");
		}
	}
	
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////
	/// ��� �� ������ 
	
	protected void selectViewPaper(int position)
	{
		int itemIds[] = {
				R.id.imageView_dot_1,
                R.id.imageView_dot_2,
                R.id.imageView_dot_3,
                R.id.imageView_dot_4,
                R.id.imageView_dot_5,
                R.id.imageView_dot_6,
                R.id.imageView_dot_7,
                R.id.imageView_dot_8,
                R.id.imageView_dot_9,
                R.id.imageView_dot_10,
		};
		int nMyId = itemIds[position];
		ImageView ivButton = null;
		for(int nId : itemIds)
		{
			ivButton = (ImageView) findViewById(nId);
			if(nId == nMyId)
			{
				ivButton.setSelected(true);
			}
			else
			{
				ivButton.setSelected(false);
			}
		}
	}
	
	private void initViewPaperDot( int count)
	{
		int itemIds[] = {
				R.id.imageView_dot_1,
                 R.id.imageView_dot_2,
                 R.id.imageView_dot_3,
                 R.id.imageView_dot_4,
                 R.id.imageView_dot_5,
                 R.id.imageView_dot_6,
                 R.id.imageView_dot_7,
                 R.id.imageView_dot_8,
                 R.id.imageView_dot_9,
                 R.id.imageView_dot_10,
                 
		};
		
		ImageView ivButton = null;
		int i = 0;
		for(int nId : itemIds)
		{
			ivButton = (ImageView) findViewById(nId);
			
			if (i < count)
			{
				ivButton.setVisibility(View.VISIBLE);
			}
			else
			{
				ivButton.setVisibility(View.GONE);
			}
			i++;
		}
	}
	
	
	
	 private class CustomPagerAdapter extends PagerAdapter{

	        
	        @Override
	        public int getCount() {
	                return m_TopBanner.size() ;
	        }

	        /**
	         * �� ������ ����
	         */
	        @Override
	        public Object instantiateItem(View collection, int position)
	        {

	        	

	        	ImageView img = new ImageView(self); 
	        	final int pos = position;
	            int resId = 0;

	            
	            OnClickListener ol  = new OnClickListener()
	            {

					@Override
					public void onClick(View v) {

						// �̺�Ʈ �� ��������...
	                	
						
					}            
	            };
	            img.setOnClickListener(ol);

	            img.setTag( m_TopBanner.get(pos).imageUrl);
				BitmapManager.INSTANCE.loadBitmap_2(m_TopBanner.get(pos).imageUrl, img);

	            ((ViewPager) collection).addView(img,0);  

	            return img;

	        }

	        @Override
	        public void destroyItem(View collection, int position, Object view) {
	                ((ViewPager) collection).removeView((View) view);
	        }

	        
	        
	        @Override
	        public boolean isViewFromObject(View view, Object object) {
	                return view==((View)object);
	        }

	        
	        @Override
	        public void finishUpdate(View v) {
	        }
	        

	        @Override
	        public void restoreState(Parcelable pc, ClassLoader cl) {
	        }

	        @Override
	        public Parcelable saveState() {
	                return null;
	        }

	        @Override
	        public void startUpdate(View v) {
	        }

	    }
///////////////////////////////////////////////////////////////////////////////////////////////////////

}
