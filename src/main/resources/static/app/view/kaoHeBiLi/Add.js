var required = '<span style="color:red;font-weight:bold" data-qtip="必填">*</span>';
Ext.define('MyApp.view.kaoHeBiLi.Add', {
    extend: 'Ext.form.Panel',
    xtype: 'kaoHeBiLiAdd',
    autoScroll:true,
    requires: [
    	'*',
        'Ext.ux.DataTip'
    ], 
    viewConfig:{loadMask:{msg:"请稍后..."}},
    frame: false,bodyPadding: '10 10 10 10',border:0,
    url:contextPath+'/kaoHeBiLi/save',
    fieldDefaults: {msgTarget: 'side',labelWidth: 100,anchor  : '75%'},
    plugins: {ptype: 'datatip'},
    defaultType: 'textfield',
    items: [
		    {
		        fieldLabel: '编号',xtype:'textfield',
		        id:'kid',name: 'kid',
	        	validator : function(value){
					var isOk = false;var msg = "";
					if(value != old) {
						Ext.Ajax.request({
							waitMsg : '正在校验编号是否已经存在,请稍后...', 
							url : contextPath+"/kaoHeBiLi/validator",
							params : {'value' : value,'keyid' : 'kid'},//
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
		        fieldLabel: '单位编号',xtype:'textfield',
		        id:'companyid',name: 'companyid',
		        allowBlank: false,afterLabelTextTpl: required,tooltip: '请输入【必填项】【单位编号】'
		    },
		    {
		        fieldLabel: '部门编号',xtype:'textfield',
		        id:'deptId',name: 'deptId',
		        allowBlank: false,afterLabelTextTpl: required,tooltip: '请输入【必填项】【部门编号】'
		    },
		    {
		        fieldLabel: '指标编号',xtype:'textfield',
		        id:'zbId',name: 'zbId',
		        allowBlank: false,afterLabelTextTpl: required,tooltip: '请输入【必填项】【指标编号】'
		    },
		    {
		        fieldLabel: '自评比例(%)',xtype:'textfield',
		        id:'zpbl',name: 'zpbl',
		        allowBlank: false,afterLabelTextTpl: required,tooltip: '请输入【必填项】【自评比例(%)】'
		    },
		    {
		        fieldLabel: '质控科比例(%)',xtype:'textfield',
		        id:'zkkbl',name: 'zkkbl',
		        allowBlank: false,afterLabelTextTpl: required,tooltip: '请输入【必填项】【质控科比例(%)】'
		    }    ],
    buttons: [{text: '保存',action:'save'},{text: '取消',action:'cancel'}],
    initComponent: function() {this.callParent();}
});
