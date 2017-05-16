package srs.Layer;

import org.gdal.gdal.Dataset;
import srs.Geometry.IEnvelope;
import srs.Utility.sRSException;
import android.graphics.RectF;

public interface IRasterLayer extends ILayer{
	Dataset getRasterData();
	void setRasterData(Dataset value) throws sRSException, Exception;
	IEnvelope getRealSize();
	RectF getDeviceExtent();
	int getOverview();
	void setOverview(int value);
	
}