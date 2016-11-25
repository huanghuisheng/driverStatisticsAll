package huang.statistics.util;

import huang.statistics.dao.BasicDao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class UserAccount {

	/**
	 * 获取已激活的imei号
	 * @throws Exception 
	 * 
	 */
	public static List<String> activeGetListImei(HttpServletRequest request) throws Exception
	{
		List<Map<String, Object>> listImei = new ArrayList<Map<String, Object>>();
		List<String> activelistImei = new ArrayList<String>();
		//获取用户的所拥有的imei号
		String type=String.valueOf(request.getSession().getAttribute("adminType"));
		String adminDepartment=String.valueOf(request.getSession().getAttribute("adminDepartment"));
		
		listImei=BasicDao.userGetMobileImei(type,adminDepartment);
		System.out.println("all imei is "+listImei.size());
		//获取已经激活的imei号
		for(int i=0;i<listImei.size();i++)
		{
			String imei=String.valueOf(listImei.get(i).get("c_imei"));
			List<Map<String, Object>> list1=BasicDao.userGetActiveMobileImei(imei);
			if(list1.size()>0){
				activelistImei.add(imei);
			}
		}
		System.out.println("all1 imei is "+activelistImei.size());
		return activelistImei;
	}
   //----------------------------------------------------------------------------------------分地区得imei号	
	public static List<String> activeGetListImei1() throws Exception
	{
		List<Map<String, Object>> listImei = new ArrayList<Map<String, Object>>();
		List<String> activelistImei = new ArrayList<String>();
		//获取用户的所拥有的imei号
		listImei=BasicDao.userGetMobileImei("1",null);
		//获取已经激活的imei号
		for(int i=0;i<listImei.size();i++)
		{
			String imei=String.valueOf(listImei.get(i).get("c_imei"));
			List<Map<String, Object>> list1=BasicDao.userGetActiveMobileImei1(imei,"allcode");
			if(list1.size()>0){
				activelistImei.add(imei);
			}
		}
		return activelistImei;
	}
	public static List<String> activeGetListImei2(String code) throws Exception
	{
		List<Map<String, Object>> listImei = new ArrayList<Map<String, Object>>();
		List<String> activelistImei = new ArrayList<String>();
		//获取用户的所拥有的imei号
		listImei=BasicDao.userGetMobileImei("1",null);
		//获取已经激活的imei号
		for(int i=0;i<listImei.size();i++)
		{
			String imei=String.valueOf(listImei.get(i).get("c_imei"));
			Object object=listImei.get(i).get("c_region");
			if(object!=null && !("").equals(object)){
				String region=String.valueOf(listImei.get(i).get("c_region"));
				region=region.substring(0, 2);
				if(region.equals(code))
				{
					activelistImei.add(imei);	
				}
			}
		}
		return activelistImei;
	}
	  //----------------------------------------------------------------------------------------分组得imei号	
		public static List<String> activeGetListImei3(String groupid) throws Exception
		{
			List<Map<String, Object>> listImei = new ArrayList<Map<String, Object>>();
			List<String> activelistImei = new ArrayList<String>();
			//获取用户的所拥有的imei号
			listImei=BasicDao.userGetMobileImei("0",groupid);
			//获取已经激活的imei号
			for(int i=0;i<listImei.size();i++)
			{
				String imei=String.valueOf(listImei.get(i).get("c_imei"));
				//List<Map<String, Object>> list1=BasicDao.userGetActiveMobileImei1(imei,"allcode");
				//if(list1.size()>0){
					activelistImei.add(imei);
				//}
			}
			return activelistImei;
		}
	
	
		  //----------------------------------------------------------------------------------------分组得imei号	
			public static List<String> activeGetListImei4(String code,String groupid) throws Exception
			{
				List<Map<String, Object>> listImei = new ArrayList<Map<String, Object>>();
				List<String> activelistImei = new ArrayList<String>();
				//获取用户的所拥有的imei号
				listImei=BasicDao.userGetMobileImei("0",groupid);
				//获取已经激活的imei号
				for(int i=0;i<listImei.size();i++)
				{
					//String imei=String.valueOf(listImei.get(i).get("c_imei"));
					//List<Map<String, Object>> list1=BasicDao.userGetActiveMobileImei1(imei,"allcode");
					//if(list1.size()>0){
						//activelistImei.add(imei);
					//}
						String imei=String.valueOf(listImei.get(i).get("c_imei"));
						Object object=listImei.get(i).get("c_region");
						if(object!=null && !("").equals(object)){
							String region=String.valueOf(listImei.get(i).get("c_region"));
							region=region.substring(0, 2);
							if(region.equals(code))
							{
								activelistImei.add(imei);	
							}
						}
						
						
						
						
						
				}
				return activelistImei;
			}
		
		
		
		
		
	
	
	
	/**
	 * 方差公式
	 */
	public static float getVariance(float number[]) throws Exception
	{
		int length=number.length;
		float sum=0;
		for(int i=0;i<length;i++)
		{
			sum=sum+number[i];
			
		}
		float averageNumber=(float)sum/(float)length;
		float varianceSum=0;
		for(int j=0;j<length;j++)
		{
			float  numbers=(number[j]-averageNumber);
			numbers=Math.abs(numbers);
			varianceSum=(float) (varianceSum+Math.pow(numbers, 2)); 
		}
		float variance=varianceSum/length;
		BigDecimal b = new BigDecimal(variance); 
		float f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue(); 
		
		System.out.println("fffffffffff"+f1);
		return f1;
	}
	
	
	
	
	
	
	
	
	

}
