Ext.define("MyApp.store.AuthCenter", {
    extend: "Ext.data.Store",
    pageSize:20,
    autoLoad:false,
    model: "MyApp.model.AuthCenter",
    proxy : {
         type: 'ajax',
         url: contextPath+'/acc/getAuthList',
         reader: {
             type: 'json',
             idProperty:'pkid',
             root: 'root',
             totalProperty:'total'
         }
    }
});
