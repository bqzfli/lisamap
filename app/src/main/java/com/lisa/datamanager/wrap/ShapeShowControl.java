package com.lisa.datamanager.wrap;

import srs.Display.Setting;
import srs.Utility.ConstSet;
import android.view.View;

/**
 * 鏍规嵁閰嶇疆鏂囦欢閲嶆柊璁剧疆鍚勪釜鐭㈤噺鐨勬樉绀烘牱寮�
 * 
 * @author BSJL
 *
 */
public class ShapeShowControl {

	static int PolygonOutlineWidth = 4;
	static int PolylineWidth = 2;
	static int PointSize = 20;

	/**
	 * 閲嶆柊璁剧疆鍚勪釜鍥惧眰鐨勫昂瀵搞��
	 * 
	 */
	public static void SetStyles(View v) {
		// WholeTask wTask =
		// GlobalTaskCollection.getWholeTaskList().get(GlobalTaskCollection.getSelectedWholeIndex());

		/*
		 * IFeatureLayer lVillageLayer=(IFeatureLayer)wTask.GetLayer("1"); int
		 * color= ((ISimpleRenderer)lVillageLayer.Renderer()).Symbol().Color();
		 * (
		 * (ISimpleRenderer)lVillageLayer.Renderer()).Symbol().Color(Color.rgb(255
		 * , 0,0));
		 */

		/*
		 * IFeatureLayer lVillageLayer=(IFeatureLayer)wTask.GetLayer("2"); int
		 * color= ((ISimpleRenderer)lVillageLayer.Renderer()).Symbol().Color();
		 * (
		 * (ISimpleRenderer)lVillageLayer.Renderer()).Symbol().Color(Color.argb(
		 * 255, Color.red(color), Color.green(color), Color.blue(color)));
		 * 
		 * IFeatureLayer lSampleLayer=(IFeatureLayer)wTask.GetLayer("3"); color=
		 * ((ISimpleRenderer)lSampleLayer.Renderer()).Symbol().Color();
		 * ((ISimpleRenderer
		 * )lSampleLayer.Renderer()).Symbol().Color(Color.argb(255,
		 * Color.red(color), Color.green(color), Color.blue(color)));
		 * 
		 * IFeatureLayer lNatureLayer=(IFeatureLayer)wTask.GetLayer("4"); color=
		 * ((ISimpleRenderer)lNatureLayer.Renderer()).Symbol().Color();
		 * ((ISimpleRenderer
		 * )lNatureLayer.Renderer()).Symbol().Color(Color.argb(255,
		 * Color.red(color), Color.green(color), Color.blue(color)));
		 * 
		 * IFeatureLayer lNaviPLayer=(IFeatureLayer)wTask.GetLayer("7"); color=
		 * ((ISimpleRenderer)lNaviPLayer.Renderer()).Symbol().Color();
		 * 
		 * IFeatureLayer lSurveyLayer=(IFeatureLayer)wTask.GetLayer("11");
		 * color= ((ISimpleRenderer)lSurveyLayer.Renderer()).Symbol().Color();
		 * ((
		 * ISimpleRenderer)lSurveyLayer.Renderer()).Symbol().Color(Color.argb(255
		 * , Color.red(color), Color.green(color), Color.blue(color)));
		 * 
		 * IFeatureLayer lLineLayer=(IFeatureLayer)wTask.GetLayer("12"); color=
		 * ((ISimpleRenderer)lLineLayer.Renderer()).Symbol().Color();
		 * 
		 * IFeatureLayer lPointLayer=(IFeatureLayer)wTask.GetLayer("13"); color=
		 * ((ISimpleRenderer)lPointLayer.Renderer()).Symbol().Color();
		 * 
		 * PolygonOutlineWidth=PWControl.getPolygonOutLineWidthOfLayer();
		 * PolylineWidth=PWControl.getPolylineWidthOfLayer();
		 * PointSize=PWControl.getPointSizeOfLayer();
		 * 
		 * ((SimpleFillSymbol)((ISimpleRenderer)lVillageLayer.Renderer()).Symbol(
		 * )).OutLineSymbol().Width(2);
		 * ((SimpleFillSymbol)((ISimpleRenderer)lSampleLayer
		 * .Renderer()).Symbol()).OutLineSymbol().Width(PolygonOutlineWidth);
		 * ((SimpleFillSymbol
		 * )((ISimpleRenderer)lNatureLayer.Renderer()).Symbol()
		 * ).OutLineSymbol().Width(PolygonOutlineWidth);
		 * ((SimpleFillSymbol)((ISimpleRenderer
		 * )lSurveyLayer.Renderer()).Symbol()
		 * ).OutLineSymbol().Width(PolygonOutlineWidth);
		 * 
		 * ((SimpleLineSymbol)((ISimpleRenderer)lLineLayer.Renderer()).Symbol()).
		 * Width(PolylineWidth);
		 * ((SimplePointSymbol)((ISimpleRenderer)lNaviPLayer
		 * .Renderer()).Symbol()).Size(PointSize);
		 * ((SimplePointSymbol)((ISimpleRenderer
		 * )lPointLayer.Renderer()).Symbol()).Size(PointSize);
		 */

		ConstSet.TrackBorderWidth = PolylineWidth;

		Setting.SelectLineFeatureStyle.setWidth(PolylineWidth);

	}
}
