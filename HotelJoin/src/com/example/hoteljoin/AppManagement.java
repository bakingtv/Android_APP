package com.example.hoteljoin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Application;
import android.graphics.Bitmap;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.euiweonjeong.base.CookieHTTP;
import com.euiweonjeong.base.UISizeConverter;



enum CURR_XML_STATE
{
	CURR_XML_DES,
	CURR_XML_LOCAL,
	CURR_XML_NAME,

	CURR_XML_UNKNOWN,
}

public class AppManagement extends Application  {
	
	
	
	
	

	//public String 			DEF_URL ="http://121.133.123.239:8080";
	public String 			DEF_URL ="http://222.231.25.30";
	public CookieHTTP		m_Cookie ;
	
	public String m_LoginID = "";
	public String m_Password = "";
	public String m_NickName = "";
	public String m_Name = "";
	public String m_Email = "";
	public String m_Mobile = "";
	
	public Integer m_EventNum;		// �̺�Ʈ ��Ƽ��Ƽ���� ������ �̺�Ʈ ��ȣ
	
	public UISizeConverter 	m_UISizeConverter;
	
	public String		m_MyLng= "-1";
	public String		m_MyLat=  "-1";
	
	public Boolean 			m_LoginCheck = false;
	
	public String 		m_SearchCode = "S1";		// �˻� �з� �ڵ� 
	public String 		m_DestinationCode ="";		// ������ �ڵ� 
	public Boolean 		m_SearchWorld = true; 
	public String 		m_CheckInDay ="";
	public String 		m_Duration ="";
	public String 		m_NumRoom ="";
	public String 		m_NumPer ="";
	
	
	public String		m_HotelCode = "";
	
	
	public String 		m_HotelLng = "";
	public String 		m_HotelLat ="";
	public String 		m_HotelName = "";
	public ArrayList<String>	m_GallayList;
	public String 		m_BoardNum = "";
	
	public Boolean 		m_UsbHotelcode = false;
	public String 		m_CityCode = "";
	public String 		m_NationCode = "";
	public String 		m_DirNum ="";
	
	public Boolean		m_DirectMyTravel = false;
	
	
	public String 		m_ReservationName;
	public String 		m_ReservationNumber;
	
	public String 		m_ReservationNationCode;

	
	public Integer		m_SNSSelect = 2;
	public String 		m_MainimageURL = "";

	public String 		m_HotelGrade = "-1";
	public String 		m_HotelReceipt = "-1";
	
	
	public RoomDetailData	m_RoomDetailData = new RoomDetailData();
	public ArrayList<RoomOptionData>	m_RoomOptionData = new ArrayList<RoomOptionData>();
	public ArrayList<RoomGuestList>	m_RoomGuestData = new ArrayList<RoomGuestList>();
	
	public String 	m_NumChild = "0";
	
	
	
	public RoomReserData 	m_RoomReserData = new RoomReserData();
	
	
	public ArrayList<CancelPolicyList>	m_RoomCancel = new ArrayList<CancelPolicyList>();
	
	public ArrayList< HotelSearchData > m_HotelSearchData = new ArrayList< HotelSearchData >();
	
	
	
	
	public ArrayList< ReservationPersonList []>  m_HotelGuestData = new ArrayList< ReservationPersonList[] >();
	public ArrayList< ReservationRemarkerList > m_RemarkerData = new ArrayList< ReservationRemarkerList >();
	
	
	
	
	
	public String 		m_TotalPrice = "";
	public String 		m_TotalPrice2 = "";
	
	public String 		m_ResvNum = "";
	public String 		m_lodgeName = "";
	public String 		m_resvName = "";
	public String 		m_resvEmail = "";
	public String 		m_resvTel = "";
	public String 		m_resvMobile = "";
	public String 		m_resvPasswd = "";
	public String 		m_resvIyagi = "";
	
	public String 		m_PayURL = "";
	
	
	public String 		supplyCode = "";
	public String 		roomRequestKey = "";

	
	public String 		m_diaryNum = "";
	public String 		m_cntntNum = "";
	public String 		m_rewriteImage = "";
	public String 		m_rewriteContent = "";
	public String 		m_rewriteTitle = "";
	public String 		m_rewritenation = "";
	public String 		m_rewritecity = "";

	public String 		m_rewritehotelcode = "";
	public String 		m_rewritehotelname = "";
	
	
	public String 		m_writeNationCode;
	public String 		m_writeCityCode;
	
	
	
	
	public String 		m_URL= "";
	public String		m_URLTitle ="";
	
	public Boolean 		m_RefreshUI = false;
	
	ArrayList< SearchListData > m_HotelSearchListData;
	
	public String 		m_RemarkString = "";
	

	@Override
	public void onCreate()
	{
		super.onCreate();
		m_Cookie = new CookieHTTP();

		m_GallayList = new ArrayList<String>();
		
		
		
		//RemoveAllHotelSearchData();
		
		LoadHotelSearchData();
		
	}

	public void screenshot(View view, String number)throws Exception 
	{      
		
		{
			File file1 = new File(Environment.getExternalStorageDirectory() +"/DCIM/Camera/"); 

			if( !file1.exists() ) // ���ϴ� ��ο� ������ �ִ��� Ȯ��
			{
				if(file1.mkdirs() )
				{
		
				}
				else
				{
			         Toast.makeText(getBaseContext(), 
			  	           "������ ����� �����ϴ�.", 
			  	           Toast.LENGTH_LONG).show();
				}
			}
			else
			{

			}
		}
		view.setDrawingCacheEnabled(true);
		 
		Bitmap screenshot = view.getDrawingCache();
		 
		String filename = number +".jpg";
		 
		try {
		 
		File f = new File(Environment.getExternalStorageDirectory() +"/DCIM/Camera/" , filename);
		 
		f.createNewFile();
		 
		OutputStream outStream = new FileOutputStream(f);
		 
		screenshot.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
		 
		outStream.close();
		 
		} catch (IOException e) {
		 
		e.printStackTrace();
		 
		}
		
		Toast.makeText(this
                .getApplicationContext(), 
                "/DCIM/Camera/" + filename + " �� ����Ǿ����ϴ�.",
                Toast.LENGTH_LONG).show();
		 
		view.setDrawingCacheEnabled(false);
		 
	}
	
	public void LoadHotelSearchData()
	{
		m_HotelSearchData.clear();
		
		String favoriteString = "";
		{
	        /////
	        {
	        	FileReader reader = null;
	        	
	        	try {
					reader = new FileReader( Environment.getExternalStorageDirectory() +"/android/data/com.hoteljoin.hoteljoin/" + "hotelsearch.xml" );
					StringBuffer buffer = new StringBuffer();
					
					int data = 1;
					while ( data > 0 )
					{
						data = reader.read();
						buffer.append((char)data );
					}
					favoriteString = buffer.toString();
					reader.close();
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
		}
		
		
		XmlPullParserFactory xmlpf = null;
		
		favoriteString = favoriteString.replaceAll("\r", "");
		favoriteString = favoriteString.replaceAll("\n", "");
		
		//try 
		{
			// XML �ļ��� �ʱ�ȭ

			try {
				xmlpf = XmlPullParserFactory.newInstance();
			} catch (XmlPullParserException e4) {
				// TODO Auto-generated catch block
				e4.printStackTrace();
			}
			XmlPullParser xmlp = null;
			try {
				xmlp = xmlpf.newPullParser();
			} catch (XmlPullParserException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
			try {
				xmlp.setInput(new StringReader(favoriteString));
			} catch (XmlPullParserException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			// �Ľ̿� �߻��ϴ� event?�� ó���ϱ� ���� �޼ҵ�
            int eventType = 0;
			try {
				eventType = xmlp.getEventType();
			} catch (XmlPullParserException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

            
            CURR_XML_STATE estate = CURR_XML_STATE.CURR_XML_UNKNOWN;
            
            HotelSearchData data = null; ;
            // XML���� �б⸦ �ݺ��Թ̴�.
            while( eventType != XmlPullParser.END_DOCUMENT )
            {


                switch ( eventType ) 
                {
                case XmlPullParser.START_DOCUMENT: // ���� ���� �±׸� ���� ���
                case XmlPullParser.END_DOCUMENT: // ���� �� �±׸� ���� ���
                    break;
                case XmlPullParser.START_TAG: // ������ ������ �±��� ������ ���� ���
                    // ��Ҹ� üũ
                    if ( xmlp.getName().equals("destination") )
                    { 
                        estate = CURR_XML_STATE.CURR_XML_DES;
                    }
                    else if ( xmlp.getName().equals("local")   )
                    {
                    	estate = CURR_XML_STATE.CURR_XML_LOCAL;

                    }
                    else if ( xmlp.getName().equals("name")   )
                    {
                    	estate = CURR_XML_STATE.CURR_XML_NAME;
                    }
                   

                case XmlPullParser.END_TAG: // ������ ������ �±��� ���� ���� ���
                    break;
                case XmlPullParser.TEXT: // TEXT�� �̷���� ������ ���� ���
                    switch ( estate )
                    {
                    case CURR_XML_DES:
                    {
                    	
                    	data.Destination = xmlp.getText();
                    }
                    	break;
                    case CURR_XML_LOCAL:
                    {
                    	data = new HotelSearchData();
                    	if ( Integer.parseInt(xmlp.getText()) ==1 )
                    	{
                    		data.bLocal = true;
                    	}
                    	else
                    	{
                    		data.bLocal = false;
                    	}
                    }
                    	break;
                    case CURR_XML_NAME:
                    {
                    	data.Name = xmlp.getText();
                    	m_HotelSearchData.add(data);
                    }
                    	break;


                    }
                    break;
                } // switch end
                
                // ���� ������ �о�ɴϴ�
                try
                {
					try {
						eventType = xmlp.next();
					} catch (XmlPullParserException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} 
                catch (IOException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
            } // while end
		}
	}
	
	public void SaveHotelSearchData()
	{
		Element listdata=new Element("Hotel");
		Document doc=new Document(listdata);
		
		// XML ���� ���� ����... 
		
		for ( int i = 0 ; i < m_HotelSearchData.size() ; i++  )
		{
		
			
			{
				Boolean temp = m_HotelSearchData.get(i).bLocal;
				Element mid=new Element("local"); 
				
				if ( temp )
					mid.addContent( "1");
				else
					mid.addContent( "0");
				
				listdata.addContent(mid);
			}
			
			{
				Element title=new Element("name"); 
				title.addContent( m_HotelSearchData.get(i).Name );			
				listdata.addContent(title);
			}
			{
				Element preacher=new Element("destination"); 
				preacher.addContent( m_HotelSearchData.get(i).Destination );
				listdata.addContent(preacher);
			}
		}
		//���Ϸ� �����ϱ� ���ؼ� XMLOutputter ��ü�� �ʿ��ϴ�
        XMLOutputter xout = new XMLOutputter();
		{
			File file1 = new File(Environment.getExternalStorageDirectory() +"/android/data/com.hoteljoin.hoteljoin/"); 

			if( !file1.exists() ) // ���ϴ� ��ο� ������ �ִ��� Ȯ��
			{
				if(file1.mkdirs() )
				{
		
				}
				else
				{
			         Toast.makeText(getBaseContext(), 
			  	           "������ ����� �����ϴ�.", 
			  	           Toast.LENGTH_LONG).show();
				}
			}
			else
			{

			}
		}
        try {
			xout.output(doc, new FileWriter(Environment.getExternalStorageDirectory() +"/android/data/com.hoteljoin.hoteljoin/" + "hotelsearch.xml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void AddHotelSearchData( HotelSearchData data )
	{
		// �ߺ� �˻�
		Boolean Check = false;
		int index = 0;
		
		for ( int i = 0 ; i < m_HotelSearchData.size() ; i++  )
		{
			if ( m_HotelSearchData.get(i).Destination.equals(data.Destination) )
			{
				Check = true;
				index = i;
			}
		}
		
		// �ߺ��� ��� ������ġ���� ������ ����Ʈ�� ������ ��ġ�� �ű��. 
		
		if ( Check )
		{
			m_HotelSearchData.remove(index);
		}

		// �ߺ��� �ƴҰ�� �׳� �߰��� �Ѵ�. 
		m_HotelSearchData.add( data );
		SaveHotelSearchData();
	}

	public void RemoveHotelSearchData( HotelSearchData data )
	{
		m_HotelSearchData.remove(data);
		SaveHotelSearchData();
	}
	
	public void RemoveAllHotelSearchData( )
	{
		m_HotelSearchData.clear();
		SaveHotelSearchData();
	}
	
	
	
	public CookieHTTP GetHttpManager()
	{
		return m_Cookie;
	}
	
	public UISizeConverter GetUISizeConverter()
	{
		return m_UISizeConverter;
	}
	public void CreateUISizeConverter( int size )
	{
		m_UISizeConverter = new UISizeConverter(getBaseContext(), size);
	}



}
