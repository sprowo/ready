package com.prowo.dynamic.webext.core.web.controller;

import com.prowo.persist.BaseAction;
import com.prowo.ydnamic.context.ComxContext;
import com.prowo.ydnamic.handler.HandlerUtil;
import com.prowo.ydnamic.handler.MapModel;
import com.prowo.ymlchain.yml.exception.YmlFileNotFoundException;
import com.prowo.ymlchain.yml.model.IHandlerContext;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Controller
@RequestMapping("system")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class AdminInterfaceController extends BaseAction {

    /**
     * 各种处理
     *
     * @param child
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "{child}.do", method = {RequestMethod.GET, RequestMethod.POST})
    public void doChain1(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
                         @PathVariable(value = "child") String child) throws Exception {
        String partnerid = "admin";

        try {
            IHandlerContext hContext = HandlerUtil.getHandlerContextInstance(httpRequest, httpResponse);
            ComxContext.getContext().getYmlChainDriver().startChain(partnerid + "/" + child + ".yml", hContext);
        } catch (YmlFileNotFoundException e) {
            MapModel model = MapModel.getModelInstance();
            model.setResult(false);
            model.setCode("m99");
            PrintWriter writer = httpResponse.getWriter();
            writer.print(model.toJson());
            writer.close();
        }
    }

    /**
     * 各种处理（为兼容之前服务端版本的适配器）
     *
     * @param child
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "{father}/{child}.do", method = {RequestMethod.GET, RequestMethod.POST})
    public void doChain2(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
                         @PathVariable(value = "father") String father, @PathVariable(value = "child") String child)
            throws Exception {
        child = father + "/" + child;
        String partnerid = "admin";

        try {
            IHandlerContext hContext = HandlerUtil.getHandlerContextInstance(httpRequest, httpResponse);
            ComxContext.getContext().getYmlChainDriver().startChain(partnerid + "/" + child + ".yml", hContext);
        } catch (YmlFileNotFoundException e) {
            PrintWriter writer = httpResponse.getWriter();
            MapModel model = MapModel.getModelInstance();
            model.setResult(false);
            model.setCode("m99");
            writer.print(model.toJson());
            writer.close();
        }
    }

    /**
     * 各种处理（为兼容之前服务端版本的适配器）
     *
     * @param child
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "{father}/{client}/{child}.do", method = {RequestMethod.GET, RequestMethod.POST})
    public void doChain3(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
                         @PathVariable(value = "father") String father, @PathVariable(value = "child") String child,
                         @PathVariable(value = "client") String client) throws Exception {
        child = father + "/" + child;
        String partnerid = "admin";

        try {
            IHandlerContext hContext = HandlerUtil.getHandlerContextInstance(httpRequest, httpResponse);
            hContext.setRequestParameter("client", client);
            ComxContext.getContext().getYmlChainDriver().startChain(partnerid + "/" + child + ".yml", hContext);
        } catch (YmlFileNotFoundException e) {
            MapModel model = MapModel.getModelInstance();
            PrintWriter writer = httpResponse.getWriter();
            model.setResult(false);
            model.setCode("m99");
            writer.print(model.toJson());
            writer.close();
        }
    }

    /**
     * 各种处理（为兼容之前服务端版本的适配器）
     *
     * @param child
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "{father}/{client}/{version}/{child}.do", method = {RequestMethod.GET, RequestMethod.POST})
    public void doChain4(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
                         @PathVariable(value = "father") String father, @PathVariable(value = "child") String child,
                         @PathVariable(value = "client") String client, @PathVariable(value = "version") String version)
            throws Exception {
        child = father + "/" + child;
        String partnerid = "admin";

        try {
            IHandlerContext hContext = HandlerUtil.getHandlerContextInstance(httpRequest, httpResponse);
            hContext.setRequestParameter("client", client);
            hContext.setRequestParameter("version", version);
            ComxContext.getContext().getYmlChainDriver().startChain(partnerid + "/" + child + ".yml", hContext);
        } catch (YmlFileNotFoundException e) {
            MapModel model = MapModel.getModelInstance();
            PrintWriter writer = httpResponse.getWriter();
            model.setResult(false);
            model.setCode("m99");
            writer.print(model.toJson());
            writer.close();
        }
    }

}
