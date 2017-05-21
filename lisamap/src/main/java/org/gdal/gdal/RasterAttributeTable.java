/*     */ package org.gdal.gdal;
/*     */ 
/*     */ public class RasterAttributeTable
/*     */   implements Cloneable
/*     */ {
/*     */   private long swigCPtr;
/*     */   protected boolean swigCMemOwn;
/*     */ 
/*     */   protected RasterAttributeTable(long cPtr, boolean cMemoryOwn)
/*     */   {
/*  16 */     if (cPtr == 0L)
/*  17 */       throw new RuntimeException();
/*  18 */     this.swigCMemOwn = cMemoryOwn;
/*  19 */     this.swigCPtr = cPtr;
/*     */   }
/*     */ 
/*     */   protected static long getCPtr(RasterAttributeTable obj) {
/*  23 */     return (obj == null) ? 0L : obj.swigCPtr;
/*     */   }
/*     */ 
/*     */   protected void finalize() {
/*  27 */     delete();
/*     */   }
/*     */ 
/*     */   public synchronized void delete() {
/*  31 */     if (this.swigCPtr != 0L) {
/*  32 */       if (this.swigCMemOwn) {
/*  33 */         this.swigCMemOwn = false;
/*  34 */         gdalJNI.delete_RasterAttributeTable(this.swigCPtr);
/*     */       }
/*  36 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*  43 */     return Clone();
/*     */   }
/*     */ 
/*     */   public RasterAttributeTable() {
/*  47 */     this(gdalJNI.new_RasterAttributeTable(), true);
/*     */   }
/*     */ 
/*     */   public RasterAttributeTable Clone() {
/*  51 */     long cPtr = gdalJNI.RasterAttributeTable_Clone(this.swigCPtr, this);
/*  52 */     return (cPtr == 0L) ? null : new RasterAttributeTable(cPtr, true);
/*     */   }
/*     */ 
/*     */   public int GetColumnCount() {
/*  56 */     return gdalJNI.RasterAttributeTable_GetColumnCount(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public String GetNameOfCol(int iCol) {
/*  60 */     return gdalJNI.RasterAttributeTable_GetNameOfCol(this.swigCPtr, this, iCol);
/*     */   }
/*     */ 
/*     */   public int GetUsageOfCol(int iCol) {
/*  64 */     return gdalJNI.RasterAttributeTable_GetUsageOfCol(this.swigCPtr, this, iCol);
/*     */   }
/*     */ 
/*     */   public int GetTypeOfCol(int iCol) {
/*  68 */     return gdalJNI.RasterAttributeTable_GetTypeOfCol(this.swigCPtr, this, iCol);
/*     */   }
/*     */ 
/*     */   public int GetColOfUsage(int eUsage) {
/*  72 */     return gdalJNI.RasterAttributeTable_GetColOfUsage(this.swigCPtr, this, eUsage);
/*     */   }
/*     */ 
/*     */   public int GetRowCount() {
/*  76 */     return gdalJNI.RasterAttributeTable_GetRowCount(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public String GetValueAsString(int iRow, int iCol) {
/*  80 */     return gdalJNI.RasterAttributeTable_GetValueAsString(this.swigCPtr, this, iRow, iCol);
/*     */   }
/*     */ 
/*     */   public int GetValueAsInt(int iRow, int iCol) {
/*  84 */     return gdalJNI.RasterAttributeTable_GetValueAsInt(this.swigCPtr, this, iRow, iCol);
/*     */   }
/*     */ 
/*     */   public double GetValueAsDouble(int iRow, int iCol) {
/*  88 */     return gdalJNI.RasterAttributeTable_GetValueAsDouble(this.swigCPtr, this, iRow, iCol);
/*     */   }
/*     */ 
/*     */   public void SetValueAsString(int iRow, int iCol, String pszValue) {
/*  92 */     gdalJNI.RasterAttributeTable_SetValueAsString(this.swigCPtr, this, iRow, iCol, pszValue);
/*     */   }
/*     */ 
/*     */   public void SetValueAsInt(int iRow, int iCol, int nValue) {
/*  96 */     gdalJNI.RasterAttributeTable_SetValueAsInt(this.swigCPtr, this, iRow, iCol, nValue);
/*     */   }
/*     */ 
/*     */   public void SetValueAsDouble(int iRow, int iCol, double dfValue) {
/* 100 */     gdalJNI.RasterAttributeTable_SetValueAsDouble(this.swigCPtr, this, iRow, iCol, dfValue);
/*     */   }
/*     */ 
/*     */   public void SetRowCount(int nCount) {
/* 104 */     gdalJNI.RasterAttributeTable_SetRowCount(this.swigCPtr, this, nCount);
/*     */   }
/*     */ 
/*     */   public int CreateColumn(String pszName, int eType, int eUsage) {
/* 108 */     return gdalJNI.RasterAttributeTable_CreateColumn(this.swigCPtr, this, pszName, eType, eUsage);
/*     */   }
/*     */ 
/*     */   public boolean GetLinearBinning(double[] pdfRow0Min, double[] pdfBinSize) {
/* 112 */     return gdalJNI.RasterAttributeTable_GetLinearBinning(this.swigCPtr, this, pdfRow0Min, pdfBinSize);
/*     */   }
/*     */ 
/*     */   public int SetLinearBinning(double dfRow0Min, double dfBinSize) {
/* 116 */     return gdalJNI.RasterAttributeTable_SetLinearBinning(this.swigCPtr, this, dfRow0Min, dfBinSize);
/*     */   }
/*     */ 
/*     */   public int GetRowOfValue(double dfValue) {
/* 120 */     return gdalJNI.RasterAttributeTable_GetRowOfValue(this.swigCPtr, this, dfValue);
/*     */   }
/*     */ }

/* Location:           E:\temp\1117\gdal.jar
 * Qualified Name:     org.gdal.gdal.RasterAttributeTable
 * JD-Core Version:    0.5.4
 */