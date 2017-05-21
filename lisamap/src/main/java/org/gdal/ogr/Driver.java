/*     */ package org.gdal.ogr;
/*     */ 
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class Driver
/*     */ {
/*     */   private long swigCPtr;
/*     */   protected boolean swigCMemOwn;
/*     */   private Object parentReference;
/*     */ 
/*     */   protected Driver(long cPtr, boolean cMemoryOwn)
/*     */   {
/*  16 */     if (cPtr == 0L)
/*  17 */       throw new RuntimeException();
/*  18 */     this.swigCMemOwn = cMemoryOwn;
/*  19 */     this.swigCPtr = cPtr;
/*     */   }
/*     */ 
/*     */   protected static long getCPtr(Driver obj) {
/*  23 */     return (obj == null) ? 0L : obj.swigCPtr;
/*     */   }
/*     */ 
/*     */   public synchronized void delete() {
/*  27 */     if (this.swigCPtr != 0L) {
/*  28 */       if (this.swigCMemOwn) {
/*  29 */         this.swigCMemOwn = false;
/*  30 */         throw new UnsupportedOperationException("C++ destructor does not have public access");
/*     */       }
/*  32 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static long getCPtrAndDisown(Driver obj)
/*     */   {
/*  39 */     if (obj != null)
/*     */     {
/*  41 */       obj.swigCMemOwn = false;
/*  42 */       obj.parentReference = null;
/*     */     }
/*  44 */     return getCPtr(obj);
/*     */   }
/*     */ 
/*     */   protected void addReference(Object reference)
/*     */   {
/*  49 */     this.parentReference = reference;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object obj) {
/*  53 */     boolean equal = false;
/*  54 */     if (obj instanceof Driver)
/*  55 */       equal = ((Driver)obj).swigCPtr == this.swigCPtr;
/*  56 */     return equal;
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/*  60 */     return (int)this.swigCPtr;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/*  65 */     return ogrJNI.Driver_name_get(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public DataSource CreateDataSource(String utf8_path, Vector options) {
/*  69 */     long cPtr = ogrJNI.Driver_CreateDataSource__SWIG_0(this.swigCPtr, this, utf8_path, options);
/*  70 */     return (cPtr == 0L) ? null : new DataSource(cPtr, true);
/*     */   }
/*     */ 
/*     */   public DataSource CreateDataSource(String utf8_path) {
/*  74 */     long cPtr = ogrJNI.Driver_CreateDataSource__SWIG_1(this.swigCPtr, this, utf8_path);
/*  75 */     return (cPtr == 0L) ? null : new DataSource(cPtr, true);
/*     */   }
/*     */ 
/*     */   public DataSource CopyDataSource(DataSource copy_ds, String utf8_path, Vector options) {
/*  79 */     long cPtr = ogrJNI.Driver_CopyDataSource__SWIG_0(this.swigCPtr, this, DataSource.getCPtr(copy_ds), copy_ds, utf8_path, options);
/*  80 */     return (cPtr == 0L) ? null : new DataSource(cPtr, true);
/*     */   }
/*     */ 
/*     */   public DataSource CopyDataSource(DataSource copy_ds, String utf8_path) {
/*  84 */     long cPtr = ogrJNI.Driver_CopyDataSource__SWIG_1(this.swigCPtr, this, DataSource.getCPtr(copy_ds), copy_ds, utf8_path);
/*  85 */     return (cPtr == 0L) ? null : new DataSource(cPtr, true);
/*     */   }
/*     */ 
/*     */   public DataSource Open(String utf8_path, int update) {
/*  89 */     long cPtr = ogrJNI.Driver_Open__SWIG_0(this.swigCPtr, this, utf8_path, update);
/*  90 */     return (cPtr == 0L) ? null : new DataSource(cPtr, true);
/*     */   }
/*     */ 
/*     */   public DataSource Open(String utf8_path) {
/*  94 */     long cPtr = ogrJNI.Driver_Open__SWIG_1(this.swigCPtr, this, utf8_path);
/*  95 */     return (cPtr == 0L) ? null : new DataSource(cPtr, true);
/*     */   }
/*     */ 
/*     */   public int DeleteDataSource(String utf8_path) {
/*  99 */     return ogrJNI.Driver_DeleteDataSource(this.swigCPtr, this, utf8_path);
/*     */   }
/*     */ 
/*     */   public boolean TestCapability(String cap) {
/* 103 */     return ogrJNI.Driver_TestCapability(this.swigCPtr, this, cap);
/*     */   }
/*     */ 
/*     */   public String GetName() {
/* 107 */     return ogrJNI.Driver_GetName(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public void Register() {
/* 111 */     ogrJNI.Driver_Register(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public void Deregister() {
/* 115 */     ogrJNI.Driver_Deregister(this.swigCPtr, this);
/*     */   }
/*     */ }

/* Location:           E:\temp\1117\gdal.jar
 * Qualified Name:     org.gdal.ogr.Driver
 * JD-Core Version:    0.5.4
 */