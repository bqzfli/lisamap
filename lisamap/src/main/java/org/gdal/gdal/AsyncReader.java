/*    */ package org.gdal.gdal;
/*    */ 
/*    */ public class AsyncReader
/*    */ {
/*    */   private long swigCPtr;
/*    */   protected boolean swigCMemOwn;
/*    */   private Object parentReference;
/*    */ 
/*    */   protected AsyncReader(long cPtr, boolean cMemoryOwn)
/*    */   {
/* 16 */     if (cPtr == 0L)
/* 17 */       throw new RuntimeException();
/* 18 */     this.swigCMemOwn = cMemoryOwn;
/* 19 */     this.swigCPtr = cPtr;
/*    */   }
/*    */ 
/*    */   protected static long getCPtr(AsyncReader obj) {
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
/* 34 */         gdalJNI.delete_AsyncReader(this.swigCPtr);
/*    */       }
/* 36 */       this.swigCPtr = 0L;
/*    */     }
/*    */   }
/*    */ 
/*    */   protected static long getCPtrAndDisown(AsyncReader obj)
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
/* 58 */     if (obj instanceof AsyncReader)
/* 59 */       equal = ((AsyncReader)obj).swigCPtr == this.swigCPtr;
/* 60 */     return equal;
/*    */   }
/*    */ 
/*    */   public int hashCode() {
/* 64 */     return (int)this.swigCPtr;
/*    */   }
/*    */ 
/*    */   public int GetNextUpdatedRegion(double timeout, int[] xoff, int[] yoff, int[] buf_xsize, int[] buf_ysize)
/*    */   {
/* 69 */     return gdalJNI.AsyncReader_GetNextUpdatedRegion(this.swigCPtr, this, timeout, xoff, yoff, buf_xsize, buf_ysize);
/*    */   }
/*    */ 
/*    */   public int LockBuffer(double timeout) {
/* 73 */     return gdalJNI.AsyncReader_LockBuffer(this.swigCPtr, this, timeout);
/*    */   }
/*    */ 
/*    */   public void UnlockBuffer() {
/* 77 */     gdalJNI.AsyncReader_UnlockBuffer(this.swigCPtr, this);
/*    */   }
/*    */ }

/* Location:           E:\temp\1117\gdal.jar
 * Qualified Name:     org.gdal.gdal.AsyncReader
 * JD-Core Version:    0.5.4
 */