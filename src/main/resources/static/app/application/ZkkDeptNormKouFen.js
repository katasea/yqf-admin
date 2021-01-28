var mask = null;
Ext.application({
			name : "MyApp",
			appFolder : 'app',
			autoCreateViewport:false,
			controllers : ["ZkkDeptNormKouFen"],
			launch : function() {
				Ext.BLANK_IMAGE_URL = blank_img_url;
				// 页面加载完成之后执行
				Ext.create('Ext.container.Viewport', {
					layout : "border",
                    id: 'vpcViewport',
                    border: '0 0 0 0',
                    //自定义方块背景
                    cls: "bgSquare",
                    border: false,
                    items : [{
                        xtype : "deptNormKouFenTree",
                        width : 250,
                        region:'west'
                    },{
                        xtype : "zkkDeptNormKouFenMain",
                        // iconCls:"Report",
                        // title:'详细信息列表',
                        dockedItems:'',
                        border:0,
                        region:'center'
                    }]
				});
				mask = new Ext.LoadMask( Ext.getBody(),{msg:"请稍候片刻..."});
			}
		});