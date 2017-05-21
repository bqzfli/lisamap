/*    */ package org.gdal.gdal;
/*    */ 
/*    */ import java.util.Vector;
/*    */ 
/*    */ public class Transformer
/*    */ {
/*    */   private long swigCPtr;
/*    */   protected boolean swigCMemOwn;
/*    */   private Object parentReference;
/*    */ 
/*    */   protected Transformer(long cPtr, boolean cMemoryOwn)
/*    */   {
/* 16 */     if (cPtr == 0L)
/* 17 */       throw new RuntimeException();
/* 18 */     this.swigCMemOwn = cMemoryOwn;
/* 19 */     this.swigCPtr = cPtr;
/*    */   }
/*    */ 
/*    */   protected static long getCPtr(Transformer obj) {
/* 23 */     return (obj == null) ? 0L : obj.swigCPtr;
/*    */   }
/*    */ 
/*    */   protected void finalize() {
/* 27 */     delete();
/*    */   }
/*    */ 
/*    */   public synchronized void delete() {
/* 31 */     if (this.swigCPtr != 0L) {
/* 32 */       if (this.swigCMemOwn) {
/* 33 */         this.swigCMemOwn = false;
/* 34 */         gdalJNI.delete_Transformer(this.swigCPtr);
/*    */       }
/* 36 */       this.swigCPtr = 0L;
/*    */     }
/*    */   }
/*    */ 
/*    */   protected static long getCPtrAndDisown(Transformer obj)
/*    */   {
/* 43 */     if (obj != null)
/*    */     {
/* 45 */       obj.swigCMemOwn = false;
/* 46 */       obj.parentReference = null;
/*    */     }
/* 48 */     return getCPtr(obj);
/*    */   }
/*    */ 
/*    */   protected void addReference(Object reference)
/*    */   {
/* 53 */     this.parentReference = reference;
/*    */   }
/*    */ 
/*    */   public boolean equals(Object obj) {
/* 57 */     boolean equal = false;
/* 58 */     if (obj instanceof Transformer)
/* 59 */       equal = ((Transformer)obj).swigCPtr == this.swigCPtr;
/* 60 */     return equal;
/*    */   }
/*    */ 
/*    */   public int hashCode() {
/* 64 */     return (int)this.swigCPtr;
/*    */   }
/*    */ 
/*    */   public Transformer(Dataset src, Dataset dst, Vector options)
/*    */   {
/* 69 */     this(gdalJNI.new_Transformer(Dataset.getCPtr(src), src, Dataset.getCPtr(dst), dst, options), true);
/*    */   }
/*    */ 
/*    */   public int TransformPoint(int bDstToSrc, double[] inout) {
/* 73 */     return gdalJNI.Transformer_TransformPoint__SWIG_0(this.swigCPtr, this, bDstToSrc, inout);
/*    */   }
/*    */ 
/*    */   public int TransformPoint(double[] argout, int bDstToSrc, double x, double y, double z) {
/* 77 */     return gdalJNI.Transformer_TransformPoint__SWIG_1(this.swigCPtr, this, argout, bDstToSrc, x, y, z);
/*    */   }
/*    */ 
/*    */   public int TransformPoint(double[] argout, int bDstToSrc, double x, double y) {
/* 81 */     return gdalJNI.Transformer_TransformPoint__SWIG_2(this.swigCPtr, this, argout, bDstToSrc, x, y);
/*    */   }
/*    */ 
/*    */   public int TransformPoints(int bDstToSrc, double[][] nCount, int[] panSuccess) {
/* 85 */     return gdalJNI.Transformer_TransformPoints(this.swigCPtr, this, bDstToSrc, nCount, panSuccess);
/*    */   }
/*    */ }

/* Location:           E:\temp\1117\gdal.jar
 * Qualified Name:     org.gdal.gdal.Transformer
 * JD-Core Version:    0.5.4
 */