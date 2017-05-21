package srs.Layer;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import srs.Geometry.Envelope;
import srs.Layer.wmts.LOD;

public class ZY3Layer extends TileLayer {

	public String TileMatrixSet;

	public ZY3Layer(){
		mEnvelope = new Envelope(-180.0 , -90.0, 180.0, 90.0);
	}

	public ZY3Layer(String source){
		mSource = source;
		mEnvelope = new Envelope(-180.0 , -90.0, 180.0, 90.0);
		//BuildTileInfo();
	}

	//Override
	protected String getTileUrl(int row, int col,LOD lod){
		String url="";
		/*  if(lod.Level<11)
            url = this.Source() + "&X=" + col + "&" + "Y=" + row + "&" + "L=" + lod.Level; 
        else*/
		url = lod.Url + "/wmts" 
				+"?SERVICE=WMTS" 
				+ "&REQUEST=GetTile"
				+ "&VERSION=1.0.0"
				+ "&LAYER=" + mName 
				+ "&STYLE=default"
				+ "&TILEMATRIXSET=matrix_id"
				+ "&TILEMATRIX=" + lod.Level
				+ "&TILEROW=" + row
				+ "&TILECOL=" + col
				+ "&FORMAT=image%2Fjpeg";
		return url;
	}
	
	
	

	/**设置TileInfo
	 * @param String Url
	 */	
	public void setTileInfo(String Url){
		/*mTileInfo.Height = 256;
		mTileInfo.Width = 256;
		mTileInfo.Origin = new Point(-180.0, 90.0);*/
		/*mTileInfo.Origin = new Point(-2.0037508342787E7, 2.0037508342787E7);
		LOD[] lodArray = new LOD[18];
		LOD lod1 = new LOD();
		lod1.Level = 1;
		lod1.Resolution = 78271.51696399994;
		lod1.ScaleDenominator = 2.95828763795777E8;
		lod1.Url = Url;
		lodArray[0] = lod1;
		LOD lod2 = new LOD();
		lod2.Level = 2;
		lod2.Resolution = 39135.75848200009;
		lod2.ScaleDenominator =  1.47914381897889E8;
		lod2.Url = Url;
		lodArray[1] = lod2;
		LOD lod3 = new LOD();
		lod3.Level = 3;
		lod3.Resolution = 19567.87924099992;
		lod3.ScaleDenominator = 7.3957190948944E7;
		lod3.Url = Url;
		lodArray[2] = lod3;
		LOD lod4 = new LOD();
		lod4.Level = 4;
		lod4.Resolution = 9783.93962049996;
		lod4.ScaleDenominator = 3.6978595474472E7;
		lod4.Url = Url;
		lodArray[3] = lod4;
		LOD lod5 = new LOD();
		lod5.Level = 5;
		lod5.Resolution = 4891.96981024998;
		lod5.ScaleDenominator = 1.8489297737236E7;
		lod5.Url = Url;
		lodArray[4] = lod5;
		LOD lod6 = new LOD();
		lod6.Level = 6;
		lod6.Resolution = 2445.98490512499;
		lod6.ScaleDenominator = 9244667.357955175;
		lod6.Url = Url;
		lodArray[5] = lod6;
		LOD lod7 = new LOD();
		lod7.Level = 7;
		lod7.Resolution = 1222.992452562495;
		lod7.ScaleDenominator = 4622324.434309;
		lod7.Url = Url;
		lodArray[6] = lod7;
		LOD lod8 = new LOD();
		lod8.Level = 8;
		lod8.Resolution = 611.4962262813797;
		lod8.ScaleDenominator = 2311162.217155;
		lod8.Url = Url;
		lodArray[7] = lod8;
		LOD lod9 = new LOD();
		lod9.Level = 9;
		lod9.Resolution = 305.74811314055756;
		lod9.ScaleDenominator = 1155581.108577;
		lod9.Url = Url;
		lodArray[8] = lod9;
		LOD lod10 = new LOD();
		lod10.Level = 10;
		lod10.Resolution = 152.87405657041106;
		lod10.ScaleDenominator = 577790.554289;
		lod10.Url = Url;
		lodArray[9] = lod10;
		LOD lod11 = new LOD();
		lod11.Level = 11;
		lod11.Resolution =  76.43702828507324;
		lod11.ScaleDenominator = 288895.277144;
		lod11.Url = Url;
		lodArray[10] = lod11;
		LOD lod12 = new LOD();
		lod12.Level = 12;
		lod12.Resolution = 38.21851414253662;
		lod12.ScaleDenominator = 144447.638572;
		lod12.Url = Url;
		lodArray[11] = lod12;
		LOD lod13 = new LOD();
		lod13.Level = 13;
		lod13.Resolution = 19.10925707126831;
		lod13.ScaleDenominator = 72223.819286;
		lod13.Url = Url;
		lodArray[12] = lod13;
		LOD lod14 = new LOD();
		lod14.Level = 14;
		lod14.Resolution = 9.554628535634155;
		lod14.ScaleDenominator =  36111.909643;
		lod14.Url = Url;
		lodArray[13] = lod14;
		LOD lod15 = new LOD();
		lod15.Level = 15;
		lod15.Resolution = 4.77731426794937;
		lod15.ScaleDenominator = 18055.954822;
		lod15.Url = Url;
		lodArray[14] = lod15;
		LOD lod16 = new LOD();
		lod16.Level = 16;
		lod16.Resolution = 2.388657133974685;
		lod16.ScaleDenominator = 9027.977411;
		lod16.Url = Url;
		lodArray[15] = lod16;
		LOD lod17 = new LOD();
		lod17.Level = 17;
		lod17.Resolution = 1.1943285668550503;
		lod17.ScaleDenominator = 4513.988705;
		lod17.Url = Url;
		lodArray[16] = lod17;
		LOD lod18 = new LOD();
		lod18.Level = 18;
		lod18.Resolution = 0.5971642835598172;
		lod18.ScaleDenominator = 2256.994353;
		lod18.Url = Url;
		lodArray[17] = lod18;*/
		/*mTileInfo.Origin = new Point(-180.0, 90.0);*/
		LOD[] lodArray = new LOD[18];
		LOD lod1 = new LOD();
		lod1.Level = 0;
		lod1.Resolution = 0.703125;
		lod1.ScaleDenominator = 2.958293554545656E8;
		lod1.Url = Url;
		lodArray[0] = lod1;
		LOD lod2 = new LOD();
		lod2.Level = 1;
		lod2.Resolution = 0.351563;
		lod2.ScaleDenominator = 1.479146777272828E8;
		lod2.Url = Url;
		lodArray[1] = lod2;
		LOD lod3 = new LOD();
		lod3.Level = 2;
		lod3.Resolution = 0.175781;
		lod3.ScaleDenominator = 7.39573388636414E7;
		lod3.Url = Url;
		lodArray[2] = lod3;
		LOD lod4 = new LOD();
		lod4.Level = 3;
		lod4.Resolution = 0.0878906;
		lod4.ScaleDenominator = 3.69786694318207E7;
		lod4.Url = Url;
		lodArray[3] = lod4;
		LOD lod5 = new LOD();
		lod5.Level = 4;
		lod5.Resolution = 0.0439453;
		lod5.ScaleDenominator = 1.848933471591035E7;
		lod5.Url = Url;
		lodArray[4] = lod5;
		LOD lod6 = new LOD();
		lod6.Level = 5;
		lod6.Resolution = 0.0219727;
		lod6.ScaleDenominator = 9244667.357955175;
		lod6.Url = Url;
		lodArray[5] = lod6;
		LOD lod7 = new LOD();
		lod7.Level = 6;
		lod7.Resolution = 0.0109863;
		lod7.ScaleDenominator = 4622333.678977588;
		lod7.Url = Url;
		lodArray[6] = lod7;
		LOD lod8 = new LOD();
		lod8.Level = 7;
		lod8.Resolution = 0.00549316;
		lod8.ScaleDenominator = 2311166.839488794;
		lod8.Url = Url;
		lodArray[7] = lod8;
		LOD lod9 = new LOD();
		lod9.Level = 8;
		lod9.Resolution = 0.00274658;
		lod9.ScaleDenominator = 1155583.419744397;
		lod9.Url = Url;
		lodArray[8] = lod9;
		LOD lod10 = new LOD();
		lod10.Level = 9;
		lod10.Resolution = 0.00137329;
		lod10.ScaleDenominator = 577791.7098721985;
		lod10.Url = Url;
		lodArray[9] = lod10;
		LOD lod11 = new LOD();
		lod11.Level = 10;
		lod11.Resolution = 0.000686646;
		lod11.ScaleDenominator = 288895.85493609926;
		lod11.Url = Url;
		lodArray[10] = lod11;
		LOD lod12 = new LOD();
		lod12.Level = 11;
		lod12.Resolution = 0.000343323;
		lod12.ScaleDenominator = 144447.92746804963;
		lod12.Url = Url;
		lodArray[11] = lod12;
		LOD lod13 = new LOD();
		lod13.Level = 12;
		lod13.Resolution = 0.000171661;
		lod13.ScaleDenominator = 72223.96373402482;
		lod13.Url = Url;
		lodArray[12] = lod13;
		LOD lod14 = new LOD();
		lod14.Level = 13;
		lod14.Resolution = 8.58307e-005;
		lod14.ScaleDenominator = 36111.98186701241;
		lod14.Url = Url;
		lodArray[13] = lod14;
		LOD lod15 = new LOD();
		lod15.Level = 14;
		lod15.Resolution = 4.29153e-005;
		lod15.ScaleDenominator = 18055.990933506204;
		lod15.Url = Url;
		lodArray[14] = lod15;
		LOD lod16 = new LOD();
		lod16.Level = 15;
		lod16.Resolution = 2.14577e-005;
		lod16.ScaleDenominator = 9027.995466753102;
		lod16.Url = Url;
		lodArray[15] = lod16;
		LOD lod17 = new LOD();
		lod17.Level = 16;
		lod17.Resolution = 1.07289e-005;
		lod17.ScaleDenominator = 4513.997733376551;
		lod17.Url = Url;
		lodArray[16] = lod17;
		LOD lod18 = new LOD();
		lod18.Level = 16;
		lod18.Resolution = 5.36445e-006;
		lod18.ScaleDenominator = 2256.998866688275;
		lod18.Url = Url;
		lodArray[17] = lod18;
		mTileInfo.LODs = lodArray;
	}


	/**
	 * @param row
	 * @param col
	 * @param lod
	 * @return
	 */
	protected Bitmap getTiledBitmap(int row, int col, LOD lod){
		String url = "";
		if (mName.isEmpty()||TileMatrixSet.isEmpty()){
			url = lod.Url + "&X=" + col + "&" + "Y=" + row + "&" + "L=" + lod.Level;
		}else{
			url = lod.Url + "?SERVICE=WMTS&VERSION=1.0.0&REQUEST=GetTile" + "&STYLE=default" + "&FORMAT=tiles" + "&ServiceMode=KVP"
					+ "&LAYER=" + mName
					+ "&TILEMATRIXSET=" + TileMatrixSet
					+ "&TILEMATRIX=" + lod.Level
					+ "&TILEROW=" + row
					+ "&TILECOL=" + col;
		}

		URI myUri;
		try {
			myUri = new URI(url);
			System.out.println("class have beee set");
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(myUri);

			System.out.println("set http!");
			HttpResponse httpResponse = httpClient.execute(httpget);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				System.out.println("get information 200");
				Bitmap image = BitmapFactory.decodeStream(httpResponse.getEntity().getContent());
				return image;
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return null;
	}	
}
