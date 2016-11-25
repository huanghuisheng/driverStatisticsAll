package huang.statistics.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.tonetime.commons.database.DataSourceBuilder;
import com.tonetime.commons.database.helper.DbHelper;
import com.tonetime.commons.database.helper.JdbcCallback;
@Repository
public class BasicDao {

	public static DataSourceBuilder builder = null;
//	Map<String, Object> map = new HashMap<String, Object>();
	
	public BasicDao(){
		 DataSourceBuilder.getInstance().getDataSourceCluster();
		 builder = DataSourceBuilder.getInstance();		 
	}
	  //查询出来再过滤
	   @SuppressWarnings("unchecked")
		public List<Map<String, Object>> divercategoryTime1(final String code ,final String imei,final String travel) throws Exception {
		        return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
		            public Object doInJdbc(Connection connect) throws SQLException, Exception {
		         	  String sql=null;
		         	  if(code.equals("allcode"))
		         	  {
		         		 sql= "SELECT   t_st_time ,t_ed_time FROM  "+travel+
			         		  " WHERE  c_imei=?"; 
		         		 return DbHelper.queryForList(connect, sql,imei);
		         	  }else{
		         		 sql= "SELECT   t_st_time ,t_ed_time FROM  "+travel+
		         			  " WHERE  c_imei=? and ((c_st_region like ?) or (c_ed_region like ?) )";
		         		 return DbHelper.queryForList(connect, sql,imei,code+"%",code+"%");
		         	  }
	 
		           }
		        });
		    }
	
	
	
	
	
	
	
	
	
	
	
	
	   // 先查所有的然后在过滤；
			@SuppressWarnings("unchecked")
			public List<Map<String, Object>> ruleActivityArea1(final String imei,final String travel) throws Exception {
		        return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
		            public Object doInJdbc(Connection connect) throws SQLException, Exception {
		         	  String sql=null;       	  
		         		 sql= "select c_st_region from  "+travel+"  where c_imei=?  GROUP BY c_st_region";  
		         		 return DbHelper.queryForList(connect, sql,imei);
		           }
		        });
		    }
	
	
	// 插入用户操作日志；
		// 插入数据
		public static Object operationLogInsertData(final String name, final String address, final String type, final String content, final Date create_time, final String group) throws Exception {
			return (Object) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
				public Object doInJdbc(Connection connect) throws SQLException, Exception {
					Map<String, Object> fields = new HashMap<String, Object>();
					fields.put("c_name", name);
					fields.put("c_address", address);
					fields.put("c_type", type);
					fields.put("c_content", content);
					fields.put("t_create_time", create_time);
					fields.put("c_group", group);
					String tableName = "iov_operation_log";
					return DbHelper.insertForRet(connect, tableName, fields);
				}
			});
		}	
	
	
	
	
	
	
	
   
  
	
	
  	
  
	
	
	
	//分页查询用户列表
	@SuppressWarnings("unchecked")
	public   List<Map<String, Object>> queryUserList(final int beginIndex,final String order,final String sort,final Date time1,final Date time2,final String timeFlag) throws Exception {
        return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
        	public Object doInJdbc(Connection connect) throws SQLException, Exception {	
                String sql=null;
               /* sql= "select * from iov_statistics_userlist      ORDER BY "+" "+sort+" " + order +" limit ?,10 "; */
                
                sql= "select * from iov_statistics_userlist where t_statistics_time =?  ORDER BY "+" "+sort+" " + order +" limit ?,10 "; 
                
                return DbHelper.queryForList(connect,sql,timeFlag,beginIndex);
        	}  
        });
    } 

	//查询所有的用户列表数量；
	@SuppressWarnings("unchecked")
	public   List<Map<String, Object>> queryUserSum(final int groupId,final String timeFlag) throws Exception {
        return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
        	public Object doInJdbc(Connection connect) throws SQLException, Exception {	
                String sql=null;
                if(groupId==0){
                	  sql= "select  count(*) as number from iov_statistics_userlist where t_statistics_time=?";              
                      return DbHelper.queryForList(connect,sql,timeFlag);	
                }else{
                	 sql= "select  count(*) as number from iov_statistics_userlist where c_department=? and t_statistics_time=? ";              
                     return DbHelper.queryForList(connect,sql,groupId,timeFlag);	
                }
        	}  
        });
    }
	//查询所有的用户列表数量；
	@SuppressWarnings("unchecked")
	public   List<Map<String, Object>> queryUserSum1(final int groupId,final Date time1,final Date time2,final String timeFlag) throws Exception {
        return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
        	public Object doInJdbc(Connection connect) throws SQLException, Exception {	
                String sql=null;
                if(groupId==0)
                {
                	 sql= "select  count(*) as number from iov_statistics_userlist where t_statistics_time =? ";              
                     return DbHelper.queryForList(connect,sql,timeFlag);	
                }else{
                	  sql= "select  count(*) as number from iov_statistics_userlist where  c_department=? and t_statistics_time =? ";              
                      return DbHelper.queryForList(connect,sql,groupId,timeFlag);
                }
        	}  
        });
    }
	
	
	
	//查询所有的分组imei号列表数量；
		@SuppressWarnings("unchecked")
		public   List<Map<String, Object>> queryGroupImeiSum(final int groupid) throws Exception {
	        return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
	        	public Object doInJdbc(Connection connect) throws SQLException, Exception {	
	                String sql=null;
	                
	                sql= "select  count(*) as number from iov_administrator_group_imei where c_group_id=?";              
	                return DbHelper.queryForList(connect,sql,groupid);
	        	}  
	        });
	    }
	
		//分页查询二级用户账号
		@SuppressWarnings("unchecked")
		public   List<Map<String, Object>> querySecondImeiList( final int groupid,final int offset) throws Exception {
	        return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
	        	public Object doInJdbc(Connection connect) throws SQLException, Exception {	
	                String sql=null;
	                sql= "select * from iov_device_client  where  c_group=?   limit ?,10";              
	                return DbHelper.queryForList(connect,sql,groupid,offset);
	        	}  
	        });
	    }
		//查询二级用户账号imei号数量
		@SuppressWarnings("unchecked")
		public   List<Map<String, Object>> querySecondImeiSum( final int groupid) throws Exception {
	        return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
	        	public Object doInJdbc(Connection connect) throws SQLException, Exception {	
	                String sql=null;
	                sql= "select count(*) as number from iov_device_client  where  c_group=?";              
	                return DbHelper.queryForList(connect,sql,groupid);
	        	}  
	        });
	    }
		
		
		


		//分页查询用户列表
		@SuppressWarnings("unchecked")
		public   List<Map<String, Object>> queryGroupImeiList( final int groupid,final int offset) throws Exception {
	        return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
	        	public Object doInJdbc(Connection connect) throws SQLException, Exception {	
	                String sql=null;
	                sql= "select * from iov_administrator_group_imei  where  c_group_id=?   limit ?,10";              
	                return DbHelper.queryForList(connect,sql,groupid,offset);
	        	}  
	        });
	    }
		
		
		
		
	//分组模糊查询
		
		@SuppressWarnings("unchecked")
		public   List<Map<String, Object>> queryFuzzyAllImeiList(final String imei) throws Exception {
	        return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
	        	public Object doInJdbc(Connection connect) throws SQLException, Exception {	
	                String sql=null;
	                
	                sql= "select * from iov_administrator_group_imei  where  c_imei  like? limit 0,10";              
	                return DbHelper.queryForList(connect,sql,imei);
	        	}  
	        });
	    }		
		
		
		
	//分组模糊查询
		
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryFuzzyGroupImeiList(final String imei, final String groupid) throws Exception {
		return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
			public Object doInJdbc(Connection connect) throws SQLException, Exception {
				String sql = null;
				System.out.println("ssss-------"+groupid);
				if (null==groupid) {
					sql = "select * from iov_administrator_group_imei  where  c_imei  like? limit 0,10";
					return DbHelper.queryForList(connect, sql, imei);	
				}else{
					sql = "select * from iov_administrator_group_imei  where  c_group_id=? and  c_imei  like? limit 0,10";
					return DbHelper.queryForList(connect, sql, groupid, imei);
				}

			}
		});
	}	
		
	
		//二级用户imei模糊查询
		@SuppressWarnings("unchecked")
		public   List<Map<String, Object>> querySecondFuzzyImeiList( final int groupid,final String imei) throws Exception {
	        return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
	        	public Object doInJdbc(Connection connect) throws SQLException, Exception {	
	                String sql=null;
	                System.out.println("imei is ---"+imei+"iiii"+groupid);
	                
	                sql= "select * from iov_device_client  where  c_group=? and  c_imei  like? limit 0,10";              
	                return DbHelper.queryForList(connect,sql,groupid,imei);
	        	}  
	        });
	    }	
		
		
		
	
	
	
	
	
	
	
	
	
	//---------------------------------------------------------------------------公共函数
	  //插入数据
    public  Object basicInsertData( final String tableName,final Map<String, Object> fields) throws Exception {
        return (Object)  DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
            public Object doInJdbc(Connection connect) throws SQLException, Exception {  
              return DbHelper.insertForRet(connect, tableName,fields);
           }
        });   
    }
    
    //修改信息；
    
    public  Object basicUpdate(final String tableName, final Map<String, Object> fields,final String condition) throws Exception {
        return (Object)  DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
            public Object doInJdbc(Connection connect) throws SQLException, Exception {  
            	 return DbHelper.update( connect,tableName,  fields, condition); 
           }
        });   
    }
    
	//删除信息；
    
    public  Object basicDeleteData(final String tableName ,final int id ) throws Exception {
        return (Object) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
            public Object doInJdbc(Connection connect) throws SQLException, Exception {
              String sql=null;
              sql= "DELETE FROM  "+tableName+" where n_id=?";
              return DbHelper.executeUpdate(connect, sql,id);
           }
        });
    }
    
    //根据imei删除信息；
    
    public  Object basicDeleteImeiData(final String tableName ,final String imei ) throws Exception {
        return (Object) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
            public Object doInJdbc(Connection connect) throws SQLException, Exception {
              String sql=null;
              sql= "DELETE FROM  "+tableName+" where c_imei=?";
              return DbHelper.executeUpdate(connect, sql,imei);
           }
        });
    }
    
   
    

	//根据id查询； 
	@SuppressWarnings("unchecked")
	public  List<Map<String, Object>> basicQueryById(final String tableName,final int id) throws Exception {
        return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
            public Object doInJdbc(Connection connect) throws SQLException, Exception {	
              String sql=null;
              
              sql= "select * from "+tableName+" where n_id= ?";              
              return DbHelper.queryForList(connect,sql,id);
           }
        });
    } 
    
    
	//根据条件字符串查询； 
		@SuppressWarnings("unchecked")
		public  List<Map<String, Object>> basicQueryByString(final String tableName,final String field,final String name) throws Exception {
	        return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
	            public Object doInJdbc(Connection connect) throws SQLException, Exception {	
	              String sql=null;
	              sql= "select * from "+tableName+" where "+field;  
	              return DbHelper.queryForList(connect,sql,name);
	           }
	        });
	    } 
		
    
    //根据map条件来查询 动态查询；
		
		@SuppressWarnings("unchecked")
		public   List<Map<String, Object>> basicQueryByFields(final String tableName,final Map<String, Object> fields) throws Exception {
	        return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
	            public Object doInJdbc(Connection connect) throws SQLException, Exception {	

	            
	              return queryForRet(connect,tableName,fields);
	           }
	        });
	    } 
		
		
		
		
		
		
		
		public  List<Map<String, Object>>   queryForRet(Connection connect, String tableName, Map<String, Object> fields) throws SQLException{
			
	      	System.out.println("sssssssssssssssssssss2");
			if(fields.size()<1){
				return null;
			}
	      	System.out.println("sssssssssssssssssssss3");
			StringBuffer sb = new StringBuffer("SELECT * from ");
			sb.append(tableName).append(" WHERE"+" ");
			Iterator<String> fds = fields.keySet().iterator();
			while(fds.hasNext()){
				sb.append(fds.next()+"=?");
				if(fds.hasNext()){
					sb.append(" AND ");
				}
			}
			sb.append(" ");

			
			System.out.println("拼接字符串是--------"+sb);
			
			
			
//			int index = 1;
			PreparedStatement prep = connect.prepareStatement(sb.toString());
//			if(params!=null){
//				for (Object param : params) {
//					prep.setObject(index++, param);
//				}
//			}
//			
			fds = fields.keySet().iterator();
			int index = 1;
			while(fds.hasNext()){
				Object v = fields.get(fds.next());
				prep.setObject(index++, v);
			}
			
			
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			ResultSet result = prep.executeQuery();
			ResultSetMetaData medata = result.getMetaData();
			int columnCount = medata.getColumnCount();
			while (result.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (index = 0; index < columnCount; index++) {
					String colName = medata.getColumnLabel(index + 1);
					map.put(colName, result.getObject(colName));
				}
				resultList.add(map);
			}
			result.close();
			prep.close();

			return resultList;
		}
    
    
		
		
		
		
		
	//-----------------------------------------
		//获取用户拥有的imei号
		@SuppressWarnings("unchecked")
		public static List<Map<String, Object>> userGetMobileImei(final String type,final String department) throws Exception {
	        return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
	            public Object doInJdbc(Connection connect) throws SQLException, Exception {
	         	   String sql=null;
	         	   if(type.equals("1"))
	         	   {
	         		  sql= "SELECT  * from  iov_device_client  where n_id>1206 and n_id <1301";		              
		              return DbHelper.queryForList(connect, sql);       
	         	   }
	         	   else
	         	   {   	
	         		   System.out.println("-----------sss---");
	         		  sql= "SELECT  * from  iov_device_client where c_group=?";		              
		              return DbHelper.queryForList(connect, sql,department);      
	         	   }           
	           }
	        });
	    } 
		//筛选获取已经激活imei号
		@SuppressWarnings("unchecked")
		public static List<Map<String, Object>> userGetActiveMobileImei(final String imei) throws Exception {
	        return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
	            public Object doInJdbc(Connection connect) throws SQLException, Exception {
	         	   String sql=null;
	         	   sql= "SELECT  c_imei from  iov_device where c_imei=?";		              
		           return DbHelper.queryForList(connect, sql,imei);       
	          
	           }
	        });
	    }	
		//分省查激活imei
		@SuppressWarnings("unchecked")
		public static List<Map<String, Object>> userGetActiveMobileImei1(final String imei,final String code) throws Exception {
	        return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
	            public Object doInJdbc(Connection connect) throws SQLException, Exception {
	         	   String sql=null;
	              
	         	   if(code.equals("allcode"))
	         	   {
	         		   sql= "SELECT  c_imei from  iov_device where c_imei=?";		              
			           return DbHelper.queryForList(connect, sql,imei);  
	         	   }else
	         	   {
	         		   sql= "SELECT  c_imei from  iov_device where c_imei=? and c_region_code like ?";		              
			           return DbHelper.queryForList(connect, sql,imei,code+'%');  
	         	   }
	          
	           }
	        });
	    }	
		
		
		
		@SuppressWarnings("unchecked")
		public  List<Map<String, Object>> basicQueryALL(final String tableName) throws Exception {
	        return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
	            public Object doInJdbc(Connection connect) throws SQLException, Exception {	
	              String sql=null;
	              sql= "select * from "+tableName;              
	              return DbHelper.queryForList(connect,sql);
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
		
		
		   //--------------------------------------获取单个用户的路径表数据；  
		   @SuppressWarnings("unchecked")
		public List<Map<String, Object>> singleGetDiverTravel(final String  flag ,final String imei ,final Date time1,final Date time2) throws Exception {
		        return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
		            public Object doInJdbc(Connection connect) throws SQLException, Exception {
		         	   String sql=null;
				          sql= "select * from "+flag+" where  c_imei=?  and t_st_time >=? and t_ed_time <=?";
				          return DbHelper.queryForList(connect, sql,imei,time1,time2);
		           }
		        });
		   }
		
		
		//-----------------------------------------获取地域信息
		   @SuppressWarnings("unchecked")
			public List<Map<String, Object>> singleGetDiverRegion(final String imei) throws Exception {
			        return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
			            public Object doInJdbc(Connection connect) throws SQLException, Exception {
			         	   String sql=null;
					          sql= "SELECT c_region_code FROM iov_t.iov_device where c_imei=? limit 1";
					          return DbHelper.queryForList(connect, sql,imei);
			           }
			        });
			   }  
		
		   //---------查询完再过滤
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> diverRuleTime1(final String code, final String imei, final String travel) throws Exception {
		return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
			public Object doInJdbc(Connection connect) throws SQLException, Exception {
				String sql = null;
				if (code.equals("allcode")) {
					System.out.println("5---------------" + imei);
					sql = "SELECT   t_st_time ,t_ed_time FROM  " + travel + " WHERE  c_imei = ?";
					return DbHelper.queryForList(connect, sql, imei);
				} else {
						System.out.println("6---------------" + imei);
					sql = "SELECT   t_st_time ,t_ed_time FROM  " + travel + " WHERE  c_imei = ? and ((c_st_region like ?) or (c_ed_region like ?) )";
					return DbHelper.queryForList(connect, sql, imei, code + "%", code + "%");
				}

			}
		});
	}
		
	   //---------查询完再过滤
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> diverRuleTimeGetRegion() throws Exception {
		return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
			public Object doInJdbc(Connection connect) throws SQLException, Exception {
				String sql = null;
					sql = "SELECT * FROM code_region group by c_province";
					return DbHelper.queryForList(connect, sql);
			}
		});
	}	
	
	//---------------获取分组；
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> diverRuleTimeGetGroup() throws Exception {
		return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
			public Object doInJdbc(Connection connect) throws SQLException, Exception {
				String sql = null;
					sql = "SELECT * FROM iov_administrator_group";
					return DbHelper.queryForList(connect, sql);
			}
		});
	}	
		
		
	 //清空数据表；
    public  Object clearTableData(final String table) throws Exception {
        return (Object) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
            public Object doInJdbc(Connection connect) throws SQLException, Exception {
              String sql=null;
              sql= "TRUNCATE  TABLE " +table;
              return DbHelper.executeUpdate(connect, sql);
           }
        });
    }
   //获取所有的数据;
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> dataTable(final String table) throws Exception {
		return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
			public Object doInJdbc(Connection connect) throws SQLException, Exception {
				String sql = null;
					sql = "SELECT * FROM "+table;
					return DbHelper.queryForList(connect, sql);
			}
		});
	}
	//查询激活地点;
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> dataRegion(final String imei) throws Exception {
		return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
			public Object doInJdbc(Connection connect) throws SQLException, Exception {
				String sql = null;
					sql = "SELECT  c_region_code from  iov_device where c_imei=?";
					return DbHelper.queryForList(connect, sql,imei);
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
	

	   
	   
	   //获取设备的所属地；
	   
			   @SuppressWarnings("unchecked")
				public List<Map<String, Object>> deviceDovRegion(final String model,final String imei) throws Exception {
			        return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
			            public Object doInJdbc(Connection connect) throws SQLException, Exception {
			         	   String sql=null;
			            
			            	 sql= "SELECT c_region_code from iov_dev_ol where c_imei =? and c_model=?  and (c_region_code is not NULL) and (c_region_code !=?) ORDER BY t_online ASC ";
				              return DbHelper.queryForList(connect, sql,imei,model,"");	 
			             
			         	   
			           }
			        });
			    }
	   
	   
			   @SuppressWarnings("unchecked")
						public List<Map<String, Object>> deviceTrackRegion(final String model,final String imei,final String flag) throws Exception {
					        return (List<Map<String, Object>>) DbHelper.execute(builder.getDataSourceCluster().getReadableDataSource(), new JdbcCallback() {
					            public Object doInJdbc(Connection connect) throws SQLException, Exception {
					         	   String sql=null;
					            
					            	 sql= "SELECT  n_lng,n_lat from "+flag+" where c_imei=?  and n_lng !=0 and n_lat!=0 limit 5";
						              return DbHelper.queryForList(connect, sql,imei);	 
					             
					         	   
					           }
					        });
					    }
	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
}
