package srs.Operation;

import srs.Geometry.IGeometry;
import srs.Geometry.IPoint;
import android.graphics.Bitmap;

public interface IFeedback {
    IPoint CurrentPoint();
	void CurrentPoint(IPoint value);
	IPoint StartPoint();
	void StartPoint(IPoint value);
	void TrackNew(IPoint point);
	void TrackMove(Bitmap bitmap, IPoint point);
	IGeometry GetExist(IPoint point);
	IGeometry GetCurrent();
}
