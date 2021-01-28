Ext.define('MyApp.view.transform.CenterPanel', {
    extend: 'Ext.grid.Panel',
    region: 'center',
    width: '400',
    title: "转换详情",
    xtype: 'CenterPanel',
    initComponent: function () {
        Ext.apply(this, {
            columns: []
        });
        this.callParent();
    }
})