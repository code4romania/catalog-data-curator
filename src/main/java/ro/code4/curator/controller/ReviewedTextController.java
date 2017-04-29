package ro.code4.curator.controller;

import java.util.List;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import ro.code4.curator.entity.ReviewedInput;
import ro.code4.curator.service.ReviewedInputService;
import ro.code4.curator.transferObjects.ParsedInputTO;
import ro.code4.curator.transferObjects.ShallowReviewedInputTO;

@Controller
@RequestMapping("/input/reviewed")
public class ReviewedTextController {

    @Autowired
    private ReviewedInputService reviewedInputService;

    @ApiOperation(value = "createOrUpdate")
    @RequestMapping(method = RequestMethod.POST, path = "/{id}")
    public @ResponseBody
    ParsedInputTO submitReviewedInput(@PathVariable("id") String id,
                                      @RequestBody ParsedInputTO reviewedInput) {
        return reviewedInputService.submitReviewedInput(id, reviewedInput);
    }

    @ApiOperation(value = "getById")
    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public @ResponseBody
    ShallowReviewedInputTO submitReviewedInput(@PathVariable("id") String id) {
        return reviewedInputService.getById(id);
    }

    @ApiOperation(value = "deleteById")
    @RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
    public @ResponseBody
    void deleteReviewedInput(@PathVariable("id") String id) {
        reviewedInputService.deleteById(id);
    }

    @ApiOperation(value = "getAll")
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    List<ShallowReviewedInputTO> getAll() {
        return reviewedInputService.list();
    }
}
