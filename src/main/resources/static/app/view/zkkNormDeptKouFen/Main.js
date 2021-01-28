Ext.define('MyApp.view.zkkNormDeptKouFen.Main', {
    extend: 'Ext.grid.Panel',
    //title:'',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.ux.CheckColumn',
        'Ext.util.*',
        'Ext.toolbar.Paging'
    ],    
    xtype: 'zkkNormDeptKouFenMain',
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
      },
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
    store: 'ZkkNormDeptKouFen',
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
          store: 'ZkkNormDeptKouFen',
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
                width:150,
                            dataIndex: 'deptOrPersonNo',
                            align:'left'
            }, {
                    text: '名称',
                    width:150,
                    dataIndex: 'deptOrPersonName',
                    align:'left'
           }, {
               text: '最高扣分',
               width:150,
               dataIndex: 'zuiGaoKouFen',
               align:'left'
             }
           ,{
                text: '科室扣分',
                 width:150,
                    dataIndex: 'realKouFen',
                align:'left'
            }, {
                text: '质控扣分(单击可编辑)',
                width:150,
                    dataIndex: 'zkkKouFen',
                align:'left'
            }, {
                text: '实际扣分',
                width:150,
                    dataIndex: 'zkkRealKouFen',
                align:'left'
            }, {
                text: '科室说明',
              width:150,
                dataIndex: 'kouFenMark',
                align:'left'
            }, {
                text: '质控说明',
                width:150,
                  dataIndex: 'zkkKouFenMark',
                  align:'left'
              }
            
            ]
        });
        this.callParent();
    }
});
