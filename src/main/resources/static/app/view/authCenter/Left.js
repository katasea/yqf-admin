Ext.define('MyApp.view.authCenter.Left', {
    extend: 'Ext.grid.Panel',
    title:'待设定列表 [ 1.单击选中 ] ',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.ux.CheckColumn',
        'Ext.util.*',
        'Ext.toolbar.Paging'
    ],
    xtype: 'authCenterLeft',
    lines: true,
    split:true,
    border:true,
    collapsible:true,
    tbar:[
        {
            id: 'keywordLeft',
            name: 'keywordLeft',
            width: 110,
            labelWidth: 20,
            xtype: 'textfield',
            emptyText: '请输入关键字'
        }, '-', {
            id: 'querybtnLeft',
            text: '搜索',
            iconCls: 'Find',
            action: 'searchLeft'
        },'-',{xtype: 'button', text: '新增', action: 'add', disabled:true,iconCls: 'Add'}
    ],
    store: 'AuthCenterLeft',
    rowLines: true,
    viewConfig: {
        loadMask: {msg: "请稍后..."}
    },
    multiSelect: true,
    columnLines: true,
    enableColumnMove: true,
    enableColumnResize: true,            //是否允许改变列宽
    //autoScroll         : true,
    stripeRows: true,
    bbar: {
        xtype: 'pagingtoolbar',
        store: 'AuthCenterLeft',
        displayInfo: false
    },
    initComponent: function () {
        this.cellEditing = new Ext.grid.plugin.CellEditing({
            clicksToEdit: 1
        });
        Ext.apply(this, {
            plugins: [this.cellEditing],
            columns: [
                {
                    text: '对象',
                    width: 90,
                    dataIndex: 'MKEY',
                    align: 'left'
                },{
                    text: '对象描述',
                    width: 120,
                    dataIndex: 'MVALUE',
                    align: 'left'
                }, {
                    menuDisabled: true,
                    sortable: false,
                    text: '操作',
                    id: 'actioncolumnEditAndDel2',
                    xtype: 'actioncolumn',
                    width: 65,
                    items: [
                        {
                            iconCls: 'Edit',
                            tooltip: '修改此条记录',
                            /**
                             * 修改操作
                             */
                            handler: function(grid, rowIndex, colIndex) {
                                var rec = grid.getStore().getAt(rowIndex);
                                this.fireEvent('editclick', {
                                    record: rec,
                                    grid:grid
                                });
                            }
                        },
                        {
                            iconCls: 'Delete',
                            tooltip: '删除此条关联',
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
