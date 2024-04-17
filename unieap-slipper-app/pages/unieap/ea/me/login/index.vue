<template>
	<view class="container">
		<view class="example">
			<uni-forms ref="baseLoginForm" v-show="showLoginForm" :modelValue="loginFormData" :rules="rulesLogin" labelWidth="80px" label-position="left">
				<uni-forms-item :label="$t('ea.login.field.userCode')" name="userCode">
					<uni-easyinput v-model="loginFormData.userCode" :placeholder="$t('ea.login.field.phoneOrEmail')" />
				</uni-forms-item>
				<text class="bold-text">
					{{ $t('ea.login.field.passwordOr') }}					
				</text>				
				<uni-forms-item :label="$t('ea.login.field.password')" name="password">
					<uni-easyinput v-model="loginFormData.password" :placeholder="$t('ea.login.field.password')" />
					{{ $t('ea.login.field.password.tipInfo') }}
				</uni-forms-item>
				<uni-forms-item :label="$t('ea.login.field.verifyCode')" name="verifyCode">
					<view class="grid-container">
						<uni-easyinput v-model="loginFormData.verifyCode" :placeholder="$t('ea.login.field.verifyCode')" />
						<button @click="sendVerifyCode" :disabled="disableVerifyCodeButton" size="mini" class="button">{{ buttonText }}</button>
				    </view>				
				</uni-forms-item>
				<uni-forms-item>
					<text class="bold-text-error">
						{{ passwordOrVerifyErrorMsg }}					
					</text>	
				</uni-forms-item>	
				<uni-forms-item>
					<checkbox-group @change="checkboxChange">
						<checkbox value="Y" :checked="agreenmentFlag"/>
						已阅读并同意
						<text class="link" @click="openLink('useagreement')">
						      《用户服务协议》
						</text>
						和
						<text class="link" @click="openLink('privacyagreement')">
						      《隐私政策》
						</text>
					</checkbox-group>
					
				</uni-forms-item>				
				<button type="primary" @click="submitLogin('baseLoginForm')">{{$t('ea.login.button.login')}}</button>
			</uni-forms>			
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				agreenmentFlag: false,
				loginFormData: {
					userCode: '',
					password:'',
					verifyCode: ''
				},
				passwordOrVerifyErrorMsg:'',
				showLoginForm:true,
				showRegistrationForm:false,
				disableVerifyCodeButton:false,
				countdown: 60,
				countdownTimer: null,
				buttonText :this.$t('ea.login.button.sendVerifyCode'),
				// 分段器数据
				current: 0,
				items: [this.$t('ea.login.menu.login'), this.$t('ea.login.menu.registration')],
				rulesLogin: {
					userCode: {rules:[
						{ required: true,errorMessage: this.$t('ea.login.field.userCode.errorMsgNull') }, 
						{
							validateFunction: function(rule, value, data, callback) {
								/// 判断是否是手机号
								const regPhoneNumber = /^1[0-9]{10}$/;
								const isPhoneNumber = regPhoneNumber.test(value);
								// 判断是否是邮箱地址
								const regEmail = /^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/;
								const isEmail = regEmail.test(value);
											
								if (isPhoneNumber || isEmail) {
									return true
								} else {
									callback('邮箱或者电话号码格式不正确')
								}
							}
						}
					]},
					password: {
						rules: [{
							required: false,
							errorMessage: this.$t('ea.login.field.verifyCode.errorMsg')
						}]
					},
					verifyCode: {
						rules: [{
							required: false,
							errorMessage: this.$t('ea.login.field.verifyCode.errorMsg')
						}]
					}
				}
			}
		},
		computed: {

		},
		onLoad() {},
		onReady() {
			// 设置自定义表单校验规则，必须在节点渲染完毕后执行
			//this.$refs.customForm.setRules(this.customRules)
		},
		methods: {
			openLink(url) {
			      // 使用uni.navigateTo或uni.redirectTo等API来打开新页面
			      uni.navigateTo({
			        url: '/pages/unieap/ea/me/login/' + url
			    });
			},
			checkboxChange(e) {
				this.agreenmentFlag = !this.agreenmentFlag;
			},
			sendVerifyCode(){
				this.disableVerifyCodeButton = true;
				let value = this.loginFormData.userCode;
				if(value == null || value ==''){
					uni.showToast({
						title: this.$t('ea.login.field.phoneOrEmail.errorMsgNull'),
						duration: 3000
					});
					this.disableVerifyCodeButton = false;
					return;
				}else{
					/// 判断是否是手机号
					const regPhoneNumber = /^1[0-9]{10}$/;
					const isPhoneNumber = regPhoneNumber.test(value);
					// 判断是否是邮箱地址
					const regEmail = /^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/;
					const isEmail = regEmail.test(value);
								
					if (isPhoneNumber || isEmail) {
						uni.request({
							url: this.$baseUrl+'/backstage/ea/login/createVerifyCode',
							data: {
								token: uni.getStorageSync("token"),
								userCode:this.loginFormData.userCode,
								verifyType: "LOGIN"
							},
							success: (res) => {
								uni.showToast({
									title: this.$t('common.operation.send.success'),
									duration: 2000
								});
							},
							fail: (e) => {
								console.log(e)
							}
						})
						// 修改按钮文本和开始倒计时
						this.buttonText = `${this.countdown} s`;
						this.countdownTimer = setInterval(() => {
							this.countdown--;
							this.buttonText = `${this.countdown} s`;
							if (this.countdown <= 0) {
							  // 倒计时结束，允许按钮再次点击
							  clearInterval(this.countdownTimer);
							  this.disableVerifyCodeButton = false;
							  this.buttonText = this.$t('ea.login.button.sendVerifyCode');
							  this.countdown = 60;
							}
						  }, 1000);
					} else {
						uni.showToast({
							title: this.$t('ea.login.field.userCode.errorMsgFormat'),
							duration: 3000
						});
						this.disableVerifyCodeButton = false;
						return;
					}
				}				
			},
			submitLogin(ref) {
				if(!this.agreenmentFlag){
					uni.showModal({
						title: this.$t('common.operation.tips'),
						content:"请先阅读并勾选同意后再登陆或注册",
						//cancelText: "取消", // 取消按钮的文字  
						confirmText: this.$t('common.operation.confirm'),
						showCancel: false, // 是否显示取消按钮，默认为 true
						success: (res) => {
						if(res.confirm) {  
							return;
						} else {  
							return;
							}  
						} 
					})
				}else{
					this.$refs[ref].validate().then(res => {
						if(this.loginFormData.password == '' && this.loginFormData.verifyCode == ''){
							this.passwordOrVerifyErrorMsg = this.$t('ea.login.field.passwordOrVerify');
						}else{
							this.passwordOrVerifyErrorMsg = '';
							uni.request({
								url: this.$baseUrl+'/backstage/ea/login/login',
								data: {
									token: uni.getStorageSync("token"),
									userCode:this.loginFormData.userCode,
									password:this.loginFormData.password,
									verifyCode:this.loginFormData.verifyCode,
									verifyType: "LOGIN"
								},
								success: (res) => {
									const result = res.data.data; 
									let resultMsg = "";
									if(result.length > 1){
										resultMsg = this.$t('ea.login.process.loginSuccess');
										uni.setStorageSync('userCode',result);
										// 返回上一页
										uni.navigateBack({
										  delta: 1 // 表示返回到上一页，若有多个页面可根据实际情况设置delta值
										});
										//uni.navigateTo({
										//	//保留当前页面，跳转到应用内的某个页面
										//	url:'/pages/unieap/ea/me/myinfo'
										//})
									}else{
										resultMsg = this.$t('ea.login.field.errorMsg');
									}									
									if(result == 1 || result == 2){
										resultMsg = this.$t('ea.login.process.loginVerifycodeIncorrect');
									}
									if(result == 3){
										resultMsg = this.$t('ea.login.process.loginVerifycodeExpired');
									}
									if(result == 4){
										resultMsg = this.$t('ea.login.process.loginVerifycodeUsed');
									}
									if(result == 5){
										resultMsg = this.$t('ea.login.field.password.errorTips');
									}
									uni.showToast({
										title: resultMsg,
										duration: 6000
									});
								},
								fail: (e) => {
									console.log(e)
								}
							})
						}					
					}).catch(err => {
						console.log('err', err);
					})					
				}											
			},
			beforeDestroy() {
			    // 在组件销毁前清除计时器
			    clearInterval(this.countdownTimer);
			},
		}
	}
</script>

<style lang="scss">
	.example {
		padding: 15px;
		background-color: #fff;
	}

	.segmented-control {
		margin-bottom: 15px;
		text-align: center;
	}

	.button-group {
		margin-top: 15px;
		display: flex;
		justify-content: space-around;
	}

	.form-item {
		display: flex;
		align-items: center;
		flex: 1;
	}

	.button {
		display: flex;
		align-items: center;
		height: 35px;
		line-height: 35px;
		margin-left: 10px;
	}
	/* Grid 布局样式 */
	.grid-container {
	  display: grid;
	  grid-template-columns: 1fr auto; /* 两列布局，第一列占据剩余空间，第二列根据内容宽度 */
	  grid-column-gap: 10px; /* 设置列之间的间隙 */
	}
	
	.grid-item {
	  width: 100%; /* 输入框宽度占据剩余空间 */
	}
	
	.bold-text {
	  font-weight: bold;
	  font-size: 13px;
	}
	.bold-text-error {
	  font-weight: bold;
	  font-size: 13px;
	  color: red;
	}
	.link {
	  color: blue;
	  text-decoration: underline;
	}
</style>
