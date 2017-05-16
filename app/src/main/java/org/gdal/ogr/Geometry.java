/*     */ package org.gdal.ogr;
/*     */ 
/*     */ import java.util.Vector;
/*     */ import org.gdal.osr.CoordinateTransformation;
/*     */ import org.gdal.osr.SpatialReference;
/*     */ 
/*     */ public class Geometry
/*     */   implements Cloneable
/*     */ {
/*     */   private long swigCPtr;
/*     */   private GeometryNative nativeObject;
/*     */   private Object parentReference;
/*     */ 
/*     */   protected Geometry(long cPtr, boolean cMemoryOwn)
/*     */   {
/*  21 */     if (cPtr == 0L)
/*  22 */       throw new RuntimeException();
/*  23 */     this.swigCPtr = cPtr;
/*  24 */     if (cMemoryOwn)
/*  25 */       this.nativeObject = new GeometryNative(this, cPtr);
/*     */   }
/*     */ 
/*     */   protected static long getCPtr(Geometry obj) {
/*  29 */     return (obj == null) ? 0L : obj.swigCPtr;
/*     */   }
/*     */ 
/*     */   public void delete()
/*     */   {
/*  34 */     if (this.nativeObject == null)
/*     */       return;
/*  36 */     this.nativeObject.delete();
/*  37 */     this.nativeObject = null;
/*     */   }
/*     */ 
/*     */   protected static long getCPtrAndDisown(Geometry obj)
/*     */   {
/*  45 */     if (obj != null)
/*     */     {
/*  47 */       if (obj.nativeObject == null)
/*  48 */         throw new RuntimeException("Cannot disown an object that was not owned...");
/*  49 */       obj.nativeObject.dontDisposeNativeResources();
/*  50 */       obj.nativeObject = null;
/*  51 */       obj.parentReference = null;
/*     */     }
/*  53 */     return getCPtr(obj);
/*     */   }
/*     */ 
/*     */   protected void addReference(Object reference)
/*     */   {
/*  58 */     this.parentReference = reference;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object obj) {
/*  62 */     boolean equal = false;
/*  63 */     if (obj instanceof Geometry)
/*  64 */       equal = Equal((Geometry)obj);
/*  65 */     return equal;
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/*  69 */     return (int)this.swigCPtr;
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*  74 */     return Clone();
/*     */   }
/*     */ 
/*     */   public double[] GetPoint_2D(int iPoint)
/*     */   {
/*  79 */     double[] coords = new double[2];
/*  80 */     GetPoint_2D(iPoint, coords);
/*  81 */     return coords;
/*     */   }
/*     */ 
/*     */   public double[] GetPoint(int iPoint)
/*     */   {
/*  86 */     double[] coords = new double[3];
/*  87 */     GetPoint(iPoint, coords);
/*  88 */     return coords;
/*     */   }
/*     */ 
/*     */   public static Geometry CreateFromWkt(String wkt)
/*     */   {
/*  93 */     return ogr.CreateGeometryFromWkt(wkt);
/*     */   }
/*     */ 
/*     */   public static Geometry CreateFromWkb(byte[] wkb)
/*     */   {
/*  98 */     return ogr.CreateGeometryFromWkb(wkb);
/*     */   }
/*     */ 
/*     */   public static Geometry CreateFromGML(String gml)
/*     */   {
/* 103 */     return ogr.CreateGeometryFromGML(gml);
/*     */   }
/*     */ 
/*     */   public static Geometry CreateFromJson(String json)
/*     */   {
/* 108 */     return ogr.CreateGeometryFromJson(json);
/*     */   }
/*     */ 
/*     */   public int ExportToWkb(byte[] wkbArray, int byte_order)
/*     */   {
/* 113 */     if (wkbArray == null)
/* 114 */       throw new NullPointerException();
/* 115 */     byte[] srcArray = ExportToWkb(byte_order);
/* 116 */     if (wkbArray.length < srcArray.length) {
/* 117 */       throw new RuntimeException("Array too small");
/*     */     }
/* 119 */     System.arraycopy(srcArray, 0, wkbArray, 0, srcArray.length);
/*     */ 
/* 121 */     return 0;
/*     */   }
/*     */ 
/*     */   public int ExportToWkt(String[] argout)
/*     */   {
/* 126 */     return ogrJNI.Geometry_ExportToWkt__SWIG_0(this.swigCPtr, this, argout);
/*     */   }
/*     */ 
/*     */   public byte[] ExportToWkb(int byte_order) {
/* 130 */     return ogrJNI.Geometry_ExportToWkb__SWIG_0(this.swigCPtr, this, byte_order);
/*     */   }
/*     */ 
/*     */   public byte[] ExportToWkb() {
/* 134 */     return ogrJNI.Geometry_ExportToWkb__SWIG_1(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public String ExportToGML(Vector options) {
/* 138 */     return ogrJNI.Geometry_ExportToGML__SWIG_0(this.swigCPtr, this, options);
/*     */   }
/*     */ 
/*     */   public String ExportToGML() {
/* 142 */     return ogrJNI.Geometry_ExportToGML__SWIG_1(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public String ExportToKML(String altitude_mode) {
/* 146 */     return ogrJNI.Geometry_ExportToKML__SWIG_0(this.swigCPtr, this, altitude_mode);
/*     */   }
/*     */ 
/*     */   public String ExportToKML() {
/* 150 */     return ogrJNI.Geometry_ExportToKML__SWIG_1(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public String ExportToJson(Vector options) {
/* 154 */     return ogrJNI.Geometry_ExportToJson__SWIG_0(this.swigCPtr, this, options);
/*     */   }
/*     */ 
/*     */   public String ExportToJson() {
/* 158 */     return ogrJNI.Geometry_ExportToJson__SWIG_1(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public void AddPoint(double x, double y, double z) {
/* 162 */     ogrJNI.Geometry_AddPoint__SWIG_0(this.swigCPtr, this, x, y, z);
/*     */   }
/*     */ 
/*     */   public void AddPoint(double x, double y) {
/* 166 */     ogrJNI.Geometry_AddPoint__SWIG_1(this.swigCPtr, this, x, y);
/*     */   }
/*     */ 
/*     */   public void AddPoint_2D(double x, double y) {
/* 170 */     ogrJNI.Geometry_AddPoint_2D(this.swigCPtr, this, x, y);
/*     */   }
/*     */ 
/*     */   public int AddGeometryDirectly(Geometry other_disown) {
/* 174 */     int ret = ogrJNI.Geometry_AddGeometryDirectly(this.swigCPtr, this, getCPtrAndDisown(other_disown), other_disown);
/* 175 */     if (other_disown != null)
/* 176 */       other_disown.addReference(this);
/* 177 */     return ret;
/*     */   }
/*     */ 
/*     */   public int AddGeometry(Geometry other) {
/* 181 */     return ogrJNI.Geometry_AddGeometry(this.swigCPtr, this, getCPtr(other), other);
/*     */   }
/*     */ 
/*     */   public Geometry Clone() {
/* 185 */     long cPtr = ogrJNI.Geometry_Clone(this.swigCPtr, this);
/* 186 */     return (cPtr == 0L) ? null : new Geometry(cPtr, true);
/*     */   }
/*     */ 
/*     */   public int GetGeometryType() {
/* 190 */     return ogrJNI.Geometry_GetGeometryType(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public String GetGeometryName() {
/* 194 */     return ogrJNI.Geometry_GetGeometryName(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public double Length() {
/* 198 */     return ogrJNI.Geometry_Length(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public double Area() {
/* 202 */     return ogrJNI.Geometry_Area(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public double GetArea() {
/* 206 */     return ogrJNI.Geometry_GetArea(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public int GetPointCount() {
/* 210 */     return ogrJNI.Geometry_GetPointCount(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public double[][] GetPoints(int nCoordDimension) {
/* 214 */     return ogrJNI.Geometry_GetPoints__SWIG_0(this.swigCPtr, this, nCoordDimension);
/*     */   }
/*     */ 
/*     */   public double[][] GetPoints() {
/* 218 */     return ogrJNI.Geometry_GetPoints__SWIG_1(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public double GetX(int point) {
/* 222 */     return ogrJNI.Geometry_GetX__SWIG_0(this.swigCPtr, this, point);
/*     */   }
/*     */ 
/*     */   public double GetX() {
/* 226 */     return ogrJNI.Geometry_GetX__SWIG_1(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public double GetY(int point) {
/* 230 */     return ogrJNI.Geometry_GetY__SWIG_0(this.swigCPtr, this, point);
/*     */   }
/*     */ 
/*     */   public double GetY() {
/* 234 */     return ogrJNI.Geometry_GetY__SWIG_1(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public double GetZ(int point) {
/* 238 */     return ogrJNI.Geometry_GetZ__SWIG_0(this.swigCPtr, this, point);
/*     */   }
/*     */ 
/*     */   public double GetZ() {
/* 242 */     return ogrJNI.Geometry_GetZ__SWIG_1(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public void GetPoint(int iPoint, double[] argout) {
/* 246 */     ogrJNI.Geometry_GetPoint(this.swigCPtr, this, iPoint, argout);
/*     */   }
/*     */ 
/*     */   public void GetPoint_2D(int iPoint, double[] argout) {
/* 250 */     ogrJNI.Geometry_GetPoint_2D(this.swigCPtr, this, iPoint, argout);
/*     */   }
/*     */ 
/*     */   public int GetGeometryCount() {
/* 254 */     return ogrJNI.Geometry_GetGeometryCount(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public void SetPoint(int point, double x, double y, double z) {
/* 258 */     ogrJNI.Geometry_SetPoint__SWIG_0(this.swigCPtr, this, point, x, y, z);
/*     */   }
/*     */ 
/*     */   public void SetPoint(int point, double x, double y) {
/* 262 */     ogrJNI.Geometry_SetPoint__SWIG_1(this.swigCPtr, this, point, x, y);
/*     */   }
/*     */ 
/*     */   public void SetPoint_2D(int point, double x, double y) {
/* 266 */     ogrJNI.Geometry_SetPoint_2D(this.swigCPtr, this, point, x, y);
/*     */   }
/*     */ 
/*     */   public Geometry GetGeometryRef(int geom) {
/* 270 */     long cPtr = ogrJNI.Geometry_GetGeometryRef(this.swigCPtr, this, geom);
/* 271 */     Geometry ret = null;
/* 272 */     if (cPtr != 0L) {
/* 273 */       ret = new Geometry(cPtr, false);
/* 274 */       ret.addReference(this);
/*     */     }
/* 276 */     return ret;
/*     */   }
/*     */ 
/*     */   public Geometry Simplify(double tolerance) {
/* 280 */     long cPtr = ogrJNI.Geometry_Simplify(this.swigCPtr, this, tolerance);
/* 281 */     return (cPtr == 0L) ? null : new Geometry(cPtr, true);
/*     */   }
/*     */ 
/*     */   public Geometry SimplifyPreserveTopology(double tolerance) {
/* 285 */     long cPtr = ogrJNI.Geometry_SimplifyPreserveTopology(this.swigCPtr, this, tolerance);
/* 286 */     return (cPtr == 0L) ? null : new Geometry(cPtr, true);
/*     */   }
/*     */ 
/*     */   public Geometry Boundary() {
/* 290 */     long cPtr = ogrJNI.Geometry_Boundary(this.swigCPtr, this);
/* 291 */     return (cPtr == 0L) ? null : new Geometry(cPtr, true);
/*     */   }
/*     */ 
/*     */   public Geometry GetBoundary() {
/* 295 */     long cPtr = ogrJNI.Geometry_GetBoundary(this.swigCPtr, this);
/* 296 */     return (cPtr == 0L) ? null : new Geometry(cPtr, true);
/*     */   }
/*     */ 
/*     */   public Geometry ConvexHull() {
/* 300 */     long cPtr = ogrJNI.Geometry_ConvexHull(this.swigCPtr, this);
/* 301 */     return (cPtr == 0L) ? null : new Geometry(cPtr, true);
/*     */   }
/*     */ 
/*     */   public Geometry Buffer(double distance, int quadsecs) {
/* 305 */     long cPtr = ogrJNI.Geometry_Buffer__SWIG_0(this.swigCPtr, this, distance, quadsecs);
/* 306 */     return (cPtr == 0L) ? null : new Geometry(cPtr, true);
/*     */   }
/*     */ 
/*     */   public Geometry Buffer(double distance) {
/* 310 */     long cPtr = ogrJNI.Geometry_Buffer__SWIG_1(this.swigCPtr, this, distance);
/* 311 */     return (cPtr == 0L) ? null : new Geometry(cPtr, true);
/*     */   }
/*     */ 
/*     */   public Geometry Intersection(Geometry other) {
/* 315 */     long cPtr = ogrJNI.Geometry_Intersection(this.swigCPtr, this, getCPtr(other), other);
/* 316 */     return (cPtr == 0L) ? null : new Geometry(cPtr, true);
/*     */   }
/*     */ 
/*     */   public Geometry Union(Geometry other) {
/* 320 */     long cPtr = ogrJNI.Geometry_Union(this.swigCPtr, this, getCPtr(other), other);
/* 321 */     return (cPtr == 0L) ? null : new Geometry(cPtr, true);
/*     */   }
/*     */ 
/*     */   public Geometry UnionCascaded() {
/* 325 */     long cPtr = ogrJNI.Geometry_UnionCascaded(this.swigCPtr, this);
/* 326 */     return (cPtr == 0L) ? null : new Geometry(cPtr, true);
/*     */   }
/*     */ 
/*     */   public Geometry Difference(Geometry other) {
/* 330 */     long cPtr = ogrJNI.Geometry_Difference(this.swigCPtr, this, getCPtr(other), other);
/* 331 */     return (cPtr == 0L) ? null : new Geometry(cPtr, true);
/*     */   }
/*     */ 
/*     */   public Geometry SymDifference(Geometry other) {
/* 335 */     long cPtr = ogrJNI.Geometry_SymDifference(this.swigCPtr, this, getCPtr(other), other);
/* 336 */     return (cPtr == 0L) ? null : new Geometry(cPtr, true);
/*     */   }
/*     */ 
/*     */   public Geometry SymmetricDifference(Geometry other) {
/* 340 */     long cPtr = ogrJNI.Geometry_SymmetricDifference(this.swigCPtr, this, getCPtr(other), other);
/* 341 */     return (cPtr == 0L) ? null : new Geometry(cPtr, true);
/*     */   }
/*     */ 
/*     */   public double Distance(Geometry other) {
/* 345 */     return ogrJNI.Geometry_Distance(this.swigCPtr, this, getCPtr(other), other);
/*     */   }
/*     */ 
/*     */   public void Empty() {
/* 349 */     ogrJNI.Geometry_Empty(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public boolean IsEmpty() {
/* 353 */     return ogrJNI.Geometry_IsEmpty(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public boolean IsValid() {
/* 357 */     return ogrJNI.Geometry_IsValid(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public boolean IsSimple() {
/* 361 */     return ogrJNI.Geometry_IsSimple(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public boolean IsRing() {
/* 365 */     return ogrJNI.Geometry_IsRing(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public boolean Intersects(Geometry other) {
/* 369 */     return ogrJNI.Geometry_Intersects(this.swigCPtr, this, getCPtr(other), other);
/*     */   }
/*     */ 
/*     */   public boolean Intersect(Geometry other) {
/* 373 */     return ogrJNI.Geometry_Intersect(this.swigCPtr, this, getCPtr(other), other);
/*     */   }
/*     */ 
/*     */   public boolean Equals(Geometry other) {
/* 377 */     return ogrJNI.Geometry_Equals(this.swigCPtr, this, getCPtr(other), other);
/*     */   }
/*     */ 
/*     */   public boolean Equal(Geometry other) {
/* 381 */     return ogrJNI.Geometry_Equal(this.swigCPtr, this, getCPtr(other), other);
/*     */   }
/*     */ 
/*     */   public boolean Disjoint(Geometry other) {
/* 385 */     return ogrJNI.Geometry_Disjoint(this.swigCPtr, this, getCPtr(other), other);
/*     */   }
/*     */ 
/*     */   public boolean Touches(Geometry other) {
/* 389 */     return ogrJNI.Geometry_Touches(this.swigCPtr, this, getCPtr(other), other);
/*     */   }
/*     */ 
/*     */   public boolean Crosses(Geometry other) {
/* 393 */     return ogrJNI.Geometry_Crosses(this.swigCPtr, this, getCPtr(other), other);
/*     */   }
/*     */ 
/*     */   public boolean Within(Geometry other) {
/* 397 */     return ogrJNI.Geometry_Within(this.swigCPtr, this, getCPtr(other), other);
/*     */   }
/*     */ 
/*     */   public boolean Contains(Geometry other) {
/* 401 */     return ogrJNI.Geometry_Contains(this.swigCPtr, this, getCPtr(other), other);
/*     */   }
/*     */ 
/*     */   public boolean Overlaps(Geometry other) {
/* 405 */     return ogrJNI.Geometry_Overlaps(this.swigCPtr, this, getCPtr(other), other);
/*     */   }
/*     */ 
/*     */   public int TransformTo(SpatialReference reference) {
/* 409 */     return ogrJNI.Geometry_TransformTo(this.swigCPtr, this, SpatialReference.getCPtr(reference), reference);
/*     */   }
/*     */ 
/*     */   public int Transform(CoordinateTransformation trans) {
/* 413 */     return ogrJNI.Geometry_Transform(this.swigCPtr, this, CoordinateTransformation.getCPtr(trans), trans);
/*     */   }
/*     */ 
/*     */   public SpatialReference GetSpatialReference() {
/* 417 */     long cPtr = ogrJNI.Geometry_GetSpatialReference(this.swigCPtr, this);
/* 418 */     return (cPtr == 0L) ? null : new SpatialReference(cPtr, true);
/*     */   }
/*     */ 
/*     */   public void AssignSpatialReference(SpatialReference reference) {
/* 422 */     ogrJNI.Geometry_AssignSpatialReference(this.swigCPtr, this, SpatialReference.getCPtr(reference), reference);
/*     */   }
/*     */ 
/*     */   public void CloseRings() {
/* 426 */     ogrJNI.Geometry_CloseRings(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public void FlattenTo2D() {
/* 430 */     ogrJNI.Geometry_FlattenTo2D(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public void Segmentize(double dfMaxLength) {
/* 434 */     ogrJNI.Geometry_Segmentize(this.swigCPtr, this, dfMaxLength);
/*     */   }
/*     */ 
/*     */   public void GetEnvelope(double[] argout) {
/* 438 */     ogrJNI.Geometry_GetEnvelope(this.swigCPtr, this, argout);
/*     */   }
/*     */ 
/*     */   public void GetEnvelope3D(double[] argout) {
/* 442 */     ogrJNI.Geometry_GetEnvelope3D(this.swigCPtr, this, argout);
/*     */   }
/*     */ 
/*     */   public int WkbSize() {
/* 446 */     return ogrJNI.Geometry_WkbSize(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public int GetCoordinateDimension() {
/* 450 */     return ogrJNI.Geometry_GetCoordinateDimension(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public void SetCoordinateDimension(int dimension) {
/* 454 */     ogrJNI.Geometry_SetCoordinateDimension(this.swigCPtr, this, dimension);
/*     */   }
/*     */ 
/*     */   public int GetDimension() {
/* 458 */     return ogrJNI.Geometry_GetDimension(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public Geometry(int type, String wkt, byte[] nLen, String gml) {
/* 462 */     this(ogrJNI.new_Geometry__SWIG_0(type, wkt, nLen, gml), true);
/*     */   }
/*     */ 
/*     */   public Geometry(int type) {
/* 466 */     this(ogrJNI.new_Geometry__SWIG_1(type), true);
/*     */   }
/*     */ 
/*     */   public String ExportToWkt() {
/* 470 */     return ogrJNI.Geometry_ExportToWkt__SWIG_1(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public Geometry Centroid() {
/* 474 */     long cPtr = ogrJNI.Geometry_Centroid(this.swigCPtr, this);
/* 475 */     return (cPtr == 0L) ? null : new Geometry(cPtr, true);
/*     */   }
/*     */ }

/* Location:           E:\temp\1117\gdal.jar
 * Qualified Name:     org.gdal.ogr.Geometry
 * JD-Core Version:    0.5.4
 */