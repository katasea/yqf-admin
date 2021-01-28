Ext.define("MyApp.store.DicComboInfo", {
    xtype:'dicComboInfoStore',
    extend: "Ext.data.Store",
    fields: ["dicval", "dickey"],
    autoLoad: true,
    proxy: {
        type: "ajax",
        actionMethods: { read: "POST" },
        url: contextPath + "/basDicInfo/getComboJson",
        reader: {
            type: "json",
            root: "root"
        }
    }
});
