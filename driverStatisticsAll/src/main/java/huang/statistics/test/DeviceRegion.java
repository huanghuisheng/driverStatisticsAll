package huang.statistics.test;

import huang.statistics.dao.BasicDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.broadsense.iov.lbs.LbsService;
import com.broadsense.iov.lbs.Precision;
import com.broadsense.iov.lbs.pojo.GPSLacBean;

public class DeviceRegion {
	//轨迹表
	public static String transCoding(String imei) {
		imei = imei.substring(13);
		String track = "iov_track_" + Integer.valueOf(imei) % 100;
		return track;
	}
	//转换地址；
	public static String convertRegion(double lng,double lat) throws Throwable  {
		GPSLacBean region = LbsService.getInstance().getGPSTrack(lng,lat, Precision.FULL);
		 if (region != null) {
	        String re=region.getRegionCode();
	        return re;
	    }
		 return null;
	}
	public static void main(String[] args) throws Throwable {
		// TODO Auto-generated method stub
		//获取设备imeihao
		BasicDao dao = new BasicDao();
		List<Map<String,Object>> listImei= new ArrayList<Map<String,Object>>();
		listImei=dao.dataTable("iov_device_client");
		for(int i=0;i<listImei.size();i++)
		{
			if(listImei.get(i).get("c_region")==null||("").equals(listImei.get(i).get("c_region")))
			{
				String imei=String.valueOf(listImei.get(i).get("c_imei"));
				//查询激活地点;
				List<Map<String,Object>> listRegion=dao.dataRegion(imei);
				if(listRegion.size()>0&&listRegion.get(0).get("c_region_code")!=null&&!("").equals(listRegion.get(0).get("c_region_code")))
				{
					String region=String.valueOf(listRegion.get(0).get("c_region_code"));
					System.out.println("--11--"+imei+"--------"+listRegion.get(0).get("c_region_code"));
					System.out.println("--1--"+imei+"--------"+region);
					//更新iov_device_client数据；
					Map<String,Object> map=new HashMap<String,Object>();
					map.put("c_region", region);
					dao.basicUpdate("iov_device_client", map, "c_imei="+imei);
				}else
				{
					List<Map<String,Object>> listRegion1=dao.deviceDovRegion("R611", imei);
					if(listRegion1.size()>0)
					{
						String region=String.valueOf(listRegion1.get(0).get("c_region_code"));
						Map<String,Object> map=new HashMap<String,Object>();
						System.out.println("--2--"+imei+"--------"+region);
						map.put("c_region", region);
						dao.basicUpdate("iov_device_client", map, "c_imei="+imei);
					}else
					{
						String track=transCoding(imei);
						List<Map<String,Object>> listRegion2=dao.deviceTrackRegion("R611", imei,track);
						System.out.println("--33--"+imei+"--------"+listRegion2+"-----"+track);
						String lng=String.valueOf(listRegion2.get(0).get("n_lng"));
						String lat=String.valueOf(listRegion2.get(0).get("n_lat"));
						String region=convertRegion(Double.valueOf(lng),Double.valueOf(lat));
						Map<String,Object> map=new HashMap<String,Object>();
						
						System.out.println("--3--"+imei+"--------"+region);
						
						map.put("c_region", region);
						dao.basicUpdate("iov_device_client", map, "c_imei="+imei);
					}
				}
			}
		}
	}
}
