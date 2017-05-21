package srs.tools;

import android.view.MotionEvent;
import android.view.View;

public abstract class BaseTool extends BaseCommand implements ITool {

	protected float mRate=1;//因版本不同而产生的不同比例3.x为1,4.x为1/2;
	
	/**设置屏幕点击位置比率
	 * 
	 */
	protected void setRate(){
		@SuppressWarnings("deprecation")
		int sdkId=Integer.valueOf(android.os.Build.VERSION.SDK);
		if(sdkId>=15){
			//android4.x
//			mRate=(float) (1/2.0);
			mRate=1;
		}else if(sdkId>=12){
			//android3.x
			mRate=1;
		}
	} 
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(!v.equals(super.mBuddyControl)){
			return false;
		}
		else return true;
	}

	
	/* 清空绘图控件中的缓存
	 * @see tools.ITool#DrawAgain()
	 */
	public void DrawAgain() {
		// TODO Auto-generated method stub
		
	}


	/* 保存当前缓存的图形
	 * @see tools.ITool#SaveResault()
	 */
	public void SaveResault() {
		// TODO Auto-generated method stub
		
	}

}
