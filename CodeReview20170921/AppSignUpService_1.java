package com.bj58.chuangxin.service.appsignup;

import com.bj58.chuangxin.common.utils.RedisUtils;
import com.bj58.chuangxin.component.FacadeComp;
import com.bj58.chuangxin.component.appsignup.SignUpComp;
import com.bj58.chuangxin.dto.appsignup.AppSignUpDTO;
import com.bj58.chuangxin.entity.SjtSignUpEntity;
import com.bj58.chuangxin.enums.AppSignUpEnum;
import com.bj58.chuangxin.service.global.AppGlobalService;
import com.bj58.chuangxin.vo.JsonResult;
import com.bj58.chuangxin.vo.TData;
import com.bj58.fbf.common.IBeatContext;
import com.bj58.wf.log.Log;
import com.bj58.wf.log.LogFactory;
import com.bj58.wf.mvc.ActionResult;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 报名保存的流程
 * 需要添加redis锁
 * @date 2017/6/2
 */
public class AppSignUpService extends AppGlobalService {
    private Log logger = LogFactory.getLog(AppSignUpService.class);
    private static SignUpComp signUpComp = FacadeComp.getFacade().getSignUpComp();

    @Override
    public ActionResult handle(IBeatContext iBeatContext) throws Exception {
        
        AppSignUpDTO dto = iBeatContext.getDTO();
        JsonResult<TData<String,Object>> result = JsonResult.newResult();
        TData<String,Object> data = new TData<String, Object>();
        Map<String,Object> map = new HashMap<String, Object>();
        String lockValue = System.currentTimeMillis() + "";
        
        boolean lock;
        try {
            do {
                lock = RedisUtils.getClient().tryLock(dto.getActivityId() + "_" + dto.getUserId(), lockValue, 8, 8);
            } while (!lock);
            
            //查询userid对应的报名信息
            long userCount = signUpComp.getCount(" user_id = ? and state = 1 and acvity_id = ? ", new Object[]{ dto.getUserId(), dto.getActivityId() });
            if (userCount != 0) {
                map = buildMap(map,AppSignUpEnum.SUCCESS.getCode(),"您已报名成功。");
                return;
            }
            
            //构建实体
            SjtSignUpEntity sjtSignUpEntity = buildEntity(dto);
            long id = signUpComp.insert(sjtSignUpEntity);
            if (id != 0) {
                map = buildMap(map,AppSignUpEnum.SUCCESS.getCode(),AppSignUpEnum.SUCCESS.getMess());
            } else {
                map = buildMap(map,AppSignUpEnum.FAILURE.getCode(),AppSignUpEnum.FAILURE.getMess());
                logger.warn("用户报名失败：userid="+sjtSignUpEntity.getUserId());
            }
            
        } catch (Exception e) {
            logger.error("AppSignUpService Exception:",e);
            map = buildMap(map,AppSignUpEnum.FAILURE.getCode(),AppSignUpEnum.FAILURE.getMess());
        } finally {
            //解锁
            RedisUtils.getClient().tryUnLock(dto.getActivityId() + "_" + dto.getUserId(), lockValue);
        }
        data.setData(map);
        result.setResult(data);
        dto.setResult(result);
        return null;
    }

    private Map<String,Object> buildMap(Map<String, Object> map, Integer code, String mess) {
        
        map.put("state",code);
        map.put("message",mess);
        return map;
    }

    private SjtSignUpEntity buildEntity(AppSignUpDTO dto) {
        
        SjtSignUpEntity sjtSignUpEntity = new SjtSignUpEntity();
        sjtSignUpEntity.setUserId(dto.getUserId());
        sjtSignUpEntity.setActivityId(dto.getActivityId());
        sjtSignUpEntity.setState(Short.parseShort(AppSignUpEnum.SUCCESS.getCode() + ""));
        sjtSignUpEntity.setUserName(dto.getUserName());
        sjtSignUpEntity.setUserPhone(dto.getUserPhone());
        sjtSignUpEntity.setUserCompany(dto.getUserCompany());
        sjtSignUpEntity.setCityId(dto.getCityId());
        sjtSignUpEntity.setCreateTime(new Date());
        sjtSignUpEntity.setUpdateTime(new Date());
        return sjtSignUpEntity;
    }

    public boolean isExist(String key) throws Exception {
        
        Long num = RedisUtils.getClient().setnx(key, "1", 10);
        return num != null && num.intValue() == 1 ? true : false;
       
    }
}
