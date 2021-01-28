Ext.define('MyApp.model.BasDicInfo', {
    extend: 'Ext.data.Model',
    fields: [
        { name: 'dicid', type: 'string'},
        { name: 'dicname', type: 'string'},
        { name: 'zjm', type: 'string'},
        { name: 'dickey', type: 'string'},
        { name: 'dicval', type: 'string'},
        { name: 'parentid', type: 'string'},
        { name: 'isleaf', type: 'int'},
        { name: 'isstop', type: 'int'},
        { name: 'id', type: 'string'},
        { name: 'text', type: 'string'},
        { name: 'leaf', type: 'boolean'},
        { name: 'expanded', type: 'boolean'},
        { name: 'iconCls', type: 'string'}    ]
});

