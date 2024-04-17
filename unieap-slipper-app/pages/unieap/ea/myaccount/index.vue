<template>
	<view class="container">
		<uni-section :title="$t('ea.account.field.accountCode')" type="line">
			<view>
				<uni-data-select v-model="baseFormData.accountCode" :localdata="optionsAccountCodeList"
					@change="changeAccount($event)" :placeholder="$t('ea.account.field.accountCode')">
				</uni-data-select>
			</view>
			<view>
				<text class="bold-text">
					<span>{{$t('ea.account.text.currentBalance')}} : {{ currentBalance }}({{balanceUnit}})&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
					<span>{{$t('ea.account.text.withdrawableBalance')}} : {{ withdrawableBalance }}({{balanceUnit}})</span>
				</text>					
			</view>
		</uni-section>
		<uni-section :title="$t('ea.myinfo.text.accountTradeStatus')" type="line">
			<view>
				<text class="bold-text"  style="color: red;">
					<span>启动自动交易后,托管账户才能自动执行交易指令</span>
				</text>
			</view>
			<view class="example-body">				
				<uni-row class="demo-uni-row">
					<uni-col :span="10">
						<view class="demo-uni-col ">
							<view class="flex-item uni-bg-green"><button type="primary" disabled="true" class="trade-bold-text">{{autoTradeStatusText}}</button></view>
						</view>
					</uni-col>
					<uni-col :span="6">
						<view class="flex-item">
							&nbsp;&nbsp;&nbsp;<button @click="handleClickChangeAutoTrade('start')" size="mini" class="button" :disabled="autoTradeStatus">{{$t('ea.account.field.operation.start')}}</button>
						</view>
					</uni-col>
					<uni-col :span="6">
						<view class="demo-uni-col ">					
							<button @click="handleClickChangeAutoTrade('stop')" size="mini" class="button" :disabled="!autoTradeStatus">{{$t('ea.account.field.operation.stop')}}</button>
						</view>						
					</uni-col>
				</uni-row>
			</view>	
			<!--
			<view class="example-body">
				<uni-row class="demo-uni-row">
					<uni-col :span="11">
						<view class="flex-item uni-bg-green">
							{{autoLoginStatus}}
						</view>
					</uni-col>
					<uni-col :span="1">
						&nbsp;&nbsp;&nbsp;
					</uni-col>
					<uni-col :span="12">
						<view class="flex-item uni-bg-green">
							{{autoStatus}}
						</view>
					</uni-col>
				</uni-row>
			</view>
			-->		
		</uni-section>
		<uni-section :title="$t('ea.account.text.tradeDetail')" type="line">
			<view>
				<uni-table ref="table" :loading="loading" border stripe type="" :emptyText="$t('common.tips.nodata')">
					<uni-tr>
						<uni-th width="80" align="center">{{$t('ea.account.field.createTime')}}</uni-th>
						<uni-th width="80" align="center">{{$t('ea.account.field.orderType')}}</uni-th>
						<uni-th width="40" align="center">{{$t('ea.account.field.volume')}}</uni-th>
						<uni-th width="50" align="center">{{$t('ea.account.field.price')}}</uni-th>
						<uni-th width="50" align="center">{{$t('ea.account.field.profitAmount')}}</uni-th>
					</uni-tr>
					<uni-tr v-for="(item, index) in tableData" :key="index">
						<uni-td>{{ item.tradeTime }}</uni-td>
						<uni-td>{{ item.orderType }}</uni-td>
						<uni-td>{{ item.volume }}</uni-td>
						<uni-td>{{ item.price }}</uni-td>
						<uni-td>{{ item.profitAmount }}</uni-td>
					</uni-tr>
				</uni-table>
				<view class="uni-pagination-box"><uni-pagination show-icon :page-size="pageSize" :current="pageCurrent"
						:total="total" @change="change" /></view>
			</view>
		</uni-section>	 
	</view>
</template>

<script>
	// some-component.vue
	import { goToLogin } from '@/api/unieap/ea/utils.js';
	export default {
		data() {
			return {
				token: '',
				language:'',
				userCode:'',
				isLogin:true,
				gutter: 0,
			    nvueWidth: 730,
				//自动交易是否已启动
				autoTradeStatusText:"自动交易已停止",
				//自动交易状态
				autoTradeStatus:false,
		        //托管账户登陆状态		
				autoLoginStatus:"托管账户未登陆",
				//账户托管是否成功
				autoStatus:"托管交易功能未启动",
				//账户列表,一个用户可以有多少账户
				optionsAccountCodeList: [],
				currentBalance:"0.00",
				withdrawableBalance:"0.00",
				dailyProfitPoint:'0.00',
				dailyProfitAmount:'0.00',
				weeklyProfitPoint:'0.00',
				weeklyProfitAmount:'0.00',
				monthlyProfitPoint:'0.00',
				monthlyProfitAmount:'0.00',
				dailyProfitpercentage:'0.00',
				weeklyProfitpercentage:'0.00',
				monthlyProfitpercentage:'0.00',
				subscribeFlag:'',
				balanceUnit:'',	
				selectedAccountCode:'',
				baseFormData:{
					accountId:'',
					accountCode:'',
					startDate: this.calculateStartDate(), // 开始日期限制
					endDate: this.calculateEndDate()
				},
				searchVal: '',
				tableData: [],
				// 每页数据量
				pageSize: 10,
				// 当前页
				pageCurrent: 1,
				// 数据总量
				total: 0,
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
			if(userCode =='' || userCode == null){
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
				this.userCode = uni.getStorageSync("userCode");
				this.accountListDicData(null);
			},
			calculateStartDate() {
			  // 计算当前日期减去3个月的日期
			  const currentDate = new Date();
			  currentDate.setMonth(currentDate.getMonth() - 3);
			  return currentDate.toISOString().split('T')[0];
			},
			calculateEndDate() {
			  // 计算当前日期减去3个月的日期
			  const currentDate = new Date();
			  currentDate.setDate(currentDate.getDate() + 1);
			  return currentDate.toISOString().split('T')[0];
			},
			//获取项目列表数据
			accountListDicData(e){
				let para = encodeURIComponent(JSON.stringify({
					'userCode': this.userCode
				}))
				uni.request({
					url: this.$baseUrl+'/backstage/ea/myaccount/getDicData?params=' + para,
					data: {
						token: this.token,
						groupCode: 'ACCOUNT_LIST'
					},
					success: (res) => {
						this.optionsAccountCodeList = []
						res.data.data.forEach(item => {
							this.optionsAccountCodeList.push({
								value: item.dicCode,
								text: item.dicName,
								brokerCode:item.attr1,
								accountBalance:item.attr2,
								accountEquity:item.attr3,
								accountMargin:item.attr4,
								accountFreemargin:item.attr5
							})
						})
						if(this.optionsAccountCodeList.length >=1){
							this.baseFormData.accountCode = this.optionsAccountCodeList[0].value
							this.changeAccount(this.baseFormData.accountCode);
						}
					},
					fail: (e) => {
						console.log(e)
					}
				})
			},
			changeAccount(value) {
				this.selectedAccountCode = value;
				this.optionsAccountCodeList.forEach(item=>{
					if(this.selectedAccountCode ==item.value){
						this.currentBalance = item.accountEquity;
						this.withdrawableBalance = item.accountFreemargin;
						if(item.brokerCode == "SUI"){
							this.balanceUnit = this.$t('ea.account.text.unitCNY')							
						}else{
							this.balanceUnit = this.$t('ea.account.text.unitUSD')	
						}
					}
				})
				this.getAccountInfo(this.selectedAccountCode);
				this.getAccountTradeInfo(this.selectedAccountCode);
				this.getData(1, this.searchVal)
			},
			getAccountInfo(accountCode){
				uni.request({
					url: this.$baseUrl+'/backstage/ea/myaccount/getAccountInfo',
					data: {
						token: this.token,
						accountCode: accountCode
					},
					success: (res) => {
						this.dailyProfitPoint = res.data.data.dailyProfitPoint;
						this.dailyProfitAmount = res.data.data.dailyProfitAmount;
						this.dailyProfitpercentage = res.data.data.dailyProfitpercentage;
						this.weeklyProfitPoint = res.data.data.weeklyProfitPoint;
						this.weeklyProfitAmount = res.data.data.weeklyProfitAmount;
						this.weeklyProfitpercentage = res.data.data.weeklyProfitpercentage;
						this.monthlyProfitPoint = res.data.data.monthlyProfitPoint;
						this.monthlyProfitAmount = res.data.data.monthlyProfitAmount;
						this.monthlyProfitpercentage = res.data.data.monthlyProfitpercentage;
						this.subscribeFlag = res.data.data.subscribeFlag;
						this.getData(1, this.baseFormData.userCode)
					},
					fail: (e) => {
						console.log(e)
					}
				})
			},
			getAccountTradeInfo(accountCode){
				uni.request({
					url: this.$baseUrl+'/backstage/ea/myaccount/getTradeStatus',
					data: {
						token: this.token,
						accountCode: accountCode
					},
					success: (res) => {
						this.autoTradeStatus = res.data.data.isAutoTrade =="Y";	
						this.autoTradeStatusText = this.autoTradeStatus?'自动交易已启动':'自动交易已停止';
						this.isAccountLogin = res.data.data.isAccountLogin =="Y";
						if(this.isAccountLogin){
							this.autoLoginStatus = "托管账户已登陆["+res.data.data.loginDate+"]";
						}
						this.isAccountAuto = res.data.data.isAccountAuto == "Y";
						if(this.isAccountAuto){
							this.autoStatus = "托管交易功能已启动["+res.data.data.autoDate+"]";
						}
					},
					fail: (e) => {
						console.log(e)
					}
				})
			},
			handleClickChangeAutoTrade(autoTrade){
				if(this.subscribeFlag =='S0' || this.subscribeFlag =='S1'){
					uni.showModal({
						title: this.$t('common.operation.tips'),
						content:"请先订阅托管服务后再启动自动交易功能[我的->订阅托管服务]",
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
					let tips = autoTrade=='start'?'自动交易启动后,托管账户会自动执行交易指令,确认启动自动交易?':'自动交易停止后,即使在账户托管状态下也不执行交易指令,确认停止自动交易?';
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
									url: that.$baseUrl+'/backstage/ea/myaccount/updateAutoTrade',
									data: {
										'token': that.token,
										'accountCode':that.selectedAccountCode,
										'autoTrade':autoTrade=='start'?'Y':'N'
									},
									success: (res) => {
										uni.showToast({
											title: that.$t('common.operation.modify.success'),
											duration: 2000
										});
										that.autoTradeStatusText = autoTrade=='start'?'自动交易已启动':'自动交易已停止';
										that.autoTradeStatus = autoTrade=='start';
									},
									fail: (e) => {
										console.log(e)
									}
								})
					        }
					    }.bind(this)
					})
				}				
			},
			// 分页触发
			change(e) {
				this.$refs.table.clearSelection()
				this.getData(e.current)
			},
			// 搜索
			search() {
				this.getData(1, this.searchVal)
			},// 获取数据
			getData(pageCurrent, value = '') {
				this.loading = true
				this.pageCurrent = pageCurrent
				uni.request({
					url: this.$baseUrl+'/backstage/ea/myaccount/tradePage',
					data: {
						token: uni.getStorageSync("token"),
						sort: 'create_time',
						dir: 'DESC',
						'pageSize': this.pageSize,
						'currentPage': this.pageCurrent,
						'accountCode': this.selectedAccountCode,
						'startTime': this.baseFormData.startDate,
						'endTime': this.baseFormData.endDate
					},
					success: (res) => {
						console.log(res)
						this.tableData = res.data.data.list
						this.total = res.data.data.total
						this.loading = false
					},
					fail: (e) => {
						console.log(e)
					}
				})

			},
			showDateTimePicker() {
			  // 显示日期时间选择器
			  this.$refs.datePicker.show();
			},
			onDateConfirm(value) {
			  // 处理日期时间选择确认事件
			  console.log('Selected Date:', value);
			},
			onDateCancel() {
			  // 处理日期时间选择取消事件
			  console.log('Date Time Picker Canceled');
			}
		}
	}
</script>

<style>
	.bold-text {
	  font-weight: bold;
	}
	.trade-bold-text {
	  font-weight: bold;
	  font-size: small;
	  color:yellow;
	}
	.tip-info {
		font-size: 10px;
		color: red;
	}
	.demo-uni-row {
		margin-bottom: 10px;
	}
	.demo-uni-col {
		height: 36px;
		border-radius: 5px;
	}

	.dark_deep {
		background-color: #99a9bf;
	}

	.dark {
		background-color: #d3dce6;
	}

	.light {
		background-color: #e5e9f2;
	}

	.example-body {
		/* #ifndef APP-NVUE */
		display: block;
		/* #endif */
		padding: 5rpx 10rpx 0;
		overflow: hidden;
	}
</style>
