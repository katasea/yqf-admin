Ext.define("MyApp.store.MidData", {
    extend: "Ext.data.Store",
    pageSize:20,
    autoLoad:false,
    model: "MyApp.model.MidData",
    proxy : {
         type: 'ajax',
         url: contextPath+'/midData/get',
         reader: {
             type: 'json',
             idProperty:"pkid",
             root: 'root',
             totalProperty:'total'
         }
    }
});
