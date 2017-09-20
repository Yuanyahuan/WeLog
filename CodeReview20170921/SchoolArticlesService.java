package com.bj58.chuangxin.service.busischool;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import com.bj58.chuangxin.common.utils.Sqls;
import com.bj58.chuangxin.common.utils.TimeUtils;
import com.bj58.chuangxin.component.busischool.SchoolArticleComp;
import com.bj58.chuangxin.component.file.FileComp;
import com.bj58.chuangxin.component.pay.HyOrderPayComp;
import com.bj58.chuangxin.dto.busischool.SchoolArticleDTO;
import com.bj58.chuangxin.entity.SjtSchoolArticleEntity;
import com.bj58.chuangxin.enums.AppResultStateEnum;
import com.bj58.chuangxin.enums.SchoolArticleCategoryEnum;
import com.bj58.chuangxin.enums.SchoolArticleStateEnum;
import com.bj58.chuangxin.service.global.AppGlobalService;
import com.bj58.chuangxin.vo.JsonResult;
import com.bj58.chuangxin.vo.TDataList;
import com.bj58.fbf.common.IBeatContext;
import com.bj58.wf.log.Log;
import com.bj58.wf.log.LogFactory;
import com.bj58.wf.mvc.ActionResult;
import com.bj58.wf.util.StringUtil;

public class SchoolArticlesService extends AppGlobalService {
    private final SchoolArticleComp schoolArticleComp = this.getFacadeComponent().getSchoolArticleComp();
    private final HyOrderPayComp hyOrderPayComp = this.getFacadeComponent().getHyOrderPayComp();
    private final FileComp fileComp = this.getFacadeComponent().getFileComp();
    private final Log logger = LogFactory.getLog(SchoolArticlesService.class);
    @Override
    public ActionResult handle(IBeatContext iBeatContext) throws Exception {
        SchoolArticleDTO dto = iBeatContext.getDTO();

        JsonResult<TDataList<Map<String, Object>>> result = JsonResult.newResult();
        TDataList<Map<String, Object>> data = new TDataList<Map<String, Object>>();
        result.setAndroidStatus(AppResultStateEnum.ANDROID_SUCCESS.getCode());
        dto.setResult(result);

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        buildArticlesListData(dto, list);
        

        data.setData(list);
        result.setResult(data);

        return null;
    }
    
    private void buildArticlesListData(SchoolArticleDTO dto,List<Map<String, Object>> list)throws Exception{
    	long sortId = dto.getSortId();
        Sqls sqls = Sqls.create().conIn("state", new short[]{SchoolArticleStateEnum.SHOW.getValue()});
               
    	if(dto.getCategoryId()!=0){
    		if(!StringUtil.isEmpty(SchoolArticleCategoryEnum.getMsgByCode(dto.getCategoryId()))){
    			if(SchoolArticleCategoryEnum.ALL_COURSE.getCode()==dto.getCategoryId()){
    				//全部课程 //0-默认排序  10-浏览量优先 20-发表时间由近及远  30-发表时间由远及近
    				sqls.conEql("type", 2);
    			}else{
    				sqls.conEql("category", dto.getCategoryId());
    			}
    			
    			if(dto.getQueryType()==0||dto.getQueryType()==20){
    				if(sortId>0){
    					sqls.conLittleThan("sort_id", sortId);
    				}
					List<SjtSchoolArticleEntity> schoolArticleEntityList = schoolArticleComp.getListByPage(sqls.getCondition(), 1, 10, "sort_id desc", sqls.getParams());
   				    if (CollectionUtils.isNotEmpty(schoolArticleEntityList)) {
                        for (SjtSchoolArticleEntity schoolArticleEntity : schoolArticleEntityList) {
                            Map<String, Object> map = wrap(schoolArticleEntity, dto);
                            list.add(map);
                        }
                    }
				}else if(dto.getQueryType()==10){
					
					if(dto.getId()>0&&dto.getNum()>=0){
						sqls.conGreatThan("id", dto.getId());
						sqls.conLittleEqual("num",dto.getNum());
					}
					
					List<SjtSchoolArticleEntity> schoolArticleEntityList = schoolArticleComp.getListByPage(sqls.getCondition(), 1, 10, "num desc,id asc", sqls.getParams());
   				    if (CollectionUtils.isNotEmpty(schoolArticleEntityList)) {
                        for (SjtSchoolArticleEntity schoolArticleEntity : schoolArticleEntityList) {
                            Map<String, Object> map = wrap(schoolArticleEntity, dto);
                            list.add(map);
                        }
                    }
				}else if(dto.getQueryType()==30){
					if(sortId>0){
						sqls.conGreatThan("sort_id", sortId);
					}
					List<SjtSchoolArticleEntity> schoolArticleEntityList = schoolArticleComp.getListByPage(sqls.getCondition(), 1, 10, "sort_id asc", sqls.getParams());
   				    if (CollectionUtils.isNotEmpty(schoolArticleEntityList)) {
                        for (SjtSchoolArticleEntity schoolArticleEntity : schoolArticleEntityList) {
                            Map<String, Object> map = wrap(schoolArticleEntity, dto);
                            list.add(map);
                        }
                    }
				}
    		}
    	}else{
    		if (sortId > 0) { //lastId<0代表下拉刷新；  0代表默认值；  >0代表上拉加载
            	sqls.conLittleThan("sort_id", sortId);
            }
            List<SjtSchoolArticleEntity> schoolArticleEntityList = schoolArticleComp.getListByPage(sqls.getCondition(), 1, 10, "sort_id desc", sqls.getParams());
            if (CollectionUtils.isNotEmpty(schoolArticleEntityList)) {
                for (SjtSchoolArticleEntity schoolArticleEntity : schoolArticleEntityList) {
                    Map<String, Object> map = wrap(schoolArticleEntity, dto);
                    list.add(map);
                }
            }
    	}
    }
    
    private Map<String, Object> wrap(SjtSchoolArticleEntity schoolArticleEntity,SchoolArticleDTO dto) throws Exception {

        Map<String, Object> map = new HashedMap();
        map.put("id", schoolArticleEntity.getId()+"");
        map.put("title", schoolArticleEntity.getTitle());
        map.put("author", schoolArticleEntity.getAuthor());
        map.put("type", schoolArticleEntity.getType()+"");
        int isPay = 0;
        if(schoolArticleEntity.getType() == 2){
        	try {
        		isPay = hyOrderPayComp.hasArticlePay(schoolArticleEntity.getId(),dto.getUserId());
			} catch (Exception e) {
				logger.error("判断是否进行过商学院课程支付结果异常"+e);
			}
        }
        map.put("isPay", isPay);
        map.put("price", schoolArticleEntity.getPrice()+"");
        String pic = schoolArticleEntity.getPic() == null ? "" : fileComp.picUrlByUserId(schoolArticleEntity.getPic(), dto.getUserId());
        map.put("pic", pic);
        map.put("date", TimeUtils.dateFormat(schoolArticleEntity.getUpdateTime(), "yyyy.M.d"));
        map.put("sortId", schoolArticleEntity.getSortId()+"");
        map.put("likeNum", SchoolArticlesDataService.getSchoolArticlesLikeNum(schoolArticleEntity));
        map.put("pv", SchoolArticlesDataService.getSchoolArticlesPV(schoolArticleEntity));
        map.put("shareUrl", "https://hyapp.58.com/app/school/article/share?id="+schoolArticleEntity.getId());
        return map;
    }
}
