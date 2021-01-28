Ext.define('MyApp.model.AuthCenter', {
    extend: 'Ext.data.Model',
    fields: [
        { name: 'pkid', type: 'string'},
        { name: 'companyid', type: 'string'},
        { name: 'morig', type: 'string'},
        { name: 'morigname', type: 'string'},
        { name: 'funcode', type: 'string'},
        { name: 'mtarg', type: 'string'},
        { name: 'mtargname', type: 'string'}    ]
});

