Ext.define('MyApp.view.basDicInfo.Main', {
    extend: 'Ext.tree.Panel',
    title: '',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.tree.*',
        'Ext.ux.CheckColumn'
    ],
    xtype: 'basDicInfoMain',
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
    store: 'BasDicInfo',
    rowLines: true,
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
                    text: '字典编号',
                    width: 200,
                    sortable: false,
                    dataIndex: 'dicid'
                },
                {
                    text: '字典名称',
                    width: 170,
                    dataIndex: 'dicname',
                    align: 'left'
                }, {
                    text: '业务系统键',
                    width: 150,
                    dataIndex: 'dickey',
                    align: 'left'
                }, {
                    text: '业务系统值',
                    width: 150,
                    dataIndex: 'dicval',
                    align: 'left'
                }, {
                    text: '字典状态',
                    width: 150,
                    dataIndex: 'isstop',
                    renderer: function (v) {
                        if (v == '1') {
                            return '已停用';
                        } else {
                            return '启用中';
                        }
                    },
                    align: 'right'
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
                        }
                    ]
                }


            ]
        });
        this.callParent();
    }
});
