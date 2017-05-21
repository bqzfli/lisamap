package srs.DataSource.Raster;

import org.gdal.gdal.Band;


public class StretchMethod {
	private StretchMethodType mStretchtype;
	private double mMaxValue;      //最大值
	private double mMinValue;      //最小值
	private double mDivValue;      //二值化分割值
	private int mNBuckets;	        //直方图数组大小
	private Band mInputData;
	private int[] m_pHistogramArray;                //直方图数组
	private int[] m_pHistogramCumulateArray;        //累积直方图数组

	//added by 李忠义
	/*private double _NoValue=0;		//影像中的背景部分的颜色；默认为0*/
	
	public StretchMethod(Band Inputdata, 
			StretchMethodType stretchType) throws Exception{
		mMaxValue = -999999;
		mMinValue = 999999;
		mDivValue = -999999;
		mStretchtype = StretchMethodType.SRS_STRETCH_NULL;
		mNBuckets = 256;
		/*_NoValue=0;*/

		if (Inputdata == null){
			throw new Exception("影像数据为空" + "SRSStretch" + "ImageStretch" + " 144");
		}

		mInputData = Inputdata;
		mStretchtype = stretchType;

		double[] maxmin = new double[2];
		//  mInputData.SetNoDataValue(0);
		mInputData.ComputeRasterMinMax(maxmin, 1);
		mMaxValue = maxmin[1] + 1;  //LGH modify
		mMinValue = maxmin[0];
		mDivValue = (mMaxValue + mMinValue) / 2;
		if (mMaxValue < mMinValue){
			throw new Exception("极值参数有误" + "SRSStretch" + "ImageStretch" + " 170");
		}

		int[] HisArray = new int[mNBuckets];
		mInputData.GetHistogram(mMinValue, mMaxValue, HisArray);
		SetHistogramArray(HisArray, mNBuckets);
	}

	public byte[] DoStretch(int OffsetX, int OffsetY, 
			int width, int height, int stride) throws Exception{
		try{
			byte[] buf = new byte[stride * height];
			//if (mNBuckets < 1)
			//{
			//    throw new Exception("图像直方图数组参数有误" + "SRSStretch" + "ImageStretch" + " 158");
			//}

			double[] pStretchBuf = new double[stride];
			long startpos = 0;
			switch (StretchMethodType.valueOf(String.valueOf(mStretchtype))){
			case SRS_STRETCH_2PERCENTLINEAR:{
				//直方图拉伸  /*0*/
				for (int i = 0; i < height; i++){
					mInputData.ReadRaster(OffsetX, i + OffsetY, width, 1, stride, 1,7, pStretchBuf,0,0);
					Do2PercentLinear(buf, pStretchBuf, startpos, stride);       
					startpos += stride; 
				}
				//添加 by lzy 20120222
				pStretchBuf=null;
				break;
			}case SRS_STRETCH_HISTOGRAMSTRETCH:{
				/*1*/
				for (int i = 0; i < height; i++){
					mInputData.ReadRaster(OffsetX, i + OffsetY, stride, 1, stride, 1,7, pStretchBuf,0,0);
					DoHistogramStrech(buf, pStretchBuf, startpos, stride);
					startpos += stride;
				}
				break;
			}case SRS_STRETCH_HISTOGRAMEQUALIZE:{
				//直方图均衡化  /*2*/
				for (int i = 0; i < height; i++){
					mInputData.ReadRaster(OffsetX, i + OffsetY, width, 1, stride, 1,7, pStretchBuf,0,0);
					DoHistogramEqualize(buf, pStretchBuf, startpos, stride);
					startpos += stride;
				}
				break;
			}case SRS_STRETCH_LOGARITHMSTRETCH:{
				//对数拉伸  /*3*/
				for (int i = 0; i < height; i++){
					mInputData.ReadRaster(OffsetX, i + OffsetY, stride, 1, stride, 1,7, pStretchBuf,0,0);
					DoLogarithmStretch(buf, pStretchBuf, startpos, stride);
					startpos += stride;
				}
				break;
			}case SRS_STRETCH_EXPONENTSTRETCH:{
				//指数拉伸  /*4*/
				for (int i = 0; i < height; i++){
					mInputData.ReadRaster(OffsetX, i + OffsetY, stride, 1, stride, 1,7,pStretchBuf,0,0);
					DoExponentStretch(buf, pStretchBuf, startpos, stride);
					startpos += stride;
				}
				break;
			}case SRS_STRETCH_BINARIZATION:{
				//二值化  /*5*/
				for (int i = 0; i < height; i++){
					mInputData.ReadRaster(OffsetX, i + OffsetY, stride, 1, stride, 1,7,pStretchBuf,0,0);
					DoBinarization(buf, pStretchBuf, startpos, stride);
					startpos += stride;
				}
				break;
			}default:{
				throw new Exception("暂不支持该种拉伸方法" + "SRSStretch" + "ImageStretch" + " 226");
			}
			}
			return buf;
		}catch(Exception e){
			throw e;
		}
	}


	private void SetHistogramArray(int[] pArray, int length){
		m_pHistogramArray = new int[length];
		int i = 0;
		for (i = 0; i < length; i++){
			m_pHistogramArray[i] = pArray[i];
		}

		m_pHistogramCumulateArray = new int[length];
		m_pHistogramCumulateArray[0] = m_pHistogramArray[0];
		mNBuckets = length;

		for (i = 1; i < length; i++){
			m_pHistogramCumulateArray[i] = m_pHistogramCumulateArray[i - 1] + m_pHistogramArray[i];
		}
	}

	private void Do2PercentLinear(byte[] pStretchBuf,
			double[] pDataBuf, 
			long startpos, 
			long size) throws Exception{
		if (mNBuckets == 0){
			throw new Exception("图像直方图数组参数有误" + "SRSStretch" + "Do2PercentLinear" + " 249");
		}
		int i = 0;
		double Maxval = mMaxValue;         
		double Minval = mMinValue;
		double segment1 = (mMaxValue - mMinValue) / mNBuckets;
		int HistogramTotal = m_pHistogramCumulateArray[mNBuckets - 1];
		//         int HistogramTotal = m_pHistogramCumulateArray[mNBuckets - 1]-m_pHistogramCumulateArray[0];

		for (i = 0; i <= mNBuckets - 1; i++){
			if ((m_pHistogramCumulateArray[i] * (double)100.0) >= (double)HistogramTotal){
				Minval = mMinValue + segment1 * (double)i;
				break;
			}
		}
		
		for (i = mNBuckets - 1; i >= 0; i--){
			if ((m_pHistogramCumulateArray[i] * (double)100.0) <= (double)99.0 * (double)HistogramTotal){
				Maxval = mMinValue + segment1 * (double)i;
				break;
			}
		}

		double segment = (Maxval - Minval) / (double)mNBuckets;

		i = (int)startpos;
		long endpos = startpos + size;
		int j = 0;
		while (i < endpos){        	 
			if (pDataBuf[j] <= Minval&&pDataBuf[j]!=0) {
				pStretchBuf[i] = (byte)-127;
				i++;
				j++;
				continue;
			}else if(pDataBuf[j] == 0){
				pStretchBuf[i] = (byte)-128;
				i++;
				j++;
				continue;
			}
			if (pDataBuf[j] >= Maxval){
				pStretchBuf[i] =(byte)127;
				i++;
				j++;
				continue;
			}
			pStretchBuf[i] = (byte)(int)((pDataBuf[j] - Minval) / segment-128); 
			if (pStretchBuf[i] < -128) 
				pStretchBuf[i] = (byte)-128;
			if (pStretchBuf[i] > 127) 
				pStretchBuf[i] = (byte)127;
			i++;
			j++;
		}

	}

	private void DoLogarithmStretch(byte[] pStretchBuf, 
			double[] pDataBuf, 
			long startpos, 
			long size) throws Exception{
		//拉伸前要将Min Max 变换到0~(Max-Min)
		if (mNBuckets == 0){
			throw new Exception("图像直方图数组参数有误" + "SRSStretch" + "Do2PercentLinear" + " 249");
		}
		double segment = (double)Math.log10(mMaxValue - mMinValue + 1.0) / (double)mNBuckets;
		int i = (int)startpos;
		long endpos = startpos + size;

		while (i < endpos){
			pStretchBuf[i] = (byte)(Math.log10(pDataBuf[i] - mMinValue + 1.0) / segment);
			if (pStretchBuf[i] < 0) pStretchBuf[i] = (byte)0;
			else if (pStretchBuf[i] > 255) pStretchBuf[i] = (byte)255;
			i++;
		}
	}

	private void DoExponentStretch(byte[] pStretchBuf,
			double[] pDataBuf,
			long startpos, 
			long size) throws Exception{
		if (mNBuckets == 0){
			throw new Exception("图像直方图数组参数有误" + "SRSStretch" + "Do2PercentLinear" + " 249");
		}
		double scale = (double)1.0 / (mMaxValue - mMinValue + (double)0.000000001);
		double segment = ((double)0.0106182883924);	//	e/256
		long i = startpos;
		long endpos = startpos + size;

		//算法有问题
		while (i < endpos){
			pStretchBuf[(int)i] = (byte)Math.exp(((pDataBuf[(int)i] - mMinValue) * scale) / segment);
			if (pStretchBuf[(int)i] < -128) pStretchBuf[(int)i] = (byte)-128;
			if (pStretchBuf[(int)i] > 127) pStretchBuf[(int)i] = (byte)127;
			i++;
		}
	}

	private void DoHistogramStrech(byte[] pStretchBuf,
			double[] pDataBuf,
			long startpos, 
			long size) throws Exception{
		if (mNBuckets == 0){
			throw new Exception("图像直方图数组参数有误" + "SRSStretch" + "Do2PercentLinear" + " 249");
		}
		double segment = (mMaxValue - mMinValue) / (double)mNBuckets;
		int i = (int)startpos;
		int j = 0;
		long endpos = startpos + size;
		while (i < endpos){
			pStretchBuf[i] = (byte)((pDataBuf[j] - mMinValue) / segment);
			if (pStretchBuf[i] < -128) pStretchBuf[i] = (byte)-128;
			if (pStretchBuf[i] > 127) pStretchBuf[i] = (byte)127;
			i++;
			j++;
		}
	}

	private void DoHistogramEqualize(byte[] pStretchBuf, 
			double[] pDataBuf, 
			long startpos, 
			long size) throws Exception{
		try{
			if (mNBuckets == 0){
				throw new Exception("图像直方图数组参数有误" + "SRSStretch" + "Do2PercentLinear" + " 249");
			}

			int i = (int)startpos;
			long endpos = startpos + size;
			int HistogramTotal = m_pHistogramCumulateArray[mNBuckets - 1];

			double HistogramAverage = (double)HistogramTotal / (double)mNBuckets;	//均衡化至256阶时每阶的平均频度

			DoHistogramStrech(pStretchBuf, pDataBuf, startpos, size);
			while (i < endpos){
				pStretchBuf[i] = (byte)((double)m_pHistogramCumulateArray[(int)pStretchBuf[i]] / HistogramAverage);
				if (pStretchBuf[i] < -128) pStretchBuf[(int)i] = (byte)-128;
				if (pStretchBuf[i] > 127) pStretchBuf[(int)i] = (byte)127;
				i++;
			}
		}catch (Exception e){
			throw e;
		}
	}

	private void DoBinarization(byte[] pStretchBuf,
			double[] pDataBuf, 
			long startpos, 
			long size){
		int i =(int) startpos;
		long endpos = startpos + size;
		while (i < endpos)
		{
			pStretchBuf[i] = (byte)(pDataBuf[i] >= mDivValue ? 127 : -128);
			i++;
		}
	}


}
