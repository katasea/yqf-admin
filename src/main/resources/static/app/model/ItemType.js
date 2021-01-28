Ext.define('MyApp.model.ItemType', {
    extend: 'Ext.data.Model',
    fields: [
        { name: 'pid', type: 'string'},
        { name: 'companyid', type: 'string'},
        { name: 'code', type: 'string'},
        { name: 'text', type: 'string'},
        { name: 'itemtype', type: 'string'},
        { name: 'parentid', type: 'string'},
        { name: 'isleaf', type: 'string'},
        { name: 'isstop', type: 'int'},
        { name: 'id', type: 'string'},
        { name: 'text', type: 'string'},
        { name: 'leaf', type: 'boolean'},
        { name: 'expanded', type: 'boolean'},
        { name: 'iconCls', type: 'string'}    ]
});

