/**
 * 
 */
package srs.Utility;

import java.lang.Exception;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 李忠义
 *
 */
@SuppressWarnings("serial")
public class sRSException extends Exception{

	private static Map<String,String> ExceptionsHashTable =addHashValue();

	public sRSException(){ 
		super();
	}

	public sRSException(String excetionNo){
		super(GetExceptionByNO(excetionNo));
	}


	/**根据异常编号抛出异常
	 * @param excetionNo 异常编号
	 * @return
	 */
	private static String GetExceptionByNO(String excetionNo){
		if (ExceptionsHashTable.containsKey(excetionNo)){        	
			String result=ExceptionsHashTable.get(excetionNo).toString();
			return result;
		}else{
			return "异常类型不明确，未能捕获，异常编号是：" + excetionNo;
		}
	}

	/** 生成系统中的异常信息
	 * @return
	 */
	private static Map<String,String> addHashValue(){
		Map<String, String> hashTable = new HashMap<String, String>();
		//数据库错误
		hashTable.put("00100001", "数据库连接超时，请检查网络");
		hashTable.put("00100002", "数据库连接失败，请检查相关的数据库连接参数是否设置正确。");
		hashTable.put("00100003", "监听未启动。");
		hashTable.put("00100004", "非法SQL语句。");
		hashTable.put("00100005", "非法文件名。");
		hashTable.put("00100006", "非法文件名。");
		hashTable.put("00100007", "请查看数据库数据文件是否已满或文件已。");
		hashTable.put("00100008", "数据类型不对或字符过长及非法字符。");
		hashTable.put("00100009", "监听程序当前无法识别连接描述符中的请求服务。");
		//RS
		hashTable.put("00200001", "指针没有被赋值或所指对象为空。");
		hashTable.put("00200002", "内存分配失败。");
		hashTable.put("00200003", "对象类型不一致，不能被强制转换。");
		hashTable.put("00200004", "参数值超出允许范围。");
		hashTable.put("00200005", "集合为空，不能获取数据。");
		hashTable.put("00200100", "自动化错误，未知的错误类型。");
		hashTable.put("00200101", "指定路径的影像文件不存在。");
		hashTable.put("00200102", "指定服务器的SDE影像集不存在。");
		hashTable.put("00200103", "无法连接指定的SDE服务器，请重新确认连接参数。");
		hashTable.put("00200104", "没有连接SDE数据源。");
		hashTable.put("00200105", "指定的影像类型不支持。");
		hashTable.put("00200106", "影像金字塔创建失败。");
		hashTable.put("00200107", "新影像创建失败。");
		hashTable.put("00200108", "影像文件是只读属性，不能被编辑。");
		hashTable.put("00200109", "当前以只读方式打开影像，不可写。");
		hashTable.put("00200110", "影像数据源没有打开，无法获取属性。");
		hashTable.put("00200201", "影像图层数据源没有设置，无法获取属性。");
		hashTable.put("00200202", "影像图层数据源没有设置，不能显示。");
		hashTable.put("00200203", "绘图设备无效，无法绘制影像。");
		hashTable.put("00200204", "视图尺寸必须大于0。");
		hashTable.put("00200301", "传入的影像数据源不符合算法处理要求。");
		hashTable.put("00200302", "用于输出的影像不可写。");
		hashTable.put("00200303", "影像处理中传入的参数不符合要求。");
		hashTable.put("00200304", "影像像元的数值类型不符合要求。");
		hashTable.put("00200305", "数组长度不合要求。");
		hashTable.put("00200306", "文件不存在。");
		hashTable.put("00200307", "设置投影属性失败。");
		hashTable.put("00200308", "获得仿射属性失败。");
		hashTable.put("00200309", "设置仿射属性失败。");
		hashTable.put("00200310", "参数获取失败。");
		hashTable.put("00200311", "分母为零。");
		//GIS
		hashTable.put("00300001", "引用对象为空。");
		hashTable.put("00300002", "引用对象类型不一致。");
		hashTable.put("00300003", "参数值超出允许范围。");
		hashTable.put("00300004", "索引值超出范围。");
		hashTable.put("00300005", "集合为空，不能获取数据。");
		hashTable.put("00300100", "系统未知错误。");
		hashTable.put("00300101", "Shapefile文件名无效。");
		hashTable.put("00300102", "指定的Shapefile文件不存在。");
		hashTable.put("00300103", "Shapefile文件内部错误。");
		hashTable.put("00300104", "Shapefile中DBF文件不存在。");
		hashTable.put("00300105", "Shapefile中DBF文件无效。");
		hashTable.put("00300106", "Shapefile中SHX文件不存在。");
		hashTable.put("00300107", "Shapefile中SHX文件无效。");
		hashTable.put("00300108", "Shapefile中坐标系设置文件无效。");
		hashTable.put("00300109", "指定服务器的SDE要素集不存在。");
		hashTable.put("00300110", "无法连接指定的SDE服务器，请重新确认连接参数。");
		hashTable.put("00300111", "没有连接SDE数据源。");
		hashTable.put("00300112", "数据源没有连接，无法读取属性。");
		hashTable.put("00300113", "空间索引文件无法创建。");
		hashTable.put("00300114", "坐标系统已经设置，并且是不能修改的。");
		hashTable.put("00300115", "不支持{0}图形类型。");
		hashTable.put("00300201", "数据源被锁定，无法进入编辑状态。");
		hashTable.put("00300202", "当前编辑状态没有结束，不能开始新的编辑状态。");
		hashTable.put("00300203", "没有在编辑状态下，无法新建、更新、删除要素。");
		hashTable.put("00300204", "至少需要一个图层处于编辑状态。");
		hashTable.put("00300205", "字段已经存在，不能追加相同名称字段。");
		hashTable.put("00300206", "编辑保存失败。");
		hashTable.put("00300301", "在图层中，数据源属性没有设置。");
		hashTable.put("00300302", "指定的画布不能被使用。");
		hashTable.put("00300303", "指定的画布无效。");
		hashTable.put("00300401", "图形是空的，不能被操作。");
		hashTable.put("00300402", "Polyline必须要大于等于两个点。");
		hashTable.put("00300403", "Polygon必须要大于等于三个点。");
		hashTable.put("00300404", "{0}空间关系在该图形中没有实现。");
		hashTable.put("00300501", "线型宽度必须大于零。");
		hashTable.put("00300502", "点符号大小必须大于零。");
		hashTable.put("00300503", "系统中，不存在指定的字体类型。");
		hashTable.put("00300601", "指定文件类型无效，当前文件输出只支持JPG、PNG、EMF和JPEG。");
		hashTable.put("00300602", "地图文件中，{0}节点的属性不存在。");
		hashTable.put("00300603", "地图文件中，{0}节点的属性类型不匹配。");
		hashTable.put("00300604", "地图文件格式内部错误。");
		hashTable.put("00300605", "地图中没有图层。");
		//上层应用程序
		hashTable.put("00400001", "您输入的用户名和密码不正确，请重新输入。");
		hashTable.put("00400002", "影像数据不存在。");
		hashTable.put("00400003", "您输入的数据类型错误。");
		hashTable.put("00400004", "您输入的参数范围错误。");
		hashTable.put("00400005", "您选择的文件不存在。");
		hashTable.put("00400006", "您选择的路径不存在。");
		hashTable.put("00400007", "您选择的文件格式错误。");
		hashTable.put("00400008", "您选择的数据类型错误。");
		hashTable.put("00400009", "影像间的摊投影信息不匹配。");
		hashTable.put("00400010", "您选择的数据源区域不正确。");
		hashTable.put("00400011", "下载数据失败。");
		hashTable.put("00400012", "未知操作异常。");

		////////////////////////////////////////////////////////////////////////
		hashTable.put("0001", "输入参数错误。");


		//
		hashTable.put("1001", "ROI文件格式不正确。");
		hashTable.put("1002", "ROI文件无效。");
		hashTable.put("1003", "属性表字段索引值越界。");

		hashTable.put("1010", "Shapefile中SHX文件不存在。");
		hashTable.put("1011", "ShapefileSHX文件无效。");
		hashTable.put("1012", "Shapefile中DBF文件不存在。");
		hashTable.put("1013", "Shapefile中DBF文件无效。");
		hashTable.put("1014", "ShapefileSHP文件不存在。");
		hashTable.put("1015", "ShapefileSHP文件无效。");

		hashTable.put("1020", "图形为空，不能进行绘制。");
		hashTable.put("1021", "要素风格为空，不能进行绘制。");
		hashTable.put("1022", "图形类型不正确，不能进行绘制。");
		hashTable.put("1023", "参数值超出允许范围。");
		hashTable.put("1024", "索引值超出范围。");
		hashTable.put("1025", "画布为空，不能进行绘制。");
		hashTable.put("1026", "引用对象为空。");
		hashTable.put("1027", "参数类型不符。");
		hashTable.put("1028", "关联的地图为空。");
		hashTable.put("1029", "当纸张类型为Custom时，才能自定义纸张大小。");
		hashTable.put("1030", "文件不存在。");
		hashTable.put("1031", "列表中不包括该项。");
		hashTable.put("1032", "指定要素集为空。");
		hashTable.put("1033", "值已在列表中。");
		hashTable.put("1034", "栅格图层为空，不能进行绘制。");
		hashTable.put("1035", "颜色表中颜色数应为256个。");
		hashTable.put("1036", "数组中项的数目不正确。");
		hashTable.put("1037", "读取栅格文件错误。");
		hashTable.put("1038", "影像波段不存在。");
		hashTable.put("1039", "图层数据源为空。");
		hashTable.put("1040", "渲染方式为空。");
		hashTable.put("1041", "参数设置有误。");
		hashTable.put("1042", "栅格图层没有颜色表信息。");
		hashTable.put("1043", "应输入数字。");
		hashTable.put("1044", "读取模板文件错误。");
		return hashTable;
	}
}
