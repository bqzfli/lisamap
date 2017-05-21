package com.lisa.datamanager.map;

import android.graphics.Bitmap;
import java.io.IOException;
import srs.Element.IElement;
import srs.Element.IPicElement;
import srs.Element.PicElement;
import srs.GPS.GPSConvert;
import srs.Geometry.IPoint;

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


}
