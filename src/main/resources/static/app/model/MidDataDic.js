Ext.define('MyApp.model.MidDataDic', {
    extend: 'Ext.data.Model',
    fields: [
        { name: 'companyid', type: 'string'},
        { name: 'companyname', type: 'string'},
        { name: 'id', type: 'string'},
        { name: 'name', type: 'string'},
        { name: 'type', type: 'int'},
        { name: 'isstop', type: 'string'},
        { name: 'parentid', type: 'string'},
        { name: 'parentname', type: 'string'},
        { name: 'deptorper', type: 'int'},
        { name: 'dec', type: 'int'}    ]
});

