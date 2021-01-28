var required = '<span style="color:red;font-weight:bold" data-qtip="必填">*</span>';
Ext.define('MyApp.view.norm.Add', {
    extend: 'Ext.form.Panel',
    xtype: 'normAdd',
    autoScroll:true,
    requires: [
    	'*',
        'Ext.ux.DataTip'
    ], 
    viewConfig:{loadMask:{msg:"请稍后..."}},
    frame: false,bodyPadding: '10 10 10 10',border:0,
    url:contextPath+'/norm/save?deptOrPerson='+deptOrPerson,
    fieldDefaults: {msgTarget: 'side',labelWidth: 100,anchor  : '75%'},
    plugins: {ptype: 'datatip'},
    defaultType: 'textfield',
    items: [
		    {
		        fieldLabel: '指标名称',xtype:'textfield',
		        id:'text',name: 'text',
		    	tooltip: '请输入【指标名称】'
		    },
		    {
		        fieldLabel: '考核标准',xtype:'textfield',
		        id:'checknorm',name: 'checknorm',
		    	tooltip: '请输入【考核标准】'
		    },
		    {
		        fieldLabel: '评分标准',xtype:'textfield',
		        id:'recordnorm',name: 'recordnorm',
		    	tooltip: '请输入【评分标准】'
		    },
		    {
		        fieldLabel: '扣分类型',xtype:'radiogroup',
		    	items:[{inputValue: '1',boxLabel: '按次扣分',anchor  : '10%',name  : 'kouFenType',checked: true}, {inputValue: '2',anchor  : '10%',name  : 'kouFenType',boxLabel: '加分项'}, {inputValue: '3',anchor  : '10%',name  : 'kouFenType',boxLabel: '标准比例计分'}],
		    	tooltip: '请输入【扣分类型】'
		    },
		    {
		        fieldLabel: '指标类型',xtype:'radiogroup',
		    	items:[{inputValue: '1',boxLabel: '月',anchor  : '10%',name  : 'zhiBiaoType',checked: true}, {inputValue: '2',anchor  : '10%',name  : 'zhiBiaoType',boxLabel: '季度'}, {inputValue: '3',anchor  : '10%',name  : 'zhiBiaoType',boxLabel: '年度'}],
		    	tooltip: '请输入【指标类型】'
		    },
		    {
		        fieldLabel: '绩效占比',xtype:'numberfield',
		        id:'jiXiaoZhanBi',name: 'jiXiaoZhanBi',
	       		decimalPrecision : '6',
		    	tooltip: '请输入【绩效占比】'
		    },
		    {
		        fieldLabel: '公式',xtype:'textfield',
		        id:'formula',name: 'formula',
		    	tooltip: '请输入【公式】'
		    },
		    {
		        fieldLabel: '是否计分',xtype:'radiogroup',
		    	items:[{inputValue: '1',boxLabel: '是',anchor  : '10%',name  : 'iscomp',checked: true}, {inputValue: '0',anchor  : '10%',name  : 'iscomp',boxLabel: '否'}],
		    	tooltip: '请输入【是否计分】'
		    },
		    {
		        fieldLabel: '是否停用',xtype:'radiogroup',
		    	items:[{inputValue: '1',boxLabel: '是',anchor  : '10%',name  : 'isstop',checked: true}, {inputValue: '0',anchor  : '10%',name  : 'isstop',boxLabel: '否'}],
		    	tooltip: '请输入【是否停用】'
		    },
		    {
		        fieldLabel: '标准分数',xtype:'numberfield',
		        id:'score',name: 'score',
	       		decimalPrecision : '6',
		    	tooltip: '请输入【标准分数】'
		    },
		    {
		        fieldLabel: '单词扣分',xtype:'numberfield',
		        id:'rec1',name: 'rec1',
	       		decimalPrecision : '6',
		    	tooltip: '请输入【单词扣分】'
		    },
		    {
		        fieldLabel: '最多扣分',xtype:'numberfield',
		        id:'recsum',name: 'recsum',
	       		decimalPrecision : '6',
		    	tooltip: '请输入【最多扣分】'
		    },
		    {
		        fieldLabel: '扣分是否可改',xtype:'radiogroup',
		    	items:[{inputValue: '1',boxLabel: '是',anchor  : '10%',name  : 'recforce',checked: true}, {inputValue: '0',anchor  : '10%',name  : 'recforce',boxLabel: '否'}],
		    	tooltip: '请输入【扣分是否可改】'
		    }    ],
    buttons: [{text: '保存',action:'save'},{text: '取消',action:'cancel'}],
    initComponent: function() {this.callParent();}
});
