Ext.define("MyApp.store.DeptTree", {
    extend: "Ext.data.TreeStore",
    autoLoad : true,
    root 	: {
        id : 'root',
        text:'辅助数据[自动计算]',
        expanded:true
    },
    model: "MyApp.model.TreeNode",
	proxy : {
		type : 'ajax',
		url:contextPath+'/deptInfo/deptTree'
	}
});