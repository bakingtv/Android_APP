package com.etoos.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.etoos.data.OMRContent;
import com.etoos.data.OMRHistoryContent;
import com.etoos.view.NumberPickerDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


// ���� ���� ���� OMR Main �κ� 
public class FastTestMain extends EtoosBaseActivity implements OnClickListener{


	FastTestMain self;
	ArrayList< OMRContent > m_ListData;
	private ListView m_ListView;


	// ��ŷ �����丮 
	ArrayList< OMRHistoryContent > m_HistoryListData;


	private OMR_Adapter m_Adapter;


	private Integer m_CurrentTab = 0; 	// ��ŷ ���� ��
	Chronometer chron;					// �ð�
	boolean chronoMeterPause = false;	// �ð踦 �����?
	private String TimerCount = "45:00";	// ���� ���� �ð� 
	private Integer PreTimeCount = 0;	// ������ ��ŷ�� �ߴ� �ð� ...

	NumberPickerDialog mCustomDialog;	// ���� �ְ��� ���� ���̾� �α�

	OMRContent	m_CurrentSolution;		// ���� ��Ŀ�̵� �ְ��� OMR ������


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fasttest_main);
		self = this;


		// ���� �ð����� ȭ�� �����°� ���� �Ѵ�. 
		//super.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


		m_HistoryListData =  new ArrayList< OMRHistoryContent >();
		m_HistoryListData.clear();

		m_ListView = ((ListView)findViewById(R.id.test_list));
		m_ListData = new ArrayList< OMRContent >();
		m_ListData.clear();
		m_Adapter = new OMR_Adapter(this, R.layout.test_row, m_ListData);

		m_ListView.setAdapter(m_Adapter);
		m_ListView.setDivider(null);





		ChangeTab(0);

		m_Adapter.notifyDataSetChanged();

		BtnEvent(R.id.tab_1);
		BtnEvent(R.id.tab_2);
		BtnEvent(R.id.tab_3);

		BtnEvent(R.id.bottom_1);
		BtnEvent(R.id.bottom_2);
		BtnEvent(R.id.bottom_3);


		AfterCreate();

		LoadSubject();


		chron = (Chronometer) findViewById(R.id.chron);
		chron.setBase(SystemClock.elapsedRealtime());
		chron.setOnChronometerTickListener(new OnChronometerTickListener() {
			@Override
			public void onChronometerTick(Chronometer chronometer) {
				// TODO Auto-generated method stub
				String msg = "������";
				int sec = (int) (((SystemClock.elapsedRealtime() - chron.getBase()) / 1000) % 5);
				String dot = ".";
				for (int i = 0; i < sec; i++) 
				{
					msg += dot;
				}
				String[] totalTime = TimerCount.split(":");
				String[] localTime = chron.getText().toString().split(":");

				Integer Min = Integer.parseInt(totalTime[0]) - Integer.parseInt(localTime[0]) ;

				Integer Sec2 = Integer.parseInt(totalTime[1]) - Integer.parseInt(localTime[1]) ;

				// �ڸ��� ��ȯ
				if ( Sec2 < 0 )
				{
					Min = Min -1;
					Sec2 = Sec2 +60;

				}

				if ( Min <= 0 && Sec2 <=  0 )
				{
					self.ShowAlertDialLog(self,"����ð�����" ,"����ð��� ����Ǿ����ϴ�. ");

					chron.stop();
				}
				else
				{
					((TextView)findViewById(R.id.title_time)).setText(String.format("%02d",Min)+":"+String.format("%02d",Sec2));
				}



			}
		});
		chron.start();

		{
			AppManagement _AppManager = (AppManagement) getApplication();

			if ( _AppManager.FastTestLoad)
			{
				LoadMarkingData();
			}
			else
			{
				_AppManager.CheckinTempSoluation();
				{
					String temp ;
					temp = "["+ _AppManager.FastTestGrade + "]" +  ((TextView)findViewById(R.id.title_text)).getText() + "\n" + _AppManager.FastTestTitle ;
					((TextView)findViewById(R.id.title_text)).setText(temp);

				}
			}

		}
		
		SaveTempData();

	}
	
	public void ClearSolution()
	{


		for ( int i = 0 ; i < m_ListData.size() ; i++ )
		{
			if ( m_ListData.get(i).Number == m_CurrentSolution.Number)
			{
				m_ListData.get(i).AnswerString = "";

			}
		}
		m_Adapter.notifyDataSetChanged();
	}

	public void SaveTempData()
	{
		SharedPreferences sharedPref;
		sharedPref = getSharedPreferences("test1", Activity.MODE_PRIVATE);

		
		// History ������ ���� 
		String history = "";
		String data = "";
		try 
		{
			JSONObject jsonObj = new JSONObject();
			JSONArray jArr = new JSONArray();


			for ( int  i = 0 ; i < m_HistoryListData.size() ; i++ )
			{
				JSONObject object = new JSONObject();
				object.put("Number", m_HistoryListData.get(i).Number );
				object.put("QuestionType", m_HistoryListData.get(i).QuestionType );
				object.put("Answer", m_HistoryListData.get(i).Answer );
				object.put("AnswerString", m_HistoryListData.get(i).AnswerString );
				object.put("Timer", m_HistoryListData.get(i).Timer );

				jArr.put(object);
			}


			jsonObj.put("history", jArr);
			
			history = jsonObj.toString();
		}
		catch (JSONException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		// ���� ��ŷ�� ������ ���� 
		try 
		{	
			JSONObject jsonObj = new JSONObject();
			JSONArray jArr = new JSONArray();


			for ( int  i = 0 ; i < m_ListData.size() ; i++ )
			{
				JSONObject object = new JSONObject();
				object.put("Number", m_ListData.get(i).Number );
				object.put("QuestionType", m_ListData.get(i).QuestionType );
				object.put("AnswerCount", m_ListData.get(i).AnswerCount );
				
				JSONArray AnswerData = new JSONArray();
				
				for ( int j = 0  ; j< m_ListData.get(i).Answer.length ;  j++ )
				{
					AnswerData.put(m_ListData.get(i).Answer[j]);
				}
				
				object.put("Answer", AnswerData );
				object.put("AnswerString", m_ListData.get(i).AnswerString );
				object.put("Later", m_ListData.get(i).Later );
				object.put("refQuestion", m_ListData.get(i).refQuestion );
				object.put("Later", m_ListData.get(i). Later );
				
				JSONArray AnswerCheckData = new JSONArray();
				
				for ( int j = 0  ; j< m_ListData.get(i).AnswerCheck.length ;  j++ )
				{
					AnswerCheckData.put(m_ListData.get(i).AnswerCheck[j]);
				}
				object.put("AnswerCheck", AnswerCheckData );
				
				object.put("Score", m_ListData.get(i).Score );
				object.put("Timer", m_ListData.get(i).Timer );

				jArr.put(object);
			}


			jsonObj.put("data", jArr);
			
			data = jsonObj.toString();
		}
		catch (JSONException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		SharedPreferences.Editor sharedEditor = sharedPref.edit();
		
		{
			AppManagement _AppManager = (AppManagement) getApplication();
			sharedEditor.putInt("sub", _AppManager.m_FastSelectIndex);
		}
		
		sharedEditor.putString ("title", (String) ((TextView)findViewById(R.id.title_text)).getText());
		sharedEditor.putString ("time", (String) ((TextView)findViewById(R.id.title_time)).getText());
		sharedEditor.putString ("history", (String) history);
		sharedEditor.putString ("sdata", (String) data);
		sharedEditor.commit();
	}
	
	public void LoadMarkingData()
	{
		// �ӽ������ ��ŷ �����͸� �ҷ��´� 
		SharedPreferences sharedPref;
		sharedPref = getSharedPreferences("test1", Activity.MODE_PRIVATE);
		
		{
			m_HistoryListData.clear();
			String strJSON = sharedPref.getString("history", "");
			try 
			{
				JSONObject json = new JSONObject(strJSON);
				JSONArray usageList = (JSONArray)json.get("history");
				for(int i = 0; i < usageList.length(); i++)
				{
					OMRHistoryContent content = new OMRHistoryContent();
					JSONObject list = (JSONObject)usageList.get(i);
					content.Answer = list.optInt("Answer");
					content.AnswerString = list.optString("AnswerString");
					content.Number = list.optInt("Number");
					content.QuestionType = list.optInt("QuestionType");
					content.Timer = list.optInt("Timer");
					m_HistoryListData.add(content);
				}
				
				
			} 
			catch (JSONException e) 
			{
				// TODO Auto-generated catch block

				e.printStackTrace();
			} 
		}
		
		{
			m_ListData.clear();
			String strJSON = sharedPref.getString("sdata", "");
			try 
			{
				JSONObject json = new JSONObject(strJSON);
				JSONArray usageList = (JSONArray)json.get("data");
				for(int i = 0; i < usageList.length(); i++)
				{
					OMRContent content = new OMRContent();
					JSONObject list = (JSONObject)usageList.get(i);
					content.Number = list.optInt("Number");
					content.AnswerString = list.optString("AnswerString");
					content.QuestionType = list.optInt("QuestionType");
					content.AnswerCount = list.optInt("AnswerCount");
					
					JSONArray usageList2 = (JSONArray)list.get("Answer");
					
					for ( int j =0 ; j < content.Answer.length; j++)
					{
						content.Answer[j] = usageList2.getInt(j);
					}
					
					
					content.Later = list.optBoolean("Later");
					content.refQuestion = list.optInt("refQuestion");
					content.Timer = list.optString ("Timer");
					
					JSONArray usageList3 = (JSONArray)list.get("AnswerCheck");
					for ( int j =0 ; j < content.AnswerCheck.length; j++)
					{
						content.AnswerCheck[j] = usageList3.getInt(j);
					}
					content.Score = list.optInt ("Score");
					
					m_ListData.add(content);
				}
				
				
			} 
			catch (JSONException e) 
			{
				// TODO Auto-generated catch block

				e.printStackTrace();
			} 
			
			
			
			
		}
		
		
		((TextView)findViewById(R.id.title_text)).setText(sharedPref.getString("title", ""));
		((TextView)findViewById(R.id.title_time)).setText(sharedPref.getString("time", "10:00"));
		
		{
			AppManagement _AppManager = (AppManagement) getApplication();
			_AppManager.m_FastSelectIndex= sharedPref.getInt("sub", 0);
		}
		TimerCount = sharedPref.getString("time", "10:00");


	}

	

	public void LoadSubject()
	{
		AppManagement _AppManager = (AppManagement) getApplication();
		switch (_AppManager.m_FastSelectIndex)
		{
		case 0:
			TimerCount = "80:00";
			((TextView)findViewById(R.id.title_time)).setText(TimerCount);
			((TextView)findViewById(R.id.title_text)).setText("���� A��");
			{
				for ( int i = 0 ; i < 45 ;  i ++ )
				{
					OMRContent temp = new OMRContent();
					temp.Number = i +1 ;
					m_ListData.add(temp);
				}
			}
			break;
		case 1:
			TimerCount = "80:00";
			((TextView)findViewById(R.id.title_time)).setText(TimerCount);
			((TextView)findViewById(R.id.title_text)).setText("���� B��");
			{
				for ( int i = 0 ; i < 45 ;  i ++ )
				{
					OMRContent temp = new OMRContent();
					temp.Number = i +1 ;
					m_ListData.add(temp);
				}
			}
			break;
		case 2:
			TimerCount = "100:00";
			((TextView)findViewById(R.id.title_time)).setText(TimerCount);
			((TextView)findViewById(R.id.title_text)).setText("���� A��");

			{
				for ( int i = 0 ; i < 30 ;  i ++ )
				{
					OMRContent temp = new OMRContent();
					temp.Number = i +1 ;
					if ( i > 21 )
						temp.QuestionType = 1;
					m_ListData.add(temp);
				}
			}
			break;
		case 3:
			TimerCount = "100:00";
			((TextView)findViewById(R.id.title_time)).setText(TimerCount);
			((TextView)findViewById(R.id.title_text)).setText("���� B��");
			{
				for ( int i = 0 ; i < 30 ;  i ++ )
				{
					OMRContent temp = new OMRContent();
					temp.Number = i +1 ;
					if ( i > 21 )
						temp.QuestionType = 1;
					m_ListData.add(temp);
				}
			}


			break;
		case 4:
			TimerCount = "70:00";
			((TextView)findViewById(R.id.title_time)).setText(TimerCount);
			((TextView)findViewById(R.id.title_text)).setText("���� A��");

			{
				for ( int i = 0 ; i < 50 ;  i ++ )
				{
					OMRContent temp = new OMRContent();
					temp.Number = i +1 ;
					m_ListData.add(temp);
				}
			}

			break;
		case 5:
			TimerCount = "70:00";
			((TextView)findViewById(R.id.title_time)).setText(TimerCount);
			((TextView)findViewById(R.id.title_text)).setText("���� B��");
			{
				for ( int i = 0 ; i < 50 ;  i ++ )
				{
					OMRContent temp = new OMRContent();
					temp.Number = i +1 ;
					m_ListData.add(temp);
				}
			}
			break;
		case 6:
			TimerCount = "30:00";
			((TextView)findViewById(R.id.title_time)).setText(TimerCount);
			((TextView)findViewById(R.id.title_text)).setText("��ȸŽ�� / ����Ž�� ");
			{
				for ( int i = 0 ; i < 20 ;  i ++ )
				{
					OMRContent temp = new OMRContent();
					temp.Number = i +1 ;
					m_ListData.add(temp);
				}
			}
			break;


		}
	}

	
	public void BtnEvent( int id )
	{
		View imageview = (View)findViewById(id);
		imageview.setOnClickListener(this);
	}
	
	@Override
	public void onBackPressed() 
	{
		if ( !chronoMeterPause)
		{
			new AlertDialog.Builder(self)
			.setMessage("���ø� ����մϱ�?") 
			.setPositiveButton("��", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int whichButton)
				{   
					AppManagement _AppManager = (AppManagement) getApplication();
					_AppManager.CheckOutTempSoluation();

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
		else
		{
			super.onBackPressed();
		}
	}


	@Override
	public void onClick(View v) 
	{
		switch(v.getId())
		{
		case R.id.tab_1:

			if ( m_CurrentTab != 0 )
				ChangeTab(0);
			break;
		case R.id.tab_2:
			if ( m_CurrentTab != 1 )
				ChangeTab(1);

			break;
		case R.id.tab_3:
			if ( m_CurrentTab != 2 )
				ChangeTab(2);
			break;

		case R.id.bottom_3:
		{
			// Ǯ�� ���� ������ �ִ��� üũ 
			
			int latercount = 0;
			int noblackcount = 0;
			int blackcount = 0;
			for( int i = 0 ; i < m_ListData.size(); i++ )
			{
				boolean check = false; 

				if (  m_ListData.get(i).QuestionType == 0)
				{
					for ( int j = 0 ;  j< m_ListData.get(i).Answer.length ; j++  )
					{
						if ( m_ListData.get(i).Answer[j] >= 2 )
						{
							check = true;
						}
					}
					if ( m_ListData.get(i).Later )
					{
						latercount++;
					}
					if ( !check )
					{
						noblackcount++;
					}
					else
					{
						blackcount++;
					}
				}

				// �ְ����� ��� Ǯ������....
				else
				{
					if ( m_ListData.get(i).Later )
					{
						latercount++;
					}
					
					if ( m_ListData.get(i).AnswerString.equals("") )
					{
						noblackcount++;
					}
					else
					{
						blackcount++;
					}
				}

			}
			
			if (latercount != 0)
			{
				String resultTitle = "";
				//if (noblackcount != 0 )
				{
					resultTitle = "\n���߿� Ǯ����� ������  " +latercount + "�� �ֽ��ϴ�";
				}
				final Integer counter = blackcount;

				new AlertDialog.Builder(self)
				.setMessage("������ �����մϱ�?" + resultTitle) 
				.setPositiveButton("��", new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int whichButton)
					{   
						AppManagement _AppManager = (AppManagement) getApplication();
						_AppManager.m_FastHistoryListData = m_HistoryListData;
						_AppManager.m_FastListData = m_ListData;

						// ���� �ð� 
						_AppManager.FastTimer  = chron.getText().toString();
						_AppManager.FastTestmakingCount = counter;
						
						_AppManager.CheckOutTempSoluation();
						Intent intent;
						intent = new Intent().setClass(baseself, FastTestResult2.class);
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
			else
			{
				String resultTitle = "";
				if (noblackcount != 0 )
				{
					resultTitle = "\n���� Ǯ�� ���� ������ " +noblackcount + "�� �ֽ��ϴ�";
				}
				final Integer counter = blackcount;

				new AlertDialog.Builder(self)
				.setMessage("������ �����մϱ�?" + resultTitle) 
				.setPositiveButton("��", new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int whichButton)
					{   
						AppManagement _AppManager = (AppManagement) getApplication();
						_AppManager.m_FastHistoryListData = m_HistoryListData;
						_AppManager.m_FastListData = m_ListData;

						// ���� �ð� 
						_AppManager.FastTimer  = chron.getText().toString();
						_AppManager.FastTestmakingCount = counter;
						
						_AppManager.CheckOutTempSoluation();
						Intent intent;
						intent = new Intent().setClass(baseself, FastTestResult2.class);
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


			break;
		}
		case R.id.bottom_1:

		{
			new AlertDialog.Builder(self)
			.setMessage("���ø� ����մϱ�?") 
			.setPositiveButton("��", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int whichButton)
				{   
					AppManagement _AppManager = (AppManagement) getApplication();
					_AppManager.CheckOutTempSoluation();

					Intent intent;
					intent = new Intent().setClass(baseself, MainActivity.class);
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
		break;
		case R.id.bottom_2:

			//if ( !chronoMeterPause)
			{
				new AlertDialog.Builder(self)
				.setMessage("���ø� �����Ͻðڽ��ϱ�? \n����  �̾ ������ �� �ֽ��ϴ�. ") 
				.setPositiveButton("��", new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int whichButton)
					{   
						Intent intent;
						intent = new Intent().setClass(baseself, ContinueActivity.class);
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

			
			break;
		}
	}

	public void ChangeTab( int index )
	{

		m_CurrentTab = index;
		((ImageView)findViewById(R.id.tab_1)).setBackgroundResource(R.drawable.pencil_off);
		((ImageView)findViewById(R.id.tab_2)).setBackgroundResource(R.drawable.pen_off_02);
		((ImageView)findViewById(R.id.tab_3)).setBackgroundResource(R.drawable.pen_off_03);
		switch( index )
		{
		case 0 :
			((ImageView)findViewById(R.id.tab_1)).setBackgroundResource(R.drawable.pencil_over);

			break;
		case 1:
			((ImageView)findViewById(R.id.tab_2)).setBackgroundResource(R.drawable.pen_on_02);

			break;
		case 2:
			((ImageView)findViewById(R.id.tab_3)).setBackgroundResource(R.drawable.pen_on_03);
			break; 
		}
	}

	public void LaterAnswer( final int number )
	{
		/*new AlertDialog.Builder(self)
		.setMessage("�̹����� ���߿� Ǫ�ðڽ��ϱ�?") 
		.setPositiveButton("��", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int whichButton)
			{   
				m_ListData.get(number-1).Later = !m_ListData.get(number-1).Later;
				m_Adapter.notifyDataSetChanged();
			}
		})
		.setNegativeButton("�ƴϿ�", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int whichButton) 
			{
				//...����
			}
		})
		.show();*/
		
		m_ListData.get(number-1).Later = !m_ListData.get(number-1).Later;
		
		// ���߿� Ǯ ���� ��ŷ �ʱ�ȭ 
		for ( int j= 0 ; j < m_ListData.get(number-1).Answer.length ; j++ )
		{
			m_ListData.get(number-1).Answer[j] = 0 ;
		}
		m_ListData.get(number-1).AnswerString = "";
		m_Adapter.notifyDataSetChanged();
		


	}
	public void ButtonCheck( int number , int answer)
	{
		if (chronoMeterPause )
		{
			self.ShowAlertDialLog(self ,"�Ͻ� ���� �Ǿ����Ƿ� ���� Ǯ�̴� �Ұ����մϴ�");
			return ;
		}
		
		
		for( int i = 0 ; i < m_ListData.size(); i++ )
		{
			// 
			if ( m_ListData.get(i).Number == number)
			{
				// �������� ��� 
				if ( m_CurrentTab ==  1 )
				{
					// �ٸ����� ������ ĥ���� �ִ� ���� �ľ��Ѵ�.
					int blackcount = 0;
					for ( int j = 0 ;  j< m_ListData.get(i).Answer.length ; j++  )
					{
						if ( m_ListData.get(i).Answer[j] > 1 )
							blackcount++;
					}

					// ��ŷ�� �������� ���� ������ Ŭ��쿡�� ��ĥ�Ѵ�. 
					if ( m_ListData.get(i).AnswerCount >  blackcount )
					{
						if (m_ListData.get(i).Answer[answer-1] == 0)
						{
							m_ListData.get(i).Answer[answer-1] = 2;
							AddHistory(m_ListData.get(i).Number , answer , 2 );


						}

						else if (m_ListData.get(i).Answer[answer-1] == 1)
						{
							m_ListData.get(i).Answer[answer-1] = 2;
							AddHistory(m_ListData.get(i).Number , answer , 2 );
						}
					}
					else
					{
						// ��ŷ �Ұ� �޼��� ..
						// �ٸ� ��ȣ�� ������ ��. 
					}

				}
				else if  ( m_CurrentTab ==  0 )
				{

					// ������ ����� 
					if (m_ListData.get(i).Answer[answer-1] == 1)
					{
						/*m_ListData.get(i).Answer[answer-1] = 0;
						AddHistory(m_ListData.get(i).Number , answer , 0 );*/
					}
					else if (m_ListData.get(i).Answer[answer-1] == 0)
					{
						m_ListData.get(i).Answer[answer-1] = 1;
						AddHistory(m_ListData.get(i).Number , answer , 1 );
					}
					else
					{
						// �������� �̹� ĥ���� �ִ� �� 
						// ���� �ؼ� �ȵǰ� �޼��� ����ϱ� 
					}
				}
				// ����� 
				else
				{
					m_ListData.get(i).Answer[answer-1] =0 ;
					AddHistory(m_ListData.get(i).Number , answer , 0 );

				}
			}
		}

		m_Adapter.notifyDataSetChanged();

	}

	private void AddHistory( int number , int answer , int type )
	{
		m_ListData.get(number-1).Later = false;
		OMRHistoryContent content = new OMRHistoryContent();

		content.Number = number;
		content.Answer = answer;
		content.QuestionType = type;

		// ���� Ǯ�̿� �ɸ� �ð�. 
		{
			String[] localTime = chron.getText().toString().split(":");
			// ���� �ð� ( �ʷ� ȯ������ )
			Integer Sec =  (Integer.parseInt(localTime[0]) *60 )  +Integer.parseInt(localTime[1]);
			content.Timer = Sec - PreTimeCount;

			PreTimeCount = Sec;

			Log.d("Time", " Total Time : " + Sec) ; 
			Log.d("History", "Number : "  + content.Number + " Answer : "  + content.Answer + " Type : " + content.QuestionType +
					" Time : " + content.Timer) ;  

		}

		m_HistoryListData.add(content);

		SaveTempData();
	}

	private void AddHistoryString( int number , String answer , int type )
	{
		m_ListData.get(number-1).Later = false;
		OMRHistoryContent content = new OMRHistoryContent();

		content.Number = number;
		content.AnswerString = answer;
		content.QuestionType = type;

		// ���� Ǯ�̿� �ɸ� �ð�. 
		{
			String[] localTime = chron.getText().toString().split(":");
			// ���� �ð� ( �ʷ� ȯ������ )
			Integer Sec =  (Integer.parseInt(localTime[0]) *60 )  +Integer.parseInt(localTime[1]);
			content.Timer = Sec - PreTimeCount;

			PreTimeCount = Sec;

			Log.d("History", "Number : "  + content.Number + " Answer : "  + content.AnswerString + " Type : " + content.QuestionType +
					"Time : " + content.Timer) ;  
		}



		m_HistoryListData.add(content);
		
		SaveTempData();
	}

	private View.OnClickListener leftClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) 
		{
			m_CurrentSolution.AnswerString = mCustomDialog.GetNumber();

			for ( int i = 0 ; i < m_ListData.size() ; i++ )
			{
				if ( m_ListData.get(i).Number == m_CurrentSolution.Number)
				{
					m_ListData.get(i).AnswerString = m_CurrentSolution.AnswerString ;
					m_ListData.get(i).AnswerStringState = m_CurrentTab ;
					AddHistoryString(m_CurrentSolution.Number , m_CurrentSolution.AnswerString , m_CurrentSolution.QuestionType );
				}
			}

			mCustomDialog.dismiss();
			m_Adapter.notifyDataSetChanged();
			//            Toast.makeText(getApplicationContext(), "���ʹ�ư Click!!", 
					//                    Toast.LENGTH_SHORT).show();
		}
	};


	public class OMR_Adapter extends ArrayAdapter<OMRContent>
	{
		private Context mContext;
		private int mResource;
		private ArrayList<OMRContent> mList;
		private LayoutInflater mInflater;
		View convertView2;
		public OMR_Adapter(Context context, int layoutResource, ArrayList<OMRContent> mTweetList)
		{
			super(context, layoutResource, mTweetList);
			this.mContext = context;
			this.mResource = layoutResource;
			this.mList = mTweetList;
			this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			//convertView2 = mInflater.inflate(mResource, null);
			//self.ListViewColSize(  (ViewGroup)convertView2 );
		}

		private void SetImage( ImageView view , int value , int state)
		{
			if ( state == 1 )
			{
				switch(value)
				{
				case 0:	view.setImageResource(R.drawable.black_0);				break;
				case 1:	view.setImageResource(R.drawable.black_1);				break;
				case 2:	view.setImageResource(R.drawable.black_2);				break;
				case 3:	view.setImageResource(R.drawable.black_3);				break;
				case 4:	view.setImageResource(R.drawable.black_4);				break;
				case 5:	view.setImageResource(R.drawable.black_5);				break;
				case 6:	view.setImageResource(R.drawable.black_6);				break;
				case 7:	view.setImageResource(R.drawable.black_7);				break;
				case 8:	view.setImageResource(R.drawable.black_8);				break;
				case 9:	view.setImageResource(R.drawable.black_9);				break;
			
				}
			}
			else if ( state == 0 )
			{
				switch(value)
				{
				case 0:	view.setImageResource(R.drawable.pencil_mark_0);				break;
				case 1:	view.setImageResource(R.drawable.pencil_mark_1);				break;
				case 2:	view.setImageResource(R.drawable.pencil_mark_2);				break;
				case 3:	view.setImageResource(R.drawable.pencil_mark_3);				break;
				case 4:	view.setImageResource(R.drawable.pencil_mark_4);				break;
				case 5:	view.setImageResource(R.drawable.pencil_mark_5);				break;
				case 6:	view.setImageResource(R.drawable.pencil_mark_6);				break;
				case 7:	view.setImageResource(R.drawable.pencil_mark_7);				break;
				case 8:	view.setImageResource(R.drawable.pencil_mark_8);				break;
				case 9:	view.setImageResource(R.drawable.pencil_mark_9);				break;
			
				}
			}
			
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			final OMRContent mBar = mList.get(position);
			final  AppManagement _AppManager = (AppManagement) getApplication();

			if(convertView == null)
			{
				convertView = mInflater.inflate(mResource, null);;

			}

			if(mBar != null) 
			{

				if (mBar.QuestionType == 0 )
				{
					convertView = mInflater.inflate(R.layout.test_row, null);;
					SetFont(convertView);
					((TextView)convertView.findViewById(R.id.number)).setText(mBar.Number.toString());  
					switch(mBar.Answer[0])
					{
					case 0: ((ImageView)convertView.findViewById(R.id.number_1)).setImageResource(R.drawable.basic_1); break;
					case 1: ((ImageView)convertView.findViewById(R.id.number_1)).setImageResource(R.drawable.pencil_mark_1); break;
					case 2:
					case 3: ((ImageView)convertView.findViewById(R.id.number_1)).setImageResource(R.drawable.color_black_1); break;
					}

					switch(mBar.Answer[1])
					{
					case 0: ((ImageView)convertView.findViewById(R.id.number_2)).setImageResource(R.drawable.basic_2); break;
					case 1: ((ImageView)convertView.findViewById(R.id.number_2)).setImageResource(R.drawable.pencil_mark_2); break;
					case 2:
					case 3: ((ImageView)convertView.findViewById(R.id.number_2)).setImageResource(R.drawable.color_black_2); break;
					}


					switch(mBar.Answer[2])
					{
					case 0: ((ImageView)convertView.findViewById(R.id.number_3)).setImageResource(R.drawable.basic_3); break;
					case 1: ((ImageView)convertView.findViewById(R.id.number_3)).setImageResource(R.drawable.pencil_mark_3); break;
					case 2:
					case 3: ((ImageView)convertView.findViewById(R.id.number_3)).setImageResource(R.drawable.color_black_3); break;
					}


					switch(mBar.Answer[3])
					{
					case 0: ((ImageView)convertView.findViewById(R.id.number_4)).setImageResource(R.drawable.basic_4); break;
					case 1: ((ImageView)convertView.findViewById(R.id.number_4)).setImageResource(R.drawable.pencil_mark_4); break;
					case 2:
					case 3: ((ImageView)convertView.findViewById(R.id.number_4)).setImageResource(R.drawable.color_black_4); break;
					}


					switch(mBar.Answer[4])
					{
					case 0: ((ImageView)convertView.findViewById(R.id.number_5)).setImageResource(R.drawable.basic_5); break;
					case 1: ((ImageView)convertView.findViewById(R.id.number_5)).setImageResource(R.drawable.pencil_mark_5); break;
					case 2:
					case 3: ((ImageView)convertView.findViewById(R.id.number_5)).setImageResource(R.drawable.color_black_5); break;
					}

					if ( mBar.Later == false )
					{
						((FrameLayout)convertView.findViewById(R.id.shop_intro_1_1)).setBackgroundColor(0xFFF8F8F8);
						((TextView)convertView.findViewById(R.id.number)).setBackgroundColor(0xFFF5F1E6);
						((TextView)convertView.findViewById(R.id.number)).setTextColor(0xFFFF9C00);
					}
					else
					{
						((FrameLayout)convertView.findViewById(R.id.shop_intro_1_1)).setBackgroundColor(0xFFFDBB48);
						((TextView)convertView.findViewById(R.id.number)).setBackgroundColor(0xFFFDBB48);
						((TextView)convertView.findViewById(R.id.number)).setTextColor(0xFFF2F2F2);
					}
					(convertView.findViewById(R.id.number)).setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
							if ( mBar.Later == false )
							{
//								new AlertDialog.Builder(self)
//								.setMessage("���߿� Ǳ�ϱ�?") 
//								.setPositiveButton("��", new DialogInterface.OnClickListener() 
//								{
//									public void onClick(DialogInterface dialog, int whichButton)
//									{   
//										self.LaterAnswer(mBar.Number);
//									}
//								})
//								.setNegativeButton("�ƴϿ�", new DialogInterface.OnClickListener() 
//								{
//									public void onClick(DialogInterface dialog, int whichButton) 
//									{
//										//...����
//									}
//								})
//								.show();
								self.LaterAnswer(mBar.Number);
							}
							else
							{
								self.LaterAnswer(mBar.Number);
							}


						}
					});


					(convertView.findViewById(R.id.number_x)).setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
							self.ShowAlertDialLog(self,mBar.Number +"�� ������ ����" ,mBar.Number + "�� ������ �����Դϴ�. �������̷��̷��Ӥ������Ӥ�");

						}
					});

					((ImageView)convertView.findViewById(R.id.number_1)).setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
							self.ButtonCheck(mBar.Number, 1);

						}
					});

					((ImageView)convertView.findViewById(R.id.number_2)).setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
							self.ButtonCheck(mBar.Number, 2);

						}
					});

					((ImageView)convertView.findViewById(R.id.number_3)).setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
							self.ButtonCheck(mBar.Number, 3);

						}
					});
					((ImageView)convertView.findViewById(R.id.number_4)).setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
							self.ButtonCheck(mBar.Number, 4);

						}
					});
					((ImageView)convertView.findViewById(R.id.number_5)).setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
							self.ButtonCheck(mBar.Number, 5);

						}
					});
				}

				else if ( mBar.QuestionType == 1  )
				{
					convertView = mInflater.inflate(R.layout.test_row3, null);;
					SetFont(convertView);
					((TextView)convertView.findViewById(R.id.number)).setText(mBar.Number.toString());  
					

					View.OnClickListener onclick =   new View.OnClickListener()
					{

						public void onClick(View v) 
						{
							if ( m_CurrentTab== 2  )
							{
								m_CurrentSolution = mBar;
								ClearSolution();
								mBar.AnswerStringState = 2;
							}
							else
							{
								mCustomDialog = new NumberPickerDialog(baseself, "","",leftClickListener);
								mCustomDialog.show();
								m_CurrentSolution = mBar;
							}
							
							
						}
					};
					((ImageView)convertView.findViewById(R.id.number_1)).setOnClickListener(onclick);
					((ImageView)convertView.findViewById(R.id.number_2)).setOnClickListener(onclick);
					((ImageView)convertView.findViewById(R.id.number_3)).setOnClickListener(onclick);
					
					
					(convertView.findViewById(R.id.number)).setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
							if ( mBar.Later == false )
							{
								self.LaterAnswer(mBar.Number);
							}
							else
							{
								self.LaterAnswer(mBar.Number);
							}


						}
					});
					
					if ( mBar.Later == false )
					{
						((FrameLayout)convertView.findViewById(R.id.shop_intro_1_1)).setBackgroundColor(0xFFF8F8F8);
						((TextView)convertView.findViewById(R.id.number)).setBackgroundColor(0xFFF5F1E6);
						((TextView)convertView.findViewById(R.id.number)).setTextColor(0xFFFF9C00);
					}
					else
					{
						((FrameLayout)convertView.findViewById(R.id.shop_intro_1_1)).setBackgroundColor(0xFFFDBB48);
						((TextView)convertView.findViewById(R.id.number)).setBackgroundColor(0xFFFDBB48);
						((TextView)convertView.findViewById(R.id.number)).setTextColor(0xFFF2F2F2);
					}
					
					
					if ( mBar.AnswerString.equals(""))
					{
						((ImageView)convertView.findViewById(R.id.number_1)).setImageResource(R.drawable.basic_0);
						((ImageView)convertView.findViewById(R.id.number_2)).setImageResource(R.drawable.basic_0);
						((ImageView)convertView.findViewById(R.id.number_3)).setImageResource(R.drawable.basic_0);
					}
					else
					{
						
						if ( Integer.parseInt( mBar.AnswerString)/100 == 0 )
						{
							((ImageView)convertView.findViewById(R.id.number_1)).setImageResource(R.drawable.basic_0);
						}
						else
						{
							SetImage(((ImageView)convertView.findViewById(R.id.number_1)), Integer.parseInt( mBar.AnswerString)/100, mBar.AnswerStringState);
						}
						
						if ( Integer.parseInt( mBar.AnswerString)/100 == 0 &&  (Integer.parseInt( mBar.AnswerString)/10)%10 == 0)
						{
							((ImageView)convertView.findViewById(R.id.number_2)).setImageResource(R.drawable.basic_0);
						}
						else
						{
							SetImage(((ImageView)convertView.findViewById(R.id.number_2)), (Integer.parseInt( mBar.AnswerString)/10)%10, mBar.AnswerStringState);
						}
						
						
						SetImage(((ImageView)convertView.findViewById(R.id.number_3)), Integer.parseInt( mBar.AnswerString)%10, mBar.AnswerStringState);
						
						
						

					}


				}




			}
			return convertView;
		}
	}



}

