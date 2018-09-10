package com.bj58.hy.link.smallapp.controllers;

import com.bj58.fbf.common.FBFContext;
import com.bj58.fbf.common.actionresult.HtmlActionResult;
import com.bj58.hy.link.smallapp.controllers.BaseController;
import com.bj58.hy.link.smallapp.interceptor.RateLimit;
import com.bj58.wf.mvc.ActionResult;
import com.bj58.wf.mvc.annotation.Path;

/**
 * Created by linhongyou on 2017/6/7.
 */

@SuppressWarnings("unchecked")
@Path("/smallapp/demo")
public class demoController extends BaseController {

    /**
     * @api {GET} smallapp/demo/user-info [GET] 获取用户信息
     * @apiVersion 1.0.0
     * @apiDescription  获取用户信息。
     * @apiName shopMsgList
     * @apiGroup demo/userInfo
     *
     * @apiHeader {String} access-token 用户令牌
     *
     * @apiSuccess {Array} userinfo 用户信息.
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *   {
     *   "code": 1,
     *   "msg": "获取成功",
     *   "ret": {
     *          "userinfo": {
     *           "id": "用户ID",
     *           "name": "昵称",
     *           "photo": "头像"
     *           },
     *        }
     *    "time":"服务器当前时间戳"
     *    }
     *
     * @apiError  access-token AccessToken is required.
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 200 Not Found
     *   {
     *      "code": 0,
     *      "msg": "AccessToken is required.",
     *      "ret": {},
     *      "time":"服务器当前时间戳"
     *    }
     */
    @Path("/user-info")
    @RateLimit(name = "user-info")
    public ActionResult userInfo()
    {
        
    }
   

    /**
     * @api {GET} smallapp/demo/shop-msg-record [GET] 商户-获取消息记录
     * @apiVersion 1.0.0
     * @apiDescription  聊天消息记录。
     * @apiName shopMsgRecord
     * @apiGroup demo/Shop
     *
     * @apiHeader {String} access-token 用户令牌
     * @apiParam {Int} id 客户id
     * @apiParam {Int} [first_msg_id] 目前消息中第一条消息的id
     *
     * @apiSuccess {Array} msg 消息内容.
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *   {
     *   "code": 1,
     *   "msg": "获取成功",
     *   "ret": {
     *          "msg": {
     *           "消息ID":{
     *           "id": "消息ID",
     *           "type": "消息类别",
     *           "content": "消息内容",
     *           "send_time": "消息发送时间",
     *           "from": "1发自用户 2发自店铺",
     *            "remark": "消息备注",
     *           },
     *           "消息ID":{
     *           "id": "消息ID",
     *           "type": "消息类别",
     *           "content": "消息内容",
     *           "send_time": "消息发送时间",
     *           "from": "1发自用户 2发自店铺",
     *            "remark": "消息备注",
     *           },
     *           ……
     *         },
     *        },
     *    "time":"服务器当前时间戳"
     *    }
     *
     * @apiError  access-token AccessToken is required.
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 200 Not Found
     *   {
     *      "code": 0,
     *      "msg": "AccessToken is required.",
     *      "ret": {},
     *      "time":"服务器当前时间戳"
     *    }
     */
    @Path("/shop-msg-record")
    @RateLimit(name = "shop-msg-record")
    public ActionResult shopMsgRecord()
    {
       
    }
    /**
     * @api {POST} smallapp/demo/shop-send [POST] 商户-发送消息
     * @apiVersion 1.0.0
     * @apiDescription  发送聊天消息。
     * @apiName shopSend
     * @apiGroup demo/Shop
     *
     * @apiHeader {String} access-token 用户令牌
     * @apiParam {Int} id 客户id
     * @apiParam {Int} type 类别 1文本 2图片 3语音,
     * @apiParam {String} content 内容 语音和图片为url
     * @apiParam {String} remark 备注
     *
     * @apiSuccess {Array} msg 消息内容.
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *   {
     *   "code": 1,
     *   "msg": "发送成功",
     *   "ret": {
     *          "msg":{
     *           "id": "消息ID",
     *           "type": "消息类别",
     *           "content": "消息内容",
     *           "remark": "消息备注",
     *           "send_time": "消息发送时间",
     *           },
     *        },
     *    "time":"服务器当前时间戳"
     *    }
     *
     * @apiError  access-token AccessToken is required.
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 200 Not Found
     *   {
     *      "code": 0,
     *      "msg": "AccessToken is required.",
     *      "ret": {},
     *      "time":"服务器当前时间戳"
     *    }
     */
    @Path("/shop-send")
    @RateLimit(name = "shop-send")
    public ActionResult shopSend()
    {
       
    }


}
