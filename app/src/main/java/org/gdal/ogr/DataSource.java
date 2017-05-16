/*     */ package org.gdal.ogr;
/*     */ 
/*     */ import java.util.Vector;
/*     */ import org.gdal.osr.SpatialReference;
/*     */ 
/*     */ public class DataSource
/*     */ {
/*     */   private long swigCPtr;
/*     */   protected boolean swigCMemOwn;
/*     */ 
/*     */   protected DataSource(long cPtr, boolean cMemoryOwn)
/*     */   {
/*  18 */     if (cPtr == 0L)
/*  19 */       throw new RuntimeException();
/*  20 */     this.swigCMemOwn = cMemoryOwn;
/*  21 */     this.swigCPtr = cPtr;
/*     */   }
/*     */ 
/*     */   protected static long getCPtr(DataSource obj) {
/*  25 */     return (obj == null) ? 0L : obj.swigCPtr;
/*     */   }
/*     */ 
/*     */   protected void finalize() {
/*  29 */     delete();
/*     */   }
/*     */ 
/*     */   public synchronized void delete() {
/*  33 */     if (this.swigCPtr != 0L) {
/*  34 */       if (this.swigCMemOwn) {
/*  35 */         this.swigCMemOwn = false;
/*  36 */         ogrJNI.delete_DataSource(this.swigCPtr);
/*     */       }
/*  38 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/*  44 */     boolean equal = false;
/*  45 */     if (obj instanceof DataSource)
/*  46 */       equal = ((DataSource)obj).swigCPtr == this.swigCPtr;
/*  47 */     return equal;
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/*  51 */     return (int)this.swigCPtr;
/*     */   }
/*     */ 
/*     */   public Layer GetLayer(int index)
/*     */   {
/*  56 */     return GetLayerByIndex(index);
/*     */   }
/*     */ 
/*     */   public Layer GetLayer(String layerName)
/*     */   {
/*  61 */     return GetLayerByName(layerName);
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/*  66 */     return ogrJNI.DataSource_name_get(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public int GetRefCount() {
/*  70 */     return ogrJNI.DataSource_GetRefCount(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public int GetSummaryRefCount() {
/*  74 */     return ogrJNI.DataSource_GetSummaryRefCount(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public int GetLayerCount() {
/*  78 */     return ogrJNI.DataSource_GetLayerCount(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public Driver GetDriver() {
/*  82 */     long cPtr = ogrJNI.DataSource_GetDriver(this.swigCPtr, this);
/*  83 */     return (cPtr == 0L) ? null : new Driver(cPtr, false);
/*     */   }
/*     */ 
/*     */   public String GetName() {
/*  87 */     return ogrJNI.DataSource_GetName(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public int DeleteLayer(int index) {
/*  91 */     return ogrJNI.DataSource_DeleteLayer(this.swigCPtr, this, index);
/*     */   }
/*     */ 
/*     */   public int SyncToDisk() {
/*  95 */     return ogrJNI.DataSource_SyncToDisk(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public Layer CreateLayer(String name, SpatialReference srs, int geom_type, Vector options) {
/*  99 */     long cPtr = ogrJNI.DataSource_CreateLayer__SWIG_0(this.swigCPtr, this, name, SpatialReference.getCPtr(srs), srs, geom_type, options);
/* 100 */     Layer ret = null;
/* 101 */     if (cPtr != 0L) {
/* 102 */       ret = new Layer(cPtr, false);
/* 103 */       ret.addReference(this);
/*     */     }
/* 105 */     return ret;
/*     */   }
/*     */ 
/*     */   public Layer CreateLayer(String name, SpatialReference srs, int geom_type) {
/* 109 */     long cPtr = ogrJNI.DataSource_CreateLayer__SWIG_1(this.swigCPtr, this, name, SpatialReference.getCPtr(srs), srs, geom_type);
/* 110 */     Layer ret = null;
/* 111 */     if (cPtr != 0L) {
/* 112 */       ret = new Layer(cPtr, false);
/* 113 */       ret.addReference(this);
/*     */     }
/* 115 */     return ret;
/*     */   }
/*     */ 
/*     */   public Layer CreateLayer(String name, SpatialReference srs) {
/* 119 */     long cPtr = ogrJNI.DataSource_CreateLayer__SWIG_2(this.swigCPtr, this, name, SpatialReference.getCPtr(srs), srs);
/* 120 */     Layer ret = null;
/* 121 */     if (cPtr != 0L) {
/* 122 */       ret = new Layer(cPtr, false);
/* 123 */       ret.addReference(this);
/*     */     }
/* 125 */     return ret;
/*     */   }
/*     */ 
/*     */   public Layer CreateLayer(String name) {
/* 129 */     long cPtr = ogrJNI.DataSource_CreateLayer__SWIG_3(this.swigCPtr, this, name);
/* 130 */     Layer ret = null;
/* 131 */     if (cPtr != 0L) {
/* 132 */       ret = new Layer(cPtr, false);
/* 133 */       ret.addReference(this);
/*     */     }
/* 135 */     return ret;
/*     */   }
/*     */ 
/*     */   public Layer CopyLayer(Layer src_layer, String new_name, Vector options) {
/* 139 */     long cPtr = ogrJNI.DataSource_CopyLayer__SWIG_0(this.swigCPtr, this, Layer.getCPtr(src_layer), src_layer, new_name, options);
/* 140 */     Layer ret = null;
/* 141 */     if (cPtr != 0L) {
/* 142 */       ret = new Layer(cPtr, false);
/* 143 */       ret.addReference(this);
/*     */     }
/* 145 */     return ret;
/*     */   }
/*     */ 
/*     */   public Layer CopyLayer(Layer src_layer, String new_name) {
/* 149 */     long cPtr = ogrJNI.DataSource_CopyLayer__SWIG_1(this.swigCPtr, this, Layer.getCPtr(src_layer), src_layer, new_name);
/* 150 */     Layer ret = null;
/* 151 */     if (cPtr != 0L) {
/* 152 */       ret = new Layer(cPtr, false);
/* 153 */       ret.addReference(this);
/*     */     }
/* 155 */     return ret;
/*     */   }
/*     */ 
/*     */   public Layer GetLayerByIndex(int index) {
/* 159 */     long cPtr = ogrJNI.DataSource_GetLayerByIndex(this.swigCPtr, this, index);
/* 160 */     Layer ret = null;
/* 161 */     if (cPtr != 0L) {
/* 162 */       ret = new Layer(cPtr, false);
/* 163 */       ret.addReference(this);
/*     */     }
/* 165 */     return ret;
/*     */   }
/*     */ 
/*     */   public Layer GetLayerByName(String layer_name) {
/* 169 */     long cPtr = ogrJNI.DataSource_GetLayerByName(this.swigCPtr, this, layer_name);
/* 170 */     Layer ret = null;
/* 171 */     if (cPtr != 0L) {
/* 172 */       ret = new Layer(cPtr, false);
/* 173 */       ret.addReference(this);
/*     */     }
/* 175 */     return ret;
/*     */   }
/*     */ 
/*     */   public boolean TestCapability(String cap) {
/* 179 */     return ogrJNI.DataSource_TestCapability(this.swigCPtr, this, cap);
/*     */   }
/*     */ 
/*     */   public Layer ExecuteSQL(String statement, Geometry spatialFilter, String dialect) {
/* 183 */     long cPtr = ogrJNI.DataSource_ExecuteSQL__SWIG_0(this.swigCPtr, this, statement, Geometry.getCPtr(spatialFilter), spatialFilter, dialect);
/* 184 */     Layer ret = null;
/* 185 */     if (cPtr != 0L) {
/* 186 */       ret = new Layer(cPtr, false);
/* 187 */       ret.addReference(this);
/*     */     }
/* 189 */     return ret;
/*     */   }
/*     */ 
/*     */   public Layer ExecuteSQL(String statement, Geometry spatialFilter) {
/* 193 */     long cPtr = ogrJNI.DataSource_ExecuteSQL__SWIG_1(this.swigCPtr, this, statement, Geometry.getCPtr(spatialFilter), spatialFilter);
/* 194 */     Layer ret = null;
/* 195 */     if (cPtr != 0L) {
/* 196 */       ret = new Layer(cPtr, false);
/* 197 */       ret.addReference(this);
/*     */     }
/* 199 */     return ret;
/*     */   }
/*     */ 
/*     */   public Layer ExecuteSQL(String statement) {
/* 203 */     long cPtr = ogrJNI.DataSource_ExecuteSQL__SWIG_2(this.swigCPtr, this, statement);
/* 204 */     Layer ret = null;
/* 205 */     if (cPtr != 0L) {
/* 206 */       ret = new Layer(cPtr, false);
/* 207 */       ret.addReference(this);
/*     */     }
/* 209 */     return ret;
/*     */   }
/*     */ 
/*     */   public void ReleaseResultSet(Layer layer) {
/* 213 */     ogrJNI.DataSource_ReleaseResultSet(this.swigCPtr, this, Layer.getCPtrAndDisown(layer), layer);
/*     */   }
/*     */ }

/* Location:           E:\temp\1117\gdal.jar
 * Qualified Name:     org.gdal.ogr.DataSource
 * JD-Core Version:    0.5.4
 */