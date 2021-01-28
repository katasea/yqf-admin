var required = '<span style="color:red;font-weight:bold" data-qtip="必填">*</span>';
Ext.define('MyApp.view.midDataFormulaDic.Add', {
    extend: 'Ext.form.Panel',
    xtype: 'midDataFormulaDicAdd',
    autoScroll:true,
    requires: [
    	'*',
        'Ext.ux.DataTip'
    ], 
    viewConfig:{loadMask:{msg:"请稍后..."}},
    frame: false,bodyPadding: '10 10 10 10',border:0,
    url:contextPath+'/midDataFormulaDic/save',
    fieldDefaults: {msgTarget: 'side',labelWidth: 100,anchor  : '75%'},
    plugins: {ptype: 'datatip'},
    defaultType: 'textfield',
    items: [
		    {
		        fieldLabel: '名称',xtype:'textfield',
		        id:'name',name: 'name',
		        allowBlank: false,afterLabelTextTpl: required,tooltip: '请输入【必填项】【名称】'
		    },
		    {
		        fieldLabel: '公式',xtype:'textfield',
		        id:'formula',name: 'formula',
		    	tooltip: '请输入【公式】'
		    },
			{
				xtype: "combobox",
				name: "isstop",
				id: "isstop",
				fieldLabel: '状态',
				store: new Ext.data.ArrayStore({
					fields: ['dickey', 'dicval'],
					data: [["0", '0 启用'],["1",'1 停用']]
				}),
				editable: false,
				displayField: "dicval",
				valueField: "dickey",
				allowBlank: false,
				afterLabelTextTpl: required,
				emptyText: "--请选择--",
				value:"0",
				queryMode: "local",
				tooltip: '请输入【状态】'
			},
		    {
		        fieldLabel: '小数位',xtype:'numberfield',
		        id:'dec',name: 'dec',
                value:2,
	       		decimalPrecision : '0',
		        allowBlank: false,afterLabelTextTpl: required,tooltip: '请输入【必填项】【小数位】'
		    },
			{
				fieldLabel: '排序号',xtype:'numberfield',
				id:'orderid',name: 'orderid',
				decimalPrecision : '0',
                blankText:'新增无需填写，默认自增',
				tooltip: '不填写默认自增'
        	}],
    buttons: [{text: '保存',action:'save'},{text: '取消',action:'cancel'}],
    initComponent: function() {this.callParent();}
});
