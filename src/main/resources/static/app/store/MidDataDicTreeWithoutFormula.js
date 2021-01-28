Ext.define("MyApp.store.MidDataDicTreeWithoutFormula", {
    extend: "Ext.data.TreeStore",
    autoLoad : true,
    root 	: {
        id : 'root',
        text:'辅助数据[录入导入]',
        expanded:true
    },
    model: "MyApp.model.TreeNode",
	proxy : {
		type : 'ajax',
		url:contextPath+'/midDataDic/nFormulaTree'
	}
});