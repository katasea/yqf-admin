Ext.define("MyApp.store.ResourcesInfo", {
    extend: "Ext.data.TreeStore",
    autoLoad:true,
    model: "MyApp.model.ResourcesInfo",
    proxy : {
         type: 'ajax',
         url: contextPath+'/resourcesInfo/get',
         reader: {
             type: 'json',
             root: 'root',
             totalProperty:'total'
         }
    }
});
