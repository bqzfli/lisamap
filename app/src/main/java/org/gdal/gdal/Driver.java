/*     */ package org.gdal.gdal;
/*     */ 
/*     */ import java.util.Vector;
/*     */ import org.gdal.gdalconst.gdalconstConstants;
/*     */ 
/*     */ public class Driver extends MajorObject
/*     */ {
/*     */   private long swigCPtr;
/*     */ 
/*     */   public Driver(long cPtr, boolean cMemoryOwn)
/*     */   {
/*  18 */     super(gdalJNI.Driver_SWIGUpcast(cPtr), cMemoryOwn);
/*  19 */     this.swigCPtr = cPtr;
/*     */   }
/*     */ 
/*     */   public static long getCPtr(Driver obj) {
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
/*  34 */     super.delete();
/*     */   }
/*     */ 
/*     */   private static Vector StringArrayToVector(String[] options)
/*     */   {
/*  40 */     if (options == null)
/*  41 */       return null;
/*  42 */     Vector v = new Vector();
/*  43 */     for (int i = 0; i < options.length; ++i)
/*  44 */       v.addElement(options[i]);
/*  45 */     return v;
/*     */   }
/*     */ 
/*     */   public Dataset Create(String name, int xsize, int ysize, int bands, int eType, String[] options) {
/*  49 */     return Create(name, xsize, ysize, bands, eType, StringArrayToVector(options));
/*     */   }
/*     */ 
/*     */   public Dataset Create(String name, int xsize, int ysize, int bands, String[] options) {
/*  53 */     return Create(name, xsize, ysize, bands, gdalconstConstants.GDT_Byte, StringArrayToVector(options));
/*     */   }
/*     */ 
/*     */   public Dataset CreateCopy(String name, Dataset src, int strict, String[] options) {
/*  57 */     return CreateCopy(name, src, strict, StringArrayToVector(options), null);
/*     */   }
/*     */ 
/*     */   public Dataset CreateCopy(String name, Dataset src, Vector options) {
/*  61 */     return CreateCopy(name, src, 1, options, null);
/*     */   }
/*     */ 
/*     */   public Dataset CreateCopy(String name, Dataset src, String[] options) {
/*  65 */     return CreateCopy(name, src, 1, StringArrayToVector(options), null);
/*     */   }
/*     */ 
/*     */   public String getShortName()
/*     */   {
/*  70 */     return gdalJNI.Driver_ShortName_get(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public String getLongName() {
/*  74 */     return gdalJNI.Driver_LongName_get(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public String getHelpTopic() {
/*  78 */     return gdalJNI.Driver_HelpTopic_get(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public Dataset Create(String utf8_path, int xsize, int ysize, int bands, int eType, Vector options) {
/*  82 */     long cPtr = gdalJNI.Driver_Create__SWIG_0(this.swigCPtr, this, utf8_path, xsize, ysize, bands, eType, options);
/*  83 */     return (cPtr == 0L) ? null : new Dataset(cPtr, true);
/*     */   }
/*     */ 
/*     */   public Dataset Create(String utf8_path, int xsize, int ysize, int bands, int eType) {
/*  87 */     long cPtr = gdalJNI.Driver_Create__SWIG_1(this.swigCPtr, this, utf8_path, xsize, ysize, bands, eType);
/*  88 */     return (cPtr == 0L) ? null : new Dataset(cPtr, true);
/*     */   }
/*     */ 
/*     */   public Dataset Create(String utf8_path, int xsize, int ysize, int bands) {
/*  92 */     long cPtr = gdalJNI.Driver_Create__SWIG_2(this.swigCPtr, this, utf8_path, xsize, ysize, bands);
/*  93 */     return (cPtr == 0L) ? null : new Dataset(cPtr, true);
/*     */   }
/*     */ 
/*     */   public Dataset Create(String utf8_path, int xsize, int ysize) {
/*  97 */     long cPtr = gdalJNI.Driver_Create__SWIG_3(this.swigCPtr, this, utf8_path, xsize, ysize);
/*  98 */     return (cPtr == 0L) ? null : new Dataset(cPtr, true);
/*     */   }
/*     */ 
/*     */   public Dataset CreateCopy(String utf8_path, Dataset src, int strict, Vector options, ProgressCallback callback) {
/* 102 */     long cPtr = gdalJNI.Driver_CreateCopy__SWIG_0(this.swigCPtr, this, utf8_path, Dataset.getCPtr(src), src, strict, options, callback);
/* 103 */     return (cPtr == 0L) ? null : new Dataset(cPtr, true);
/*     */   }
/*     */ 
/*     */   public Dataset CreateCopy(String utf8_path, Dataset src, int strict, Vector options) {
/* 107 */     long cPtr = gdalJNI.Driver_CreateCopy__SWIG_2(this.swigCPtr, this, utf8_path, Dataset.getCPtr(src), src, strict, options);
/* 108 */     return (cPtr == 0L) ? null : new Dataset(cPtr, true);
/*     */   }
/*     */ 
/*     */   public Dataset CreateCopy(String utf8_path, Dataset src, int strict) {
/* 112 */     long cPtr = gdalJNI.Driver_CreateCopy__SWIG_3(this.swigCPtr, this, utf8_path, Dataset.getCPtr(src), src, strict);
/* 113 */     return (cPtr == 0L) ? null : new Dataset(cPtr, true);
/*     */   }
/*     */ 
/*     */   public Dataset CreateCopy(String utf8_path, Dataset src) {
/* 117 */     long cPtr = gdalJNI.Driver_CreateCopy__SWIG_4(this.swigCPtr, this, utf8_path, Dataset.getCPtr(src), src);
/* 118 */     return (cPtr == 0L) ? null : new Dataset(cPtr, true);
/*     */   }
/*     */ 
/*     */   public int Delete(String utf8_path) {
/* 122 */     return gdalJNI.Driver_Delete(this.swigCPtr, this, utf8_path);
/*     */   }
/*     */ 
/*     */   public int Rename(String newName, String oldName) {
/* 126 */     return gdalJNI.Driver_Rename(this.swigCPtr, this, newName, oldName);
/*     */   }
/*     */ 
/*     */   public int CopyFiles(String newName, String oldName) {
/* 130 */     return gdalJNI.Driver_CopyFiles(this.swigCPtr, this, newName, oldName);
/*     */   }
/*     */ 
/*     */   public int Register() {
/* 134 */     return gdalJNI.Driver_Register(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public void Deregister() {
/* 138 */     gdalJNI.Driver_Deregister(this.swigCPtr, this);
/*     */   }
/*     */ }

/* Location:           E:\temp\1117\gdal.jar
 * Qualified Name:     org.gdal.gdal.Driver
 * JD-Core Version:    0.5.4
 */