package com.bj58.chuangxin.service.shop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import com.bj58.chuangxin.common.exception.AppBaseException;
import com.bj58.chuangxin.component.FacadeComp;
import com.bj58.chuangxin.component.info.InfoComp;
import com.bj58.chuangxin.component.pangu.CateComp;
import com.bj58.chuangxin.component.pangu.DispCateComp;
import com.bj58.chuangxin.component.pangu.LocalComp;
import com.bj58.chuangxin.component.shop.ShopBranchComp;
import com.bj58.chuangxin.component.shop.ShopPhoneComp;
import com.bj58.chuangxin.dto.shop.ShopBranchDTO;
import com.bj58.chuangxin.entity.CateValueEntity;
import com.bj58.chuangxin.entity.SjtShopBranchEntity;
import com.bj58.chuangxin.entity.SjtShopPhoneEntity;
import com.bj58.chuangxin.enums.AppResultStateEnum;
import com.bj58.chuangxin.pangu.entity.HyCategoryEntity;
import com.bj58.chuangxin.pangu.entity.HyDispCategoryEntity;
import com.bj58.chuangxin.pangu.entity.HyLocalEntity;
import com.bj58.chuangxin.service.global.AppGlobalService;
import com.bj58.chuangxin.vo.JsonResult;
import com.bj58.chuangxin.vo.TData;
import com.bj58.chuangxin.vo.info.InfoResult;
import com.bj58.fbf.common.IBeatContext;
import com.bj58.sfft.imc.entity.Info;
import com.bj58.wf.mvc.ActionResult;
import com.bj58.wf.util.StringUtil;

public class ShopInfoService extends AppGlobalService{
	private final ShopBranchComp shopBranchComp = FacadeComp.getFacade().getShopBranchComp();
	private final ShopPhoneComp shopPhoneComp = FacadeComp.getFacade().getShopPhoneComp();
	private final CateComp cateComp = FacadeComp.getFacade().getCateComp();
	private final InfoComp infoComp = FacadeComp.getFacade().getInfoComp();
	private final DispCateComp dispCateComp = FacadeComp.getFacade().getDispCateComp();
	private final LocalComp localComp = FacadeComp.getFacade().getLocalComp();
	
	@Override
	public ActionResult handle(IBeatContext t) throws Exception {
		ShopBranchDTO dto = t.getDTO();
        JsonResult<TData<String, Object>> result = JsonResult.newResult();
        TData<String, Object> data = new TData<String, Object>();
		Map<String, Object>  map = buildShopInfo(dto);
		data.setData(map);
        result.setResult(data);
		dto.setResult(result);
		return null;
	}
	
	public Map<String, Object> buildShopInfo(ShopBranchDTO dto)throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		if(!StringUtil.isEmpty(dto.getId())){
			long id = Long.valueOf(dto.getId());
			if(id>0){
				SjtShopBranchEntity entity = shopBranchComp.getEntityById(id);
				if(entity!=null){
					map.put("id", entity.getId()+"");
					map.put("cateList", buildInfoResult(dto, entity.getCateId()));
					//获取分店区域
					map.put("cityId", entity.getCityId());
					map.put("areaId", entity.getAreaId());
					map.put("districtId",entity.getDistrictId());
					map.put("areaName", null==getAreaName(entity, map)?"":getAreaName(entity, map));
					map.put("name",null==entity.getName()?"":entity.getName());
					//电话
					List<SjtShopPhoneEntity> phoneList = shopPhoneComp.getListByPage("shop_branch_id=? and state=1", 1, 1, new Object[]{entity.getId()});
					SjtShopPhoneEntity phoneEntity = nullOrFirst(phoneList);
					if(phoneEntity!=null){
						map.put("phone", null==phoneEntity.getPhone()?"":phoneEntity.getPhone());
					}else{
						map.put("phone", "");
					}
					map.put("shopAddress", null==entity.getShopAddress()?"":entity.getShopAddress());
					map.put("longitude", entity.getLongitude());
					map.put("latitude", entity.getLatitude());
					map.put("detailAddress", null==entity.getDetailAddress()?"":entity.getDetailAddress());
				}
			}
		}
		return map;
	}
	
	
	public List<CateValueEntity> buildInfoResult(ShopBranchDTO dto,int cateid)throws Exception{
		 InfoResult infoResult = infoComp.getInfoListBySes(1, 100, "userId = " + dto.getUserId()+"&sort=adddate_desc");
		 Map<String, CateValueEntity> map = new HashMap<String, CateValueEntity>();
		 if(infoResult!=null){
			 List<Info> infoList = infoResult.getInfos();
			 if(CollectionUtils.isNotEmpty(infoList)){
				  Set<String> cateIdSet = new HashSet<String>();
				  for(Info info:infoList){
					  cateIdSet.add(info.getCateID()+"");
				  }
				  for(String cateId : cateIdSet){
					  CateValueEntity cateBean = getCateBean((int)Integer.valueOf(cateId),cateid);
					  if(cateBean!=null){
						  map.put(cateBean.getId()+"", cateBean);
					  }
			      } 
			 }
		 }else{
			 throw new AppBaseException(AppResultStateEnum.BUSINESS_EXCEPTION.getCode(),"您还没发过贴，发过贴才可以设置店铺");  
		 }
		 return new ArrayList<CateValueEntity>(map.values());
	}
	
	private CateValueEntity getCateBean(int cateId,int cateid) throws Exception {
		 //获取此类目的 级别
	     HyCategoryEntity categoryEntity = cateComp.getCategoryEntityByCateID(cateId);
	     if (categoryEntity == null) {
	            return null;
	     }

	     int targetCateId = 0;

	     String fullPath = categoryEntity.getFullPath();
	     String[] ids = fullPath.split(",");
	     if (ids.length >= 2) {
	        targetCateId = Integer.valueOf(ids[1]);
	     }

	     //归属id转成表现id
	     HyDispCategoryEntity dispCategoryEntity = dispCateComp.getDispCategoryEntityByCateID(targetCateId);
	     if (dispCategoryEntity == null) {
	         return null;
	     }
         
	     CateValueEntity entity = new CateValueEntity();
	     entity.setId(dispCategoryEntity.getDispCategoryID());
	     entity.setName(dispCategoryEntity.getCateName());
	     
	     if(cateid==targetCateId){
	    	 entity.setCheck(1);//选中
	     }
	     
	     return entity;
	}
    
    public String getAreaName(SjtShopBranchEntity entity,Map<String, Object> map)throws Exception{
    	  StringBuffer sbuff = new StringBuffer();
    	  
    	  HyLocalEntity cityEntity = localComp.getLocalEntityByLocalID(entity.getCityId());
    	  if(cityEntity!=null){
    		  sbuff.append(cityEntity.getLocalName());
    	  }
    	  
    	  HyLocalEntity  areaEntity = localComp.getLocalEntityByLocalID(entity.getAreaId());
    	 
    	  if(areaEntity!=null){
    		  sbuff.append(areaEntity.getLocalName());
    	  }
    	  HyLocalEntity  distinctEntity = localComp.getLocalEntityByLocalID(entity.getDistrictId());
     	 
    	  if(distinctEntity!=null){
    		  sbuff.append(distinctEntity.getLocalName());
    	  }
    	 return sbuff.toString(); 
    }
}
