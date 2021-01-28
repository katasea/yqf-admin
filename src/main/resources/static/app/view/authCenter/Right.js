Ext.define('MyApp.view.authCenter.Right', {
    extend: 'Ext.grid.Panel',
    title:'待选列表 [ 2.双击关联 ] ',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.ux.CheckColumn',
        'Ext.util.*',
        'Ext.toolbar.Paging'
    ],
    tbar: [
        {
            id: 'keywordRight',
            name: 'keywordRight',
            width: 110,
            labelWidth: 20,
            xtype: 'textfield',
            emptyText: '请输入关键字'
        }, '-', {
            id: 'querybtnRight',
            text: '搜索',
            iconCls: 'Find',
            action: 'searchRight'
        }
        // ,'-','<span style="color:green;font-weight:bold;">2.双击选取</span>'
    ],
    xtype: 'authCenterRight',
    lines: true,
    store: 'AuthCenterRight',
    split:true,
    border:true,
    collapsible:true,
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
        store: 'AuthCenterRight',
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
                    width: 100,
                    dataIndex: 'MKEY',
                    align: 'left'
                },{
                    text: '对象描述',
                    width: 140,
                    dataIndex: 'MVALUE',
                    align: 'left'
                }
            ]
        });
        this.callParent();
    }
});
