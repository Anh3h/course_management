package com.j2ee.course_management.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import com.j2ee.course_management.exception.ForbiddenException;
import com.j2ee.course_management.model.Course;
import com.j2ee.course_management.model.Department;
import com.j2ee.course_management.model.User;
import com.j2ee.course_management.service.command.CourseCommand;
import com.j2ee.course_management.service.query.CourseQuery;
import com.j2ee.course_management.service.query.DepartmentQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CourseController {

	@Autowired
	private CourseQuery courseQuery;

	@Autowired
	private CourseCommand courseCommand;

	@Autowired
	private DepartmentQuery departmentQuery;

	@RequestMapping(
			value = "/courses/create",
			method = RequestMethod.POST
	)
	public ModelAndView createCourse(@ModelAttribute Course course, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		bindingResult = this.courseCommand.createCourse(course, bindingResult);
		if (bindingResult.hasErrors()){
			modelAndView.setViewName("courses/create");
		}
		modelAndView.setViewName("redirect:/courses/");
		return modelAndView;
	}

	@RequestMapping(
			value = "/courses/create",
			method = RequestMethod.GET
	)
	public ModelAndView createDepartmentForm(Authentication authentication) {
		ModelAndView modelAndView = new ModelAndView();
		Course course = new Course();
		List<Department> departments = this.departmentQuery.findAll();
		modelAndView.addObject("role", authentication.getAuthorities().iterator().next().getAuthority());
		modelAndView.addObject("course", course);
		modelAndView.setViewName("course/create");
		return modelAndView;
	}

	@RequestMapping(
			value = "/courses",
			method = RequestMethod.GET
	)
	public ModelAndView getCourses( Authentication authentication, @RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size) {
		ModelAndView modelAndView = new ModelAndView();
		Map<String, Integer> pageAttributes = PageValidator.validatePageAndSize(page, size);
		page = pageAttributes.get("page");
		size = pageAttributes.get("size");

		Page<Course> courses = this.courseQuery.findAll(page, size);
		modelAndView.addObject("role", authentication.getAuthorities().iterator().next().getAuthority());
		modelAndView.setViewName("course/list");
		modelAndView.addObject("courses", courses);
		return modelAndView;
	}

	@RequestMapping(
			value = "/courses/{courseId}",
			method = RequestMethod.GET
	)
	public ModelAndView getCourse( Authentication authentication, @PathVariable("courseId") Long courseId) {
		ModelAndView modelAndView = new ModelAndView();
		Course course = this.courseQuery.findById(courseId);
		List<User> lecturers = this.courseQuery.getCourseLecturers(courseId);
		/*Resource file = courseQuery.downloadCourseOutline(courseId);
		modelAndView.addObject("file", file);*/
		modelAndView.addObject("role", authentication.getAuthorities().iterator().next().getAuthority());
		modelAndView.addObject("course", course);
		modelAndView.addObject("lecturers", lecturers);
		modelAndView.setViewName("course/view");
		return modelAndView;
	}

	@RequestMapping(
			value = "/courses/edit/{courseId}",
			method = RequestMethod.GET
	)
	public ModelAndView updateCourseForm(Authentication authentication, @PathVariable("courseId") Long courseId) {
		ModelAndView modelAndView = new ModelAndView();
		Course course = this.courseQuery.findById(courseId);
		Page<Department> departments = this.departmentQuery.findAll(1, 20);
		List<User> users = this.courseQuery.getCourseLecturers(courseId);
		modelAndView.addObject("role", authentication.getAuthorities().iterator().next().getAuthority());
		modelAndView.addObject("course", course);
		modelAndView.setViewName("course/edit");
		return modelAndView;
	}

	@RequestMapping(
			value = "/courses/update/{courseId}",
			method = RequestMethod.POST
	)
	public ModelAndView updateCourse(@PathVariable("courseId") Long courseId, @ModelAttribute Course course,
			BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		bindingResult = this.courseCommand.updateCourse(course, courseId, bindingResult);
		if (bindingResult.hasErrors()){
			modelAndView.setViewName("courses/edit");
		}
		modelAndView.setViewName("redirect:/courses/" + courseId);
		return modelAndView;
	}

	@RequestMapping(
			value = "/courses/{courseId}/course-outline",
			method = RequestMethod.POST
	)
	public ModelAndView uploadCourseOutline(Authentication authentication, @PathVariable("courseId") Long courseId, @RequestParam("file") MultipartFile file) {
		ModelAndView modelAndView = new ModelAndView();
		if (!file.isEmpty() || !StringUtils.cleanPath(file.getOriginalFilename()).contains("..")) {
			this.courseCommand.uploadCourseOutline(file, courseId);
			modelAndView.setViewName("redirect:/courses/" + courseId);
			return modelAndView;
		}
//		bindingResult.rejectValue("outline", "error.outline", "Please upload the course outline");
		Course course = this.courseQuery.findById(courseId);
		modelAndView.addObject("role", authentication.getAuthorities().iterator().next().getAuthority());
		modelAndView.addObject("course", course);
		modelAndView.setViewName("course/course-outline");
		return modelAndView;
	}

	@RequestMapping(
			value = "/courses/{courseId}/course-outline",
			method = RequestMethod.GET
	)
	public ModelAndView uploadCourseOutline(Authentication authentication, @PathVariable("courseId") Long courseId) {
		ModelAndView modelAndView = new ModelAndView();
		Course course = this.courseQuery.findById(courseId);;
		modelAndView.addObject("role", authentication.getAuthorities().iterator().next().getAuthority());
		modelAndView.addObject("course", course);
		modelAndView.setViewName("course/course-outline");
		return modelAndView;
	}

	@RequestMapping(
			value = "/courses/{courseId}/get-course-outline",
			method = RequestMethod.GET
	)
	public void uploadCourseOutline(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("courseId") Long courseId) {
		Resource file = this.courseQuery.downloadCourseOutline(courseId);
		response.setContentType("application/pdf");
		response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+file);
		try {
			Files.copy(file.getFile().toPath(), response.getOutputStream());
			response.getOutputStream().flush();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(
			value = "/courses/delete/{courseId}",
			method = RequestMethod.DELETE
	)
	public ModelAndView deleteCourse(@PathVariable("courseId") Long courseId) {
		ModelAndView modelAndView = new ModelAndView();
		this.courseCommand.deleteCourse(courseId);
		modelAndView.setViewName("redirect:/courses");
		return modelAndView;
	}

}
