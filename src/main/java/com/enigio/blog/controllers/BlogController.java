package com.enigio.blog.controllers;

import com.enigio.blog.config.CustomUserDetails;
import com.enigio.blog.entities.Comment;
import com.enigio.blog.entities.Post;
import com.enigio.blog.entities.User;
import com.enigio.blog.pojos.CommentPojo;
import com.enigio.blog.service.CommentService;
import com.enigio.blog.service.PostService;
import com.enigio.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
public class BlogController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @GetMapping(value="/posts")
    public List<Post> posts(){
        return postService.getAllPosts();
    }

    @GetMapping(value="/the_post/{id}")
    public Post getPostById(@PathVariable Long id){
        return postService.getPost(id);
    }

    @PostMapping(value="/post")//setirame nashiot creator kako loged in User.
    	//Se proveruva preku kontrolerot so instance od CustomUserDetails.
    public String publishPost(@RequestBody Post post){//mapped so @RequestBody
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(post.getDateCreated() == null)//ako ne naglasime vreme, ni go dava lokalnto vreme so postMan (kako timeStamp)
            post.setDateCreated(new Date());//ke treba ekstra da se implementira so vue.
        post.setCreator(userService.getUser(userDetails.getUsername()));
        //post.setCreator(userService.getUser("admin"));
        postService.insert(post);
        return "Post was published";
    }

    @GetMapping(value="/posts/{username}")
    public List<Post> postsByUser(@PathVariable String username){
        return postService.findByUser(userService.getUser(username));
    }

    @DeleteMapping(value = "/post/{id}")
    public boolean deletePost(@PathVariable Long id){
        return postService.deletePost(id);
    }

    @DeleteMapping(value = "/comment/{id}")
    public boolean deleteComment(@PathVariable Long id){
        return commentService.deletePost(id);
    }


    @GetMapping(value = "/comments/{postId}")
    public List<Comment> getComments(@PathVariable Long postId){
        return commentService.getComments(postId);
    }

    @PostMapping(value = "/post/postComment")
    public boolean postComment(@RequestBody CommentPojo comment){
        Post post = postService.find(comment.getPostId());
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        User creator = userService.getUser(userDetails.getUsername());
        if(post == null || creator == null)
            return false;

        commentService.comment(new Comment(comment.getText(),post,creator));
        return true;
    }
}
