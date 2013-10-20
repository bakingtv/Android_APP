package com.humapcontents.mapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import twitter4j.Twitter;
import twitter4j.auth.RequestToken;

import com.euiweonjeong.base.CookieHTTP;
import com.euiweonjeong.base.CustomMultipartEntity;
import com.euiweonjeong.base.CustomMultipartEntity.ProgressListener;
import com.euiweonjeong.base.UISizeConverter;
import com.humapcontents.mapp.data.SquareNetworkData;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;


enum CURR_XML_STATE
{
	CURR_XML_NO,
	CURR_XML_IDX,
	CURR_XML_TITLE,
	CURR_XML_NICK,
	CURR_XML_IMG,
	CURR_XML_TYPE,
	CURR_XML_UNKNOWN,
}

public class AppManagement extends Application  {
	
	public CookieHTTP		m_Cookie = new CookieHTTP();
	
	public UISizeConverter 	m_UISizeConverter;
	
	public String 			DEF_URL ="http://www.humapcontents.com:8080";
	
	
	public ArrayList<Activity> 	activityList1 = new ArrayList<Activity>();
	
	public Boolean m_bLogin = false;
	
	public Boolean m_bYoutube = false;
	
	
	public Integer m_PublicIndex;
	
	public Integer m_AlbumIndex;
	
	public String m_MappToken = "";
	public String m_LoginID = "";
	public String m_Password = "";
	
	
	public String m_YoutubeToken = "";
	
	public ArrayList<String>  m_SquarePicture;
	public String m_SquarePicture2;
	public String m_SquareDetailString = "";
	public String m_SquareDetailTitle = "";
	
	public String m_SquareURL;
	
	public String m_AlbumTitle = "";
	public String m_AlbumDesc = "";
	public Integer m_AlbumTrackIndex  = 0;
	public String[] m_Jacket;
	
	
	public String m_ReplyTitle;
	public String m_ReplyDesc;
	public Integer m_ReplyIndex;
	
	
	public String m_MyPageID;
	
	public Twitter mTwitter;
	public RequestToken mRqToken;
	public twitter4j.auth.AccessToken mAccessToken;
	
	public String m_ReciverID;
	
	public ArrayList<SquareNetworkData> m_SquareDetailNetworkData;
	
	public ArrayList<FavoriteData> m_TrackData;
	public ArrayList<FavoriteData> m_StageData;
	
	
	public class FavoriteData
	{
		int  no;
		int  idx;
		String title;
		String nick;
		String img;
		String type;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
		
		
		SharedPreferences prefs =getSharedPreferences("login", MODE_PRIVATE);
		m_LoginID = prefs.getString("id", "");
		m_MappToken = prefs.getString("skey", "");

		if ( m_MappToken.equals("")|| m_LoginID.equals("") )
			m_bLogin = false;
		else
			m_bLogin = true;

		
		m_TrackData = new ArrayList<FavoriteData>();
		m_StageData = new ArrayList<FavoriteData>();
		m_TrackData.clear();
		m_StageData.clear();
		LoadFavorite();
		/// YouTube ���� üũ 
		// ���� ��� ���������� �Ѿ���� �����Ѵ�. 
		{
			try {
				List<PackageInfo> appinfo = getPackageManager().getInstalledPackages(PackageManager.GET_ACTIVITIES);
				
	            for(int i = 0 ; i <appinfo.size() ; i ++)
	            {

	     	        PackageInfo pi = appinfo.get(i);

	     	        String appname = pi.packageName;
	     	 
	     	        
	     	        if ( "com.google.android.youtube".equals(appname))
	     	        	m_bYoutube = true;
	            }
			}

			catch (RuntimeException e) {
	            // To catch RuntimeException("Package manager has died") that can occur on some version of Android,
	            // when the remote PackageManager is unavailable. sI suspect this sometimes occurs when the App is being reinstalled.
				m_bYoutube = false;
	        }
			
    		


        }

	}
	
	public Boolean AddStageData( FavoriteData data )
	{
		m_StageData.add(data);
		SaveFavorite();
		
		return false;
	}
	
	public Boolean AddTrackData( FavoriteData data )
	{
		for ( int i = 0 ; i < m_TrackData.size() ; i++ )
		{
			if ( data.idx == m_TrackData.get(i).idx )
				return false;
		}
		m_TrackData.add(data);
		SaveFavorite();
		return true;
	}
	
	
	public Boolean AddTrackData( Integer idx , String title,String nick, String img, String type  )
	{
		for ( int i = 0 ; i < m_TrackData.size() ; i++ )
		{
			if ( idx == m_TrackData.get(i).idx )
				return false;
		}
		FavoriteData data = new FavoriteData();
		
		data.no = 0;
		data.idx = idx;
		data.title = title;
		data.nick = nick;
		data.img = img;
		data.type = type;

		m_TrackData.add(data);
		SaveFavorite();
		return true;
	}
	
	public Boolean AddStageData( Integer idx , String title,String nick, String img, String type  )
	{
		for ( int i = 0 ; i < m_TrackData.size() ; i++ )
		{
			if ( idx == m_TrackData.get(i).idx )
				return false;
		}
		FavoriteData data = new FavoriteData();
		
		data.no = 0;
		data.idx = idx;
		data.title = title;
		data.nick = nick;
		data.img = img;
		data.type = type;

		m_StageData.add(data);
		SaveFavorite();
		return true;
	}
	

	
	public void RemoveStageData( int index  )
	{
		FavoriteData data = null;
		for ( int i = 0 ; i < m_StageData.size() ; i++ )
		{
			if ( index == m_StageData.get(i).idx )
				data = m_StageData.get(i);
		}
		
		
		if ( data != null )
		{
			m_StageData.remove(data);
			SaveFavorite();
		}

	}
	
	public void RemoveTrackData( int index  )
	{
		
		FavoriteData data = null;
		for ( int i = 0 ; i < m_TrackData.size() ; i++ )
		{
			if ( index == m_TrackData.get(i).idx )
				data = m_TrackData.get(i);
		}
		
		
		if ( data != null )
		{
			m_TrackData.remove(data);
			SaveFavorite();
		}

	
	}
	
	public void SaveFavoriteStage()
	{
		Element listdata=new Element("Stage");
		Document doc=new Document(listdata);
		
		// XML ���� ���� ����... 
		
		for ( int i = 0 ; i < m_StageData.size() ; i++  )
		{
			
			
			{
				Integer temp = m_StageData.get(i).no;
				Element mid=new Element("no"); 
				mid.addContent( temp.toString() );
				
				listdata.addContent(mid);
			}
			
			{
				Integer temp = m_StageData.get(i).idx;
				Element mid=new Element("idx"); 
				mid.addContent( temp.toString() );
				
				listdata.addContent(mid);
			}
			
			{
				Element title=new Element("title"); 
				title.addContent( m_StageData.get(i).title );			
				listdata.addContent(title);
			}
			{
				Element preacher=new Element("nick"); 
				preacher.addContent( m_StageData.get(i).nick );
				listdata.addContent(preacher);
			}
			{
				Element playtime=new Element("type"); 
				playtime.addContent( m_StageData.get(i).type );
				listdata.addContent(playtime);				
			}
			{
				Element img=new Element("img"); 
				img.addContent( m_StageData.get(i).img );
				listdata.addContent(img);	
			}

			

		}
		//���Ϸ� �����ϱ� ���ؼ� XMLOutputter ��ü�� �ʿ��ϴ�
        XMLOutputter xout = new XMLOutputter();
		{
			File file1 = new File(Environment.getExternalStorageDirectory() +"/android/data/com.humapcontents.mapp/"); 

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
			xout.output(doc, new FileWriter(Environment.getExternalStorageDirectory() +"/android/data/com.humapcontents.mapp/" + "stagedata.xml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void SaveFavoriteTrack()
	{
		Element listdata=new Element("Track");
		Document doc=new Document(listdata);
		
		// XML ���� ���� ����... 
		
		for ( int i = 0 ; i < m_TrackData.size() ; i++  )
		{
			
			
			{
				Integer temp = m_TrackData.get(i).no;
				Element mid=new Element("no"); 
				mid.addContent( temp.toString() );
				
				listdata.addContent(mid);
			}
			
			{
				Integer temp = m_TrackData.get(i).idx;
				Element mid=new Element("idx"); 
				mid.addContent( temp.toString() );
				
				listdata.addContent(mid);
			}
			
			{
				Element title=new Element("title"); 
				title.addContent( m_TrackData.get(i).title );			
				listdata.addContent(title);
			}
			{
				Element preacher=new Element("nick"); 
				preacher.addContent( m_TrackData.get(i).nick );
				listdata.addContent(preacher);
			}
			{
				Element playtime=new Element("type"); 
				playtime.addContent( m_TrackData.get(i).type );
				listdata.addContent(playtime);				
			}
			{
				Element img=new Element("img"); 
				img.addContent( m_TrackData.get(i).img );
				listdata.addContent(img);	
			}

			

		}
		//���Ϸ� �����ϱ� ���ؼ� XMLOutputter ��ü�� �ʿ��ϴ�
        XMLOutputter xout = new XMLOutputter();
		{
			File file1 = new File(Environment.getExternalStorageDirectory() +"/android/data/com.humapcontents.mapp/"); 

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
			xout.output(doc, new FileWriter(Environment.getExternalStorageDirectory() +"/android/data/com.humapcontents.mapp/" + "trackdata.xml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void SaveFavorite()
	{
		
		{
			SaveFavoriteStage();
			SaveFavoriteTrack();
		}
		LoadFavorite();

	}
	
	
	public void LoadFavoriteStage()
	{
		m_StageData.clear();
		
		String favoriteString = "";
		{
	        /////
	        {
	        	FileReader reader = null;
	        	
	        	try {
					reader = new FileReader( Environment.getExternalStorageDirectory() +"/android/data/com.humapcontents.mapp/" + "stagedata.xml" );
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
            
            FavoriteData data = null; ;
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
                    if ( xmlp.getName().equals("no") )
                    { 
                        estate = CURR_XML_STATE.CURR_XML_NO;
                    }
                    else if ( xmlp.getName().equals("idx")   )
                    {
                    	estate = CURR_XML_STATE.CURR_XML_IDX;

                    }
                    else if ( xmlp.getName().equals("title")   )
                    {
                    	estate = CURR_XML_STATE.CURR_XML_TITLE;
                    }
                    else if ( xmlp.getName().equals("nick")   )
                    {
                    	estate = CURR_XML_STATE.CURR_XML_NICK;

                    }
                    else if ( xmlp.getName().equals("type")   )
                    {
                    	estate = CURR_XML_STATE.CURR_XML_TYPE;

                    }

                    else if ( xmlp.getName().equals("img")   )
                    {
                    	estate = CURR_XML_STATE.CURR_XML_IMG;

                    }

                case XmlPullParser.END_TAG: // ������ ������ �±��� ���� ���� ���
                    break;
                case XmlPullParser.TEXT: // TEXT�� �̷���� ������ ���� ���
                    switch ( estate )
                    {
                    case CURR_XML_NO:
                    {
                    	data = new FavoriteData();
                    	data.no = Integer.parseInt(xmlp.getText());
                    }
                    	break;
                    case CURR_XML_IDX:
                    {
                    	data.idx = Integer.parseInt(xmlp.getText());
                    }
                    	break;
                    case CURR_XML_NICK:
                    {
                    	data.nick = xmlp.getText();
                    }
                    	break;
                    	
                    case CURR_XML_TITLE:
                    {
                    	data.title = xmlp.getText();
                    	
                    }
                    	break;
                    case CURR_XML_IMG:
                    	data.img = xmlp.getText();
                    	m_StageData.add(data);
                    	break;
                    case CURR_XML_TYPE:
                    	data.type = xmlp.getText();
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
	
	public void LoadFavoriteTrack()
	{
		m_TrackData.clear();
		
		
		String favoriteString = "";
		{
	        /////
	        {
	        	FileReader reader = null;
	        	
	        	try {
					reader = new FileReader( Environment.getExternalStorageDirectory() +"/android/data/com.humapcontents.mapp/" + "trackdata.xml" );
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
            
            FavoriteData data = null; ;
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
                    if ( xmlp.getName().equals("no") )
                    { 
                        estate = CURR_XML_STATE.CURR_XML_NO;
                    }
                    else if ( xmlp.getName().equals("idx")   )
                    {
                    	estate = CURR_XML_STATE.CURR_XML_IDX;

                    }
                    else if ( xmlp.getName().equals("title")   )
                    {
                    	estate = CURR_XML_STATE.CURR_XML_TITLE;
                    }
                    else if ( xmlp.getName().equals("nick")   )
                    {
                    	estate = CURR_XML_STATE.CURR_XML_NICK;

                    }
                    else if ( xmlp.getName().equals("type")   )
                    {
                    	estate = CURR_XML_STATE.CURR_XML_TYPE;

                    }

                    else if ( xmlp.getName().equals("img")   )
                    {
                    	estate = CURR_XML_STATE.CURR_XML_IMG;

                    }

                case XmlPullParser.END_TAG: // ������ ������ �±��� ���� ���� ���
                    break;
                case XmlPullParser.TEXT: // TEXT�� �̷���� ������ ���� ���
                    switch ( estate )
                    {
                    case CURR_XML_NO:
                    {
                    	data = new FavoriteData();
                    	data.no = Integer.parseInt(xmlp.getText());
                    }
                    	break;
                    case CURR_XML_IDX:
                    {
                    	data.idx = Integer.parseInt(xmlp.getText());
                    }
                    	break;
                    case CURR_XML_NICK:
                    {
                    	data.nick = xmlp.getText();
                    }
                    	break;
                    	
                    case CURR_XML_TITLE:
                    {
                    	data.title = xmlp.getText();
                    	
                    }
                    	break;
                    case CURR_XML_IMG:
                    	data.img = xmlp.getText();
                    	m_TrackData.add(data);
                    	break;
                    case CURR_XML_TYPE:
                    	data.type = xmlp.getText();
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
	
	public void LoadFavorite()
	{
		LoadFavoriteStage();
		LoadFavoriteTrack();
		        
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

	public String GetError( int errcode)
	{
		switch(errcode)
		{
		case -1:
			return "�� �� ���� ����";
		case 0:
			return "����";
		case 1000:
			return "�����ͺ��̽� ����";
		case 1001:
			return "JSON ����";
		case 1002:
			return "�����ͺ��̽��� Ű���� �������� ����";
		case 1003:
			return "Multipart Form ����";
		case 1004:
			return "IO����";
		case 1050:
			return "�Ķ������ ũ�Ⱑ �ʹ� ŭ.\n �Ķ���Ͱ� �������� ����.\n��Ÿ ���Ե� �Ķ���Ͱ� ��ȿ���� ����";
		case 1051:
			return "��û�� �̹����� �������� ����";
		case 1100:
			return "�н����尡 Ʋ�Ȱų� ���̵� �������� ����";
		case 1101:
			return "������ ��ȿ���� ���� \n �α��εǾ� ���� ����";
		case 1102:
			return "���� �������.";
		case 1200:
			return "���̵� �ߺ�";
		case 1201:
			return "�г��� �ߺ�";
		case 2000:
			return "�̹� ��õ �Ͽ���";
		case 2001:
			return "����� idx�� �������� ����.";
		case 3000:
			return "idx�� �������� ����.";
		case 4000:
			return "������ �ϳ� �̻� �Է��ϼ���.";
		case 4001:
			return "��ûid�� ���� ����ڰ� ����";
		case 4002:
			return "�г��� �ߺ�";
		case 6000:
			return "�������� �ʴ� ī�װ���";
		case 6001:
			return "����� ��� ����";
			
		case 6010:
			return "�������� �ʴ� idx�� \n Ȥ�� ���� �ø� ������� �ƴ�.";
			
		case 6500:
			return "�������� �ʴ� idx��";
		case 7000:
			return "����� ������ ������ ����";
		case 7001:
			return "�������� �ʴ� idx��";
			
		case 8000:
			return "idx�� �������� ����.";
		case 8500:
			return "idx�� �������� ����";
		case 9000:
			return "��û�� �Ű��� ī�װ��� ����.";
		case 9998:
			return "���� �߼� ����";
		case 9999:
			return "���� �߼� ����";
			
		}
		return "";
	}
	
	private class YoutubeAsyncTask extends AsyncTask<HttpResponse, Integer, Long> {
		private ProgressDialog mDialog = new ProgressDialog(getApplicationContext());
		long totalSize;
		@Override
		protected Long doInBackground(HttpResponse... arg0) {
			String url = "http://uploads.gdata.youtube.com/feeds/api/users/default/uploads";
			//�Ķ���� ���
			Map<String, Object> params = new HashMap<String, Object>();
			//�����Ķ���� ���
			Map<String, File> fileParams = new HashMap<String, File>();
			
			
			try {
				HttpClient httpClient = new DefaultHttpClient();
				HttpContext httpContext = new BasicHttpContext();
				HttpPost httpPost = new HttpPost(url);
				CustomMultipartEntity multipart = new CustomMultipartEntity(new ProgressListener() {
					public void transferred(long transferred) {
						// TODO Auto-generated method stub
						publishProgress((int)transferred);
					}
				});
				
				//Params ÷��
				for (String strKey : params.keySet()) {
					StringBody body = new StringBody(params.get(strKey).toString());
					multipart.addPart(strKey, body);
				}
				
				//����÷��
				for (String keys : fileParams.keySet()) {
					multipart.addPart(keys , new FileBody(fileParams.get(keys)));
				}
				
				totalSize = multipart.getContentLength();
				//Time Check
				mDialog.setMax((int)totalSize);
				httpPost.setEntity(multipart);
				HttpResponse response = httpClient.execute(httpPost,httpContext);
				InputStream is = response.getEntity().getContent();
				/**
					is�� ������ �߰� �۾�
				 */
			} catch (Exception e) {
				return 0L;
			}
			
			return 0L;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			mDialog.dismiss();
		}

		@Override
		protected void onPostExecute(Long result) {
			super.onPostExecute(result);
			mDialog.dismiss();
			
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mDialog.setCancelable(true);
			mDialog.setOnCancelListener(cancelListener);
			mDialog.setMessage("���ε����Դϴ�.");
			mDialog.show();
		}
		
		@Override
		protected void onProgressUpdate(Integer... progress) {
			super.onProgressUpdate(progress);
			//Progress ������Ʈ
			mDialog.setProgress((int)progress[0]);
		}
		
		OnCancelListener cancelListener = new OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				
			}
		};
		

	}

}
