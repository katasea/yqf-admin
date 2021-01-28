Ext.define('MyApp.view.deptInfo.Main', {
    extend: 'Ext.tree.Panel',
    title: '',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.tree.*',
        'Ext.ux.CheckColumn'
    ],
    xtype: 'deptInfoMain',
    useArrows: true,
    //iconCls:'Tree',
    lines: true,
    collapseFirst: false,
    tbar: [
        {xtype: 'button', text: '刷新', action: 'refresh', iconCls: 'Refresh'}, '-',
        {xtype: 'button', text: '新增', action: 'add', iconCls: 'Add'}, '-',
        {xtype: 'button', text: '初始内置', action: 'autoInitFromAccess', iconCls: 'Cogwheel'}, '-',
        {
            xtype: 'button',
            text: "Excel",
            iconCls: 'Pageexcel',
            menu:{
                xtype:'menu',
                margin: '0 0 10 0',
                plain: true,
                frame: true,
                items: [
                    { xtype:'menuitem', text: '模板下载', action: 'downTemplate', iconCls: 'Pagewhiteexcel'},'-',
                    { xtype:'menuitem', text: '导入数据', action: 'importData', iconCls: 'Pagewhiteget'},'-',
                    { xtype:'menuitem', text: '导出数据', action: 'exportData', iconCls: 'Pagewhiteput'}
                ]
            }
        }
        , '-',
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
    store: 'DeptInfo',
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
                    text: '科室编号',
                    width: 200,
                    sortable: false,
                    dataIndex: 'deptid'
                },
                {
                    text: '科室名称',
                    width: 150,
                    dataIndex: 'deptname',
                    align: 'left'
                }, {
                    text: '科室类型',
                    width: 120,
                    dataIndex: 'depttypeText',
                    align: 'left'
                }, {
                    text: '科室状态',
                    width: 90,
                    dataIndex: 'isstop',
                    renderer: function (v) {
                        if (v == '1') {
                            return '已停用';
                        } else {
                            return '启用中';
                        }
                    },
                    align: 'center'
                },{
                    text: '单位编号',
                    width: 90,
                    dataIndex: 'companyid',
                    align: 'center'
                },
                {
                    text: '单位名称',
                    width: 90,
                    dataIndex: 'companyname',
                    align: 'left'
                }, {
                    text: '停用时间',
                    width: 90,
                    dataIndex: 'stoptime',
                    align: 'center'
                },{
                    text: '启用时间',
                    width: 90,
                    dataIndex: 'inserttime',
                    align: 'center',
                    renderer: function (v) {
                        if (v == '') {
                            return '通用';
                        } else {
                            return v;
                        }
                    }
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
