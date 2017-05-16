/*    */ package org.gdal.gdal;
/*    */ 
/*    */ public class ProgressCallback
/*    */ {
/*    */   private long swigCPtr;
/*    */   protected boolean swigCMemOwn;
/*    */ 
/*    */   public ProgressCallback(long cPtr, boolean cMemoryOwn)
/*    */   {
/* 16 */     this.swigCMemOwn = cMemoryOwn;
/* 17 */     this.swigCPtr = cPtr;
/*    */   }
/*    */ 
/*    */   public static long getCPtr(ProgressCallback obj) {
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
/* 32 */         gdalJNI.delete_ProgressCallback(this.swigCPtr);
/*    */       }
/* 34 */       this.swigCPtr = 0L;
/*    */     }
/*    */   }
/*    */ 
/*    */   public int run(double dfComplete, String pszMessage) {
/* 39 */     return gdalJNI.ProgressCallback_run(this.swigCPtr, this, dfComplete, pszMessage);
/*    */   }
/*    */ 
/*    */   public ProgressCallback() {
/* 43 */     this(gdalJNI.new_ProgressCallback(), true);
/*    */   }
/*    */ }

/* Location:           E:\temp\1117\gdal.jar
 * Qualified Name:     org.gdal.gdal.ProgressCallback
 * JD-Core Version:    0.5.4
 */