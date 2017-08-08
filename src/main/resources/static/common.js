/**
 * Created by mac on 17/2/16.
 */
var baseip="https://localhost:8443/";
var baseurl=baseip+"api/";
function _get(url,xml,callback,failcall,errorcall){
    $.ajax({
        url: url,
        type: 'GET',
        data:{"xml":xml,"sessionid":localStorage.getItem("sessionid")},
        success: function (data) {
            var parsedata=$.parseXML(data);
            var $parsedata=$(parsedata);
            if($parsedata.find("Result").attr("value")=="Success"||$parsedata.find("Result").attr("value")=="success"){
                callback($parsedata);
            }else if($parsedata.find("Result").attr("value")=="AuthErr"){
                location.href="./tologin.html"
            } else{
                var msg=$parsedata.find("ErrorMsg").text()
                failcall($parsedata,msg);
            }
        },
        error: function (xhr) {
            errorcall(xhr);
        }
    });
}
//保留一位小数
//功能：将浮点数四舍五入，取小数点后2位
function toDecimal(x) {
    var f = parseFloat(x);
    if (isNaN(f)) {
        return;
    }
    f = Math.round(x*10)/10;
    return f;
}
function geturlvalue(name) {
    var str = window.location.search;   //location.search是从当前URL的?号开始的字符串 例如：http://www.51job.com/viewthread.jsp?tid=22720 它的search就是?
    tid = 22720
    var value;
    if (str.indexOf(name) != -1) {
        var pos_start = str.indexOf(name) + name.length + 1;
        var pos_end = str.indexOf("&", pos_start);
        if (pos_end == -1) {
            value= str.substring(pos_start)
        } else {
            alert("对不起这个值不存在！");
        }
    }
    return value;
}
function _post(url,xml,callback,failcall,errorcall){
    $.ajax({
        url: url,
        type: 'POST',
        data:{"xml":xml,"sessionid":localStorage.getItem("sessionid")},
        success: function (data) {
            var parsedata=$.parseXML(data);
            var $parsedata=$(parsedata);
            if($parsedata.find("Result").attr("value")=="Success"||$parsedata.find("Result").attr("value")=="success"){
                callback($parsedata);
            }else{
                var msg=$parsedata.find("ErrorMsg").text();
                failcall($parsedata,msg);
            }
        },
        error: function (xhr) {
            errorcall(xhr);
        }
    });
}
function _postparam(url,data,callback,failcall){
    $.ajax({
        url: url,
        type: 'POST',
        data:data,
        async: true,
        success: function (data) {
            callback(data);
        },
        error: function (xhr) {
            failcall(xhr);
        }
    });
}
function _getparam(url,data,callback,failcall){
    $.ajax({
        url: url,
        type: 'GET',
        data:data,
        async: true,
        success: function (data) {
            callback(data);
        },
        error: function (xhr) {
            failcall(xhr);
        }
    });
}