/**
 *@description 医学数字化教学平台移动端-切片
 *@author geekwangc
 *@data 2015年12月22日
 *@version 1.0.0
 */
$(function(){
    Vue.config.debug = false;
    Vue.filter('noEmp', function () {return 0;});
    var w = window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;//设备宽度
    var h = window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight;//设备高度
    function log2(x) { return  Math.LOG2E * Math.log(x); } 
    var config = {//此页面可能需要的参数配置
        id:getParamUrl("id"),//切片id
        name:decodeURI(getParamUrl("name")),//切片name
        ISlice:"GetSliceFromId",//获取某个切片
        type:getParamUrl("type"),//切片类型-区分大体病理
        step:0,//大体病理旋转步数
        a:getParamUrl("a")
    };
    var qpV = new Vue({//一个页面建议使用一个Vue实例
        el:"#qp-view",
        data:{
            sliceData:{},
            pinglunData:[],
            biaozhuData:[],
            biaozhuCount:0,
            topTitle:config.name
        },
        methods:{
            reply:function($event){//评论回复
                var parentID = $event.target.getAttribute("data-id");
                var url = common.interfaceBase + "SendComment?commentContent="+$('#txt-pinglun').val()+"&parentID="+parentID+"&dataID="+config.id+"&commentType=2&mentionMe=&picLable=";
                var method = "post";
                var result = WAjax(url,method);
                this.pinglunData = qpMethod.getPinglunData();
            },
            toScreen:function(){//全屏事件
                window.location.href = "qpscreen.html?id="+config.id+"&name="+config.name+"&type="+config.type+"&a=s";
            }
        },
        computed: {
            width: {//切片宽度
                get: function () {
                    return parseInt(this.sliceData.Size);
                }
            },
            height:{//切片高度
                get: function () {
                    var a = this.sliceData.Size.split(/[0,1,2,3,4,5,6,7,8,9]/).sort();
                    var b = this.sliceData.Size.split(a[a.length-1]);
                    var sliceHeight = b[1];
                    return sliceHeight;
                }
            }
        }
    });
    var qpMethod = {};//此页面用到的方法
    qpMethod.windowToCanvas = function(canvas,x,y){//window to canvas坐标系转换
        var bbox = canvas.getBoundingClientRect();
        return {
            x:x - bbox.left - (bbox.width - canvas.width) / 2,
            y:y - bbox.top - (bbox.height - canvas.height) / 2
        };
    };
    qpMethod.getSliceData = function(){//获取单个切片数据
        var url = common.interfaceBase + config.ISlice + "/" + config.id;
        var method = "get";
        var result = WAjax(url,method);
        common.contactUrl(result,"FileURL");
        //result[0].Description=common.del_html_tags(result[0].Description,"<br />","\n")
        return result[0];
    };
    qpMethod.getPinglunData = function(){//获取切片评论数据
        var url = common.interfaceBase + "GetCommentFromId/"+ config.id +"?commentType=2";
        var method = "get";
        var result = WAjax(url,method);
        var PLData = [];
        var commentids = [];//子评论id和含有子评论的父评论id
        result.forEach(function(e){
            if(e["creatTime"]){
                e["newcreatTime"]=new Date(e["creatTime"]).format("yyyy年MM月dd日"); 
            }
        });
        result.sort(common.createComparionFun("creatTime"));
        result.forEach(function(e,index){
            if(index <= 2){
                PLData.push(e);
            }    
        });
        var PLListData = [PLData,result];
        /*result.forEach(function(e){
            result.forEach(function(el){
                if(el.parentID == e.commentID){
                    e.childrenPL = el;
                    PLData.push(e);
                    commentids.push(el.parentID);
                    commentids.push(el.commentID);
                    return;
                }
            })
        });
        result.forEach(function(e){
            var is = common.isContain(e.commentID,commentids);
            if(!is){
                PLData.push(e);
            }
        });*/
        return PLListData;
    };
    qpMethod.setPinglunData = function(){//发送评论
        var url = common.interfaceBase + "SendComment?commentContent="+$('#txt-pinglun').val()+"&parentID=&dataID="+config.id+"&commentType=2&mentionMe=&picLable=";
        var method = "post";
        var result = WAjax(url,method);
        qpV.pinglunData = qpMethod.getPinglunData();
        $("#txt-pinglun").val("");
    };
    qpMethod.getBiaozhuData = function(){//获取切片标注数据
        var url = common.interfaceBase + "GetAnnoFromId/"+ config.id;
        var method = "get";
        var result = WAjax(url,method);
        qpV.biaozhuCount = result.length;
        return result;
    };
    qpMethod.drawPathologyOff = function(src){//绘制大体病理缓存
        var offSlice = new Image();
        offSlice.onload = function () {
            offscreenContext.drawImage(offSlice,0,0,w,w*sliceHeight/sliceWidth);
            qpMethod.drawPathology(offscreenCanvas);
        };
        offSlice.src = src;
    };
    qpMethod.drawPathology = function(){//绘制大体病理
        cxt.clearRect(0,0,canvas.width,canvas.height);
        cxt.drawImage(offscreenCanvas,0,0,offscreenCanvas.width,offscreenCanvas.height,imgX,imgY,offscreenCanvas.width*imgScale,offscreenCanvas.height*imgScale);
        imgX = newImgX;
        imgY = newImgY;
    };
    qpMethod.drawPathologyScale = function(center,type){//绘制大体病理缩放
        if(imgScale > mixScale && type=="in"){
            imgScale *= 19/20;
            imgScale = imgScale <= mixScale ? mixScale : imgScale;
        }else if(imgScale < maxScale && type=="out"){
            imgScale *= 21/20;
            imgScale = imgScale >= maxScale ? maxScale : imgScale;
        }
        center = qpMethod.windowToCanvas(canvas,center.x,center.y);
        imgX=center.x - (center.x-imgX)*imgScale;
        imgY=center.y - (center.y-imgY)*imgScale;
        qpMethod.drawPathology();
    };
    qpMethod.drawPathologyPY = function(e){//绘制大体病理平移
        imgX += e.deltaX;
        imgY += e.deltaY;
       /* imgX = imgX > 0?0:imgX;
        imgY = imgY > 0?0:imgY;
        imgX = imgX < -offscreenCanvas.width*imgScale + canvas.width ? -offscreenCanvas.width*imgScale + canvas.width : imgX;
        imgY = imgY < -offscreenCanvas.height*imgScale + canvas.height ? -offscreenCanvas.height*imgScale + canvas.height : imgY;*/
        qpMethod.drawPathology();
    };
    qpMethod.drawRotate = function(dir){//绘制大体病理3D旋转
        var PicsNum = qpV.sliceData.PicsNum;
        var n = config.step || 0; 
        if(dir=='left'){ 
            (n==0)? n=PicsNum:n--; 
        }else if(dir=='right'){ 
            (n==PicsNum)? n=0:n++; 
        } 
        config.step =n;
        var src = qpV.sliceData.FileURL+"/"+n+".jpg";
        qpMethod.drawPathologyOff(src);
    };
    qpMethod.editL = function(){//修改L瓦片求取规则
        L.TileLayer.WebDogTileLayer = L.TileLayer.extend({
            getTileUrl: function (tilePoint) {
                var urlArgs,
                    getUrlArgs = this.options.getUrlArgs;
         
                if (getUrlArgs) {
                    var urlArgs = getUrlArgs(tilePoint);
                } else {
                    urlArgs = {
                        z: tilePoint.z,
                        x: tilePoint.x,
                        y: tilePoint.y
                    };
                }
                return L.Util.template(this._url, L.extend(urlArgs, this.options, {s: this._getSubdomain(tilePoint)}));
            }
        });
        L.tileLayer.webdogTileLayer = function (url, options) {
            return new L.TileLayer.WebDogTileLayer(url, options);
        };
    };
    qpMethod.drawSlice = function(){//瓦片绘制
        var defaultZ = Math.ceil(log2(w/256));
        var oneZ =16-Math.floor(log2(qpV.width/256));
        var beginZ = oneZ + defaultZ - 1;
        var imageBaseSrc = qpV.sliceData.FileURL;
        var map = L.map('container').setView([51.505, -0.09], defaultZ);//初始化切片的中心位置和缩放大小
        //添加图层
        var url = imageBaseSrc+"/0/{z}/{x}-{y}.jpg";
        var options = {
            subdomain: '012',
            //tileSize:qpV.sliceData.Size,
            minZoom:1,
            maxZoom:16,
            zoomReverse:true,
            getUrlArgs: function (tilePoint) {
                var zr = oneZ + (16-Number(tilePoint.z));
                zr = zr>16?16:zr;
                var maxX = Math.ceil(qpV.width/256); - 1;
                var maxY = Math.ceil(qpV.height/256); - 1;
                var xr = tilePoint.x;
                xr = xr > maxX? maxX:xr;
                var yr = tilePoint.y;
                yr = yr> maxY? maxY:yr; 
                var a = {
                    z: zr,
                    x: xr ,//xr > maxX?maxX:xr,
                    y: yr //yr > maxY?maxY:yr,//tilePoint.y//Math.pow(2, tilePoint.z) - 1 - tilePoint.y
                }
                return a;
            }        
        };
        L.tileLayer.webdogTileLayer(url, options).addTo(map);
        L.marker([51.5, -0.09]).addTo(map)
            .bindPopup("单层立方上皮")
            //.openPopup();
        L.control.scale().addTo(map);
    };
    qpV.sliceData = qpMethod.getSliceData();
    qpV.pinglunData = qpMethod.getPinglunData()[0];
    qpV.biaozhuData = qpMethod.getBiaozhuData();
    document.title = config.name;
    if(config.type == "Pathology"){
        var sliceWidth = qpV.width;//切片宽度
        var sliceHeight = qpV.height;//切片高度
        var sliceSrc = qpV.sliceData.FileURL + "/0.jpg";//切片首屏地址
        var offscreenCanvas = document.createElement('canvas');//离屏canvas
        var offscreenContext = offscreenCanvas.getContext('2d');//离屏canvas画笔
            offscreenCanvas.width = w;
            offscreenCanvas.height = w*sliceHeight/sliceWidth;
        var canvas = document.getElementById("canvas");//获取画布
            canvas.width = w;
            if(config.a){
                canvas.height = h;
            }else{
                canvas.height = w * 2/3;
            }
        var cxt = canvas.getContext("2d");//获取画笔
        var imgX=0,imgY=0,imgScale=1;//图像的左上角坐标，以及缩放比例
        var newImgX=0,newImgY=0;//重置图像的左上角坐标
        var maxScale = 6;//最大缩放倍数
        var mixScale = 1;//最小缩放倍数
        qpMethod.drawPathologyOff(sliceSrc);
        qpMethod.drawRotate("left");//绘制下一帧缓存
        qpMethod.drawRotate("right");//绘制下一阵缓存
        $("#tool").show();
    }else{
        $("#tool").hide();
        if(config.a){
            $("#container").height(h);
        }else{
            $("#container").height($("#container").width()*2/3);
        }
        qpMethod.editL();
        qpMethod.drawSlice();
    }
    /*****触屏事件绑定*****/
    var sliceH = document.getElementById("slice");
    var hammer  = new Hammer(sliceH);//为该dom元素指定触屏移动事件
    hammer.add(new Hammer.Pinch());
    hammer.on('pinchin', function(e) {//缩小
        qpMethod.drawPathologyScale(e.center,"in");
    });
    hammer.on('pinchout', function(e) {//放大
        qpMethod.drawPathologyScale(e.center,"out");
    });
    hammer.on("panmove", function(e) {//平移    
        qpMethod.drawPathologyPY(e);     
    }); 
    hammer.on("panend", function(e) {//平移结束    
        newImgX += e.deltaX;
        newImgY += e.deltaY;
        imgX += e.deltaX;
        imgY += e.deltaY;
    }); 
    hammer.on("press", function(e) {//平移结束    
        qpMethod.drawRotate("left");//绘制下一帧缓存
    }); 
    /*****触屏事件结束*****/
    $("#arr_left").on("tap",function(){qpMethod.drawRotate("left")});
    $("#arr_right").on("tap",function(){qpMethod.drawRotate("right")});
    $(".tabs a").click(function(){//tab页签切换
        $(this).addClass("selected").siblings().removeClass("selected");
        var dataID = $(this).data('id');
        $("#" + dataID).removeClass("none").siblings().addClass("none");
    });
    $("#submit-pinglun").on("tap",qpMethod.setPinglunData);//评论按钮绑定事件
    $(".more_pinglun").on("tap",function(){//更多评论
        window.name = JSON.stringify(qpMethod.getPinglunData()[1]);
        window.location.href = "pinglunlist.html";
    })
})


    

