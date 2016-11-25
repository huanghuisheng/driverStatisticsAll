package huang.statistics.util;

public class CoordinateUtil
{
  public static final String BAIDU_LBS_TYPE = "bd09ll";
  public static double pi = 3.141592653589793D;
  public static double a = 6378245.0D;
  public static double ee = 0.006693421622965943D;
  private static final double EARTH_RADIUS = 6378.1369999999997D;
  
  public static double[] wgs2gcj02(double lat, double lng)
  {
    return gps84ToGcj02(lat, lng);
  }
  
  public static double[] gps84ToGcj02(double lat, double lon)
  {
    if (outOfChina(lat, lon)) {
      return new double[] { lat, lon };
    }
    double dLat = transformLat(lon - 105.0D, lat - 35.0D);
    double dLon = transformLon(lon - 105.0D, lat - 35.0D);
    double radLat = lat / 180.0D * pi;
    double magic = Math.sin(radLat);
    magic = 1.0D - ee * magic * magic;
    double sqrtMagic = Math.sqrt(magic);
    dLat = dLat * 180.0D / (a * (1.0D - ee) / (magic * sqrtMagic) * pi);
    dLon = dLon * 180.0D / (a / sqrtMagic * Math.cos(radLat) * pi);
    double mgLat = lat + dLat;
    double mgLon = lon + dLon;
    return new double[] { mgLat, mgLon };
  }
  
  public static double[] gcjToGps84(double lat, double lon)
  {
    double[] gps = transform(lat, lon);
    double lontitude = lon * 2.0D - gps[1];
    double latitude = lat * 2.0D - gps[0];
    return new double[] { latitude, lontitude };
  }
  
  public static double[] gcj02ToBd09(double gglat, double gglon)
  {
    double x = gglon;double y = gglat;
    double z = Math.sqrt(x * x + y * y) + 2.E-005D * Math.sin(y * pi);
    double theta = Math.atan2(y, x) + 3.E-006D * Math.cos(x * pi);
    double bdlon = z * Math.cos(theta) + 0.0065D;
    double bdlat = z * Math.sin(theta) + 0.006D;
    return new double[] { bdlat, bdlon };
  }
  
  public static double[] bd09ToGcj02(double bdlat, double bdlon)
  {
    double x = bdlon - 0.0065D;double y = bdlat - 0.006D;
    double z = Math.sqrt(x * x + y * y) - 2.E-005D * Math.sin(y * pi);
    double theta = Math.atan2(y, x) - 3.E-006D * Math.cos(x * pi);
    double gglon = z * Math.cos(theta);
    double gglat = z * Math.sin(theta);
    return new double[] { gglat, gglon };
  }
  
  public static double[] bd09ToGps84(double bdlat, double bdlon)
  {
    double[] gcj02 = bd09ToGcj02(bdlat, bdlon);
    double[] map84 = gcjToGps84(gcj02[0], gcj02[1]);
    return map84;
  }
  
  public static double[] gps84Tobd09(double bdlat, double bdlon)
  {
    double[] gcj02 = gps84ToGcj02(bdlat, bdlon);
    double[] bd09 = gcj02ToBd09(gcj02[0], gcj02[1]);
    return bd09;
  }
  
  public static boolean outOfChina(double lat, double lon)
  {
    if ((lon < 72.004000000000005D) || (lon > 137.8347D)) {
      return true;
    }
    if ((lat < 0.8293D) || (lat > 55.827100000000002D)) {
      return true;
    }
    return false;
  }
  
  public static double[] transform(double lat, double lon)
  {
    if (outOfChina(lat, lon)) {
      return new double[] { lat, lon };
    }
    double dLat = transformLat(lon - 105.0D, lat - 35.0D);
    double dLon = transformLon(lon - 105.0D, lat - 35.0D);
    double radLat = lat / 180.0D * pi;
    double magic = Math.sin(radLat);
    magic = 1.0D - ee * magic * magic;
    double sqrtMagic = Math.sqrt(magic);
    dLat = dLat * 180.0D / (a * (1.0D - ee) / (magic * sqrtMagic) * pi);
    dLon = dLon * 180.0D / (a / sqrtMagic * Math.cos(radLat) * pi);
    double mgLat = lat + dLat;
    double mgLon = lon + dLon;
    return new double[] { mgLat, mgLon };
  }
  
  public static double transformLat(double x, double y)
  {
    double ret = -100.0D + 2.0D * x + 3.0D * y + 0.2D * y * y + 0.1D * x * y + 0.2D * Math.sqrt(Math.abs(x));
    ret += (20.0D * Math.sin(6.0D * x * pi) + 20.0D * Math.sin(2.0D * x * pi)) * 2.0D / 3.0D;
    ret += (20.0D * Math.sin(y * pi) + 40.0D * Math.sin(y / 3.0D * pi)) * 2.0D / 3.0D;
    ret += (160.0D * Math.sin(y / 12.0D * pi) + 320.0D * Math.sin(y * pi / 30.0D)) * 2.0D / 3.0D;
    return ret;
  }
  
  public static double transformLon(double x, double y)
  {
    double ret = 300.0D + x + 2.0D * y + 0.1D * x * x + 0.1D * x * y + 0.1D * Math.sqrt(Math.abs(x));
    ret += (20.0D * Math.sin(6.0D * x * pi) + 20.0D * Math.sin(2.0D * x * pi)) * 2.0D / 3.0D;
    ret += (20.0D * Math.sin(x * pi) + 40.0D * Math.sin(x / 3.0D * pi)) * 2.0D / 3.0D;
    ret += (150.0D * Math.sin(x / 12.0D * pi) + 300.0D * Math.sin(x / 30.0D * pi)) * 2.0D / 3.0D;
    return ret;
  }
  
  private static double rad(double d)
  {
    return d * 3.141592653589793D / 180.0D;
  }
  
  public static double getDistance(double lng1, double lat1, double lng2, double lat2)
  {
    double radLat1 = rad(lat1);
    double radLat2 = rad(lat2);
    double deltaLat = radLat1 - radLat2;
    double deltaLng = rad(lng1) - rad(lng2);
    double s = 2.0D * Math.asin(Math.sqrt(Math.pow(Math.sin(deltaLat / 2.0D), 2.0D) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(deltaLng / 2.0D), 2.0D)));
    s *= 6378.1369999999997D;
    
    return s * 1000.0D;
  }
  
  public static void main(String[] args)
  {
    double[] gps = { 31.426895999999999D, 119.496145D };
    System.out.println("gps :" + gps);
    double[] gcj = gps84ToGcj02(gps[0], gps[1]);
    System.out.println("gcj :" + gcj);
    double[] star = gcjToGps84(gcj[0], gcj[1]);
    System.out.println("star:" + star);
    double[] bd = gcj02ToBd09(gcj[0], gcj[1]);
    System.out.println("bd  :" + bd);
    double[] gcj2 = bd09ToGcj02(bd[0], bd[1]);
    System.out.println("gcj :" + gcj2);
  }
}
