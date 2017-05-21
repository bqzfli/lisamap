/*    */ package org.gdal.gdal;
/*    */ 
/*    */ public final class XMLNodeType
/*    */ {
/* 12 */   public static final XMLNodeType CXT_Element = new XMLNodeType("CXT_Element", gdalJNI.CXT_Element_get());
/* 13 */   public static final XMLNodeType CXT_Text = new XMLNodeType("CXT_Text", gdalJNI.CXT_Text_get());
/* 14 */   public static final XMLNodeType CXT_Attribute = new XMLNodeType("CXT_Attribute", gdalJNI.CXT_Attribute_get());
/* 15 */   public static final XMLNodeType CXT_Comment = new XMLNodeType("CXT_Comment", gdalJNI.CXT_Comment_get());
/* 16 */   public static final XMLNodeType CXT_Literal = new XMLNodeType("CXT_Literal", gdalJNI.CXT_Literal_get());
/*    */ 
/* 52 */   private static XMLNodeType[] swigValues = { CXT_Element, CXT_Text, CXT_Attribute, CXT_Comment, CXT_Literal };
/* 53 */   private static int swigNext = 0;
/*    */   private final int swigValue;
/*    */   private final String swigName;
/*    */ 
/*    */   public final int swigValue()
/*    */   {
/* 19 */     return this.swigValue;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 23 */     return this.swigName;
/*    */   }
/*    */ 
/*    */   public static XMLNodeType swigToEnum(int swigValue) {
/* 27 */     if ((swigValue < swigValues.length) && (swigValue >= 0) && (swigValues[swigValue].swigValue == swigValue))
/* 28 */       return swigValues[swigValue];
/* 29 */     for (int i = 0; i < swigValues.length; ++i)
/* 30 */       if (swigValues[i].swigValue == swigValue)
/* 31 */         return swigValues[i];
/* 32 */     throw new IllegalArgumentException("No enum " + XMLNodeType.class + " with value " + swigValue);
/*    */   }
/*    */ 
/*    */   private XMLNodeType(String swigName) {
/* 36 */     this.swigName = swigName;
/* 37 */     this.swigValue = (swigNext++);
/*    */   }
/*    */ 
/*    */   private XMLNodeType(String swigName, int swigValue) {
/* 41 */     this.swigName = swigName;
/* 42 */     this.swigValue = swigValue;
/* 43 */     swigNext = swigValue + 1;
/*    */   }
/*    */ 
/*    */   private XMLNodeType(String swigName, XMLNodeType swigEnum) {
/* 47 */     this.swigName = swigName;
/* 48 */     this.swigValue = swigEnum.swigValue;
/* 49 */     swigNext = this.swigValue + 1;
/*    */   }
/*    */ }

/* Location:           E:\temp\1117\gdal.jar
 * Qualified Name:     org.gdal.gdal.XMLNodeType
 * JD-Core Version:    0.5.4
 */