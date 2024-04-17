export default function() {
	// #ifdef APP-PLUS
	return new Promise((resolve, reject) => {
		plus.runtime.getProperty(plus.runtime.appid, function(widgetInfo) {
			const data = {
				action: 'checkVersion',
				appid: plus.runtime.appid,
				appVersion: plus.runtime.version,
				wgtVersion: widgetInfo.version
			}
			console.log("data: ",data);
			//const myCloud = uniCloud.init({
			//  provider: 'aliyun',
			//  spaceId: 'mp-a78200fa-0268-4cbb-804a-f581c33ecb4f',
			//  clientSecret: 'wbm5gpwrS6w37dq6qGFlSw=='
			//});
			uniCloud.callFunction({
			//myCloud.callFunction({
				name: 'uni-upgrade-center',
				data,
				success: (e) => {
					console.log("e: ", e);
					resolve(e)
				},
				fail: (error) => {
					reject(error)
				}
			})
		})
	})
	// #endif
	// #ifndef APP-PLUS
	return new Promise((resolve, reject) => {
		reject({
			message: '请在App中使用'
		})
	})
	// #endif
}
