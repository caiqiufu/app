<template>
	<view>
		<view class="uni-container">
			<uni-table ref="table" :loading="loading" border stripe type="" emptyText="暂无更多数据">
				<uni-tr>
					<uni-th width="100" align="center">日志时间</uni-th>
					<uni-th width="120" align="center">项目</uni-th>
					<uni-th width="80" align="center">操作</uni-th>
				</uni-tr>
				<uni-tr v-for="(item, index) in tableData" :key="index">
					<uni-td>{{ item.logTime }}</uni-td>
					<uni-td>{{ item.projectName }}</uni-td>
					<uni-td>
						<button type="primary" size="mini" @click="viewLog(item)">查看</button>
						<button v-if="item.status ==='Pending'" type="warn" size="mini" @click="approveLog(item)">审核</button>
					</uni-td>
				</uni-tr>
			</uni-table>
			<view class="uni-pagination-box"><uni-pagination show-icon :page-size="pageSize" :current="pageCurrent"
					:total="total" @change="change" /></view>
		</view>
	</view>
</template>
<script>
	export default {
		data() {
			return {
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
		onLoad() {
			//this.selectedIndexs = []
			//this.getData(1)
		},
		onShow() {
			this.selectedIndexs = []
			this.getData(1)
		},
		methods: {
			viewLog(e) {
				uni.navigateTo({
					//保留当前页面，跳转到应用内的某个页面
					//url:`./home/index?title=${title}`
					url: './view?logId=' + e.id
					//url: '../wxlogin'
				})			
			},
			approveLog(e) {
				uni.navigateTo({
					//保留当前页面，跳转到应用内的某个页面
					//url:`./home/index?title=${title}`
					url: './approve?logId=' + e.id
					//url: '../wxlogin'
				})
			},
			// 多选处理
			selectedItems() {
				return this.selectedIndexs.map(i => this.tableData[i])
			},
			// 多选
			selectionChange(e) {
				console.log(e.detail.index)
				this.selectedIndexs = e.detail.index
			},
			//批量删除
			delTable() {
				console.log(this.selectedItems())
			},
			// 分页触发
			change(e) {
				this.$refs.table.clearSelection()
				this.selectedIndexs.length = 0
				this.getData(e.current)
			},
			// 搜索
			search() {
				this.getData(1, this.searchVal)
			},
			// 获取数据
			getData(pageCurrent, value = '') {
				this.loading = true
				let wxcode = uni.getStorageSync("wxcode")
				this.pageCurrent = pageCurrent
				uni.request({
					url: this.$baseUrl+'/backstage/projectoa/inf/page',
					data: {
						token: uni.getStorageSync("token"),
						wxauId:wxcode,
						sort: 'id',
						dir: 'asc',
						'pageSize': this.pageSize,
						'currentPage': this.pageCurrent,
						'createBy': ''
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
			// 伪request请求
			request(options) {
				const {
					pageSize,
					pageCurrent,
					success,
					value
				} = options

				let total = tableData.length
				let data = tableData.filter((item, index) => {
					const idx = index - (pageCurrent - 1) * pageSize
					return idx < pageSize && idx >= 0
				})
				if (value) {
					data = []
					tableData.forEach(item => {
						if (item.name.indexOf(value) !== -1) {
							data.push(item)
						}
					})
					total = data.length
				}

				setTimeout(() => {
					typeof success === 'function' &&
						success({
							data: data,
							total: total
						})
				}, 500)
			}
		}
	}
</script>
<style lang="scss">
	/* #ifndef H5 */
	/* page {
	padding-top: 85px;
} */
	/* #endif */
	.uni-group {
		display: flex;
		align-items: center;
	}
</style>