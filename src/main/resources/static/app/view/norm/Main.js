Ext.define('MyApp.view.norm.Main', {
    extend: 'Ext.tree.Panel',
    //title:'质量综合指标',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.tree.*',
        'Ext.ux.CheckColumn'
    ],    
    xtype: 'normMain',
    useArrows: true,
	//iconCls:'Tree',
    lines:true,
	collapseFirst:false,
	tbar: [
	  { xtype: 'button', text: '刷新',action:'refresh',iconCls:'Refresh' },'-',
	  { xtype: 'button', text: '新增',action:'add',iconCls: 'Add' },'-',
	  { xtype: 'button', text: '初始内置',action:'autoInitFromAccess',iconCls: 'Cogwheel' },'-',
	  {
          xtype: 'button',
          text: "Excel",
          iconCls: 'Pageexcel',
          menu:{
              xtype:'menu',
              margin: '0 0 10 0',
              plain: true,
              frame: true,
              items: [
                  { xtype:'menuitem', text: '模板下载', action: 'downTemplate', iconCls: 'Pagewhiteexcel'},'-',
                  { xtype:'menuitem', text: '导入数据', action: 'importData', iconCls: 'Pagewhiteget'},'-',
                  { xtype:'menuitem', text: '导出数据', action: 'exportData', iconCls: 'Pagewhiteput'}
              ]
          }
      }
	  , '-',
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
    store: 'Norm',
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
                text: '指标编号',
                flex: 2,
                sortable: false,
                dataIndex: 'uid'
            },
            {
                text: '指标名称',
                width:100,
                                dataIndex: 'text',
                align:'left'
            },            {
                text: '考核标准',
                width:100,
                                dataIndex: 'checknorm',
                align:'left'
            },            {
                text: '评分标准',
                width:100,
                                dataIndex: 'recordnorm',
                align:'left'
            },            {
                text: '扣分类型',
                flex: 1,
                dataIndex: 'kouFenType',
                renderer : function(v){if(v=='1'){return '按次扣分';}else if(v=='2'){return '加分项';}else{return '标准比例计分';}},
                align:'right'
            },            {
                text: '指标类型',
                flex: 1,
                dataIndex: 'zhiBiaoType',
                renderer : function(v){if(v=='1'){return '月';}else if(v=='2'){return '季度';}else{return '年度';}},
                align:'right'
            },            {
                text: '绩效占比',
                width:150,
                                dataIndex: 'jiXiaoZhanBi',
                align:'right'
            },            {
                text: '公式',
                width:150,
                                dataIndex: 'formula',
                align:'left'
            },            {
                text: '是否计分',
                flex: 1,
                dataIndex: 'iscomp',
                renderer : function(v){if(v=='1'){return '是';}else{return '否';}},
                align:'right'
            },            {
                text: '是否停用',
                flex: 1,
                dataIndex: 'isstop',
                renderer : function(v){if(v=='1'){return '是';}else{return '否';}},
                align:'right'
            },            {
                text: '标准分数',
                width:100,
                                dataIndex: 'score',
                align:'right'
            },            {
                text: '单词扣分',
                width:100,
                                dataIndex: 'rec1',
                align:'right'
            },            {
                text: '最多扣分',
                width:100,
                                dataIndex: 'recsum',
                align:'right'
            },            {
                text: '扣分是否可改',
                flex: 1,
                dataIndex: 'recforce',
                renderer : function(v){if(v=='1'){return '是';}else{return '否';}},
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
