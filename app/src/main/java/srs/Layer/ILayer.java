package srs.Layer;

import srs.CoordinateSystem.ICoordinateSystem;
import srs.Geometry.IEnvelope;
import srs.Layer.Event.LayerActiveChangedManager;
import srs.Layer.Event.LayerNameChangedManager;
import srs.Layer.Event.LayerRendererChangedManager;
import srs.Rendering.IRenderer;
import srs.Utility.sRSException;

public interface ILayer{
	/** 名称*/
	String getName();
	void setName(String value);
	/** 来源*/
	String getSource();
	/** 是否显示*/
	boolean getVisible();
	void setVisible(boolean value);
	/** 是否活跃*/
	boolean isActive();
	void setActive();
	/** 最大比例*/
	double getMaximumScale();
	void setMaximumScale(double value);
	/** 最小比例*/
	double getMinimumScale();
	void setMinimumScale(double value);
	/** 是否可用*/
	boolean getUseAble();
	/** 范围*/
	IEnvelope getExtent();
	/** 渲染*/
	IRenderer getRenderer();
	void setRenderer(IRenderer value) throws sRSException;
	/** 坐标系*/
	ICoordinateSystem getCoordinateSystem();
	void setCoordinateSystem(ICoordinateSystem value);
	

	LayerActiveChangedManager getLayerActiveChanged();
	LayerNameChangedManager getLayerNameChanged();
	LayerRendererChangedManager getLayerRendererChanged();
	
	 void dispose() throws Exception;
}