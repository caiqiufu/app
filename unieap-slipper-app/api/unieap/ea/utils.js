// utils.js
export function sayHello(name) {
    return `Hello, ${name}!`;
}

/**
 * 跳转到登陆界面
 */
export function goToLogin() {
	uni.navigateTo({
		//保留当前页面，跳转到应用内的某个页面
		url:'/pages/unieap/ea/me/login/index'
	})   
}