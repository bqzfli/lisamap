/*    */ package org.gdal.gdalconst;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public class gdalconstJNI
/*    */ {
/* 14 */   private static boolean available = false;
/*    */ 
/*    */   public static boolean isAvailable()
/*    */   {
/* 28 */     return available;
/*    */   }
/*    */ 
/*    */   public static final native int GDT_Unknown_get();
/*    */ 
/*    */   public static final native int GDT_Byte_get();
/*    */ 
/*    */   public static final native int GDT_UInt16_get();
/*    */ 
/*    */   public static final native int GDT_Int16_get();
/*    */ 
/*    */   public static final native int GDT_UInt32_get();
/*    */ 
/*    */   public static final native int GDT_Int32_get();
/*    */ 
/*    */   public static final native int GDT_Float32_get();
/*    */ 
/*    */   public static final native int GDT_Float64_get();
/*    */ 
/*    */   public static final native int GDT_CInt16_get();
/*    */ 
/*    */   public static final native int GDT_CInt32_get();
/*    */ 
/*    */   public static final native int GDT_CFloat32_get();
/*    */ 
/*    */   public static final native int GDT_CFloat64_get();
/*    */ 
/*    */   public static final native int GDT_TypeCount_get();
/*    */ 
/*    */   public static final native int GA_ReadOnly_get();
/*    */ 
/*    */   public static final native int GA_Update_get();
/*    */ 
/*    */   public static final native int GF_Read_get();
/*    */ 
/*    */   public static final native int GF_Write_get();
/*    */ 
/*    */   public static final native int GCI_Undefined_get();
/*    */ 
/*    */   public static final native int GCI_GrayIndex_get();
/*    */ 
/*    */   public static final native int GCI_PaletteIndex_get();
/*    */ 
/*    */   public static final native int GCI_RedBand_get();
/*    */ 
/*    */   public static final native int GCI_GreenBand_get();
/*    */ 
/*    */   public static final native int GCI_BlueBand_get();
/*    */ 
/*    */   public static final native int GCI_AlphaBand_get();
/*    */ 
/*    */   public static final native int GCI_HueBand_get();
/*    */ 
/*    */   public static final native int GCI_SaturationBand_get();
/*    */ 
/*    */   public static final native int GCI_LightnessBand_get();
/*    */ 
/*    */   public static final native int GCI_CyanBand_get();
/*    */ 
/*    */   public static final native int GCI_MagentaBand_get();
/*    */ 
/*    */   public static final native int GCI_YellowBand_get();
/*    */ 
/*    */   public static final native int GCI_BlackBand_get();
/*    */ 
/*    */   public static final native int GCI_YCbCr_YBand_get();
/*    */ 
/*    */   public static final native int GCI_YCbCr_CrBand_get();
/*    */ 
/*    */   public static final native int GCI_YCbCr_CbBand_get();
/*    */ 
/*    */   public static final native int GRA_NearestNeighbour_get();
/*    */ 
/*    */   public static final native int GRA_Bilinear_get();
/*    */ 
/*    */   public static final native int GRA_Cubic_get();
/*    */ 
/*    */   public static final native int GRA_CubicSpline_get();
/*    */ 
/*    */   public static final native int GRA_Lanczos_get();
/*    */ 
/*    */   public static final native int GPI_Gray_get();
/*    */ 
/*    */   public static final native int GPI_RGB_get();
/*    */ 
/*    */   public static final native int GPI_CMYK_get();
/*    */ 
/*    */   public static final native int GPI_HLS_get();
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
/*    */   public static final native int CE_None_get();
/*    */ 
/*    */   public static final native int CE_Debug_get();
/*    */ 
/*    */   public static final native int CE_Warning_get();
/*    */ 
/*    */   public static final native int CE_Failure_get();
/*    */ 
/*    */   public static final native int CE_Fatal_get();
/*    */ 
/*    */   public static final native int CPLE_None_get();
/*    */ 
/*    */   public static final native int CPLE_AppDefined_get();
/*    */ 
/*    */   public static final native int CPLE_OutOfMemory_get();
/*    */ 
/*    */   public static final native int CPLE_FileIO_get();
/*    */ 
/*    */   public static final native int CPLE_OpenFailed_get();
/*    */ 
/*    */   public static final native int CPLE_IllegalArg_get();
/*    */ 
/*    */   public static final native int CPLE_NotSupported_get();
/*    */ 
/*    */   public static final native int CPLE_AssertionFailed_get();
/*    */ 
/*    */   public static final native int CPLE_NoWriteAccess_get();
/*    */ 
/*    */   public static final native int CPLE_UserInterrupt_get();
/*    */ 
/*    */   public static final native String DMD_LONGNAME_get();
/*    */ 
/*    */   public static final native String DMD_HELPTOPIC_get();
/*    */ 
/*    */   public static final native String DMD_MIMETYPE_get();
/*    */ 
/*    */   public static final native String DMD_EXTENSION_get();
/*    */ 
/*    */   public static final native String DMD_CREATIONOPTIONLIST_get();
/*    */ 
/*    */   public static final native String DMD_CREATIONDATATYPES_get();
/*    */ 
/*    */   public static final native String DCAP_CREATE_get();
/*    */ 
/*    */   public static final native String DCAP_CREATECOPY_get();
/*    */ 
/*    */   public static final native String DCAP_VIRTUALIO_get();
/*    */ 
/*    */   public static final native int CPLES_BackslashQuotable_get();
/*    */ 
/*    */   public static final native int CPLES_XML_get();
/*    */ 
/*    */   public static final native int CPLES_URL_get();
/*    */ 
/*    */   public static final native int CPLES_SQL_get();
/*    */ 
/*    */   public static final native int CPLES_CSV_get();
/*    */ 
/*    */   public static final native int GFT_Integer_get();
/*    */ 
/*    */   public static final native int GFT_Real_get();
/*    */ 
/*    */   public static final native int GFT_String_get();
/*    */ 
/*    */   public static final native int GFU_Generic_get();
/*    */ 
/*    */   public static final native int GFU_PixelCount_get();
/*    */ 
/*    */   public static final native int GFU_Name_get();
/*    */ 
/*    */   public static final native int GFU_Min_get();
/*    */ 
/*    */   public static final native int GFU_Max_get();
/*    */ 
/*    */   public static final native int GFU_MinMax_get();
/*    */ 
/*    */   public static final native int GFU_Red_get();
/*    */ 
/*    */   public static final native int GFU_Green_get();
/*    */ 
/*    */   public static final native int GFU_Blue_get();
/*    */ 
/*    */   public static final native int GFU_Alpha_get();
/*    */ 
/*    */   public static final native int GFU_RedMin_get();
/*    */ 
/*    */   public static final native int GFU_GreenMin_get();
/*    */ 
/*    */   public static final native int GFU_BlueMin_get();
/*    */ 
/*    */   public static final native int GFU_AlphaMin_get();
/*    */ 
/*    */   public static final native int GFU_RedMax_get();
/*    */ 
/*    */   public static final native int GFU_GreenMax_get();
/*    */ 
/*    */   public static final native int GFU_BlueMax_get();
/*    */ 
/*    */   public static final native int GFU_AlphaMax_get();
/*    */ 
/*    */   public static final native int GFU_MaxCount_get();
/*    */ 
/*    */   public static final native int GMF_ALL_VALID_get();
/*    */ 
/*    */   public static final native int GMF_PER_DATASET_get();
/*    */ 
/*    */   public static final native int GMF_ALPHA_get();
/*    */ 
/*    */   public static final native int GMF_NODATA_get();
/*    */ 
/*    */   public static final native int GARIO_PENDING_get();
/*    */ 
/*    */   public static final native int GARIO_UPDATE_get();
/*    */ 
/*    */   public static final native int GARIO_ERROR_get();
/*    */ 
/*    */   public static final native int GARIO_COMPLETE_get();
/*    */ 
/*    */   static
/*    */   {
/*    */     try
/*    */     {
/* 18 */       System.loadLibrary("gdalconstjni");
/* 19 */       available = true;
/*    */     } catch (UnsatisfiedLinkError e) {
/* 21 */       available = false;
/* 22 */       System.err.println("Native library load failed.");
/* 23 */       System.err.println(e);
/*    */     }
/*    */   }
/*    */ }

/* Location:           E:\temp\1117\gdal.jar
 * Qualified Name:     org.gdal.gdalconst.gdalconstJNI
 * JD-Core Version:    0.5.4
 */