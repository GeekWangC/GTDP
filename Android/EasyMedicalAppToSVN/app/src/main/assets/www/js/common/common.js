/**
 *@description 医学数字化教学平台移动端
 *@author geekwangc
 *@data 2015年12月8日
 *@version 1.0.0
 */
var u = navigator.userAgent; 
var	app = navigator.appVersion;
var common = {
	basrUrl:"http://115.29.97.231:3000/",//基地址
	interfaceBase:"http://115.29.97.231:3000/api/Mobile/",//接口基路径
	sliceImgBase:"http://115.29.97.231:3000/",//切片缩略图地址
	bannerBase:"http://115.29.97.231:3000/",//banner图片渎职
	coursePPT:"http://115.29.97.231:3000/",//课件PPT地址
	isAndroid:u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或者uc浏览器
	isiOS:!!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
	isContain:function(find,arr){/*单元体是否存在于数组中，true存在，false不存在*/
		var result = false;
		for(var i = 0,len = arr.length;i < len;i++){
			if(arr[i] === find){
				result = true;
				break;
			}
		}
		return result;
	},	
	reCotainArr:function(find,arr,attr){/*返回包含某个属性的对象数组*/
		var newArr = [];
		attr = attr || ParentID;
		for(var i = 0,len = arr.length;i < len;i++){
			if(arr[i][attr] === find){
				newArr.push(arr[i]);
			}
		}
		return newArr;
	},
	contactUrl:function(result,attr,base){/*拼接地址*/
		base = base || basrUrl;
		result.forEach(function(e){
			if(e[attr]){
				e[attr]= base+""+e[attr] 
			}
		})
	},
	jsmpzz:function(e){/*阻止时间冒泡*/
		var e = window.event || event; 
		if ( e.stopPropagation ){ //如果提供了事件对象，则这是一个非IE浏览器 
			e.stopPropagation(); 
		}else{ 
			//兼容IE的方式来取消事件冒泡 
			window.event.cancelBubble = true; 
		} 
	},
	backHome:function(){/*返回主页*/
		if(common.isiOS){
			var url="IOS:BACK";
        	document.location = url;
		}else if(common.isAndroid){
			var isBack = '1';
			window.local_obj.jsGetBack(isBack);
		}else{
			console.log("孩子你能不能用点正常的机器");
		}
	},
	recordHistory:function(href){/*记录浏览历史*/
		var hisHref = [];
		if(localStorage.getItem("saveUrl")){
			hisHref = localStorage.getItem("saveUrl").split(",");
			if(hisHref.length>5){
				localStorage.clear("saveUrl");
			}
		}
		hisHref.push(href);
		localStorage["saveUrl"] = hisHref;
	},
	backHistory:function(){/*返回*/
		var his = [];
		if(localStorage.getItem("saveUrl")){
			his = localStorage.getItem("saveUrl").split(",");
			window.location.href=his.pop();
			localStorage["saveUrl"] = his;
		}else{
			common.backHome();
		}
	},
	goToSecondPage:function(type,url){/*android交互，跳转二级页面*/
		if(common.isAndroid){
			if(type==1){//课程
				window.local_obj.CourseJump(url);
			}else if(type==2){//练习
				window.local_obj.PracticeJump(url);
			}else if(type==3){//我的
				window.local_obj.MyJump(url);
			}
		}else{
			common.recordHistory(url);
			window.location.href = url;
		}
		
	},
	goToHistoryPage:function(){/*android交互，返回上层页面*/
		if(common.isAndroid){
			var isBack = "1"
	        window.local_obj.jsGetBack(isBack);
	    }else{
	    	common.backHistory();
	    }
	},
	del_html_tags:function(str,reallyDo,replaceWith) {/*str是目标字符串 reallyDo是替换谁 eplaceWith是替换成什么。*/
		var e=new RegExp(reallyDo,"g"); 
		words = str.replace(e, replaceWith); 
		return words; 
	},
	createComparionFun:function(propertyName){/*对象数组某属性排序*/
	    return function(object1,object2){
			var value1=object1[propertyName];
			var value2=object2[propertyName];
			if(value1<value2){
				return 1;
			}else {
				return 0;
			}
	    }
	} 
}

var WAjax = function(url,method,data){//接口
	url = url || "";//http://115.29.97.231/api/Mobile/GetCatalogue";
	method = method || "get";
	data = data || "";
	var result = "";
	var startDate = new Date();
	console.log("请求接口开始时间："+ startDate)
	if(url){
		$.ajax({
			type:method,
			async:false,
			url:url,
			/*dataType:'jsonp',
			jsonp:'callback',*/
			headers: {
                'UserInfo': sessionStorage.USERINFO || "6AFTTD8oWuSGsiItYRO4mQ==" //
            },
			data:data,
			//contentType:'application/json',
			success:function(data,status, xhr){
				if(data){
					console.log(data);
					result =  data;
				}
				var authorization = xhr.getResponseHeader("UserInfo");
				if(!!authorization){
					sessionStorage.USERINFO = authorization
                    console.log(authorization);
				}
				var endDate = new Date();
			    console.log("接口返回数据时间："+ endDate)
			    var date3=endDate.getTime()-startDate.getTime()  //时间差的毫秒数
			    console.log("接口耗时："+ date3)
			},
			error:function(){
				console.log("请求失败");
			}
		})
	}
	return result;
}

var getParamUrl = function(name){//获取URL参数
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  decodeURI(r[2]); return null;
}

Date.prototype.format =function(format){//Date扩展
    var o = {
        "M+" : this.getMonth()+1, //month
        "d+" : this.getDate(), //day
        "h+" : this.getHours(), //hour
        "m+" : this.getMinutes(), //minute
        "s+" : this.getSeconds(), //second
        "q+" : Math.floor((this.getMonth()+3)/3), //quarter
        "S" : this.getMilliseconds() //millisecond
    }
    if(/(y+)/.test(format)) format=format.replace(RegExp.$1,(this.getFullYear()+"").substr(4- RegExp.$1.length));
    for(var k in o)
        if(new RegExp("("+ k +")").test(format))
            format = format.replace(RegExp.$1,RegExp.$1.length==1? o[k] :("00"+ o[k]).substr((""+ o[k]).length));
    return format;
}


