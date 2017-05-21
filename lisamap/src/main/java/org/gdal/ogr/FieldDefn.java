/*     */ package org.gdal.ogr;
/*     */ 
/*     */ public class FieldDefn
/*     */ {
/*     */   private long swigCPtr;
/*     */   protected boolean swigCMemOwn;
/*     */   private Object parentReference;
/*     */ 
/*     */   protected FieldDefn(long cPtr, boolean cMemoryOwn)
/*     */   {
/*  16 */     if (cPtr == 0L)
/*  17 */       throw new RuntimeException();
/*  18 */     this.swigCMemOwn = cMemoryOwn;
/*  19 */     this.swigCPtr = cPtr;
/*     */   }
/*     */ 
/*     */   protected static long getCPtr(FieldDefn obj) {
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
/*  34 */         ogrJNI.delete_FieldDefn(this.swigCPtr);
/*     */       }
/*  36 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static long getCPtrAndDisown(FieldDefn obj)
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
/*  58 */     if (obj instanceof FieldDefn)
/*  59 */       equal = ((FieldDefn)obj).swigCPtr == this.swigCPtr;
/*  60 */     return equal;
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/*  64 */     return (int)this.swigCPtr;
/*     */   }
/*     */ 
/*     */   public FieldDefn(String name_null_ok, int field_type)
/*     */   {
/*  69 */     this(ogrJNI.new_FieldDefn__SWIG_0(name_null_ok, field_type), true);
/*     */   }
/*     */ 
/*     */   public FieldDefn(String name_null_ok) {
/*  73 */     this(ogrJNI.new_FieldDefn__SWIG_1(name_null_ok), true);
/*     */   }
/*     */ 
/*     */   public FieldDefn() {
/*  77 */     this(ogrJNI.new_FieldDefn__SWIG_2(), true);
/*     */   }
/*     */ 
/*     */   public String GetName() {
/*  81 */     return ogrJNI.FieldDefn_GetName(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public String GetNameRef() {
/*  85 */     return ogrJNI.FieldDefn_GetNameRef(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public void SetName(String name) {
/*  89 */     ogrJNI.FieldDefn_SetName(this.swigCPtr, this, name);
/*     */   }
/*     */ 
/*     */   public int GetFieldType() {
/*  93 */     return ogrJNI.FieldDefn_GetFieldType(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public void SetType(int type) {
/*  97 */     ogrJNI.FieldDefn_SetType(this.swigCPtr, this, type);
/*     */   }
/*     */ 
/*     */   public int GetJustify() {
/* 101 */     return ogrJNI.FieldDefn_GetJustify(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public void SetJustify(int justify) {
/* 105 */     ogrJNI.FieldDefn_SetJustify(this.swigCPtr, this, justify);
/*     */   }
/*     */ 
/*     */   public int GetWidth() {
/* 109 */     return ogrJNI.FieldDefn_GetWidth(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public void SetWidth(int width) {
/* 113 */     ogrJNI.FieldDefn_SetWidth(this.swigCPtr, this, width);
/*     */   }
/*     */ 
/*     */   public int GetPrecision() {
/* 117 */     return ogrJNI.FieldDefn_GetPrecision(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public void SetPrecision(int precision) {
/* 121 */     ogrJNI.FieldDefn_SetPrecision(this.swigCPtr, this, precision);
/*     */   }
/*     */ 
/*     */   public String GetTypeName() {
/* 125 */     return ogrJNI.FieldDefn_GetTypeName(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public String GetFieldTypeName(int type) {
/* 129 */     return ogrJNI.FieldDefn_GetFieldTypeName(this.swigCPtr, this, type);
/*     */   }
/*     */ 
/*     */   public int IsIgnored() {
/* 133 */     return ogrJNI.FieldDefn_IsIgnored(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public void SetIgnored(int bIgnored) {
/* 137 */     ogrJNI.FieldDefn_SetIgnored(this.swigCPtr, this, bIgnored);
/*     */   }
/*     */ }

/* Location:           E:\temp\1117\gdal.jar
 * Qualified Name:     org.gdal.ogr.FieldDefn
 * JD-Core Version:    0.5.4
 */