Ext.define('MyApp.view.transform.Left', {
    extend: 'Ext.tree.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.ux.CheckColumn',
        'Ext.util.*',
        'Ext.toolbar.Paging'
    ],
    title: "转换对象",
    width: 200,
    region: 'west',
    xtype: 'transformType',
    rootVisible: false,
    store: 'TransformType',
    tbar: [
        {
            xtype: 'button',
            text: '执行转换',
            action: 'transform',
            iconCls: 'Calculator'
        }
    ],
    rowLines: true,
    enableColumnResize: true,            //是否允许改变列宽
    stripeRows: true,
    initComponent: function () {
        this.callParent();
    }
});
