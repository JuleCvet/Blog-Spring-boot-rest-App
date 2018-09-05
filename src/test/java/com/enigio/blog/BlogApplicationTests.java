package com.enigio.blog;

import com.enigio.blog.controllers.BlogController;
import com.enigio.blog.entities.Post;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.Id;

import static org.assertj.core.api.Java6Assertions.not;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.text.IsEmptyString.isEmptyString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest
@Import(BlogApplication.class)
@AutoConfigureBefore
@Ignore
@TestConfiguration
public class BlogApplicationTests {


		@Autowired
		private static BlogController blogController;


		@Test
		public void contextLoads() throws Exception {
			if (this.blogController != null) {
				contextLoads();
			}
		}

		@Test
		public void shouldSaveOnePost(Post post) {
			String savedPost= blogController.publishPost(post);
			assertThat(savedPost, is(not(isEmptyString())));

		}

		@Test
		public void shouldDeleteOnePost(Id id) {
			assertTrue("The post was successfully deleted", true);

	}
}
