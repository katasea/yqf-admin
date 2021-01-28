Ext.define("MyApp.store.MidDataFormulaDic", {
    extend: "Ext.data.Store",
    pageSize:20,
    autoLoad:false,
    model: "MyApp.model.MidDataFormulaDic",
    proxy : {
         type: 'ajax',
         url: contextPath+'/midDataFormulaDic/get',
         reader: {
             type: 'json',
             idProperty:"pkid",
             root: 'root',
             totalProperty:'total'
         }
    }
});
