package com.etoos.data;


public class OMRHistoryContent 
{
	public Integer Number = 1 ; // ���� ��ȣ
	public Integer 	QuestionType = 0 ;	// ���� Ÿ��, �ְ������� ����������. ( ������ 0 , �ְ��� 1 )
	public Integer 	Answer = 0;	
	public String 	AnswerString = "";	// �ְ��� ���� 
	public Integer Timer	= 0;	// Ÿ�̸� ... �缱�ý� ���� �ִ� �ð����� �߰��ؼ� ó���ؾ��Ѵ�. 
	
	
	public Integer	refQuestion = -1; // ���� ���� ��ȣ (���� ���� ��ư�� ������ ��� )
	public Integer 	AnswerType = 0;
}
