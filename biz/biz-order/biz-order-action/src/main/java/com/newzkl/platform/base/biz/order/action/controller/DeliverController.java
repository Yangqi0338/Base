package com.newzkl.platform.base.biz.order.action.controller;

import com.alibaba.excel.EasyExcel;
import com.newzkl.platform.base.biz.order.action.cmd.DeliverCmd;
import com.newzkl.platform.base.biz.order.application.service.OrderService;
import com.newzkl.platform.base.biz.order.domain.service.IOrderDomain;
import com.newzkl.platform.base.biz.order.model.dto.ExcelErrorVO;
import com.newzkl.platform.base.biz.order.model.req.DeliverCodeCommand;
import com.newzkl.platform.base.biz.order.model.req.DeliverCommand;
import com.newzkl.platform.base.biz.order.model.vo.DeliverVO;
import com.newzkl.platform.base.biz.order.model.vo.FullDeliverExcelVO;
import com.newzkl.platform.base.biz.order.model.vo.SplitDeliverExcelVO;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * 交易-发货
 * @author fang
 */
@RestController
@RequestMapping("/sale/deliver")
public class DeliverController {

    @Autowired
    private IOrderDomain orderDomain;
    @Autowired
    private OrderService orderService;

    /**
     * 发货创建
     * @param deliverCommand
     * @return
     */
    @PostMapping("deliverCreate")
    public PlatformResult<Void> deliverCreate(@Validated @RequestBody DeliverCommand deliverCommand) {
        orderService.deliverCreate(deliverCommand);
        return PlatformResult.success();
    }
    /**
     * SPU订单发货信息
     */
    @PostMapping("orderDeliverInfo")
    public PlatformResult<Map<Long, List<DeliverVO>>> orderDeliverInfo(@Validated @RequestBody DeliverCmd.OrderDeliverInfoReq deliverInfoReq) {
        return PlatformResult.success(orderDomain.orderDeliverInfo(deliverInfoReq.getSpuOrderId()));
    }
    /**
     * 修改物流单号
     */
    @PostMapping("deliverEdit")
    public PlatformResult<Void> deliverEdit(@Validated @RequestBody DeliverCodeCommand deliverCodeCommand) {
        orderDomain.deliverEdit(deliverCodeCommand);
        return PlatformResult.success();
    }
    /**
     * 整单发货
     * */
    @PostMapping(value = "/fullDeliver")
    public PlatformResult<ExcelErrorVO> fullDeliver(MultipartFile file){
        ExcelErrorVO errorVO = null;
        try {
            //获取文件的输入流
            InputStream inputStream = file.getInputStream();
            List<FullDeliverExcelVO> lst = EasyExcel.read(inputStream) //调用read方法
                //注册自定义监听器，字段校验可以在监听器内实现
                //.registerReadListener(new UserListener())
                .head(FullDeliverExcelVO.class) //对应导入的实体类
                .sheet(0) //导入数据的sheet页编号，0代表第一个sheet页，如果不填，则会导入所有sheet页的数据
                .headRowNumber(1) //列表头行数，1代表列表头有1行，第二行开始为数据行
                .doReadSync(); //开始读Excel，返回一个List<T>集合，继续后续入库操作
            //发货
            errorVO = orderService.fullDeliver(lst);
        }catch (IOException exception){
            throw new  RuntimeException(exception);
        }
        return PlatformResult.success(errorVO);
    }
    /**
     * 拆单发货
     * */
    @PostMapping(value = "/splitDeliver")
    public PlatformResult<ExcelErrorVO> splitDeliver(MultipartFile file){
        ExcelErrorVO errorVO;
        try {
            //获取文件的输入流
            InputStream inputStream = file.getInputStream();
            List<SplitDeliverExcelVO> lst = EasyExcel.read(inputStream) //调用read方法
                //注册自定义监听器，字段校验可以在监听器内实现
                //.registerReadListener(new UserListener())
                .head(SplitDeliverExcelVO.class) //对应导入的实体类
                .sheet(0) //导入数据的sheet页编号，0代表第一个sheet页，如果不填，则会导入所有sheet页的数据
                .headRowNumber(1) //列表头行数，1代表列表头有1行，第二行开始为数据行
                .doReadSync(); //开始读Excel，返回一个List<T>集合，继续后续入库操作
            //发货
            errorVO = orderService.splitDeliver(lst);
        }catch (IOException exception){
            throw new  RuntimeException(exception);
        }
        return PlatformResult.success(errorVO);
    }
}
