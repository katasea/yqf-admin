var required = '<span style="color:red;font-weight:bold" data-qtip="必填">*</span>';
Ext.define('MyApp.view.transform.NurseAdd', {
    extend: 'Ext.form.Panel',
    xtype: 'transformNurseAdd',
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
            {
                fieldLabel: '执行医生科室编号', xtype: 'textfield',
                id: 'condition_id', name: 'condition_id',
                allowBlank: false, afterLabelTextTpl: required, tooltip: '请输入执行医生科室编号'
            },
            {
                fieldLabel: '执行医生科室名称', xtype: 'textfield',
                id: 'condition_name', name: 'condition_name',
                allowBlank: false, afterLabelTextTpl: required, tooltip: '请输入执行医生科室名称'
            },
            {
                fieldLabel: '护士科室编号', xtype: 'textfield',
                id: 'adjustment_id', name: 'adjustment_id',
                allowBlank: false, afterLabelTextTpl: required, tooltip: '请输入护士科室编号'
            },
            {
                fieldLabel: '护士科室名称', xtype: 'textfield',
                id: 'adjustment_name', name: 'adjustment_name',
                allowBlank: false, afterLabelTextTpl: required, tooltip: '请输入护士科室名称'
            },
        {
            fieldLabel: '护士比例', xtype: 'textfield',
            id: 'transform_percent', name: 'transform_percent',
            allowBlank: false, afterLabelTextTpl: required, tooltip: '请输入护士比例'
        }
     ],
    buttons: [{text: '保存', action: 'addSave'}, {text: '取消', action: 'addCancel'}],
    initComponent: function () {
        this.callParent();
    }
});