Ext.define("MyApp.store.Norm", {
    extend: "Ext.data.TreeStore",
    autoLoad:true,
    model: "MyApp.model.Norm",
    proxy : {
         type: 'ajax',
         url: contextPath+'/norm/get?deptOrPerson='+deptOrPerson,
         reader: {
             type: 'json',
             root: 'root',
             totalProperty:'total'
         }
    }
});
