Ext.define('MyApp.view.baseFormula.Main', {
    extend: 'Ext.grid.Panel',
    //title:'基本公式',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.ux.CheckColumn',
        'Ext.util.*',
        'Ext.toolbar.Paging'
    ],
    xtype: 'baseFormulaMain',
    lines: true,
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
    store: 'BaseFormula',
    rowLines: true,
    viewConfig: {
        loadMask: {msg: "请稍后..."}
    },
    columnLines: true,
    rootVisible: false,
    multiSelect: true,
    columnLines: true,
    enableColumnMove: true,
    enableColumnResize: true,            //是否允许改变列宽
    //autoScroll         : true,
    stripeRows: true,
    bbar: {
        xtype: 'pagingtoolbar',
        store: 'BaseFormula',
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
                    text: '占位名称',
                    width: 180,
                    sortable: false,
                    dataIndex: 'name'
                },
                {
                    text: '单位编号',
                    width: 90,
                    dataIndex: 'companyid',
                    align: 'left'
                }, {
                    text: '公式内容',
                    flex: 1,
                    dataIndex: 'formula',
                    align: 'left'
                }, {
                    text: '状态',
                    width: 90,
                    dataIndex: 'isstop',
                    renderer: function (v) {
                        if (v == '0') {
                            return '<span style="color:green"><b>启用中</b></span>';
                        } else {
                            return '<span style="color:red"><b>已停用</b></span>';
                        }
                    },
                    align: 'left'
                }, {
                    text: '类型',
                    width: 90,
                    dataIndex: 'type',
                    renderer: function (v) {
                        if (v == 'PQL') {
                            return '<span style="color:green"><b>PQL语法</b></span>';
                        } else {
                            return '<span style="color:red"><b>SQL语法</b></span>';
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
