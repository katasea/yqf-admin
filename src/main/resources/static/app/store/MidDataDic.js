Ext.define("MyApp.store.MidDataDic", {
    extend: "Ext.data.Store",
    pageSize:20,
    autoLoad:true,
    model: "MyApp.model.MidDataDic",
    proxy : {
         type: 'ajax',
         url: contextPath+'/midDataDic/get',
         reader: {
             type: 'json',
             root: 'root',
             totalProperty:'total'
         }
    }
});
