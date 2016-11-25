package huang.statistics.test;

import huang.statistics.dao.BasicDao;
import huang.statistics.util.DateUtil;
import huang.statistics.util.UserAccount;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupRultTime {
	// -----------------------------------------------转换到路径表
	public String transTravelDrive(String imei) {
		imei = imei.substring(13);
		String travel = "iov_travel_" + Integer.valueOf(imei) % 10;
		return travel;
	}
	public static void main(String[] args) throws Throwable {
		GroupRultTime test = new GroupRultTime();
		BasicDao dao = new BasicDao();
		List<Map<String, Object>> listSum = new ArrayList<Map<String, Object>>();
		List<String> activeImeiList = UserAccount.activeGetListImei1();
		// 清空数据；
		dao.clearTableData("iov_group_ruletime");
		long date1=System.currentTimeMillis();
		for (int i = 0; i < activeImeiList.size(); i++) {
			int number1 = 0, number2 = 0, number3 = 0, number4 = 0, number5 = 0, number6 = 0, number7 = 0, number8 = 0, number9 = 0, number10 = 0, number11 = 0, number12 = 0, number13 = 0, number14 = 0, number15 = 0, number16 = 0, number17 = 0, number18 = 0, number19 = 0, number20 = 0, number21 = 0, number22 = 0, number23 = 0, number24 = 0;
			int numberList[] = { number1, number2, number3, number4, number5, number6, number7, number8, number9, number10, number11, number12, number13, number14, number15, number16, number17, number18,
					number19, number20, number21, number22, number23, number24 };
			String imei = activeImeiList.get(i);
			String travel = test.transTravelDrive(imei);
			//获取激活地域信息；
			String region=String.valueOf(dao.singleGetDiverRegion(imei).get(0).get("c_region_code"));
			//所属分组；
			String group=String.valueOf(dao.basicQueryByString("iov_administrator_group_imei", "c_imei=?", imei).get(0).get("c_group_id"));
			List<Map<String, Object>> listNumber = dao.diverRuleTime1("allcode", imei, travel);
			for (int j = 0; j < 24; j++) {
				for (Map<String, Object> timeMap : listNumber) {
					
					if(timeMap.get("t_st_time")!=null&&timeMap.get("t_ed_time")!=null)
					{
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
							if (hours1 == (j + 1) || hours2 == (j + 1)) {
								numberList[j] = numberList[j] + 1;
								break;
							}	
					}
				}
			}
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("c_imei", imei);
			map.put("c_region", region);
			map.put("c_group", group);
			for(int k=0;k<24;k++)
			{
				map.put("c_time"+(k+1), numberList[k]);	
			}
	      dao.basicInsertData("iov_group_ruletime", map);
		}
//	      long date2=System.currentTimeMillis();
//		  System.out.println("time is ----"+(date2-date1));
	}
}
