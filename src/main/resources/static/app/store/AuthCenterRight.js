Ext.define("MyApp.store.AuthCenterRight", {
    extend: "Ext.data.Store",
    pageSize:13,
    autoLoad:false,
    fields: ["MKEY", "MVALUE"],
    proxy : {
         type: 'ajax',
         url: contextPath+'/acc/getMtarg',
         reader: {
             type: 'json',
             root: 'root',
             totalProperty:'total'
         }
    }
});
