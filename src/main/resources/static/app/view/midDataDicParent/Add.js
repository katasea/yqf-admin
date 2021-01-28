var required = '<span style="color:red;font-weight:bold" data-qtip="必填">*</span>';
Ext.define('MyApp.view.midDataDicParent.Add', {
    extend: 'Ext.form.Panel',
    xtype: 'midDataDicParentAdd',
    autoScroll:true,
    requires: [
    	'*',
        'Ext.ux.DataTip'
    ], 
    viewConfig:{loadMask:{msg:"请稍后..."}},
    frame: false,bodyPadding: '10 10 10 10',border:0,
    url:contextPath+'/midDataDicParent/save',
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
							url : contextPath+"/midDataDicParent/validator",
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
		        fieldLabel: '名称',xtype:'textfield',
		        id:'name',name: 'name',
		        allowBlank: false,afterLabelTextTpl: required,tooltip: '请输入【必填项】【名称】'
		    },
		    {
		        fieldLabel: '排序号',xtype:'numberfield',
		        id:'orderid',name: 'orderid',
	       		decimalPrecision : '0',
		    	tooltip: '请输入【排序号】'
		    }    ],
    buttons: [{text: '保存',action:'save'},{text: '取消',action:'cancel'}],
    initComponent: function() {this.callParent();}
});
