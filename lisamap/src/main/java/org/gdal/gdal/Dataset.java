/*     */ package org.gdal.gdal;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class Dataset extends MajorObject
/*     */ {
/*     */   private long swigCPtr;
/*     */ 
/*     */   public Dataset(long cPtr, boolean cMemoryOwn)
/*     */   {
/*  15 */     super(gdalJNI.Dataset_SWIGUpcast(cPtr), cMemoryOwn);
/*  16 */     this.swigCPtr = cPtr;
/*     */   }
/*     */ 
/*     */   public static long getCPtr(Dataset obj) {
/*  20 */     return (obj == null) ? 0L : obj.swigCPtr;
/*     */   }
/*     */ 
/*     */   protected void finalize() {
/*  24 */     delete();
/*     */   }
/*     */ 
/*     */   public synchronized void delete() {
/*  28 */     if (this.swigCPtr != 0L) {
/*  29 */       if (this.swigCMemOwn) {
/*  30 */         this.swigCMemOwn = false;
/*  31 */         gdalJNI.delete_Dataset(this.swigCPtr);
/*     */       }
/*  33 */       this.swigCPtr = 0L;
/*     */     }
/*  35 */     super.delete();
/*     */   }
/*     */ 
/*     */   public int GetRasterXSize()
/*     */   {
/*  40 */     return getRasterXSize();
/*     */   }
/*     */   public int GetRasterYSize() {
/*  43 */     return getRasterYSize();
/*     */   }
/*     */   public int GetRasterCount() {
/*  46 */     return getRasterCount();
/*     */   }
/*     */   public int BuildOverviews(int[] overviewlist, ProgressCallback callback) {
/*  49 */     return BuildOverviews(null, overviewlist, callback);
/*     */   }
/*     */ 
/*     */   public int BuildOverviews(int[] overviewlist) {
/*  53 */     return BuildOverviews(null, overviewlist, null);
/*     */   }
/*     */ 
/*     */   public Vector GetGCPs() {
/*  57 */     Vector gcps = new Vector();
/*  58 */     GetGCPs(gcps);
/*  59 */     return gcps;
/*     */   }
/*     */ 
/*     */   public double[] GetGeoTransform() {
/*  63 */     double[] adfGeoTransform = new double[6];
/*  64 */     GetGeoTransform(adfGeoTransform);
/*  65 */     return adfGeoTransform;
/*     */   }
/*     */ 
/*     */   public int getRasterXSize() {
/*  69 */     return gdalJNI.Dataset_RasterXSize_get(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public int getRasterYSize() {
/*  73 */     return gdalJNI.Dataset_RasterYSize_get(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public int getRasterCount() {
/*  77 */     return gdalJNI.Dataset_RasterCount_get(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public Driver GetDriver() {
/*  81 */     long cPtr = gdalJNI.Dataset_GetDriver(this.swigCPtr, this);
/*  82 */     return (cPtr == 0L) ? null : new Driver(cPtr, false);
/*     */   }
/*     */ 
/*     */   public Band GetRasterBand(int nBand) {
/*  86 */     long cPtr = gdalJNI.Dataset_GetRasterBand(this.swigCPtr, this, nBand);
/*  87 */     Band ret = null;
/*  88 */     if (cPtr != 0L) {
/*  89 */       ret = new Band(cPtr, false);
/*  90 */       ret.addReference(this);
/*     */     }
/*  92 */     return ret;
/*     */   }
/*     */ 
/*     */   public String GetProjection() {
/*  96 */     return gdalJNI.Dataset_GetProjection(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public String GetProjectionRef() {
/* 100 */     return gdalJNI.Dataset_GetProjectionRef(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public int SetProjection(String prj) {
/* 104 */     return gdalJNI.Dataset_SetProjection(this.swigCPtr, this, prj);
/*     */   }
/*     */ 
/*     */   public void GetGeoTransform(double[] argout) {
/* 108 */     gdalJNI.Dataset_GetGeoTransform(this.swigCPtr, this, argout);
/*     */   }
/*     */ 
/*     */   public int SetGeoTransform(double[] argin) {
/* 112 */     return gdalJNI.Dataset_SetGeoTransform(this.swigCPtr, this, argin);
/*     */   }
/*     */ 
/*     */   public int BuildOverviews(String resampling, int[] overviewlist, ProgressCallback callback) {
/* 116 */     return gdalJNI.Dataset_BuildOverviews__SWIG_0(this.swigCPtr, this, resampling, overviewlist, callback);
/*     */   }
/*     */ 
/*     */   public int BuildOverviews(String resampling, int[] overviewlist) {
/* 120 */     return gdalJNI.Dataset_BuildOverviews__SWIG_2(this.swigCPtr, this, resampling, overviewlist);
/*     */   }
/*     */ 
/*     */   public int GetGCPCount() {
/* 124 */     return gdalJNI.Dataset_GetGCPCount(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public String GetGCPProjection() {
/* 128 */     return gdalJNI.Dataset_GetGCPProjection(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public void GetGCPs(Vector nGCPs) {
/* 132 */     gdalJNI.Dataset_GetGCPs(this.swigCPtr, this, nGCPs);
/*     */   }
/*     */ 
/*     */   public int SetGCPs(GCP[] nGCPs, String pszGCPProjection) {
/* 136 */     return gdalJNI.Dataset_SetGCPs(this.swigCPtr, this, nGCPs, pszGCPProjection);
/*     */   }
/*     */ 
/*     */   public void FlushCache() {
/* 140 */     gdalJNI.Dataset_FlushCache(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public int AddBand(int datatype, Vector options) {
/* 144 */     return gdalJNI.Dataset_AddBand__SWIG_0(this.swigCPtr, this, datatype, options);
/*     */   }
/*     */ 
/*     */   public int AddBand(int datatype) {
/* 148 */     return gdalJNI.Dataset_AddBand__SWIG_1(this.swigCPtr, this, datatype);
/*     */   }
/*     */ 
/*     */   public int AddBand() {
/* 152 */     return gdalJNI.Dataset_AddBand__SWIG_2(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public int CreateMaskBand(int nFlags) {
/* 156 */     return gdalJNI.Dataset_CreateMaskBand(this.swigCPtr, this, nFlags);
/*     */   }
/*     */ 
/*     */   public Vector GetFileList() {
/* 160 */     return gdalJNI.Dataset_GetFileList(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public int ReadRaster_Direct(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, ByteBuffer nioBuffer, int[] band_list, int nPixelSpace, int nLineSpace, int nBandSpace) {
/* 164 */     return gdalJNI.Dataset_ReadRaster_Direct__SWIG_0(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, nioBuffer, band_list, nPixelSpace, nLineSpace, nBandSpace);
/*     */   }
/*     */ 
/*     */   public int ReadRaster_Direct(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, ByteBuffer nioBuffer, int[] band_list, int nPixelSpace, int nLineSpace) {
/* 168 */     return gdalJNI.Dataset_ReadRaster_Direct__SWIG_1(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, nioBuffer, band_list, nPixelSpace, nLineSpace);
/*     */   }
/*     */ 
/*     */   public int ReadRaster_Direct(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, ByteBuffer nioBuffer, int[] band_list, int nPixelSpace) {
/* 172 */     return gdalJNI.Dataset_ReadRaster_Direct__SWIG_2(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, nioBuffer, band_list, nPixelSpace);
/*     */   }
/*     */ 
/*     */   public int ReadRaster_Direct(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, ByteBuffer nioBuffer, int[] band_list) {
/* 176 */     return gdalJNI.Dataset_ReadRaster_Direct__SWIG_3(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, nioBuffer, band_list);
/*     */   }
/*     */ 
/*     */   public int ReadRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, byte[] regularArrayOut, int[] band_list, int nPixelSpace, int nLineSpace, int nBandSpace) {
/* 180 */     return gdalJNI.Dataset_ReadRaster__SWIG_0(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayOut, band_list, nPixelSpace, nLineSpace, nBandSpace);
/*     */   }
/*     */ 
/*     */   public int ReadRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, byte[] regularArrayOut, int[] band_list, int nPixelSpace, int nLineSpace) {
/* 184 */     return gdalJNI.Dataset_ReadRaster__SWIG_1(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayOut, band_list, nPixelSpace, nLineSpace);
/*     */   }
/*     */ 
/*     */   public int ReadRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, byte[] regularArrayOut, int[] band_list, int nPixelSpace) {
/* 188 */     return gdalJNI.Dataset_ReadRaster__SWIG_2(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayOut, band_list, nPixelSpace);
/*     */   }
/*     */ 
/*     */   public int ReadRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, byte[] regularArrayOut, int[] band_list) {
/* 192 */     return gdalJNI.Dataset_ReadRaster__SWIG_3(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayOut, band_list);
/*     */   }
/*     */ 
/*     */   public int ReadRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, short[] regularArrayOut, int[] band_list, int nPixelSpace, int nLineSpace, int nBandSpace) {
/* 196 */     return gdalJNI.Dataset_ReadRaster__SWIG_4(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayOut, band_list, nPixelSpace, nLineSpace, nBandSpace);
/*     */   }
/*     */ 
/*     */   public int ReadRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, short[] regularArrayOut, int[] band_list, int nPixelSpace, int nLineSpace) {
/* 200 */     return gdalJNI.Dataset_ReadRaster__SWIG_5(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayOut, band_list, nPixelSpace, nLineSpace);
/*     */   }
/*     */ 
/*     */   public int ReadRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, short[] regularArrayOut, int[] band_list, int nPixelSpace) {
/* 204 */     return gdalJNI.Dataset_ReadRaster__SWIG_6(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayOut, band_list, nPixelSpace);
/*     */   }
/*     */ 
/*     */   public int ReadRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, short[] regularArrayOut, int[] band_list) {
/* 208 */     return gdalJNI.Dataset_ReadRaster__SWIG_7(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayOut, band_list);
/*     */   }
/*     */ 
/*     */   public int ReadRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, int[] regularArrayOut, int[] band_list, int nPixelSpace, int nLineSpace, int nBandSpace) {
/* 212 */     return gdalJNI.Dataset_ReadRaster__SWIG_8(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayOut, band_list, nPixelSpace, nLineSpace, nBandSpace);
/*     */   }
/*     */ 
/*     */   public int ReadRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, int[] regularArrayOut, int[] band_list, int nPixelSpace, int nLineSpace) {
/* 216 */     return gdalJNI.Dataset_ReadRaster__SWIG_9(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayOut, band_list, nPixelSpace, nLineSpace);
/*     */   }
/*     */ 
/*     */   public int ReadRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, int[] regularArrayOut, int[] band_list, int nPixelSpace) {
/* 220 */     return gdalJNI.Dataset_ReadRaster__SWIG_10(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayOut, band_list, nPixelSpace);
/*     */   }
/*     */ 
/*     */   public int ReadRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, int[] regularArrayOut, int[] band_list) {
/* 224 */     return gdalJNI.Dataset_ReadRaster__SWIG_11(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayOut, band_list);
/*     */   }
/*     */ 
/*     */   public int ReadRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, float[] regularArrayOut, int[] band_list, int nPixelSpace, int nLineSpace, int nBandSpace) {
/* 228 */     return gdalJNI.Dataset_ReadRaster__SWIG_12(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayOut, band_list, nPixelSpace, nLineSpace, nBandSpace);
/*     */   }
/*     */ 
/*     */   public int ReadRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, float[] regularArrayOut, int[] band_list, int nPixelSpace, int nLineSpace) {
/* 232 */     return gdalJNI.Dataset_ReadRaster__SWIG_13(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayOut, band_list, nPixelSpace, nLineSpace);
/*     */   }
/*     */ 
/*     */   public int ReadRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, float[] regularArrayOut, int[] band_list, int nPixelSpace) {
/* 236 */     return gdalJNI.Dataset_ReadRaster__SWIG_14(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayOut, band_list, nPixelSpace);
/*     */   }
/*     */ 
/*     */   public int ReadRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, float[] regularArrayOut, int[] band_list) {
/* 240 */     return gdalJNI.Dataset_ReadRaster__SWIG_15(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayOut, band_list);
/*     */   }
/*     */ 
/*     */   public int ReadRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, double[] regularArrayOut, int[] band_list, int nPixelSpace, int nLineSpace, int nBandSpace) {
/* 244 */     return gdalJNI.Dataset_ReadRaster__SWIG_16(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayOut, band_list, nPixelSpace, nLineSpace, nBandSpace);
/*     */   }
/*     */ 
/*     */   public int ReadRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, double[] regularArrayOut, int[] band_list, int nPixelSpace, int nLineSpace) {
/* 248 */     return gdalJNI.Dataset_ReadRaster__SWIG_17(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayOut, band_list, nPixelSpace, nLineSpace);
/*     */   }
/*     */ 
/*     */   public int ReadRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, double[] regularArrayOut, int[] band_list, int nPixelSpace) {
/* 252 */     return gdalJNI.Dataset_ReadRaster__SWIG_18(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayOut, band_list, nPixelSpace);
/*     */   }
/*     */ 
/*     */   public int ReadRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, double[] regularArrayOut, int[] band_list) {
/* 256 */     return gdalJNI.Dataset_ReadRaster__SWIG_19(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayOut, band_list);
/*     */   }
/*     */ 
/*     */   public int WriteRaster_Direct(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, ByteBuffer nioBuffer, int[] band_list, int nPixelSpace, int nLineSpace, int nBandSpace) {
/* 260 */     return gdalJNI.Dataset_WriteRaster_Direct__SWIG_0(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, nioBuffer, band_list, nPixelSpace, nLineSpace, nBandSpace);
/*     */   }
/*     */ 
/*     */   public int WriteRaster_Direct(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, ByteBuffer nioBuffer, int[] band_list, int nPixelSpace, int nLineSpace) {
/* 264 */     return gdalJNI.Dataset_WriteRaster_Direct__SWIG_1(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, nioBuffer, band_list, nPixelSpace, nLineSpace);
/*     */   }
/*     */ 
/*     */   public int WriteRaster_Direct(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, ByteBuffer nioBuffer, int[] band_list, int nPixelSpace) {
/* 268 */     return gdalJNI.Dataset_WriteRaster_Direct__SWIG_2(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, nioBuffer, band_list, nPixelSpace);
/*     */   }
/*     */ 
/*     */   public int WriteRaster_Direct(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, ByteBuffer nioBuffer, int[] band_list) {
/* 272 */     return gdalJNI.Dataset_WriteRaster_Direct__SWIG_3(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, nioBuffer, band_list);
/*     */   }
/*     */ 
/*     */   public int WriteRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, byte[] regularArrayIn, int[] band_list, int nPixelSpace, int nLineSpace, int nBandSpace) {
/* 276 */     return gdalJNI.Dataset_WriteRaster__SWIG_0(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayIn, band_list, nPixelSpace, nLineSpace, nBandSpace);
/*     */   }
/*     */ 
/*     */   public int WriteRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, byte[] regularArrayIn, int[] band_list, int nPixelSpace, int nLineSpace) {
/* 280 */     return gdalJNI.Dataset_WriteRaster__SWIG_1(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayIn, band_list, nPixelSpace, nLineSpace);
/*     */   }
/*     */ 
/*     */   public int WriteRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, byte[] regularArrayIn, int[] band_list, int nPixelSpace) {
/* 284 */     return gdalJNI.Dataset_WriteRaster__SWIG_2(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayIn, band_list, nPixelSpace);
/*     */   }
/*     */ 
/*     */   public int WriteRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, byte[] regularArrayIn, int[] band_list) {
/* 288 */     return gdalJNI.Dataset_WriteRaster__SWIG_3(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayIn, band_list);
/*     */   }
/*     */ 
/*     */   public int WriteRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, short[] regularArrayIn, int[] band_list, int nPixelSpace, int nLineSpace, int nBandSpace) {
/* 292 */     return gdalJNI.Dataset_WriteRaster__SWIG_4(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayIn, band_list, nPixelSpace, nLineSpace, nBandSpace);
/*     */   }
/*     */ 
/*     */   public int WriteRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, short[] regularArrayIn, int[] band_list, int nPixelSpace, int nLineSpace) {
/* 296 */     return gdalJNI.Dataset_WriteRaster__SWIG_5(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayIn, band_list, nPixelSpace, nLineSpace);
/*     */   }
/*     */ 
/*     */   public int WriteRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, short[] regularArrayIn, int[] band_list, int nPixelSpace) {
/* 300 */     return gdalJNI.Dataset_WriteRaster__SWIG_6(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayIn, band_list, nPixelSpace);
/*     */   }
/*     */ 
/*     */   public int WriteRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, short[] regularArrayIn, int[] band_list) {
/* 304 */     return gdalJNI.Dataset_WriteRaster__SWIG_7(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayIn, band_list);
/*     */   }
/*     */ 
/*     */   public int WriteRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, int[] regularArrayIn, int[] band_list, int nPixelSpace, int nLineSpace, int nBandSpace) {
/* 308 */     return gdalJNI.Dataset_WriteRaster__SWIG_8(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayIn, band_list, nPixelSpace, nLineSpace, nBandSpace);
/*     */   }
/*     */ 
/*     */   public int WriteRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, int[] regularArrayIn, int[] band_list, int nPixelSpace, int nLineSpace) {
/* 312 */     return gdalJNI.Dataset_WriteRaster__SWIG_9(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayIn, band_list, nPixelSpace, nLineSpace);
/*     */   }
/*     */ 
/*     */   public int WriteRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, int[] regularArrayIn, int[] band_list, int nPixelSpace) {
/* 316 */     return gdalJNI.Dataset_WriteRaster__SWIG_10(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayIn, band_list, nPixelSpace);
/*     */   }
/*     */ 
/*     */   public int WriteRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, int[] regularArrayIn, int[] band_list) {
/* 320 */     return gdalJNI.Dataset_WriteRaster__SWIG_11(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayIn, band_list);
/*     */   }
/*     */ 
/*     */   public int WriteRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, float[] regularArrayIn, int[] band_list, int nPixelSpace, int nLineSpace, int nBandSpace) {
/* 324 */     return gdalJNI.Dataset_WriteRaster__SWIG_12(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayIn, band_list, nPixelSpace, nLineSpace, nBandSpace);
/*     */   }
/*     */ 
/*     */   public int WriteRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, float[] regularArrayIn, int[] band_list, int nPixelSpace, int nLineSpace) {
/* 328 */     return gdalJNI.Dataset_WriteRaster__SWIG_13(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayIn, band_list, nPixelSpace, nLineSpace);
/*     */   }
/*     */ 
/*     */   public int WriteRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, float[] regularArrayIn, int[] band_list, int nPixelSpace) {
/* 332 */     return gdalJNI.Dataset_WriteRaster__SWIG_14(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayIn, band_list, nPixelSpace);
/*     */   }
/*     */ 
/*     */   public int WriteRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, float[] regularArrayIn, int[] band_list) {
/* 336 */     return gdalJNI.Dataset_WriteRaster__SWIG_15(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayIn, band_list);
/*     */   }
/*     */ 
/*     */   public int WriteRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, double[] regularArrayIn, int[] band_list, int nPixelSpace, int nLineSpace, int nBandSpace) {
/* 340 */     return gdalJNI.Dataset_WriteRaster__SWIG_16(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayIn, band_list, nPixelSpace, nLineSpace, nBandSpace);
/*     */   }
/*     */ 
/*     */   public int WriteRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, double[] regularArrayIn, int[] band_list, int nPixelSpace, int nLineSpace) {
/* 344 */     return gdalJNI.Dataset_WriteRaster__SWIG_17(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayIn, band_list, nPixelSpace, nLineSpace);
/*     */   }
/*     */ 
/*     */   public int WriteRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, double[] regularArrayIn, int[] band_list, int nPixelSpace) {
/* 348 */     return gdalJNI.Dataset_WriteRaster__SWIG_18(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayIn, band_list, nPixelSpace);
/*     */   }
/*     */ 
/*     */   public int WriteRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, double[] regularArrayIn, int[] band_list) {
/* 352 */     return gdalJNI.Dataset_WriteRaster__SWIG_19(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayIn, band_list);
/*     */   }
/*     */ }

/* Location:           E:\temp\1117\gdal.jar
 * Qualified Name:     org.gdal.gdal.Dataset
 * JD-Core Version:    0.5.4
 */