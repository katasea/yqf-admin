Ext.define("MyApp.store.CompanyInfo", {
    extend: "Ext.data.Store",
    pageSize:20,
    autoLoad:true,
    model: "MyApp.model.CompanyInfo",
    proxy : {
         type: 'ajax',
         url: contextPath+'/companyInfo/get',
         reader: {
             type: 'json',
             root: 'root',
             totalProperty:'total'
         }
    }
});
