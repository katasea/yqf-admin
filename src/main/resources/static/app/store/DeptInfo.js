Ext.define("MyApp.store.DeptInfo", {
    extend: "Ext.data.TreeStore",
    autoLoad:true,
    model: "MyApp.model.DeptInfo",
    proxy : {
         type: 'ajax',
         url: contextPath+'/deptInfo/get',
         reader: {
             type: 'json',
             root: 'root',
             totalProperty:'total'
         }
    }
});
