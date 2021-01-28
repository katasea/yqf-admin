Ext.define('MyApp.view.calculatePage.Main', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.ux.CheckColumn',
        'Ext.util.*',
        'Ext.toolbar.Paging'
    ],    
    xtype: 'calculatePage',
    lines:true,
	tbar: [
	  { xtype: 'button', text: '计算',action:'calculate',iconCls: 'Calculator' }
	],
    store: 'CalculatePage',
    rowLines:true,
    columnLines:true,
    rootVisible: false,
    multiSelect: true,
    enableColumnMove   : true,
    enableColumnResize : true,            //是否允许改变列宽
    stripeRows         : true,
    initComponent: function() {
        Ext.apply(this, {
            columns: [
                {
                    text: '步骤',
                    width: 80,
                    dataIndex: 'id'
                },{
                    text: '详情',
                    width: 500,
                    dataIndex: 'name'
                }
            ]
        })
        this.callParent();
    }
});
