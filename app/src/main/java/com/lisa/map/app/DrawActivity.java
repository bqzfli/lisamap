package com.lisa.map.app;
/*package com.thcore201;

import srs.Geometry.IPart;
import srs.Geometry.IPoint;
import srs.Geometry.Part;
import srs.Geometry.Point;

import com.sun.org.apache.bcel.internal.generic.NEW;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.LinearLayout;

public class DrawActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_draw);
		IPoint[] iPoints = new Point[5];
		iPoints[0] = new Point(1,1);
		iPoints[1] = new Point(1,3);
		iPoints[2] = new Point(3,3);
		iPoints[3] = new Point(3,1);
		iPoints[4] = new Point(1,1);
		IPart iPart = new Part(iPoints);
		double x = iPart.CenterPoint().X();
		double y = iPart.CenterPoint().Y();
		System.out.println("========"+x);
		System.out.println("========"+y);
		init();  
    }  
  
    private void init() {  
        LinearLayout layout=(LinearLayout) findViewById(R.id.root);  
        final DrawView view=new DrawView(this);  
        //通知view组件重绘    
        view.invalidate();  
        layout.addView(view);  
          
    }  

}
*/