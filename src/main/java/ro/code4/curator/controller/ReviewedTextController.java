package ro.code4.curator.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ro.code4.curator.entity.ReviewedTextManager;
import ro.code4.curator.transferObjects.ParsedTextTO;
import ro.code4.curator.transferObjects.ShallowReviewedTextTO;

import java.util.List;

@Controller
@RequestMapping("/input/reviewed")
public class ReviewedTextController {

    @Autowired
    private ReviewedTextManager IReviewedInputService;

    @ApiOperation(value = "createOrUpdate")
    @RequestMapping(method = RequestMethod.POST, path = "/{id}")
    public @ResponseBody
    ParsedTextTO submitReviewedInput(@PathVariable("id") int id,
                                     @RequestBody ParsedTextTO reviewedInput) {
        return IReviewedInputService.submitReview(id, reviewedInput);
    }

    @ApiOperation(value = "getById")
    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public @ResponseBody
    ShallowReviewedTextTO submitReviewedInput(@PathVariable("id") String id) {
        return IReviewedInputService.getReviewById(id);
    }

    @ApiOperation(value = "deleteById")
    @RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
    public @ResponseBody
    void deleteReviewedInput(@PathVariable("id") String id) {
        IReviewedInputService.deleteReviewById(id);
    }

    @ApiOperation(value = "getAll")
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    List<ShallowReviewedTextTO> getAll() {
        return IReviewedInputService.getAllReviewedTexts();
    }
}
