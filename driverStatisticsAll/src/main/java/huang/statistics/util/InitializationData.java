package huang.statistics.util;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InitializationData {

	public InitializationQueryData initializationQueryData = null;

	private InitializationData() {

		initializationQueryData = InitializationQueryData.getInstance();
	}

	public static InitializationData getInstance() {
		return Data.instance;
	}

	// 在第一次被引用时被加载
	static class Data {
		private static InitializationData instance = new InitializationData();
	}
	
	
	//在线设备坐标，每5分钟刷新一次
	public void insertData() throws Exception{
		
		initializationQueryData.clearData2();
		
		Date day1=DateUtil.dateToDateMinute().get(0);
		
		Date day2=DateUtil.dateToDateMinute().get(1);
		
		List<Map<String, Object>> list=initializationQueryData.queryListMap(day1, day2);
		
		filterData1(list,"iov_analyzer_dev_time");
		
	}
	
	public void insertData1() throws Exception {
		Date today = new Date();
//		String day1="2016-09-06";
//		
//		
//		Date today = DateUtil.StringToDateStart(day1, "yyyy-MM-dd");
		
//		 List<Map<String, Object>> data2 = initializationQueryData.queryList5();
//		 if (null != data2 || data2.size() != 0) {
		 //清除数据；
			 initializationQueryData.clearData();
		// }
	

		List<Date> pastDaySumList = DateUtil.dateToPastSumDay(today, 7);

		Date[] pastDay = new Date[7];
		// 从当天时间到过去时间
		Date[] pastDayList = new Date[14];
		for (int i = 0; i < pastDaySumList.size(); i++) {
			pastDay[i] = pastDaySumList.get(i);
			List<Date> todayList = DateUtil.dateToTodayDate(pastDay[i]);
			pastDayList[i * 2] = todayList.get(0);
			pastDayList[i * 2 + 1] = todayList.get(1);

		}
		// 从当前到过去7天时间
		Date pastDayDate0 = pastDayList[0];
		Date pastDayDate00 = pastDayList[1];
		Date pastDayDate1 = pastDayList[2];
		Date pastDayDate11 = pastDayList[3];
		Date pastDayDate2 = pastDayList[4];
		Date pastDayDate22 = pastDayList[5];
		Date pastDayDate3 = pastDayList[6];
		Date pastDayDate33 = pastDayList[7];
		Date pastDayDate4 = pastDayList[8];
		Date pastDayDate44 = pastDayList[9];
		Date pastDayDate5 = pastDayList[10];
		Date pastDayDate55 = pastDayList[11];
		Date pastDayDate6 = pastDayList[12];
		Date pastDayDate66 = pastDayList[13];

		// // 上个月 ，上个季度，上一年；
		// List<Date> pastmonthList = DateUtil.dateToPastSumMonth(today);
		// Date pastmonth1 = pastmonthList.get(1);
		// Date pastmonth3 = pastmonthList.get(2);
		// Date pastmonth12 = pastmonthList.get(3);
		//
		// // 过去七天时间
		// Date pastDate7 = DateUtil.dateToPastDay(today, 7);
		//
		// Date pastDate30 = DateUtil.dateToPastDay(today, 30);
		//
		// Date pastDate60 = DateUtil.dateToPastDay(today, 60);

		// 累计用户数据-字段；
		String[] total = { "n_total_0", "n_total_1", "n_total_2", "n_total_3", "n_total_4", "n_total_5", "n_total_6", };
		for (int k = 0; k < total.length; k++) {

			List<Map<String, Object>> cumulativeList = initializationQueryData
					.cumulativeQueryList(pastDayList[k * 2 + 1]);

			filterData(cumulativeList, "iov_analyzer_dev_daily", total[k]);

		}

		// 新增用户数据-字段；
		String[] newUser = { "n_new_0", "n_new_1", "n_new_2", "n_new_3", "n_new_4", "n_new_5", "n_new_6" };

		for (int k = 0; k < newUser.length; k++) {

			List<Map<String, Object>> increasedList = initializationQueryData.queryIncreasedList(pastDayList[k * 2],
					pastDayList[k * 2 + 1]);
			filterData(increasedList, "iov_analyzer_dev_daily", newUser[k]);

		}

		// 活跃用户数据-字段；
		String[] activeUser = { "n_uv_0", "n_uv_1", "n_uv_2", "n_uv_3", "n_uv_4", "n_uv_5", "n_uv_6" };

		for (int k = 0; k < newUser.length; k++) {

			List<Map<String, Object>> activeList = initializationQueryData.queryActiveData(pastDayList[k * 2],
					pastDayList[k * 2 + 1]);

			System.out.println("新增用户------------" + activeList.size());

			filterData(activeList, "iov_analyzer_dev_daily", activeUser[k]);
		}

		String totalMonth[] = { "n_total1", "n_total3", "n_total12" };

		// 上个月 ，上个季度，上一年；
		List<Date> pastmonthList = DateUtil.dateToPastSumMonth(today);
		Date pastmonth1 = pastmonthList.get(1);
		Date pastmonth3 = pastmonthList.get(2);
		Date pastmonth12 = pastmonthList.get(3);

		for (int i = 1; i < pastmonthList.size(); i++) {
			List<Map<String, Object>> cumulativeList2 = initializationQueryData
					.cumulativeQueryList(pastmonthList.get(i));
			filterData(cumulativeList2, "iov_analyzer_dev_daily", totalMonth[i - 1]);
		}

		// 过去七天时间
		Date pastDate7 = DateUtil.dateToPastDay(today, 7);

		Date pastDate30 = DateUtil.dateToPastDay(today, 30);

		Date pastDate60 = DateUtil.dateToPastDay(today, 60);

		// 过去7天新增数据；
		List<Map<String, Object>> increasedList2 = initializationQueryData.queryIncreasedList(pastDate7,
				pastDayDate00);
		filterData(increasedList2, "iov_analyzer_dev_daily", "n_new7");

		// 过去30新增数据；
		List<Map<String, Object>> increasedList3 = initializationQueryData.queryIncreasedList(pastDate30,
				pastDayDate00);
		filterData(increasedList3, "iov_analyzer_dev_daily", "n_new30");

		// 过去7天
		List<Map<String, Object>> activeDataList7 = initializationQueryData.queryActiveData(pastDate7,
				pastDayDate00);
		filterData(activeDataList7, "iov_analyzer_dev_daily", "n_uv7");

		// 30天
		List<Map<String, Object>> activeDataList30 = initializationQueryData.queryActiveData(pastDate30,
				pastDayDate00);
		System.out.println("过去活跃人数------------"+pastDate30);
		System.out.println("过去活跃人数------------"+pastDate30);
		filterData(activeDataList30, "iov_analyzer_dev_daily", "n_uv30");

	}
	
	
	
	//----------------------------------------------------------------地区分布初始化话
	public void insertData2() throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
		Map<String, Object> map2 = new HashMap<String, Object>();
		
		String starttime1="00.00.00 00:00:00";
		
		Date starttime2 = null;
		Date endtime2 = new Date();
		
//         endtime2=DateUtil.StringToDateEnd(endtime1, "yyyy.MM.dd HH:mm:ss");
		starttime2=DateUtil.StringToDateStart(starttime1, "yyyy.MM.dd HH:mm:ss");
		
		// 创建一个数值格式化对象
		NumberFormat numberFormat = NumberFormat.getInstance();
		// 设置精确到小数点后2位
		numberFormat.setMaximumFractionDigits(2);
		String area[] = { "北京", "天津", "河北", "山西", "内蒙古", "辽宁", "吉林", "黑龙江", "上海",
				"江苏", "浙江", "安徽", "福建", "江西", "山东", "河南", "湖北", "湖南", "广东", "广西",
				"海南", "重庆", "四川", "贵州", "云南", "西藏", "陕西", "甘肃", "青海", "宁夏", "新疆", 
				"台湾", "香港", "澳门" };
		int area1[] = { 11, 12, 13, 14, 15, 21, 22, 23, 31, 32, 33, 34, 35, 36, 37, 41, 42,
				 43, 44, 45, 46, 50, 51, 52, 53, 54, 61, 62, 63, 64, 65, 71, 81,82};
		
//		String flag = null;
//		list = initializationQueryData.areaDistributionList(starttime2, endtime2, flag);
//		map2.put("total", list.get(0).get("number"));
//		float total = Float.parseFloat(list.get(0).get("number").toString());
	

		for (int i = 0; i < area.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			list = initializationQueryData.areaDistributionList(starttime2, endtime2,
					area1[i] + "%");
			map.put("number", list.get(0).get("number"));
			
			map.put("city", i+1);
			list1.add(map);
		
		}
		initializationQueryData.clearDataArea();
	
		filterData3(list1,"iov_analyzer_dev_area");
		
	}
	
	//------------------------------------------------------------------------活跃地区初始化话；
	public void insertData3() throws Exception {
		List<Map<String, Object>> list= new ArrayList<Map<String, Object>>();
		List <Map<String, Object>> list1=new ArrayList<Map<String, Object>>();	
		Map<String, Object> map2 = new HashMap<String, Object>();
		String starttime1="00.00.00 00:00:00";
		
		Date starttime2 = null;
		Date endtime2 = new Date();
	   /* endtime2=DateUtil.StringToDateEnd(endtime1, "yyyy.MM.dd HH:mm:ss");*/
		starttime2=DateUtil.StringToDateStart(starttime1, "yyyy.MM.dd HH:mm:ss");
		// 创建一个数值格式化对象
		NumberFormat numberFormat = NumberFormat.getInstance();
		// 设置精确到小数点后2位
		numberFormat.setMaximumFractionDigits(2);
		String area[] = { "北京", "天津", "河北", "山西", "内蒙古", "辽宁", "吉林", "黑龙江", "上海",
				"江苏", "浙江", "安徽", "福建", "江西", "山东", "河南", "湖北", "湖南", "广东", "广西",
				"海南", "重庆", "四川", "贵州", "云南", "西藏", "陕西", "甘肃", "青海", "宁夏", "新疆", 
				"台湾", "香港", "澳门" };
		int area1[] = { 11, 12, 13, 14, 15, 21, 22, 23, 31, 32, 33, 34, 35, 36, 37, 41, 42,
				 43, 44, 45, 46, 50, 51, 52, 53, 54, 61, 62, 63, 64, 65, 71, 81,82};
		String flag = null;
		// 拿到活跃数据
		for (int i = 0; i < area.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			list = initializationQueryData.activeAreaDistributionList(starttime2, endtime2,
					area1[i] + "%");
			map.put("number", list.get(0).get("number"));
			map.put("city", i+1);
			list1.add(map);
		}
		// 计算剩余的地区百分比

		
		System.out.println("shujsssss----------"+list1);
		initializationQueryData.clearDataActive();
	
		filterData3(list1,"iov_analyzer_dev_active");
	}
	

	
	// 过滤3 地区分布
		public void filterData3(List<Map<String, Object>> list, String table) throws Exception {
			Map<String, Object> map2 = new HashMap<String, Object>();
			for (int i = 0; i < list.size(); i++) {
				String flag = String.valueOf(list.get(i).get("city"));
		
				int number = Integer.parseInt(String.valueOf(list.get(i).get("number")));
		

					map2.put("n_city_"+flag, number);
					//initializationQueryData.insertForRet(table, map2);	
			}
			
			System.out.println("-----------------------s----------------" + map2+table );
			
			
			initializationQueryData.insertForRet(table, map2);		
		}
	
	
	
	
	
	

	// 过滤
	public void filterData(List<Map<String, Object>> list, String table, String flag) throws Exception {

		for (int i = 0; i < list.size(); i++) {
			String c_region_code = String.valueOf(list.get(i).get("c_region_code"));
			String c_model = String.valueOf(list.get(i).get("c_model"));
			String c_build_time = String.valueOf(list.get(i).get("c_build_time"));
			int number = Integer.parseInt(String.valueOf(list.get(i).get("number")));
			List<Map<String, Object>> data1 = initializationQueryData.queryRecordList(c_region_code, c_model,
					c_build_time);

			if (null == data1 || data1.size() == 0) {
				Map<String, Object> map2 = new HashMap<String, Object>();

				map2.put("c_region_code", c_region_code);
				map2.put("c_model", c_model);
				map2.put("c_build_time", c_build_time);
				map2.put(flag, number);

				System.out.println("---------------------------------------" + map2);

				initializationQueryData.insertForRet(table, map2);

			} else {
				// 更新；

				System.out.println("--------------------更新-------------------");
				initializationQueryData.queryUpdateList(number, flag, c_model, c_build_time, c_region_code);
			}
		}
	}
	
	//过滤2;
	public void filterData1(List<Map<String, Object>> list, String table) throws Exception {

		for (int i = 0; i < list.size(); i++) {
			String c_imei = String.valueOf(list.get(i).get("c_imei"));
			String c_model = String.valueOf(list.get(i).get("c_model"));
			String c_build_time = String.valueOf(list.get(i).get("c_build_time"));
			//c_region_code
			String c_region_code = String.valueOf(list.get(i).get("c_region_code"));
			
			String n_lng = String.valueOf(list.get(i).get("n_lng"));
			String n_lat = String.valueOf(list.get(i).get("n_lat"));
		
	
//			List<Map<String, Object>> data1 = initializationQueryData.queryOnlineRecordList(c_imei, c_model);
//
//			if (null == data1 || data1.size() == 0) {
				
			Map<String, Object> map2 = new HashMap<String, Object>();
				map2.put("c_imei", c_imei);
				map2.put("c_model", c_model);
				map2.put("c_build_time", c_build_time);
				map2.put("n_lng", n_lng);
				map2.put("n_lat", n_lat);
				map2.put("c_region_code", c_region_code);
				
				
				

				System.out.println("-------------------hhh--------------------" + map2);

				initializationQueryData.insertForRet(table, map2);

//			} else {
//				// 更新；
//
//				System.out.println("--------------------更新-------------------");
//				initializationQueryData.queryOnlineUpdateList(c_imei, c_model, c_build_time, n_lng, n_lat);
//			}
		}
	}
	
	
	
	
	
	
	
	
	
	
}
