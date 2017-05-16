package srs.tools;

import android.view.View.OnTouchListener;

public interface ITool extends ICommand,OnTouchListener{
	/**清空绘图控件中的缓存
	 * 
	 */
	void DrawAgain();
	/**保存当前缓存的图形
	 * 
	 */
	void SaveResault();
	
}
