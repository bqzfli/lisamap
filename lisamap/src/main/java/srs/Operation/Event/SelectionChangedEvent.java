package srs.Operation.Event;

import java.util.EventObject;
import srs.DataSource.Vector.ShapeFileClass;

@SuppressWarnings("serial")
public class SelectionChangedEvent extends EventObject {

	ShapeFileClass _shape;
	SelectEventArgs _selectEventArgs;
	public SelectionChangedEvent(Object source,SelectEventArgs e){
		super(source);
		this._shape=(ShapeFileClass)source;
		this._selectEventArgs=e;
	}
	
	public ShapeFileClass getShapeFileClass() {
		return this._shape;
	}
	
	public SelectEventArgs getSelectEventArgs(){
		return this._selectEventArgs;
	}

}
