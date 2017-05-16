/*    */ package org.gdal.osr;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.util.Vector;
/*    */ 
/*    */ public class osrJNI
/*    */ {
/* 16 */   private static boolean available = false;
/*    */ 
/*    */   public static final native void UseExceptions();
/*    */ 
/*    */   public static final native void DontUseExceptions();
/*    */ 
/*    */   public static boolean isAvailable()
/*    */   {
/* 30 */     return available;
/*    */   }
/*    */ 
/*    */   public static final native String GetWellKnownGeogCSAsWKT__SWIG_0(String paramString);
/*    */ 
/*    */   public static final native String GetUserInputAsWKT__SWIG_0(String paramString);
/*    */ 
/*    */   public static final native int GetWellKnownGeogCSAsWKT__SWIG_1(String paramString, String[] paramArrayOfString);
/*    */ 
/*    */   public static final native int GetUserInputAsWKT__SWIG_1(String paramString, String[] paramArrayOfString);
/*    */ 
/*    */   public static final native Vector GetProjectionMethods();
/*    */ 
/*    */   public static final native String[] GetProjectionMethodParameterList(String paramString, String[] paramArrayOfString);
/*    */ 
/*    */   public static final native void GetProjectionMethodParamInfo(String paramString1, String paramString2, String[] paramArrayOfString1, String[] paramArrayOfString2, double[] paramArrayOfDouble);
/*    */ 
/*    */   public static final native long new_SpatialReference__SWIG_0(String paramString);
/*    */ 
/*    */   public static final native long new_SpatialReference__SWIG_1();
/*    */ 
/*    */   public static final native void delete_SpatialReference(long paramLong);
/*    */ 
/*    */   public static final native String SpatialReference___str__(long paramLong, SpatialReference paramSpatialReference);
/*    */ 
/*    */   public static final native int SpatialReference_IsSame(long paramLong1, SpatialReference paramSpatialReference1, long paramLong2, SpatialReference paramSpatialReference2);
/*    */ 
/*    */   public static final native int SpatialReference_IsSameGeogCS(long paramLong1, SpatialReference paramSpatialReference1, long paramLong2, SpatialReference paramSpatialReference2);
/*    */ 
/*    */   public static final native int SpatialReference_IsSameVertCS(long paramLong1, SpatialReference paramSpatialReference1, long paramLong2, SpatialReference paramSpatialReference2);
/*    */ 
/*    */   public static final native int SpatialReference_IsGeographic(long paramLong, SpatialReference paramSpatialReference);
/*    */ 
/*    */   public static final native int SpatialReference_IsProjected(long paramLong, SpatialReference paramSpatialReference);
/*    */ 
/*    */   public static final native int SpatialReference_IsCompound(long paramLong, SpatialReference paramSpatialReference);
/*    */ 
/*    */   public static final native int SpatialReference_IsGeocentric(long paramLong, SpatialReference paramSpatialReference);
/*    */ 
/*    */   public static final native int SpatialReference_IsLocal(long paramLong, SpatialReference paramSpatialReference);
/*    */ 
/*    */   public static final native int SpatialReference_IsVertical(long paramLong, SpatialReference paramSpatialReference);
/*    */ 
/*    */   public static final native int SpatialReference_EPSGTreatsAsLatLong(long paramLong, SpatialReference paramSpatialReference);
/*    */ 
/*    */   public static final native int SpatialReference_SetAuthority(long paramLong, SpatialReference paramSpatialReference, String paramString1, String paramString2, int paramInt);
/*    */ 
/*    */   public static final native String SpatialReference_GetAttrValue__SWIG_0(long paramLong, SpatialReference paramSpatialReference, String paramString, int paramInt);
/*    */ 
/*    */   public static final native String SpatialReference_GetAttrValue__SWIG_1(long paramLong, SpatialReference paramSpatialReference, String paramString);
/*    */ 
/*    */   public static final native int SpatialReference_SetAttrValue(long paramLong, SpatialReference paramSpatialReference, String paramString1, String paramString2);
/*    */ 
/*    */   public static final native int SpatialReference_SetAngularUnits(long paramLong, SpatialReference paramSpatialReference, String paramString, double paramDouble);
/*    */ 
/*    */   public static final native double SpatialReference_GetAngularUnits(long paramLong, SpatialReference paramSpatialReference);
/*    */ 
/*    */   public static final native int SpatialReference_SetTargetLinearUnits(long paramLong, SpatialReference paramSpatialReference, String paramString1, String paramString2, double paramDouble);
/*    */ 
/*    */   public static final native int SpatialReference_SetLinearUnits(long paramLong, SpatialReference paramSpatialReference, String paramString, double paramDouble);
/*    */ 
/*    */   public static final native int SpatialReference_SetLinearUnitsAndUpdateParameters(long paramLong, SpatialReference paramSpatialReference, String paramString, double paramDouble);
/*    */ 
/*    */   public static final native double SpatialReference_GetLinearUnits(long paramLong, SpatialReference paramSpatialReference);
/*    */ 
/*    */   public static final native String SpatialReference_GetLinearUnitsName(long paramLong, SpatialReference paramSpatialReference);
/*    */ 
/*    */   public static final native String SpatialReference_GetAuthorityCode(long paramLong, SpatialReference paramSpatialReference, String paramString);
/*    */ 
/*    */   public static final native String SpatialReference_GetAuthorityName(long paramLong, SpatialReference paramSpatialReference, String paramString);
/*    */ 
/*    */   public static final native int SpatialReference_SetUTM__SWIG_0(long paramLong, SpatialReference paramSpatialReference, int paramInt1, int paramInt2);
/*    */ 
/*    */   public static final native int SpatialReference_SetUTM__SWIG_1(long paramLong, SpatialReference paramSpatialReference, int paramInt);
/*    */ 
/*    */   public static final native int SpatialReference_GetUTMZone(long paramLong, SpatialReference paramSpatialReference);
/*    */ 
/*    */   public static final native int SpatialReference_SetStatePlane__SWIG_0(long paramLong, SpatialReference paramSpatialReference, int paramInt1, int paramInt2, String paramString, double paramDouble);
/*    */ 
/*    */   public static final native int SpatialReference_SetStatePlane__SWIG_1(long paramLong, SpatialReference paramSpatialReference, int paramInt1, int paramInt2, String paramString);
/*    */ 
/*    */   public static final native int SpatialReference_SetStatePlane__SWIG_2(long paramLong, SpatialReference paramSpatialReference, int paramInt1, int paramInt2);
/*    */ 
/*    */   public static final native int SpatialReference_SetStatePlane__SWIG_3(long paramLong, SpatialReference paramSpatialReference, int paramInt);
/*    */ 
/*    */   public static final native int SpatialReference_AutoIdentifyEPSG(long paramLong, SpatialReference paramSpatialReference);
/*    */ 
/*    */   public static final native int SpatialReference_SetProjection(long paramLong, SpatialReference paramSpatialReference, String paramString);
/*    */ 
/*    */   public static final native int SpatialReference_SetProjParm(long paramLong, SpatialReference paramSpatialReference, String paramString, double paramDouble);
/*    */ 
/*    */   public static final native double SpatialReference_GetProjParm__SWIG_0(long paramLong, SpatialReference paramSpatialReference, String paramString, double paramDouble);
/*    */ 
/*    */   public static final native double SpatialReference_GetProjParm__SWIG_1(long paramLong, SpatialReference paramSpatialReference, String paramString);
/*    */ 
/*    */   public static final native int SpatialReference_SetNormProjParm(long paramLong, SpatialReference paramSpatialReference, String paramString, double paramDouble);
/*    */ 
/*    */   public static final native double SpatialReference_GetNormProjParm__SWIG_0(long paramLong, SpatialReference paramSpatialReference, String paramString, double paramDouble);
/*    */ 
/*    */   public static final native double SpatialReference_GetNormProjParm__SWIG_1(long paramLong, SpatialReference paramSpatialReference, String paramString);
/*    */ 
/*    */   public static final native double SpatialReference_GetSemiMajor(long paramLong, SpatialReference paramSpatialReference);
/*    */ 
/*    */   public static final native double SpatialReference_GetSemiMinor(long paramLong, SpatialReference paramSpatialReference);
/*    */ 
/*    */   public static final native double SpatialReference_GetInvFlattening(long paramLong, SpatialReference paramSpatialReference);
/*    */ 
/*    */   public static final native int SpatialReference_SetACEA(long paramLong, SpatialReference paramSpatialReference, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6);
/*    */ 
/*    */   public static final native int SpatialReference_SetAE(long paramLong, SpatialReference paramSpatialReference, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4);
/*    */ 
/*    */   public static final native int SpatialReference_SetBonne(long paramLong, SpatialReference paramSpatialReference, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4);
/*    */ 
/*    */   public static final native int SpatialReference_SetCEA(long paramLong, SpatialReference paramSpatialReference, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4);
/*    */ 
/*    */   public static final native int SpatialReference_SetCS(long paramLong, SpatialReference paramSpatialReference, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4);
/*    */ 
/*    */   public static final native int SpatialReference_SetEC(long paramLong, SpatialReference paramSpatialReference, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6);
/*    */ 
/*    */   public static final native int SpatialReference_SetEckertIV(long paramLong, SpatialReference paramSpatialReference, double paramDouble1, double paramDouble2, double paramDouble3);
/*    */ 
/*    */   public static final native int SpatialReference_SetEckertVI(long paramLong, SpatialReference paramSpatialReference, double paramDouble1, double paramDouble2, double paramDouble3);
/*    */ 
/*    */   public static final native int SpatialReference_SetEquirectangular(long paramLong, SpatialReference paramSpatialReference, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4);
/*    */ 
/*    */   public static final native int SpatialReference_SetEquirectangular2(long paramLong, SpatialReference paramSpatialReference, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5);
/*    */ 
/*    */   public static final native int SpatialReference_SetGaussSchreiberTMercator(long paramLong, SpatialReference paramSpatialReference, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5);
/*    */ 
/*    */   public static final native int SpatialReference_SetGS(long paramLong, SpatialReference paramSpatialReference, double paramDouble1, double paramDouble2, double paramDouble3);
/*    */ 
/*    */   public static final native int SpatialReference_SetGH(long paramLong, SpatialReference paramSpatialReference, double paramDouble1, double paramDouble2, double paramDouble3);
/*    */ 
/*    */   public static final native int SpatialReference_SetIGH(long paramLong, SpatialReference paramSpatialReference);
/*    */ 
/*    */   public static final native int SpatialReference_SetGEOS(long paramLong, SpatialReference paramSpatialReference, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4);
/*    */ 
/*    */   public static final native int SpatialReference_SetGnomonic(long paramLong, SpatialReference paramSpatialReference, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4);
/*    */ 
/*    */   public static final native int SpatialReference_SetHOM(long paramLong, SpatialReference paramSpatialReference, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, double paramDouble7);
/*    */ 
/*    */   public static final native int SpatialReference_SetHOM2PNO(long paramLong, SpatialReference paramSpatialReference, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, double paramDouble7, double paramDouble8);
/*    */ 
/*    */   public static final native int SpatialReference_SetKrovak(long paramLong, SpatialReference paramSpatialReference, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, double paramDouble7);
/*    */ 
/*    */   public static final native int SpatialReference_SetLAEA(long paramLong, SpatialReference paramSpatialReference, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4);
/*    */ 
/*    */   public static final native int SpatialReference_SetLCC(long paramLong, SpatialReference paramSpatialReference, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6);
/*    */ 
/*    */   public static final native int SpatialReference_SetLCC1SP(long paramLong, SpatialReference paramSpatialReference, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5);
/*    */ 
/*    */   public static final native int SpatialReference_SetLCCB(long paramLong, SpatialReference paramSpatialReference, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6);
/*    */ 
/*    */   public static final native int SpatialReference_SetMC(long paramLong, SpatialReference paramSpatialReference, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4);
/*    */ 
/*    */   public static final native int SpatialReference_SetMercator(long paramLong, SpatialReference paramSpatialReference, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5);
/*    */ 
/*    */   public static final native int SpatialReference_SetMollweide(long paramLong, SpatialReference paramSpatialReference, double paramDouble1, double paramDouble2, double paramDouble3);
/*    */ 
/*    */   public static final native int SpatialReference_SetNZMG(long paramLong, SpatialReference paramSpatialReference, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4);
/*    */ 
/*    */   public static final native int SpatialReference_SetOS(long paramLong, SpatialReference paramSpatialReference, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5);
/*    */ 
/*    */   public static final native int SpatialReference_SetOrthographic(long paramLong, SpatialReference paramSpatialReference, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4);
/*    */ 
/*    */   public static final native int SpatialReference_SetPolyconic(long paramLong, SpatialReference paramSpatialReference, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4);
/*    */ 
/*    */   public static final native int SpatialReference_SetPS(long paramLong, SpatialReference paramSpatialReference, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5);
/*    */ 
/*    */   public static final native int SpatialReference_SetRobinson(long paramLong, SpatialReference paramSpatialReference, double paramDouble1, double paramDouble2, double paramDouble3);
/*    */ 
/*    */   public static final native int SpatialReference_SetSinusoidal(long paramLong, SpatialReference paramSpatialReference, double paramDouble1, double paramDouble2, double paramDouble3);
/*    */ 
/*    */   public static final native int SpatialReference_SetStereographic(long paramLong, SpatialReference paramSpatialReference, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5);
/*    */ 
/*    */   public static final native int SpatialReference_SetSOC(long paramLong, SpatialReference paramSpatialReference, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4);
/*    */ 
/*    */   public static final native int SpatialReference_SetTM(long paramLong, SpatialReference paramSpatialReference, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5);
/*    */ 
/*    */   public static final native int SpatialReference_SetTMVariant(long paramLong, SpatialReference paramSpatialReference, String paramString, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5);
/*    */ 
/*    */   public static final native int SpatialReference_SetTMG(long paramLong, SpatialReference paramSpatialReference, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4);
/*    */ 
/*    */   public static final native int SpatialReference_SetTMSO(long paramLong, SpatialReference paramSpatialReference, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5);
/*    */ 
/*    */   public static final native int SpatialReference_SetVDG(long paramLong, SpatialReference paramSpatialReference, double paramDouble1, double paramDouble2, double paramDouble3);
/*    */ 
/*    */   public static final native int SpatialReference_SetWellKnownGeogCS(long paramLong, SpatialReference paramSpatialReference, String paramString);
/*    */ 
/*    */   public static final native int SpatialReference_SetFromUserInput(long paramLong, SpatialReference paramSpatialReference, String paramString);
/*    */ 
/*    */   public static final native int SpatialReference_CopyGeogCSFrom(long paramLong1, SpatialReference paramSpatialReference1, long paramLong2, SpatialReference paramSpatialReference2);
/*    */ 
/*    */   public static final native int SpatialReference_SetTOWGS84(long paramLong, SpatialReference paramSpatialReference, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, double paramDouble7);
/*    */ 
/*    */   public static final native int SpatialReference_GetTOWGS84(long paramLong, SpatialReference paramSpatialReference, double[] paramArrayOfDouble);
/*    */ 
/*    */   public static final native int SpatialReference_SetLocalCS(long paramLong, SpatialReference paramSpatialReference, String paramString);
/*    */ 
/*    */   public static final native int SpatialReference_SetGeogCS__SWIG_0(long paramLong, SpatialReference paramSpatialReference, String paramString1, String paramString2, String paramString3, double paramDouble1, double paramDouble2, String paramString4, double paramDouble3, String paramString5, double paramDouble4);
/*    */ 
/*    */   public static final native int SpatialReference_SetGeogCS__SWIG_1(long paramLong, SpatialReference paramSpatialReference, String paramString1, String paramString2, String paramString3, double paramDouble1, double paramDouble2, String paramString4, double paramDouble3, String paramString5);
/*    */ 
/*    */   public static final native int SpatialReference_SetGeogCS__SWIG_2(long paramLong, SpatialReference paramSpatialReference, String paramString1, String paramString2, String paramString3, double paramDouble1, double paramDouble2, String paramString4, double paramDouble3);
/*    */ 
/*    */   public static final native int SpatialReference_SetGeogCS__SWIG_3(long paramLong, SpatialReference paramSpatialReference, String paramString1, String paramString2, String paramString3, double paramDouble1, double paramDouble2, String paramString4);
/*    */ 
/*    */   public static final native int SpatialReference_SetGeogCS__SWIG_4(long paramLong, SpatialReference paramSpatialReference, String paramString1, String paramString2, String paramString3, double paramDouble1, double paramDouble2);
/*    */ 
/*    */   public static final native int SpatialReference_SetProjCS__SWIG_0(long paramLong, SpatialReference paramSpatialReference, String paramString);
/*    */ 
/*    */   public static final native int SpatialReference_SetProjCS__SWIG_1(long paramLong, SpatialReference paramSpatialReference);
/*    */ 
/*    */   public static final native int SpatialReference_SetGeocCS__SWIG_0(long paramLong, SpatialReference paramSpatialReference, String paramString);
/*    */ 
/*    */   public static final native int SpatialReference_SetGeocCS__SWIG_1(long paramLong, SpatialReference paramSpatialReference);
/*    */ 
/*    */   public static final native int SpatialReference_SetVertCS__SWIG_0(long paramLong, SpatialReference paramSpatialReference, String paramString1, String paramString2, int paramInt);
/*    */ 
/*    */   public static final native int SpatialReference_SetVertCS__SWIG_1(long paramLong, SpatialReference paramSpatialReference, String paramString1, String paramString2);
/*    */ 
/*    */   public static final native int SpatialReference_SetVertCS__SWIG_2(long paramLong, SpatialReference paramSpatialReference, String paramString);
/*    */ 
/*    */   public static final native int SpatialReference_SetVertCS__SWIG_3(long paramLong, SpatialReference paramSpatialReference);
/*    */ 
/*    */   public static final native int SpatialReference_SetCompoundCS(long paramLong1, SpatialReference paramSpatialReference1, String paramString, long paramLong2, SpatialReference paramSpatialReference2, long paramLong3, SpatialReference paramSpatialReference3);
/*    */ 
/*    */   public static final native int SpatialReference_ImportFromWkt(long paramLong, SpatialReference paramSpatialReference, String paramString);
/*    */ 
/*    */   public static final native int SpatialReference_ImportFromProj4(long paramLong, SpatialReference paramSpatialReference, String paramString);
/*    */ 
/*    */   public static final native int SpatialReference_ImportFromUrl(long paramLong, SpatialReference paramSpatialReference, String paramString);
/*    */ 
/*    */   public static final native int SpatialReference_ImportFromESRI(long paramLong, SpatialReference paramSpatialReference, Vector paramVector);
/*    */ 
/*    */   public static final native int SpatialReference_ImportFromEPSG(long paramLong, SpatialReference paramSpatialReference, int paramInt);
/*    */ 
/*    */   public static final native int SpatialReference_ImportFromEPSGA(long paramLong, SpatialReference paramSpatialReference, int paramInt);
/*    */ 
/*    */   public static final native int SpatialReference_ImportFromPCI__SWIG_0(long paramLong, SpatialReference paramSpatialReference, String paramString1, String paramString2, double[] paramArrayOfDouble);
/*    */ 
/*    */   public static final native int SpatialReference_ImportFromPCI__SWIG_1(long paramLong, SpatialReference paramSpatialReference, String paramString1, String paramString2);
/*    */ 
/*    */   public static final native int SpatialReference_ImportFromPCI__SWIG_2(long paramLong, SpatialReference paramSpatialReference, String paramString);
/*    */ 
/*    */   public static final native int SpatialReference_ImportFromUSGS__SWIG_0(long paramLong, SpatialReference paramSpatialReference, int paramInt1, int paramInt2, double[] paramArrayOfDouble, int paramInt3);
/*    */ 
/*    */   public static final native int SpatialReference_ImportFromUSGS__SWIG_1(long paramLong, SpatialReference paramSpatialReference, int paramInt1, int paramInt2, double[] paramArrayOfDouble);
/*    */ 
/*    */   public static final native int SpatialReference_ImportFromUSGS__SWIG_2(long paramLong, SpatialReference paramSpatialReference, int paramInt1, int paramInt2);
/*    */ 
/*    */   public static final native int SpatialReference_ImportFromUSGS__SWIG_3(long paramLong, SpatialReference paramSpatialReference, int paramInt);
/*    */ 
/*    */   public static final native int SpatialReference_ImportFromXML(long paramLong, SpatialReference paramSpatialReference, String paramString);
/*    */ 
/*    */   public static final native int SpatialReference_ImportFromERM(long paramLong, SpatialReference paramSpatialReference, String paramString1, String paramString2, String paramString3);
/*    */ 
/*    */   public static final native int SpatialReference_ImportFromMICoordSys(long paramLong, SpatialReference paramSpatialReference, String paramString);
/*    */ 
/*    */   public static final native int SpatialReference_ExportToWkt(long paramLong, SpatialReference paramSpatialReference, String[] paramArrayOfString);
/*    */ 
/*    */   public static final native int SpatialReference_ExportToPrettyWkt__SWIG_0(long paramLong, SpatialReference paramSpatialReference, String[] paramArrayOfString, int paramInt);
/*    */ 
/*    */   public static final native int SpatialReference_ExportToPrettyWkt__SWIG_1(long paramLong, SpatialReference paramSpatialReference, String[] paramArrayOfString);
/*    */ 
/*    */   public static final native int SpatialReference_ExportToProj4(long paramLong, SpatialReference paramSpatialReference, String[] paramArrayOfString);
/*    */ 
/*    */   public static final native int SpatialReference_ExportToPCI(long paramLong, SpatialReference paramSpatialReference, String[] paramArrayOfString1, String[] paramArrayOfString2, double[] paramArrayOfDouble);
/*    */ 
/*    */   public static final native int SpatialReference_ExportToUSGS(long paramLong, SpatialReference paramSpatialReference, int[] paramArrayOfInt1, int[] paramArrayOfInt2, double[] paramArrayOfDouble, int[] paramArrayOfInt3);
/*    */ 
/*    */   public static final native int SpatialReference_ExportToXML__SWIG_0(long paramLong, SpatialReference paramSpatialReference, String[] paramArrayOfString, String paramString);
/*    */ 
/*    */   public static final native int SpatialReference_ExportToXML__SWIG_1(long paramLong, SpatialReference paramSpatialReference, String[] paramArrayOfString);
/*    */ 
/*    */   public static final native int SpatialReference_ExportToMICoordSys(long paramLong, SpatialReference paramSpatialReference, String[] paramArrayOfString);
/*    */ 
/*    */   public static final native long SpatialReference_CloneGeogCS(long paramLong, SpatialReference paramSpatialReference);
/*    */ 
/*    */   public static final native long SpatialReference_Clone(long paramLong, SpatialReference paramSpatialReference);
/*    */ 
/*    */   public static final native int SpatialReference_Validate(long paramLong, SpatialReference paramSpatialReference);
/*    */ 
/*    */   public static final native int SpatialReference_StripCTParms(long paramLong, SpatialReference paramSpatialReference);
/*    */ 
/*    */   public static final native int SpatialReference_FixupOrdering(long paramLong, SpatialReference paramSpatialReference);
/*    */ 
/*    */   public static final native int SpatialReference_Fixup(long paramLong, SpatialReference paramSpatialReference);
/*    */ 
/*    */   public static final native int SpatialReference_MorphToESRI(long paramLong, SpatialReference paramSpatialReference);
/*    */ 
/*    */   public static final native int SpatialReference_MorphFromESRI(long paramLong, SpatialReference paramSpatialReference);
/*    */ 
/*    */   public static final native long new_CoordinateTransformation(long paramLong1, SpatialReference paramSpatialReference1, long paramLong2, SpatialReference paramSpatialReference2);
/*    */ 
/*    */   public static final native void delete_CoordinateTransformation(long paramLong);
/*    */ 
/*    */   public static final native void CoordinateTransformation_TransformPoint__SWIG_0(long paramLong, CoordinateTransformation paramCoordinateTransformation, double[] paramArrayOfDouble);
/*    */ 
/*    */   public static final native void CoordinateTransformation_TransformPoint__SWIG_1(long paramLong, CoordinateTransformation paramCoordinateTransformation, double[] paramArrayOfDouble, double paramDouble1, double paramDouble2, double paramDouble3);
/*    */ 
/*    */   public static final native void CoordinateTransformation_TransformPoint__SWIG_2(long paramLong, CoordinateTransformation paramCoordinateTransformation, double[] paramArrayOfDouble, double paramDouble1, double paramDouble2);
/*    */ 
/*    */   public static final native void CoordinateTransformation_TransformPoints(long paramLong, CoordinateTransformation paramCoordinateTransformation, double[][] paramArrayOfDouble);
/*    */ 
/*    */   static
/*    */   {
/*    */     try
/*    */     {
/* 20 */       System.loadLibrary("osrjni");
/* 21 */       available = true;
/*    */     } catch (UnsatisfiedLinkError e) {
/* 23 */       available = false;
/* 24 */       System.err.println("Native library load failed.");
/* 25 */       System.err.println(e);
/*    */     }
/*    */   }
/*    */ }

/* Location:           E:\temp\1117\gdal.jar
 * Qualified Name:     org.gdal.osr.osrJNI
 * JD-Core Version:    0.5.4
 */