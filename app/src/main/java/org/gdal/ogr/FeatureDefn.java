/*     */ package org.gdal.ogr;
/*     */ 
/*     */ public class FeatureDefn
/*     */ {
/*     */   private long swigCPtr;
/*     */   protected boolean swigCMemOwn;
/*     */   private Object parentReference;
/*     */ 
/*     */   protected FeatureDefn(long cPtr, boolean cMemoryOwn)
/*     */   {
/*  16 */     if (cPtr == 0L)
/*  17 */       throw new RuntimeException();
/*  18 */     this.swigCMemOwn = cMemoryOwn;
/*  19 */     this.swigCPtr = cPtr;
/*     */   }
/*     */ 
/*     */   protected static long getCPtr(FeatureDefn obj) {
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
/*  34 */         ogrJNI.delete_FeatureDefn(this.swigCPtr);
/*     */       }
/*  36 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static long getCPtrAndDisown(FeatureDefn obj)
/*     */   {
/*  43 */     if (obj != null)
/*     */     {
/*  45 */       obj.swigCMemOwn = false;
/*  46 */       obj.parentReference = null;
/*     */     }
/*  48 */     return getCPtr(obj);
/*     */   }
/*     */ 
/*     */   protected void addReference(Object reference)
/*     */   {
/*  53 */     this.parentReference = reference;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object obj) {
/*  57 */     boolean equal = false;
/*  58 */     if (obj instanceof FeatureDefn)
/*  59 */       equal = ((FeatureDefn)obj).swigCPtr == this.swigCPtr;
/*  60 */     return equal;
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/*  64 */     return (int)this.swigCPtr;
/*     */   }
/*     */ 
/*     */   public FeatureDefn(String name_null_ok)
/*     */   {
/*  69 */     this(ogrJNI.new_FeatureDefn__SWIG_0(name_null_ok), true);
/*     */   }
/*     */ 
/*     */   public FeatureDefn() {
/*  73 */     this(ogrJNI.new_FeatureDefn__SWIG_1(), true);
/*     */   }
/*     */ 
/*     */   public String GetName() {
/*  77 */     return ogrJNI.FeatureDefn_GetName(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public int GetFieldCount() {
/*  81 */     return ogrJNI.FeatureDefn_GetFieldCount(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public FieldDefn GetFieldDefn(int i) {
/*  85 */     long cPtr = ogrJNI.FeatureDefn_GetFieldDefn(this.swigCPtr, this, i);
/*  86 */     FieldDefn ret = null;
/*  87 */     if (cPtr != 0L) {
/*  88 */       ret = new FieldDefn(cPtr, false);
/*  89 */       ret.addReference(this);
/*     */     }
/*  91 */     return ret;
/*     */   }
/*     */ 
/*     */   public int GetFieldIndex(String name) {
/*  95 */     return ogrJNI.FeatureDefn_GetFieldIndex(this.swigCPtr, this, name);
/*     */   }
/*     */ 
/*     */   public void AddFieldDefn(FieldDefn defn) {
/*  99 */     ogrJNI.FeatureDefn_AddFieldDefn(this.swigCPtr, this, FieldDefn.getCPtr(defn), defn);
/*     */   }
/*     */ 
/*     */   public int GetGeomType() {
/* 103 */     return ogrJNI.FeatureDefn_GetGeomType(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public void SetGeomType(int geom_type) {
/* 107 */     ogrJNI.FeatureDefn_SetGeomType(this.swigCPtr, this, geom_type);
/*     */   }
/*     */ 
/*     */   public int GetReferenceCount() {
/* 111 */     return ogrJNI.FeatureDefn_GetReferenceCount(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public int IsGeometryIgnored() {
/* 115 */     return ogrJNI.FeatureDefn_IsGeometryIgnored(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public void SetGeometryIgnored(int bIgnored) {
/* 119 */     ogrJNI.FeatureDefn_SetGeometryIgnored(this.swigCPtr, this, bIgnored);
/*     */   }
/*     */ 
/*     */   public int IsStyleIgnored() {
/* 123 */     return ogrJNI.FeatureDefn_IsStyleIgnored(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public void SetStyleIgnored(int bIgnored) {
/* 127 */     ogrJNI.FeatureDefn_SetStyleIgnored(this.swigCPtr, this, bIgnored);
/*     */   }
/*     */ }

/* Location:           E:\temp\1117\gdal.jar
 * Qualified Name:     org.gdal.ogr.FeatureDefn
 * JD-Core Version:    0.5.4
 */