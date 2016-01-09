package com.quincy.cancelviewpagercache;

/**
 * 全局参数声明
 * 
 * @author quincy
 * 
 */
public class GlobalContants {


    public static final String SERVER_URL = "http://app.bbicar.com/MobileWebService.asmx/";// 服务器根地址
   // public static final String SERVER_URL = "http://app.hcn.com/MobileWebService.asmx/";// App内网测试地址


	// 注册
	public static final String REG_URL = SERVER_URL + "Register";
	// 获取商家列表
	//public static final String GETSHOP_URL = "http://192.168.1.103:8080/testdata1.json";
	public static final String GETSHOP_URL = SERVER_URL + "getShop";
	// 修改账号信息
	public static final String ACCOUNTOP_URL = SERVER_URL + "accountOp";
	// 获取验证码
	public static final String GETCODE_URL = SERVER_URL + "getCode";
	// 忘记密码
	public static final String FORGETPWD_URL = SERVER_URL + "forgetPwd";
	// 用户是否存在
	public static final String USERISEXIST_URL = SERVER_URL + "userIsExist";
	// 用户登录
	public static final String USERLOGIN_URL = SERVER_URL + "UserLogin";
	// 上传店铺位置
	public static final String UPLOAD_POSITION_URL = SERVER_URL + "shopPosition";
	// 获取广告列表
//	public static  final String GETADV_URL=SERVER_URL+"getAdv";
	//版本更新
	public static  final String VERSION_URL=SERVER_URL+"appVersion";
	//获取用户信息   通过ID
	public static  final String GETUSERBGID_URL=SERVER_URL+"getUserById";
	// 获取初始化数据
	public static  final String INIT_URL=SERVER_URL+"appInit";
	//违章查询
	public static  final String WeiZhang_URL=SERVER_URL+"WeiZhang";
	//生成或更改报修订单
	public static  final String AddOrUpdateRepairOrder_URL=SERVER_URL+"AddOrUpdateRepairOrder" ;
	//我的报修单     个人车主
	public static  final String myRepairOrderList_URL=SERVER_URL+"myRepairOrderList";
	//获取全部的车型
	public static  final String selectMyCar_URL=SERVER_URL+"selectMyCar";
	//设置默认的车型
	public static  final String addDefaultCar_URL=SERVER_URL+"addDefaultCar";
	//删除车型
	public static  final String delCarType_URL=SERVER_URL+"delCarType";
	//添加车型
	public static  final String addMyCar_URL=SERVER_URL+"addMyCar";
	// 查询  省   市   区
	public static  final String addressInfo_URL= SERVER_URL+"addressInfo";
	//删除维修订单
	public static  final String deleteRepairOrder_URL=  SERVER_URL+"deleteRepairOrder";
	// 图片上传
	public static  final  String UploadPic_URL=SERVER_URL+"UploadPic";
	// 商家查看报修列表
    public static  final  String getRepairForExp_URL=SERVER_URL+"getRepairForExp";
    // 增加修改评价
    public static  final  String updateRepairEvaluation_URL=SERVER_URL+"updateRepairEvaluation";   
    //查询订单
    public static  final  String repairDetail_URL=SERVER_URL+"repairDetail";
    //商家报价列表
    public static  final  String getReturnInfo_URL=SERVER_URL+"getReturnInfo";
    //商家提交报价
    public static  final  String addRepairReturn_URL=SERVER_URL+"addRepairReturn";
    //根据商家的id获取店铺
    public static  final  String getExpFromID_URL=SERVER_URL+"getExpFromID";
    //求购单详情,求购主单的详情
    public static  final  String procurment_Info_Model_URL=SERVER_URL+"Procurment_InfoMst_Model";  
    //求购单中的所有清单
    public static  final  String procurment_InfoDtl_Select_URL=SERVER_URL+"Procurment_InfoDtl_Select"; 
    //发布修改求购信息           主单
    public static  final  String procurment_Info_Update_URL=SERVER_URL+"Procurment_InfoMst_Update";
    //求购附件信息                   增加 附件
    public static  final  String Procurment_Attachment_Add_URL=SERVER_URL+"Procurment_Attachment_Add";
    //求购明细信息                    明细表单
    public static  final  String Procurment_InfoDtl_Update_URL=SERVER_URL+"Procurment_InfoDtl_Update";
    
    
    //提交求购的图片
    public static  final String procurment_Image_Add_URL=SERVER_URL+"Procurment_Image_Add";  
    
    
    //求购信息列表
    public static final String procurment_Info_Select_URL=SERVER_URL+"Procurment_InfoMst_Select";
    //报修领养
    public static final String adoptionSumbit_URL=SERVER_URL+"adoptionSumbit";
    //求购清单详情中的图片
    public static final String Procurment_Image_Select_URL=SERVER_URL+ "Procurment_Image_Select";    
    //供货信息列表
    public static final String Procurment_Return_Select_URL=SERVER_URL+ "Procurment_Return_Select";    
    //求购主单中的图片附件
    public static final String Procurment_Attachment_Select_URL=SERVER_URL+ "Procurment_Attachment_Select";    
    //删除求购表
    public static final String procurment_Info_Del_URL=SERVER_URL+ "Procurment_InfoMst_Del";
    //获取到汽车的品牌
    public static final String CarBrandSelectAll_URL = SERVER_URL+ "CarBrandSelectAll";
    //获取报修详情中的图片
    public static final String Repair_Image_Select_URL = SERVER_URL+ "Repair_Image_Select";    
    //提交意见反馈文本内容
    public static final String User_Advice_Update_URL = SERVER_URL+ "User_Advice_Update";
    //提交意见反馈的图片
    public static final String User_Advice_Image_Add_URL = SERVER_URL+ "User_Advice_Image_Add";
    // 支付
    public static final String Pay_URL= SERVER_URL+ "Pay";   
    //确认发货
    public static final String Delivery_URL= SERVER_URL+ "Delivery"; 
    //查看消息
    public static final String User_Msg_Select_URL= SERVER_URL+ "User_Msg_Select"; 
    // 修改消息读取状态
    public static final String User_Msg_Update_URL= SERVER_URL+ "User_Msg_Update"; 
    
    // 发布求购配件分类
    public static final String GetAllAccType= SERVER_URL + "GetAllAccType"; 
    
    
}
