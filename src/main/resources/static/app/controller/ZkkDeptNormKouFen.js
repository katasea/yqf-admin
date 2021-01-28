var id = null;//定义一个全局变量id 有值就是修改无值就是新增
var store = null;//需要刷新哪个节点
var currentnodeid = '';
var old = '';
var deptId=null;
var father = null;//定义一个全局变量parentid用于记录是新增子节点还是顶级父节点
Ext.define('MyApp.controller.ZkkDeptNormKouFen', {
    extend: 'Ext.app.Controller',
    stores: ['ZkkDeptNormKouFen','DeptTree'],
    models: ['DeptNormKouFen'],
    views:  ['zkkDeptNormKouFen.Main','deptNormKouFen.Tree'],
    init: function () {
    	//this can deal and add component listener
    	this.control({
    		'actioncolumn#actioncolumnEditAndDel': {  
    			addclick : function(pa){
    				this.addLeaf(pa.record.data.kid,pa.record);
    			},
    			editclick : function(pa){
    				this.edit(pa.record.data.kid,pa.record);
    			},
    			deleteclick: function(pa) {
    				var name = pa.record.data.kid;
    				var id = pa.record.data.kid;
    				currentnodeid = id;
                    Ext.Msg.confirm("提示","确定删除<span style='color:red;font-weight:bold'>"+name+"</span>吗？不可恢复！",function(btn){
                    	if(btn=='yes') {
                    		mask.show();
                    		Ext.Ajax.request({
                			    url: contextPath + '/deptNormKouFen/del',
                			    params: {
                			        id   : id,
                			        name : name
                			    },
                			    success: function(response){
                			    	mask.hide();
                			        var result = Ext.JSON.decode(response.responseText);
                			        if(result.flag) {
                			        	parent.window.showMessage(1,"已经成功删除指定行："+id +"-"+name,2,"提示");
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
    	 	'zkkDeptNormKouFenMain button[action=refresh]': {
                click: this.refresh
            },
            'zkkDeptNormKouFenMain button[action=add]': {
                click: this.add
            },
            'zkkDeptNormKouFenMain button[action=print]': {
                click: this.print
            },
            'zkkDeptNormKouFenMain button[action=search]': {
                click: this.search
            },
            'zkkDeptNormKouFenAdd button[action=save]': {
                click: this.save
            },
            'zkkDeptNormKouFenAdd button[action=cancel]': {
                click: this.cancel
            },
            'zkkDeptNormKouFenMain':{
            	itemdblclick : this.gridDBClickListener
            },
            'textfield[name=keyword]': {
            	specialkey: this.searchTxListener
            },
            'deptNormKouFenTree' : {
                itemclick: this.treeItemClick
            }
        });
        store = this.getZkkDeptNormKouFenStore();
        //Get my store and add listener 
        this.getZkkDeptNormKouFenStore().on('beforeload',function(store, operation, eOpts ){
        	var zhiBiaoInFo = encodeURIComponent(Ext.getCmp('zhiBiaoInFo').getValue());
        	var zhiBiaoType = encodeURIComponent(Ext.getCmp('zhiBiaoType').getValue());
        	var isKouFen = encodeURIComponent(Ext.getCmp('isKouFen').getValue());
        	var isMarkNull = encodeURIComponent(Ext.getCmp('isMarkNull').getValue());
        	
        	Ext.apply(this.proxy.extraParams,{
        		'zhiBiaoInFo' : zhiBiaoInFo,
        		'deptId':deptId,
        		'zhiBiaoType':zhiBiaoType,
        		'isKouFen':isKouFen,
        		'isMarkNull':isMarkNull
        	});
        });
    },
    refresh : function(btn) {
    	this.getZkkDeptNormKouFenStore().load({params:{start:0}});
    },
    add : function(btn){
        old = '';
   		father = null;id = null;
    	this.createAddWin('add');
    	parentnodeid = null;currentnodeid = null;
    },
    edit:function(vid,record) {
    	id = vid;
		old = record.data.kid;
		var win = this.createAddWin('edit');
		node = null;
		win.down('zkkDeptNormKouFenAdd').getForm().loadRecord(record);
    },
    createAddWin:function(flag){
    	var iconCls = '';var title = '';if(flag == 'add') {iconCls = 'Add';title = '新增'}else if(flag == 'edit'){iconCls='Edit';title='修改'}
    	var form = Ext.widget('zkkDeptNormKouFenAdd');
    	var window = Ext.create('Ext.window.Window', {
		    title:title,iconCls:iconCls,height: pageHeight-100,
		    frame : true,border : 0,autoScroll:true,width: pageWidth-400,minWidth:350,
		    scroll:true,modal:true,layout: 'fit', items: form
		});window.show();return window;
    },
    treeItemClick:function(objtree, record, item, index, e, eOpts ){
    	deptId = record.data.id;
        var leaf = record.data.leaf;
        if(leaf) {
            this.getZkkDeptNormKouFenStore().load();
            Ext.getCmp('vpcViewport').down('zkkDeptNormKouFenMain').down('button[action="search"]').setDisabled(false);
        }
    },
    search:function(btn) {
    	this.refresh();
    },
    searchTxListener: function (textfield, e) {
        if (e.getKey() == Ext.EventObject.ENTER) {
        	this.search();
        }
    },
    save:function(btn){
    	var form = btn.up('form').getForm();
        if (form.isValid()) {
        	mask.show();
            // Submit the Ajax request and handle the response
            form.submit({
            	params:{father:father,id:id},
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
    gridDBClickListener:function(tree, record, item, index, e, eOpts ) {
    	this.edit(record.data.kid,record);
    }
    
});
