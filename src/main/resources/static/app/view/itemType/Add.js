var required = '<span style="color:red;font-weight:bold" data-qtip="必填">*</span>';
Ext.define('MyApp.view.itemType.Add', {
    extend: 'Ext.form.Panel',
    xtype: 'itemTypeAdd',
    autoScroll:true,
    requires: [
    	'*',
        'Ext.ux.DataTip'
    ], 
    viewConfig:{loadMask:{msg:"请稍后..."}},
    frame: false,bodyPadding: '10 10 10 10',border:0,
    url:contextPath+'/itemType/save',
    fieldDefaults: {msgTarget: 'side',labelWidth: 100, anchor: '75%'},
    plugins: {ptype: 'datatip'},
    defaultType: 'textfield',
    items: [
		    {
		        fieldLabel: '分类编码',xtype:'textfield',
		        id:'code',name: 'code',
		        anchor : '75%',
	        	validator : function(value){
					var isOk = false;var msg = "";
					if(value != old) {
						Ext.Ajax.request({
							waitMsg : '正在校验分类编码是否已经存在,请稍后...', 
							url : contextPath+"/itemType/validator",
							params : {'value' : value,'keyid' : 'code'},//
							method : "POST",timeout : 40000, async : false,
							success : function(response, opts) {
								var resObj = Ext.decode(response.responseText);
								if(resObj.success){ isOk = true;}else{isOk = false;msg = resObj.msg;}
							},failure: function(response, opts) {isOk = false; msg ="错误"; }
						});
					}else {isOk = true;}
					if(isOk){return true;}else{return msg;}//错误原因
				},
		        allowBlank: false,afterLabelTextTpl: required,tooltip: '请输入【必填项】【分类编码】'
		    },
		    {
		        fieldLabel: '分类名称',xtype:'textfield',
		        id:'text',name: 'text',
		        anchor : '75%',
		        allowBlank: false,afterLabelTextTpl: required,tooltip: '请输入【必填项】【分类名称】'
		    },
		    {
		        fieldLabel: '收支类型',xtype:'radiogroup',
		        anchor : '40%',
		    	items:[{inputValue: '1',boxLabel: '收入类型',anchor  : '30%',name  : 'itemtype',checked: true}, {inputValue: '2',anchor  : '30%',name  : 'itemtype',boxLabel: '支出类型'}],
		    	tooltip: '请输入【收支类型】'
		    },
		    {
		        fieldLabel: '是否停用',xtype:'radiogroup',
		        anchor : '40%',
		    	items:[{inputValue: '1',boxLabel: '是',anchor  : '10%',name  : 'isstop',checked: true}, {inputValue: '0',anchor  : '10%',name  : 'isstop',boxLabel: '否'}],
		    	tooltip: '请输入【是否停用】'
		    }    ],
    buttons: [{text: '保存',action:'save'},{text: '取消',action:'cancel'}],
    initComponent: function() {this.callParent();}
});
