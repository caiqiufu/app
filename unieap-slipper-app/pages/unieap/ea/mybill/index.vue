<template>
	<view class="container">
		<uni-section :title="$t('ea.bill.user.text.userBillInfo')" type="line">
			<view>
				<text class="bold-text">
					<span>{{$t('ea.bill.user.text.totalBillAmount')}}:{{ totalBillAmount }}({{$t('common.money.text.unit')}})&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
					<span>{{$t('ea.bill.user.text.totalOutstandingAmount')}}:{{ totalOutstandingAmount }}({{$t('common.money.text.unit')}})&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
					<span>{{$t('ea.bill.user.text.totalPaidAmount')}}:{{ totalPaidAmount }}({{$t('common.money.text.unit')}})</span>
				</text>
			</view>			
		</uni-section>	
		<uni-section :title="$t('ea.bill.user.text.accountBillInfo')" type="line">
			<view>
				<uni-table ref="table" :loading="loading" border stripe type="" :emptyText="$t('common.tips.nodata')">
					<uni-tr>
						<uni-th width="40" align="center">{{$t('ea.bill.user.text.billDate')}}</uni-th>
						<uni-th width="100" align="center">{{$t('ea.bill.user.text.feeType')}}</uni-th>
						<uni-th width="50" align="center">{{$t('ea.bill.user.text.billAmount')}}</uni-th>
						<uni-th width="60" align="center">{{$t('ea.bill.user.text.outstandingAmount')}}</uni-th>
						<uni-th width="60" align="center">{{$t('ea.bill.user.text.paidAmount')}}</uni-th>
					</uni-tr>
					<uni-tr v-for="(item, index) in tableData" :key="index">
						<uni-td>{{ item.billDate }}</uni-td>
						<uni-td>{{ item.feeDesc }}</uni-td>
						<uni-td>{{ item.billAmount }}</uni-td>
						<uni-td>{{ item.outstandingAmount }}</uni-td>
						<uni-td>{{ item.paidAmount }}</uni-td>
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
				//账户列表,一个用户可以有多少账户
				optionsAccountCodeList: [],
				totalBillAmount:"0.00",
				totalOutstandingAmount:"0.00",
				totalPaidAmount:"0.00",
				accountTotalBillAmount:"0.00",
				accountTotalOutstandingAmount:"0.00",
				accountTotalPaidAmount:"0.00",			
				baseFormData:{
					userCode:'',
					accountCode:'',
					startDate: this.calculateMinDate(), // 开始日期限制
					endDate: new Date().toISOString().split('T')[0]
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
			convertFeeType(){
				return function(value){
					switch(value){
						case "S":
						    return this.$t('ea.dic.bill.feeType.S');
						case "A":
						    return this.$t('ea.dic.bill.feeType.A');
						case "B":
						    return this.$t('ea.dic.bill.feeType.B');
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
				this.baseFormData.userCode =  uni.getStorageSync("userCode");
				this.getUserBillSummary();						
			},
			gotoLogin(){
				uni.navigateTo({
					//保留当前页面，跳转到应用内的某个页面
					//url:`./home/index?title=${title}`
					url: '/pages/unieap/ea/me/login/index'
					//url: '../wxlogin'
				})
			},
			calculateMinDate() {
			  // 计算当前日期减去3个月的日期
			  const currentDate = new Date();
			  currentDate.setMonth(currentDate.getMonth() - 3);
			  return currentDate.toISOString().split('T')[0];
			},
			getUserBillSummary(){
				uni.request({
					url: this.$baseUrl+'/backstage/ea/mybill/getBillSummaryInfo',
					data: {
						token: this.token,
						userCode: this.baseFormData.userCode
					},
					success: (res) => {
						if(res.data!=null && res.data.data!=null){
							this.totalBillAmount = res.data.data.totalBillAmount;
							this.totalOutstandingAmount = res.data.data.totalOutstandingAmount;
							this.totalPaidAmount = res.data.data.totalPaidAmount;
							this.getData(1, this.baseFormData.userCode)
						}						
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
					url: this.$baseUrl+'/backstage/ea/mybill/userBillDetailPage',
					data: {
						token: uni.getStorageSync("token"),
						sort: 'bill_date',
						dir: 'DESC',
						'pageSize': this.pageSize,
						'currentPage': this.pageCurrent,
						'userCode': this.baseFormData.userCode,
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
</style>
