Ext.define("MyApp.store.NormDeptKouFen", {
    extend: "Ext.data.Store",
    pageSize:20,
    autoLoad:false,
    model: "MyApp.model.NormDeptKouFen",
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
