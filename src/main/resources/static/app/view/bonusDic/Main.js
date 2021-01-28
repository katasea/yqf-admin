Ext.define('MyApp.view.bonusDic.Main', {
    extend: 'Ext.tree.Panel',
    //title:'',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.tree.*',
        'Ext.ux.CheckColumn'
    ],    
    xtype: 'bonusDicMain',
    useArrows: true,
	//iconCls:'Tree',
    lines:true,
	collapseFirst:false,
	tbar: [
	  { xtype: 'button', text: '刷新',action:'refresh',iconCls:'Refresh' },'-',
	  { xtype: 'button', text: '新增',action:'add',iconCls: 'Add' },'-',
	  { xtype: 'button', text: '初始内置',action:'autoInitFromAccess',iconCls: 'Cogwheel' },'-',
	  { xtype: 'button', text: '拷贝上年',action:'copyLastYearData',iconCls: 'Submit' },'-',
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
    store: 'BonusDic',
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
                text: '字典编号',
                flex: 2,
                sortable: false,
                dataIndex: 'bh'
            },
            {
                text: '单位名称',
                width:150,
                                dataIndex: 'companyid',
                align:'left'
            },            {
                text: '年份',
                width:150,
                                dataIndex: 'year',
                align:'left'
            },            {
                text: '月份',
                width:150,
                                dataIndex: 'month',
                align:'left'
            },            {
                text: '字典名称',
                width:150,
                                dataIndex: 'name',
                align:'left'
            },            {
                text: '级别',
                width:150,
                                dataIndex: 'grade',
                align:'right'
            },            {
                text: '公式',
                width:150,
                                dataIndex: 'formula',
                align:'left'
            },            {
                text: '状态',
                flex: 1,
                dataIndex: 'isstop',
                renderer : function(v){if(v=='1'){return '已停用';}else {return '启用中';}},
                align:'right'
            },{
                menuDisabled: true,
                sortable: false,
                text    : '操作',
                id : 'actioncolumnEditAndDel',
                xtype: 'actioncolumn',
                width: 100,
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
