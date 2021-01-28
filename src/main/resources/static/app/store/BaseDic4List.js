Ext.define("MyApp.store.BaseDic4List", {
    extend: "Ext.data.Store",
    pageSize:20,
    autoLoad:true,
    model: "MyApp.model.BaseDic4List",
    proxy : {
         type: 'ajax',
         url: contextPath+'/baseDic4List/get',
         reader: {
             type: 'json',
             root: 'root',
             totalProperty:'total'
         }
    }
});
