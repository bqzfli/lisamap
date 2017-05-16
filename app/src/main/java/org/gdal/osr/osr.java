/*    */ package org.gdal.osr;
/*    */ 
/*    */ import java.util.Vector;
/*    */ 
/*    */ public class osr
/*    */   implements osrConstants
/*    */ {
/*    */   public static void UseExceptions()
/*    */   {
/* 13 */     osrJNI.UseExceptions();
/*    */   }
/*    */ 
/*    */   public static void DontUseExceptions() {
/* 17 */     osrJNI.DontUseExceptions();
/*    */   }
/*    */ 
/*    */   public static String GetWellKnownGeogCSAsWKT(String name)
/*    */   {
/* 28 */     return osrJNI.GetWellKnownGeogCSAsWKT__SWIG_0(name);
/*    */   }
/*    */ 
/*    */   public static String GetUserInputAsWKT(String name) {
/* 32 */     return osrJNI.GetUserInputAsWKT__SWIG_0(name);
/*    */   }
/*    */ 
/*    */   public static int GetWellKnownGeogCSAsWKT(String name, String[] argout) {
/* 36 */     return osrJNI.GetWellKnownGeogCSAsWKT__SWIG_1(name, argout);
/*    */   }
/*    */ 
/*    */   public static int GetUserInputAsWKT(String name, String[] argout) {
/* 40 */     return osrJNI.GetUserInputAsWKT__SWIG_1(name, argout);
/*    */   }
/*    */ 
/*    */   public static Vector GetProjectionMethods() {
/* 44 */     return osrJNI.GetProjectionMethods();
/*    */   }
/*    */ 
/*    */   public static String[] GetProjectionMethodParameterList(String method, String[] username) {
/* 48 */     return osrJNI.GetProjectionMethodParameterList(method, username);
/*    */   }
/*    */ 
/*    */   public static void GetProjectionMethodParamInfo(String method, String param, String[] usrname, String[] type, double[] defaultval) {
/* 52 */     osrJNI.GetProjectionMethodParamInfo(method, param, usrname, type, defaultval);
/*    */   }
/*    */ }

/* Location:           E:\temp\1117\gdal.jar
 * Qualified Name:     org.gdal.osr.osr
 * JD-Core Version:    0.5.4
 */