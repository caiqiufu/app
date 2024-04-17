//定位授权
export function getLocation() {
	let that = this;
	// 1、判断手机定位服务【GPS】 是否授权
	uni.getSystemInfo({
		success(res) {
			console.log("判断手机定位服务是否授权:", res);
			let locationEnabled = res.locationEnabled; //判断手机定位服务是否开启
			let locationAuthorized = res.locationAuthorized; //判断定位服务是否允许微信授权
			if (locationEnabled == false || locationAuthorized == false) {
				//手机定位服务（GPS）未授权
				uni.showToast({
					title: "请打开手机GPS",
					icon: "none",
				});
			} else {
				//手机定位服务（GPS）已授权
				// 2、判断微信小程序是否授权位置信息
				// 微信小程序已授权位置信息
				uni.authorize({
					//授权请求窗口
					scope: "scope.userLocation", //授权的类型
					success: (res) => {
						that.fnGetlocation();
					},
					fail: (err) => {
						err = err["errMsg"];
						uni.showModal({
								content: "需要授权位置信息",
								confirmText: "确认授权",
							})
							.then((res) => {
								console.log(res);
								if (res[1]["confirm"]) {
									uni.openSetting({
										success: (res) => {
											if (res.authSetting[
													"scope.userLocation"]) {
												// 授权成功
												uni.showToast({
													title: "授权成功",
													icon: "none",
												});
												that.fnGetlocation();
											} else {
												// 未授权
												uni.showToast({
													title: "授权失败,请重新授权",
													icon: "none",
												});
												uni.showModal({
													title: "授权",
													content: "获取授权" +
														authouName +
														"失败,是否前往授权设置？",
													success: function(
														result) {
														if (result
															.confirm) {
															uni
																.openSetting();
														}
													},
													fail: function() {
														uni.showToast({
															title: "系统错误！",
															icon: "none",
														});
													},
												});
											}
										},
									});
								}
								if (res[1]["cancel"]) {
									// 取消授权
									uni.showToast({
										title: "你拒绝了授权，无法获得周边信息",
										icon: "none",
									});
								}
							});
					},
					complete(res) {
						// console.log('授权弹框', res);
						if (res.errMsg == "authorize:ok") {
							that.fnGetlocation();
						} else {
							uni.showModal({
								title: "授权",
								content: "获取授权" + authouName + "失败,是否前往授权设置？",
								success: function(result) {
									if (result.confirm) {
										uni.openSetting();
									}
								},
								fail: function() {
									uni.showToast({
										title: "系统错误！",
										icon: "none",
									});
								},
							});
						}
					},
				});
			}
		},
	});
}
//定位获取
export function fnGetlocation() {
	let that = this;
	uni.getLocation({
		type: "wgs84", //默认为 wgs84 返回 gps 坐标
		geocode: "true",
		isHighAccuracy: "true",
		accuracy: "best", // 精度值为20m
		success: function(res) {
			console.log("定位获取:", res);
			return res;
		},
		fail(err) {
			if (
				err.errMsg ===
				"getLocation:fail 频繁调用会增加电量损耗，可考虑使用 wx.onLocationChange 监听地理位置变化"
			) {
				uni.showToast({
					title: "请勿频繁定位",
					icon: "none",
				});
			}
			if (err.errMsg === "getLocation:fail auth deny") {
				// 未授权
				uni.showToast({
					title: "无法定位，请重新获取位置信息",
					icon: "none",
				});
				authDenyCb && authDenyCb();
				that.isLocated = false;
			}
			if (
				err.errMsg ===
				"getLocation:fail:ERROR_NOCELL&WIFI_LOCATIONSWITCHOFF"
			) {
				uni.showModal({
					content: "请开启手机定位服务",
					showCancel: false,
				});
			}
			return err;
		},
	});
}