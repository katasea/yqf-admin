Ext.define('MyApp.view.authCenter.Main', {
    extend: 'Ext.panel.Panel',
    xtype: 'authCenterMainFrame',
    tbar: [

        // '人员信息：',
        // {
        //     xtype: "combobox",
        //     name: "userid",
        //     id: "userid",
        //     // fieldLabel: '归属类别',
        //     store: {
        //         extend: "Ext.data.JsonStore",
        //         autoDestroy: true,
        //         fields: ["dicval", "dickey"],
        //         autoLoad: true,
        //         proxy: {
        //             type: "ajax",
        //             actionMethods: {read: "POST"},
        //             extraParams: {},
        //             url: contextPath + "/userInfo/list",
        //             reader: {
        //                 type: "json",
        //                 root: "root"
        //             }
        //         }
        //     },
        //     editable: true,
        //     displayField: "dicval",
        //     valueField: "dickey",
        //     allowBlank: false,
        //     emptyText: "--请选择--",
        //     minChars:1,
        //     typeAhead:false,
        //     allowBlank:false,
        //     queryCaching :false,
        //     queryDelay :10,
        //     triggerAction :'all',
        //     selectOnFocus :true,
        //     queryMode:'remote',
        //     queryParam:'keyword'
        // },
        '权限类别：',
        {
            xtype: "combobox",
            name: "authType",
            id: "authType",
            // fieldLabel: '归属类别',
            store: {
                extend: "Ext.data.Store",
                fields: ["dicval", "dickey"],
                autoLoad: true,
                proxy: {
                    type: "ajax",
                    actionMethods: {read: "POST"},
                    extraParams: {},
                    url: contextPath + "/acc/type/list",
                    reader: {
                        type: "json",
                        root: "root"
                    }
                }
            },
            editable: false,
            value:'1-0',
            displayField: "dicval",
            valueField: "dickey",
            allowBlank: false,
            emptyText: "--请选择--",
            queryMode: "local"
        },'->', '提示：<span style="color:red;font-weight:bold;">超级管理员角色</span>拥有所有权限无需设置'
    ],
    items:[
        {region:'west',width:280,xtype:'authCenterLeft'},
        {region:'center',xtype:'authCenterMain'},
        {region:'east',width:280,xtype:'authCenterRight'}],
    layout : "border",
    frame:false,
    border:false,
    //自定义方块背景
    bodyCls: "bgSquare",
    initComponent: function () {
        this.callParent();
    }
});
