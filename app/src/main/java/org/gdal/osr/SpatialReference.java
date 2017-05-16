/*     */ package org.gdal.osr;
/*     */ 
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class SpatialReference
/*     */   implements Cloneable
/*     */ {
/*     */   private long swigCPtr;
/*     */   protected boolean swigCMemOwn;
/*     */ 
/*     */   public SpatialReference(long cPtr, boolean cMemoryOwn)
/*     */   {
/*  16 */     this.swigCMemOwn = cMemoryOwn;
/*  17 */     this.swigCPtr = cPtr;
/*     */   }
/*     */ 
/*     */   public static long getCPtr(SpatialReference obj) {
/*  21 */     return (obj == null) ? 0L : obj.swigCPtr;
/*     */   }
/*     */ 
/*     */   protected void finalize() {
/*  25 */     delete();
/*     */   }
/*     */ 
/*     */   public synchronized void delete() {
/*  29 */     if (this.swigCPtr != 0L) {
/*  30 */       if (this.swigCMemOwn) {
/*  31 */         this.swigCMemOwn = false;
/*  32 */         osrJNI.delete_SpatialReference(this.swigCPtr);
/*     */       }
/*  34 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean equals(Object obj) {
/*  39 */     boolean equal = false;
/*  40 */     if (obj instanceof SpatialReference)
/*  41 */       equal = ((SpatialReference)obj).swigCPtr == this.swigCPtr;
/*  42 */     return equal;
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*  47 */     return Clone();
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/*  51 */     return (int)this.swigCPtr;
/*     */   }
/*     */ 
/*     */   public String toString() {
/*  55 */     return __str__();
/*     */   }
/*     */ 
/*     */   public String ExportToWkt() {
/*  59 */     String[] array = { null };
/*  60 */     ExportToWkt(array);
/*  61 */     return array[0];
/*     */   }
/*     */ 
/*     */   public String ExportToPrettyWkt(int simplify) {
/*  65 */     String[] array = { null };
/*  66 */     ExportToPrettyWkt(array, simplify);
/*  67 */     return array[0];
/*     */   }
/*     */ 
/*     */   public String ExportToPrettyWkt() {
/*  71 */     String[] array = { null };
/*  72 */     ExportToPrettyWkt(array);
/*  73 */     return array[0];
/*     */   }
/*     */ 
/*     */   public String ExportToProj4() {
/*  77 */     String[] array = { null };
/*  78 */     ExportToProj4(array);
/*  79 */     return array[0];
/*     */   }
/*     */ 
/*     */   public String ExportToXML(String dialect) {
/*  83 */     String[] array = { null };
/*  84 */     ExportToXML(array, dialect);
/*  85 */     return array[0];
/*     */   }
/*     */ 
/*     */   public String ExportToXML() {
/*  89 */     String[] array = { null };
/*  90 */     ExportToXML(array);
/*  91 */     return array[0];
/*     */   }
/*     */ 
/*     */   public String ExportToMICoordSys() {
/*  95 */     String[] array = { null };
/*  96 */     ExportToMICoordSys(array);
/*  97 */     return array[0];
/*     */   }
/*     */ 
/*     */   public double[] GetTOWGS84()
/*     */   {
/* 102 */     double[] array = new double[7];
/* 103 */     GetTOWGS84(array);
/* 104 */     return array;
/*     */   }
/*     */ 
/*     */   public int SetTOWGS84(double p1, double p2, double p3)
/*     */   {
/* 109 */     return SetTOWGS84(p1, p2, p3, 0.0D, 0.0D, 0.0D, 0.0D);
/*     */   }
/*     */ 
/*     */   public SpatialReference(String wkt) {
/* 113 */     this(osrJNI.new_SpatialReference__SWIG_0(wkt), true);
/*     */   }
/*     */ 
/*     */   public SpatialReference() {
/* 117 */     this(osrJNI.new_SpatialReference__SWIG_1(), true);
/*     */   }
/*     */ 
/*     */   public String __str__() {
/* 121 */     return osrJNI.SpatialReference___str__(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public int IsSame(SpatialReference rhs) {
/* 125 */     return osrJNI.SpatialReference_IsSame(this.swigCPtr, this, getCPtr(rhs), rhs);
/*     */   }
/*     */ 
/*     */   public int IsSameGeogCS(SpatialReference rhs) {
/* 129 */     return osrJNI.SpatialReference_IsSameGeogCS(this.swigCPtr, this, getCPtr(rhs), rhs);
/*     */   }
/*     */ 
/*     */   public int IsSameVertCS(SpatialReference rhs) {
/* 133 */     return osrJNI.SpatialReference_IsSameVertCS(this.swigCPtr, this, getCPtr(rhs), rhs);
/*     */   }
/*     */ 
/*     */   public int IsGeographic() {
/* 137 */     return osrJNI.SpatialReference_IsGeographic(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public int IsProjected() {
/* 141 */     return osrJNI.SpatialReference_IsProjected(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public int IsCompound() {
/* 145 */     return osrJNI.SpatialReference_IsCompound(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public int IsGeocentric() {
/* 149 */     return osrJNI.SpatialReference_IsGeocentric(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public int IsLocal() {
/* 153 */     return osrJNI.SpatialReference_IsLocal(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public int IsVertical() {
/* 157 */     return osrJNI.SpatialReference_IsVertical(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public int EPSGTreatsAsLatLong() {
/* 161 */     return osrJNI.SpatialReference_EPSGTreatsAsLatLong(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public int SetAuthority(String pszTargetKey, String pszAuthority, int nCode) {
/* 165 */     return osrJNI.SpatialReference_SetAuthority(this.swigCPtr, this, pszTargetKey, pszAuthority, nCode);
/*     */   }
/*     */ 
/*     */   public String GetAttrValue(String name, int child) {
/* 169 */     return osrJNI.SpatialReference_GetAttrValue__SWIG_0(this.swigCPtr, this, name, child);
/*     */   }
/*     */ 
/*     */   public String GetAttrValue(String name) {
/* 173 */     return osrJNI.SpatialReference_GetAttrValue__SWIG_1(this.swigCPtr, this, name);
/*     */   }
/*     */ 
/*     */   public int SetAttrValue(String name, String value) {
/* 177 */     return osrJNI.SpatialReference_SetAttrValue(this.swigCPtr, this, name, value);
/*     */   }
/*     */ 
/*     */   public int SetAngularUnits(String name, double to_radians) {
/* 181 */     return osrJNI.SpatialReference_SetAngularUnits(this.swigCPtr, this, name, to_radians);
/*     */   }
/*     */ 
/*     */   public double GetAngularUnits() {
/* 185 */     return osrJNI.SpatialReference_GetAngularUnits(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public int SetTargetLinearUnits(String target, String name, double to_meters) {
/* 189 */     return osrJNI.SpatialReference_SetTargetLinearUnits(this.swigCPtr, this, target, name, to_meters);
/*     */   }
/*     */ 
/*     */   public int SetLinearUnits(String name, double to_meters) {
/* 193 */     return osrJNI.SpatialReference_SetLinearUnits(this.swigCPtr, this, name, to_meters);
/*     */   }
/*     */ 
/*     */   public int SetLinearUnitsAndUpdateParameters(String name, double to_meters) {
/* 197 */     return osrJNI.SpatialReference_SetLinearUnitsAndUpdateParameters(this.swigCPtr, this, name, to_meters);
/*     */   }
/*     */ 
/*     */   public double GetLinearUnits() {
/* 201 */     return osrJNI.SpatialReference_GetLinearUnits(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public String GetLinearUnitsName() {
/* 205 */     return osrJNI.SpatialReference_GetLinearUnitsName(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public String GetAuthorityCode(String target_key) {
/* 209 */     return osrJNI.SpatialReference_GetAuthorityCode(this.swigCPtr, this, target_key);
/*     */   }
/*     */ 
/*     */   public String GetAuthorityName(String target_key) {
/* 213 */     return osrJNI.SpatialReference_GetAuthorityName(this.swigCPtr, this, target_key);
/*     */   }
/*     */ 
/*     */   public int SetUTM(int zone, int north) {
/* 217 */     return osrJNI.SpatialReference_SetUTM__SWIG_0(this.swigCPtr, this, zone, north);
/*     */   }
/*     */ 
/*     */   public int SetUTM(int zone) {
/* 221 */     return osrJNI.SpatialReference_SetUTM__SWIG_1(this.swigCPtr, this, zone);
/*     */   }
/*     */ 
/*     */   public int GetUTMZone() {
/* 225 */     return osrJNI.SpatialReference_GetUTMZone(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public int SetStatePlane(int zone, int is_nad83, String unitsname, double units) {
/* 229 */     return osrJNI.SpatialReference_SetStatePlane__SWIG_0(this.swigCPtr, this, zone, is_nad83, unitsname, units);
/*     */   }
/*     */ 
/*     */   public int SetStatePlane(int zone, int is_nad83, String unitsname) {
/* 233 */     return osrJNI.SpatialReference_SetStatePlane__SWIG_1(this.swigCPtr, this, zone, is_nad83, unitsname);
/*     */   }
/*     */ 
/*     */   public int SetStatePlane(int zone, int is_nad83) {
/* 237 */     return osrJNI.SpatialReference_SetStatePlane__SWIG_2(this.swigCPtr, this, zone, is_nad83);
/*     */   }
/*     */ 
/*     */   public int SetStatePlane(int zone) {
/* 241 */     return osrJNI.SpatialReference_SetStatePlane__SWIG_3(this.swigCPtr, this, zone);
/*     */   }
/*     */ 
/*     */   public int AutoIdentifyEPSG() {
/* 245 */     return osrJNI.SpatialReference_AutoIdentifyEPSG(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public int SetProjection(String arg) {
/* 249 */     return osrJNI.SpatialReference_SetProjection(this.swigCPtr, this, arg);
/*     */   }
/*     */ 
/*     */   public int SetProjParm(String name, double val) {
/* 253 */     return osrJNI.SpatialReference_SetProjParm(this.swigCPtr, this, name, val);
/*     */   }
/*     */ 
/*     */   public double GetProjParm(String name, double default_val) {
/* 257 */     return osrJNI.SpatialReference_GetProjParm__SWIG_0(this.swigCPtr, this, name, default_val);
/*     */   }
/*     */ 
/*     */   public double GetProjParm(String name) {
/* 261 */     return osrJNI.SpatialReference_GetProjParm__SWIG_1(this.swigCPtr, this, name);
/*     */   }
/*     */ 
/*     */   public int SetNormProjParm(String name, double val) {
/* 265 */     return osrJNI.SpatialReference_SetNormProjParm(this.swigCPtr, this, name, val);
/*     */   }
/*     */ 
/*     */   public double GetNormProjParm(String name, double default_val) {
/* 269 */     return osrJNI.SpatialReference_GetNormProjParm__SWIG_0(this.swigCPtr, this, name, default_val);
/*     */   }
/*     */ 
/*     */   public double GetNormProjParm(String name) {
/* 273 */     return osrJNI.SpatialReference_GetNormProjParm__SWIG_1(this.swigCPtr, this, name);
/*     */   }
/*     */ 
/*     */   public double GetSemiMajor() {
/* 277 */     return osrJNI.SpatialReference_GetSemiMajor(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public double GetSemiMinor() {
/* 281 */     return osrJNI.SpatialReference_GetSemiMinor(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public double GetInvFlattening() {
/* 285 */     return osrJNI.SpatialReference_GetInvFlattening(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public int SetACEA(double stdp1, double stdp2, double clat, double clong, double fe, double fn) {
/* 289 */     return osrJNI.SpatialReference_SetACEA(this.swigCPtr, this, stdp1, stdp2, clat, clong, fe, fn);
/*     */   }
/*     */ 
/*     */   public int SetAE(double clat, double clong, double fe, double fn) {
/* 293 */     return osrJNI.SpatialReference_SetAE(this.swigCPtr, this, clat, clong, fe, fn);
/*     */   }
/*     */ 
/*     */   public int SetBonne(double stdp, double cm, double fe, double fn) {
/* 297 */     return osrJNI.SpatialReference_SetBonne(this.swigCPtr, this, stdp, cm, fe, fn);
/*     */   }
/*     */ 
/*     */   public int SetCEA(double stdp1, double cm, double fe, double fn) {
/* 301 */     return osrJNI.SpatialReference_SetCEA(this.swigCPtr, this, stdp1, cm, fe, fn);
/*     */   }
/*     */ 
/*     */   public int SetCS(double clat, double clong, double fe, double fn) {
/* 305 */     return osrJNI.SpatialReference_SetCS(this.swigCPtr, this, clat, clong, fe, fn);
/*     */   }
/*     */ 
/*     */   public int SetEC(double stdp1, double stdp2, double clat, double clong, double fe, double fn) {
/* 309 */     return osrJNI.SpatialReference_SetEC(this.swigCPtr, this, stdp1, stdp2, clat, clong, fe, fn);
/*     */   }
/*     */ 
/*     */   public int SetEckertIV(double cm, double fe, double fn) {
/* 313 */     return osrJNI.SpatialReference_SetEckertIV(this.swigCPtr, this, cm, fe, fn);
/*     */   }
/*     */ 
/*     */   public int SetEckertVI(double cm, double fe, double fn) {
/* 317 */     return osrJNI.SpatialReference_SetEckertVI(this.swigCPtr, this, cm, fe, fn);
/*     */   }
/*     */ 
/*     */   public int SetEquirectangular(double clat, double clong, double fe, double fn) {
/* 321 */     return osrJNI.SpatialReference_SetEquirectangular(this.swigCPtr, this, clat, clong, fe, fn);
/*     */   }
/*     */ 
/*     */   public int SetEquirectangular2(double clat, double clong, double pseudostdparallellat, double fe, double fn) {
/* 325 */     return osrJNI.SpatialReference_SetEquirectangular2(this.swigCPtr, this, clat, clong, pseudostdparallellat, fe, fn);
/*     */   }
/*     */ 
/*     */   public int SetGaussSchreiberTMercator(double clat, double clong, double sc, double fe, double fn) {
/* 329 */     return osrJNI.SpatialReference_SetGaussSchreiberTMercator(this.swigCPtr, this, clat, clong, sc, fe, fn);
/*     */   }
/*     */ 
/*     */   public int SetGS(double cm, double fe, double fn) {
/* 333 */     return osrJNI.SpatialReference_SetGS(this.swigCPtr, this, cm, fe, fn);
/*     */   }
/*     */ 
/*     */   public int SetGH(double cm, double fe, double fn) {
/* 337 */     return osrJNI.SpatialReference_SetGH(this.swigCPtr, this, cm, fe, fn);
/*     */   }
/*     */ 
/*     */   public int SetIGH() {
/* 341 */     return osrJNI.SpatialReference_SetIGH(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public int SetGEOS(double cm, double satelliteheight, double fe, double fn) {
/* 345 */     return osrJNI.SpatialReference_SetGEOS(this.swigCPtr, this, cm, satelliteheight, fe, fn);
/*     */   }
/*     */ 
/*     */   public int SetGnomonic(double clat, double clong, double fe, double fn) {
/* 349 */     return osrJNI.SpatialReference_SetGnomonic(this.swigCPtr, this, clat, clong, fe, fn);
/*     */   }
/*     */ 
/*     */   public int SetHOM(double clat, double clong, double azimuth, double recttoskew, double scale, double fe, double fn) {
/* 353 */     return osrJNI.SpatialReference_SetHOM(this.swigCPtr, this, clat, clong, azimuth, recttoskew, scale, fe, fn);
/*     */   }
/*     */ 
/*     */   public int SetHOM2PNO(double clat, double dfLat1, double dfLong1, double dfLat2, double dfLong2, double scale, double fe, double fn) {
/* 357 */     return osrJNI.SpatialReference_SetHOM2PNO(this.swigCPtr, this, clat, dfLat1, dfLong1, dfLat2, dfLong2, scale, fe, fn);
/*     */   }
/*     */ 
/*     */   public int SetKrovak(double clat, double clong, double azimuth, double pseudostdparallellat, double scale, double fe, double fn) {
/* 361 */     return osrJNI.SpatialReference_SetKrovak(this.swigCPtr, this, clat, clong, azimuth, pseudostdparallellat, scale, fe, fn);
/*     */   }
/*     */ 
/*     */   public int SetLAEA(double clat, double clong, double fe, double fn) {
/* 365 */     return osrJNI.SpatialReference_SetLAEA(this.swigCPtr, this, clat, clong, fe, fn);
/*     */   }
/*     */ 
/*     */   public int SetLCC(double stdp1, double stdp2, double clat, double clong, double fe, double fn) {
/* 369 */     return osrJNI.SpatialReference_SetLCC(this.swigCPtr, this, stdp1, stdp2, clat, clong, fe, fn);
/*     */   }
/*     */ 
/*     */   public int SetLCC1SP(double clat, double clong, double scale, double fe, double fn) {
/* 373 */     return osrJNI.SpatialReference_SetLCC1SP(this.swigCPtr, this, clat, clong, scale, fe, fn);
/*     */   }
/*     */ 
/*     */   public int SetLCCB(double stdp1, double stdp2, double clat, double clong, double fe, double fn) {
/* 377 */     return osrJNI.SpatialReference_SetLCCB(this.swigCPtr, this, stdp1, stdp2, clat, clong, fe, fn);
/*     */   }
/*     */ 
/*     */   public int SetMC(double clat, double clong, double fe, double fn) {
/* 381 */     return osrJNI.SpatialReference_SetMC(this.swigCPtr, this, clat, clong, fe, fn);
/*     */   }
/*     */ 
/*     */   public int SetMercator(double clat, double clong, double scale, double fe, double fn) {
/* 385 */     return osrJNI.SpatialReference_SetMercator(this.swigCPtr, this, clat, clong, scale, fe, fn);
/*     */   }
/*     */ 
/*     */   public int SetMollweide(double cm, double fe, double fn) {
/* 389 */     return osrJNI.SpatialReference_SetMollweide(this.swigCPtr, this, cm, fe, fn);
/*     */   }
/*     */ 
/*     */   public int SetNZMG(double clat, double clong, double fe, double fn) {
/* 393 */     return osrJNI.SpatialReference_SetNZMG(this.swigCPtr, this, clat, clong, fe, fn);
/*     */   }
/*     */ 
/*     */   public int SetOS(double dfOriginLat, double dfCMeridian, double scale, double fe, double fn) {
/* 397 */     return osrJNI.SpatialReference_SetOS(this.swigCPtr, this, dfOriginLat, dfCMeridian, scale, fe, fn);
/*     */   }
/*     */ 
/*     */   public int SetOrthographic(double clat, double clong, double fe, double fn) {
/* 401 */     return osrJNI.SpatialReference_SetOrthographic(this.swigCPtr, this, clat, clong, fe, fn);
/*     */   }
/*     */ 
/*     */   public int SetPolyconic(double clat, double clong, double fe, double fn) {
/* 405 */     return osrJNI.SpatialReference_SetPolyconic(this.swigCPtr, this, clat, clong, fe, fn);
/*     */   }
/*     */ 
/*     */   public int SetPS(double clat, double clong, double scale, double fe, double fn) {
/* 409 */     return osrJNI.SpatialReference_SetPS(this.swigCPtr, this, clat, clong, scale, fe, fn);
/*     */   }
/*     */ 
/*     */   public int SetRobinson(double clong, double fe, double fn) {
/* 413 */     return osrJNI.SpatialReference_SetRobinson(this.swigCPtr, this, clong, fe, fn);
/*     */   }
/*     */ 
/*     */   public int SetSinusoidal(double clong, double fe, double fn) {
/* 417 */     return osrJNI.SpatialReference_SetSinusoidal(this.swigCPtr, this, clong, fe, fn);
/*     */   }
/*     */ 
/*     */   public int SetStereographic(double clat, double clong, double scale, double fe, double fn) {
/* 421 */     return osrJNI.SpatialReference_SetStereographic(this.swigCPtr, this, clat, clong, scale, fe, fn);
/*     */   }
/*     */ 
/*     */   public int SetSOC(double latitudeoforigin, double cm, double fe, double fn) {
/* 425 */     return osrJNI.SpatialReference_SetSOC(this.swigCPtr, this, latitudeoforigin, cm, fe, fn);
/*     */   }
/*     */ 
/*     */   public int SetTM(double clat, double clong, double scale, double fe, double fn) {
/* 429 */     return osrJNI.SpatialReference_SetTM(this.swigCPtr, this, clat, clong, scale, fe, fn);
/*     */   }
/*     */ 
/*     */   public int SetTMVariant(String pszVariantName, double clat, double clong, double scale, double fe, double fn) {
/* 433 */     return osrJNI.SpatialReference_SetTMVariant(this.swigCPtr, this, pszVariantName, clat, clong, scale, fe, fn);
/*     */   }
/*     */ 
/*     */   public int SetTMG(double clat, double clong, double fe, double fn) {
/* 437 */     return osrJNI.SpatialReference_SetTMG(this.swigCPtr, this, clat, clong, fe, fn);
/*     */   }
/*     */ 
/*     */   public int SetTMSO(double clat, double clong, double scale, double fe, double fn) {
/* 441 */     return osrJNI.SpatialReference_SetTMSO(this.swigCPtr, this, clat, clong, scale, fe, fn);
/*     */   }
/*     */ 
/*     */   public int SetVDG(double clong, double fe, double fn) {
/* 445 */     return osrJNI.SpatialReference_SetVDG(this.swigCPtr, this, clong, fe, fn);
/*     */   }
/*     */ 
/*     */   public int SetWellKnownGeogCS(String name) {
/* 449 */     return osrJNI.SpatialReference_SetWellKnownGeogCS(this.swigCPtr, this, name);
/*     */   }
/*     */ 
/*     */   public int SetFromUserInput(String name) {
/* 453 */     return osrJNI.SpatialReference_SetFromUserInput(this.swigCPtr, this, name);
/*     */   }
/*     */ 
/*     */   public int CopyGeogCSFrom(SpatialReference rhs) {
/* 457 */     return osrJNI.SpatialReference_CopyGeogCSFrom(this.swigCPtr, this, getCPtr(rhs), rhs);
/*     */   }
/*     */ 
/*     */   public int SetTOWGS84(double p1, double p2, double p3, double p4, double p5, double p6, double p7) {
/* 461 */     return osrJNI.SpatialReference_SetTOWGS84(this.swigCPtr, this, p1, p2, p3, p4, p5, p6, p7);
/*     */   }
/*     */ 
/*     */   public int GetTOWGS84(double[] argout) {
/* 465 */     return osrJNI.SpatialReference_GetTOWGS84(this.swigCPtr, this, argout);
/*     */   }
/*     */ 
/*     */   public int SetLocalCS(String pszName) {
/* 469 */     return osrJNI.SpatialReference_SetLocalCS(this.swigCPtr, this, pszName);
/*     */   }
/*     */ 
/*     */   public int SetGeogCS(String pszGeogName, String pszDatumName, String pszEllipsoidName, double dfSemiMajor, double dfInvFlattening, String pszPMName, double dfPMOffset, String pszUnits, double dfConvertToRadians) {
/* 473 */     return osrJNI.SpatialReference_SetGeogCS__SWIG_0(this.swigCPtr, this, pszGeogName, pszDatumName, pszEllipsoidName, dfSemiMajor, dfInvFlattening, pszPMName, dfPMOffset, pszUnits, dfConvertToRadians);
/*     */   }
/*     */ 
/*     */   public int SetGeogCS(String pszGeogName, String pszDatumName, String pszEllipsoidName, double dfSemiMajor, double dfInvFlattening, String pszPMName, double dfPMOffset, String pszUnits) {
/* 477 */     return osrJNI.SpatialReference_SetGeogCS__SWIG_1(this.swigCPtr, this, pszGeogName, pszDatumName, pszEllipsoidName, dfSemiMajor, dfInvFlattening, pszPMName, dfPMOffset, pszUnits);
/*     */   }
/*     */ 
/*     */   public int SetGeogCS(String pszGeogName, String pszDatumName, String pszEllipsoidName, double dfSemiMajor, double dfInvFlattening, String pszPMName, double dfPMOffset) {
/* 481 */     return osrJNI.SpatialReference_SetGeogCS__SWIG_2(this.swigCPtr, this, pszGeogName, pszDatumName, pszEllipsoidName, dfSemiMajor, dfInvFlattening, pszPMName, dfPMOffset);
/*     */   }
/*     */ 
/*     */   public int SetGeogCS(String pszGeogName, String pszDatumName, String pszEllipsoidName, double dfSemiMajor, double dfInvFlattening, String pszPMName) {
/* 485 */     return osrJNI.SpatialReference_SetGeogCS__SWIG_3(this.swigCPtr, this, pszGeogName, pszDatumName, pszEllipsoidName, dfSemiMajor, dfInvFlattening, pszPMName);
/*     */   }
/*     */ 
/*     */   public int SetGeogCS(String pszGeogName, String pszDatumName, String pszEllipsoidName, double dfSemiMajor, double dfInvFlattening) {
/* 489 */     return osrJNI.SpatialReference_SetGeogCS__SWIG_4(this.swigCPtr, this, pszGeogName, pszDatumName, pszEllipsoidName, dfSemiMajor, dfInvFlattening);
/*     */   }
/*     */ 
/*     */   public int SetProjCS(String name) {
/* 493 */     return osrJNI.SpatialReference_SetProjCS__SWIG_0(this.swigCPtr, this, name);
/*     */   }
/*     */ 
/*     */   public int SetProjCS() {
/* 497 */     return osrJNI.SpatialReference_SetProjCS__SWIG_1(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public int SetGeocCS(String name) {
/* 501 */     return osrJNI.SpatialReference_SetGeocCS__SWIG_0(this.swigCPtr, this, name);
/*     */   }
/*     */ 
/*     */   public int SetGeocCS() {
/* 505 */     return osrJNI.SpatialReference_SetGeocCS__SWIG_1(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public int SetVertCS(String VertCSName, String VertDatumName, int VertDatumType) {
/* 509 */     return osrJNI.SpatialReference_SetVertCS__SWIG_0(this.swigCPtr, this, VertCSName, VertDatumName, VertDatumType);
/*     */   }
/*     */ 
/*     */   public int SetVertCS(String VertCSName, String VertDatumName) {
/* 513 */     return osrJNI.SpatialReference_SetVertCS__SWIG_1(this.swigCPtr, this, VertCSName, VertDatumName);
/*     */   }
/*     */ 
/*     */   public int SetVertCS(String VertCSName) {
/* 517 */     return osrJNI.SpatialReference_SetVertCS__SWIG_2(this.swigCPtr, this, VertCSName);
/*     */   }
/*     */ 
/*     */   public int SetVertCS() {
/* 521 */     return osrJNI.SpatialReference_SetVertCS__SWIG_3(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public int SetCompoundCS(String name, SpatialReference horizcs, SpatialReference vertcs) {
/* 525 */     return osrJNI.SpatialReference_SetCompoundCS(this.swigCPtr, this, name, getCPtr(horizcs), horizcs, getCPtr(vertcs), vertcs);
/*     */   }
/*     */ 
/*     */   public int ImportFromWkt(String ppszInput) {
/* 529 */     return osrJNI.SpatialReference_ImportFromWkt(this.swigCPtr, this, ppszInput);
/*     */   }
/*     */ 
/*     */   public int ImportFromProj4(String ppszInput) {
/* 533 */     return osrJNI.SpatialReference_ImportFromProj4(this.swigCPtr, this, ppszInput);
/*     */   }
/*     */ 
/*     */   public int ImportFromUrl(String url) {
/* 537 */     return osrJNI.SpatialReference_ImportFromUrl(this.swigCPtr, this, url);
/*     */   }
/*     */ 
/*     */   public int ImportFromESRI(Vector ppszInput) {
/* 541 */     return osrJNI.SpatialReference_ImportFromESRI(this.swigCPtr, this, ppszInput);
/*     */   }
/*     */ 
/*     */   public int ImportFromEPSG(int arg) {
/* 545 */     return osrJNI.SpatialReference_ImportFromEPSG(this.swigCPtr, this, arg);
/*     */   }
/*     */ 
/*     */   public int ImportFromEPSGA(int arg) {
/* 549 */     return osrJNI.SpatialReference_ImportFromEPSGA(this.swigCPtr, this, arg);
/*     */   }
/*     */ 
/*     */   public int ImportFromPCI(String proj, String units, double[] argin) {
/* 553 */     return osrJNI.SpatialReference_ImportFromPCI__SWIG_0(this.swigCPtr, this, proj, units, argin);
/*     */   }
/*     */ 
/*     */   public int ImportFromPCI(String proj, String units) {
/* 557 */     return osrJNI.SpatialReference_ImportFromPCI__SWIG_1(this.swigCPtr, this, proj, units);
/*     */   }
/*     */ 
/*     */   public int ImportFromPCI(String proj) {
/* 561 */     return osrJNI.SpatialReference_ImportFromPCI__SWIG_2(this.swigCPtr, this, proj);
/*     */   }
/*     */ 
/*     */   public int ImportFromUSGS(int proj_code, int zone, double[] argin, int datum_code) {
/* 565 */     return osrJNI.SpatialReference_ImportFromUSGS__SWIG_0(this.swigCPtr, this, proj_code, zone, argin, datum_code);
/*     */   }
/*     */ 
/*     */   public int ImportFromUSGS(int proj_code, int zone, double[] argin) {
/* 569 */     return osrJNI.SpatialReference_ImportFromUSGS__SWIG_1(this.swigCPtr, this, proj_code, zone, argin);
/*     */   }
/*     */ 
/*     */   public int ImportFromUSGS(int proj_code, int zone) {
/* 573 */     return osrJNI.SpatialReference_ImportFromUSGS__SWIG_2(this.swigCPtr, this, proj_code, zone);
/*     */   }
/*     */ 
/*     */   public int ImportFromUSGS(int proj_code) {
/* 577 */     return osrJNI.SpatialReference_ImportFromUSGS__SWIG_3(this.swigCPtr, this, proj_code);
/*     */   }
/*     */ 
/*     */   public int ImportFromXML(String xmlString) {
/* 581 */     return osrJNI.SpatialReference_ImportFromXML(this.swigCPtr, this, xmlString);
/*     */   }
/*     */ 
/*     */   public int ImportFromERM(String proj, String datum, String units) {
/* 585 */     return osrJNI.SpatialReference_ImportFromERM(this.swigCPtr, this, proj, datum, units);
/*     */   }
/*     */ 
/*     */   public int ImportFromMICoordSys(String pszCoordSys) {
/* 589 */     return osrJNI.SpatialReference_ImportFromMICoordSys(this.swigCPtr, this, pszCoordSys);
/*     */   }
/*     */ 
/*     */   public int ExportToWkt(String[] argout) {
/* 593 */     return osrJNI.SpatialReference_ExportToWkt(this.swigCPtr, this, argout);
/*     */   }
/*     */ 
/*     */   public int ExportToPrettyWkt(String[] argout, int simplify) {
/* 597 */     return osrJNI.SpatialReference_ExportToPrettyWkt__SWIG_0(this.swigCPtr, this, argout, simplify);
/*     */   }
/*     */ 
/*     */   public int ExportToPrettyWkt(String[] argout) {
/* 601 */     return osrJNI.SpatialReference_ExportToPrettyWkt__SWIG_1(this.swigCPtr, this, argout);
/*     */   }
/*     */ 
/*     */   public int ExportToProj4(String[] argout) {
/* 605 */     return osrJNI.SpatialReference_ExportToProj4(this.swigCPtr, this, argout);
/*     */   }
/*     */ 
/*     */   public int ExportToPCI(String[] proj, String[] units, double[] parms) {
/* 609 */     return osrJNI.SpatialReference_ExportToPCI(this.swigCPtr, this, proj, units, parms);
/*     */   }
/*     */ 
/*     */   public int ExportToUSGS(int[] code, int[] zone, double[] parms, int[] datum) {
/* 613 */     return osrJNI.SpatialReference_ExportToUSGS(this.swigCPtr, this, code, zone, parms, datum);
/*     */   }
/*     */ 
/*     */   public int ExportToXML(String[] argout, String dialect) {
/* 617 */     return osrJNI.SpatialReference_ExportToXML__SWIG_0(this.swigCPtr, this, argout, dialect);
/*     */   }
/*     */ 
/*     */   public int ExportToXML(String[] argout) {
/* 621 */     return osrJNI.SpatialReference_ExportToXML__SWIG_1(this.swigCPtr, this, argout);
/*     */   }
/*     */ 
/*     */   public int ExportToMICoordSys(String[] argout) {
/* 625 */     return osrJNI.SpatialReference_ExportToMICoordSys(this.swigCPtr, this, argout);
/*     */   }
/*     */ 
/*     */   public SpatialReference CloneGeogCS() {
/* 629 */     long cPtr = osrJNI.SpatialReference_CloneGeogCS(this.swigCPtr, this);
/* 630 */     return (cPtr == 0L) ? null : new SpatialReference(cPtr, true);
/*     */   }
/*     */ 
/*     */   public SpatialReference Clone() {
/* 634 */     long cPtr = osrJNI.SpatialReference_Clone(this.swigCPtr, this);
/* 635 */     return (cPtr == 0L) ? null : new SpatialReference(cPtr, true);
/*     */   }
/*     */ 
/*     */   public int Validate() {
/* 639 */     return osrJNI.SpatialReference_Validate(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public int StripCTParms() {
/* 643 */     return osrJNI.SpatialReference_StripCTParms(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public int FixupOrdering() {
/* 647 */     return osrJNI.SpatialReference_FixupOrdering(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public int Fixup() {
/* 651 */     return osrJNI.SpatialReference_Fixup(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public int MorphToESRI() {
/* 655 */     return osrJNI.SpatialReference_MorphToESRI(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public int MorphFromESRI() {
/* 659 */     return osrJNI.SpatialReference_MorphFromESRI(this.swigCPtr, this);
/*     */   }
/*     */ }

/* Location:           E:\temp\1117\gdal.jar
 * Qualified Name:     org.gdal.osr.SpatialReference
 * JD-Core Version:    0.5.4
 */