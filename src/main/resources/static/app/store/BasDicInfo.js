Ext.define("MyApp.store.BasDicInfo", {
    extend: "Ext.data.TreeStore",
    autoLoad:true,
    model: "MyApp.model.BasDicInfo",
    proxy : {
         type: 'ajax',
         url: contextPath+'/basDicInfo/get',
         reader: {
             type: 'json',
             root: 'root',
             totalProperty:'total'
         }
    }
});
