var required = '<span style="color:red;font-weight:bold" data-qtip="必填">*</span>';
Ext.define('MyApp.view.midDataDic.Add', {
    extend: 'Ext.form.Panel',
    xtype: 'midDataDicAdd',
    autoScroll:true,
    requires: [
    	'*',
        'Ext.ux.DataTip'
    ], 
    viewConfig:{loadMask:{msg:"请稍后..."}},
    frame: false,bodyPadding: '10 10 10 10',border:0,
    url:contextPath+'/midDataDic/save',
    fieldDefaults: {msgTarget: 'side',labelWidth: 100,anchor  : '75%'},
    plugins: {ptype: 'datatip'},
    defaultType: 'textfield',
    items: [
		    {
		        fieldLabel: '编号',xtype:'textfield',
		        id:'id',name: 'id',
	        	validator : function(value){
					var isOk = false;var msg = "";
					if(value != old) {
						Ext.Ajax.request({
							waitMsg : '正在校验编号是否已经存在,请稍后...', 
							url : contextPath+"/midDataDic/validator",
							params : {'value' : value,'keyid' : 'id'},//
							method : "POST",timeout : 40000, async : false,
							success : function(response, opts) {
								var resObj = Ext.decode(response.responseText);
								if(resObj.success){ isOk = true;}else{isOk = false;msg = resObj.msg;}
							},failure: function(response, opts) {isOk = false; msg ="错误"; }
						});
					}else {isOk = true;}
					if(isOk){return true;}else{return msg;}//错误原因
				},
		        allowBlank: false,afterLabelTextTpl: required,tooltip: '请输入【必填项】【编号】'
		    },
		    {
		        fieldLabel: '名称',xtype:'textfield',
		        id:'name',name: 'name',
		        allowBlank: false,afterLabelTextTpl: required,tooltip: '请输入【必填项】【名称】'
		    },
		    {
		        fieldLabel: '类型',xtype:'radiogroup',
		    	items:[{inputValue: '1',boxLabel: '数据录入',anchor  : '60%',name  : 'type',checked: true}, {inputValue: '2',anchor  : '60%',name  : 'type',boxLabel: '自动计算'}],
		    	tooltip: '请输入【类型】'
		    },
            {
                xtype: "combobox",
                name: "isstop",
                id: "isstop",
                fieldLabel: '状态',
                store: new Ext.data.ArrayStore({
                    fields: ['dickey', 'dicval'],
                    data: [["0", '0 启用'],["1",'1 停用']]
                }),
                editable: false,
                displayField: "dicval",
                valueField: "dickey",
                allowBlank: false,
                afterLabelTextTpl: required,
                emptyText: "--请选择--",
                value:"0",
                queryMode: "local",
                tooltip: '请输入【状态】'
            },

			{
				xtype: "combobox",
				name: "parentid",
				id: "parentid",
				fieldLabel: '归属类别',
				store: {
					extend: "Ext.data.Store",
					fields: ["dicval", "dickey"],
					autoLoad: true,
					proxy: {
						type: "ajax",
						actionMethods: {read: "POST"},
						extraParams: {type: 2},
						url: contextPath + "/midDataDicParent/list",
						reader: {
							type: "json",
							root: "root"
						}
					}
				},
				editable: false,
				displayField: "dicval",
				valueField: "dickey",
				allowBlank: false,
				afterLabelTextTpl: required,
				emptyText: "--请选择--",
				queryMode: "local"
			},
			{
				xtype: "combobox",
				name: "deptorper",
				id: "deptorper",
				fieldLabel: '使用范围',
                store: new Ext.data.ArrayStore({
                    fields: ['dickey', 'dicval'],
                    data: [["0", '0 部门'],["1",'1 个人']]
                }),
				editable: false,
				displayField: "dicval",
				valueField: "dickey",
				allowBlank: false,
				afterLabelTextTpl: required,
				emptyText: "--请选择--",
				value:"1",
				queryMode: "local"
			},
		    {
		        fieldLabel: '小数位',xtype:'numberfield',
		        id:'dec',name: 'dec',
	       		decimalPrecision : '0',
				value:2,
		        allowBlank: false,afterLabelTextTpl: required,tooltip: '请输入【必填项】【小数位】'
		    }    ],
    buttons: [{text: '保存',action:'save'},{text: '取消',action:'cancel'}],
    initComponent: function() {this.callParent();}
});
