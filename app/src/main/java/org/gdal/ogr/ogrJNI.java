/*    */ package org.gdal.ogr;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.util.Vector;
/*    */ import org.gdal.gdal.gdal;
/*    */ import org.gdal.osr.CoordinateTransformation;
/*    */ import org.gdal.osr.SpatialReference;
/*    */ 
/*    */ public class ogrJNI
/*    */ {
/* 18 */   private static boolean available = false;
/*    */ 
/*    */   public static final native void UseExceptions();
/*    */ 
/*    */   public static final native void DontUseExceptions();
/*    */ 
/*    */   public static boolean isAvailable()
/*    */   {
/* 38 */     return available;
/*    */   }
/*    */ 
/*    */   public static final native String Driver_name_get(long paramLong, Driver paramDriver);
/*    */ 
/*    */   public static final native long Driver_CreateDataSource__SWIG_0(long paramLong, Driver paramDriver, String paramString, Vector paramVector);
/*    */ 
/*    */   public static final native long Driver_CreateDataSource__SWIG_1(long paramLong, Driver paramDriver, String paramString);
/*    */ 
/*    */   public static final native long Driver_CopyDataSource__SWIG_0(long paramLong1, Driver paramDriver, long paramLong2, DataSource paramDataSource, String paramString, Vector paramVector);
/*    */ 
/*    */   public static final native long Driver_CopyDataSource__SWIG_1(long paramLong1, Driver paramDriver, long paramLong2, DataSource paramDataSource, String paramString);
/*    */ 
/*    */   public static final native long Driver_Open__SWIG_0(long paramLong, Driver paramDriver, String paramString, int paramInt);
/*    */ 
/*    */   public static final native long Driver_Open__SWIG_1(long paramLong, Driver paramDriver, String paramString);
/*    */ 
/*    */   public static final native int Driver_DeleteDataSource(long paramLong, Driver paramDriver, String paramString);
/*    */ 
/*    */   public static final native boolean Driver_TestCapability(long paramLong, Driver paramDriver, String paramString);
/*    */ 
/*    */   public static final native String Driver_GetName(long paramLong, Driver paramDriver);
/*    */ 
/*    */   public static final native void Driver_Register(long paramLong, Driver paramDriver);
/*    */ 
/*    */   public static final native void Driver_Deregister(long paramLong, Driver paramDriver);
/*    */ 
/*    */   public static final native String DataSource_name_get(long paramLong, DataSource paramDataSource);
/*    */ 
/*    */   public static final native void delete_DataSource(long paramLong);
/*    */ 
/*    */   public static final native int DataSource_GetRefCount(long paramLong, DataSource paramDataSource);
/*    */ 
/*    */   public static final native int DataSource_GetSummaryRefCount(long paramLong, DataSource paramDataSource);
/*    */ 
/*    */   public static final native int DataSource_GetLayerCount(long paramLong, DataSource paramDataSource);
/*    */ 
/*    */   public static final native long DataSource_GetDriver(long paramLong, DataSource paramDataSource);
/*    */ 
/*    */   public static final native String DataSource_GetName(long paramLong, DataSource paramDataSource);
/*    */ 
/*    */   public static final native int DataSource_DeleteLayer(long paramLong, DataSource paramDataSource, int paramInt);
/*    */ 
/*    */   public static final native int DataSource_SyncToDisk(long paramLong, DataSource paramDataSource);
/*    */ 
/*    */   public static final native long DataSource_CreateLayer__SWIG_0(long paramLong1, DataSource paramDataSource, String paramString, long paramLong2, SpatialReference paramSpatialReference, int paramInt, Vector paramVector);
/*    */ 
/*    */   public static final native long DataSource_CreateLayer__SWIG_1(long paramLong1, DataSource paramDataSource, String paramString, long paramLong2, SpatialReference paramSpatialReference, int paramInt);
/*    */ 
/*    */   public static final native long DataSource_CreateLayer__SWIG_2(long paramLong1, DataSource paramDataSource, String paramString, long paramLong2, SpatialReference paramSpatialReference);
/*    */ 
/*    */   public static final native long DataSource_CreateLayer__SWIG_3(long paramLong, DataSource paramDataSource, String paramString);
/*    */ 
/*    */   public static final native long DataSource_CopyLayer__SWIG_0(long paramLong1, DataSource paramDataSource, long paramLong2, Layer paramLayer, String paramString, Vector paramVector);
/*    */ 
/*    */   public static final native long DataSource_CopyLayer__SWIG_1(long paramLong1, DataSource paramDataSource, long paramLong2, Layer paramLayer, String paramString);
/*    */ 
/*    */   public static final native long DataSource_GetLayerByIndex(long paramLong, DataSource paramDataSource, int paramInt);
/*    */ 
/*    */   public static final native long DataSource_GetLayerByName(long paramLong, DataSource paramDataSource, String paramString);
/*    */ 
/*    */   public static final native boolean DataSource_TestCapability(long paramLong, DataSource paramDataSource, String paramString);
/*    */ 
/*    */   public static final native long DataSource_ExecuteSQL__SWIG_0(long paramLong1, DataSource paramDataSource, String paramString1, long paramLong2, Geometry paramGeometry, String paramString2);
/*    */ 
/*    */   public static final native long DataSource_ExecuteSQL__SWIG_1(long paramLong1, DataSource paramDataSource, String paramString, long paramLong2, Geometry paramGeometry);
/*    */ 
/*    */   public static final native long DataSource_ExecuteSQL__SWIG_2(long paramLong, DataSource paramDataSource, String paramString);
/*    */ 
/*    */   public static final native void DataSource_ReleaseResultSet(long paramLong1, DataSource paramDataSource, long paramLong2, Layer paramLayer);
/*    */ 
/*    */   public static final native int Layer_GetRefCount(long paramLong, Layer paramLayer);
/*    */ 
/*    */   public static final native void Layer_SetSpatialFilter(long paramLong1, Layer paramLayer, long paramLong2, Geometry paramGeometry);
/*    */ 
/*    */   public static final native void Layer_SetSpatialFilterRect(long paramLong, Layer paramLayer, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4);
/*    */ 
/*    */   public static final native long Layer_GetSpatialFilter(long paramLong, Layer paramLayer);
/*    */ 
/*    */   public static final native int Layer_SetAttributeFilter(long paramLong, Layer paramLayer, String paramString);
/*    */ 
/*    */   public static final native void Layer_ResetReading(long paramLong, Layer paramLayer);
/*    */ 
/*    */   public static final native String Layer_GetName(long paramLong, Layer paramLayer);
/*    */ 
/*    */   public static final native int Layer_GetGeomType(long paramLong, Layer paramLayer);
/*    */ 
/*    */   public static final native String Layer_GetGeometryColumn(long paramLong, Layer paramLayer);
/*    */ 
/*    */   public static final native String Layer_GetFIDColumn(long paramLong, Layer paramLayer);
/*    */ 
/*    */   public static final native long Layer_GetFeature(long paramLong, Layer paramLayer, int paramInt);
/*    */ 
/*    */   public static final native long Layer_GetNextFeature(long paramLong, Layer paramLayer);
/*    */ 
/*    */   public static final native int Layer_SetNextByIndex(long paramLong, Layer paramLayer, int paramInt);
/*    */ 
/*    */   public static final native int Layer_SetFeature(long paramLong1, Layer paramLayer, long paramLong2, Feature paramFeature);
/*    */ 
/*    */   public static final native int Layer_CreateFeature(long paramLong1, Layer paramLayer, long paramLong2, Feature paramFeature);
/*    */ 
/*    */   public static final native int Layer_DeleteFeature(long paramLong, Layer paramLayer, int paramInt);
/*    */ 
/*    */   public static final native int Layer_SyncToDisk(long paramLong, Layer paramLayer);
/*    */ 
/*    */   public static final native long Layer_GetLayerDefn(long paramLong, Layer paramLayer);
/*    */ 
/*    */   public static final native int Layer_GetFeatureCount__SWIG_0(long paramLong, Layer paramLayer, int paramInt);
/*    */ 
/*    */   public static final native int Layer_GetFeatureCount__SWIG_1(long paramLong, Layer paramLayer);
/*    */ 
/*    */   public static final native int Layer_GetExtent(long paramLong, Layer paramLayer, double[] paramArrayOfDouble, int paramInt);
/*    */ 
/*    */   public static final native boolean Layer_TestCapability(long paramLong, Layer paramLayer, String paramString);
/*    */ 
/*    */   public static final native int Layer_CreateField__SWIG_0(long paramLong1, Layer paramLayer, long paramLong2, FieldDefn paramFieldDefn, int paramInt);
/*    */ 
/*    */   public static final native int Layer_CreateField__SWIG_1(long paramLong1, Layer paramLayer, long paramLong2, FieldDefn paramFieldDefn);
/*    */ 
/*    */   public static final native int Layer_DeleteField(long paramLong, Layer paramLayer, int paramInt);
/*    */ 
/*    */   public static final native int Layer_ReorderField(long paramLong, Layer paramLayer, int paramInt1, int paramInt2);
/*    */ 
/*    */   public static final native int Layer_ReorderFields(long paramLong, Layer paramLayer, int[] paramArrayOfInt);
/*    */ 
/*    */   public static final native int Layer_AlterFieldDefn(long paramLong1, Layer paramLayer, int paramInt1, long paramLong2, FieldDefn paramFieldDefn, int paramInt2);
/*    */ 
/*    */   public static final native int Layer_StartTransaction(long paramLong, Layer paramLayer);
/*    */ 
/*    */   public static final native int Layer_CommitTransaction(long paramLong, Layer paramLayer);
/*    */ 
/*    */   public static final native int Layer_RollbackTransaction(long paramLong, Layer paramLayer);
/*    */ 
/*    */   public static final native long Layer_GetSpatialRef(long paramLong, Layer paramLayer);
/*    */ 
/*    */   public static final native long Layer_GetFeaturesRead(long paramLong, Layer paramLayer);
/*    */ 
/*    */   public static final native int Layer_SetIgnoredFields(long paramLong, Layer paramLayer, Vector paramVector);
/*    */ 
/*    */   public static final native void delete_Feature(long paramLong);
/*    */ 
/*    */   public static final native long new_Feature(long paramLong, FeatureDefn paramFeatureDefn);
/*    */ 
/*    */   public static final native long Feature_GetDefnRef(long paramLong, Feature paramFeature);
/*    */ 
/*    */   public static final native int Feature_SetGeometry(long paramLong1, Feature paramFeature, long paramLong2, Geometry paramGeometry);
/*    */ 
/*    */   public static final native int Feature_SetGeometryDirectly(long paramLong1, Feature paramFeature, long paramLong2, Geometry paramGeometry);
/*    */ 
/*    */   public static final native long Feature_GetGeometryRef(long paramLong, Feature paramFeature);
/*    */ 
/*    */   public static final native long Feature_Clone(long paramLong, Feature paramFeature);
/*    */ 
/*    */   public static final native boolean Feature_Equal(long paramLong1, Feature paramFeature1, long paramLong2, Feature paramFeature2);
/*    */ 
/*    */   public static final native int Feature_GetFieldCount(long paramLong, Feature paramFeature);
/*    */ 
/*    */   public static final native long Feature_GetFieldDefnRef__SWIG_0(long paramLong, Feature paramFeature, int paramInt);
/*    */ 
/*    */   public static final native long Feature_GetFieldDefnRef__SWIG_1(long paramLong, Feature paramFeature, String paramString);
/*    */ 
/*    */   public static final native String Feature_GetFieldAsString__SWIG_0(long paramLong, Feature paramFeature, int paramInt);
/*    */ 
/*    */   public static final native String Feature_GetFieldAsString__SWIG_1(long paramLong, Feature paramFeature, String paramString);
/*    */ 
/*    */   public static final native int Feature_GetFieldAsInteger__SWIG_0(long paramLong, Feature paramFeature, int paramInt);
/*    */ 
/*    */   public static final native int Feature_GetFieldAsInteger__SWIG_1(long paramLong, Feature paramFeature, String paramString);
/*    */ 
/*    */   public static final native double Feature_GetFieldAsDouble__SWIG_0(long paramLong, Feature paramFeature, int paramInt);
/*    */ 
/*    */   public static final native double Feature_GetFieldAsDouble__SWIG_1(long paramLong, Feature paramFeature, String paramString);
/*    */ 
/*    */   public static final native void Feature_GetFieldAsDateTime(long paramLong, Feature paramFeature, int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3, int[] paramArrayOfInt4, int[] paramArrayOfInt5, int[] paramArrayOfInt6, int[] paramArrayOfInt7);
/*    */ 
/*    */   public static final native int[] Feature_GetFieldAsIntegerList(long paramLong, Feature paramFeature, int paramInt);
/*    */ 
/*    */   public static final native double[] Feature_GetFieldAsDoubleList(long paramLong, Feature paramFeature, int paramInt);
/*    */ 
/*    */   public static final native String[] Feature_GetFieldAsStringList(long paramLong, Feature paramFeature, int paramInt);
/*    */ 
/*    */   public static final native boolean Feature_IsFieldSet__SWIG_0(long paramLong, Feature paramFeature, int paramInt);
/*    */ 
/*    */   public static final native boolean Feature_IsFieldSet__SWIG_1(long paramLong, Feature paramFeature, String paramString);
/*    */ 
/*    */   public static final native int Feature_GetFieldIndex(long paramLong, Feature paramFeature, String paramString);
/*    */ 
/*    */   public static final native int Feature_GetFID(long paramLong, Feature paramFeature);
/*    */ 
/*    */   public static final native int Feature_SetFID(long paramLong, Feature paramFeature, int paramInt);
/*    */ 
/*    */   public static final native void Feature_DumpReadable(long paramLong, Feature paramFeature);
/*    */ 
/*    */   public static final native void Feature_UnsetField__SWIG_0(long paramLong, Feature paramFeature, int paramInt);
/*    */ 
/*    */   public static final native void Feature_UnsetField__SWIG_1(long paramLong, Feature paramFeature, String paramString);
/*    */ 
/*    */   public static final native void Feature_SetField__SWIG_0(long paramLong, Feature paramFeature, int paramInt, String paramString);
/*    */ 
/*    */   public static final native void Feature_SetField__SWIG_1(long paramLong, Feature paramFeature, String paramString1, String paramString2);
/*    */ 
/*    */   public static final native void Feature_SetField__SWIG_2(long paramLong, Feature paramFeature, int paramInt1, int paramInt2);
/*    */ 
/*    */   public static final native void Feature_SetField__SWIG_3(long paramLong, Feature paramFeature, String paramString, int paramInt);
/*    */ 
/*    */   public static final native void Feature_SetField__SWIG_4(long paramLong, Feature paramFeature, int paramInt, double paramDouble);
/*    */ 
/*    */   public static final native void Feature_SetField__SWIG_5(long paramLong, Feature paramFeature, String paramString, double paramDouble);
/*    */ 
/*    */   public static final native void Feature_SetField__SWIG_6(long paramLong, Feature paramFeature, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8);
/*    */ 
/*    */   public static final native void Feature_SetField__SWIG_7(long paramLong, Feature paramFeature, String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7);
/*    */ 
/*    */   public static final native void Feature_SetFieldIntegerList(long paramLong, Feature paramFeature, int paramInt, int[] paramArrayOfInt);
/*    */ 
/*    */   public static final native void Feature_SetFieldDoubleList(long paramLong, Feature paramFeature, int paramInt, double[] paramArrayOfDouble);
/*    */ 
/*    */   public static final native void Feature_SetFieldStringList(long paramLong, Feature paramFeature, int paramInt, Vector paramVector);
/*    */ 
/*    */   public static final native int Feature_SetFrom__SWIG_0(long paramLong1, Feature paramFeature1, long paramLong2, Feature paramFeature2, int paramInt);
/*    */ 
/*    */   public static final native int Feature_SetFrom__SWIG_1(long paramLong1, Feature paramFeature1, long paramLong2, Feature paramFeature2);
/*    */ 
/*    */   public static final native int Feature_SetFromWithMap(long paramLong1, Feature paramFeature1, long paramLong2, Feature paramFeature2, int paramInt, int[] paramArrayOfInt);
/*    */ 
/*    */   public static final native String Feature_GetStyleString(long paramLong, Feature paramFeature);
/*    */ 
/*    */   public static final native void Feature_SetStyleString(long paramLong, Feature paramFeature, String paramString);
/*    */ 
/*    */   public static final native int Feature_GetFieldType__SWIG_0(long paramLong, Feature paramFeature, int paramInt);
/*    */ 
/*    */   public static final native int Feature_GetFieldType__SWIG_1(long paramLong, Feature paramFeature, String paramString);
/*    */ 
/*    */   public static final native void delete_FeatureDefn(long paramLong);
/*    */ 
/*    */   public static final native long new_FeatureDefn__SWIG_0(String paramString);
/*    */ 
/*    */   public static final native long new_FeatureDefn__SWIG_1();
/*    */ 
/*    */   public static final native String FeatureDefn_GetName(long paramLong, FeatureDefn paramFeatureDefn);
/*    */ 
/*    */   public static final native int FeatureDefn_GetFieldCount(long paramLong, FeatureDefn paramFeatureDefn);
/*    */ 
/*    */   public static final native long FeatureDefn_GetFieldDefn(long paramLong, FeatureDefn paramFeatureDefn, int paramInt);
/*    */ 
/*    */   public static final native int FeatureDefn_GetFieldIndex(long paramLong, FeatureDefn paramFeatureDefn, String paramString);
/*    */ 
/*    */   public static final native void FeatureDefn_AddFieldDefn(long paramLong1, FeatureDefn paramFeatureDefn, long paramLong2, FieldDefn paramFieldDefn);
/*    */ 
/*    */   public static final native int FeatureDefn_GetGeomType(long paramLong, FeatureDefn paramFeatureDefn);
/*    */ 
/*    */   public static final native void FeatureDefn_SetGeomType(long paramLong, FeatureDefn paramFeatureDefn, int paramInt);
/*    */ 
/*    */   public static final native int FeatureDefn_GetReferenceCount(long paramLong, FeatureDefn paramFeatureDefn);
/*    */ 
/*    */   public static final native int FeatureDefn_IsGeometryIgnored(long paramLong, FeatureDefn paramFeatureDefn);
/*    */ 
/*    */   public static final native void FeatureDefn_SetGeometryIgnored(long paramLong, FeatureDefn paramFeatureDefn, int paramInt);
/*    */ 
/*    */   public static final native int FeatureDefn_IsStyleIgnored(long paramLong, FeatureDefn paramFeatureDefn);
/*    */ 
/*    */   public static final native void FeatureDefn_SetStyleIgnored(long paramLong, FeatureDefn paramFeatureDefn, int paramInt);
/*    */ 
/*    */   public static final native void delete_FieldDefn(long paramLong);
/*    */ 
/*    */   public static final native long new_FieldDefn__SWIG_0(String paramString, int paramInt);
/*    */ 
/*    */   public static final native long new_FieldDefn__SWIG_1(String paramString);
/*    */ 
/*    */   public static final native long new_FieldDefn__SWIG_2();
/*    */ 
/*    */   public static final native String FieldDefn_GetName(long paramLong, FieldDefn paramFieldDefn);
/*    */ 
/*    */   public static final native String FieldDefn_GetNameRef(long paramLong, FieldDefn paramFieldDefn);
/*    */ 
/*    */   public static final native void FieldDefn_SetName(long paramLong, FieldDefn paramFieldDefn, String paramString);
/*    */ 
/*    */   public static final native int FieldDefn_GetFieldType(long paramLong, FieldDefn paramFieldDefn);
/*    */ 
/*    */   public static final native void FieldDefn_SetType(long paramLong, FieldDefn paramFieldDefn, int paramInt);
/*    */ 
/*    */   public static final native int FieldDefn_GetJustify(long paramLong, FieldDefn paramFieldDefn);
/*    */ 
/*    */   public static final native void FieldDefn_SetJustify(long paramLong, FieldDefn paramFieldDefn, int paramInt);
/*    */ 
/*    */   public static final native int FieldDefn_GetWidth(long paramLong, FieldDefn paramFieldDefn);
/*    */ 
/*    */   public static final native void FieldDefn_SetWidth(long paramLong, FieldDefn paramFieldDefn, int paramInt);
/*    */ 
/*    */   public static final native int FieldDefn_GetPrecision(long paramLong, FieldDefn paramFieldDefn);
/*    */ 
/*    */   public static final native void FieldDefn_SetPrecision(long paramLong, FieldDefn paramFieldDefn, int paramInt);
/*    */ 
/*    */   public static final native String FieldDefn_GetTypeName(long paramLong, FieldDefn paramFieldDefn);
/*    */ 
/*    */   public static final native String FieldDefn_GetFieldTypeName(long paramLong, FieldDefn paramFieldDefn, int paramInt);
/*    */ 
/*    */   public static final native int FieldDefn_IsIgnored(long paramLong, FieldDefn paramFieldDefn);
/*    */ 
/*    */   public static final native void FieldDefn_SetIgnored(long paramLong, FieldDefn paramFieldDefn, int paramInt);
/*    */ 
/*    */   public static final native long CreateGeometryFromWkb__SWIG_0(byte[] paramArrayOfByte, long paramLong, SpatialReference paramSpatialReference);
/*    */ 
/*    */   public static final native long CreateGeometryFromWkb__SWIG_1(byte[] paramArrayOfByte);
/*    */ 
/*    */   public static final native long CreateGeometryFromWkt__SWIG_0(String paramString, long paramLong, SpatialReference paramSpatialReference);
/*    */ 
/*    */   public static final native long CreateGeometryFromWkt__SWIG_1(String paramString);
/*    */ 
/*    */   public static final native long CreateGeometryFromGML(String paramString);
/*    */ 
/*    */   public static final native long CreateGeometryFromJson(String paramString);
/*    */ 
/*    */   public static final native long BuildPolygonFromEdges__SWIG_0(long paramLong, Geometry paramGeometry, int paramInt1, int paramInt2, double paramDouble);
/*    */ 
/*    */   public static final native long BuildPolygonFromEdges__SWIG_1(long paramLong, Geometry paramGeometry, int paramInt1, int paramInt2);
/*    */ 
/*    */   public static final native long BuildPolygonFromEdges__SWIG_2(long paramLong, Geometry paramGeometry, int paramInt);
/*    */ 
/*    */   public static final native long BuildPolygonFromEdges__SWIG_3(long paramLong, Geometry paramGeometry);
/*    */ 
/*    */   public static final native long ApproximateArcAngles(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, double paramDouble7, double paramDouble8, double paramDouble9);
/*    */ 
/*    */   public static final native long ForceToPolygon(long paramLong, Geometry paramGeometry);
/*    */ 
/*    */   public static final native long ForceToMultiPolygon(long paramLong, Geometry paramGeometry);
/*    */ 
/*    */   public static final native long ForceToMultiPoint(long paramLong, Geometry paramGeometry);
/*    */ 
/*    */   public static final native long ForceToMultiLineString(long paramLong, Geometry paramGeometry);
/*    */ 
/*    */   public static final native void delete_Geometry(long paramLong);
/*    */ 
/*    */   public static final native int Geometry_ExportToWkt__SWIG_0(long paramLong, Geometry paramGeometry, String[] paramArrayOfString);
/*    */ 
/*    */   public static final native byte[] Geometry_ExportToWkb__SWIG_0(long paramLong, Geometry paramGeometry, int paramInt);
/*    */ 
/*    */   public static final native byte[] Geometry_ExportToWkb__SWIG_1(long paramLong, Geometry paramGeometry);
/*    */ 
/*    */   public static final native String Geometry_ExportToGML__SWIG_0(long paramLong, Geometry paramGeometry, Vector paramVector);
/*    */ 
/*    */   public static final native String Geometry_ExportToGML__SWIG_1(long paramLong, Geometry paramGeometry);
/*    */ 
/*    */   public static final native String Geometry_ExportToKML__SWIG_0(long paramLong, Geometry paramGeometry, String paramString);
/*    */ 
/*    */   public static final native String Geometry_ExportToKML__SWIG_1(long paramLong, Geometry paramGeometry);
/*    */ 
/*    */   public static final native String Geometry_ExportToJson__SWIG_0(long paramLong, Geometry paramGeometry, Vector paramVector);
/*    */ 
/*    */   public static final native String Geometry_ExportToJson__SWIG_1(long paramLong, Geometry paramGeometry);
/*    */ 
/*    */   public static final native void Geometry_AddPoint__SWIG_0(long paramLong, Geometry paramGeometry, double paramDouble1, double paramDouble2, double paramDouble3);
/*    */ 
/*    */   public static final native void Geometry_AddPoint__SWIG_1(long paramLong, Geometry paramGeometry, double paramDouble1, double paramDouble2);
/*    */ 
/*    */   public static final native void Geometry_AddPoint_2D(long paramLong, Geometry paramGeometry, double paramDouble1, double paramDouble2);
/*    */ 
/*    */   public static final native int Geometry_AddGeometryDirectly(long paramLong1, Geometry paramGeometry1, long paramLong2, Geometry paramGeometry2);
/*    */ 
/*    */   public static final native int Geometry_AddGeometry(long paramLong1, Geometry paramGeometry1, long paramLong2, Geometry paramGeometry2);
/*    */ 
/*    */   public static final native long Geometry_Clone(long paramLong, Geometry paramGeometry);
/*    */ 
/*    */   public static final native int Geometry_GetGeometryType(long paramLong, Geometry paramGeometry);
/*    */ 
/*    */   public static final native String Geometry_GetGeometryName(long paramLong, Geometry paramGeometry);
/*    */ 
/*    */   public static final native double Geometry_Length(long paramLong, Geometry paramGeometry);
/*    */ 
/*    */   public static final native double Geometry_Area(long paramLong, Geometry paramGeometry);
/*    */ 
/*    */   public static final native double Geometry_GetArea(long paramLong, Geometry paramGeometry);
/*    */ 
/*    */   public static final native int Geometry_GetPointCount(long paramLong, Geometry paramGeometry);
/*    */ 
/*    */   public static final native double[][] Geometry_GetPoints__SWIG_0(long paramLong, Geometry paramGeometry, int paramInt);
/*    */ 
/*    */   public static final native double[][] Geometry_GetPoints__SWIG_1(long paramLong, Geometry paramGeometry);
/*    */ 
/*    */   public static final native double Geometry_GetX__SWIG_0(long paramLong, Geometry paramGeometry, int paramInt);
/*    */ 
/*    */   public static final native double Geometry_GetX__SWIG_1(long paramLong, Geometry paramGeometry);
/*    */ 
/*    */   public static final native double Geometry_GetY__SWIG_0(long paramLong, Geometry paramGeometry, int paramInt);
/*    */ 
/*    */   public static final native double Geometry_GetY__SWIG_1(long paramLong, Geometry paramGeometry);
/*    */ 
/*    */   public static final native double Geometry_GetZ__SWIG_0(long paramLong, Geometry paramGeometry, int paramInt);
/*    */ 
/*    */   public static final native double Geometry_GetZ__SWIG_1(long paramLong, Geometry paramGeometry);
/*    */ 
/*    */   public static final native void Geometry_GetPoint(long paramLong, Geometry paramGeometry, int paramInt, double[] paramArrayOfDouble);
/*    */ 
/*    */   public static final native void Geometry_GetPoint_2D(long paramLong, Geometry paramGeometry, int paramInt, double[] paramArrayOfDouble);
/*    */ 
/*    */   public static final native int Geometry_GetGeometryCount(long paramLong, Geometry paramGeometry);
/*    */ 
/*    */   public static final native void Geometry_SetPoint__SWIG_0(long paramLong, Geometry paramGeometry, int paramInt, double paramDouble1, double paramDouble2, double paramDouble3);
/*    */ 
/*    */   public static final native void Geometry_SetPoint__SWIG_1(long paramLong, Geometry paramGeometry, int paramInt, double paramDouble1, double paramDouble2);
/*    */ 
/*    */   public static final native void Geometry_SetPoint_2D(long paramLong, Geometry paramGeometry, int paramInt, double paramDouble1, double paramDouble2);
/*    */ 
/*    */   public static final native long Geometry_GetGeometryRef(long paramLong, Geometry paramGeometry, int paramInt);
/*    */ 
/*    */   public static final native long Geometry_Simplify(long paramLong, Geometry paramGeometry, double paramDouble);
/*    */ 
/*    */   public static final native long Geometry_SimplifyPreserveTopology(long paramLong, Geometry paramGeometry, double paramDouble);
/*    */ 
/*    */   public static final native long Geometry_Boundary(long paramLong, Geometry paramGeometry);
/*    */ 
/*    */   public static final native long Geometry_GetBoundary(long paramLong, Geometry paramGeometry);
/*    */ 
/*    */   public static final native long Geometry_ConvexHull(long paramLong, Geometry paramGeometry);
/*    */ 
/*    */   public static final native long Geometry_Buffer__SWIG_0(long paramLong, Geometry paramGeometry, double paramDouble, int paramInt);
/*    */ 
/*    */   public static final native long Geometry_Buffer__SWIG_1(long paramLong, Geometry paramGeometry, double paramDouble);
/*    */ 
/*    */   public static final native long Geometry_Intersection(long paramLong1, Geometry paramGeometry1, long paramLong2, Geometry paramGeometry2);
/*    */ 
/*    */   public static final native long Geometry_Union(long paramLong1, Geometry paramGeometry1, long paramLong2, Geometry paramGeometry2);
/*    */ 
/*    */   public static final native long Geometry_UnionCascaded(long paramLong, Geometry paramGeometry);
/*    */ 
/*    */   public static final native long Geometry_Difference(long paramLong1, Geometry paramGeometry1, long paramLong2, Geometry paramGeometry2);
/*    */ 
/*    */   public static final native long Geometry_SymDifference(long paramLong1, Geometry paramGeometry1, long paramLong2, Geometry paramGeometry2);
/*    */ 
/*    */   public static final native long Geometry_SymmetricDifference(long paramLong1, Geometry paramGeometry1, long paramLong2, Geometry paramGeometry2);
/*    */ 
/*    */   public static final native double Geometry_Distance(long paramLong1, Geometry paramGeometry1, long paramLong2, Geometry paramGeometry2);
/*    */ 
/*    */   public static final native void Geometry_Empty(long paramLong, Geometry paramGeometry);
/*    */ 
/*    */   public static final native boolean Geometry_IsEmpty(long paramLong, Geometry paramGeometry);
/*    */ 
/*    */   public static final native boolean Geometry_IsValid(long paramLong, Geometry paramGeometry);
/*    */ 
/*    */   public static final native boolean Geometry_IsSimple(long paramLong, Geometry paramGeometry);
/*    */ 
/*    */   public static final native boolean Geometry_IsRing(long paramLong, Geometry paramGeometry);
/*    */ 
/*    */   public static final native boolean Geometry_Intersects(long paramLong1, Geometry paramGeometry1, long paramLong2, Geometry paramGeometry2);
/*    */ 
/*    */   public static final native boolean Geometry_Intersect(long paramLong1, Geometry paramGeometry1, long paramLong2, Geometry paramGeometry2);
/*    */ 
/*    */   public static final native boolean Geometry_Equals(long paramLong1, Geometry paramGeometry1, long paramLong2, Geometry paramGeometry2);
/*    */ 
/*    */   public static final native boolean Geometry_Equal(long paramLong1, Geometry paramGeometry1, long paramLong2, Geometry paramGeometry2);
/*    */ 
/*    */   public static final native boolean Geometry_Disjoint(long paramLong1, Geometry paramGeometry1, long paramLong2, Geometry paramGeometry2);
/*    */ 
/*    */   public static final native boolean Geometry_Touches(long paramLong1, Geometry paramGeometry1, long paramLong2, Geometry paramGeometry2);
/*    */ 
/*    */   public static final native boolean Geometry_Crosses(long paramLong1, Geometry paramGeometry1, long paramLong2, Geometry paramGeometry2);
/*    */ 
/*    */   public static final native boolean Geometry_Within(long paramLong1, Geometry paramGeometry1, long paramLong2, Geometry paramGeometry2);
/*    */ 
/*    */   public static final native boolean Geometry_Contains(long paramLong1, Geometry paramGeometry1, long paramLong2, Geometry paramGeometry2);
/*    */ 
/*    */   public static final native boolean Geometry_Overlaps(long paramLong1, Geometry paramGeometry1, long paramLong2, Geometry paramGeometry2);
/*    */ 
/*    */   public static final native int Geometry_TransformTo(long paramLong1, Geometry paramGeometry, long paramLong2, SpatialReference paramSpatialReference);
/*    */ 
/*    */   public static final native int Geometry_Transform(long paramLong1, Geometry paramGeometry, long paramLong2, CoordinateTransformation paramCoordinateTransformation);
/*    */ 
/*    */   public static final native long Geometry_GetSpatialReference(long paramLong, Geometry paramGeometry);
/*    */ 
/*    */   public static final native void Geometry_AssignSpatialReference(long paramLong1, Geometry paramGeometry, long paramLong2, SpatialReference paramSpatialReference);
/*    */ 
/*    */   public static final native void Geometry_CloseRings(long paramLong, Geometry paramGeometry);
/*    */ 
/*    */   public static final native void Geometry_FlattenTo2D(long paramLong, Geometry paramGeometry);
/*    */ 
/*    */   public static final native void Geometry_Segmentize(long paramLong, Geometry paramGeometry, double paramDouble);
/*    */ 
/*    */   public static final native void Geometry_GetEnvelope(long paramLong, Geometry paramGeometry, double[] paramArrayOfDouble);
/*    */ 
/*    */   public static final native void Geometry_GetEnvelope3D(long paramLong, Geometry paramGeometry, double[] paramArrayOfDouble);
/*    */ 
/*    */   public static final native int Geometry_WkbSize(long paramLong, Geometry paramGeometry);
/*    */ 
/*    */   public static final native int Geometry_GetCoordinateDimension(long paramLong, Geometry paramGeometry);
/*    */ 
/*    */   public static final native void Geometry_SetCoordinateDimension(long paramLong, Geometry paramGeometry, int paramInt);
/*    */ 
/*    */   public static final native int Geometry_GetDimension(long paramLong, Geometry paramGeometry);
/*    */ 
/*    */   public static final native long new_Geometry__SWIG_0(int paramInt, String paramString1, byte[] paramArrayOfByte, String paramString2);
/*    */ 
/*    */   public static final native long new_Geometry__SWIG_1(int paramInt);
/*    */ 
/*    */   public static final native String Geometry_ExportToWkt__SWIG_1(long paramLong, Geometry paramGeometry);
/*    */ 
/*    */   public static final native long Geometry_Centroid(long paramLong, Geometry paramGeometry);
/*    */ 
/*    */   public static final native int GetDriverCount();
/*    */ 
/*    */   public static final native int GetOpenDSCount();
/*    */ 
/*    */   public static final native int SetGenerate_DB2_V72_BYTE_ORDER(int paramInt);
/*    */ 
/*    */   public static final native void RegisterAll();
/*    */ 
/*    */   public static final native String GeometryTypeToName(int paramInt);
/*    */ 
/*    */   public static final native String GetFieldTypeName(int paramInt);
/*    */ 
/*    */   public static final native long GetOpenDS(int paramInt);
/*    */ 
/*    */   public static final native long Open__SWIG_0(String paramString, int paramInt);
/*    */ 
/*    */   public static final native long Open__SWIG_1(String paramString);
/*    */ 
/*    */   public static final native long OpenShared__SWIG_0(String paramString, int paramInt);
/*    */ 
/*    */   public static final native long OpenShared__SWIG_1(String paramString);
/*    */ 
/*    */   public static final native long GetDriverByName(String paramString);
/*    */ 
/*    */   public static final native long GetDriver(int paramInt);
/*    */ 
/*    */   public static final native Vector GeneralCmdLineProcessor__SWIG_0(Vector paramVector, int paramInt);
/*    */ 
/*    */   public static final native Vector GeneralCmdLineProcessor__SWIG_1(Vector paramVector);
/*    */ 
/*    */   static
/*    */   {
/*    */     try
/*    */     {
/* 22 */       System.loadLibrary("ogrjni");
/* 23 */       available = true;
/*    */ 
/* 25 */       if (gdal.HasThreadSupport() == 0)
/*    */       {
/* 27 */         System.err.println("WARNING : GDAL should be compiled with thread support for safe execution in Java.");
/*    */       }
/*    */     }
/*    */     catch (UnsatisfiedLinkError e) {
/* 31 */       available = false;
/* 32 */       System.err.println("Native library load failed.");
/* 33 */       System.err.println(e);
/*    */     }
/*    */   }
/*    */ }

/* Location:           E:\temp\1117\gdal.jar
 * Qualified Name:     org.gdal.ogr.ogrJNI
 * JD-Core Version:    0.5.4
 */