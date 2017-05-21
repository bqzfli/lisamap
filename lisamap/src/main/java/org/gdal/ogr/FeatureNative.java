/*    */ package org.gdal.ogr;
/*    */ 
/*    */ import java.lang.ref.ReferenceQueue;
/*    */ import java.lang.ref.WeakReference;
/*    */ import java.util.Collections;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ 
/*    */ class FeatureNative extends WeakReference
/*    */ {
/*    */   private long swigCPtr;
/* 26 */   private static ReferenceQueue refQueue = new ReferenceQueue();
/* 27 */   private static Set refList = Collections.synchronizedSet(new HashSet());
/* 28 */   private static Thread cleanupThread = null;
/*    */ 
/*    */   public FeatureNative(Feature javaObject, long cPtr)
/*    */   {
/* 64 */     super(javaObject, refQueue);
/*    */ 
/* 66 */     if (cleanupThread == null)
/*    */     {
/*    */       while (true)
/*    */       {
/* 72 */         FeatureNative nativeObject = (FeatureNative)refQueue.poll();
/*    */ 
/* 74 */         if (nativeObject == null) break;
/* 75 */         nativeObject.delete();
/*    */       }
/*    */ 
/*    */     }
/*    */ 
/* 81 */     refList.add(this);
/*    */ 
/* 83 */     this.swigCPtr = cPtr;
/*    */   }
/*    */ 
/*    */   public void dontDisposeNativeResources()
/*    */   {
/* 88 */     refList.remove(this);
/* 89 */     this.swigCPtr = 0L;
/*    */   }
/*    */ 
/*    */   public void delete()
/*    */   {
/* 94 */     refList.remove(this);
/* 95 */     if (this.swigCPtr != 0L) {
/* 96 */       ogrJNI.delete_Feature(this.swigCPtr);
/*    */     }
/* 98 */     this.swigCPtr = 0L;
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/* 34 */     cleanupThread = new Thread()
/*    */     {
/*    */       public void run()
/*    */       {
/*    */         try
/*    */         {
/* 41 */           FeatureNative nativeObject = (FeatureNative)FeatureNative.refQueue.remove();
/*    */ 
/* 43 */           if (nativeObject != null)
/* 44 */             nativeObject.delete();
/*    */         }
/*    */         catch (InterruptedException ie)
/*    */         {
/*    */         }
/*    */       }
/*    */     };
/*    */     try {
/* 52 */       cleanupThread.setName("FeatureNativeObjectsCleaner");
/* 53 */       cleanupThread.setDaemon(true);
/* 54 */       cleanupThread.start();
/*    */     }
/*    */     catch (SecurityException se)
/*    */     {
/* 59 */       cleanupThread = null;
/*    */     }
/*    */   }
/*    */ }

/* Location:           E:\temp\1117\gdal.jar
 * Qualified Name:     org.gdal.ogr.FeatureNative
 * JD-Core Version:    0.5.4
 */