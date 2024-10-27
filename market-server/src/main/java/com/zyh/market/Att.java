package com.zyh.market;
public class  Att {
  public static final class ChatList {
    /**
   * 评论标识
   */
   public static final String id = "id";
    /**
   * 商品标识
   */
   public static final String productId = "product_id";
    /**
   * 商品图片
   */
   public static final String productImage = "product_image";
    /**
   * 评论者用户标识
   */
   public static final String fromUserId = "from_user_id";
    /**
   * 评论者用户头像
   */
   public static final String fromUserAvatar = "from_user_avatar";
    /**
   * 评论者用户昵称
   */
   public static final String fromUserNick = "from_user_nick";
    /**
   * 被评论者用户标识
   */
   public static final String toUserId = "to_user_id";
    /**
   * 被评论者用户昵称
   */
   public static final String toUserNick = "to_user_nick";
    /**
   * 被评论者用户头像
   */
   public static final String toUserAvatar = "to_user_avatar";
    /**
   * 创建时间
   */
   public static final String createTime = "create_time";
    /**
   * 更新时间
   */
   public static final String updateTime = "update_time";

  }
  public static final class ChatMessage {
    /**
   * 聊天信息标识
   */
   public static final String id = "id";
    /**
   * 聊天记录标识
   */
   public static final String chatListId = "chat_list_id";
    /**
   * 发起者标识
   */
   public static final String fromUserId = "from_user_id";
    /**
   * 接受者标识
   */
   public static final String toUserId = "to_user_id";
    /**
   * 发起者昵称
   */
   public static final String fromUserNick = "from_user_nick";
    /**
   * 接受者昵称
   */
   public static final String toUserNick = "to_user_nick";
    /**
   * 聊天内容
   */
   public static final String content = "content";
    /**
   * 发送时间
   */
   public static final String sendTime = "send_time";
    /**
   * 1已读0未读
   */
   public static final String isRead = "is_read";

  }
  public static final class Comment {
    /**
   * 评论标识
   */
   public static final String id = "id";
    /**
   * 商品标识
   */
   public static final String productId = "product_id";
    /**
   * 父级评论标识
   */
   public static final String parentId = "parent_id";
    /**
   * 发布者标识
   */
   public static final String pubUserId = "pub_user_id";
    /**
   * 发布者昵称
   */
   public static final String pubNickname = "pub_nickname";
    /**
   * 父级用户标识
   */
   public static final String parentUserId = "parent_user_id";
    /**
   * 父级用户昵称
   */
   public static final String parentUserNickname = "parent_user_nickname";
    /**
   * 评论内容
   */
   public static final String content = "content";
    /**
   * 创建时间
   */
   public static final String createTime = "create_time";

  }
  public static final class PaymentOrder {
    /**
   * 订单id
   */
   public static final String id = "id";
    /**
   * 订单编号
   */
   public static final String orderNumber = "order_number";
    /**
   * 订单所属的用户
   */
   public static final String userId = "user_id";
    /**
   * 总价
   */
   public static final String payPrice = "pay_price";
    /**
   * 支付类型
   */
   public static final String payTypeId = "pay_type_id";
    /**
   * 支付标题
   */
   public static final String payTypeName = "pay_type_name";
    /**
   * 状态，0待生成付款，1待付款，2已付款，3付款失败，8关闭订单，9完成
   */
   public static final String orderStatus = "order_status";
    /**
   * 支付id
   */
   public static final String paymentPayId = "payment_pay_id";
    /**
   * 0待付款，1付款中，2付款失败，9付款成功
   */
   public static final String paymentStatus = "payment_status";
    /**
   * 支付方式
   */
   public static final String paymentType = "payment_type";
    /**
   * 状态处理、0不处理、1未处理、9已处理
   */
    /**
   * 创建时间
   */
   public static final String timeCreate = "time_create";
    /**
   * 更新时间
   */
   public static final String timeUpdate = "time_update";
    /**
   * 结束时间
   */
   public static final String timeFinish = "time_finish";

  }
  public static final class PaymentPay {
    /**
   * 支付id
   */
   public static final String id = "id";
    /**
   * 用户id
   */
   public static final String userId = "user_id";
    /**
   * 订单UUID
   */
   public static final String orderId = "order_id";
    /**
   * 支付金额
   */
   public static final String paymentPrice = "payment_price";
    /**
   * 支付类型
   */
   public static final String paymentType = "payment_type";
    /**
   * 支付返回数据
   */
   public static final String paymentResultData = "payment_result_data";
    /**
   * 交易起始时间
   */
   public static final String paymentTimeStart = "payment_time_start";
    /**
   * 交易结束时间
   */
   public static final String paymentTimeExpire = "payment_time_expire";
    /**
   * 支付状态，0待支付，1支付中，2支付失败,8已退款，9支付成功
   */
   public static final String paymentStatus = "payment_status";
    /**
   * 支付状态处理、0不处理，1未处理、9已处理
   */
   public static final String processStatus = "process_status";
    /**
   * 创建时间
   */
   public static final String timeCreate = "time_create";
    /**
   * 最后一次更新时间
   */
   public static final String timeUpdate = "time_update";
    /**
   * 完成时间
   */
   public static final String timeFinish = "time_finish";

  }
  public static final class PaymentType {
    /**
   * 类型标识
   */
   public static final String id = "id";
    /**
   * 类型名
   */
   public static final String typeName = "type_name";
    /**
   * 1 可用 0 不可用
   */
   public static final String wxPay = "wx_pay";
    /**
   * 1 可用 0不可用
   */
   public static final String aliPay = "ali_pay";

  }
  public static final class ProductCollect {
    /**
   * 收藏标识
   */
   public static final String id = "id";
    /**
   * 用户标识
   */
   public static final String userId = "user_id";
    /**
   * 商品标识
   */
   public static final String productId = "product_id";
    /**
   * 创建时间
   */
   public static final String createTime = "create_time";
    /**
   * 更新时间
   */
   public static final String updateTime = "update_time";

  }
  public static final class ProductInfo {
    /**
   * 商品标识
   */
   public static final String id = "id";
    /**
   * 发布用户id
   */
   public static final String userId = "user_id";
    /**
   * 商品标题
   */
   public static final String title = "title";
    /**
   * 商品描述
   */
   public static final String intro = "intro";
    /**
   * 商品图片
   */
   public static final String image = "image";
    /**
   * 商品价格
   */
   public static final String price = "price";
    /**
   * 商品原价
   */
   public static final String originalPrice = "original_price";
    /**
   * 种类代码
   */
   public static final String typeCode = "type_code";
    /**
   * 种类名称
   */
   public static final String typeName = "type_name";
    /**
   * 发货方式 0邮寄 1自提
   */
   public static final String postType = "post_type";
    /**
   * 想要的人数
   */
   public static final String likeCount = "like_count";
    /**
   * 地址代码
   */
   public static final String adcode = "adcode";
    /**
   * 省
   */
   public static final String province = "province";
    /**
   * 市
   */
   public static final String city = "city";
    /**
   * 区
   */
   public static final String district = "district";
    /**
   * 状态 9 上线 12 卖出
   */
   public static final String status = "status";
    /**
   * 创建时间
   */
   public static final String createTime = "create_time";
    /**
   * 更新时间
   */
   public static final String updateTime = "update_time";

  }
  public static final class ProductOrder {
    /**
   * 订单标识
   */
   public static final String id = "id";
    /**
   * 订单编号
   */
   public static final String orderNumber = "order_number";
    /**
   * 商品发布用户标识
   */
   public static final String productUserId = "product_user_id";
    /**
   * 商品标识
   */
   public static final String productId = "product_id";
    /**
   * 买入 用户标识
   */
   public static final String userId = "user_id";
    /**
   * 商品标题
   */
   public static final String productTitle = "product_title";
    /**
   * 商品图片
   */
   public static final String productImg = "product_img";
    /**
   * 商品原价
   */
   public static final String productPrice = "product_price";
    /**
   * 商品分类
   */
   public static final String productType = "product_type";
    /**
   * 商品分类名称
   */
   public static final String productTypeName = "product_type_name";
    /**
   * 商品售价
   */
   public static final String productSellPrice = "product_sell_price";
    /**
   * 购买数量
   */
   public static final String productNum = "product_num";
    /**
   * 快递费用
   */
   public static final String productPost = "product_post";
    /**
   * 发货方式
   */
   public static final String productPostStatus = "product_post_status";
    /**
   * 购入商品价格
   */
   public static final String productMoney = "product_money";
    /**
   * 商品描述
   */
   public static final String productInfo = "product_info";
    /**
   * 应支付金额
   */
   public static final String buyMoneyAll = "buy_money_all";
    /**
   * 实际支付金额
   */
   public static final String buyMoney = "buy_money";
    /**
   * 客户备注
   */
   public static final String buyInfo = "buy_info";
    /**
   * 物流发货，学校领取，用户自提
   */
   public static final String postMode = "post_mode";
    /**
   * 自提码
   */
   public static final String postSelfCode = "post_self_code";
    /**
   * 收货人姓名
   */
   public static final String postUsername = "post_username";
    /**
   * 收货人联系方式
   */
   public static final String postPhone = "post_phone";
    /**
   * 收货地址
   */
   public static final String postAddress = "post_address";
    /**
   * 发货人姓名
   */
   public static final String shipUsername = "ship_username";
    /**
   * 发货人联系方式
   */
   public static final String shipPhone = "ship_phone";
    /**
   * 发货人地址
   */
   public static final String shipAddress = "ship_address";
    /**
   * 物流订单号码
   */
   public static final String shipNum = "ship_num";
    /**
   * 物流公司
   */
   public static final String shipCompany = "ship_company";
    /**
   * 发货时间
   */
   public static final String shipTime = "ship_time";
    /**
   * 0待支付，1支付中，9支付完成
   */
   public static final String payStatus = "pay_status";
    /**
   * 支付订单id
   */
   public static final String payOrderId = "pay_order_id";
    /**
   * 交易状态，0临时，1交易失败，2，付款中，3，待发货，4待确认，7退货中，9待评价 11评价完成
   */
   public static final String dealStatus = "deal_status";
    /**
   * 评价打分
   */
   public static final String evaScore = "eva_score";
    /**
   * 评价内容
   */
   public static final String evaContent = "eva_content";
    /**
   * 创建时间
   */
   public static final String createTime = "create_time";
    /**
   * 更新时间
   */
   public static final String updateTime = "update_time";

  }
  public static final class ProductType {
    /**
   * 类型标识
   */
   public static final String id = "id";
    /**
   * 类型代码
   */
   public static final String typeCode = "type_code";
    /**
   * 类型名称
   */
   public static final String typeName = "type_name";
    /**
   * 创建时间
   */
   public static final String createTime = "create_time";
    /**
   * 更新时间
   */
   public static final String updateTime = "update_time";

  }
  public static final class ProductVoucher {
   public static final String id = "id";
   public static final String productId = "product_id";
   public static final String title = "title";
    /**
   * 优惠金额
   */
   public static final String voucherValue = "voucher_value";
   public static final String stock = "stock";
   public static final String beginTime = "begin_time";
   public static final String endTime = "end_time";
    /**
   * 0不可见 9 可见
   */
   public static final String voucherStatus = "voucher_status";
    /**
   * 创建时间
   */
   public static final String createTime = "create_time";
    /**
   * 更新时间
   */
   public static final String updateTime = "update_time";

  }
  public static final class SystemRole {
    /**
   * 角色标识
   */
   public static final String id = "id";
    /**
   * 角色code
   */
   public static final String roleCode = "role_code";
    /**
   * 角色名
   */
   public static final String roleName = "role_name";
    /**
   * 创建时间
   */
   public static final String createTime = "create_time";
    /**
   * 更新时间
   */
   public static final String updateTime = "update_time";

  }
  public static final class SystemUser {
    /**
   * 管理用户标识
   */
   public static final String id = "id";
    /**
   * 用户名
   */
   public static final String username = "username";
    /**
   * 密码
   */
   public static final String password = "password";
    /**
   * 姓名
   */
   public static final String name = "name";
    /**
   * 角色标识
   */
   public static final String roleId = "role_id";
    /**
   * 角色代码
   */
   public static final String roleCode = "role_code";
    /**
   * 角色名称
   */
   public static final String roleName = "role_name";
    /**
   * 创建时间
   */
   public static final String createTime = "create_time";
    /**
   * 更新时间
   */
   public static final String updateTime = "update_time";

  }
  public static final class User {
    /**
   * 用户标识
   */
   public static final String id = "id";
    /**
   * 头像
   */
   public static final String avatar = "avatar";
    /**
   * 个人简介
   */
   public static final String intro = "intro";
    /**
   * 昵称
   */
   public static final String nickName = "nick_name";
    /**
   * 手机号
   */
   public static final String phone = "phone";
    /**
   * 密码
   */
   public static final String password = "password";
    /**
   * 状态
   */
   public static final String status = "status";
    /**
   * 创建时间
   */
   public static final String updateTime = "update_time";
    /**
   * 更新时间
   */
   public static final String createTime = "create_time";
    /**
   * 定位省
   */
   public static final String province = "province";
    /**
   * 定位市
   */
   public static final String city = "city";
    /**
   * 平台编号
   */
   public static final String number = "number";
    /**
   * 待审核昵称
   */
   public static final String checkNickName = "check_nick_name";
    /**
   * 待审核简介
   */
   public static final String checkIntro = "check_intro";
    /**
   * 待审核头像
   */
   public static final String checkAvatar = "check_avatar";
    /**
   * 审核状态 0待审核 7审核失败 9审核成功
   */
   public static final String checkStatus = "check_status";

  }
  public static final class UserAddress {
    /**
   * 地址标识
   */
   public static final String id = "id";
    /**
   * 用户标识
   */
   public static final String userId = "user_id";
    /**
   * 姓名
   */
   public static final String name = "name";
    /**
   * 手机号
   */
   public static final String phone = "phone";
    /**
   * 详细地址
   */
   public static final String address = "address";
    /**
   * 创建时间
   */
   public static final String createTime = "create_time";
    /**
   * 更新时间
   */
   public static final String updateTime = "update_time";

  }
  public static final class VoucherOrder {
   public static final String id = "id";
   public static final String userId = "user_id";
   public static final String productId = "product_id";
   public static final String voucherId = "voucher_id";
   public static final String voucherValue = "voucher_value";
    /**
   * 0已使用 9 未使用
   */
   public static final String status = "status";
    /**
   * 创建时间
   */
   public static final String createTime = "create_time";
    /**
   * 更新时间
   */
   public static final String updateTime = "update_time";

  }

}