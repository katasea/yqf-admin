var id = null;//定义一个全局变量id 有值就是修改无值就是新增
var funcode = 1;
var allowAdd=0;
var morig = null;
var morigname = null;
var parentnodeid = '';//需要刷新哪个节点
var currentnodeid = '';
var old = '';
var father = null;//定义一个全局变量parentid用于记录是新增子节点还是顶级父节点
Ext.define('MyApp.controller.AuthCenter', {
    extend: 'Ext.app.Controller',
    stores: ['AuthCenter','AuthCenterLeft','AuthCenterRight'],
    models: ['AuthCenter'],
    views:  ['authCenter.Main','authCenter.Add','authCenter.Left','authCenter.Right','authCenter.Center'],
    init: function () {
    	//this can deal and add component listener
    	this.control({
    		'actioncolumn#actioncolumnEditAndDel': {  
    			deleteclick: function(pa) {
    				var id = pa.record.data.pkid;
                    Ext.Msg.confirm("提示","确定删除<span style='color:red;font-weight:bold'>"+pa.record.data.mtargname+"</span>对应吗？",function(btn){
                    	if(btn==='yes') {
                    		mask.show();
                    		Ext.Ajax.request({
                			    url: contextPath + '/acc/delAuth',
                			    params: {
                                    "pkid":pa.record.data.pkid
                			    },
                			    success: function(response){
                			    	mask.hide();
                			        var result = Ext.JSON.decode(response.responseText);
                			        if(result.flag) {
                			        	parent.window.showMessage(1,"删除对应关系成功",2,"提示");
                                        Ext.getCmp('vpcViewport').down('authCenterRight').getStore().load({});
                                        Ext.getCmp('vpcViewport').down('authCenterMain').getStore().load({});
                			        }else {
                			        	Ext.Msg.alert('失败了', result ? result.msg : '服务器未响应');
                			        }
                			    }
                			});
                    	}
    				});
    			}
    		},
            'actioncolumn#actioncolumnEditAndDel2': {
                editclick : function(pa){
                    this.edit(pa.record.data.dicid,pa.record);
                },
                deleteclick: function(pa) {
                    if(allowAdd=='0') {
                        parent.window.showMessage(2,'当前选项不允许对列表做操作！',2,'提示');
                    }else {
                        var name = pa.record.data.MVALUE;
                        var id = pa.record.data.MKEY;
                        currentnodeid = id;
                        Ext.Msg.confirm("提示","确定删除<span style='color:red;font-weight:bold'>"+pa.record.data.MVALUE+"</span>吗？不可恢复！",function(btn){
                            if(btn==='yes') {
                                mask.show();
                                Ext.Ajax.request({
                                    url: contextPath + '/baseDic4List/del',
                                    params: {
                                        id   : id,
                                        type : funcode
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
                }
            },
    	 	'authCenterMainFrame combobox[name=authType]': {
                change: this.comboboxChange
            },
    	 	'authCenterLeft button[action=searchLeft]': {
                click: this.refreshLeft
            },
    	 	'authCenterLeft button[action=add]': {
                click: this.add
            },
    	 	'authCenterMain button[action=searchCenter]': {
                click: this.refreshCenter
            },
    	 	'authCenterMain button[action=delete]': {
                click: this.clearRelaData
            },
    	 	'authCenterRight button[action=searchRight]': {
                click: this.refreshRight
            },
            'authCenterLeftAdd button[action=save]': {
                click: this.save
            },
            'authCenterLeftAdd button[action=cancel]': {
                click: this.cancel
            },
            'authCenterLeft':{
            	itemclick : this.leftClickListener,
                itemdblclick: this.leftDLClickListener
            },
            'authCenterRight':{
                itemdblclick: this.rightDLClickListener
            },
            'authCenterMain':{
                itemdblclick: this.centerDLClickListener
            },
            'textfield[name=keywordLeft]': {
            	specialkey: this.searchLeftTxListener
            },
            'textfield[name=keywordCenter]': {
            	specialkey: this.searchCenterTxListener
            },
            'textfield[name=keywordRight]': {
            	specialkey: this.searchRightTxListener
            }
        });
        
        //Get my store and add listener 
        this.getAuthCenterStore().on('beforeload',function(store, operation, eOpts ){
        	var keyword = encodeURIComponent(Ext.getCmp('keywordCenter').getValue());
        	Ext.apply(this.proxy.extraParams,{
        		'keyword' : keyword,
                'funcode' : funcode,
                'morig'   : morig
        	});
        });
        this.getAuthCenterLeftStore().on('beforeload',function(store, operation, eOpts ){
        	var keyword = encodeURIComponent(Ext.getCmp('keywordLeft').getValue());
        	Ext.apply(this.proxy.extraParams,{
        		'keyword' : keyword,
                'funcode' : funcode
        	});
        });
        this.getAuthCenterRightStore().on('beforeload',function(store, operation, eOpts ){
        	var keyword = encodeURIComponent(Ext.getCmp('keywordRight').getValue());
        	Ext.apply(this.proxy.extraParams,{
        		'keyword' : keyword,
                'funcode' : funcode,
                'morig'   : morig
        	});
        });
    },
    comboboxChange : function(field, newValue, oldValue, eOpts ){
        funcode = newValue.split("-")[0];
        allowAdd = newValue.split("-")[1];
        //允许新增左边的待设定列表
        if(allowAdd === '1') {
            Ext.getCmp('vpcViewport').down('authCenterLeft').down('button[action=add]').setDisabled(false);
        }else {
            Ext.getCmp('vpcViewport').down('authCenterLeft').down('button[action=add]').setDisabled(true);
        }
        this.getAuthCenterLeftStore().load({params:{start:0}});
        this.getAuthCenterStore().removeAll();
        this.getAuthCenterRightStore().removeAll();
    },

    refreshLeft : function(btn) {
    	var store = this.getAuthCenterLeftStore();
    	store.removeAll();store.load({params:{start:0}});
    },
    refreshCenter : function(btn) {
    	var store = this.getAuthCenterStore();
    	store.removeAll();store.load({params:{start:0}});
    },
    refreshRight : function(btn) {
    	var store = this.getAuthCenterRightStore();
    	store.removeAll();store.load({params:{start:0}});
    },
    searchLeftTxListener: function (textfield, e) {
        if (e.getKey() === Ext.EventObject.ENTER) {
            this.refreshLeft();
        }
    },
    searchCenterTxListener: function (textfield, e) {
        if (e.getKey() === Ext.EventObject.ENTER) {
            this.refreshCenter();
        }
    },
    searchRightTxListener: function (textfield, e) {
        if (e.getKey() === Ext.EventObject.ENTER) {
            this.refreshRight();
        }
    },

    add : function(btn){
        if(allowAdd=='0') {
            parent.window.showMessage(2,'当前选项不允许对列表做操作！',2,'提示');
        }else {
            old = '';
            father = null;id = null;
            var win = this.createAddWin('add');
            parentnodeid = null;currentnodeid = null;
            win.down('form').down('textfield[id=type]').setValue(funcode);
        }
    },
    edit:function(vid,record) {
        if(allowAdd=='0') {
            parent.window.showMessage(2,'当前选项不允许对列表做操作！',2,'提示');
        }else {
            id = record.data.MKEY;
            old = record.data.MKEY;
            var win = this.createAddWin('edit');
            node = null;
            win.down('form').down('textfield[id=mvalue]').setValue(record.data.MVALUE);
            win.down('form').down('textfield[id=mkey]').setValue(record.data.MKEY);
            win.down('form').down('textfield[id=type]').setValue(funcode);
        }
    },
    createAddWin:function(flag){
        var iconCls = '';var title = '';if(flag == 'add') {iconCls = 'Add';title = '新增'}else if(flag == 'edit'){iconCls='Edit';title='修改'}
        var form = Ext.widget('authCenterLeftAdd');
        var window = Ext.create('Ext.window.Window', {
            title:title,iconCls:iconCls,height: 280,
            frame : true,border : 0,autoScroll:true,width: 300,minWidth:350,
            scroll:true,modal:true,layout: 'fit', items: form
        });window.show();return window;
    },

    save:function(btn){
        if(allowAdd=='0') {
            parent.window.showMessage(2,'当前选项不允许对列表做操作！',2,'提示');
        }else {
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
                        Ext.getCmp('vpcViewport').down('authCenterLeft').getStore().load({});
                    },
                    failure: function(form, action) {
                        mask.hide();
                        Ext.Msg.alert('失败了', action.result ? action.result.message : '服务器未响应');
                    }
                });
            }
        }
    },
    cancel:function(btn){
        btn.up('form').up('window').close();
    },
    // autoInitFromAccess:function(btn) {
    // 	parentnodeid = null;currentnodeid = null;
    // 	Ext.Msg.confirm('提示','确定初始化内置数据吗?操作不可逆，会删除本年已存在数据!确定操作吗?',function(btn) {
    // 		if(btn==='yes') {mask.show();
    //     		Ext.Ajax.request({
    // 			    url: contextPath + '/basDicInfo/autoInitFromAccess',params: {},
    // 			    success: function(response){
    // 			    	mask.hide();var result = Ext.JSON.decode(response.responseText);
    // 			        if(result.flag) {
    // 			        	parent.window.showMessage(1,"初始化内置操作成功！",2,"成功");
    // 			        	refreshNode(Ext.getCmp('vpcViewport').down('basDicInfoMain'), currentnodeid, parentnodeid);
    // 			        }else {Ext.Msg.alert('失败', result ? result.msg : '服务器未响应');}
    // 			    }
    // 			});
    //     	}
    // 	});
    // },
    clearRelaData:function(btn) {
    	Ext.Msg.confirm('提示','清空'+morigname+'的全部对应关系吗?操作不可逆，会删除所有已经对应的数据!确定操作吗?',function(btn) {
    		if(btn==='yes') {mask.show();
        		Ext.Ajax.request({
    			    url: contextPath + '/acc/delAuthByMorig',params: {morig:morig},
    			    success: function(response){
    			    	mask.hide();
    			    	var result = Ext.JSON.decode(response.responseText);
    			        if(result.flag) {
    			        	parent.window.showMessage(1,"完成清除"+morigname+"对应关系",2,"成功");
                            Ext.getCmp('vpcViewport').down('authCenterRight').getStore().load({});
                            Ext.getCmp('vpcViewport').down('authCenterMain').getStore().load({});
    			        }else {Ext.Msg.alert('失败', result ? result.msg : '服务器未响应');}
    			    }
    			});
        	}
    	});
    },
    leftClickListener:function(tree, record, item, index, e, eOpts ) {
        morig = record.data.MKEY;
        morigname = record.data.MVALUE;
    	this.refreshCenter();
    	this.refreshRight();
    },
    leftDLClickListener:function(tree, record, item, index, e, eOpts ) {
    	this.edit(record.data.dicid,record);
    },
    rightDLClickListener:function(tree, record, item, index, e, eOpts ) {
        //新增关联
        Ext.Ajax.request({
            url: contextPath + '/acc/addAuth',method:'post',
            params: {"morig":morig,"morigname":morigname,"funcode":funcode,"mtarg":record.data.MKEY,"mtargname":record.data.MVALUE},
            success: function(response){
                mask.hide();var result = Ext.JSON.decode(response.responseText);
                if(result.flag) {
                    parent.window.showMessage(1,"成功关联"+morigname+"-"+record.data.MVALUE,2,"提示");
                    Ext.getCmp('vpcViewport').down('authCenterMain').getStore().load({});
                    Ext.getCmp('vpcViewport').down('authCenterRight').getStore().load({});
                }else {Ext.Msg.alert('失败', result ? result.msg : '服务器未响应');}
            }
        });
    },
    centerDLClickListener:function(tree, record, item, index, e, eOpts ) {
        //删除关联
        Ext.Ajax.request({
            url: contextPath + '/acc/delAuth',method:'post',
            params: {"pkid":record.data.pkid},
            success: function(response){
                mask.hide();var result = Ext.JSON.decode(response.responseText);
                if(result.flag) {
                    parent.window.showMessage(2,"取消关联"+record.data.morigname+"-"+record.data.mtargname,2,"提示");
                    Ext.getCmp('vpcViewport').down('authCenterRight').getStore().load({});
                    Ext.getCmp('vpcViewport').down('authCenterMain').getStore().load({});
                }else {Ext.Msg.alert('失败', result ? result.msg : '服务器未响应');}
            }
        });
    }

});
