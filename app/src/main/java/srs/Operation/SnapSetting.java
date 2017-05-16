package srs.Operation;

public final class SnapSetting{
	public Object target;
	public boolean useEnd;
	public boolean useVertex;
	public boolean useLine;
	public double buffer;

	public SnapSetting clone(){
		SnapSetting varCopy = new SnapSetting();

		varCopy.target = this.target;
		varCopy.useEnd = this.useEnd;
		varCopy.useVertex = this.useVertex;
		varCopy.useLine = this.useLine;
		varCopy.buffer = this.buffer;

		return varCopy;
	}
}
