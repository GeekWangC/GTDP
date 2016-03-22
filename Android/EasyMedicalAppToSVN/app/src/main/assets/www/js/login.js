//登录特效
$(".form_box").find("input").blur(function(){
	if( this.value=="" ){
		//为空时移除类wrong
		$(this).parent().removeClass("no-empty");
		//错误或者为空时添加类wrong
		$(this).parent().addClass("wrong");
	}else{
		//不为空时添加类wrong
		$(this).parent().addClass("no-empty");
		//正确或者不为空时移除类wrong
		$(this).parent().removeClass("wrong");
	}
	
	if( $(this).is('#user_IP') ){
		if( this.value == "" ){
			$(".color1").hide();
		} else {
			$(".color1").css({'display':'inline-block'});
		}
	}
}).keyup(function(){
    $(this).triggerHandler("blur");
}).focus(function(){
	$(this).parent().addClass("no-empty");
	$(this).parent().removeClass("wrong");
});