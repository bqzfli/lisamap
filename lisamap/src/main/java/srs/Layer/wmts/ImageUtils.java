package srs.Layer.wmts;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.WorkerThread;
import android.util.LruCache;
import srs.Utility.Log;

public class ImageUtils {

	/**当前线程中瓦片下载完成
	 */
	public final static int DOWNLOAD_THREAD_SUCCESS = 1;
	/**当前瓦片下载完成
	 */
	public final static int DOWNLOAD_TILE_SUCCESS = 2;
	/**当前瓦片下载失败
	 */
	public final static int DOWNLOAD_TILE_FAILED = 2;

	/**瓦片下载开启的线程数量
	 *
	 */
	public static int DOWNLOAD_THREAD_COUNT = 10;

	/**缓存切片sd卡位置
	 *
	 */
	public static String CacheDir = Environment.getExternalStorageDirectory().getAbsolutePath()+"/tiles/";
	//获取系统分配给每个应用程序的最大内存，每个应用系统分配32M
	//  ((ActivityManager)context.getSystemService(context.ACTIVITY_SERVICE)).getMemoryClass()*1024*1024/8;
	//	public static int MaxCacheNum ;


	/**缓存切片内存位置
	 * 给LruCache分配((ActivityManager)context.getSystemService(context.ACTIVITY_SERVICE)).getMemoryClass()*1024*1024/8;
	 */
	public static  LruCache<String,Bitmap> Caches = null;

	/**设置切片的内存大小
	 * @param MaxCacheNum 最大内存数据
	 *           ((ActivityManager)context.getSystemService(context.ACTIVITY_SERVICE)).getMemoryClass()*1024*1024/8;
	 */
	public static void GeneratImageCachesMemory(int MaxCacheNum){
		if(Caches!=null||MaxCacheNum<0){
			return;
		}
		Caches = new LruCache<String, Bitmap>(MaxCacheNum){
			/*必须重写此方法，来测量Bitmap的大小
             * @see android.util.LruCache#sizeOf(java.lang.Object, java.lang.Object)
             */
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes() * value.getHeight();
			}
		};
	}

	/**构建缓存文件夹
	 *
	 */
	public static void CreateImageSDdir(){
		File directory=new File(CacheDir);
		if(!directory.exists()){
			directory.mkdirs();
		}
	}

	/**
	 * 保存Image的方法，有sd卡存储到sd卡，没有就存储到手机目录
	 * @param fileName
	 * @param bitmap
	 * @throws IOException
	 */
	@WorkerThread
	public static void SaveBitmap(Bitmap bitmap,String fileName) throws Exception{
		FileOutputStream outStream = new FileOutputStream(CacheDir + File.separator + fileName);
		bitmap.compress(CompressFormat.JPEG, 75, outStream);
		outStream.flush();
		outStream.close();
	}

	/**判断 bitmap 是否已经下载过
	 * @param fileName 照片名
	 * @return
	 */
	public static boolean isBitmapSDCardExist(String fileName){
		String filePath = CacheDir + File.separator + fileName;
		File file = new File(filePath);
		return file.exists();
	}

	/**
	 * 从手机或者sd卡获取Bitmap
	 * @param fileName
	 * @return
	 */
	public static Bitmap getBitmap(String fileName){
		Bitmap image = Caches.get(fileName);
		if(image == null||image.isRecycled()){
			//为空或已经被回收
			Log.i("LEVEL-ROW-COLUMN", "ImageUtils.getBitmap:" + "缓存中不存在，从硬盘读取"
					+ "，" + fileName);
			String filePath = CacheDir + File.separator + fileName;
			File file = new File(filePath);
			if(file.exists()){
				image = BitmapFactory.decodeFile(filePath);
				return image;
			}
		}else{
			Log.i("LEVEL-ROW-COLUMN", "ImageUtils.getBitmap:" + "缓存中存在");
		}
		return image;
	}

	/**
	 * 判断文件是否存在
	 * @param fileName
	 * @return
	 */
	public boolean isFileExists(String fileName){
		return new File(CacheDir + File.separator + fileName).exists();
	}

	/**
	 * 获取文件的大小
	 * @param fileName
	 * @return
	 */
	public long getFileSize(String fileName) {
		return new File(CacheDir + File.separator + fileName).length();
	}


	/**
	 * 删除SD卡或者手机的缓存图片和目录
	 */
	public void deleteFile() {
		File dirFile = new File(CacheDir);
		if(! dirFile.exists()){
			return;
		}
		if (dirFile.isDirectory()) {
			String[] children = dirFile.list();
			for (int i = 0; i < children.length; i++) {
				new File(dirFile, children[i]).delete();
			}
		}
		dirFile.delete();
	}
}
