Ext.define("MyApp.store.MidDataDicParentCombo", {
    extend: "Ext.data.Store",
    fields: ["dicval", "dickey"],
    autoLoad: true,
    proxy: {
        type: "ajax",
        actionMethods: {read: "POST"},
        extraParams: {deptOrper: deptOrper},
        url: contextPath + "/midDataDicParent/list",
        reader: {
            type: "json",
            root: "root"
        }
    }
});
