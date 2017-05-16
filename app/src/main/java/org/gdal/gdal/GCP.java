/*     */ package org.gdal.gdal;
/*     */ 
/*     */ public class GCP
/*     */ {
/*     */   private long swigCPtr;
/*     */   protected boolean swigCMemOwn;
/*     */ 
/*     */   protected GCP(long cPtr, boolean cMemoryOwn)
/*     */   {
/*  16 */     if (cPtr == 0L)
/*  17 */       throw new RuntimeException();
/*  18 */     this.swigCMemOwn = cMemoryOwn;
/*  19 */     this.swigCPtr = cPtr;
/*     */   }
/*     */ 
/*     */   protected static long getCPtr(GCP obj) {
/*  23 */     return (obj == null) ? 0L : obj.swigCPtr;
/*     */   }
/*     */ 
/*     */   protected void finalize() {
/*  27 */     delete();
/*     */   }
/*     */ 
/*     */   public synchronized void delete() {
/*  31 */     if (this.swigCPtr != 0L) {
/*  32 */       if (this.swigCMemOwn) {
/*  33 */         this.swigCMemOwn = false;
/*  34 */         gdalJNI.delete_GCP(this.swigCPtr);
/*     */       }
/*  36 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */ 
/*     */   public GCP(double x, double y, double z, double pixel, double line)
/*     */   {
/*  42 */     this(x, y, z, pixel, line, "", "");
/*     */   }
/*     */ 
/*     */   public GCP(double x, double y, double pixel, double line, String info, String id)
/*     */   {
/*  47 */     this(x, y, 0.0D, pixel, line, info, id);
/*     */   }
/*     */ 
/*     */   public GCP(double x, double y, double pixel, double line)
/*     */   {
/*  52 */     this(x, y, 0.0D, pixel, line, "", "");
/*     */   }
/*     */ 
/*     */   public void setGCPX(double value) {
/*  56 */     gdalJNI.GCP_GCPX_set(this.swigCPtr, this, value);
/*     */   }
/*     */ 
/*     */   public double getGCPX() {
/*  60 */     return gdalJNI.GCP_GCPX_get(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public void setGCPY(double value) {
/*  64 */     gdalJNI.GCP_GCPY_set(this.swigCPtr, this, value);
/*     */   }
/*     */ 
/*     */   public double getGCPY() {
/*  68 */     return gdalJNI.GCP_GCPY_get(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public void setGCPZ(double value) {
/*  72 */     gdalJNI.GCP_GCPZ_set(this.swigCPtr, this, value);
/*     */   }
/*     */ 
/*     */   public double getGCPZ() {
/*  76 */     return gdalJNI.GCP_GCPZ_get(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public void setGCPPixel(double value) {
/*  80 */     gdalJNI.GCP_GCPPixel_set(this.swigCPtr, this, value);
/*     */   }
/*     */ 
/*     */   public double getGCPPixel() {
/*  84 */     return gdalJNI.GCP_GCPPixel_get(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public void setGCPLine(double value) {
/*  88 */     gdalJNI.GCP_GCPLine_set(this.swigCPtr, this, value);
/*     */   }
/*     */ 
/*     */   public double getGCPLine() {
/*  92 */     return gdalJNI.GCP_GCPLine_get(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public void setInfo(String value) {
/*  96 */     gdalJNI.GCP_Info_set(this.swigCPtr, this, value);
/*     */   }
/*     */ 
/*     */   public String getInfo() {
/* 100 */     return gdalJNI.GCP_Info_get(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public void setId(String value) {
/* 104 */     gdalJNI.GCP_Id_set(this.swigCPtr, this, value);
/*     */   }
/*     */ 
/*     */   public String getId() {
/* 108 */     return gdalJNI.GCP_Id_get(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public GCP(double x, double y, double z, double pixel, double line, String info, String id) {
/* 112 */     this(gdalJNI.new_GCP(x, y, z, pixel, line, info, id), true);
/*     */   }
/*     */ }

/* Location:           E:\temp\1117\gdal.jar
 * Qualified Name:     org.gdal.gdal.GCP
 * JD-Core Version:    0.5.4
 */