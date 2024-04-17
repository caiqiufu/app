<script>
	export default {
		data() {
			return {}
		},
		onLoad() {
			this.init()
 
		},
		methods: {
			//初始化
			init() {
				// #ifdef APP-PLUS
				this.checkVersion()
				// #endif
			},
			//检查版本更新情况
			checkVersion() {
				//模拟接口获取最新版本号，版本号固定为整数
				setTimeout(() => {
					const newVersionName = "V1.2.0" //线上最新版本名
					const newVersionCode = 20; //线上最新版本号
					const selfVersionCode = Number(uni.getSystemInfoSync().appVersionCode) //当前App版本号
 
					//线上版本号高于当前，进行在线升级
					if (selfVersionCode < newVersionCode) {
						let platform = uni.getSystemInfoSync().platform //手机平台
						//安卓手机弹窗升级
						if (platform === 'android') {
							uni.navigateTo({
								url: './upgrade'
							})
						}
						//IOS无法在线升级提示到商店下载
						else {
							uni.showModal({
								title: '发现新版本 ' + newVersionName,
								content: '请到App store进行升级',
								showCancel: false
							})
						}
					}
 
 
				}, 200)
			}
		}
	}
</script>