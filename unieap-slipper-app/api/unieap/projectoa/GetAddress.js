export default async function wxGetLocation() {
	try {
		//小程序获取地理位置
		let result = await getLocationApi({})
		return result
	} catch (err) {
		let scope = 'scope.userLocation'
		let setting = await wxGetSetting() //获取用户的当前设置
		if (!setting[scope]) {
			let result = await showModalApi({
				content: '您拒绝了定位权限，将无法正常使用此功能',
				confirmText: '去开启'
			})
			if (result.confirm) {
				let auth = await wxOpenSetting()
				if (auth.authSetting[scope]) {
					let result = await getLocationApi({})
					return result
				}
				showToastApi({
					title: '您拒绝了定位权限'
				})
				return false
			}
			return false
		}
	}
}