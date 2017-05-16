package srs.DataSource.DB.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.os.Environment;

/**
* @ClassName: FileUtil
* @Description: TODO(杩欓噷鐢ㄤ竴鍙ヨ瘽鎻忚堪杩欎釜绫荤殑浣滅敤)
* @Version: V1.0.0.0
* @author lisa
* @date 2016骞�12鏈�25鏃� 涓嬪崍2:52:17
***********************************
* @editor lisa 
* @data 2016骞�12鏈�25鏃� 涓嬪崍2:52:17
* @todo TODO
*/
public class FileUtil {
	public static boolean deleteFile(String path) {
		System.out.println("delete:" + path);
		boolean flag = true;
		try {
			File file = new File(path);
			if (file != null) {
				if (file.exists()) {
					flag = file.delete();
				}
			}
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	public static String getSavePath() {
		String path = Environment.getExternalStorageDirectory() + "/surveyPlus";
		// String path = "/storage/sdcard1/test";
		return path;
	}

	public static String getTime(File f) {
		long time = f.lastModified();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		String s = cal.getTime().toLocaleString();
		return s;
	}

	public static File createFile(String path) {
		File mPhotoFile = new File(path);
		if (!mPhotoFile.exists()) {
			try {
				if (path.endsWith("/")) {// 閺傚洣娆㈡径锟�
					mPhotoFile.mkdirs(); // 婵″倹鐏夐悥鍓佹窗瑜版洑绗夌�涙ê婀敍灞芥皑閺傛澘缂撴稉锟芥稉锟�
				} else {
					if (!mPhotoFile.getParentFile().exists()) {
						mPhotoFile.getParentFile().mkdirs(); // 婵″倹鐏夐悥鍓佹窗瑜版洑绗夌�涙ê婀敍灞芥皑閺傛澘缂撴稉锟芥稉锟�
					}
					mPhotoFile.createNewFile();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return mPhotoFile;
	}

	/**
	 * 鏉╄棄濮為弬鍥︽閿涙矮濞囬悽鈥礽leWriter
	 * 
	 * @param fileName
	 * @param content
	 */
	public static void writeFile(String fileName, String content) {
		FileWriter writer = null;
		try {
			// 閹垫挸绱戞稉锟芥稉顏勫晸閺傚洣娆㈤崳顭掔礉閺嬪嫰锟界姴鍤遍弫棰佽厬閻ㄥ嫮顑囨禍灞奸嚋閸欏倹鏆焧rue鐞涖劎銇氭禒銉ㄦ嫹閸旂姴鑸板蹇撳晸閺傚洣娆�
			writer = new FileWriter(fileName, true);
			writer.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.close();
					System.gc();
					writer = null;
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 缂佹瑦鏋冩禒璺烘嚒閸氾拷
	 * 
	 * @param after
	 *            閸氬海绱戦崥锟�
	 * @return
	 */
	public static String getPhotoFileName(String after) {

		Date date = new Date(System.currentTimeMillis());
		String name = "'IMG'_yyyyMMdd_HHmmss";
		if (after.contains("mp3")) {
			name = name.replace("IMG", "VOICE");
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(name);
		return dateFormat.format(date) + after;// ".jpg"
	}

	/**
	 * 闁插秴鎳￠崥宥嗘瀮娴犺泛銇�
	 * 
	 * @param path
	 *            鐠侯垰绶�
	 * @param newName
	 *            閺傛澘鎮曠�涳拷
	 * @return
	 */
	public static Boolean renameFile(String path, String newPath) {
		File f = new File(path);
		if (f.exists()) {
			File newFile = new File(newPath);
			return f.renameTo(newFile);
		}
		return false;

	}

	/**
	 * 閺傚洣娆㈤弰顖氭儊鐎涙ê婀�
	 * 
	 * @param path
	 * @return
	 */
	public static boolean isExistFile(String path) {
		File mPhotoFile = new File(path);
		return mPhotoFile.exists();
	}

	/**
	 * 婢跺秴鍩楅崡鏇氶嚋閺傚洣娆�
	 * 
	 * @param oldPath
	 *            String 閸樼喐鏋冩禒鎯扮熅瀵帮拷 婵″偊绱癱:/fqf.txt
	 * @param newPath
	 *            String 婢跺秴鍩楅崥搴ょ熅瀵帮拷 婵″偊绱癴:/fqf.txt
	 * @return boolean
	 */
	public static void copyFile(String oldPath, String newPath) {
		try {
			int byteread = 0;
			File oldfile = new File(oldPath);
			File newfile = new File(newPath);
			System.out.println("-----婢跺秴鍩楅弬鍥︽");
			if (!oldfile.exists()) {
				createFile(oldPath);
				copyFile(oldPath, newPath);
			} else if (!newfile.exists()) {
				createFile(newPath);
				copyFile(oldPath, newPath);
			} else {
				InputStream inStream = new FileInputStream(oldPath); // 鐠囪鍙嗛崢鐔告瀮娴狅拷
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			System.out.println("婢跺秴鍩楅崡鏇氶嚋閺傚洣娆㈤幙宥勭稊閸戞椽鏁�");
			e.printStackTrace();
		}

	}

	public static void saveFile(String fileName, String content) {
		File file = new File(fileName);
		if (!file.exists()) {
			try {
				if (fileName.endsWith("/")) {// 閺傚洣娆㈡径锟�
					file.mkdirs(); // 婵″倹鐏夐悥鍓佹窗瑜版洑绗夌�涙ê婀敍灞芥皑閺傛澘缂撴稉锟芥稉锟�
				} else {
					if (!file.getParentFile().exists()) {
						file.getParentFile().mkdirs(); // 婵″倹鐏夐悥鍓佹窗瑜版洑绗夌�涙ê婀敍灞芥皑閺傛澘缂撴稉锟芥稉锟�
					}
					file.createNewFile();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileWriter writer = null;
		try {
			// 閹垫挸绱戞稉锟芥稉顏勫晸閺傚洣娆㈤崳顭掔礉閺嬪嫰锟界姴鍤遍弫棰佽厬閻ㄥ嫮顑囨禍灞奸嚋閸欏倹鏆焧rue鐞涖劎銇氭禒銉ㄦ嫹閸旂姴鑸板蹇撳晸閺傚洣娆�
			writer = new FileWriter(fileName, true);
			writer.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.close();
					System.gc();
					writer = null;
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
