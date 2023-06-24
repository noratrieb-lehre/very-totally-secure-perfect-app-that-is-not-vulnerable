package ch.bbw.m183.vulnerapp.service;

import ch.bbw.m183.vulnerapp.datamodel.BlogEntity;
import ch.bbw.m183.vulnerapp.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BlogService {

    private final BlogRepository blogRepository;

    public Page<BlogEntity> getBlogs(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }


    @PreAuthorize("hasAuthority('POSTER')")
    public UUID createBlog(BlogEntity blog) {
        blog.setId(UUID.randomUUID());
        // This can actually be null.
        if (blog.getCreatedAt() == null ) {
            // Dates and times, my seemingly eternal enemy. Getting dates and times correct is always way harder
            // than it should be. I wanted to make it so that all dates are UTC and then rendered nicely with the local
            // time in the frontend. A seemingly reasonable decision. But somehow, the API response always messed
            // up the dates. They were created nicely at the current UTC time here but then in the API response
            // I got my local time. Something is messing up the time (maybe the database?) and I've given up,
            // just using the local time here. But it hurts a lot. I'm sorry.
            LocalDateTime createdAt = LocalDateTime.now();
            blog.setCreatedAt(createdAt);
        }
        log.info("Created new blog post at {} with title `{}`", blog.getCreatedAt(), blog.getTitle());
        return blogRepository.save(blog).getId();
    }

    @EventListener(ContextRefreshedEvent.class)
    public void loadTestBlogs() {
        // sample XSS: <img src=a onerror='alert();'>
        Stream.of(
                new BlogEntity().setTitle("Understanding Cross-Site Request Forgery (CSRF)")
                        .setBody("""
                                Cross-Site Request Forgery (CSRF) is a type of cyber attack where a malicious actor
                                tricks a user into unknowingly performing an action on a web application. The attack takes
                                advantage of the trust relationship between the user and the web application. When a user
                                is logged in to a web application, the application may store cookies or other information
                                on the user's computer, which the attacker can use to make fraudulent requests on the
                                user's behalf. CSRF attacks can be prevented by implementing measures such as anti-CSRF
                                tokens.
                                """.stripIndent())
                        .setCreatedAt(LocalDateTime.of(2023, 1, 1, 1, 1)),
                new BlogEntity().setTitle("Understanding Cross-Site Scripting (XSS)")
                        .setBody("""
                                Cross-Site Scripting (XSS) is a type of cyber attack that targets web applications
                                (frontend) by injecting malicious code into web pages viewed by other users. In an XSS
                                attack, an attacker may use a variety of techniques to inject malicious code into a web
                                page, including input fields, URLs, or cookies. Once the code is executed by a user's
                                browser, the attacker can potentially steal sensitive information, hijack user sessions,
                                or deface websites. To prevent XSS attacks, web developers can implement measures such as
                                input validation, output encoding, and the use of Content Security Policy (CSP).
                                """.stripIndent())
                        .setCreatedAt(LocalDateTime.of(2023, 1, 1, 1, 1))
        ).forEach(this::createBlog);
    }
}
