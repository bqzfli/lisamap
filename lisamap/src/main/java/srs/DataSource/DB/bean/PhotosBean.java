package srs.DataSource.DB.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @ClassName: PhotosBean
* @Description: TODO(这里用一句话描述这个类的作用)
* @Version: V1.0.0.0
* @author lisa
* @date 2016年12月25日 下午2:48:18
***********************************
* @editor lisa 
* @data 2016年12月25日 下午2:48:18
* @todo TODO
*/
public class PhotosBean {
	public String photoFileName;// 鍥剧墖瀛樺偍鍚�
	public String photoLat;//鍥剧墖绾害
	public String photoLon;//鍥剧墖缁忓害
	public String photoDate;//鍥剧墖鏃ユ湡
	public List<String> markList = new ArrayList<String>();//鍥剧墖鏍囪淇℃伅
	public Map<String, String> markMap = new HashMap<String, String>();
	
	
	
	public PhotosBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public PhotosBean(String photoFileName, String photoLat, String photoLon,
			String photoDate, List<String> markList) {
		super();
		this.photoFileName = photoFileName;
		this.photoLat = photoLat;
		this.photoLon = photoLon;
		this.photoDate = photoDate;
		this.markList = markList;
	}
	public PhotosBean(String photoFileName, String photoLat, String photoLon,
			String photoDate, Map<String, String> markMap) {
		super();
		this.photoFileName = photoFileName;
		this.photoLat = photoLat;
		this.photoLon = photoLon;
		this.photoDate = photoDate;
		this.markMap = markMap;
	}
	public String getPhotoLat() {
		return photoLat;
	}

	public void setPhotoLat(String photoLat) {
		this.photoLat = photoLat;
	}

	public String getPhotoLon() {
		return photoLon;
	}

	public void setPhotoLon(String photoLon) {
		this.photoLon = photoLon;
	}

	public String getPhotoDate() {
		return photoDate;
	}

	public void setPhotoDate(String photoDate) {
		this.photoDate = photoDate;
	}

	public List<String> getMarkList() {
		return markList;
	}

	public void setMarkList(List<String> markList) {
		this.markList = markList;
	}

	public String getPhotoFileName() {
		return photoFileName;
	}

	public void setPhotoFileName(String photoFileName) {
		this.photoFileName = photoFileName;
	}


	public Map<String, String> getMarkMap() {
		return markMap;
	}


	public void setMarkMap(Map<String, String> markMap) {
		this.markMap = markMap;
	}
	
}
