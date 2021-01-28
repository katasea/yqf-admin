Ext.define('MyApp.controller.InitSys', {
    extend: 'Ext.app.Controller',
    stores: [],
    models: [],
    views:  ['InitSysForm'],
    init: function () {
    	//this can deal and add component listener
    	this.control({
    	 	'myViewInitSysForm button[action=save]': {
                click: this.initSys
            },
            'myViewInitSysForm button[action=cancel]': {
                click: this.cancel
            }
        });
    },
    cancel : function(btn){
        window.location.href = getRootPath();
    },
    initSys:function(btn){
    	var form = btn.up('form').getForm();
        if (form.isValid()) {
        	mask.show();
            // Submit the Ajax request and handle the response
            form.submit({
            	params:{},
                success: function(form, action) {
                   mask.hide();
                   alert('初始化数据成功！点击确定跳转到登录页面');
                   window.location.href = getRootPath();
                },
                failure: function(form, action) {
                	mask.hide();
                    Ext.Msg.alert('失败了', action.result ? action.result.message : '服务器未响应');
                }
            });
        }

    }
});
