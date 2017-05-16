package srs.Geometry;

public interface IGeoInterOperator {

    /**导出到GML
     * @return
     */
    String ExportToGML();

    /**导出到KML
     * @param altitudeMode 导出到KML
     * @return
     */
    String ExportToKML(String altitudeMode);

    /**导出到WKB
     * @return WKB
     */
    byte[] ExportToWKB();

    /**导出到ESRI
     * @return
     */
    byte[] ExportToESRI();

    /**导出到WKT
     * @return WKT
     */
    String ExportToWKT();
}
