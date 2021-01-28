var required = '<span style="color:red;font-weight:bold" data-qtip="必填">*</span>';
Ext.define('MyApp.view.midData.Add', {
    extend: 'Ext.form.Panel',
    xtype: 'midDataAdd',
    autoScroll:true,
    requires: [
    	'*',
        'Ext.ux.DataTip'
    ], 
    viewConfig:{loadMask:{msg:"请稍后..."}},
    frame: false,bodyPadding: '10 10 10 10',border:0,
    url:contextPath+'/midData/save',
    fieldDefaults: {msgTarget: 'side',labelWidth: 100,anchor  : '75%'},
    plugins: {ptype: 'datatip'},
    defaultType: 'textfield',
    items: [
		    {
		        fieldLabel: '单位编号',xtype:'textfield',
		        id:'companyid',name: 'companyid',
		    	tooltip: '请输入【单位编号】'
		    },
		    {
		        fieldLabel: '编号',xtype:'textfield',
		        id:'id',name: 'id',
	        	validator : function(value){
					var isOk = false;var msg = "";
					if(value != old) {
						Ext.Ajax.request({
							waitMsg : '正在校验编号是否已经存在,请稍后...', 
							url : contextPath+"/midData/validator",
							params : {'value' : value,'keyid' : 'id'},//
							method : "POST",timeout : 40000, async : false,
							success : function(response, opts) {
								var resObj = Ext.decode(response.responseText);
								if(resObj.success){ isOk = true;}else{isOk = false;msg = resObj.msg;}
							},failure: function(response, opts) {isOk = false; msg ="错误"; }
						});
					}else {isOk = true;}
					if(isOk){return true;}else{return msg;}//错误原因
				},
		        allowBlank: false,afterLabelTextTpl: required,tooltip: '请输入【必填项】【编号】'
		    },
		    {
		        fieldLabel: '部门或个人',xtype:'numberfield',
		        id:'deptorper',name: 'deptorper',
	       		decimalPrecision : '0',
		    	tooltip: '请输入【部门或个人】'
		    },
		    {
		        fieldLabel: '月份',xtype:'numberfield',
		        id:'month',name: 'month',
	       		decimalPrecision : '0',
		    	tooltip: '请输入【月份】'
		    },
		    {
		        fieldLabel: '数据',xtype:'numberfield',
		        id:'data',name: 'data',
	       		decimalPrecision : '6',
		    	tooltip: '请输入【数据】'
		    },
		    {
		        fieldLabel: '过程数据',xtype:'textfield',
		        id:'process',name: 'process',
		    	tooltip: '请输入【过程数据】'
		    }    ],
    buttons: [{text: '保存',action:'save'},{text: '取消',action:'cancel'}],
    initComponent: function() {this.callParent();}
});
