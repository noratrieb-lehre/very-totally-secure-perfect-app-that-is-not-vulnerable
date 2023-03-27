package ch.bbw.m183.vulnerapp.controller;

import java.util.UUID;

import ch.bbw.m183.vulnerapp.datamodel.BlogEntity;
import ch.bbw.m183.vulnerapp.service.BlogService;
import ch.bbw.m183.vulnerapp.service.HealthService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/blog")
@RequiredArgsConstructor
public class BlogController {

	private final BlogService blogService;

	private final HealthService healthService;

	@GetMapping
	public Page<BlogEntity> getBlogs(@PageableDefault(sort = "createdAt", direction = Direction.DESC) Pageable pageable) {
		return blogService.getBlogs(pageable);
	}

	@PostMapping
	public UUID createBlog(@RequestBody BlogEntity blog) {
		return blogService.createBlog(blog);
	}

	@GetMapping("/health")
	public String health(@RequestHeader String host) {
		return healthService.health(host);
	}
}
