package srs.DataSource.DB.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ParseException;
import android.os.Environment;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
* @ClassName: StringUtil
* @Description: TODO(杩欓噷鐢ㄤ竴鍙ヨ瘽鎻忚堪杩欎釜绫荤殑浣滅敤)
* @Version: V1.0.0.0
* @author lisa
* @date 2016骞�12鏈�25鏃� 涓嬪崍2:52:27
***********************************
* @editor lisa 
* @data 2016骞�12鏈�25鏃� 涓嬪崍2:52:27
* @todo TODO
*/
public class StringUtil {

    
    public static String emptyValueFormatter(String value){
        if(isEmpty(value)){
            return "";
        }else{
            return value;
        }
        
    }

	// /**
	// * 閸樺娅嶺ListView缂佸嫪娆㈤惃鍕閸氾拷
	// * @param mListView
	// */
	// public static void removeXListView(XListView mListView) {
	// if (mListView.mHeaderView != null) {
	// mListView.removeHeaderView(mListView.mHeaderView);
	// }
	// if (mListView.mFooterView != null) {
	// mListView.removeFooterView(mListView.mFooterView);
	// }
	// mListView.setPullLoadEnable(false);
	// mListView.setPullRefreshEnable(false);
	// }
	/**
	 * 閺嶈宓侀幍瀣簚閻ㄥ嫬鍨庢潏銊у芳娴狅拷 dp 閻ㄥ嫬宕熸担锟� 鏉烆剚鍨氭稉锟� px(閸嶅繒绀�)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 閺嶈宓侀幍瀣簚閻ㄥ嫬鍨庢潏銊у芳娴狅拷 px(閸嶅繒绀�) 閻ㄥ嫬宕熸担锟� 鏉烆剚鍨氭稉锟� dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 閼惧嘲褰囩仦蹇撶閸掑棜椴搁悳锟�
	 * 
	 * @param context
	 * @return
	 */
	public static int[] getScreenDispaly(Context context) {
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		int width = windowManager.getDefaultDisplay().getWidth();// 閹靛婧�鐏炲繐绠烽惃鍕啍鎼达拷
		int height = windowManager.getDefaultDisplay().getHeight();// 閹靛婧�鐏炲繐绠烽惃鍕彯鎼达拷
		int result[] = { width, height };
		return result;
	}

	public static String createUploadTime() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHss");
		String id = sdf.format(date);
		return id;
	}

	public static String formatDateTimeToString() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
		String dt = sdf.format(date);
		return dt;
	}

	public static String getDate(String dateTime) {
		return dateTime.split(" ")[0];
	}

	public static String getTime(String dateTime) {
		return dateTime.split(" ")[1];
	}

	public static boolean isEmpty(String str) {
		if (null == str || "".equals(str) || str.trim().isEmpty()
				|| "null".equals(str) || "閺嗗倹妫�".equals(str)) {
			return true;
		}
		return false;
	}

	public static boolean isNotEmpty(String str) {
		if (isEmpty(str)) {
			return false;
		}
		return true;
	}

	/**
	 * 閸︼拷500濮ｎ偆顫楅崘鍜冪礉闂冨弶顒涚悮顐ゎ儑娴滃本顐奸悙鐟板毊
	 */
	private static long lastClickTime;

	/**
	 * 0.5缁夋帒鍞撮崣灞藉毊鏉╂柨娲栭柅锟介崙锟�
	 * 
	 * @param part
	 *            閺冨爼妫�
	 * @return
	 */
	public static boolean isFastDoubleClick(int part) {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < part) {
			return true;
		}
		lastClickTime = time;
		return false;
	}

	/**
	 * 閺勵垰鎯侀崚閿嬫煀閺佺増宓侀敍鍫滅閸掑棝鎸撻崘鍛瑝閸掗攱鏌婇敍锟�
	 */
	private static String key = "";
	private static long fitClickTime;

	public static boolean isFitOnRefreshByTime(String keyName) {
		long time = System.currentTimeMillis();
		long timeD = time - fitClickTime;
		if (!(0 < timeD && timeD < 60000)) {
			fitClickTime = time;
			return true;
		}
		if (!key.equals(keyName)) {
			key = keyName;
			return true;
		}
		return false;
	}

	/**
	 * 閺勵垰鎯侀崚閿嬫煀閺佺増宓�,娑撱倖顐肩拋鍧楁６閻ㄥ嫭鐖ｇ拠鍡樻Ц娑擄拷閺嶉娈戦敍灞肩瑝閸掗攱鏌�
	 */
	private static String titleKey = "";

	public static boolean isFitOnRefreshByKey(String keyName) {
		if (!key.equals(keyName)) {
			key = keyName;
			return true;
		}
		return false;
	}

	public static boolean dismissDialog(Dialog dialog) {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
			return true;
		}
		return false;
	}

	@SuppressLint("SimpleDateFormat")
	public static Boolean apartTimeOneMin(String lastTime, String nowTime) {
		SimpleDateFormat format = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Date nowDate = null;
		Date lastDate = null;
		try {
			lastDate = format.parse(lastTime);
			nowDate = format.parse(nowTime);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(nowDate);
		long now = c.getTimeInMillis();

		c.setTime(lastDate);
		long lastly = c.getTimeInMillis();

		return (now - lastly) >= 60000;
	}

	/**
	 * 缂佹┇ap鐠у锟斤拷
	 * 
	 * @param oldMap
	 * @param newMap
	 * @return
	 */
	public static Map setMapToMap(Map oldMap, Map newMap) {
		for (Iterator it = oldMap.keySet().iterator(); it.hasNext();) {
			String key = it.next().toString();
			newMap.put(key, oldMap.get(key));
		}
		return newMap;
	}

	/**
	 * 闂冨弶顒泂crollview娑撳窅istview閸愯尙鐛婇敍宀冾吀缁犳istview婢堆冪毈 item妞ょ懓绻�妞ょ粯妲窵inearlayout
	 * 
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		// 閼惧嘲褰嘗istView鐎电懓绨查惃鍑檇apter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
			// listAdapter.getCount()鏉╂柨娲栭弫鐗堝祦妞ゅ湱娈戦弫鎵窗
			View listItem = listAdapter.getView(i, null, listView);
			// 鐠侊紕鐣荤�涙劙銆峍iew 閻ㄥ嫬顔旀锟�
			listItem.measure(0, 0);
			// 缂佺喕顓搁幍锟介張澶婄摍妞ゅ湱娈戦幀濠氱彯鎼达拷
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		// listView.getDividerHeight()閼惧嘲褰囩�涙劙銆嶉梻鏉戝瀻闂呮梻顑侀崡鐘垫暏閻ㄥ嫰鐝惔锟�
		// params.height閺堬拷閸氬骸绶遍崚鐗堟殻娑撶嫸istView鐎瑰本鏆ｉ弰鍓с仛闂囷拷鐟曚胶娈戞妯哄
		listView.setLayoutParams(params);
	}

	/**
	 * 閸掋倖鏌囩�电钖勯弰顖氭儊娑撶癄tr
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isString(Object obj) {
		boolean flag = false;
		if (obj instanceof String) {
			flag = true;
		}
		return flag;
	}

	public static String doWithFailureData(Context context, JSONObject jo)
			throws JSONException {
		int status = jo.getInt("status");
		String str = "";
		switch (status) {
		case 900:
			str = "缂冩垹绮堕崼闈涱敚閿涘矁顕柌宥堢槸";
			break;
		case 901:
			str = "鐠佸潡妫剁搾鍛閿涘矁顕柌宥堢槸";
			break;
		case 902:
			str = "閺堝秴濮熼崳銊ュ毉闁挎瑱绱濈拠鐑藉櫢鐠囷拷";
			break;
		case 903:
			str = "鐠囬攱鐪伴弫鐗堝祦閸戞椽鏁婇敍宀冾嚞闁插秷鐦�";
			break;
		case 904:
			str = "閻ц缍嶇搾鍛閿涘矂娓堕柌宥嗘煀閻ц缍�";
			// skipToLogin((Activity) context);
			break;
		default:
			break;
		}
		return str;
		// 900閿涙氨缍夌紒婊冪壄婵夛拷
		// 901閿涙俺顔栭梻顔跨Т閺冿拷
		// 902閿涙碍婀囬崝鈥虫珤閸戞椽鏁�
		// 903閿涙俺顕Ч鍌涙殶閹诡喖鍤柨锟�
		// 904閿涙岸鍣搁弬鎵瑜版洩绱欓悽銊﹀煕Session閸婄厧銇戦弫鍫礉闂囷拷闁拷閸ョ偛鍩岄惂璇茬秿閻ｅ矂娼伴敍锟�
		// 905: 閻ц缍嶇�靛棛鐖滈柨娆掝嚖

	}

	/**
	 * 濡拷濞村顕拠婵囶攱閺勵垰鎯佺搾鍛
	 *
	 * @param dialog
	 * @param context
	 */
	public static void testDialog(final Dialog dialog, final Context context) {
		new Handler().postDelayed(new Runnable() {
			public void run() {
				if (StringUtil.dismissDialog(dialog)) {// 濞屸�虫惙鎼达拷
					Toast.makeText(
							context,
							"鏉╃偞甯存径杈Е閿涘矁顕柌宥堢槸!",
							Toast.LENGTH_SHORT).show();
				}
			}
		}, 50000);
	}

	public static String removeNull(String str) {
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		Matcher m = p.matcher(str);
		str = m.replaceAll("");
		if (str.equals("") || str.contains("null")) {
			str = "閺嗗倹妫�";
		}
		return str;
	}

	public static String setNull(String str) {
		if (str.equals("閺嗗倹妫�")) {
			str = "";
		}
		return str;
	}

	/**
	 * 
	 */

	public static boolean getBoolean(String status) {
		if ("0".equals(status)) {
			return false;
		} else if ("1".equals(status)) {
			return true;
		}
		return false;
	}

	/**
	 * 鐏忓棙妲搁崥锕佹祮娑撶皧tr
	 * 
	 * @param
	 * @return
	 */
	public static String isToStr(String is) {

		// 閸氾拷0閵嗕焦妲�1
		if (is != null && is.equals("1")) {
			is = "閺勶拷";
		} else {
			is = "閸氾拷";
		}

		return is;
	}

	/**
	 * 閹靛婧�閸欓鐖滄宀冪槈
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 闁喚顔堟宀冪槈
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isEmail(String email) {
		String str = "^\\w+@\\w+(\\.[a-z]{2,3}){1,2}$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	/**
	 * 閻絻鐦介崣椋庣垳妤犲矁鐦�
	 * 
	 * @param str
	 * @return 妤犲矁鐦夐柅姘崇箖鏉╂柨娲杢rue
	 */
	public static boolean isPhone(String str) {
		Pattern p1 = null, p2 = null;
		Matcher m = null;
		boolean b = false;
		p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$"); // 妤犲矁鐦夌敮锕�灏崣椋庢畱
		p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$"); // 妤犲矁鐦夊▽鈩冩箒閸栧搫褰块惃锟�
		if (str.length() > 9) {
			m = p1.matcher(str);
			b = m.matches();
		} else {
			m = p2.matcher(str);
			b = m.matches();
		}
		return b;
	}

	/**
	 * 缁旑垰褰涢弽鐓庣础濡拷閺岋拷
	 */
	public static boolean isboolIP(String ipAddress) {
		String ip = "(2[5][0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\:\\d{1,4}\\b";
		Pattern pattern = Pattern.compile(ip);
		Matcher matcher = pattern.matcher(ipAddress);
		return matcher.matches();

	}

	/**
	 * 閺嶇厧绱￠崠鏍ㄦ闂傦拷
	 * 
	 * @param time
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String formatDateTime(String time) {
		SimpleDateFormat format = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm");
		if (time == null || "".equals(time)) {
			return "";
		}
		Date date = null;
		try {
			date = format.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Calendar current = Calendar.getInstance();

		Calendar today = Calendar.getInstance(); // 娴犲﹤銇�

		today.set(Calendar.YEAR, current.get(Calendar.YEAR));
		today.set(Calendar.MONTH, current.get(Calendar.MONTH));
		today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
		// Calendar.HOUR閳ユ柡锟斤拷12鐏忓繑妞傞崚鍓佹畱鐏忓繑妞傞弫锟� Calendar.HOUR_OF_DAY閳ユ柡锟斤拷24鐏忓繑妞傞崚鍓佹畱鐏忓繑妞傞弫锟�
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);

		Calendar yesterday = Calendar.getInstance(); // 閺勩劌銇�

		yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
		yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
		yesterday.set(Calendar.DAY_OF_MONTH,
				current.get(Calendar.DAY_OF_MONTH) - 1);
		yesterday.set(Calendar.HOUR_OF_DAY, 0);
		yesterday.set(Calendar.MINUTE, 0);
		yesterday.set(Calendar.SECOND, 0);

		current.setTime(date);
		String[] str = time.split(":");
		time = str[0] + ":" + str[1];
		if (current.after(today)) {
			return "娴犲﹤銇� " + time.split(" ")[1];
		} else if (current.before(today) && current.after(yesterday)) {
			return "閺勩劌銇� " + time.split(" ")[1];
		} else {
			if (date.getYear() == new Date().getYear()) {
				int index = time.indexOf("-") + 1;
				time = time.substring(index, time.length());
			}
			return time;
		}
	}

	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

	/**
	 * 閸樺娅庨悧瑙勭暕鐎涙顑侀幋鏍х殺閹碉拷閺堝鑵戦弬鍥ㄧ垼閸欓攱娴涢幑顫礋閼昏鲸鏋冮弽鍥у娇
	 * 
	 * @param str
	 * @return
	 */
	public static String stringFilter(String str) {
		str = str.replaceAll("閵嗭拷", "[").replaceAll("閵嗭拷", "]")
				.replaceAll("閿涳拷", "!").replaceAll("閿涳拷", ":");// 閺囨寧宕叉稉顓熸瀮閺嶅洤褰�
		String regEx = "[閵嗗簺锟藉滑"; // 濞撳懘娅庨幒澶屽濞堝﹤鐡х粭锟�
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

	/**
	 * 鐠佸墽鐤嗘妯瑰瘨
	 * 
	 * @param str
	 * @param searchContent
	 * @return
	 */
	public static SpannableString setHigh(String str, String searchContent) {
		String reg = "[abcdefghijklmnopqrstuvwxyz]";
		SpannableString s = new SpannableString(str);
		if (searchContent.length() == 1) {
			if (reg.indexOf(searchContent) != -1) {
				String t = searchContent.toUpperCase();
				Pattern pp = Pattern.compile(t);
				Matcher mm = pp.matcher(s);
				while (mm.find()) {
					int start = mm.start();
					int end = mm.end();
					s.setSpan(new ForegroundColorSpan(Color.RED), start, end,
							Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			}
		}
		Pattern p = Pattern.compile(searchContent);
		Matcher m = p.matcher(s);
		while (m.find()) {
			int start = m.start();
			int end = m.end();
			s.setSpan(new ForegroundColorSpan(Color.RED), start, end,
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return s;
	}

	public static boolean isSDCardAvailable() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 鐟欏嫯瀵栭梽鍕婢堆冪毈
	 * 
	 * @param size
	 * @return
	 */
	public static String getSize(String size) {
		String[] sizes = size.split("[.]");
		try {
			if (StringUtil.isNotEmpty(sizes[1])) {
				if (sizes[1].length() > 2) {
					sizes[1] = sizes[1].substring(0, 2);
				}
			}
			size = sizes[0] + "" + sizes[1];
		} catch (Exception e) {
			System.out.println(size + "error");
		}
		return size + "MB";
	}
}
