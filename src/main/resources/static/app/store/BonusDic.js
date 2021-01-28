Ext.define("MyApp.store.BonusDic", {
    extend: "Ext.data.TreeStore",
    autoLoad:true,
    model: "MyApp.model.BonusDic",
    proxy : {
         type: 'ajax',
         url: contextPath+'/bonusDic/get',
         reader: {
             type: 'json',
             root: 'root',
             totalProperty:'total'
         }
    }
});
