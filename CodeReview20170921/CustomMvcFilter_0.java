package com.bj58.chuangxin;

import com.alibaba.fastjson.JSON;
import com.bj58.chuangxin.common.exception.AppBaseException;
import com.bj58.chuangxin.common.utils.ParameterRequestWrapper;
import com.bj58.chuangxin.common.utils.SystemGlobals;
import com.bj58.chuangxin.common.velocity.VelocityTemplateFactory;
import com.bj58.chuangxin.component.FacadeComp;
import com.bj58.chuangxin.component.common.ConfigComp;
import com.bj58.chuangxin.component.user.UserComp;
import com.bj58.chuangxin.entity.SjtUserEntity;
import com.bj58.chuangxin.enums.AppResultStateEnum;
import com.bj58.chuangxin.enums.ConfigEnum;
import com.bj58.chuangxin.service.esbmessage.hunteresb.HunterEsbMessageService;
import com.bj58.chuangxin.service.esbmessage.lianjieesb.LianjieEsbMessageService;
import com.bj58.chuangxin.service.esbmessage.wltesb.WltEsbMessageService;
import com.bj58.chuangxin.vo.JsonResult;
import com.bj58.spat.passport.client.domain.RemoteValid;
import com.bj58.spat.passport.client.service.PassportService;
import com.bj58.uc.passport.webclient.util.PPUCookieUtil;
import com.bj58.wf.core.WF;
import com.bj58.wf.log.Log;
import com.bj58.wf.log.LogFactory;
import com.bj58.wf.mvc.MvcConstants;
import com.bj58.wf.mvc.MvcFilter;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.apache.velocity.io.VelocityWriter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomMvcFilter extends MvcFilter {
    private static final Log logger = LogFactory.getLog(CustomMvcFilter.class);
    private final UserComp userComp = FacadeComp.getFacade().getUserComp();
    private final ConfigComp configComp = FacadeComp.getFacade().getConfigComp();
    private boolean isSkipPPUForQA = false;

    @Override
    public void init(FilterConfig config) throws ServletException {
        VelocityTemplateFactory.init();
        super.init(config);
        try {
//            WltEsbMessageService.init();//启动wlt的esb消息接收线程
            HunterEsbMessageService.init();//启动猎人系统esb消息接收线程
//            LianjieEsbMessageService.init();//启动连接平台的186号段的消息接收线程
            isSkipPPUForQA = isSkipPPUForQA();
        } catch (Exception e) {
            logger.error("系统初始化失败",e);
        }
    }

    @Override
    public void doFilter(ServletRequest request0, ServletResponse response0, FilterChain chain) throws IOException, ServletException {
        request0.setCharacterEncoding("utf-8");
        response0.setCharacterEncoding("utf-8");
        ParameterRequestWrapper request = new ParameterRequestWrapper((HttpServletRequest) request0);
        HttpServletResponse response = (HttpServletResponse) response0;
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8");
        //对于沙箱和线上环境，必须进行PPU校验；对于线下环境，如果request请求中有userId参数，则给QA自动化测试使用。
        if(isSkipPPUForQA && StringUtils.isNotBlank(request.getParameter("userId"))){
            super.doFilter(request, response, chain);
        }else {
            try {
            	if("/app/school/article/share".equals(request.getRequestURI())){
            		super.doFilter(request, response, chain);
            	}else{
            		if(!filterReqUrl(request)) {
                        long ppuUserId = PassportService.passportService.getLoginUserId(RemoteValid.SAPCE_ONE_HOUR, request, response);
                        //登录用户的ID，若<2，则可认为此userId的ppu过期，登录失败
                        if (ppuUserId < 2) {
                            response.getWriter().write(this.generateResponse(AppResultStateEnum.PPU_UNVALID.getCodeStr(), "登录认证信息已过期，请重新登录"));
                            log.error("ppu返回的userId:" + ppuUserId + ",ppu过期,ppu=" + PPUCookieUtil.getPpuCookie(request) + ",url=" + request.getRequestURI());
                            log.error("imei=" + request.getParameter("imei") + ",version=" + request.getParameter("version") + ",platform=" + request.getParameter("platform"));
                        } else {
                            boolean flag = isTouchSingleDeviceLimitWithoutLogin(ppuUserId, request);
                            if (flag) {
                                String singleDeviceLoginContent = configComp.getValueByConfigTable(ConfigEnum.APP_SINGLE_DEVICE_LOGIN_CONTENT);
                                boolean isH5 = "1".equals(request.getParameter("isH5"));//约定：商家通中和hyapp.58.com域名通信的H5页面，都需要在请求中添加isH5=1
                                if(isH5){//H5页面所有发出的请求，包括ajax和herf请求
                                    String jsAjaxHeader = request.getHeader("X-Requested-With");//约定：每个hyapp.58.com域名下的js的ajax请求，header中都会有X-Requested-With，且值为XMLHttpRequest
                                    if("XMLHttpRequest".equals(jsAjaxHeader)){//H5页面的ajax请求，返回json数据
                                        response.getWriter().write(this.generateResponse(AppResultStateEnum.SINGLE_DEVICE_LOGIN.getCodeStr(), singleDeviceLoginContent));
                                    }else {//H5页面中的herf链接，返回结果是html页面
                                        renderSingleDeviceHtml(response,"/single_device_error");
                                    }
                                }else {//正常的APP发出的请求,返回json数据
                                    response.getWriter().write(this.generateResponse(AppResultStateEnum.SINGLE_DEVICE_LOGIN.getCodeStr(), singleDeviceLoginContent));
                                }
                                log.error("触发单设备登录限制,userId=" + ppuUserId + " , imei=" + request.getParameter("imei") + " , platform=" + request.getParameter("platform"));
                            } else {
                                request.addParameter("userId", new String[]{ppuUserId + ""});
                                super.doFilter(request, response, chain);
                            }
                        }
                    }
            	}
            } catch (Exception e) {
                logger.error("业务处理异常,url="+request.getRequestURI(),e);
            }
        }
    }

    /**
     * 是否为QA跳过PPU校验的开关
     * @return
     */
    private boolean isSkipPPUForQA(){
        return "true".equals(SystemGlobals.getString("isSkipPPU"));
    }

    /**
     * 单设备登录判断的两种情况
     * 1、login接口：后登录者将前者踢下线：发push消息
     * 2、非login接口：返回的json体的状态码为 AppResultStateEnum.SINGLE_DEVICE_LOGIN
     * 判断第2种情况，是否触发单设备限制
     * @return
     */
    private boolean isTouchSingleDeviceLimitWithoutLogin(long ppuUserId,ParameterRequestWrapper request) throws Exception {
        if("/app/global/login".equals(request.getRequestURI())) //放行，在AppLoginService做处理
            return false;

        String newImei = request.getParameter("imei");
        if (StringUtils.isBlank(newImei))
            throw new AppBaseException(AppResultStateEnum.PARA_EXCEPTION.getCode(), "imei must not be empty");

        SjtUserEntity sjtUser = userComp.getByUserId(ppuUserId);
        if (null == sjtUser)//非login接口，不可能存在空的user实体
            throw new AppBaseException(AppResultStateEnum.VALID_EXCEPTION.getCode(),"sjtUser 不可能为 null,userId="+ppuUserId);

        if(!newImei.equals(sjtUser.getImei())){
            logger.info("------******------接口触发单设备登录排查：userId="+ppuUserId+",dto_imei="+newImei+",mysql_imei="+sjtUser.getImei()+",url="+request.getRequestURI()+",platform="+request.getParameter("platform"));
        }
        return !newImei.equals(sjtUser.getImei());
    }

    private String generateResponse(String status, String msg) {
        JsonResult<String> result = new JsonResult<String>();
        result.setStatus(status);
        result.setMsg(msg);
        result.setResult("");
        return JSON.toJSONString(result);
    }

    /**
     * 过滤请求的url
     * @param request
     * @return
     * @throws Exception
     */
    private boolean filterReqUrl(ParameterRequestWrapper request)throws Exception{
        if(!"/favicon.ico".equals(request.getRequestURI())){
            return false;
        }else{
            return true;
        }
    }

    /**
     * H5页面的href链接跳转时，触发单设备登录返回的页面，便于用户可以通过左上角图标返回上一页
     * @param response
     * @throws IOException
     */
    private void renderSingleDeviceHtml(HttpServletResponse response,String viewName) throws IOException {
        String prefix = MvcConstants.VIEW_PREFIX;
        String suffix = ".html";
        String path = prefix  +"\\"+ viewName + suffix;

        Template template =  Velocity.getTemplate(path);

        response.setContentType("text/html;charset=\"UTF-8\"");
        response.setHeader("X-HOST", WF.getNamespace());
        response.setCharacterEncoding("UTF-8");
        // init context:
        Context context = new VelocityContext();
        // render:
        VelocityWriter vw = new VelocityWriter(response.getWriter());
        try {
            template.merge(context, vw);
            vw.flush();
        }finally {
            vw.recycle(null);
        }
    }
}
