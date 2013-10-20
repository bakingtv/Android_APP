package com.etoos.test;

import java.util.ArrayList;



import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FastTestSelect2 extends EtoosBaseActivity  implements OnClickListener {


	String TestName;
	String TesterName;
	Integer TesterSex = 0 ;
	String TesterSchool;
	String TesterInstitute;
	String TesterGrade = "";


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fastselect2);


		AfterCreate();

		BtnEvent(R.id.select_male);
		BtnEvent(R.id.name_3_1);
		BtnEvent(R.id.name_3_2);
		BtnEvent(R.id.select_female);
		BtnEvent(R.id.test_grade);
		BtnEvent(R.id.ok_btn);

		BtnEvent(R.id.title_icon);

		((TextView)findViewById(R.id.test_grade)).setText("��1");

		{
			AppManagement _AppManager = (AppManagement) getApplication();
			if (_AppManager.m_LoginCheck)
			{
				TesterGrade = _AppManager.m_LoginData.gname;
				((EditText)findViewById(R.id.tester_name)).setText(_AppManager.m_LoginData.name);
				if ( _AppManager.m_LoginData.gname.equals(""))
				{
					((TextView)findViewById(R.id.test_grade)).setText("��1");
				}
				else
				{
					((TextView)findViewById(R.id.test_grade)).setText(_AppManager.m_LoginData.gname);
				}
				
				((EditText)findViewById(R.id.tester_school)).setText(_AppManager.m_LoginData.shname);
				((EditText)findViewById(R.id.tester_institute)).setText(_AppManager.m_LoginData.eduinstitute1);

				if ( _AppManager.m_LoginData.gender.equals("1"))
				{
					TesterSex = 1;
					((ImageView)findViewById(R.id.select_female)).setBackgroundResource(R.drawable.r_1_bt);
					((ImageView)findViewById(R.id.select_male)).setBackgroundResource(R.drawable.r_2_bt);
				}
				else if ( _AppManager.m_LoginData.gender.equals("2"))
				{
					TesterSex = 2;
					((ImageView)findViewById(R.id.select_female)).setBackgroundResource(R.drawable.r_2_bt);
					((ImageView)findViewById(R.id.select_male)).setBackgroundResource(R.drawable.r_1_bt);
				}
				else
				{
					TesterSex = 0;
					((ImageView)findViewById(R.id.select_female)).setBackgroundResource(R.drawable.r_1_bt);
					((ImageView)findViewById(R.id.select_male)).setBackgroundResource(R.drawable.r_1_bt);
				}
			}
		}

	}


	public void BtnEvent( int id )
	{
		View imageview = (View)findViewById(id);
		imageview.setOnClickListener(this);
	}


	@Override
	public void onClick(View arg0) {
		switch(arg0.getId())
		{
		case R.id.title_icon:
		{
			onBackPressed();
		}

		break;

		case R.id.name_3_1:
		case R.id.select_male:
		{
			TesterSex = 1;
			((ImageView)findViewById(R.id.select_female)).setBackgroundResource(R.drawable.r_1_bt);
			((ImageView)findViewById(R.id.select_male)).setBackgroundResource(R.drawable.r_2_bt);
			
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(((EditText)findViewById(R.id.tester_institute)).getWindowToken(), 0);
			imm.hideSoftInputFromWindow(((EditText)findViewById(R.id.test_name)).getWindowToken(), 0);
			imm.hideSoftInputFromWindow(((EditText)findViewById(R.id.tester_name)).getWindowToken(), 0);
			imm.hideSoftInputFromWindow(((EditText)findViewById(R.id.tester_school)).getWindowToken(), 0);
		}
		break; 
		case R.id.name_3_2:
		case R.id.select_female:
		{
			TesterSex = 2;
			((ImageView)findViewById(R.id.select_female)).setBackgroundResource(R.drawable.r_2_bt);
			((ImageView)findViewById(R.id.select_male)).setBackgroundResource(R.drawable.r_1_bt);
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(((EditText)findViewById(R.id.tester_institute)).getWindowToken(), 0);
			imm.hideSoftInputFromWindow(((EditText)findViewById(R.id.test_name)).getWindowToken(), 0);
			imm.hideSoftInputFromWindow(((EditText)findViewById(R.id.tester_name)).getWindowToken(), 0);
			imm.hideSoftInputFromWindow(((EditText)findViewById(R.id.tester_school)).getWindowToken(), 0);
		}
		break;
		case R.id.test_grade:
		{
			TesterSchool =((EditText)findViewById(R.id.tester_school)).getText().toString();
			
			if ( TesterSchool.indexOf("����")>= 0 )
			{
				final String[] Grade = {"��1", "��2", "��3","��Ÿ" };
				AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
				alt_bld.setTitle("�´� �г��� �������ּ���");
				alt_bld.setSingleChoiceItems(Grade, -1, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {

						TesterGrade =Grade[item];
						((TextView)findViewById(R.id.test_grade)).setText(TesterGrade);
						dialog.cancel();
					}
				});
				AlertDialog alert = alt_bld.create();
				alert.show();
			}
			else if (TesterSchool.indexOf("����")>= 0|| TesterSchool.indexOf("����б�")>= 0|| 
					 TesterSchool.indexOf("��")>= 0|| TesterSchool.indexOf("�ܰ�")>= 0||
					 TesterSchool.indexOf("���а�")>= 0|| TesterSchool.indexOf("����")>= 0||
					 TesterSchool.indexOf("����")>= 0|| TesterSchool.indexOf("���")>= 0||
					 TesterSchool.indexOf("���ͳݰ�")>= 0)
			{
				final String[] Grade = {"��1", "��2", "��3","�����", "����̻�", "��Ÿ" };
				AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
				alt_bld.setTitle("�´� �г��� �������ּ���");
				alt_bld.setSingleChoiceItems(Grade, -1, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {

						TesterGrade =Grade[item];
						((TextView)findViewById(R.id.test_grade)).setText(TesterGrade);
						dialog.cancel();
					}
				});
				AlertDialog alert = alt_bld.create();
				alert.show();
			}
			else if (TesterSchool.indexOf("����")>= 0)
			{
				final String[] Grade = {"��1", "��2", "��3","��Ÿ" };
				AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
				alt_bld.setTitle("�´� �г��� �������ּ���");
				alt_bld.setSingleChoiceItems(Grade, -1, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {

						TesterGrade =Grade[item];
						((TextView)findViewById(R.id.test_grade)).setText(TesterGrade);
						dialog.cancel();
					}
				});
				AlertDialog alert = alt_bld.create();
				alert.show();
			}
			else if (TesterSchool.indexOf("���")>= 0)
			{
				final String[] Grade = {"��1", "��2", "��3", "�����", "����̻�", "��Ÿ" };
				AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
				alt_bld.setTitle("�´� �г��� �������ּ���");
				alt_bld.setSingleChoiceItems(Grade, -1, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {

						TesterGrade =Grade[item];
						((TextView)findViewById(R.id.test_grade)).setText(TesterGrade);
						dialog.cancel();
					}
				});
				AlertDialog alert = alt_bld.create();
				alert.show();
			}
			else
			{
				final String[] Grade = {"��1", "��2", "��3","��1", "��2", "��3", "�����", "����̻�", "��Ÿ" };
				AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
				alt_bld.setTitle("�´� �г��� �������ּ���");
				alt_bld.setSingleChoiceItems(Grade, -1, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {

						TesterGrade =Grade[item];
						((TextView)findViewById(R.id.test_grade)).setText(TesterGrade);
						dialog.cancel();
					}
				});
				AlertDialog alert = alt_bld.create();
				alert.show();
			}
			
			

		}
		break;
		case R.id.ok_btn:
		{
			TestName = ((EditText)findViewById(R.id.test_name)).getText().toString();
			TesterName = ((EditText)findViewById(R.id.tester_name)).getText().toString();
			TesterSchool = ((EditText)findViewById(R.id.tester_school)).getText().toString();
			TesterInstitute = ((EditText)findViewById(R.id.tester_institute)).getText().toString();

			if ( TestName == null || TestName.equals(""))
			{
				baseself.ShowAlertDialLog(baseself, "������� �Է����ּ���. ");
			}
			else
			{
				String ResultString  = "����� : " +  TestName;
				ResultString  += "\n�̸� : " +  TesterName;
				ResultString  += "\n���� : ";
				if ( TesterSex ==  0 )
				{
					ResultString += "�������� ����\n";
				}
				else if ( TesterSex ==  1 )
				{
					ResultString += "����\n";
				}
				else if ( TesterSex ==  2 )
				{
					ResultString  += "����\n";
				}
				ResultString  += "�г� : ";

				if ( ((TextView)findViewById(R.id.test_grade)).getText().equals("�����ϼ���")) 
				{
					ResultString  += "�������� ����\n";
				}
				else
				{
					ResultString  +=((TextView)findViewById(R.id.test_grade)).getText() + "\n";
				}
				ResultString += "�б��� : " +TesterSchool;
				ResultString += "\n�п��� : " +TesterInstitute;

				ResultString += "\n\n�Է��Ͻ� ������ �½��ϱ�? "; 


				new AlertDialog.Builder(baseself)
				.setTitle("�Է� Ȯ��")
				.setMessage(ResultString) //�ٿ���
				.setPositiveButton("��", new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int whichButton)
					{   
						Intent intent;
						intent = new Intent().setClass(baseself, FastTestMain.class);
						startActivity( intent ); 

						AppManagement _AppManager = (AppManagement) getApplication();
						_AppManager.FastTestTitle = TestName;
						_AppManager.FastTestName = TesterName;
						_AppManager.FastTestSex = TesterSex;
						_AppManager.FastTestGrade = (String) ((TextView)findViewById(R.id.test_grade)).getText();
						_AppManager.FastTestSchool = TesterSchool;
						_AppManager.FastTestInstute = TesterInstitute;



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




		}
		break;

		}

	}



}
