<template>
	<view class="container">
		<view class="example">
			<!-- 基础用法，不包含校验规则 -->
			<uni-forms ref="baseForm" :model="baseFormData" :rules="rules" labelWidth="80px">
				<uni-forms-item label="项目" name="parentId" required>
					<uni-data-select v-model="baseFormData.parentId" :localdata="optionsParentProjectList" 
						@change="changeParent" placeholder="选择项目"></uni-data-select>
				</uni-forms-item>
				<uni-forms-item label="标段" name="projectId" required>
					<uni-data-select v-model="baseFormData.projectId" :localdata="optionsProjectList"
						@change="changeProject" placeholder="选择标段"></uni-data-select>
				</uni-forms-item>
				<uni-list :border="true" v-for="(buildingPartInfo,index) in baseFormData.deliveryBuildingpartList" :key="index">
					<uni-forms-item :label="'施工部位' + (index + 1)" :key="'sgbw_' + buildingPartInfo.id">				
						<!--
						<uni-data-select v-model="buildingPartInfo.sgbw" :localdata="optionsBuildingPartList"
							placeholder="选择施工部位"></uni-data-select>
						-->
						<uni-easyinput v-model="buildingPartInfo.sgbw" placeholder="请输入施工部位" />
					</uni-forms-item>
					<uni-forms-item :label="'施工内容' + (index + 1)" :key="'sglr_' + buildingPartInfo.id">
						<uni-easyinput v-model="buildingPartInfo.sglr" placeholder="请输入施工内容" />
					</uni-forms-item>
					<uni-forms-item :label="'施工情况' + (index + 1)" :key="'sgqk_' + buildingPartInfo.id">
						<uni-easyinput v-model="buildingPartInfo.sgqk" placeholder="请输入施工情况" />
					</uni-forms-item>
					<button type="primary" size="mini" :disabled="baseFormData.deliveryBuildingpartList.length <= 1"
						@click="del(buildingPartInfo.id)">删除</button>
				</uni-list>
				<button type="primary" size="mini" @click="add">新增施工情况</button>
				<uni-forms-item label="巡检记录">
					<uni-easyinput type="textarea" v-model="baseFormData.xjjl" placeholder="请输入巡检记录" />
				</uni-forms-item>
				<uni-forms-item label="旁站记录">
					<uni-easyinput v-model="baseFormData.pzjl" placeholder="请输入旁站项目及旁站记录情况" />
				</uni-forms-item>
				<uni-forms-item label="见证记录">
					<uni-easyinput v-model="baseFormData.jzjl" placeholder="请输入见证项目及见证记录情况" />
				</uni-forms-item>
				<uni-forms-item label="隐蔽工程">
					<uni-easyinput v-model="baseFormData.ybgcys" placeholder="请输入隐蔽工程验收情况" />
				</uni-forms-item>
				<uni-forms-item label="分项工程">
					<uni-easyinput v-model="baseFormData.fxgcjy" placeholder="请输入分项工程检验批验收情况" />
				</uni-forms-item>
				<uni-forms-item label="工程例会">
					<uni-easyinput v-model="baseFormData.gclh" placeholder="请输入工程(例会)会议情况" />
				</uni-forms-item>
				<uni-forms-item label="安全问题">
					<uni-easyinput v-model="baseFormData.aqls" placeholder="请输入施工安全问题及处理后落实情况" />
				</uni-forms-item>
				<uni-forms-item label="监理指令">
					<uni-easyinput v-model="baseFormData.jlzlzx" placeholder="请输入监理书面指令,执行及回复情况" />
				</uni-forms-item>
				<uni-forms-item label="口头指示">
					<uni-easyinput v-model="baseFormData.ktzscl" placeholder="请输入建设单位口头指示及处理意见" />
				</uni-forms-item>
				<uni-forms-item label="汇报事项">
					<uni-easyinput v-model="baseFormData.hbsxcl" placeholder="请输入施工单位汇报事项及处理意见" />
				</uni-forms-item>
				<uni-forms-item label="其他事项">
					<uni-easyinput v-model="baseFormData.qtsx" placeholder="请输入其他事项" />
				</uni-forms-item>
				<view class="button-group">
					<button type="primary" size="mini" @click="submit('Draft')">保存草稿</button>
					<button type="primary" size="mini" @click="submit('Pending')">提交审核</button>
				</view>
			</uni-forms>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				logId: '',
				//项目信息
				optionsParentProjectList: [],
				//标段信息
				optionsProjectList: [],
				optionsBuildingPartList: [],
				parentId:'',
				projectId:'',
				// 基础表单数据
				baseFormData: {
					id: null,
					parentId: '',
					projectId: '',
					projectName: '',
					logTime: Date.now(),
					status: '',
					dateStart: '',
					dateEnd: '',
					tempMax: '',
					tempMin: '',
					weatherMorning: '',
					weatherAfternoon: '',
					sgdw: '',
					sgbw: '',
					sglr: '',
					sgqk: '',
					xjjl: '',
					pzjl: '',
					jzjl: '',
					ybgcys: '',
					fxgcjy: '',
					gclh: '',
					aqls: '',
					jlzlzx: '',
					ktzscl: '',
					hbsxcl: '',
					qtsx: '',
					zjlyj: '',
					zjlName: '',
					zjlDate: '',
					createDate: '',
					createBy: '',
					modifyDate: '',
					modifyBy: '',
					deliveryBuildingpartList: [],
					wxauId: ''
				},
				// 校验规则
				rules: {
					parentId: {
						rules: [{
							required: true,
							errorMessage: '项目不能为空'
						}]
					},
					projectId: {
						required: true,
						errorMessage: '标段不能为空'
					}
				}
			}
		},
		computed: {},
		onLoad(option) {
			this.logId = option.logId
		},
		onShow() {
			this.projectDicData(null)
			this.getData()
		},
		onReady() {
			// 设置自定义表单校验规则，必须在节点渲染完毕后执行
			//this.$refs.customForm.setRules(this.customRules)
		},
		methods: {
			//获取项目信息
			projectDicData(e) {
				let para = encodeURIComponent(JSON.stringify({
					'parentId': ''
				}))
				uni.request({
					url: this.$baseUrl+'/backstage/projectoa/inf/getDicData?params=' + para,
					data: {
						token: uni.getStorageSync("token"),
						groupCode: 'PROJECT_LIST'
					},
					success: (res) => {
						this.optionsParentProjectList = []
						res.data.data.forEach(item => {
							this.optionsParentProjectList.push({
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
			changeParent(parentId) {
				if (parentId != '') {
					this.getProjectList(parentId)
				}
			},
			getProjectList(parentId) {
				let para = encodeURIComponent(JSON.stringify({
					'parentId': parentId
				}))
				uni.request({
					url: this.$baseUrl+'/backstage/projectoa/inf/getDicData?params=' +
						para,
					data: {
						token: uni.getStorageSync("token"),
						groupCode: 'PROJECT_LIST'
					},
					success: (res) => {
						this.optionsProjectList = []
						res.data.data.forEach(item => {
							this.optionsProjectList.push({
								value: item.dicCode,
								text: item.dicName
							})
						})
					},
					fail: (e) => {
						console.log(e)
					}
				})
			},
			changeProject(projectId) {
				//this.baseFormData.projectName = node.text;
				if (projectId != '') {
					this.optionsProjectList.forEach(item=>{
						if(item.value === projectId){
							this.baseFormData.projectName = item.text
						}
					})
					this.getBuildingpartList(projectId)
				}
			},
			getBuildingpartList(projectId) {
				this.baseFormData.deliveryBuildingpartList = []
				this.baseFormData.deliveryBuildingpartList.push({
					id: Date.now(),
					projectId: this.baseFormData.projectId,
					logId: this.baseFormData.id,
					sgbw: '',
					sglr: '',
					sgqk: ''
				})
				let para = encodeURIComponent(JSON.stringify({
					projectId: projectId
				}))
				uni.request({
					url: this.$baseUrl+'/backstage/projectoa/inf/getDicData?params=' +
						para,
					data: {
						token: uni.getStorageSync("token"),
						groupCode: 'BUILDING_PART'
					},
					success: (res) => {
						this.optionsBuildingPartList = []
						res.data.data.forEach(item => {
							this.optionsBuildingPartList.push({
								value: item.dicCode,
								text: item.dicName
							})
						})
					},
					fail: (e) => {
						console.log(e)
					}
				})
			},
			getData() {
				uni.request({
					url: this.$baseUrl+'/backstage/projectoa/inf/info',
					data: {
						token: uni.getStorageSync("token"),
						id: this.logId
					},
					success: (res) => {
						//this.projectDicData(null)
						//this.getProjectList(res.data.data.projectId)
						//console.log('getData res='+res)
						this.parentId = res.data.data.parentId.toString()
						this.projectId = res.data.data.projectId.toString()
						this.getProjectList(this.parentId)
						this.getBuildingpartList(this.projectId)
						if (this.baseFormData.deliveryBuildingpartList.length === 0) {
							this.baseFormData.deliveryBuildingpartList = [{
								id: Date.now(),
								projectId: this.projectId,
								logId: '',
								sgbw: '',
								sglr: '',
								sgqk: ''
							}]
						}
						this.baseFormData = res.data.data
						this.baseFormData.parentId = res.data.data.parentId.toString()
						this.baseFormData.projectId = res.data.data.projectId.toString()
						//console.log('getData this.baseFormData.parentId=', this.baseFormData.parentId);
					},
					fail: (e) => {
						console.log(e)
					}
				})
			},
			add() {
				this.baseFormData.deliveryBuildingpartList.push({
					id: Date.now(),
					projectId: this.baseFormData.projectId,
					logId: this.baseFormData.id,
					sgbw: '',
					sglr: '',
					sgqk: ''
				})
			},
			del(id) {
				let index = this.baseFormData.deliveryBuildingpartList.findIndex(v => v.id === id)
				this.baseFormData.deliveryBuildingpartList.splice(index, 1)
			},
			submit(optType) {
				console.log(this.baseFormData);
				this.$refs['baseForm'].validate().then(res => {
					this.baseFormData.status = optType
					uni.request({
						url: this.$baseUrl+'/backstage/projectoa/inf/create?token=' +uni.getStorageSync("token"),
						method: 'POST',
						data: this.baseFormData,
						success: (res) => {
							uni.showToast({
								title: '提交成功',
								duration: 2000
							});
						},
						fail: (e) => {
							console.log(e)
						}
					})
				}).catch(err => {
					console.log('err', err);
				})
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
</style>
