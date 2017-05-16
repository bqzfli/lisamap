package srs.tools;

import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;

public interface ICommand extends OnClickListener{
	
	/**工具名称
	 * @return
	 */
	String getText();
	
	/**工具对应的图片
	 * @return
	 */
	Bitmap getBitmap();
	
	
	/**获取工具的可用性
	 * @return 工具是否可用
	 */
	Boolean getEnable();
	/**设置工具是否可用
	 * @param isEnable 是否可用
	 */
	void setEnable(Boolean isEnable);
	
	BaseControl getBuddyControl();
	
	void setBuddyControl(BaseControl basecontrol);
	
	void onClick(View v);
	
	
}
