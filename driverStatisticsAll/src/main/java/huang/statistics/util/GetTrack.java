package huang.statistics.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.broadsense.iov.base.api.DataFacade;
import com.broadsense.iov.base.data.entity.LacEntity;
import com.broadsense.iov.base.data.entity.TravelEntity;
import com.broadsense.iov.lbs.GpsProcess;
import com.tonetime.commons.util.Arith;

public class GetTrack {
	
	private static Logger logger = Logger.getLogger(GetTrack.class);

    


    public void index(String imei,String model,Date startDate,Date endDate) throws Exception {
  



        List<TravelEntity> resultList = queryTravel(startDate,endDate,imei,model);
      
//        List<List<LacEntity>> resultList1 =new ArrayList<List<LacEntity>>();
//        
//        for(int i=0 ;i<resultList.size();i++)
//        {
//          Date  strartTime=	 resultList.get(i).getStTime();        
//          Date  endTime  =	 resultList.get(i).getEdTime();
//          
//          List<LacEntity> resultList2  = show(request,response,strartTime,endTime,imei,model);
//          
//          resultList1.add(resultList2);
//          
//    
//          
//        }
        
 
    }
    public static void correctGps(LacEntity gps) {
        if (gps.getClng() <= 0 || gps.getClat() <= 0) {
            double lng = gps.getLng();
            double lat = gps.getLat();
            if (gps.getType() == LacEntity.TYPE_GPS) {
                double[] tmp = CoordinateUtil.wgs2gcj02(lat, lng);
                lng = Arith.round(tmp[1], 6).doubleValue();
                lat = Arith.round(tmp[0], 6).doubleValue();
            }
            gps.setClng(lng);
            gps.setClat(lat);
        }

    }

    public static List<LacEntity> show(HttpServletRequest request,Date strartTime,Date endTime, String imei,String model) throws Exception {

        final List<LacEntity> resultList = new ArrayList<LacEntity>();
     
        logger.error("model is "+model+"-----"+imei);
        logger.error("time  is "+strartTime+"------"+endTime);
        
        List<LacEntity> list = DataFacade.getInstance().findTrackList(model, imei, strartTime, endTime);
        
        logger.error("轨迹点。。。。  is "+list.size());
        
        
        
         GpsProcess gpsProcess = new GpsProcess(null);

        
        
        for (int idx = list.size() - 1; idx >= 0; idx--) {
            LacEntity gps = list.get(idx);
//            if (gpsProcess.filter(gps)) {
            correctGps(gps);
            double[] pos = CoordinateUtil.gcj02ToBd09(gps.getClat(), gps.getClng());
            gps.setClat(pos[0] - 0.0004);
            gps.setClng(pos[1] + 0.0003);
            resultList.add(gps);
//            }
        }
        

        return resultList;
      
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    


    public List<TravelEntity> filterNoFinishTravel(List<TravelEntity> list) {
        final List<TravelEntity> resultList = new ArrayList<TravelEntity>();
        for (TravelEntity travel : list) {
            if (travel.getEdTime() != null) {
                resultList.add(travel);
            }
        }
        return resultList;
    }
    public  static List<TravelEntity> queryTravel( Date startDate, Date endDate,String imei,String model) throws Exception {
        final List<TravelEntity> travelList = new ArrayList<TravelEntity>();
        GpsProcess gpsProcess = new GpsProcess(new GpsProcess.OnTrackListener() {
            public void onStart(LacEntity lac) {
                if (!travelList.isEmpty()) {
                    TravelEntity last = travelList.get(travelList.size() - 1);
                    if (last.getEdTime() == null) {
                        last.setEdTime(lac.getDataTime());
                        last.setEdlng(lac.getClng());
                        last.setEdLat(lac.getClat());
                    }
                }
                TravelEntity travel = new TravelEntity();
                travel.setModel(lac.getModel());
                travel.setImei(lac.getImei());
                travel.setStTime(lac.getDataTime());
                travel.setStLat(lac.getClat());
                travel.setStLng(lac.getClng());
                travelList.add(travel);
            }

            public void onStop(LacEntity lac) {
                if (!travelList.isEmpty()) {
                    TravelEntity last = travelList.get(travelList.size() - 1);
                    if (last.getEdTime() == null) {
                        last.setEdTime(lac.getDataTime());
                        last.setEdlng(lac.getClng());
                        last.setEdLat(lac.getClat());
                    }
                }
            }
        });
        long time1 = System.currentTimeMillis();
     
        List<LacEntity> list = DataFacade.getInstance().findTrackList(model, imei, startDate, endDate);
        
        
    

        long time2 = System.currentTimeMillis();
        
        
     /*   for (int idx = list.size() - 1; idx >= 0; idx--) {
            gpsProcess.process(list.get(idx));
        }
*/
        for (int idx = list.size() - 1; idx >= 0; idx--) {
        	LacEntity   gps = list.get(idx);
            correctGps(gps);
            gpsProcess.process(gps);
        }
        
    
        
        
       /*  final List<TravelEntity> resultList = filterNoFinishTravel(travelList); */
      // travelList.add(travelList.get(1));
       
       final List<TravelEntity> resultList=travelList;
       

       // System.out.println("time222   is ----------"+resultList.size());
        
       
         Collections.sort(resultList, new Comparator<TravelEntity>() {
            public int compare(TravelEntity o1, TravelEntity o2) {
                return o1.getStTime().compareTo(o2.getStTime());
            }
        });       
        return resultList;
    }
    
    
    
    
    

}
