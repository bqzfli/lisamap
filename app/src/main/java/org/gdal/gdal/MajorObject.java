/*     */ package org.gdal.gdal;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class MajorObject
/*     */ {
/*     */   private long swigCPtr;
/*     */   protected boolean swigCMemOwn;
/*     */   private Object parentReference;
/*     */ 
/*     */   protected MajorObject(long cPtr, boolean cMemoryOwn)
/*     */   {
/*  16 */     if (cPtr == 0L)
/*  17 */       throw new RuntimeException();
/*  18 */     this.swigCMemOwn = cMemoryOwn;
/*  19 */     this.swigCPtr = cPtr;
/*     */   }
/*     */ 
/*     */   protected static long getCPtr(MajorObject obj) {
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
/*     */   protected void addReference(Object reference)
/*     */   {
/*  40 */     this.parentReference = reference;
/*     */   }
/*     */ 
/*     */   public int SetMetadata(Hashtable metadata, String domain)
/*     */   {
/*  46 */     if (metadata == null)
/*  47 */       return SetMetadata((Vector)null, domain);
/*  48 */     Vector v = new Vector();
/*  49 */     Enumeration values = metadata.elements();
/*  50 */     Enumeration keys = metadata.keys();
/*  51 */     while (keys.hasMoreElements())
/*     */     {
/*  53 */       v.add((String)keys.nextElement() + "=" + (String)values.nextElement());
/*     */     }
/*  55 */     return SetMetadata(v, domain);
/*     */   }
/*     */ 
/*     */   public int SetMetadata(Hashtable metadata)
/*     */   {
/*  60 */     return SetMetadata(metadata, null);
/*     */   }
/*     */ 
/*     */   public String GetDescription() {
/*  64 */     return gdalJNI.MajorObject_GetDescription(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public void SetDescription(String pszNewDesc) {
/*  68 */     gdalJNI.MajorObject_SetDescription(this.swigCPtr, this, pszNewDesc);
/*     */   }
/*     */ 
/*     */   public Hashtable GetMetadata_Dict(String pszDomain) {
/*  72 */     return gdalJNI.MajorObject_GetMetadata_Dict__SWIG_0(this.swigCPtr, this, pszDomain);
/*     */   }
/*     */ 
/*     */   public Hashtable GetMetadata_Dict() {
/*  76 */     return gdalJNI.MajorObject_GetMetadata_Dict__SWIG_1(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public Vector GetMetadata_List(String pszDomain) {
/*  80 */     return gdalJNI.MajorObject_GetMetadata_List__SWIG_0(this.swigCPtr, this, pszDomain);
/*     */   }
/*     */ 
/*     */   public Vector GetMetadata_List() {
/*  84 */     return gdalJNI.MajorObject_GetMetadata_List__SWIG_1(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public int SetMetadata(Vector papszMetadata, String pszDomain) {
/*  88 */     return gdalJNI.MajorObject_SetMetadata__SWIG_0(this.swigCPtr, this, papszMetadata, pszDomain);
/*     */   }
/*     */ 
/*     */   public int SetMetadata(Vector papszMetadata) {
/*  92 */     return gdalJNI.MajorObject_SetMetadata__SWIG_1(this.swigCPtr, this, papszMetadata);
/*     */   }
/*     */ 
/*     */   public int SetMetadata(String pszMetadataString, String pszDomain) {
/*  96 */     return gdalJNI.MajorObject_SetMetadata__SWIG_2(this.swigCPtr, this, pszMetadataString, pszDomain);
/*     */   }
/*     */ 
/*     */   public int SetMetadata(String pszMetadataString) {
/* 100 */     return gdalJNI.MajorObject_SetMetadata__SWIG_3(this.swigCPtr, this, pszMetadataString);
/*     */   }
/*     */ 
/*     */   public String GetMetadataItem(String pszName, String pszDomain) {
/* 104 */     return gdalJNI.MajorObject_GetMetadataItem__SWIG_0(this.swigCPtr, this, pszName, pszDomain);
/*     */   }
/*     */ 
/*     */   public String GetMetadataItem(String pszName) {
/* 108 */     return gdalJNI.MajorObject_GetMetadataItem__SWIG_1(this.swigCPtr, this, pszName);
/*     */   }
/*     */ 
/*     */   public int SetMetadataItem(String pszName, String pszValue, String pszDomain) {
/* 112 */     return gdalJNI.MajorObject_SetMetadataItem__SWIG_0(this.swigCPtr, this, pszName, pszValue, pszDomain);
/*     */   }
/*     */ 
/*     */   public int SetMetadataItem(String pszName, String pszValue) {
/* 116 */     return gdalJNI.MajorObject_SetMetadataItem__SWIG_1(this.swigCPtr, this, pszName, pszValue);
/*     */   }
/*     */ }

/* Location:           E:\temp\1117\gdal.jar
 * Qualified Name:     org.gdal.gdal.MajorObject
 * JD-Core Version:    0.5.4
 */