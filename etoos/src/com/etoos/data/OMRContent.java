package com.etoos.data;


public class OMRContent 
{
	public Integer Number = 1 ; // ���� ��ȣ
	public Integer 	QuestionType = 0 ;	// ���� Ÿ��, �ְ������� ����������. ( ������ 0 , �ְ��� 1 )
	public Integer	AnswerCount = 2;	// ���� ���� ( �ְ����϶��� �׻�  0 )
	public Integer []	Answer ={0,0,0,0,0};	// ����  0 = NONE 1 = ���� 2 = ���� 3 = ����  + ���� 
	public String 	AnswerString = "";	// �ְ��� ���� 
	public Integer 	AnswerStringState = 2;	// �ְ��� ��ŷ���� 
	public Boolean Later  = false;		// ���߿� Ǯ��
	public Integer	refQuestion = -1; // ���� ���� ��ȣ (���� ���� ��ư�� ������ ��� )
	public String Timer	= "0";	// Ÿ�̸� ... �缱�ý� ���� �ִ� �ð����� �߰��ؼ� ó���ؾ��Ѵ�. 
	
	public  Integer []	AnswerCheck ={0,0,0,0,0}; // ����  0 = NONE 1 = üũ
	public Integer 	AnswerCheck2 = 0; 
	
	public Integer Score = 2;
	
}
