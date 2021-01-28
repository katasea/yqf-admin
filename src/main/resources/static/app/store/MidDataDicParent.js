Ext.define("MyApp.store.MidDataDicParent", {
    extend: "Ext.data.Store",
    pageSize:20,
    autoLoad:true,
    model: "MyApp.model.MidDataDicParent",
    proxy : {
         type: 'ajax',
         url: contextPath+'/midDataDicParent/get',
         reader: {
             type: 'json',
             root: 'root',
             totalProperty:'total'
         }
    }
});
