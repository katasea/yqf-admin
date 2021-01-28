Ext.define('MyApp.model.DeptInfo', {
    extend: 'Ext.data.Model',
    fields: [
        { name: 'pid', type: 'string'},
        { name: 'companyid', type: 'string'},
        { name: 'deptid', type: 'string'},
        { name: 'deptname', type: 'string'},
        { name: 'zjm', type: 'string'},
        { name: 'depttype', type: 'string'},
        { name: 'depttypeText', type: 'string'},
        { name: 'parentid', type: 'string'},
        { name: 'isleaf', type: 'string'},
        { name: 'isstop', type: 'string'},
        { name: 'id', type: 'string'},
        { name: 'text', type: 'string'},
        { name: 'leaf', type: 'boolean'},
        { name: 'expanded', type: 'boolean'},
        { name: 'stoptime', type: 'string'},
        { name: 'inserttime', type: 'string'},
        { name: 'iconCls', type: 'string'}    ]
});

