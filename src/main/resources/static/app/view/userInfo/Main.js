Ext.define('MyApp.view.userInfo.Main', {
    extend: 'Ext.grid.Panel',
    title: '',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.ux.CheckColumn',
        'Ext.util.*',
        'Ext.toolbar.Paging'
    ],
    xtype: 'userInfoMain',
    lines: true,
    tbar: [
        {xtype: 'button', text: '刷新', action: 'refresh', iconCls: 'Refresh'}, '-',
        {xtype: 'button', text: '新增', action: 'add', iconCls: 'Add'}, '-',
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
            xtype: 'button', text:'重置密码', action: 'uptpwd', iconCls:'Lockkey'
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
    store: 'UserInfo',
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
    //autoScroll         : true,
    stripeRows: true,
    bbar: {
        xtype: 'pagingtoolbar',
        store: 'UserInfo',
        displayInfo: true
    },
    initComponent: function () {
        this.cellEditing = new Ext.grid.plugin.CellEditing({
            clicksToEdit: 1
        });
        Ext.apply(this, {
            plugins: [this.cellEditing],
            columns: [
                {
                    text: '用户账号',
                    width: 150,
                    locked: true,
                    sortable: false,
                    dataIndex: 'userid'
                }, {
                    text: '用户名称',
                    width: 150,
                    locked: true,
                    dataIndex: 'username',
                    align: 'left'
                }, {
                    text: '锁定状态',
                    width: 100,
                    locked: true,
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
                    menuDisabled: true,
                    sortable: false,
                    text: '操作',
                    id: 'actioncolumnEditAndDel',
                    xtype: 'actioncolumn',
                    width: 100,
                    items: [
                        {
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
                        },' ', {
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
                },
                {
                    text: '所属科室',
                    width: 150,
                    dataIndex: 'deptname',
                    align: 'left'
                },
                {
                    text: '联系电话',
                    width: 150,
                    dataIndex: 'phone',
                    align: 'left'
                }, {
                    text: '邮件地址',
                    width: 150,
                    dataIndex: 'email',
                    align: 'left'
                }, {
                    text: '生日日期',
                    width: 150,
                    dataIndex: 'birthday',
                    align: 'left'
                }, {
                    text: '身份证号',
                    width: 150,
                    dataIndex: 'idcard',
                    align: 'left'
                }, {
                    text: '人员性别',
                    width: 150,
                    dataIndex: 'sexText',
                    align: 'left'
                }
                // , {
                //     text: '人员岗位',
                //     width: 150,
                //     dataIndex: 'postText',
                //     align: 'left'
                // }
                , {
                    text: '人员类型',
                    width: 150,
                    dataIndex: 'userstyleText',
                    align: 'left'
                }, {
                    text: '人员状态',
                    width: 150,
                    dataIndex: 'userstatusText',
                    align: 'left'
                }, {
                    text: '入职时间',
                    width: 150,
                    dataIndex: 'entrytime',
                    align: 'left'
                }


            ]
        });
        this.callParent();
    }
});
