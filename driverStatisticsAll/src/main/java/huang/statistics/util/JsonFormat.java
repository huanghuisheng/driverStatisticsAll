package huang.statistics.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonFormat {
	
	
	private  String  state;
	private String msg;
	private List<Map<String,Object>> list;
	
	
  public  static  Map<String,Object>  getJson(String state,String msg,List<Map<String,Object>> list){
	  
	  Map<String, Object> listMap = new HashMap<String, Object>();
	 
	  listMap.put("state", state);
	  listMap.put("msg", msg);
	  listMap.put("result", list);
	
  		
	  return listMap;
	  
  }
	

}
