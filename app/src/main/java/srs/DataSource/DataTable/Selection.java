package srs.DataSource.DataTable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**DataTable中所需要的Select语句的搜索工作
 * @author bqzf
 * @version 20150606
 */
public class Selection {

	private DataTable mTable;
	private List<Integer> mListTrue;
	private List<Integer> mListFalse;
	private String mOr=" or ";
	private String mAnd=" and ";

	/**判断小括号  
	 * $0:全部； $1:and或or；$2:and或or；$3:小括号部分 
	 */
	String Strregex_hasbrace="(\\s(or|and)\\s)?(\\([\\w\\s\\.><=!\']+\\))"; 

	/** 捕获 判断语句，仅仅捕获一个括号之内的语句，同时捕获其尾部所连带的 or或and
	 *  $0:全部； $1:完整的判断表达式；$2:左表达式；$3:判断符号；$4:右表达式；$5:or或and；$6:or或and
	 */
	//	String Strregex_rightOrAnd="(([\\w]+)\\s?(>|<|=|!=|>=|<=)\\s?([\\w\\.\']+))(\\s(or|and)\\s)?";

	/** 捕获 判断语句，仅仅捕获一个括号之内的语句，同时捕获其头部所连带的 or或and
	 *  $0:全部； $1:and或or；$2:and或or；$3:完整的判断表达式；$4:左表达式；$5:判断符号；$6:右表达式
	 */
	String Strregex_leftOrAnd="(\\s(or|and)\\s)?(([\\w]+)\\s?(>|<|=|!=|>=|<=)\\s?([\\w\\.\']+))";	

	public Selection(DataTable table){
		mTable=table;
		mListTrue=new ArrayList<Integer>();
		mListFalse=new ArrayList<Integer>();
	}

	/**返回满足条件的DataRow的序号
	 * @return
	 */
	public List<Integer> getListTrue(){
		return mListTrue;
	}

	/**设置所有行为非选中状态
	 * 
	 */
	private void SetSelectNone(){
		mListTrue.clear();
		mListFalse.clear();
		for(int i=0;i<mTable.getRows().size();i++){
			mListFalse.add(i);
		}
	}

	/**设置所有行为选中状态
	 * 
	 */
	private void SetSelectAll(){
		mListTrue.clear();
		mListFalse.clear();
		for(int i=0;i<mTable.getRows().size();i++){
			mListTrue.add(i);
		}
	}

	public void Verify(String exp) throws Exception{

		Pattern patternHasbrace = Pattern.compile(Strregex_hasbrace);
		Pattern patternUnbras=Pattern.compile(Strregex_leftOrAnd);
		Matcher matcher=patternHasbrace.matcher(exp);
		if(matcher.find()){
			return;
		}else if(patternUnbras.matcher(exp).find()){
			return;
		}else{
			throw new Exception("表达式不符合语法要求！");
		}
	}

	/**仅仅支持全是“or”，或者全是“and”的查询
	 * @param exp
	 * @throws Exception
	 */
	private void SwitchCaculate(String exp) throws Exception{
		if(exp.contains(mOr)){
			/**当全部为“或”关系*/
			SetSelectNone();
			SelectOfOr(Split(exp));
		}else{
			/**当全部为“并且”关系，或单个判断信息的查询*/
			SetSelectAll();
			SelectOfAnd(Split(exp));
		}
	}
	
	/** 仅仅支持全是“or”，或者全是“and”的查询
	 * @param lTrue 保存选中DataRow的行号  
	 * @param lFalse 保存未选中DataRow的行号
	 * @param exp 表达式
	 * @throws Exception
	 */
	private void SelectSwitch(List<Integer> lTrue,List<Integer> lFalse, String exp) throws Exception{
		if(exp.startsWith(mAnd)){
			exp= exp.substring(mAnd.length()).trim();			
		}else if(exp.startsWith(mOr)){
			exp= exp.substring(mOr.length()).trim();		
		}else{
			throw new Exception("选择条件表达式+\""+exp+"\"不正确！");
		}
		
		if(exp.contains(mOr)){
			/**当全部为“或”关系*/
			SelectNone(lTrue,lFalse);
			String[] exps=Split(exp); //每条 判断 语句 			
			String[] exp_value;
			int exp_decide;
			int i=0;
			mListFalse.addAll(lFalse);
			while(i<exps.length&&mListFalse.size()>0){
				exp_value = getDecideExps(exps[i]);
				exp_decide=getDecide(exps[i]);
				SelectListOr(lTrue,lFalse,exp_value[0],exp_decide,exp_value[1]);
				i++;
			}
		}else{
			/**当全部为“并且”关系，或单个判断信息的查询*/
			SelectAll(lTrue,lFalse);
			String[] exps=Split(exp); //每条 判断 语句 	
			String[] exp_value;
			int exp_decide;
			int i=0;
			while(i<exps.length&&mListTrue.size()>0){
				exp_value = getDecideExps(exps[i]);
				exp_decide=getDecide(exps[i]);
				SelectListAnd(lTrue,lFalse,exp_value[0],exp_decide,exp_value[1]);
				i++;
			}
			
		}
	}
	
	/**将lFasle中 的 所有 行 标记 为 选定 ，放入 lTrue
	 * @param lTrue 选中 行的 序号 
	 * @param lFalse 未选中 行 的序号
	 */
	private void SelectAll(List<Integer> lTrue,List<Integer> lFalse){
		for(int i=lFalse.size()-1;i>-1;i--){
			lTrue.add(lFalse.get(i));
			lFalse.remove(i);
		}
	}
	
	/**将lTrue中 的 所有 行 标记 为不选  ，放入 lFasle
	 * @param lTrue 选中 行的 序号 
	 * @param lFalse 未选中 行 的序号
	 */
	private void SelectNone(List<Integer> lTrue,List<Integer> lFalse){
		for(int i=lTrue.size()-1;i>-1;i--){
			lFalse.add(lTrue.get(i));
			lTrue.remove(i);
		}
	}
	

	public void Calculate2(String exp) throws Exception{
		/*Pattern patternHasbrace = Pattern.compile(Strregex_hasbrace);
		Pattern patternNonebrace=Pattern.compile(Strregex_leftOrAnd);
		Matcher matcherHasbrace=patternHasbrace.matcher(exp);
		Matcher matcherNonebrace=patternNonebrace.matcher(exp);
		if(matcherHasbrace.find()){*/
			/**初始化选中与被选中列表*/
			SetSelectAll();
			List<String> exps = CalculateBrace(exp,1);
			CalculateExps(exps);
		/*}else if(matcherNonebrace.find()){
			SwitchCaculate(exp);
		}else{
			throw new Exception("表达式不符合语法要求！");
		}*/
	}

	/**按顺序将满足条件
	 * @param exps
	 * @throws Exception 
	 */
	private void CalculateExps(List<String> exps) throws Exception {
		for(String exp : exps){
			if(exp.startsWith(mAnd)){
				List<Integer> lFalse=new ArrayList<Integer>();
//				String[] expChilds=Split(exp);
				SelectSwitch(mListTrue, lFalse, exp);
				this.mListFalse.addAll(lFalse);
			}else if(exp.startsWith(mOr)){
				List<Integer> lTrue=new ArrayList<Integer>();
//				String[] expChilds=Split(exp);
				SelectSwitch(lTrue,mListFalse, exp);
				this.mListTrue.addAll(lTrue);
			}		
		}
	}

	/**目前仅仅支持全是"||"和全是"&&"的表达式。不支持小括号
	 * @param exp 表达式
	 * @return
	 * @throws Exception 
	 */
	private  String[] Split(String exp) throws Exception{
		if(exp.startsWith("(")&&exp.endsWith(")")){
			exp=exp.substring(1);
			exp=exp.substring(0,exp.length()-1);
		}
		if(exp.contains(mOr)&&!exp.contains(mAnd)){
			String[] exps= exp.split(mOr);
			return exps;
		}
		else if(exp.contains(mAnd)&&!exp.contains(mOr)){
			String[] exps=exp.split(mAnd);
			return exps;
		}
		else if(exp.contains(mAnd)&&exp.contains(mOr)&&!exp.contains("(")&&!exp.contains(")")){
			throw new Exception("表达式\""+exp+"\"不正确 ！");
		}
		else{
			return new String[]{exp};
		}
		
	}

	/**按小括号分解选择语句，将整条语句按顺序分解成若干组
	 * 分为若干段：有小括号的、没有小括号的
	 * @param exp
	 * @param IsAnd
	 * @return
	 * @throws Exception 
	 */
	private List<String> CalculateBrace(String exp,int IsAnd) throws Exception {
		List<String> patters=new ArrayList<String>();
		exp=mAnd+exp.trim();
		Pattern patternHasbrace = Pattern.compile(Strregex_hasbrace);
		Matcher matcherHasbrace=patternHasbrace.matcher(exp);
		Pattern patternNonebrace=Pattern.compile(Strregex_leftOrAnd);
		Matcher matcherNonebrace=patternNonebrace.matcher(exp);
		if(matcherHasbrace.find()){
			int currentIndex=0;
			while(matcherHasbrace.find(currentIndex)){
				String strbrace= matcherHasbrace.group(0);
				int resultStartIndex=exp.indexOf(strbrace);
				if(currentIndex!=resultStartIndex){				
					patters.add(exp.substring(currentIndex,exp.indexOf(strbrace)-1));
				}
				patters.add(strbrace);
				currentIndex=resultStartIndex+strbrace.length();				
			}
			if(currentIndex!=exp.length()){
				patters.add(exp.substring(currentIndex));
			}
		}else if(matcherNonebrace.find()){
			patters.add(exp);
		}else{
			throw new Exception("表达式不符合语法要求！");
		}
		return patters;
	}

	/**满足任意一条关系的行（各个exp之间都使用“||”连接）
	 * @param exp
	 * @throws Exception 
	 */
	private void SelectOfOr(String[] exps) throws Exception{
		String[] exp_value;
		int exp_decide;
		int i=0;
		while(i<exps.length&&mListFalse.size()>0){
			exp_value = getDecideExps(exps[i]);
			exp_decide=getDecide(exps[i]);
			SelectListOr(exp_value[0],exp_decide,exp_value[1]);
			i++;
		}
	}

	/**满足所有判断关系的行（各个exp之间都使用“&&”连接）
	 * @param exp
	 * @throws Exception 
	 */
	private void SelectOfAnd(String[] exps) throws Exception{
		String[] exp_value;
		int exp_decide;
		int i=0;
		while(i<exps.length&&mListTrue.size()>0){
			exp_value = getDecideExps(exps[i]);
			exp_decide=getDecide(exps[i]);
			SelectListAnd(exp_value[0],exp_decide,exp_value[1]);
			i++;
		}
	}


	/**满足任意一条关系的行（各个exp之间都使用“||”连接）
	 * @param exp
	 * @throws Exception 
	 */
	/*private void SelectOfOr(List<Integer> lTrue,List<Integer> lFalse,String[] exps) throws Exception{
		String[] exp_value;
		int exp_decide;
		int i=0;
		while(i<exps.length&&mListFalse.size()>0){
			exp_value = getDecideExps(exps[i]);
			exp_decide=getDecide(exps[i]);
			SelectListOr(lTrue,lFalse,exp_value[0],exp_decide,exp_value[1]);
			i++;
		}
	}*/

	/**满足所有判断关系的行（各个exp之间都使用“&&”连接）
	 * @param exp
	 * @throws Exception 
	 */
	/*private void SelectOfAnd(List<Integer> lTrue,List<Integer> lFalse,String[] exps) throws Exception{
		String[] exp_value;
		int exp_decide;
		int i=0;
		while(i<exps.length&&mListTrue.size()>0){
			exp_value = getDecideExps(exps[i]);
			exp_decide=getDecide(exps[i]);
			SelectListAnd(lTrue,lFalse,exp_value[0],exp_decide,exp_value[1]);
			i++;
		}
	}*/


	/**从指定的数据表中搜索出满足条件的行
	 * @param exp 判断条件
	 * @throws Exception 包含表达式中的语法错误
	 */
	public void Calculate(String exp) throws Exception{
		try{
			Verify(exp);
			SwitchCaculate(exp);
		}catch(Exception e){
			throw e;
		}
	}

	/**获取判断关系符号
	 * @param exp
	 * @return
	 */
	private  int getDecide(String exp){
		if(exp.contains(">")){
			return Decide.DEFAULT_GREATER;
		}else if(exp.contains("<")){
			return Decide.DEFAULT_LESS;
		}else if(exp.contains("=")){
			return Decide.DEFAULT_EQUAL;
		}else if(exp.contains(">=")){
			return Decide.DEFAULT_GREATEROREQUAL;
		}else if(exp.contains("<=")){
			return Decide.DEFAULT_LESSOREQUAL;
		}else if(exp.contains("!=")){
			return Decide.DEFAULT_UNEQUAL;
		}else{
			return Decide.DEFAULT_WRONG;
		}
	}

	/**拆饭判断关系表达式为左表达式与右表达式
	 * @param exp 关系判断表达式
	 * @return [左表达式，右表达式]  
	 * @throws Exception 若拆分结果不是长度为2的字符串数组则抛出异常，并指示出错误的表达式
	 */
	private  String[] getDecideExps(String exp) throws Exception{
		String[] results=null;
		if(exp.contains(">")){
			results=exp.split(">");
		}else if(exp.contains("<")){
			results=exp.split("<");
		}else if(exp.contains("=")){
			results=exp.split("=");
		}else if(exp.contains(">=")){
			results=exp.split(">=");
		}else if(exp.contains("<=")){
			results=exp.split("<=");
		}else if(exp.contains("!=")){
			results=exp.split("!=");
		}
		if(results.length==2){
			return results;
		}else{
			throw new Exception("判断关系表达式"+exp+"有错误");
		}
	}

	/*private  List<Integer> DecideRow(String FieldName,String Value,Decide decide){
		List<Integer> result=new ArrayList<Integer>();
		return result;
	}
*/
	/**从“ListFalse”中选择出满足条件的行号保存在“lTrue”中,不满足条件的放入lFalse中;
	 * @param lTrue
	 * @param FieldName
	 * @param decide
	 * @param right
	 * @throws Exception 
	 */
	private void SelectListOr(List<Integer> lTrue,List<Integer> lFalse, String FieldName,
			int decide,String right) throws Exception{
		DataColumn column= mTable.getColumns().get(FieldName);
		switch(column.getDataType()){
		case DataTypes.DATATABLE_INT:
			int right_int=Integer.valueOf(right);
			for(int i= lFalse.size()-1;i>-1;i--){
				int fvalue=(Integer)column.getObject(lFalse.get(i));
				if(CheckDecide(fvalue,decide,right_int)){
					lTrue.add(lFalse.get(i));
					lFalse.remove(i);
				}
			}
			break;
		case DataTypes.DATATABLE_DOUBLE:
			double right_double=Double.valueOf(right);
			for(int i= lFalse.size()-1;i>-1;i--){
				double fvalue=(Double)column.getObject(lFalse.get(i));
				if(CheckDecide(fvalue,decide,right_double)){
					lTrue.add(lFalse.get(i));
					lFalse.remove(i);
				}
			}
			break;
		case DataTypes.DATATABLE_FLOAT:
			float right_float=Float.valueOf(right);
			for(int i= lFalse.size()-1;i>-1;i--){
				float fvalue=(Float)column.getObject(lFalse.get(i));
				if(CheckDecide(fvalue,decide,right_float)){
					lTrue.add(lFalse.get(i));
					lFalse.remove(i);
				}
			}
			break;
		case DataTypes.DATATABLE_SHORT:
			short right_short=Short.valueOf(right);
			for(int i= lFalse.size()-1;i>-1;i--){
				short fvalue=(Short)column.getObject(lFalse.get(i));
				if(CheckDecide(fvalue,decide,right_short)){
					lTrue.add(lFalse.get(i));
					lFalse.remove(i);
				}
			}
			break;
		case DataTypes.DATATABLE_LONG:
			long right_long=Integer.valueOf(right);
			for(int i= lFalse.size()-1;i>-1;i--){
				long fvalue=(Long)column.getObject(lFalse.get(i));
				if(CheckDecide(fvalue,decide,right_long)){
					lTrue.add(lFalse.get(i));
					lFalse.remove(i);
				}
			}
			break;
		case DataTypes.DATATABLE_STRING:
			if(right.contains("'")){
				String right_string=right.substring(right.indexOf("'")+1);
				right_string=right_string.substring(0,right_string.indexOf("'"));
				for(int i= lFalse.size()-1;i>-1;i--){
					String fvalue=new String(column.getString(lFalse.get(i)).toString().getBytes("ISO-8859-1"),"GBK");
					if(CheckDecide(fvalue,decide,right_string)){
						lTrue.add(lFalse.get(i));
						lFalse.remove(i);
					}
				}
			}else{
				throw new Exception("选择语句中"+right+"为字符串，需要在首位使用"+"'"+"号");
			}
			break;
		}
	}


	/**从“ListFalse”中选择出满足条件的行号保存在“ListTrue”中;
	 * @param FieldName
	 * @param decide
	 * @param right
	 * @throws Exception 
	 */
	private void SelectListOr(String FieldName,
			int decide,String right) throws Exception{
		SelectListOr(this.mListTrue,this.mListFalse,FieldName,decide,right);
	}

	/**从指定数据源的选择出满足条件的 DataRow的编号,和不满足条件的DataRow的编号
	 * @param FieldName 要判断的目标字段的名称
	 * @param decide 匹配字符
	 * @param right 判断匹配的表达式
	 * @throws Exception 
	 */
	private  void SelectListAnd(String FieldName,
			int decide,String right) throws Exception{
		SelectListAnd(this.mListTrue,this.mListFalse,FieldName,decide,right);
	}

	/**从指定数据源的选择出满足条件的 DataRow的编号,和不满足条件的DataRow的编号，并将不满足的编号放在lFalse里
	 * @author lTrue 满足条件的行号
	 * @param lFalse 不满足条件的行号
	 * @param FieldName 要判断的目标字段的名称
	 * @param decide 匹配字符
	 * @param right 判断匹配的表达式
	 * @throws Exception 
	 */
	private  void SelectListAnd(List<Integer> lTrue, List<Integer> lFalse, String FieldName,
			int decide,String right) throws Exception{
		DataColumn column= mTable.getColumns().get(FieldName);
		switch(column.getDataType()){
		case DataTypes.DATATABLE_INT:
			int right_int=Integer.valueOf(right);
			for(int i=lTrue.size()-1;i>-1;i--){
				int fvalue=(Integer)column.getObject(lTrue.get(i));
				if(!CheckDecide(fvalue,decide,right_int)){
					lFalse.add(lTrue.get(i));
					lTrue.remove(i);
				}
			}
			break;
		case DataTypes.DATATABLE_DOUBLE:
			double right_double=Double.valueOf(right);
			for(int i=lTrue.size()-1;i>-1;i--){
				double fvalue=(Double)column.getObject(lTrue.get(i));
				if(!CheckDecide(fvalue,decide,right_double)){
					lFalse.add(lTrue.get(i));
					lTrue.remove(i);
				}
			}
			break;
		case DataTypes.DATATABLE_FLOAT:
			float right_float=Float.valueOf(right);
			for(int i=lTrue.size()-1;i>-1;i--){
				float fvalue=(Float)column.getObject(lTrue.get(i));
				if(!CheckDecide(fvalue,decide,right_float)){
					lFalse.add(lTrue.get(i));
					lTrue.remove(i);
				}
			}
			break;
		case DataTypes.DATATABLE_SHORT:
			short right_short=Short.valueOf(right);
			for(int i=lTrue.size()-1;i>-1;i--){
				short fvalue=(Short)column.getObject(lTrue.get(i));
				if(!CheckDecide(fvalue,decide,right_short)){
					lFalse.add(lTrue.get(i));
					lTrue.remove(i);
				}
			}
			break;
		case DataTypes.DATATABLE_LONG:
			long right_long=Integer.valueOf(right);
			for(int i=lTrue.size()-1;i>-1;i--){
				long fvalue=(Long)column.getObject(lTrue.get(i));
				if(!CheckDecide(fvalue,decide,right_long)){
					lFalse.add(lTrue.get(i));
					lTrue.remove(i);
				}
			}
		case DataTypes.DATATABLE_STRING:
			if(right.contains("'")){
				String right_string=right.substring(right.indexOf("'")+1);
				right_string=right_string.substring(0,right_string.indexOf("'"));
				for(int i=lTrue.size()-1;i>-1;i--){
					String fvalue=new String(column.getString(lTrue.get(i)).toString().getBytes("ISO-8859-1"),"GBK");
					if(!CheckDecide(fvalue,decide,right_string)){
						lFalse.add(lTrue.get(i));
						lTrue.remove(i);
					}
				}
			}else{
				throw new Exception("选择语句中"+right+"为字符串，需要在首位使用"+"'"+"号");
			}
			break;
		}
	}

	/**选择出满足条件的 DataRow的编号
	 * @param table 源数据
	 * @param FieldName 要判断的目标字段的名称
	 * @param decide 匹配字符
	 * @param right 判断匹配的表达式
	 * @return
	 * @throws DataException
	 * @throws UnsupportedEncodingException
	 *//*
	private  List<Integer> SelectList( String FieldName,
			int decide,String right) throws DataException, UnsupportedEncodingException{
		DataColumn column= mTable.getColumns().get(FieldName);
		List<Integer> result=new ArrayList<Integer>();
		int lengthTable=mTable.getRows().size();
		switch(column.getDataType()){
		case DataTypes.DATATABLE_INT:
			int right_int=Integer.valueOf(right);
			for(int i=0;i<lengthTable;i++){
				int fvalue=(Integer)column.getObject(i);
				if(CheckDecide(fvalue,decide,right_int)){
					result.add(i);
				}
			}
			break;
		case DataTypes.DATATABLE_DOUBLE:
			double right_double=Double.valueOf(right);
			for(int i=0;i<lengthTable;i++){
				double fvalue=(Double)column.getObject(i);
				if(CheckDecide(fvalue,decide,right_double)){
					result.add(i);
				}
			}
			break;
		case DataTypes.DATATABLE_FLOAT:
			float right_float=Float.valueOf(right);
			for(int i=0;i<lengthTable;i++){
				float fvalue=(Float)column.getObject(i);
				if(CheckDecide(fvalue,decide,right_float)){
					result.add(i);
				}
			}
			break;
		case DataTypes.DATATABLE_SHORT:
			short right_short=Short.valueOf(right);
			for(int i=0;i<lengthTable;i++){
				short fvalue=(Short)column.getObject(i);
				if(CheckDecide(fvalue,decide,right_short)){
					result.add(i);
				}
			}
			break;
		case DataTypes.DATATABLE_LONG:
			long right_long=Integer.valueOf(right);
			for(int i=0;i<lengthTable;i++){
				long fvalue=(Long)column.getObject(i);
				if(CheckDecide(fvalue,decide,right_long)){
					result.add(i);
				}
			}
		case DataTypes.DATATABLE_STRING:
			String right_string=right;
			for(int i=0;i<lengthTable;i++){
				String fvalue=new String(column.getString(i).toString().getBytes("ISO-8859-1"),"GBK");
				if(CheckDecide(fvalue,decide,right_string)){
					result.add(i);
				}
			}
			break;
		}
		return result;
	}*/

	/**判断两整数关系
	 * @param left 关系符左边的表达式
	 * @param decident 关系符号右边的表达式
	 * @param right 关系符号右边的表达式
	 * @return 若满足条件则返回true，否则返回false
	 */
	private  boolean CheckDecide(int left,int decident,int right){
		switch(decident){
		case Decide.DEFAULT_GREATER:
			if(left>right)
				return true;
			break;
		case Decide.DEFAULT_LESS:
			if(left<right)
				return true;
			break;
		case Decide.DEFAULT_EQUAL:
			if(left==right)
				return true;
			break;
		case Decide.DEFAULT_GREATEROREQUAL:
			if(left>=right)
				return true;
			break;
		case Decide.DEFAULT_LESSOREQUAL:
			if(left<=right)
				return true;
			break;
		case Decide.DEFAULT_UNEQUAL:
			if(left!=right)
				return true;
			break;
		}
		return false;
	}

	/**判断两整数关系
	 * @param left 关系符左边的表达式
	 * @param decident 关系符号右边的表达式
	 * @param right 关系符号右边的表达式
	 * @return 若满足条件则返回true，否则返回false
	 */
	private  boolean CheckDecide(double left,int decident,double right){
		switch(decident){
		case Decide.DEFAULT_GREATER:
			if(left-right>0)
				return true;
			break;
		case Decide.DEFAULT_LESS:
			if(left-right<0)
				return true;
			break;
		case Decide.DEFAULT_EQUAL:
			if(left-right<Double.MIN_VALUE)
				return true;
			break;
		case Decide.DEFAULT_GREATEROREQUAL:
			if(left>right||left-right<Double.MIN_VALUE)
				return true;
			break;
		case Decide.DEFAULT_LESSOREQUAL:
			if(left<right||left-right<Double.MIN_VALUE)
				return true;
			break;
		case Decide.DEFAULT_UNEQUAL:
			if(left-right>Double.MIN_VALUE)
				return true;
			break;
		}
		return false;
	}

	/**判断两整数关系
	 * @param left 关系符左边的表达式
	 * @param decident 关系符号右边的表达式
	 * @param right 关系符号右边的表达式
	 * @return 若满足条件则返回true，否则返回false
	 */
	private  boolean CheckDecide(float left,int decident,float right){
		switch(decident){
		case Decide.DEFAULT_GREATER:
			if(left-right>0)
				return true;
			break;
		case Decide.DEFAULT_LESS:
			if(left-right<0)
				return true;
			break;
		case Decide.DEFAULT_EQUAL:
			if(left-right<Float.MIN_VALUE)
				return true;
			break;
		case Decide.DEFAULT_GREATEROREQUAL:
			if(left>right||left-right<Float.MIN_VALUE)
				return true;
			break;
		case Decide.DEFAULT_LESSOREQUAL:
			if(left<right||left-right<Float.MIN_VALUE)
				return true;
			break;
		case Decide.DEFAULT_UNEQUAL:
			if(left-right>Float.MIN_VALUE)
				return true;
			break;
		}
		return false;
	}

	/**判断两整数关系
	 * @param left 关系符左边的表达式
	 * @param decident 关系符号右边的表达式
	 * @param right 关系符号右边的表达式
	 * @return 若满足条件则返回true，否则返回false
	 */
	private  boolean CheckDecide(long left,int decident,long right){
		switch(decident){
		case Decide.DEFAULT_GREATER:
			if(left>right)
				return true;
			break;
		case Decide.DEFAULT_LESS:
			if(left<right)
				return true;
			break;
		case Decide.DEFAULT_EQUAL:
			if(left==right)
				return true;
			break;
		case Decide.DEFAULT_GREATEROREQUAL:
			if(left>=right)
				return true;
			break;
		case Decide.DEFAULT_LESSOREQUAL:
			if(left<=right)
				return true;
			break;
		case Decide.DEFAULT_UNEQUAL:
			if(left!=right)
				return true;
			break;
		}
		return false;
	}

	/**判断两整数关系
	 * @param left 关系符左边的表达式
	 * @param decident 关系符号右边的表达式
	 * @param right 关系符号右边的表达式
	 * @return 若满足条件则返回true，否则返回false
	 */
	private  boolean CheckDecide(short left,int decident,short right){
		switch(decident){
		case Decide.DEFAULT_GREATER:
			if(left>right)
				return true;
			break;
		case Decide.DEFAULT_LESS:
			if(left<right)
				return true;
			break;
		case Decide.DEFAULT_EQUAL:
			if(left==right)
				return true;
			break;
		case Decide.DEFAULT_GREATEROREQUAL:
			if(left>=right)
				return true;
			break;
		case Decide.DEFAULT_LESSOREQUAL:
			if(left<=right)
				return true;
			break;
		case Decide.DEFAULT_UNEQUAL:
			if(left!=right)
				return true;
			break;
		}
		return false;
	}

	/**判断两整数关系
	 * @param left 关系符左边的表达式
	 * @param decident 关系符号右边的表达式
	 * @param right 关系符号右边的表达式
	 * @return 若满足条件则返回true，否则返回false
	 */
	private  boolean CheckDecide(String left,int decident,String right){
		switch(decident){
		case Decide.DEFAULT_EQUAL:
			if(left.trim().equalsIgnoreCase(right.trim()))
				return true;
			break;
		case Decide.DEFAULT_UNEQUAL:
			if(!left.trim().equalsIgnoreCase(right.trim()))
				return true;
			break;
		case Decide.DEFAULT_GREATER:
		case Decide.DEFAULT_LESS:
		case Decide.DEFAULT_GREATEROREQUAL:
		case Decide.DEFAULT_LESSOREQUAL:
			break;
		}
		return false;
	}
}
