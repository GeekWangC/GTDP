// JavaScript Document
//下拉框
Zepto.fn.divselect = function() { 
	return $(this).each(function(index, e) {
		var that=$(this);
        that.tap(function(){
        	that.parents("li").siblings().find("dl").addClass('none');
			if(that.next("dl").hasClass("none")){
				that.next("dl").removeClass('none');
			} else {
				that.next("dl").addClass('none');
			}
			that.next("dl").on("tap","dd",function(){
				that.text($(this).text()); 
				if($(this).text() == "课程收藏" || $(this).text() == "课件收藏"){
					$("#newnew").hide();
					$("#new_list").show();
					$("#km_list").removeClass("none");
				} else if ($(this).text() == "试卷收藏" || $(this).text() == "试题收藏"){
					$("#newnew,#new_list").hide();
					$("#km_list").removeClass("none");
				}
				var value = $(this).data("id"); 
				that.next().next("input").val(value); 
				that.next("dl").addClass('none');
				return false;
			});
		});
    });
};