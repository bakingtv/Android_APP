package com.hoteljoin.data;

import java.util.ArrayList;

// 6.  ���� �ɼǸ��(����)
public class HotelDetailRoomOptionList {

	public ArrayList<optionListData> optionList = new  ArrayList<optionListData>();
	public static class optionListData
	{

		public String optionCode;
		public String optionName;
		public String optionSendPrice;
		public String optionPrice;
		public String optionPriceType;
		public String optionMethodType;
	}
}
