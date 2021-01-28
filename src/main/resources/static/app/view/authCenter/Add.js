var required = '<span style="color:red;font-weight:bold" data-qtip="必填">*</span>';
Ext.define('MyApp.view.authCenter.Add', {
    extend: 'Ext.form.Panel',
    xtype: 'authCenterLeftAdd',
    autoScroll:true,
    requires: [
    	'*',
        'Ext.ux.DataTip'
    ], 
    viewConfig:{loadMask:{msg:"请稍后..."}},
    frame: false,bodyPadding: '10 10 10 10',border:0,
    url:contextPath+'/baseDic4List/save',
    fieldDefaults: {msgTarget: 'side',labelWidth: 100,anchor  : '75%'},
    plugins: {ptype: 'datatip'},
    defaultType: 'textfield',
    items: [
		    {
		        fieldLabel: '对象ID',xtype:'textfield',
		        id:'mkey',name: 'mkey',
	        	validator : function(value){
					var isOk = false;var msg = "";
					if(value != old) {
						Ext.Ajax.request({
							waitMsg : '正在校验对象ID是否已经存在,请稍后...', 
							url : contextPath+"/baseDic4List/validator",
							params : {'value' : value,'keyid' : 'mkey'},//
							method : "POST",timeout : 40000, async : false,
							success : function(response, opts) {
								var resObj = Ext.decode(response.responseText);
								if(resObj.success){ isOk = true;}else{isOk = false;msg = resObj.msg;}
							},failure: function(response, opts) {isOk = false; msg ="错误"; }
						});
					}else {isOk = true;}
					if(isOk){return true;}else{return msg;}//错误原因
				},
		        allowBlank: false,afterLabelTextTpl: required,tooltip: '请输入【必填项】【对象ID】'
		    },
		    {
		        fieldLabel: '类型',xtype:'numberfield',
		        id:'type',name: 'type',
	       		decimalPrecision : '0',readOnly:true,
		        allowBlank: false,afterLabelTextTpl: required,tooltip: '请输入【必填项】【类型】'
		    },
		    {
		        fieldLabel: '对象名称',xtype:'textfield',
		        id:'mvalue',name: 'mvalue',
		        allowBlank: false,afterLabelTextTpl: required,tooltip: '请输入【必填项】【对象名称】'
		    }   ],
    buttons: [{text: '保存',action:'save'},{text: '取消',action:'cancel'}],
    initComponent: function() {this.callParent();}
});
