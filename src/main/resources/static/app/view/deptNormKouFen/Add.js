var required = '<span style="color:red;font-weight:bold" data-qtip="必填">*</span>';
Ext.define('MyApp.view.deptNormKouFen.Add', {
    extend: 'Ext.form.Panel',
    xtype: 'deptNormKouFenAdd',
    autoScroll:true,
    requires: [
    	'*',
        'Ext.ux.DataTip'
    ], 
    viewConfig:{loadMask:{msg:"请稍后..."}},
    frame: false,bodyPadding: '10 10 10 10',border:0,
    url:contextPath+'/deptNormKouFen/save',
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
							url : contextPath+"/deptNormKouFen/validator",
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
		        fieldLabel: '科室id',xtype:'textfield',
		        id:'deptId',name: 'deptId',
		        allowBlank: false,afterLabelTextTpl: required,tooltip: '请输入【必填项】【科室id】'
		    },
		    {
		        fieldLabel: '科室编号',xtype:'textfield',
		        id:'deptNo',name: 'deptNo',
		        allowBlank: false,afterLabelTextTpl: required,tooltip: '请输入【必填项】【科室编号】'
		    },
		    {
		        fieldLabel: '科室名称',xtype:'textfield',
		        id:'deptName',name: 'deptName',
		        allowBlank: false,afterLabelTextTpl: required,tooltip: '请输入【必填项】【科室名称】'
		    },
		    {
		        fieldLabel: '指标id',xtype:'textfield',
		        id:'zhiBiaoId',name: 'zhiBiaoId',
		        allowBlank: false,afterLabelTextTpl: required,tooltip: '请输入【必填项】【指标id】'
		    },
		    {
		        fieldLabel: '指标类型',xtype:'textfield',
		        id:'zhiBiaoType',name: 'zhiBiaoType',
		        allowBlank: false,afterLabelTextTpl: required,tooltip: '请输入【必填项】【指标类型】'
		    },
		    {
		        fieldLabel: '扣分(单击可编辑)',xtype:'textfield',
		        id:'kouFen',name: 'kouFen',
		        allowBlank: false,afterLabelTextTpl: required,tooltip: '请输入【必填项】【扣分(单击可编辑)】'
		    },
		    {
		        fieldLabel: '实际扣分',xtype:'textfield',
		        id:'realKouFen',name: 'realKouFen',
		        allowBlank: false,afterLabelTextTpl: required,tooltip: '请输入【必填项】【实际扣分】'
		    },
		    {
		        fieldLabel: '最高扣分',xtype:'textfield',
		        id:'higeKouFen',name: 'higeKouFen',
		        allowBlank: false,afterLabelTextTpl: required,tooltip: '请输入【必填项】【最高扣分】'
		    },
		    {
		        fieldLabel: '扣分说明',xtype:'textfield',
		        id:'kouFenMark',name: 'kouFenMark',
		    	tooltip: '请输入【扣分说明】'
		    }    ],
    buttons: [{text: '保存',action:'save'},{text: '取消',action:'cancel'}],
    initComponent: function() {this.callParent();}
});
