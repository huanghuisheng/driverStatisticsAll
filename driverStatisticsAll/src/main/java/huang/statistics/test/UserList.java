package huang.statistics.test;

import huang.statistics.dao.BasicDao;
import huang.statistics.dao.StatisticsDaoImpl;
import huang.statistics.util.DateUtil;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

public class UserList {
	public String transTravelDrive(String imei) {
		imei = imei.substring(13);
		String travel = "iov_travel_" + Integer.valueOf(imei) % 10;
		return travel;
	}

	// 身份判断；
	public String judgmentIdentity(List<Map<String, Object>> travelList) throws Exception {
		int number1 = 0, number2 = 0, number3 = 0, number4 = 0;
		for (Map<String, Object> timeMap : travelList) {
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
		if(travelList.size()==0)
		{
			number4++;
		}
		
		
		int[] intArray = new int[] { number1, number2, number3, number4 };
		Arrays.sort(intArray);

		if (intArray[intArray.length - 1] == number1) {
			return "货车司机";
		} else if (intArray[intArray.length - 1] == number2) {
			return "上班族";
		} else if (intArray[intArray.length - 1] == number3) {
			return "出租司机";
		} else if (intArray[intArray.length - 1] == number4) {
			return "其他";
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		StatisticsDaoImpl firstAccountDaoImpl = new StatisticsDaoImpl();
		BasicDao dao = new BasicDao();
		UserList userList = new UserList();
		// 清空数据；
		dao.clearTableData("iov_statistics_userlist");
		List<Map<String, Object>> list = dao.basicQueryALL("iov_device_client");
		System.out.println("listImei is ---" + list.size());

		Date date = new Date();

		for (Map<String, Object> map : list) {
			String imei = String.valueOf(map.get("c_imei"));
			System.out.println("----imei------" + imei);

			String travel = userList.transTravelDrive(imei);
			Map<String, Object> userMap = new HashMap<String, Object>();
			userMap.put("c_department", map.get("c_group"));
			userMap.put("c_user_name", map.get("c_client_name"));
			userMap.put("c_imei", map.get("c_imei"));
			userMap.put("c_device_name", "智能后视镜，v 1.2.6");
			// 获取激活时间；
			List<Map<String, Object>> time = dao.getCreateTime(imei);
			Object createTime = time.get(0).get("t_create_time");
			userMap.put("t_create_time", createTime);

			// 获取驾驶行为；
			String timeYear[] = { "week", "month1", "month3", "year" };
			for (int i = 0; i < timeYear.length; i++) {
				// 一年；
				List<Date> listDate = DateUtil.stringToDateTimeFlag(timeYear[i]);
				Date time1 = listDate.get(0);
				Date time2 = listDate.get(1);
				// c_driver
				List<Map<String, Object>> travelList = firstAccountDaoImpl.singleGetDiverTravel(travel, imei, time1, time2);
				int turn = 0, collide = 0, speed_down = 0, speed_up = 0, fatigue = 0;
				float grade = 0;
				float path = 0;
				float timeTravel = 0;
				for (Map<String, Object> mapTravel : travelList) {
					path = path + Float.valueOf(String.valueOf(mapTravel.get("n_distance")));
					Object driver = mapTravel.get("c_driver");
					JSONObject jsonObject = JSONObject.fromObject(driver);
					turn = turn + Integer.valueOf(String.valueOf(jsonObject.get("turn")));
					timeTravel = timeTravel + Float.valueOf(String.valueOf(jsonObject.get("time")));
					collide = collide + Integer.valueOf(String.valueOf(jsonObject.get("collide")));
					speed_down = speed_down + Integer.valueOf(String.valueOf(jsonObject.get("speed_down")));
					speed_up = speed_up + Integer.valueOf(String.valueOf(jsonObject.get("speed_up")));
					fatigue = fatigue + Integer.valueOf(String.valueOf(jsonObject.get("fatigue")));
					grade = grade + Float.valueOf(String.valueOf(jsonObject.get("grade")));
				}

				if (travelList.size() > 0) {
					grade = grade / travelList.size();
					BigDecimal bg1 = new BigDecimal(grade);
					grade = bg1.setScale(3, BigDecimal.ROUND_HALF_UP).floatValue();
				}else{
					grade=100;
				}
				
				userMap.put("c_trun", turn);
				userMap.put("c_total_mileage", path);
				userMap.put("c_collide", collide);
				userMap.put("c_speed_up", speed_up);
				userMap.put("c_fatigue", fatigue);
				userMap.put("c_total_time", timeTravel);
				userMap.put("c_grade", grade);
				userMap.put("t_statistics_time", timeYear[i]);
				String identity = userList.judgmentIdentity(travelList);
				userMap.put("c_user_identity", identity);
				dao.basicInsertData("iov_statistics_userlist", userMap);

			}
		}

	}

}
