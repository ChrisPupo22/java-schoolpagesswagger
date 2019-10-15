package com.lambdaschool.school.controller;

import com.lambdaschool.school.model.Course;
import com.lambdaschool.school.service.CourseService;
import com.lambdaschool.school.view.CountStudentsInCourses;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;


    @ApiOperation(value = "Return all courses", response = Course.class, responseContainer = "List")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page."),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported.")})

    //http:localhost:2019/courses/courses
    //http:localhost:2019/courses/courses/paging/?page=1&size=3
    //http:localhost:2019/courses/courses/?sort=city&sort=name

    @GetMapping(value = "/courses/paging",
            produces = {"application/json"})
    public ResponseEntity<?> listAllCoursesByPage(
            @ApiParam(value = "listing courses by page")
            @PageableDefault(page = 0, size = 3) Pageable pageable) {

        List<Course> myPageableCourses = courseService.findAllPageable(pageable);
        return new ResponseEntity<>(myPageableCourses, HttpStatus.OK);
    }

    @GetMapping(value = "/courses", produces = {"application/json"})
    public ResponseEntity<?> listAllCourses(
            @ApiParam(value = "Listing all courses") Pageable pageable) {
        ArrayList<Course> myCourses = courseService.findAll();
        return new ResponseEntity<>(myCourses, HttpStatus.OK);
    }

    @GetMapping(value = "/studcount", produces = {"application/json"})
    public ResponseEntity<?> getCountStudentsInCourses(
            @ApiParam(value = "get total student count in course") Pageable pageable) {
        return new ResponseEntity<>(courseService.getCountStudentsInCourse(), HttpStatus.OK);
    }

    @DeleteMapping("/courses/{courseid}")
    public ResponseEntity<?> deleteCourseById(
            @ApiParam(value = "Delete course by ID", required = true, example = "2")
            @PathVariable long courseid) {
        courseService.delete(courseid);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
