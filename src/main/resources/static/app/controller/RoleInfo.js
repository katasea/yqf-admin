var id = null;//定义一个全局变量id 有值就是修改无值就是新增
var store = null;//需要刷新哪个节点
var currentnodeid = '';
var old = '';
var father = null;//定义一个全局变量parentid用于记录是新增子节点还是顶级父节点
var chooseUserAddRoleAuth;
var userRoleInfoTimes;//新增或者删除用户对应角色权限的时候，提示信息只提示一次。
Ext.define('MyApp.controller.RoleInfo', {
    extend: 'Ext.app.Controller',
    stores: ['RoleInfo','UserMgr','UserInfo','UserInfoNoPage'],
    models: ['RoleInfo','UserInfo'],
    views:  ['roleInfo.Main','roleInfo.Add','common.UserMgr','common.ComboGridBox'],
    init: function () {
    	//this can deal and add component listener
    	this.control({
    		'actioncolumn#actioncolumnEditAndDel': {  
    			addclick : function(pa){
    				this.addLeaf(pa.record.data.pkid,pa.record);
    			},
    			editclick : function(pa){
    				this.edit(pa.record.data.pkid,pa.record);
    			},
    			deleteclick: function(pa) {
    				var name = pa.record.data.roledesc;
    				var id = pa.record.data.pkid;
    				currentnodeid = id;
                    Ext.Msg.confirm("提示","确定删除<span style='color:red;font-weight:bold'>"+name+"</span>吗？不可恢复！",function(btn){
                    	if(btn=='yes') {
                    		mask.show();
                    		Ext.Ajax.request({
                			    url: contextPath + '/roleInfo/del',
                			    params: {
                			        id   : id,
                			        name : name
                			    },
                			    success: function(response){
                			    	mask.hide();
                			        var result = Ext.JSON.decode(response.responseText);
                			        if(result.flag) {
                                        parent.window.showMessage(1, "已经成功删除指定行："+id +"-"+name, 2, '提示');
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
            'actioncolumn#actioncolumnEditAndDelOfWin': {
                deleteUserMgrClick: function (pa) {
                    this.removeUserOutRole(pa.record);
                }
            },
    	 	'roleInfoMain button[action=refresh]': {
                click: this.refresh
            },
            'roleInfoMain button[action=add]': {
                click: this.add
            },
            'roleInfoMain button[action=print]': {
                click: this.print
            },
            'roleInfoMain button[action=search]': {
                click: this.search
            },
            'roleInfoAdd button[action=save]': {
                click: this.save
            },
            'roleInfoAdd button[action=cancel]': {
                click: this.cancel
            },
            'button[action=addUserAuth]':{
                click: this.addUserAuth
            },
            'roleInfoMain':{
            	itemdblclick : this.gridDBClickListener
            },
            'textfield[name=keyword]': {
            	specialkey: this.searchTxListener
            }
        });
        store = this.getRoleInfoStore();
        //Get my store and add listener 
        this.getRoleInfoStore().on('beforeload',function(store, operation, eOpts ){
        	var keyword = encodeURIComponent(Ext.getCmp('keyword').getValue());
        	Ext.apply(this.proxy.extraParams,{
        		'keyword' : keyword
        	});
        });
    },
    removeUserOutRole: function (record) {
        var userpkid = record.data.pkid;
        var resfrom = record.data.resfrom;
        var username = record.data.username;
        var controller = this;
        Ext.Msg.confirm("提示", "确定将用户“<span style='color:red;font-weight:bold'>" + username + "</span>”从当前角色中移除吗？", function (btn) {
            if (btn == 'yes') {
                mask.show();
                //在临时存储里面查找，如果找到就临时删除。
                for(var i = 0; i < chooseUserAddRoleAuth.length; i++) {
                    if(chooseUserAddRoleAuth[i] == userpkid) {
                        mask.hide();
                        chooseUserAddRoleAuth.splice(i);
                        controller.getUserMgrStore().remove(record);
                        parent.window.showMessage(2, '临时移除权限成功，点击保存后才会生效！', 2, '提示');
                        return;
                    }
                }
                //如果没有找到说明记录已存在后台，就后台直接删除。
                Ext.Ajax.request({
                    url: contextPath + '/userInfo/delUserResRoleAuth',
                    params: {
                        rolepkid: id,
                        userpkid: userpkid,
                        resfrom: resfrom
                    },
                    success: function (response) {
                        mask.hide();
                        var result = Ext.JSON.decode(response.responseText);
                        if (result.flag) {
                            //前台脚本删除store里面的记录，省后台访问数据库
                            controller.getUserMgrStore().remove(record);
                            parent.window.showMessage(1, '移除权限成功！', 2, '提示');
                        } else {
                            Ext.Msg.alert('失败了', result ? result.msg : '服务器未响应');
                        }
                    }
                });
            }
        });
    },
    refresh : function(btn) {
    	var store = this.getRoleInfoStore();
        store.load({params:{start:0}});
    },
    add : function(btn){
   		father = null;id = null;
    	this.createAddWin('add');
    	parentnodeid = null;currentnodeid = null;
    },
    searchUser: function (btn) {
        this.getUserMgrStore().on('beforeload', function (store, operation, eOpts) {
            var keyword = encodeURIComponent(Ext.getCmp('keyword4User').getValue());
            Ext.apply(this.proxy.extraParams, {
                'rolePkid': id,
                'keyword': keyword
            });
        });
        this.getUserMgrStore().load();
        this.getUserInfoStore().on('beforeload',function(store, operation, eOpts) {
            Ext.apply(this.proxy.extraParams, {
                'rolePkid': id
            });
        });
        this.getUserInfoStore().load();

    },
    edit:function(vid,record) {
    	id = vid;
		var win = this.createAddWin('edit');
		old = record.data.roleid;
		win.down('roleInfoAdd').getForm().loadRecord(record);
    },
    createAddWin:function(flag){
    	var iconCls = '';var title = '';if(flag == 'add') {iconCls = 'Add';title = '新增'}else if(flag == 'edit'){iconCls='Edit';title='修改'}
    	var form = Ext.widget('roleInfoAdd');
    	var window = Ext.create('Ext.window.Window', {
		    title:title,iconCls:iconCls,height: pageHeight-60,
		    frame : true,border : 0,autoScroll:true,width: pageWidth-500,minWidth:500,
		    scroll:true,modal:true,layout: 'fit', items: form
		});window.show();
		//初始化记录临时添加的用户，等待传递到后台
        chooseUserAddRoleAuth = new Array();
        userRoleInfoTimes = 0;
        this.searchUser();
        return window;
    },
    search:function(btn) {
    	this.refresh();
    },
    searchTxListener: function (textfield, e) {
        if (e.getKey() == Ext.EventObject.ENTER) {
        	this.search();
        }
    },
    addUserAuth: function(btn) {
        var comboOfSelectUser = btn.up('userMgr').down('comboGridBox');
        var username = comboOfSelectUser.getDisplayValue();
        if(username === '' || username == null) {
            parent.window.showMessage(3,'请选择用户后再点添加！',3,'错误');
        }else {
            var userpkid = comboOfSelectUser.getValue();
            //存储选取的用户id。
            chooseUserAddRoleAuth.push(userpkid);
            //选取的记录
            var record = comboOfSelectUser.displayTplData[0];
            record.resfrom = '1';
            this.getUserMgrStore().insert(0,record);
            //置空选取框
            comboOfSelectUser.setValue("");
            if(userRoleInfoTimes == 0) {
                parent.window.showMessage(2, '临时添加成功，点击保存后才会生效！', 4, '警告');
                userRoleInfoTimes = 1;
            }
        }
    },
    save:function(btn){
    	var form = btn.up('form').getForm();
    	//ztree 对象
    	var ztreeObj = $.fn.zTree.getZTreeObj("role4resZtreeV3");
    	//获取选中的节点信息
    	var resTreeChecked = ztreeObj.getCheckedNodes(true);
    	var chooseResAddRoleAuth = '';
    	for(var i = 0 ; i < resTreeChecked.length ; i++) {
    	    var single = resTreeChecked[i];
    	    if(chooseResAddRoleAuth == '') {
    	        chooseResAddRoleAuth = single.id;
            }else {
    	        chooseResAddRoleAuth = chooseResAddRoleAuth + "," + single.id;
            }
        }
        if (form.isValid()) {
        	mask.show();
            // Submit the Ajax request and handle the response
            form.submit({
            	params:{
            	    father:father,
                    id:id,
                    chooseUserAddRoleAuth:chooseUserAddRoleAuth,
                    chooseResAddRoleAuth:chooseResAddRoleAuth
                },
                success: function(form, action) {
                   mask.hide();
                   old = '';
                   parent.window.showMessage(1, '保存数据成功！', 2, '提示');
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
    	this.edit(record.data.pkid,record);
    }
    
});
