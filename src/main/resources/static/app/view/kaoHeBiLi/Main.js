Ext.define('MyApp.view.kaoHeBiLi.Main', {
    extend: 'Ext.grid.Panel',
    //title:'',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.ux.CheckColumn',
        'Ext.util.*',
        'Ext.toolbar.Paging'
    ],    
    xtype: 'kaoHeBiLiMain',
    lines:true,
	tbar: [
	  { xtype: 'button', text: '刷新',action:'refresh',iconCls:'Refresh' },'-',
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
    store: 'KaoHeBiLi',
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
          store: 'KaoHeBiLi',
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
                text: '指标编号',
    width:150,
                    dataIndex: 'zbId',
                align:'left'
            },            {
                text: '指标名称',
    width:150,
                    dataIndex: 'zhiBiaoName',
                align:'left'
            },            {
                text: '自评比例(%)',
    width:150,
                    dataIndex: 'zpbl',
                align:'left'
            },            {
                text: '质控科比例(%)',
    width:150,
                    dataIndex: 'zkkbl',
                align:'left'
            }
            ]
        });
        this.callParent();
    }
});
