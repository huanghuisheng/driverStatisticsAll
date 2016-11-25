package huang.statistics.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Repository;

import com.tonetime.commons.database.helper.DbHelper;
import com.tonetime.commons.database.helper.JdbcCallback;


public class StatisticsDaoImpl extends BasicDao {
	 //--------------------------------------获取单个用户的驾驶行为  
	   @SuppressWarnings("unchecked")
	public List<Map<String, Object>> singleGetDiverBehavior(final String  flag ,final String imei ,final Date time1,final Date time2,final String code) throws Exception {
	        return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
	            public Object doInJdbc(Connection connect) throws SQLException, Exception {
	         	   String sql=null;
	         	   if(code.equals("allcode")){
	         		  sql= "select c_driver from "+flag+" where  c_imei=?  and t_st_time >=? and t_ed_time<=?";
			          return DbHelper.queryForList(connect, sql,imei,time1,time2);
	         	   }else{
	         		  sql= "select c_driver from "+flag+" where  c_imei=?  and t_st_time >=? and t_ed_time<=?  and (c_st_region like  ? OR c_ed_region like  ? )   ";
			          return DbHelper.queryForList(connect, sql,imei,time1,time2,code+'%',code+'%'); 
	         	   }
	         	   
	         	   
			        
	           }
	        });
	    }
	   

//----------------------------------------统计分析
	   //单个驾驶行为分析；   
	   @SuppressWarnings("unchecked")
	public List<Map<String, Object>> singleGetImei(final String code) throws Exception {
	        return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
	            public Object doInJdbc(Connection connect) throws SQLException, Exception {
	         	   String sql=null;
	         	  
	         	if(code.equals("allcode"))
	         	{
	         		System.out.println("dddd-"+code);
	         		 sql= "SELECT  c_imei from iov_device  ORDER BY  n_id  limit 0,10";		              
		              return DbHelper.queryForList(connect, sql);	
	         	}else
	         	{
	         		
	         		System.out.println("ddsssdd-"+code);
	         	    sql= "SELECT  c_imei from iov_device where c_region_code=? ORDER BY  n_id  limit 0,10";		              
		              return DbHelper.queryForList(connect, sql,code);
	         	}
	         	   
	          
	           }
	        });
	    }
	 //--------------------------------------------------移动的所有的IMEI  
	   @SuppressWarnings("unchecked")
	public List<Map<String, Object>> singleGetImei1(final String code) throws Exception {
	        return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
	            public Object doInJdbc(Connection connect) throws SQLException, Exception {
	         	   String sql=null;
	         	  
	         	if(code.equals("allcode"))
	         	{
	         		System.out.println("dddd-"+code);
	         		 sql= "SELECT  c_imei from iov_device  ORDER BY  n_id  limit 0,20";		              
		              return DbHelper.queryForList(connect, sql);	
	         	}else
	         	{
	         		
	         		System.out.println("ddsssdd-"+code);
	         	    sql= "SELECT  c_imei from iov_device where c_region_code=? ORDER BY  n_id  limit 0,20 ";		              
		              return DbHelper.queryForList(connect, sql,code);
	         	}
	         	   
	          
	           }
	        });
	    }   
//-----------------------------------------------------真实的imei号
		@SuppressWarnings("unchecked")
		public List<Map<String, Object>> singleGetMobileImei() throws Exception {
	        return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
	            public Object doInJdbc(Connection connect) throws SQLException, Exception {
	         	   String sql=null;

	         	    sql= "SELECT  c_imei from  iov_administrator_group_imei";		              
		              return DbHelper.queryForList(connect, sql);          
	           }
	        });
	    }    
	   

		
//---------------------------------------------------获取二级账号imei号
		@SuppressWarnings("unchecked")
		public List<Map<String, Object>> singleGetMobileSecondeImei(final String groupId) throws Exception {
	        return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
	            public Object doInJdbc(Connection connect) throws SQLException, Exception {
	         	   String sql=null;
	         	    sql= "SELECT   c_imei from  iov_device_client where c_group =?";		              
		              return DbHelper.queryForList(connect, sql,groupId);          
	           }
	        });
	    }
		

	   
	   
	   
	   
	   
	   
	   
	   @SuppressWarnings("unchecked")
	public List<Map<String, Object>> singleGetDiverBehaviorSum(final String  flag ,final String imei ,final String token ,final Date time1,final Date time2) throws Exception {
	        return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
	            public Object doInJdbc(Connection connect) throws SQLException, Exception {
	         	   String sql=null;
	         	   if(token.equals("trun")){
	         		  String turn1="left_turn";
			          String turn2="right_turn";
			          sql= "select COUNT(c_token) as number  from "+flag+" where  c_imei=? and (c_token=? or c_token=?) and t_data_time BETWEEN ? and ?";
			          return DbHelper.queryForList(connect, sql,imei,turn1,turn2,time1,time2);
	         		
//	         		  sql= "SELECT  COUNT(*)  as number from "+flag+" where c_token= ? and c_imei=? and  t_data_time BETWEEN  ? and ?";
//		              return DbHelper.queryForList(connect, sql,token,imei,time1,time2);
	         		   
	         	   }else{
	         		  sql= "SELECT  COUNT(*)  as number from "+flag+" where c_token= ? and c_imei=? and  t_data_time BETWEEN  ? and ?";
		              return DbHelper.queryForList(connect, sql,token,imei,time1,time2);
	         	   }
	         	   
	         	   
	             
	           }
	        });
	    } 
	   
	   
	   
	   @SuppressWarnings("unchecked")
	public List<Map<String, Object>> singleGetTiredDiverBehaviorTime(final String imei,  final Date time1,final Date time2) throws Exception {
	        return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
	            public Object doInJdbc(Connection connect) throws SQLException, Exception {
	         	   String sql=null;
	              sql= "select  n_id ,t_online, t_offline  from iov_dev_ol WHERE c_imei=? and  t_online BETWEEN  ?   and ? ;";
	              return DbHelper.queryForList(connect, sql,time1,time2);
	           }
	        });
	    }
	   

	   //---------------------------------------------------------------------规律活动地域
	   @SuppressWarnings("unchecked")
	   
	public List<Map<String, Object>> ruleActivityArea( final String flag ) throws Exception {
	        return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
	            public Object doInJdbc(Connection connect) throws SQLException, Exception {
	         	  String sql=null;
	 
	         	  
	         		 sql= "select count(*) as number from (select * from iov_dev_ol ORDER BY  n_id  limit 0,100 ) iov where  c_region_code like? ";  
	         		 return DbHelper.queryForList(connect, sql,flag);
	         	 
	           }
	        });
	    }    
	   
	   
	  	  //----------------------------------------------------------------------驾驶类别；  
	   
	   @SuppressWarnings("unchecked")
	public List<Map<String, Object>> divercategoryTime(final String code) throws Exception {
	        return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
	            public Object doInJdbc(Connection connect) throws SQLException, Exception {
	         	  String sql=null;
	 
	         	  if(code.equals("allcode"))
	         	  {
	         		 sql= "SELECT t_online ,t_offline  from iov_dev_ol   ORDER BY t_online   limit 0,100";  
	         		 return DbHelper.queryForList(connect, sql); 
	         	  }else{
	         		 sql= "SELECT t_online ,t_offline  from iov_dev_ol where c_region_code=?  ORDER BY t_online  limit 0,100";  
	         		 return DbHelper.queryForList(connect, sql,code);
	         	  }
 
	           }
	        });
	    }
	   
	   
		
			@SuppressWarnings("unchecked")
			public List<Map<String, Object>> divercategoryTime1( final String code ,final int time1,final int time2) throws Exception {
		        return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
		            public Object doInJdbc(Connection connect) throws SQLException, Exception {
		         	  String sql=null;
		         	 if(code.equals("allcode"))
		         	  {
		         		sql= " SELECT   count(*) as number FROM  (select * from iov_dev_ol ORDER BY  n_id  limit 0,100 ) doc  WHERE  DATE_FORMAT(doc.t_offline, '%H') BETWEEN ? and ? " ;  
			         	 return DbHelper.queryForList(connect, sql,time1,time2); 
		         	  }else{
		         		 sql= " SELECT   count(*) as number FROM  (select * from iov_dev_ol ORDER BY  n_id  limit 0,100 )  doc  WHERE doc.c_region_code=? and DATE_FORMAT(doc.t_offline, '%H') BETWEEN ?and ?";  
			         	 return DbHelper.queryForList(connect, sql,code,time1,time2);   
		         	  }
		         	  
		         	 
		         	 
		           }
		        });
		    }
	  //--------------------------------------------------------------------------------行车评分条件；

			 //查询驾驶行为
			   //急刹车
			   @SuppressWarnings("unchecked")
			public List<Map<String, Object>> driveUser1(final String flag, final String imei,final Date startTime,final Date endTime) throws Exception {
			        return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
			            public Object doInJdbc(Connection connect) throws SQLException, Exception {
			         	   String sql=null;
			         	   String speed="speed_down";
			         	   
			              sql= "select COUNT(c_token) as number  from "+flag+" where c_imei=? and c_token =? and t_data_time BETWEEN ? and ?";
			
			              
			              
			              return DbHelper.queryForList(connect, sql,imei,speed,startTime,endTime);
			           }
			        });
			    }
			   
			   //急加速
			   @SuppressWarnings("unchecked")
			public List<Map<String, Object>> driveUser2(final String flag,final String imei,final Date startTime,final Date endTime) throws Exception {
			        return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
			            public Object doInJdbc(Connection connect) throws SQLException, Exception {
			         	   String sql=null;
			         	  String speed="speed_up";
			              sql= "select COUNT(c_token) as number  from "+flag+" where c_imei=? and c_token=? and t_data_time BETWEEN ? and ?";
    
			              return DbHelper.queryForList(connect, sql,imei,speed,startTime,endTime);
			           }
			        });
			    }
			   
			   
			   //急转弯
			   @SuppressWarnings("unchecked")
			public List<Map<String, Object>> driveUser3(final String flag, final String imei,final Date startTime,final Date endTime) throws Exception {
			        return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
			            public Object doInJdbc(Connection connect) throws SQLException, Exception {
			         	   String sql=null;
			         	  String speed="left_turn";
			         	 String speed1="right_turn";
			              sql= "select COUNT(c_token) as number  from "+flag+" where  c_imei=? and (c_token=? or c_token=?) and t_data_time BETWEEN ? and ?";
			              return DbHelper.queryForList(connect, sql,imei,speed,speed1,startTime,endTime);
			           }
			        });
			    }
			   
			   //碰撞
			   @SuppressWarnings("unchecked")
			public List<Map<String, Object>> driveUser4(final String flag,final String imei,final Date startTime,final Date endTime) throws Exception {
			        return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
			            public Object doInJdbc(Connection connect) throws SQLException, Exception {
			         	   String sql=null;
			         	  String collide="collide";
			              sql= "select COUNT(c_token) as number  from "+flag+" where  c_imei=? and c_token=? and t_data_time BETWEEN ? and ?";
			           
			              
			              return DbHelper.queryForList(connect, sql,imei,collide,startTime,endTime);
			           }
			        });
			    }
			
			
			   //一分钟内没有发生不良驾驶行为的；
			   
			   @SuppressWarnings("unchecked")
			public List<Map<String, Object>> driveUser5(final String flag,final String imei,final Date startTime,final Date endTime) throws Exception {
			        return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
			            public Object doInJdbc(Connection connect) throws SQLException, Exception {
			         	   String sql=null;
			         	  String collide="collide";
			         	 String left_turn="left_turn";
			         	String right_turn="right_turn";
			         	String speed_up="speed_up";
			         	String speed_down="speed_down"; 
			         	  
			         	  
			              sql= "select *  from "+flag+" where  c_imei=? and (c_token=? or c_token=? or c_token=? or c_token=? or c_token=?) and t_data_time BETWEEN ? and ?";
			            
			              
			              return DbHelper.queryForList(connect, sql,imei,collide,left_turn,right_turn,speed_up,speed_down,startTime,endTime);
			           }
			        });
			    }		   
			   
			
//-----------------------------------------------------------------------------------单个设备主页-移动			
			
			   //获取昵称
			   @SuppressWarnings("unchecked")
			public List<Map<String, Object>> getName(final String imei) throws Exception {
			        return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
			            public Object doInJdbc(Connection connect) throws SQLException, Exception {
			         	   String sql=null;
			         	
			              sql= "select * from  iov_device_client where c_imei=? ";
			              return DbHelper.queryForList(connect, sql,imei);
			           }
			        });
			    }
			   
			   //获取激活时间
			   @SuppressWarnings("unchecked")
			public List<Map<String, Object>> getCreateTime(final String imei) throws Exception {
			        return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
			            public Object doInJdbc(Connection connect) throws SQLException, Exception {
			         	   String sql=null;
			         	
			              sql= "select  c_imei ,t_create_time from iov_device  where c_imei=?";
			              return DbHelper.queryForList(connect, sql,imei);
			           }
			        });
			    }  
			
				//获取在线信息；
			
			   @SuppressWarnings("unchecked")
				public List<Map<String, Object>> getlocal(final String imei,final Date startTime,final Date endTime) throws Exception {
				        return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
				            public Object doInJdbc(Connection connect) throws SQLException, Exception {
				         	   String sql=null;
				         	
				              sql= "SELECT  n_lng,n_lat from iov_dev_ol where  c_imei =? AND t_online BETWEEN ? and ?";
				              return DbHelper.queryForList(connect, sql,imei,startTime,endTime);
				           }
				        });
				    }  
			
  
			   
			   
			   //查询碰撞视频数量和路径；
			   @SuppressWarnings("unchecked")
			public List<Map<String, Object>> videoUserNumber(final String flag,  final String model,  final String imei,final Date startTime,final Date endTime) throws Exception {
			        return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
			            public Object doInJdbc(Connection connect) throws SQLException, Exception {
			         	   String sql=null;
			         	   String stopEvent="stopEvent";
			              sql= "select   n_id,c_model,c_imei ,n_lat ,n_lng ,c_res_path ,t_data_time from  "+flag+" where c_token= ?  and c_model =?  and c_imei=? and t_data_time  BETWEEN   ? and ?";

			              return DbHelper.queryForList(connect, sql,stopEvent,model,imei,startTime,endTime);
			           }
			        });
			    }
			   
			   
			   
			   
			 //获取个人 一段里 时间行驶 规律  
				@SuppressWarnings("unchecked")
				public List<Map<String, Object>> singleDiverRuleTime(final String imei, final int time1,final int time2 ,final Date startTime,final Date endTime) throws Exception {
			        return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
			            public Object doInJdbc(Connection connect) throws SQLException, Exception {
			         	  String sql=null;
			         	
			         		sql= " SELECT   count(*) as number FROM  iov_dev_ol doc  WHERE  c_imei=? and   ( DATE_FORMAT(doc.t_offline, '%H') BETWEEN ? and ?) AND (  t_online >=? and t_offline<=?)   ";  
				         	 return DbHelper.queryForList(connect, sql,imei,time1,time2,startTime,endTime); 
			         	  
			         	  
			         	 
			         	 
			           }
			        });
			    }   

			   
				 //----------------------------------------------------------------单个设备-地域规律
			    
 
			  	@SuppressWarnings("unchecked")
				public List<Map<String, Object>> singleDiverRuleArea( final String imei,  final String flag,  final Date startTime,final Date endTime) throws Exception {
			        return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
			            public Object doInJdbc(Connection connect) throws SQLException, Exception {
			         	  String sql=null;
			         	
			         		//sql= " SELECT   count(*) as number FROM  iov_dev_ol doc  WHERE     t_online> =? and t_offline<=?  ";  
			         		 sql= "select t_online  ,t_offline from iov_dev_ol where c_imei=? and c_region_code like? and ( t_online >=? and t_offline <=?) ";  
			         		
			         		return DbHelper.queryForList(connect, sql,imei,flag,startTime,endTime); 
	 
			           }
			        });
			    }   
			 //--------------------------------查询设备轨迹坐标；
				
				   @SuppressWarnings("unchecked")
				public List<Map<String, Object>> trackUser(final String model,final String imei,final Date startTime,final Date endTime,final String flag,final String type) throws Exception {
			        return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
			            public Object doInJdbc(Connection connect) throws SQLException, Exception {
			         	   String sql=null;
			             if(type.equals("2"))
			             {
			            	 sql= "select c_model as model,c_imei as imei ,n_lat as lat ,n_lng  as lng ,n_type as type,t_data_time as datatime from "+flag+" where n_type =2 and c_imei=? and c_model=? and  t_data_time BETWEEN ? and ? order by t_data_time ";
				              return DbHelper.queryForList(connect, sql,imei,model,startTime,endTime);	 
			             }else
			             {
			            	 sql= "select c_model as model,c_imei as imei ,n_lat as lat ,n_lng  as lng ,n_type as type,t_data_time as datatime from "+flag+" where n_type =1 and c_imei=? and c_model=? and  t_data_time BETWEEN ? and ? order by t_data_time ";
				              return DbHelper.queryForList(connect, sql,imei,model,startTime,endTime);	 
			             }
			         	   
			           }
			        });
			    }
	   
	   

}
