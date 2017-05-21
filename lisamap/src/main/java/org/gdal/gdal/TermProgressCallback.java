/*    */ package org.gdal.gdal;
/*    */ 
/*    */ public class TermProgressCallback extends ProgressCallback
/*    */ {
/*    */   private long swigCPtr;
/*    */ 
/*    */   public TermProgressCallback(long cPtr, boolean cMemoryOwn)
/*    */   {
/* 15 */     super(gdalJNI.TermProgressCallback_SWIGUpcast(cPtr), cMemoryOwn);
/* 16 */     this.swigCPtr = cPtr;
/*    */   }
/*    */ 
/*    */   public static long getCPtr(TermProgressCallback obj) {
/* 20 */     return (obj == null) ? 0L : obj.swigCPtr;
/*    */   }
/*    */ 
/*    */   protected void finalize() {
/* 24 */     delete();
/*    */   }
/*    */ 
/*    */   public synchronized void delete() {
/* 28 */     if (this.swigCPtr != 0L) {
/* 29 */       if (this.swigCMemOwn) {
/* 30 */         this.swigCMemOwn = false;
/* 31 */         gdalJNI.delete_TermProgressCallback(this.swigCPtr);
/*    */       }
/* 33 */       this.swigCPtr = 0L;
/*    */     }
/* 35 */     super.delete();
/*    */   }
/*    */ 
/*    */   public TermProgressCallback() {
/* 39 */     this(gdalJNI.new_TermProgressCallback(), true);
/*    */   }
/*    */ 
/*    */   public int run(double dfComplete, String pszMessage) {
/* 43 */     return gdalJNI.TermProgressCallback_run(this.swigCPtr, this, dfComplete, pszMessage);
/*    */   }
/*    */ }

/* Location:           E:\temp\1117\gdal.jar
 * Qualified Name:     org.gdal.gdal.TermProgressCallback
 * JD-Core Version:    0.5.4
 */