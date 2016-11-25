package huang.statistics.test;

import huang.statistics.dao.BasicDao;
import huang.statistics.util.UserAccount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GruoupArea {
	// -----------------------------------------------转换到路径表
	public String transTravelDrive(String imei) {
		imei = imei.substring(13);
		String travel = "iov_travel_" + Integer.valueOf(imei) % 10;
		return travel;
	}

	public static void main(String[] args) throws Throwable {
		GruoupArea test = new GruoupArea();
		BasicDao dao = new BasicDao();
		long date1 = System.currentTimeMillis();
		List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> list3 = new ArrayList<Map<String, Object>>();

		List<List<Map<String, Object>>> list4 = new ArrayList<List<Map<String, Object>>>();

		String area[] = { "北京", "天津", "河北", "山西", "内蒙古", "辽宁", "吉林", "黑龙江", "上海", "江苏", "浙江", "安徽", "福建", "江西", "山东", "河南", "湖北", "湖南", "广东", "广西", "海南", "重庆", "四川", "贵州", "云南", "西藏", "陕西", "甘肃",
				"青海", "宁夏", "新疆", "台湾", "香港", "澳门" };
		// 获取有几个分组
		List<Map<String, Object>> listGroup = dao.diverRuleTimeGetGroup();
		// 或所有的的省；
		List<Map<String, Object>> listRegion = dao.diverRuleTimeGetRegion();
		// 清空数据；
		dao.clearTableData("iov_group_area");
		int province1 = 0, province2 = 0, province3 = 0, province4 = 0, province5 = 0, province6 = 0, province7 = 0, province8 = 0, province9 = 0, province10 = 0, province11 = 0, province12 = 0, province13 = 0, province14 = 0, province15 = 0, province16 = 0, province17 = 0, province18 = 0, province19 = 0, province20 = 0, province21 = 0, province22 = 0, province23 = 0, province24 = 0, province25 = 0, province26 = 0, province27 = 0, province28 = 0, province29 = 0, province30 = 0, province31 = 0, province32 = 0, province33 = 0, province34 = 0;

		int provinceNumber[] = { province1, province2, province3, province4, province5, province6, province7, province8, province9, province10, province11, province12, province13, province14,
				                 province15, province16, province17, province18, province19, province20, province21, province22, province23, province24, province25, province26, province27, province28, province29,
				                 province30, province31, province32, province33, province34 };
		for (Map<String, Object> map : listGroup) {
			String groupid = String.valueOf(map.get("n_id"));
			// 获取要统计的imei
			List<String> listImei = UserAccount.activeGetListImei3(groupid);
			System.out.println("----"+groupid+"--------"+listImei.size());
			
			List<Map<String, Object>> listNumber = new ArrayList<Map<String, Object>>();
			for (int k = 0; k < listImei.size(); k++) {
				
				String imei = listImei.get(k);
			
				String travel = test.transTravelDrive(imei);
				
				List<Map<String, Object>> list = dao.ruleActivityArea1(imei, travel);
			
				System.out.println("----travel--------"+travel);
				
				for (int j = 0; j < listRegion.size(); j++) {
					System.out.println("--imei "+imei+"--travel--------"+k*j+"-----"+list);
					String city = String.valueOf(listRegion.get(j).get("c_province"));
					String c_region1 = String.valueOf(listRegion.get(j).get("c_code"));
					String c_region = c_region1.substring(0, 2);
					for (Map<String, Object> region : list) {
						Object stRegion = region.get("c_st_region");
						if (stRegion != null) {
							String stRegion2 = String.valueOf(stRegion);
							String stRegion1 = stRegion2.substring(0, 2);
							if (c_region.equals(stRegion1)) {
								provinceNumber[j] = provinceNumber[j] + 1;
								break;
							}
						}
					}
					if(list.size()==0)
					{
						//获取激活时地域编码；
						List<Map<String, Object>> client =dao.basicQueryByString("iov_device_client", "c_imei=", imei);
						String region=String.valueOf(client.get(0).get("c_region"));
						if (c_region.equals(region)) {
							provinceNumber[j] = provinceNumber[j] + 1;
							break;
						}
					}
				}
			}
			
			for (int i = 0; i < listRegion.size(); i++) {
				Map<String, Object> map1 = new HashMap<String, Object>();
				map1.put("c_city", listRegion.get(i).get("c_province"));
				map1.put("c_group", groupid);
				map1.put("c_region", listRegion.get(i).get("c_code"));
				map1.put("c_number", provinceNumber[i]);
				dao.basicInsertData("iov_group_area", map1);
				provinceNumber[i]=0;
			}
		}
	}
}
