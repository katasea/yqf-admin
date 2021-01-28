Ext.define("MyApp.store.AuthCenterLeft", {
    extend: "Ext.data.Store",
    pageSize:20,
    autoLoad:true,
    fields: ["MKEY", "MVALUE"],
    proxy : {
         type: 'ajax',
         url: contextPath+'/acc/getMorig',
         reader: {
             type: 'json',
             root: 'root',
             totalProperty:'total'
         }
    }
});
