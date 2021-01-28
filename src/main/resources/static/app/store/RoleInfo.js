Ext.define("MyApp.store.RoleInfo", {
    extend: "Ext.data.Store",
    pageSize:20,
    autoLoad:true,
    model: "MyApp.model.RoleInfo",
    proxy : {
         type: 'ajax',
         url: contextPath+'/roleInfo/get',
         reader: {
             type: 'json',
             root: 'root',
             totalProperty:'total'
         }
    }
});
