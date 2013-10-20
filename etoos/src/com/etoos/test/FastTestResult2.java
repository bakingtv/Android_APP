package com.etoos.test;



import java.util.ArrayList;

import com.etoos.data.OMRHistoryContent;
import com.etoos.view.FastResultItem_0;
import com.etoos.view.FastResultItem_0_2;
import com.etoos.view.FastResultItem_0_3;
import com.etoos.view.FastResultItem_1;
import com.etoos.view.FastResultItem_2;
import com.etoos.view.FastResultItem_3;
import com.etoos.view.FastResultItem_4;
import com.etoos.view.FastResultItem_4_1;
import com.etoos.view.FastResultItem_dummy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;



public class FastTestResult2 extends EtoosBaseActivity implements OnClickListener{

	FastTestResult2 self;

	ArrayList <View>	m_ExpainList = new ArrayList <View>();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fastresult_2);

		self = this;


		AfterCreate();

		RefreshUI();

		BtnEvent(R.id.title_icon);
		BtnEvent(R.id.bottom_1);
		BtnEvent(R.id.bottom_3);
	}


	public void RefreshUI()
	{
		m_ExpainList.clear();
		TestInfomation();
		{
			FastResultItem_0_3 item = new FastResultItem_0_3(self);
			item.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View arg0) {
					FastResultItem_0_3 item = (FastResultItem_0_3)(arg0);
					item.HideView();

					for ( int i = 0 ; i < m_ExpainList.size() ; i ++ )
					{
						if ( item.m_Hide )
						{
							m_ExpainList.get(i).setVisibility(View.GONE);
						}
						else
						{
							m_ExpainList.get(i).setVisibility(View.VISIBLE);
						}
					}
				}

			});
			((LinearLayout)findViewById(R.id.data_view)).addView(item);
		}

		// History üũ 
		TestCheckHistory();



		{
			FastResultItem_dummy item = new FastResultItem_dummy(self);
			((LinearLayout)findViewById(R.id.data_view)).addView(item);
		}


		// �� ���׺��� History 
		TestHistoryDetail();

		{
			FastResultItem_dummy item = new FastResultItem_dummy(self);
			((LinearLayout)findViewById(R.id.data_view)).addView(item);

		}

		SetFont();
	}

	public void TestInfomation()
	{
		SharedPreferences sharedPref;
		sharedPref = getSharedPreferences("test1", Activity.MODE_PRIVATE);
		Integer Index = sharedPref.getInt("sub", 0);

		{
			AppManagement _AppManager = (AppManagement) getApplication();
			String Title = "["+ _AppManager.FastTestGrade + "]";
			String Subject = "";

			
			


			switch (Index)
			{
			case 0:				Subject = ("���� A��");				break;
			case 1:				Subject = ("���� B��");				break;
			case 2:				Subject = ("���� A��");				break;
			case 3:				Subject = ("���� B��");				break;
			case 4:				Subject = ("���� A��");				break;
			case 5:				Subject = ("���� B��");				break;
			case 6:				Subject = ("��ȸŽ�� / ����Ž�� ");	break;
			}

			FastResultItem_0 item = new FastResultItem_0(self);

			item.SetData(Title , Subject , "");


			((LinearLayout)findViewById(R.id.data_view)).addView(item);
		}

		{
			FastResultItem_dummy item = new FastResultItem_dummy(self);
			((LinearLayout)findViewById(R.id.data_view)).addView(item);
		}

		{
			AppManagement _AppManager = (AppManagement) getApplication();
			FastResultItem_0_2 item = new FastResultItem_0_2(self);
			((LinearLayout)findViewById(R.id.data_view)).addView(item);

			String Time = "" ;

			{
				String[] totalTime = _AppManager.FastTimer.split(":");
				if (  Integer.parseInt(totalTime[0])/60 != 0)
				{
					Time  = Integer.toString(  Integer.parseInt(totalTime[0])/60);
					Time += "�ð� ";
				}
				
				{
					Integer Min=  Integer.parseInt(totalTime[0])%60;
					Time += Min.toString();
					Time += "�� / ";
				}
				
			}
			switch (Index)
			{
			case 0:				Time += ("1�ð� 20��");				break;
			case 1:				Time += ("1�ð� 20��");				break;
			case 2:				Time += ("1�ð� 40��");				break;
			case 3:				Time += ("1�ð� 40��");				break;
			case 4:				Time += ("1�ð� 10��");				break;
			case 5:				Time += ("1�ð� 10��");				break;
			case 6:				Time += ("20��");				break;
			}


			item.SetData("���ýð�",  Time);



		}

		{

			FastResultItem_0_2 item = new FastResultItem_0_2(self);
			((LinearLayout)findViewById(R.id.data_view)).addView(item);

			AppManagement _AppManager = (AppManagement) getApplication();
			String Making = _AppManager.FastTestmakingCount + "���� /";



			switch (_AppManager.m_FastSelectIndex)
			{
			case 0:				Making += "45����";				break;
			case 1:				Making += "45����";				break;
			case 2:				Making += "30����";				break;
			case 3:				Making += "30����";				break;
			case 4:				Making += "50����";				break;
			case 5:				Making += "50����";				break;
			case 6:				Making += "20����";				break;
			}
			item.SetData("��ŷ����",  Making);
		}

		{
			Integer Total = 0;
			FastResultItem_0_2 item = new FastResultItem_0_2(self);
			((LinearLayout)findViewById(R.id.data_view)).addView(item);

			AppManagement _AppManager = (AppManagement) getApplication();

			String[] totalTime1= _AppManager.FastTimer.split(":");

			Total = Integer.parseInt(totalTime1[0])  *60 ;
			Total += Integer.parseInt(totalTime1[1])  ;
			Integer Output= 0;
			if ( _AppManager.FastTestmakingCount != 0 )
			{
				Output = (Total / _AppManager.FastTestmakingCount);
			}
			
			item.SetData("������� Ǯ�̽ð�", Output.toString()+ "��");
		}
	}
	public void TestCheckHistory()
	{


		{
			FastResultItem_dummy item = new FastResultItem_dummy(self);
			((LinearLayout)findViewById(R.id.data_view)).addView(item);
			m_ExpainList.add(item);
		}


		AppManagement _AppManager = (AppManagement) getApplication();

		int counter  = 0;
		ArrayList< OMRHistoryContent > HistoryListData = null ;
		for ( int i = 0 ; i < _AppManager.m_FastHistoryListData.size() ; i ++ )
		{
			if ( counter == 0  )
				HistoryListData = new ArrayList< OMRHistoryContent >();
			
			// �������� ��츸 �߰� ���ش�. 
			if ( _AppManager.m_FastHistoryListData.get(i).AnswerString.equals(""))
			{
				HistoryListData.add(  _AppManager.m_FastHistoryListData.get(i) );
				counter++;
			}
			

			if ( HistoryListData.size() >= 9 )
			{
				{
					FastResultItem_1 item = new FastResultItem_1(self);
					((LinearLayout)findViewById(R.id.data_view)).addView(item);

					item.SetData(HistoryListData);
					m_ExpainList.add(item);
				}

				{
					FastResultItem_2 item = new FastResultItem_2(self);
					((LinearLayout)findViewById(R.id.data_view)).addView(item);


					item.SetData(HistoryListData);

					m_ExpainList.add(item);
				}

				counter = 0; 
			}


		}

		if ( HistoryListData.size() < 9 &&  HistoryListData.size() != 0 )
		{
			{
				FastResultItem_1 item = new FastResultItem_1(self);
				((LinearLayout)findViewById(R.id.data_view)).addView(item);
				item.SetData(HistoryListData);
				m_ExpainList.add(item);
			}

			{
				FastResultItem_2 item = new FastResultItem_2(self);
				((LinearLayout)findViewById(R.id.data_view)).addView(item);
				item.SetData(HistoryListData);

				m_ExpainList.add(item);
			}
		}
	}

	public void TestHistoryDetail()
	{
		AppManagement _AppManager = (AppManagement) getApplication();
		{
			FastResultItem_3 item = new FastResultItem_3(self);
			((LinearLayout)findViewById(R.id.data_view)).addView(item);
		}
		{


			// ������ ���鼭 ��ȣ ������� ���� �ؼ� ����Ʈ�� �־��ش�. 
			for ( int j = 0  ; j  < _AppManager.m_FastListData.size() ;  j++)
			{
				ArrayList< OMRHistoryContent > tempList = new ArrayList< OMRHistoryContent > ();
				tempList.clear();
				for ( int  i = 0 ; i < _AppManager.m_FastHistoryListData.size() ; i++)
				{
					if ( j+ 1 == _AppManager.m_FastHistoryListData.get(i).Number)
					{
						// �������� ��츸 �߰� 
						if ( _AppManager.m_FastHistoryListData.get(i).AnswerString.equals(""))
							tempList.add(_AppManager.m_FastHistoryListData.get(i));
					}
				}

				if ( tempList.size() != 0)
				{

					String tempString = "";
					for ( int c = 0 ; c <  _AppManager.m_FastListData.get(j).Answer.length ; c++)
					{
						if (_AppManager.m_FastListData.get(j).Answer[c] > 1)
						{
							tempString +=_AppManager.m_FastListData.get(j).Answer[c] + " ";
						}
						
					}
					
					
					if ( !tempString.equals(""))
					{
						FastResultItem_4 item = new FastResultItem_4(self);
						((LinearLayout)findViewById(R.id.data_view)).addView(item);


						ArrayList<View> viewList = new ArrayList<View>();


						// 4���� ����Ʈ�� ©��.. 
						ArrayList< OMRHistoryContent > tempList2 = new ArrayList< OMRHistoryContent > ();

						int counter = 0;
						for ( int k = 0; k < tempList.size() ; k++ )
						{
							tempList2.add(tempList.get(k));
							counter++;

							if ( counter > 3)
							{

								FastResultItem_4_1 item1 = new FastResultItem_4_1(self);
								item1.setVisibility(View.GONE);
								((LinearLayout)findViewById(R.id.data_view)).addView(item1);
								item1.SetData(tempList2);
								viewList.add(item1);



								counter = 0; 
							}

						}
						if ( counter != 0)
						{
							FastResultItem_4_1 item1 = new FastResultItem_4_1(self);
							item1.setVisibility(View.GONE);
							((LinearLayout)findViewById(R.id.data_view)).addView(item1);
							item1.SetData(tempList2);
							viewList.add(item1);
						}

						item.SetData(_AppManager.m_FastListData.get(j).Number , _AppManager.m_FastListData.get(j).Answer, viewList, tempList);
						item.setOnClickListener(new OnClickListener()
						{

							@Override
							public void onClick(View arg0) {
								FastResultItem_4 item = (FastResultItem_4)(arg0);
								item.ShowDetail();

							}

						});
					}
					
					


				}

			}


			/*ArrayList<View> viewList = new ArrayList<View>();
			for ( int i = 0 ; i < 3  ; i++)
			{
				FastResultItem_4_1 item1 = new FastResultItem_4_1(self);
				item1.setVisibility(View.GONE);
				((LinearLayout)findViewById(R.id.data_view)).addView(item1);
				viewList.add(item1);
			}
			item.SetData("test", "Index", "Name", viewList);


			item.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View arg0) {
					FastResultItem_4 item = (FastResultItem_4)(arg0);
					item.ShowDetail();

				}

			});*/
		}
	}

	@Override
	public void onBackPressed() 
	{

		new AlertDialog.Builder(this)
		.setTitle("ä�� Ȯ��")
		.setMessage("���߿� ä�� �Ͻðڽ��ϱ�?") 
		.setPositiveButton("��", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int whichButton)
			{   
				AppManagement _AppManager = (AppManagement) getApplication();
				_AppManager.WEB_URL = "http://etoos.hubweb.net:8080/SMART_OMR/examinee/examinee.jsp";
				Intent intent;
				intent = new Intent().setClass(baseself, WebActivity.class);
				startActivity( intent ); 
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

	public void BtnEvent( int id )
	{
		View imageview = (View)findViewById(id);
		imageview.setOnClickListener(this);
	}


	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

		switch( arg0.getId())
		{
		case R.id.title_icon:
		{
			AppManagement _AppManager = (AppManagement) getApplication();
			_AppManager.WEB_URL = "http://etoos.hubweb.net:8080/SMART_OMR/examinee/examinee.jsp";
			Intent intent;
			intent = new Intent().setClass(baseself, WebActivity.class);
			startActivity( intent ); 
		}
		break; 
		case R.id.bottom_1:
		{

			AppManagement _AppManager = (AppManagement) getApplication();
			if (_AppManager.m_LoginCheck)
			{
				new AlertDialog.Builder(this)

				.setMessage("���� ���� ����� ����Ǿ����ϴ�. \nMy������>����ǥ���� ä�� �����մϴ�") 
				.setPositiveButton("Ȯ��", new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int whichButton)
					{   
						AppManagement _AppManager = (AppManagement) getApplication();
						_AppManager.WEB_URL = "http://etoos.hubweb.net:8080/SMART_OMR/examinee/examinee.jsp";
						Intent intent;
						intent = new Intent().setClass(baseself, WebActivity.class);
						startActivity( intent ); 
					}
				})

				.show();
			}
			else
			{
				new AlertDialog.Builder(this)
				.setMessage("���� ���� ����� �����Ϸ��� �α����ؾ� �մϴ�.") 
				.setPositiveButton("�α���", new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int whichButton)
					{   
						AppManagement _AppManager = (AppManagement) getApplication();
						_AppManager.WEB_URL = "http://etoos.hubweb.net:8080/SMART_OMR/login/login.jsp";
						Intent intent;
						intent = new Intent().setClass(baseself, ResultWebActivity.class);
						startActivity( intent ); 
					}
				})
				.setNegativeButton("����", new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int whichButton) 
					{
						//...����
						AppManagement _AppManager = (AppManagement) getApplication();
						_AppManager.WEB_URL = "http://etoos.hubweb.net:8080/SMART_OMR/examinee/examinee.jsp";
						Intent intent;
						intent = new Intent().setClass(baseself, WebActivity.class);
						startActivity( intent ); 
					}
				})
				.show();
			}



		}
		break;
		case R.id.bottom_3:
		{
			AppManagement _AppManager = (AppManagement) getApplication();
			if (_AppManager.m_LoginCheck)
			{
				Intent intent;
				intent = new Intent().setClass(baseself, FastTestResult1.class);
				startActivity( intent ); 
			}
			else
			{
				new AlertDialog.Builder(this)
				.setTitle("ä�� Ȯ��")
				.setMessage("���ð���� �����ϰ� ä���� �Ϸ��� �α����ؾ� �մϴ�.") 
				.setPositiveButton("�α��� ��", new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int whichButton)
					{   
						AppManagement _AppManager = (AppManagement) getApplication();
						_AppManager.WEB_URL = "http://etoos.hubweb.net:8080/SMART_OMR/login/login.jsp";
						Intent intent;
						intent = new Intent().setClass(baseself, ResultWebActivity.class);
						startActivity( intent ); 
					}
				})
				.setNegativeButton("����/ä�� ����", new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int whichButton) 
					{
						AppManagement _AppManager = (AppManagement) getApplication();
						_AppManager.WEB_URL = "http://etoos.hubweb.net:8080/SMART_OMR/examinee/examinee.jsp";
						Intent intent;
						intent = new Intent().setClass(baseself, WebActivity.class);
						startActivity( intent ); 
					}
				})
				.show();
			}

		}
		break;

		}

	}



}
