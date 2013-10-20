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
import com.google.zxing.client.android.integration.IntentIntegrator;
import com.google.zxing.client.android.integration.IntentResult;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


// ���� ���� ���� OMR Main �κ� 
public class OMRMain extends EtoosBaseActivity implements OnClickListener{


	OMRMain self;
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

				if ( (Min <= 0 && Sec2 <=  0)|| Min < 0 || (Min == 0 && Sec2 ==  0)  )
				{
					new AlertDialog.Builder(self)
					.setMessage("���ýð��� ����Ǿ� OMR�� �ڵ� ����˴ϴ�. \n\n�ð��� �����Ͽ� ��� ������ �� ������ ���� �ÿ� ������ ������ ����ǥ�� �ݿ����� �ʽ��ϴ�. �׷��� �����Ͻðڽ��ϱ�?") 
					.setPositiveButton("�׳� ����", new DialogInterface.OnClickListener() 
					{
						public void onClick(DialogInterface dialog, int whichButton)
						{   
							SendOMRData();

						}
					})
					.setNegativeButton("�����Ͽ� ����", new DialogInterface.OnClickListener() 
					{
						public void onClick(DialogInterface dialog, int whichButton) 
						{
							//...����
							self.ShowAlertDialLog(self, "����� �����Ͽ� ���� ����� �������� �ʽ��ϴ�");
						}
					})
					.show();

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

			if ( _AppManager.CheckOMRMaingIDList(Integer.parseInt(_AppManager.gid)))
			{
				LoadMarkingData();
			}
			else
			{
				{
					String temp ;
					temp = "["+ _AppManager.FastTestGrade + "]" +  ((TextView)findViewById(R.id.title_text)).getText() + "\n" + _AppManager.FastTestTitle ;
					((TextView)findViewById(R.id.title_text)).setText(temp);

				}
				
				// ����Ʈ�� �߰� 
				_AppManager.AddOMRMaingIDList( Integer.parseInt(_AppManager.gid) );
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
		AppManagement _AppManager = (AppManagement) getApplication();
		SharedPreferences sharedPref;
		sharedPref = getSharedPreferences("gid"+_AppManager.gid, Activity.MODE_PRIVATE);

		
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
				object.put("AnswerStringState", m_ListData.get(i).AnswerStringState );

				
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
		sharedEditor.putString ("title", (String) ((TextView)findViewById(R.id.title_text)).getText());
		sharedEditor.putString ("time", (String) ((TextView)findViewById(R.id.title_time)).getText());
		sharedEditor.putString ("history", (String) history);
		sharedEditor.putString ("sdata", (String) data);
		sharedEditor.commit();
	}
	
	public void LoadMarkingData()
	{
		// �ӽ������ ��ŷ �����͸� �ҷ��´� 
		AppManagement _AppManager = (AppManagement) getApplication();
		SharedPreferences sharedPref;
		sharedPref = getSharedPreferences("gid"+_AppManager.gid, Activity.MODE_PRIVATE);
		
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
					content.AnswerStringState =  list.optInt("AnswerStringState");
					 
					
					
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
			
		}
		TimerCount = sharedPref.getString("time", "10:00");
		

	}

	

	@SuppressWarnings("unchecked")
	public void LoadSubject()
	{
		AppManagement _AppManager = (AppManagement) getApplication();
		
		
		
		{
			Integer min = Integer.parseInt(_AppManager.m_DefaultTestData.trialtime )/60;
			Integer sec = Integer.parseInt(_AppManager.m_DefaultTestData.trialtime )%60;
			TimerCount = min.toString() + ":" + sec.toString();
		}
		
		_AppManager.FastTestGrade = _AppManager.m_DefaultTestData.target;
		((TextView)findViewById(R.id.title_time)).setText(TimerCount);
		((TextView)findViewById(R.id.title_text)).setText(_AppManager.m_DefaultTestData.title);;
		
		m_ListData.clear();
		
		for ( int i = 0 ; i < _AppManager.m_DefaultOMR.size() ; i++ )
		{
			m_ListData.add( _AppManager.m_DefaultOMR.get(i));
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
					_AppManager.WEB_URL = "http://etoos.hubweb.net:8080/SMART_OMR/examinee/examinee.jsp";
					_AppManager.RemoveOMRMaingIDList( Integer.parseInt(_AppManager.gid) );
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
			
			
			{
				String[] totalTime = ((String) ((TextView)findViewById(R.id.title_time)).getText()).split(":");


				Integer Min = Integer.parseInt(totalTime[0]);
				
				if ( Min < 0 )
				{
					self.ShowAlertDialLog(self, "���ýð��� ����� �Ŀ� ����Ǯ�̸� ����Ͽ��� OMR�� �����Ҽ� �����ϴ�. ");
					break;
				}
				
			}
			
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
						SendOMRData(); 
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
						SendOMRData();
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
					_AppManager.WEB_URL = "http://etoos.hubweb.net:8080/SMART_OMR/examinee/examinee.jsp";
					_AppManager.RemoveOMRMaingIDList( Integer.parseInt(_AppManager.gid) );
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
		break;
		case R.id.bottom_2:

			if ( !chronoMeterPause)
			{
				new AlertDialog.Builder(self)
				.setMessage("���ø� �����Ͻðڽ��ϱ�? \n����  �̾ ������ �� �ֽ��ϴ�. ") 
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
			else
			{
				new AlertDialog.Builder(self)
				.setMessage("����Ǯ�̸� �簳�մϱ�?") 
				.setPositiveButton("��", new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int whichButton)
					{   
						chron.setBase(SystemClock.elapsedRealtime());
						chron.start();
						chronoMeterPause  =  !chronoMeterPause;
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
	
	public void SendOMRData()
	{
		AppManagement _AppManager = (AppManagement) getApplication();
		// �ӽ� List�� ����� ������ ����...
		_AppManager.RemoveOMRMaingIDList( Integer.parseInt(_AppManager.gid) );
		
		_AppManager.ParamData.clear();
		_AppManager.ParamData.put("uid", _AppManager.uid);
		_AppManager.ParamData.put("gid", _AppManager.gid);
		_AppManager.ParamData.put("title", _AppManager.m_DefaultTestData.title);
		_AppManager.ParamData.put("name", _AppManager.m_LoginData.name);
		_AppManager.ParamData.put("gender", _AppManager.m_LoginData.gender);
		_AppManager.ParamData.put("school", _AppManager.m_LoginData.shname);
		_AppManager.ParamData.put("type", "0");
		_AppManager.ParamData.put("totalitem", _AppManager.m_DefaultTestData.totalitem);
		
		
		{
			Integer time =   Integer.parseInt(chron.getText().toString().split(":")[0] ) *60 + Integer.parseInt(chron.getText().toString().split(":")[1] );
			_AppManager.ParamData.put("totaltimer", time.toString());
			
			Integer totaltimer =  Integer.parseInt(_AppManager.m_DefaultTestData.trialtime) - time;
			_AppManager.ParamData.put("remaintimer", totaltimer.toString());
		}
		
		// omr ������ 
		try 
		{
			JSONObject rootobject = new JSONObject();
			JSONArray array = new JSONArray();
			
			for ( int i = 0; i < m_ListData.size() ;i++  )
			{
				JSONObject object = new JSONObject();

				object.put("number", m_ListData.get(i).Number.toString());
				object.put("iid", m_ListData.get(i).refQuestion.toString());
				object.put("type", m_ListData.get(i).QuestionType.toString());
				object.put("answercnt", m_ListData.get(i).AnswerCount.toString());
				if (m_ListData.get(i).Later )
				{
					object.put("skip", "1");
				}
				else
				{
					object.put("skip", "0");
				}
				
				object.put("mark1", m_ListData.get(i).Answer[0].toString());
				object.put("mark2", m_ListData.get(i).Answer[1].toString());
				object.put("mark3", m_ListData.get(i).Answer[2].toString());
				object.put("mark4", m_ListData.get(i).Answer[3].toString());
				object.put("mark5", m_ListData.get(i).Answer[4].toString());
				
				object.put("answer", m_ListData.get(i).AnswerString);
				object.put("time", m_ListData.get(i).Timer);
				

				array.put(object);
			}
			rootobject.put("mark", array);
			_AppManager.ParamData.put("mark", rootobject.toString());
		}
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Intent intent = new Intent(baseself, NetPopup.class);
		intent.putExtra("API", 3);
		startActivityForResult(intent , 3);
	}
	
	
	
	public void SendOMRHistory()
	{
		AppManagement _AppManager = (AppManagement) getApplication();
		_AppManager.ParamData.clear();
		_AppManager.ParamData.put("uid", _AppManager.uid);
		_AppManager.ParamData.put("eid", _AppManager.eid);

		
		// omr ������ 
		try 
		{
			JSONObject rootobject = new JSONObject();
			JSONArray array = new JSONArray();
			
			for ( int i = 0; i < m_HistoryListData.size() ;i++  )
			{
				JSONObject object = new JSONObject();

				object.put("NUM", m_HistoryListData.get(i).Number.toString());
				object.put("IID", m_HistoryListData.get(i).refQuestion.toString());
				object.put("T", m_HistoryListData.get(i).AnswerType.toString());

				if ( m_HistoryListData.get(i).QuestionType == 0 )
				{
					object.put("M", "0");
				}

				else if ( m_HistoryListData.get(i).QuestionType == 1 )
				{
					object.put("M", "2");
				}
				else if ( m_HistoryListData.get(i).QuestionType == 2 )
				{
					object.put("M", "3");
				}


				
				// ��ŷ ��ȣ�� �ϳ�. 
				if ( m_HistoryListData.get(i).AnswerString.equals(""))
				{
					object.put("N", m_HistoryListData.get(i).Answer.toString());
				}
				else
				{
					object.put("N", m_HistoryListData.get(i).AnswerString);
				}
				
				object.put("S", m_HistoryListData.get(i).Timer.toString());

				

				array.put(object);
			}
			rootobject.put("markhistory", array);
			_AppManager.ParamData.put("history", rootobject.toString());
		}
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Intent intent = new Intent(baseself, NetPopup.class);
		intent.putExtra("API", 4);
		startActivityForResult(intent , 4);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		
		AppManagement _AppManager = (AppManagement) getApplication();
		if ( requestCode == 3 )
		{
			if ( resultCode == 10 )
			{

				
				JSONObject json;
				

				try 
				{
					json = new JSONObject(_AppManager.ParseString);
					
					if(json.getString("resultcode").equals("success"))
					{
						// OMR ���� ������ ���� 
						// History Data ������.
						_AppManager.eid =  json.getString("eid");
						
						SendOMRHistory();
						
					}
					else
					{
						self.ShowAlertDialLog(baseself, json.getString("resultmsg"));
					}
				} 
				catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					self.ShowAlertDialLog(baseself, "����",e.toString());
				}
				

				
			}
		}
		else if (requestCode == 4 )
		{
			if ( resultCode == 10 )
			{
				JSONObject json;


				try 
				{
					json = new JSONObject(_AppManager.ParseString);

					if(json.getString("resultcode").equals("success"))
					{
						// History ������ ���� .. 

						new AlertDialog.Builder(self)
						.setMessage("��ŷ�� ��������  ������ �Ϸ�Ǿ����ϴ�.") 
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
						self.ShowAlertDialLog(baseself, json.getString("resultmsg"));
					}
				} 
				catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					self.ShowAlertDialLog(baseself, "����",e.toString());
				}
				
			}
		}


		

	}

	public void ChangeTab( int index )
	{

		m_CurrentTab = index;
		((ImageView)findViewById(R.id.tab_1)).setBackgroundResource(R.drawable.pen_off_01);
		((ImageView)findViewById(R.id.tab_2)).setBackgroundResource(R.drawable.pen_off_02);
		((ImageView)findViewById(R.id.tab_3)).setBackgroundResource(R.drawable.pen_off_03);
		switch( index )
		{
		case 0 :
			((ImageView)findViewById(R.id.tab_1)).setBackgroundResource(R.drawable.pen_on_01);

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
							AddHistory(m_ListData.get(i).Number , answer , 2 , m_ListData.get(i).QuestionType , m_ListData.get(i).refQuestion);


						}

						else if (m_ListData.get(i).Answer[answer-1] == 1)
						{
							m_ListData.get(i).Answer[answer-1] = 2;
							AddHistory(m_ListData.get(i).Number , answer , 2, m_ListData.get(i).QuestionType , m_ListData.get(i).refQuestion);
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
						AddHistory(m_ListData.get(i).Number , answer , 1, m_ListData.get(i).QuestionType , m_ListData.get(i).refQuestion);
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
					AddHistory(m_ListData.get(i).Number , answer , 0 , m_ListData.get(i).QuestionType , m_ListData.get(i).refQuestion);

				}
			}
		}

		m_Adapter.notifyDataSetChanged();

	}

	private void AddHistory( int number , int answer , int type , int type2 , int iid  )
	{
		m_ListData.get(number-1).Later = false;
		OMRHistoryContent content = new OMRHistoryContent();

		content.Number = number;
		content.Answer = answer;
		content.QuestionType = type;
		content.refQuestion = iid;
		content.AnswerType = type2;
		
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

	private void AddHistoryString( int number , String answer , int type, int type2 , int iid  )
	{
		m_ListData.get(number-1).Later = false;
		OMRHistoryContent content = new OMRHistoryContent();

		content.Number = number;
		content.AnswerString = answer;
		content.QuestionType = type;
		content.refQuestion = iid;
		content.AnswerType = type2;
		// ���� Ǯ�̿� �ɸ� �ð�. 
		{
			String[] localTime = chron.getText().toString().split(":");
			// ���� �ð� ( �ʷ� ȯ������ )
			Integer Sec =  (Integer.parseInt(localTime[0]) *60 )  +Integer.parseInt(localTime[1]);
			content.Timer = Sec - PreTimeCount;

			PreTimeCount = Sec;

			Log.d("History", "Number : "  + content.Number + " Answer : "  + content.AnswerString + " Type : " + content.QuestionType +
					" Time : " + content.Timer) ;  
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
					if ( m_CurrentTab ==2  )
					{
						m_ListData.get(i).AnswerStringState = 0 ;
						AddHistoryString(m_CurrentSolution.Number , m_CurrentSolution.AnswerString ,0 ,m_CurrentSolution.QuestionType ,m_CurrentSolution.refQuestion );
					}
					else if ( m_CurrentTab ==0 )
					{
						m_ListData.get(i).AnswerStringState = 2 ;
						AddHistoryString(m_CurrentSolution.Number , m_CurrentSolution.AnswerString ,2 ,m_CurrentSolution.QuestionType ,m_CurrentSolution.refQuestion );
					}
					else if  ( m_CurrentTab ==1 )
					{
						m_ListData.get(i).AnswerStringState = 3 ;
						AddHistoryString(m_CurrentSolution.Number , m_CurrentSolution.AnswerString ,3 ,m_CurrentSolution.QuestionType ,m_CurrentSolution.refQuestion );
					}

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
			if ( state == 3 )
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
			else if ( state == 2 )
			{
				switch(value)
				{
				case 0:	view.setImageResource(R.drawable.red_0);				break;
				case 1:	view.setImageResource(R.drawable.red_1);				break;
				case 2:	view.setImageResource(R.drawable.red_2);				break;
				case 3:	view.setImageResource(R.drawable.red_3);				break;
				case 4:	view.setImageResource(R.drawable.red_4);				break;
				case 5:	view.setImageResource(R.drawable.red_5);				break;
				case 6:	view.setImageResource(R.drawable.red_6);				break;
				case 7:	view.setImageResource(R.drawable.red_7);				break;
				case 8:	view.setImageResource(R.drawable.red_8);				break;
				case 9:	view.setImageResource(R.drawable.red_9);				break;
			
				}
			}
			else
			{
				view.setImageResource(R.drawable.basic_0);	
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
					
					if (mBar.refQuestion <= 0 )
					{
						((ImageView)convertView.findViewById(R.id.number_x)).setVisibility(View.GONE);
					}
					else
					{
						((ImageView)convertView.findViewById(R.id.number_x)).setVisibility(View.VISIBLE);
					}
					
					switch(mBar.Answer[0])
					{
					case 0: ((ImageView)convertView.findViewById(R.id.number_1)).setImageResource(R.drawable.basic_1); break;
					case 1: ((ImageView)convertView.findViewById(R.id.number_1)).setImageResource(R.drawable.red_1); break;
					case 2:
					case 3: ((ImageView)convertView.findViewById(R.id.number_1)).setImageResource(R.drawable.color_black_1); break;
					}

					switch(mBar.Answer[1])
					{
					case 0: ((ImageView)convertView.findViewById(R.id.number_2)).setImageResource(R.drawable.basic_2); break;
					case 1: ((ImageView)convertView.findViewById(R.id.number_2)).setImageResource(R.drawable.red_2); break;
					case 2:
					case 3: ((ImageView)convertView.findViewById(R.id.number_2)).setImageResource(R.drawable.color_black_2); break;
					}


					switch(mBar.Answer[2])
					{
					case 0: ((ImageView)convertView.findViewById(R.id.number_3)).setImageResource(R.drawable.basic_3); break;
					case 1: ((ImageView)convertView.findViewById(R.id.number_3)).setImageResource(R.drawable.red_3); break;
					case 2:
					case 3: ((ImageView)convertView.findViewById(R.id.number_3)).setImageResource(R.drawable.color_black_3); break;
					}


					switch(mBar.Answer[3])
					{
					case 0: ((ImageView)convertView.findViewById(R.id.number_4)).setImageResource(R.drawable.basic_4); break;
					case 1: ((ImageView)convertView.findViewById(R.id.number_4)).setImageResource(R.drawable.red_4); break;
					case 2:
					case 3: ((ImageView)convertView.findViewById(R.id.number_4)).setImageResource(R.drawable.color_black_4); break;
					}


					switch(mBar.Answer[4])
					{
					case 0: ((ImageView)convertView.findViewById(R.id.number_5)).setImageResource(R.drawable.basic_5); break;
					case 1: ((ImageView)convertView.findViewById(R.id.number_5)).setImageResource(R.drawable.red_5); break;
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
					
					((ImageView)convertView.findViewById(R.id.number_x)).setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
							Intent intent = new Intent(baseself, WebDialog.class);
							startActivity(intent);
						}
					});
					
					
				}

				else if ( mBar.QuestionType == 1  )
				{
					convertView = mInflater.inflate(R.layout.test_row3, null);;
					SetFont(convertView);
					((TextView)convertView.findViewById(R.id.number)).setText(mBar.Number.toString());  
					
					(convertView.findViewById(R.id.number_x)).setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
							self.ShowAlertDialLog(self,mBar.Number +"�� ������ ����" ,mBar.Number + "�� ������ �����Դϴ�. �������̷��̷��Ӥ������Ӥ�");

						}
					});
					
					if (mBar.refQuestion <= 0 )
					{
						((ImageView)convertView.findViewById(R.id.number_x)).setVisibility(View.GONE);
					}
					else
					{
						((ImageView)convertView.findViewById(R.id.number_x)).setVisibility(View.VISIBLE);
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
								self.LaterAnswer(mBar.Number);
							}
							else
							{
								self.LaterAnswer(mBar.Number);
							}


						}
					});
					
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

					
					((ImageView)convertView.findViewById(R.id.number_x)).setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
							Intent intent = new Intent(baseself, WebDialog.class);
							startActivity(intent);
						}
					});
					


				}
				else if ( mBar.QuestionType == 2  )
				{
					convertView = mInflater.inflate(R.layout.test_row2, null);;
					SetFont(convertView);
					final View convertView1 =convertView;
					((EditText)convertView.findViewById(R.id.sol_1)).setText(mBar.AnswerString);
					((TextView)convertView.findViewById(R.id.number)).setText(mBar.Number.toString());  
					
					
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
					
					
					if (mBar.refQuestion <= 0 )
					{
						((ImageView)convertView.findViewById(R.id.number_x)).setVisibility(View.GONE);
					}
					else
					{
						((ImageView)convertView.findViewById(R.id.number_x)).setVisibility(View.VISIBLE);
					}
					
					final EditText SearchText = (EditText)convertView.findViewById(R.id.sol_1); 
					//����
					TextWatcher watcher = new TextWatcher()
					{    
						@Override    
						public void afterTextChanged(Editable s)
						{         
							//�ؽ�Ʈ ���� �� �߻��� �̺�Ʈ�� �ۼ�. 
						}     
						@Override    
						public void beforeTextChanged(CharSequence s, int start, int count, int after)    
						{        
							//�ؽ�Ʈ�� ���̰� ����Ǿ��� ��� �߻��� �̺�Ʈ�� �ۼ�.   
						}   
						@Override    
						public void onTextChanged(CharSequence s, int start, int before, int count)    
						{         
							//�ؽ�Ʈ�� ����ɶ����� �߻��� �̺�Ʈ�� �ۼ�.
							if (SearchText.isFocusable())         
							{             
								//m_SearchText EditText �� ��Ŀ�� �Ǿ����� ��쿡�� ����˴ϴ�.  
								// �˻�� ���� �ɶ� ���� ������ �������� 
								String Text = ((EditText)convertView1.findViewById(R.id.sol_1)).getText().toString();							
								mBar.AnswerString = Text;

							}   
						}
					};
					//ȣ��
					SearchText.addTextChangedListener(watcher);
					
					((ImageView)convertView.findViewById(R.id.number_x)).setOnClickListener(new View.OnClickListener() 
					{

						public void onClick(View v) 
						{
							Intent intent = new Intent(baseself, WebDialog.class);
							startActivity(intent);
						}
					});
					

					
				}




			}
			return convertView;
		}
	}



}

