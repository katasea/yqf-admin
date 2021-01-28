Ext.define('MyApp.view.itemType.Main', {
    extend: 'Ext.tree.Panel',
    //title:'收支项目字典',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.tree.*',
        'Ext.ux.CheckColumn'
    ],    
    xtype: 'itemTypeMain',
    useArrows: true,
	//iconCls:'Tree',
    lines:true,
	collapseFirst:false,
	tbar: [
	  { xtype: 'button', text: '刷新',action:'refresh',iconCls:'Refresh' },'-',
	  { xtype: 'button', text: '新增',action:'add',iconCls: 'Add' },'-',
	  { xtype: 'button', text: '初始内置',action:'autoInitFromAccess',iconCls: 'Cogwheel' },'-',
	  {
			id : 'keyword',
			name:'keyword',
			width : 170,
			labelWidth : 70,
			xtype : 'textfield',
			emptyText: '请输入关键字'
	  },'-', {
			id:'querybtn',
            text: '搜索',
            iconCls:'Find',
            action:'search'
      },'-',
      '->','将鼠标悬浮在操作图标上可获得对应提示'
	],
    store: 'ItemType',
    rowLines:true,
    viewConfig:{
	    loadMask:{msg:"请稍后..."}
	},
    columnLines:true,
    rootVisible: false,
    multiSelect: true,
    singleExpand: false,
    
    initComponent: function() {
    	this.cellEditing = new Ext.grid.plugin.CellEditing({
            clicksToEdit: 1
        });
        Ext.apply(this, {
        	plugins: [this.cellEditing],
            columns: [
            {
                xtype: 'treecolumn', //this is so we know which column will show the tree
                header: '分类编码',
                width : 150,
            	sortable : true,
                dataIndex: 'code'
            },{
            	header: '分类名称',
                width : 150,
            	sortable : true,
                dataIndex: 'text'
            },{
            	header: '收支类型',
                dataIndex: 'itemtype',
                renderer : function(v){if(v=='1'){return '收入类型';}else {return '支出类型';}},
                width : 150,
            	sortable : true,
            }, {
            	header: '是否停用',
                dataIndex: 'isstop',
                renderer : function(v){if(v=='1'){return '是';}else{return '否';}},
                width : 150,
            	sortable : true,
            },{
                menuDisabled: true,
                sortable: false,
                header    : '操作',
                id : 'actioncolumnEditAndDel',
                xtype: 'actioncolumn',
                width: 120,
                items: [
                	{
	                    iconCls: 'Add',
	                    tooltip: '新增子节点',
	                    /**
	                     * 修改操作
	                     */
	                    handler: function(grid, rowIndex, colIndex) {
	                        var rec = grid.getStore().getAt(rowIndex);  
	                        this.fireEvent('addclick', {  
	                           record: rec,
	                           grid:grid
	                        });  
                    	}
                  	},{
	                    iconCls: 'Edit',
	                    tooltip: '修改此条记录',
	                    /**
	                     * 修改操作
	                     */
	                    handler: function(grid, rowIndex, colIndex) {
	                        var rec = grid.getStore().getAt(rowIndex);  
	                        this.fireEvent('editclick', {  
	                           record: rec,
	                           grid:grid
	                        });  
                    	}
                  	},{
	                    iconCls: 'Delete',
	                    tooltip: '删除此条记录',
	                    /**
	                     * 删除操作
	                     */
	                    handler: function(grid, rowIndex, colIndex) {
	                        var rec = grid.getStore().getAt(rowIndex);  
	                        this.fireEvent('deleteclick', {  
	                           record: rec  
	                        });  
                    	}
                  	}
                ]
	        }
            
            
            ]
        });
        this.callParent();
    }
});
