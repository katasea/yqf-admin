var id = null;//定义一个全局变量id 有值就是修改无值就是新增
var store = null;//需要刷新哪个节点
var currentnodeid = '';
var old = '';
var midBh = '';
var deptorper = '';
var father = null;//定义一个全局变量parentid用于记录是新增子节点还是顶级父节点
Ext.define('MyApp.controller.MidDataFormulaDic', {
    extend: 'Ext.app.Controller',
    stores: ['MidDataFormulaDic','MidDataDicTreeWithFormula','MidData'],
    models: ['MidDataFormulaDic','TreeNode','MidData'],
    views:  ['midDataFormulaDic.Main','midDataFormulaDic.Add','midDataFormulaDic.Tree','midData.Main'],
    init: function () {
    	//this can deal and add component listener
    	this.control({
    		'actioncolumn#actioncolumnEditAndDel': {  
    			addclick : function(pa){
    				this.addLeaf(pa.record.data.id,pa.record);
    			},
    			editclick : function(pa){
    				this.edit(pa.record.data.pkid,pa.record);
    			},
    			deleteclick: function(pa) {
    				var name = pa.record.data.name;
    				var id = pa.record.data.pkid;
    				currentnodeid = id;
                    Ext.Msg.confirm("提示","确定删除<span style='color:red;font-weight:bold'>"+name+"</span>吗？不可恢复！",function(btn){
                    	if(btn==='yes') {
                    		mask.show();
                    		Ext.Ajax.request({
                			    url: contextPath + '/midDataFormulaDic/del',
                			    params: {
                			        id   : id,
                			        name : name
                			    },
                			    success: function(response){
                			    	mask.hide();
                			        var result = Ext.JSON.decode(response.responseText);
                			        if(result.flag) {
                			        	parent.window.showMessage(1,"已经成功删除指定行："+name,2,"提示");
                			        	store.load();
                			        }else {
                			        	Ext.Msg.alert('失败了', result ? result.msg : '服务器未响应');
                			        }
                			    }
                			});
                    	}
    				});
    			}
    		},
    	 	'midDataFormulaDicMain button[action=refresh]': {
                click: this.refresh
            },
            'midDataFormulaDicMain button[action=add]': {
                click: this.add
            },
            'midDataFormulaDicMain button[action=print]': {
                click: this.print
            },
            'midDataFormulaDicMain button[action=autoInitFromAccess]': {
                click: this.autoInitFromAccess
            },
            'midDataFormulaDicMain button[action=calc]': {
                click: this.calc
            },
            'midDataFormulaDicMain button[action=calcAll]': {
                click: this.calcAll
            },
            'midDataFormulaDicMain button[action=calcView]': {
                click: this.calcView
            },
            'midDataFormulaDicMain button[action=search]': {
                click: this.search
            },
            'midDataMain button[action=search]': {
                click: this.searchMidData
            },
            'midDataMain button[action=refresh2]': {
                click: this.refresh2
            },
            'midDataFormulaDicAdd button[action=save]': {
                click: this.save
            },
            'midDataFormulaDicAdd button[action=cancel]': {
                click: this.cancel
            },
            'midDataFormulaDicMain':{
            	itemdblclick : this.gridDBClickListener
            },
            'textfield[name=keyword]': {
            	specialkey: this.searchTxListener
            },
            'textfield[name=keyword2]': {
                specialkey: this.searchTxListener2
            },
            'midDataFormulaDicTree' : {
                itemclick: this.treeItemClick
            }
        });
        store = this.getMidDataFormulaDicStore();
        //Get my store and add listener 
        this.getMidDataFormulaDicStore().on('beforeload',function(store, operation, eOpts ){
        	var keyword = encodeURIComponent(Ext.getCmp('keyword').getValue());
        	Ext.apply(this.proxy.extraParams,{
        		'keyword' : keyword,
                'midBh':midBh
        	});
        });
        this.getMidDataStore().on('beforeload',function(store, operation, eOpts ){
            var keyword2 = encodeURIComponent(Ext.getCmp('keyword2').getValue());
            Ext.apply(this.proxy.extraParams,{
                'keyword' : keyword2,
                'midBh':midBh,
                'deptorper':deptorper
            });
        });
    },
    refresh : function(btn) {
    	this.getMidDataFormulaDicStore().load({params:{start:0}});
    },
    refresh2 : function(btn) {
    	this.getMidDataStore().load({params:{start:0}});
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
		win.down('midDataFormulaDicAdd').getForm().loadRecord(record);
    },
    createAddWin:function(flag){
    	var iconCls = '';var title = '';if(flag === 'add') {iconCls = 'Add';title = '新增'}else if(flag === 'edit'){iconCls='Edit';title='修改'}
    	var form = Ext.widget('midDataFormulaDicAdd');
    	var window = Ext.create('Ext.window.Window', {
		    title:title,iconCls:iconCls,height: pageHeight-100,
		    frame : true,border : 0,autoScroll:true,width: pageWidth-400,minWidth:350,
		    scroll:true,modal:true,layout: 'fit', items: form
		});window.show();return window;
    },
    treeItemClick:function(objtree, record, item, index, e, eOpts ){
        midBh = record.data.id;
        var leaf = record.data.leaf;
        deptorper = record.data.deptorper;
        if(leaf) {
            this.getMidDataFormulaDicStore().load();
            //点击了左边树列表之后，右边的按钮才允许操作。
            // Ext.getCmp('vpcViewport').down('midDataFormulaDicMain').down('button[action="refresh"]').setDisabled(false);
            Ext.getCmp('vpcViewport').down('midDataFormulaDicMain').down('button[action="add"]').setDisabled(false);
            Ext.getCmp('vpcViewport').down('midDataFormulaDicMain').down('button[action="calc"]').setDisabled(false);
            Ext.getCmp('vpcViewport').down('midDataFormulaDicMain').down('button[action="calcAll"]').setDisabled(false);
            Ext.getCmp('vpcViewport').down('midDataFormulaDicMain').down('button[action="calcView"]').setDisabled(false);
            Ext.getCmp('vpcViewport').down('midDataFormulaDicMain').down('button[action="autoInitFromAccess"]').setDisabled(false);
            Ext.getCmp('vpcViewport').down('midDataFormulaDicMain').down('button[action="search"]').setDisabled(false);
        }
    },
    search:function(btn) {
    	this.refresh();
    },
    searchMidData:function(btn) {
    	this.refresh2();
    },
    searchTxListener: function (textfield, e) {
        if (e.getKey() === Ext.EventObject.ENTER) {
        	this.search();
        }
    },
    searchTxListener2: function (textfield, e) {
        if (e.getKey() === Ext.EventObject.ENTER) {
        	this.searchMidData();
        }
    },
    save:function(btn){
    	var form = btn.up('form').getForm();
        if (form.isValid()) {
        	mask.show();
            // Submit the Ajax request and handle the response
            form.submit({
            	params:{father:father,id:id,midBh:midBh,deptorper:deptorper},
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
    calc:function(btn) {
        Ext.Msg.confirm('提示','确定开始试算本次定义吗?操作不可逆，会删除已存在数据!确定计算吗?',function(btn) {
            if(btn==='yes') {mask.show();
                Ext.Ajax.request({
                    url: contextPath + '/midDataFormulaDic/calc',params: {midBh:midBh},
                    success: function(response){
                        mask.hide();var result = Ext.JSON.decode(response.responseText);
                        store.load();
                        if(result.flag) {
                            parent.window.showMessage(1,"试算完成！",2,"成功");
                        }else {Ext.Msg.alert('失败', result ? result.msg : '服务器未响应');}
                    }
                });
            }
        });
    },
    calcAll:function(btn) {
        Ext.Msg.confirm('提示','确定开始试算本次定义吗?操作不可逆，会删除已存在数据!确定计算吗?',function(btn) {
            if(btn==='yes') {mask.show();
                Ext.Ajax.request({
                    url: contextPath + '/midDataFormulaDic/calc',params: {},
                    success: function(response){
                        mask.hide();var result = Ext.JSON.decode(response.responseText);
                        store.load();
                        if(result.flag) {
                            parent.window.showMessage(1,"试算完成！",2,"成功");
                        }else {Ext.Msg.alert('失败', result ? result.msg : '服务器未响应');}
                    }
                });
            }
        });
    },
    calcView:function(btn) {
        var iconCls = '';var title = '结果预览';
        var form = Ext.widget('midDataMain');
        var window = Ext.create('Ext.window.Window', {
            title:title,iconCls:iconCls,height: pageHeight-100,
            frame : true,border : 0,autoScroll:true,width: pageWidth-400,minWidth:350,
            scroll:true,modal:true,layout: 'fit', items: form
        });
        window.show();
        this.refresh2();
        return window;
    },
	autoInitFromAccess:function(btn) {
    	parentnodeid = null;currentnodeid = null;
    	Ext.Msg.confirm('提示','确定初始化内置数据吗?操作不可逆，会删除已存在数据!确定操作吗?',function(btn) {
    		if(btn==='yes') {mask.show();
        		Ext.Ajax.request({
    			    url: contextPath + '/midDataFormulaDic/autoInitFromAccess',params: {},
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
    	this.edit(record.data.pkid,record);
    }
    
});
