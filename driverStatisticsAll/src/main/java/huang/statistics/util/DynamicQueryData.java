package huang.statistics.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tonetime.commons.database.DataSourceBuilder;
import com.tonetime.commons.database.helper.DbHelper;
import com.tonetime.commons.database.helper.JdbcCallback;
import com.tonetime.commons.util.DateUtils;
import com.tonetime.commons.util.StringUtils;


public class DynamicQueryData  {

	public static DataSourceBuilder builder = null;
	    private DynamicQueryData(){  
			 DataSourceBuilder.getInstance().getDataSourceCluster();
			 builder = DataSourceBuilder.getInstance();	   	
	    }  
	 
	    public static DynamicQueryData getInstance()  
	    {  
	        return Data.instance;       
	    }  
	      
	    //在第一次被引用时被加载  
	    static class Data  
	    {  
	    	
	        private static DynamicQueryData instance = new DynamicQueryData();  
	    }  
	  
	
	    
	//-------------------------------------------------累计用户
	public  List<Map<String, Object>>  queryAccumulatingData( final Date endDate,final String c_model,final String c_region_code) throws Exception{
	     List<Map<String, Object>> increasedList1;	 
	    	 System.out.println ("编码为："+c_region_code);
	    	 increasedList1=   accumulativeDate(endDate, c_model, c_region_code);       
	 	return increasedList1;
	 	}
	
	
//----------------------------------------------------------------------------新增用户
	 	public  List<Map<String, Object>>  queryIncreasedData( final Date strart ,final Date endDate,final String c_model,final String c_region_code) throws Exception{
	
	
	 	     List<Map<String, Object>> increasedList1;

	 	    	 increasedList1=   increasedDate(strart,endDate, c_model, c_region_code);  
		return increasedList1;
	}
	//----------------------------------------------------------------------------活跃用户
	 	public  List<Map<String, Object>>  queryActiveData( final Date strart ,final Date endDate,final String c_model,final String c_region_code) throws Exception{
	
	 	     List<Map<String, Object>> increasedList1;	    	 
	 	    	 System.out.println ("编码为："+c_region_code);
	 	    	 increasedList1=   activeDate(strart,endDate, c_model, c_region_code);    
		return increasedList1;
	}	 	

     
        //查询累计到当天时间用户数量
        @SuppressWarnings("unchecked")
		public  List<Map<String, Object>> accumulativeDate(final Date endDate,final String c_model,final String c_region_code ) throws Exception {
        return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
            public Object doInJdbc(Connection connect) throws SQLException, Exception {
         
              String sql= null;
            if(!c_region_code.equals("allcode") && !c_model.equals("allmodel"))
            {
            	
            	sql="SELECT  COUNT(*) AS number  FROM iov_device  where  t_create_time <? and c_model=? and c_region_code=? ";
            	 return DbHelper.queryForList(connect, sql,endDate,c_model,c_region_code);
            }
            else if (c_region_code.equals("allcode") && !c_model.equals("allmodel")){
            
            	sql="SELECT  COUNT(*) AS number  FROM iov_device  where  t_create_time <? and c_model=?  ";
           	 return DbHelper.queryForList(connect, sql,endDate,c_model);
            }
            else if (!c_region_code.equals("allcode") && c_model.equals("allmodel") ){
            
            	sql="SELECT  COUNT(*) AS number  FROM iov_device  where  t_create_time <? and c_region_code=?  ";
           	 return DbHelper.queryForList(connect, sql,endDate,c_region_code);
            }
            else{
            	sql="SELECT  COUNT(*) AS number  FROM iov_device  where  t_create_time <?";
              	 return DbHelper.queryForList(connect, sql,endDate);
            }
           }
        });
    } 
        
        
        //---------------------------------------------------------------------------------------------查询当天新增用户数量；  
        @SuppressWarnings("unchecked")
		public  List<Map<String, Object>> increasedDate( final Date start,final Date endDate,final String c_model,final String c_region_code ) throws Exception {
        return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
            public Object doInJdbc(Connection connect) throws SQLException, Exception {
         
              String sql= null;
            if(!c_region_code.equals("allcode") && !c_model.equals("allmodel"))
            {
            	
            	sql="SELECT  COUNT(*) AS number  FROM iov_device  where  t_create_time between  ? and ? and c_model=? and c_region_code=? ";
            	 return DbHelper.queryForList(connect, sql,start,endDate,c_model,c_region_code);
            }
            else if (c_region_code.equals("allcode") && !c_model.equals("allmodel")){
            	
            	sql="SELECT  COUNT(*) AS number  FROM iov_device  where  t_create_time between  ? and ? and c_model=?  ";
           	 return DbHelper.queryForList(connect, sql,start,endDate,c_model);
            }
            else if (!c_region_code.equals("allcode") && c_model.equals("allmodel") ){
            	
            	sql="SELECT  COUNT(*) AS number  FROM iov_device  where  t_create_time between  ? and ? and c_region_code=?  ";
           	 return DbHelper.queryForList(connect, sql,start,endDate,c_region_code);
            }
            else{
            
            	sql="SELECT  COUNT(*) AS number  FROM iov_device  where  t_create_time between  ? and ?";
              	 return DbHelper.queryForList(connect, sql,start,endDate);
            }
           }
        });
    } 
        
        
        //---------------------------------------------------------------------------------------------查询当天活跃用户数量；  
        @SuppressWarnings("unchecked")
		public  List<Map<String, Object>> activeDate( final Date start,final Date endDate,final String c_model,final String c_region_code ) throws Exception {
        return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
            public Object doInJdbc(Connection connect) throws SQLException, Exception {
              String sql= null;
            if(!c_region_code.equals("allcode") && !c_model.equals("allmodel"))
            {
            	
            	sql="SELECT  COUNT(*) AS number  FROM iov_dev_ol  where  ((t_online between  ? and ? ) or (t_offline between  ? and ?) or (  t_online< ? and t_offline> ?)) and c_model=? and c_region_code=? ";
            	 return DbHelper.queryForList(connect, sql,start,endDate,start,endDate,start,endDate,c_model,c_region_code);
            }
            else if (c_region_code.equals("allcode") && !c_model.equals("allmodel")){
            	
            	sql="SELECT  COUNT(*) AS number  FROM iov_dev_ol  where ((t_online between  ? and ? ) or (t_offline between  ? and ?) or (  t_online< ? and t_offline> ?)) and c_model=?";
           	 return DbHelper.queryForList(connect, sql,start,endDate,start,endDate,start,endDate,c_model);
            }
            else if (!c_region_code.equals("allcode") && c_model.equals("allmodel") ){
            	
            	sql="SELECT  COUNT(*) AS number  FROM iov_dev_ol  where  ((t_online between  ? and ? ) or (t_offline between  ? and ?) or (  t_online< ? and t_offline> ?)) and c_region_code=?";
           	 return DbHelper.queryForList(connect, sql,start,endDate,start,endDate,start,endDate,c_region_code);
            }
            else{
            	
            	sql="SELECT  COUNT(*) AS number  FROM iov_dev_ol  where  (t_online between  ? and ? ) or (t_offline between  ? and ?) or (  t_online< ? and t_offline> ?)";
              	 return DbHelper.queryForList(connect, sql,start,endDate,start,endDate,start,endDate);
            }
           }
        });
    }   
        
        //查询地方编码；
        @SuppressWarnings("unchecked")
		public  List<Map<String, Object>> areaDate(final String province,final String city,final String county ) throws Exception {
        return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
            public Object doInJdbc(Connection connect) throws SQLException, Exception {
         
              String sql=null;
             sql= "select  c_code FROM code_region_new where c_province=? and c_city=? and c_county=?";
             
              return DbHelper.queryForList(connect, sql,province,city,county);
           }
        });
    }   
        
        
        
        
        
        
        
        
        

	
}
