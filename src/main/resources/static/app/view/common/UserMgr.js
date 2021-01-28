/**
 * 资源 角色 公用类。用于管理该资源或角色目前哪些用户拥有权限。
 */
Ext.define('MyApp.view.common.UserMgr', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.ux.CheckColumn',
        'Ext.util.*',
        'Ext.toolbar.Paging'
    ],
    xtype: 'userMgr',
    lines: true,
    tbar: [
        {
            xtype:'comboGridBox',
            multiSelect : false,
            displayField : 'username',
            valueField : 'userid',
            store: 'UserInfoNoPage',
            emptyText:'选择人员进行添加',
            pickerAlign: 'bl',
            gridCfg : {
                width: 450,
                height:300,
                columns: [
                    {
                        text: '单位编号',
                        flex: 1,
                        sortable: false,
                        dataIndex: 'companyid'
                    },
                    {
                        text: '用户账号',
                        flex: 1,
                        sortable: false,
                        dataIndex: 'userid'
                    },
                    {
                        text: '用户名称',
                        flex: 1,
                        dataIndex: 'username',
                        align: 'left'
                    }, {
                        text: '锁定状态',
                        flex: 1,
                        dataIndex: 'enable',
                        renderer: function (v) {
                            if (v == '1') {
                                return '<span style="color:green"><b>正常</b></span>';
                            } else {
                                return '<span style="color:red"><b>锁定</b></span>';
                            }
                        },
                        align: 'left'
                    }
                ]
            }
        }, {
            text: '新增',
            iconCls: 'Find',
            action: 'addUserAuth'
        }
        , '-',
        {
            id: 'keyword4User',
            name: 'keyword',
            width: 170,
            labelWidth: 70,
            xtype: 'textfield',
            emptyText: '请输入关键字'
        }, {
            text: '搜索',
            iconCls: 'Find',
            action: 'searchUser'
        }

    ],
    store: 'UserMgr',
    rowLines: true,
    viewConfig: {
        loadMask: {msg: "请稍后..."}
    },
    columnLines: true,
    rootVisible: false,
    multiSelect: true,
    columnLines: true,
    enableColumnMove: true,
    enableColumnResize: true,			//是否允许改变列宽
    // autoScroll         : true,
    stripeRows: true,
    bbar: {
        xtype: 'pagingtoolbar',
        store: 'UserMgr',
        displayInfo: true
    },
    initComponent: function () {
        // this.cellEditing = new Ext.grid.plugin.CellEditing({
        //     clicksToEdit: 1
        // });
        Ext.apply(this, {
            // plugins: [this.cellEditing],
            columns: [
                {
                    text: '单位编号',
                    flex: 1,
                    sortable: false,
                    dataIndex: 'companyid'
                },
                {
                    text: '用户账号',
                    flex: 1,
                    sortable: false,
                    dataIndex: 'userid'
                },
                {
                    text: '用户名称',
                    flex: 1,
                    dataIndex: 'username',
                    align: 'left'
                }, {
                    text: '锁定状态',
                    flex: 1,
                    dataIndex: 'enable',
                    renderer: function (v) {
                        if (v == '1') {
                            return '<span style="color:green"><b>正常</b></span>';
                        } else {
                            return '<span style="color:red"><b>锁定</b></span>';
                        }
                    },
                    align: 'left'
                }, {
                    text: '权限来源',
                    flex: 1,
                    dataIndex: 'resfrom',
                    renderer: function (v) {
                        if (v == '1') {
                            return '<span style="color:green"><b>角色关联</b></span>';
                        } else {
                            return '<span style="color:red"><b>用户关联</b></span>';
                        }
                    },
                    align: 'left'
                }, {
                    menuDisabled: true,
                    sortable: false,
                    flex: 1,
                    text: '操作',
                    id: 'actioncolumnEditAndDelOfWin',
                    xtype: 'actioncolumn',
                    items: [
                        {
                            iconCls: 'Delete',
                            tooltip: '删除此条记录',
                            /**
                             * 删除操作
                             */
                            handler: function (grid, rowIndex, colIndex) {
                                var rec = grid.getStore().getAt(rowIndex);
                                this.fireEvent('deleteUserMgrClick', {
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
