<template>
	<view class="content">
		<uni-section :title="$t('ea.eainfo.text.weekEAInfo')" type="line">
			<view v-if="isShowH">
				<text style="color: blue;">{{weekEA1Summary}}</text>
			</view>
			<view>
				<text style="color: green;">{{weekEA2Summary}}</text>
			</view>
			<view class="charts-box">
			  <qiun-data-charts type="line" :chartData="weekEAData"/>
			</view>
		</uni-section>
		<uni-section :title="$t('ea.eainfo.text.monthEAInfo')" type="line">
			<view v-if="isShowH">
				<text style="color: blue;">{{monthEA1Summary}}</text>
			</view>
			<view>
				<text style="color: green;">{{monthEA2Summary}}</text>
			</view>
			<view class="charts-box">
			  <qiun-data-charts type="line" :chartData="monthEAData"/>
			</view>
		</uni-section>
		
		<uni-section :title="$t('ea.eainfo.text.24WeeksEAInfo')" type="line">
			<view v-if="isShowH">
				<text style="color: blue;">{{halfYearWeeksEA1Summary}}</text>
			</view>
			<view>
				<text style="color: green;">{{halfYearWeeksEA2Summary}}</text>
			</view>
			<view class="charts-box">
			  <qiun-data-charts type="line" :chartData="halfYearWeeksEAData"/>
			</view>
		</uni-section>
		
		<uni-section :title="$t('ea.eainfo.text.24WeeksAccuEAInfo')" type="line">
			<view class="charts-box">
			  <qiun-data-charts type="line" :chartData="halfYearWeeksAccuEAData"/>
			</view>
		</uni-section>
		
		<uni-section :title="$t('ea.eainfo.text.weekEAHCommandInfo')" type="line" v-if="isShowH">
			<view>
				<uni-table ref="table" :loading="loading" border stripe type="" :emptyText="$t('common.tips.nodata')">
					<uni-tr>
						<uni-th width="80" align="center">{{$t('ea.eainfo.field.eaTime')}}</uni-th>
						<uni-th width="80" align="center">{{$t('ea.eainfo.field.orderType')}}</uni-th>
						<uni-th width="80" align="center">{{$t('ea.eainfo.field.openPrice')}}</uni-th>
						<uni-th width="80" align="center">{{$t('ea.eainfo.field.closePrice')}}</uni-th>
						<uni-th width="80" align="center">{{$t('ea.eainfo.field.profitPoint')}}</uni-th>
					</uni-tr>
					<uni-tr v-for="(item, index) in weekEAHCommandInfo" :key="index">
						<uni-td>{{ item.eaTime }}</uni-td>
						<uni-td>{{ item.orderType }}</uni-td>
						<uni-td>{{ item.openPrice }}</uni-td>
						<uni-td>{{ item.closePrice }}</uni-td>
						<uni-td>{{ item.profitPoint }}</uni-td>
					</uni-tr>
				</uni-table>
				<view class="uni-pagination-box"><uni-pagination show-icon :page-size="pageSize" :current="pageCurrent"
						:total="total" @change="change" /></view>
			</view>
		</uni-section>
		<uni-section :title="$t('ea.eainfo.text.weekEADCommandInfo')" type="line">	
			<uni-table ref="table" :loading="loading" border stripe type="" :emptyText="$t('common.tips.nodata')">
				<uni-tr>
					<uni-th width="80" align="center">{{$t('ea.eainfo.field.eaTime')}}</uni-th>
					<uni-th width="80" align="center">{{$t('ea.eainfo.field.orderType')}}</uni-th>
					<uni-th width="80" align="center">{{$t('ea.eainfo.field.openPrice')}}</uni-th>
					<uni-th width="80" align="center">{{$t('ea.eainfo.field.closePrice')}}</uni-th>
					<uni-th width="80" align="center">{{$t('ea.eainfo.field.profitPoint')}}</uni-th>
				</uni-tr>
				<uni-tr v-for="(item, index) in weekEADCommandInfo" :key="index">
					<uni-td>{{ item.eaTime }}</uni-td>
					<uni-td>{{ item.orderType }}</uni-td>
					<uni-td>{{ item.openPrice }}</uni-td>
					<uni-td>{{ item.closePrice }}</uni-td>
					<uni-td>{{ item.profitPoint }}</uni-td>
				</uni-tr>
			</uni-table>
		</uni-section>
	</view>	
</template>

<script>
	// some-component.vue
	import { goToLogin } from '@/api/unieap/ea/utils.js';
	export default {
		data() {
			return {
				isShowH:false,
				eaType:"D",
				weekEATitle:"",
				weekEA1Summary:"",
				weekEA2Summary:"",
				weekEAData: {},
				monthEATitle:"",
				monthEA1Summary:"",
				monthEA2Summary:"",
				monthEAData: {},
				halfYearWeeksEAData:{},
				halfYearWeeksEA1Summary:"",
				halfYearWeeksEA2Summary:"",
				halfYearWeeksAccuEAData:{},
				durationEATitle:"",
				durationEAData: {},
				weekEAHCommand:[],
				weekEADCommand:[],
				weekEAHCommandInfo:[],
				weekEADCommandInfo:[],
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
				this.language = uni.getStorageSync("language");
				this.userCode = uni.getStorageSync("userCode");
				if(this.userCode =='caiqiufu@sohu.com' || this.userCode == '15994725242')
				{
					this.isShowH = true;
					this.eaType = 'A';
				}
				this.getWeekEAInfo();
				this.getMonthEAInfo();
				this.get24WeeksEAInfo();
				this.get24WeeksAccuEAInfo();
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
			getWeekEAInfo(){
				uni.request({
					url: this.$baseUrl+'/backstage/ea/eainfo/getWeekEAInfo',
					data: {
						token: uni.getStorageSync("token"),
						eaType: this.eaType
					},
					success: (res) => {
						if(res.data.data.length==1){
							if(this.eaType =='H'){
								let sdata = res.data.data[0];
								if(sdata.totalTradeOrder == 0){
									this.weekEA1Summary = "【"+this.eaType+"策略暂无数据】";
								}else{
									this.weekEA1Summary = "【"+this.eaType+"策略】总盈亏点数["+sdata.totalProfitPoint+"]"+"总单数["+sdata.totalTradeOrder+"]"+"盈利单数["+sdata.totalProfitOrder+"]"+"亏损单数["+sdata.totalLostOrder+"]";
								}
								this.weekEAHCommand = sdata.eaCommandList;
								this.weekEAHCommandInfo = sdata.eaInfoList;
								this.weekEAData.categories = sdata.categories;
								this.weekEAData.series = [{
											"name":sdata.name,
											"data":sdata.datas,
										}]
							}else{
								let ddata = res.data.data[0];
								if(ddata.totalTradeOrder == 0){
									this.weekEA2Summary = "【D策略暂无数据】";
								}else{
									this.weekEA2Summary = "【D策略】总盈亏点数["+ddata.totalProfitPoint+"]"+"总单数["+ddata.totalTradeOrder+"]"+"盈利单数["+ddata.totalProfitOrder+"]"+"亏损单数["+ddata.totalLostOrder+"]";
								}
								this.weekEADCommand = ddata.eaCommandList;
								this.weekEADCommandInfo = ddata.eaInfoList;
								this.total = ddata.eaInfoList.length;
								this.weekEAData.categories = ddata.categories;
								this.weekEAData.series = [{
											"name":ddata.name,
											"data":ddata.datas,
											"color":"#008000"
										}]
							}
							
						}
						if(res.data.data.length==2){
							let hdata = res.data.data[0];
							if(hdata.totalTradeOrder == 0){
								this.weekEA1Summary = "【H策略暂无数据】";
							}else{
								this.weekEA1Summary = "【H策略】总盈亏点数["+ hdata.totalProfitPoint+"]"+"总单数["+hdata.totalTradeOrder+"]"+"盈利单数["+hdata.totalProfitOrder+"]"+"亏损单数["+hdata.totalLostOrder+"]";
							}
							this.weekEAHCommand = hdata.eaCommandList;
							this.weekEAHCommandInfo = hdata.eaInfoList;
							this.weekEAData.categories = hdata.categories;
							this.weekEAData.series = [{
										"name":hdata.name,
										"data":hdata.datas,
										"color":"#0000FF"
									}]
							let ddata = res.data.data[1];
							if(ddata.totalTradeOrder == 0){
								this.weekEA2Summary = "【D策略暂无数据】";
							}else{
								this.weekEA2Summary = "【D策略】总盈亏点数["+ddata.totalProfitPoint+"]"+"总单数["+ddata.totalTradeOrder+"]"+"盈利单数["+ddata.totalProfitOrder+"]"+"亏损单数["+ddata.totalLostOrder+"]";
							}
							this.weekEADCommand = ddata.eaCommandList;
							this.weekEADCommandInfo = ddata.eaInfoList;
							this.weekEAData.series.push({
										"name":ddata.name,
										"data":ddata.datas,
										"color":"#008000"
									});
						}
						
					},
					fail: (e) => {
						console.log(e)
					}
				})
			},
			getMonthEAInfo(){
				this.monthEATitle = this.$t('ea.eainfo.text.monthEAInfo');
				uni.request({
					url: this.$baseUrl+'/backstage/ea/eainfo/getMonthEAInfo',
					data: {
						token: uni.getStorageSync("token"),
						eaType: this.eaType
					},
					success: (res) => {
						if(res.data.data.length==1){
							if(this.eaType=='H'){
								let sdata = res.data.data[0];
								if(sdata.totalTradeOrder == 0){
									this.monthEA1Summary = "【"+this.eaType+"策略暂无数据】";
								}else{
									this.monthEA1Summary = "【"+this.eaType+"策略暂无数据】"+"总盈亏点数["+sdata.totalProfitPoint+"]"+"总单数["+sdata.totalTradeOrder+"]"+"盈利单数["+sdata.totalProfitOrder+"]"+"亏损单数["+sdata.totalLostOrder+"]";
								}
								this.monthEAData.categories = sdata.categories;
								this.monthEAData.series = [{
											"name":sdata.name,
											"data":sdata.datas,
										}]
							}else{
								let ddata = res.data.data[0];
								if(ddata.totalTradeOrder == 0){
									this.monthEA2Summary = "【D策略暂无数据】";
								}else{
									this.monthEA2Summary = "【D策略】总盈亏点数["+ ddata.totalProfitPoint+"]"+"总单数["+ddata.totalTradeOrder+"]"+"盈利单数["+ddata.totalProfitOrder+"]"+"亏损单数["+ddata.totalLostOrder+"]";
								}
								this.monthEAData.categories = ddata.categories;
								this.monthEAData.series = [{
											"name":ddata.name,
											"data":ddata.datas,
											"color":"#008000"
										}]
								
							}							
						}
						if(res.data.data.length==2){
							let hdata = res.data.data[0];
							if(hdata.totalTradeOrder == 0){
								this.monthEA1Summary = "【H策略暂无数据】";
							}else{
								this.monthEA1Summary = "【H策略】总盈亏点数["+hdata.totalProfitPoint+"]"+"总单数["+ hdata.totalTradeOrder+"]"+"盈利单数["+hdata.totalProfitOrder+"]"+"亏损单数["+hdata.totalLostOrder+"]";
							}
							this.monthEAData.categories = hdata.categories;
							this.monthEAData.series = [{
										"name":hdata.name,
										"data":hdata.datas,
										"color":"#0000FF"
									}]
							let ddata = res.data.data[1];
							if(ddata.totalTradeOrder == 0){
								this.monthEA2Summary = "【D策略暂无数据】";
							}else{
								this.monthEA2Summary = "【D策略】总盈亏点数["+ ddata.totalProfitPoint+"]"+"总单数["+ddata.totalTradeOrder+"]"+"盈利单数["+ddata.totalProfitOrder+"]"+"亏损单数["+ddata.totalLostOrder+"]";
							}
							this.monthEAData.series.push({
										"name":ddata.name,
										"data":ddata.datas,
										"color":"#008000"
									});
						}
					},
					fail: (e) => {
						console.log(e)
					}
				})
			},
			get24WeeksEAInfo(){
				uni.request({
					url: this.$baseUrl+'/backstage/ea/eainfo/get24WeeksEAInfo',
					data: {
						token: uni.getStorageSync("token"),
						eaType: this.eaType
					},
					success: (res) => {
						if(res.data.data.length==1){
							if(this.eaType=='H'){
								let sdata = res.data.data[0];
								if(sdata.totalTradeOrder == 0){
									this.halfYearWeeksEA1Summary = "【"+this.eaType+"策略暂无数据】";
								}else{
									this.halfYearWeeksEA1Summary = "【"+this.eaType+"策略暂无数据】"+"总盈亏点数["+sdata.totalProfitPoint+"]"+"总单数["+ sdata.totalTradeOrder+"]"+"盈利单数["+sdata.totalProfitOrder+"]"+"亏损单数["+sdata.totalLostOrder+"]";
								}
								this.halfYearWeeksEAData.categories = sdata.categories;
								this.halfYearWeeksEAData.series = [{
											"name":sdata.name,
											"data":sdata.datas,
										}]
							}else{
								let ddata = res.data.data[0];
								if(ddata.totalTradeOrder == 0){
									this.halfYearWeeksEA2Summary = "【D策略暂无数据】";
								}else{
									this.halfYearWeeksEA2Summary = "【D策略】总盈亏点数["+ ddata.totalProfitPoint+"]"+"总单数["+ddata.totalTradeOrder+"]"+"盈利单数["+ddata.totalProfitOrder+"]"+"亏损单数["+ddata.totalLostOrder+"]";
								}
								this.halfYearWeeksEAData.categories = ddata.categories;
								this.halfYearWeeksEAData.series = [{
											"name":ddata.name,
											"data":ddata.datas,
											"color":"#008000"
										}]
							}
							
						}
						if(res.data.data.length==2){
							let hdata = res.data.data[0];
							if(hdata.totalTradeOrder == 0){
								this.halfYearWeeksEA1Summary = "【H策略暂无数据】";
							}else{
								this.halfYearWeeksEA1Summary = "【H策略】总盈亏点数["+hdata.totalProfitPoint+"]"+"总单数["+ hdata.totalTradeOrder+"]"+"盈利单数["+hdata.totalProfitOrder+"]"+"亏损单数["+hdata.totalLostOrder+"]";
							}
							this.halfYearWeeksEAData.categories = hdata.categories;
							this.halfYearWeeksEAData.series = [{
										"name":hdata.name,
										"data":hdata.datas,
										"color":"#0000FF"
									}]
							let ddata = res.data.data[1];
							if(ddata.totalTradeOrder == 0){
								this.halfYearWeeksEA2Summary = "【D策略暂无数据】";
							}else{
								this.halfYearWeeksEA2Summary = "【D策略】总盈亏点数["+ddata.totalProfitPoint+"]"+"总单数["+ddata.totalTradeOrder+"]"+"盈利单数["+ddata.totalProfitOrder+"]"+"亏损单数["+ddata.totalLostOrder+"]";
							}
							this.halfYearWeeksEAData.series.push({
										"name":ddata.name,
										"data":ddata.datas,
										"color":"#008000"
									});
						}
					},
					fail: (e) => {
						console.log(e)
					}
				})
			},
			get24WeeksAccuEAInfo(){
				uni.request({
					url: this.$baseUrl+'/backstage/ea/eainfo/get24WeeksAccuEAInfo',
					data: {
						token: uni.getStorageSync("token"),
						eaType: this.eaType
					},
					success: (res) => {
						if(res.data.data.length==1){
							if(this.eaType=='H'){
								let sdata = res.data.data[0];
								this.halfYearWeeksAccuEAData.categories = sdata.categories;
								this.halfYearWeeksAccuEAData.series = [{
											"name":sdata.name,
											"data":sdata.datas,
										}]
							}else{
								let ddata = res.data.data[0];
								this.halfYearWeeksAccuEAData.categories = ddata.categories;
								this.halfYearWeeksAccuEAData.series = [{
											"name":ddata.name,
											"data":ddata.datas,
											"color":"#008000"
										}]
							}
							
						}
						if(res.data.data.length==2){
							let hdata = res.data.data[0];
							this.halfYearWeeksAccuEAData.categories = hdata.categories;
							this.halfYearWeeksAccuEAData.series = [{
										"name":hdata.name,
										"data":hdata.datas,
										"color":"#0000FF"
									}]
							let ddata = res.data.data[1];
							this.halfYearWeeksAccuEAData.series.push({
										"name":ddata.name,
										"data":ddata.datas,
										"color":"#008000"
									});
						}
					},
					fail: (e) => {
						console.log(e)
					}
				})
			}
		}
	}
</script>

<style>

</style>
