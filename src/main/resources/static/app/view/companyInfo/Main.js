Ext.define('MyApp.view.companyInfo.Main', {
    extend: 'Ext.grid.Panel',
    title: '',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.ux.CheckColumn',
        'Ext.util.*',
        'Ext.toolbar.Paging'
    ],
    xtype: 'companyInfoMain',
    lines: true,
    tbar: [
        {xtype: 'button', text: '刷新', action: 'refresh', iconCls: 'Refresh'}, '-',
        {xtype: 'button', text: '新增', action: 'add', iconCls: 'Add'}, '-',
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
    store: 'CompanyInfo',
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
        store: 'CompanyInfo',
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
                    text: '单位编号',
                    width: 150,
                    sortable: false,
                    dataIndex: 'companyid'
                },
                {
                    text: '单位名称',
                    width: 150,
                    dataIndex: 'companyname',
                    align: 'left'
                }, {
                    text: '单位状态',
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
