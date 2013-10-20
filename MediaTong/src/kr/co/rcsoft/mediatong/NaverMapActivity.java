package kr.co.rcsoft.mediatong;

import android.graphics.Rect;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.euiweonjeong.base.BaseNaverMapActivity;

import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapOverlay;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.NMapView.OnMapStateChangeListener;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.mapviewer.overlay.NMapCalloutOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager.OnCalloutOverlayListener;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;
import com.nhn.android.mapviewer.overlay.NMapResourceProvider;


public class NaverMapActivity extends BaseNaverMapActivity implements OnMapStateChangeListener , OnCalloutOverlayListener {
	
	
	// API-KEY
	public static final String API_KEY = "8641b3d08171d0fb0b2ca835bbdaa108";
	// ���̹� �� ��ü
	NMapView mMapView = null;
	// �� ��Ʈ�ѷ�
	NMapController mMapController = null;
	// ���� �߰��� ���̾ƿ�
	LinearLayout MapContainer;
	
	NMapResourceProvider mMapViewerResourceProvider;
	NMapOverlayManager mOverlayManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.navermap_layout);
		
		// ���̹� ������ �ֱ� ���� LinearLayout ������Ʈ
		MapContainer = (LinearLayout) findViewById(R.id.naver_map_layout);

		// ���̹� ���� ��ü ����
		mMapView = new NMapView(this);
		
		// ���� ��ü�κ��� ��Ʈ�ѷ� ����
		mMapController = mMapView.getMapController();

		// ���̹� ���� ��ü�� APIKEY ����
		mMapView.setApiKey(API_KEY);

		// ������ ���̹� ���� ��ü�� LinearLayout�� �߰���Ų��.
		MapContainer.addView(mMapView);

		// ������ ��ġ�� �� �ֵ��� �ɼ� Ȱ��ȭ
		mMapView.setClickable(true);
		
		// Ȯ��/��Ҹ� ���� �� ��Ʈ�ѷ� ǥ�� �ɼ� Ȱ��ȭ
		mMapView.setBuiltInZoomControls(true, null);	

		// ������ ���� ���� ���� �̺�Ʈ ����
		mMapView.setOnMapStateChangeListener(this);
		/******************* ���� �ʱ�ȭ �� ********************/
		
		
		/******************* �������� ���� �ڵ� ���� ********************/
		// �������� ���ҽ� ������ü �Ҵ�
		mMapViewerResourceProvider = new NMapViewerResourceProvider(this);

		// �������� ������ �߰�
		mOverlayManager = new NMapOverlayManager(this, mMapView, 
									mMapViewerResourceProvider);
		
		// �������̵��� �����ϱ� ���� id�� ����
		int markerId = NMapPOIflagType.PIN;

		// ǥ���� ��ġ �����͸� �����Ѵ�. ������ ���ڰ� �������̸� �ν��ϱ� ���� id��
		NMapPOIdata poiData = new NMapPOIdata(1, mMapViewerResourceProvider);
		poiData.beginPOIdata(1);
		AppManagement _AppManager = (AppManagement) getApplication();
		
		/*poiData.addPOIitem(Double.parseDouble(_AppManager.m_MapInfomation.Latitude), 
				Double.parseDouble(_AppManager.m_MapInfomation.Longitude), 
				_AppManager.m_MapInfomation.Name, markerId, 0);*/
		
		poiData.addPOIitem(Double.parseDouble(_AppManager.m_MapInfomation.Longitude), 
				Double.parseDouble(_AppManager.m_MapInfomation.Latitude), 
				_AppManager.m_MapInfomation.Name, markerId, 0);
		poiData.endPOIdata();

		// ��ġ �����͸� ����Ͽ� �������� ����
		NMapPOIdataOverlay poiDataOverlay 
			= mOverlayManager.createPOIdataOverlay(poiData, null);
		
		// id���� 0���� ������ ��� �������̰� ǥ�õǰ� �ִ� ��ġ��
		// ������ �߽ɰ� ZOOM�� �缳��
		poiDataOverlay.showAllPOIdata(0);
		
		// �������� �̺�Ʈ ���
		mOverlayManager.setOnCalloutOverlayListener(this);
		/******************* �������� ���� �ڵ� �� ********************/
	}

	/**
	 * ������ �ʱ�ȭ�� �� ȣ��ȴ�.
	 * ���������� �ʱ�ȭ�Ǹ� errorInfo ��ü�� null�� ���޵Ǹ�,
	 * �ʱ�ȭ ���� �� errorInfo��ü�� ���� ������ ���޵ȴ�
	 */
	public void onMapInitHandler(NMapView mapview, NMapError errorInfo) {
		if (errorInfo == null)
		{ 
			AppManagement _AppManager = (AppManagement) getApplication();

			// success
			mMapController.setMapCenter(
						new NGeoPoint(Double.parseDouble(_AppManager.m_MapInfomation.Longitude),
								Double.parseDouble(_AppManager.m_MapInfomation.Latitude)), 11);
		} else { // fail
			android.util.Log.e("NMAP", "onMapInitHandler: error=" 
						+ errorInfo.toString());
		}
	}

	/**
	 * ���� ���� ���� �� ȣ��Ǹ� ����� ���� ������ �Ķ���ͷ� ���޵ȴ�.
	 */
	public void onZoomLevelChange(NMapView mapview, int level) {}

	/**
	 * ���� �߽� ���� �� ȣ��Ǹ� ����� �߽� ��ǥ�� �Ķ���ͷ� ���޵ȴ�.
	 */
	public void onMapCenterChange(NMapView mapview, NGeoPoint center) {}

	/**
	 * ���� �ִϸ��̼� ���� ���� �� ȣ��ȴ�.
	 * animType : ANIMATION_TYPE_PAN or ANIMATION_TYPE_ZOOM
	 * animState : ANIMATION_STATE_STARTED or ANIMATION_STATE_FINISHED
	 */
	public void onAnimationStateChange(
					NMapView arg0, int animType, int animState) {}

	public void onMapCenterChangeFine(NMapView arg0) {}
	
	
	/** �������̰� Ŭ���Ǿ��� ���� �̺�Ʈ */
	public NMapCalloutOverlay onCreateCalloutOverlay(NMapOverlay arg0,
			NMapOverlayItem arg1, Rect arg2) {
		
		return new NMapCalloutBasicOverlay(arg0, arg1, arg2);

		/*Toast.makeText(this, arg1.getTitle(),
					Toast.LENGTH_SHORT).show();*/
		//return null;
	}
	
	
}
