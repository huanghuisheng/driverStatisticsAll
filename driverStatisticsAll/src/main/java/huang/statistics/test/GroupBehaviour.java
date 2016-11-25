package huang.statistics.test;

import huang.statistics.dao.BasicDao;
import huang.statistics.dao.StatisticsDaoImpl;
import huang.statistics.util.DateUtil;
import huang.statistics.util.UserAccount;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

public class GroupBehaviour {
	//-----------------------------------------------转换到路径表
	public String transTravelDrive(String imei) {
		imei = imei.substring(13);
		String travel = "iov_travel_" + Integer.valueOf(imei) % 10;
		return travel;
	}
	public static void main(String[] args) throws Throwable {
		GroupBehaviour test = new GroupBehaviour();
		BasicDao dao = new BasicDao();
		StatisticsDaoImpl statisticsDaoImpl =new StatisticsDaoImpl();
		//清空表数据；
		dao.clearTableData("iov_group_behaviour");
		String [] time={"week","month1","month3","year"};
		for(int k=0;k<time.length;k++)
		{
			List<Date> listDate = DateUtil.stringToDateTimeFlag(time[k]);
			Date dateTime1 = listDate.get(0);
			Date dataTime2 = listDate.get(1);
			// 所有的用户
			List<String> activeImeiList = UserAccount.activeGetListImei1();
			
			for (int i = 0; i <activeImeiList.size(); i++) {
				String imei = activeImeiList.get(i);
				System.out.println("-----"+imei);
				String travel = test.transTravelDrive(imei);
				//获取激活地域信息；
				String region=String.valueOf(dao.singleGetDiverRegion(imei).get(0).get("c_region_code"));
				//所属分组；
				String group=String.valueOf(dao.basicQueryByString("iov_administrator_group_imei", "c_imei=?", imei).get(0).get("c_group_id"));
				
				// 单个用户驾驶行为评分
				float userGradeSum = 0;
				int userTurnSum=0;
				int userCollideSum=0;
				int userSpeedDownSum=0;
				int userSpeedUpSum=0;
				int userFatigueSum=0;
				
				// 单个用户驾驶行为平均分
				float userGrade = 0;
				// 获取单个用户所有的轨迹的驾驶行为；
				List<Map<String, Object>> listBehavior = statisticsDaoImpl.singleGetDiverBehavior(travel, imei, dateTime1, dataTime2, "allcode");
				for (int j = 0; j < listBehavior.size(); j++) {
					String driver = String.valueOf(listBehavior.get(j).get("c_driver"));
					JSONObject jsonObject = JSONObject.fromObject(driver);
					//评分
					float userGrade1 = Float.valueOf(jsonObject.getString("grade"));
					userGradeSum = userGradeSum + userGrade1;
				//	{"turn":0,"time":10.946,"grade":100,"collide":0,"speed_down":0,"speed_up":0,"fatigue":1	
					int userTurn=Integer.valueOf(jsonObject.getString("turn"));
					int userCollide=Integer.valueOf(jsonObject.getString("collide"));
					int userSpeedDown=Integer.valueOf(jsonObject.getString("speed_down"));
					int userSpeedUp=Integer.valueOf(jsonObject.getString("speed_up"));
					int userFatigue=Integer.valueOf(jsonObject.getString("fatigue"));
					 userTurnSum+=userTurn;
					 userCollideSum+=userCollide;
					 userSpeedDownSum+=userSpeedDown;
					 userSpeedUpSum+=userSpeedUp;
					 userFatigueSum+=userFatigue;
				}
				if (listBehavior.size() > 0) {
					userGrade = userGradeSum / listBehavior.size();
				}
				// 驾驶行为；
				Map<String, Object> behavier = new HashMap<String,Object>();

				behavier.put("turn", userTurnSum);
				behavier.put("collide", userCollideSum);
				behavier.put("speed_down", userSpeedDownSum);
				behavier.put("speed_up", userSpeedUpSum);
				behavier.put("fatigue", userFatigueSum);
				behavier.put("grade", userGrade);
				
				JSONObject jsonObject = JSONObject.fromObject(behavier);
				Map<String, Object> driver = new HashMap<String, Object>();
				driver.put("c_driver", jsonObject.toString());
				driver.put("c_imei", imei);
				driver.put("c_time", time[k]);
				driver.put("c_region",region);
				driver.put("c_group",group);
				System.out.println("----"+driver);
				dao.basicInsertData("iov_group_behaviour", driver);	
			}
		}
		
		
		
		
		
		
	

		
		
		
	}	

}
