package com.hoteljoin.data;

import java.util.ArrayList;


// �������� ����ȸ
public class DiaryDetail {

	public tourDiaryInfoData tourDiaryInfo = new tourDiaryInfoData();
	public ArrayList<subDiaryListData> subDiaryList = new ArrayList<subDiaryListData>();
	public static class tourDiaryInfoData
	{
		public String diaryNum;
		public String prevDiaryNum;
		public String nextDiaryNum;
		public String subject;
		public String mamberId;
		public String writerName;
		public String hitCount;
		public String recommendCount;
		public String replyCount;
		public String regDay;
		public String hotelCode;
		public String hotelName;
		public String cityCode;
		public String nationCode;

	}
	
	public static class subDiaryListData
	{
		public String cntntNum;
		public String contents;
		public String regDay;
		public String imageUrl;

	}

}
