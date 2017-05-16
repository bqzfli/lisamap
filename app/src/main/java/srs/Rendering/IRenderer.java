package srs.Rendering;

import java.lang.reflect.InvocationTargetException;

import srs.Utility.sRSException;

public interface IRenderer {
	/** 
	 透明度
	 
	*/
	float getTransparency();
	void setTransparency(float value);
	/** 
	 克隆该对象
	 
	 @return 渲染方式的副本
	*/
	IRenderer Clone();
	/** 
	 创建图例项
	 
	 @param layerItem 需要创建图例项的图层项
	 @return 图层项
	*//*
	ILayerItem CreateLegendItems(ILayerItem layerItem);*/

	void LoadXMLData(org.dom4j.Element node)throws SecurityException, 
	IllegalArgumentException, sRSException,
	NoSuchMethodException, InstantiationException, 
	IllegalAccessException, InvocationTargetException,
	ClassNotFoundException;
	

	void SaveXMLData(org.dom4j.Element node);
}