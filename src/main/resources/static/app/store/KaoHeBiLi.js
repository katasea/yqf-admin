Ext.define("MyApp.store.KaoHeBiLi", {
    extend: "Ext.data.Store",
    pageSize:20,
    autoLoad:false,
    model: "MyApp.model.KaoHeBiLi",
    proxy : {
         type: 'ajax',
         url: contextPath+'/kaoHeBiLi/get',
         reader: {
             type: 'json',
             root: 'root',
             totalProperty:'total'
         }
    }
});
