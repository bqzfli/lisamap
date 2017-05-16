package srs.tools;

import java.util.List;

import srs.Display.IScreenDisplay;
import srs.Geometry.IEnvelope;


/** 选择
 * @author 李忠义
 *
 */
public class Select {

	public IEnvelope mEnvelope;
	public IScreenDisplay mScreenDisplay;
	public boolean mIsPoint;
	
	public boolean IsPoint(){
		return this.mIsPoint;
	}
	
	public IEnvelope getBox(){
		return this.mEnvelope;
	}
	
	public void setScreenDisplay(IScreenDisplay value){
		this.mScreenDisplay=value;
	}
	
	
	/**更新选中项列表
	 * @param oldIDs 原选中项列表
	 * @param selectedIDs 当前选中项列表
	 * @param isMore 是否为继续选择，若为真则继续选择，否则清除已选项
	 * @return 更新后的选中项列表
	 */
	public static List<Integer> SwapSelected(List<Integer> oldIDs,List<Integer> selectedIDs,boolean isMore){
		if(oldIDs==null||oldIDs.size()==0){
			return selectedIDs;
		}
		if(!isMore){
			oldIDs.clear();
		}
		//若重复选择同一元素，则视为将其清除选中状态
		for(int i=0;i<selectedIDs.size();i++){
			if(oldIDs.contains(selectedIDs.get(i))){
				oldIDs.remove(selectedIDs.get(i));
			}
			else{
				oldIDs.add(selectedIDs.get(i));
			}
		}
		return oldIDs;
	}
}
