<template>
	<view class="container">
		<uni-section :title="$t('ea.agent.user.text.agentAccountSummary')" type="line">
			<view>
				<text class="bold-text">
					<span>{{$t('ea.agent.user.text.totalNumber')}} : {{ totalNumber }}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>     
					<span>{{$t('ea.agent.user.text.subscribedNumber')}} : {{ subscribedNumber }}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>     
					<span>{{$t('ea.agent.user.text.outstandingNumber')}} : {{ outstandingNumber }}</span>			       
				</text>
			</view>			
		</uni-section>	
		<uni-section :title="$t('ea.agent.user.text.agentRevenueSummary')" type="line">
			<view>
				<text class="bold-text">
					<span>{{$t('ea.agent.user.text.totalRevenueAmount')}} : {{ totalRevenueAmount }}({{$t('ea.account.text.unit')}})&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
				    <span>{{ revenueMonth }} {{$t('ea.agent.user.text.currMonthRevenue')}} : {{ revenueAmount }}({{$t('ea.account.text.unit')}})</span>
				</text>
			</view>
		</uni-section>
		<uni-section :title="$t('ea.agent.user.text.agentAccountDetail')" type="line">
			<view>
				<text class="bold-text"  style="color: red;">
					<span>已停用的账户,不再产生订阅费用,无佣金返还</span>
				</text>
			</view>
			<view class="uni-container">
				<uni-table ref="table" :loading="loading" border stripe type="" :emptyText="$t('common.tips.nodata')">
					<uni-tr>
						<uni-th width="60" align="center">{{$t('ea.account.field.accountCode')}}</uni-th>
						<uni-th width="60" align="center">{{$t('ea.agent.user.text.userCode')}}</uni-th>
						<uni-th width="60" align="center">{{$t('ea.account.field.activateFlag')}}</uni-th>
						<uni-th width="150" align="center">{{$t('ea.agent.user.text.subscribeFlag')}}</uni-th>
						<uni-th width="50" align="center">...</uni-th>
					</uni-tr>
					<uni-tr v-for="(item, index) in tableData" :key="index">
						<uni-td>{{ item.accountCode }}</uni-td>
						<uni-td>{{ item.userCode }}</uni-td>
						<uni-td>{{ item.activateFlagDesc }}</uni-td>
						<uni-td>{{ item.subscribeFlagDesc }}</uni-td>
						<uni-td><button class="uni-btn" size="mini" @click="handleClickShowMore(item)">{{$t('common.tips.more')}}</button></uni-td>
					</uni-tr>
				</uni-table>
				<view class="uni-pagination-box"><uni-pagination show-icon :page-size="pageSize" :current="pageCurrent"
						:total="total" @change="change" />
				</view>
			</view>
		</uni-section>	
		<!-- 普通弹窗 -->
		<uni-popup ref="popup" background-color="#fff">
			<view class="popup-content">
				<uni-section :title="$t('ea.agent.user.text.agentAccountMoreDetail')" type="line">
					<uni-group>
						<view class="uni-container">
							<uni-forms ref="baseForm" :model="baseFormData" labelWidth="80px">
								<uni-forms-item :label="$t('ea.account.field.accountCode')">
									<uni-easyinput v-model="baseFormData.accountCode" :disabled="true"/>
								</uni-forms-item>
								<uni-forms-item :label="$t('ea.user.field.userCode')">
									<uni-easyinput v-model="baseFormData.userCode" :disabled="true" />
								</uni-forms-item>
								<uni-forms-item :label="$t('ea.user.field.phoneNumber')">
									<uni-easyinput v-model="baseFormData.phoneNumber" :disabled="true" />
								</uni-forms-item>
								<uni-forms-item :label="$t('ea.user.field.weixin')">
									<uni-easyinput v-model="baseFormData.weixin" :disabled="true" />
								</uni-forms-item>
								<uni-forms-item :label="$t('ea.user.field.email')" >
									<uni-easyinput v-model="baseFormData.email" :disabled="true"/>
								</uni-forms-item>
							    <uni-forms-item :label="$t('ea.user.field.whatsApp')">
									<uni-easyinput v-model="baseFormData.whatsApp" :disabled="true"/>
							    </uni-forms-item>
							</uni-forms>
						</view>						
					</uni-group>					
				</uni-section>												
			</view>						
		</uni-popup>
	</view>
</template>

<script>
	// some-component.vue
	import { goToLogin } from '@/api/unieap/ea/utils.js';
	export default {
		data() {
			return {
				token: '',
				userCode:'',
				isLogin:true,
				//账户列表,一个用户可以有多少账户
				optionsAccountCodeList: [],	
				totalNumber:"0.00",
				subscribedNumber:"0.00",
				outstandingNumber:"0.00",
				revenueMonth:"0.00",
				revenueAmount:"0.00",
				totalRevenueAmount:"0.00",			
				baseFormData:{
					userCode:'',
					phoneNumber:'',
					weixin:'',
					email:'',
					whatsApp:'',
					address:''
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
		computed: {
			convertSubscribeFlag(){
				return function(value){
					switch(value){
						case "Y":
						    return this.$t('ea.agent.user.text.subscribe.Y');
						case "N":
						    return this.$t('ea.agent.user.text.subscribe.N');
						default:
						    return value; 
					}
				}
			}
		},
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
				this.getAgentSummary();	
			},
			calculateMinDate() {
			  // 计算当前日期减去3个月的日期
			  const currentDate = new Date();
			  currentDate.setMonth(currentDate.getMonth() - 3);
			  return currentDate.toISOString().split('T')[0];
			},
			handleClickShowMore(data){
				this.baseFormData = data
				this.$refs.popup.open('center')
			},
			getAgentSummary(){
				uni.request({
					url: this.$baseUrl+'/backstage/ea/myagent/getAgentSummaryInfo',
					data: {
						token: this.token,
						userCode: this.userCode
					},
					success: (res) => {
						this.totalNumber = res.data.data.totalNumber;
						this.subscribedNumber = res.data.data.subscribedNumber;
						this.outstandingNumber = res.data.data.outstandingNumber;
						this.revenueMonth = res.data.data.revenueMonth;
						this.revenueAmount = res.data.data.revenueAmount;
						this.totalRevenueAmount = res.data.data.totalRevenueAmount;
						this.getData(1, this.userCode)
					},
					fail: (e) => {
						console.log(e)
					}
				})
			},// 分页触发
			change(e) {
				this.$refs.table.clearSelection()
				this.getData(e.current)
			},
			// 搜索
			search() {
				this.getData(1, this.searchVal)
			},// 获取数据
			getData(pageCurrent,userCode) {
				this.loading = true
				this.pageCurrent = pageCurrent
				uni.request({
					url: this.$baseUrl+'/backstage/ea/myagent/agentAccountPage',
					data: {
						token: uni.getStorageSync("token"),
						sort: 'id',
						dir: 'DESC',
						'pageSize': this.pageSize,
						'currentPage': this.pageCurrent,
						'userCode': this.userCode
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

<style lang="scss">
	.example {
		padding: 15px;
		background-color: #fff;
	}
	.form-item {
		display: flex;
		align-items: center;
		flex: 1;
	}
	.bold-text {
	  font-weight: bold;
	}
	.text span {
		font-weight: bold;
		word-spacing: 15px;
	}
</style>
