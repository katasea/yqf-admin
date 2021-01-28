var required = '<span style="color:red;font-weight:bold" data-qtip="必填">*</span>';

var comboStore1 = Ext.create('Ext.data.Store', {
    fields: ['index', 'name'],
    data : [
        {index: 'sbmbh', name: '下嘱医生部门'},
        {index: 'zbmbh', name: '执行医生部门'},
        //{index: 'mzorzy', name: '门诊或住院'},
        {index: 'sgrbh', name: '下嘱医生'},
        {index: 'zgrbh', name: '执行医生'},
        {index: 'itembh', name: '收费项目'},
        //{index: 'finc_item', name: '财务项目编号'},
        {index: 'reverse', name: '备注'}
    ]
});

var comboBox1 = Ext.create('Ext.form.ComboBox', {
    id: 'condition_id',
    name: 'condition_id',
    fieldLabel: '选择条件字段',
    store: comboStore1,
    queryMode: 'local',
    displayField: 'name',
    valueField: 'index',
    listeners:{
        'select': function (combo,records,eOpts ) {
            Ext.getCmp("condition_name").setValue(combo.getRawValue());
        }
    }
});

var comboBox2 = Ext.create('Ext.form.ComboBox', {
    id: 'adjustment_id',
    name: 'adjustment_id',
    fieldLabel: '选择调整字段',
    store: comboStore1,
    queryMode: 'local',
    displayField: 'name',
    valueField: 'index',
    listeners:{
        'select': function (combo,records,eOpts ) {
            Ext.getCmp("adjustment_name").setValue(combo.getRawValue());
        }
    }
});

Ext.define('MyApp.view.transform.CommonAdd', {
    extend: 'Ext.form.Panel',
    xtype: 'transformCommonAdd',
    autoScroll: true,
    requires: [
        '*',
        'Ext.ux.DataTip'
    ],
    viewConfig: {loadMask: {msg: "请稍后..."}},
    frame: false, bodyPadding: '10 10 10 10', border: 0,
    url: contextPath + '/transform/add',
    fieldDefaults: {labelWidth: 75},
    plugins: {ptype: 'datatip'},
    defaultType: 'textfield',
    items: [
            comboBox1,
            {
                fieldLabel: '条件字段名称', xtype: 'textfield',
                id: 'condition_name', name: 'condition_name',
                allowBlank: false, afterLabelTextTpl: required, tooltip: '条件字段名称'
            },{
                fieldLabel: '条件值', xtype: 'textfield',
                id: 'condition_value', name: 'condition_value',
                allowBlank: false, afterLabelTextTpl: required, tooltip: '条件值'
            },
             comboBox2,
            {
                fieldLabel: '调整字段名称', xtype: 'textfield',
                id: 'adjustment_name', name: 'adjustment_name',
                allowBlank: false, afterLabelTextTpl: required, tooltip: '调整字段名称'
            },
            {
                fieldLabel: '调整值', xtype: 'textfield',
                id: 'adjustment_value', name: 'adjustment_value',
                allowBlank: false, afterLabelTextTpl: required, tooltip: '调整后的值'
            }
     ],
    buttons: [{text: '保存', action: 'addSave'}, {text: '取消', action: 'addCancel'}],
    initComponent: function () {
        this.callParent();
    }
});
