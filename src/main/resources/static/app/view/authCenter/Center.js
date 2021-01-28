Ext.define('MyApp.view.authCenter.Center', {
    extend: 'Ext.grid.Panel',
    title:'关联关系信息 [ 3.双击取消关联 ] ',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.ux.CheckColumn',
        'Ext.util.*',
        'Ext.toolbar.Paging'
    ],
    xtype: 'authCenterMain',
    split:true,
    border:true,
    collapsible:true,
    lines: true,
    tbar: [
        {
            id: 'keywordCenter',
            name: 'keywordCenter',
            width: 110,
            labelWidth: 20,
            xtype: 'textfield',
            emptyText: '请输入关键字'
        }, '-', {
            id: 'querybtnCenter',
            text: '搜索',
            iconCls: 'Find',
            action: 'searchCenter'
        }, '-',{xtype: 'button', text: '清空', action: 'delete', iconCls: 'Delete'}
        // ,'->','<span style="color:green;font-weight:bold;">3.双击取消选择</span>'
    ],
    store: 'AuthCenter',
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
        store: 'AuthCenter',
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
                    text: '权限类别',
                    width: 130,
                    sortable: false,
                    dataIndex: 'funcode'
                },
                {
                    text: '对象',
                    width: 130,
                    dataIndex: 'morig',
                    align: 'left'
                },{
                    text: '对象描述',
                    width: 130,
                    dataIndex: 'morigname',
                    align: 'left'
                },  {
                    text: '关联对象',
                    width: 130,
                    dataIndex: 'mtarg',
                    align: 'left'
                }, {
                    text: '关联对象描述',
                    width: 150,
                    dataIndex: 'mtargname',
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
