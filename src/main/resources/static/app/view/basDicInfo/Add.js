var required = '<span style="color:red;font-weight:bold" data-qtip="必填">*</span>';
Ext.define('MyApp.view.basDicInfo.Add', {
    extend: 'Ext.form.Panel',
    xtype: 'basDicInfoAdd',
    autoScroll:true,
    requires: [
    	'*',
        'Ext.ux.DataTip'
    ], 
    viewConfig:{loadMask:{msg:"请稍后..."}},
    frame: false,bodyPadding: '10 10 10 10',border:0,
    url:contextPath+'/basDicInfo/save',
    fieldDefaults: {msgTarget: 'side',labelWidth: 100,anchor  : '75%'},
    plugins: {ptype: 'datatip'},
    defaultType: 'textfield',
    items: [
		    {
		        fieldLabel: '字典名称',xtype:'textfield',
		        id:'dicname',name: 'dicname',
		        allowBlank: false,afterLabelTextTpl: required,tooltip: '请输入【必填项】【字典名称】'
		    },
		    {
		        fieldLabel: '业务保存数值',xtype:'textfield',
		        id:'dickey',name: 'dickey',
		        allowBlank: false,afterLabelTextTpl: required,tooltip: '请输入【必填项】【业务系统键】'
		    },
		    {
		        fieldLabel: '业务显示名称',xtype:'textfield',
		        id:'dicval',name: 'dicval',
		        allowBlank: false,afterLabelTextTpl: required,tooltip: '请输入【必填项】【业务系统值】'
		    },
		    {
		        fieldLabel: '字典状态',xtype:'radiogroup',
		    	items:[{inputValue: '0',boxLabel: '启用',anchor  : '60%',name  : 'isstop',checked: true}, {inputValue: '1',anchor  : '60%',name  : 'isstop',boxLabel: '停用'}],
		    	tooltip: '请输入【字典状态】'
		    }    ],
    buttons: [{text: '保存',action:'save'},{text: '取消',action:'cancel'}],
    initComponent: function() {this.callParent();}
});
