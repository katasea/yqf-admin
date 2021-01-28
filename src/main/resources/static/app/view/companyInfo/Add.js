var required = '<span style="color:red;font-weight:bold" data-qtip="必填">*</span>';
Ext.define('MyApp.view.companyInfo.Add', {
    extend: 'Ext.form.Panel',
    xtype: 'companyInfoAdd',
    autoScroll:true,
    requires: [
    	'*',
        'Ext.ux.DataTip'
    ], 
    viewConfig:{loadMask:{msg:"请稍后..."}},
    frame: false,bodyPadding: '10 10 10 10',border:0,
    url:contextPath+'/companyInfo/save',
    fieldDefaults: {msgTarget: 'side',labelWidth: 100,anchor  : '75%'},
    plugins: {ptype: 'datatip'},
    defaultType: 'textfield',
    items: [
		    {
		        fieldLabel: '单位编号',xtype:'textfield',
		        id:'companyid',name: 'companyid',
	        	validator : function(value){
					var isOk = false;var msg = "";
					if(value != old) {
						Ext.Ajax.request({
							waitMsg : '正在校验单位编号是否已经存在,请稍后...', 
							url : contextPath+"/companyInfo/validator",
							params : {'value' : value,'keyid' : 'companyid'},//
							method : "POST",timeout : 40000, async : false,
							success : function(response, opts) {
								var resObj = Ext.decode(response.responseText);
								if(resObj.success){ isOk = true;}else{isOk = false;msg = resObj.msg;}
							},failure: function(response, opts) {isOk = false; msg ="错误"; }
						});
					}else {isOk = true;}
					if(isOk){return true;}else{return msg;}//错误原因
				},
		        allowBlank: false,afterLabelTextTpl: required,tooltip: '请输入【必填项】【单位编号】'
		    },
		    {
		        fieldLabel: '单位名称',xtype:'textfield',
		        id:'companyname',name: 'companyname',
		        allowBlank: false,afterLabelTextTpl: required,tooltip: '请输入【必填项】【单位名称】'
		    },
		    {
		        fieldLabel: '单位状态',xtype:'radiogroup',
		    	items:[{inputValue: '0',boxLabel: '启用',anchor  : '60%',name  : 'isstop',checked: true}, {inputValue: '1',anchor  : '60%',name  : 'isstop',boxLabel: '停用'}],
		    	tooltip: '请输入【单位状态】'
		    }    ],
    buttons: [{text: '保存',action:'save'},{text: '取消',action:'cancel'}],
    initComponent: function() {this.callParent();}
});
