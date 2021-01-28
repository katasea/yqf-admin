var required = '<span style="color:red;font-weight:bold" data-qtip="必填">*</span>';
Ext.define('MyApp.view.resourcesInfo.Add', {
    extend: 'Ext.form.Panel',
    xtype: 'resourcesInfoAdd',
    autoScroll:true,
    requires: [
    	'*',
        'Ext.ux.DataTip'
    ], 
    viewConfig:{loadMask:{msg:"请稍后..."}},
    frame: false,bodyPadding: '5 5 5 5',border:0,
    url:contextPath+'/resourcesInfo/save',
    fieldDefaults: {msgTarget: 'side',labelWidth: 80,anchor  : '85%'},
    plugins: {ptype: 'datatip'},
    defaultType: 'textfield',
    items: [
		    // {
		    //     fieldLabel: '资源编号',xtype:'textfield',
		    //     id:'resid',name: 'resid',
	        	// validator : function(value){
				// 	var isOk = false;var msg = "";
				// 	if(value != old) {
				// 		Ext.Ajax.request({
				// 			waitMsg : '正在校验资源编号是否已经存在,请稍后...',
				// 			url : contextPath+"/resourcesInfo/validator",
				// 			params : {'value' : value,'keyid' : 'resid'},//
				// 			method : "POST",timeout : 40000, async : false,
				// 			success : function(response, opts) {
				// 				var resObj = Ext.decode(response.responseText);
				// 				if(resObj.success){ isOk = true;}else{isOk = false;msg = resObj.msg;}
				// 			},failure: function(response, opts) {isOk = false; msg ="错误"; }
				// 		});
				// 	}else {isOk = true;}
				// 	if(isOk){return true;}else{return msg;}//错误原因
				// },
		    //     allowBlank: false,afterLabelTextTpl: required,tooltip: '请输入【必填项】【资源编号】'
		    // },
		    {
		        fieldLabel: '资源名称',xtype:'textfield',
		        id:'name',name: 'name',
		        allowBlank: false,afterLabelTextTpl: required,tooltip: '请输入【必填项】【资源名称】'
		    },
		    {
		        fieldLabel: '资源地址',xtype:'textfield',
		        id:'resurl',name: 'resurl',
		        allowBlank: true,afterLabelTextTpl: required,tooltip: '请输入【必填项】【资源地址】'
		    },
		    {
		        fieldLabel: '图标信息',xtype:'textfield',
		        id:'fa',name: 'fa',
		        allowBlank: false,afterLabelTextTpl: required,tooltip: '请输入【必填项】【图标信息】'
		    }    ],
    buttons: [{text: '保存',action:'save'},{text: '取消',action:'cancel'}],
    initComponent: function() {this.callParent();}
});
