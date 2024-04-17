<template>
	<view>
		1.目录结构
		1.1 common/unieapConfig.js //配置全局变量
		1.2 main.js //把配置的全局变量注册到Vue属性中
		1.3 pages/unieap/projectxxx/business   //项目业务页面
		1.4 配置appid  
		a)https://mp.weixin.qq.com/wxamp/devprofile/get_profile?token=748692350&lang=zh_CN  开发管理->开发设置
		b)后台程序配置文件 application-dev.yml  application-prod.yml  
		c)微信开发者工具 详情->基本信息
		这3个地方的appid secret 必须保持一致
		2. pages.json 菜单配置列表
	</view>
</template>

<script>
	export default {
		data() {
			return {
				
			}
		},
		methods: {
			
		}
	}
</script>

<style>

</style>
