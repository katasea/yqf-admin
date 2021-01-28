Ext.define("MyApp.store.NormTree", {
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
		url:contextPath+'/norm/normTree?deptOrPerson='+deptOrPerson
	}
});