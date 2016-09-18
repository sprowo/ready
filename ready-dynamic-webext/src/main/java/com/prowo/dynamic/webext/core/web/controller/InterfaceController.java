package com.prowo.dynamic.webext.core.web.controller;

import com.prowo.persist.BaseAction;
import com.prowo.ydnamic.context.ComxContext;
import com.prowo.ydnamic.encryption.Base64;
import com.prowo.ydnamic.handler.HandlerConstans;
import com.prowo.ydnamic.handler.HandlerUtil;
import com.prowo.ydnamic.handler.MapModel;
import com.prowo.ydnamic.validation.Validate;
import com.prowo.ymlchain.yml.exception.YmlFileNotFoundException;
import com.prowo.ymlchain.yml.model.IHandlerContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Controller
@RequestMapping(value = "service")
public class InterfaceController extends BaseAction {

    @RequestMapping(value = "interface.do", method = {RequestMethod.POST, RequestMethod.GET})
    public void doChain(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
        String request = httpRequest.getParameter("request");
        String partnerid = httpRequest.getParameter("partnerid");
        String data = httpRequest.getParameter("data");
        if (Validate.isNull(data)) {
            data = httpRequest.getParameter("xmldata");
        }

        MapModel model = MapModel.getModelInstance();

        String originalData = Base64.decode(data);

        try {
            IHandlerContext hContext = HandlerUtil.getHandlerContextInstance(httpRequest, httpResponse);
            hContext.setConverzationStr(HandlerConstans.REQUEST_DATA, originalData);
            hContext.setConverzationObj(HandlerConstans.RESPONSR_MODEL, model);
            ComxContext.getContext().getYmlChainDriver().startChain(partnerid + "/" + request + ".yml", hContext);
        } catch (YmlFileNotFoundException e) {
            PrintWriter writer = httpResponse.getWriter();
            model.setResult(false);
            model.setCode("m99");
            writer.print(model.toJson());
            writer.close();
        }
    }

}
