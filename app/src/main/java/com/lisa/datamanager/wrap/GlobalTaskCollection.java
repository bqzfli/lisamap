package com.lisa.datamanager.wrap;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.dom4j.DocumentException;
import com.lisa.datamanager.wrap.event.SelectedIndexChangedManager;


/**  
 * All rights Reserved, Designed By lisa ^_^
 * @Title:  GlobalTaskCollection.java   
 * @Package com.lisa.datamanager.wrap   
 * @Description:    TODO 璇ョ被璐熻矗宸ョ▼鏂囦欢鐨勭鐞� 涓昏绠＄悊宸ヤ綔绌洪棿鍐呯殑宸ョ▼鏂囦欢
 * @author: UDHAWK723     
 * @date:   2017骞�5鏈�16鏃� 涓婂崍11:32:31   
 * @version V1.0 
 * @Copyright: 2017 ^_^. All rights reserved. 
 *
 */
public class GlobalTaskCollection {

	private static List<WholeTask> pWholeTaskList = new ArrayList<WholeTask>();
	public static int pSelectedWholeIndex = -1;
	/**褰撳墠鏌ョ湅鐨勮皟鏌ヤ换鍔�
	 * 
	 */
	private static int mCurrentCheckWholeIndex = -1;
	public static SelectedIndexChangedManager SelectedIndexChanged=new SelectedIndexChangedManager();

	public static List<WholeTask> getWholeTaskList(){
		return pWholeTaskList; 
	}

	/**璁剧疆姝ｅ湪鏌ョ湅鐨勮皟鏌ヤ换鍔�
	 * @param index 璋冩煡浠诲姟搴忓垪鍙�
	 * @return
	 */
	public static WholeTask setCurrentWholeTaskByIndex(int index){
		if (index >= 0 && index < pWholeTaskList.size()){
			mCurrentCheckWholeIndex = index;
			return pWholeTaskList.get(mCurrentCheckWholeIndex);
		}
		return null;
	}

	/**鑾峰彇姝ｅ湪鏌ョ湅鐨勮皟鏌ヤ换鍔�
	 * @return
	 */
	public static WholeTask getCurrentCheckWholeTask(){
		if (mCurrentCheckWholeIndex >= 0 && mCurrentCheckWholeIndex < pWholeTaskList.size()){
			return pWholeTaskList.get(mCurrentCheckWholeIndex);
		}
		return null;		
	}

	/**鑾峰彇鎸囧畾搴忓彿鐨勪换鍔�
	 * @param index 鎸囧畾浠诲姟鍦ㄦ墍鏈変换鍔′腑寰峰垎缁�
	 * @return
	 */
	public static WholeTask getWholeTaskByIndex(int index){
		if (index >= 0 && index < pWholeTaskList.size()){
			return pWholeTaskList.get(index);
		}
		return null;
	}

	public static int getSelectedWholeIndex(){
		return pSelectedWholeIndex; 
	}

	/**璁剧疆閫変腑椤圭洰涓哄綋鍓嶆鍦ㄦ煡鐪嬬殑椤圭洰
	 * @throws Exception
	 */
	public static void SetCheck2Selected()throws Exception{
		if (pSelectedWholeIndex != mCurrentCheckWholeIndex){
			//			DisposeTaskLayer(pSelectedWholeIndex);
			pSelectedWholeIndex = mCurrentCheckWholeIndex;

			if (SelectedIndexChanged != null){
				SelectedIndexChanged.fireListener();
			}
		}
	}

	public static void setSelectedWholeIndex(int value) throws Exception{
		//		if (pSelectedWholeIndex != value){
		//			DisposeTaskLayer(pSelectedWholeIndex);
		pSelectedWholeIndex = value;

		if (SelectedIndexChanged != null){
			SelectedIndexChanged.fireListener();
		}
		//		}
	}

	public static WholeTask getSelectedWholeTask(){
		if (pSelectedWholeIndex >= 0 && pSelectedWholeIndex < pWholeTaskList.size()){
			return pWholeTaskList.get(pSelectedWholeIndex);
		}
		return null;
	}

	public static void DisposeTaskLayer(int i){
		if (i < 0 || i >= pWholeTaskList.size())
			return;
		pWholeTaskList.get(i).DisposeLayer();
	}

	/**娣诲姞浠诲姟閰嶇疆淇℃伅
	 * @param filePath
	 */
	public static void AddWholeConfig(String filePath){
		File file=new File(filePath);

		if (file.exists() && filePath.substring(filePath.indexOf(".")).equalsIgnoreCase(".tcf")){
			WholeTask task = new WholeTask();
			try {
				task.LoadFromFile(filePath);
				pWholeTaskList.add(task);
				if (pSelectedWholeIndex == -1)
					pSelectedWholeIndex = 0;

			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}
	}


	/**娣诲姞浠诲姟閰嶇疆淇℃伅
	 * @param filePath
	 */
	public static void AddWholeTask(String filePath){
		File file=new File(filePath);

		if (file.exists() && filePath.substring(filePath.indexOf(".")).equalsIgnoreCase(".TCF")){
			WholeTask task = new WholeTask();
			try {
				task.LoadFromFile(filePath);
				pWholeTaskList.add(task);
				if (pSelectedWholeIndex == -1)
					pSelectedWholeIndex = 0;

			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}
	}

	public static void ClearWholeTask(){
		pWholeTaskList.clear();   
	}

}

