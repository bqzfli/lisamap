/*    */ package org.gdal.gdal;
/*    */ 
/*    *//* import java.awt.Color*/
		 import android.graphics.Color;
/*    */ import java.io.PrintStream;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.util.Hashtable;
/*    */ import java.util.Vector;
/*    */ import org.gdal.ogr.Layer;
/*    */ 
/*    */ public class gdalJNI
/*    */ {
/* 14 */   private static boolean available = false;
/*    */ 
/*    */   public static boolean isAvailable()
/*    */   {
/* 34 */     return available;
/*    */   }
/*    */ 
/*    */   public static final native void delete_ProgressCallback(long paramLong);
/*    */ 
/*    */   public static final native int ProgressCallback_run(long paramLong, ProgressCallback paramProgressCallback, double paramDouble, String paramString);
/*    */ 
/*    */   public static final native long new_ProgressCallback();
/*    */ 
/*    */   public static final native long new_TermProgressCallback();
/*    */ 
/*    */   public static final native int TermProgressCallback_run(long paramLong, TermProgressCallback paramTermProgressCallback, double paramDouble, String paramString);
/*    */ 
/*    */   public static final native void delete_TermProgressCallback(long paramLong);
/*    */ 
/*    */   public static final native void Debug(String paramString1, String paramString2);
/*    */ 
/*    */   public static final native int PushErrorHandler__SWIG_0(String paramString);
/*    */ 
/*    */   public static final native int PushErrorHandler__SWIG_1();
/*    */ 
/*    */   public static final native void Error(int paramInt1, int paramInt2, String paramString);
/*    */ 
/*    */   public static final native void PopErrorHandler();
/*    */ 
/*    */   public static final native void ErrorReset();
/*    */ 
/*    */   public static final native String EscapeString__SWIG_0(byte[] paramArrayOfByte, int paramInt);
/*    */ 
/*    */   public static final native String EscapeString__SWIG_1(String paramString, int paramInt);
/*    */ 
/*    */   public static final native int GetLastErrorNo();
/*    */ 
/*    */   public static final native int GetLastErrorType();
/*    */ 
/*    */   public static final native String GetLastErrorMsg();
/*    */ 
/*    */   public static final native void PushFinderLocation(String paramString);
/*    */ 
/*    */   public static final native void PopFinderLocation();
/*    */ 
/*    */   public static final native void FinderClean();
/*    */ 
/*    */   public static final native String FindFile(String paramString1, String paramString2);
/*    */ 
/*    */   public static final native Vector ReadDir(String paramString);
/*    */ 
/*    */   public static final native void SetConfigOption(String paramString1, String paramString2);
/*    */ 
/*    */   public static final native String GetConfigOption__SWIG_0(String paramString1, String paramString2);
/*    */ 
/*    */   public static final native String GetConfigOption__SWIG_1(String paramString);
/*    */ 
/*    */   public static final native String CPLBinaryToHex(byte[] paramArrayOfByte);
/*    */ 
/*    */   public static final native byte[] CPLHexToBinary(String paramString);
/*    */ 
/*    */   public static final native void FileFromMemBuffer(String paramString, byte[] paramArrayOfByte);
/*    */ 
/*    */   public static final native int Unlink(String paramString);
/*    */ 
/*    */   public static final native int HasThreadSupport();
/*    */ 
/*    */   public static final native int Mkdir(String paramString, int paramInt);
/*    */ 
/*    */   public static final native int Rmdir(String paramString);
/*    */ 
/*    */   public static final native int Rename(String paramString1, String paramString2);
/*    */ 
/*    */   public static final native int CXT_Element_get();
/*    */ 
/*    */   public static final native int CXT_Text_get();
/*    */ 
/*    */   public static final native int CXT_Attribute_get();
/*    */ 
/*    */   public static final native int CXT_Comment_get();
/*    */ 
/*    */   public static final native int CXT_Literal_get();
/*    */ 
/*    */   public static final native int XMLNode_Type_get(long paramLong, XMLNode paramXMLNode);
/*    */ 
/*    */   public static final native String XMLNode_Value_get(long paramLong, XMLNode paramXMLNode);
/*    */ 
/*    */   public static final native long XMLNode_Next_get(long paramLong, XMLNode paramXMLNode);
/*    */ 
/*    */   public static final native long XMLNode_Child_get(long paramLong, XMLNode paramXMLNode);
/*    */ 
/*    */   public static final native long new_XMLNode__SWIG_0(String paramString);
/*    */ 
/*    */   public static final native long new_XMLNode__SWIG_1(int paramInt, String paramString);
/*    */ 
/*    */   public static final native void delete_XMLNode(long paramLong);
/*    */ 
/*    */   public static final native long XMLNode_ParseXMLFile(String paramString);
/*    */ 
/*    */   public static final native String XMLNode_SerializeXMLTree(long paramLong, XMLNode paramXMLNode);
/*    */ 
/*    */   public static final native String XMLNode_toString(long paramLong, XMLNode paramXMLNode);
/*    */ 
/*    */   public static final native long XMLNode_SearchXMLNode(long paramLong, XMLNode paramXMLNode, String paramString);
/*    */ 
/*    */   public static final native long XMLNode_GetXMLNode(long paramLong, XMLNode paramXMLNode, String paramString);
/*    */ 
/*    */   public static final native String XMLNode_GetXMLValue(long paramLong, XMLNode paramXMLNode, String paramString1, String paramString2);
/*    */ 
/*    */   public static final native void XMLNode_AddXMLChild(long paramLong1, XMLNode paramXMLNode1, long paramLong2, XMLNode paramXMLNode2);
/*    */ 
/*    */   public static final native void XMLNode_AddXMLSibling(long paramLong1, XMLNode paramXMLNode1, long paramLong2, XMLNode paramXMLNode2);
/*    */ 
/*    */   public static final native long XMLNode_Clone(long paramLong, XMLNode paramXMLNode);
/*    */ 
/*    */   public static final native int XMLNode_SetXMLValue(long paramLong, XMLNode paramXMLNode, String paramString1, String paramString2);
/*    */ 
/*    */   public static final native void XMLNode_StripXMLNamespace(long paramLong, XMLNode paramXMLNode, String paramString, int paramInt);
/*    */ 
/*    */   public static final native String MajorObject_GetDescription(long paramLong, MajorObject paramMajorObject);
/*    */ 
/*    */   public static final native void MajorObject_SetDescription(long paramLong, MajorObject paramMajorObject, String paramString);
/*    */ 
/*    */   public static final native Hashtable MajorObject_GetMetadata_Dict__SWIG_0(long paramLong, MajorObject paramMajorObject, String paramString);
/*    */ 
/*    */   public static final native Hashtable MajorObject_GetMetadata_Dict__SWIG_1(long paramLong, MajorObject paramMajorObject);
/*    */ 
/*    */   public static final native Vector MajorObject_GetMetadata_List__SWIG_0(long paramLong, MajorObject paramMajorObject, String paramString);
/*    */ 
/*    */   public static final native Vector MajorObject_GetMetadata_List__SWIG_1(long paramLong, MajorObject paramMajorObject);
/*    */ 
/*    */   public static final native int MajorObject_SetMetadata__SWIG_0(long paramLong, MajorObject paramMajorObject, Vector paramVector, String paramString);
/*    */ 
/*    */   public static final native int MajorObject_SetMetadata__SWIG_1(long paramLong, MajorObject paramMajorObject, Vector paramVector);
/*    */ 
/*    */   public static final native int MajorObject_SetMetadata__SWIG_2(long paramLong, MajorObject paramMajorObject, String paramString1, String paramString2);
/*    */ 
/*    */   public static final native int MajorObject_SetMetadata__SWIG_3(long paramLong, MajorObject paramMajorObject, String paramString);
/*    */ 
/*    */   public static final native String MajorObject_GetMetadataItem__SWIG_0(long paramLong, MajorObject paramMajorObject, String paramString1, String paramString2);
/*    */ 
/*    */   public static final native String MajorObject_GetMetadataItem__SWIG_1(long paramLong, MajorObject paramMajorObject, String paramString);
/*    */ 
/*    */   public static final native int MajorObject_SetMetadataItem__SWIG_0(long paramLong, MajorObject paramMajorObject, String paramString1, String paramString2, String paramString3);
/*    */ 
/*    */   public static final native int MajorObject_SetMetadataItem__SWIG_1(long paramLong, MajorObject paramMajorObject, String paramString1, String paramString2);
/*    */ 
/*    */   public static final native String Driver_ShortName_get(long paramLong, Driver paramDriver);
/*    */ 
/*    */   public static final native String Driver_LongName_get(long paramLong, Driver paramDriver);
/*    */ 
/*    */   public static final native String Driver_HelpTopic_get(long paramLong, Driver paramDriver);
/*    */ 
/*    */   public static final native long Driver_Create__SWIG_0(long paramLong, Driver paramDriver, String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Vector paramVector);
/*    */ 
/*    */   public static final native long Driver_Create__SWIG_1(long paramLong, Driver paramDriver, String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*    */ 
/*    */   public static final native long Driver_Create__SWIG_2(long paramLong, Driver paramDriver, String paramString, int paramInt1, int paramInt2, int paramInt3);
/*    */ 
/*    */   public static final native long Driver_Create__SWIG_3(long paramLong, Driver paramDriver, String paramString, int paramInt1, int paramInt2);
/*    */ 
/*    */   public static final native long Driver_CreateCopy__SWIG_0(long paramLong1, Driver paramDriver, String paramString, long paramLong2, Dataset paramDataset, int paramInt, Vector paramVector, ProgressCallback paramProgressCallback);
/*    */ 
/*    */   public static final native long Driver_CreateCopy__SWIG_2(long paramLong1, Driver paramDriver, String paramString, long paramLong2, Dataset paramDataset, int paramInt, Vector paramVector);
/*    */ 
/*    */   public static final native long Driver_CreateCopy__SWIG_3(long paramLong1, Driver paramDriver, String paramString, long paramLong2, Dataset paramDataset, int paramInt);
/*    */ 
/*    */   public static final native long Driver_CreateCopy__SWIG_4(long paramLong1, Driver paramDriver, String paramString, long paramLong2, Dataset paramDataset);
/*    */ 
/*    */   public static final native int Driver_Delete(long paramLong, Driver paramDriver, String paramString);
/*    */ 
/*    */   public static final native int Driver_Rename(long paramLong, Driver paramDriver, String paramString1, String paramString2);
/*    */ 
/*    */   public static final native int Driver_CopyFiles(long paramLong, Driver paramDriver, String paramString1, String paramString2);
/*    */ 
/*    */   public static final native int Driver_Register(long paramLong, Driver paramDriver);
/*    */ 
/*    */   public static final native void Driver_Deregister(long paramLong, Driver paramDriver);
/*    */ 
/*    */   public static final native void GCP_GCPX_set(long paramLong, GCP paramGCP, double paramDouble);
/*    */ 
/*    */   public static final native double GCP_GCPX_get(long paramLong, GCP paramGCP);
/*    */ 
/*    */   public static final native void GCP_GCPY_set(long paramLong, GCP paramGCP, double paramDouble);
/*    */ 
/*    */   public static final native double GCP_GCPY_get(long paramLong, GCP paramGCP);
/*    */ 
/*    */   public static final native void GCP_GCPZ_set(long paramLong, GCP paramGCP, double paramDouble);
/*    */ 
/*    */   public static final native double GCP_GCPZ_get(long paramLong, GCP paramGCP);
/*    */ 
/*    */   public static final native void GCP_GCPPixel_set(long paramLong, GCP paramGCP, double paramDouble);
/*    */ 
/*    */   public static final native double GCP_GCPPixel_get(long paramLong, GCP paramGCP);
/*    */ 
/*    */   public static final native void GCP_GCPLine_set(long paramLong, GCP paramGCP, double paramDouble);
/*    */ 
/*    */   public static final native double GCP_GCPLine_get(long paramLong, GCP paramGCP);
/*    */ 
/*    */   public static final native void GCP_Info_set(long paramLong, GCP paramGCP, String paramString);
/*    */ 
/*    */   public static final native String GCP_Info_get(long paramLong, GCP paramGCP);
/*    */ 
/*    */   public static final native void GCP_Id_set(long paramLong, GCP paramGCP, String paramString);
/*    */ 
/*    */   public static final native String GCP_Id_get(long paramLong, GCP paramGCP);
/*    */ 
/*    */   public static final native long new_GCP(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, String paramString1, String paramString2);
/*    */ 
/*    */   public static final native void delete_GCP(long paramLong);
/*    */ 
/*    */   public static final native double GDAL_GCP_GCPX_get(long paramLong, GCP paramGCP);
/*    */ 
/*    */   public static final native void GDAL_GCP_GCPX_set(long paramLong, GCP paramGCP, double paramDouble);
/*    */ 
/*    */   public static final native double GDAL_GCP_GCPY_get(long paramLong, GCP paramGCP);
/*    */ 
/*    */   public static final native void GDAL_GCP_GCPY_set(long paramLong, GCP paramGCP, double paramDouble);
/*    */ 
/*    */   public static final native double GDAL_GCP_GCPZ_get(long paramLong, GCP paramGCP);
/*    */ 
/*    */   public static final native void GDAL_GCP_GCPZ_set(long paramLong, GCP paramGCP, double paramDouble);
/*    */ 
/*    */   public static final native double GDAL_GCP_GCPPixel_get(long paramLong, GCP paramGCP);
/*    */ 
/*    */   public static final native void GDAL_GCP_GCPPixel_set(long paramLong, GCP paramGCP, double paramDouble);
/*    */ 
/*    */   public static final native double GDAL_GCP_GCPLine_get(long paramLong, GCP paramGCP);
/*    */ 
/*    */   public static final native void GDAL_GCP_GCPLine_set(long paramLong, GCP paramGCP, double paramDouble);
/*    */ 
/*    */   public static final native String GDAL_GCP_Info_get(long paramLong, GCP paramGCP);
/*    */ 
/*    */   public static final native void GDAL_GCP_Info_set(long paramLong, GCP paramGCP, String paramString);
/*    */ 
/*    */   public static final native String GDAL_GCP_Id_get(long paramLong, GCP paramGCP);
/*    */ 
/*    */   public static final native void GDAL_GCP_Id_set(long paramLong, GCP paramGCP, String paramString);
/*    */ 
/*    */   public static final native double GDAL_GCP_get_GCPX(long paramLong, GCP paramGCP);
/*    */ 
/*    */   public static final native void GDAL_GCP_set_GCPX(long paramLong, GCP paramGCP, double paramDouble);
/*    */ 
/*    */   public static final native double GDAL_GCP_get_GCPY(long paramLong, GCP paramGCP);
/*    */ 
/*    */   public static final native void GDAL_GCP_set_GCPY(long paramLong, GCP paramGCP, double paramDouble);
/*    */ 
/*    */   public static final native double GDAL_GCP_get_GCPZ(long paramLong, GCP paramGCP);
/*    */ 
/*    */   public static final native void GDAL_GCP_set_GCPZ(long paramLong, GCP paramGCP, double paramDouble);
/*    */ 
/*    */   public static final native double GDAL_GCP_get_GCPPixel(long paramLong, GCP paramGCP);
/*    */ 
/*    */   public static final native void GDAL_GCP_set_GCPPixel(long paramLong, GCP paramGCP, double paramDouble);
/*    */ 
/*    */   public static final native double GDAL_GCP_get_GCPLine(long paramLong, GCP paramGCP);
/*    */ 
/*    */   public static final native void GDAL_GCP_set_GCPLine(long paramLong, GCP paramGCP, double paramDouble);
/*    */ 
/*    */   public static final native String GDAL_GCP_get_Info(long paramLong, GCP paramGCP);
/*    */ 
/*    */   public static final native void GDAL_GCP_set_Info(long paramLong, GCP paramGCP, String paramString);
/*    */ 
/*    */   public static final native String GDAL_GCP_get_Id(long paramLong, GCP paramGCP);
/*    */ 
/*    */   public static final native void GDAL_GCP_set_Id(long paramLong, GCP paramGCP, String paramString);
/*    */ 
/*    */   public static final native int GCPsToGeoTransform__SWIG_0(GCP[] paramArrayOfGCP, double[] paramArrayOfDouble, int paramInt);
/*    */ 
/*    */   public static final native int GCPsToGeoTransform__SWIG_1(GCP[] paramArrayOfGCP, double[] paramArrayOfDouble);
/*    */ 
/*    */   public static final native void delete_AsyncReader(long paramLong);
/*    */ 
/*    */   public static final native int AsyncReader_GetNextUpdatedRegion(long paramLong, AsyncReader paramAsyncReader, double paramDouble, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3, int[] paramArrayOfInt4);
/*    */ 
/*    */   public static final native int AsyncReader_LockBuffer(long paramLong, AsyncReader paramAsyncReader, double paramDouble);
/*    */ 
/*    */   public static final native void AsyncReader_UnlockBuffer(long paramLong, AsyncReader paramAsyncReader);
/*    */ 
/*    */   public static final native int Dataset_RasterXSize_get(long paramLong, Dataset paramDataset);
/*    */ 
/*    */   public static final native int Dataset_RasterYSize_get(long paramLong, Dataset paramDataset);
/*    */ 
/*    */   public static final native int Dataset_RasterCount_get(long paramLong, Dataset paramDataset);
/*    */ 
/*    */   public static final native void delete_Dataset(long paramLong);
/*    */ 
/*    */   public static final native long Dataset_GetDriver(long paramLong, Dataset paramDataset);
/*    */ 
/*    */   public static final native long Dataset_GetRasterBand(long paramLong, Dataset paramDataset, int paramInt);
/*    */ 
/*    */   public static final native String Dataset_GetProjection(long paramLong, Dataset paramDataset);
/*    */ 
/*    */   public static final native String Dataset_GetProjectionRef(long paramLong, Dataset paramDataset);
/*    */ 
/*    */   public static final native int Dataset_SetProjection(long paramLong, Dataset paramDataset, String paramString);
/*    */ 
/*    */   public static final native void Dataset_GetGeoTransform(long paramLong, Dataset paramDataset, double[] paramArrayOfDouble);
/*    */ 
/*    */   public static final native int Dataset_SetGeoTransform(long paramLong, Dataset paramDataset, double[] paramArrayOfDouble);
/*    */ 
/*    */   public static final native int Dataset_BuildOverviews__SWIG_0(long paramLong, Dataset paramDataset, String paramString, int[] paramArrayOfInt, ProgressCallback paramProgressCallback);
/*    */ 
/*    */   public static final native int Dataset_BuildOverviews__SWIG_2(long paramLong, Dataset paramDataset, String paramString, int[] paramArrayOfInt);
/*    */ 
/*    */   public static final native int Dataset_GetGCPCount(long paramLong, Dataset paramDataset);
/*    */ 
/*    */   public static final native String Dataset_GetGCPProjection(long paramLong, Dataset paramDataset);
/*    */ 
/*    */   public static final native void Dataset_GetGCPs(long paramLong, Dataset paramDataset, Vector paramVector);
/*    */ 
/*    */   public static final native int Dataset_SetGCPs(long paramLong, Dataset paramDataset, GCP[] paramArrayOfGCP, String paramString);
/*    */ 
/*    */   public static final native void Dataset_FlushCache(long paramLong, Dataset paramDataset);
/*    */ 
/*    */   public static final native int Dataset_AddBand__SWIG_0(long paramLong, Dataset paramDataset, int paramInt, Vector paramVector);
/*    */ 
/*    */   public static final native int Dataset_AddBand__SWIG_1(long paramLong, Dataset paramDataset, int paramInt);
/*    */ 
/*    */   public static final native int Dataset_AddBand__SWIG_2(long paramLong, Dataset paramDataset);
/*    */ 
/*    */   public static final native int Dataset_CreateMaskBand(long paramLong, Dataset paramDataset, int paramInt);
/*    */ 
/*    */   public static final native Vector Dataset_GetFileList(long paramLong, Dataset paramDataset);
/*    */ 
/*    */   public static final native int Dataset_ReadRaster_Direct__SWIG_0(long paramLong, Dataset paramDataset, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, ByteBuffer paramByteBuffer, int[] paramArrayOfInt, int paramInt8, int paramInt9, int paramInt10);
/*    */ 
/*    */   public static final native int Dataset_ReadRaster_Direct__SWIG_1(long paramLong, Dataset paramDataset, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, ByteBuffer paramByteBuffer, int[] paramArrayOfInt, int paramInt8, int paramInt9);
/*    */ 
/*    */   public static final native int Dataset_ReadRaster_Direct__SWIG_2(long paramLong, Dataset paramDataset, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, ByteBuffer paramByteBuffer, int[] paramArrayOfInt, int paramInt8);
/*    */ 
/*    */   public static final native int Dataset_ReadRaster_Direct__SWIG_3(long paramLong, Dataset paramDataset, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, ByteBuffer paramByteBuffer, int[] paramArrayOfInt);
/*    */ 
/*    */   public static final native int Dataset_ReadRaster__SWIG_0(long paramLong, Dataset paramDataset, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, byte[] paramArrayOfByte, int[] paramArrayOfInt, int paramInt8, int paramInt9, int paramInt10);
/*    */ 
/*    */   public static final native int Dataset_ReadRaster__SWIG_1(long paramLong, Dataset paramDataset, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, byte[] paramArrayOfByte, int[] paramArrayOfInt, int paramInt8, int paramInt9);
/*    */ 
/*    */   public static final native int Dataset_ReadRaster__SWIG_2(long paramLong, Dataset paramDataset, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, byte[] paramArrayOfByte, int[] paramArrayOfInt, int paramInt8);
/*    */ 
/*    */   public static final native int Dataset_ReadRaster__SWIG_3(long paramLong, Dataset paramDataset, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, byte[] paramArrayOfByte, int[] paramArrayOfInt);
/*    */ 
/*    */   public static final native int Dataset_ReadRaster__SWIG_4(long paramLong, Dataset paramDataset, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, short[] paramArrayOfShort, int[] paramArrayOfInt, int paramInt8, int paramInt9, int paramInt10);
/*    */ 
/*    */   public static final native int Dataset_ReadRaster__SWIG_5(long paramLong, Dataset paramDataset, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, short[] paramArrayOfShort, int[] paramArrayOfInt, int paramInt8, int paramInt9);
/*    */ 
/*    */   public static final native int Dataset_ReadRaster__SWIG_6(long paramLong, Dataset paramDataset, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, short[] paramArrayOfShort, int[] paramArrayOfInt, int paramInt8);
/*    */ 
/*    */   public static final native int Dataset_ReadRaster__SWIG_7(long paramLong, Dataset paramDataset, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, short[] paramArrayOfShort, int[] paramArrayOfInt);
/*    */ 
/*    */   public static final native int Dataset_ReadRaster__SWIG_8(long paramLong, Dataset paramDataset, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt8, int paramInt9, int paramInt10);
/*    */ 
/*    */   public static final native int Dataset_ReadRaster__SWIG_9(long paramLong, Dataset paramDataset, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt8, int paramInt9);
/*    */ 
/*    */   public static final native int Dataset_ReadRaster__SWIG_10(long paramLong, Dataset paramDataset, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt8);
/*    */ 
/*    */   public static final native int Dataset_ReadRaster__SWIG_11(long paramLong, Dataset paramDataset, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int[] paramArrayOfInt1, int[] paramArrayOfInt2);
/*    */ 
/*    */   public static final native int Dataset_ReadRaster__SWIG_12(long paramLong, Dataset paramDataset, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, float[] paramArrayOfFloat, int[] paramArrayOfInt, int paramInt8, int paramInt9, int paramInt10);
/*    */ 
/*    */   public static final native int Dataset_ReadRaster__SWIG_13(long paramLong, Dataset paramDataset, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, float[] paramArrayOfFloat, int[] paramArrayOfInt, int paramInt8, int paramInt9);
/*    */ 
/*    */   public static final native int Dataset_ReadRaster__SWIG_14(long paramLong, Dataset paramDataset, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, float[] paramArrayOfFloat, int[] paramArrayOfInt, int paramInt8);
/*    */ 
/*    */   public static final native int Dataset_ReadRaster__SWIG_15(long paramLong, Dataset paramDataset, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, float[] paramArrayOfFloat, int[] paramArrayOfInt);
/*    */ 
/*    */   public static final native int Dataset_ReadRaster__SWIG_16(long paramLong, Dataset paramDataset, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, double[] paramArrayOfDouble, int[] paramArrayOfInt, int paramInt8, int paramInt9, int paramInt10);
/*    */ 
/*    */   public static final native int Dataset_ReadRaster__SWIG_17(long paramLong, Dataset paramDataset, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, double[] paramArrayOfDouble, int[] paramArrayOfInt, int paramInt8, int paramInt9);
/*    */ 
/*    */   public static final native int Dataset_ReadRaster__SWIG_18(long paramLong, Dataset paramDataset, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, double[] paramArrayOfDouble, int[] paramArrayOfInt, int paramInt8);
/*    */ 
/*    */   public static final native int Dataset_ReadRaster__SWIG_19(long paramLong, Dataset paramDataset, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, double[] paramArrayOfDouble, int[] paramArrayOfInt);
/*    */ 
/*    */   public static final native int Dataset_WriteRaster_Direct__SWIG_0(long paramLong, Dataset paramDataset, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, ByteBuffer paramByteBuffer, int[] paramArrayOfInt, int paramInt8, int paramInt9, int paramInt10);
/*    */ 
/*    */   public static final native int Dataset_WriteRaster_Direct__SWIG_1(long paramLong, Dataset paramDataset, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, ByteBuffer paramByteBuffer, int[] paramArrayOfInt, int paramInt8, int paramInt9);
/*    */ 
/*    */   public static final native int Dataset_WriteRaster_Direct__SWIG_2(long paramLong, Dataset paramDataset, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, ByteBuffer paramByteBuffer, int[] paramArrayOfInt, int paramInt8);
/*    */ 
/*    */   public static final native int Dataset_WriteRaster_Direct__SWIG_3(long paramLong, Dataset paramDataset, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, ByteBuffer paramByteBuffer, int[] paramArrayOfInt);
/*    */ 
/*    */   public static final native int Dataset_WriteRaster__SWIG_0(long paramLong, Dataset paramDataset, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, byte[] paramArrayOfByte, int[] paramArrayOfInt, int paramInt8, int paramInt9, int paramInt10);
/*    */ 
/*    */   public static final native int Dataset_WriteRaster__SWIG_1(long paramLong, Dataset paramDataset, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, byte[] paramArrayOfByte, int[] paramArrayOfInt, int paramInt8, int paramInt9);
/*    */ 
/*    */   public static final native int Dataset_WriteRaster__SWIG_2(long paramLong, Dataset paramDataset, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, byte[] paramArrayOfByte, int[] paramArrayOfInt, int paramInt8);
/*    */ 
/*    */   public static final native int Dataset_WriteRaster__SWIG_3(long paramLong, Dataset paramDataset, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, byte[] paramArrayOfByte, int[] paramArrayOfInt);
/*    */ 
/*    */   public static final native int Dataset_WriteRaster__SWIG_4(long paramLong, Dataset paramDataset, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, short[] paramArrayOfShort, int[] paramArrayOfInt, int paramInt8, int paramInt9, int paramInt10);
/*    */ 
/*    */   public static final native int Dataset_WriteRaster__SWIG_5(long paramLong, Dataset paramDataset, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, short[] paramArrayOfShort, int[] paramArrayOfInt, int paramInt8, int paramInt9);
/*    */ 
/*    */   public static final native int Dataset_WriteRaster__SWIG_6(long paramLong, Dataset paramDataset, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, short[] paramArrayOfShort, int[] paramArrayOfInt, int paramInt8);
/*    */ 
/*    */   public static final native int Dataset_WriteRaster__SWIG_7(long paramLong, Dataset paramDataset, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, short[] paramArrayOfShort, int[] paramArrayOfInt);
/*    */ 
/*    */   public static final native int Dataset_WriteRaster__SWIG_8(long paramLong, Dataset paramDataset, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt8, int paramInt9, int paramInt10);
/*    */ 
/*    */   public static final native int Dataset_WriteRaster__SWIG_9(long paramLong, Dataset paramDataset, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt8, int paramInt9);
/*    */ 
/*    */   public static final native int Dataset_WriteRaster__SWIG_10(long paramLong, Dataset paramDataset, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt8);
/*    */ 
/*    */   public static final native int Dataset_WriteRaster__SWIG_11(long paramLong, Dataset paramDataset, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int[] paramArrayOfInt1, int[] paramArrayOfInt2);
/*    */ 
/*    */   public static final native int Dataset_WriteRaster__SWIG_12(long paramLong, Dataset paramDataset, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, float[] paramArrayOfFloat, int[] paramArrayOfInt, int paramInt8, int paramInt9, int paramInt10);
/*    */ 
/*    */   public static final native int Dataset_WriteRaster__SWIG_13(long paramLong, Dataset paramDataset, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, float[] paramArrayOfFloat, int[] paramArrayOfInt, int paramInt8, int paramInt9);
/*    */ 
/*    */   public static final native int Dataset_WriteRaster__SWIG_14(long paramLong, Dataset paramDataset, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, float[] paramArrayOfFloat, int[] paramArrayOfInt, int paramInt8);
/*    */ 
/*    */   public static final native int Dataset_WriteRaster__SWIG_15(long paramLong, Dataset paramDataset, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, float[] paramArrayOfFloat, int[] paramArrayOfInt);
/*    */ 
/*    */   public static final native int Dataset_WriteRaster__SWIG_16(long paramLong, Dataset paramDataset, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, double[] paramArrayOfDouble, int[] paramArrayOfInt, int paramInt8, int paramInt9, int paramInt10);
/*    */ 
/*    */   public static final native int Dataset_WriteRaster__SWIG_17(long paramLong, Dataset paramDataset, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, double[] paramArrayOfDouble, int[] paramArrayOfInt, int paramInt8, int paramInt9);
/*    */ 
/*    */   public static final native int Dataset_WriteRaster__SWIG_18(long paramLong, Dataset paramDataset, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, double[] paramArrayOfDouble, int[] paramArrayOfInt, int paramInt8);
/*    */ 
/*    */   public static final native int Dataset_WriteRaster__SWIG_19(long paramLong, Dataset paramDataset, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, double[] paramArrayOfDouble, int[] paramArrayOfInt);
/*    */ 
/*    */   public static final native int Band_XSize_get(long paramLong, Band paramBand);
/*    */ 
/*    */   public static final native int Band_YSize_get(long paramLong, Band paramBand);
/*    */ 
/*    */   public static final native int Band_DataType_get(long paramLong, Band paramBand);
/*    */ 
/*    */   public static final native int Band_GetBand(long paramLong, Band paramBand);
/*    */ 
/*    */   public static final native void Band_GetBlockSize(long paramLong, Band paramBand, int[] paramArrayOfInt1, int[] paramArrayOfInt2);
/*    */ 
/*    */   public static final native int Band_GetColorInterpretation(long paramLong, Band paramBand);
/*    */ 
/*    */   public static final native int Band_GetRasterColorInterpretation(long paramLong, Band paramBand);
/*    */ 
/*    */   public static final native int Band_SetColorInterpretation(long paramLong, Band paramBand, int paramInt);
/*    */ 
/*    */   public static final native int Band_SetRasterColorInterpretation(long paramLong, Band paramBand, int paramInt);
/*    */ 
/*    */   public static final native void Band_GetNoDataValue(long paramLong, Band paramBand, Double[] paramArrayOfDouble);
/*    */ 
/*    */   public static final native int Band_SetNoDataValue(long paramLong, Band paramBand, double paramDouble);
/*    */ 
/*    */   public static final native String Band_GetUnitType(long paramLong, Band paramBand);
/*    */ 
/*    */   public static final native int Band_SetUnitType(long paramLong, Band paramBand, String paramString);
/*    */ 
/*    */   public static final native Vector Band_GetRasterCategoryNames(long paramLong, Band paramBand);
/*    */ 
/*    */   public static final native int Band_SetRasterCategoryNames(long paramLong, Band paramBand, Vector paramVector);
/*    */ 
/*    */   public static final native void Band_GetMinimum(long paramLong, Band paramBand, Double[] paramArrayOfDouble);
/*    */ 
/*    */   public static final native void Band_GetMaximum(long paramLong, Band paramBand, Double[] paramArrayOfDouble);
/*    */ 
/*    */   public static final native void Band_GetOffset(long paramLong, Band paramBand, Double[] paramArrayOfDouble);
/*    */ 
/*    */   public static final native void Band_GetScale(long paramLong, Band paramBand, Double[] paramArrayOfDouble);
/*    */ 
/*    */   public static final native int Band_SetOffset(long paramLong, Band paramBand, double paramDouble);
/*    */ 
/*    */   public static final native int Band_SetScale(long paramLong, Band paramBand, double paramDouble);
/*    */ 
/*    */   public static final native int Band_GetStatistics(long paramLong, Band paramBand, int paramInt1, int paramInt2, double[] paramArrayOfDouble1, double[] paramArrayOfDouble2, double[] paramArrayOfDouble3, double[] paramArrayOfDouble4);
/*    */ 
/*    */   public static final native int Band_ComputeStatistics__SWIG_0(long paramLong, Band paramBand, boolean paramBoolean, double[] paramArrayOfDouble1, double[] paramArrayOfDouble2, double[] paramArrayOfDouble3, double[] paramArrayOfDouble4, ProgressCallback paramProgressCallback);
/*    */ 
/*    */   public static final native int Band_ComputeStatistics__SWIG_2(long paramLong, Band paramBand, boolean paramBoolean, double[] paramArrayOfDouble1, double[] paramArrayOfDouble2, double[] paramArrayOfDouble3, double[] paramArrayOfDouble4);
/*    */ 
/*    */   public static final native int Band_ComputeStatistics__SWIG_3(long paramLong, Band paramBand, boolean paramBoolean, double[] paramArrayOfDouble1, double[] paramArrayOfDouble2, double[] paramArrayOfDouble3);
/*    */ 
/*    */   public static final native int Band_ComputeStatistics__SWIG_4(long paramLong, Band paramBand, boolean paramBoolean, double[] paramArrayOfDouble1, double[] paramArrayOfDouble2);
/*    */ 
/*    */   public static final native int Band_ComputeStatistics__SWIG_5(long paramLong, Band paramBand, boolean paramBoolean, double[] paramArrayOfDouble);
/*    */ 
/*    */   public static final native int Band_ComputeStatistics__SWIG_6(long paramLong, Band paramBand, boolean paramBoolean);
/*    */ 
/*    */   public static final native int Band_SetStatistics(long paramLong, Band paramBand, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4);
/*    */ 
/*    */   public static final native int Band_GetOverviewCount(long paramLong, Band paramBand);
/*    */ 
/*    */   public static final native long Band_GetOverview(long paramLong, Band paramBand, int paramInt);
/*    */ 
/*    */   public static final native int Band_Checksum(long paramLong, Band paramBand, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*    */ 
/*    */   public static final native void Band_ComputeRasterMinMax__SWIG_0(long paramLong, Band paramBand, double[] paramArrayOfDouble, int paramInt);
/*    */ 
/*    */   public static final native void Band_ComputeRasterMinMax__SWIG_1(long paramLong, Band paramBand, double[] paramArrayOfDouble);
/*    */ 
/*    */   public static final native void Band_ComputeBandStats__SWIG_0(long paramLong, Band paramBand, double[] paramArrayOfDouble, int paramInt);
/*    */ 
/*    */   public static final native void Band_ComputeBandStats__SWIG_1(long paramLong, Band paramBand, double[] paramArrayOfDouble);
/*    */ 
/*    */   public static final native int Band_Fill__SWIG_0(long paramLong, Band paramBand, double paramDouble1, double paramDouble2);
/*    */ 
/*    */   public static final native int Band_Fill__SWIG_1(long paramLong, Band paramBand, double paramDouble);
/*    */ 
/*    */   public static final native void Band_FlushCache(long paramLong, Band paramBand);
/*    */ 
/*    */   public static final native long Band_GetRasterColorTable(long paramLong, Band paramBand);
/*    */ 
/*    */   public static final native long Band_GetColorTable(long paramLong, Band paramBand);
/*    */ 
/*    */   public static final native int Band_SetRasterColorTable(long paramLong1, Band paramBand, long paramLong2, ColorTable paramColorTable);
/*    */ 
/*    */   public static final native int Band_SetColorTable(long paramLong1, Band paramBand, long paramLong2, ColorTable paramColorTable);
/*    */ 
/*    */   public static final native long Band_GetDefaultRAT(long paramLong, Band paramBand);
/*    */ 
/*    */   public static final native int Band_SetDefaultRAT(long paramLong1, Band paramBand, long paramLong2, RasterAttributeTable paramRasterAttributeTable);
/*    */ 
/*    */   public static final native long Band_GetMaskBand(long paramLong, Band paramBand);
/*    */ 
/*    */   public static final native int Band_GetMaskFlags(long paramLong, Band paramBand);
/*    */ 
/*    */   public static final native int Band_CreateMaskBand(long paramLong, Band paramBand, int paramInt);
/*    */ 
/*    */   public static final native int Band_SetDefaultHistogram(long paramLong, Band paramBand, double paramDouble1, double paramDouble2, int[] paramArrayOfInt);
/*    */ 
/*    */   public static final native boolean Band_HasArbitraryOverviews(long paramLong, Band paramBand);
/*    */ 
/*    */   public static final native Vector Band_GetCategoryNames(long paramLong, Band paramBand);
/*    */ 
/*    */   public static final native int Band_SetCategoryNames(long paramLong, Band paramBand, Vector paramVector);
/*    */ 
/*    */   public static final native int Band_ReadRaster_Direct__SWIG_0(long paramLong, Band paramBand, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, ByteBuffer paramByteBuffer, int paramInt8, int paramInt9);
/*    */ 
/*    */   public static final native int Band_ReadRaster_Direct__SWIG_1(long paramLong, Band paramBand, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, ByteBuffer paramByteBuffer, int paramInt8);
/*    */ 
/*    */   public static final native int Band_ReadRaster_Direct__SWIG_2(long paramLong, Band paramBand, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, ByteBuffer paramByteBuffer);
/*    */ 
/*    */   public static final native int Band_ReadRaster__SWIG_0(long paramLong, Band paramBand, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, byte[] paramArrayOfByte, int paramInt8, int paramInt9);
/*    */ 
/*    */   public static final native int Band_ReadRaster__SWIG_1(long paramLong, Band paramBand, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, byte[] paramArrayOfByte, int paramInt8);
/*    */ 
/*    */   public static final native int Band_ReadRaster__SWIG_2(long paramLong, Band paramBand, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, byte[] paramArrayOfByte);
/*    */ 
/*    */   public static final native int Band_ReadRaster__SWIG_3(long paramLong, Band paramBand, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, short[] paramArrayOfShort, int paramInt8, int paramInt9);
/*    */ 
/*    */   public static final native int Band_ReadRaster__SWIG_4(long paramLong, Band paramBand, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, short[] paramArrayOfShort, int paramInt8);
/*    */ 
/*    */   public static final native int Band_ReadRaster__SWIG_5(long paramLong, Band paramBand, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, short[] paramArrayOfShort);
/*    */ 
/*    */   public static final native int Band_ReadRaster__SWIG_6(long paramLong, Band paramBand, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int[] paramArrayOfInt, int paramInt8, int paramInt9);
/*    */ 
/*    */   public static final native int Band_ReadRaster__SWIG_7(long paramLong, Band paramBand, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int[] paramArrayOfInt, int paramInt8);
/*    */ 
/*    */   public static final native int Band_ReadRaster__SWIG_8(long paramLong, Band paramBand, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int[] paramArrayOfInt);
/*    */ 
/*    */   public static final native int Band_ReadRaster__SWIG_9(long paramLong, Band paramBand, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, float[] paramArrayOfFloat, int paramInt8, int paramInt9);
/*    */ 
/*    */   public static final native int Band_ReadRaster__SWIG_10(long paramLong, Band paramBand, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, float[] paramArrayOfFloat, int paramInt8);
/*    */ 
/*    */   public static final native int Band_ReadRaster__SWIG_11(long paramLong, Band paramBand, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, float[] paramArrayOfFloat);
/*    */ 
/*    */   public static final native int Band_ReadRaster__SWIG_12(long paramLong, Band paramBand, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, double[] paramArrayOfDouble, int paramInt8, int paramInt9);
/*    */ 
/*    */   public static final native int Band_ReadRaster__SWIG_13(long paramLong, Band paramBand, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, double[] paramArrayOfDouble, int paramInt8);
/*    */ 
/*    */   public static final native int Band_ReadRaster__SWIG_14(long paramLong, Band paramBand, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, double[] paramArrayOfDouble);
/*    */ 
/*    */   public static final native int Band_WriteRaster_Direct__SWIG_0(long paramLong, Band paramBand, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, ByteBuffer paramByteBuffer, int paramInt8, int paramInt9);
/*    */ 
/*    */   public static final native int Band_WriteRaster_Direct__SWIG_1(long paramLong, Band paramBand, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, ByteBuffer paramByteBuffer, int paramInt8);
/*    */ 
/*    */   public static final native int Band_WriteRaster_Direct__SWIG_2(long paramLong, Band paramBand, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, ByteBuffer paramByteBuffer);
/*    */ 
/*    */   public static final native int Band_WriteRaster__SWIG_0(long paramLong, Band paramBand, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, byte[] paramArrayOfByte, int paramInt8, int paramInt9);
/*    */ 
/*    */   public static final native int Band_WriteRaster__SWIG_1(long paramLong, Band paramBand, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, byte[] paramArrayOfByte, int paramInt8);
/*    */ 
/*    */   public static final native int Band_WriteRaster__SWIG_2(long paramLong, Band paramBand, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, byte[] paramArrayOfByte);
/*    */ 
/*    */   public static final native int Band_WriteRaster__SWIG_3(long paramLong, Band paramBand, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, short[] paramArrayOfShort, int paramInt8, int paramInt9);
/*    */ 
/*    */   public static final native int Band_WriteRaster__SWIG_4(long paramLong, Band paramBand, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, short[] paramArrayOfShort, int paramInt8);
/*    */ 
/*    */   public static final native int Band_WriteRaster__SWIG_5(long paramLong, Band paramBand, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, short[] paramArrayOfShort);
/*    */ 
/*    */   public static final native int Band_WriteRaster__SWIG_6(long paramLong, Band paramBand, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int[] paramArrayOfInt, int paramInt8, int paramInt9);
/*    */ 
/*    */   public static final native int Band_WriteRaster__SWIG_7(long paramLong, Band paramBand, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int[] paramArrayOfInt, int paramInt8);
/*    */ 
/*    */   public static final native int Band_WriteRaster__SWIG_8(long paramLong, Band paramBand, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int[] paramArrayOfInt);
/*    */ 
/*    */   public static final native int Band_WriteRaster__SWIG_9(long paramLong, Band paramBand, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, float[] paramArrayOfFloat, int paramInt8, int paramInt9);
/*    */ 
/*    */   public static final native int Band_WriteRaster__SWIG_10(long paramLong, Band paramBand, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, float[] paramArrayOfFloat, int paramInt8);
/*    */ 
/*    */   public static final native int Band_WriteRaster__SWIG_11(long paramLong, Band paramBand, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, float[] paramArrayOfFloat);
/*    */ 
/*    */   public static final native int Band_WriteRaster__SWIG_12(long paramLong, Band paramBand, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, double[] paramArrayOfDouble, int paramInt8, int paramInt9);
/*    */ 
/*    */   public static final native int Band_WriteRaster__SWIG_13(long paramLong, Band paramBand, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, double[] paramArrayOfDouble, int paramInt8);
/*    */ 
/*    */   public static final native int Band_WriteRaster__SWIG_14(long paramLong, Band paramBand, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, double[] paramArrayOfDouble);
/*    */ 
/*    */   public static final native int Band_ReadBlock_Direct(long paramLong, Band paramBand, int paramInt1, int paramInt2, ByteBuffer paramByteBuffer);
/*    */ 
/*    */   public static final native int Band_WriteBlock_Direct(long paramLong, Band paramBand, int paramInt1, int paramInt2, ByteBuffer paramByteBuffer);
/*    */ 
/*    */   public static final native int Band_GetHistogram__SWIG_0(long paramLong, Band paramBand, double paramDouble1, double paramDouble2, int[] paramArrayOfInt, boolean paramBoolean1, boolean paramBoolean2, ProgressCallback paramProgressCallback);
/*    */ 
/*    */   public static final native int Band_GetHistogram__SWIG_1(long paramLong, Band paramBand, double paramDouble1, double paramDouble2, int[] paramArrayOfInt, boolean paramBoolean1, boolean paramBoolean2);
/*    */ 
/*    */   public static final native int Band_GetHistogram__SWIG_2(long paramLong, Band paramBand, double paramDouble1, double paramDouble2, int[] paramArrayOfInt);
/*    */ 
/*    */   public static final native int Band_GetHistogram__SWIG_3(long paramLong, Band paramBand, int[] paramArrayOfInt);
/*    */ 
/*    */   public static final native int Band_GetDefaultHistogram__SWIG_0(long paramLong, Band paramBand, double[] paramArrayOfDouble1, double[] paramArrayOfDouble2, int[][] paramArrayOfInt, boolean paramBoolean, ProgressCallback paramProgressCallback);
/*    */ 
/*    */   public static final native int Band_GetDefaultHistogram__SWIG_2(long paramLong, Band paramBand, double[] paramArrayOfDouble1, double[] paramArrayOfDouble2, int[][] paramArrayOfInt, boolean paramBoolean);
/*    */ 
/*    */   public static final native int Band_GetDefaultHistogram__SWIG_3(long paramLong, Band paramBand, double[] paramArrayOfDouble1, double[] paramArrayOfDouble2, int[][] paramArrayOfInt);
/*    */ 
/*    */   public static final native long new_ColorTable__SWIG_0(int paramInt);
/*    */ 
/*    */   public static final native long new_ColorTable__SWIG_1();
/*    */ 
/*    */   public static final native void delete_ColorTable(long paramLong);
/*    */ 
/*    */   public static final native long ColorTable_Clone(long paramLong, ColorTable paramColorTable);
/*    */ 
/*    */   public static final native int ColorTable_GetPaletteInterpretation(long paramLong, ColorTable paramColorTable);
/*    */ 
/*    */   public static final native int ColorTable_GetCount(long paramLong, ColorTable paramColorTable);
/*    */ 
/*    */   public static final native Color ColorTable_GetColorEntry(long paramLong, ColorTable paramColorTable, int paramInt);
/*    */ 
/*    */   public static final native void ColorTable_SetColorEntry(long paramLong, ColorTable paramColorTable, int paramInt, Color paramColor);
/*    */ 
/*    */   public static final native void ColorTable_CreateColorRamp(long paramLong, ColorTable paramColorTable, int paramInt1, Color paramColor1, int paramInt2, Color paramColor2);
/*    */ 
/*    */   public static final native long new_RasterAttributeTable();
/*    */ 
/*    */   public static final native void delete_RasterAttributeTable(long paramLong);
/*    */ 
/*    */   public static final native long RasterAttributeTable_Clone(long paramLong, RasterAttributeTable paramRasterAttributeTable);
/*    */ 
/*    */   public static final native int RasterAttributeTable_GetColumnCount(long paramLong, RasterAttributeTable paramRasterAttributeTable);
/*    */ 
/*    */   public static final native String RasterAttributeTable_GetNameOfCol(long paramLong, RasterAttributeTable paramRasterAttributeTable, int paramInt);
/*    */ 
/*    */   public static final native int RasterAttributeTable_GetUsageOfCol(long paramLong, RasterAttributeTable paramRasterAttributeTable, int paramInt);
/*    */ 
/*    */   public static final native int RasterAttributeTable_GetTypeOfCol(long paramLong, RasterAttributeTable paramRasterAttributeTable, int paramInt);
/*    */ 
/*    */   public static final native int RasterAttributeTable_GetColOfUsage(long paramLong, RasterAttributeTable paramRasterAttributeTable, int paramInt);
/*    */ 
/*    */   public static final native int RasterAttributeTable_GetRowCount(long paramLong, RasterAttributeTable paramRasterAttributeTable);
/*    */ 
/*    */   public static final native String RasterAttributeTable_GetValueAsString(long paramLong, RasterAttributeTable paramRasterAttributeTable, int paramInt1, int paramInt2);
/*    */ 
/*    */   public static final native int RasterAttributeTable_GetValueAsInt(long paramLong, RasterAttributeTable paramRasterAttributeTable, int paramInt1, int paramInt2);
/*    */ 
/*    */   public static final native double RasterAttributeTable_GetValueAsDouble(long paramLong, RasterAttributeTable paramRasterAttributeTable, int paramInt1, int paramInt2);
/*    */ 
/*    */   public static final native void RasterAttributeTable_SetValueAsString(long paramLong, RasterAttributeTable paramRasterAttributeTable, int paramInt1, int paramInt2, String paramString);
/*    */ 
/*    */   public static final native void RasterAttributeTable_SetValueAsInt(long paramLong, RasterAttributeTable paramRasterAttributeTable, int paramInt1, int paramInt2, int paramInt3);
/*    */ 
/*    */   public static final native void RasterAttributeTable_SetValueAsDouble(long paramLong, RasterAttributeTable paramRasterAttributeTable, int paramInt1, int paramInt2, double paramDouble);
/*    */ 
/*    */   public static final native void RasterAttributeTable_SetRowCount(long paramLong, RasterAttributeTable paramRasterAttributeTable, int paramInt);
/*    */ 
/*    */   public static final native int RasterAttributeTable_CreateColumn(long paramLong, RasterAttributeTable paramRasterAttributeTable, String paramString, int paramInt1, int paramInt2);
/*    */ 
/*    */   public static final native boolean RasterAttributeTable_GetLinearBinning(long paramLong, RasterAttributeTable paramRasterAttributeTable, double[] paramArrayOfDouble1, double[] paramArrayOfDouble2);
/*    */ 
/*    */   public static final native int RasterAttributeTable_SetLinearBinning(long paramLong, RasterAttributeTable paramRasterAttributeTable, double paramDouble1, double paramDouble2);
/*    */ 
/*    */   public static final native int RasterAttributeTable_GetRowOfValue(long paramLong, RasterAttributeTable paramRasterAttributeTable, double paramDouble);
/*    */ 
/*    */   public static final native int ComputeMedianCutPCT__SWIG_0(long paramLong1, Band paramBand1, long paramLong2, Band paramBand2, long paramLong3, Band paramBand3, int paramInt, long paramLong4, ColorTable paramColorTable, ProgressCallback paramProgressCallback);
/*    */ 
/*    */   public static final native int ComputeMedianCutPCT__SWIG_2(long paramLong1, Band paramBand1, long paramLong2, Band paramBand2, long paramLong3, Band paramBand3, int paramInt, long paramLong4, ColorTable paramColorTable);
/*    */ 
/*    */   public static final native int DitherRGB2PCT__SWIG_0(long paramLong1, Band paramBand1, long paramLong2, Band paramBand2, long paramLong3, Band paramBand3, long paramLong4, Band paramBand4, long paramLong5, ColorTable paramColorTable, ProgressCallback paramProgressCallback);
/*    */ 
/*    */   public static final native int DitherRGB2PCT__SWIG_2(long paramLong1, Band paramBand1, long paramLong2, Band paramBand2, long paramLong3, Band paramBand3, long paramLong4, Band paramBand4, long paramLong5, ColorTable paramColorTable);
/*    */ 
/*    */   public static final native int ReprojectImage__SWIG_0(long paramLong1, Dataset paramDataset1, long paramLong2, Dataset paramDataset2, String paramString1, String paramString2, int paramInt, double paramDouble1, double paramDouble2, ProgressCallback paramProgressCallback);
/*    */ 
/*    */   public static final native int ReprojectImage__SWIG_2(long paramLong1, Dataset paramDataset1, long paramLong2, Dataset paramDataset2, String paramString1, String paramString2, int paramInt, double paramDouble1, double paramDouble2);
/*    */ 
/*    */   public static final native int ReprojectImage__SWIG_3(long paramLong1, Dataset paramDataset1, long paramLong2, Dataset paramDataset2, String paramString1, String paramString2, int paramInt, double paramDouble);
/*    */ 
/*    */   public static final native int ReprojectImage__SWIG_4(long paramLong1, Dataset paramDataset1, long paramLong2, Dataset paramDataset2, String paramString1, String paramString2, int paramInt);
/*    */ 
/*    */   public static final native int ReprojectImage__SWIG_5(long paramLong1, Dataset paramDataset1, long paramLong2, Dataset paramDataset2, String paramString1, String paramString2);
/*    */ 
/*    */   public static final native int ReprojectImage__SWIG_6(long paramLong1, Dataset paramDataset1, long paramLong2, Dataset paramDataset2, String paramString);
/*    */ 
/*    */   public static final native int ReprojectImage__SWIG_7(long paramLong1, Dataset paramDataset1, long paramLong2, Dataset paramDataset2);
/*    */ 
/*    */   public static final native int ComputeProximity__SWIG_0(long paramLong1, Band paramBand1, long paramLong2, Band paramBand2, Vector paramVector, ProgressCallback paramProgressCallback);
/*    */ 
/*    */   public static final native int ComputeProximity__SWIG_2(long paramLong1, Band paramBand1, long paramLong2, Band paramBand2, Vector paramVector);
/*    */ 
/*    */   public static final native int ComputeProximity__SWIG_3(long paramLong1, Band paramBand1, long paramLong2, Band paramBand2);
/*    */ 
/*    */   public static final native int RasterizeLayer__SWIG_0(long paramLong, Dataset paramDataset, int[] paramArrayOfInt, Layer paramLayer, double[] paramArrayOfDouble, Vector paramVector, ProgressCallback paramProgressCallback);
/*    */ 
/*    */   public static final native int RasterizeLayer__SWIG_2(long paramLong, Dataset paramDataset, int[] paramArrayOfInt, Layer paramLayer, double[] paramArrayOfDouble, Vector paramVector);
/*    */ 
/*    */   public static final native int RasterizeLayer__SWIG_3(long paramLong, Dataset paramDataset, int[] paramArrayOfInt, Layer paramLayer, double[] paramArrayOfDouble);
/*    */ 
/*    */   public static final native int RasterizeLayer__SWIG_5(long paramLong, Dataset paramDataset, int[] paramArrayOfInt, Layer paramLayer);
/*    */ 
/*    */   public static final native int Polygonize__SWIG_0(long paramLong1, Band paramBand1, long paramLong2, Band paramBand2, Layer paramLayer, int paramInt, Vector paramVector, ProgressCallback paramProgressCallback);
/*    */ 
/*    */   public static final native int Polygonize__SWIG_2(long paramLong1, Band paramBand1, long paramLong2, Band paramBand2, Layer paramLayer, int paramInt, Vector paramVector);
/*    */ 
/*    */   public static final native int Polygonize__SWIG_3(long paramLong1, Band paramBand1, long paramLong2, Band paramBand2, Layer paramLayer, int paramInt);
/*    */ 
/*    */   public static final native int FillNodata__SWIG_0(long paramLong1, Band paramBand1, long paramLong2, Band paramBand2, double paramDouble, int paramInt, Vector paramVector, ProgressCallback paramProgressCallback);
/*    */ 
/*    */   public static final native int FillNodata__SWIG_2(long paramLong1, Band paramBand1, long paramLong2, Band paramBand2, double paramDouble, int paramInt, Vector paramVector);
/*    */ 
/*    */   public static final native int FillNodata__SWIG_3(long paramLong1, Band paramBand1, long paramLong2, Band paramBand2, double paramDouble, int paramInt);
/*    */ 
/*    */   public static final native int SieveFilter__SWIG_0(long paramLong1, Band paramBand1, long paramLong2, Band paramBand2, long paramLong3, Band paramBand3, int paramInt1, int paramInt2, Vector paramVector, ProgressCallback paramProgressCallback);
/*    */ 
/*    */   public static final native int SieveFilter__SWIG_2(long paramLong1, Band paramBand1, long paramLong2, Band paramBand2, long paramLong3, Band paramBand3, int paramInt1, int paramInt2, Vector paramVector);
/*    */ 
/*    */   public static final native int SieveFilter__SWIG_3(long paramLong1, Band paramBand1, long paramLong2, Band paramBand2, long paramLong3, Band paramBand3, int paramInt1, int paramInt2);
/*    */ 
/*    */   public static final native int SieveFilter__SWIG_4(long paramLong1, Band paramBand1, long paramLong2, Band paramBand2, long paramLong3, Band paramBand3, int paramInt);
/*    */ 
/*    */   public static final native int RegenerateOverviews__SWIG_0(long paramLong, Band paramBand, Band[] paramArrayOfBand, String paramString, ProgressCallback paramProgressCallback);
/*    */ 
/*    */   public static final native int RegenerateOverviews__SWIG_2(long paramLong, Band paramBand, Band[] paramArrayOfBand, String paramString);
/*    */ 
/*    */   public static final native int RegenerateOverviews__SWIG_3(long paramLong, Band paramBand, Band[] paramArrayOfBand);
/*    */ 
/*    */   public static final native int RegenerateOverview__SWIG_0(long paramLong1, Band paramBand1, long paramLong2, Band paramBand2, String paramString, ProgressCallback paramProgressCallback);
/*    */ 
/*    */   public static final native int RegenerateOverview__SWIG_2(long paramLong1, Band paramBand1, long paramLong2, Band paramBand2, String paramString);
/*    */ 
/*    */   public static final native int RegenerateOverview__SWIG_3(long paramLong1, Band paramBand1, long paramLong2, Band paramBand2);
/*    */ 
/*    */   public static final native int GridCreate__SWIG_0(String paramString, double[][] paramArrayOfDouble, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, int paramInt1, int paramInt2, int paramInt3, ByteBuffer paramByteBuffer, ProgressCallback paramProgressCallback);
/*    */ 
/*    */   public static final native int GridCreate__SWIG_2(String paramString, double[][] paramArrayOfDouble, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, int paramInt1, int paramInt2, int paramInt3, ByteBuffer paramByteBuffer);
/*    */ 
/*    */   public static final native int ContourGenerate__SWIG_0(long paramLong, Band paramBand, double paramDouble1, double paramDouble2, double[] paramArrayOfDouble, int paramInt1, double paramDouble3, Layer paramLayer, int paramInt2, int paramInt3, ProgressCallback paramProgressCallback);
/*    */ 
/*    */   public static final native int ContourGenerate__SWIG_2(long paramLong, Band paramBand, double paramDouble1, double paramDouble2, double[] paramArrayOfDouble, int paramInt1, double paramDouble3, Layer paramLayer, int paramInt2, int paramInt3);
/*    */ 
/*    */   public static final native long AutoCreateWarpedVRT__SWIG_0(long paramLong, Dataset paramDataset, String paramString1, String paramString2, int paramInt, double paramDouble);
/*    */ 
/*    */   public static final native long AutoCreateWarpedVRT__SWIG_1(long paramLong, Dataset paramDataset, String paramString1, String paramString2, int paramInt);
/*    */ 
/*    */   public static final native long AutoCreateWarpedVRT__SWIG_2(long paramLong, Dataset paramDataset, String paramString1, String paramString2);
/*    */ 
/*    */   public static final native long AutoCreateWarpedVRT__SWIG_3(long paramLong, Dataset paramDataset, String paramString);
/*    */ 
/*    */   public static final native long AutoCreateWarpedVRT__SWIG_4(long paramLong, Dataset paramDataset);
/*    */ 
/*    */   public static final native long new_Transformer(long paramLong1, Dataset paramDataset1, long paramLong2, Dataset paramDataset2, Vector paramVector);
/*    */ 
/*    */   public static final native void delete_Transformer(long paramLong);
/*    */ 
/*    */   public static final native int Transformer_TransformPoint__SWIG_0(long paramLong, Transformer paramTransformer, int paramInt, double[] paramArrayOfDouble);
/*    */ 
/*    */   public static final native int Transformer_TransformPoint__SWIG_1(long paramLong, Transformer paramTransformer, double[] paramArrayOfDouble, int paramInt, double paramDouble1, double paramDouble2, double paramDouble3);
/*    */ 
/*    */   public static final native int Transformer_TransformPoint__SWIG_2(long paramLong, Transformer paramTransformer, double[] paramArrayOfDouble, int paramInt, double paramDouble1, double paramDouble2);
/*    */ 
/*    */   public static final native int Transformer_TransformPoints(long paramLong, Transformer paramTransformer, int paramInt, double[][] paramArrayOfDouble, int[] paramArrayOfInt);
/*    */ 
/*    */   public static final native void ApplyGeoTransform(double[] paramArrayOfDouble1, double paramDouble1, double paramDouble2, double[] paramArrayOfDouble2, double[] paramArrayOfDouble3);
/*    */ 
/*    */   public static final native int InvGeoTransform(double[] paramArrayOfDouble1, double[] paramArrayOfDouble2);
/*    */ 
/*    */   public static final native String VersionInfo__SWIG_0(String paramString);
/*    */ 
/*    */   public static final native String VersionInfo__SWIG_1();
/*    */ 
/*    */   public static final native void AllRegister();
/*    */ 
/*    */   public static final native void GDALDestroyDriverManager();
/*    */ 
/*    */   public static final native int GetCacheMax();
/*    */ 
/*    */   public static final native int GetCacheUsed();
/*    */ 
/*    */   public static final native void SetCacheMax(int paramInt);
/*    */ 
/*    */   public static final native int GetDataTypeSize(int paramInt);
/*    */ 
/*    */   public static final native int DataTypeIsComplex(int paramInt);
/*    */ 
/*    */   public static final native String GetDataTypeName(int paramInt);
/*    */ 
/*    */   public static final native int GetDataTypeByName(String paramString);
/*    */ 
/*    */   public static final native String GetColorInterpretationName(int paramInt);
/*    */ 
/*    */   public static final native String GetPaletteInterpretationName(int paramInt);
/*    */ 
/*    */   public static final native String DecToDMS__SWIG_0(double paramDouble, String paramString, int paramInt);
/*    */ 
/*    */   public static final native String DecToDMS__SWIG_1(double paramDouble, String paramString);
/*    */ 
/*    */   public static final native double PackedDMSToDec(double paramDouble);
/*    */ 
/*    */   public static final native double DecToPackedDMS(double paramDouble);
/*    */ 
/*    */   public static final native long ParseXMLString(String paramString);
/*    */ 
/*    */   public static final native String SerializeXMLTree(long paramLong, XMLNode paramXMLNode);
/*    */ 
/*    */   public static final native int GetDriverCount();
/*    */ 
/*    */   public static final native long GetDriverByName(String paramString);
/*    */ 
/*    */   public static final native long GetDriver(int paramInt);
/*    */ 
/*    */   public static final native long Open__SWIG_0(String paramString, int paramInt);
/*    */ 
/*    */   public static final native long Open__SWIG_1(String paramString);
/*    */ 
/*    */   public static final native long OpenShared__SWIG_0(String paramString, int paramInt);
/*    */ 
/*    */   public static final native long OpenShared__SWIG_1(String paramString);
/*    */ 
/*    */   public static final native long IdentifyDriver__SWIG_0(String paramString, Vector paramVector);
/*    */ 
/*    */   public static final native long IdentifyDriver__SWIG_1(String paramString);
/*    */ 
/*    */   public static final native Vector GeneralCmdLineProcessor__SWIG_0(Vector paramVector, int paramInt);
/*    */ 
/*    */   public static final native Vector GeneralCmdLineProcessor__SWIG_1(Vector paramVector);
/*    */ 
/*    */   public static final native long TermProgressCallback_SWIGUpcast(long paramLong);
/*    */ 
/*    */   public static final native long Driver_SWIGUpcast(long paramLong);
/*    */ 
/*    */   public static final native long Dataset_SWIGUpcast(long paramLong);
/*    */ 
/*    */   public static final native long Band_SWIGUpcast(long paramLong);
/*    */ 
/*    */   static
/*    */   {
/*    */     try
/*    */     {
/* 18 */       System.loadLibrary("gdaljni");
/* 19 */       available = true;
/*    */ 
/* 21 */       if (gdal.HasThreadSupport() == 0)
/*    */       {
/* 23 */         System.err.println("WARNING : GDAL should be compiled with thread support for safe execution in Java.");
/*    */       }
/*    */     }
/*    */     catch (UnsatisfiedLinkError e) {
/* 27 */       available = false;
/* 28 */       System.err.println("Native library load failed.");
/* 29 */       System.err.println(e);
/*    */     }
/*    */   }
/*    */ }

/* Location:           E:\temp\1117\gdal.jar
 * Qualified Name:     org.gdal.gdal.gdalJNI
 * JD-Core Version:    0.5.4
 */