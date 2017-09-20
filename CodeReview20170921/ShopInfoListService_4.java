package com.bj58.chuangxin.service.shop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.bj58.chuangxin.component.FacadeComp;
import com.bj58.chuangxin.component.pangu.DispCateComp;
import com.bj58.chuangxin.component.pangu.LocalComp;
import com.bj58.chuangxin.component.shop.ShopBranchComp;
import com.bj58.chuangxin.component.shop.ShopPhoneComp;
import com.bj58.chuangxin.dto.shop.ShopBranchDTO;
import com.bj58.chuangxin.entity.SjtShopBranchEntity;
import com.bj58.chuangxin.entity.SjtShopPhoneEntity;
import com.bj58.chuangxin.pangu.entity.HyDispCategoryEntity;
import com.bj58.chuangxin.pangu.entity.HyLocalEntity;
import com.bj58.chuangxin.service.global.AppGlobalService;
import com.bj58.chuangxin.vo.JsonResult;
import com.bj58.chuangxin.vo.TData;
import com.bj58.fbf.common.IBeatContext;
import com.bj58.wf.log.Log;
import com.bj58.wf.log.LogFactory;
import com.bj58.wf.mvc.ActionResult;
import com.bj58.wf.util.StringUtil;
/**
* <p>Title:店铺信息管理list </p>
* <p>Description: </p>
* <p>Company:58同城 </p>
* @date 2017年6月26日
 */
public class ShopInfoListService extends AppGlobalService{
	private final ShopBranchComp shopBranchComp = FacadeComp.getFacade().getShopBranchComp();
	private final ShopPhoneComp shopPhoneComp = FacadeComp.getFacade().getShopPhoneComp();
	private final DispCateComp dispCateComp = FacadeComp.getFacade().getDispCateComp();
    private final LocalComp localComp = FacadeComp.getFacade().getLocalComp();
	private final Log logger = LogFactory.getLog(ShopInfoListService.class);
	
	@Override
	public ActionResult handle(IBeatContext t) throws Exception {
		ShopBranchDTO dto = t.getDTO();
        JsonResult<TData<String, Object>> result = JsonResult.newResult();
        TData<String, Object> data = new TData<String, Object>();
		Map<String, Object> list = buildShopInfo(dto);
		data.setData(list);
        result.setResult(data);
		dto.setResult(result);
		return null;
	}
	
	public Map<String, Object> buildShopInfo(ShopBranchDTO dto)throws Exception{
		Map<String, Object> shopMap = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		List<SjtShopBranchEntity> shopBranchList = null;
		int hintFlag=0;
		if(!StringUtil.isEmpty(dto.getId())){
			//加载更多
			long id = Long.valueOf(dto.getId());
			shopBranchList = shopBranchComp.getListByPage("user_id=? and id>? and state>0", 1, 30, "id asc", new Object[]{dto.getUserId(),id});
		}
		
		if(CollectionUtils.isNotEmpty(shopBranchList)){
			for(SjtShopBranchEntity entity:shopBranchList){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", entity.getId()+"");
				map.put("name", getShopName(entity));
				map.put("type", entity.getType());
				
				String cateName="";
				try {
					HyDispCategoryEntity dispCategoryEntity = dispCateComp.getDispCategoryEntityByCateID(entity.getCateId());
					if(dispCategoryEntity!=null){
						cateName = dispCategoryEntity.getCateName();
					}
				} catch (Exception e) {
					logger.error("根据归属类别id获取归属实体失败", e);
				}
				map.put("cateName", cateName);
				
				List<SjtShopPhoneEntity> shopPhoneList = shopPhoneComp.getListByPage("shop_branch_id=? and state=1", 1, 1, new Object[]{entity.getId()});
				SjtShopPhoneEntity shopPhoneEntity = nullOrFirst(shopPhoneList);
				if(shopPhoneEntity!=null){
					map.put("phone", null==shopPhoneEntity.getPhone()?"":shopPhoneEntity.getPhone());
				}else{
					map.put("phone", "");
				}
				
				String shopAddress="";
				if(!StringUtil.isEmpty(entity.getShopAddress())){
					shopAddress = entity.getShopAddress();
				}
				String detailAddress="";
				if(!StringUtil.isEmpty(entity.getDetailAddress())){
					detailAddress = entity.getDetailAddress();
				}
				String address = shopAddress+detailAddress;
				map.put("address", address);
				map.put("state", entity.getState());
				
				if("0".equals(dto.getId())&&hintFlag==0&&entity.getState()==2){
					hintFlag=1;
				}
				
				list.add(map);
			}
		}
		
		if(hintFlag==1){
			shopMap.put("hint", "店铺信息补充完整，才能让用户联系到您");
		}else{
			shopMap.put("hint", "");
		}
		
		shopMap.put("shopList", list);
		
		return shopMap;
	}
	
	private String getShopName(SjtShopBranchEntity entity)throws Exception{
		String name = "";
		int targetId=0;
		if(!StringUtil.isEmpty(entity.getName())){
			if(entity.getDistrictId()!=0){
				targetId = entity.getDistrictId();
			}else if(entity.getAreaId()!=0){
				targetId = entity.getAreaId();
			}else{
				targetId = entity.getCityId();
			}
			if(targetId!=0){
				try {
					HyLocalEntity localEntity = localComp.getLocalEntityByLocalID(targetId);
					if(localEntity!=null){
						name = entity.getName()+"("+localEntity.getLocalName()+"店)";
					}else{
						name = entity.getName();
					}
				} catch (Exception e) {
					name = entity.getName();
				}				
			}else{
				name = entity.getName();
			}
		}
		return name;
	}
	
}
