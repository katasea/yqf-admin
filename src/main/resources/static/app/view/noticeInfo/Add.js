var required = '<span style="color:red;font-weight:bold" data-qtip="必填">*</span>';
Ext.define('MyApp.view.noticeInfo.Add', {
    extend: 'Ext.form.Panel',
    xtype: 'noticeInfoAdd',
    autoScroll:true,
    requires: [
    	'*',
        'Ext.ux.DataTip'
    ], 
    viewConfig:{loadMask:{msg:"请稍后..."}},
    frame: false,bodyPadding: '10 10 10 10',border:0,
    url:contextPath+'/noticeInfo/save',
    fieldDefaults: {msgTarget: 'side',labelWidth: 80,anchor  : '100%'},
    plugins: {ptype: 'datatip'},
    defaultType: 'textfield',
    items: [
		    {
		        fieldLabel: '通知标题',xtype:'textfield',
		        id:'title',name: 'title',
		        allowBlank: false,afterLabelTextTpl: required,tooltip: '请输入【必填项】【通知标题】'
		    },
		    {
		        fieldLabel: '通知内容',xtype:'textarea',rows:10,cols:80,
		        id:'content',name: 'content',
		        allowBlank: false,afterLabelTextTpl: required,tooltip: '请输入【必填项】【通知内容】'
		    },
            {
                fieldLabel: '通知类型', xtype: 'radiogroup',
                items: [
                    // {inputValue: '1', anchor: '33%', name: 'enable', boxLabel: '系统通知'},
                    {inputValue: '2', anchor: '33%', name: 'type', boxLabel: '通知', checked: true},
                    {inputValue: '3', anchor: '33%', name: 'type', boxLabel: '提醒'}
                ]
            }],
    buttons: [{text: '保存',action:'save'},{text: '取消',action:'cancel'}],
    initComponent: function() {this.callParent();}
});
