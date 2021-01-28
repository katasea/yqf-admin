var transform1 = Ext.define('MyApp.view.transform.Main', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.ux.CheckColumn',
        'Ext.util.*',
        'Ext.toolbar.Paging'
    ],
    region: 'center',
    xtype: 'transformMain',
    title: "转换详情",
    lines: true,
    store: 'Transform',
    width: 200,
    rowLines: true,
    viewConfig: {
        loadMask: {msg: "请稍后..."}
    },
    tbar: [
        {
            xtype: 'button',
            text: '增加',
            action: 'add',
            iconCls: 'Add'
        },{
            xtype: 'button',
            text: '刷新',
            action: 'refresh',
            iconCls: 'Refresh'
        }
    ],
    bbar: {
        xtype: 'pagingtoolbar',
        store: 'Transform',
        displayInfo: true
    },
    rootVisible: false,
    multiSelect: true,
    columnLines: true,
    enableColumnMove: true,
    enableColumnResize: true,            //是否允许改变列宽
    stripeRows: true,
    initComponent: function () {
        Ext.apply(this, {
            columns: [
                {
                    text: column_cd_id,
                    width: 140,
                    dataIndex: 'condition_id'
                },
                {
                    text: column_cd_name,
                    width: 140,
                    dataIndex: 'condition_name'
                },{
                    text: "条件值",
                    hidden:hideColumnValue,
                    width: 140,
                    dataIndex: 'condition_value'
                },  {
                    text: column_aj_id,
                    width: 140,
                    dataIndex: 'adjustment_id'
                },
                {
                    text: column_aj_name,
                    width: 140,
                    dataIndex: 'adjustment_name'
                },
                {
                    text: "调整值",
                    hidden: hideColumnValue,
                    width: 140,
                    dataIndex: 'adjustment_value'
                },{
                    text: "护士比例",
                    hidden:hiddenNurse,
                    width: 140,
                    dataIndex: 'transform_percent'
                },{
                    menuDisabled: true,
                    sortable: false,
                    text: '操作',
                    id: 'actioncolumnEditAndDel',
                    xtype: 'actioncolumn',
                    width: 100,
                    items: [
                        {
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