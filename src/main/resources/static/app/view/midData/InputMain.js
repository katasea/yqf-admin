Ext.define('MyApp.view.midData.InputMain', {
    extend: 'Ext.panel.Panel',
    xtype: 'midDataMainPanel',
    id:'dataInputMain',
    tbar: [
        '辅助分类：',
        {
            xtype: "combobox",
            name: "authType",
            id: "authType",
            // fieldLabel: '归属类别',
            store: 'MidDataDicParentCombo',
            editable: false,
            displayField: "dicval",
            valueField: "dickey",
            allowBlank: false,
            emptyText: "--请选择--",
            queryMode: "local"
        },{
            id : 'keyword',
            name:'keyword',
            width : 170,
            labelWidth : 70,
            xtype : 'textfield',
            emptyText: '请输入关键字'
        },'-', {
            id:'querybtn',
            text: '搜索',
            iconCls:'Find',
            action:'search'
        },'->', '提示：<span style="color:red;font-weight:bold;">选择类别进行相关数据维护</span>'
    ],
    items:[
        {
            xtype:'panel',
            id:'dataInputDynamic'
        }
    ],
    layout: "fit",
    frame:false,
    border:false,
    //自定义方块背景
    bodyCls: "bgSquare",
    initComponent: function () {
        this.callParent();
    }
});
