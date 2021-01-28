Ext.define("MyApp.store.ItemType", {
    extend: "Ext.data.TreeStore",
    autoLoad:true,
    model: "MyApp.model.ItemType",
    proxy : {
         type: 'ajax',
         url: contextPath+'/itemType/get',
         reader: {
             type: 'json',
             root: 'root',
             totalProperty:'total'
         }
    }
});
