Ext.define("MyApp.store.BaseFormula", {
    extend: "Ext.data.Store",
    pageSize:20,
    autoLoad:true,
    model: "MyApp.model.BaseFormula",
    proxy : {
         type: 'ajax',
         url: contextPath+'/baseFormula/get',
         reader: {
             type: 'json',
             root: 'root',
             totalProperty:'total'
         }
    }
});
