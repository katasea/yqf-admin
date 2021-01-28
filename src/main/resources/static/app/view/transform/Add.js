var required = '<span style="color:red;font-weight:bold" data-qtip="必填">*</span>';
Ext.define('MyApp.view.transform.Add', {
    extend: 'Ext.form.Panel',
    xtype: 'transformAdd',
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
                fieldLabel: '原始编号', xtype: 'textfield',
                id: 'condition_id', name: 'condition_id',
                allowBlank: false, afterLabelTextTpl: required, tooltip: '请输入原始编号'
            },
            {
                fieldLabel: '原始名称', xtype: 'textfield',
                id: 'condition_name', name: 'condition_name',
                allowBlank: false, afterLabelTextTpl: required, tooltip: '请输入原始名称'
            },
            {
                fieldLabel: '系统编号', xtype: 'textfield',
                id: 'adjustment_id', name: 'adjustment_id',
                allowBlank: false, afterLabelTextTpl: required, tooltip: '请输入系统编号'
            },
            {
                fieldLabel: '系统名称', xtype: 'textfield',
                id: 'adjustment_name', name: 'adjustment_name',
                allowBlank: false, afterLabelTextTpl: required, tooltip: '请输入系统名称'
            }
     ],
    buttons: [{text: '保存', action: 'addSave'}, {text: '取消', action: 'addCancel'}],
    initComponent: function () {
        this.callParent();
    }
});