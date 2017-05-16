/*     */ package org.gdal.ogr;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
/*     */ import org.gdal.osr.SpatialReference;
/*     */ 
/*     */ public class ogr
/*     */   implements ogrConstants
/*     */ {
/*     */   public static void UseExceptions()
/*     */   {
/*  15 */     ogrJNI.UseExceptions();
/*     */   }
/*     */ 
/*     */   public static void DontUseExceptions() {
/*  19 */     ogrJNI.DontUseExceptions();
/*     */   }
/*     */ 
/*     */   public static Geometry CreateGeometryFromWkb(byte[] nLen, SpatialReference reference) {
/*  23 */     long cPtr = ogrJNI.CreateGeometryFromWkb__SWIG_0(nLen, SpatialReference.getCPtr(reference), reference);
/*  24 */     return (cPtr == 0L) ? null : new Geometry(cPtr, true);
/*     */   }
/*     */ 
/*     */   public static Geometry CreateGeometryFromWkb(byte[] nLen) {
/*  28 */     long cPtr = ogrJNI.CreateGeometryFromWkb__SWIG_1(nLen);
/*  29 */     return (cPtr == 0L) ? null : new Geometry(cPtr, true);
/*     */   }
/*     */ 
/*     */   public static Geometry CreateGeometryFromWkt(String val, SpatialReference reference) {
/*  33 */     long cPtr = ogrJNI.CreateGeometryFromWkt__SWIG_0(val, SpatialReference.getCPtr(reference), reference);
/*  34 */     return (cPtr == 0L) ? null : new Geometry(cPtr, true);
/*     */   }
/*     */ 
/*     */   public static Geometry CreateGeometryFromWkt(String val) {
/*  38 */     long cPtr = ogrJNI.CreateGeometryFromWkt__SWIG_1(val);
/*  39 */     return (cPtr == 0L) ? null : new Geometry(cPtr, true);
/*     */   }
/*     */ 
/*     */   public static Geometry CreateGeometryFromGML(String input_string) {
/*  43 */     long cPtr = ogrJNI.CreateGeometryFromGML(input_string);
/*  44 */     return (cPtr == 0L) ? null : new Geometry(cPtr, true);
/*     */   }
/*     */ 
/*     */   public static Geometry CreateGeometryFromJson(String input_string) {
/*  48 */     long cPtr = ogrJNI.CreateGeometryFromJson(input_string);
/*  49 */     return (cPtr == 0L) ? null : new Geometry(cPtr, true);
/*     */   }
/*     */ 
/*     */   public static Geometry BuildPolygonFromEdges(Geometry hLineCollection, int bBestEffort, int bAutoClose, double dfTolerance) {
/*  53 */     long cPtr = ogrJNI.BuildPolygonFromEdges__SWIG_0(Geometry.getCPtr(hLineCollection), hLineCollection, bBestEffort, bAutoClose, dfTolerance);
/*  54 */     return (cPtr == 0L) ? null : new Geometry(cPtr, true);
/*     */   }
/*     */ 
/*     */   public static Geometry BuildPolygonFromEdges(Geometry hLineCollection, int bBestEffort, int bAutoClose) {
/*  58 */     long cPtr = ogrJNI.BuildPolygonFromEdges__SWIG_1(Geometry.getCPtr(hLineCollection), hLineCollection, bBestEffort, bAutoClose);
/*  59 */     return (cPtr == 0L) ? null : new Geometry(cPtr, true);
/*     */   }
/*     */ 
/*     */   public static Geometry BuildPolygonFromEdges(Geometry hLineCollection, int bBestEffort) {
/*  63 */     long cPtr = ogrJNI.BuildPolygonFromEdges__SWIG_2(Geometry.getCPtr(hLineCollection), hLineCollection, bBestEffort);
/*  64 */     return (cPtr == 0L) ? null : new Geometry(cPtr, true);
/*     */   }
/*     */ 
/*     */   public static Geometry BuildPolygonFromEdges(Geometry hLineCollection) {
/*  68 */     long cPtr = ogrJNI.BuildPolygonFromEdges__SWIG_3(Geometry.getCPtr(hLineCollection), hLineCollection);
/*  69 */     return (cPtr == 0L) ? null : new Geometry(cPtr, true);
/*     */   }
/*     */ 
/*     */   public static Geometry ApproximateArcAngles(double dfCenterX, double dfCenterY, double dfZ, double dfPrimaryRadius, double dfSecondaryAxis, double dfRotation, double dfStartAngle, double dfEndAngle, double dfMaxAngleStepSizeDegrees) {
/*  73 */     long cPtr = ogrJNI.ApproximateArcAngles(dfCenterX, dfCenterY, dfZ, dfPrimaryRadius, dfSecondaryAxis, dfRotation, dfStartAngle, dfEndAngle, dfMaxAngleStepSizeDegrees);
/*  74 */     return (cPtr == 0L) ? null : new Geometry(cPtr, true);
/*     */   }
/*     */ 
/*     */   public static Geometry ForceToPolygon(Geometry geom_in) {
/*  78 */     long cPtr = ogrJNI.ForceToPolygon(Geometry.getCPtr(geom_in), geom_in);
/*  79 */     return (cPtr == 0L) ? null : new Geometry(cPtr, true);
/*     */   }
/*     */ 
/*     */   public static Geometry ForceToMultiPolygon(Geometry geom_in) {
/*  83 */     long cPtr = ogrJNI.ForceToMultiPolygon(Geometry.getCPtr(geom_in), geom_in);
/*  84 */     return (cPtr == 0L) ? null : new Geometry(cPtr, true);
/*     */   }
/*     */ 
/*     */   public static Geometry ForceToMultiPoint(Geometry geom_in) {
/*  88 */     long cPtr = ogrJNI.ForceToMultiPoint(Geometry.getCPtr(geom_in), geom_in);
/*  89 */     return (cPtr == 0L) ? null : new Geometry(cPtr, true);
/*     */   }
/*     */ 
/*     */   public static Geometry ForceToMultiLineString(Geometry geom_in) {
/*  93 */     long cPtr = ogrJNI.ForceToMultiLineString(Geometry.getCPtr(geom_in), geom_in);
/*  94 */     return (cPtr == 0L) ? null : new Geometry(cPtr, true);
/*     */   }
/*     */ 
/*     */   public static int GetDriverCount() {
/*  98 */     return ogrJNI.GetDriverCount();
/*     */   }
/*     */ 
/*     */   public static int GetOpenDSCount() {
/* 102 */     return ogrJNI.GetOpenDSCount();
/*     */   }
/*     */ 
/*     */   public static int SetGenerate_DB2_V72_BYTE_ORDER(int bGenerate_DB2_V72_BYTE_ORDER) {
/* 106 */     return ogrJNI.SetGenerate_DB2_V72_BYTE_ORDER(bGenerate_DB2_V72_BYTE_ORDER);
/*     */   }
/*     */ 
/*     */   public static void RegisterAll() {
/* 110 */     ogrJNI.RegisterAll();
/*     */   }
/*     */ 
/*     */   public static String GeometryTypeToName(int eType) {
/* 114 */     return ogrJNI.GeometryTypeToName(eType);
/*     */   }
/*     */ 
/*     */   public static String GetFieldTypeName(int type) {
/* 118 */     return ogrJNI.GetFieldTypeName(type);
/*     */   }
/*     */ 
/*     */   public static DataSource GetOpenDS(int ds_number) {
/* 122 */     long cPtr = ogrJNI.GetOpenDS(ds_number);
/* 123 */     return (cPtr == 0L) ? null : new DataSource(cPtr, false);
/*     */   }
/*     */ 
/*     */   public static DataSource Open(String utf8_path, int update) {
/* 127 */     long cPtr = ogrJNI.Open__SWIG_0(utf8_path, update);
/* 128 */     return (cPtr == 0L) ? null : new DataSource(cPtr, true);
/*     */   }
/*     */ 
/*     */   public static DataSource Open(String utf8_path) {
/* 132 */     long cPtr = ogrJNI.Open__SWIG_1(utf8_path);
/* 133 */     return (cPtr == 0L) ? null : new DataSource(cPtr, true);
/*     */   }
/*     */ 
/*     */   public static DataSource OpenShared(String utf8_path, int update) {
/* 137 */     long cPtr = ogrJNI.OpenShared__SWIG_0(utf8_path, update);
/* 138 */     return (cPtr == 0L) ? null : new DataSource(cPtr, true);
/*     */   }
/*     */ 
/*     */   public static DataSource OpenShared(String utf8_path) {
/* 142 */     long cPtr = ogrJNI.OpenShared__SWIG_1(utf8_path);
/* 143 */     return (cPtr == 0L) ? null : new DataSource(cPtr, true);
/*     */   }
/*     */ 
/*     */   public static Driver GetDriverByName(String name) {
/* 147 */     long cPtr = ogrJNI.GetDriverByName(name);
/* 148 */     return (cPtr == 0L) ? null : new Driver(cPtr, false);
/*     */   }
/*     */ 
/*     */   public static Driver GetDriver(int driver_number) {
/* 152 */     long cPtr = ogrJNI.GetDriver(driver_number);
/* 153 */     return (cPtr == 0L) ? null : new Driver(cPtr, false);
/*     */   }
/*     */ 
/*     */   public static Vector GeneralCmdLineProcessor(Vector papszArgv, int nOptions) {
/* 157 */     return ogrJNI.GeneralCmdLineProcessor__SWIG_0(papszArgv, nOptions);
/*     */   }
/*     */ 
/*     */   public static Vector GeneralCmdLineProcessor(Vector papszArgv) {
/* 161 */     return ogrJNI.GeneralCmdLineProcessor__SWIG_1(papszArgv);
/*     */   }
/*     */ 
/*     */   public static String[] GeneralCmdLineProcessor(String[] args, int nOptions)
/*     */   {
/* 173 */     Vector vArgs = new Vector();
/*     */ 
/* 175 */     for (int i = 0; i < args.length; ++i) {
/* 176 */       vArgs.addElement(args[i]);
/*     */     }
/* 178 */     vArgs = GeneralCmdLineProcessor(vArgs, nOptions);
/* 179 */     Enumeration eArgs = vArgs.elements();
/* 180 */     args = new String[vArgs.size()];
/* 181 */     int i = 0;
/* 182 */     while (eArgs.hasMoreElements())
/*     */     {
/* 184 */       String arg = (String)eArgs.nextElement();
/* 185 */       args[(i++)] = arg;
/*     */     }
/*     */ 
/* 188 */     return args;
/*     */   }
/*     */ 
/*     */   public static String[] GeneralCmdLineProcessor(String[] args)
/*     */   {
/* 193 */     return GeneralCmdLineProcessor(args, 0);
/*     */   }
/*     */ 
/*     */   public static DataSource Open(String filename, boolean update)
/*     */   {
/* 198 */     return Open(filename, (update) ? 1 : 0);
/*     */   }
/*     */ }

/* Location:           E:\temp\1117\gdal.jar
 * Qualified Name:     org.gdal.ogr.ogr
 * JD-Core Version:    0.5.4
 */