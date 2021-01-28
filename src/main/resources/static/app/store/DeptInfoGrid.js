Ext.define("MyApp.store.DeptInfoGrid", {
    extend: "Ext.data.Store",
    pageSize:300,
    autoLoad:false,
    model: "MyApp.model.DeptInfo",
    proxy : {
         type: 'ajax',
         url: contextPath+'/deptInfo/getListJson',
         reader: {
             type: 'json',
             root: 'root',
             totalProperty:'total'
         }
    }
});
