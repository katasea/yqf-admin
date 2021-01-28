Ext.define('MyApp.model.TreeNode', {
    extend: 'Ext.data.Model',
    fields: [
        { name: 'id', type: 'string' },
        { name: 'text', type: 'string' },
        { name: 'parentId', type: 'string' },
        { name: 'leaf', type: 'boolean' },
        { name: 'expanded', type: 'boolean' },
        { name: 'status', type : 'string'},
        { name: 'deptorper', type : 'string'},
        { name: 'children',type : 'TreeNode[]'}
    ]
});