<template>
	<view>
		<button type="primary" plain="true" @click="helloHandle">按钮</button>
	</view>
</template>

<script>
	export default {
		data() {
			return {}
		},
		methods: {
			helloHandle() {
				console.log('hello world')
				uni.login({
					provider: 'weixin',
					success:function(res){
						console.log(res)
					},
					fail: function(error) {
						console.log(error)
					}
				})
			}
		}
	}
</script>

<style>

</style>
