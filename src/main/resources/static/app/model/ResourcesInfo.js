Ext.define('MyApp.model.ResourcesInfo', {
    extend: 'Ext.data.Model',
    fields: [
        { name: 'pkid', type: 'string'},
        { name: 'resid', type: 'string'},
        { name: 'name', type: 'string'},
        { name: 'resurl', type: 'string'},
        { name: 'parentid', type: 'string'},
        { name: 'isleaf', type: 'string'},
        { name: 'type', type: 'string'},
        { name: 'sort', type: 'int'},
        { name: 'fa', type: 'string'},
        { name: 'id', type: 'string'},
        { name: 'text', type: 'string'},
        { name: 'leaf', type: 'boolean'},
        { name: 'expanded', type: 'boolean'},
        { name: 'iconCls', type: 'string'}    ]
});

