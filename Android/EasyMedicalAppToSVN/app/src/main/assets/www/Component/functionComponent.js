var FunctionComponent = Vue.extend({//功能按键组件
	template:'<div id="bgwin"></div>'+
			'<div class="fixed_list">'+
				'<ul>'+
			    	'<li id="messageID" v-on:tap="gotoMessage"><a href="javascript:void(0);">消息</a></li>'+
			        '<li id="courseID" v-on:tap="gotoHome"><a href="javascript:void(0);">返回主页</a></li>'+
			    '</ul>'+
			    '<div class="cancleImg" v-on:tap="cancle"><a href="javascript:void(0);">取消</a></div>'+
			'</div>',
	methods:{
		cancle:function(){//取消
			$("#bgwin,.fixed_list").hide();
		},
		gotoHome:function(){//返回主页
			common.backHome();
		},
		gotoMessage:function(){//消息
			common.recordHistory(window.location.href);
    		window.location.href='messages.html';
		}
	}
});
var TopComponent = Vue.extend({//top组件
	props: ['topTitle'],
	template:'<div class="top">'+
				'<div class="back" v-on:tap="gotoHistory"><a href="javascript:void(0)" ><img src="img/back.png" /></a></div>'+
				'<div class="more" v-on:tap="gotoMore"><a href="###">。。。</a></div>'+
			    '<h1>{{topTitle}}</h1>'+
			'</div>',
	methods:{
		gotoHistory:function(){
			common.backHistory();
		},
		gotoMore:function(){
			$("#bgwin").css({'display':'block'});
			$(".fixed_list").show();
		}
	}
});
Vue.component('function-component', FunctionComponent);
Vue.component('top-component', TopComponent);