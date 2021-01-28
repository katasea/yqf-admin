Ext.define('MyApp.model.MidDataFormulaDic', {
    extend: 'Ext.data.Model',
    fields: [
        { name: 'pkid', type: 'string'},
        { name: 'companyid', type: 'string'},
        { name: 'id', type: 'string'},
        { name: 'orderid', type: 'int'},
        { name: 'name', type: 'string'},
        { name: 'formula', type: 'string'},
        { name: 'isstop', type: 'string'},
        { name: 'dec', type: 'int'},
        { name: 'caclfalg', type: 'int'},
        { name: 'deptorper', type: 'int'},
        { name: 'failreason', type: 'string'}    ]
});

