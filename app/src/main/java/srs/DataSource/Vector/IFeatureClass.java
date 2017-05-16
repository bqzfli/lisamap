package srs.DataSource.Vector;

import java.io.IOException;
import java.util.List;

import srs.CoordinateSystem.ICoordinateSystem;
import srs.DataSource.DataTable.DataTable;
import srs.DataSource.Table.IFields;
import srs.DataSource.Vector.Event.AttributeEditEnableManager;
import srs.DataSource.Vector.Event.AttributeManager;
import srs.Geometry.*;
import srs.Operation.Event.FeatureChangedManager;
import srs.Operation.Event.SelectionChangedEventManager;


public interface IFeatureClass{
	String getFid();
	String getName();
	String getSource();
	IEnvelope getExtent() throws IOException;
	srsGeometryType getGeometryType();
	ICoordinateSystem getCoordinateSystem();
	void setCoordinateSystem(ICoordinateSystem value);
	boolean IsEditing();

	int getFeatureCount();
	IFeature getFeature(int fid);
	List<IFeature> getAllFeature();
	IGeometry getGeometry(int fid) throws IOException;	

	DataTable getAttributeTable();

	List<Integer> getSelectionSet();
	void setSelectionSet(List<Integer> value);
	List<Integer> Select(IGeometry geometry, SearchType type) throws IOException;
	List<Integer> SelectOnly(IGeometry geometry, SearchType type,double distance) throws IOException;
	List<Integer> SelectOnlyOne(IGeometry geometry, SearchType type,double distance) throws IOException;
	List<Integer> SelectOnly(IGeometry geometry, SearchType type,double distance,List<Integer> selectionOfDisplay) throws IOException;
	List<Integer> SelectOnlyOne(IGeometry geometry, SearchType type,double distance,List<Integer> selectionOfDisplay) throws IOException;

	void AddFeature(IFeature[] features) throws IOException;
	void ModifyFeature(IFeature[] features) throws IOException;
	void ModifyAttribute(IFeature[] features) throws IOException;
	void DeleteFeature(IFeature[] features) throws IOException;
	void DeleteFeature(List<Integer> fids) throws IOException;
	void DeleteFeature(int fid) throws IOException;
	
	/**  字段集*/
	IFields getFields();
	void setFields(IFields value);
	void ExportFeatures(String file, List<Integer> index, ICoordinateSystem spatialRef);

	/**记录改变事件
	 * @return
	 */
	FeatureChangedManager getFeatureChanged();
	/**选择集改变事件
	 * @return
	 */
	SelectionChangedEventManager getSelectionSetChanged();	
	AttributeEditEnableManager getEditEnableChanged();
	AttributeManager getAtrributeChanged();
	
	/**释放资源
	 * @throws Exception
	 */
	void dispose() throws Exception;
}