package com.hoteljoin.data;

import java.util.ArrayList;

// ������ ���� 
public class MyMileageList {
	public String totalMileage;
	
	public ArrayList < mileageListData> mileageList = new ArrayList < mileageListData>();
	
	public static  class mileageListData
	{
		public String workDay;
		public String itemName;
		public String mileage;

	}
}
