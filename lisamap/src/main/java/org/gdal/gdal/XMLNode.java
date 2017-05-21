/*     */ package org.gdal.gdal;
/*     */ 
/*     */ public class XMLNode
/*     */ {
/*     */   private long swigCPtr;
/*     */   protected boolean swigCMemOwn;
/*     */   private Object parentReference;
/*     */ 
/*     */   protected XMLNode(long cPtr, boolean cMemoryOwn)
/*     */   {
/*  16 */     if (cPtr == 0L)
/*  17 */       throw new RuntimeException();
/*  18 */     this.swigCMemOwn = cMemoryOwn;
/*  19 */     this.swigCPtr = cPtr;
/*     */   }
/*     */ 
/*     */   protected static long getCPtr(XMLNode obj) {
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
/*  34 */         gdalJNI.delete_XMLNode(this.swigCPtr);
/*     */       }
/*  36 */       this.swigCPtr = 0L;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static long getCPtrAndDisown(XMLNode obj)
/*     */   {
/*  43 */     if (obj != null)
/*     */     {
/*  45 */       obj.swigCMemOwn = false;
/*  46 */       obj.parentReference = null;
/*     */     }
/*  48 */     return getCPtr(obj);
/*     */   }
/*     */ 
/*     */   protected void addReference(Object reference)
/*     */   {
/*  53 */     this.parentReference = reference;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object obj) {
/*  57 */     boolean equal = false;
/*  58 */     if (obj instanceof XMLNode)
/*  59 */       equal = ((XMLNode)obj).swigCPtr == this.swigCPtr;
/*  60 */     return equal;
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/*  64 */     return (int)this.swigCPtr;
/*     */   }
/*     */ 
/*     */   public XMLNodeType getType()
/*     */   {
/*  69 */     return XMLNodeType.swigToEnum(gdalJNI.XMLNode_Type_get(this.swigCPtr, this));
/*     */   }
/*     */ 
/*     */   public String getValue() {
/*  73 */     return gdalJNI.XMLNode_Value_get(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public XMLNode getNext() {
/*  77 */     long cPtr = gdalJNI.XMLNode_Next_get(this.swigCPtr, this);
/*  78 */     return (cPtr == 0L) ? null : new XMLNode(cPtr, false);
/*     */   }
/*     */ 
/*     */   public XMLNode getChild() {
/*  82 */     long cPtr = gdalJNI.XMLNode_Child_get(this.swigCPtr, this);
/*  83 */     return (cPtr == 0L) ? null : new XMLNode(cPtr, false);
/*     */   }
/*     */ 
/*     */   public XMLNode(String pszString) {
/*  87 */     this(gdalJNI.new_XMLNode__SWIG_0(pszString), true);
/*     */   }
/*     */ 
/*     */   public XMLNode(XMLNodeType eType, String pszText) {
/*  91 */     this(gdalJNI.new_XMLNode__SWIG_1(eType.swigValue(), pszText), true);
/*     */   }
/*     */ 
/*     */   public static XMLNode ParseXMLFile(String pszFilename) {
/*  95 */     long cPtr = gdalJNI.XMLNode_ParseXMLFile(pszFilename);
/*  96 */     return (cPtr == 0L) ? null : new XMLNode(cPtr, true);
/*     */   }
/*     */ 
/*     */   public String SerializeXMLTree() {
/* 100 */     return gdalJNI.XMLNode_SerializeXMLTree(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 104 */     return gdalJNI.XMLNode_toString(this.swigCPtr, this);
/*     */   }
/*     */ 
/*     */   public XMLNode SearchXMLNode(String pszElement) {
/* 108 */     long cPtr = gdalJNI.XMLNode_SearchXMLNode(this.swigCPtr, this, pszElement);
/* 109 */     XMLNode ret = null;
/* 110 */     if (cPtr != 0L) {
/* 111 */       ret = new XMLNode(cPtr, false);
/* 112 */       ret.addReference(this);
/*     */     }
/* 114 */     return ret;
/*     */   }
/*     */ 
/*     */   public XMLNode GetXMLNode(String pszPath) {
/* 118 */     long cPtr = gdalJNI.XMLNode_GetXMLNode(this.swigCPtr, this, pszPath);
/* 119 */     XMLNode ret = null;
/* 120 */     if (cPtr != 0L) {
/* 121 */       ret = new XMLNode(cPtr, false);
/* 122 */       ret.addReference(this);
/*     */     }
/* 124 */     return ret;
/*     */   }
/*     */ 
/*     */   public String GetXMLValue(String pszPath, String pszDefault) {
/* 128 */     return gdalJNI.XMLNode_GetXMLValue(this.swigCPtr, this, pszPath, pszDefault);
/*     */   }
/*     */ 
/*     */   public void AddXMLChild(XMLNode psChild) {
/* 132 */     gdalJNI.XMLNode_AddXMLChild(this.swigCPtr, this, getCPtr(psChild), psChild);
/*     */   }
/*     */ 
/*     */   public void AddXMLSibling(XMLNode psNewSibling) {
/* 136 */     gdalJNI.XMLNode_AddXMLSibling(this.swigCPtr, this, getCPtr(psNewSibling), psNewSibling);
/*     */   }
/*     */ 
/*     */   public XMLNode Clone() {
/* 140 */     long cPtr = gdalJNI.XMLNode_Clone(this.swigCPtr, this);
/* 141 */     return (cPtr == 0L) ? null : new XMLNode(cPtr, true);
/*     */   }
/*     */ 
/*     */   public int SetXMLValue(String pszPath, String pszValue) {
/* 145 */     return gdalJNI.XMLNode_SetXMLValue(this.swigCPtr, this, pszPath, pszValue);
/*     */   }
/*     */ 
/*     */   public void StripXMLNamespace(String pszNamespace, int bRecurse) {
/* 149 */     gdalJNI.XMLNode_StripXMLNamespace(this.swigCPtr, this, pszNamespace, bRecurse);
/*     */   }
/*     */ }

/* Location:           E:\temp\1117\gdal.jar
 * Qualified Name:     org.gdal.gdal.XMLNode
 * JD-Core Version:    0.5.4
 */