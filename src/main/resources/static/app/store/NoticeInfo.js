Ext.define("MyApp.store.NoticeInfo", {
    extend: "Ext.data.Store",
    pageSize:20,
    autoLoad:true,
    model: "MyApp.model.NoticeInfo",
    proxy : {
         type: 'ajax',
         url: contextPath+'/noticeInfo/get',
         reader: {
             type: 'json',
             root: 'root',
             totalProperty:'total'
         }
    }
});
