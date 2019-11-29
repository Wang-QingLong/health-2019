package com.itcast.controller;


import cn.hutool.core.date.DateUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.itcat.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/11/28 19:11
 * @description:
 */
@RestController
@RequestMapping("/pay")
public class PayController {
    @Reference
    MemberService memberService;

    Logger log = LoggerFactory.getLogger(PayController.class);
    //支付宝服务器地址
    String SERVERURL = "https://openapi.alipaydev.com/gateway.do";
    //商户appid
    String APP_ID = "2016101600698540";
    //商户私钥
    String APP_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCJLdoWxhAZ5zXkp6lPMcZB6SnRP9T/1eIBfGYO8KkTIwnbTq07tkpGPSOVcRVlH6m9D+pQvcLMJbI6OO82VaRkL+0dT+gwOkHKFTfY4LPswF5JKzAwkTZK+kN1tBYsPxmK9ICjwTEyKZ2284seM9EY3MqrxUt8vJvFyM1DNwZ/HHSo7GRMmCl0eznlds0QdkDyOnVpBayCXwP+49VdCUZtHGusERMFFuD+Sx3/xhb0syef3MX+q3lEB1x0XE0nh754doOiun40yRY6tBs361dyqf04aLwi7KTLcyoqJJEjbYHwdTAmhOOz2/pz2kHmGVobiCr+lMZkVXPCTfPGl6VlAgMBAAECggEAEaMNiSQJ6eqqKS3NPNtfbBX4RGj6j7IGDr7ZuE4EzusQV1v0NdsoDYPK0WnqoEfD4QTs0Vi0dN7b3VR4nPnB2we2arBdX+X9k2fK+F1bhLLu7GVbGsNwwY8PoVB4EVRDinQJHgS2uIZVOrJxTn7HAXVRsWf6Xp05QeKhUWT/ttPXaVhLW6o4iWy8Km6IqifgOkgt+rIyBtwkp5avzohV5PwdlZfyyOYzgEkVCjirXxlYhqPkRGxvSM1+kNmhJa4ENBKwo35uhKs8vYA8t3mqP9YZ3W7/N4Vkkl6FK8T0Zje9w40+I+U9ZYD1C4DMU+ROHhWKHopb3FNh/9Hzh7GRQQKBgQDLnlsKiWsUMLKq6LUExeI7kSzAyj6e7fWtKrBfPiIiSk9amjXVGgs6UN/BS3/LS9t+9yok2NVhOMuag+MHR8/ur0Wa/5LXL9nb+ynAiHqe16aTYUex+p+VvwOgZE41poNmNwrc4YVhnJvM65C02f7gLrGRJHMZNYeyJQGETJCfrQKBgQCseANiRUkHTLznfEescLjFkmG8EVAdh1eMy7bKm1gHO5eyxbuiCC6zLhm3JIPctgGoibH+iG94zcbtUIlBCKNG4ZdV8lvoyyeyDY//q0v3ko5zhBlNh4AJhriXQPEmOQlXeiENic1WnytwyNLiKDfTPKHZ9LtWVHCYQhfFxdPzmQKBgQC+VVdK4Str4x2QLxjOzl7sSYco66RfkZZd+ZKGZSEpJodI7O+3NLIcH+8Y04cwtMcHxujpKptDVwDTfcchThprRgFtostTRRKD1CKyK6Fbi7/U8lE/aEP2iWhNbJXDBtS7HmaUFGdG2Eq24dgRhdj/HAa14vmILgabGoZxcdSavQKBgBAO5JHDzbbmSDhtc+b0X9/Vb1ApdIIrVdWAKjbXG+0geVFl48CEboS0aCSLO9PTBniAyZffcDGZR0pVU1JA0aMJ4iNYe4JDWCDV7nBprJsShDzxMcHsOdmpN4qtu0ZJzTrd3jH6gKnuDAdmS2xPPlNms9MtXDv/hLRIgxog/0kRAoGAd/w1IQj1tVCF3ZUaCo6Cj2vlz4x2W3zJXnzKlkbihC56BxH/JIh3Ld8VbNmCfAWs65aO3SLZwZzyd/xUgVO1GR44RZpYv6xXdWVBjhEv6D1Y+KJY84l9j+4LJCbjOu26OsKjAcxJAihUth+op4CJYkKj6g6oxB54hoI3E2a/dKE=";

    String CHARSET = "UTF-8";
    //支付宝公钥
    String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiS3aFsYQGec15KepTzHGQekp0T/U/9XiAXxmDvCpEyMJ206tO7ZKRj0jlXEVZR+pvQ/qUL3CzCWyOjjvNlWkZC/tHU/oMDpByhU32OCz7MBeSSswMJE2SvpDdbQWLD8ZivSAo8ExMimdtvOLHjPRGNzKq8VLfLybxcjNQzcGfxx0qOxkTJgpdHs55XbNEHZA8jp1aQWsgl8D/uPVXQlGbRxrrBETBRbg/ksd/8YW9LMnn9zF/qt5RAdcdFxNJ4e+eHaDorp+NMkWOrQbN+tXcqn9OGi8Iuyky3MqKiSRI22B8HUwJoTjs9v6c9pB5hlaG4gq/pTGZFVzwk3zxpelZQIDAQAB";

    /**
     * 构造支付请求
     * @param httpRequest
     * @param httpResponse
     * @param orderId
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping("/alipay")
    public void alipay(HttpServletRequest httpRequest,
                       HttpServletResponse httpResponse, Integer orderId) throws ServletException, IOException {
        AlipayClient alipayClient =
                new DefaultAlipayClient(SERVERURL,
                        APP_ID, APP_PRIVATE_KEY,
                        "json",
                        CHARSET,
                        ALIPAY_PUBLIC_KEY, "RSA2"); //获得初始化的AlipayClient

        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();//创建API对应的request
        alipayRequest.setReturnUrl("http://732xhs.natappfree.cc/pages/paySuccess.html");//支付成功之后跳转的地址(由于我们没有域名,我们使用natapp内网穿透工具)
        alipayRequest.setNotifyUrl("http://732xhs.natappfree.cc/pay/alipayNotifyNotice.do");//支付成功通知地址

        Map orderInfo = memberService.findByOrderId(orderId);

        JSONObject json = new JSONObject();
        json.put("out_trade_no",outTradeNo(orderId));//商户订单号，需要保证不重复(支付回调会带上这个参数，我们可以用来查询对应的订单修改状态)
        json.put("total_amount",0.01);//订单金额
        json.put("subject",orderInfo.get("setmeal"));//订单标题
        alipayRequest.setBizContent(json.toJSONString());

        String form="";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        httpResponse.setContentType("text/html;charset=" + CHARSET);
        httpResponse.getWriter().write(form);//直接将完整的表单html输出到页面
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }

    /**
     * 生成商户订单号（年月日时分秒 + 订单号）
     * @param orderId
     * @return
     */
    private String outTradeNo(Integer orderId){
        String dateStr = DateUtil.format(new Date(), "yyyyMMddHHmmss");
        return dateStr + orderId;
    }

    /**
     * 支付宝通知我们支付状态，在这个接口里面可以修改订单支付的状态
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/alipayNotify")
    @ResponseBody
    public String alipayNotify(HttpServletRequest request, HttpServletRequest response) throws Exception {

        log.info("支付成功, 进入异步通知接口...");

        //获取支付宝POST过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        Map<String,String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
//			valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        boolean signVerified = AlipaySignature.rsaCheckV1(params, ALIPAY_PUBLIC_KEY, "utf-8", "RSA2"); //调用SDK验证签名
        //——请在这里编写您的程序（以下代码仅作参考）——
		/* 实际验证过程建议商户务必添加以下校验：
		1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
		2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
		3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
		4、验证app_id是否为该商户本身。
		*/
        if(signVerified) {//验证成功
            //商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

            //支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

            //交易状态
            String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");

            //付款金额
            String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"),"UTF-8");

            if(trade_status.equals("TRADE_FINISHED")){
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序

                //注意： 尚自习的订单没有退款功能, 这个条件判断是进不来的, 所以此处不必写代码
                //退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
            }else if (trade_status.equals("TRADE_SUCCESS")){
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //付款完成后，支付宝系统发送该交易状态通知

                // 修改叮当状态，改为 支付成功，已付款; 同时新增支付流水
            }
            log.info("支付成功...");
        }else {//验证失败
            log.info("支付, 验签失败...");
        }

        return "success";
    }
}
