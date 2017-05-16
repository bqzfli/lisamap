package srs.Display.Symbol;

import android.annotation.SuppressLint;

public enum SimpleFillStyle { 
	Soild(999), //实心图案
	Hollow(1000), //空心图案
	Cross(1/*HatchStyle.Cross*/), //交叉的水平线和垂直线
	Vertical(2/*HatchStyle.Vertical*/), //垂直线的图案
	Horizontal(3/*HatchStyle.Horizontal*/), //水平线的图案
	ForwardDiagonal(4/*HatchStyle.ForwardDiagonal*/), //从左上到右下的对角线的线条图案
	BackwardDiagonal(5/*HatchStyle.BackwardDiagonal*/), //从右上到左下的对角线的线条图案
	DiagonalCross(6/*HatchStyle.DiagonalCross*/); //交叉对角线的图案

	private int intValue;
	private static java.util.HashMap<Integer, SimpleFillStyle> mappings;
	@SuppressLint("UseSparseArrays")
	private synchronized static java.util.HashMap<Integer, SimpleFillStyle> getMappings(){
		if (mappings == null){
			mappings = new java.util.HashMap<Integer, SimpleFillStyle>();
		}
		return mappings;
	}

	private SimpleFillStyle(int value){
		intValue = value;
		SimpleFillStyle.getMappings().put(value, this);
	}

	public int getValue(){
		return intValue;
	}

	public static SimpleFillStyle forValue(int value){
		return getMappings().get(value);
	}

}
