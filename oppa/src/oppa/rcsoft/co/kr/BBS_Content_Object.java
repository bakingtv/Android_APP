package oppa.rcsoft.co.kr;

import java.util.ArrayList;

import android.graphics.Bitmap;

// �Խ��� ��� ������ Ŭ���� 


public class BBS_Content_Object 
{

	public String  BBStype = "commu";
	public boolean isMainContent;
	public boolean isOverLapReply;
	
	public String mb_grade;
	public boolean isDelete = false;
	public Bitmap th_img = null;
	public boolean isLine = false;
	private Integer is_notice;		// ������������...
	private Integer wr_id;			// �Խù� ��ȣ
	private String ca_name;			// ī�װ� ��
	private Integer reply_len;		// ��� �ܰ� ( �� ���� ���µ� ...)
	private Integer is_secret;		// ��б����� 
	private String wr_subject;		// ����
	private String wr_content;		// ����
	private String wr_name;			// �ۼ���
	private String mb_id;			// �ۼ��� ���̵� 
	private String wr_datetime;		// �ۼ� ����
	private Integer wr_hit;			// ��ȸ��
	private String wr_5;			// ��ü ���̵�
	private Integer wr_6;			// ����
	private String wr_8;			// �Ű�� �Խù�
	private Integer comment_cnt;	// ��� ���� 
	private ArrayList<Bitmap>  image;		// ÷������ �̹��� 
	public Integer cmt_id;				// ��� ������ȣ
	
	
	public Integer getIsNotice()
	{
		return is_notice;
	}
	
	public void setIsNotice(int notice)
	{
		this.is_notice = notice;
	}
	
	public Integer getWrId()
	{
		return wr_id;
	}
	
	public void setWrId(int id)
	{
		this.wr_id = id;
	}
	
	public String getCaName()
	{
		return ca_name;
	}
	
	public void setCaName(String name)
	{
		this.ca_name = name;
	}
	
	public Integer getReplyLen()
	{
		return reply_len;
	}
	
	public void setReplyLen(int len)
	{
		this.reply_len = len;
	}
	
	public Integer getIsSecret()
	{
		return is_secret;
	}
	
	public void setIsSecret(int secret)
	{
		this.is_secret = secret;
	}
	
	public String getWrSubject()
	{
		return wr_subject;
	}
	
	public void setWrSubject(String subject)
	{
		this.wr_subject = subject;
	}
	
	public String getWrContent()
	{
		return wr_content;
	}
	
	public void setWrContent(String content)
	{
		this.wr_content = content;
	}
	
	public String getWrName()
	{
		return wr_name;
	}
	
	public void setWrName(String name)
	{
		this.wr_name = name;
	}
	
	public String getMbId()
	{
		return mb_id;
	}
	
	public void setMbId(String id)
	{
		this.mb_id = id;
	}
	
	public String getWrDatetime()
	{
		return wr_datetime;
	}
	
	public void setWrDatetime(String time)
	{
		this.wr_datetime = time;
	}
	
	public Integer getWrHit()
	{
		return wr_hit;
	}
	
	public void setWrHit(int hit)
	{
		this.wr_hit = hit;
	}
	
	public String getWr5()
	{
		return wr_5;
	}
	
	public void setWr5(String no)
	{
		this.wr_5 = no;
	}
	
	public Integer getWr6()
	{
		return wr_6;
	}
	
	public void setWr6(int no)
	{
		this.wr_6 = no;
	}
	
	public String getWr8()
	{
		return wr_8;
	}
	
	public void setWr8(String no)
	{
		this.wr_8 = no;
	}
	
	public Integer getCommentCnt()
	{
		return comment_cnt;
	}
	
	public void setCommentCnt(int cnt)
	{
		this.comment_cnt = cnt;
	}
	
	public ArrayList<Bitmap> getBitmap()
	{
		return image;
	}
	
	public void setBitmap(ArrayList<Bitmap> cnt)
	{
		this.image = cnt;
	}
	


}
