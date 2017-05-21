/*     */ package org.gdal.gdal;
/*     */ 
///*     */ import java.awt.Color;//android环境不认此包，需要导入jre运行环境
		  import android.graphics.Color;
///*     */ import java.awt.image.IndexColorModel;
/*     */ 
/*     */ public class ColorTable
/*     */   implements Cloneable
/*     */ {
/*     */   private long swigCPtr;
/*     */   protected boolean swigCMemOwn;
/*     */   private Object parentReference;
/*     */ 
/*     */   protected ColorTable(long cPtr, boolean cMemoryOwn)
/*     */   {
/*  20 */     if (cPtr == 0L)
/*  21 */       throw new RuntimeException();
/*  22 */     this.swigCMemOwn = cMemoryOwn;
/*  23 */     this.swigCPtr = cPtr;
/*     */   }
/*     */ 
/*     */   protected static long getCPtr(ColorTable obj) {
/*  27 */     return (obj == null) ? 0L : obj.swigCPtr;
/*     */   }
/*     */ 
/*     */   protected void finalize() {
/*  31 */     delete();
/*     */   }
/*     */ 
/*     */   public synchronized void delete() {
/*  35 */     if (this.swigCPtr != 0L) {
/*  36 */       if (this.swigCMemOwn) {
/*  37 */         this.swigCMemOwn = false;
/*  38 */         gdalJNI.delete_ColorTable(this.swigCPtr);
/*     */       }
/*  40 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void addReference(Object reference)
/*     */   {
/*  48 */     this.parentReference = reference;
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*  53 */     return Clone();
/*     */   }
/*     */ 
/*     */   /*public IndexColorModel getIndexColorModel(int bits)
        {
  58      int size = GetCount();
  59      byte[] reds = new byte[size];
  60      byte[] greens = new byte[size];
  61      byte[] blues = new byte[size];
  62      byte[] alphas = new byte[size];
  63      int noAlphas = 0;
  64      int zeroAlphas = 0;
  65      int lastAlphaIndex = -1;
      
  67      Color entry = null;
  68      for (int i = 0; i < size; ++i) {
  69        entry = GetColorEntry(i);
  70        reds[i] = (byte)(entry.getRed() & 0xFF);
  71        greens[i] = (byte)(entry.getGreen() & 0xFF);
  72        blues[i] = (byte)(entry.getBlue() & 0xFF);
  73        byte alpha = (byte)(entry.getAlpha() & 0xFF);
  74        if (alpha == 255) {
  75          ++noAlphas;
            }
  77        else if (alpha == 0) {
  78          ++zeroAlphas;
  79          lastAlphaIndex = i;
            }
      
  82        alphas[i] = alpha;
          }
  84      if (noAlphas == size)
  85        return new IndexColorModel(bits, size, reds, greens, blues);
  86      if ((noAlphas == size - 1) && (zeroAlphas == 1)) {
  87        return new IndexColorModel(bits, size, reds, greens, blues, lastAlphaIndex);
          }
  89      return new IndexColorModel(bits, size, reds, greens, blues, alphas);
        }*/
/*     */ 
/*     */   public ColorTable(int palette) {
/*  93 */     this(gdalJNI.new_ColorTable__SWIG_0(palette), true);
/*     */   }
/*     */ 
/*     */   public ColorTable() {
/*  97 */     this(gdalJNI.new_ColorTable__SWIG_1(), true);
/*     */   }
/*     */ 
/*     */   public ColorTable Clone() {
/* 101 */     long cPtr = gdalJNI.ColorTable_Clone(this.swigCPtr, this);
/* 102 */     return (cPtr == 0L) ? null : new ColorTable(cPtr, true);
/*     */   }
/*     */ 
/*     */   public int GetPaletteInterpretation() {
/* 106 */     return gdalJNI.ColorTable_GetPaletteInterpretation(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public int GetCount() {
/* 110 */     return gdalJNI.ColorTable_GetCount(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public Color GetColorEntry(int entry) {
/* 114 */     return gdalJNI.ColorTable_GetColorEntry(this.swigCPtr, this, entry);
/*     */   }
/*     */ 
/*     */   public void SetColorEntry(int entry, Color centry) {
/* 118 */     gdalJNI.ColorTable_SetColorEntry(this.swigCPtr, this, entry, centry);
/*     */   }
/*     */ 
/*     */   public void CreateColorRamp(int nStartIndex, Color startcolor, int nEndIndex, Color endcolor) {
/* 122 */     gdalJNI.ColorTable_CreateColorRamp(this.swigCPtr, this, nStartIndex, startcolor, nEndIndex, endcolor);
/*     */   }
/*     */ }

/* Location:           E:\temp\1117\gdal.jar
 * Qualified Name:     org.gdal.gdal.ColorTable
 * JD-Core Version:    0.5.4
 */