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
    String APP_PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCcgWn1/l0gpxNvmWoAfRJGlFmvjBa6o7r1M9kckzyfiE+CKkRKmHGo6RBBslMJnmpMMuaBfc0aHCZfsJxEEENyOJCgTlRTTpztG/dM8t1PJZqXG/oosCIhSj+Sk/zL7QL7a6Lsb3RcFk4Pp76f9Gn5y8NcROTtxvoUmEiEy7TQtd8khbxP9jN3YQhHoWGshFRTdsuSMgx2rdeG9KuotYKgnub8IlPRFvAK7nZ2NsnT/D270QWR+S07Lr4ewFWRzJOnAIEJMW8meTmXbfR7lUbjepXSNP5PLXxl8xWdOZAcNbrqP0gW+ZnYTq+x6iehlvs18tZTaCONHZ9O+P3pTaYpAgMBAAECggEAB2fEOMSRaYMeU1CCCIXigH5yg/Dv/mLS7NlIM6bv3miXwoP3eUQfVKu6fjFO5oydYP+jkor+Unb0heCYdEwEtPniNgOez9ZPbJS/Ld/sAtu3peDIBHbZFpQpz+i6tHeBhZ4BNojLi7Pue6UnSvBIt24hz1LfNjUNDj5qaAYvhxSQq4+HQDz78Te52Dg+Q0iGEaSBAEJzbAfx2KAiRBRx3JZwvHv5Q9tLspBVBupd8CE2fOC6/QmYoCclvtqksGsqjBaPek73N2GBmwp18fz2qY5QrXdzWUFhX0RlPBkQhjAUr0cuDHSOKNZ8zRJiIeK/W99yqTKjNUqNVXj1VHaKBQKBgQDzy7V+xGrkWxEOaDqov/bNOPc5uJeX+RsnFmsYSujh4061GjFm/CoNgdWGoqTV9jDLWzAIishi40NUUDXchFO0UtIk8p2zm2wVvozwQ6ntEXe3bcdJKPu0XM2EhGOas0JZ5yl3Ok2zYiUWcBOw+RU2fDZDOjhqZkIFqnWOoeGOZwKBgQCkVxA8PlY2PsPLURIlr8+KuN9HfQXiVINtRq8ljUqxVIb75gKcxLLNvlehDKo8Tsx4EPI7imc4qAQF1XF/KBEFsgk/Cj1rmi5+Mtt8i43YlJfG4eZqtbIZ4DgZmcKsFMc5G9zTncxLD/1hKCXyOCkq9XBqbbJ0jicV4mehWsAs7wKBgFXrkD5qCOYL9jnmn+3DIb73+aFdHggQYfJnozy/gc3CZ4XKAmkyK7vw6wj1HzTM/CtfKtGH39GCGMOJNUJPXJSrr6YX315iQzhynIc8wZFsW43I4bg1+md5YXD3m4b08EhxAJslcCbtSvbabwYq8r/uDM2qoiFVgqUuixnGkZC3AoGBAKJGvmdefq7t51S65xqDsoKe7wuebtMA9Q5Qv95q2JimiOvwQiAfMxhqngkPgoIupUTiJu+YSTju35oLHBmdrMFc/jhbmi7IfqRq/8TJ4PU+NWlT47z3UbivaXYOGObnVZoU40erB/IPBfqMNHwr7KrY1Xqie5cQT5QSjpYMELvNAoGBANZHfKNOzxTGGM7gOAS6HauuVUjM09sYbA2n6m07nK6SlydTA50nbptn9MpL8ddvMQiSGvivnBzE417PGPimINA+kjZ8NKVvQwKa4vUEPFTLaANWVtiDmHsPVJmO4YrBfEHwGjfmBJJ67NUr3GjiQ+ArYrIfhQ0+aaWQ//k1LER7";

    String CHARSET = "UTF-8";
    //支付宝公钥
    String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAyCJ6Af6PEb+PXgc4qhxEs5We6YWCyO+9T84Ib+WtwfpPkBsHOeVaAvL2Nz6xpNfMwTm+PC/+NXZJbHK90BsDBGW/QPENI/2qw4oWdHIJBBbBmO9cGKrSUpfid0GWT24oTCvy8Tgiq5Vf3p+xWHaujBJCJqAJduvOPPEb7OVP5uyx0In57q7pbMYU6NUMIZCvrzaBcBLPjjav41Q8CDkeDACZhjER7Cw/M+Blkk9dfrwBLoilIhoIaqXs+KjHnqP47wrxoFPRXOhONVvFFnrMME7i0MM6X22rI1a1uhvcieGPDwFS/pvuRAM6na0uJ4T0uvmSeumSANRW51AWk0QipQIDAQAB";

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
        alipayRequest.setReturnUrl("http://5muhnf.natappfree.cc/pages/paySuccess.html");//支付成功之后跳转的地址(由于我们没有域名,我们使用natapp内网穿透工具)
        alipayRequest.setNotifyUrl("http://5muhnf.natappfree.cc/pay/alipayNotifyNotice.do");//支付成功通知地址

        Map orderInfo = memberService.findByOrderId(orderId);

        JSONObject json = new JSONObject();
        json.put("out_trade_no",outTradeNo(orderId));//商户订单号，需要保证不重复(支付回调会带上这个参数，我们可以用来查询对应的订单修改状态)
        json.put("total_amount",1000);//订单金额
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
