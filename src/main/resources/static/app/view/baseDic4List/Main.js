Ext.define('MyApp.view.baseDic4List.Main', {
    extend: 'Ext.grid.Panel',
    //title:'',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.ux.CheckColumn',
        'Ext.util.*',
        'Ext.toolbar.Paging'
    ],    
    xtype: 'baseDic4ListMain',
    lines:true,
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
    store: 'BaseDic4List',
    rowLines:true,
    viewConfig:{
	    loadMask:{msg:"请稍后..."}
	},
    rootVisible: false,
    multiSelect: true,
    columnLines        : true,
    enableColumnMove   : true,
    enableColumnResize : true,            //是否允许改变列宽
    //autoScroll         : true,
    stripeRows         : true,
    bbar: {
          xtype: 'pagingtoolbar',
          store: 'BaseDic4List',
          displayInfo: true
    },
    initComponent: function() {
    	this.cellEditing = new Ext.grid.plugin.CellEditing({
            clicksToEdit: 1
        });
        Ext.apply(this, {
        	plugins: [this.cellEditing],
            columns: [
            {
                text: '对象ID',
                width: 150,
                sortable: false,
                dataIndex: 'mkey'
            },
            {
                text: '公司编号',
    flex: 1,
                dataIndex: 'companyid',
                align:'left'
            },            {
                text: '类型',
    width:150,
                    dataIndex: 'type',
                align:'right'
            },            {
                text: '对象名称',
    width:150,
                    dataIndex: 'mvalue',
                align:'left'
            },            {
                text: '备用信息',
    width:150,
                    dataIndex: 'reverse',
                align:'left'
            },{
                menuDisabled: true,
                sortable: false,
                text    : '操作',
                id : 'actioncolumnEditAndDel',
                xtype: 'actioncolumn',
                width: 100,
                items: [
                	{
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
