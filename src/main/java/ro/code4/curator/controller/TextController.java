package ro.code4.curator.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ro.code4.curator.entity.Text;
import ro.code4.curator.service.TextService;

import java.util.List;

@Controller
@RequestMapping("/text")
public class TextController {

    @Autowired
    private TextService textService;

    @ApiOperation(value = "createOrUpdate")
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    Text acceptParsedText(@RequestBody Text input) {
        return textService.persist(input);
    }

    @ApiOperation(value = "getAll")
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    List<Text> getAll() {
        return textService.findAll();
    }

    @ApiOperation(value = "getById")
    @RequestMapping(path = "/{type}/{id}", method = RequestMethod.GET)
    public @ResponseBody
    Text getById(@PathVariable String type, @PathVariable String id) {
        return textService.getByTextTypeAndTextSourceId(type, id);
    }

    @ApiOperation(value = "deleteById")
    @RequestMapping(path = "/{type}/{id}", method = RequestMethod.DELETE)
    public @ResponseBody
    void deleteById(@PathVariable String type, @PathVariable String id) {
        textService.deleteById(type, id);
    }

}
