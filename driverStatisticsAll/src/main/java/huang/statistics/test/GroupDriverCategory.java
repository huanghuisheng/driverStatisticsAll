package huang.statistics.test;

import huang.statistics.dao.BasicDao;
import huang.statistics.dao.StatisticsDaoImpl;
import huang.statistics.util.DateUtil;
import huang.statistics.util.UserAccount;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupDriverCategory {
	// -----------------------------------------------转换到路径表
	public String transTravelDrive(String imei) {
		imei = imei.substring(13);
		String travel = "iov_travel_" + Integer.valueOf(imei) % 10;
		return travel;
	}
	public static void main(String[] args) throws Exception {
		GroupDriverCategory test = new GroupDriverCategory();
		BasicDao dao = new BasicDao();
		StatisticsDaoImpl statisticsDaoImpl = new StatisticsDaoImpl();
		List<Map<String, Object>> listSum = new ArrayList<Map<String, Object>>();
		// 清空数据；
		dao.clearTableData("iov_group_bcategory");
		// 获取所有省市
		
		// 获取有几个分组
		List<Map<String, Object>> listGroup = dao.diverRuleTimeGetGroup();
		List<Map<String, Object>> listRegion = dao.diverRuleTimeGetRegion();
		
		for (Map<String, Object> map1 : listGroup) {
			String groupid = String.valueOf(map1.get("n_id"));
			
			for (int j = 0; j < listRegion.size(); j++) {
				int number1 = 0, number2 = 0, number3 = 0, number4 = 0;
				String region1 = String.valueOf(listRegion.get(j).get("c_code"));
				String region = region1.substring(0, 2);
				List<String> activeListImei = UserAccount.activeGetListImei4(region,groupid);
				System.out.println("------2-------"+activeListImei.size());
				
				if(activeListImei.size()>0)
				{
					for (int i = 0; i < activeListImei.size(); i++) {
						String imei = activeListImei.get(i);
						String travel = test.transTravelDrive(imei);
						
						List<Map<String, Object>> listNumber = dao.divercategoryTime1(region, imei, travel);
						System.out.println("-----number----"+listNumber.size());
						for (Map<String, Object> timeMap : listNumber) {
							String time1 = String.valueOf(timeMap.get("t_st_time"));
							String time2 = String.valueOf(timeMap.get("t_ed_time"));
							Date time11 = DateUtil.StringToday(time1, "yyyy-MM-dd HH:mm:ss");
							Date time22 = DateUtil.StringToday(time2, "yyyy-MM-dd HH:mm:ss");
							// 获取小时数；
							Calendar calendar = Calendar.getInstance();
							calendar.setTime(time11);
							int hours1 = calendar.get(Calendar.HOUR_OF_DAY);
							calendar.setTime(time22);
							int hours2 = calendar.get(Calendar.HOUR_OF_DAY);
							if ((hours1 < 8 && hours1 >= 0) || (hours2 < 8 && hours2 >= 0)) {
								number1++;
								break;
							} else if ((hours1 < 10 && hours1 >= 8) || (hours2 < 10 && hours2 >= 8)) {
								number2++;
								break;
							} else if ((hours1 < 19 && hours1 >= 17) || (hours2 < 19 && hours2 >= 17)) {
								number2++;
								break;
							} else if ((hours1 < 15 && hours1 >= 10) || (hours2 < 15 && hours2 >= 10)) {
								number3++;
								break;
							} else if ((hours1 < 24 && hours1 >= 19) || (hours2 < 24 && hours2 >= 19)) {
								number3++;
								break;
							} else {
								number4++;
								break;
							}
						}
						if(listNumber.size()==0)
						{
							number4++;	
						}
					}
					System.out.println("---"+number1+"---"+number2+"----"+number3+"------"+number4);
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("c_taxi_driver", number3);
					map.put("c_truck_driver", number1);
					map.put("c_office_worker", number2);
					map.put("c_other", number4);
					map.put("c_region", region1);
					map.put("c_group", groupid);
					dao.basicInsertData("iov_group_bcategory", map);	
				}
			}
			
			
			
		}
		
		


		
		
	}
}
