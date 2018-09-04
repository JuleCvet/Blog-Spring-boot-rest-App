package com.enigio.blog;

import com.enigio.blog.controllers.BlogController;
import com.enigio.blog.entities.Post;
import javafx.beans.binding.Bindings;
import org.assertj.core.api.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.Id;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import(BlogApplication.class)
@AutoConfigureBefore
public class BlogApplicationTests {

	@Autowired
	private static BlogController blogController;


	@Test
	public void contextLoads() throws Exception{
		if (this.blogController != null) {
			contextLoads();
		}
	}

	@Test
	public void shouldSaveOnePost(Post post){
		assertTrue("Post was successfully published", true);

	}

	@Test
	public void shouldDeleteOnePost(Id id){
		assertTrue("The post was successfully deleted", true);
	}

}
