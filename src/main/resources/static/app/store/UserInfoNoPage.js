Ext.define("MyApp.store.UserInfoNoPage", {
    extend: "Ext.data.Store",
    pageSize:300,
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
