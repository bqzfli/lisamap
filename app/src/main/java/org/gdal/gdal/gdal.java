/*     */ package org.gdal.gdal;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
/*     */ import org.gdal.ogr.Layer;
/*     */ 
/*     */ public class gdal
/*     */ {
/*     */   public static String[] GeneralCmdLineProcessor(String[] args, int nOptions)
/*     */   {
/*  21 */     Vector vArgs = new Vector();
/*     */ 
/*  23 */     for (int i = 0; i < args.length; ++i) {
/*  24 */       vArgs.addElement(args[i]);
/*     */     }
/*  26 */     vArgs = GeneralCmdLineProcessor(vArgs, nOptions);
/*  27 */     Enumeration eArgs = vArgs.elements();
/*  28 */     args = new String[vArgs.size()];
/*  29 */     int i = 0;
/*  30 */     while (eArgs.hasMoreElements())
/*     */     {
/*  32 */       String arg = (String)eArgs.nextElement();
/*  33 */       args[(i++)] = arg;
/*     */     }
/*     */ 
/*  36 */     return args;
/*     */   }
/*     */ 
/*     */   public static String[] GeneralCmdLineProcessor(String[] args)
/*     */   {
/*  41 */     return GeneralCmdLineProcessor(args, 0);
/*     */   }
/*     */ 
/*     */   public static double[] InvGeoTransform(double[] gt_in)
/*     */   {
/*  46 */     double[] gt_out = new double[6];
/*  47 */     if (InvGeoTransform(gt_in, gt_out) == 1) {
/*  48 */       return gt_out;
/*     */     }
/*  50 */     return null;
/*     */   }
/*     */ 
/*     */   public static void Debug(String msg_class, String message) {
/*  54 */     gdalJNI.Debug(msg_class, message);
/*     */   }
/*     */ 
/*     */   public static int PushErrorHandler(String pszCallbackName) {
/*  58 */     return gdalJNI.PushErrorHandler__SWIG_0(pszCallbackName);
/*     */   }
/*     */ 
/*     */   public static int PushErrorHandler() {
/*  62 */     return gdalJNI.PushErrorHandler__SWIG_1();
/*     */   }
/*     */ 
/*     */   public static void Error(int msg_class, int err_code, String msg) {
/*  66 */     gdalJNI.Error(msg_class, err_code, msg);
/*     */   }
/*     */ 
/*     */   public static void PopErrorHandler() {
/*  70 */     gdalJNI.PopErrorHandler();
/*     */   }
/*     */ 
/*     */   public static void ErrorReset() {
/*  74 */     gdalJNI.ErrorReset();
/*     */   }
/*     */ 
/*     */   public static String EscapeString(byte[] len, int scheme) {
/*  78 */     return gdalJNI.EscapeString__SWIG_0(len, scheme);
/*     */   }
/*     */ 
/*     */   public static String EscapeString(String str, int scheme) {
/*  82 */     return gdalJNI.EscapeString__SWIG_1(str, scheme);
/*     */   }
/*     */ 
/*     */   public static int GetLastErrorNo() {
/*  86 */     return gdalJNI.GetLastErrorNo();
/*     */   }
/*     */ 
/*     */   public static int GetLastErrorType() {
/*  90 */     return gdalJNI.GetLastErrorType();
/*     */   }
/*     */ 
/*     */   public static String GetLastErrorMsg() {
/*  94 */     return gdalJNI.GetLastErrorMsg();
/*     */   }
/*     */ 
/*     */   public static void PushFinderLocation(String utf8_path) {
/*  98 */     gdalJNI.PushFinderLocation(utf8_path);
/*     */   }
/*     */ 
/*     */   public static void PopFinderLocation() {
/* 102 */     gdalJNI.PopFinderLocation();
/*     */   }
/*     */ 
/*     */   public static void FinderClean() {
/* 106 */     gdalJNI.FinderClean();
/*     */   }
/*     */ 
/*     */   public static String FindFile(String pszClass, String utf8_path) {
/* 110 */     return gdalJNI.FindFile(pszClass, utf8_path);
/*     */   }
/*     */ 
/*     */   public static Vector ReadDir(String utf8_path) {
/* 114 */     return gdalJNI.ReadDir(utf8_path);
/*     */   }
/*     */ 
/*     */   public static void SetConfigOption(String pszKey, String pszValue) {
/* 118 */     gdalJNI.SetConfigOption(pszKey, pszValue);
/*     */   }
/*     */ 
/*     */   public static String GetConfigOption(String pszKey, String pszDefault) {
/* 122 */     return gdalJNI.GetConfigOption__SWIG_0(pszKey, pszDefault);
/*     */   }
/*     */ 
/*     */   public static String GetConfigOption(String pszKey) {
/* 126 */     return gdalJNI.GetConfigOption__SWIG_1(pszKey);
/*     */   }
/*     */ 
/*     */   public static String CPLBinaryToHex(byte[] nBytes) {
/* 130 */     return gdalJNI.CPLBinaryToHex(nBytes);
/*     */   }
/*     */ 
/*     */   public static byte[] CPLHexToBinary(String pszHex) {
/* 134 */     return gdalJNI.CPLHexToBinary(pszHex);
/*     */   }
/*     */ 
/*     */   public static void FileFromMemBuffer(String utf8_path, byte[] nBytes) {
/* 138 */     gdalJNI.FileFromMemBuffer(utf8_path, nBytes);
/*     */   }
/*     */ 
/*     */   public static int Unlink(String utf8_path) {
/* 142 */     return gdalJNI.Unlink(utf8_path);
/*     */   }
/*     */ 
/*     */   public static int HasThreadSupport() {
/* 146 */     return gdalJNI.HasThreadSupport();
/*     */   }
/*     */ 
/*     */   public static int Mkdir(String utf8_path, int mode) {
/* 150 */     return gdalJNI.Mkdir(utf8_path, mode);
/*     */   }
/*     */ 
/*     */   public static int Rmdir(String utf8_path) {
/* 154 */     return gdalJNI.Rmdir(utf8_path);
/*     */   }
/*     */ 
/*     */   public static int Rename(String pszOld, String pszNew) {
/* 158 */     return gdalJNI.Rename(pszOld, pszNew);
/*     */   }
/*     */ 
/*     */   public static double GDAL_GCP_GCPX_get(GCP gcp) {
/* 162 */     return gdalJNI.GDAL_GCP_GCPX_get(GCP.getCPtr(gcp), gcp);
/*     */   }
/*     */ 
/*     */   public static void GDAL_GCP_GCPX_set(GCP gcp, double dfGCPX) {
/* 166 */     gdalJNI.GDAL_GCP_GCPX_set(GCP.getCPtr(gcp), gcp, dfGCPX);
/*     */   }
/*     */ 
/*     */   public static double GDAL_GCP_GCPY_get(GCP gcp) {
/* 170 */     return gdalJNI.GDAL_GCP_GCPY_get(GCP.getCPtr(gcp), gcp);
/*     */   }
/*     */ 
/*     */   public static void GDAL_GCP_GCPY_set(GCP gcp, double dfGCPY) {
/* 174 */     gdalJNI.GDAL_GCP_GCPY_set(GCP.getCPtr(gcp), gcp, dfGCPY);
/*     */   }
/*     */ 
/*     */   public static double GDAL_GCP_GCPZ_get(GCP gcp) {
/* 178 */     return gdalJNI.GDAL_GCP_GCPZ_get(GCP.getCPtr(gcp), gcp);
/*     */   }
/*     */ 
/*     */   public static void GDAL_GCP_GCPZ_set(GCP gcp, double dfGCPZ) {
/* 182 */     gdalJNI.GDAL_GCP_GCPZ_set(GCP.getCPtr(gcp), gcp, dfGCPZ);
/*     */   }
/*     */ 
/*     */   public static double GDAL_GCP_GCPPixel_get(GCP gcp) {
/* 186 */     return gdalJNI.GDAL_GCP_GCPPixel_get(GCP.getCPtr(gcp), gcp);
/*     */   }
/*     */ 
/*     */   public static void GDAL_GCP_GCPPixel_set(GCP gcp, double dfGCPPixel) {
/* 190 */     gdalJNI.GDAL_GCP_GCPPixel_set(GCP.getCPtr(gcp), gcp, dfGCPPixel);
/*     */   }
/*     */ 
/*     */   public static double GDAL_GCP_GCPLine_get(GCP gcp) {
/* 194 */     return gdalJNI.GDAL_GCP_GCPLine_get(GCP.getCPtr(gcp), gcp);
/*     */   }
/*     */ 
/*     */   public static void GDAL_GCP_GCPLine_set(GCP gcp, double dfGCPLine) {
/* 198 */     gdalJNI.GDAL_GCP_GCPLine_set(GCP.getCPtr(gcp), gcp, dfGCPLine);
/*     */   }
/*     */ 
/*     */   public static String GDAL_GCP_Info_get(GCP gcp) {
/* 202 */     return gdalJNI.GDAL_GCP_Info_get(GCP.getCPtr(gcp), gcp);
/*     */   }
/*     */ 
/*     */   public static void GDAL_GCP_Info_set(GCP gcp, String pszInfo) {
/* 206 */     gdalJNI.GDAL_GCP_Info_set(GCP.getCPtr(gcp), gcp, pszInfo);
/*     */   }
/*     */ 
/*     */   public static String GDAL_GCP_Id_get(GCP gcp) {
/* 210 */     return gdalJNI.GDAL_GCP_Id_get(GCP.getCPtr(gcp), gcp);
/*     */   }
/*     */ 
/*     */   public static void GDAL_GCP_Id_set(GCP gcp, String pszId) {
/* 214 */     gdalJNI.GDAL_GCP_Id_set(GCP.getCPtr(gcp), gcp, pszId);
/*     */   }
/*     */ 
/*     */   public static double GDAL_GCP_get_GCPX(GCP gcp) {
/* 218 */     return gdalJNI.GDAL_GCP_get_GCPX(GCP.getCPtr(gcp), gcp);
/*     */   }
/*     */ 
/*     */   public static void GDAL_GCP_set_GCPX(GCP gcp, double dfGCPX) {
/* 222 */     gdalJNI.GDAL_GCP_set_GCPX(GCP.getCPtr(gcp), gcp, dfGCPX);
/*     */   }
/*     */ 
/*     */   public static double GDAL_GCP_get_GCPY(GCP gcp) {
/* 226 */     return gdalJNI.GDAL_GCP_get_GCPY(GCP.getCPtr(gcp), gcp);
/*     */   }
/*     */ 
/*     */   public static void GDAL_GCP_set_GCPY(GCP gcp, double dfGCPY) {
/* 230 */     gdalJNI.GDAL_GCP_set_GCPY(GCP.getCPtr(gcp), gcp, dfGCPY);
/*     */   }
/*     */ 
/*     */   public static double GDAL_GCP_get_GCPZ(GCP gcp) {
/* 234 */     return gdalJNI.GDAL_GCP_get_GCPZ(GCP.getCPtr(gcp), gcp);
/*     */   }
/*     */ 
/*     */   public static void GDAL_GCP_set_GCPZ(GCP gcp, double dfGCPZ) {
/* 238 */     gdalJNI.GDAL_GCP_set_GCPZ(GCP.getCPtr(gcp), gcp, dfGCPZ);
/*     */   }
/*     */ 
/*     */   public static double GDAL_GCP_get_GCPPixel(GCP gcp) {
/* 242 */     return gdalJNI.GDAL_GCP_get_GCPPixel(GCP.getCPtr(gcp), gcp);
/*     */   }
/*     */ 
/*     */   public static void GDAL_GCP_set_GCPPixel(GCP gcp, double dfGCPPixel) {
/* 246 */     gdalJNI.GDAL_GCP_set_GCPPixel(GCP.getCPtr(gcp), gcp, dfGCPPixel);
/*     */   }
/*     */ 
/*     */   public static double GDAL_GCP_get_GCPLine(GCP gcp) {
/* 250 */     return gdalJNI.GDAL_GCP_get_GCPLine(GCP.getCPtr(gcp), gcp);
/*     */   }
/*     */ 
/*     */   public static void GDAL_GCP_set_GCPLine(GCP gcp, double dfGCPLine) {
/* 254 */     gdalJNI.GDAL_GCP_set_GCPLine(GCP.getCPtr(gcp), gcp, dfGCPLine);
/*     */   }
/*     */ 
/*     */   public static String GDAL_GCP_get_Info(GCP gcp) {
/* 258 */     return gdalJNI.GDAL_GCP_get_Info(GCP.getCPtr(gcp), gcp);
/*     */   }
/*     */ 
/*     */   public static void GDAL_GCP_set_Info(GCP gcp, String pszInfo) {
/* 262 */     gdalJNI.GDAL_GCP_set_Info(GCP.getCPtr(gcp), gcp, pszInfo);
/*     */   }
/*     */ 
/*     */   public static String GDAL_GCP_get_Id(GCP gcp) {
/* 266 */     return gdalJNI.GDAL_GCP_get_Id(GCP.getCPtr(gcp), gcp);
/*     */   }
/*     */ 
/*     */   public static void GDAL_GCP_set_Id(GCP gcp, String pszId) {
/* 270 */     gdalJNI.GDAL_GCP_set_Id(GCP.getCPtr(gcp), gcp, pszId);
/*     */   }
/*     */ 
/*     */   public static int GCPsToGeoTransform(GCP[] nGCPs, double[] argout, int bApproxOK) {
/* 274 */     return gdalJNI.GCPsToGeoTransform__SWIG_0(nGCPs, argout, bApproxOK);
/*     */   }
/*     */ 
/*     */   public static int GCPsToGeoTransform(GCP[] nGCPs, double[] argout) {
/* 278 */     return gdalJNI.GCPsToGeoTransform__SWIG_1(nGCPs, argout);
/*     */   }
/*     */ 
/*     */   public static int ComputeMedianCutPCT(Band red, Band green, Band blue, int num_colors, ColorTable colors, ProgressCallback callback) {
/* 282 */     return gdalJNI.ComputeMedianCutPCT__SWIG_0(Band.getCPtr(red), red, Band.getCPtr(green), green, Band.getCPtr(blue), blue, num_colors, ColorTable.getCPtr(colors), colors, callback);
/*     */   }
/*     */ 
/*     */   public static int ComputeMedianCutPCT(Band red, Band green, Band blue, int num_colors, ColorTable colors) {
/* 286 */     return gdalJNI.ComputeMedianCutPCT__SWIG_2(Band.getCPtr(red), red, Band.getCPtr(green), green, Band.getCPtr(blue), blue, num_colors, ColorTable.getCPtr(colors), colors);
/*     */   }
/*     */ 
/*     */   public static int DitherRGB2PCT(Band red, Band green, Band blue, Band target, ColorTable colors, ProgressCallback callback) {
/* 290 */     return gdalJNI.DitherRGB2PCT__SWIG_0(Band.getCPtr(red), red, Band.getCPtr(green), green, Band.getCPtr(blue), blue, Band.getCPtr(target), target, ColorTable.getCPtr(colors), colors, callback);
/*     */   }
/*     */ 
/*     */   public static int DitherRGB2PCT(Band red, Band green, Band blue, Band target, ColorTable colors) {
/* 294 */     return gdalJNI.DitherRGB2PCT__SWIG_2(Band.getCPtr(red), red, Band.getCPtr(green), green, Band.getCPtr(blue), blue, Band.getCPtr(target), target, ColorTable.getCPtr(colors), colors);
/*     */   }
/*     */ 
/*     */   public static int ReprojectImage(Dataset src_ds, Dataset dst_ds, String src_wkt, String dst_wkt, int eResampleAlg, double WarpMemoryLimit, double maxerror, ProgressCallback callback) {
/* 298 */     return gdalJNI.ReprojectImage__SWIG_0(Dataset.getCPtr(src_ds), src_ds, Dataset.getCPtr(dst_ds), dst_ds, src_wkt, dst_wkt, eResampleAlg, WarpMemoryLimit, maxerror, callback);
/*     */   }
/*     */ 
/*     */   public static int ReprojectImage(Dataset src_ds, Dataset dst_ds, String src_wkt, String dst_wkt, int eResampleAlg, double WarpMemoryLimit, double maxerror) {
/* 302 */     return gdalJNI.ReprojectImage__SWIG_2(Dataset.getCPtr(src_ds), src_ds, Dataset.getCPtr(dst_ds), dst_ds, src_wkt, dst_wkt, eResampleAlg, WarpMemoryLimit, maxerror);
/*     */   }
/*     */ 
/*     */   public static int ReprojectImage(Dataset src_ds, Dataset dst_ds, String src_wkt, String dst_wkt, int eResampleAlg, double WarpMemoryLimit) {
/* 306 */     return gdalJNI.ReprojectImage__SWIG_3(Dataset.getCPtr(src_ds), src_ds, Dataset.getCPtr(dst_ds), dst_ds, src_wkt, dst_wkt, eResampleAlg, WarpMemoryLimit);
/*     */   }
/*     */ 
/*     */   public static int ReprojectImage(Dataset src_ds, Dataset dst_ds, String src_wkt, String dst_wkt, int eResampleAlg) {
/* 310 */     return gdalJNI.ReprojectImage__SWIG_4(Dataset.getCPtr(src_ds), src_ds, Dataset.getCPtr(dst_ds), dst_ds, src_wkt, dst_wkt, eResampleAlg);
/*     */   }
/*     */ 
/*     */   public static int ReprojectImage(Dataset src_ds, Dataset dst_ds, String src_wkt, String dst_wkt) {
/* 314 */     return gdalJNI.ReprojectImage__SWIG_5(Dataset.getCPtr(src_ds), src_ds, Dataset.getCPtr(dst_ds), dst_ds, src_wkt, dst_wkt);
/*     */   }
/*     */ 
/*     */   public static int ReprojectImage(Dataset src_ds, Dataset dst_ds, String src_wkt) {
/* 318 */     return gdalJNI.ReprojectImage__SWIG_6(Dataset.getCPtr(src_ds), src_ds, Dataset.getCPtr(dst_ds), dst_ds, src_wkt);
/*     */   }
/*     */ 
/*     */   public static int ReprojectImage(Dataset src_ds, Dataset dst_ds) {
/* 322 */     return gdalJNI.ReprojectImage__SWIG_7(Dataset.getCPtr(src_ds), src_ds, Dataset.getCPtr(dst_ds), dst_ds);
/*     */   }
/*     */ 
/*     */   public static int ComputeProximity(Band srcBand, Band proximityBand, Vector options, ProgressCallback callback) {
/* 326 */     return gdalJNI.ComputeProximity__SWIG_0(Band.getCPtr(srcBand), srcBand, Band.getCPtr(proximityBand), proximityBand, options, callback);
/*     */   }
/*     */ 
/*     */   public static int ComputeProximity(Band srcBand, Band proximityBand, Vector options) {
/* 330 */     return gdalJNI.ComputeProximity__SWIG_2(Band.getCPtr(srcBand), srcBand, Band.getCPtr(proximityBand), proximityBand, options);
/*     */   }
/*     */ 
/*     */   public static int ComputeProximity(Band srcBand, Band proximityBand) {
/* 334 */     return gdalJNI.ComputeProximity__SWIG_3(Band.getCPtr(srcBand), srcBand, Band.getCPtr(proximityBand), proximityBand);
/*     */   }
/*     */ 
/*     */   public static int RasterizeLayer(Dataset dataset, int[] bands, Layer layer, double[] burn_values, Vector options, ProgressCallback callback) {
/* 338 */     return gdalJNI.RasterizeLayer__SWIG_0(Dataset.getCPtr(dataset), dataset, bands, layer, burn_values, options, callback);
/*     */   }
/*     */ 
/*     */   public static int RasterizeLayer(Dataset dataset, int[] bands, Layer layer, double[] burn_values, Vector options) {
/* 342 */     return gdalJNI.RasterizeLayer__SWIG_2(Dataset.getCPtr(dataset), dataset, bands, layer, burn_values, options);
/*     */   }
/*     */ 
/*     */   public static int RasterizeLayer(Dataset dataset, int[] bands, Layer layer, double[] burn_values) {
/* 346 */     return gdalJNI.RasterizeLayer__SWIG_3(Dataset.getCPtr(dataset), dataset, bands, layer, burn_values);
/*     */   }
/*     */ 
/*     */   public static int RasterizeLayer(Dataset dataset, int[] bands, Layer layer) {
/* 350 */     return gdalJNI.RasterizeLayer__SWIG_5(Dataset.getCPtr(dataset), dataset, bands, layer);
/*     */   }
/*     */ 
/*     */   public static int Polygonize(Band srcBand, Band maskBand, Layer outLayer, int iPixValField, Vector options, ProgressCallback callback) {
/* 354 */     return gdalJNI.Polygonize__SWIG_0(Band.getCPtr(srcBand), srcBand, Band.getCPtr(maskBand), maskBand, outLayer, iPixValField, options, callback);
/*     */   }
/*     */ 
/*     */   public static int Polygonize(Band srcBand, Band maskBand, Layer outLayer, int iPixValField, Vector options) {
/* 358 */     return gdalJNI.Polygonize__SWIG_2(Band.getCPtr(srcBand), srcBand, Band.getCPtr(maskBand), maskBand, outLayer, iPixValField, options);
/*     */   }
/*     */ 
/*     */   public static int Polygonize(Band srcBand, Band maskBand, Layer outLayer, int iPixValField) {
/* 362 */     return gdalJNI.Polygonize__SWIG_3(Band.getCPtr(srcBand), srcBand, Band.getCPtr(maskBand), maskBand, outLayer, iPixValField);
/*     */   }
/*     */ 
/*     */   public static int FillNodata(Band targetBand, Band maskBand, double maxSearchDist, int smoothingIterations, Vector options, ProgressCallback callback) {
/* 366 */     return gdalJNI.FillNodata__SWIG_0(Band.getCPtr(targetBand), targetBand, Band.getCPtr(maskBand), maskBand, maxSearchDist, smoothingIterations, options, callback);
/*     */   }
/*     */ 
/*     */   public static int FillNodata(Band targetBand, Band maskBand, double maxSearchDist, int smoothingIterations, Vector options) {
/* 370 */     return gdalJNI.FillNodata__SWIG_2(Band.getCPtr(targetBand), targetBand, Band.getCPtr(maskBand), maskBand, maxSearchDist, smoothingIterations, options);
/*     */   }
/*     */ 
/*     */   public static int FillNodata(Band targetBand, Band maskBand, double maxSearchDist, int smoothingIterations) {
/* 374 */     return gdalJNI.FillNodata__SWIG_3(Band.getCPtr(targetBand), targetBand, Band.getCPtr(maskBand), maskBand, maxSearchDist, smoothingIterations);
/*     */   }
/*     */ 
/*     */   public static int SieveFilter(Band srcBand, Band maskBand, Band dstBand, int threshold, int connectedness, Vector options, ProgressCallback callback) {
/* 378 */     return gdalJNI.SieveFilter__SWIG_0(Band.getCPtr(srcBand), srcBand, Band.getCPtr(maskBand), maskBand, Band.getCPtr(dstBand), dstBand, threshold, connectedness, options, callback);
/*     */   }
/*     */ 
/*     */   public static int SieveFilter(Band srcBand, Band maskBand, Band dstBand, int threshold, int connectedness, Vector options) {
/* 382 */     return gdalJNI.SieveFilter__SWIG_2(Band.getCPtr(srcBand), srcBand, Band.getCPtr(maskBand), maskBand, Band.getCPtr(dstBand), dstBand, threshold, connectedness, options);
/*     */   }
/*     */ 
/*     */   public static int SieveFilter(Band srcBand, Band maskBand, Band dstBand, int threshold, int connectedness) {
/* 386 */     return gdalJNI.SieveFilter__SWIG_3(Band.getCPtr(srcBand), srcBand, Band.getCPtr(maskBand), maskBand, Band.getCPtr(dstBand), dstBand, threshold, connectedness);
/*     */   }
/*     */ 
/*     */   public static int SieveFilter(Band srcBand, Band maskBand, Band dstBand, int threshold) {
/* 390 */     return gdalJNI.SieveFilter__SWIG_4(Band.getCPtr(srcBand), srcBand, Band.getCPtr(maskBand), maskBand, Band.getCPtr(dstBand), dstBand, threshold);
/*     */   }
/*     */ 
/*     */   public static int RegenerateOverviews(Band srcBand, Band[] overviewBandCount, String resampling, ProgressCallback callback) {
/* 394 */     return gdalJNI.RegenerateOverviews__SWIG_0(Band.getCPtr(srcBand), srcBand, overviewBandCount, resampling, callback);
/*     */   }
/*     */ 
/*     */   public static int RegenerateOverviews(Band srcBand, Band[] overviewBandCount, String resampling) {
/* 398 */     return gdalJNI.RegenerateOverviews__SWIG_2(Band.getCPtr(srcBand), srcBand, overviewBandCount, resampling);
/*     */   }
/*     */ 
/*     */   public static int RegenerateOverviews(Band srcBand, Band[] overviewBandCount) {
/* 402 */     return gdalJNI.RegenerateOverviews__SWIG_3(Band.getCPtr(srcBand), srcBand, overviewBandCount);
/*     */   }
/*     */ 
/*     */   public static int RegenerateOverview(Band srcBand, Band overviewBand, String resampling, ProgressCallback callback) {
/* 406 */     return gdalJNI.RegenerateOverview__SWIG_0(Band.getCPtr(srcBand), srcBand, Band.getCPtr(overviewBand), overviewBand, resampling, callback);
/*     */   }
/*     */ 
/*     */   public static int RegenerateOverview(Band srcBand, Band overviewBand, String resampling) {
/* 410 */     return gdalJNI.RegenerateOverview__SWIG_2(Band.getCPtr(srcBand), srcBand, Band.getCPtr(overviewBand), overviewBand, resampling);
/*     */   }
/*     */ 
/*     */   public static int RegenerateOverview(Band srcBand, Band overviewBand) {
/* 414 */     return gdalJNI.RegenerateOverview__SWIG_3(Band.getCPtr(srcBand), srcBand, Band.getCPtr(overviewBand), overviewBand);
/*     */   }
/*     */ 
/*     */   public static int GridCreate(String algorithmOptions, double[][] points, double xMin, double xMax, double yMin, double yMax, int xSize, int ySize, int dataType, ByteBuffer nioBuffer, ProgressCallback callback) {
/* 418 */     return gdalJNI.GridCreate__SWIG_0(algorithmOptions, points, xMin, xMax, yMin, yMax, xSize, ySize, dataType, nioBuffer, callback);
/*     */   }
/*     */ 
/*     */   public static int GridCreate(String algorithmOptions, double[][] points, double xMin, double xMax, double yMin, double yMax, int xSize, int ySize, int dataType, ByteBuffer nioBuffer) {
/* 422 */     return gdalJNI.GridCreate__SWIG_2(algorithmOptions, points, xMin, xMax, yMin, yMax, xSize, ySize, dataType, nioBuffer);
/*     */   }
/*     */ 
/*     */   public static int ContourGenerate(Band srcBand, double contourInterval, double contourBase, double[] fixedLevelCount, int useNoData, double noDataValue, Layer dstLayer, int idField, int elevField, ProgressCallback callback) {
/* 426 */     return gdalJNI.ContourGenerate__SWIG_0(Band.getCPtr(srcBand), srcBand, contourInterval, contourBase, fixedLevelCount, useNoData, noDataValue, dstLayer, idField, elevField, callback);
/*     */   }
/*     */ 
/*     */   public static int ContourGenerate(Band srcBand, double contourInterval, double contourBase, double[] fixedLevelCount, int useNoData, double noDataValue, Layer dstLayer, int idField, int elevField) {
/* 430 */     return gdalJNI.ContourGenerate__SWIG_2(Band.getCPtr(srcBand), srcBand, contourInterval, contourBase, fixedLevelCount, useNoData, noDataValue, dstLayer, idField, elevField);
/*     */   }
/*     */ 
/*     */   public static Dataset AutoCreateWarpedVRT(Dataset src_ds, String src_wkt, String dst_wkt, int eResampleAlg, double maxerror) {
/* 434 */     long cPtr = gdalJNI.AutoCreateWarpedVRT__SWIG_0(Dataset.getCPtr(src_ds), src_ds, src_wkt, dst_wkt, eResampleAlg, maxerror);
/* 435 */     return (cPtr == 0L) ? null : new Dataset(cPtr, true);
/*     */   }
/*     */ 
/*     */   public static Dataset AutoCreateWarpedVRT(Dataset src_ds, String src_wkt, String dst_wkt, int eResampleAlg) {
/* 439 */     long cPtr = gdalJNI.AutoCreateWarpedVRT__SWIG_1(Dataset.getCPtr(src_ds), src_ds, src_wkt, dst_wkt, eResampleAlg);
/* 440 */     return (cPtr == 0L) ? null : new Dataset(cPtr, true);
/*     */   }
/*     */ 
/*     */   public static Dataset AutoCreateWarpedVRT(Dataset src_ds, String src_wkt, String dst_wkt) {
/* 444 */     long cPtr = gdalJNI.AutoCreateWarpedVRT__SWIG_2(Dataset.getCPtr(src_ds), src_ds, src_wkt, dst_wkt);
/* 445 */     return (cPtr == 0L) ? null : new Dataset(cPtr, true);
/*     */   }
/*     */ 
/*     */   public static Dataset AutoCreateWarpedVRT(Dataset src_ds, String src_wkt) {
/* 449 */     long cPtr = gdalJNI.AutoCreateWarpedVRT__SWIG_3(Dataset.getCPtr(src_ds), src_ds, src_wkt);
/* 450 */     return (cPtr == 0L) ? null : new Dataset(cPtr, true);
/*     */   }
/*     */ 
/*     */   public static Dataset AutoCreateWarpedVRT(Dataset src_ds) {
/* 454 */     long cPtr = gdalJNI.AutoCreateWarpedVRT__SWIG_4(Dataset.getCPtr(src_ds), src_ds);
/* 455 */     return (cPtr == 0L) ? null : new Dataset(cPtr, true);
/*     */   }
/*     */ 
/*     */   public static void ApplyGeoTransform(double[] padfGeoTransform, double dfPixel, double dfLine, double[] pdfGeoX, double[] pdfGeoY) {
/* 459 */     gdalJNI.ApplyGeoTransform(padfGeoTransform, dfPixel, dfLine, pdfGeoX, pdfGeoY);
/*     */   }
/*     */ 
/*     */   public static int InvGeoTransform(double[] gt_in, double[] gt_out) {
/* 463 */     return gdalJNI.InvGeoTransform(gt_in, gt_out);
/*     */   }
/*     */ 
/*     */   public static String VersionInfo(String request) {
/* 467 */     return gdalJNI.VersionInfo__SWIG_0(request);
/*     */   }
/*     */ 
/*     */   public static String VersionInfo() {
/* 471 */     return gdalJNI.VersionInfo__SWIG_1();
/*     */   }
/*     */ 
/*     */   public static void AllRegister() {
/* 475 */     gdalJNI.AllRegister();
/*     */   }
/*     */ 
/*     */   public static void GDALDestroyDriverManager() {
/* 479 */     gdalJNI.GDALDestroyDriverManager();
/*     */   }
/*     */ 
/*     */   public static int GetCacheMax() {
/* 483 */     return gdalJNI.GetCacheMax();
/*     */   }
/*     */ 
/*     */   public static int GetCacheUsed() {
/* 487 */     return gdalJNI.GetCacheUsed();
/*     */   }
/*     */ 
/*     */   public static void SetCacheMax(int nBytes) {
/* 491 */     gdalJNI.SetCacheMax(nBytes);
/*     */   }
/*     */ 
/*     */   public static int GetDataTypeSize(int eDataType) {
/* 495 */     return gdalJNI.GetDataTypeSize(eDataType);
/*     */   }
/*     */ 
/*     */   public static int DataTypeIsComplex(int eDataType) {
/* 499 */     return gdalJNI.DataTypeIsComplex(eDataType);
/*     */   }
/*     */ 
/*     */   public static String GetDataTypeName(int eDataType) {
/* 503 */     return gdalJNI.GetDataTypeName(eDataType);
/*     */   }
/*     */ 
/*     */   public static int GetDataTypeByName(String pszDataTypeName) {
/* 507 */     return gdalJNI.GetDataTypeByName(pszDataTypeName);
/*     */   }
/*     */ 
/*     */   public static String GetColorInterpretationName(int eColorInterp) {
/* 511 */     return gdalJNI.GetColorInterpretationName(eColorInterp);
/*     */   }
/*     */ 
/*     */   public static String GetPaletteInterpretationName(int ePaletteInterp) {
/* 515 */     return gdalJNI.GetPaletteInterpretationName(ePaletteInterp);
/*     */   }
/*     */ 
/*     */   public static String DecToDMS(double dfAngle, String pszAxis, int nPrecision) {
/* 519 */     return gdalJNI.DecToDMS__SWIG_0(dfAngle, pszAxis, nPrecision);
/*     */   }
/*     */ 
/*     */   public static String DecToDMS(double dfAngle, String pszAxis) {
/* 523 */     return gdalJNI.DecToDMS__SWIG_1(dfAngle, pszAxis);
/*     */   }
/*     */ 
/*     */   public static double PackedDMSToDec(double dfPacked) {
/* 527 */     return gdalJNI.PackedDMSToDec(dfPacked);
/*     */   }
/*     */ 
/*     */   public static double DecToPackedDMS(double dfDec) {
/* 531 */     return gdalJNI.DecToPackedDMS(dfDec);
/*     */   }
/*     */ 
/*     */   public static XMLNode ParseXMLString(String pszXMLString) {
/* 535 */     long cPtr = gdalJNI.ParseXMLString(pszXMLString);
/* 536 */     return (cPtr == 0L) ? null : new XMLNode(cPtr, true);
/*     */   }
/*     */ 
/*     */   public static String SerializeXMLTree(XMLNode xmlnode) {
/* 540 */     return gdalJNI.SerializeXMLTree(XMLNode.getCPtr(xmlnode), xmlnode);
/*     */   }
/*     */ 
/*     */   public static int GetDriverCount() {
/* 544 */     return gdalJNI.GetDriverCount();
/*     */   }
/*     */ 
/*     */   public static Driver GetDriverByName(String name) {
/* 548 */     long cPtr = gdalJNI.GetDriverByName(name);
/* 549 */     return (cPtr == 0L) ? null : new Driver(cPtr, false);
/*     */   }
/*     */ 
/*     */   public static Driver GetDriver(int i) {
/* 553 */     long cPtr = gdalJNI.GetDriver(i);
/* 554 */     return (cPtr == 0L) ? null : new Driver(cPtr, false);
/*     */   }
/*     */ 
/*     */   public static Dataset Open(String utf8_path, int eAccess) {
/* 558 */     long cPtr = gdalJNI.Open__SWIG_0(utf8_path, eAccess);
/* 559 */     return (cPtr == 0L) ? null : new Dataset(cPtr, true);
/*     */   }
/*     */ 
/*     */   public static Dataset Open(String name) {
/* 563 */     long cPtr = gdalJNI.Open__SWIG_1(name);
/* 564 */     return (cPtr == 0L) ? null : new Dataset(cPtr, true);
/*     */   }
/*     */ 
/*     */   public static Dataset OpenShared(String utf8_path, int eAccess) {
/* 568 */     long cPtr = gdalJNI.OpenShared__SWIG_0(utf8_path, eAccess);
/* 569 */     return (cPtr == 0L) ? null : new Dataset(cPtr, true);
/*     */   }
/*     */ 
/*     */   public static Dataset OpenShared(String utf8_path) {
/* 573 */     long cPtr = gdalJNI.OpenShared__SWIG_1(utf8_path);
/* 574 */     return (cPtr == 0L) ? null : new Dataset(cPtr, true);
/*     */   }
/*     */ 
/*     */   public static Driver IdentifyDriver(String utf8_path, Vector papszSiblings) {
/* 578 */     long cPtr = gdalJNI.IdentifyDriver__SWIG_0(utf8_path, papszSiblings);
/* 579 */     return (cPtr == 0L) ? null : new Driver(cPtr, false);
/*     */   }
/*     */ 
/*     */   public static Driver IdentifyDriver(String utf8_path) {
/* 583 */     long cPtr = gdalJNI.IdentifyDriver__SWIG_1(utf8_path);
/* 584 */     return (cPtr == 0L) ? null : new Driver(cPtr, false);
/*     */   }
/*     */ 
/*     */   public static Vector GeneralCmdLineProcessor(Vector papszArgv, int nOptions) {
/* 588 */     return gdalJNI.GeneralCmdLineProcessor__SWIG_0(papszArgv, nOptions);
/*     */   }
/*     */ 
/*     */   public static Vector GeneralCmdLineProcessor(Vector papszArgv) {
/* 592 */     return gdalJNI.GeneralCmdLineProcessor__SWIG_1(papszArgv);
/*     */   }
/*     */ }

/* Location:           E:\temp\1117\gdal.jar
 * Qualified Name:     org.gdal.gdal.gdal
 * JD-Core Version:    0.5.4
 */