var id = null;//定义一个全局变量id 有值就是修改无值就是新增
var store = null;//需要刷新哪个节点
var currentnodeid = '';
var old = '';
var father = null;//定义一个全局变量parentid用于记录是新增子节点还是顶级父节点
Ext.define('MyApp.controller.CompanyInfo', {
    extend: 'Ext.app.Controller',
    stores: ['CompanyInfo'],
    models: ['CompanyInfo'],
    views: ['companyInfo.Main', 'companyInfo.Add'],
    init: function () {
        //this can deal and add component listener
        this.control({
            'actioncolumn#actioncolumnEditAndDel': {
                addclick: function (pa) {
                    this.addLeaf(pa.record.data.companyid, pa.record);
                },
                editclick: function (pa) {
                    this.edit(pa.record.data.companyid, pa.record);
                },
                deleteclick: function (pa) {
                    var name = pa.record.data.companyname;
                    var id = pa.record.data.companyid;
                    currentnodeid = id;
                    Ext.Msg.confirm("提示", "确定删除<span style='color:red;font-weight:bold'>" + name + "</span>吗？不可恢复！", function (btn) {
                        if (btn == 'yes') {
                            mask.show();
                            Ext.Ajax.request({
                                url: contextPath + '/companyInfo/del',
                                params: {
                                    id: id,
                                    name: name
                                },
                                success: function (response) {
                                    mask.hide();
                                    var result = Ext.JSON.decode(response.responseText);
                                    if (result.flag) {
                                        parent.window.showMessage(1, "已经成功删除指定行：" + id + "-" + name, 2, "提示");
                                        store.load();
                                    } else {
                                        Ext.Msg.alert('失败了', result ? result.msg : '服务器未响应');
                                    }
                                }
                            });
                        }
                    });
                }
            },
            'companyInfoMain button[action=refresh]': {
                click: this.refresh
            },
            'companyInfoMain button[action=add]': {
                click: this.add
            },
            'companyInfoMain button[action=print]': {
                click: this.print
            },
            'companyInfoMain button[action=search]': {
                click: this.search
            },
            'companyInfoAdd button[action=save]': {
                click: this.save
            },
            'companyInfoAdd button[action=cancel]': {
                click: this.cancel
            },
            'companyInfoMain': {
                itemdblclick: this.gridDBClickListener
            },
            'textfield[name=keyword]': {
                specialkey: this.searchTxListener
            },
            'textfield[name=date]': {
                render: function (e) {
                    e.getEl().on('click', function () {
                        WdatePicker({dateFmt: 'yyyy'});
                    });
                }
            }
        });
        store = this.getCompanyInfoStore();
        //Get my store and add listener 
        this.getCompanyInfoStore().on('beforeload', function (store, operation, eOpts) {
            var keyword = encodeURIComponent(Ext.getCmp('keyword').getValue());
            Ext.apply(this.proxy.extraParams, {
                'keyword': keyword
            });
        });
    },
    refresh: function (btn) {
        var store = this.getCompanyInfoStore();
        store.load({params:{start:0}});
    },
    add: function (btn) {
        father = null;
        id = null;
        this.createAddWin('add');
        parentnodeid = null;
        currentnodeid = null;
    },
    edit: function (vid, record) {
        id = vid;
        var win = this.createAddWin('edit');
        old = record.data.companyid;
        node = null;
        win.down('companyInfoAdd').getForm().loadRecord(record);
    },
    createAddWin: function (flag) {
        var iconCls = '';
        var title = '';
        if (flag == 'add') {
            iconCls = 'Add';
            title = '新增'
        } else if (flag == 'edit') {
            iconCls = 'Edit';
            title = '修改'
        }
        var form = Ext.widget('companyInfoAdd');
        var window = Ext.create('Ext.window.Window', {
            title: title, iconCls: iconCls, height: pageHeight - 100,
            frame: true, border: 0, autoScroll: true, width: pageWidth - 400, minWidth: 350,
            scroll: true, modal: true, layout: 'fit', items: form
        });
        window.show();
        return window;
    },
    search: function (btn) {
        this.refresh();
    },
    searchTxListener: function (textfield, e) {
        if (e.getKey() == Ext.EventObject.ENTER) {
            this.search();
        }
    },
    save: function (btn) {
        var form = btn.up('form').getForm();
        if (form.isValid()) {
            mask.show();
            // Submit the Ajax request and handle the response
            form.submit({
                params: {father: father, id: id},
                success: function (form, action) {
                    mask.hide();
                    old = '';
                    parent.window.showMessage(2, '保存成功！手动刷新页面更新主页面单位名称！', 2, '提示');
                    btn.up('window').close();
                    store.load();
                },
                failure: function (form, action) {
                    mask.hide();
                    Ext.Msg.alert('失败了', action.result ? action.result.message : '服务器未响应');
                }
            });
        }
    },
    cancel: function (btn) {
        btn.up('form').up('window').close();
    },
    gridDBClickListener: function (tree, record, item, index, e, eOpts) {
        this.edit(record.data.companyid, record);
    }
});
