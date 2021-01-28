var required = '<span style="color:red;font-weight:bold" data-qtip="必填">*</span>';
Ext.define('MyApp.view.userInfo.Add', {
    extend: 'Ext.form.Panel',
    xtype: 'userInfoAdd',
    autoScroll: true,
    requires: [
        '*',
        'Ext.ux.DataTip'
    ],
    viewConfig: {loadMask: {msg: "请稍后..."}},
    frame: false, bodyPadding: '10 10 10 10', border: 0,
    url: contextPath + '/userInfo/save',
    fieldDefaults: {labelWidth: 75},
    plugins: {ptype: 'datatip'},
    defaultType: 'textfield',
    items: [
        {
            xtype: 'fieldset',
            title: '人员基本信息',
            layout: 'column',
            autoHeight: true,
            autoScroll: false,
            defaults: {
                width: 250
            },
            items: [
                {
                    layout: 'form', flex: 1, border: 0, margin: '0 15 0 0', items: [
                        {
                            fieldLabel: '用户账号', xtype: 'textfield',
                            id: 'userid', name: 'userid',
                            validator: function (value) {
                                var isOk = false;
                                var msg = "";
                                if (value != old) {
                                    Ext.Ajax.request({
                                        waitMsg: '正在校验用户账号是否已经存在,请稍后...',
                                        url: contextPath + "/userInfo/validator",
                                        params: {'value': value, 'keyid': 'userid'},//
                                        method: "POST", timeout: 40000, async: false,
                                        success: function (response, opts) {
                                            var resObj = Ext.decode(response.responseText);
                                            if (resObj.success) {
                                                isOk = true;
                                            } else {
                                                isOk = false;
                                                msg = resObj.msg;
                                            }
                                        }, failure: function (response, opts) {
                                            isOk = false;
                                            msg = "错误";
                                        }
                                    });
                                } else {
                                    isOk = true;
                                }
                                if (isOk) {
                                    return true;
                                } else {
                                    return msg;
                                }
                            },
                            allowBlank: false, afterLabelTextTpl: required, tooltip: '请输入【用户账号】'
                        },
                        {
                            fieldLabel: '联系电话', xtype: 'textfield',
                            id: 'phone', name: 'phone',
                            allowBlank: false, afterLabelTextTpl: required, tooltip: '请输入【联系电话】'
                        },
                        {
                            fieldLabel: '邮件地址', xtype: 'textfield',
                            id: 'email', name: 'email',
                            allowBlank: false, afterLabelTextTpl: required, tooltip: '请输入【邮件地址】'
                        },
                        {
                            fieldLabel: '用户名称', xtype: 'textfield',
                            id: 'username', name: 'username',
                            allowBlank: false, afterLabelTextTpl: required, tooltip: '请输入【用户名称】'
                        }, {
                            fieldLabel: '身份证号', xtype: 'textfield',
                            id: 'idcard', name: 'idcard',
                            tooltip: '请输入【身份证号】'
                        },
                        {
                            fieldLabel: '锁定状态', xtype: 'radiogroup',
                            items: [
                                {inputValue: '1', boxLabel: '启用',anchor: '33%',name: 'enable',checked: true},
                                {inputValue: '2', anchor: '33%', name: 'enable', boxLabel: '锁定'},
                                {inputValue: '3', anchor: '33%', name: 'enable', boxLabel: '停用'}
                                ]
                        }]
                },
                {
                    layout: 'form', flex: 1, border: 0, margin: '0 15 0 0', items: [
                        {
                            xtype: 'comboGridBox',
                            multiSelect: false,
                            id: 'deptid',
                            name: 'deptid',
                            displayField: 'deptname',
                            valueField: 'deptid',
                            store: 'DeptInfoGrid',
                            emptyText: '选择所属科室',
                            pickerAlign: 'bl',
                            gridCfg: {
                                width: 450,
                                height: 300,
                                columns: [
                                    {
                                        text: '科室编号',
                                        flex: 1,
                                        sortable: true,
                                        dataIndex: 'deptid'
                                    },
                                    {
                                        text: '科室名称',
                                        flex: 1,
                                        sortable: true,
                                        dataIndex: 'deptname'
                                    },
                                    {
                                        text: '科室类型',
                                        flex: 1,
                                        dataIndex: 'depttype',
                                        align: 'left'
                                    }, {
                                        text: '科室状态',
                                        flex: 1,
                                        dataIndex: 'isstop',
                                        renderer: function (v) {
                                            if (v == '1') {
                                                return '已停用';
                                            } else {
                                                return '启用中';
                                            }
                                        },
                                        align: 'center'
                                    }
                                ]
                            }
                        }, {
                            fieldLabel: '生日日期', xtype: 'textfield',
                            id: 'birthday', name: 'birthday',
                            allowBlank: false, afterLabelTextTpl: required, tooltip: '请输入【生日日期】',
                            listeners: {
                                render: function (obj, e) {
                                    obj.getEl().on('click', function () {
                                        WdatePicker({dateFmt: 'yyyy-MM-dd'});
                                    });
                                }
                            }
                        },
                        {
                            fieldLabel: '入职时间', xtype: 'textfield',
                            id: 'entrytime', name: 'entrytime',
                            allowBlank: false, afterLabelTextTpl: required,
                            listeners: {
                                render: function (obj, e) {
                                    obj.getEl().on('click', function () {
                                        WdatePicker({dateFmt: 'yyyy-MM-dd'});
                                    });
                                }
                            }
                        },
                        {
                            xtype: "combobox",
                            name: "sex",
                            id: "sex",
                            fieldLabel: '人员性别',
                            store: {
                                extend: "Ext.data.Store",
                                fields: ["dicval", "dickey"],
                                autoLoad: true,
                                proxy: {
                                    type: "ajax",
                                    actionMethods: {read: "POST"},
                                    extraParams: {type: 2},
                                    url: contextPath + "/basDicInfo/getComboJson",
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
                            name: "userstyle",
                            id: "userstyle",
                            fieldLabel: '人员类型',
                            store: {
                                extend: "Ext.data.Store",
                                fields: ["dicval", "dickey"],
                                autoLoad: true,
                                proxy: {
                                    type: "ajax",
                                    actionMethods: {read: "POST"},
                                    extraParams: {type: 4},
                                    url: contextPath + "/basDicInfo/getComboJson",
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
                            name: "userstatus",
                            id: "userstatus",
                            fieldLabel: '人员状态',
                            store: {
                                extend: "Ext.data.Store",
                                fields: ["dicval", "dickey"],
                                autoLoad: true,
                                proxy: {
                                    type: "ajax",
                                    actionMethods: {read: "POST"},
                                    extraParams: {type: 5},
                                    url: contextPath + "/basDicInfo/getComboJson",
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
                        }
                    ]
                }
            ]

        },
        {
            fieldLabel: '权限控制', xtype: 'tabpanel', activeTab: 0,
            height: 300,
            items: [
                {
                    title: '关联岗位',
                    html: "<div id=\"postZtreeV3\" class=\"ztree\"></div>",
                    closable: false, layout: 'fit',
                    listeners: {
                        'afterrender': function () {
                            //嵌入ztree显示
                            initSetting(1);
                        }
                    }
                },
                {
                    title: '关联角色',
                    html: "<div id=\"roleZtreeV3\" class=\"ztree\"></div>",
                    closable: false, layout: 'fit',
                    listeners: {
                        'afterrender': function () {
                            //嵌入ztree显示
                            initSetting(2);
                        }
                    }

                },
                {
                    title: '关联资源',
                    html: "<div id=\"resZtreeV3\" class=\"ztree\"></div>",
                    closable: false, layout: 'fit',
                    listeners: {
                        'afterrender': function () {
                            //嵌入ztree显示
                            initSetting(3);
                        }
                    }
                }
            ]
        }],
    buttons: [{text: '保存', action: 'save'}, {text: '取消', action: 'cancel'}],
    initComponent: function () {
        this.callParent();
    }
});


function initSetting(flag) {
    var settings;
    var divid;
    if (flag === 1) {
        //岗位
        settings = {
            check: {
                enable: true
            },
            data: {
                simpleData: {
                    enable: true
                }
            },
            async: {
                enable: true,
                url: contextPath + "/basDicInfo/getDicZTree",
                //autoParam:["id", "name=n", "level=lv"],
                otherParam: {"userpkid": id, type: '3'},
                dataFilter: filter
            }
        };
        divid = "#postZtreeV3";
    } else if (flag === 2) {
        //角色
        settings = {
            check: {
                enable: true
            },
            data: {
                simpleData: {
                    enable: true
                }
            },
            async: {
                enable: true,
                url: contextPath + "/roleInfo/getRoleZTree",
                otherParam: {"userpkid": id},
                dataFilter: filter
            }
        };
        divid = "#roleZtreeV3";
    } else if (flag === 3) {
        //资源
        settings = {
            check: {
                enable: true
            },
            data: {
                simpleData: {
                    enable: true
                }
            },
            async: {
                enable: true,
                url: contextPath + "/resourcesInfo/getResZTree",
                otherParam: {"userpkid": id},
                dataFilter: filter
            }
        };
        divid = "#resZtreeV3";
    }
    initTree(settings, divid);
}

/**
 * ztree code
 */
function initTree(settings, divid) {
    $.fn.zTree.init($(divid), settings);
}

function filter(treeId, parentNode, childNodes) {
    if (!childNodes) return null;
    for (var i = 0, l = childNodes.length; i < l; i++) {
        childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
    }
    return childNodes;
}