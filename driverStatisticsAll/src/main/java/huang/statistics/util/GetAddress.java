package huang.statistics.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import com.tonetime.commons.util.StringUtils;

public class GetAddress {
	

	public static String getIpAddr(HttpServletRequest request) {   
	     String ipAddress = null;   
	    
	     ipAddress = request.getHeader("x-forwarded-for");   
	     if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {   
	      ipAddress = request.getHeader("Proxy-Client-IP");   
	     }   
	     if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {   
	         ipAddress = request.getHeader("WL-Proxy-Client-IP");   
	     }   
	     if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {   
	      ipAddress = request.getRemoteAddr();   
	      if(ipAddress.equals("127.0.0.1")){   
	       //根据网卡取本机配置的IP   
	       InetAddress inet=null;   
	    try {   
	     inet = InetAddress.getLocalHost();   
	    } catch (UnknownHostException e) {   
	     e.printStackTrace();   
	    }   
	    ipAddress= inet.getHostAddress();   
	      }   
	            
	     }   
	  
	     //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割   
	     if(ipAddress!=null && ipAddress.length()>15){ //"***.***.***.***".length() = 15   
	         if(ipAddress.indexOf(",")>0){   
	             ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));   
	         }   
	     }   
	     return ipAddress;    
	  }  
	
	
	//获取IP地址2
	


	         public static String getIp2(HttpServletRequest request) {
	             String ip = request.getHeader("X-Forwarded-For");
	             if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
	                 //多次反向代理后会有多个ip值，第一个ip才是真实ip
	                 int index = ip.indexOf(",");
	                 if(index != -1){
	                     return ip.substring(0,index);
	                 }else{
	                     return ip;
                }
	            }
	            ip = request.getHeader("X-Real-IP");
	            if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
	                return ip;
	            }
	            return request.getRemoteAddr();
	        }
	
	
	
	
	
	
	
	
	
	
	
	
}
