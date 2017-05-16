package srs.Display.Symbol;

public interface ISymbol {
	
    /**颜色
     * @return 颜色
     */
    int getColor();
    
    /**颜色
     * @param value 颜色
     */
    void setColor(int value);
    
    
    /**透明度
     * @return
     */
    int getTransparent();
    
    /**透明度
     * @param value
     */
    void setTransparent(int value);

    ISymbol Clone();

	void dispose();
}
