<template>
	<view class="container">
		<uni-section :title="$t('ea.myinfo.text.myInfo')" type="line">
			<view class="uni-container">
				<uni-forms ref="baseForm" :model="baseFormData" labelWidth="60px">
					<uni-forms-item :label="$t('ea.user.field.userCode')">
						<uni-easyinput v-model="baseFormData.userCode" :disabled="userInfoReadOnly"/>
					</uni-forms-item>				
					<uni-forms-item :label="$t('ea.user.field.phoneNumber')">
						<view class="grid-container">
							<uni-easyinput v-model="baseFormData.phoneNumber" />
							<button @click="handleClickChangePhoneNumber" size="mini" class="button" >{{$t('common.operation.save')}}</button>
						</view>							
					</uni-forms-item>
					<uni-forms-item :label="$t('ea.user.field.email')" >							
						<view class="grid-container">
							<uni-easyinput v-model="baseFormData.email"/>
							<button @click="handleClickChangeEmail" size="mini" class="button" >{{$t('common.operation.save')}}</button>
						</view>
					</uni-forms-item>
					<uni-forms-item :label="$t('ea.user.field.createDate')">
						<uni-easyinput v-model="baseFormData.createDate" :disabled="userInfoReadOnly"/>
					</uni-forms-item>
					<uni-forms-item :label="$t('ea.user.field.agentCode')">
						<uni-easyinput v-model="baseFormData.agentCode" :disabled="userInfoReadOnly"/>
					</uni-forms-item>
				</uni-forms>
			</view>
		</uni-section>
		<uni-section :title="$t('ea.myinfo.text.accountInfo')" type="line">
			<view>
				<button @click="openAddAccountWindow" size="mini" type="primary">{{$t('ea.account.text.addAccount')}}</button>	
			</view>
			<view>
				<uni-list :border="true" v-for="(accountInfo,index) in baseFormData.accountList" :key="index">
					<uni-card :title="$t('ea.myinfo.text.accountInfo')+(index + 1)">
						<uni-forms labelWidth="50px">
							<uni-forms-item :label="$t('ea.account.field.accountCode')" :key="'accountCode_' + accountInfo.id" >
								<uni-easyinput v-model="accountInfo.accountCode" disabled/>
							</uni-forms-item>
							<uni-forms-item :label="$t('ea.account.field.brokerName')" :key="'brokerName_' + accountInfo.id" >
								<uni-easyinput v-model="accountInfo.brokerName" disabled/>
							</uni-forms-item>
							<uni-forms-item :label="$t('ea.account.field.password')" :key="'password_' + accountInfo.password" >
								<view class="grid-container">
									<uni-easyinput v-model="accountInfo.password"/>
									<button @click="handleClickChangePassword(accountInfo)" size="mini" class="button" >{{$t('common.operation.save')}}</button>
								</view>	
							</uni-forms-item>
							<uni-forms-item :label="$t('ea.account.field.trialDuration')" :key="'trialDuration_' + accountInfo.id" >
								<uni-easyinput v-model="accountInfo.trialDuration" disabled/>
							</uni-forms-item>
							<uni-forms-item :label="$t('ea.account.field.activateFlag')" :key="'activateFlag_' + accountInfo.id" >
								<view class="grid-container">
									<uni-data-checkbox v-model="accountInfo.activateFlag" :localdata="optionsActivateFlagList" />
									<button @click="handleClickChangeActivateFlag(accountInfo)" size="mini" class="button">{{$t('common.operation.save')}}</button>				
								</view>	
							</uni-forms-item>
							<uni-forms-item :label="$t('ea.account.field.eaType')">
								<view class="grid-container">
									<uni-data-checkbox v-model="accountInfo.eaType" :localdata="eaTypeList"></uni-data-checkbox>
									<button @click="handleClickChangeEaType(accountInfo)" size="mini" class="button">{{$t('common.operation.save')}}</button>
								</view>	
								<view v-if="isShowH">
									<text class="bold-text" style="color: red;">H(策略1)策略盈亏较小，交易订单数量较多，D(策略2)策略盈亏较大，交易订单数量较少</text>
								</view>
							</uni-forms-item>
							<uni-forms-item :label="$t('ea.account.field.subscribeFlag')" :key="'subscribeFlag_' + accountInfo.id" >
								<view>
									<text class="bold-text" style="color: red;">提醒：托管功能订阅后，隔天才能生效</text>
								</view>
								<view class="grid-container">
									<uni-data-checkbox v-model="accountInfo.subscribeFlag" :localdata="optionsSubscribeFlagList" />
									<button @click="handleClickChangeSubscribeFlag(accountInfo)" size="mini" class="button">{{$t('common.operation.save')}}</button>				
								</view>					
							</uni-forms-item>
							<uni-forms-item :label="$t('ea.account.field.valumn')" :key="'valumn_' + accountInfo.id" >
								<view class="grid-container">
									<uni-easyinput v-model="accountInfo.valumn" type="number"/>
									<button @click="handleClickChangeValumn(accountInfo)" size="mini" class="button" v-if="accountInfo.subscribeFlag!='S0' && accountInfo.subscribeFlag!='S1'">{{$t('common.operation.save')}}</button>
								</view>	
							</uni-forms-item>
							<!--
							<uni-forms-item :label="$t('ea.account.field.autoFlag')" :key="'autoFlag_' + accountInfo.id" >
								<view class="grid-container">
									<uni-data-select v-model="accountInfo.autoFlag" :localdata="optionsAutoFlagList">
									</uni-data-select>
									<button @click="handleClickChangeAutoFlag(accountInfo)" size="mini" class="button">{{$t('common.operation.modify')}}</button>
								</view>
							</uni-forms-item>
							-->								
						</uni-forms>
					</uni-card>								
				</uni-list>
			</view>
		</uni-section>	 
	</view>
	<uni-popup ref="popup" background-color="#fff">
		<view class="popup-content">
			{{subscribeFlagChangeTips}}
		</view>
	</uni-popup>
	<!-- 普通弹窗 -->
	<uni-popup ref="popup2" background-color="#fff">
		<view class="popup-content">
			<uni-section :title="$t('ea.account.text.addAccount')" type="line">
				<uni-group>
					<view class="uni-container">
						<uni-forms ref="accountForm" :rules="addAccountRules" :model="accountFormData" labelWidth="80px">
							<uni-forms-item :label="$t('ea.account.field.brokerName')">
								<uni-data-select v-model="accountFormData.brokerCode" :localdata="optionsBrokerList">
								</uni-data-select>
							</uni-forms-item>
							<uni-forms-item :label="$t('ea.account.field.accountType')">
								<uni-data-checkbox v-model="accountFormData.accountType" :localdata="accountTypeList" :value="0"></uni-data-checkbox>
							</uni-forms-item>							
							<uni-forms-item :label="$t('ea.account.field.accountCode')" name="accountCode">
								<uni-easyinput v-model="accountFormData.accountCode" />
							</uni-forms-item>
							<uni-forms-item :label="$t('ea.account.field.password')" name="password">
								<uni-easyinput v-model="accountFormData.password"  />
								{{ $t('ea.account.field.passwordTips') }}
							</uni-forms-item>
							<uni-forms-item :label="$t('ea.user.field.agentCode')" name="agentCode">
								<uni-easyinput type="number" v-model="accountFormData.agentCode"  />
								{{ $t('ea.user.field.agentCode.errorMsgNull') }}
							</uni-forms-item>
						</uni-forms>
					</view>
					<view class="uni-container">
						<button type="primary" @click="addAccount()">{{ $t('common.operation.submit') }}</button>
					</view>
				</uni-group>				
			</uni-section>												
		</view>						
	</uni-popup>
</template>

<script>	
	// some-component.vue
	import { goToLogin } from '@/api/unieap/ea/utils.js';
	export default {
		data() {
			return {
				isShowH:false,
				token: '',
				language:'',
				userCode:'',
				isLogin:true,
				userInfoReadOnly:true,	
				accountInfoReadOnly:true,	
				subscribeFlag:'',
				//订阅状态
				optionsSubscribeFlagList:[],
				optionsAutoFlagList:[],
				optionsActivateFlagList:[{text: '启用',value: 'Y'},{text: '停用',value: 'N'}],
				subscribeFlagChangeTips:'',
				accountTypeList:[{text: 'Live',value: 0},{text: 'Demo',value: 1}],
				//eaTypeList:[{text: 'H(策略2)',value: 'H'},{text: 'D(策略1)',value: 'D'}],
				eaTypeList:[{text: 'D(策略1)',value: 'D'}],
				baseFormData:{
					id:'',
					userCode:'',
					userName:'',					
					phoneNumber:'',
					weixin:'',
					email:'',
					whatsApp:'',
					address:'',
					createDate:'',
					activateFlag:'',
					activateFlagDesc:'',
					accountList:[]
				},
				optionsBrokerList:[],
				accountFormData:{
					id:'',
					accountCode:'',
					accountType:'',
					password:'',
					brokerCode:'',
					brokerName:'',
					agentCode:''
				},
				addAccountRules:{
					brokerCode: {
						rules: [{
							required: true,
							errorMessage: this.$t('ea.account.field.brokerCode.errorMsgNull')
						}]
					},
					accountCode: {
						rules: [{
							required: true,
							errorMessage: this.$t('ea.account.field.accountCode.errorMsgNull')
						}]
					},
					password: {
						rules: [{
							required: true,
							errorMessage: this.$t('ea.account.field.password.errorMsgNull')
						}]
					},
					agentCode: {
						rules: [{
							required: true,
							errorMessage: this.$t('ea.user.field.agentCode.errorMsgNull')
						}]
					}
				},
				loading: false
			}
		},
		computed: {},
		//触发时机： 页面加载时触发。
        //使用场景： 在页面加载时进行一些初始化操作，如获取页面参数、发送请求等。
		onLoad(){},
		//触发时机： 页面显示时触发。
		//使用场景： 在页面显示时进行一些操作，如刷新数据、更新页面状态等。
		onShow() {
			let userCode = uni.getStorageSync("userCode");
			if(userCode =='' || userCode == null || userCode == undefined){
				goToLogin();
			}else{
				this.initUnieapApp();	
			}		
		},
		//触发时机： 页面初次渲染完成时触发。
        //使用场景： 在页面初次渲染完成后执行一些操作，如操作 DOM、启用定时器等。
		onReady(){},
		methods: {
			initUnieapApp(){
				//每次请求都需要加载token
				this.token = uni.getStorageSync("token");
				this.language = uni.getStorageSync("language");
				this.userCode = uni.getStorageSync("userCode");
				if(this.userCode =='caiqiufu@sohu.com' || this.userCode == '15994725242')
				{
					this.isShowH = true;
					this.eaTypeList = [{text: 'H(策略2)',value: 'H'},{text: 'D(策略1)',value: 'D'}];
				}
				this.userInfoReadOnly = true;
				this.subscribeFlagDicData(null);
				this.autoFlagDicData(null);
				//this.activateFlagDicData(null);
				this.getUserInfo(null);
				this.brokerDicData(null);
			},
			subscribeFlagDicData(e){
				let para = encodeURIComponent(JSON.stringify({
					
				}))
				uni.request({
					url: this.$baseUrl+'/backstage/ea/me/getSubscribeDicDataList?params=' + para,
					data: {
						token: uni.getStorageSync("token"),
						groupCode: 'BILL_FEE_TYPE'
					},
					success: (res) => {
						this.optionsSubscribeFlagList = []
						res.data.data.forEach(item => {
							this.optionsSubscribeFlagList.push({
								value: item.dicCode,
								text: item.dicName
							})
						})
						//this.getData()
					},
					fail: (e) => {
						console.log(e)
					}
				})
			},
			autoFlagDicData(e){
				let para = encodeURIComponent(JSON.stringify({
					
				}))
				uni.request({
					url: this.$baseUrl+'/backstage/common/getLanDicData?params=' + para,
					data: {
						token: uni.getStorageSync("token"),
						groupCode: 'AUTO_FLAG'
					},
					success: (res) => {
						this.optionsAutoFlagList = []
						res.data.data.forEach(item => {
							this.optionsAutoFlagList.push({
								value: item.dicCode,
								text: item.dicName
							})
						})
						//this.getData()
					},
					fail: (e) => {
						console.log(e)
					}
				})
			},
			brokerDicData(e){
				let para = encodeURIComponent(JSON.stringify({
					
				}))
				uni.request({
					url: this.$baseUrl+'/backstage/ea/me/getBrokerDicDataList?params=' + para,
					data: {
						token: uni.getStorageSync("token"),
						groupCode: 'AUTO_FLAG'
					},
					success: (res) => {
						this.optionsBrokerList = []
						res.data.data.forEach(item => {
							this.optionsBrokerList.push({
								value: item.dicCode,
								text: item.dicName
							})
						})
						//this.getData()
					},
					fail: (e) => {
						console.log(e)
					}
				})
			},
			activateFlagDicData(e){
				let para = encodeURIComponent(JSON.stringify({
					
				}))
				uni.request({
					url: this.$baseUrl+'/backstage/common/getLanDicData?params=' + para,
					data: {
						token: uni.getStorageSync("token"),
						groupCode: 'ACTIVATE_FLAG'
					},
					success: (res) => {
						this.optionsActivateFlagList = []
						res.data.data.forEach(item => {
							this.optionsActivateFlagList.push({
								value: item.dicCode,
								text: item.dicName
							})
						})
						//this.getData()
					},
					fail: (e) => {
						console.log(e)
					}
				})
			},
			getUserInfo(){
				uni.request({
					url: this.$baseUrl+'/backstage/ea/me/getMeInfo',
					data: {
						token: this.token,
						userCode: this.userCode
					},
					success: (res) => {
						this.baseFormData = res.data.data
					},
					fail: (e) => {
						console.log(e)
					}
				})
			},
			handleSelectActivateFlag(value,accountInfo){
				let oActivateFlag = accountInfo.activateFlag;
				let nActivateFlag = value;
				let tips = nActivateFlag =='Y'? this.$t('ea.account.field.activateFlag.changeActivateTips') : this.$t('ea.account.field.activateFlag.changeDeactivateTips');
				uni.showModal({
				        title: this.$t('common.operation.tips'),
						cancelText:this.$t('common.operation.cancel'),
						confirmText:this.$t('common.operation.confirm'),
				        content: tips,
				        success: function(res) {
							var that  = this; 
				        if (res.confirm) {
				            // 执行确认后的操作
							uni.request({
								url: that.$baseUrl+'/backstage/ea/me/updateActivateFlag?token=' +uni.getStorageSync("token"),
								method: 'POST',
								data: {'id':accountInfo.id,'activateFlag':nActivateFlag},
								success: (res) => {
									uni.showToast({
										title: that.$t('common.operation.modify.success'),
										duration: 2000
									});
									that.getUserInfo();
								},
								fail: (e) => {
									console.log(e)
								}
							})
				        } 
				        else {
				            // 执行取消后的操作
							that.getUserInfo();
				        }
				    }.bind(this)
				})
			},
			handleClickChangeActivateFlag(accountInfo){
				let activateFlag = accountInfo.activateFlag;
				let tips = activateFlag =='Y'? this.$t('ea.account.field.activateFlag.changeActivateTips') : this.$t('ea.account.field.activateFlag.changeDeactivateTips');
				uni.showModal({
				        title: this.$t('common.operation.tips'),
						cancelText:this.$t('common.operation.cancel'),
						confirmText:this.$t('common.operation.confirm'),
				        content: tips,
				        success: function(res) {
							var that  = this; 
				        if (res.confirm) {
				            // 执行确认后的操作
							uni.request({
								url: that.$baseUrl+'/backstage/ea/me/updateActivateFlag?token=' +uni.getStorageSync("token"),
								method: 'POST',
								data: {'id':accountInfo.id,'activateFlag':activateFlag},
								success: (res) => {
									const result = res.data.data;
									if(result == 0){
										uni.showToast({
											title: that.$t('common.operation.modify.success'),
											duration: 2000
										});
										that.getUserInfo();
									}else{
										uni.showToast({
											title: "状态未修改",
											duration: 2000
										});
									}
								},
								fail: (e) => {
									console.log(e)
								}
							})
				        } 
				        else {
				            // 执行取消后的操作
							that.getUserInfo();
				        }
				    }.bind(this)
				})
			},
			handleSelectSubscribeFlag(value,accountInfo){
				let oSubscribeFlag = accountInfo.subscribeFlag;
				let nSubscribeFlag = value;
				let tips = nSubscribeFlag =='Y'? this.$t('ea.account.field.subscribeFlag.changeSubscribeTips') : this.$t('ea.account.field.subscribeFlag.changeUnsubscribeTips');
				uni.showModal({
				        title: this.$t('common.operation.tips'),
						cancelText:this.$t('common.operation.cancel'),
						confirmText:this.$t('common.operation.confirm'),
				        content: tips,
				        success: function(res) {
							var that  = this; 
				        if (res.confirm) {
				            // 执行确认后的操作
							uni.request({
								url: that.$baseUrl+'/backstage/ea/me/updateSubscribeFlag?token=' +uni.getStorageSync("token"),
								method: 'POST',
								data: {'id':accountInfo.id,'accountCode':accountInfo.accountCode,'subscribeFlag':nSubscribeFlag},
								success: (res) => {
									uni.showToast({
										title: that.$t('common.operation.modify.success'),
										duration: 2000
									});
									that.getUserInfo();
								},
								fail: (e) => {
									console.log(e)
								}
							})
				        } 
				        else {
				            // 执行取消后的操作
							that.getUserInfo();
				        }
				    }.bind(this)
				})
			},
			handleSelectAutoFlag(value,accountInfo){
				let oAutoFlag = accountInfo.autoFlag;
				let nAutoFlag = value;
				let tips = nAutoFlag =='Y'? this.$t('ea.account.field.subscribeFlag.changeSubscribeTips') : this.$t('ea.account.field.subscribeFlag.changeUnsubscribeTips');
				let subscribeFlag = accountInfo.subscribeFlag;
				if(nAutoFlag=='Y' && subscribeFlag=='N'){
					uni.showToast({
						title: this.$t('ea.account.field.autoFlag.changeBeforeActivateTips'),
						duration: 2000
					});
					this.getUserInfo();
				}else{
					uni.showModal({
					        title: this.$t('common.operation.tips'),
							cancelText:this.$t('common.operation.cancel'),
							confirmText:this.$t('common.operation.confirm'),
					        content: tips,
					        success: function(res) {
								var that  = this; 
					        if (res.confirm) {
					            // 执行确认后的操作
								uni.request({
									url: that.$baseUrl+'/backstage/ea/me/updateAutoFlag?token=' +uni.getStorageSync("token"),
									method: 'POST',
									data: {'id':accountInfo.id,'accountCode':accountInfo.accountCode,'autoFlag':nAutoFlag},
									success: (res) => {
										uni.showToast({
											title: that.$t('common.operation.modify.success'),
											duration: 2000
										});
										that.getUserInfo();
									},
									fail: (e) => {
										console.log(e)
									}
								})
					        } 
					        else {
					            // 执行取消后的操作
								that.getUserInfo();
					        }
					    }.bind(this)
					})
				}				
			},
			handleClickChangeSubscribeFlag(accountInfo){				
				uni.showModal({
				        title: this.$t('common.operation.tips'),
						cancelText:this.$t('common.operation.cancel'),
						confirmText:this.$t('common.operation.confirm'),
				        content: this.$t('ea.account.field.subscribeFlag.changeSubscribeTips'),
				        success: function(res) {
							var that  = this; 
				        if (res.confirm) {
				            // 执行确认后的操作
							uni.request({
								url: that.$baseUrl+'/backstage/ea/me/updateSubscribeFlag?token=' +uni.getStorageSync("token"),
								method: 'POST',
								data: {'id':accountInfo.id,'accountCode':accountInfo.accountCode,'subscribeFlag':accountInfo.subscribeFlag},
								success: (res) => {
									uni.showToast({
										title: that.$t('common.operation.modify.success'),
										duration: 2000
									});
									that.getUserInfo();
								},
								fail: (e) => {
									console.log(e)
								}
							})
				        } 
				        else {
				            // 执行取消后的操作
							that.getUserInfo();
				        }
				    }.bind(this)
				})
			},
			handleClickChangeValumn(accountInfo){
				let newValumn = accountInfo.valumn;
				if(newValumn<=0){
					
					uni.showModal({
						title: this.$t('common.operation.tips'),
						content:this.$t('ea.account.field.valumn.valumnErrorTips1'),
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
					}).bind(this)
				}
				let subscribeFlag = accountInfo.subscribeFlag;
				if(subscribeFlag == 'S2' && newValumn > 1){
					uni.showModal({
						title: this.$t('common.operation.tips'),
						content:this.$t('ea.account.field.valumn.valumnErrorTips2'),
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
					}).bind(this)					
				}
				if(subscribeFlag == 'S0' || subscribeFlag == 'S1'){					
					uni.showModal({
						title: this.$t('common.operation.tips'),
						content:this.$t('ea.account.field.valumn.valumnErrorTips3'),
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
					}).bind(this)
				}
				let tips2 = this.$t('ea.account.field.valumn.valumnTips');
				uni.showModal({
				        title: this.$t('common.operation.tips'),
						cancelText:this.$t('common.operation.cancel'),
						confirmText:this.$t('common.operation.confirm'),
				        content: tips2,
				        success: function(res) {
							var that  = this; 
				        if (res.confirm) {
				            // 执行确认后的操作
							uni.request({
								url: that.$baseUrl+'/backstage/ea/me/updateValumn?token=' +uni.getStorageSync("token"),
								method: 'POST',
								data: {'id':accountInfo.id,'accountCode':accountInfo.accountCode,'valumn':newValumn},
								success: (res) => {
									uni.showToast({
										title: that.$t('common.operation.modify.success'),
										duration: 2000
									});
									that.getUserInfo();
								},
								fail: (e) => {
									console.log(e)
								}
							})
				        } 
				        else {
				            // 执行取消后的操作
							that.getUserInfo();
				        }
				    }.bind(this)
				})				
			},
			handleClickChangeEaType(accountInfo){
				let eaType = accountInfo.eaType;
				uni.showModal({
				        title: this.$t('common.operation.tips'),
						cancelText:this.$t('common.operation.cancel'),
						confirmText:this.$t('common.operation.confirm'),
				        content: this.$t('ea.account.text.eaTypeTips'),
				        success: function(res) {
							var that  = this; 
				        if (res.confirm) {
				            // 执行确认后的操作
							uni.request({
								url: that.$baseUrl+'/backstage/ea/me/updateEaType?token=' +uni.getStorageSync("token"),
								method: 'POST',
								data: {'id':accountInfo.id,'eaType':accountInfo.eaType},
								success: (res) => {
									uni.showToast({
										title: that.$t('common.operation.modify.success'),
										duration: 2000
									});
									that.getUserInfo();
								},
								fail: (e) => {
									console.log(e)
								}
							})
				        } 
				        else {
				            // 执行取消后的操作
							that.getUserInfo();
				        }
				    }.bind(this)
				})				
			},
			handleClickChangeEmail(){
				let email = this.baseFormData.email;
				// 判断是否是邮箱地址
				const regEmail = /^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/;
				const isEmail = regEmail.test(email);
				if(isEmail){
					uni.showModal({
					        title: this.$t('common.operation.tips'),
							cancelText:this.$t('common.operation.cancel'),
							confirmText:this.$t('common.operation.confirm'),
					        content: this.$t('ea.login.field.email.tipInfo'),
					        success: function(res) {
								var that  = this; 
					        if (res.confirm) {
					            // 执行确认后的操作
								uni.request({
									url: that.$baseUrl+'/backstage/ea/me/updateEmail?token=' +uni.getStorageSync("token"),
									method: 'POST',
									data: {'id':that.baseFormData.id,'email':that.baseFormData.email},
									success: (res) => {
										uni.showToast({
											title: that.$t('common.operation.modify.success'),
											duration: 2000
										});
									},
									fail: (e) => {
										console.log(e)
									}
								})
					        } 
					    }.bind(this)
					})	
				}else{
					uni.showToast({
						title: this.$t('ea.login.field.email.errorMsgFormat'),
						duration: 2000
					});
				}			
			},
			handleClickChangePhoneNumber(){
				let phoneNumber = this.baseFormData.phoneNumber;
				// 判断是否是邮箱地址
				const regPhoneNumber = /(.*?)(^|\s*\+?0?0?86|\D)(1\d{2})[-\s]{0,3}(\d{4})[-\s]{0,3}(\d{4})(?=\D|$)/;
				const isPhoneNumber = regPhoneNumber.test(phoneNumber);
				if(isPhoneNumber){
					uni.showModal({
					        title: this.$t('common.operation.tips'),
							cancelText:this.$t('common.operation.cancel'),
							confirmText:this.$t('common.operation.confirm'),
					        content: this.$t('ea.login.field.phoneNumber.tipInfo'),
					        success: function(res) {
								var that  = this; 
					        if (res.confirm) {
					            // 执行确认后的操作
								uni.request({
									url: that.$baseUrl+'/backstage/ea/me/updatePhoneNumber?token=' +uni.getStorageSync("token"),
									method: 'POST',
									data: {'id':that.baseFormData.id,'phoneNumber':that.baseFormData.phoneNumber},
									success: (res) => {
										uni.showToast({
											title: that.$t('common.operation.modify.success'),
											duration: 2000
										});
									},
									fail: (e) => {
										console.log(e)
									}
								})
					        } 
					    }.bind(this)
					})	
				}else{
					uni.showToast({
						title: this.$t('ea.login.field.email.errorMsgFormat'),
						duration: 2000
					});
				}			
			},
			handleClickChangePassword(accountInfo){
				uni.showModal({
				        title: this.$t('common.operation.tips'),
						cancelText:this.$t('common.operation.cancel'),
						confirmText:this.$t('common.operation.confirm'),
				        content: this.$t('common.operation.confirmModify'),
				        success: function(res) {
							var that  = this; 
				        if (res.confirm) {
				            // 执行确认后的操作
							uni.request({
								url: that.$baseUrl+'/backstage/ea/me/updatePassword?token=' +uni.getStorageSync("token"),
								method: 'POST',
								data: {'id':accountInfo.id,'accountCode':accountInfo.accountCode,'password':accountInfo.password},
								success: (res) => {
									uni.showToast({
										title: that.$t('common.operation.modify.success'),
										duration: 2000
									});
									that.getUserInfo();
								},
								fail: (e) => {
									console.log(e)
								}
							})
				        } 
				        else {
				            // 执行取消后的操作
							that.getUserInfo();
				        }
				    }.bind(this)
				})				
			},
			handleClickChangeAutoFlag(accountInfo){
				let oAutoFlag = accountInfo.autoFlag;
				let nAutoFlag = accountInfo.autoFlag =='Y' ? 'N':'Y';
				let subscribeFlag = accountInfo.subscribeFlag;
				let tips = nAutoFlag=='Y'? this.$t('ea.account.field.autoFlag.changeActivateTips'):this.$t('ea.account.field.autoFlag.changeDeactivateTips');
				if(nAutoFlag=='Y' && subscribeFlag=='N'){
					uni.showToast({
						title: this.$t('ea.account.field.autoFlag.changeBeforeActivateTips'),
						duration: 2000
					});
					this.getUserInfo();
				}else{
					uni.showModal({
					        title: this.$t('common.operation.tips'),
							cancelText:this.$t('common.operation.cancel'),
							confirmText:this.$t('common.operation.confirm'),
					        content: tips,
					        success: function(res) {
								var that  = this; 
					        if (res.confirm) {
					            // 执行确认后的操作
								uni.request({
									url: that.$baseUrl+'/backstage/ea/me/updateAutoFlag?token=' +uni.getStorageSync("token"),
									method: 'POST',
									data: {'id':accountInfo.id,'accountCode':accountInfo.accountCode,'autoFlag':nAutoFlag},
									success: (res) => {
										uni.showToast({
											title: that.$t('common.operation.modify.success'),
											duration: 2000
										});
										that.getUserInfo();
									},
									fail: (e) => {
										console.log(e)
									}
								})
					        } 
					        else {
					            // 执行取消后的操作
								that.getUserInfo();
					        }
					    }.bind(this)
					})
				}
			},
			openAddAccountWindow(){
				if(this.baseFormData.email == '' || this.baseFormData.email == null){
					uni.showModal({
						title: this.$t('common.operation.tips'),
						cancelText:this.$t('common.operation.cancel'),
						confirmText:this.$t('common.operation.confirm'),
						content: this.$t('ea.login.field.email.tipInfo'),
						showCancel:false,
						success: function(res){
							return;
						}
					});
				}else{
					this.$refs.popup2.open('center')
					this.$nextTick(function(){
						this.$refs.accountForm.setRules(this.addAccountRules);
					})
				}				
			},
			addAccount(){
				if(this.baseFormData.email == '' || this.baseFormData.email == null){
					uni.showModal({
						title: this.$t('common.operation.tips'),
						content:this.$t('ea.login.field.email.tipInfo'),
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
					this.$refs.accountForm.validate().then(res => {
						uni.showModal({
						        title: this.$t('common.operation.tips'),
								cancelText:this.$t('common.operation.cancel'),
								confirmText:this.$t('common.operation.confirm'),
						        content: this.$t('ea.account.text.addAccountTips'),
						        success: function(res) {
									var that  = this; 
									if (res.confirm) {
										that.optionsBrokerList.forEach(item => {
											if(item.value ==that.accountFormData.brokerCode){
												that.accountFormData.brokerName = item.text;
											}
										})
										// 执行确认后的操作
										uni.request({
											url: that.$baseUrl+'/backstage/ea/me/addAccount?token=' +uni.getStorageSync("token"),
											method: 'POST',
											data: {'userCode':that.userCode,'accountCode':that.accountFormData.accountCode,'accountType':that.accountFormData.accountType == 0 ?"Live":"Demo",'agentCode':that.accountFormData.agentCode,'brokerCode':that.accountFormData.brokerCode,'brokerName':that.accountFormData.brokerName,'password':that.accountFormData.password,'userId':that.baseFormData.id,'subscribeFlag':'S0','eaType':'H'},
											success: (res) => {
												const result = res.data.data; 
												let resultMsg = "";
												if(result == 0){
													uni.showToast({
														title: that.$t('common.operation.add.success'),
														duration: 2000
													});
													that.$refs.popup2.close();
													that.getUserInfo();
												}else{
													resultMsg = result;
													if(result == 3){
														resultMsg = this.$t('ea.user.field.agentCode.errorMsgNotExist');
													}
													if(result == 4){
														resultMsg = this.$t('ea.account.field.accountCode.errorMsgExist');
													}
													if(result == 5){
														resultMsg = "请输入正确的推荐码(不能输入自己的推荐码)";
													}
													uni.showToast({
														title: resultMsg,
														duration: 3000
													});
												}
												
											},
											fail: (e) => {
												console.log(e)
											}
										})
									} 
									else {
										// 执行取消后的操作
										that.getUserInfo();
									}
						    }.bind(this)
						})
					}).catch(err => {
						console.log('err', err);
					})
				}												
			}
		}
	}
</script>

<style>
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
	}
</style>
