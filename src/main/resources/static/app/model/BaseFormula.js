Ext.define('MyApp.model.BaseFormula', {
    extend: 'Ext.data.Model',
    fields: [
        { name: 'companyid', type: 'string'},
        { name: 'name', type: 'string'},
        { name: 'formula', type: 'string'},
        { name: 'isstop', type: 'string'},
        { name: 'type', type: 'string'}    ]
});

