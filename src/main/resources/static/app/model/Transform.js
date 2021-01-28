Ext.define('MyApp.model.Transform', {
    extend: 'Ext.data.Model',
    fields: [
        { name: 'id',type: 'string'},
        { name: 'condition_id', type: 'string'},
        { name: 'condition_name', type: 'string'},
        { name: 'condition_value', type: 'string'},
        { name: 'adjustment_id', type: 'string'},
        { name: 'adjustment_name', type: 'string'},
        { name: 'adjustment_value', type: 'string'},
        { name: 'transform_percent',type:'string'}
        ]
});


var comboStore1 = Ext.create('Ext.data.Store', {
    fields: ['index', 'name'],
    data : [
        {index: 'order_dept', name: '下嘱医生部门'},
        {index: 'do_dept', name: '执行医生部门'},
        {index: 'mzorzy', name: '门诊或住院'},
        {index: 'order_staff', name: '下嘱医生'},
        {index: 'do_staff', name: '执行医生'},
        {index: 'item', name: '收费项目'},
        {index: 'finc_item', name: '财务项目编号'},
        {index: 'remark', name: '备注'}
    ]
});
