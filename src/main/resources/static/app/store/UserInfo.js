Ext.define("MyApp.store.UserInfo", {
    extend: "Ext.data.Store",
    pageSize:20,
    autoLoad:true,
    model: "MyApp.model.UserInfo",
    proxy : {
         type: 'ajax',
         url: contextPath+'/userInfo/get',
         reader: {
             type: 'json',
             root: 'root',
             totalProperty:'total'
         }
    }
});
