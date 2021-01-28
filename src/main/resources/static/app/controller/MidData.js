var id = null;//定义一个全局变量id 有值就是修改无值就是新增
var store = null;//需要刷新哪个节点
var currentnodeid = '';
var gridStore;
var chooseParentId = '';
var controllerObj = null;
//var deptOrper = '';//这个定义在页面上了。
var old = '';
var father = null;//定义一个全局变量parentid用于记录是新增子节点还是顶级父节点
Ext.define('MyApp.controller.MidData', {
    extend: 'Ext.app.Controller',
    stores: ['MidDataDicParentCombo','MidData'],
    models: ['TreeNode'],
    views:  ['midData.InputMain'],
    init: function () {
    	//this can deal and add component listener
    	this.control({
    	 	'midDataMainPanel combobox[name=authType]': {
                change: this.changeCombox
            },
    	 	'midDataMainPanel button[action=search]': {
                click: this.refresh
            },'textfield[name=keyword]': {
                specialkey: this.searchTxListener
            },
            'midDataMain button[action=add]': {
                click: this.add
            },
            'midDataMain button[action=print]': {
                click: this.print
            },
            'midDataMain button[action=autoInitFromAccess]': {
                click: this.autoInitFromAccess
            },
            'midDataMain button[action=search]': {
                click: this.search
            },
            'midDataAdd button[action=save]': {
                click: this.save
            },
            'midDataAdd button[action=cancel]': {
                click: this.cancel
            },
            'midDataMain':{
            	itemdblclick : this.gridDBClickListener
            },
            'textfield[name=keyword]': {
            	specialkey: this.searchTxListener
            },
            'textfield[name=date]': {render:function(e) {e.getEl().on('click',function(){WdatePicker({dateFmt:'yyyy'});});}}
        });
        store = this.getMidDataStore();
        controllerObj = this;
        //Get my store and add listener 
        this.getMidDataDicParentComboStore().on('load',function(store, operation, eOpts ){
            //默认赋值第一个
            Ext.getCmp('authType').reset();
            var record = Ext.getCmp('authType').getStore().getAt(0);
            chooseParentId = record.data.dickey;
            Ext.getCmp('authType').setValue(record);
            controllerObj.dynamicDataPanel();
        });
    },
    dynamicDataPanel:function() {
        //后台获取动态列
        mask.show();
        Ext.Ajax.request({
            url: contextPath + '/midData/getDynamicColumn',params: {deptOrper:deptOrper,chooseParentId:chooseParentId},
            success: function(response){
                mask.hide();
                //移除旧面板
                Ext.getCmp('dataInputMain').remove(Ext.getCmp('dataInputDynamic'));
                Ext.getCmp('dataInputMain').doLayout(true,true);
                Ext.getCmp('vpcViewport').doLayout(true,true);
                //解析
                var result = Ext.JSON.decode(response.responseText);
                if(result.flag) {
                    //拼凑列表相关信息
                    var dataColumns;
                    var fieldArray = [{ name: 'bh', type: 'string'},{ name: 'name', type: 'string'},{ name: 'deptbh', type: 'string'}];
                    if(deptOrper==='1'){
                        dataColumns = [
                            {header : '人员编号', dataIndex : 'bh', width : 80, summaryRenderer : function() {  return '合计'; }, locked:true },
                            {header : '人员姓名', width : 100, dataIndex : 'name', locked:true },
                            {header : '归属组', width : 150, dataIndex : 'deptbh', locked:true }
                        ];
                    } else{
                        dataColumns = [
                            {header : '部门编号', dataIndex : 'bh', width : 80, summaryType: 'count', summaryRenderer: function() {  return '合计'; }, locked:true},
                            {header : '部门名称', width : 100, dataIndex : 'name', locked:true }
                        ];
                    }

                    // header: '最终劳动工分',
                    // width:120,
                    // dataIndex: 'works',
                    // editor:{xtype: 'numberfield',selectOnFocus :true,
                    // allowBlank:false,minValue: 0,decimalPrecision:6
                    Ext.each(result.data,function(obj, index, json){
                        var colobj = {
                            header:obj.name,
                            width:120,
                            dataIndex:'C'+obj.id,
                            editor:{
                                xtype:'numberfield',
                                selectOnFocus:true,
                                allowBlank:false,
                                minValue:0,
                                decimalPrecision:obj.dec
                            }
                        };
                        var fieldObj = {
                            name:'C'+obj.id,
                            type:'float'
                        };
                        dataColumns.push(colobj);
                        fieldArray.push(fieldObj)
                    });

                    gridStore = Ext.create('Ext.data.Store', {
                        fields : fieldArray,			//根据上面的ajax从后台action动态获得
                        pageSize:15,
                        autoLoad : true,
                        proxy : {
                            type : 'ajax',
                            url : contextPath+"/midData/getDynamicJson",
                            extraParams: {deptOrper: deptOrper,chooseParentId:chooseParentId},
                            actionMethods:{read:'POST'},
                            reader : {
                                type : 'json',root : 'root',totalProperty : 'totalSize'	//json数据,表示分页数据总数
                            }
                        }
                    });

                    gridStore.on('beforeload',function(store, operation, eOpts ){
                        var keyword = encodeURIComponent(Ext.getCmp('keyword').getValue());
                        Ext.apply(this.proxy.extraParams,{
                            'keyword' : keyword
                        });
                    });

                    var grid4Data = Ext.create('Ext.grid.Panel', {
                        // tbar : toolBar,
                        //region: 'center',
                        flex : 1,
                        features: [{ ftype: 'summary', dock : 'bottom' }],
                        id : 'dataInputDynamic',
                        border : false, store: gridStore,
                        columns : dataColumns,
                        plugins:{
                            ptype: 'cellediting', clicksToEdit: 1,
                            listeners:{
                                'edit' : function(editor, e){
                                    if(e.originalValue !== e.value){
                                        controllerObj.saveMidData(e.record.data.bh, e.field, e.value);
                                    }
                                }
                            }
                        },
                        viewConfig: {
                            // focusRow: Ext.emptyFn
                            focus: Ext.emptyFn
                        },
                        dockedItems : [ {
                            xtype : 'pagingtoolbar',
                            store : gridStore, // GridPanel中使用的数据
                            dock : 'bottom',
                            displayInfo : true,
                            emptyMsg : "没有数据",
                            displayMsg : "显示从{0}条数据到{1}条数据，共{2}条数据"
                        }],
                        stripeRows : true, // 是否隔行换色
                        columnLines : true, // 是否显示列分割线
                        enableColumnMove : false, // 是否允许拖放列
                        enableColumnResize : true, // 是否允许改变列宽
                        trackMouseOver : true // 是否高亮显示鼠标所在的行
                    });

                    //新增新面板
                    Ext.getCmp('dataInputMain').add(grid4Data);
                    Ext.getCmp('dataInputMain').doLayout(true,true);
                    Ext.getCmp('vpcViewport').doLayout(true,true);
                    //加载数据
                    //gridStore.load({params:{start:0}});
                    // controllerObj.refresh();
                }else {Ext.Msg.alert('失败', result ? result.msg : '服务器未响应');}
            }
        });
    },
    changeCombox:function(field,newValue,oldValue,eOpts) {
        chooseParentId = newValue;
        this.dynamicDataPanel();
    },
    refresh : function(btn) {
        if(gridStore!=null) {
            gridStore.load({params:{start:0}});
        }
    },
    add : function(btn){
        old = '';
   		father = null;id = null;
    	this.createAddWin('add');
    	parentnodeid = null;currentnodeid = null;
    },
    edit:function(vid,record) {
    	id = vid;
		old = record.data.id;
		var win = this.createAddWin('edit');
		node = null;
		win.down('midDataAdd').getForm().loadRecord(record);
    },
    createAddWin:function(flag) {
    	var iconCls = '';var title = '';if(flag == 'add') {iconCls = 'Add';title = '新增'}else if(flag == 'edit'){iconCls='Edit';title='修改'}
    	var form = Ext.widget('midDataAdd');
    	var window = Ext.create('Ext.window.Window', {
		    title:title,iconCls:iconCls,height: pageHeight-100,
		    frame : true,border : 0,autoScroll:true,width: pageWidth-400,minWidth:350,
		    scroll:true,modal:true,layout: 'fit', items: form
		});window.show();return window;
    },
    searchTxListener: function (textfield, e) {
        if (e.getKey() == Ext.EventObject.ENTER) {
        	this.refresh();
        }
    },
    saveMidData:function(bh,field,value) {
        Ext.Ajax.request({
            url: contextPath + '/midData/input/save',params: {deptorper:deptOrper,bh:bh,field:field,value:value},
            success: function(response){
                var result = Ext.JSON.decode(response.responseText);
                if(!result.flag) {
                    Ext.Msg.alert('失败', result ? result.msg : '服务器未响应');
                }
            }
        });
    },
    save:function(btn){
    	var form = btn.up('form').getForm();
    	var date = Ext.getCmp('date').getValue();
        if (form.isValid()) {
        	mask.show();
            // Submit the Ajax request and handle the response
            form.submit({
            	params:{father:father,id:id,date:date},
                success: function(form, action) {
                   mask.hide();
                   parent.window.showMessage(1,'保存成功！',2,'提示');
                   btn.up('window').close();
                   store.load();
                },
                failure: function(form, action) {
                	mask.hide();
                    Ext.Msg.alert('失败了', action.result ? action.result.message : '服务器未响应');
                }
            });
        }

    },
    cancel:function(btn){
    	btn.up('form').up('window').close();
    },
	autoInitFromAccess:function(btn) {
    	var date = Ext.getCmp('date').getValue();
    	parentnodeid = null;currentnodeid = null;
    	Ext.Msg.confirm('提示','确定初始化内置数据吗?操作不可逆，会删除本年已存在数据!确定操作吗?',function(btn) {
    		if(btn=='yes') {mask.show();
        		Ext.Ajax.request({
    			    url: contextPath + '/midData/autoInitFromAccess',params: {date:date},
    			    success: function(response){
    			    	mask.hide();var result = Ext.JSON.decode(response.responseText);
    			        if(result.flag) {
    			        	parent.window.showMessage(1,"初始化内置操作成功！",2,"成功");
    			        	store.load();
    			        }else {Ext.Msg.alert('失败', result ? result.msg : '服务器未响应');}
    			    }
    			});
        	}
    	});
    },
    gridDBClickListener:function(tree, record, item, index, e, eOpts ) {
    	this.edit(record.data.id,record);
    }
    
});
