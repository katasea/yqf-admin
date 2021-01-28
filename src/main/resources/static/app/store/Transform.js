Ext.define("MyApp.store.Transform", {
    extend: "Ext.data.Store",
    pageSize:20,
    autoLoad:true,
    model: "MyApp.model.Transform",
    proxy : {
         type: 'ajax',
         url: contextPath+'/transform/get',
         reader: {
             type: 'json',
             root: 'root',
             totalProperty:'total'
         }
    }
});
