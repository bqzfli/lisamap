/*     */ package org.gdal.gdal;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Vector;
/*     */ import org.gdal.gdalconst.gdalconstConstants;
/*     */ 
/*     */ public class Band extends MajorObject
/*     */ {
/*     */   private long swigCPtr;
/*     */ 
/*     */   public Band(long cPtr, boolean cMemoryOwn)
/*     */   {
/*  17 */     super(gdalJNI.Band_SWIGUpcast(cPtr), cMemoryOwn);
/*  18 */     this.swigCPtr = cPtr;
/*     */   }
/*     */ 
/*     */   public static long getCPtr(Band obj) {
/*  22 */     return (obj == null) ? 0L : obj.swigCPtr;
/*     */   }
/*     */ 
/*     */   public synchronized void delete() {
/*  26 */     if (this.swigCPtr != 0L) {
/*  27 */       if (this.swigCMemOwn) {
/*  28 */         this.swigCMemOwn = false;
/*  29 */         throw new UnsupportedOperationException("C++ destructor does not have public access");
/*     */       }
/*  31 */       this.swigCPtr = 0L;
/*     */     }
/*  33 */     super.delete();
/*     */   }
/*     */ 
/*     */   public int GetXSize()
/*     */   {
/*  38 */     return getXSize();
/*     */   }
/*     */   public int GetYSize() {
/*  41 */     return getYSize();
/*     */   }
/*     */   public int GetRasterDataType() {
/*  44 */     return getDataType();
/*     */   }
/*     */ 
/*     */   public int GetBlockXSize() {
/*  48 */     int[] anBlockXSize = new int[1];
/*  49 */     int[] anBlockYSize = new int[1];
/*  50 */     GetBlockSize(anBlockXSize, anBlockYSize);
/*  51 */     return anBlockXSize[0];
/*     */   }
/*     */ 
/*     */   public int GetBlockYSize()
/*     */   {
/*  56 */     int[] anBlockXSize = new int[1];
/*  57 */     int[] anBlockYSize = new int[1];
/*  58 */     GetBlockSize(anBlockXSize, anBlockYSize);
/*  59 */     return anBlockYSize[0];
/*     */   }
/*     */ 
/*     */   public int Checksum() {
/*  63 */     return Checksum(0, 0, getXSize(), getYSize());
/*     */   }
/*     */ 
/*     */   public int GetStatistics(boolean approx_ok, boolean force, double[] min, double[] max, double[] mean, double[] stddev) {
/*  67 */     return GetStatistics((approx_ok) ? 1 : 0, (force) ? 1 : 0, min, max, mean, stddev);
/*     */   }
/*     */ 
/*     */   public int ReadRaster_Direct(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, ByteBuffer nioBuffer)
/*     */   {
/*  72 */     return ReadRaster_Direct(xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, gdalconstConstants.GDT_Byte, nioBuffer);
/*     */   }
/*     */ 
/*     */   public int ReadRaster_Direct(int xoff, int yoff, int xsize, int ysize, ByteBuffer nioBuffer)
/*     */   {
/*  77 */     return ReadRaster_Direct(xoff, yoff, xsize, ysize, xsize, ysize, gdalconstConstants.GDT_Byte, nioBuffer);
/*     */   }
/*     */ 
/*     */   public ByteBuffer ReadRaster_Direct(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type)
/*     */   {
/*  83 */     long buf_size = buf_xsize * buf_ysize * (gdal.GetDataTypeSize(buf_type) / 8);
/*  84 */     if ((int)buf_size != buf_size)
/*  85 */       throw new OutOfMemoryError();
/*  86 */     ByteBuffer nioBuffer = ByteBuffer.allocateDirect((int)buf_size);
/*  87 */     int ret = ReadRaster_Direct(xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, nioBuffer);
/*  88 */     if (ret == gdalconstConstants.CE_None) {
/*  89 */       return nioBuffer;
/*     */     }
/*  91 */     return null;
/*     */   }
/*     */ 
/*     */   public ByteBuffer ReadRaster_Direct(int xoff, int yoff, int xsize, int ysize, int buf_type)
/*     */   {
/*  96 */     return ReadRaster_Direct(xoff, yoff, xsize, ysize, xsize, ysize, buf_type);
/*     */   }
/*     */ 
/*     */   public ByteBuffer ReadRaster_Direct(int xoff, int yoff, int xsize, int ysize)
/*     */   {
/* 101 */     return ReadRaster_Direct(xoff, yoff, xsize, ysize, xsize, ysize, gdalconstConstants.GDT_Byte);
/*     */   }
/*     */ 
/*     */   public int ReadRaster(int xoff, int yoff, int xsize, int ysize, int buf_type, byte[] array) {
/* 105 */     return ReadRaster(xoff, yoff, xsize, ysize, xsize, ysize, buf_type, array);
/*     */   }
/*     */ 
/*     */   public int ReadRaster(int xoff, int yoff, int xsize, int ysize, byte[] array) {
/* 109 */     return ReadRaster(xoff, yoff, xsize, ysize, xsize, ysize, gdalconstConstants.GDT_Byte, array);
/*     */   }
/*     */ 
/*     */   public int ReadRaster(int xoff, int yoff, int xsize, int ysize, int buf_type, short[] array) {
/* 113 */     return ReadRaster(xoff, yoff, xsize, ysize, xsize, ysize, buf_type, array);
/*     */   }
/*     */ 
/*     */   public int ReadRaster(int xoff, int yoff, int xsize, int ysize, short[] array) {
/* 117 */     return ReadRaster(xoff, yoff, xsize, ysize, xsize, ysize, gdalconstConstants.GDT_Int16, array);
/*     */   }
/*     */ 
/*     */   public int ReadRaster(int xoff, int yoff, int xsize, int ysize, int buf_type, int[] array) {
/* 121 */     return ReadRaster(xoff, yoff, xsize, ysize, xsize, ysize, buf_type, array);
/*     */   }
/*     */ 
/*     */   public int ReadRaster(int xoff, int yoff, int xsize, int ysize, int[] array) {
/* 125 */     return ReadRaster(xoff, yoff, xsize, ysize, xsize, ysize, gdalconstConstants.GDT_Int32, array);
/*     */   }
/*     */ 
/*     */   public int ReadRaster(int xoff, int yoff, int xsize, int ysize, int buf_type, float[] array) {
/* 129 */     return ReadRaster(xoff, yoff, xsize, ysize, xsize, ysize, buf_type, array);
/*     */   }
/*     */ 
/*     */   public int ReadRaster(int xoff, int yoff, int xsize, int ysize, float[] array) {
/* 133 */     return ReadRaster(xoff, yoff, xsize, ysize, xsize, ysize, gdalconstConstants.GDT_Float32, array);
/*     */   }
/*     */ 
/*     */   public int ReadRaster(int xoff, int yoff, int xsize, int ysize, int buf_type, double[] array) {
/* 137 */     return ReadRaster(xoff, yoff, xsize, ysize, xsize, ysize, buf_type, array);
/*     */   }
/*     */ 
/*     */   public int ReadRaster(int xoff, int yoff, int xsize, int ysize, double[] array) {
/* 141 */     return ReadRaster(xoff, yoff, xsize, ysize, xsize, ysize, gdalconstConstants.GDT_Float64, array);
/*     */   }
/*     */ 
/*     */   public int WriteRaster_Direct(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, ByteBuffer nioBuffer)
/*     */   {
/* 146 */     return WriteRaster_Direct(xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, gdalconstConstants.GDT_Byte, nioBuffer);
/*     */   }
/*     */ 
/*     */   public int WriteRaster_Direct(int xoff, int yoff, int xsize, int ysize, int buf_type, ByteBuffer nioBuffer)
/*     */   {
/* 151 */     return WriteRaster_Direct(xoff, yoff, xsize, ysize, xsize, ysize, buf_type, nioBuffer);
/*     */   }
/*     */ 
/*     */   public int WriteRaster_Direct(int xoff, int yoff, int xsize, int ysize, ByteBuffer nioBuffer)
/*     */   {
/* 156 */     return WriteRaster_Direct(xoff, yoff, xsize, ysize, xsize, ysize, gdalconstConstants.GDT_Byte, nioBuffer);
/*     */   }
/*     */ 
/*     */   public int WriteRaster(int xoff, int yoff, int xsize, int ysize, int buf_type, byte[] array) {
/* 160 */     return WriteRaster(xoff, yoff, xsize, ysize, xsize, ysize, buf_type, array);
/*     */   }
/*     */ 
/*     */   public int WriteRaster(int xoff, int yoff, int xsize, int ysize, byte[] array) {
/* 164 */     return WriteRaster(xoff, yoff, xsize, ysize, xsize, ysize, gdalconstConstants.GDT_Byte, array);
/*     */   }
/*     */ 
/*     */   public int WriteRaster(int xoff, int yoff, int xsize, int ysize, int buf_type, short[] array) {
/* 168 */     return WriteRaster(xoff, yoff, xsize, ysize, xsize, ysize, buf_type, array);
/*     */   }
/*     */ 
/*     */   public int WriteRaster(int xoff, int yoff, int xsize, int ysize, short[] array) {
/* 172 */     return WriteRaster(xoff, yoff, xsize, ysize, xsize, ysize, gdalconstConstants.GDT_Int16, array);
/*     */   }
/*     */ 
/*     */   public int WriteRaster(int xoff, int yoff, int xsize, int ysize, int buf_type, int[] array) {
/* 176 */     return WriteRaster(xoff, yoff, xsize, ysize, xsize, ysize, buf_type, array);
/*     */   }
/*     */ 
/*     */   public int WriteRaster(int xoff, int yoff, int xsize, int ysize, int[] array) {
/* 180 */     return WriteRaster(xoff, yoff, xsize, ysize, xsize, ysize, gdalconstConstants.GDT_Int32, array);
/*     */   }
/*     */ 
/*     */   public int WriteRaster(int xoff, int yoff, int xsize, int ysize, int buf_type, float[] array) {
/* 184 */     return WriteRaster(xoff, yoff, xsize, ysize, xsize, ysize, buf_type, array);
/*     */   }
/*     */ 
/*     */   public int WriteRaster(int xoff, int yoff, int xsize, int ysize, float[] array) {
/* 188 */     return WriteRaster(xoff, yoff, xsize, ysize, xsize, ysize, gdalconstConstants.GDT_Float32, array);
/*     */   }
/*     */ 
/*     */   public int WriteRaster(int xoff, int yoff, int xsize, int ysize, int buf_type, double[] array) {
/* 192 */     return WriteRaster(xoff, yoff, xsize, ysize, xsize, ysize, buf_type, array);
/*     */   }
/*     */ 
/*     */   public int WriteRaster(int xoff, int yoff, int xsize, int ysize, double[] array) {
/* 196 */     return WriteRaster(xoff, yoff, xsize, ysize, xsize, ysize, gdalconstConstants.GDT_Float64, array);
/*     */   }
/*     */ 
/*     */   public int getXSize() {
/* 200 */     return gdalJNI.Band_XSize_get(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public int getYSize() {
/* 204 */     return gdalJNI.Band_YSize_get(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public int getDataType() {
/* 208 */     return gdalJNI.Band_DataType_get(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public int GetBand() {
/* 212 */     return gdalJNI.Band_GetBand(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public void GetBlockSize(int[] pnBlockXSize, int[] pnBlockYSize) {
/* 216 */     gdalJNI.Band_GetBlockSize(this.swigCPtr, this, pnBlockXSize, pnBlockYSize);
/*     */   }
/*     */ 
/*     */   public int GetColorInterpretation() {
/* 220 */     return gdalJNI.Band_GetColorInterpretation(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public int GetRasterColorInterpretation() {
/* 224 */     return gdalJNI.Band_GetRasterColorInterpretation(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public int SetColorInterpretation(int val) {
/* 228 */     return gdalJNI.Band_SetColorInterpretation(this.swigCPtr, this, val);
/*     */   }
/*     */ 
/*     */   public int SetRasterColorInterpretation(int val) {
/* 232 */     return gdalJNI.Band_SetRasterColorInterpretation(this.swigCPtr, this, val);
/*     */   }
/*     */ 
/*     */   public void GetNoDataValue(Double[] val) {
/* 236 */     gdalJNI.Band_GetNoDataValue(this.swigCPtr, this, val);
/*     */   }
/*     */ 
/*     */   public int SetNoDataValue(double d) {
/* 240 */     return gdalJNI.Band_SetNoDataValue(this.swigCPtr, this, d);
/*     */   }
/*     */ 
/*     */   public String GetUnitType() {
/* 244 */     return gdalJNI.Band_GetUnitType(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public int SetUnitType(String val) {
/* 248 */     return gdalJNI.Band_SetUnitType(this.swigCPtr, this, val);
/*     */   }
/*     */ 
/*     */   public Vector GetRasterCategoryNames() {
/* 252 */     return gdalJNI.Band_GetRasterCategoryNames(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public int SetRasterCategoryNames(Vector names) {
/* 256 */     return gdalJNI.Band_SetRasterCategoryNames(this.swigCPtr, this, names);
/*     */   }
/*     */ 
/*     */   public void GetMinimum(Double[] val) {
/* 260 */     gdalJNI.Band_GetMinimum(this.swigCPtr, this, val);
/*     */   }
/*     */ 
/*     */   public void GetMaximum(Double[] val) {
/* 264 */     gdalJNI.Band_GetMaximum(this.swigCPtr, this, val);
/*     */   }
/*     */ 
/*     */   public void GetOffset(Double[] val) {
/* 268 */     gdalJNI.Band_GetOffset(this.swigCPtr, this, val);
/*     */   }
/*     */ 
/*     */   public void GetScale(Double[] val) {
/* 272 */     gdalJNI.Band_GetScale(this.swigCPtr, this, val);
/*     */   }
/*     */ 
/*     */   public int SetOffset(double val) {
/* 276 */     return gdalJNI.Band_SetOffset(this.swigCPtr, this, val);
/*     */   }
/*     */ 
/*     */   public int SetScale(double val) {
/* 280 */     return gdalJNI.Band_SetScale(this.swigCPtr, this, val);
/*     */   }
/*     */ 
/*     */   public int GetStatistics(int approx_ok, int force, double[] min, double[] max, double[] mean, double[] stddev) {
/* 284 */     return gdalJNI.Band_GetStatistics(this.swigCPtr, this, approx_ok, force, min, max, mean, stddev);
/*     */   }
/*     */ 
/*     */   public int ComputeStatistics(boolean approx_ok, double[] min, double[] max, double[] mean, double[] stddev, ProgressCallback callback) {
/* 288 */     return gdalJNI.Band_ComputeStatistics__SWIG_0(this.swigCPtr, this, approx_ok, min, max, mean, stddev, callback);
/*     */   }
/*     */ 
/*     */   public int ComputeStatistics(boolean approx_ok, double[] min, double[] max, double[] mean, double[] stddev) {
/* 292 */     return gdalJNI.Band_ComputeStatistics__SWIG_2(this.swigCPtr, this, approx_ok, min, max, mean, stddev);
/*     */   }
/*     */ 
/*     */   public int ComputeStatistics(boolean approx_ok, double[] min, double[] max, double[] mean) {
/* 296 */     return gdalJNI.Band_ComputeStatistics__SWIG_3(this.swigCPtr, this, approx_ok, min, max, mean);
/*     */   }
/*     */ 
/*     */   public int ComputeStatistics(boolean approx_ok, double[] min, double[] max) {
/* 300 */     return gdalJNI.Band_ComputeStatistics__SWIG_4(this.swigCPtr, this, approx_ok, min, max);
/*     */   }
/*     */ 
/*     */   public int ComputeStatistics(boolean approx_ok, double[] min) {
/* 304 */     return gdalJNI.Band_ComputeStatistics__SWIG_5(this.swigCPtr, this, approx_ok, min);
/*     */   }
/*     */ 
/*     */   public int ComputeStatistics(boolean approx_ok) {
/* 308 */     return gdalJNI.Band_ComputeStatistics__SWIG_6(this.swigCPtr, this, approx_ok);
/*     */   }
/*     */ 
/*     */   public int SetStatistics(double min, double max, double mean, double stddev) {
/* 312 */     return gdalJNI.Band_SetStatistics(this.swigCPtr, this, min, max, mean, stddev);
/*     */   }
/*     */ 
/*     */   public int GetOverviewCount() {
/* 316 */     return gdalJNI.Band_GetOverviewCount(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public Band GetOverview(int i) {
/* 320 */     long cPtr = gdalJNI.Band_GetOverview(this.swigCPtr, this, i);
/* 321 */     Band ret = null;
/* 322 */     if (cPtr != 0L) {
/* 323 */       ret = new Band(cPtr, false);
/* 324 */       ret.addReference(this);
/*     */     }
/* 326 */     return ret;
/*     */   }
/*     */ 
/*     */   public int Checksum(int xoff, int yoff, int xsize, int ysize) {
/* 330 */     return gdalJNI.Band_Checksum(this.swigCPtr, this, xoff, yoff, xsize, ysize);
/*     */   }
/*     */ 
/*     */   public void ComputeRasterMinMax(double[] argout, int approx_ok) {
/* 334 */     gdalJNI.Band_ComputeRasterMinMax__SWIG_0(this.swigCPtr, this, argout, approx_ok);
/*     */   }
/*     */ 
/*     */   public void ComputeRasterMinMax(double[] argout) {
/* 338 */     gdalJNI.Band_ComputeRasterMinMax__SWIG_1(this.swigCPtr, this, argout);
/*     */   }
/*     */ 
/*     */   public void ComputeBandStats(double[] argout, int samplestep) {
/* 342 */     gdalJNI.Band_ComputeBandStats__SWIG_0(this.swigCPtr, this, argout, samplestep);
/*     */   }
/*     */ 
/*     */   public void ComputeBandStats(double[] argout) {
/* 346 */     gdalJNI.Band_ComputeBandStats__SWIG_1(this.swigCPtr, this, argout);
/*     */   }
/*     */ 
/*     */   public int Fill(double real_fill, double imag_fill) {
/* 350 */     return gdalJNI.Band_Fill__SWIG_0(this.swigCPtr, this, real_fill, imag_fill);
/*     */   }
/*     */ 
/*     */   public int Fill(double real_fill) {
/* 354 */     return gdalJNI.Band_Fill__SWIG_1(this.swigCPtr, this, real_fill);
/*     */   }
/*     */ 
/*     */   public void FlushCache() {
/* 358 */     gdalJNI.Band_FlushCache(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public ColorTable GetRasterColorTable() {
/* 362 */     long cPtr = gdalJNI.Band_GetRasterColorTable(this.swigCPtr, this);
/* 363 */     ColorTable ret = null;
/* 364 */     if (cPtr != 0L) {
/* 365 */       ret = new ColorTable(cPtr, false);
/* 366 */       ret.addReference(this);
/*     */     }
/* 368 */     return ret;
/*     */   }
/*     */ 
/*     */   public ColorTable GetColorTable() {
/* 372 */     long cPtr = gdalJNI.Band_GetColorTable(this.swigCPtr, this);
/* 373 */     ColorTable ret = null;
/* 374 */     if (cPtr != 0L) {
/* 375 */       ret = new ColorTable(cPtr, false);
/* 376 */       ret.addReference(this);
/*     */     }
/* 378 */     return ret;
/*     */   }
/*     */ 
/*     */   public int SetRasterColorTable(ColorTable arg) {
/* 382 */     return gdalJNI.Band_SetRasterColorTable(this.swigCPtr, this, ColorTable.getCPtr(arg), arg);
/*     */   }
/*     */ 
/*     */   public int SetColorTable(ColorTable arg) {
/* 386 */     return gdalJNI.Band_SetColorTable(this.swigCPtr, this, ColorTable.getCPtr(arg), arg);
/*     */   }
/*     */ 
/*     */   public RasterAttributeTable GetDefaultRAT() {
/* 390 */     long cPtr = gdalJNI.Band_GetDefaultRAT(this.swigCPtr, this);
/* 391 */     return (cPtr == 0L) ? null : new RasterAttributeTable(cPtr, false);
/*     */   }
/*     */ 
/*     */   public int SetDefaultRAT(RasterAttributeTable table) {
/* 395 */     return gdalJNI.Band_SetDefaultRAT(this.swigCPtr, this, RasterAttributeTable.getCPtr(table), table);
/*     */   }
/*     */ 
/*     */   public Band GetMaskBand() {
/* 399 */     long cPtr = gdalJNI.Band_GetMaskBand(this.swigCPtr, this);
/* 400 */     Band ret = null;
/* 401 */     if (cPtr != 0L) {
/* 402 */       ret = new Band(cPtr, false);
/* 403 */       ret.addReference(this);
/*     */     }
/* 405 */     return ret;
/*     */   }
/*     */ 
/*     */   public int GetMaskFlags() {
/* 409 */     return gdalJNI.Band_GetMaskFlags(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public int CreateMaskBand(int nFlags) {
/* 413 */     return gdalJNI.Band_CreateMaskBand(this.swigCPtr, this, nFlags);
/*     */   }
/*     */ 
/*     */   public int SetDefaultHistogram(double min, double max, int[] buckets_in) {
/* 417 */     return gdalJNI.Band_SetDefaultHistogram(this.swigCPtr, this, min, max, buckets_in);
/*     */   }
/*     */ 
/*     */   public boolean HasArbitraryOverviews() {
/* 421 */     return gdalJNI.Band_HasArbitraryOverviews(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public Vector GetCategoryNames() {
/* 425 */     return gdalJNI.Band_GetCategoryNames(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public int SetCategoryNames(Vector papszCategoryNames) {
/* 429 */     return gdalJNI.Band_SetCategoryNames(this.swigCPtr, this, papszCategoryNames);
/*     */   }
/*     */ 
/*     */   public int ReadRaster_Direct(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, ByteBuffer nioBuffer, int nPixelSpace, int nLineSpace) {
/* 433 */     return gdalJNI.Band_ReadRaster_Direct__SWIG_0(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, nioBuffer, nPixelSpace, nLineSpace);
/*     */   }
/*     */ 
/*     */   public int ReadRaster_Direct(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, ByteBuffer nioBuffer, int nPixelSpace) {
/* 437 */     return gdalJNI.Band_ReadRaster_Direct__SWIG_1(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, nioBuffer, nPixelSpace);
/*     */   }
/*     */ 
/*     */   public int ReadRaster_Direct(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, ByteBuffer nioBuffer) {
/* 441 */     return gdalJNI.Band_ReadRaster_Direct__SWIG_2(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, nioBuffer);
/*     */   }
/*     */ 
/*     */   public int ReadRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, byte[] regularArrayOut, int nPixelSpace, int nLineSpace) {
/* 445 */     return gdalJNI.Band_ReadRaster__SWIG_0(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayOut, nPixelSpace, nLineSpace);
/*     */   }
/*     */ 
/*     */   public int ReadRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, byte[] regularArrayOut, int nPixelSpace) {
/* 449 */     return gdalJNI.Band_ReadRaster__SWIG_1(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayOut, nPixelSpace);
/*     */   }
/*     */ 
/*     */   public int ReadRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, byte[] regularArrayOut) {
/* 453 */     return gdalJNI.Band_ReadRaster__SWIG_2(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayOut);
/*     */   }
/*     */ 
/*     */   public int ReadRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, short[] regularArrayOut, int nPixelSpace, int nLineSpace) {
/* 457 */     return gdalJNI.Band_ReadRaster__SWIG_3(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayOut, nPixelSpace, nLineSpace);
/*     */   }
/*     */ 
/*     */   public int ReadRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, short[] regularArrayOut, int nPixelSpace) {
/* 461 */     return gdalJNI.Band_ReadRaster__SWIG_4(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayOut, nPixelSpace);
/*     */   }
/*     */ 
/*     */   public int ReadRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, short[] regularArrayOut) {
/* 465 */     return gdalJNI.Band_ReadRaster__SWIG_5(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayOut);
/*     */   }
/*     */ 
/*     */   public int ReadRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, int[] regularArrayOut, int nPixelSpace, int nLineSpace) {
/* 469 */     return gdalJNI.Band_ReadRaster__SWIG_6(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayOut, nPixelSpace, nLineSpace);
/*     */   }
/*     */ 
/*     */   public int ReadRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, int[] regularArrayOut, int nPixelSpace) {
/* 473 */     return gdalJNI.Band_ReadRaster__SWIG_7(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayOut, nPixelSpace);
/*     */   }
/*     */ 
/*     */   public int ReadRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, int[] regularArrayOut) {
/* 477 */     return gdalJNI.Band_ReadRaster__SWIG_8(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayOut);
/*     */   }
/*     */ 
/*     */   public int ReadRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, float[] regularArrayOut, int nPixelSpace, int nLineSpace) {
/* 481 */     return gdalJNI.Band_ReadRaster__SWIG_9(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayOut, nPixelSpace, nLineSpace);
/*     */   }
/*     */ 
/*     */   public int ReadRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, float[] regularArrayOut, int nPixelSpace) {
/* 485 */     return gdalJNI.Band_ReadRaster__SWIG_10(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayOut, nPixelSpace);
/*     */   }
/*     */ 
/*     */   public int ReadRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, float[] regularArrayOut) {
/* 489 */     return gdalJNI.Band_ReadRaster__SWIG_11(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayOut);
/*     */   }
/*     */ 
/*     */   public int ReadRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, double[] regularArrayOut, int nPixelSpace, int nLineSpace) {
/* 493 */     return gdalJNI.Band_ReadRaster__SWIG_12(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayOut, nPixelSpace, nLineSpace);
/*     */   }
/*     */ 
/*     */   public int ReadRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, double[] regularArrayOut, int nPixelSpace) {
/* 497 */     return gdalJNI.Band_ReadRaster__SWIG_13(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayOut, nPixelSpace);
/*     */   }
/*     */ 
/*     */   public int ReadRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, double[] regularArrayOut) {
/* 501 */     return gdalJNI.Band_ReadRaster__SWIG_14(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayOut);
/*     */   }
/*     */ 
/*     */   public int WriteRaster_Direct(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, ByteBuffer nioBuffer, int nPixelSpace, int nLineSpace) {
/* 505 */     return gdalJNI.Band_WriteRaster_Direct__SWIG_0(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, nioBuffer, nPixelSpace, nLineSpace);
/*     */   }
/*     */ 
/*     */   public int WriteRaster_Direct(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, ByteBuffer nioBuffer, int nPixelSpace) {
/* 509 */     return gdalJNI.Band_WriteRaster_Direct__SWIG_1(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, nioBuffer, nPixelSpace);
/*     */   }
/*     */ 
/*     */   public int WriteRaster_Direct(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, ByteBuffer nioBuffer) {
/* 513 */     return gdalJNI.Band_WriteRaster_Direct__SWIG_2(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, nioBuffer);
/*     */   }
/*     */ 
/*     */   public int WriteRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, byte[] regularArrayIn, int nPixelSpace, int nLineSpace) {
/* 517 */     return gdalJNI.Band_WriteRaster__SWIG_0(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayIn, nPixelSpace, nLineSpace);
/*     */   }
/*     */ 
/*     */   public int WriteRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, byte[] regularArrayIn, int nPixelSpace) {
/* 521 */     return gdalJNI.Band_WriteRaster__SWIG_1(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayIn, nPixelSpace);
/*     */   }
/*     */ 
/*     */   public int WriteRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, byte[] regularArrayIn) {
/* 525 */     return gdalJNI.Band_WriteRaster__SWIG_2(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayIn);
/*     */   }
/*     */ 
/*     */   public int WriteRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, short[] regularArrayIn, int nPixelSpace, int nLineSpace) {
/* 529 */     return gdalJNI.Band_WriteRaster__SWIG_3(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayIn, nPixelSpace, nLineSpace);
/*     */   }
/*     */ 
/*     */   public int WriteRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, short[] regularArrayIn, int nPixelSpace) {
/* 533 */     return gdalJNI.Band_WriteRaster__SWIG_4(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayIn, nPixelSpace);
/*     */   }
/*     */ 
/*     */   public int WriteRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, short[] regularArrayIn) {
/* 537 */     return gdalJNI.Band_WriteRaster__SWIG_5(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayIn);
/*     */   }
/*     */ 
/*     */   public int WriteRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, int[] regularArrayIn, int nPixelSpace, int nLineSpace) {
/* 541 */     return gdalJNI.Band_WriteRaster__SWIG_6(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayIn, nPixelSpace, nLineSpace);
/*     */   }
/*     */ 
/*     */   public int WriteRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, int[] regularArrayIn, int nPixelSpace) {
/* 545 */     return gdalJNI.Band_WriteRaster__SWIG_7(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayIn, nPixelSpace);
/*     */   }
/*     */ 
/*     */   public int WriteRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, int[] regularArrayIn) {
/* 549 */     return gdalJNI.Band_WriteRaster__SWIG_8(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayIn);
/*     */   }
/*     */ 
/*     */   public int WriteRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, float[] regularArrayIn, int nPixelSpace, int nLineSpace) {
/* 553 */     return gdalJNI.Band_WriteRaster__SWIG_9(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayIn, nPixelSpace, nLineSpace);
/*     */   }
/*     */ 
/*     */   public int WriteRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, float[] regularArrayIn, int nPixelSpace) {
/* 557 */     return gdalJNI.Band_WriteRaster__SWIG_10(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayIn, nPixelSpace);
/*     */   }
/*     */ 
/*     */   public int WriteRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, float[] regularArrayIn) {
/* 561 */     return gdalJNI.Band_WriteRaster__SWIG_11(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayIn);
/*     */   }
/*     */ 
/*     */   public int WriteRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, double[] regularArrayIn, int nPixelSpace, int nLineSpace) {
/* 565 */     return gdalJNI.Band_WriteRaster__SWIG_12(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayIn, nPixelSpace, nLineSpace);
/*     */   }
/*     */ 
/*     */   public int WriteRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, double[] regularArrayIn, int nPixelSpace) {
/* 569 */     return gdalJNI.Band_WriteRaster__SWIG_13(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayIn, nPixelSpace);
/*     */   }
/*     */ 
/*     */   public int WriteRaster(int xoff, int yoff, int xsize, int ysize, int buf_xsize, int buf_ysize, int buf_type, double[] regularArrayIn) {
/* 573 */     return gdalJNI.Band_WriteRaster__SWIG_14(this.swigCPtr, this, xoff, yoff, xsize, ysize, buf_xsize, buf_ysize, buf_type, regularArrayIn);
/*     */   }
/*     */ 
/*     */   public int ReadBlock_Direct(int nXBlockOff, int nYBlockOff, ByteBuffer nioBuffer) {
/* 577 */     return gdalJNI.Band_ReadBlock_Direct(this.swigCPtr, this, nXBlockOff, nYBlockOff, nioBuffer);
/*     */   }
/*     */ 
/*     */   public int WriteBlock_Direct(int nXBlockOff, int nYBlockOff, ByteBuffer nioBuffer) {
/* 581 */     return gdalJNI.Band_WriteBlock_Direct(this.swigCPtr, this, nXBlockOff, nYBlockOff, nioBuffer);
/*     */   }
/*     */ 
/*     */   public int GetHistogram(double min, double max, int[] buckets, boolean include_out_of_range, boolean approx_ok, ProgressCallback callback) {
/* 585 */     return gdalJNI.Band_GetHistogram__SWIG_0(this.swigCPtr, this, min, max, buckets, include_out_of_range, approx_ok, callback);
/*     */   }
/*     */ 
/*     */   public int GetHistogram(double min, double max, int[] buckets, boolean include_out_of_range, boolean approx_ok) {
/* 589 */     return gdalJNI.Band_GetHistogram__SWIG_1(this.swigCPtr, this, min, max, buckets, include_out_of_range, approx_ok);
/*     */   }
/*     */ 
/*     */   public int GetHistogram(double min, double max, int[] buckets) {
/* 593 */     return gdalJNI.Band_GetHistogram__SWIG_2(this.swigCPtr, this, min, max, buckets);
/*     */   }
/*     */ 
/*     */   public int GetHistogram(int[] buckets) {
/* 597 */     return gdalJNI.Band_GetHistogram__SWIG_3(this.swigCPtr, this, buckets);
/*     */   }
/*     */ 
/*     */   public int GetDefaultHistogram(double[] min_ret, double[] max_ret, int[][] buckets_ret, boolean force, ProgressCallback callback) {
/* 601 */     return gdalJNI.Band_GetDefaultHistogram__SWIG_0(this.swigCPtr, this, min_ret, max_ret, buckets_ret, force, callback);
/*     */   }
/*     */ 
/*     */   public int GetDefaultHistogram(double[] min_ret, double[] max_ret, int[][] buckets_ret, boolean force) {
/* 605 */     return gdalJNI.Band_GetDefaultHistogram__SWIG_2(this.swigCPtr, this, min_ret, max_ret, buckets_ret, force);
/*     */   }
/*     */ 
/*     */   public int GetDefaultHistogram(double[] min_ret, double[] max_ret, int[][] buckets_ret) {
/* 609 */     return gdalJNI.Band_GetDefaultHistogram__SWIG_3(this.swigCPtr, this, min_ret, max_ret, buckets_ret);
/*     */   }
/*     */ }

/* Location:           E:\temp\1117\gdal.jar
 * Qualified Name:     org.gdal.gdal.Band
 * JD-Core Version:    0.5.4
 */