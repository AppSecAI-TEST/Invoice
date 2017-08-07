package com.swntek.api;

import com.github.sd4324530.fastweixin.api.QrcodeAPI;
import com.github.sd4324530.fastweixin.api.config.ApiConfig;
import com.github.sd4324530.fastweixin.api.enums.QrcodeType;
import com.github.sd4324530.fastweixin.api.response.QrcodeResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by mac on 17/8/7.
 */
@RestController
@RequestMapping("/api/qrcode")
public class QrcodeController {
    @GetMapping(value = "getQrcode")
    public QrcodeResponse getQrcode(@RequestParam String shopid){
        ApiConfig apiConfig=new ApiConfig(WeixinController.getAPPID(),WeixinController.getAPPSECRET());
        QrcodeAPI qrcodeAPI=new QrcodeAPI(apiConfig);
        QrcodeResponse qrcode = qrcodeAPI.createQrcode(QrcodeType.QR_LIMIT_STR_SCENE, shopid + "","test", 2592000);
        return qrcode;
    }
}
