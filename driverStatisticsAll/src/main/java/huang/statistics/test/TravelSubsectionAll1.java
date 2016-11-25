package huang.statistics.test;

import huang.statistics.dao.BasicDao;
import huang.statistics.dao.StatisticsDaoImpl;
import huang.statistics.util.DateUtil;
import huang.statistics.util.GetTrack;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.ThreadPoolExecutor.DiscardPolicy;
import java.util.concurrent.TimeUnit;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.broadsense.commons.utils.CoordinateUtil;
import com.broadsense.iov.base.data.entity.TravelEntity;
import com.broadsense.iov.lbs.LbsService;
import com.broadsense.iov.lbs.Precision;
import com.broadsense.iov.lbs.pojo.GPSLacBean;
public class TravelSubsectionAll1 {
	private static Logger logger = Logger.getLogger(TravelSubsectionAll1.class);
	
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
			//List<Map<String, Object>> list1=BasicDao.userGetActiveMobileImei(imei);
			//if(list1.size()>0){
				activelistImei.add(imei);
			//}
		}
		return activelistImei;
	}
	
	  //创建线程；
    //线程池维护线程的最少数量    
    private static final int COREPOOLSIZE = 3;    
    //线程池维护线程的最大数量    
    private static final int MAXINUMPOOLSIZE = 7;    
    //线程池维护线程所允许的空闲时间    
    private static final long KEEPALIVETIME = 4;    
    //线程池维护线程所允许的空闲时间的单位    
    private static final TimeUnit UNIT = TimeUnit.SECONDS;    
    //线程池所使用的缓冲队列,这里队列大小为3    
    private static final BlockingQueue<Runnable> WORKQUEUE = new ArrayBlockingQueue<Runnable>(500);    
    //线程池对拒绝任务的处理策略：AbortPolicy为抛出异常；CallerRunsPolicy为重试添加当前的任务，他会自动重复调用execute()方法；DiscardOldestPolicy为抛弃旧的任务，DiscardPolicy为抛弃当前的任务    
   // private static final AbortPolicy HANDLER = new ThreadPoolExecutor.AbortPolicy();   
    private static final AbortPolicy HANDLER = new ThreadPoolExecutor.AbortPolicy();
    
	public void travelSubsectionList() throws Throwable {
		Date time11=DateUtil.StringToDateStart("2016-01-01","yyyy-MM-dd HH:mm:ss");
		Date time22=DateUtil.StringToDateEnd("2016-12-31", "yyyy-MM-dd HH:mm:ss");
	    //获取activeIMEI；
	    List<String> listImei=activeGetListImei();
	    System.out.println("--ative------"+listImei.size());
	    ThreadPoolExecutor threadPool = new ThreadPoolExecutor(COREPOOLSIZE, MAXINUMPOOLSIZE, KEEPALIVETIME, UNIT, WORKQUEUE, HANDLER);    
	    for (int i = 1; i < listImei.size(); i++) {    
            String task = "task@"+i;    
            System.out.println("put->"+task); 
            Date time=new Date();
            logger.error(time+"imei is "+listImei.get(i));
            
            //一个任务通过 execute(Runnable)方法被添加到线程池，任务就是一个 Runnable类型的对象，任务的执行方法就是 Runnable类型对象的run()方法    
            //处理任务的优先级为：核心线程corePoolSize、任务队列workQueue、最大线程maximumPoolSize，如果三者都满了，使用handler处理被拒绝的任务    
            //设此时线程池中的数量为currentPoolSize,若currentPoolSize>corePoolSize,则创建新的线程执行被添加的任务,    
            //当corePoolSize+workQueue>currentPoolSize>=corePoolSize,新增任务被放入缓冲队列,    
            //当maximumPoolSize>currentPoolSize>=corePoolSize+workQueue,建新线程来处理被添加的任务,    
            //当currentPoolSize>=maximumPoolSize,通过 handler所指定的策略来处理新添加的任务    
            //本例中可以同时可以被处理的任务最多为maximumPoolSize+WORKQUEUE=8个，其中最多5个在线程中正在处理，3个在缓冲队列中等待被处理    
            //当currentPoolSize>corePoolSize时，如果某线程空闲时间超过keepAliveTime，线程将被终止。这样，线程池可以动态的调整池中的线程数    
          
            threadPool.execute(new ThreadPoolTask11(task,listImei.get(i),time11,time22));   
            
           
        } 
	    
	    threadPool.shutdown();//关闭主线程，但线程池会继续运行，直到所有任务执行完才会停止。若不调用该方法线程池会一直保持下去，以便随时添加新的任务  
	    
	}
	public static void main(String[] args) throws Throwable{
		TravelSubsectionAll1 travelSubsection =new TravelSubsectionAll1();
		travelSubsection.travelSubsectionList();
			
	}
		
}
	
	/**
	* 线程池执行的任务
	*/
	class ThreadPoolTask11 implements Runnable, Serializable
	{
		private static Logger logger = Logger.getLogger(ThreadPoolTask11.class);
	   private static final long serialVersionUID = 0;
	   private static int consumeTaskSleepTime = 2000;
	   // 保存任务所需要的数据
	   private Object threadPoolTaskData;
	   private String imei1;
	   
	   public Date time11;
	   public Date time22;
		BasicDao basicDao=new BasicDao();

	   ThreadPoolTask11(Object tasks,String imei1,Date time1,Date time2)
	   {
	       this.threadPoolTaskData = tasks;
	       this.imei1=imei1;
	       this.time11=time1;
	       this.time22=time2;
	       
	       
	   }
	 //时间表
		public  String transDrive(String imei) {
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
		public double singleDeviceDriveSingleTrave(Date startDate, Date endDate, String imei, String traveFlag, String model,BasicDao dao) throws Exception {
			// 行驶里程，总耗时，平均时速；
			
			
			
			List<Map<String, Object>> trackList = dao.trackUser(model, imei, startDate, endDate, traveFlag,"2");
			if(trackList.size()==0)
			{
				trackList = dao.trackUser(model, imei, startDate, endDate, traveFlag,"1");
			}
			
			
			
			
			System.out.println("-----track size is-----"+trackList.size());
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
	   public void run()
	   {
	       // 处理一个任务，这里的处理方式太简单了，仅仅是一个打印语句
	       System.out.println(Thread.currentThread().getName());
	       System.out.println("start .." + threadPoolTaskData);

	       try
	       {
	    	   String imei=this.imei1;
				List<TravelEntity> resultList = GetTrack.queryTravel(time11, time22, imei, "R611");
				String travel =travelCoding(imei);
				String track=transCoding(imei);
				
				for (int j = 0; j < resultList.size(); j++) {
					System.out.println("imei is --------" + imei+"---------"+j+"-------------"+resultList.size());
					System.out.println("travel " + j + " is --------" + resultList.get(j));
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
					 System.out.println("---region1----"+c_st_region+"------region2------"+c_ed_region);
					 map.put("c_st_region",c_st_region);
					 map.put("c_ed_region",c_ed_region);
					 System.out.println("---time1----"+time1+"------time2------"+time2);
					if(time1!=null&&time2!=null) 
					{
						System.out.println("--------"+time1+"----"+time2+"----"+imei+"----"+track+"----"+basicDao);
						//计算路程
						double path =singleDeviceDriveSingleTrave(time1, time2, imei, track, "R611",basicDao);
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
					
						basicDao.basicInsertData(travel, map); 
					}else
					{
						 System.out.println("---time1----"+time1+"------time2------"+time2);
						continue;
					}
					
					 System.out.println("---last----");
					 
				} 
				
				 System.out.println("---end----");
	       }
	       catch (Exception e)
	       {
	           e.printStackTrace();
	           System.out.println("----"+e);
	       } catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			   System.out.println("----"+e);
		}
	       threadPoolTaskData = null;
	       
	       System.out.println("---end1----");
	    Date time=new Date();
        logger.error(time+"allimei is "+imei1);
	       
	   }

	   public Object getTask()
	   {
	       return this.threadPoolTaskData;
	   }
	}  
	
	
	
