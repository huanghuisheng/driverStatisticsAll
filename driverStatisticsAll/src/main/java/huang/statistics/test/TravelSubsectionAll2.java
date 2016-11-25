package huang.statistics.test;

import huang.statistics.dao.BasicDao;
import huang.statistics.dao.StatisticsDaoImpl;
import huang.statistics.util.DateUtil;
import huang.statistics.util.GetTrack;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.broadsense.commons.utils.CoordinateUtil;
import com.broadsense.iov.base.data.entity.TravelEntity;
import com.broadsense.iov.lbs.LbsService;
import com.broadsense.iov.lbs.Precision;
import com.broadsense.iov.lbs.pojo.GPSLacBean;
import com.tonetime.commons.exception.ServiceException;
public class TravelSubsectionAll2 {
	
	//时间表
	public String transDrive(String imei) {
		imei = imei.substring(13);
		String event = "iov_event_" + Integer.valueOf(imei) % 20;
		return event;
	}
	
	//路程表
	public String travelCoding(String imei) {
		imei = imei.substring(13);
		String travel = "iov_travel_" + Integer.valueOf(imei) % 10;
		return travel;
	}
	//轨迹表
	public String transCoding(String imei) {
		imei = imei.substring(13);
		String track = "iov_track_" + Integer.valueOf(imei) % 100;
		return track;
	}
	//转换地址；
	public String convertRegion(double lng,double lat) throws Throwable  {
		GPSLacBean region = LbsService.getInstance().getGPSTrack(lng,lat, Precision.FULL);
		 if (region != null) {
	        String re=region.getRegionCode();
	        return re;
	    }
		 return null;
	}
	//计算路程；
	public double singleDeviceDriveSingleTrave(Date startDate, Date endDate, String imei, String traveFlag, String model) throws Exception {
		// 行驶里程，总耗时，平均时速；
		StatisticsDaoImpl firstAccountDao = new StatisticsDaoImpl();
		//List<Map<String, Object>> trackList = firstAccountDao.trackUser(model, imei, startDate, endDate, traveFlag);
		
		List<Map<String, Object>> trackList = firstAccountDao.trackUser(model, imei, startDate, endDate, traveFlag,"2");
		if(trackList.size()==0)
		{
			trackList = firstAccountDao.trackUser(model, imei, startDate, endDate, traveFlag,"1");
		}
		
		double path = 0;
		for (int j = 0; j < trackList.size() - 1; j++) {
			double startLng = Double.valueOf(String.valueOf(trackList.get(j).get("lng")));
			double endtLat = Double.valueOf(String.valueOf(trackList.get(j).get("lat")));
			double startLng1 = Double.valueOf(String.valueOf(trackList.get(j + 1).get("lng")));
			double endtLat1 = Double.valueOf(String.valueOf(trackList.get(j + 1).get("lat")));
			path = path + CoordinateUtil.getDistance(startLng, endtLat, startLng1, endtLat1);
		}
		path = path / 1000;
		BigDecimal bg1 = new BigDecimal(path);
		path = bg1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return path;

	}
	//驾驶因素；
		public Map<String, Object> driveUser2(String imei, Date startTime, Date endTime) throws Exception {
			StatisticsDaoImpl firstAccountDao = new StatisticsDaoImpl();
			String flag = transDrive(imei);
			List<Map<String, Object>> list1 = firstAccountDao.driveUser1(flag, imei, startTime, endTime);
			int number1 = Integer.valueOf(String.valueOf(list1.get(0).get("number")));
			List<Map<String, Object>> list2 = firstAccountDao.driveUser2(flag, imei, startTime, endTime);
			int number2 = Integer.valueOf(String.valueOf(list2.get(0).get("number")));
			List<Map<String, Object>> list3 = firstAccountDao.driveUser3(flag, imei, startTime, endTime);
			int number3 = Integer.valueOf(String.valueOf(list3.get(0).get("number")));
			List<Map<String, Object>> list4 = firstAccountDao.driveUser4(flag, imei, startTime, endTime);
			int number4 = Integer.valueOf(String.valueOf(list4.get(0).get("number")));
			float time = endTime.getTime() - startTime.getTime();
			float minute = time / (1000 * 60 * 60);
			int fatigue = 0;
			if (minute > 2) {
				fatigue = 1;
			} else {
				fatigue = 0;
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("speed_down", number1);
			map.put("speed_up", number2);
			map.put("turn", number3);
			map.put("collide", number4);
			map.put("fatigue", fatigue);
			return map;
		}
		
		//驾驶评分
		public int driveUser1(String imei, Date startTime, Date endTime) throws Exception {
			StatisticsDaoImpl firstAccountDao = new StatisticsDaoImpl();
			String flag = transDrive(imei);
			// 相差多小分钟；
			float time = endTime.getTime() - startTime.getTime();
			float minute = time / (1000 * 60);
			int minute1 = (int) minute;
			int goodDrade = 0;
			// 总分；
			int sumGrade = 80;
			for (int i = 0; i < minute1; i++) {
				// datetoDateOneMinute
				List<Date> listDate = DateUtil.datetoDateOneMinute(startTime);
				Date minute11 = listDate.get(0);
				Date minute22 = listDate.get(1);
				List<Map<String, Object>> list5 = firstAccountDao.driveUser5(flag, imei, minute11, minute22);
				if (list5.size() == 0) {
					goodDrade = 1;
				} else {
					goodDrade = 0;
				}
				List<Map<String, Object>> list1 = firstAccountDao.driveUser1(flag, imei, minute11, minute22);
				int number1 = Integer.valueOf(String.valueOf(list1.get(0).get("number")));
				List<Map<String, Object>> list2 = firstAccountDao.driveUser2(flag, imei, minute11, minute22);
				int number2 = Integer.valueOf(String.valueOf(list2.get(0).get("number")));
				List<Map<String, Object>> list3 = firstAccountDao.driveUser3(flag, imei, minute11, minute22);
				int number3 = Integer.valueOf(String.valueOf(list3.get(0).get("number")));
				List<Map<String, Object>> list4 = firstAccountDao.driveUser4(flag, imei, minute11, minute22);
				int number4 = Integer.valueOf(String.valueOf(list4.get(0).get("number")));
				sumGrade = sumGrade - (number1 + number2) * 4 - number3 * 5 + goodDrade;
			}
			if (sumGrade < 0) {
				System.out.println("imei is ------------" + imei);
				sumGrade = 0;
			} else if (sumGrade > 100) {
				sumGrade = 100;
			}
			return sumGrade;
		}
	//已经激活的IMEI号；
	@SuppressWarnings("static-access")
	public static List<String> activeGetListImei() throws Exception
	{
		List<Map<String, Object>> listImei = new ArrayList<Map<String, Object>>();
		List<String> activelistImei = new ArrayList<String>();
		//获取用户的所拥有的imei号
		BasicDao basicDao=new BasicDao();
		listImei=basicDao.userGetMobileImei("1",null);
		System.out.println("---list is --"+listImei);
		
		//获取已经激活的imei号
		for(int i=0;i<listImei.size();i++)
		{
			String imei=String.valueOf(listImei.get(i).get("c_imei"));
			List<Map<String, Object>> list1=BasicDao.userGetActiveMobileImei(imei);
			//if(list1.size()>0){
				activelistImei.add(imei);
			//}
		}
		return activelistImei;
	}
	public void travelSubsectionList() throws Throwable {
		List<Thread> threads = new ArrayList<Thread>();
		final BasicDao basicDao=new BasicDao();
/*		Date today = new Date();
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(today);
	    rightNow.add(Calendar.MONTH, -1);
	    Date day = rightNow.getTime();
	    
	    List<Date> list=DateUtil.dateToTodayDate(day);
	    Date time11=list.get(0);
	    Date time22=list.get(1);*/
		
		final Date time11=DateUtil.StringToDateStart("2016-01-01","yyyy-MM-dd HH:mm:ss");
		final Date time22=DateUtil.StringToDateEnd("2016-12-31", "yyyy-MM-dd HH:mm:ss");
		
	    //获取activeIMEI；
	    List<String> listImei=activeGetListImei();
	  System.out.println("--ative------"+listImei.size());
	    
	    for(int i=0;i<listImei.size();i++)
	    {
	    	final String imei1=listImei.get(i);
	    	Thread t = new Thread(new Runnable() {
				public void run() {
					try {
						String imei=imei1;
						List<TravelEntity> resultList = GetTrack.queryTravel(time11, time22, imei, "R611");
						String travel =travelCoding(imei);
						String track=transCoding(imei);
						System.out.println("imei is --------" + imei);
						for (int j = 0; j < resultList.size(); j++) {
							System.out.println(  "imei is"+imei+   "travel " + j + " is --------" + resultList.get(j));
							//时间；
							Date time1=resultList.get(j).getStTime();
							Date time2=resultList.get(j).getEdTime();
							// 插入轨迹段
							 Map<String,Object> map=new HashMap<String,Object>();
							 map.put("c_model", resultList.get(j).getModel());
							 map.put("c_imei", resultList.get(j).getImei());
							 map.put("t_st_time", time1);
							 map.put("t_ed_time", time2);
							 map.put("n_st_lng", resultList.get(j).getStLng());
							 map.put("n_st_lat", resultList.get(j).getStLat());
							 map.put("n_ed_lng", resultList.get(j).getEdlng());
							 map.put("n_ed_lat", resultList.get(j).getEdLat());
							 
							 //转换地址；
							 String c_st_region=convertRegion(resultList.get(j).getStLng(),resultList.get(j).getStLat());
							 String c_ed_region=convertRegion(resultList.get(j).getEdlng(),resultList.get(j).getEdLat());
							 map.put("c_st_region",c_st_region);
							 map.put("c_ed_region",c_ed_region);
							if(time1!=null&&time2!=null) 
							{
								 //计算路程
								double path =singleDeviceDriveSingleTrave(time1, time2, imei, track, "R611");
								//System.out.println("path is --" + path); 
								 
								 //计算平均速度；
								float time = time2.getTime() - time1.getTime();
								float minute = time / (1000 * 60 * 60);
								double speed = path / minute;
								System.out.println("------------------a1aaa"+path);
								System.out.println("------------------a2aaa"+minute);
								System.out.println("------------------a3aaa"+speed);
								BigDecimal bg1 = new BigDecimal(speed);
								speed = bg1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
								
								System.out.println("------------------ssss"+speed);
								map.put("n_speed", speed);
								map.put("n_distance", path * 1000);
						       
								//计算驾驶评分；
								int grade = driveUser1(imei, time1, time2);
								//驾驶时间
								BigDecimal bg2 = new BigDecimal(minute);
								minute = bg2.setScale(3, BigDecimal.ROUND_HALF_UP).floatValue();
								//驾驶因素；
								Map<String, Object> behavier =driveUser2(imei, time1, time2);
								behavier.put("grade", grade);
								behavier.put("time", minute);
								JSONObject jsonObject = JSONObject.fromObject(behavier);
								map.put("c_driver", jsonObject.toString());
								System.out.println("----"+map);
								basicDao.basicInsertData(travel, map); 
							}
							 
						}
						
					} catch (NumberFormatException e) {

						e.printStackTrace();
					} catch (Exception e) {

						e.printStackTrace();
					} catch (Throwable e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			t.start();

			threads.add(t);
	    	int a=(i+1)%10;
	    	System.out.println("进行第几个"+a);
	    	//阻塞线程
			if((i+1)%10==0){
				for (Thread iThread : threads) {
					try {
						// 等待所有线程执行完毕
						iThread.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}	
			}
		
	    	
	    	
	    	
	    	
	    	
	    	
	    
			
	    }
	}
	public static void main(String[] args) throws Throwable{
		TravelSubsectionAll travelSubsection =new TravelSubsectionAll();
		travelSubsection.travelSubsectionList();
	}
	
	
	
	
	
	
}
