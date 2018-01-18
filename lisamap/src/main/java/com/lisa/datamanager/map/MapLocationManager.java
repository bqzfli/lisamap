package com.lisa.datamanager.map;

import android.graphics.Bitmap;
import android.util.Log;

import com.Factory.FactoryGPS;

import java.io.IOException;
import srs.Element.IElement;
import srs.Element.IPicElement;
import srs.Element.PicElement;
import srs.GPS.GPSControl;
import srs.GPS.GPSConvert;
import srs.Geometry.Envelope;
import srs.Geometry.IEnvelope;
import srs.Geometry.IPoint;
import srs.Geometry.Point;
import srs.Map.IMap;
import srs.tools.MapControl;

/**
 * Created by lisa on 2016/12/15.
 */
public class MapLocationManager {

    private static Bitmap mBmpMyposition = null;
    private static IPoint mGeoMyposition = null;
    private static IElement mElementMyposition = null;

    public static Bitmap getBmpMyposition(){
        return mBmpMyposition;
    }

    public static void setBmpMyposition(Bitmap bmp) {
        mBmpMyposition = bmp;
        mElementMyposition = new PicElement();
        //((IPicElement) mElementMyposition).setPic(mBmpMyposition, -mBmpMyposition.getWidth() / 2, -mBmpMyposition.getHeight() / 2);
    }


    public static void addMyLocationElements(double ownerLongitude, double ownerLatitude) throws IOException {
        //owner point
        double[] xyOwner = GPSConvert.GEO2PROJECT(ownerLongitude, ownerLatitude, MapsManager.getMap().getGeoProjectType());
        mGeoMyposition.X(xyOwner[0]);
        mGeoMyposition.Y(xyOwner[1]);
        mElementMyposition.setGeometry(mGeoMyposition);
        MapsManager.getMap().getElementContainer().AddElement(mElementMyposition);
    }


    public static void addMypositionElement(double myPositionLong, double myPositionLat, float myPositionDirect) throws IOException {
        MapsManager.getMap().getElementContainer().RemoveElement(mElementMyposition);
        //build geometrys -- My position point
        double[] xyOwner = GPSConvert.GEO2PROJECT(myPositionLong, myPositionLat, MapsManager.getMap().getGeoProjectType());
        mGeoMyposition.X(xyOwner[0]);
        mGeoMyposition.Y(xyOwner[1]);
        mElementMyposition.setGeometry(mGeoMyposition);
        //rotate the bitmap and set pic to element
        Bitmap bmpRotated = MapsUtil.rotateImg(mBmpMyposition, myPositionDirect);
        ((IPicElement) mElementMyposition).setPic(bmpRotated, -bmpRotated.getWidth() / 2, -bmpRotated.getHeight() / 2);
        MapsManager.getMap().getElementContainer().AddElement(mElementMyposition);
    }

    /**
     * 设置当前位置屏幕局中
     *  @param mapControl  地图控件
     */
    public static boolean SetLocationCenter(MapControl mapControl) {

        IEnvelope mapEnv = MapsManager.getMap().getExtent();
        GPSControl gpsControl = GPSControl.getInstance();
        if(gpsControl.GPSLongitude==0|| gpsControl.GPSLatitude==0){
            Log.i("MapLocationManager","LOG:"+gpsControl.GPSLongitude+"-----"+"LAT:"+gpsControl.GPSLatitude);
            return false;
        }
        double xy[] = GPSConvert.GEO2PROJECT(gpsControl.GPSLongitude,
                gpsControl.GPSLatitude,  MapsManager.getMap().getGeoProjectType());
        IPoint centerPoint = new Point(xy[0], xy[1]);
        double width = mapEnv.XMax() - mapEnv.XMin();
        double height = mapEnv.YMax() - mapEnv.YMin();
        IEnvelope envelope = new Envelope(centerPoint.X() - width / 2,
                centerPoint.Y() - height / 2, centerPoint.X() + width / 2,
                centerPoint.Y() + height / 2);
        mapControl.getActiveView().FocusMap().setExtent(envelope);
        mapControl.Refresh();
        return  true;
    }


    public static void dispose(){
        try {
            MapsManager.getMap().getElementContainer().RemoveElement(mElementMyposition);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("MapLocationManager",e.getMessage());
        }
        if(mElementMyposition!=null) {
            ((IPicElement) mElementMyposition).dispose();
        }
        mGeoMyposition = null;
        mBmpMyposition = null;
    }

}
