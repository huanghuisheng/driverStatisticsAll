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


public class InitializationQueryData  {

	  Map<String, String> processors = new HashMap<String, String>();
	
	  public static DataSourceBuilder builder = null;
	    private InitializationQueryData(){  
	    
			 DataSourceBuilder.getInstance().getDataSourceCluster();
			 builder = DataSourceBuilder.getInstance();	 
			processors.put("n_total_0", "update  iov_analyzer_dev_daily set n_total_0=? where c_model=? and c_build_time=? and c_region_code=?");
			processors.put("n_total_1", "update  iov_analyzer_dev_daily set n_total_1=? where c_model=? and c_build_time=? and c_region_code=?");
			processors.put("n_total_2", "update  iov_analyzer_dev_daily set n_total_2=? where c_model=? and c_build_time=? and c_region_code=?");
			processors.put("n_total_3", "update  iov_analyzer_dev_daily set n_total_3=? where c_model=? and c_build_time=? and c_region_code=?");
			processors.put("n_total_4", "update  iov_analyzer_dev_daily set n_total_4=? where c_model=? and c_build_time=? and c_region_code=?");
			processors.put("n_total_5", "update  iov_analyzer_dev_daily set n_total_5=? where c_model=? and c_build_time=? and c_region_code=?");
			processors.put("n_total_6", "update  iov_analyzer_dev_daily set n_total_6=? where c_model=? and c_build_time=? and c_region_code=?");
			
			processors.put("n_new_0", "update  iov_analyzer_dev_daily set n_new_0=? where c_model=? and c_build_time=? and c_region_code=?");
			processors.put("n_new_1", "update  iov_analyzer_dev_daily set n_new_1=? where c_model=? and c_build_time=? and c_region_code=?");
			processors.put("n_new_2", "update  iov_analyzer_dev_daily set n_new_2=? where c_model=? and c_build_time=? and c_region_code=?");
			processors.put("n_new_3", "update  iov_analyzer_dev_daily set n_new_3=? where c_model=? and c_build_time=? and c_region_code=?");
			processors.put("n_new_4", "update  iov_analyzer_dev_daily set n_new_4=? where c_model=? and c_build_time=? and c_region_code=?");
			processors.put("n_new_5", "update  iov_analyzer_dev_daily set n_new_5=? where c_model=? and c_build_time=? and c_region_code=?");
			processors.put("n_new_6", "update  iov_analyzer_dev_daily set n_new_6=? where c_model=? and c_build_time=? and c_region_code=?");
			
			processors.put("n_uv_0", "update  iov_analyzer_dev_daily set n_uv_0=? where c_model=? and c_build_time=? and c_region_code=?");
			processors.put("n_uv_1", "update  iov_analyzer_dev_daily set n_uv_1=? where c_model=? and c_build_time=? and c_region_code=?");
			processors.put("n_uv_2", "update  iov_analyzer_dev_daily set n_uv_2=? where c_model=? and c_build_time=? and c_region_code=?");
			processors.put("n_uv_3", "update  iov_analyzer_dev_daily set n_uv_3=? where c_model=? and c_build_time=? and c_region_code=?");
			processors.put("n_uv_4", "update  iov_analyzer_dev_daily set n_uv_4=? where c_model=? and c_build_time=? and c_region_code=?");
			processors.put("n_uv_5", "update  iov_analyzer_dev_daily set n_uv_5=? where c_model=? and c_build_time=? and c_region_code=?");
			processors.put("n_uv_6", "update  iov_analyzer_dev_daily set n_uv_6=? where c_model=? and c_build_time=? and c_region_code=?");
			
			
			
			processors.put("n_total1", "update  iov_analyzer_dev_daily set n_total1=? where c_model=? and c_build_time=? and c_region_code=?");
			processors.put("n_total3", "update  iov_analyzer_dev_daily set n_total3=? where c_model=? and c_build_time=? and c_region_code=?");
			processors.put("n_total12", "update  iov_analyzer_dev_daily set n_total12=? where c_model=? and c_build_time=? and c_region_code=?");
			
			processors.put("n_new7", "update  iov_analyzer_dev_daily set n_new7=? where c_model=? and c_build_time=? and c_region_code=?");
			processors.put("n_new30", "update  iov_analyzer_dev_daily set n_new30=? where c_model=? and c_build_time=? and c_region_code=?");
			processors.put("n_uv7", "update  iov_analyzer_dev_daily set n_uv7=? where c_model=? and c_build_time=? and c_region_code=?");
			processors.put("n_uv30", "update  iov_analyzer_dev_daily set n_uv30=? where c_model=? and c_build_time=? and c_region_code=?");

                
	    }  
	    public static InitializationQueryData getInstance()  
	    {  
	        return Data.instance;       
	    }     
	    //在第一次被引用时被加载  
	    static class Data  
	    {  	
	        private static InitializationQueryData instance = new InitializationQueryData();  
	    }  
	 
	    
	    
	    
	    
	    
	    
	    
	    //---------------------------------------------------------------新增地区分布
	    @SuppressWarnings("unchecked")
		public List<Map<String, Object>> areaDistributionList(final Date starttime,final Date endtime, final String flag ) throws Exception {
		        return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
		            public Object doInJdbc(Connection connect) throws SQLException, Exception {
		         	  String sql=null;
		           	 if(flag == null || flag.isEmpty()){
		         		  System.out.println("地区分布"+flag);
		         		sql= "select count(*) as number from iov_device where  t_create_time  between ? and ?"; 
		         		 return DbHelper.queryForList(connect, sql,starttime,endtime);
		         	  }else
		         	  {
		         		 sql= "select count(*) as number from iov_device where ( t_create_time  between ? and ?) and c_region_code like?";  
		         		 return DbHelper.queryForList(connect, sql,starttime,endtime, flag);
		         	  }
		           }
		        });
		    }
		//活跃用户地区分布
		   @SuppressWarnings("unchecked")
		public List<Map<String, Object>> activeAreaDistributionList(final Date starttime,final Date endtime, final String flag ) throws Exception {
		        return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
		            public Object doInJdbc(Connection connect) throws SQLException, Exception {
		         	  String sql=null;
		          	 if(flag == null || flag.isEmpty()){
		         		  System.out.println("地区分布"+flag);
		         		sql= "select count(*) as number from iov_dev_ol where (t_online between  ? and ? ) or (t_offline between  ? and ?)or (  t_online< ? and t_offline> ?)"; 
		         		 return DbHelper.queryForList(connect, sql,starttime,endtime,starttime,endtime,starttime,endtime);
		         	  }else
		         	  {
		         		 sql= "select count(*) as number from iov_dev_ol where ( (t_online between  ? and ? ) or (t_offline between  ? and ?)or (  t_online< ? and t_offline> ?)) and c_region_code like?";  
		         		 return DbHelper.queryForList(connect, sql,starttime,endtime,starttime,endtime,starttime,endtime,flag);
		         	  }
		             //return null;
		           }
		        });
		    }
	    
	   //清空数据库；
		   public  Object clearDataActive() throws Exception {
	            return (Object) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
	                public Object doInJdbc(Connection connect) throws SQLException, Exception {
	                  String sql=null;
	                  sql= "TRUNCATE  TABLE   iov_analyzer_dev_active  ";
	                  return DbHelper.executeUpdate(connect, sql);
	               }
	            });
	        }
		   public  Object clearDataArea() throws Exception {
	            return (Object) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
	                public Object doInJdbc(Connection connect) throws SQLException, Exception {
	                  String sql=null;
	                  sql= "TRUNCATE  TABLE   iov_analyzer_dev_area  ";
	                  return DbHelper.executeUpdate(connect, sql);
	               }
	            });
	        }
	    
	    
	    
	    
	    
	    
	    //首页设备坐标；
	    //---------------------------------------------------设备坐标查询；
	 	   @SuppressWarnings("unchecked")
	 	public List<Map<String, Object>> queryListMap(final Date startTime,final Date endTime) throws Exception {
	         return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
	             public Object doInJdbc(Connection connect) throws SQLException, Exception {
	          	   String sql=null;
	          	 sql= "SELECT c_imei ,c_model ,c_build_time , n_lng ,n_lat,c_region_code from iov_dev_ol ";
	          	//   sql= "SELECT c_imei ,c_model ,c_build_time , n_lng ,n_lat,c_region_code from iov_dev_ol  where t_online BETWEEN ? and ?";
	               return DbHelper.queryForList(connect, sql);
	            }
	         });
	     } 
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    

        //活跃数据；
       @SuppressWarnings("unchecked")
	public  List<Map<String, Object>> queryActiveData(final Date startDate, final Date endDate) throws Exception {
       return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
           public Object doInJdbc(Connection connect) throws SQLException, Exception {
             String sql=null;
            
           sql= "SELECT  c_model,c_build_time,c_region_code ,COUNT(*) AS number FROM  ( SELECT * FROM iov_dev_ol  where (t_online between  ? and ? ) or (t_offline between  ? and ?) or (  t_online< ? and t_offline> ?) ) iov  GROUP BY c_model,c_build_time,c_region_code ORDER BY c_region_code,c_build_time,c_model desc "; 
             return DbHelper.queryForList(connect, sql,startDate,endDate,startDate,endDate,startDate,endDate);
          }
       });
   } 
    //查询新增当天的数据;
       @SuppressWarnings("unchecked")
	public  List<Map<String, Object>> queryIncreasedList(final Date startDate, final Date endDate) throws Exception {
       return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
           public Object doInJdbc(Connection connect) throws SQLException, Exception {
             String sql=null;
           
           sql= "SELECT  c_model,c_build_time,c_region_code ,COUNT(*) AS number FROM  ( SELECT * FROM iov_device  where t_create_time between  ? and ? ) iov  GROUP BY c_model,c_build_time,c_region_code ORDER BY c_region_code,c_build_time,c_model desc "; 
             return DbHelper.queryForList(connect, sql,startDate,endDate);
          }
       });
   }   
       //查询时间<的数据
       @SuppressWarnings("unchecked")
	public  List<Map<String, Object>> cumulativeQueryList(final Date endDate) throws Exception {
       return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
           public Object doInJdbc(Connection connect) throws SQLException, Exception {
        
             String sql=null;

            sql= "SELECT  c_model,c_build_time,c_region_code ,COUNT(*) AS number FROM  ( SELECT * FROM iov_device  where  t_create_time < ? ) iov  GROUP BY c_model,c_build_time,c_region_code ORDER BY c_region_code,c_build_time,c_model desc ";
            
             return DbHelper.queryForList(connect, sql,endDate);
          }
       });
   }  
       
      
       //更新新增或累积数据；
        public  Object queryUpdateList(final int number, final String flag, final String c_model,final String c_build_time, final String c_region_code) throws Exception {
       return (Object) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
           public Object doInJdbc(Connection connect) throws SQLException, Exception { 
             String sql=null;
           	 sql=processors.get(flag);  	 
                return DbHelper.executeUpdate(connect,sql ,number,c_model,c_build_time,c_region_code);	 
       
         
          }
       });
       }
        
        //设备在线坐标数据更新；c_imei, c_model, c_build_time, n_lng, n_lat
        public  Object queryOnlineUpdateList(final String c_imei, final String c_model, final String c_build_time,final String n_lng, final String n_lat) throws Exception {
       return (Object) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
           public Object doInJdbc(Connection connect) throws SQLException, Exception { 
             String sql=null;
           	 sql="update  iov_analyzer_dev_time set n_lng=? ,n_lat=? where c_imei=? and c_model=? ";  	 
                return DbHelper.executeUpdate(connect,sql ,n_lng,n_lat,c_imei,c_model);	 
       
         
          }
       });
       
       }
        
        
 
         //查询设备查询表是否存在记录；
        @SuppressWarnings("unchecked")
		public  List<Map<String, Object>> queryRecordList(final String c_region_code,final String c_model,final String c_build_time) throws Exception {
            return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
                public Object doInJdbc(Connection connect) throws SQLException, Exception {
                  String sql=null;
                  sql= "select * from  iov_analyzer_dev_daily  where  c_region_code= ? and c_model= ?and c_build_time =?";
                  return DbHelper.queryForList(connect, sql,c_region_code,c_model,c_build_time);
               }
            });
        }
        
        //查询设备在线表是否存在记录；
        @SuppressWarnings("unchecked")
		public  List<Map<String, Object>> queryOnlineRecordList(final String c_imei,final String c_model) throws Exception {
            return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
                public Object doInJdbc(Connection connect) throws SQLException, Exception {
                  String sql=null;
                  sql= "select * from  iov_analyzer_dev_time  where  c_imei= ? and c_model= ?";
                  return DbHelper.queryForList(connect, sql,c_imei,c_model);
               }
            });
        }
        
 
        //插入数据
        public  Object insertForRet( final String tableName,final Map<String, Object> fields) throws Exception {
            return (Object)  DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
                public Object doInJdbc(Connection connect) throws SQLException, Exception {  
                  return DbHelper.insertForRet(connect, tableName,fields);
               }
            });   
        }  
        
        //查询数据表是否有数据；
        @SuppressWarnings("unchecked")
		public  List<Map<String, Object>> queryList5() throws Exception {
            return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
                public Object doInJdbc(Connection connect) throws SQLException, Exception {	
                  String sql=null;
                  sql= "select * from  iov_analyzer_dev_daily ";
                  return DbHelper.queryForList(connect, sql);
               }
            });
        }  
        
        
        //查询数据表是否有数据；
        @SuppressWarnings("unchecked")
		public  List<Map<String, Object>> queryList6() throws Exception {
            return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
                public Object doInJdbc(Connection connect) throws SQLException, Exception {
                  String sql=null;
                  sql= "select * from  iov_analyzer_dev_time  ";
                  return DbHelper.queryForList(connect, sql);
               }
            });
        }  
        
        
          //清空数据表；
        public  Object clearData2() throws Exception {
            return (Object) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
                public Object doInJdbc(Connection connect) throws SQLException, Exception {
                  String sql=null;
                  sql= "TRUNCATE  TABLE   iov_analyzer_dev_time ";
                  return DbHelper.executeUpdate(connect, sql);
               }
            });
        }  

        //清空数据表；
        public  Object clearData() throws Exception {
            return (Object) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
                public Object doInJdbc(Connection connect) throws SQLException, Exception {
                  String sql=null;
                  sql= "TRUNCATE  TABLE   iov_analyzer_dev_daily  ";
                  return DbHelper.executeUpdate(connect, sql);
               }
            });
        }

        //测试；
        @SuppressWarnings("unchecked")
		public  List<Map<String, Object>> test() throws Exception {
        	DataSourceBuilder.getInstance().getDataSourceCluster();
   		 builder = DataSourceBuilder.getInstance();	
            return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
                public Object doInJdbc(Connection connect) throws SQLException, Exception {
                  String sql=null;
                  sql= "select * from  iov_analyzer_dev_daily";
                  return DbHelper.queryForList(connect, sql);
               }
            });
        }
		public Map<String, String> getProcessors() {
			return processors;
		}
		public void setProcessors(Map<String, String> processors) {
			this.processors = processors;
		}  
        
 
	
}
