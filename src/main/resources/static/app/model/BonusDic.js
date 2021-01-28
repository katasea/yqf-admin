Ext.define('MyApp.model.BonusDic', {
    extend: 'Ext.data.Model',
    fields: [
        { name: 'companyid', type: 'string'},
        { name: 'bh', type: 'string'},
        { name: 'year', type: 'string'},
        { name: 'month', type: 'string'},
        { name: 'name', type: 'string'},
        { name: 'grade', type: 'int'},
        { name: 'leaf', type: 'int'},
        { name: 'formula', type: 'string'},
        { name: 'parentid', type: 'string'},
        { name: 'isstop', type: 'int'}    ]
});

