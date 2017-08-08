package com.swntek.api;

import com.fast.sd4324530.fastweixin.api.QrcodeAPI;
import com.fast.sd4324530.fastweixin.api.config.ApiConfig;
import com.fast.sd4324530.fastweixin.api.enums.QrcodeType;
import com.fast.sd4324530.fastweixin.api.response.QrcodeResponse;
import com.swntek.bean.Shop;
import com.swntek.dao.Shopdao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Created by mac on 17/8/7.
 */
@RestController
@RequestMapping("/api/qrcode")
public class QrcodeController {
    @Autowired
    Shopdao shopdao;
    @GetMapping(value = "getQrcode")
    public String getQrcode(@RequestParam String shopid){
        ApiConfig apiConfig=new ApiConfig(WeixinController.getAPPID(),WeixinController.getAPPSECRET());
        QrcodeAPI qrcodeAPI=new QrcodeAPI(apiConfig);
        QrcodeResponse qrcode = qrcodeAPI.createQrcode(QrcodeType.QR_LIMIT_STR_SCENE, shopid + "",shopid, 2592000);
        String url="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+qrcode.getTicket();
        return url;
    }
    @PostMapping(value = "createshop")
    public void creatshop(@RequestParam String name){
        Shop shop=new Shop();
        shop.setName(name);
        shop.setSerialnumber(UUID.randomUUID().toString());
        shopdao.save(shop);
    }
}
