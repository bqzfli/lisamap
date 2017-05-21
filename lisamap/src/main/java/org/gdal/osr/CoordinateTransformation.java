/*    */ package org.gdal.osr;
/*    */ 
/*    */ public class CoordinateTransformation
/*    */ {
/*    */   private long swigCPtr;
/*    */   protected boolean swigCMemOwn;
/*    */ 
/*    */   public CoordinateTransformation(long cPtr, boolean cMemoryOwn)
/*    */   {
/* 16 */     this.swigCMemOwn = cMemoryOwn;
/* 17 */     this.swigCPtr = cPtr;
/*    */   }
/*    */ 
/*    */   public static long getCPtr(CoordinateTransformation obj) {
/* 21 */     return (obj == null) ? 0L : obj.swigCPtr;
/*    */   }
/*    */ 
/*    */   protected void finalize() {
/* 25 */     delete();
/*    */   }
/*    */ 
/*    */   public synchronized void delete() {
/* 29 */     if (this.swigCPtr != 0L) {
/* 30 */       if (this.swigCMemOwn) {
/* 31 */         this.swigCMemOwn = false;
/* 32 */         osrJNI.delete_CoordinateTransformation(this.swigCPtr);
/*    */       }
/* 34 */       this.swigCPtr = 0L;
/*    */     }
/*    */   }
/*    */ 
/*    */   public double[] TransformPoint(double x, double y, double z) {
/* 39 */     double[] ret = new double[3];
/* 40 */     TransformPoint(ret, x, y, z);
/* 41 */     return ret;
/*    */   }
/*    */ 
/*    */   public double[] TransformPoint(double x, double y) {
/* 45 */     return TransformPoint(x, y, 0.0D);
/*    */   }
/*    */ 
/*    */   public CoordinateTransformation(SpatialReference src, SpatialReference dst) {
/* 49 */     this(osrJNI.new_CoordinateTransformation(SpatialReference.getCPtr(src), src, SpatialReference.getCPtr(dst), dst), true);
/*    */   }
/*    */ 
/*    */   public void TransformPoint(double[] inout) {
/* 53 */     osrJNI.CoordinateTransformation_TransformPoint__SWIG_0(this.swigCPtr, this, inout);
/*    */   }
/*    */ 
/*    */   public void TransformPoint(double[] argout, double x, double y, double z) {
/* 57 */     osrJNI.CoordinateTransformation_TransformPoint__SWIG_1(this.swigCPtr, this, argout, x, y, z);
/*    */   }
/*    */ 
/*    */   public void TransformPoint(double[] argout, double x, double y) {
/* 61 */     osrJNI.CoordinateTransformation_TransformPoint__SWIG_2(this.swigCPtr, this, argout, x, y);
/*    */   }
/*    */ 
/*    */   public void TransformPoints(double[][] nCount) {
/* 65 */     osrJNI.CoordinateTransformation_TransformPoints(this.swigCPtr, this, nCount);
/*    */   }
/*    */ }

/* Location:           E:\temp\1117\gdal.jar
 * Qualified Name:     org.gdal.osr.CoordinateTransformation
 * JD-Core Version:    0.5.4
 */