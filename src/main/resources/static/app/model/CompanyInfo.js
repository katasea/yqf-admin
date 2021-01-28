Ext.define('MyApp.model.CompanyInfo', {
    extend: 'Ext.data.Model',
    fields: [
        { name: 'companyid', type: 'string'},
        { name: 'companyname', type: 'string'},
        { name: 'isstop', type: 'int'}    ]
});

