Ext.define("MyApp.store.UserMgr", {
    extend: "Ext.data.Store",
    pageSize:11,
    autoLoad:false,
    model: "MyApp.model.UserInfo",
    proxy : {
         type: 'ajax',
         url: contextPath+'/userInfo/getUserMgrInfo',
         reader: {
             type: 'json',
             root: 'root',
             totalProperty:'total'
         }
    }
});
