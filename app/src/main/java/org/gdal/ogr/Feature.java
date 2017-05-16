/*     */ package org.gdal.ogr;
/*     */ 
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class Feature
/*     */   implements Cloneable
/*     */ {
/*     */   private long swigCPtr;
/*     */   private FeatureNative nativeObject;
/*     */ 
/*     */   protected Feature(long cPtr, boolean cMemoryOwn)
/*     */   {
/*  18 */     if (cPtr == 0L)
/*  19 */       throw new RuntimeException();
/*  20 */     this.swigCPtr = cPtr;
/*  21 */     if (cMemoryOwn)
/*  22 */       this.nativeObject = new FeatureNative(this, cPtr);
/*     */   }
/*     */ 
/*     */   protected static long getCPtr(Feature obj) {
/*  26 */     return (obj == null) ? 0L : obj.swigCPtr;
/*     */   }
/*     */ 
/*     */   public void delete()
/*     */   {
/*  31 */     if (this.nativeObject == null)
/*     */       return;
/*  33 */     this.nativeObject.delete();
/*  34 */     this.nativeObject = null;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/*  41 */     boolean equal = false;
/*  42 */     if (obj instanceof Feature)
/*  43 */       equal = Equal((Feature)obj);
/*  44 */     return equal;
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/*  48 */     return (int)this.swigCPtr;
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*  53 */     return Clone();
/*     */   }
/*     */ 
/*     */   public Feature(FeatureDefn feature_def) {
/*  57 */     this(ogrJNI.new_Feature(FeatureDefn.getCPtr(feature_def), feature_def), true);
/*     */   }
/*     */ 
/*     */   public FeatureDefn GetDefnRef() {
/*  61 */     long cPtr = ogrJNI.Feature_GetDefnRef(this.swigCPtr, this);
/*  62 */     FeatureDefn ret = null;
/*  63 */     if (cPtr != 0L) {
/*  64 */       ret = new FeatureDefn(cPtr, false);
/*  65 */       ret.addReference(this);
/*     */     }
/*  67 */     return ret;
/*     */   }
/*     */ 
/*     */   public int SetGeometry(Geometry geom) {
/*  71 */     return ogrJNI.Feature_SetGeometry(this.swigCPtr, this, Geometry.getCPtr(geom), geom);
/*     */   }
/*     */ 
/*     */   public int SetGeometryDirectly(Geometry geom) {
/*  75 */     int ret = ogrJNI.Feature_SetGeometryDirectly(this.swigCPtr, this, Geometry.getCPtrAndDisown(geom), geom);
/*  76 */     if (geom != null)
/*  77 */       geom.addReference(this);
/*  78 */     return ret;
/*     */   }
/*     */ 
/*     */   public Geometry GetGeometryRef() {
/*  82 */     long cPtr = ogrJNI.Feature_GetGeometryRef(this.swigCPtr, this);
/*  83 */     Geometry ret = null;
/*  84 */     if (cPtr != 0L) {
/*  85 */       ret = new Geometry(cPtr, false);
/*  86 */       ret.addReference(this);
/*     */     }
/*  88 */     return ret;
/*     */   }
/*     */ 
/*     */   public Feature Clone() {
/*  92 */     long cPtr = ogrJNI.Feature_Clone(this.swigCPtr, this);
/*  93 */     return (cPtr == 0L) ? null : new Feature(cPtr, true);
/*     */   }
/*     */ 
/*     */   public boolean Equal(Feature feature) {
/*  97 */     return ogrJNI.Feature_Equal(this.swigCPtr, this, getCPtr(feature), feature);
/*     */   }
/*     */ 
/*     */   public int GetFieldCount() {
/* 101 */     return ogrJNI.Feature_GetFieldCount(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public FieldDefn GetFieldDefnRef(int id) {
/* 105 */     long cPtr = ogrJNI.Feature_GetFieldDefnRef__SWIG_0(this.swigCPtr, this, id);
/* 106 */     FieldDefn ret = null;
/* 107 */     if (cPtr != 0L) {
/* 108 */       ret = new FieldDefn(cPtr, false);
/* 109 */       ret.addReference(this);
/*     */     }
/* 111 */     return ret;
/*     */   }
/*     */ 
/*     */   public FieldDefn GetFieldDefnRef(String name) {
/* 115 */     long cPtr = ogrJNI.Feature_GetFieldDefnRef__SWIG_1(this.swigCPtr, this, name);
/* 116 */     FieldDefn ret = null;
/* 117 */     if (cPtr != 0L) {
/* 118 */       ret = new FieldDefn(cPtr, false);
/* 119 */       ret.addReference(this);
/*     */     }
/* 121 */     return ret;
/*     */   }
/*     */ 
/*     */   public String GetFieldAsString(int id) {
/* 125 */     return ogrJNI.Feature_GetFieldAsString__SWIG_0(this.swigCPtr, this, id);
/*     */   }
/*     */ 
/*     */   public String GetFieldAsString(String name) {
/* 129 */     return ogrJNI.Feature_GetFieldAsString__SWIG_1(this.swigCPtr, this, name);
/*     */   }
/*     */ 
/*     */   public int GetFieldAsInteger(int id) {
/* 133 */     return ogrJNI.Feature_GetFieldAsInteger__SWIG_0(this.swigCPtr, this, id);
/*     */   }
/*     */ 
/*     */   public int GetFieldAsInteger(String name) {
/* 137 */     return ogrJNI.Feature_GetFieldAsInteger__SWIG_1(this.swigCPtr, this, name);
/*     */   }
/*     */ 
/*     */   public double GetFieldAsDouble(int id) {
/* 141 */     return ogrJNI.Feature_GetFieldAsDouble__SWIG_0(this.swigCPtr, this, id);
/*     */   }
/*     */ 
/*     */   public double GetFieldAsDouble(String name) {
/* 145 */     return ogrJNI.Feature_GetFieldAsDouble__SWIG_1(this.swigCPtr, this, name);
/*     */   }
/*     */ 
/*     */   public void GetFieldAsDateTime(int id, int[] pnYear, int[] pnMonth, int[] pnDay, int[] pnHour, int[] pnMinute, int[] pnSecond, int[] pnTZFlag) {
/* 149 */     ogrJNI.Feature_GetFieldAsDateTime(this.swigCPtr, this, id, pnYear, pnMonth, pnDay, pnHour, pnMinute, pnSecond, pnTZFlag);
/*     */   }
/*     */ 
/*     */   public int[] GetFieldAsIntegerList(int id) {
/* 153 */     return ogrJNI.Feature_GetFieldAsIntegerList(this.swigCPtr, this, id);
/*     */   }
/*     */ 
/*     */   public double[] GetFieldAsDoubleList(int id) {
/* 157 */     return ogrJNI.Feature_GetFieldAsDoubleList(this.swigCPtr, this, id);
/*     */   }
/*     */ 
/*     */   public String[] GetFieldAsStringList(int id) {
/* 161 */     return ogrJNI.Feature_GetFieldAsStringList(this.swigCPtr, this, id);
/*     */   }
/*     */ 
/*     */   public boolean IsFieldSet(int id) {
/* 165 */     return ogrJNI.Feature_IsFieldSet__SWIG_0(this.swigCPtr, this, id);
/*     */   }
/*     */ 
/*     */   public boolean IsFieldSet(String name) {
/* 169 */     return ogrJNI.Feature_IsFieldSet__SWIG_1(this.swigCPtr, this, name);
/*     */   }
/*     */ 
/*     */   public int GetFieldIndex(String name) {
/* 173 */     return ogrJNI.Feature_GetFieldIndex(this.swigCPtr, this, name);
/*     */   }
/*     */ 
/*     */   public int GetFID() {
/* 177 */     return ogrJNI.Feature_GetFID(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public int SetFID(int fid) {
/* 181 */     return ogrJNI.Feature_SetFID(this.swigCPtr, this, fid);
/*     */   }
/*     */ 
/*     */   public void DumpReadable() {
/* 185 */     ogrJNI.Feature_DumpReadable(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public void UnsetField(int id) {
/* 189 */     ogrJNI.Feature_UnsetField__SWIG_0(this.swigCPtr, this, id);
/*     */   }
/*     */ 
/*     */   public void UnsetField(String name) {
/* 193 */     ogrJNI.Feature_UnsetField__SWIG_1(this.swigCPtr, this, name);
/*     */   }
/*     */ 
/*     */   public void SetField(int id, String value) {
/* 197 */     ogrJNI.Feature_SetField__SWIG_0(this.swigCPtr, this, id, value);
/*     */   }
/*     */ 
/*     */   public void SetField(String name, String value) {
/* 201 */     ogrJNI.Feature_SetField__SWIG_1(this.swigCPtr, this, name, value);
/*     */   }
/*     */ 
/*     */   public void SetField(int id, int value) {
/* 205 */     ogrJNI.Feature_SetField__SWIG_2(this.swigCPtr, this, id, value);
/*     */   }
/*     */ 
/*     */   public void SetField(String name, int value) {
/* 209 */     ogrJNI.Feature_SetField__SWIG_3(this.swigCPtr, this, name, value);
/*     */   }
/*     */ 
/*     */   public void SetField(int id, double value) {
/* 213 */     ogrJNI.Feature_SetField__SWIG_4(this.swigCPtr, this, id, value);
/*     */   }
/*     */ 
/*     */   public void SetField(String name, double value) {
/* 217 */     ogrJNI.Feature_SetField__SWIG_5(this.swigCPtr, this, name, value);
/*     */   }
/*     */ 
/*     */   public void SetField(int id, int year, int month, int day, int hour, int minute, int second, int tzflag) {
/* 221 */     ogrJNI.Feature_SetField__SWIG_6(this.swigCPtr, this, id, year, month, day, hour, minute, second, tzflag);
/*     */   }
/*     */ 
/*     */   public void SetField(String name, int year, int month, int day, int hour, int minute, int second, int tzflag) {
/* 225 */     ogrJNI.Feature_SetField__SWIG_7(this.swigCPtr, this, name, year, month, day, hour, minute, second, tzflag);
/*     */   }
/*     */ 
/*     */   public void SetFieldIntegerList(int id, int[] nList) {
/* 229 */     ogrJNI.Feature_SetFieldIntegerList(this.swigCPtr, this, id, nList);
/*     */   }
/*     */ 
/*     */   public void SetFieldDoubleList(int id, double[] nList) {
/* 233 */     ogrJNI.Feature_SetFieldDoubleList(this.swigCPtr, this, id, nList);
/*     */   }
/*     */ 
/*     */   public void SetFieldStringList(int id, Vector pList) {
/* 237 */     ogrJNI.Feature_SetFieldStringList(this.swigCPtr, this, id, pList);
/*     */   }
/*     */ 
/*     */   public int SetFrom(Feature other, int forgiving) {
/* 241 */     return ogrJNI.Feature_SetFrom__SWIG_0(this.swigCPtr, this, getCPtr(other), other, forgiving);
/*     */   }
/*     */ 
/*     */   public int SetFrom(Feature other) {
/* 245 */     return ogrJNI.Feature_SetFrom__SWIG_1(this.swigCPtr, this, getCPtr(other), other);
/*     */   }
/*     */ 
/*     */   public int SetFromWithMap(Feature other, int forgiving, int[] nList) {
/* 249 */     return ogrJNI.Feature_SetFromWithMap(this.swigCPtr, this, getCPtr(other), other, forgiving, nList);
/*     */   }
/*     */ 
/*     */   public String GetStyleString() {
/* 253 */     return ogrJNI.Feature_GetStyleString(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public void SetStyleString(String the_string) {
/* 257 */     ogrJNI.Feature_SetStyleString(this.swigCPtr, this, the_string);
/*     */   }
/*     */ 
/*     */   public int GetFieldType(int id) {
/* 261 */     return ogrJNI.Feature_GetFieldType__SWIG_0(this.swigCPtr, this, id);
/*     */   }
/*     */ 
/*     */   public int GetFieldType(String name) {
/* 265 */     return ogrJNI.Feature_GetFieldType__SWIG_1(this.swigCPtr, this, name);
/*     */   }
/*     */ }

/* Location:           E:\temp\1117\gdal.jar
 * Qualified Name:     org.gdal.ogr.Feature
 * JD-Core Version:    0.5.4
 */