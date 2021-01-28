Ext.define('MyApp.view.zkkDeptNormKouFen.Main', {
    extend: 'Ext.grid.Panel',
    //title:'',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.ux.CheckColumn',
        'Ext.util.*',
        'Ext.toolbar.Paging'
    ],    
    xtype: 'zkkDeptNormKouFenMain',
    lines:true,
	tbar: [{
	    xtype: "combobox",
	    name: "zhiBiaoType",
	    id: "zhiBiaoType",
	    fieldLabel: '指标类型',
	    store: new Ext.data.ArrayStore({
	        fields: ['dickey', 'dicval'],
	        data: [["1", '月'],["2",'季度'],["3",'年度']]
	    }),
	    editable: false,
	    width : 130,
	    labelWidth : 60,
	    displayField: "dicval",
	    valueField: "dickey",
	    allowBlank: true,
	    emptyText: "--请选择--",
	    value:"1",
	    queryMode: "local"
	},'-',
	  {
			id : 'zhiBiaoInFo',
			name:'zhiBiaoInFo',
			width : 150,
			labelWidth : 70,
			xtype : 'textfield',
			emptyText: '请输入指标信息'
	  },'-',{
          xtype: "combobox",
          name: "isKouFen",
          id: "isKouFen",
          fieldLabel: '科室是否扣分',
          store: new Ext.data.ArrayStore({
              fields: ['dickey', 'dicval'],
              data: [["1", '是'],["0",'否']]
          }),
          editable: false,
          width : 200,
          displayField: "dicval",
          valueField: "dickey",
          allowBlank: true,
          emptyText: "--请选择--",
          queryMode: "local"
      },'-' ,{
          xtype: "combobox",
          name: "isMarkNull",
          id: "isMarkNull",
          fieldLabel: '科室扣分说明是否为空',
          store: new Ext.data.ArrayStore({
              fields: ['dickey', 'dicval'],
              data: [["1", '是'],["0",'否']]
          }),
          editable: false,
          width : 200,
          displayField: "dicval",
          valueField: "dickey",
          allowBlank: true,
          emptyText: "--请选择--",
          queryMode: "local"
      },'-',  { xtype: 'button', text: '刷新',action:'refresh',iconCls:'Refresh' },'-',{
			id:'querybtn',
            text: '搜索',
            iconCls:'Find',
            action:'search'
      },'-',
      '->','将鼠标悬浮在操作图标上可获得对应提示'
	],
    store: 'ZkkDeptNormKouFen',
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
          store: 'ZkkDeptNormKouFen',
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
                  text: '考核标准',
                  width:150,
                  dataIndex: 'kaoHeBiaoZhun',
                  align:'left'
                },{
                    text: '评分标准',
                    width:150,
                    dataIndex: 'pingFenBiaoZhun',
                    align:'left'
                  },{
                   text: '最高扣分',
                   width:150,
                   dataIndex: 'zuiGaoKouFen',
                   align:'left'
              },{
                text: '科室扣分',
                width:150,
                    dataIndex: 'realKouFen',
                align:'left'
               },{
                   text: '质控扣分',
                   width:150,
                       dataIndex: 'zkkKouFen',
                   align:'left'
                  },{
                      text: '实际扣分',
                      width:150,
                          dataIndex: 'zkkRealKouFen',
                      align:'left'
                     },{
		                text: '科室说明',
		                width:150,
		                    dataIndex: 'kouFenMark',
		                align:'left'
                    },{
		                text: '质控科说明',
		                width:150,
		                    dataIndex: 'zkkKouFenMark',
		                align:'left'
                    }
            ]
        });
        this.callParent();
    }
});
