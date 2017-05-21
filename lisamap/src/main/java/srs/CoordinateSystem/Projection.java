/**
 * 
 */
package srs.CoordinateSystem;

import java.lang.String;

/**投影
 * @author bqzf
 * @version 20150606
 *
 */
public class Projection {
		
		String mName;
        double mAzimuth;
        double mCentralMeridian;
        double mFalseEasting;
        double mFalseNorthing;
        double mLatitudeOfCenter;
        double mLongitudeOfCenter;
        double mLatitudeOfOrigin;
        double mLongitudeOfOrigin;
        double mScaleFactor;
        double mStandardParallel1;
        double mStandardParallel2;

        
        public Projection(){ 
        	this("未知",0,0,0,0,0,0,0,0,0,0,0);
        }

        public Projection(String name){
        	this(name, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        }

        public Projection(String name, double azimuth, double centralMeridian, double falseEasting,
            double falseNorthing, double latitudeOfCenter, double longitudeOfCenter, double latitudeOfOrigin,
            double longitudeOfOrigin, double scaleFactor, double standardParallel1, double standardParallel2){
        	mName = name;
            mAzimuth = azimuth;
            mCentralMeridian = centralMeridian;
            mFalseEasting = falseEasting;
            mFalseNorthing = falseNorthing;
            mLatitudeOfCenter = latitudeOfCenter;
            mLongitudeOfCenter = longitudeOfCenter;
            mLatitudeOfOrigin = latitudeOfOrigin;
            mLongitudeOfOrigin = longitudeOfOrigin;
            mScaleFactor = scaleFactor;
            mStandardParallel1 = standardParallel1;
            mStandardParallel2 = standardParallel2;
        }

        
        /**名称
         * @return 名称
         */
        public String getName(){
        	return mName;
        }
        
        /**名称
         * @param value 名称
         */
        public void setName(String value){
        	mName = value;
        }

        /**方位角
         * @return 方位角
         */
        public double getAzimuth(){
                return mAzimuth;
        }
        
        /**方位角
         * @param value 方位角
         */
        public void setAzimuth(double value){
            if (value >= 0 && value <= 360){
                mAzimuth = value;
            }
        }

        /**中央经线
         * @return 中央经线
         */
        public double getCentralMeridian(){
                return mCentralMeridian;
        }
        /**中央经线
         * @param value 中央经线
         */
        public void setCentralMeridian(double value){
            if (value >= 0 && value <= 360){
                mCentralMeridian = value;
            }
        }

        /**假东
         * @return 假东
         */
        public double getFalseEasting(){
                return mFalseEasting;
        }
        
        /**假东
         * @param value
         */
        public void setFalseEasting(double value){
                mFalseEasting = value;
        }

        /**假北
         * @return 假北
         */
        public double getFalseNorthing(){
                return mFalseNorthing;
        }
        
        /**假北
         * @param value
         */
        public void setFalseNorthing(double value){
                mFalseNorthing = value;
            
        }

        /**
         * @return
         */
        public double getLatitudeOfCenter(){
            return mLatitudeOfCenter;
        }
        /**
         * @param value
         */
        public void  setLatitudeOfCenter(double value){
            if (value >= -90 && value <= 90){
                mLatitudeOfCenter = value;
            }
        }

        public double getLongitudeOfCenter(){
              return mLongitudeOfCenter;
        }
        
        public void setLongitudeOfCenter(double value){
            if (value >= 0 && value <= 360){
                mLongitudeOfCenter = value;
            }
        }

        public double getLatitudeOfOrigin(){
                return mLatitudeOfOrigin;
        }
        
        public void setLatitudeOfOrigin(double value){
            if (value >= -90 && value <= 90){
                mLatitudeOfOrigin = value;
            }
        }

        public double getLongitudeOfOrigin(){
                return mLongitudeOfOrigin;
        }
        
        public void setLongitudeOfOrigin(double value){
            if (value >= 0 && value <= 360){
                mLongitudeOfOrigin = value;
            }
        }

        public double getScaleFactor(){
                return mScaleFactor;
        }
        
        public void setScaleFactor(double value){
            if (value > 0 && value <= 1){
                mScaleFactor = value;
            }
        }

        public double getStandardParallel1(){
                return mStandardParallel1;
        }
        
        public void setStandardParallel1(double value){
            if (value >= -90 && value <= 90){
                mStandardParallel1 = value;
            }
        }

        public double getStandardParallel2(){
                return mStandardParallel2;
        }
        
        public void setStandardParallel2(double value){
            if (value >= -90 && value <= 90){
                mStandardParallel2 = value;
            }
        }
		
  }



