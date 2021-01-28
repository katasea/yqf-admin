Ext.define('MyApp.view.midData.Main', {
    extend: 'Ext.grid.Panel',
    //title:'',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.ux.CheckColumn',
        'Ext.util.*',
        'Ext.toolbar.Paging'
    ],
    xtype: 'midDataMain',
    lines: true,
    tbar: [
        {xtype: 'button', text: '刷新', action: 'refresh2', iconCls: 'Refresh'}, '-',
        {
            id: 'keyword2',
            name: 'keyword2',
            width: 170,
            labelWidth: 70,
            xtype: 'textfield',
            emptyText: '请输入关键字'
        }, '-', {
            id: 'querybtn2',
            text: '搜索',
            iconCls: 'Find',
            action: 'search'
        }
    ],
    store: 'MidData',
    rowLines: true,
    viewConfig: {
        loadMask: {msg: "请稍后..."}
    },
    columnLines: true,
    rootVisible: false,
    multiSelect: true,
    enableColumnMove: true,
    enableColumnResize: true,            //是否允许改变列宽
    //autoScroll         : true,
    stripeRows: true,
    bbar: {
        xtype: 'pagingtoolbar',
        store: 'MidData',
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
                    text: '部门或个人',
                    width: 90,
                    dataIndex: 'deptorper',
                    align: 'center',
                    renderer: function (v) {
                        if (v == 1) {
                            return '<span style="color:green"><b>部门</b></span>';
                        } else {
                            return '<span style="color:red"><b>个人</b></span>';
                        }
                    }
                },
                {
                    text: '编号',
                    width: 105,
                    dataIndex: 'bh',
                    align: 'left'
                },
                {
                    text: '名称',
                    width: 120,
                    dataIndex: 'name',
                    align: 'left'
                },
                 {
                    text: '数据',
                    width: 125,
                    dataIndex: 'data',
                    align: 'right'
                }, {
                    text: '过程数据',
                    flex: 1,
                    dataIndex: 'process',
                    align: 'left'
                }
            ]
        });
        this.callParent();
    }
});
