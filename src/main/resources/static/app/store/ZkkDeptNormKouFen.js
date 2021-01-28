Ext.define("MyApp.store.ZkkDeptNormKouFen", {
    extend: "Ext.data.Store",
    pageSize:20,
    autoLoad:false,
    model: "MyApp.model.DeptNormKouFen",
    proxy : {
         type: 'ajax',
         url: contextPath+'/deptNormKouFen/get',
         reader: {
             type: 'json',
             root: 'root',
             totalProperty:'total'
         }
    }
});
