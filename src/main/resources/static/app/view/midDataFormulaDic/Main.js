Ext.define('MyApp.view.midDataFormulaDic.Main', {
    extend: 'Ext.grid.Panel',
    //title:'',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.ux.CheckColumn',
        'Ext.util.*',
        'Ext.toolbar.Paging'
    ],    
    xtype: 'midDataFormulaDicMain',
    lines:true,
	tbar: [
	  // { xtype: 'button', text: '刷新',action:'refresh',iconCls:'Refresh',disabled:true },'-',
	  { xtype: 'button', text: '新增',action:'add',iconCls: 'Add',disabled:true },'-',
	  { xtype: 'button', text: '试算当前',action:'calc',iconCls: 'Calculator',disabled:true },'-',
	  { xtype: 'button', text: '试算全部',action:'calcAll',iconCls: 'Calendar',disabled:true },'-',
	  { xtype: 'button', text: '结果预览',action:'calcView',iconCls: 'Page',disabled:true },'-',
	  { xtype: 'button', text: '初始内置',action:'autoInitFromAccess',iconCls: 'Cogwheel',disabled:true },'-',
	  {
			id : 'keyword',
			name:'keyword',
			width : 120,
			labelWidth : 70,
			xtype : 'textfield',
			emptyText: '请输入关键字'
	  },'-', {
			id:'querybtn',
            text: '搜索',
            iconCls:'Find',
            disabled:true,
            action:'search'
      },'-',
      '->','将鼠标悬浮在操作图标上可获得对应提示'
	],
    store: 'MidDataFormulaDic',
    rowLines:true,
    viewConfig:{
	    loadMask:{msg:"请稍后..."}
	},
    columnLines:true,
    rootVisible: false,
    multiSelect: true,
    enableColumnMove   : true,
    enableColumnResize : true,            //是否允许改变列宽
    stripeRows         : true,
    bbar: {
          xtype: 'pagingtoolbar',
          store: 'MidDataFormulaDic',
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
                text: '排序号',
                width:70,
                dataIndex: 'orderid',
                align:'left'
            },            {
                text: '名称',
                width:200,
                dataIndex: 'name',
                align:'left'
            },            {
                text: '公式',
                flex:1,
                dataIndex: 'formula',
                align:'left'
            },            {
                text: '状态',
                width:80,
                dataIndex: 'isstop',
                renderer : function(v){if(v=='0'){return '<span style="color:green"><b>启用中</b></span>';}else {return '<span style="color:red"><b>已停用</b></span>';}},
                align:'left'
            },            {
                text: '小数位',
                width:75, dataIndex: 'dec',
                align:'right'
            },            {
                text: '计算标识',
                width:85,dataIndex: 'caclfalg',
                renderer : function(v){if(v=='1'){return '<span style="color:green"><b>计算完成</b></span>';}else if(v=='0'){return '<span style="color:black">待计算</span>'}else {return '<span style="color:red"><b>计算失败</b></span>';}},
                align:'right'
            },            {
                text: '失败原因',
                width:100,dataIndex: 'failreason',
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
