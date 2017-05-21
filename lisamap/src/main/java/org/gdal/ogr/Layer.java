/*     */ package org.gdal.ogr;
/*     */ 
/*     */ import java.util.Vector;
/*     */ import org.gdal.osr.SpatialReference;
/*     */ 
/*     */ public class Layer
/*     */ {
/*     */   private long swigCPtr;
/*     */   protected boolean swigCMemOwn;
/*     */   private Object parentReference;
/*     */ 
/*     */   protected Layer(long cPtr, boolean cMemoryOwn)
/*     */   {
/*  18 */     if (cPtr == 0L)
/*  19 */       throw new RuntimeException();
/*  20 */     this.swigCMemOwn = cMemoryOwn;
/*  21 */     this.swigCPtr = cPtr;
/*     */   }
/*     */ 
/*     */   protected static long getCPtr(Layer obj) {
/*  25 */     return (obj == null) ? 0L : obj.swigCPtr;
/*     */   }
/*     */ 
/*     */   public synchronized void delete() {
/*  29 */     if (this.swigCPtr != 0L) {
/*  30 */       if (this.swigCMemOwn) {
/*  31 */         this.swigCMemOwn = false;
/*  32 */         throw new UnsupportedOperationException("C++ destructor does not have public access");
/*     */       }
/*  34 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static long getCPtrAndDisown(Layer obj)
/*     */   {
/*  41 */     if (obj != null)
/*     */     {
/*  43 */       obj.swigCMemOwn = false;
/*  44 */       obj.parentReference = null;
/*     */     }
/*  46 */     return getCPtr(obj);
/*     */   }
/*     */ 
/*     */   protected void addReference(Object reference)
/*     */   {
/*  51 */     this.parentReference = reference;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object obj) {
/*  55 */     boolean equal = false;
/*  56 */     if (obj instanceof Layer)
/*  57 */       equal = ((Layer)obj).swigCPtr == this.swigCPtr;
/*  58 */     return equal;
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/*  62 */     return (int)this.swigCPtr;
/*     */   }
/*     */ 
/*     */   public double[] GetExtent(boolean force)
/*     */   {
/*  67 */     double[] argout = new double[4];
/*     */     try
/*     */     {
/*  70 */       int ret = GetExtent(argout, (force) ? 1 : 0);
/*  71 */       return (ret == 0) ? argout : null;
/*     */     }
/*     */     catch (RuntimeException e) {
/*     */     }
/*  75 */     return null;
/*     */   }
/*     */ 
/*     */   public double[] GetExtent()
/*     */   {
/*  81 */     return GetExtent(true);
/*     */   }
/*     */ 
/*     */   public int GetRefCount() {
/*  85 */     return ogrJNI.Layer_GetRefCount(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public void SetSpatialFilter(Geometry filter) {
/*  89 */     ogrJNI.Layer_SetSpatialFilter(this.swigCPtr, this, Geometry.getCPtr(filter), filter);
/*     */   }
/*     */ 
/*     */   public void SetSpatialFilterRect(double minx, double miny, double maxx, double maxy) {
/*  93 */     ogrJNI.Layer_SetSpatialFilterRect(this.swigCPtr, this, minx, miny, maxx, maxy);
/*     */   }
/*     */ 
/*     */   public Geometry GetSpatialFilter() {
/*  97 */     long cPtr = ogrJNI.Layer_GetSpatialFilter(this.swigCPtr, this);
/*  98 */     Geometry ret = null;
/*  99 */     if (cPtr != 0L) {
/* 100 */       ret = new Geometry(cPtr, false);
/* 101 */       ret.addReference(this);
/*     */     }
/* 103 */     return ret;
/*     */   }
/*     */ 
/*     */   public int SetAttributeFilter(String filter_string) {
/* 107 */     return ogrJNI.Layer_SetAttributeFilter(this.swigCPtr, this, filter_string);
/*     */   }
/*     */ 
/*     */   public void ResetReading() {
/* 111 */     ogrJNI.Layer_ResetReading(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public String GetName() {
/* 115 */     return ogrJNI.Layer_GetName(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public int GetGeomType() {
/* 119 */     return ogrJNI.Layer_GetGeomType(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public String GetGeometryColumn() {
/* 123 */     return ogrJNI.Layer_GetGeometryColumn(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public String GetFIDColumn() {
/* 127 */     return ogrJNI.Layer_GetFIDColumn(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public Feature GetFeature(int fid) {
/* 131 */     long cPtr = ogrJNI.Layer_GetFeature(this.swigCPtr, this, fid);
/* 132 */     return (cPtr == 0L) ? null : new Feature(cPtr, true);
/*     */   }
/*     */ 
/*     */   public Feature GetNextFeature() {
/* 136 */     long cPtr = ogrJNI.Layer_GetNextFeature(this.swigCPtr, this);
/* 137 */     return (cPtr == 0L) ? null : new Feature(cPtr, true);
/*     */   }
/*     */ 
/*     */   public int SetNextByIndex(int new_index) {
/* 141 */     return ogrJNI.Layer_SetNextByIndex(this.swigCPtr, this, new_index);
/*     */   }
/*     */ 
/*     */   public int SetFeature(Feature feature) {
/* 145 */     return ogrJNI.Layer_SetFeature(this.swigCPtr, this, Feature.getCPtr(feature), feature);
/*     */   }
/*     */ 
/*     */   public int CreateFeature(Feature feature) {
/* 149 */     return ogrJNI.Layer_CreateFeature(this.swigCPtr, this, Feature.getCPtr(feature), feature);
/*     */   }
/*     */ 
/*     */   public int DeleteFeature(int fid) {
/* 153 */     return ogrJNI.Layer_DeleteFeature(this.swigCPtr, this, fid);
/*     */   }
/*     */ 
/*     */   public int SyncToDisk() {
/* 157 */     return ogrJNI.Layer_SyncToDisk(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public FeatureDefn GetLayerDefn() {
/* 161 */     long cPtr = ogrJNI.Layer_GetLayerDefn(this.swigCPtr, this);
/* 162 */     FeatureDefn ret = null;
/* 163 */     if (cPtr != 0L) {
/* 164 */       ret = new FeatureDefn(cPtr, false);
/* 165 */       ret.addReference(this);
/*     */     }
/* 167 */     return ret;
/*     */   }
/*     */ 
/*     */   public int GetFeatureCount(int force) {
/* 171 */     return ogrJNI.Layer_GetFeatureCount__SWIG_0(this.swigCPtr, this, force);
/*     */   }
/*     */ 
/*     */   public int GetFeatureCount() {
/* 175 */     return ogrJNI.Layer_GetFeatureCount__SWIG_1(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public int GetExtent(double[] argout, int force) {
/* 179 */     return ogrJNI.Layer_GetExtent(this.swigCPtr, this, argout, force);
/*     */   }
/*     */ 
/*     */   public boolean TestCapability(String cap) {
/* 183 */     return ogrJNI.Layer_TestCapability(this.swigCPtr, this, cap);
/*     */   }
/*     */ 
/*     */   public int CreateField(FieldDefn field_def, int approx_ok) {
/* 187 */     return ogrJNI.Layer_CreateField__SWIG_0(this.swigCPtr, this, FieldDefn.getCPtr(field_def), field_def, approx_ok);
/*     */   }
/*     */ 
/*     */   public int CreateField(FieldDefn field_def) {
/* 191 */     return ogrJNI.Layer_CreateField__SWIG_1(this.swigCPtr, this, FieldDefn.getCPtr(field_def), field_def);
/*     */   }
/*     */ 
/*     */   public int DeleteField(int iField) {
/* 195 */     return ogrJNI.Layer_DeleteField(this.swigCPtr, this, iField);
/*     */   }
/*     */ 
/*     */   public int ReorderField(int iOldFieldPos, int iNewFieldPos) {
/* 199 */     return ogrJNI.Layer_ReorderField(this.swigCPtr, this, iOldFieldPos, iNewFieldPos);
/*     */   }
/*     */ 
/*     */   public int ReorderFields(int[] nList) {
/* 203 */     return ogrJNI.Layer_ReorderFields(this.swigCPtr, this, nList);
/*     */   }
/*     */ 
/*     */   public int AlterFieldDefn(int iField, FieldDefn field_def, int nFlags) {
/* 207 */     return ogrJNI.Layer_AlterFieldDefn(this.swigCPtr, this, iField, FieldDefn.getCPtr(field_def), field_def, nFlags);
/*     */   }
/*     */ 
/*     */   public int StartTransaction() {
/* 211 */     return ogrJNI.Layer_StartTransaction(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public int CommitTransaction() {
/* 215 */     return ogrJNI.Layer_CommitTransaction(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public int RollbackTransaction() {
/* 219 */     return ogrJNI.Layer_RollbackTransaction(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public SpatialReference GetSpatialRef() {
/* 223 */     long cPtr = ogrJNI.Layer_GetSpatialRef(this.swigCPtr, this);
/* 224 */     return (cPtr == 0L) ? null : new SpatialReference(cPtr, true);
/*     */   }
/*     */ 
/*     */   public long GetFeaturesRead() {
/* 228 */     return ogrJNI.Layer_GetFeaturesRead(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public int SetIgnoredFields(Vector options) {
/* 232 */     return ogrJNI.Layer_SetIgnoredFields(this.swigCPtr, this, options);
/*     */   }
/*     */ }

/* Location:           E:\temp\1117\gdal.jar
 * Qualified Name:     org.gdal.ogr.Layer
 * JD-Core Version:    0.5.4
 */