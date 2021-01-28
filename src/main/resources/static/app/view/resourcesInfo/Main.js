Ext.define('MyApp.view.resourcesInfo.Main', {
    extend: 'Ext.tree.Panel',
    title: '',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.tree.*',
        'Ext.ux.CheckColumn'
    ],
    xtype: 'resourcesInfoMain',
    useArrows: true,
    //iconCls:'Tree',
    lines: true,
    collapseFirst: false,
    tbar: [
        {xtype: 'button', text: '刷新', action: 'refresh', iconCls: 'Refresh'}, '-',
        {xtype: 'button', text: '新增', action: 'add', iconCls: 'Add'}, '-',
        {xtype: 'button', text: '初始内置', action: 'autoInitFromAccess', iconCls: 'Cogwheel'}, '-',
        {
            id: 'keyword',
            name: 'keyword',
            width: 170,
            labelWidth: 70,
            xtype: 'textfield',
            emptyText: '请输入关键字'
        }, '-', {
            id: 'querybtn',
            text: '搜索',
            iconCls: 'Find',
            action: 'search'
        }, '-',
        '->', '将鼠标悬浮在操作图标上可获得对应提示'
    ],
    store: 'ResourcesInfo',
    rowLines: true,
    autoExpandColumn: 'resurlautocolumnid',
    viewConfig: {
        loadMask: {msg: "请稍后..."}
    },
    columnLines: true,
    rootVisible: false,
    multiSelect: true,
    singleExpand: false,

    initComponent: function () {
        this.cellEditing = new Ext.grid.plugin.CellEditing({
            clicksToEdit: 1
        });
        Ext.apply(this, {
            plugins: [this.cellEditing],
            columns: [
                {
                    xtype: 'treecolumn', //this is so we know which column will show the tree
                    text: '资源编号',
                    width: 200,
                    sortable: false,
                    dataIndex: 'resid'
                },
                {
                    text: '资源名称',
                    width: 200,
                    dataIndex: 'name',
                    align: 'left'
                }, {
                    text: '资源地址',
                    width: 350,
                    dataIndex: 'resurl',
                    id: 'resurlautocolumnid',
                    align: 'left'
                }, {
                    text: '图标信息',
                    width: 150,
                    dataIndex: 'fa',
                    align: 'left'
                }, {
                    menuDisabled: true,
                    sortable: false,
                    text: '操作',
                    id: 'actioncolumnEditAndDel',
                    xtype: 'actioncolumn',
                    width: 100,
                    items: [
                        {
                            iconCls: 'Add',
                            tooltip: '新增子节点',
                            /**
                             * 修改操作
                             */
                            handler: function (grid, rowIndex, colIndex) {
                                var rec = grid.getStore().getAt(rowIndex);
                                this.fireEvent('addclick', {
                                    record: rec,
                                    grid: grid
                                });
                            }
                        }, {
                            iconCls: 'Edit',
                            tooltip: '修改此条记录',
                            /**
                             * 修改操作
                             */
                            handler: function (grid, rowIndex, colIndex) {
                                var rec = grid.getStore().getAt(rowIndex);
                                this.fireEvent('editclick', {
                                    record: rec,
                                    grid: grid
                                });
                            }
                        }, {
                            iconCls: 'Delete',
                            tooltip: '删除此条记录',
                            /**
                             * 删除操作
                             */
                            handler: function (grid, rowIndex, colIndex) {
                                var rec = grid.getStore().getAt(rowIndex);
                                this.fireEvent('deleteclick', {
                                    record: rec
                                });
                            }
                        }, {
                            iconCls: 'Sitemap',
                            tooltip: '管理拥有此菜单的用户',
                            /**
                             * 管理权限的用户操作
                             */
                            handler: function (grid, rowIndex, colIndex) {
                                var rec = grid.getStore().getAt(rowIndex);
                                this.fireEvent('userMgrclick', {
                                    record: rec
                                });
                            }
                        }
                    ]
                }


            ]
        });
        this.callParent();
    }
});
