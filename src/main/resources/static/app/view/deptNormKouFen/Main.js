Ext.define('MyApp.view.deptNormKouFen.Main', {
    extend: 'Ext.grid.Panel',
    //title:'',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.ux.CheckColumn',
        'Ext.util.*',
        'Ext.toolbar.Paging'
    ],    
    xtype: 'deptNormKouFenMain',
    lines:true,
	tbar: [
	  { xtype: 'button', text: '刷新',action:'refresh',iconCls:'Refresh' },'-',{
          xtype: "combobox",
          name: "zhiBiaoType",
          id: "zhiBiaoType",
          fieldLabel: '指标类型',
          store: new Ext.data.ArrayStore({
              fields: ['dickey', 'dicval'],
              data: [["1", '月'],["2",'季度'],["3",'年度']]
          }),
          editable: false,
          displayField: "dicval",
          valueField: "dickey",
          allowBlank: true,
          emptyText: "--请选择--",
          value:"1",
          queryMode: "local"
      },'-',
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
    store: 'DeptNormKouFen',
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
          store: 'DeptNormKouFen',
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
                dataIndex: 'zhiBiaoId',
                align:'left'
            },{
                text: '指标名称',
                width:150,
                dataIndex: 'zhiBiaoName',
                align:'left'
              },{
                   text: '最高扣分',
                   width:150,
                   dataIndex: 'zuiGaoKouFen',
                   align:'left'
              },{
                text: '扣分(单击可编辑)',
                width:150,
                    dataIndex: 'kouFen',
                align:'left'
              },{
                text: '实际扣分',
                width:150,
                    dataIndex: 'realKouFen',
                align:'left'
               },{
                text: '扣分说明',
                width:150,
                    dataIndex: 'kouFenMark',
                align:'left'
            }
            ]
        });
        this.callParent();
    }
});
