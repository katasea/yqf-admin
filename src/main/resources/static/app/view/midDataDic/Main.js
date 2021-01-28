Ext.define('MyApp.view.midDataDic.Main', {
    extend: 'Ext.grid.Panel',
    //title:'',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.ux.CheckColumn',
        'Ext.util.*',
        'Ext.toolbar.Paging'
    ],    
    xtype: 'midDataDicMain',
    lines:true,
	tbar: [
	  { xtype: 'button', text: '刷新',action:'refresh',iconCls:'Refresh' },'-',
	  { xtype: 'button', text: '新增',action:'add',iconCls: 'Add' },'-',
      { xtype: 'button', text: '初始内置', action: 'autoInitFromAccess', iconCls: 'Cogwheel'}, '-',
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
    store: 'MidDataDic',
    rowLines:true,
    viewConfig:{
	    loadMask:{msg:"请稍后..."}
	},
    columnLines:true,
    rootVisible: false,
    multiSelect: true,
    enableColumnMove   : true,
    enableColumnResize : true,            //是否允许改变列宽
    //autoScroll         : true,
    stripeRows         : true,
    bbar: {
          xtype: 'pagingtoolbar',
          store: 'MidDataDic',
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
                text: '编号',
                width: 150,
                sortable: false,
                dataIndex: 'id'
            },
            {
                text: '单位编号',
                width:150,
                dataIndex: 'companyname',
                align:'left'
            },            {
                text: '名称',
                width:150,
                dataIndex: 'name',
                align:'left'
            },            {
                text: '类型',
                flex: 1,
                dataIndex: 'type',
                renderer : function(v){if(v=='1'){return '数据录入';}else {return '自动计算';}},
                align:'right'
            },            {
                text: '状态',
                flex: 1,
                dataIndex: 'isstop',
                renderer : function(v){if(v=='0'){return '<span style="color:green"><b>启用中</b></span>';}else {return '<span style="color:red"><b>已停用</b></span>';}},
                align:'left'
            },            {
                text: '父类',
                width:150, dataIndex: 'parentname',
                align:'left'
            },            {
                text: '使用范围',
                flex: 1,
                dataIndex: 'deptorper',
                renderer : function(v){if(v=='0'){return '部门';}else if(v=='1'){return '个人';}else {return '未知:'+v;}},
                align:'right'
            },            {
                text: '小数位',
                width:150,
                dataIndex: 'dec',
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
