
// 加载数据
var getDate = function(id,example2,event){
    
    var url = "http://115.29.97.231:3000/api/Mobile/GetSection?CourseID="+id;
    var method = "get";
    var result = WAjax(url,method);
    var nameAll;
    var nameCount;
    for (var i = 0; i < result.length; i++) {
        if (result[i].NodeName.split(" ")[1]!="") {
            result[i].nameAll=result[i].NodeName.split(" ")[1];
        }else{
            result[i].nameAll=result[i].NodeName.split(" ")[2];
        }
        result[i].nameCount=(i+1);
    };

    example2.itemsons = result;
    $("#bgwin").css({'display':'block'});
    $(event.target).addClass("selected").siblings().removeClass("selected");
    $(".exam_list").show();
    // event.stopPropagation();
    return id;
}

// 获取练习id
 var getexerciseIdp = function(isshoucang,ID,type,CourseID,collectType){


    var url = "http://115.29.97.231:3000/api/Mobile/CreateExercise?sections="+ID+"&type="+type+"&courseId="+CourseID+"&isNew=ture";
    var method = "post";
    var result = WAjax(url,method);
    var resultarray = new Array()
    resultarray[0] = result;
    resultarray[1] = gettestid(result,type);

    if (isshoucang) {
    if (ID!=undefined) {
         var url = "http://115.29.97.231:3000/api/Mobile/AddCollection/"+resultarray[0]+"?collectType="+collectType;
        // var url = "http://115.29.97.231:3000/api/Mobile/AddCollection/1655,1656?collectType=1";
        var method = "post";
        var result = WAjax(url,method);
     };
    };
    return resultarray;
}

 // 获取试题id
 var gettestid = function(ExerciseID,type){

    if (ExerciseID == -200) {
        alert("暂无试题");
        if (type == 3) {
            window.location.href="simulationtest.html";
        }else{
            window.location.href="lianxi.html";
        }
        return;
    };
    var url = "http://115.29.97.231:3000/api/Mobile/GetExercise?ExerciseID="+ExerciseID;
    var method = "get";
    var result = WAjax(url,method);

    return result;

 }

 // 加载试题
function loadDatap(Questionarray,QuestionNumber,ExerciseID){

    if (Questionarray != undefined) {
            
    var url = "http://115.29.97.231:3000/api/Mobile/GetSingleQuestion?QuestionID="+Questionarray[QuestionNumber]+"&ExerciseID="+ExerciseID;
    // var url = "http://115.29.97.231:3000/api/Mobile/GetSingleQuestion?QuestionID="+1+"&ExerciseID="+2055;
    var method = "get";
    var resultp = WAjax(url,method);
    }

    return loadDatapublic(resultp);
}
var tx;
var node;
function loadDatapublic(result){
    var ischoose = false;
    example1.items = result;
    tx = result.Questiontype;
    document.getElementById('submit_btns').style.display = "block";

    switch(tx)
    {
        case 0:
        Qtype="单";
        document.getElementById('submit_btns').style.display = "none";
         
         ischoose = true;
          break;
        case 1:
        Qtype="多";

        ischoose = true;

          break;
        case 2:
        Qtype="述";

          break; 
        case 3:
        Qtype="简";
          break;
        case 4:
        Qtype="自";

          break;    

        default:
          break;

    }

    if (ischoose) {
        document.getElementById('tiankong').style.display = "none";
        document.getElementById('exam_A_list').style.display = "block";
    }else{
        document.getElementById('tiankong').style.display = "block";
        document.getElementById('exam_A_list').style.display = "none";
    }

    if (checkHtml(result.Content) ) {
        if (checkHtmlImg(result.Content)) {
            var src=result.Content.substring(result.Content.indexOf("src=\"")+5,result.Content.indexOf("\">"));
            node=document.createElement("img"); //创建一个节点
            node.src = "http://115.29.97.231:8000"+src;
            node.id="imgid"
            document.getElementById("testimg").appendChild(node); //将li节点，添加到test几点下面


            $(".imgs").each(function(){
                $(this).val("http://115.29.97.231:3000"+src);
            });

            var reg=new RegExp(/src=['"]?([^'"]*)['"]?/i);
            var str=result.Content.replace(reg,'');

            document.getElementById("Questioncontentid").innerHTML=str;

        }else{
            if (document.getElementById("imgid") != undefined) {
                document.getElementById("imgid").remove(); 
            };
            
            var str=result.Content.replace(/<[^>]+>/g,"");
            document.getElementById("Questioncontentid").innerHTML=str;

        }
        

    }else{
        if (document.getElementById("imgid") != undefined) {
                document.getElementById("imgid").remove(); 
            };
        document.getElementById("Questioncontentid").innerHTML=result.Content;
    }

    document.getElementById("QuestionTypeid").innerHTML=Qtype;
    

    // 是否收藏
    if (result.IsCollect) {
        shoucangIsTrue();
        
    }else{
         shoucangIsFalse();

    }
    var QuestionNumbertitle=parseInt(QuestionNumber)+1;

    document.getElementById("allcount").innerHTML= QuestionNumbertitle+"/"+Questionarray.length;


    return result.Questiontype;
}

/**
  * 字符串是否含有html标签的检测
 * @param htmlStr
  */
 function checkHtml(htmlStr) {
     var  reg = /<[^>]+>/g;
     var testvalue=reg.test(htmlStr);
     return testvalue;
 }
/**
  * 字符串是否含有html img标签的检测
 * @param htmlStr
  */
 function checkHtmlImg(htmlStr) {
     var  reg = /<img[^>]+>/g;
     var testvalue=reg.test(htmlStr);
     return testvalue;
 }



// 提交试题

function submit(answerid,tiankong,ExerciseID,Questionarray,QuestionNumber){

     var optionIds= new Array

    if (answerid == 0) {//填空题
        optionIds=tiankong;

    }else{
        optionIds=answerid.split(",");
    }
    var data = {
        optionIds:optionIds,
        exerciseId:ExerciseID,  
        questionId:Questionarray[QuestionNumber]
    }

    var url = "http://115.29.97.231:3000/api/Mobile/SubmitQuestion";
    var method = "post";
    var result = WAjax(url,method,data);
   return result;
}



// 提交练习

function submitLX(answerid,tiankong,ExerciseID,Questionarray,QuestionNumber){
    var optionIds= new Array
    if(answerid!=0){
          optionIds=answerid.split(",");  
    }
   var result = submit(answerid,tiankong,ExerciseID,Questionarray,QuestionNumber);
   return isRight(result,optionIds,ExerciseID,Questionarray,QuestionNumber);
}


// 提交测试

function submitCS(answerid,ExerciseID,Questionarray,QuestionNumber){

    return submit(answerid,ExerciseID,Questionarray,QuestionNumber);

}


function isRight(result,optionIds,ExerciseID,Questionarray,QuestionNumber){

    if (result.IsRight) {

     return true;

    }else{
    document.getElementById('tiankong').style.display = "none";
    document.getElementById('exam_A_list').style.display = "block";
    document.getElementById('submit_btns').style.display = "none";

    // 根据返回结果改变按钮样式
    for (var i = optionIds.length - 1; i >= 0; i--) {

        document.getElementById(optionIds[i]).style.background="#f15e5e";
        document.getElementById(optionIds[i]).style.border="1px solid #f15e5e";
        document.getElementById(optionIds[i]).innerHTML="<img src='img/wrong.png'/>";
    };
           
    var resultanswer = result.Answer;
    var resultanswerarray = new Array()   //答案数组
    resultanswerarray = resultanswer.split(",");

    var Optionsarray = new Array(); //选项数组
    Optionsarray=example1.items.Options;


    for (var m = 0; m < Optionsarray.length; m++) {

        var title = Optionsarray[m].Title;//选项
        var titleid = Optionsarray[m].OptionID;//选项id
       for (var n = 0; n < resultanswerarray.length ; n++) {
        
        if (resultanswerarray[n] == title) {

            document.getElementById(titleid).style.background="#4dd1b0";
            document.getElementById(titleid).style.border="1px solid #4dd1b0";
            document.getElementById(titleid).innerHTML="<img src='img/right.png'/>";//正确答案图标
            break;   
        }

      };

    }

    lookdetail(Questionarray[QuestionNumber]);

    return false;
  }

}



var example3 = new Vue({        
                el: '#lookdetail',
                data: {
                    detail: []
                }
            }) 


// 试题详解
function lookdetail(QuestionId){

    $(".Q_introduce").css({'display':'block'});
    $("#commentid").css({'display':'block'});
    
    var url = "http://115.29.97.231:3000/api/Mobile/GetQuestionDetails?questionId="+QuestionId;
    var method = "get";
    var result = WAjax(url,method);
    example3.detail = result;
    var number=new Array()
    number = result.Replies;

    document.getElementById("detailid").innerHTML=result.Description;
}



// 收藏
function collectp(QuestionID,collectType){

    if ($("#shoucang").attr('src')=="img/exam-save.png"){//收藏

        var url = "http://115.29.97.231:3000/api/Mobile/AddCollection/"+QuestionID+"?collectType="+collectType;
        var method = "post";
        var result = WAjax(url,method);
        if (result) {
            shoucangIsTrue();
        };

    }else{//取消收藏

        var url = "http://115.29.97.231:3000/api/Mobile/DeleteCollection/"+QuestionID+"?collectType="+collectType;
        var method = "post";
        var result = WAjax(url,method);
        if (result) {
           shoucangIsFalse();
        };
    }
}

// 试题列表
function qulistp(isfist,Questionarray){
     if (isfist) {
         for (var i = 1; i < Questionarray.length+1; i++) {
            $('#test1').append('<li id="'+Questionarray[i-1]+'"  onclick="Questiolist('+Questionarray[i-1]+')">'+i+'</li>');
            isfist = false;
        };
    };
    
    return isfist;
}

function shoucangIsTrue(){
    document.getElementById("shoucang").src="img/exam-save2.png";
    $("#exam_save").addClass("selected");
    $("#shoucangcolor").addClass("selected");

    if(document.getElementById("shoucangBig")!=undefined){

        document.getElementById("shoucangBig").src="img/shoucang.png";
    }
}

function shoucangIsFalse(){
    document.getElementById("shoucang").src="img/exam-save.png";
        
        $("#exam_save").removeClass("selected");
        $("#shoucangcolor").removeClass("selected");

         if(document.getElementById("shoucangBig")!=undefined){
            
            document.getElementById("shoucangBig").src="img/save12.png";
        }
}

function getSortFun(order,sortBy){
    var ordAlpah = (order == "asc")?">":"<";
    var sortFun = new Function("a","b","return a." +sortBy+ordAlpah +"b." + sortBy+"?1:-1");
    return sortFun;
}





function getMyConmment(example7,url){

    var method = "get";
    var result = WAjax(url,method);

    var parentArray = new Array()

    var newResult = result.slice(0);


    parentArray = common.reCotainArr(0,newResult,"parentID");


    var lasrArray = [];
    var temp = "";  
    // 把子评论和父评论组合在一起
    for (var m = 0; m < parentArray.length; m++) {
        for (var n = 0; n < newResult.length; n++) {
            if (parentArray[m].commentID == newResult[n].parentID) {
                parentArray[m].childComment = newResult[n];
                temp = JSON.stringify(parentArray.slice(m,m+1)[0]);
                //alert(newResult[n].commentContent);
                lasrArray.push(JSON.parse(temp));
                newResult.splice(n,1);
                n=n-1;
                
            }else{
                if (parentArray[m].childComment == undefined) {
                    parentArray[m].childComment = 0;
                };
            }
        };
    };
// 没有子评论
    for (var i = 0; i < parentArray.length; i++) {
        if (parentArray[i].childComment == 0) {
            lasrArray.push(parentArray[i]);
        };
    };



    for (var i = 0; i < lasrArray.length; i++) {
        if (lasrArray[i].childComment != 0 ) {
            lasrArray[i].childComment.mytime=new Date(lasrArray[i].childComment.creatTime).format('yyyy年MM月dd日');
            lasrArray[i].mycreatTime=lasrArray[i].childComment.creatTime;

        }else{

            lasrArray[i].mytime=new Date(lasrArray[i].creatTime).format('yyyy年MM月dd日');
            lasrArray[i].mycreatTime=lasrArray[i].creatTime;

        }
        

        
    };

        lasrArray.sort(getSortFun("desc","mycreatTime"));


    example7.commentdetail = lasrArray;

    document.getElementById("conmmentcountid").innerHTML= "共"+result.length+"条" ;
    
}

